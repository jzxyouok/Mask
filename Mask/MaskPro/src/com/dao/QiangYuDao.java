package com.dao;

import com.adapter.AIContentAdapter;
import com.myview.MyProgressDialog;
import com.bean.Resource;
import com.bean.Topic;
import com.util.ToastFactory;

import android.content.Context;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;


public class QiangYuDao {
	/**
	 * ɾ������
	 */
	public void deleteQiangYu(final Context mContext, final Topic qiangYu,
			final AIContentAdapter mAdapter) {
		final MyProgressDialog pDialog = new MyProgressDialog(mContext);
		pDialog.setMsg("����ɾ��");
		pDialog.show();
		//ɾ�������е�ͼƬ
		BmobFile oldFile = qiangYu.getContentfigureurl();
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
		//ɾ����������
		qiangYu.delete(mContext, new DeleteListener() {
			@Override
			public void onSuccess() {
				if(mAdapter!=null){
					mAdapter.getDataList().remove(qiangYu);
					mAdapter.notifyDataSetChanged();
				}
				pDialog.dismiss();
				ToastFactory.showToast(mContext, "ɾ���ɹ�");
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				pDialog.dismiss();
				ToastFactory.showToast(mContext, "ɾ��ʧ��");
			}
		});
	}
	
	/**
	 * ���������������һ��
	 */
	public void incrementLook(Context mContext,Topic qiangYu){
		qiangYu.increment("lookNumber");
		qiangYu.update(mContext);
	}
	
	//���������������һ��
	public void incrementLook(Context mContext,Resource aCourse){
		aCourse.increment("lookNumber");
		aCourse.update(mContext);
	}
}
