package androidx.apppickerview.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RestrictTo;
import androidx.apppickerview.widget.AppPickerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Credits to Samsung, all rights reserved to the original owners.

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP_PREFIX})
public class DataManager {
    private static final Uri APP_LIST_PROVIDER_CONTENT_URI = Uri.parse("content://com.samsung.android.settings.applist");
    private static final String KEY_APP_LABEL = "app_title";
    private static final String KEY_PACKAGE_NAME = "package_name";
    private static final int MAX_APP_LIST_COUNT = 10000;
    private static final String TAG = "DataManager";
    private static final boolean sIsSupportQUERY;
    private static final boolean sIsSupportSCS;

    static {
        int i = Build.VERSION.SDK_INT;
        boolean z = true;
        sIsSupportSCS = i > 29;
        if (i < 26) {
            z = false;
        }
        sIsSupportQUERY = z;
    }

    private static String getLabelFromPackageManager(Context context, ComponentName componentName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ActivityInfo activityInfo = packageManager.getActivityInfo(componentName, 0);
            return activityInfo != null ? activityInfo.loadLabel(packageManager).toString() : "Unknown";
        } catch (PackageManager.NameNotFoundException unused) {
            Log.i(TAG, "can't find label for " + componentName);
            return "Unknown";
        }
    }

    private static String getLabelFromPackageManager(Context context, String str) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, 0);
            return applicationInfo != null ? (String) packageManager.getApplicationLabel(applicationInfo) : "Unknown";
        } catch (PackageManager.NameNotFoundException unused) {
            Log.i(TAG, "can't find label for " + str);
            return "Unknown";
        }
    }

    private static HashMap<String, String> loadLabelFromSettings(Context context) {
        Cursor query = context.getContentResolver().query(APP_LIST_PROVIDER_CONTENT_URI, null, null, null, null);
        HashMap<String, String> hashMap = new HashMap<>();
        if (query != null && query.moveToFirst()) {
            do {
                hashMap.put(query.getString(query.getColumnIndex(KEY_PACKAGE_NAME)), query.getString(query.getColumnIndex(KEY_APP_LABEL)));
            } while (query.moveToNext());
            query.close();
        }
        return hashMap;
    }

    public static List<AppPickerView.AppLabelInfo> resetPackages(Context context, List<String> list) {
        return resetPackages(context, list, null, null);
    }

    public static List<AppPickerView.AppLabelInfo> resetPackages(Context context, List<String> list, List<AppPickerView.AppLabelInfo> list2, List<ComponentName> list3) {
        HashMap hashMap;
        boolean z = list3 != null;
        HashMap<String, String> labelFromSCS = sIsSupportQUERY ? getLabelFromSCS(context, z) : z ? null : loadLabelFromSettings(context);
        if (list2 != null) {
            hashMap = new HashMap();
            for (AppPickerView.AppLabelInfo appLabelInfo : list2) {
                String packageName = appLabelInfo.getPackageName();
                if (appLabelInfo.getActivityName() != null && !appLabelInfo.getActivityName().equals("")) {
                    packageName = packageName + "/" + appLabelInfo.getActivityName();
                }
                hashMap.put(packageName, appLabelInfo.getLabel());
            }
        } else {
            hashMap = null;
        }
        ArrayList arrayList = new ArrayList();
        if (z) {
            for (ComponentName componentName : list3) {
                String str = componentName.getPackageName() + "/" + componentName.getClassName();
                String str2 = hashMap != null ? (String) hashMap.get(str) : null;
                if (str2 == null && labelFromSCS != null) {
                    str2 = labelFromSCS.get(str);
                }
                if (str2 == null) {
                    str2 = getLabelFromPackageManager(context, componentName);
                }
                arrayList.add(new AppPickerView.AppLabelInfo(componentName.getPackageName(), str2, componentName.getClassName()));
            }
        } else {
            for (String str3 : list) {
                String str4 = hashMap != null ? (String) hashMap.get(str3) : null;
                if (str4 == null && labelFromSCS != null) {
                    str4 = labelFromSCS.get(str3);
                }
                if (str4 == null) {
                    str4 = getLabelFromPackageManager(context, str3);
                }
                arrayList.add(new AppPickerView.AppLabelInfo(str3, str4, ""));
            }
        }
        return arrayList;
    }

    public static List<AppPickerView.AppLabelInfo> resetPackages(List<ComponentName> list, Context context) {
        return resetPackages(context, null, null, list);
    }
}
