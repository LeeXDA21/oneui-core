package androidx.apppickerview.widget;

import android.content.ComponentName;
import android.content.Context;
import android.icu.text.AlphabeticIndex;
import android.os.Build;
import android.os.LocaleList;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SectionIndexer;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.apppickerview.R;
import androidx.apppickerview.widget.AppPickerView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.reflect.text.SeslTextUtilsReflector;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP_PREFIX})
/* loaded from: C:\Users\LeeXD\Documents\sesl5port\sesl5.dex */
public abstract class AbsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable, SectionIndexer {
    private static Comparator<AppPickerView.AppLabelInfo> APP_LABEL_ASCENDING = new Comparator<AppPickerView.AppLabelInfo>() { // from class: androidx.apppickerview.widget.AbsAdapter.1
        @Override // java.util.Comparator
        public int compare(AppPickerView.AppLabelInfo appLabelInfo, AppPickerView.AppLabelInfo appLabelInfo2) {
            Collator collator = Collator.getInstance(Locale.getDefault());
            collator.setStrength(2);
            return collator.compare(appLabelInfo.getLabel(), appLabelInfo2.getLabel());
        }
    };
    private static Comparator<AppPickerView.AppLabelInfo> APP_LABEL_ASCENDING_IGNORE_CASE = new Comparator<AppPickerView.AppLabelInfo>() { // from class: androidx.apppickerview.widget.AbsAdapter.2
        @Override // java.util.Comparator
        public int compare(AppPickerView.AppLabelInfo appLabelInfo, AppPickerView.AppLabelInfo appLabelInfo2) {
            Collator collator = Collator.getInstance(Locale.getDefault());
            collator.setStrength(0);
            return collator.compare(appLabelInfo.getLabel(), appLabelInfo2.getLabel());
        }
    };
    private static Comparator<AppPickerView.AppLabelInfo> APP_LABEL_DESCENDING = new Comparator<AppPickerView.AppLabelInfo>() { // from class: androidx.apppickerview.widget.AbsAdapter.3
        @Override // java.util.Comparator
        public int compare(AppPickerView.AppLabelInfo appLabelInfo, AppPickerView.AppLabelInfo appLabelInfo2) {
            Collator collator = Collator.getInstance(Locale.getDefault());
            collator.setStrength(2);
            return collator.compare(appLabelInfo2.getLabel(), appLabelInfo.getLabel());
        }
    };
    private static Comparator<AppPickerView.AppLabelInfo> APP_LABEL_DESCENDING_IGNORE_CASE = new Comparator<AppPickerView.AppLabelInfo>() { // from class: androidx.apppickerview.widget.AbsAdapter.4
        @Override // java.util.Comparator
        public int compare(AppPickerView.AppLabelInfo appLabelInfo, AppPickerView.AppLabelInfo appLabelInfo2) {
            Collator collator = Collator.getInstance(Locale.getDefault());
            collator.setStrength(0);
            return collator.compare(appLabelInfo2.getLabel(), appLabelInfo.getLabel());
        }
    };
    private static final String TAG = "AppPickerViewAdapter";
    private AppPickerIconLoader mAppPickerIconLoader;
    public Context mContext;
    private int mForegroundColor;
    private AppPickerView.OnBindListener mOnBindListener;
    private AppPickerView.OnSearchFilterListener mOnSearchFilterListener;
    private int mOrder;
    private int[] mPositionToSectionIndex;
    public int mType;
    private final int MAX_OFFSET = 200;
    private List<AppPickerView.AppLabelInfo> mDataSet = new ArrayList();
    private List<AppPickerView.AppLabelInfo> mDataSetFiltered = new ArrayList();
    private Map<String, Integer> mSectionMap = new HashMap();
    private String[] mSections = new String[0];
    private boolean mHideAllApps = false;
    private String mSearchText = "";

    public AbsAdapter(Context context, int i, int i2, AppPickerIconLoader appPickerIconLoader) {
        this.mContext = context;
        this.mType = i;
        this.mOrder = i2;
        this.mAppPickerIconLoader = appPickerIconLoader;
        TypedValue typedValue = new TypedValue();
        this.mContext.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        this.mForegroundColor = typedValue.resourceId != 0 ? this.mContext.getResources().getColor(typedValue.resourceId) : typedValue.data;
    }

