package com.hk.uu.imageload;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImageSaveMemory {

	/**
	 * 浠ユ渶鐪佸唴瀛樼殑鏂瑰紡璇诲彇鏈�?湴璧勬簮鐨勫浘鐗�?
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 鑾峰彇璧勬簮鍥剧�?
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}
	
	/**
	 * 浠ユ渶鐪佸唴瀛樼殑鏂瑰紡璇诲彇鏈�?湴璧勬簮鐨勫浘鐗囪浆鎹㈡垚Drawable
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public Drawable readDrawable(Context context, int resId) {
		Drawable drawable = new BitmapDrawable(readBitMap(context,
				resId));
		return drawable;
	}
}
