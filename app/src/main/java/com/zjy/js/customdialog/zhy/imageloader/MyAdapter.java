package com.zjy.js.customdialog.zhy.imageloader;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.zjy.js.customdialog.zhy.utils.CommonAdapter;
import com.zjy.js.customdialog.zhy.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends CommonAdapter<String> {

    /**
     用户选择的图片，存储为图片的完整路径
     */
    public static ArrayList<String> mSelectedImage = new ArrayList<String>();

    /**
     文件夹路径
     */
    private String mDirPath;

    public MyAdapter(Context context, List<String> mDatas, int itemLayoutId,
                     String dirPath) {
        super(context, mDatas, itemLayoutId);
        this.mDirPath = dirPath;
    }


    @Override
    public void convert(ViewHolder helper, final String item) {
        //设置no_pic
        helper.setImageResource(com.zjy.js.customdialog.R.id.id_item_image, com.zjy.js.customdialog.R.drawable.pictures_no);
        //设置no_selected
        helper.setImageResource(com.zjy.js.customdialog.R.id.id_item_select,
                com.zjy.js.customdialog.R.drawable.picture_unselected);
        //设置图片
        helper.setImageByUrl(com.zjy.js.customdialog.R.id.id_item_image, mDirPath + "/" + item);
        final ImageView mImageView = helper.getView(com.zjy.js.customdialog.R.id.id_item_image);
//        File file = new File(mDirPath + "/" + item);
//        Picasso.with(mContext).load(file).resize(100,100);
        final ImageView mSelect = helper.getView(com.zjy.js.customdialog.R.id.id_item_select);
        mImageView.setColorFilter(null);
        //设置ImageView的点击事件
        mImageView.setOnClickListener(new OnClickListener() {
            //选择，则将图片变暗，反之则反之
            @Override
            public void onClick(View v) {
                // 已经选择过该图片
                if (mSelectedImage.contains(mDirPath + "/" + item)) {
                    mSelectedImage.remove(mDirPath + "/" + item);
                    Log.e("zjy", "MyAdapter->onClick(): select img==" + item);
                    mSelect.setImageResource(com.zjy.js.customdialog.R.drawable.picture_unselected);
                    mImageView.setColorFilter(null);
                } else
                // 未选择该图片
                {
                    mSelectedImage.add(mDirPath + "/" + item);
                    mSelect.setImageResource(com.zjy.js.customdialog.R.drawable.pictures_selected);
                    mImageView.setColorFilter(Color.parseColor("#77000000"));
                }

            }
        });

        /**
         * 已经选择过的图片，显示出选择过的效果
         */
        if (mSelectedImage.contains(mDirPath + "/" + item)) {
            mSelect.setImageResource(com.zjy.js.customdialog.R.drawable.pictures_selected);
            mImageView.setColorFilter(Color.parseColor("#77000000"));
        } else {
            mSelect.setImageResource(com.zjy.js.customdialog.R.drawable.picture_unselected);
            mImageView.setColorFilter(null);
        }

    }
}
