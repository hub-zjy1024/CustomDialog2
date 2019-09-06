package com.zjy.js.customdialog.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 Created by 张建宇 on 2018/8/21. */
class BitmapUtil {
    public static Bitmap bytes2Bitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static Bitmap compressBitmapBySampleSize(Bitmap bitmap, int i) {
        byte[] oldData = bitmap2Bytes(bitmap);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = i;
        Bitmap newBitmap = BitmapFactory.decodeByteArray(oldData, 0, oldData.length, options);
        return newBitmap;
    }

    public static byte[] bitmap2Bytes(Bitmap bitmapAfter) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmapAfter.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        return bao.toByteArray();
    }
}
