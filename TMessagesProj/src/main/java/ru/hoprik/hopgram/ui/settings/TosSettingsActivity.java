package ru.hoprik.hopgram.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;
import org.telegram.messenger.*;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.DrawerLayoutAdapter;
import org.telegram.ui.Cells.*;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.LaunchActivity;
import ru.hoprik.hopgram.HopgramStorage;
import ru.hoprik.hopgram.ui.AppSelectActivity;

import java.io.InputStream;

import static org.telegram.messenger.AndroidUtilities.dp;
import static org.telegram.messenger.LocaleController.getString;

public class TosSettingsActivity extends BaseFragment {
    RecyclerListView listView;
    private int rowCount = 0;
    private int deleteMessagesRow;
    private int saveDeleteRow;

    private int premiumRow;
    private int localPremiumRow;
    private int disableAdsRow;

    //    private int ghostRow;
    private int otherRow;
    private int replayMessagesRow;

    private boolean saveDeleteOnElement;
    private boolean localPremiumOnElement;
    private boolean disableAdsOnElement;
    private boolean replyMessagesOnElement;
    private ListAdapter adapter;
    @SuppressWarnings("FieldCanBeLocal")
    private LinearLayoutManager layoutManager;

    public TosSettingsActivity() {
        super();
    }

    @Override
    public boolean onFragmentCreate() {
        saveDeleteOnElement = HopgramStorage.saveDeleteOnElement;
        localPremiumOnElement = HopgramStorage.localPremiumOnElement;
        disableAdsOnElement = HopgramStorage.disableAdsOnElement;
        replyMessagesOnElement = HopgramStorage.replyMessagesOnElement;

        deleteMessagesRow = rowCount++;
        saveDeleteRow = rowCount++;
        premiumRow = rowCount++;
        localPremiumRow = rowCount++;
        disableAdsRow = rowCount++;
        otherRow = rowCount++;
        replayMessagesRow = rowCount++;
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
        this.adapter = new ListAdapter(context);
        fragmentView = new FrameLayout(context);
        FrameLayout frameLayout = (FrameLayout) fragmentView;

        listView = new RecyclerListView(context);
        listView.setAdapter(this.adapter);
        listView.setLayoutManager(layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        });
        listView.setVerticalScrollBarEnabled(false);
        listView.setVerticalScrollBarEnabled(false);
        frameLayout.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        listView.setOnItemClickListener(((view, position, x, y) -> {
            if (position == saveDeleteRow) {
                boolean newData = !saveDeleteOnElement;
                saveDeleteOnElement = newData;
                HopgramStorage.saveDeleteOnElement = newData;
                HopgramStorage.saveBoolean("SaveDeleteOnElement", newData);
                ((TextCheckCell) view).setChecked(newData);
            } else if (position == localPremiumRow) {
                boolean newData = !localPremiumOnElement;
                localPremiumOnElement = newData;
                HopgramStorage.localPremiumOnElement = newData;
                HopgramStorage.saveBoolean("LocalPremiumOnElement", newData);
                ((TextCheckCell) view).setChecked(newData);
            } else if (position == disableAdsRow) {
                boolean newData = !disableAdsOnElement;
                disableAdsOnElement = newData;
                HopgramStorage.disableAdsOnElement = newData;
                HopgramStorage.saveBoolean("DisableAdsOnElement", newData);
                ((TextCheckCell) view).setChecked(newData);
            } else if (position == replayMessagesRow) {
                boolean newData = !replyMessagesOnElement;
                replyMessagesOnElement = newData;
                HopgramStorage.replyMessagesOnElement = newData;
                HopgramStorage.saveBoolean("ReplyMessagesOnElement", newData);
                ((TextCheckCell) view).setChecked(newData);
            }
        }));
        return fragmentView;
    }

    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            int position = holder.getAdapterPosition();
            return !(position == deleteMessagesRow || position == saveDeleteRow || position == premiumRow ||
                    position == localPremiumRow || position == disableAdsRow || position == otherRow ||
                    position == replayMessagesRow);
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
            switch (viewType) {
                case 0:
                    view = new HeaderCell(mContext, resourceProvider);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 1:
                    view = new TextCell(mContext, resourceProvider);
                    view.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                    break;
                case 2:
                    view = new TextCheckCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                default:
                    view = new TextInfoPrivacyCell(mContext, resourceProvider);
                    view.setBackgroundDrawable(Theme.getThemedDrawableByKey(mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                    break;
            }

            return new RecyclerListView.Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case 0:
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position == deleteMessagesRow) {
                        headerCell.setText(getString("DeletedMessages", R.string.DeletedMessages));
                    } else if (position == premiumRow) {
                        headerCell.setText(getString("Premium", R.string.Premium));
                    } else if (position == otherRow) {
                        headerCell.setText(getString("Other", R.string.Other));
                    }
                    break;
                case 2:
                    TextCheckCell textCheckCell = (TextCheckCell) holder.itemView;
                    if (position == saveDeleteRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.SaveDeletedMessages), saveDeleteOnElement, false);
                    } else if (position == localPremiumRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.LocalPremium), localPremiumOnElement, true);
                    } else if (position == disableAdsRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.DisableAds), disableAdsOnElement, false);
                    } else if (position == replayMessagesRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.RemoveForwardingRestriction), replyMessagesOnElement, false);
                    }
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == deleteMessagesRow || position == premiumRow || position == otherRow) {
                return 0;
            }
            if (position == saveDeleteRow || position == localPremiumRow || position == disableAdsRow ||
                    position == replayMessagesRow) {
                return 2;
            }
            return 3;
        }
    }
}
