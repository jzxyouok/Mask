package com.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * �б��������Ļ���������
 */
public abstract class BaseContentAdapter<T> extends BaseAdapter {

	protected Context mContext; // �����Ķ���
	protected List<T> dataList; // ���ݼ���
	protected LayoutInflater mInflater; // ���ز��ֵĶ���

	/**
	 * ���췽��,��ʼ�������Ķ�������ݼ���
	 */
	public BaseContentAdapter(Context context, List<T> list) {
		mContext = context;
		dataList = list;
		// �õ����ز��ֶ���ķ���
		mInflater = LayoutInflater.from(mContext);
	}

	/*
	 * �õ����ݼ���
	 */
	public List<T> getDataList() {
		return dataList;
	}

	/*
	 * �������ݼ���
	 */
	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public T getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getConvertView(position, convertView, parent);
	}
	/**
	 * �õ�ÿһ��ֵĳ��󷽷�,������ʵ��
	 */
	public abstract View getConvertView(int position, View convertView,
			ViewGroup parent);

}
