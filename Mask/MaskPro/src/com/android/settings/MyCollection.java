package com.android.settings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

import com.adapter.SmallCourseAdapter;
import com.android.mask.Constant;
import com.android.mask.MainActivity1;
import com.android.mask.R;
import com.android.recommend.RecommendActivity;
import com.bean.Collect;
import com.bean.Resource;
import com.bean._User;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.util.ToastFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MyCollection extends Activity{
	
	private ArrayList<Collect> myCollects = new ArrayList<Collect>();//��ȡ�����ղؼ�¼
	private ArrayList<Resource> myResourcesList;//��Ƶ��Դ�б�
	private SmallCourseAdapter myAdapter;//������
	private PullToRefreshListView refreshList; //�б�
	private ListView resourceList;
	private int pageNum = 0; //��ǰ���ص��ڼ�ҳ
	private boolean haveData = true,isRefresh = false; //�Ƿ�������
	
	private TextView title;// top_bar�����������ʾ
	private RelativeLayout return_settings_center;//���ذ�ť
	private Context mycollection_context;
	
	private LinearLayout bg_logo_layout;
	private TextView tips;//��ʾ
	private LinearLayout list_show;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.my_collections);
		init();
		setListener();
		setListInfo();
		//���ý������ˢ��
		refreshList.setRefreshing(true);
	}
	
	
	/**
	 * ��ʼ���ؼ�
	 */
	private void init() {
		mycollection_context = this;
		title = (TextView) findViewById(R.id.top_bar_title);
		title.setText("�ҵ��ղ�");
		return_settings_center = (RelativeLayout) findViewById(R.id.return_layout);
		
		refreshList = (PullToRefreshListView) findViewById(R.id.listView_my_collections);
		resourceList = refreshList.getRefreshableView();
		//��ʼ���б����ݣ�Ϊ�б��������
		myResourcesList = new ArrayList<Resource>();
		//��ʼ��������
		myAdapter = new SmallCourseAdapter(mycollection_context, myResourcesList);
		//Ϊ�б��������
		resourceList.setAdapter(myAdapter);
		
		list_show = (LinearLayout) findViewById(R.id.list);
		bg_logo_layout = (LinearLayout) findViewById(R.id.bg_logo_layout);
		tips = (TextView) findViewById(R.id.text_tips);//û���ղ�ʱ��������ʾ
		tips.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mycollection_context, MainActivity1.class);
				finish();
			    startActivity(intent);
			}
		});
	}
	//Ϊ��ť��ӵ����¼�
	private void setListener() {
		return_settings_center.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyCollection.this.finish();
			}
		});
		
	}
	/**
	 * ���ݵ�ǰ�û���ѯ���ղص���Դ
	 */
	private void queryResource() {
		if (!haveData) {
			return;
		}
		_User userInfo = BmobUser.getCurrentUser(mycollection_context, _User.class);
		BmobQuery<Collect> query = new BmobQuery<Collect>();
		query.order("-createdAt");
		query.setLimit(Constant.NUMBERS_PER_PAGE);
		query.setSkip(Constant.NUMBERS_PER_PAGE*(pageNum++));
		query.include("resourceId");
		query.addWhereEqualTo("userId", new BmobPointer(userInfo));
		query.findObjects(mycollection_context, new FindListener<Collect>() {
			@Override
			public void onError(int arg0, String arg1) {
				refreshList.onRefreshComplete();
				ToastFactory.showToast(mycollection_context, "�����ղ�ʧ�ܣ�"+arg1);
			}
			@Override
			public void onSuccess(List<Collect> data) {
				Log.e("---------", "--------data size:"+data.size());
				if (data.size() != 0 && data.get(data.size()-1)!=null) {
					if (isRefresh) {
						myCollects.clear();
						myResourcesList.clear();
					}
					myCollects.addAll(data);
					Iterator it = myCollects.iterator();
					while (it.hasNext()) {//��ȡ���ղ��б����ղص���Դ
						myResourcesList.add(((Collect)it.next()).getResourceId());
					}
					myAdapter.notifyDataSetChanged();//֪ͨ��������������
					if(data.size()<Constant.NUMBERS_PER_PAGE){
						haveData = false;
					}	
				} else {
					ToastFactory.showToast(mycollection_context, "��û���ղأ����ղ�һ����");
					haveData = false;
				//	list_show.setVisibility(View.GONE);
					bg_logo_layout.setVisibility(View.VISIBLE);
				}
				refreshList.onRefreshComplete();
				
			}
			
		});
	}
	
	/**
	 * �����б�����Լ��¼�����
	 */
	private void setListInfo() {
		refreshList.setMode(Mode.BOTH);
		refreshList.getLoadingLayoutProxy(true, false).setPullLabel("����ˢ��");
		refreshList.getLoadingLayoutProxy(true, false).setReleaseLabel("�ɿ�ˢ��");
		refreshList.getLoadingLayoutProxy(true, false).setRefreshingLabel("����ˢ��");
		refreshList.getLoadingLayoutProxy(false, true).setPullLabel("�������ظ���");
		refreshList.getLoadingLayoutProxy(false, true).setReleaseLabel("�ɿ����ظ���");
		refreshList.getLoadingLayoutProxy(false, true).setRefreshingLabel("���ڼ���");
		refreshList.getLoadingLayoutProxy(true, true).setLoadingDrawable(getResources().getDrawable(R.drawable.refresh));
		//����ˢ��
		refreshList.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				pageNum = 0;
				haveData = true;
				isRefresh = true;
				queryResource();
			}
			//�������ظ���
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				if(haveData){
					isRefresh = false;
					queryResource();
				}else{
					refreshList.postDelayed(new Runnable() {
			            @Override
			            public void run() {
			            	ToastFactory.showToast(mycollection_context, "�Ѽ�������������");
			            	refreshList.onRefreshComplete();
			            }
			        }, 500);
				}
			}
	    });
		
		//�б����¼�
		resourceList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				showDialog(myResourcesList.get(position - 1),new String[]{"�鿴","ɾ��"});
			}
		});
	}

	/**
	 * ��ʾ�б����ĶԻ���
	 * @param resource �б��Ӧ����Դ
	 * @param strings �Ի������ʾ��������
	 */
	protected void showDialog(final Resource resource, String[] stringItems) {
		 final ActionSheetDialog menuDialog = new ActionSheetDialog(mycollection_context, stringItems, null);
		 menuDialog.setOnOperItemClickL(new OnOperItemClickL() {
			@Override
			public void onOperItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch(position) {
				case 0: //�鿴������Դ�������������ݸ�������ʾҳ��
					Resource.incrementlookNumber(mycollection_context, resource);
					Log.e("-------", "�鿴");
					Intent intent = new Intent();
					intent.setClass(mycollection_context, RecommendActivity.class);
					intent.putExtra("data", resource);
					finish();
					mycollection_context.startActivity(intent);
					
					break;
				case 1://ɾ����������Դ������Resource�ľ�̬����ɾ��
					Log.e("-------", "----------ɾ��");
					//��Դ�ղ���-1
					Resource.decrementCollect(mycollection_context, resource);
					int index = myResourcesList.indexOf(resource);
					Log.e("-----", "zzzzzzzzz"+index);
					Collect aCollect = myCollects.get(index);
					Log.e("-----", "zzzzzzzzz"+myCollects.size()+aCollect.getObjectId());
					Collect.deleteCollect(mycollection_context, aCollect);
					refreshList.setRefreshing(true);
					myAdapter.notifyDataSetChanged();//֪ͨ��������������
					break;
				}
				menuDialog.dismiss();
			}
		 });
		 menuDialog.isTitleShow(false).show();
	}

}
