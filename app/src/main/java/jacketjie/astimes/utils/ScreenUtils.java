package jacketjie.astimes.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;


/**
 * 获得屏幕相关的辅助类
 * 
 * @author zhy
 * 
 */
public class ScreenUtils {
	private ScreenUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 获得屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	/**
	 * 获得屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}

	/**
	 * 获得状态栏的高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusHeight(Context context) {

		int statusHeight = -1;
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height")
					.get(object).toString());
			statusHeight = context.getResources().getDimensionPixelSize(height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusHeight;
	}

	public static int getStatusHeight(Activity activity) {
		int statusHeight = 0;
		Rect localRect = new Rect();
		activity.getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass
						.getField("status_bar_height").get(localObject)
						.toString());
				statusHeight = activity.getResources()
						.getDimensionPixelSize(i5);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		return statusHeight;
	}

	/**
	 * 获取当前屏幕截图，包含状态栏
	 * 
	 * @param activity
	 * @return
	 */
	public static Bitmap snapShotWithStatusBar(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		int width = getScreenWidth(activity);
		int height = getScreenHeight(activity);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
		view.destroyDrawingCache();
		return bp;

	}

	/**
	 * 获取当前屏幕截图，不包含状态栏
	 * 
	 * @param activity
	 * @return
	 */
	public static Bitmap snapShotWithoutStatusBar(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		int width = getScreenWidth(activity);
		int height = getScreenHeight(activity);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
				- statusBarHeight);
		view.destroyDrawingCache();
		return bp;

	}

	/**
	 * 获取设备dpi
	 * 
	 * @param context
	 * @return
	 */
	public static int getDensityDpi(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.densityDpi;
	}

	/**
	 * 获取组件的x，y坐标
	 * 
	 * @param view
	 * @return
	 */
	public static int[] messurePosition(View view) {
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		return location;
	}

	/**
	 * 动态设置ListView的高度 只适用于自不觉为linearlayout的listview
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null)
			return;
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);

	}

	/**
	 * 获取listview的高度
	 * 
	 * @param listView
	 * @return
	 */
	public static int getListViewHeight(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();

		if (listAdapter == null) {
			return 0;

		}

		int totalHeight = 0;

		for (int i = 0; i < listAdapter.getCount(); i++) {

			View listItem = listAdapter.getView(i, null, listView);

			listItem.measure(0, 0);

			totalHeight += listItem.getMeasuredHeight();

		}

		return totalHeight;
	}

	/**
	 * 设置滤镜
	 * 
	 * @param view
	 * @param color
	 */
	public static void setFilter(View view, int color) {
		// 先获取设置的src图片
		ImageView image = null;
		Drawable drawable = null;
		if (view instanceof ImageView) {
			image = (ImageView) view;
		}
		if (image != null) {
			drawable = image.getDrawable();
		}
		// 当src图片为Null，获取背景图片
		if (drawable == null) {
			drawable = view.getBackground();
		}

		if (drawable != null) {
			// 设置滤镜
			drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
		}
	}

	/**
	 * 清除滤镜
	 * 
	 * @param view
	 */
	public static void removeFilter(View view) {
		ImageView image = null;
		Drawable drawable = null;
		if (view instanceof ImageView) {
			image = (ImageView) view;
		}
		// 先获取设置的src图片
		if (image != null) {
			drawable = image.getDrawable();
		}
		// 当src图片为Null，获取背景图片
		if (drawable == null) {
			drawable = view.getBackground();
		}
		if (drawable != null) {
			// 清除滤镜
			drawable.clearColorFilter();
		}
	}

	/**
	 * dp转px
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int dp2px(Context context,int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
	}
}
