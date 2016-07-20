package com.android.mask;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.adapter.CourseAdapter;
import com.android.recommend.RecommendActivity;
import com.android.settings.SettingsCenterActivity;
import com.bean.Resource;
import com.bean._User;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.util.ToastFactory;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

public class FragmentRecommend extends Fragment {
	protected ArrayList<Resource> mListItems; //�����б�
	private Context mContext;
    private CourseAdapter mAdapter; //�б�������
	private PullToRefreshListView refreshList; //�б�
	private ListView mainList;
	private boolean haveData = true,isRefresh = false; //�Ƿ�������
	private int pageNum = 0; //��ǰ���ص��ڼ�ҳ
	private ImageView  t1;
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		return inflater.inflate(R.layout.activity_content3, container, false);
	}
    
    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        initView(getActivity()); //��ʼ�����
        //Ϊ�б�����������
		mainList.setAdapter(mAdapter);
		//�����б��¼���������
		setListInfo();
		//���ý������ˢ��
		refreshList.setRefreshing(true);
	}

   public void  initView(Activity activity)
   {
   refreshList = (PullToRefreshListView) activity.findViewById(R.id.main_list3);
   mainList = refreshList.getRefreshableView();
   t1=(ImageView)activity.findViewById(R.id.id_img_returnHome);
   t1.setVisibility(View.GONE);
   //��ʼ�����ݼ��Ϻ�������
   mListItems = new ArrayList<Resource>();
   mAdapter = new CourseAdapter(mContext, mListItems);
   }
   public void queryContent()
   {
	   if(!haveData){
			return;
		}
		
		BmobQuery<Resource> query = new BmobQuery<Resource>();
		query.order("-createdAt");
//		query.setCachePolicy(CachePolicy.NETWORK_ONLY);
		query.setLimit(Constant.NUMBERS_PER_PAGE);
		query.setSkip(Constant.NUMBERS_PER_PAGE*(pageNum++));
		BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
		query.addWhereLessThan("createdAt", date);
		query.addWhereGreaterThan("lookNumber", 100);
		//query.addWhereEqualTo("classify", "����"); //����
		query.include("author.rank");
		query.findObjects(mContext, new FindListener<Resource>() {
			@Override
			public void onSuccess(List<Resource> list) {
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
					ToastFactory.showToast(mContext, "�ף���û���Ƽ����ݣ������ڴ�");
					haveData = false;
				}
				refreshList.onRefreshComplete();
			}
			@Override
			public void onError(int arg0, String arg1) {
				refreshList.onRefreshComplete();
				ToastFactory.showToast(mContext, "��ȡ����ʧ��,��������"+arg1);
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
				Intent intent = new Intent();
				intent.setClass(mContext, RecommendActivity.class);
				intent.putExtra("data", mListItems.get(position-1));
				mContext.startActivity(intent);
			}
		});
	}
}

