package com.myview;

import com.android.mask.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyConfirmDialog extends Dialog{
	TextView titleText,contentText;
	Button cancleButton,confirmButton;
	String title,content;
	android.view.View.OnClickListener listener;

	public MyConfirmDialog(Context context) {
		super(context,R.style.CustomDialog);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myview_confirm_dialog);
		
		//��ʼ�����
		initView();
		if(title!=null){
			titleText.setText(title);
		}
		if(content!=null){
			contentText.setText(content);
		}
		cancleButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		if(listener!=null){
			confirmButton.setOnClickListener(listener);
		}
	}

	/**
	 * ��ʼ�����
	 */
	private void initView() {
		titleText = (TextView) findViewById(R.id.dialog_title);
		contentText = (TextView) findViewById(R.id.dialog_content);
		cancleButton = (Button) findViewById(R.id.dialog_button1);
		confirmButton = (Button) findViewById(R.id.dialog_button2);
	}
	/**
	 * ���ñ���
	 */
	public void setTitle(String title){
		this.title = title;
	}
	/**
	 * ��������
	 */
	public void setContent(String content){
		this.content = content;
	}
	/**
	 * ����ȷ����ť�ĵ���¼�
	 */
	public void setConfirmButtonListener(android.view.View.OnClickListener listener){
		this.listener = listener;
	}
}