    private Comparator<AppPickerView.AppLabelInfo> getAppLabelComparator(int i) {
        if (i != 1) {
            if (i == 2) {
                return APP_LABEL_ASCENDING_IGNORE_CASE;
            }
            if (i == 3) {
                return APP_LABEL_DESCENDING;
            }
            if (i == 4) {
                return APP_LABEL_DESCENDING_IGNORE_CASE;
            }
            return null;
        }
        return APP_LABEL_ASCENDING;
    }

    public static AbsAdapter getAppPickerAdapter(Context context, List<String> list, int i, int i2, List<AppPickerView.AppLabelInfo> list2, AppPickerIconLoader appPickerIconLoader, List<ComponentName> list3) {
        AbsAdapter gridAdapter = i >= 7 ? new GridAdapter(context, i, i2, appPickerIconLoader) : new ListAdapter(context, i, i2, appPickerIconLoader);
        gridAdapter.setHasStableIds(true);
        gridAdapter.resetPackages(list, false, list2, list3);
        return gridAdapter;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshSectionMap() {
        this.mSectionMap.clear();
        ArrayList arrayList = new ArrayList();
        if (Build.VERSION.SDK_INT >= 24) {
            LocaleList locales = this.mContext.getResources().getConfiguration().getLocales();
            if (locales.size() == 0) {
                locales = new LocaleList(Locale.ENGLISH);
            }
            AlphabeticIndex alphabeticIndex = new AlphabeticIndex(locales.get(0));
            int size = locales.size();
            for (int i = 1; i < size; i++) {
                alphabeticIndex.addLabels(locales.get(i));
            }
            alphabeticIndex.addLabels(Locale.ENGLISH);
            AlphabeticIndex.ImmutableIndex buildImmutableIndex = alphabeticIndex.buildImmutableIndex();
            this.mPositionToSectionIndex = new int[this.mDataSetFiltered.size()];
            for (int i2 = 0; i2 < this.mDataSetFiltered.size(); i2++) {
                String label = this.mDataSetFiltered.get(i2).getLabel();
                if (TextUtils.isEmpty(label)) {
                    label = "";
                }
                String label2 = buildImmutableIndex.getBucket(buildImmutableIndex.getBucketIndex(label)).getLabel();
                if (!this.mSectionMap.containsKey(label2)) {
                    arrayList.add(label2);
                    this.mSectionMap.put(label2, Integer.valueOf(i2));
                }
                this.mPositionToSectionIndex[i2] = arrayList.size() - 1;
            }
            String[] strArr = new String[arrayList.size()];
            this.mSections = strArr;
            arrayList.toArray(strArr);
        }
    }

    public void addCustomViewItem(int i, int i2) {
        this.mDataSet.add(i, new AppPickerView.AppLabelInfo(AppPickerView.KEY_CUSTOM_VIEW_ITEM, "", "").setCustomViewItem(true, i2));
        this.mDataSetFiltered.clear();
        this.mDataSetFiltered.addAll(this.mDataSet);
        refreshSectionMap();
        notifyItemInserted(i);
    }

    public void addPackage(int i, String str) {
        this.mDataSet.add(i, new AppPickerView.AppLabelInfo("", str, ""));
        this.mDataSetFiltered.clear();
        this.mDataSetFiltered.addAll(this.mDataSet);
        refreshSectionMap();
        notifyItemInserted(i);
    }

    public void addSeparator(int i) {
        this.mDataSet.add(i, new AppPickerView.AppLabelInfo(AppPickerView.KEY_APP_SEPARATOR, "", "").setSeparator(true));
        this.mDataSetFiltered.clear();
        this.mDataSetFiltered.addAll(this.mDataSet);
        refreshSectionMap();
        notifyItemInserted(i);
    }

    public AppPickerView.AppLabelInfo getAppInfo(int i) {
        return this.mDataSetFiltered.get(i);
    }

    public List<AppPickerView.AppLabelInfo> getDataSet() {
        return this.mDataSet;
    }

    @Override // android.widget.Filterable
    public Filter getFilter() {
        return new Filter() { // from class: androidx.apppickerview.widget.AbsAdapter.5
            @Override // android.widget.Filter
            public Filter.FilterResults performFiltering(CharSequence charSequence) {
                String charSequence2 = charSequence.toString();
                Filter.FilterResults filterResults = new Filter.FilterResults();
                if (charSequence2.isEmpty()) {
                    AbsAdapter.this.mSearchText = "";
                    filterResults.values = AbsAdapter.this.mDataSet;
                } else {
                    AbsAdapter.this.mSearchText = charSequence2;
                    ArrayList arrayList = new ArrayList();
                    for (AppPickerView.AppLabelInfo appLabelInfo : AbsAdapter.this.mDataSet) {
                        if (!AppPickerView.ALL_APPS_STRING.equals(appLabelInfo.getPackageName())) {
                            String label = appLabelInfo.getLabel();
                            if (!TextUtils.isEmpty(label)) {
                                StringTokenizer stringTokenizer = new StringTokenizer(charSequence2.toLowerCase());
                                boolean z = true;
                                String lowerCase = label.toLowerCase();
                                while (true) {
                                    if (!stringTokenizer.hasMoreTokens()) {
                                        break;
                                    } else if (!lowerCase.contains(stringTokenizer.nextToken())) {
                                        z = false;
                                        break;
                                    }
                                }
                                if (z) {
                                    arrayList.add(appLabelInfo);
                                }
                            }
                        }
                    }
                    filterResults.values = arrayList;
                }
                return filterResults;
            }

            @Override // android.widget.Filter
            public void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
                if ("".equals(AbsAdapter.this.mSearchText)) {
                    AbsAdapter.this.mHideAllApps = false;
                } else {
                    AbsAdapter.this.mHideAllApps = true;
                }
                AbsAdapter.this.mDataSetFiltered.clear();
                AbsAdapter.this.mDataSetFiltered.addAll((ArrayList) filterResults.values);
                AbsAdapter.this.refreshSectionMap();
                AbsAdapter.this.notifyDataSetChanged();
                if (AbsAdapter.this.mOnSearchFilterListener != null) {
                    AbsAdapter.this.mOnSearchFilterListener.onSearchFilterCompleted(AbsAdapter.this.getItemCount());
                }
            }
        };
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mDataSetFiltered.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return this.mDataSetFiltered.get(i).hashCode();
    }

