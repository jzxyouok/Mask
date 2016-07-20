package com.android.topic;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

import com.adapter.CommentAdapter;
import com.android.mask.Constant;
import com.android.mask.R;
import com.android.settings.LoginActivity;
import com.bean.Comment;
import com.bean.Topic;
import com.bean._User;
import com.dao.CommentDao;
import com.dao.QiangYuDao;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.util.ToastFactory;

/**
 * չʾ�����б�,��Ҫһ������:
 * QiangYu - "data" Ҫ��ѯ������
 * @author dulang
 */
public class CommentActivity extends Activity implements OnClickListener {
	private Context mContext;
	private PullToRefreshListView refreshList; //pullListView
	private ListView commentList;	//�����б�
	private EditText commentContent;	//���ۿ�
	private Button commentCommit;	//�����ύ��ť
	private ViewGroup headGroup;
	private TextView userName;	//¥���û���
	private TextView timeText,contentText;//��������
	private ImageView contentImage,userLogo; //����ͼƬ,¥��ͷ��
	private TextView lookText,commentText;	//
	private ImageView shareBtn;	//�ղ�
	private Topic qiangYu;	//����
	private ImageView addBtn; //����߼����۵İ�ť
	private CommentAdapter mAdapter;
	private List<Comment> comments = new ArrayList<Comment>();
	private int pageNum = 0; //��ǰ���ص��ڼ�ҳ
	private boolean haveData=true,isRefresh=false;//�Ƿ�������
	private _User currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_comment);
		
		//�õ���������
		qiangYu = (Topic) getIntent().getSerializableExtra("data");
		//��ʼ�����
		initView();
		//���ͷ��
		commentList.addHeaderView(headGroup);
		//����������
		commentList.setAdapter(mAdapter);
		//����pullList���Լ��¼�
		setListInfo();
		//���ý������ˢ��,��������
		refreshList.setRefreshing();
		//���ø����������������1��
		QiangYuDao dao = new QiangYuDao();
		dao.incrementLook(mContext, qiangYu);
	}
	/**
	 * ��ʼ�����
	 */
	protected void initView() {
		mContext = this;
		currentUser = BmobUser.getCurrentUser(mContext, _User.class);
		//�õ����
		refreshList = (PullToRefreshListView) findViewById(R.id.comment_list);
		commentList = refreshList.getRefreshableView();
		commentContent = (EditText) findViewById(R.id.comment_content);
		commentCommit = (Button) findViewById(R.id.comment_commit);
		headGroup = (ViewGroup) getLayoutInflater().inflate(R.layout.item_list_content, null);
		userLogo = (ImageView) headGroup.findViewById(R.id.user_logo);
		userName = (TextView) headGroup.findViewById(R.id.user_name);
		timeText = (TextView) headGroup.findViewById(R.id.school);
		contentText = (TextView) headGroup.findViewById(R.id.content_text);
		contentImage = (ImageView) headGroup.findViewById(R.id.content_image);
		shareBtn = (ImageView) headGroup.findViewById(R.id.item_action_fav);
		lookText = (TextView) headGroup.findViewById(R.id.item_action_love);
		commentText = (TextView) headGroup.findViewById(R.id.item_action_comment);
		addBtn = (ImageView) findViewById(R.id.comment_add);
		//������������
		initMoodView(qiangYu);
		//Ϊ������ü����¼�
		commentCommit.setOnClickListener(this);
		shareBtn.setOnClickListener(this);
		addBtn.setOnClickListener(this);
		//Ϊ�б���������
		mAdapter = new CommentAdapter(CommentActivity.this, comments);
	}
	/**
	 * ������������
	 */
	private void initMoodView(Topic mood2) {
		if (mood2 == null) {
			return;
		}
		//�ж��ǲ�������
		if(mood2.isAnonymous()){
			userName.setText("����");
		}else{
			_User temUser = mood2.getAuthor();
			userName.setText(temUser.getUserNick());
			BmobFile avatar = temUser.getHead();
			if (null != avatar) {
				avatar.loadImage(mContext, userLogo);
			}
			userLogo.setOnClickListener(this);
		}
		timeText.setText(mood2.getCreatedAt());
		lookText.setText((mood2.getLookNumber()+1) + "");
		commentText.setText(mood2.getCommentNumber()+"");
		contentText.setText(mood2.getContent());
		if (null == mood2.getContentfigureurl()) {
			contentImage.setVisibility(View.GONE);
		} else {
			contentImage.setVisibility(View.VISIBLE);
			mood2.getContentfigureurl().loadImage(mContext, contentImage);
			LayoutParams params  = contentImage.getLayoutParams();
			params.height = LayoutParams.WRAP_CONTENT;
			contentImage.setLayoutParams(params);
//			//����ͼƬ�ĵ���¼�
//			contentImage.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View arg0) {
//					Intent intent = new Intent();
//					intent.setClass(mContext, LookImageActivity.class);
//					intent.putExtra("url", url);
//					mContext.startActivity(intent);
//				}
//			});
		}
	}
	/**
	 * ��������
	 */
	private void fetchComment() {
		if(!haveData){
			return;
		}
		BmobQuery<Comment> query = new BmobQuery<Comment>();
		query.addWhereRelatedTo("relation", new BmobPointer(qiangYu));
		query.include("user.rank,parent.user");
		query.order("createdAt");
		query.setLimit(Constant.NUMBERS_PER_PAGE);
		query.setSkip(Constant.NUMBERS_PER_PAGE*(pageNum++));
		query.findObjects(this, new FindListener<Comment>() {
			@Override
			public void onSuccess(List<Comment> data) {
				if(data.size()!=0&&data.get(data.size()-1)!=null){
					if(isRefresh){
						comments.clear();
					}
					comments.addAll(data);
					mAdapter.notifyDataSetChanged();
					//û�и���������
					if(data.size()<Constant.NUMBERS_PER_PAGE){
						haveData = false;
					}
				}else{
					ToastFactory.showToast(mContext, "��û������,�췢���һ����");
					haveData = false;
				}
				refreshList.onRefreshComplete();
			}
			@Override
			public void onError(int arg0, String arg1) {
				refreshList.onRefreshComplete();
				ToastFactory.showToast(mContext, "��������ʧ�ܣ�"+arg1);
			}
		});
	}
	/**
	 * �����б����Լ��¼�����
	 */
	private void setListInfo(){
		refreshList.setMode(Mode.BOTH);
		refreshList.getLoadingLayoutProxy(true, false).setPullLabel("����ˢ��");
		refreshList.getLoadingLayoutProxy(true, false).setReleaseLabel("�ɿ�ˢ��");
		refreshList.getLoadingLayoutProxy(true, false).setRefreshingLabel("����ˢ��");
		refreshList.getLoadingLayoutProxy(false, true).setPullLabel("�������ظ���");
		refreshList.getLoadingLayoutProxy(false, true).setReleaseLabel("�ɿ����ظ���");
		refreshList.getLoadingLayoutProxy(false, true).setRefreshingLabel("���ڼ���");
		refreshList.getLoadingLayoutProxy(true, true).setLoadingDrawable(getResources().getDrawable(R.drawable.refresh));
		//����ˢ��
		refreshList.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				pageNum = 0;
				haveData = true;
				isRefresh = true;
				fetchComment();
			}
			//�������ظ���
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				if(haveData){
					isRefresh = false;
					fetchComment();
				}else{
					refreshList.postDelayed(new Runnable() {
			            @Override
			            public void run() {
			            	ToastFactory.showToast(mContext, "�Ѽ�������������");
			            	refreshList.onRefreshComplete();
			            }
			        }, 500);
				}
			}
		});
		//�б����¼�
		commentList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if(position==0){
					return;
				}
				if(position==1){
					showDialog(null,new String[]{"��������"});
					return;
				}
				currentUser = BmobUser.getCurrentUser(mContext,_User.class);
				if(currentUser== null)
				{
				Toast.makeText(getApplicationContext(), "���ȵ�¼",
			    Toast.LENGTH_SHORT).show();
				}
				else
				{
				String userId = comments.get(position - 2).getUser().getObjectId();
				if(userId.equals(currentUser.getObjectId())){
					showDialog(comments.get(position - 2),new String[]{"��������","�ظ�","ɾ������"});
				}else{
					showDialog(comments.get(position - 2),new String[]{"��������","�ظ�"});
				}		
				}	
			}
		});
	}
	/**
	 * �û�ͷ�����¼�
	 */
	private void onClickUserLogo() {
//		Intent intent = new Intent();
//		intent.setClass(mContext, PersonalActivity.class);
//		intent.putExtra("user", qiangYu.getAuthor());
//		mContext.startActivity(intent);
	}
	/**
	 * ���۰�ť����¼�
	 */
	private void onClickCommit() {
		if (TextUtils.isEmpty(commentContent.getText())) {
			ToastFactory.showToast(mContext, "˵��ʲô��");
			return;
		}
		CommentDao dao = new CommentDao();
		dao.publishComment(mContext,commentContent,currentUser,qiangYu,mAdapter);
	}
	/**
	 * ��ʾ�б����ĶԻ���
	 */
	private void showDialog(final Comment comment,final String[] stringItems){
        final ActionSheetDialog menuDialog = new ActionSheetDialog(mContext, stringItems, null);
        menuDialog.setOnOperItemClickL(new OnOperItemClickL() {
			@Override
			public void onOperItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				switch (position) {
				case 0:
					if(comment==null){
//						StringUtils.setClip(mContext, qiangYu.getContent());
					}else{
//						StringUtils.setClip(mContext, comment.getCommentContent());
					}
					break;
				case 1:
					if(stringItems.length>2){
						ToastFactory.showToast(mContext, "���ܻظ��Լ�");
					}else{
						//��ת���߼��ظ�����
						Intent intent = new Intent();
						intent.setClass(mContext, PublishCommentActivity.class);
						intent.putExtra("qiangYu", qiangYu);
						intent.putExtra("parentComment", comment);
						mContext.startActivity(intent);
					}
					break;
				case 2:
					CommentDao dao = new CommentDao();
					dao.deleteComment(mContext, comment, qiangYu, mAdapter);
					break;
				default:
					break;
				}
				menuDialog.dismiss();
			}
        });
        menuDialog.isTitleShow(false).show();
	}
	/**
	 * ��������ĵ���¼�
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_logo:
			onClickUserLogo(); break;
		case R.id.comment_commit:
			if(BmobUser.getCurrentUser(mContext)==null){
				Intent intent = new Intent();
				intent.setClass(mContext, LoginActivity.class);
				mContext.startActivity(intent);
			}else{
				onClickCommit();
			}
			 break;
		case R.id.comment_add:
			haveData = true;
			//��ת���߼��ظ�����
			Intent intent = new Intent();
			if(BmobUser.getCurrentUser(mContext)==null){
				intent.setClass(mContext, LoginActivity.class);
			}else{
				intent.setClass(mContext, PublishCommentActivity.class);
				intent.putExtra("qiangYu", qiangYu);
			}
			mContext.startActivity(intent);
			break;
		default: break;
		}
	}
	/**
	�˵�����¼�
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
	   		    break;
		}
		return super.onOptionsItemSelected(item);
	}
}
