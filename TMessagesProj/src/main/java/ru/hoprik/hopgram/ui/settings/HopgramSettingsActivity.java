package ru.hoprik.hopgram.ui.settings;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;
import org.telegram.messenger.*;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import ru.hoprik.hopgram.ui.components.HeaderInfoCell;

import static org.telegram.messenger.LocaleController.getString;

public class HopgramSettingsActivity extends BaseFragment {
    RecyclerListView listView;
    private int rowCount = 0;

    private int appInfoRow;

    private int settingsRow;
    private int aboutRow;

    private int generalsRow;
    private int channelRow;
    private int sourcecodeRow;
    @SuppressWarnings("FieldCanBeLocal")
    private LinearLayoutManager layoutManager;
    public HopgramSettingsActivity(){
        super();
    }

    @Override
    public boolean onFragmentCreate() {
        appInfoRow = rowCount++;
        settingsRow = rowCount++;
        generalsRow = rowCount++;
        aboutRow = rowCount++;
        channelRow = rowCount++;
        sourcecodeRow = rowCount++;

        return super.onFragmentCreate();
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(false);
        actionBar.setTitle(LocaleController.getString(R.string.HopgramSettings));

        actionBar.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        actionBar.setItemsColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText), false);
        actionBar.setItemsBackgroundColor(Theme.getColor(Theme.key_actionBarWhiteSelector), false);
        actionBar.setTitleColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));

        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });

        fragmentView = new FrameLayout(context);
        FrameLayout frameLayout = (FrameLayout) fragmentView;

        listView = new RecyclerListView(context);
        listView.setAdapter(new ListAdapter(context));
        listView.setLayoutManager(layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        });
        listView.setVerticalScrollBarEnabled(false);
        listView.setVerticalScrollBarEnabled(false);
        frameLayout.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        listView.setOnItemClickListener(((view, position) -> {
            if (position == generalsRow){
                presentFragment(new GeneralSettingsActivity());
            }
            if (position == channelRow){
                MessagesController.getInstance(currentAccount).openByUserName(("hoprikgram"), this, 1);
            }
            if (position == sourcecodeRow){
                Browser.openUrl(getParentActivity(), "https://github.com/hoprik/hopgram.git");
            }
        }));

        return fragmentView;
    }

    public class ListAdapter extends RecyclerListView.SelectionAdapter{
        private Context mContext;

        public ListAdapter(Context context){
            this.mContext = context;
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            int position = holder.getAdapterPosition();
            return !(position == aboutRow || position == settingsRow || position == generalsRow ||
                    position == appInfoRow || position == channelRow || position == sourcecodeRow);
        }

        @Override
        public int getItemCount() {
            return rowCount;
        }


        @NonNull
        @NotNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View view;
            switch (viewType){
                case 0:
                    view = new HeaderCell(mContext, resourceProvider);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 1:
                    view = new TextCell(mContext, resourceProvider);
                    view.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                    break;
                case 2:
                    view = new HeaderInfoCell(mContext);
                    view.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                    break;
                case 3:
                default:
                    view = new TextInfoPrivacyCell(mContext, resourceProvider);
                    view.setBackgroundDrawable(Theme.getThemedDrawableByKey(mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                    break;
            }

            return new RecyclerListView.Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()){
                case 0:
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position == settingsRow) {
                        headerCell.setText(getString("Settings", R.string.Settings));
                    } else if (position == aboutRow) {
                        headerCell.setText(getString("About", R.string.HopgramAbout));
                    }
                    break;
                case 1:
                    TextCell textCell = (TextCell) holder.itemView;
                    if (position == generalsRow){
                        textCell.setTextAndIcon(getString("GeneralSettings", R.string.GeneralSettings), R.drawable.msg_settings, true);
                    } else if (position == channelRow){
                        textCell.setTextAndValueAndIcon(getString("HopgramChannel", R.string.HopgramChannel),"@hoprikgram", R.drawable.notification, false);
                    }else if (position == sourcecodeRow){
                        textCell.setTextAndIcon(getString("SourceCode", R.string.SourceCode), R.drawable.msg_hybrid, false);
                    }
                    break;
                case 2:
                    HeaderInfoCell headerInfoCell = (HeaderInfoCell) holder.itemView;
                    headerInfoCell.setPadding(0, ActionBar.getCurrentActionBarHeight() + (actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) - AndroidUtilities.dp(40), 0, 0);
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == aboutRow || position == settingsRow){
                return 0;
            }
            if (position == generalsRow || position == channelRow || position == sourcecodeRow){
                return 1;
            }
            if (position == appInfoRow){
                return 2;
            }
            return 3;
        }
    }
}