    @Override // android.widget.SectionIndexer
    public int getPositionForSection(int i) {
        String[] strArr = this.mSections;
        if (i >= strArr.length) {
            return 0;
        }
        return this.mSectionMap.get(strArr[i]).intValue();
    }

    @Override // android.widget.SectionIndexer
    public int getSectionForPosition(int i) {
        int[] iArr = this.mPositionToSectionIndex;
        if (i >= iArr.length) {
            return 0;
        }
        return iArr[i];
    }

    @Override // android.widget.SectionIndexer
    public Object[] getSections() {
        return this.mSections;
    }

    public boolean hasAllAppsInList() {
        int i = this.mType;
        return (i == 3 || i == 6) && !this.mHideAllApps;
    }

    public void limitFontLarge(TextView textView) {
        if (textView != null) {
            textView.setTextSize(0, limitFontScale(textView));
        }
    }

    public void limitFontLarge2LinesHeight(TextView textView) {
        if (textView != null) {
            float limitFontScale = limitFontScale(textView);
            textView.setTextSize(0, limitFontScale);
            textView.setMinHeight(Math.round((limitFontScale * 2.0f) + 0.5f));
        }
    }

    public float limitFontScale(@NonNull TextView textView) {
        float f = textView.getResources().getConfiguration().fontScale;
        int i = (f > 1.3f ? 1 : (f == 1.3f ? 0 : -1));
        float textSize = textView.getTextSize();
        return i > 0 ? (textSize / f) * 1.3f : textSize;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        boolean z;
        String packageName = this.mDataSetFiltered.get(i).getPackageName();
        String activityName = this.mDataSetFiltered.get(i).getActivityName();
        AppPickerView.ViewHolder viewHolder2 = (AppPickerView.ViewHolder) viewHolder;
        if (!(viewHolder instanceof AppPickerView.HeaderViewHolder) && !(viewHolder instanceof AppPickerView.SeparatorViewHolder) && !((z = viewHolder instanceof AppPickerView.CustomViewItemViewHolder))) {
            AppPickerIconLoader appPickerIconLoader = this.mAppPickerIconLoader;
            if (appPickerIconLoader != null) {
                appPickerIconLoader.loadIcon(packageName, activityName, viewHolder2.getAppIcon());
            }
            String label = this.mDataSetFiltered.get(i).getLabel();
            if (this.mSearchText.length() > 0) {
                SpannableString spannableString = new SpannableString(label);
                StringTokenizer stringTokenizer = new StringTokenizer(this.mSearchText);
                while (stringTokenizer.hasMoreTokens()) {
                    String nextToken = stringTokenizer.nextToken();
                    int i2 = 0;
                    String str = label;
                    do {
                        char[] semGetPrefixCharForSpan = SeslTextUtilsReflector.semGetPrefixCharForSpan(viewHolder2.getAppLabel().getPaint(), str, nextToken.toCharArray());
                        if (semGetPrefixCharForSpan != null) {
                            nextToken = new String(semGetPrefixCharForSpan);
                        }
                        String lowerCase = str.toLowerCase();
                        int indexOf = str.length() == lowerCase.length() ? lowerCase.indexOf(nextToken.toLowerCase()) : str.indexOf(nextToken);
                        int length = nextToken.length() + indexOf;
                        if (indexOf < 0) {
                            break;
                        }
                        int i3 = indexOf + i2;
                        i2 += length;
                        spannableString.setSpan(new ForegroundColorSpan(this.mForegroundColor), i3, i2, 17);
                        str = str.substring(length);
                        if (str.toLowerCase().indexOf(nextToken.toLowerCase()) != -1) {
                        }
                    } while (i2 < 200);
                }
                viewHolder2.getAppLabel().setText(spannableString);
            } else if (!z) {
                viewHolder2.getAppLabel().setText(label);
            }
        }
        onBindViewHolderAction(viewHolder2, i, packageName);
        AppPickerView.OnBindListener onBindListener = this.mOnBindListener;
        if (onBindListener != null) {
            onBindListener.onBindViewHolder(viewHolder2, i, packageName);
        }
    }

