/*
 * This is the source code of Telegram for Android v. 5.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2018.
 */

package ru.hoprik.hopgram.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.*;
import org.telegram.ui.ActionBar.*;
import org.telegram.ui.Cells.DividerCell;
import org.telegram.ui.Cells.LetterSectionCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Collator;
import java.util.Timer;
import java.util.*;

public class AppSelectActivity extends BaseFragment {

    public interface AppSelectActivityDelegate {
        void didSelectCountry(AppID AppID);
    }

    private RecyclerListView listView;
    private EmptyTextProgressView emptyView;
    private AppIDAdapter listViewAdapter;

    private AppSelectActivityDelegate delegate;

    public AppSelectActivity() {
        super();
    }

    @Override
    public boolean onFragmentCreate() {
        return super.onFragmentCreate();
    }

    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
    }

    @Override
    public boolean isLightStatusBar() {
        int color = Theme.getColor(Theme.key_windowBackgroundWhite, null, true);
        return ColorUtils.calculateLuminance(color) > 0.7f;
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(false);
        actionBar.setTitle(LocaleController.getString(R.string.SelectAppId));

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
        
        listViewAdapter = new AppIDAdapter(context);

        fragmentView = new FrameLayout(context);
        FrameLayout frameLayout = (FrameLayout) fragmentView;

        emptyView = new EmptyTextProgressView(context);
        emptyView.showTextView();
        emptyView.setShowAtCenter(true);
        emptyView.setText(LocaleController.getString(R.string.NoResult));
        frameLayout.addView(emptyView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        listView = new RecyclerListView(context);
        listView.setSectionsType(RecyclerListView.SECTIONS_TYPE_FAST_SCROLL_ONLY);
        listView.setEmptyView(emptyView);
        listView.setVerticalScrollBarEnabled(false);
        listView.setFastScrollEnabled(RecyclerListView.FastScroll.LETTER_TYPE);
        listView.setFastScrollVisible(true);
        listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        listView.setAdapter(listViewAdapter);
        listView.setVerticalScrollbarPosition(LocaleController.isRTL ? RecyclerListView.SCROLLBAR_POSITION_LEFT : RecyclerListView.SCROLLBAR_POSITION_RIGHT);
        frameLayout.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        listView.setOnItemClickListener((view, position) -> {
            AppID appID;
            int section = listViewAdapter.getSectionForPosition(position);
            int row = listViewAdapter.getPositionInSectionForPosition(position);
            if (row < 0 || section < 0) {
                return;
            }
            appID = listViewAdapter.getItem(section, row);
            if (position < 0) {
                return;
            }
            finishFragment();
            if (appID != null && delegate != null) {
                delegate.didSelectCountry(appID);
            }
        });

        listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    AndroidUtilities.hideKeyboard(getParentActivity().getCurrentFocus());
                }
            }
        });

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listViewAdapter != null) {
            listViewAdapter.notifyDataSetChanged();
        }
    }

    public void setAppSelectActivityDelegate(AppSelectActivityDelegate delegate) {
        this.delegate = delegate;
    }

    public static class AppID {
        public String name;
        public String shortname;
        public String appId;
        public String appHash;
    }

    public class AppIDAdapter extends RecyclerListView.SectionsAdapter {
        private final static int TYPE_COUNTRY = 0, TYPE_DIVIDER = 1;

        private Context mContext;
        private final HashMap<String, ArrayList<AppID>> apps = new HashMap<>();
        private ArrayList<String> sortedApps = new ArrayList<>();

        public AppIDAdapter(Context context) {
            mContext = context;
            try {
                InputStream stream = ApplicationLoader.applicationContext.getResources().getAssets().open("clients.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] args = line.split(";");
                    AppID c = new AppID();
                    c.name = args[2];
                    c.appId = args[0];
                    c.appHash = args[1];
                    c.shortname = "";
                    String n = c.name.substring(0, 1).toUpperCase();
                    ArrayList<AppID> arr = apps.get(n);
                    if (arr == null) {
                        arr = new ArrayList<>();
                        apps.put(n, arr);
                        sortedApps.add(n);
                    }
                    arr.add(c);
                }
                reader.close();
                stream.close();
            } catch (Exception e) {
                FileLog.e(e);
            }
            Comparator<String> comparator;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Collator collator = Collator.getInstance(LocaleController.getInstance().getCurrentLocale() != null ? LocaleController.getInstance().getCurrentLocale() : Locale.getDefault());
                comparator = collator::compare;
            } else {
                comparator = String::compareTo;
            }
            
            Collections.sort(sortedApps, comparator);
            
            for (ArrayList<AppID> arr : apps.values()) {
                Collections.sort(arr, (AppID, country2) -> comparator.compare(AppID.name, country2.name));
            }
        }

        public HashMap<String, ArrayList<AppID>> getCountries() {
            return apps;
        }

        @Override
        public AppID getItem(int section, int position) {
            if (section < 0 || section >= sortedApps.size()) {
                return null;
            }
            ArrayList<AppID> arr = apps.get(sortedApps.get(section));
            if (position < 0 || position >= arr.size()) {
                return null;
            }
            return arr.get(position);
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder, int section, int row) {
            ArrayList<AppID> arr = apps.get(sortedApps.get(section));
            return row < arr.size();
        }

        @Override
        public int getSectionCount() {
            return sortedApps.size();
        }

        @Override
        public int getCountForSection(int section) {
            int count = apps.get(sortedApps.get(section)).size();
            if (section != sortedApps.size() - 1) {
                count++;
            }
            return count;
        }

        @Override
        public View getSectionHeaderView(int section, View view) {
            return null;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case TYPE_COUNTRY:
                    view = createSettingsCell(mContext);
                    break;
                case TYPE_DIVIDER:
                default:
                    view = new DividerCell(mContext);
                    view.setPadding(AndroidUtilities.dp(24), AndroidUtilities.dp(8), AndroidUtilities.dp(24), AndroidUtilities.dp(8));
                    break;
            }
            return new RecyclerListView.Holder(view);
        }

        @Override
        public void onBindViewHolder(int section, int position, RecyclerView.ViewHolder holder) {
            if (holder.getItemViewType() == TYPE_COUNTRY) {
                ArrayList<AppID> arr = apps.get(sortedApps.get(section));
                AppID c = arr.get(position);
                TextSettingsCell settingsCell = (TextSettingsCell) holder.itemView;
                settingsCell.setTextAndValue(getCountryNameWithFlag(c), null, false);
            }
        }

        @Override
        public int getItemViewType(int section, int position) {
            ArrayList<AppID> arr = apps.get(sortedApps.get(section));
            return position < arr.size() ? TYPE_COUNTRY : TYPE_DIVIDER;
        }

        @Override
        public String getLetter(int position) {
            int section = getSectionForPosition(position);
            if (section == -1) {
                section = sortedApps.size() - 1;
            }
            return sortedApps.get(section);
        }

        @Override
        public void getPositionForScrollProgress(RecyclerListView listView, float progress, int[] position) {
            position[0] = (int) (getItemCount() * progress);
            position[1] = 0;
        }
    }

    private static TextSettingsCell createSettingsCell(Context context) {
        TextSettingsCell view = new TextSettingsCell(context);
        view.setPadding(AndroidUtilities.dp(LocaleController.isRTL ? 16 : 12), 0, AndroidUtilities.dp(LocaleController.isRTL ? 12 : 16), 0);
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            private NotificationCenter.NotificationCenterDelegate listener = (id, account, args) -> {
                if (id == NotificationCenter.emojiLoaded) {
                    view.getTextView().invalidate();
                }
            };

            @Override
            public void onViewAttachedToWindow(View v) {
                NotificationCenter.getGlobalInstance().addObserver(listener, NotificationCenter.emojiLoaded);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                NotificationCenter.getGlobalInstance().removeObserver(listener, NotificationCenter.emojiLoaded);
            }
        });
        return view;
    }

    private static CharSequence getCountryNameWithFlag(AppID c) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        String flag = LocaleController.getLanguageFlag(c.shortname);
        if (flag != null) {
            sb.append(flag).append(" ");
            sb.setSpan(new ReplacementSpan() {
                @Override
                public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
                    return AndroidUtilities.dp(16);
                }

                @Override
                public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {}
            }, flag.length(), flag.length() + 1, 0);
        }
        sb.append(c.name);
        return sb;
    }

    @Override
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> themeDescriptions = new ArrayList<>();

        themeDescriptions.add(new ThemeDescription(fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));

        themeDescriptions.add(new ThemeDescription(actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault));
        themeDescriptions.add(new ThemeDescription(listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault));
        themeDescriptions.add(new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        themeDescriptions.add(new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        themeDescriptions.add(new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        themeDescriptions.add(new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, Theme.key_actionBarDefaultSearch));
        themeDescriptions.add(new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, Theme.key_actionBarDefaultSearchPlaceholder));

        themeDescriptions.add(new ThemeDescription(listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));

        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));

        themeDescriptions.add(new ThemeDescription(listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, Theme.key_fastScrollActive));
        themeDescriptions.add(new ThemeDescription(listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, Theme.key_fastScrollInactive));
        themeDescriptions.add(new ThemeDescription(listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, Theme.key_fastScrollText));

        themeDescriptions.add(new ThemeDescription(emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_emptyListPlaceholder));

        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteValueText));

        themeDescriptions.add(new ThemeDescription(listView, ThemeDescription.FLAG_SECTIONS, new Class[]{LetterSectionCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4));

        return themeDescriptions;
    }
}
