package com.adapter;

import java.util.List;

import com.android.mask.R;
import com.bean.Resource;
import com.bean._User;
import com.myview.EmoticonsTextView;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CourseAdapter extends BaseContentAdapter<Resource> {

	public CourseAdapter(Context context, List<Resource> list) {
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
			convertView = mInflater.inflate(R.layout.item_list_maincourse, null);
			viewHolder.userNameText = (TextView) convertView
					.findViewById(R.id.course_user_name);
			viewHolder.timeText = (TextView) convertView
					.findViewById(R.id.course_time);
			viewHolder.userLogo = (ImageView) convertView
					.findViewById(R.id.course_user_logo);
			viewHolder.shareBtn = (ImageView) convertView
					.findViewById(R.id.course_item_action_fav);
			viewHolder.contentText = (EmoticonsTextView) convertView
					.findViewById(R.id.course_content_text);
			viewHolder.contentImage = (ImageView) convertView
					.findViewById(R.id.course_content_image);
			viewHolder.lookNumberText = (TextView) convertView
					.findViewById(R.id.course_item_action_love);
			viewHolder.commentNumberText = (TextView) convertView
					.findViewById(R.id.course_item_action_comment);
			viewHolder.contentText.setMaxLines(5);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// �õ���ǰ��Ƶ��Դ
		final Resource entity = dataList.get(position);
		// ������Ƶ����
		viewHolder.contentText.setText(entity.getResName());
		// �����������
		if(entity.getLookNumber() != null){
			viewHolder.lookNumberText.setText(entity.getLookNumber().toString());
		}else{
			viewHolder.lookNumberText.setText("0");
		}
		// ������������
		if(entity.getLookNumber() != null){
			viewHolder.commentNumberText.setText(entity.getCommentNumber().toString());
		}else{
			viewHolder.commentNumberText.setText("0");
		}
		
		// ���÷���ʱ��
		viewHolder.timeText.setText(entity.getUpdatedAt());
		// �ж��ǲ�������
		_User user = entity.getUser();
		if (user == null ) {
			viewHolder.userNameText.setText("����");
			// ���÷���ʱ��
			viewHolder.userLogo
					.setImageResource(R.drawable.user_icon_default_main);
			// ͷ�����õ���¼�
			viewHolder.userLogo.setOnClickListener(null);
		} else {
			if(user.getUserNick() == null ){
				viewHolder.userNameText.setText("��Ը������Ta");
			}else{
			// �����û���
			    viewHolder.userNameText.setText(user.getUserNick());
			}
			// �����û�ͷ��
			if (user.getHead() != null) {
				user.getHead().loadImage(mContext, viewHolder.userLogo);
			}
		
		}
		// ��������ͼƬ
		if (null == entity.getPicture()) {
			viewHolder.contentImage.setVisibility(View.GONE);
		} else {
			viewHolder.contentImage.setVisibility(View.VISIBLE);
			entity.getPicture().loadImage(mContext,
					viewHolder.contentImage);
		}
		// ����ť����¼�
		viewHolder.shareBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				String url = "";
				if (entity.getResUrl() != null) {
					url = entity.getResUrl();
				}
				intent.putExtra(Intent.EXTRA_TEXT, entity.getResName() + "\n"
						+ url);
				mContext.startActivity(Intent.createChooser(intent, "��������"));
			}
		});
		return convertView;
	}

	public static class ViewHolder {
		public ImageView userLogo; // ͷ��
		public TextView userNameText; // �û���
		public TextView timeText; // ʱ��
		public EmoticonsTextView contentText; // ����
		public ImageView contentImage; // ͼƬ
		public ImageView shareBtn; // �ղ�
		public TextView lookNumberText; // ����
		public TextView commentNumberText; // ����
	}

	/**
	 * ͷ���û����ĵ���¼�
	 */
	private class MyUserOnClick implements OnClickListener {
		_User user;

		public MyUserOnClick(_User user) {
			super();
			this.user = user;
		}

		@Override
		public void onClick(View arg0) {
			// Intent intent = new Intent();
			// intent.setClass(mContext, PersonalActivity.class);
			// intent.putExtra("user", user);
			// mContext.startActivity(intent);
		}
	}
}