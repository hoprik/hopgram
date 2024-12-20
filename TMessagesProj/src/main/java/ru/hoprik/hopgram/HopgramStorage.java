package ru.hoprik.hopgram;

import android.content.SharedPreferences;
import android.util.Log;
import org.telegram.messenger.MessagesController;

public class HopgramStorage {
    public static SharedPreferences preferences;
    public static String emojiStyle;
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
    public static boolean enableTosSettings;
    public static boolean saveDeleteOnElement;
    public static boolean localPremiumOnElement;
    public static boolean disableAdsOnElement;
    public static boolean replyMessagesOnElement;
    public static long emojiStatus;
    public static void load() {
        preferences = MessagesController.getGlobalMainSettings();

        emojiStyle = preferences.getString("EmojiStyle", "apple");

        newGroupOnElement = preferences.getBoolean("NewGroupOnElement", true);
        newSecretOnElement = preferences.getBoolean("NewSecretOnElement", false);
        newChannelOnElement = preferences.getBoolean("NewChannelOnElement", false);
        botsViewOnElement = preferences.getBoolean("BotsViewOnElement", true);
        storiesOnElement = preferences.getBoolean("StoriesOnElement", false);
        emojiChangeOnElement = preferences.getBoolean("EmojiChangeOnElement", true);
        contactsOnElement = preferences.getBoolean("ContactsOnElement", true);
        callsOnElement = preferences.getBoolean("CallsOnElement", true);
        savedOnElement = preferences.getBoolean("SavedOnElement", true);
        inviteOnElement = preferences.getBoolean("InviteOnElement", true);
        helpOnElement = preferences.getBoolean("HelpOnElement", true);

        enableTosSettings = preferences.getBoolean("EnableTosSettings", false);

        saveDeleteOnElement = preferences.getBoolean("SaveDeleteOnElement", false);
        localPremiumOnElement = preferences.getBoolean("LocalPremiumOnElement", false);
        disableAdsOnElement = preferences.getBoolean("DisableAdsOnElement", false);
        replyMessagesOnElement = preferences.getBoolean("ReplyMessagesOnElement", false);

        emojiStatus = preferences.getLong("EmojiStatus", -1);
    }

    public static void saveString(String name, String data) {
        SharedPreferences.Editor e = preferences.edit();
        e.putString(name, data);
        e.apply();
    }

    public static void saveBoolean(String name, boolean data) {
        SharedPreferences.Editor e = preferences.edit();
        e.putBoolean(name, data);
        e.apply();
    }

    public static void saveLong(String name, Long data){
        SharedPreferences.Editor e = preferences.edit();
        e.putLong(name, data);
        e.apply();
    }
}
