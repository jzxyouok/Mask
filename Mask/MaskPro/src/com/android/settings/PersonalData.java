package com.android.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.EmailVerifyListener;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.android.mask.R;
import com.bean._User;
import com.util.CacheUtils;
import com.util.ImageUtil;
import com.util.ToastFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalData extends Activity {

	private RelativeLayout return_settings_center;
	private TextView saveUpdate;// �����޸ģ��ϴ����ƶ�
	private TextView title;// top_bar�����������ʾ
	private RelativeLayout head_change_layout;// ����޸�ͷ�����ͼ
	private ImageView head;// ��ʾͷ�����ͼ
	private String iconUrl;
	_User currentUser;
	private Context personal_data_context;
	
	private TextView userName;//��ʾ�û����Ŀؼ�
	private EditText newNick;
	// �޸�����
	private RelativeLayout pwd_change_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.personal_data);
		init();
		setListener();
	}

	/**
	 * ��ʼ��ҳ��ĸ����ؼ�
	 */
	private void init() {
		personal_data_context = this;
		currentUser = BmobUser.getCurrentUser(personal_data_context,_User.class);
		return_settings_center = (RelativeLayout) findViewById(R.id.return_layout);
		saveUpdate = (TextView) findViewById(R.id.save_personal_data_update);
		saveUpdate.setText("����");
		title = (TextView) findViewById(R.id.top_bar_title);
		title.setText("�޸�����");
		head_change_layout = (RelativeLayout) findViewById(R.id.change_head);
		head = (ImageView) findViewById(R.id.head);
		//�����¼���û�������ͷ����ʾͷ��
		if (currentUser.getHead() != null) {
			currentUser.getHead().loadImage(personal_data_context, head);
		}
		userName = (TextView) findViewById(R.id.user_name);
		userName.setText(currentUser.getUsername());
		// �޸��ǳƵ��ı���
		newNick = (EditText) findViewById(R.id.nick);
		if(currentUser.getUserNick() != null) {
			newNick.setText(currentUser.getUserNick());
		} else {
			newNick.setHint("Ů��");
		}
		pwd_change_layout = (RelativeLayout) findViewById(R.id.change_pwd_layout);
	}
	/**
	 * �޸�ͷ��
	 */
	String dateTime;
	AlertDialog albumDialog;
	public void showAlbumDialog() {
		//����ѡ��ͼƬ��ʽ�Ի���
		albumDialog = new AlertDialog.Builder(personal_data_context).create();
		albumDialog.setCanceledOnTouchOutside(true);
		View v = LayoutInflater.from(personal_data_context).inflate(R.layout.myview_dialog_usericon, null);
		albumDialog.show();
		albumDialog.setContentView(v);
		albumDialog.getWindow().setGravity(Gravity.CENTER);

		TextView albumPic = (TextView) v.findViewById(R.id.album_pic);
		TextView cameraPic = (TextView) v.findViewById(R.id.camera_pic);
		//ͼ��ѡ��
		albumPic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				albumDialog.dismiss();
				Date date1 = new Date(System.currentTimeMillis());
				dateTime = date1.getTime() + "";
				getAvataFromAlbum();
			}
		});
		//����ѡ��
		cameraPic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				albumDialog.dismiss();
				Date date = new Date(System.currentTimeMillis());
				dateTime = date.getTime() + "";
				getAvataFromCamera();
			}
		});
	}
	/**
	 * ��ת��ͼ��
	 */
	private void getAvataFromAlbum() {
		Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
		intent2.setType("image/*");
		startActivityForResult(intent2, 2);
	}
	/**
	 * ��ת�����
	 */
	private void getAvataFromCamera() {
		File f = new File(CacheUtils.getCacheDirectory(personal_data_context, true, "icon")
				+ dateTime);
		if (f.exists()) {
			f.delete();
		}
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Uri uri = Uri.fromFile(f);
		Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(camera, 1);
	}
	
	
	private void setListener() {
		//������ذ�ť��������������
		return_settings_center.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			PersonalData.this.finish();
			}
		});
		head_change_layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			showAlbumDialog();
			}
		});
		//����޸����룺�����޸��ڴ˴����浽�ƶ�
		pwd_change_layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showRepassword();
			}
			private void showRepassword() {
			final String email = currentUser.getEmail();
			BmobUser.resetPasswordByEmail(personal_data_context, email, new ResetPasswordByEmailListener() {
			@Override
			public void onSuccess() {
			if (currentUser.getEmailVerified() == null || (!currentUser.getEmailVerified())) {//û��������֤���Ƚ�����֤
			BmobUser.requestEmailVerify(personal_data_context, email, new EmailVerifyListener() {
			@Override
			public void onSuccess() {
			Toast.makeText(personal_data_context,"�޸����룬���ȵ�"+email+"���������֤",Toast.LENGTH_SHORT).show();
			}	
			@Override
			public void onFailure(int arg0, String arg1) {
			}
			});
			} else {
			Toast.makeText(personal_data_context,"������������ɹ����뵽" + email + "��������������ò���",Toast.LENGTH_SHORT).show();
			}
			}
			@Override
			public void onFailure(int arg0, String arg1) {
			Toast.makeText(personal_data_context,"������������ʧ��",Toast.LENGTH_SHORT).show();
			}
		});
	      }
		});
	}
	/**
	 * ����ѡ��ͼƬ���ؽ��
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK){
			switch (requestCode) {
			//����������ļ���
			case 1:
				String files = CacheUtils.getCacheDirectory(personal_data_context, true,"icon") + dateTime;
				File file = new File(files);
				if (file.exists() && file.length() > 0) {
					Uri uri = Uri.fromFile(file);
					//�ü�ͼƬ
					startPhotoZoom(uri);
				}
				break;
			case 2:
				//ͼ��ѡ����ļ���
				if (data == null) {
					return;
				}
				//�ü�ͼƬ
				startPhotoZoom(data.getData());
				break;
			case 3:
				//�ü���ϵļ���
				if (data != null) {
					Bundle extras = data.getExtras();
					if (extras != null) {
						Bitmap bitmap = extras.getParcelable("data");
						iconUrl = saveToSdCard(bitmap);
						head.setImageBitmap(bitmap);
						//�ϴ�ͷ���޸�����
						updateIcon(iconUrl);
					}
				}
				break;
			}
		}
	}
	/**
	 * ����ͼ��ü�ͼƬ
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 120);
		intent.putExtra("outputY", 120);
		intent.putExtra("crop", "true");
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);
		// intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}
	/**
	 * ����ѡ�õ�ͼƬ��SD��
	 */
	public String saveToSdCard(Bitmap bitmap) {
		String files = CacheUtils.getCacheDirectory(personal_data_context, true, "icon")+ dateTime + "_12.jpg";
		File file = new File(files);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}
	/**
	 * �ϴ�ͷ���޸�����
	 */
	private void updateIcon(String avataPath) {
		final _User currentUser = BmobUser.getCurrentUser(personal_data_context,_User.class);
		//ɾ����ͼƬ
		BmobFile oldFile = currentUser.getHead();
		if(oldFile!=null){
			oldFile.delete(personal_data_context, new DeleteListener() {
				@Override
				public void onSuccess() {
					
				}
				@Override
				public void onFailure(int arg0, String arg1) {
					
				}
			});
		}
		if (avataPath != null) {
			final BmobFile file = new BmobFile(new File(avataPath));
			file.upload(personal_data_context, new UploadFileListener() {
				@Override
				public void onSuccess() {
					currentUser.setHead(file);
					currentUser.update(personal_data_context, new UpdateListener() {
						@Override
						public void onSuccess() {
							ToastFactory.showToast(personal_data_context, "�޸ĳɹ�");
						}
						@Override
						public void onFailure(int arg0, String arg1) {
							ToastFactory.showToast(personal_data_context, "�޸�ʧ��");
						}
					});
				}
				@Override
				public void onProgress(Integer arg0) {
				}
				@Override
				public void onFailure(int arg0, String arg1) {
					ToastFactory.showToast(personal_data_context, "�޸�ʧ��");
				}
			});
		}
	}
}

