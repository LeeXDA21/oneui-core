package androidx.apppickerview.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.appcompat.util.SeslSubheaderRoundedCorner;
import androidx.appcompat.widget.SwitchCompat;
import androidx.apppickerview.R;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\LeeXD\Documents\sesl5port\sesl5.dex */
public class AppPickerView extends RecyclerView implements RecyclerView.RecyclerListener {
    public static final String ALL_APPS_STRING = "all_apps";
    public static final int CUSTOM_VIEW_ITEM = -10;
    private static final boolean DEBUG = false;
    public static final String KEY_APP_SEPARATOR = "app_picker_separator";
    public static final String KEY_CUSTOM_VIEW_ITEM = "app_picker_custom_view_item";
    public static final int ORDER_ASCENDING = 1;
    public static final int ORDER_ASCENDING_IGNORE_CASE = 2;
    public static final int ORDER_DESCENDING = 3;
    public static final int ORDER_DESCENDING_IGNORE_CASE = 4;
    public static final int ORDER_NONE = 0;
    private static final String TAG = "AppPickerView";
    public static final int TYPE_GRID = 7;
    public static final int TYPE_GRID_CHECKBOX = 8;
    public static final int TYPE_LIST = 0;
    public static final int TYPE_LIST_ACTION_BUTTON = 1;
    public static final int TYPE_LIST_CHECKBOX = 2;
    public static final int TYPE_LIST_CHECKBOX_WITH_ALL_APPS = 3;
    public static final int TYPE_LIST_RADIOBUTTON = 4;
    public static final int TYPE_LIST_SWITCH = 5;
    public static final int TYPE_LIST_SWITCH_WITH_ALL_APPS = 6;
    private AbsAdapter mAdapter;
    private AppPickerIconLoader mAppPickerIconLoader;
    private Context mContext;
    private RecyclerView.ItemDecoration mGridSpacingDecoration;
    private boolean mIsCustomViewItemEnabled;
    private int mOrder;
    private SeslSubheaderRoundedCorner mRoundedCorner;
    private ArrayList<Integer> mSeparators;
    private int mSpanCount;
    private int mType;

    /* loaded from: C:\Users\LeeXD\Documents\sesl5port\sesl5.dex */
    public static class AppLabelInfo {
        private String mActivityName;
        private String mLabel;
        private String mPackageName;
        private int mSpanCount;
        private boolean mIsSeparator = false;
        private boolean mIsCustomViewItem = false;

        public AppLabelInfo(String str, String str2, String str3) {
            this.mPackageName = str;
            this.mLabel = str2;
            this.mActivityName = str3;
        }

        public String getActivityName() {
            return this.mActivityName;
        }

        public String getLabel() {
            return this.mLabel;
        }

        public String getPackageName() {
            return this.mPackageName;
        }

        public int getSpanCount() {
            return this.mSpanCount;
        }

        public boolean isCustomViewItem() {
            return this.mIsCustomViewItem;
        }

        public boolean isSeparator() {
            return this.mIsSeparator;
        }

        public void setActivityName(String str) {
            this.mActivityName = str;
        }

        public AppLabelInfo setCustomViewItem(boolean z, int i) {
            this.mIsCustomViewItem = z;
            this.mSpanCount = i;
            return this;
        }

        public void setLabel(String str) {
            this.mLabel = str;
        }

        public void setPackageName(String str) {
            this.mPackageName = str;
        }

        public AppLabelInfo setSeparator(boolean z) {
            this.mIsSeparator = z;
            return this;
        }

