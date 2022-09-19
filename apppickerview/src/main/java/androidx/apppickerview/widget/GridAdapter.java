package androidx.apppickerview.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.RestrictTo;
import androidx.apppickerview.R;
import androidx.apppickerview.widget.AppPickerView;
import androidx.recyclerview.widget.RecyclerView;

// Credits to Samsung, all rights reserved to the original owners.

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP_PREFIX})
public class GridAdapter extends AbsAdapter {
    private static final int TYPE_HEADER = 256;
    private static final int TYPE_ITEM = 257;
    private static final int TYPE_SEPARATOR = 259;

    public GridAdapter(Context context, int i, int i2, AppPickerIconLoader appPickerIconLoader) {
        super(context, i, i2, appPickerIconLoader);
    }

    @Override
    public int getItemViewType(int i) {
        return getAppInfo(i).isSeparator() ? TYPE_SEPARATOR : getAppInfo(i).isCustomViewItem() ? -10 : 257;
    }

    @Override
    public void onBindViewHolderAction(AppPickerView.ViewHolder viewHolder, int i, String str) {
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == TYPE_SEPARATOR) {
            return new AppPickerView.SeparatorViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.app_picker_list_separator, viewGroup, false));
        }
        View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.app_picker_grid, viewGroup, false);
        if (this.mType == 7) {
            inflate.findViewById(R.id.check_widget).setVisibility(8);
        }
        limitFontLarge2LinesHeight((TextView) inflate.findViewById(R.id.title));
        return new AppPickerView.ViewHolder(inflate);
    }
}
