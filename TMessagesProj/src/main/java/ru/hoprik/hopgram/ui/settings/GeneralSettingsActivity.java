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
import ru.hoprik.hopgram.HopgramStorage;
import ru.hoprik.hopgram.ui.AppSelectActivity;

import java.io.InputStream;

import static org.telegram.messenger.AndroidUtilities.dp;
import static org.telegram.messenger.LocaleController.getString;

public class GeneralSettingsActivity extends BaseFragment {
    RecyclerListView listView;
    private int rowCount = 0;
    private int appRow;
    private int emojiRow;
    private int navbarRow;

    private int switchAppRow;

    private int switchEmojiRow;

    private ListAdapter adapter;

    private String appName;
    @SuppressWarnings("FieldCanBeLocal")
    private LinearLayoutManager layoutManager;
    public static int newGroupRow;
    public static int newSecretRow;
    public static int newChannelRow;
    public static int botsViewRow;
    public static int storiesRow;
    public static int emojiChangeRow;
    public static int contactsRow;
    public static int callsRow;
    public static int savedRow;
    public static int inviteRow;
    public static int helpRow;
    public static boolean newGroupOnElement;
    public static boolean newSecretOnElement;
    public static boolean newChannelOnElement;
    public static boolean botsViewOnElement;
    public static boolean storiesOnElement;
    public static boolean emojiChangeOnElement;
    public static boolean contactsOnElement;
    public static boolean callsOnElement;
    public static boolean savedOnElement;
    public static boolean inviteOnElement;
    public static boolean helpOnElement;

    public GeneralSettingsActivity() {
        super();
    }

