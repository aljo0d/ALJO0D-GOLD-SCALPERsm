package com.aljood.goldscalper;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.Gravity;
import android.widget.*;

public class MainActivity extends Activity {

    LinearLayout root;
    TextView status;
    TextView profit;
    TextView trades;
    TextView lastTrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(28, 28, 28, 28);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setBackgroundColor(Color.rgb(10, 14, 20));

        TextView title = new TextView(this);
        title.setText("ALJO0D GOLD SCALPER");
        title.setTextColor(Color.WHITE);
        title.setTextSize(25);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        root.addView(title, match());

        TextView subtitle = new TextView(this);
        subtitle.setText("XAUUSD • GOLD SCALPING");
        subtitle.setTextColor(Color.LTGRAY);
        subtitle.setTextSize(14);
        subtitle.setGravity(Gravity.CENTER);
        root.addView(subtitle, match());

        addSpace();

        status = info("🔴 STOPPED");
        root.addView(status, match());

        Spinner symbol = new Spinner(this);
        symbol.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"XAUUSD"}
        ));
        root.addView(symbol, match());

        Spinner platform = new Spinner(this);
        platform.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"MT5", "MT4"}
        ));
        root.addView(platform, match());

        Spinner lot = new Spinner(this);
        lot.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Minimum Lot", "0.01", "0.02", "0.05", "0.10"}
        ));
        root.addView(lot, match());

        Spinner maxTrades = new Spinner(this);
        maxTrades.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"1 Trade", "2 Trades", "3 Trades", "Custom"}
        ));
        root.addView(maxTrades, match());

        EditText sl = input("Stop Loss");
        root.addView(sl, match());

        EditText tp = input("Take Profit");
        root.addView(tp, match());

        addSpace();

        Button start = new Button(this);
        start.setText("START");
        start.setTextSize(18);
        start.setOnClickListener(v -> {
            status.setText("🟢 RUNNING");
            status.setTextColor(Color.GREEN);
        });
        root.addView(start, match());

        Button stop = new Button(this);
        stop.setText("STOP");
        stop.setTextSize(18);
        stop.setOnClickListener(v -> {
            status.setText("🔴 STOPPED");
            status.setTextColor(Color.RED);
        });
        root.addView(stop, match());

        addSpace();

        profit = info("Floating Profit: $0.00");
        root.addView(profit, match());

        trades = info("Open Trades: 0");
        root.addView(trades, match());

        lastTrade = info("Last Trade: —");
        root.addView(lastTrade, match());

        TextView warning = new TextView(this);
        warning.setText(
                "⚠ No Martingale\n" +
                "⚠ No loss-based multiplier\n" +
                "⚠ Demo testing recommended before real trading"
        );
        warning.setTextColor(Color.YELLOW);
        warning.setTextSize(13);
        warning.setGravity(Gravity.CENTER);
        warning.setPadding(10, 20, 10, 10);
        root.addView(warning, match());

        setContentView(root);
    }

    private TextView info(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(17);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(10, 12, 10, 12);
        return tv;
    }

    private EditText input(String hint) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setHintTextColor(Color.GRAY);
        e.setTextColor(Color.WHITE);
        e.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL
        );
        return e;
    }

    private LinearLayout.LayoutParams match() {
        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
    }

    private void addSpace() {
        Space s = new Space(this);
        root.addView(s, new LinearLayout.LayoutParams(1, 12));
    }
}
