package com.bean;

import android.content.Context;
import android.util.Log;

import com.util.ToastFactory;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.DeleteListener;

public class Collect extends BmobObject{

	private Resource resourceId;//���ղص���Դ
	private _User userId;//�û�
	
	public Collect() {
	}
	public Collect(Resource r, _User u) {
		this.resourceId = r;
		this.userId = u;
	}
	public Resource getResourceId() {
		return resourceId;
	}
	public void setResourceId(Resource resourceId) {
		this.resourceId = resourceId;
	}
	public _User getUserId() {
		return userId;
	}
	public void setUserId(_User userId) {
		this.userId = userId;
	}
	
	public static void deleteCollect(final Context mContext, Collect collect) {
		
	//	Collect c = new Collect();
	//	c.setObjectId(collect.getObjectId());
		collect.delete(mContext,new DeleteListener() {
        @Override
        public void onSuccess() {
        	ToastFactory.showToast(mContext, "ɾ���ղسɹ�");
        }
        @Override
        public void onFailure(int i, String s) {
        	ToastFactory.showToast(mContext, "ɾ���ղ�ʧ��"+s);
        	Log.e("------", "ɾ���ղ�ʧ��"+s);
        }
    });	
	}
	
}
