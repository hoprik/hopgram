package ru.hoprik.hopgram;

import android.content.SharedPreferences;
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
}
