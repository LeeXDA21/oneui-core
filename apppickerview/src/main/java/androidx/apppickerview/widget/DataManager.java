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

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP_PREFIX})
/* loaded from: C:\Users\LeeXD\Documents\sesl5port\sesl5.dex */
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

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0094, code lost:
        if (r1 != null) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x009d, code lost:
        if (r1 == null) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x009f, code lost:
        r1.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x00a2, code lost:
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.util.HashMap<java.lang.String, java.lang.String> getLabelFromSCS(android.content.Context r6, boolean r7) {
        /*
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            r1 = 0
            boolean r2 = androidx.apppickerview.widget.DataManager.sIsSupportSCS     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            if (r2 == 0) goto Ld
            java.lang.String r2 = "com.samsung.android.scs.ai.search/v1"
            goto Lf
        Ld:
            java.lang.String r2 = "com.samsung.android.bixby.service.bixbysearch/v1"
        Lf:
            java.lang.String r3 = "*"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            r4.<init>()     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            java.lang.String r5 = "content://"
            r4.append(r5)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            r4.append(r2)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            java.lang.String r2 = r4.toString()     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            android.net.Uri r2 = android.net.Uri.parse(r2)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            java.lang.String r4 = "application"
            android.net.Uri r2 = android.net.Uri.withAppendedPath(r2, r4)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            android.os.Bundle r4 = new android.os.Bundle     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            r4.<init>()     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            java.lang.String r5 = "android:query-arg-sql-selection"
            r4.putString(r5, r3)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            java.lang.String r3 = "query-arg-all-apps"
            r5 = 1
            r4.putBoolean(r3, r5)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            java.lang.String r3 = "android:query-arg-limit"
            r5 = 10000(0x2710, float:1.4013E-41)
            r4.putInt(r3, r5)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            android.content.ContentResolver r6 = r6.getContentResolver()     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            android.database.Cursor r1 = r6.query(r2, r1, r4, r1)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            if (r1 == 0) goto L94
        L4d:
            boolean r6 = r1.moveToNext()     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            if (r6 == 0) goto L94
            java.lang.String r6 = "label"
            int r6 = r1.getColumnIndex(r6)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            java.lang.String r6 = r1.getString(r6)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            java.lang.String r2 = "packageName"
            if (r7 == 0) goto L88
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            r3.<init>()     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            java.lang.String r4 = "componentName"
            int r4 = r1.getColumnIndex(r4)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            java.lang.String r4 = r1.getString(r4)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            r3.append(r4)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            java.lang.String r4 = "/"
            r3.append(r4)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            int r2 = r1.getColumnIndex(r2)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            java.lang.String r2 = r1.getString(r2)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            r3.append(r2)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            java.lang.String r2 = r3.toString()     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            goto L90
        L88:
            int r2 = r1.getColumnIndex(r2)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            java.lang.String r2 = r1.getString(r2)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
        L90:
            r0.put(r2, r6)     // Catch: java.lang.Throwable -> L97 java.lang.Exception -> L99
            goto L4d
        L94:
            if (r1 == 0) goto La2
            goto L9f
        L97:
            r6 = move-exception
            goto La3
        L99:
            r6 = move-exception
            r6.printStackTrace()     // Catch: java.lang.Throwable -> L97
            if (r1 == 0) goto La2
        L9f:
            r1.close()
        La2:
            return r0
        La3:
            if (r1 == 0) goto La8
            r1.close()
        La8:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.apppickerview.widget.DataManager.getLabelFromSCS(android.content.Context, boolean):java.util.HashMap");
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