        public String toString() {
            return "[AppLabel] label=" + this.mLabel + ", packageName=" + this.mPackageName;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    /* loaded from: C:\Users\LeeXD\Documents\sesl5port\sesl5.dex */
    public @interface AppPickerOrder {
    }

    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    /* loaded from: C:\Users\LeeXD\Documents\sesl5port\sesl5.dex */
    public @interface AppPickerType {
    }

    /* loaded from: C:\Users\LeeXD\Documents\sesl5port\sesl5.dex */
    public static class CustomViewItemViewHolder extends ViewHolder {
        public CustomViewItemViewHolder(View view) {
            super(view);
        }
    }

    /* loaded from: C:\Users\LeeXD\Documents\sesl5port\sesl5.dex */
    public static class FooterViewHolder extends ViewHolder {
        public FooterViewHolder(View view) {
            super(view);
        }
    }

    /* loaded from: C:\Users\LeeXD\Documents\sesl5port\sesl5.dex */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private float density;
        private boolean includeEdge;
        private int spacing;
        private int spacingTop;
        private int spanCount;

        public GridSpacingItemDecoration(int i, int i2, boolean z) {
            this.spanCount = i;
            float f = Resources.getSystem().getDisplayMetrics().density;
            this.density = f;
            this.spacing = (int) (i2 * f);
            this.spacingTop = (int) (f * 12.0f);
            this.includeEdge = z;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            int i;
            int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
            int i2 = childAdapterPosition % this.spanCount;
            Iterator it = AppPickerView.this.mSeparators.iterator();
            int i3 = -1;
            do {
                i = i3;
                if (!it.hasNext()) {
                    break;
                }
                i3 = ((Integer) it.next()).intValue();
            } while (i3 < childAdapterPosition);
            if (!this.includeEdge) {
                int i4 = this.spacing;
                int i5 = this.spanCount;
                rect.left = (i2 * i4) / i5;
                rect.right = i4 - (((i2 + 1) * i4) / i5);
            } else if (childAdapterPosition == i3) {
            } else {
                int i6 = this.spacing;
                int i7 = this.spanCount;
                rect.left = i6 - ((i2 * i6) / i7);
                rect.right = ((i2 + 1) * i6) / i7;
                if (i != -1) {
                    if ((childAdapterPosition - i) - 1 < i7) {
                        rect.top = this.spacingTop;
                    }
                } else if (childAdapterPosition < i7) {
                    rect.top = this.spacingTop;
                }
                rect.bottom = this.spacingTop;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public void seslOnDispatchDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
            super.seslOnDispatchDraw(canvas, recyclerView, state);
            int childCount = recyclerView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = recyclerView.getChildAt(i);
                if (recyclerView.getChildViewHolder(childAt) instanceof SeparatorViewHolder) {
                    AppPickerView.this.mRoundedCorner.drawRoundedCorner(childAt, canvas);
                }
            }
        }
    }

    /* loaded from: C:\Users\LeeXD\Documents\sesl5port\sesl5.dex */
    public static class HeaderViewHolder extends ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    /* loaded from: C:\Users\LeeXD\Documents\sesl5port\sesl5.dex */
    public class ListDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;
        private int mDividerLeft;
        private int mType;

