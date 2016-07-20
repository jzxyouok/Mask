package com.bean;


import java.io.Serializable;
import java.sql.Date;

import android.content.Context;

import com.bmob.BmobProFile;
import com.util.ToastFactory;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.DeleteListener;

public class Resource extends BmobObject implements Serializable{
	private static final long serialVersionUID = 1L;
	private String resName;
	private Integer lookNumber = 0;//�����
	private Integer CommentNumber= 0;//������
	private Integer reportNum= 0;//�ٱ���
	private Integer collectNum= 0; //�ղ���
	private BmobFile resContent;
	private BmobFile picture;
	private String resUrl;
	private String labels;//��ǩ
	private String pictureUrl;
	private _User user;//�ϴ���Ƶ��user��Id
	private String clissify;//��Դ�ķ���
	private String resType;
	private BmobRelation comments;//�����б�
	private BmobRelation Report; // �ٱ��б�
	private BmobRelation Collect; //���� �ղ��б�
	
	public Resource() {
		
	}
	public Resource(String name,BmobFile content,String url) {
		this.resName = name;
		this.resContent = content;
		this.resUrl = url;
	}
	
	public String getResName() {
		return resName;
	}
	public void setResName(String resName) {
		this.resName = resName;
	}
	public String getResType() {
		return resType;
	}
	public void setResType(String resType) {
		this.resType = resType;
	}
	public BmobFile getResContent() {
		return resContent;
	}
	public void setResContent(BmobFile resContent) {
		this.resContent = resContent;
	}
	public Integer getLookNumber() {
		return lookNumber;
	}
	public void setLookNumber(Integer lookNumber) {
		this.lookNumber = lookNumber;
	}
	public BmobFile getPicture() {
		return picture;
	}
	public void setPicture(BmobFile picture) {
		this.picture = picture;
	}
	public String getResUrl() {
		return resUrl;
	}
	public void setResUrl(String resUrl) {
		this.resUrl = resUrl;
	}
	public String getClissify() {
		return clissify;
	}
	public void setClissify(String clissify) {
		this.clissify = clissify;
	}
	public String getLabels() {
		return labels;
	}
	public void setLabels(String labels) {
		this.labels = labels;
	}
	public _User getUser() {
		return user;
	}
	public void setUser(_User user) {
		this.user = user;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	
	//�����+1�ľ�̬����
	public static void incrementlookNumber(Context mContext, Resource r) {
		r.increment("lookNumber");
		r.update(mContext);
	}
	//�ٱ���+1�ľ�̬����
	public static void incrementReportNum(Context mContext, Resource r) {
		r.increment("reportNum");
		r.update(mContext);
	}
	//������+1
	public static void incrementCommentNumber(Context mContext, Resource r) {
		r.increment("CommentNumber");
		r.update(mContext);
	}
			
	//�ղ���+1�ľ�̬����
	public static void incrementCollectNum(Context mContext, Resource r) {
		r.increment("collectNum");
		r.update(mContext);
	}
	//ɾ����Դ�ľ�̬����
	public static void deleteResource(final Context mContext, Resource r) {
		//ɾ����Դ�е���Ƶ��ͼƬ
		BmobFile oldFile = r.getResContent();
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
		// ɾ�������е�ͼƬ
		BmobFile oldFilePic = r.getPicture();
		if (oldFilePic != null) {
			oldFilePic.delete(mContext, new DeleteListener() {
				@Override
				public void onSuccess() {
				}
				@Override
				public void onFailure(int arg0, String arg1) {
				}
			});
		}
		 r.delete(mContext,new DeleteListener() {
	            @Override
	            public void onSuccess() {
	            	ToastFactory.showToast(mContext, "ɾ���ɹ�");
	            }
	 
	            @Override
	            public void onFailure(int i, String s) {
	            	ToastFactory.showToast(mContext, "ɾ��ʧ��");
	            }
	        });	
	}
	//�ղ���-1
	public static void decrementCollect(final Context mContext, Resource r) {
		r.setCollectNum(r.getCollectNum()-1);
		if (r.getCollectNum() <= 0)
			r.setCollectNum(0);
		r.update(mContext);
	}
	public Integer getReportNum() {
		return reportNum;
	}
	public void setReportNum(Integer reportNum) {
		this.reportNum = reportNum;
	}
	public BmobRelation getComments() {
		return comments;
	}
	public void setComments(BmobRelation comments) {
		this.comments = comments;
	}
	public BmobRelation getReport() {
		return Report;
	}
	public void setReport(BmobRelation report) {
		Report = report;
	}
	public BmobRelation getCollect() {
		return Collect;
	}
	public void setCollect(BmobRelation collect) {
		Collect = collect;
	}
	public Integer getCollectNum() {
		return collectNum;
	}
	public void setCollectNum(Integer collectNum) {
		this.collectNum = collectNum;
	}
	public Integer getCommentNumber() {
		return CommentNumber;
	}
	public void setCommentNumber(Integer resCommentNum) {
		this.CommentNumber = resCommentNum;
	}
	
}

