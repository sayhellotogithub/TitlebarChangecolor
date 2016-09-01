package com.iblogstreet.titlebarchangecolor;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iblogstreet.titlebarchangecolor.view.MyListView;
import com.iblogstreet.titlebarchangecolor.view.ObservableScrollView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity
        extends AppCompatActivity
        implements ObservableScrollView.IScrollViewListener
{
    private static final String TAG = "MainActivity";
    //ScrollView
    ObservableScrollView obserView;
    TextView             tvTitle;
    MyListView           lvData;
    ImageView            image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initEvent();
    }

    int height;

    private void initEvent() {

        ViewTreeObserver viewTreeObserver = image.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                image.getViewTreeObserver()
                     .removeGlobalOnLayoutListener(this);

                height = image.getHeight();
                Log.e(TAG, "height:" + height);
                obserView.setScrollViewListener(MainActivity.this);
            }
        });
    }

    List<String> mData;

    private void initData() {

        mData = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mData.add("This is Test" + i);

        }

        lvData.setAdapter(new MyAdapter());
    }

    class MyAdapter
            extends BaseAdapter
    {

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewholder = null;
            if (convertView == null) {
                viewholder = new ViewHolder();
                convertView = View.inflate(getApplicationContext(), R.layout.item_lv, null);
                viewholder.tvDesc = (TextView) convertView.findViewById(R.id.tvdesc);
                convertView.setTag(viewholder);

            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            viewholder.tvDesc.setText("" + mData.get(position));
            return convertView;
        }

        class ViewHolder {
            TextView tvDesc;
        }
    }

    private void initView() {

        obserView = (ObservableScrollView) findViewById(R.id.scroll);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        lvData = (MyListView) findViewById(R.id.lv_data);
        image = (ImageView) findViewById(R.id.iv);
    }

    @Override
    public void onScrollChanged(int x, int y, int oldx, int oldy) {
        if (y <= 0) {
            Log.e(TAG, "Top");
            tvTitle.setBackgroundColor(Color.argb((int) 0, 227, 29, 26));//AGB由相关工具获得，或
        } else if (y > 0 && y < height) {
            Log.e(TAG, "middle");
            float scale = (float) y / height;
            float alpha = (255 * scale);
            Log.e(TAG, "alpha:" + alpha);
            // 只是layout背景透明(仿知乎滑动效果)
            tvTitle.setBackgroundColor(Color.argb((int) alpha, 227, 29, 26));
        } else {
            Log.e(TAG, "Bottom");
            tvTitle.setBackgroundColor(Color.argb((int) 255, 227, 29, 26));
        }
    }
}