    public abstract void onBindViewHolderAction(AppPickerView.ViewHolder viewHolder, int i, String str);

    public void resetPackages(List<String> list, boolean z, List<AppPickerView.AppLabelInfo> list2, List<ComponentName> list3) {
        List<AppPickerView.AppLabelInfo> list4;
        Log.i(TAG, "Start resetpackage dataSetchanged : " + z);
        this.mDataSet.clear();
        this.mDataSet.addAll(DataManager.resetPackages(this.mContext, list, list2, list3));
        if (Build.VERSION.SDK_INT >= 24 && getAppLabelComparator(this.mOrder) != null) {
            this.mDataSet.sort(getAppLabelComparator(this.mOrder));
        }
        if (hasAllAppsInList() && (list4 = this.mDataSet) != null && list4.size() > 0) {
            this.mDataSet.add(0, new AppPickerView.AppLabelInfo(AppPickerView.ALL_APPS_STRING, "", ""));
        }
        this.mDataSetFiltered.clear();
        this.mDataSetFiltered.addAll(this.mDataSet);
        refreshSectionMap();
        if (z) {
            notifyDataSetChanged();
        }
        Log.i(TAG, "End resetpackage");
    }

    public void setOnBindListener(@NonNull AppPickerView.OnBindListener onBindListener) {
        this.mOnBindListener = onBindListener;
    }

    public void setOnSearchFilterListener(AppPickerView.OnSearchFilterListener onSearchFilterListener) {
        this.mOnSearchFilterListener = onSearchFilterListener;
    }

    public void setOrder(int i) {
        this.mOrder = i;
        if (Build.VERSION.SDK_INT >= 24 && getAppLabelComparator(i) != null) {
            this.mDataSet.sort(getAppLabelComparator(i));
            this.mDataSetFiltered.sort(getAppLabelComparator(i));
        }
        refreshSectionMap();
        notifyDataSetChanged();
    }
}
