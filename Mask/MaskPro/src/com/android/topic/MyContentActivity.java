package com.android.topic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

import com.adapter.AIContentAdapter;
import com.dao.QiangYuDao;
import com.android.mask.Constant;

import com.android.mask.R;
import com.android.settings.SettingsCenterActivity;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.bean.Topic;
import com.bean._User;
import com.util.ToastFactory;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.myview.MyConfirmDialog;

/**
 * չʾ�û������ӻ����ղ�,��Ҫ����������
 * User - "user" ��ǰ���û�
 * boolean - "isFav" �Ƿ����ղ�
 * @author dulang
 */
public class MyContentActivity extends Activity{
	private PullToRefreshListView refreshList; //�б�
	private ListView mainList;
	private ArrayList<Topic> mListItems; //�����б�
	private AIContentAdapter mAdapter; //�б�������
	private _User mUser;
	private boolean isFav;
	private boolean haveData = true,isRefresh = false; //�Ƿ�������
	private int pageNum = 0; //��ǰ���ص��ڼ�ҳ
	private Context mContext;
	
	private LinearLayout bg_logo_layout;
	private TextView tips;//��ʾ
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.activity_mycontent);
		
        initView(); //��ʼ�����
		//�����б��¼���������
		setListInfo();
		//���ý������ˢ��
		refreshList.setRefreshing(true);
	}
	/**
	 * ��ʼ�����
	 */
	private void initView(){
		mContext = this;
        refreshList = (PullToRefreshListView) findViewById(R.id.main_list);
        mainList = refreshList.getRefreshableView();
        //��ʼ�����ݼ���,Ϊ�б�����������
        mListItems = new ArrayList<Topic>();
		mAdapter = new AIContentAdapter(this, mListItems);
		mainList.setAdapter(mAdapter);
		//�õ���ѯ������
	//	mUser = (User) getIntent().getSerializableExtra("user");
		mUser = BmobUser.getCurrentUser(mContext,_User.class);
		
		bg_logo_layout = (LinearLayout) findViewById(R.id.bg_logo_layout);
		tips = (TextView) findViewById(R.id.text_tips);//û���ղ�ʱ��������ʾ
	}
	/**
	 * ��ѯ��ǰ�û���������ӻ����ղص�����
	 */
	private void queryContent(){
		if(!haveData){
			return;
		}
		BmobQuery<Topic> query = new BmobQuery<Topic>();
		query.order("-createdAt");
//		query.setCachePolicy(CachePolicy.NETWORK_ONLY);
		query.setLimit(Constant.NUMBERS_PER_PAGE);
		query.setSkip(Constant.NUMBERS_PER_PAGE*(pageNum++));
		BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
		query.addWhereLessThan("createdAt", date);
		//�ж��Ƿ����ղ����ò�ѯ����
		query.addWhereEqualTo("author", mUser);
		query.include("author");
		query.findObjects(this, new FindListener<Topic>() {
			@Override
			public void onSuccess(List<Topic> list) {
				if(list.size()!=0&&list.get(list.size()-1)!=null){
					if(isRefresh){
						mListItems.clear();
					}
					mListItems.addAll(list);
					mAdapter.notifyDataSetChanged();
					//û�и���������
					if(list.size()<Constant.NUMBERS_PER_PAGE){
						haveData = false;
					}
				}else{
					ToastFactory.showToast(mContext, "��û������");
					haveData = false;
					bg_logo_layout.setVisibility(View.VISIBLE);
				}
				refreshList.onRefreshComplete();
			}
			@Override
			public void onError(int arg0, String arg1) {
				refreshList.onRefreshComplete();
				ToastFactory.showToast(mContext, "��ȡ����ʧ��"+arg1);
			}
		});
	}
	/**
	 * �����б����Լ��¼�����
	 */
	private void setListInfo(){
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
				queryContent();
			}
			//�������ظ���
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				if(haveData){
					isRefresh = false;
					queryContent();
				}else{
					refreshList.postDelayed(new Runnable() {
			            @Override
			            public void run() {
			            	ToastFactory.showToast(mContext, "�Ѽ�������������");
			            	refreshList.onRefreshComplete();
			            }
			        }, 500);
				}
			}
		});
		//�б����¼�
		mainList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				_User currenUser = BmobUser.getCurrentUser(mContext, _User.class);
				if(!currenUser.getObjectId().equals(mUser.getObjectId())){
					Intent intent = new Intent();
					intent.setClass(mContext, CommentActivity.class);
					intent.putExtra("data", mListItems.get(position-1));
					startActivity(intent);
					return;
				}
				if(isFav){
					showDialog(mListItems.get(position-1), new String[]{"�鿴����","ɾ������","ȡ���ղ�"});
				}else{
					showDialog(mListItems.get(position-1), new String[]{"�鿴����","ɾ������"});
				}
			}
		});
	}
	/**
	 * ��ʾ�б����ĶԻ���
	 */
	private void showDialog(final Topic data,final String[] stringItems){
        final ActionSheetDialog menuDialog = new ActionSheetDialog(mContext, stringItems, null);
        menuDialog.setOnOperItemClickL(new OnOperItemClickL() {
			@Override
			public void onOperItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				final QiangYuDao dao = new QiangYuDao();
				switch (position) {
				case 0:
					Intent intent = new Intent();
					intent.setClass(mContext, CommentActivity.class);
					intent.putExtra("data", data);
					startActivity(intent);
					break;
				case 1:
					if(!data.getAuthor().getObjectId().equals(mUser.getObjectId())){
						ToastFactory.showToast(mContext, "�㲻��ɾ�����˵�����");
						return;
					}
					//ɾ������
					final MyConfirmDialog cDialog = new MyConfirmDialog(mContext);
					cDialog.setTitle("ɾ������");
					cDialog.setContent("ȷ��ɾ����");
					cDialog.setConfirmButtonListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							cDialog.dismiss();
							dao.deleteQiangYu(mContext, data, mAdapter);
						}
					});
					cDialog.show();
					break;
				default:
					break;
				}
				menuDialog.dismiss();
			}
        });
        menuDialog.isTitleShow(false).show();
	}
	/**
	�˵�����¼�
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				MyContentActivity.this.finish();
	   		    break;
		}
		return super.onOptionsItemSelected(item);
	}
}