        public ListDividerItemDecoration(Context context, int i) {
            this.mType = i;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{16843284});
            this.mDivider = obtainStyledAttributes.getDrawable(0);
            obtainStyledAttributes.recycle();
            this.mDividerLeft = (int) AppPickerView.this.getResources().getDimension(R.dimen.app_picker_list_icon_frame_width);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public void onDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
            super.onDraw(canvas, recyclerView, state);
            int childCount = recyclerView.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                View childAt = recyclerView.getChildAt(i);
                if (!(recyclerView.getChildViewHolder(childAt) instanceof SeparatorViewHolder)) {
                    int i2 = this.mDividerLeft;
                    int width = recyclerView.getWidth() - recyclerView.getPaddingRight();
                    int bottom = childAt.getBottom() + ((ViewGroup.MarginLayoutParams) ((RecyclerView.LayoutParams) childAt.getLayoutParams())).bottomMargin;
                    int intrinsicHeight = this.mDivider.getIntrinsicHeight() + bottom;
                    if (i == 0 && this.mType == 6) {
                        i2 = recyclerView.getPaddingLeft();
                    }
                    this.mDivider.setBounds(i2, bottom, width, intrinsicHeight);
                    this.mDivider.draw(canvas);
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public void seslOnDispatchDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
            super.seslOnDispatchDraw(canvas, recyclerView, state);
            int childCount = recyclerView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = recyclerView.getChildAt(i);
                if (recyclerView.getChildViewHolder(childAt) instanceof SeparatorViewHolder) {
                    AppPickerView.this.mRoundedCorner.drawRoundedCorner(childAt, canvas);
                }
            }
        }
    }

    /* loaded from: C:\Users\LeeXD\Documents\sesl5port\sesl5.dex */
    public interface OnBindListener {
        void onBindViewHolder(ViewHolder viewHolder, int i, String str);
    }

    /* loaded from: C:\Users\LeeXD\Documents\sesl5port\sesl5.dex */
    public interface OnSearchFilterListener {
        void onSearchFilterCompleted(int i);
    }

    /* loaded from: C:\Users\LeeXD\Documents\sesl5port\sesl5.dex */
    public static class SeparatorViewHolder extends ViewHolder {
        private final TextView mSeparatorText;

        public SeparatorViewHolder(View view) {
            super(view);
            this.mSeparatorText = (TextView) view.findViewById(R.id.separator);
        }

        public TextView getSeparatorText() {
            return this.mSeparatorText;
        }

        public void setSeparatorHeight(int i) {
            this.mSeparatorText.setHeight(i);
        }

        public void setSeparatorText(String str) {
            ViewGroup.LayoutParams layoutParams = this.mSeparatorText.getLayoutParams();
            layoutParams.height = -2;
            this.mSeparatorText.setLayoutParams(layoutParams);
            this.mSeparatorText.setText(str);
        }
    }

    /* loaded from: C:\Users\LeeXD\Documents\sesl5port\sesl5.dex */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton mActionButton;
        private final ImageView mAppIcon;
        private final ViewGroup mAppIconContainer;
        private final TextView mAppName;
        private final CheckBox mCheckBox;
        private final ViewGroup mLeftContainer;
        private final RadioButton mRadioButton;
        private final TextView mSummary;
        private final SwitchCompat mSwitch;
        private final ViewGroup mTitleContainer;
        private final ViewGroup mWidgetContainer;

        public ViewHolder(View view) {
            super(view);
            this.mAppName = (TextView) view.findViewById(R.id.title);
            this.mAppIcon = (ImageView) view.findViewById(R.id.icon);
            this.mAppIconContainer = (ViewGroup) view.findViewById(R.id.icon_frame);
            this.mTitleContainer = (ViewGroup) view.findViewById(R.id.title_frame);
            this.mSummary = (TextView) view.findViewById(R.id.summary);
            this.mLeftContainer = (ViewGroup) view.findViewById(R.id.left_frame);
            this.mCheckBox = (CheckBox) view.findViewById(R.id.check_widget);
            this.mRadioButton = (RadioButton) view.findViewById(R.id.radio_widget);
            this.mWidgetContainer = (ViewGroup) view.findViewById(R.id.widget_frame);
            this.mSwitch = (SwitchCompat) view.findViewById(R.id.switch_widget);
            this.mActionButton = (ImageButton) view.findViewById(R.id.image_button);
        }

        public ImageButton getActionButton() {
            return this.mActionButton;
        }

        public ImageView getAppIcon() {
            return this.mAppIcon;
        }

        public ViewGroup getAppIconContainer() {
            return this.mAppIconContainer;
        }

        public TextView getAppLabel() {
            return this.mAppName;
        }

        public CheckBox getCheckBox() {
            return this.mCheckBox;
        }

        public View getItem() {
            return this.itemView;
        }

        public View getLeftConatiner() {
            return this.mLeftContainer;
        }

        public RadioButton getRadioButton() {
            return this.mRadioButton;
        }

        public TextView getSummary() {
            return this.mSummary;
        }

        public SwitchCompat getSwitch() {
            return this.mSwitch;
        }

        public ViewGroup getTitleContainer() {
            return this.mTitleContainer;
        }

        public ViewGroup getWidgetContainer() {
            return this.mWidgetContainer;
        }
    }

    public AppPickerView(@NonNull Context context) {
        this(context, null);
    }

    public AppPickerView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public AppPickerView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mSpanCount = 4;
        this.mIsCustomViewItemEnabled = false;
        this.mContext = context;
        setRecyclerListener(this);
        this.mAppPickerIconLoader = new AppPickerIconLoader(this.mContext);
    }

    public static List<AppLabelInfo> getAppLabelinfoList(Context context, List<String> list) {
        return DataManager.resetPackages(context, list);
    }

    public static List<AppLabelInfo> getAppLabelinfoList(List<ComponentName> list, Context context) {
        return DataManager.resetPackages(list, context);
    }

    public static List<String> getInstalledPackages(Context context) {
        List<ApplicationInfo> installedApplications = context.getPackageManager().getInstalledApplications(0);
        ArrayList arrayList = new ArrayList();
        for (ApplicationInfo applicationInfo : installedApplications) {
            arrayList.add(applicationInfo.packageName);
        }
        return arrayList;
    }

    public static List<AppLabelInfo> getInstalledPackagesWithLabel(Context context) {
        return DataManager.resetPackages(context, getInstalledPackages(context));
    }

    private RecyclerView.LayoutManager getLayoutManager(int i) {
        return (i == 7 || i == 8) ? new GridLayoutManager(this.mContext, this.mSpanCount) : new LinearLayoutManager(this.mContext);
    }

    private void setAppPickerView(int i, List<String> list, int i2, List<AppLabelInfo> list2, List<ComponentName> list3) {
        int i3;
        List<String> list4;
        TypedValue typedValue = new TypedValue();
        this.mContext.getTheme().resolveAttribute(R.attr.roundedCornerColor, typedValue, true);
        SeslSubheaderRoundedCorner seslSubheaderRoundedCorner = new SeslSubheaderRoundedCorner(this.mContext);
        this.mRoundedCorner = seslSubheaderRoundedCorner;
        seslSubheaderRoundedCorner.setRoundedCorners(15);
        if (typedValue.resourceId > 0) {
            this.mRoundedCorner.setRoundedCornerColor(15, getResources().getColor(typedValue.resourceId, null));
        }
        if (list == null && list3 == null) {
            list4 = getInstalledPackages(this.mContext);
            i3 = i;
        } else {
            i3 = i;
            list4 = list;
        }
        this.mType = i3;
        this.mOrder = i2;
        if (!this.mIsCustomViewItemEnabled) {
            this.mAdapter = AbsAdapter.getAppPickerAdapter(this.mContext, list4, i, i2, list2, this.mAppPickerIconLoader, list3);
        }
        int i4 = this.mType;
        switch (i4) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                addItemDecoration(new ListDividerItemDecoration(this.mContext, i4));
                break;
            case 7:
            case 8:
                if (this.mGridSpacingDecoration == null) {
                    GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(this.mSpanCount, 8, true);
                    this.mGridSpacingDecoration = gridSpacingItemDecoration;
                    addItemDecoration(gridSpacingItemDecoration);
                    break;
                }
                break;
        }
        setLayoutManager(getLayoutManager(i));
        setAdapter(this.mAdapter);
        seslSetGoToTopEnabled(true);
        seslSetFastScrollerEnabled(true);
        seslSetFillBottomEnabled(true);
        this.mSeparators = new ArrayList<>();
    }

    public void addCustomViewItem(int i) {
        addCustomViewItem(i, -1);
    }

    public void addCustomViewItem(int i, int i2) {
        if (i2 == -1) {
            this.mSeparators.add(Integer.valueOf(i));
        }
        this.mAdapter.addCustomViewItem(i, i2);
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager == null || !(layoutManager instanceof GridLayoutManager)) {
            return;
        }
        ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: androidx.apppickerview.widget.AppPickerView.2
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i3) {
                if (i3 < AppPickerView.this.mAdapter.getDataSet().size()) {
                    if (AppPickerView.this.mAdapter.getDataSet().get(i3).isCustomViewItem()) {
                        return AppPickerView.this.mAdapter.getDataSet().get(i3).getSpanCount() == -1 ? AppPickerView.this.mSpanCount : AppPickerView.this.mAdapter.getDataSet().get(i3).getSpanCount();
                    } else if (AppPickerView.this.mAdapter.getDataSet().get(i3).isSeparator()) {
                        return AppPickerView.this.mSpanCount;
                    }
                }
                return 1;
            }
        });
    }

    public void addPackage(int i, String str) {
        this.mAdapter.addPackage(i, str);
    }

    public void addSeparator(int i) {
        this.mSeparators.add(Integer.valueOf(i));
        Collections.sort(this.mSeparators);
        this.mAdapter.addSeparator(i);
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager == null || !(layoutManager instanceof GridLayoutManager)) {
            return;
        }
        ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: androidx.apppickerview.widget.AppPickerView.1
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i2) {
                if (i2 < AppPickerView.this.mAdapter.getDataSet().size()) {
                    if (AppPickerView.this.mAdapter.getDataSet().get(i2).isCustomViewItem()) {
                        return AppPickerView.this.mAdapter.getDataSet().get(i2).getSpanCount() == -1 ? AppPickerView.this.mSpanCount : AppPickerView.this.mAdapter.getDataSet().get(i2).getSpanCount();
                    } else if (AppPickerView.this.mAdapter.getDataSet().get(i2).isSeparator()) {
                        return AppPickerView.this.mSpanCount;
                    }
                }
                return 1;
            }
        });
    }

    public AppLabelInfo getAppLabelInfo(int i) {
        AbsAdapter absAdapter = this.mAdapter;
        if (absAdapter != null) {
            return absAdapter.getAppInfo(i);
        }
        return null;
    }

    public int getAppLabelOrder() {
        return this.mOrder;
    }

    public AppPickerIconLoader getAppPickerIconLoader() {
        return this.mAppPickerIconLoader;
    }

    public int getType() {
        return this.mType;
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mAppPickerIconLoader.startIconLoaderThread();
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        this.mAppPickerIconLoader.stopIconLoaderThread();
        super.onDetachedFromWindow();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.RecyclerListener
    public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        ImageButton actionButton = viewHolder2.getActionButton();
        if (actionButton != null && actionButton.hasOnClickListeners()) {
            actionButton.setOnClickListener(null);
        }
        ImageView appIcon = viewHolder2.getAppIcon();
        if (appIcon != null && appIcon.hasOnClickListeners()) {
            appIcon.setOnClickListener(null);
        }
        CheckBox checkBox = viewHolder2.getCheckBox();
        if (checkBox != null) {
            checkBox.setOnCheckedChangeListener(null);
        }
        View item = viewHolder2.getItem();
        if (item != null && item.hasOnClickListeners()) {
            item.setOnClickListener(null);
        }
        SwitchCompat switchCompat = viewHolder2.getSwitch();
        if (switchCompat != null) {
            switchCompat.setOnCheckedChangeListener(null);
        }
    }

    public void refresh() {
        post(new Runnable() { // from class: androidx.apppickerview.widget.AppPickerView.5
            @Override // java.lang.Runnable
            public void run() {
                Log.i(AppPickerView.TAG, "run refresh");
                AppPickerView.this.mAdapter.notifyDataSetChanged();
            }
        });
    }

    public void refreshUI() {
        post(new Runnable() { // from class: androidx.apppickerview.widget.AppPickerView.3
            @Override // java.lang.Runnable
            public void run() {
                Log.i(AppPickerView.TAG, "run refreshUI");
                AppPickerView.this.mAdapter.notifyItemRangeChanged(0, AppPickerView.this.mAdapter.getItemCount());
            }
        });
    }

    public void refreshUI(final int i) {
        post(new Runnable() { // from class: androidx.apppickerview.widget.AppPickerView.4
            @Override // java.lang.Runnable
            public void run() {
                Log.i(AppPickerView.TAG, "run refreshUI by position");
                AppPickerView.this.mAdapter.notifyItemChanged(i);
            }
        });
    }

    public void resetComponentName(List<ComponentName> list) {
        this.mAdapter.resetPackages(null, true, null, list);
    }

    public void resetComponentName(List<ComponentName> list, List<AppLabelInfo> list2) {
        this.mAdapter.resetPackages(null, true, list2, list);
    }

    public void resetPackages(List<String> list) {
        this.mAdapter.resetPackages(list, true, null, null);
    }

    public void resetPackages(List<String> list, List<AppLabelInfo> list2) {
        this.mAdapter.resetPackages(list, true, list2, null);
    }

    public void setAppLabelOrder(int i) {
        this.mOrder = i;
        this.mAdapter.setOrder(i);
    }

    public void setAppPickerView(int i) {
        setAppPickerView(i, (List<String>) null, 2, (List<AppLabelInfo>) null);
    }

    public void setAppPickerView(int i, int i2) {
        setAppPickerView(i, (List<String>) null, i2, (List<AppLabelInfo>) null);
    }

    public void setAppPickerView(int i, int i2, List<ComponentName> list) {
        setAppPickerView(i, null, i2, null, list);
    }

    public void setAppPickerView(int i, int i2, List<AppLabelInfo> list, List<ComponentName> list2) {
        setAppPickerView(i, null, i2, list, list2);
    }

    public void setAppPickerView(int i, List<String> list) {
        setAppPickerView(i, list, 2, (List<AppLabelInfo>) null);
    }

    public void setAppPickerView(int i, List<String> list, int i2) {
        setAppPickerView(i, list, i2, (List<AppLabelInfo>) null);
    }

    public void setAppPickerView(int i, List<String> list, int i2, List<AppLabelInfo> list2) {
        setAppPickerView(i, list, i2, list2, null);
    }

    public void setAppPickerView(int i, List<String> list, List<AppLabelInfo> list2) {
        setAppPickerView(i, list, 2, list2);
    }

    public void setAppPickerView(List<ComponentName> list, int i) {
        setAppPickerView(i, null, 2, null, list);
    }

    public void setCustomAdapter(AbsAdapter absAdapter) {
        this.mAdapter = absAdapter;
    }

    public void setCustomViewItemEnabled(boolean z) {
        this.mIsCustomViewItemEnabled = z;
    }

    public void setGridSpanCount(int i) {
        this.mSpanCount = i;
    }

    public void setOnBindListener(@NonNull OnBindListener onBindListener) {
        AbsAdapter absAdapter = this.mAdapter;
        if (absAdapter != null) {
            absAdapter.setOnBindListener(onBindListener);
        }
    }

    public void setSearchFilter(String str) {
        setSearchFilter(str, null);
    }

    public void setSearchFilter(String str, OnSearchFilterListener onSearchFilterListener) {
        if (onSearchFilterListener != null) {
            this.mAdapter.setOnSearchFilterListener(onSearchFilterListener);
        }
        this.mAdapter.getFilter().filter(str);
    }
}
