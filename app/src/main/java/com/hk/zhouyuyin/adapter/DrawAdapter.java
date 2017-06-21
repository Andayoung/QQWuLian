package com.hk.zhouyuyin.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hk.zhouyuyin.MyApplication;
import com.hk.zhouyuyin.PublicWebActivity;
import com.tencent.devicedemo.R;
import com.hk.zhouyuyin.modle.Message;

public class DrawAdapter extends BaseAdapter {

    private Context context;
    private List<Message> draws;

    public DrawAdapter(Context context, List<Message> draws) {
        super();
        this.context = context;
        this.draws = draws;
    }

    /*
     * 返回条目视图的数量，�??般就是数据的数量
     */
    @Override
    public int getCount() {
        return draws.size();
    }

    /*
     * 通常在用户触摸position位置条目时，调用此方法获取数�??
     */
    @Override
    public Object getItem(int position) {
        return draws.get(position);
    }

    /*
     * 通常在用户触摸position位置条目时，调用此方法获取数据id
     * 多数情况下可以直接返回position下标，或者也可以返回数据的id，比如数据库中主键id
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_layout,
                    null); // 充气�??
            vh = new ViewHolder();
            vh.llLeftOrRight = (LinearLayout) convertView
                    .findViewById(R.id.ll_left_or_right);
            vh.llBg = (LinearLayout) convertView.findViewById(R.id.ll_bg);
            vh.imgUU = (ImageView) convertView.findViewById(R.id.img_uu_head);
            vh.imgUser = (ImageView) convertView.findViewById(R.id.img_send_head);
            vh.tvMessage = (TextView) convertView.findViewById(R.id.tv_list_item);

            convertView.setTag(vh);// 把vh绑定到视图上
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        if (draws.get(position).isLeft()) {
            if (draws.get(position).getVoideUrl() != null) {
                vh.imgUU.getLayoutParams().width = MyApplication.screenWidth / 8;
                vh.imgUU.getLayoutParams().height = MyApplication.screenWidth / 8;
                vh.imgUser.getLayoutParams().width = MyApplication.screenWidth / 8;
                vh.imgUser.getLayoutParams().height = MyApplication.screenWidth / 8;
                vh.tvMessage.setText("");
                vh.llLeftOrRight.setGravity(Gravity.LEFT);
                vh.llBg.setBackgroundResource(R.drawable.shiping);
                vh.imgUU.setVisibility(View.VISIBLE);
                vh.imgUser.setVisibility(View.INVISIBLE);
                vh.llBg.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        toPublicWebActivty(draws.get(position).getVoideUrl());
                    }
                });
            } else {
                vh.imgUU.getLayoutParams().width = MyApplication.screenWidth / 8;
                vh.imgUU.getLayoutParams().height = MyApplication.screenWidth / 8;
                vh.imgUser.getLayoutParams().width = MyApplication.screenWidth / 8;
                vh.imgUser.getLayoutParams().height = MyApplication.screenWidth / 8;
                vh.tvMessage.setText(draws.get(position).getStrMessage());
                vh.llLeftOrRight.setGravity(Gravity.LEFT);
                vh.llBg.setBackgroundResource(R.drawable.ceshi_2);
                vh.imgUU.setVisibility(View.VISIBLE);
                vh.imgUser.setVisibility(View.INVISIBLE);
            }
        } else {
            vh.imgUU.getLayoutParams().width = MyApplication.screenWidth / 8;
            vh.imgUU.getLayoutParams().height = MyApplication.screenWidth / 8;
            vh.imgUser.getLayoutParams().width = MyApplication.screenWidth / 8;
            vh.imgUser.getLayoutParams().height = MyApplication.screenWidth / 8;
            vh.tvMessage.setText(draws.get(position).getStrMessage());
            vh.llLeftOrRight.setGravity(Gravity.RIGHT);
            vh.llBg.setBackgroundResource(R.drawable.ceshi_1);
            vh.imgUU.setVisibility(View.INVISIBLE);
            vh.imgUser.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private void toPublicWebActivty(String url) {
        Intent intentSimple = new Intent();
        intentSimple.setClass(context, PublicWebActivity.class);
        Bundle bundleSimple = new Bundle();
        bundleSimple.putString("webUrl", url);
        bundleSimple.putString("title", "视频");
        intentSimple.putExtras(bundleSimple);
        context.startActivity(intentSimple);
    }

    /**
     * 持有视图
     *
     * @author Administrator 惯用类名
     */
    class ViewHolder {
        LinearLayout llLeftOrRight;
        LinearLayout llBg;
        TextView tvMessage;
        ImageView imgUU;
        ImageView imgUser;
    }

}
