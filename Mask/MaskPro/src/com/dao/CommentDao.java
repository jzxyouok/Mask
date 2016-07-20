package com.dao;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.EditText;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.myview.MyProgressDialog;
import com.adapter.CommentAdapter;
import com.android.mask.Constant;
import com.bean.Comment;
import com.bean.Resource;
import com.bean.Topic;
import com.bean._User;
import com.util.ToastFactory;

public class CommentDao {
	/**
	 * ɾ������
	 */
	public void deleteComment(final Context mContext, final Comment comment, final Topic aTopic, final CommentAdapter mAdapter) {
		final MyProgressDialog pDialog = new MyProgressDialog(mContext);
		pDialog.show();
		//ɾ�������е�ͼƬ
		BmobFile oldFile = comment.getContentfigureurl();
		if (oldFile != null) {
			oldFile.delete(mContext, new DeleteListener() {
				@Override
				public void onSuccess() {
				}

				@Override
				public void onFailure(int arg0, String arg1) {
				}
			});
		}
		//ɾ������
		comment.delete(mContext, new DeleteListener() {
			@Override
			public void onSuccess() {
				mAdapter.getDataList().remove(comment);
				mAdapter.notifyDataSetChanged();
				pDialog.dismiss();
				aTopic.setCommentNumber(aTopic.getCommentNumber()-1);
				aTopic.update(mContext, new UpdateListener() {
					@Override
					public void onSuccess() {
						pDialog.dismiss();
						ToastFactory.showToast(mContext, "ɾ���ɹ�");
					}
					@Override
					public void onFailure(int arg0, String arg1) {
						pDialog.dismiss();
						ToastFactory.showToast(mContext, "�쳣���룺x001" + arg1);
					}
				});
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				pDialog.dismiss();
				ToastFactory.showToast(mContext, "ɾ��ʧ��");
			}
		});
	}


	
	public void deleteComment(final Context mContext, final Comment comment, final Resource aCourse, final CommentAdapter mAdapter) {
		final MyProgressDialog pDialog = new MyProgressDialog(mContext);
		pDialog.show();
		//ɾ�������е�ͼƬ
		BmobFile oldFile = comment.getContentfigureurl();
		if (oldFile != null) {
			oldFile.delete(mContext, new DeleteListener() {
				@Override
				public void onSuccess() {
				}

				@Override
				public void onFailure(int arg0, String arg1) {
				}
			});
		}
		//ɾ������
		comment.delete(mContext, new DeleteListener() {
			@Override
			public void onSuccess() {
				mAdapter.getDataList().remove(comment);
				mAdapter.notifyDataSetChanged();
				pDialog.dismiss();
				aCourse.setCommentNumber(aCourse.getCommentNumber()-1);
				aCourse.update(mContext, new UpdateListener() {
					@Override
					public void onSuccess() {
						pDialog.dismiss();
						ToastFactory.showToast(mContext, "ɾ���ɹ�");
					}
					@Override
					public void onFailure(int arg0, String arg1) {
						pDialog.dismiss();
						ToastFactory.showToast(mContext, "�쳣���룺x001" + arg1);
					}
				});
							
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				pDialog.dismiss();
				ToastFactory.showToast(mContext, "ɾ��ʧ��");
			}
		});
	}


	/**
	 * �������۵ķ���
	 * ��ͨ����,��ͼƬ,�޸�����,������,���ǿ��Ը��½���
	 */
	public void publishComment(final Context mContext,
			final EditText commentContent, final _User user,final Topic qiangYu,
			final CommentAdapter mAdapter) {
		final MyProgressDialog pDialog = new MyProgressDialog(mContext);
		pDialog.show();
		final Comment comment = new Comment();
		comment.setUser(user);
		comment.setCommentContent(commentContent.getText().toString());
		if(!qiangYu.getAuthor().getObjectId().equals(user.getObjectId())){
			comment.setToUser(qiangYu.getAuthor().getObjectId());
		}
		comment.setToNote(qiangYu);
		comment.setRead(false);
		comment.save(mContext, new SaveListener() {
			@Override
			public void onSuccess() {
				if (mAdapter.getDataList().size() < Constant.NUMBERS_PER_PAGE
						||mAdapter.getDataList().size()/Constant.NUMBERS_PER_PAGE!=0) {
					mAdapter.getDataList().add(comment);
					mAdapter.notifyDataSetChanged();
				}
				commentContent.setText("");
				//����������Ϣ�������۵��û�
//				UserDao dao = new UserDao(mContext);
//				dao.pushMsg(qiangYu.getAuthor().getInstallId(), user.getNick()+" �������������");
				// �������������Ӱ󶨵�һ��
				BmobRelation relation = new BmobRelation();
				relation.add(comment);
				qiangYu.setCommentNumber(qiangYu.getCommentNumber()+1);
				qiangYu.setRelation(relation);
				qiangYu.update(mContext, new UpdateListener() {
					@Override
					public void onSuccess() {
						pDialog.dismiss();
						ToastFactory.showToast(mContext, "���۳ɹ�");
					}
					@Override
					public void onFailure(int arg0, String arg1) {
						pDialog.dismiss();
						ToastFactory.showToast(mContext, "����ʧ�ܣ�" + arg1);
					}
				});
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				pDialog.dismiss();
				ToastFactory.showToast(mContext, "����ʧ�ܣ�" + arg1);
			}
		});
	}
	
