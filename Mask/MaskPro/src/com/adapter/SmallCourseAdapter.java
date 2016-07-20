package com.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.adapter.CourseAdapter.ViewHolder;
import com.android.mask.R;
import com.bean.Resource;

public class SmallCourseAdapter extends BaseContentAdapter<Resource>{

	public SmallCourseAdapter(Context context, List<Resource> list) {
		super(context, list);
		mContext = context;
		dataList = list;
	}
	/**
	 * ʵ�ָ����еõ�ÿһ��ֵĳ��󷽷�
	 */
	@Override
	public View getConvertView(int position, View view, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = mInflater.inflate(R.layout.item_list_resource, null);
			viewHolder.res_img = (ImageView) view.findViewById(R.id.resource_img);
			viewHolder.res_name = (TextView) view.findViewById(R.id.resource_name);
			viewHolder.res_data = (TextView) view.findViewById(R.id.resource_data);
			viewHolder.res_share = (ImageView) view.findViewById(R.id.resource_share);
			viewHolder.look_num = (TextView) view.findViewById(R.id.look_num_show);
			viewHolder.comment_num = (TextView) view.findViewById(R.id.comment_num_show);
			
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		// �õ���ǰ��Ƶ��Դ
		final Resource entity = dataList.get(position);
		//������ƵͼƬ��ʾ
		
		// ������Ƶ����
		viewHolder.res_name.setText(entity.getResName());
		// �����������
		if(entity.getLookNumber() != null){
			viewHolder.look_num.setText(entity.getLookNumber() + "");
		}else{
			viewHolder.look_num.setText(0 + "");
		}
		// ������������
		if (entity.getCommentNumber() != null) {
			viewHolder.comment_num.setText(entity.getCommentNumber() + ""); 
		} else {
			viewHolder.comment_num.setText(0+"");
		}
		
		// ���÷���ʱ��
		viewHolder.res_data.setText(entity.getUpdatedAt());
		//��ʾ��ƵͼƬ
		if (entity.getPicture() != null) {
			entity.getPicture().loadImage(mContext, viewHolder.res_img);
		}
		// ����ť����¼�
				viewHolder.res_share.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Intent.ACTION_SEND);
						intent.setType("text/plain");
						String url = "";
						if (entity.getResUrl() != null ) {
							url = entity.getResUrl();
						}
						intent.putExtra(Intent.EXTRA_TEXT, entity.getResName() + "\n"
								+ url);
						mContext.startActivity(Intent.createChooser(intent, "��������"));
					}
				});
		return view;
	}
	
	public static class ViewHolder {
		public ImageView res_img;//��ԴͼƬ
		public TextView res_name;//��Դ����
		public TextView res_data;//�ϴ���ʱ��
		public ImageView res_share;//����İ�ť
		public TextView comment_num;//������
		public TextView look_num;//�����
	}

}


