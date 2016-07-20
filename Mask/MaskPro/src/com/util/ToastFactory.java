package com.util;


import com.android.mask.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ���Toast�ظ����ֶ�Σ�����ȫ��ֻ��һ��Toastʵ��
 * ģ��QQ��ʾUI
 * @author ��ҹ�¶���
 */
public class ToastFactory {
	private static Context context = null;
	private static Toast toast = null;
	private static TextView text;
	/**
	 * ������Ϣ
	 */
	public static void showToast(Context context, String message) {
		if (ToastFactory.context == context) {
			//cancelToast();
			if(text==null){
				text = (TextView) toast.getView().findViewById(R.id.toast_message);
			}
			text.setText(message);
		} else {
			cancelToast();
			ToastFactory.context = context;
			toast = new Toast(context);
			toast.setGravity(Gravity.TOP, 0, 100);
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.myview_custom_toast, null);// �Զ��岼��
			text = (TextView) view.findViewById(R.id.toast_message);// ��ʾ����ʾ����
			text.setText(message);
			toast.setView(view);
		}
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.show();
	}
	/**
	 * �رյ�ǰtoast
	 */
	public static void cancelToast() {
		if (toast != null) {
			toast.cancel();
		}
	}
}