	// �������۵ķ���
	// ��ͨ����,��ͼƬ,�޸�����,������,���ǿ��Ը��½���
	public void publishComment(final Context mContext,
			final EditText commentContent, final _User user,final Resource qiangYu,
			final CommentAdapter mAdapter) {
		final MyProgressDialog pDialog = new MyProgressDialog(mContext);
		pDialog.show();
		final Comment comment = new Comment();
		comment.setUser(user);
		comment.setCommentContent(commentContent.getText().toString());
		if(!qiangYu.getUser().getObjectId().equals(user.getObjectId())){
			comment.setToUser(qiangYu.getUser().getObjectId());
		}
		comment.setToNoteResource(qiangYu);
		comment.setRead(false);
		comment.save(mContext, new SaveListener() {
			@Override
			public void onSuccess() {
				if (mAdapter.getDataList().size() < Constant.NUMBERS_PER_PAGE
						||mAdapter.getDataList().size()/Constant.NUMBERS_PER_PAGE!=0) {
					mAdapter.getDataList().add(comment);
					mAdapter.notifyDataSetChanged();
				}
				commentContent.setText("");
				//����������Ϣ�������۵��û�
//				UserDao dao = new UserDao(mContext);
//				dao.pushMsg(qiangYu.getAuthor().getInstallId(), user.getNick()+" �������������");
				// �������������Ӱ󶨵�һ��
				BmobRelation relation = new BmobRelation();
				relation.add(comment);
				qiangYu.setCommentNumber(qiangYu.getCommentNumber()+1);
				qiangYu.setComments(relation);
				qiangYu.update(mContext, new UpdateListener() {
					@Override
					public void onSuccess() {
						pDialog.dismiss();
						ToastFactory.showToast(mContext, "���۳ɹ�");
					}
					@Override
					public void onFailure(int arg0, String arg1) {
						pDialog.dismiss();
						ToastFactory.showToast(mContext, "����ʧ�ܣ�" + arg1);
					}
				});
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				pDialog.dismiss();
				ToastFactory.showToast(mContext, "����ʧ�ܣ�" + arg1);
			}
		});
	}
	/**
	 * ��������������ϢΪ�Ѷ�
	 */
	public void setCommentRead(Context context,ArrayList<Comment> comments){
		ArrayList<BmobObject> tComments = new ArrayList<BmobObject>();
		for(Comment temp : comments){
			Comment t = new Comment();
			t.setRead(true);
			t.setObjectId(temp.getObjectId());
			tComments.add(t);
		}
		new BmobObject().updateBatch(context, tComments, new UpdateListener() {
			@Override
			public void onSuccess() {
//				LogUtils.i("Log", "����������Ϣ״̬Ϊ�Ѷ��ɹ�");
			}
			@Override
			public void onFailure(int arg0, String arg1) {
//				LogUtils.i("Log", "����������Ϣ״̬Ϊ�Ѷ�ʧ��"+arg1);
			}
		});
	}
	/**
	 * ��ѯ�ж���δ��������
	 */
	public void queryNotReadComment(final Context context){
		_User user = BmobUser.getCurrentUser(context,_User.class);
		BmobQuery<Comment> query = new BmobQuery<Comment>();
		query.addWhereContains("toUser", user.getObjectId());
		query.addWhereEqualTo("isRead", false);
		query.count(context, Comment.class, new CountListener() {
			@Override
			public void onSuccess(int count) {
				if(count>0){
					String msg = "��"+count+"������������";
//					BmobNotifyManager.getInstance(context).showNotify(true, true, R.drawable.ic_launcher, msg, "����δ����Ϣ", msg, NewsCommentActivity.class);
				}
			}
			@Override
			public void onFailure(int arg0, String arg1) {
//				LogUtils.i("Log", "��ѯδ�����۸�������"+arg1);
			}
		});
	}
}
