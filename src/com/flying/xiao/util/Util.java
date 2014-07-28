package com.flying.xiao.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Util
{
public static final float DENSITY=Resources.getSystem().getDisplayMetrics().density;
public static final String APP_ID="1101686352";
private static final String TAG = "Xiao.Util";
/**
 * ����һ����������(String)��ȡbitmapͼ��
 * 
 * @param imageUri
 * @return
 * @throws MalformedURLException
 */
public static Bitmap getbitmap(String imageUri) {
	Log.v(TAG, "getbitmap:" + imageUri);
	// ��ʾ�����ϵ�ͼƬ
	Bitmap bitmap = null;
	try {
		URL myFileUrl = new URL(imageUri);
		HttpURLConnection conn = (HttpURLConnection) myFileUrl
				.openConnection();
		conn.setDoInput(true);
		conn.connect();
		InputStream is = conn.getInputStream();
		bitmap = BitmapFactory.decodeStream(is);
		is.close();

		Log.v(TAG, "image download finished." + imageUri);
	} catch (IOException e) {
		e.printStackTrace();
		Log.v(TAG, "getbitmap bmp fail---");
		return null;
	}
	return bitmap;
}
}
