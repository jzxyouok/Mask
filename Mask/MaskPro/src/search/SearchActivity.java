package search;

 import java.util.ArrayList;
 import java.util.Date;
 import java.util.List;
 import cn.bmob.v3.BmobQuery;
 import cn.bmob.v3.datatype.BmobDate;
 import cn.bmob.v3.listener.FindListener;
 import com.adapter.CourseAdapter;
 import com.android.mask.Constant;
 import com.android.mask.R;
 import com.android.recommend.RecommendActivity;
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
	
import android.util.Log;
	import android.view.View;
import android.view.Window;
	
	import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
	
	import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

	public class SearchActivity extends Activity {
		private ImageView to_classifyActivity;
		private String keyWord;
		private EditText searchStr;
		private TextView search;
		

		// 1~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		protected ArrayList<Resource> mListItems; // �����б�
		private CourseAdapter mAdapter; // �б�������
		private PullToRefreshListView refreshList; // �б�
		private ListView mainList;
		private RadioGroup headTypeGroup;
		private String type2;// �������
		private int pageNum = 0; // ��ǰ���ص��ڼ�ҳ
		private boolean haveData = true, isRefresh = false; // �Ƿ�������
		private Context mContext;
		

		// 2~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		// 1~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// 2~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		private _User userInfo;

		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE); // ȡ��Ĭ��top bar
			
			setContentView(R.layout.search_course); // �󶨵�ǰlayoutΪactivity_main.xml

			// 1~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			initView(); // ��ʼ�����
			
			// 2~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		}

		// 1~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		private void initView() {
			mContext = this;
			
			
			//���ذ�ť����¼�
			to_classifyActivity = (ImageView) findViewById(R.id.return_classify);
			to_classifyActivity.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {				
					finish();
				}
			});
		
			
			//��������¼�
			search = (TextView) findViewById(R.id.id_tv_search);
			searchStr = (EditText) findViewById(R.id.id_et_search);
			search.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String wantSearchStr = searchStr.getText().toString();
					Log.e("-----","wantSearchStr="+wantSearchStr);
					if(wantSearchStr == null || wantSearchStr.equals("")){
						ToastFactory.showToast(mContext, "����������");
					}else{
						keyWord = wantSearchStr;
						// �����б��¼���������
						setListInfo();
						// ���ý������ˢ��
						refreshList.setRefreshing(true);
					}
				}
			});
			
			refreshList = (PullToRefreshListView) findViewById(R.id.main_list);
			mainList = refreshList.getRefreshableView();
			// �õ��б�ͷ��������ɸѡ���
			headTypeGroup = (RadioGroup) getLayoutInflater().inflate(
					R.layout.header_hall_type, null);
			// ��ʼ�����ݼ���,Ϊ�б�����������
			mListItems = new ArrayList<Resource>();
			mAdapter = new CourseAdapter(this, mListItems);
			// �����б�ͷ�����
			// mainList.addHeaderView(headTypeGroup);
			mainList.setAdapter(mAdapter);
		}

		private void queryContent() {
			if (!haveData) {
				return;
			}

			// ��ȡ������Ƶ��Դ
			BmobQuery<Resource> query = new BmobQuery<Resource>();
			query.order("-createdAt");
			// query.setCachePolicy(CachePolicy.NETWORK_ONLY);
			query.setLimit(Constant.NUMBERS_PER_PAGE);
			query.setSkip(Constant.NUMBERS_PER_PAGE * (pageNum++));
			BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
			query.addWhereLessThan("createdAt", date);
			query.addWhereContains("labels", keyWord);
			if (type2 != null) {
				query.addWhereEqualTo("type", type2);
			}
			query.include("user.rank");
			query.findObjects(this, new FindListener<Resource>() {
				@Override
				public void onSuccess(List<Resource> list) {
					if (list.size() != 0 && list.get(list.size() - 1) != null) {
						if (isRefresh) {
							mListItems.clear();
						}
						mListItems.addAll(list);
						mAdapter.notifyDataSetChanged();
						// û�и���������
						if (list.size() < Constant.NUMBERS_PER_PAGE) {
							haveData = false;
						}
					} else {
						ToastFactory.showToast(mContext, "��û������,�췢���һ����");
						haveData = false;
					}
					refreshList.onRefreshComplete();
				}

				@Override
				public void onError(int arg0, String arg1) {
					refreshList.onRefreshComplete();
					ToastFactory.showToast(mContext, "��ȡ����ʧ��,��������" + arg1);
				}
			});
		}
		
		/**
		 * �����б����Լ��¼�����
		 */
		private void setListInfo() {
			refreshList.setMode(Mode.BOTH);
			refreshList.getLoadingLayoutProxy(true, false).setPullLabel("����ˢ��");
			refreshList.getLoadingLayoutProxy(true, false).setReleaseLabel("�ɿ�ˢ��");
			refreshList.getLoadingLayoutProxy(true, false).setRefreshingLabel(
					"����ˢ��");
			refreshList.getLoadingLayoutProxy(false, true).setPullLabel("�������ظ���");
			refreshList.getLoadingLayoutProxy(false, true)
					.setReleaseLabel("�ɿ����ظ���");
			refreshList.getLoadingLayoutProxy(false, true).setRefreshingLabel(
					"���ڼ���");
			refreshList.getLoadingLayoutProxy(true, true).setLoadingDrawable(
					getResources().getDrawable(R.drawable.refresh));
			// ����ˢ��
			refreshList.setOnRefreshListener(new OnRefreshListener2<ListView>() {
				@Override
				public void onPullDownToRefresh(
						PullToRefreshBase<ListView> refreshView) {
					pageNum = 0;
					haveData = true;
					isRefresh = true;
					
					    queryContent();
				}

				// �������ظ���
				@Override
				public void onPullUpToRefresh(
						PullToRefreshBase<ListView> refreshView) {
					if (haveData) {
						isRefresh = false;					
						    queryContent();
					} else {
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
			// �б����¼�
			mainList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent();
					intent.setClass(mContext, RecommendActivity.class);
					intent.putExtra("data", mListItems.get(position - 1));
					mContext.startActivity(intent);
				}
			});
			// ѡ�����͵ļ����¼���ɸѡ��������
			headTypeGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup arg0, int arg1) {
					switch (arg1) {
					case R.id.hall_type_radio1:
						type2 = null;
						break;
					case R.id.hall_type_radio2:
						type2 = Constant.TYPE_1;
						break;
					case R.id.hall_type_radio3:
						type2 = Constant.TYPE_2;
						break;
					case R.id.hall_type_radio4:
						type2 = Constant.TYPE_3;
						break;
					default:
						break;
					}
					haveData = true;
					pageNum = 0;
					mListItems.clear();
					mAdapter.notifyDataSetChanged();
					refreshList.setRefreshing();
				}
			});
		

		// 2~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		}
	}
