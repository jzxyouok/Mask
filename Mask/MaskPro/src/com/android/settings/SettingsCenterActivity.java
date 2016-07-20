package com.android.settings;

import cn.bmob.v3.BmobUser;

import com.android.mask.MainActivity1;
import com.android.mask.R;
import com.android.recommend.RecommendActivity;
import com.android.topic.MyContentActivity;
import com.bean._User;
import com.util.ToastFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsCenterActivity extends Activity {

	private RelativeLayout personal_data;
	private RelativeLayout my_collection;
	private RelativeLayout mytopic;
	private RelativeLayout my_message;
	private RelativeLayout my_upload;
	private RelativeLayout contact_us;
	private RelativeLayout software_update;
	private RelativeLayout logout;

	_User userInfo;// ��ǰ��¼�˵��û�
	private TextView login_register_text;
	private ImageView btn_login_register;
	private ImageView topic;
	private Context btn_user_center_Context;
	private ImageView return_to_home;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_center_layout);
		init();// ��ʼ����Ҫ�õ���textView
		// �ӻ����л�ȡ��ǰ��¼���û�
		userInfo = BmobUser.getCurrentUser(this, _User.class);
		setListener();
		// ��ʼ��ҳ����ʾ
		if (userInfo != null) {// ������û���¼
			if (userInfo.getUserNick() == null)// �û�û�������ǳƵ�ʱ��Ĭ����Ů���������ǳ�֮���Ϊ�ǳ�
				login_register_text.setText("Ů��" + "(" + userInfo.getUsername()
						+ ")");
			else
				login_register_text.setText(userInfo.getUserNick().toString()
						+ "(" + userInfo.getUsername() + ")");
			// ��ʾ�û�ͷ��
			if (userInfo.getHead() != null) {
				userInfo.getHead().loadImage(btn_user_center_Context,
						btn_login_register);
			}
		}
	}

	private void init() {
		personal_data = (RelativeLayout) findViewById(R.id.personal_data);
		my_collection = (RelativeLayout) findViewById(R.id.my_collection);
		my_message = (RelativeLayout) findViewById(R.id.my_message);
		mytopic = (RelativeLayout) findViewById(R.id.mytopic);
		my_upload = (RelativeLayout) findViewById(R.id.my_upload);
		contact_us = (RelativeLayout) findViewById(R.id.contact_us);
		software_update = (RelativeLayout) findViewById(R.id.software_update);
		logout = (RelativeLayout) findViewById(R.id.logout);

		btn_user_center_Context = this;
		btn_login_register = (ImageView) findViewById(R.id.id_img1);
		login_register_text = (TextView) findViewById(R.id.id_login_or_register);
		return_to_home = (ImageView) findViewById(R.id.return_right);
		return_to_home.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}

	/**
	 * Ϊÿ����ť�󶨼����¼�
	 */
	private void setListener() {
		// ��¼ע��İ�ť��������ʾͷ���λ�ã����û���û���¼�������ת����¼/ע��
		// ������ת�ĸ�������ҳ��
		btn_login_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (userInfo == null) {
					Intent intent = new Intent(btn_user_center_Context,
							LoginActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(btn_user_center_Context,
							PersonalData.class);
					startActivity(intent);
				}
			}
		});
		// �������ϵİ�ť��������û���¼����ת����������ҳ��
		// ������ת����¼ע��
		personal_data.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent;
				if (userInfo == null) {
					intent = new Intent(btn_user_center_Context,
							LoginActivity.class);
				} else {
					intent = new Intent(btn_user_center_Context,
							PersonalData.class);
				}
				startActivity(intent);
			}
		});
		// �ҵ����Ӱ�ť�����û���û���¼����ת��¼ע��
		// ������ת���ҵ�ҳ�棡����

		mytopic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent;
				if (userInfo == null) {
					intent = new Intent(btn_user_center_Context,
							LoginActivity.class);

				} else {
					intent = new Intent(btn_user_center_Context,
							MyContentActivity.class);
				}
				startActivity(intent);
			}
		});

		// �ҵ��ղذ�ť�����û���û���¼����ת��¼ע��
		// ������ת���ҵ��ղ�ҳ�棡����
		my_collection.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent;
				if (userInfo == null) {
					intent = new Intent(btn_user_center_Context,
							LoginActivity.class);
				} else {
					intent = new Intent(btn_user_center_Context,
							MyCollection.class);
				}
				startActivity(intent);
			}
		});
		// ��Ϣ֪ͨ��ť�����û���û���¼����ת��¼ע��
		// ������ת����Ϣ֪ͨҳ�棡����
		my_message.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent;
				if (userInfo == null) {
					intent = new Intent(btn_user_center_Context,
							LoginActivity.class);
				} else {
					intent = new Intent(btn_user_center_Context,
							MyMessage.class);
				}
				startActivity(intent);
			}
		});
		my_upload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent;
				if (userInfo == null) {
					intent = new Intent(btn_user_center_Context,
							LoginActivity.class);
				} else {
					intent = new Intent(btn_user_center_Context,
							MyUpload.class);
				}
				startActivity(intent);
			}
		});
		// ��ϵ���ǣ����û���û���¼����ת����¼/ע��
		// ������ת����ϵ���ǵ�ҳ��
		contact_us.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent;
				if (userInfo == null) {
					intent = new Intent(btn_user_center_Context,
							LoginActivity.class);
				} else {
					intent = new Intent(btn_user_center_Context,
							AboutUs.class);
				}
				startActivity(intent);
			}
		});

		// �����������ʾ�����������Ϣ
		software_update.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastFactory.showToast(btn_user_center_Context, "Ŀǰ�������°汾��");
			}
		});

		// �˳���¼�����û�е�¼����ʾ���ȵ�¼
		// �������useInfo����Ϣ����������ʾͷ��Ϊ����me
		logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (userInfo == null) {
					Toast.makeText(SettingsCenterActivity.this,// ��ʾ����ͬʱ��ʾ��ѯ����
							"���ȵ�¼", Toast.LENGTH_SHORT).show();
				} else {
					BmobUser.logOut(btn_user_center_Context);// ��������û�����
					userInfo = (_User) BmobUser
							.getCurrentUser(btn_user_center_Context); // ���ڵ�currentUser��null��
					// btn_login_register.setImageBitmap(null);//setBackgroundResource(R.drawable.iv_background_me);
					btn_login_register.setImageResource(R.drawable.bigger_logo);
					login_register_text.setText("��¼/ע��");
					Intent intent=new Intent();
					intent.setClass(SettingsCenterActivity.this,MainActivity1.class);
					startActivity(intent);
					finish();
				}
			}
		});
	}

}
