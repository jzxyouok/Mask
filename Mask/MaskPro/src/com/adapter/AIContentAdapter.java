package com.adapter;

import java.util.List;

import com.android.mask.R;
import com.bean.Topic;
import com.bean._User;
import com.myview.EmoticonsTextView;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AIContentAdapter extends BaseContentAdapter<Topic> {

	public AIContentAdapter(Context context, List<Topic> list) {
		super(context, list);
	}
	/**
	 * ʵ�ָ����еõ�ÿһ��ֵĳ��󷽷�
	 */
	@Override
	public View getConvertView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_list_content, null);
			viewHolder.userNameText = (TextView) convertView.findViewById(R.id.user_name);
			viewHolder.timeText = (TextView) convertView.findViewById(R.id.school);
			viewHolder.userLogo = (ImageView) convertView.findViewById(R.id.user_logo);
			viewHolder.shareBtn = (ImageView) convertView.findViewById(R.id.item_action_fav);
			viewHolder.contentText = (EmoticonsTextView) convertView.findViewById(R.id.content_text);
			viewHolder.contentImage = (ImageView) convertView.findViewById(R.id.content_image);
			viewHolder.lookNumberText = (TextView) convertView.findViewById(R.id.item_action_love);
			viewHolder.commentNumberText = (TextView) convertView.findViewById(R.id.item_action_comment);
			viewHolder.contentText.setMaxLines(5);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		//�õ���ǰ����
		final Topic entity = dataList.get(position);
		//������������
		viewHolder.contentText.setText(entity.getContent());
		//�����������
		viewHolder.lookNumberText.setText(entity.getLookNumber() + "");
		//������������
		viewHolder.commentNumberText.setText(entity.getCommentNumber()+"");
		//���÷���ʱ��
		viewHolder.timeText.setText(entity.getUpdatedAt());
		//�ж��ǲ�������
		_User user = entity.getAuthor();
		if(entity.isAnonymous()){
			viewHolder.userNameText.setText("����");
			//���÷���ʱ��
			viewHolder.userLogo.setImageResource(R.drawable.user_icon_default_main);
			//ͷ�����õ���¼�
			viewHolder.userLogo.setOnClickListener(null);
		}else{
			//�����û���
			viewHolder.userNameText.setText(user.getUserNick());
			//�����û�ͷ��
			if (user.getHead() != null) {
				user.getHead().loadImage(mContext, viewHolder.userLogo);
			}
			//ͷ�����õ���¼�
			viewHolder.userLogo.setOnClickListener(new MyUserOnClick(user));
		}
		//��������ͼƬ
		if (null == entity.getContentfigureurl()) {
			viewHolder.contentImage.setVisibility(View.GONE);
		} else {
			viewHolder.contentImage.setVisibility(View.VISIBLE);
			entity.getContentfigureurl().loadImage(mContext, viewHolder.contentImage);
		}
		//����ť����¼�
		viewHolder.shareBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				String url = "";
				if(entity.getContentfigureurl()!=null){
					url = entity.getContentfigureurl().getFileUrl(mContext);
				}
				intent.putExtra(Intent.EXTRA_TEXT,entity.getContent()+"\n"+url);
				mContext.startActivity(Intent.createChooser(intent, "��������"));
			}
		});
		return convertView;
	}

	public static class ViewHolder {
		public ImageView userLogo;	//ͷ��
		public TextView userNameText;	//�û���
		public TextView timeText;	//ʱ��
		public EmoticonsTextView contentText;	//����
		public ImageView contentImage;	//ͼƬ
		public ImageView shareBtn;	//�ղ�
		public TextView lookNumberText;	//����
		public TextView commentNumberText;	//����
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