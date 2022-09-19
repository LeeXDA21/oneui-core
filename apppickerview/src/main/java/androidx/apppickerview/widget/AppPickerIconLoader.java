package androidx.apppickerview.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ImageView;
import androidx.reflect.app.SeslApplicationPackageManagerReflector;

/* loaded from: C:\Users\LeeXD\Documents\sesl5port\sesl5.dex */
public class AppPickerIconLoader {
    private static final String THREAD_NAME = "AppPickerIconLoader";
    private Context mContext;
    private LoadIconTask mLoadIconTask;
    private PackageManager mPackageManager;

    /* loaded from: C:\Users\LeeXD\Documents\sesl5port\sesl5.dex */
    public static class IconInfo {
        public String activityName;
        public Drawable drawable = null;
        public ImageView imageView;
        public String packageName;

        public IconInfo(String str, String str2, ImageView imageView) {
            this.packageName = str;
            this.imageView = imageView;
            this.activityName = str2;
        }
    }

    /* loaded from: C:\Users\LeeXD\Documents\sesl5port\sesl5.dex */
    public class LoadIconTask extends AsyncTask<Void, Void, Drawable> {
        private final IconInfo mIconInfo;

        public LoadIconTask(IconInfo iconInfo) {
            this.mIconInfo = iconInfo;
        }

        @Override // android.os.AsyncTask
        public Drawable doInBackground(Void... voidArr) {
            AppPickerIconLoader appPickerIconLoader = AppPickerIconLoader.this;
            IconInfo iconInfo = this.mIconInfo;
            return appPickerIconLoader.getAppIcon(iconInfo.packageName, iconInfo.activityName);
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Drawable drawable) {
            ImageView imageView;
            IconInfo iconInfo = this.mIconInfo;
            if (iconInfo == null || (imageView = iconInfo.imageView) == null || drawable == null) {
                return;
            }
            imageView.setImageDrawable(drawable);
        }
    }

    public AppPickerIconLoader(Context context) {
        this.mContext = context;
        this.mPackageManager = context.getPackageManager();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Drawable getAppIcon(String str, String str2) {
        if (str2 == null || str2.equals("")) {
            Drawable semGetApplicationIconForIconTray = SeslApplicationPackageManagerReflector.semGetApplicationIconForIconTray(this.mPackageManager, str, 1);
            if (semGetApplicationIconForIconTray == null) {
                try {
                    return this.mPackageManager.getApplicationIcon(str);
                } catch (PackageManager.NameNotFoundException unused) {
                }
            }
            return semGetApplicationIconForIconTray;
        }
        ComponentName componentName = new ComponentName(str, str2);
        Drawable semGetActivityIconForIconTray = SeslApplicationPackageManagerReflector.semGetActivityIconForIconTray(this.mPackageManager, componentName, 1);
        if (semGetActivityIconForIconTray != null) {
            return semGetActivityIconForIconTray;
        }
        try {
            return this.mPackageManager.getActivityIcon(componentName);
        } catch (PackageManager.NameNotFoundException unused2) {
            return semGetActivityIconForIconTray;
        }
    }

    public void loadIcon(String str, String str2, ImageView imageView) {
        if (TextUtils.isEmpty(str) || imageView == null) {
            return;
        }
        imageView.setTag(str);
        new LoadIconTask(new IconInfo(str, str2, imageView)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public void startIconLoaderThread() {
    }

    public void stopIconLoaderThread() {
    }
}
