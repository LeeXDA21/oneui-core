package androidx.apppickerview.widget;

import android.content.ComponentName;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.Filter;
import androidx.annotation.NonNull;
import androidx.apppickerview.widget.AppPickerView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/* loaded from: C:\Users\LeeXD\Documents\sesl5port\sesl5.dex */
public class CustomListAdapter extends ListAdapter {
    public CustomListAdapter(Context context, int i, int i2, AppPickerIconLoader appPickerIconLoader) {
        super(context, i, i2, appPickerIconLoader);
        setHasStableIds(true);
    }

    @Override // androidx.apppickerview.widget.AbsAdapter
    public /* bridge */ /* synthetic */ AppPickerView.AppLabelInfo getAppInfo(int i) {
        return super.getAppInfo(i);
    }

    @Override // androidx.apppickerview.widget.AbsAdapter, android.widget.Filterable
    public /* bridge */ /* synthetic */ Filter getFilter() {
        return super.getFilter();
    }

    @Override // androidx.apppickerview.widget.AbsAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ int getItemCount() {
        return super.getItemCount();
    }

    @Override // androidx.apppickerview.widget.AbsAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ long getItemId(int i) {
        return super.getItemId(i);
    }

    @Override // androidx.apppickerview.widget.ListAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ int getItemViewType(int i) {
        return super.getItemViewType(i);
    }

    @Override // androidx.apppickerview.widget.AbsAdapter, android.widget.SectionIndexer
    public /* bridge */ /* synthetic */ int getPositionForSection(int i) {
        return super.getPositionForSection(i);
    }

    @Override // androidx.apppickerview.widget.AbsAdapter, android.widget.SectionIndexer
    public /* bridge */ /* synthetic */ int getSectionForPosition(int i) {
        return super.getSectionForPosition(i);
    }

    @Override // androidx.apppickerview.widget.AbsAdapter, android.widget.SectionIndexer
    public /* bridge */ /* synthetic */ Object[] getSections() {
        return super.getSections();
    }

    public void initialize() {
        resetPackages(AppPickerView.getInstalledPackages(this.mContext), false, null, null);
    }

    public void initialize(List<String> list) {
        resetPackages(list, false, null, null);
    }

    public void initialize(List<String> list, List<AppPickerView.AppLabelInfo> list2) {
        resetPackages(list, false, list2, null);
    }

    public void initialize(List<String> list, List<AppPickerView.AppLabelInfo> list2, List<ComponentName> list3) {
        resetPackages(list, false, list2, list3);
    }

    @Override // androidx.apppickerview.widget.AbsAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        super.onBindViewHolder(viewHolder, i);
    }

    @Override // androidx.apppickerview.widget.ListAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder */
    public /* bridge */ /* synthetic */ RecyclerView.ViewHolder mo209onCreateViewHolder(ViewGroup viewGroup, int i) {
        return super.mo209onCreateViewHolder(viewGroup, i);
    }

    @Override // androidx.apppickerview.widget.AbsAdapter
    public /* bridge */ /* synthetic */ void setOnBindListener(@NonNull AppPickerView.OnBindListener onBindListener) {
        super.setOnBindListener(onBindListener);
    }

    @Override // androidx.apppickerview.widget.AbsAdapter
    public /* bridge */ /* synthetic */ void setOnSearchFilterListener(AppPickerView.OnSearchFilterListener onSearchFilterListener) {
        super.setOnSearchFilterListener(onSearchFilterListener);
    }

    @Override // androidx.apppickerview.widget.AbsAdapter
    public /* bridge */ /* synthetic */ void setOrder(int i) {
        super.setOrder(i);
    }
}
