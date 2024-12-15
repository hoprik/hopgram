package ru.hoprik.hopgram;

import android.content.SharedPreferences;
import org.telegram.messenger.MessagesController;

public class HopgramStorage {
    public static SharedPreferences preferences;
    public static String emojiStyle;

    public static void load() {
        preferences = MessagesController.getGlobalMainSettings();

        emojiStyle = preferences.getString("EmojiStyle", "apple");
    }

    public static void saveString(String name, String data) {
        SharedPreferences.Editor e = preferences.edit();
        e.putString(name, data);
        e.apply();
    }
}
