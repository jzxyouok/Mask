package com.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;


public class _User extends BmobUser {
	
	private BmobFile head;//�û�ͷ��
	private  String userNick;//�û��ǳ�
	public _User () {
		
	}
	
	public BmobFile getHead() {
		return head;
	}
	public void setHead(BmobFile userPic) {
		this.head = userPic;
	}

	public String getUserNick() {
		return userNick;
	}

	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}
}
