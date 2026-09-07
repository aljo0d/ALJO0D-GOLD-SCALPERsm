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

    private Button startButton;
    private Button stopButton;

    private Spinner accountModeSpinner;
    private Spinner platformSpinner;
    private Spinner symbolSpinner;
    private Spinner lotSpinner;
    private Spinner maxTradesSpinner;

    private EditText slInput;
    private EditText tpInput;

    private final int BG = Color.rgb(8, 12, 18);
    private final int CARD = Color.rgb(17, 23, 32);
    private final int CARD2 = Color.rgb(23, 30, 41);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(155, 165, 180);
    private final int GREEN = Color.rgb(48, 210, 130);
    private final int RED = Color.rgb(245, 75, 85);
    private final int GOLD = Color.rgb(235, 180, 65);
    private final int BLUE = Color.rgb(75, 145, 255);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);

        createUI();
    }

    private void createUI() {

        ScrollView scrollView = new ScrollView(this);
        scrollView.setBackgroundColor(BG);
        scrollView.setFillViewport(true);

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(18), dp(14), dp(18), dp(30));
        root.setBackgroundColor(BG);

        scrollView.addView(root);

        // HEADER
        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);

        TextView logo = makeText(
                "ALJO0D",
                25,
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

        TextView live = makeText(
                "● LIVE",
                12,
                GREEN,
                true
        );

        live.setGravity(Gravity.CENTER);
        live.setPadding(dp(12), dp(7), dp(12), dp(7));
        live.setBackground(round(GREEN_DARK(), 50));

        header.addView(live);

        root.addView(header);

        TextView subtitle = makeText(
                "GOLD SCALPER",
                12,
                MUTED,
                false
        );

        subtitle.setPadding(0, dp(2), 0, dp(12));
        root.addView(subtitle);

        // ROBOT CARD
        LinearLayout robotCard = makeCard();

        LinearLayout robotRow = new LinearLayout(this);
        robotRow.setGravity(Gravity.CENTER_VERTICAL);

        TextView gold = makeText(
                "Au",
                22,
                GOLD,
                true
        );

        gold.setGravity(Gravity.CENTER);
        gold.setPadding(
                dp(14),
                dp(11),
                dp(14),
                dp(11)
        );

        gold.setBackground(
                round(Color.rgb(55, 44, 22), 18)
        );

        robotRow.addView(gold);

        LinearLayout robotInfo = new LinearLayout(this);
        robotInfo.setOrientation(LinearLayout.VERTICAL);
        robotInfo.setPadding(dp(12), 0, 0, 0);

        TextView robotName = makeText(
                "ALJO0D GOLD SCALPER",
                16,
                WHITE,
                true
        );

        TextView robotDesc = makeText(
                "XAUUSD • FAST GOLD SCALPING",
                11,
                MUTED,
                false
        );

        robotInfo.addView(robotName);
        robotInfo.addView(robotDesc);

        robotRow.addView(
                robotInfo,
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        TextView version = makeText(
                "V1.0",
                10,
                GOLD,
                true
        );

        robotRow.addView(version);

        robotCard.addView(robotRow);

        addDivider(robotCard);

        statusText = makeText(
                "● STOPPED",
                13,
                RED,
                true
        );

        robotCard.addView(statusText);

        root.addView(robotCard);

        // ACCOUNT
        addSectionTitle("ACCOUNT");

        LinearLayout accountCard = makeCard();

        LinearLayout accountRow = new LinearLayout(this);
        accountRow.setGravity(Gravity.CENTER_VERTICAL);

        TextView mt = makeText(
                "MT",
                14,
                WHITE,
                true
        );

        mt.setGravity(Gravity.CENTER);
        mt.setBackground(
                round(Color.rgb(35, 46, 65), 50)
        );

        mt.setPadding(
                dp(12),
                dp(12),
                dp(12),
                dp(12)
        );

        accountRow.addView(mt);

        LinearLayout accountInfo = new LinearLayout(this);
        accountInfo.setOrientation(LinearLayout.VERTICAL);
        accountInfo.setPadding(dp(12), 0, 0, 0);

        TextView accountTitle = makeText(
                "Trading Account",
                15,
                WHITE,
                true
        );

        connectionText = makeText(
                "Not connected",
                11,
                MUTED,
                false
        );

        accountInfo.addView(accountTitle);
        accountInfo.addView(connectionText);

        accountRow.addView(
                accountInfo,
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        Button connectButton = smallButton(
                "CONNECT",
                BLUE
        );

        connectButton.setOnClickListener(v ->
                connectAccount()
        );

        accountRow.addView(connectButton);

        accountCard.addView(accountRow);
        root.addView(accountCard);

        // SETTINGS
        addSectionTitle("TRADING SETTINGS");

        LinearLayout settingsCard = makeCard();

        addLabel(settingsCard, "ACCOUNT MODE");

        accountModeSpinner = makeSpinner(
                new String[]{
                        "Demo",
                        "Real"
                }
        );

        settingsCard.addView(accountModeSpinner);

        addGap(settingsCard, 8);

        addLabel(settingsCard, "PLATFORM");

        platformSpinner = makeSpinner(
                new String[]{
                        "MT5",
                        "MT4"
                }
        );

        settingsCard.addView(platformSpinner);

        addGap(settingsCard, 8);

        addLabel(settingsCard, "SYMBOL");

        symbolSpinner = makeSpinner(
                new String[]{
                        "XAUUSD"
                }
        );

        settingsCard.addView(symbolSpinner);

        addGap(settingsCard, 8);

        addLabel(settingsCard, "LOT SIZE");

        lotSpinner = makeSpinner(
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

        settingsCard.addView(lotSpinner);

        addGap(settingsCard, 8);

        addLabel(settingsCard, "MAX OPEN TRADES");

        maxTradesSpinner = makeSpinner(
                new String[]{
                        "1 Trade",
                        "2 Trades",
                        "3 Trades",
                        "5 Trades",
                        "Custom"
                }
        );

        settingsCard.addView(maxTradesSpinner);

        addGap(settingsCard, 8);

        addLabel(settingsCard, "STOP LOSS");

        slInput = makeInput(
                "Enter Stop Loss"
        );

        settingsCard.addView(slInput);

        addGap(settingsCard, 8);

        addLabel(settingsCard, "TAKE PROFIT");

        tpInput = makeInput(
                "Enter Take Profit"
        );

        settingsCard.addView(tpInput);

        root.addView(settingsCard);

        // DASHBOARD
        addSectionTitle("LIVE DASHBOARD");

        LinearLayout dashboardRow1 =
                new LinearLayout(this);

        profitText = dashboardItem(
                dashboardRow1,
                "FLOATING PROFIT",
                "$0.00",
                GREEN
        );

        tradesText = dashboardItem(
                dashboardRow1,
                "OPEN TRADES",
                "0",
                WHITE
        );

        root.addView(dashboardRow1);

        addGap(root, 10);

        LinearLayout dashboardRow2 =
                new LinearLayout(this);

        dashboardItem(
                dashboardRow2,
                "DIRECTION",
                "WAITING",
                GOLD
        );

        dashboardItem(
                dashboardRow2,
                "LAST TRADE",
                "—",
                MUTED
        );

        root.addView(dashboardRow2);

        // CONTROL
        addSectionTitle("ROBOT CONTROL");

        startButton = new Button(this);

        startButton.setText("START SCALPER");
        startButton.setTextSize(16);
        startButton.setTextColor(Color.WHITE);
        startButton.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        startButton.setAllCaps(false);

        startButton.setBackground(
                round(GREEN, 18)
        );

        startButton.setOnClickListener(
                v -> startRobot()
        );

        root.addView(
                startButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(58)
                )
        );

        addGap(root, 10);

        stopButton = new Button(this);

        stopButton.setText("STOP ROBOT");
        stopButton.setTextSize(15);
        stopButton.setTextColor(WHITE);

        stopButton.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        stopButton.setAllCaps(false);

        stopButton.setBackground(
                round(
                        Color.rgb(65, 25, 32),
                        18
                )
        );

        stopButton.setEnabled(false);
        stopButton.setAlpha(0.5f);

        stopButton.setOnClickListener(
                v -> stopRobot()
        );

        root.addView(
                stopButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(55)
                )
        );

        // NOTICE
        addGap(root, 14);

        LinearLayout notice = makeCard();

        TextView noticeTitle = makeText(
                "SCALPER STATUS",
                12,
                GOLD,
                true
        );

        notice.addView(noticeTitle);

        TextView noticeText = makeText(
                "Ready for XAUUSD scalping\n\n" +
                "• No Martingale\n" +
                "• No loss multiplier\n" +
                "• Maximum trades controlled\n" +
                "• Demo testing before Real trading",
                12,
                MUTED,
                false
        );

        noticeText.setPadding(
                0,
                dp(8),
                0,
                0
        );

        notice.addView(noticeText);

        root.addView(notice);

        // FOOTER
        TextView footer = makeText(
                "ALJO0D GOLD SCALPER • TRADING ENGINE",
                10,
                Color.rgb(90, 100, 115),
                false
        );

        footer.setGravity(Gravity.CENTER);

        footer.setPadding(
                0,
                dp(20),
                0,
                0
        );

        root.addView(footer);

        setContentView(scrollView);
    }

    /*
     * CONNECT ACCOUNT
     *
     * في هذه المرحلة نتأكد من أن التطبيق
     * يستطيع الوصول إلى Trading Engine.
     */
    private void connectAccount() {

        connectionText.setText(
                "Connecting..."
        );

        connectionText.setTextColor(GOLD);

        TradingApi.getAccount(
                new TradingApi.ApiCallback() {

                    @Override
                    public void onSuccess(
                            String response
                    ) {

                        connectionText.setText(
                                "Engine connected"
                        );

                        connectionText.setTextColor(
                                GREEN
                        );

                        Toast.makeText(
                                MainActivity.this,
                                "Trading Engine connected",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    @Override
                    public void onError(
                            String error
                    ) {

                        connectionText.setText(
                                "Connection failed"
                        );

                        connectionText.setTextColor(
                                RED
                        );

                        Toast.makeText(
                                MainActivity.this,
                                "Connection failed",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    /*
     * START ROBOT
     *
     * يرسل جميع إعدادات المستخدم إلى Railway.
     */
    private void startRobot() {

        String accountMode =
                accountModeSpinner
                        .getSelectedItem()
                        .toString();

        String platform =
                platformSpinner
                        .getSelectedItem()
                        .toString();

        String symbol =
                symbolSpinner
                        .getSelectedItem()
                        .toString();

        String lot =
                lotSpinner
                        .getSelectedItem()
                        .toString();

        String maxTrades =
                maxTradesSpinner
                        .getSelectedItem()
                        .toString();

        String stopLoss =
                slInput.getText()
                        .toString()
                        .trim();

        String takeProfit =
                tpInput.getText()
                        .toString()
                        .trim();

        startButton.setEnabled(false);
        startButton.setAlpha(0.55f);

        connectionText.setText(
                "Starting..."
        );

        connectionText.setTextColor(GOLD);

        TradingApi.startRobot(
                platform,
                accountMode,
                symbol,
                lot,
                maxTrades,
                stopLoss,
                takeProfit,
                new TradingApi.ApiCallback() {

                    @Override
                    public void onSuccess(
                            String response
                    ) {

                        statusText.setText(
                                "● RUNNING"
                        );

                        statusText.setTextColor(
                                GREEN
                        );

                        connectionText.setText(
                                "Trading session active"
                        );

                        connectionText.setTextColor(
                                GREEN
                        );

                        profitText.setText(
                                "$0.00"
                        );

                        tradesText.setText(
                                "0"
                        );

                        stopButton.setEnabled(
                                true
                        );

                        stopButton.setAlpha(
                                1f
                        );

                        Toast.makeText(
                                MainActivity.this,
                                "START command accepted",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    @Override
                    public void onError(
                            String error
                    ) {

                        startButton.setEnabled(
                                true
                        );

                        startButton.setAlpha(
                                1f
                        );

                        connectionText.setText(
                                "Start failed"
                        );

                        connectionText.setTextColor(
                                RED
                        );

                        Toast.makeText(
                                MainActivity.this,
                                "Start failed",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    /*
     * STOP ROBOT
     */
    private void stopRobot() {

        stopButton.setEnabled(false);
        stopButton.setAlpha(0.5f);

        connectionText.setText(
                "Stopping..."
        );

        connectionText.setTextColor(GOLD);

        TradingApi.stopRobot(
                new TradingApi.ApiCallback() {

                    @Override
                    public void onSuccess(
                            String response
                    ) {

                        statusText.setText(
                                "● STOPPED"
                        );

                        statusText.setTextColor(
                                RED
                        );

                        connectionText.setText(
                                "Session stopped"
                        );

                        connectionText.setTextColor(
                                MUTED
                        );

                        profitText.setText(
                                "$0.00"
                        );

                        tradesText.setText(
                                "0"
                        );

                        startButton.setEnabled(
                                true
                        );

                        startButton.setAlpha(
                                1f
                        );

                        Toast.makeText(
                                MainActivity.this,
                                "STOP command accepted",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    @Override
                    public void onError(
                            String error
                    ) {

                        stopButton.setEnabled(
                                true
                        );

                        stopButton.setAlpha(
                                1f
                        );

                        connectionText.setText(
                                "Stop failed"
                        );

                        connectionText.setTextColor(
                                RED
                        );

                        Toast.makeText(
                                MainActivity.this,
                                "Stop failed",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    private TextView makeText(
            String text,
            float size,
            int color,
            boolean bold
    ) {

        TextView view =
                new TextView(this);

        view.setText(text);
        view.setTextSize(size);
        view.setTextColor(color);

        if (bold) {

            view.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD
            );
        }

        return view;
    }

    private LinearLayout makeCard() {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                dp(16),
                dp(16),
                dp(16),
                dp(16)
        );

        card.setBackground(
                round(CARD, 20)
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                0,
                dp(4),
                0,
                dp(6)
        );

        card.setLayoutParams(params);

        return card;
    }

    private void addSectionTitle(
            String title
    ) {

        TextView titleView =
                makeText(
                        title,
                        11,
                        MUTED,
                        true
                );

        titleView.setPadding(
                dp(3),
                dp(16),
                0,
                dp(7)
        );

        root.addView(titleView);
    }

    private void addLabel(
            LinearLayout parent,
            String label
    ) {

        TextView labelView =
                makeText(
                        label,
                        10,
                        MUTED,
                        true
                );

        labelView.setPadding(
                dp(2),
                0,
                0,
                dp(5)
        );

        parent.addView(labelView);
    }

    private Spinner makeSpinner(
            String[] values
    ) {

        Spinner spinner =
                new Spinner(this);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_item,
                        values
                ) {

                    @Override
                    public View getView(
                            int position,
                            View convertView,
                            ViewGroup parent
                    ) {

                        TextView view =
                                (TextView)
                                        super.getView(
                                                position,
                                                convertView,
                                                parent
                                        );

                        view.setTextColor(
                                WHITE
                        );

                        view.setTextSize(14);

                        view.setGravity(
                                Gravity.CENTER_VERTICAL
                        );

                        return view;
                    }
                };

        adapter.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item
        );

        spinner.setAdapter(adapter);

        spinner.setBackground(
                round(CARD2, 14)
        );

        return spinner;
    }

    private EditText makeInput(
            String hint
    ) {

        EditText input =
                new EditText(this);

        input.setHint(hint);

        input.setHintTextColor(
                Color.rgb(105, 115, 130)
        );

        input.setTextColor(WHITE);
        input.setTextSize(14);
        input.setSingleLine(true);

        input.setPadding(
                dp(15),
                0,
                dp(15),
                0
        );

        input.setBackground(
                round(CARD2, 14)
        );

        return input;
    }

    private Button smallButton(
            String title,
            int color
    ) {

        Button button =
                new Button(this);

        button.setText(title);
        button.setTextSize(11);
        button.setTextColor(WHITE);

        button.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        button.setAllCaps(false);

        button.setBackground(
                round(color, 12)
        );

        return button;
    }

    private TextView dashboardItem(
            LinearLayout row,
            String title,
            String value,
            int valueColor
    ) {

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setGravity(
                Gravity.CENTER
        );

        box.setPadding(
                dp(8),
                dp(14),
                dp(8),
                dp(14)
        );

        box.setBackground(
                round(CARD, 18)
        );

        TextView titleView =
                makeText(
                        title,
                        9,
                        MUTED,
                        true
                );

        titleView.setGravity(
                Gravity.CENTER
        );

        TextView valueView =
                makeText(
                        value,
                        18,
                        valueColor,
                        true
                );

        valueView.setGravity(
                Gravity.CENTER
        );

        box.addView(titleView);
        box.addView(valueView);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        0,
                        dp(85),
                        1
                );

        params.setMargins(
                dp(3),
                0,
                dp(3),
                0
        );

        row.addView(
                box,
                params
        );

        return valueView;
    }

    private void addDivider(
            LinearLayout parent
    ) {

        View divider =
                new View(this);

        divider.setBackgroundColor(
                Color.rgb(38, 46, 58)
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(1)
                );

        params.setMargins(
                0,
                dp(14),
                0,
                dp(12)
        );

        parent.addView(
                divider,
                params
        );
    }

    private void addGap(
            LinearLayout parent,
            int height
    ) {

        Space space =
                new Space(this);

        parent.addView(
                space,
                new LinearLayout.LayoutParams(
                        1,
                        dp(height)
                )
        );
    }

    private GradientDrawable round(
            int color,
            int radius
    ) {

        GradientDrawable drawable =
                new GradientDrawable();

        drawable.setColor(color);
        drawable.setCornerRadius(
                dp(radius)
        );

        return drawable;
    }

    private int GREEN_DARK() {

        return Color.rgb(
                18,
                48,
                38
        );
    }

    private int dp(int value) {

        return (int) (
                value *
                getResources()
                        .getDisplayMetrics()
                        .density
        );
    }
}
