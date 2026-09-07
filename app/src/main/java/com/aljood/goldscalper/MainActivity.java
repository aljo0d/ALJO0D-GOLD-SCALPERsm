package com.aljood.goldscalper;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class MainActivity extends Activity {

    private LinearLayout root;
    private TextView statusText;
    private TextView connectionText;
    private TextView profitText;
    private TextView tradesText;
    private TextView lastTradeText;
    private Button startButton;
    private Button stopButton;

    private int BG = Color.rgb(8, 12, 18);
    private int CARD = Color.rgb(17, 23, 32);
    private int CARD2 = Color.rgb(22, 29, 40);
    private int WHITE = Color.WHITE;
    private int MUTED = Color.rgb(155, 165, 180);
    private int GREEN = Color.rgb(48, 210, 130);
    private int RED = Color.rgb(245, 75, 85);
    private int GOLD = Color.rgb(235, 180, 65);
    private int BLUE = Color.rgb(75, 145, 255);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);

        buildInterface();
    }

    private void buildInterface() {

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BG);

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(18), dp(12), dp(18), dp(28));
        root.setBackgroundColor(BG);

        scroll.addView(root);

        // =====================================================
        // HEADER
        // =====================================================

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);

        TextView logo = text(
                "ALJO0D",
                24,
                WHITE,
                true
        );

        header.addView(
                logo,
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        TextView live = text(
                "●  LIVE",
                12,
                GREEN,
                true
        );
        live.setGravity(Gravity.CENTER);

        GradientDrawable liveBg = rounded(
                Color.rgb(18, 48, 38),
                100
        );
        live.setBackground(liveBg);
        live.setPadding(dp(12), dp(7), dp(12), dp(7));

        header.addView(live);

        root.addView(header);

        TextView subtitle = text(
                "GOLD SCALPER",
                12,
                MUTED,
                false
        );
        subtitle.setPadding(0, dp(2), 0, dp(8));
        root.addView(subtitle);

        // =====================================================
        // ROBOT CARD
        // =====================================================

        LinearLayout robotCard = card();

        LinearLayout robotTop = new LinearLayout(this);
        robotTop.setOrientation(LinearLayout.HORIZONTAL);
        robotTop.setGravity(Gravity.CENTER_VERTICAL);

        TextView goldIcon = text(
                "Au",
                22,
                GOLD,
                true
        );
        goldIcon.setGravity(Gravity.CENTER);
        goldIcon.setBackground(
                rounded(Color.rgb(53, 43, 22), 18)
        );
        goldIcon.setPadding(
                dp(13), dp(10), dp(13), dp(10)
        );

        robotTop.addView(goldIcon);

        LinearLayout robotInfo = new LinearLayout(this);
        robotInfo.setOrientation(LinearLayout.VERTICAL);
        robotInfo.setPadding(dp(12), 0, 0, 0);

        TextView robotName = text(
                "ALJO0D GOLD SCALPER",
                17,
                WHITE,
                true
        );

        TextView robotDescription = text(
                "XAUUSD • Fast Gold Scalping",
                12,
                MUTED,
                false
        );

        robotInfo.addView(robotName);
        robotInfo.addView(robotDescription);

        robotTop.addView(
                robotInfo,
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        TextView version = text(
                "V1.0",
                11,
                GOLD,
                true
        );

        robotTop.addView(version);

        robotCard.addView(robotTop);

        addDivider(robotCard);

        statusText = text(
                "●  STOPPED",
                13,
                RED,
                true
        );
        robotCard.addView(statusText);

        root.addView(robotCard);

        // =====================================================
        // ACCOUNT CONNECTION
        // =====================================================

        addTitle("ACCOUNT");

        LinearLayout accountCard = card();

        LinearLayout connectionRow = new LinearLayout(this);
        connectionRow.setOrientation(LinearLayout.HORIZONTAL);
        connectionRow.setGravity(Gravity.CENTER_VERTICAL);

        TextView accountCircle = text(
                "MT",
                15,
                WHITE,
                true
        );
        accountCircle.setGravity(Gravity.CENTER);
        accountCircle.setBackground(
                rounded(Color.rgb(35, 46, 65), 100)
        );
        accountCircle.setPadding(
                dp(10), dp(10), dp(10), dp(10)
        );

        connectionRow.addView(accountCircle);

        LinearLayout accountInfo = new LinearLayout(this);
        accountInfo.setOrientation(LinearLayout.VERTICAL);
        accountInfo.setPadding(dp(12), 0, 0, 0);

        TextView accountTitle = text(
                "Trading Account",
                15,
                WHITE,
                true
        );

        connectionText = text(
                "Not connected",
                12,
                MUTED,
                false
        );

        accountInfo.addView(accountTitle);
        accountInfo.addView(connectionText);

        connectionRow.addView(
                accountInfo,
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        Button connect = smallButton(
                "CONNECT",
                BLUE
        );

        connect.setOnClickListener(v -> {
            connectionText.setText("Demo account ready");
            connectionText.setTextColor(GREEN);
        });

        connectionRow.addView(connect);

        accountCard.addView(connectionRow);

        root.addView(accountCard);

        // =====================================================
        // SETTINGS
        // =====================================================

        addTitle("TRADING SETTINGS");

        LinearLayout settings = card();

        addLabel(settings, "ACCOUNT MODE");
        Spinner mode = spinner(
                new String[]{"Demo", "Real"}
        );
        settings.addView(mode);

        addSpace(settings, 8);

        addLabel(settings, "PLATFORM");
        Spinner platform = spinner(
                new String[]{"MT5", "MT4"}
        );
        settings.addView(platform);

        addSpace(settings, 8);

        addLabel(settings, "SYMBOL");
        Spinner symbol = spinner(
                new String[]{"XAUUSD"}
        );
        settings.addView(symbol);

        addSpace(settings, 8);

        addLabel(settings, "LOT SIZE");

        LinearLayout lotRow = new LinearLayout(this);
        lotRow.setOrientation(LinearLayout.HORIZONTAL);

        Spinner lot = spinner(
                new String[]{
                        "Minimum",
                        "0.01",
                        "0.02",
                        "0.05",
                        "0.10",
                        "0.20",
                        "Custom"
                }
        );

        lotRow.addView(
                lot,
                new LinearLayout.LayoutParams(
                        0,
                        dp(52),
                        1
                )
        );

        settings.addView(lotRow);

        addSpace(settings, 8);

        addLabel(settings, "MAX OPEN TRADES");

        Spinner maxTrades = spinner(
                new String[]{
                        "1 Trade",
                        "2 Trades",
                        "3 Trades",
                        "5 Trades",
                        "Custom"
                }
        );

        settings.addView(maxTrades);

        addSpace(settings, 8);

        addLabel(settings, "STOP LOSS");

        EditText sl = input("Enter SL");

        settings.addView(sl);

        addSpace(settings, 8);

        addLabel(settings, "TAKE PROFIT");

        EditText tp = input("Enter TP");

        settings.addView(tp);

        root.addView(settings);

        // =====================================================
        // DASHBOARD
        // =====================================================

        addTitle("LIVE DASHBOARD");

        LinearLayout dashboard = new LinearLayout(this);
        dashboard.setOrientation(LinearLayout.VERTICAL);

        LinearLayout row1 = new LinearLayout(this);
        row1.setOrientation(LinearLayout.HORIZONTAL);

        profitText = dashboardBox(
                row1,
                "FLOATING PROFIT",
                "$0.00",
                GREEN
        );

        tradesText = dashboardBox(
                row1,
                "OPEN TRADES",
                "0",
                WHITE
        );

        dashboard.addView(row1);

        addSpace(dashboard, 10);

        LinearLayout row2 = new LinearLayout(this);
        row2.setOrientation(LinearLayout.HORIZONTAL);

        TextView direction = dashboardBox(
                row2,
                "DIRECTION",
                "WAITING",
                GOLD
        );

        lastTradeText = dashboardBox(
                row2,
                "LAST TRADE",
                "—",
                MUTED
        );

        dashboard.addView(row2);

        root.addView(dashboard);

        // =====================================================
        // START / STOP
        // =====================================================

        addTitle("ROBOT CONTROL");

        startButton = new Button(this);
        startButton.setText("START SCALPER");
        startButton.setTextSize(16);
        startButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        startButton.setTextColor(Color.WHITE);
        startButton.setAllCaps(false);
        startButton.setBackground(
                rounded(GREEN, 18)
        );
        startButton.setPadding(
                dp(10), dp(5), dp(10), dp(5)
        );

        startButton.setOnClickListener(v -> {

            statusText.setText("●  RUNNING");
            statusText.setTextColor(GREEN);

            connectionText.setText("Trading session active");
            connectionText.setTextColor(GREEN);

            profitText.setText("$0.00");
            tradesText.setText("0");

            startButton.setEnabled(false);
            startButton.setAlpha(0.55f);

            stopButton.setEnabled(true);
            stopButton.setAlpha(1f);
        });

        root.addView(
                startButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(58)
                )
        );

        addSpace(root, 10);

        stopButton = new Button(this);
        stopButton.setText("STOP ROBOT");
        stopButton.setTextSize(15);
        stopButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        stopButton.setTextColor(WHITE);
        stopButton.setAllCaps(false);
        stopButton.setBackground(
                rounded(Color.rgb(55, 24, 30), 18)
        );

        stopButton.setOnClickListener(v -> {

            statusText.setText("●  STOPPED");
            statusText.setTextColor(RED);

            connectionText.setText("Session stopped");
            connectionText.setTextColor(MUTED);

            tradesText.setText("0");
            profitText.setText("$0.00");

            startButton.setEnabled(true);
            startButton.setAlpha(1f);

            stopButton.setEnabled(false);
            stopButton.setAlpha(0.55f);
        });

        stopButton.setEnabled(false);
        stopButton.setAlpha(0.55f);

        root.addView(
                stopButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(55)
                )
        );

        // =====================================================
        // TRADE STATUS
        // =====================================================

        addSpace(root, 14);

        LinearLayout notice = card();

        TextView noticeTitle = text(
                "SCALPER STATUS",
                13,
                GOLD,
                true
        );

        notice.addView(noticeTitle);

        TextView noticeText = text(
                "Ready for XAUUSD scalping\n\n" +
                "• No Martingale\n" +
                "• No loss multiplier\n" +
                "• Maximum trades controlled\n" +
                "• Demo testing recommended before Real",
                12,
                MUTED,
                false
        );

        noticeText.setPadding(
                0
