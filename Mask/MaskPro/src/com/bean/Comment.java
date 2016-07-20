package com.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Comment extends BmobObject {
	private static final long serialVersionUID = 1L;
	private _User user;// �������۵��û�
	private String commentContent;// ���۵�����
	private BmobFile Contentfigureurl; // ����ͼƬ
	private Comment parent;	//������
	private boolean isAnonymous;	//�Ƿ�����
	private String toUser;	//�����۵�user
	private Topic toNote;	//�����۵�����
//1~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private Resource toNoteResource; //�����۵Ŀγ�
//2~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private boolean isRead; //���������Ƿ��Ѷ�

	public _User getUser() {
		return user;
	}

	public void setUser(_User user) {
		this.user = user;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public BmobFile getContentfigureurl() {
		return Contentfigureurl;
	}

	public void setContentfigureurl(BmobFile contentfigureurl) {
		Contentfigureurl = contentfigureurl;
	}

	public Comment getParent() {
		return parent;
	}

	public void setParent(Comment parent) {
		this.parent = parent;
	}

	public boolean isAnonymous() {
		return isAnonymous;
	}

	public void setAnonymous(boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public Topic getToNote() {
		return toNote;
	}

	public void setToNote(Topic toNote) {
		this.toNote = toNote;
	}

	
//1~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public Resource getToNoteResource() {
		return toNoteResource;
	}

	public void setToNoteResource(Resource toNoteResource) {
		this.toNoteResource = toNoteResource;
	}
//2~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	
}
