package com.android.recommend;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.adapter.CommentAdapter;
import com.android.mask.Constant;
import com.android.mask.R;
import com.android.settings.LoginActivity;
import com.bean.Collect;
import com.bean.Comment;
import com.bean.Resource;
import com.bean._User;
import com.dao.CommentDao;
import com.dao.QiangYuDao;
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
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RecommendActivity extends Activity implements OnClickListener,
OnInfoListener, OnBufferingUpdateListener {
	private Resource aCourse = null;

	public Context context;
	public Context mContext;

	private ImageView img_returnHome;
	
	/*
	 * ��Ƶ�������
	 */
	private VideoView mVideoView; 
	private ProgressBar pb;
	private TextView downloadRateView, loadRateView;
	
	private TextView tv_MUname;
	private TextView tv_courseViewCount;
	private TextView tv_courseCollectCount;
	private ImageView img_courseCollectCount;
	private ImageView img_courseReport;

	private RelativeLayout comment;
	private TextView userName;	//¥���û���
	private List<Comment> comments = new ArrayList<Comment>();
	private int pageNum = 0; //��ǰ���ص��ڼ�ҳ
	private boolean haveData=true,isRefresh=false;//�Ƿ�������
	private _User currentUser;
	
	private PullToRefreshListView refreshList; //pullListView
	private ListView commentList;	//�����б�
	private CommentAdapter mAdapter;
	
	private ArrayList<Resource> mListItems = new ArrayList<Resource>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); //ȡ��Ĭ��top bar
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.course); //�󶨵�ǰlayoutΪactivity_main.xml
		Vitamio.isInitialized(getApplicationContext());
		mContext = this;
		aCourse = (Resource) getIntent().getSerializableExtra("data");
		//��ʼ�����
		initView();
		//��ʼ����Ƶ������������
		initData();
		//����������
		commentList.setAdapter(mAdapter);
		//����pullList���Լ��¼�
		setListInfo();
		//���ý������ˢ��,��������
		refreshList.setRefreshing();
		//���ø����������������1��
		QiangYuDao dao = new QiangYuDao();
		dao.incrementLook(context, aCourse);
	}

	private void initData() {
		// TODO Auto-generated method stub
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		context = this;
		
		//������ҳ�¼�
		img_returnHome = (ImageView) findViewById(R.id.id_img_returnHome);
		img_returnHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				 Intent intent = new Intent();
	             intent.putExtra("result", " ");
	             RecommendActivity.this.setResult(RESULT_OK, intent);
	             RecommendActivity.this.finish();
			}
		});
		
		//����Ƶ��������ť
		tv_MUname = (TextView) findViewById(R.id.id_tv_courseName);
		mVideoView = (VideoView) findViewById(R.id.id_videoV_course);
		pb = (ProgressBar) findViewById(R.id.probar);
		downloadRateView = (TextView) findViewById(R.id.download_rate);
		loadRateView = (TextView) findViewById(R.id.load_rate);
		
		tv_courseViewCount = (TextView) findViewById(R.id.id_tv_courseViewcount);
		tv_courseCollectCount = (TextView) findViewById(R.id.id_tv_courseCollectcount);
		img_courseCollectCount = (ImageView) findViewById(R.id.id_img_courseCollectcount);
		img_courseCollectCount.setOnClickListener(this);
		img_courseReport = (ImageView) findViewById(R.id.id_img_courseReport);
		img_courseReport.setOnClickListener(this);
		comment=(RelativeLayout)findViewById(R.id.comment);
		comment.setOnClickListener(this);
		tv_MUname.setText(aCourse.getResName());
		//��ʼ������
		Uri uri = Uri.parse(aCourse.getResUrl()); 
		mVideoView.setVideoURI(uri);
		FrameLayout fl = (FrameLayout) findViewById(R.id.video_fl);
		MediaController mc = new MediaController(this,true,fl);
		mc.setVisibility(View.GONE);
		mVideoView.setMediaController(mc);
		mVideoView.requestFocus();
		mVideoView.setOnInfoListener(this);
		mVideoView.setOnBufferingUpdateListener(this);
		mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mediaPlayer) {
				// optional need Vitamio 4.0
				mediaPlayer.setPlaybackSpeed(1.0f);
			}
		});
		
		tv_courseViewCount.setText(aCourse.getLookNumber().toString());
		tv_courseCollectCount.setText(aCourse.getCollectNum().toString());
		currentUser = BmobUser.getCurrentUser(context, _User.class);
		//�õ����
		refreshList = (PullToRefreshListView) findViewById(R.id.comment_list);
		commentList = refreshList.getRefreshableView();
		mAdapter = new CommentAdapter(context, comments);
	}
	
	private void fetchComment() {
		if(!haveData){
			return;
		}
		BmobQuery<Comment> query = new BmobQuery<Comment>();
		query.addWhereRelatedTo("comments", new BmobPointer(aCourse));
		query.include("user.rank,parent.user");
		query.order("createdAt");
		query.setLimit(Constant.NUMBERS_PER_PAGE);
		query.setSkip(Constant.NUMBERS_PER_PAGE*(pageNum++));
		query.findObjects(this, new FindListener<Comment>() {
			@Override
			public void onSuccess(List<Comment> data) {
				if(data.size()!=0&&data.get(data.size()-1)!=null){
					if(isRefresh){
						comments.clear();
					}
					comments.addAll(data);
					mAdapter.notifyDataSetChanged();
					//û�и���������
					if(data.size()<Constant.NUMBERS_PER_PAGE){
						haveData = false;
					}
				}else{
					ToastFactory.showToast(context, "��û������,�췢���һ����");
					haveData = false;
				}
				refreshList.onRefreshComplete();
			}
			@Override
			public void onError(int arg0, String arg1) {
				refreshList.onRefreshComplete();
				ToastFactory.showToast(context, "��������ʧ�ܣ�"+arg1);
			}
		});
	}

	//�����б����Լ��¼�����
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
				fetchComment();
			}
			//�������ظ���
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				if(haveData){
					isRefresh = false;
					fetchComment();
				}else{
					refreshList.postDelayed(new Runnable() {
			            @Override
			            public void run() {
			            	ToastFactory.showToast(context, "�Ѽ�������������");
			            	refreshList.onRefreshComplete();
			            }
			        }, 500);
				}
			}
		});
		//�б����¼�
		commentList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {

                if (currentUser == null) {
                    String text = "���ȵ�¼";
                    Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                } else {
                    if (position == 0) {
                        return;
                    }

                    currentUser = BmobUser.getCurrentUser(context, _User.class);

                    String userId = comments.get(position - 1).getUser()
                            .getObjectId();
                    if (userId.equals(currentUser.getObjectId())) {
                        showDialog(comments.get(position - 1), new String[] {
                                "��������", "�ظ�", "ɾ������" });
                    } else {
                        showDialog(comments.get(position - 1), new String[] {
                                "��������", "�ظ�" });
                    }
                }
            }
		});
	}	
		//��ʾ�б����ĶԻ���
		private void showDialog(final Comment comment,final String[] stringItems){
	        final ActionSheetDialog menuDialog = new ActionSheetDialog(context, stringItems, null);
	        menuDialog.setOnOperItemClickL(new OnOperItemClickL() {
				@Override
				public void onOperItemClick(AdapterView<?> parent, View view, int position,
						long id) {
					switch (position) {
					case 0:
						if(comment==null){
//							StringUtils.setClip(mContext, qiangYu.getContent());
						}else{
//							StringUtils.setClip(mContext, comment.getCommentContent());
						}
						break;
					case 1:
						if(stringItems.length>2){
							ToastFactory.showToast(context, "���ܻظ��Լ�");
						}else{
							//��ת���߼��ظ�����
							Intent intent = new Intent();
							
							if(BmobUser.getCurrentUser(context)==null){
								intent.setClass(context, LoginActivity.class);
							}else{
								intent.setClass(context, aCoursePublishCommentActivity.class);
								intent.putExtra("qiangYu", aCourse);
								intent.putExtra("parentComment", comment);
							}
							context.startActivity(intent);
							break;
						}
						break;
					case 2:
						CommentDao dao = new CommentDao();
						dao.deleteComment(context, comment, aCourse, mAdapter);
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
		 * ��������ĵ���¼�
		 */
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.id_img_courseReport:
			    if (currentUser ==  null) {
				Intent intent=new Intent(context, LoginActivity.class);
			    startActivity(intent);
			   } else {
				Resource.incrementReportNum(context, aCourse);
				ToastFactory.showToast(context, "�ٱ�");
			   }
			   break;
			case R.id.id_img_courseCollectcount:
				if (currentUser ==  null) {
					Intent intent=new Intent(context, LoginActivity.class);
				    startActivity(intent);
				} else {
					Resource.incrementCollectNum(context, aCourse);
					Collect c = new Collect(aCourse,currentUser);
					c.save(context, new SaveListener() {
						@Override
						public void onSuccess() {
							tv_courseCollectCount.setText(aCourse.getCollectNum()+1+"");
							ToastFactory.showToast(context, "�ղسɹ�");
						}
						@Override
						public void onFailure(int arg0, String arg1) {
							Log.e("-------", "�ղ�ʧ�ܣ�");
						}
					});
				}
			break;
			case R.id.comment:
				haveData = true;
				//��ת���߼��ظ�����
				Intent intent = new Intent();
				if(BmobUser.getCurrentUser(context)==null){
					intent.setClass(context, LoginActivity.class);
				}else{
					intent.setClass(context, aCoursePublishCommentActivity.class);
					intent.putExtra("qiangYu", aCourse);
				}
				context.startActivity(intent);
				break;
			default: break;
			}
		}

		@Override
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			loadRateView.setText(percent + "%");
		}

		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			switch (what) {
		    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
		      if (mVideoView.isPlaying()) {
		        mVideoView.pause();
		        pb.setVisibility(View.VISIBLE);
		        downloadRateView.setText("");
		        loadRateView.setText("");
		        downloadRateView.setVisibility(View.VISIBLE);
		        loadRateView.setVisibility(View.VISIBLE);

		      }
		      break;
		    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
		      mVideoView.start();
		      pb.setVisibility(View.GONE);
		      downloadRateView.setVisibility(View.GONE);
		      loadRateView.setVisibility(View.GONE);
		      break;
		    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
		      downloadRateView.setText("" + extra + "kb/s" + "  ");
		      break;
		    }
		    return true;
		}
	
}