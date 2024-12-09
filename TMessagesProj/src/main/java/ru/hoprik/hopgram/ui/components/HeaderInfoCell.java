package ru.hoprik.hopgram.ui.components;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Premium.HelloParticles;

public class HeaderInfoCell extends FrameLayout {

    public final TextView titleTextView;

    public HeaderInfoCell(Context context) {
        super(context);

        Drawable arrow = ContextCompat.getDrawable(context, R.drawable.hopgram_ic_fg).mutate();
        Theme.ThemeInfo theme = Theme.getActiveTheme();
        int color = ContextCompat.getColor(context, R.color.ic_background);

        ImageView logo = new ImageView(context);
        logo.setScaleType(ImageView.ScaleType.CENTER);
        logo.setBackground(Theme.createCircleDrawable(AndroidUtilities.dp(108), color));
        //TODO: logo.setBackground(new GradientArrowBackground(context, color));
        logo.setImageDrawable(arrow);
        addView(logo, LayoutHelper.createFrame(108, 108, Gravity.CENTER | Gravity.TOP, 0, 20, 0, 0));

        titleTextView = new TextView(context);
        titleTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        titleTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
        titleTextView.setText(String.format("%s %s", LocaleController.getString(R.string.AppHopgramName), BuildVars.BUILD_VERSION_STRING));
        titleTextView.setLines(1);
        titleTextView.setMaxLines(1);
        titleTextView.setSingleLine(true);
        titleTextView.setPadding(0, 0, 0, 0);
        titleTextView.setGravity(Gravity.CENTER);
        addView(titleTextView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER | Gravity.TOP, 50, 145, 50, 0));

        TextView subtitleTextView = new TextView(context);
        subtitleTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
        subtitleTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        subtitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        subtitleTextView.setLineSpacing(AndroidUtilities.dp(2), 1f);
        subtitleTextView.setText(LocaleController.getString("SettingsDescription", R.string.AppHopgramDescription));
        subtitleTextView.setGravity(Gravity.CENTER);
        subtitleTextView.setLines(0);
        subtitleTextView.setMaxLines(0);
        subtitleTextView.setSingleLine(false);
        subtitleTextView.setPadding(0, 0, 0, 0);
        addView(subtitleTextView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER | Gravity.TOP, 60, 180, 60, 27));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
    }
}