    @Override
    public boolean onFragmentCreate() {
        newGroupOnElement = HopgramStorage.newGroupOnElement;
        newSecretOnElement = HopgramStorage.newSecretOnElement;
        newChannelOnElement = HopgramStorage.newChannelOnElement;
        botsViewOnElement = HopgramStorage.botsViewOnElement;
        storiesOnElement = HopgramStorage.storiesOnElement;
        emojiChangeOnElement = HopgramStorage.emojiChangeOnElement;
        contactsOnElement = HopgramStorage.contactsOnElement;
        callsOnElement = HopgramStorage.callsOnElement;
        savedOnElement = HopgramStorage.savedOnElement;
        inviteOnElement = HopgramStorage.inviteOnElement;
        helpOnElement = HopgramStorage.helpOnElement;

        appRow = rowCount++;
        switchAppRow = rowCount++;

        emojiRow = rowCount++;
        switchEmojiRow = rowCount++;
        navbarRow = rowCount++;
        newGroupRow = rowCount++;
        newSecretRow = rowCount++;
        newChannelRow = rowCount++;
        storiesRow = rowCount++;
        botsViewRow = rowCount++;
        emojiChangeRow = rowCount++;
        contactsRow = rowCount++;
        callsRow = rowCount++;
        savedRow = rowCount++;
        inviteRow = rowCount++;
        helpRow = rowCount++;


        SharedPreferences p = UserConfig.getInstance(UserConfig.selectedAccount).getPreferences();
        appName = p.getString("AppName", "Telegram");


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
            if (position == switchAppRow) {
                AppSelectActivity fragment = new AppSelectActivity();
                fragment.setAppSelectActivityDelegate(this::selectApp);
                presentFragment(fragment);
            }
            if (position == switchEmojiRow) {
                showDialog(createSwitchEmojiDialog(getParentActivity(), null));
            }
            if (position == newGroupRow) {
                boolean newData = !newGroupOnElement;
                HopgramStorage.newGroupOnElement = newData;
                HopgramStorage.saveBoolean("NewGroupOnElement", newData);
                ((TextCheckCell) view).setChecked(newData);
            }
            if (position == newSecretRow) {
                boolean newData = !newSecretOnElement;
                HopgramStorage.newSecretOnElement = newData;
                HopgramStorage.saveBoolean("NewSecretOnElement", newData);
                ((TextCheckCell) view).setChecked(newData);
            }
            if (position == newChannelRow) {
                boolean newData = !newChannelOnElement;
                HopgramStorage.newChannelOnElement = newData;
                HopgramStorage.saveBoolean("NewChannelOnElement", newData);
                ((TextCheckCell) view).setChecked(newData);
            }
            if (position == botsViewRow) {
                boolean newData = !botsViewOnElement;
                HopgramStorage.botsViewOnElement = newData;
                HopgramStorage.saveBoolean("BotsViewOnElement", newData);
                ((TextCheckCell) view).setChecked(newData);
            }
            if (position == storiesRow) {
                boolean newData = !storiesOnElement;
                HopgramStorage.storiesOnElement = newData;
                HopgramStorage.saveBoolean("StoriesOnElement", newData);
                ((TextCheckCell) view).setChecked(newData);
            }
            if (position == emojiChangeRow) {
                boolean newData = !emojiChangeOnElement;
                HopgramStorage.emojiChangeOnElement = newData;
                HopgramStorage.saveBoolean("EmojiChangeOnElement", newData);
                ((TextCheckCell) view).setChecked(newData);
            }
            if (position == contactsRow) {
                boolean newData = !contactsOnElement;
                HopgramStorage.contactsOnElement = newData;
                HopgramStorage.saveBoolean("ContactsOnElement", newData);
                ((TextCheckCell) view).setChecked(newData);
            }
            if (position == callsRow) {
                boolean newData = !callsOnElement;
                HopgramStorage.callsOnElement = newData;
                HopgramStorage.saveBoolean("CallsOnElement", newData);
                ((TextCheckCell) view).setChecked(newData);
            }
            if (position == savedRow) {
                boolean newData = !savedOnElement;
                HopgramStorage.savedOnElement = newData;
                HopgramStorage.saveBoolean("SavedOnElement", newData);
                ((TextCheckCell) view).setChecked(newData);
            }
            if (position == inviteRow) {
                boolean newData = !inviteOnElement;
                HopgramStorage.inviteOnElement = newData;
                HopgramStorage.saveBoolean("InviteOnElement", newData);
                ((TextCheckCell) view).setChecked(newData);
            }
            if (position == helpRow) {
                boolean newData = !helpOnElement;
                HopgramStorage.helpOnElement = newData;
                HopgramStorage.saveBoolean("HelpOnElement", newData);
                ((TextCheckCell) view).setChecked(newData);
            }
        }));
        DrawerLayoutAdapter.instance.notifyDataSetChanged();
        return fragmentView;
    }


    private Spannable getPreviewEmoji(String styleEmoji, int alignment, Paint.FontMetricsInt fontMetrics) {
        Spannable s = Spannable.Factory.getInstance().newSpannable("");
        Emoji.EmojiSpan span;
        Drawable drawable;

        drawable = new PreviewEmojiDrawable(styleEmoji);
        span = new Emoji.EmojiSpan(drawable, alignment, fontMetrics);
        span.emoji = "\uD83D\uDE03";
        s.setSpan(span, 0, 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }

    private AlertDialog createSwitchEmojiDialog(Context parentActivity, Theme.ResourcesProvider provider) {
        SharedPreferences preferences = MessagesController.getGlobalMainSettings();
        String selectEmoji = preferences.getString("EmojiStyle", "apple");

        String[] emojis = new String[]{"Apple", "Microsoft", "Microsoft flat", "Google", "Twemoji"};


        final LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity, null);
        for (String emoji : emojis) {
            RadioColorCell cell = new RadioColorCell(parentActivity, provider);
            cell.setPadding(dp(2), 0, dp(2), 0);
            cell.setCheckColor(Theme.getColor(Theme.key_radioBackground, provider), Theme.getColor(Theme.key_dialogRadioBackgroundChecked, provider));
            cell.setTextAndValue(emoji, emoji.toLowerCase().replace(" ", "_").equals(selectEmoji));
            cell.setOnClickListener(v -> {
                int count = linearLayout.getChildCount();
                for (int a1 = 0; a1 < count; a1++) {
                    View child = linearLayout.getChildAt(a1);
                    if (child instanceof RadioColorCell) {
                        ((RadioColorCell) child).setChecked(child == v, true);
                    }
                    HopgramStorage.emojiStyle = emoji.toLowerCase().replace(" ", "_");
                    HopgramStorage.saveString("EmojiStyle", emoji.toLowerCase().replace(" ", "_"));
                    Emoji.reloadEmoji();
                    this.adapter.notifyDataSetChanged();
                    listView.destroyDrawingCache();
                    listView.setVisibility(ListView.INVISIBLE);
                    listView.setVisibility(ListView.VISIBLE);
                    builder.getDismissRunnable().run();
                }
            });
            linearLayout.addView(cell);
        }
        builder.setTitle(LocaleController.getString(R.string.SelectEmoji));
        builder.setView(linearLayout);
        builder.setPositiveButton(LocaleController.getString(R.string.Cancel), null);
        return builder.create();
    }

    private void selectApp(AppSelectActivity.AppID app) {
        ConnectionsManager.getInstance(currentAccount).setAppPaused(true, false);
        BuildVars.APP_ID = Integer.parseInt(app.appId);
        BuildVars.APP_HASH = app.appHash;
        appName = app.name;

        ConnectionsManager.getInstance(currentAccount).setAppPaused(false, false);

        SharedPreferences.Editor editor = UserConfig.getInstance(currentAccount).getPreferences().edit();
        editor.putString("AppName", app.name);
        editor.putString("AppID", app.appId);
        editor.putString("AppHash", app.appHash);
        editor.apply();
        UserConfig.getInstance(currentAccount).saveConfig(true);
        this.adapter.notifyDataSetChanged();
        listView.destroyDrawingCache();
        listView.setVisibility(ListView.INVISIBLE);
        listView.setVisibility(ListView.VISIBLE);
    }

    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            int position = holder.getAdapterPosition();
            return !(position == appRow || position == emojiRow || position == navbarRow ||
                    position == switchEmojiRow || position == switchAppRow ||
                    position == newGroupRow || position == newSecretRow || position == newChannelRow ||
                    position == botsViewRow || position == storiesRow || position == emojiChangeRow ||
                    position == contactsRow || position == callsRow || position == savedRow ||
                    position == inviteRow || position == helpRow);
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
                    if (position == appRow) {
                        headerCell.setText(getString("AppID", R.string.AppID));
                    } else if (position == emojiRow) {
                        headerCell.setText(getString("Emoji", R.string.Emoji));
                    } else if (position == navbarRow) {
                        headerCell.setText(getString("Navbar", R.string.Navbar));
                    }
                    break;
                case 1:
                    TextCell textCell = (TextCell) holder.itemView;
                    textCell.setEnabled(true);
                    if (position == switchAppRow) {
                        textCell.setTextAndValueAndIcon(getString("SelectAppId", R.string.SelectAppId), appName, R.drawable.msg_settings, true);
                    } else if (position == switchEmojiRow) {
                        CharSequence emoji = Emoji.replaceEmoji("\uD83D\uDE03", textCell.getTextView().getPaint().getFontMetricsInt(), AndroidUtilities.dp(20), false);
                        textCell.setTextAndValueAndIcon(getString("SelectEmoji", R.string.SelectEmoji), emoji, R.drawable.msg_emoji_activities, false);
                    }
                    break;
                case 2:
                    TextCheckCell textCheckCell = (TextCheckCell) holder.itemView;
                    if (position == newGroupRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.NewGroup), newGroupOnElement, false);
                    } else if (position == newSecretRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.NewSecretChat), newSecretOnElement, false);
                    } else if (position == newChannelRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.NewChannel), newChannelOnElement, false);
                    } else if (position == botsViewRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.Bots), botsViewOnElement, false);
                    } else if (position == storiesRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.ProfileStories), storiesOnElement, false);
                    } else if (position == emojiChangeRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.SetEmojiStatus), emojiChangeOnElement, false);
                    } else if (position == contactsRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.Contacts), contactsOnElement, false);
                    } else if (position == callsRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.Calls), callsOnElement, false);
                    } else if (position == savedRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.SavedMessages), savedOnElement, false);
                    } else if (position == inviteRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.InviteFriends), inviteOnElement, false);
                    } else if (position == helpRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.TelegramFeatures), helpOnElement, false);
                    }
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == appRow || position == emojiRow || position == navbarRow) {
                return 0;
            }
            if (position == switchAppRow || position == switchEmojiRow) {
                return 1;
            }
            if (position == newGroupRow || position == newSecretRow || position == newChannelRow ||
                    position == botsViewRow || position == storiesRow || position == emojiChangeRow ||
                    position == contactsRow || position == callsRow || position == savedRow ||
                    position == inviteRow || position == helpRow){
                return 2;
            }
            return 3;
        }
    }

    public static class PreviewEmojiDrawable extends Emoji.EmojiDrawable {
        private static Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        private static Rect rect = new Rect();
        String styleEmoji;

        public PreviewEmojiDrawable(String styleEmoji) {
            this.styleEmoji = styleEmoji;
        }

        private Bitmap getPreviewEmojiBitmap(String styleEmoji) {
            Bitmap bitmap = null;
            try {
                int imageResize;
                if (AndroidUtilities.density <= 1.0f) {
                    imageResize = 2;
                } else {
                    imageResize = 1;
                }

                InputStream is = ApplicationLoader.applicationContext.getAssets().open("emoji/" + styleEmoji + "/0_1.png");
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = false;
                opts.inSampleSize = imageResize;
                bitmap = BitmapFactory.decodeStream(is, null, opts);
                is.close();
            } catch (Throwable e) {
                FileLog.e(e);
            }
            return bitmap;
        }

        @Override
        public void draw(Canvas canvas) {
            Rect b;
            b = getBounds();
            if (!canvas.quickReject(b.left, b.top, b.right, b.bottom, Canvas.EdgeType.AA)) {
                canvas.drawBitmap(getPreviewEmojiBitmap(styleEmoji), null, b, paint);
            }
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSPARENT;
        }

        @Override
        public void setAlpha(int alpha) {
            paint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter cf) {

        }

    }
}
