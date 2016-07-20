package com.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mask.R;
import com.bean.Comment;
import com.bean._User;
import com.myview.EmoticonsTextView;

public class CommentAdapter extends BaseContentAdapter<Comment>{

	public CommentAdapter(Context context, List<Comment> list) {
		super(context, list);
	}

	@Override
	public View getConvertView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_list_comment, null);
			viewHolder.userName = (TextView)convertView.findViewById(R.id.userName_comment);
			viewHolder.commentContent = (EmoticonsTextView)convertView.findViewById(R.id.content_comment);
			viewHolder.index = (TextView)convertView.findViewById(R.id.index_comment);
			viewHolder.time = (TextView)convertView.findViewById(R.id.content_list_time);
			viewHolder.user_icon = (ImageView) convertView.findViewById(R.id.user_logo);
			viewHolder.commentParent = (EmoticonsTextView) convertView.findViewById(R.id.comment_parent);
			viewHolder.commentImage = (ImageView) convertView.findViewById(R.id.comment_image);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		final Comment comment = dataList.get(position);
		viewHolder.time.setText(comment.getUpdatedAt());
		viewHolder.index.setText((position+1)+"¥");
		viewHolder.commentContent.setText(comment.getCommentContent());
		//�жϸ������ǲ�������
		_User user = comment.getUser();
		if(comment.isAnonymous()){
			viewHolder.userName.setText("����");
			viewHolder.rank.setVisibility(View.GONE);
			viewHolder.user_icon.setImageResource(R.drawable.user_icon_default_main);
			//ͷ�����õ���¼�
			viewHolder.user_icon.setOnClickListener(null);
		}else{
			viewHolder.userName.setText(user.getUserNick());
			//�õ���ǰ���ӵ��û�ͷ��
			String avatarUrl = null;
			if (comment.getUser().getHead() != null) {
				comment.getUser().getHead().loadImage(mContext, viewHolder.user_icon);
			}
			//ͷ�����õ���¼�
			viewHolder.user_icon.setOnClickListener(new MyUserOnClick(comment.getUser()));
		}
		//���ø�����
		if(comment.getParent()!=null&&comment.getParent().getUser()!=null){
			viewHolder.commentParent.setVisibility(View.VISIBLE);
			Comment parentComment = comment.getParent();
			String pContent; //�жϸ������ǲ�������
			if(parentComment.isAnonymous()){
				pContent = "�ظ�   ����\n";
			}else{
				pContent = "�ظ�   "+parentComment.getUser().getUserNick()+"\n";
			}
			viewHolder.commentParent.setText(pContent+parentComment.getCommentContent());
		}else{
			viewHolder.commentParent.setVisibility(View.GONE);
		}
		//��������ͼƬ
		if(comment.getContentfigureurl()!=null){
			viewHolder.commentImage.setVisibility(View.VISIBLE);
			comment.getContentfigureurl().loadImage(mContext, viewHolder.commentImage);
			//����ͼƬ�ĵ���¼�
			viewHolder.commentImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
//					Intent intent = new Intent();
//					intent.setClass(mContext, LookImageActivity.class);
//					intent.putExtra("url", url);
//					mContext.startActivity(intent);
				}
			});
		}else{
			viewHolder.commentImage.setVisibility(View.GONE);
		}
		return convertView;
	}

	public static class ViewHolder{
		public TextView userName;
		public EmoticonsTextView commentContent,commentParent;
		public TextView index,time,rank;
		public ImageView user_icon,commentImage;
	}
	/**
	 * ͷ���û����ĵ���¼�
	 */
	private class MyUserOnClick implements OnClickListener{
		_User user;
		public MyUserOnClick(_User user) {
			super();
			this.user = user;
		}
		@Override
		public void onClick(View arg0) {
//			Intent intent = new Intent();
//			intent.setClass(mContext, PersonalActivity.class);
//			intent.putExtra("user", user);
//			mContext.startActivity(intent);
		}
	}
}
