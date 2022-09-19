package androidx.apppickerview.widget;

import android.content.ComponentName;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.Filter;
import androidx.annotation.NonNull;
import androidx.apppickerview.widget.AppPickerView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// Credits to Samsung, all rights reserved to the original owners.

public class CustomGridAdapter extends GridAdapter {
    public CustomGridAdapter(Context context, int i, int i2, AppPickerIconLoader appPickerIconLoader) {
        super(context, i, i2, appPickerIconLoader);
        setHasStableIds(true);
    }

    @Override
    public AppPickerView.AppLabelInfo getAppInfo(int i) {
        return super.getAppInfo(i);
    }

    @Override
    public Filter getFilter() {
        return super.getFilter();
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public long getItemId(int i) {
        return super.getItemId(i);
    }

    @Override
    public int getItemViewType(int i) {
        return super.getItemViewType(i);
    }

    @Override
    public int getPositionForSection(int i) {
        return super.getPositionForSection(i);
    }

    @Override
    public int getSectionForPosition(int i) {
        return super.getSectionForPosition(i);
    }

    @Override
    public Object[] getSections() {
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

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        super.onBindViewHolder(viewHolder, i);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return super.onCreateViewHolder(viewGroup, i);
    }

    @Override
    public void setOnBindListener(@NonNull AppPickerView.OnBindListener onBindListener) {
        super.setOnBindListener(onBindListener);
    }

    @Override
    public void setOnSearchFilterListener(AppPickerView.OnSearchFilterListener onSearchFilterListener) {
        super.setOnSearchFilterListener(onSearchFilterListener);
    }

    @Override
    public void setOrder(int i) {
        super.setOrder(i);
    }
}
