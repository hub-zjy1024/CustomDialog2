package com.zjy.js.customdialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张建宇 on 2017/6/14.
 */

public class FilePicker {
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void pickWordFile(Context context, int REQUEST_WORD_CODE) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/msword");       // .doc .dot等word文件
//        intent.setType("application/vnd.ms-excel");       // .xls .xla .xlt 等 Excel 文件
        intent.setType("image/*");        //所有图片文件
        //intent.setType("image/jpeg");      // jpeg文件，类推。
        ((Activity) context).startActivityForResult(intent, REQUEST_WORD_CODE);
    }
    public static void pickWord(Context context, int REQUEST_WORD_CODE) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/msword");       // .doc .dot等word文件
//        intent.setType("application/vnd.ms-excel");       // .xls .xla .xlt 等 Excel 文件
//        intent.setType("image/*");        //所有图片文件
        //intent.setType("image/jpeg");      // jpeg文件，类推。
        ((Activity) context).startActivityForResult(intent, REQUEST_WORD_CODE);
    }

    public static void pickWord(Context context, int REQUEST_WORD_CODE, String type) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(type);       // .doc .dot等word文件
        //        intent.setType("application/vnd.ms-excel");       // .xls .xla .xlt 等
        // Excel 文件
        //        intent.setType("image/*");        //所有图片文件
        //intent.setType("image/jpeg");      // jpeg文件，类推。
        ((Activity) context).startActivityForResult(intent, REQUEST_WORD_CODE);
    }

    /**
     * 以下各类文件路径获取
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf
                                (id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        Log.d("aauu ", "isExternalStorageDocument");
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        Log.d("aauu ", "isDownloadsDocument");
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        Log.d("aauu ", "isMediaDocument");
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        Log.d("aauu ", "isGooglePhotosUri");
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection,
                    selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    public static List<String> getDataColumn(Context context, Uri uri) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        List<String> arrayList = new ArrayList<>();
        try {
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                String path = cursor.getString(index);
                Log.e("zjy", "FilePicker->getDataColumn(): path==" + path);
                arrayList.add(path);
            }
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null)
                cursor.close();
        }
        return arrayList;
    }

}
