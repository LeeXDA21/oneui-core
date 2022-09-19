package androidx.apppickerview.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;
import androidx.annotation.RestrictTo;
import androidx.apppickerview.R;
import androidx.apppickerview.widget.AppPickerView;
import androidx.recyclerview.widget.RecyclerView;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP_PREFIX})
/* loaded from: C:\Users\LeeXD\Documents\sesl5port\sesl5.dex */
public class ListAdapter extends AbsAdapter {
    private static final int TYPE_FOOTER = 258;
    private static final int TYPE_HEADER = 256;
    private static final int TYPE_ITEM = 257;
    private static final int TYPE_SEPARATOR = 259;

    public ListAdapter(Context context, int i, int i2, AppPickerIconLoader appPickerIconLoader) {
        super(context, i, i2, appPickerIconLoader);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        if (getAppInfo(i).isSeparator()) {
            return TYPE_SEPARATOR;
        }
        if (getAppInfo(i).isCustomViewItem()) {
            return -10;
        }
        if ((i == 0 || (getAppInfo(i).isSeparator() && i == 1)) && hasAllAppsInList()) {
            return 256;
        }
        return i == getItemCount() - 1 ? 258 : 257;
    }

    @Override // androidx.apppickerview.widget.AbsAdapter
    public void onBindViewHolderAction(final AppPickerView.ViewHolder viewHolder, int i, String str) {
        if (getItemViewType(i) == TYPE_SEPARATOR || getItemViewType(i) == -10) {
            return;
        }
        switch (this.mType) {
            case 0:
                viewHolder.getWidgetContainer().setVisibility(8);
                viewHolder.getLeftConatiner().setVisibility(8);
                return;
            case 1:
                viewHolder.getWidgetContainer().setVisibility(0);
                viewHolder.getActionButton().setVisibility(0);
                return;
            case 2:
            case 3:
                viewHolder.getLeftConatiner().setVisibility(0);
                viewHolder.getWidgetContainer().setVisibility(8);
                viewHolder.getItem().setOnClickListener(new View.OnClickListener() { // from class: androidx.apppickerview.widget.ListAdapter.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        viewHolder.getCheckBox().setChecked(!viewHolder.getCheckBox().isChecked());
                    }
                });
                if (!((AccessibilityManager) this.mContext.getSystemService("accessibility")).isEnabled()) {
                    return;
                }
                viewHolder.getCheckBox().setFocusable(false);
                viewHolder.getCheckBox().setClickable(false);
                viewHolder.getItem().setContentDescription(null);
                return;
            case 4:
                viewHolder.getLeftConatiner().setVisibility(0);
                viewHolder.getWidgetContainer().setVisibility(8);
                viewHolder.getItem().setOnClickListener(new View.OnClickListener() { // from class: androidx.apppickerview.widget.ListAdapter.3
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        viewHolder.getRadioButton().setChecked(!viewHolder.getRadioButton().isChecked());
                    }
                });
                if (!((AccessibilityManager) this.mContext.getSystemService("accessibility")).isEnabled()) {
                    return;
                }
                viewHolder.getRadioButton().setFocusable(false);
                viewHolder.getRadioButton().setClickable(false);
                viewHolder.getItem().setContentDescription(null);
                return;
            case 5:
            case 6:
                viewHolder.getLeftConatiner().setVisibility(8);
                viewHolder.getWidgetContainer().setVisibility(0);
                viewHolder.getItem().setOnClickListener(new View.OnClickListener() { // from class: androidx.apppickerview.widget.ListAdapter.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        viewHolder.getSwitch().setChecked(!viewHolder.getSwitch().isChecked());
                    }
                });
                if (!((AccessibilityManager) this.mContext.getSystemService("accessibility")).isEnabled()) {
                    return;
                }
                viewHolder.getSwitch().setFocusable(false);
                viewHolder.getSwitch().setClickable(false);
                viewHolder.getItem().setContentDescription(null);
                return;
            default:
                return;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder */
    public RecyclerView.ViewHolder mo209onCreateViewHolder(ViewGroup viewGroup, int i) {
        int i2 = R.layout.app_picker_list;
        if (i == 256 && hasAllAppsInList()) {
            i2 = R.layout.app_picker_list_header;
        } else if (i == 258) {
            i2 = R.layout.app_picker_list_footer;
        } else if (i == TYPE_SEPARATOR) {
            i2 = R.layout.app_picker_list_separator;
        }
        View inflate = LayoutInflater.from(this.mContext).inflate(i2, viewGroup, false);
        ViewGroup viewGroup2 = (ViewGroup) inflate.findViewById(R.id.widget_frame);
        if (viewGroup2 != null) {
            switch (this.mType) {
                case 0:
                case 5:
                case 6:
                    LayoutInflater.from(this.mContext).inflate(R.layout.app_picker_frame_switch, viewGroup2, true);
                    break;
                case 1:
                    LayoutInflater.from(this.mContext).inflate(R.layout.app_picker_frame_actionbutton, viewGroup2, true);
                    break;
                case 2:
                case 3:
                    LayoutInflater.from(this.mContext).inflate(R.layout.app_picker_frame_checkbox, (ViewGroup) inflate.findViewById(R.id.left_frame), true);
                    break;
                case 4:
                    inflate.setPadding(this.mContext.getResources().getDimensionPixelSize(R.dimen.app_picker_list_radio_padding_start), 0, this.mContext.getResources().getDimensionPixelSize(R.dimen.app_picker_list_padding_end), 0);
                    LayoutInflater.from(this.mContext).inflate(R.layout.app_picker_frame_radiobutton, (ViewGroup) inflate.findViewById(R.id.left_frame), true);
                    break;
            }
        }
        limitFontLarge((TextView) inflate.findViewById(R.id.title));
        limitFontLarge((TextView) inflate.findViewById(R.id.summary));
        return (i != 256 || !hasAllAppsInList()) ? i == 258 ? new AppPickerView.FooterViewHolder(inflate) : i == TYPE_SEPARATOR ? new AppPickerView.SeparatorViewHolder(inflate) : new AppPickerView.ViewHolder(inflate) : new AppPickerView.HeaderViewHolder(inflate);
    }
}
