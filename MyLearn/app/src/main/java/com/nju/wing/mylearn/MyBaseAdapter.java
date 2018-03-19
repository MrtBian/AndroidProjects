package com.nju.wing.mylearn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Wing on 2017/7/24.
 */

public class MyBaseAdapter extends BaseAdapter{

    private List<ItemWord> mList;
    private LayoutInflater mInflater;

    // 通过构造器关联数据源与数据适配器
    public MyBaseAdapter(Context context, List<ItemWord> list){
        mList = list;
        // 使用当前要使用的界面对象context去初始化布局装载器对象mInflater
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId (int i) {
        return i;
    }



   /* *//**
     * 返回每一项对应的内容
     * 缺点：没有利用到ListView的缓存机制
     *      每次都会创建新的View，不管当前这个Item是否在屏幕上被调用过(即是否被缓存过)
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_word,null);
            viewHolder.word = convertView.findViewById(R.id.item_word);
            viewHolder.grade = convertView.findViewById(R.id.item_grade);
            viewHolder.times = convertView.findViewById(R.id.item_occur);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.word.setText(mList.get(position).getWord());
        viewHolder.grade.setText(mList.get(position).getGrade()+"");
        viewHolder.times.setText(mList.get(position).getOccurtime()+"");
        return convertView;
        /*// 将布局文件转为View对象
        View view = mInflater.inflate(R.layout.item_word, null);
        TextView word = (TextView) view.findViewById(R.id.item_word);
        TextView grade = (TextView) view.findViewById(R.id.item_grade);
        TextView occur = (TextView) view.findViewById(R.id.item_occur);
        ItemWord bean = mList.get(position);
        word.setText(bean.getWord());
        grade.setText(bean.getGrade()+"");
        occur.setText(bean.getOccurtime()+"");
        return view;*/
    }

    // 避免重复的findViewById的操作
    class ViewHolder{
        public TextView word;
        public TextView grade;
        public TextView times;
    }
}