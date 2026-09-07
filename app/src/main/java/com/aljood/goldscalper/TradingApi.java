package com.aljood.goldscalper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TradingApi {

    /*
     * ALJO0D GOLD SCALPER
     * Trading Engine API
     */

    private static final int TIMEOUT = 15000;

    private static final String API_BASE_URL =
            "https://aljo0d-gold-scalpersm-production.up.railway.app";

    public interface ApiCallback {

        void onSuccess(String response);

        void onError(String error);
    }

    public static void startRobot(
            String platform,
            String accountMode,
            String symbol,
            String lot,
            String maxTrades,
            String stopLoss,
            String takeProfit,
            ApiCallback callback
    ) {

        String json =
                "{"
                + "\"action\":\"START\","
                + "\"platform\":\"" + escape(platform) + "\","
                + "\"account_mode\":\"" + escape(accountMode) + "\","
                + "\"symbol\":\"" + escape(symbol) + "\","
                + "\"lot\":\"" + escape(lot) + "\","
                + "\"max_trades\":\"" + escape(maxTrades) + "\","
                + "\"stop_loss\":\"" + escape(stopLoss) + "\","
                + "\"take_profit\":\"" + escape(takeProfit) + "\""
                + "}";

        post("/robot/start", json, callback);
    }

    public static void stopRobot(
            ApiCallback callback
    ) {

        String json =
                "{"
                + "\"action\":\"STOP\""
                + "}";

        post("/robot/stop", json, callback);
    }

    public static void getStatus(
            ApiCallback callback
    ) {

        get("/robot/status", callback);
    }

    public static void getAccount(
            ApiCallback callback
    ) {

        get("/account", callback);
    }

    private static void post(
            String endpoint,
            String json,
            ApiCallback callback
    ) {

        new Thread(() -> {

            HttpURLConnection connection = null;

            try {

                URL url = new URL(
                        API_BASE_URL + endpoint
                );

                connection =
                        (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");

                connection.setConnectTimeout(TIMEOUT);
                connection.setReadTimeout(TIMEOUT);

                connection.setDoInput(true);
                connection.setDoOutput(true);

                connection.setRequestProperty(
                        "Content-Type",
                        "application/json; charset=UTF-8"
                );

                connection.setRequestProperty(
                        "Accept",
                        "application/json"
                );

                OutputStream output =
                        connection.getOutputStream();

                output.write(
                        json.getBytes("UTF-8")
                );

                output.flush();
                output.close();

                int responseCode =
                        connection.getResponseCode();

                InputStream stream;

                if (responseCode >= 200 &&
                        responseCode < 300) {

                    stream =
                            connection.getInputStream();

                } else {

                    stream =
                            connection.getErrorStream();
                }

                String response =
                        readStream(stream);

                if (responseCode >= 200 &&
                        responseCode < 300) {

                    runCallbackSuccess(
                            callback,
                            response
                    );

                } else {

                    runCallbackError(
                            callback,
                            "HTTP " +
                            responseCode +
                            ": " +
                            response
                    );
                }

            } catch (Exception e) {

                runCallbackError(
                        callback,
                        e.getMessage() != null
                                ? e.getMessage()
                                : "Connection error"
                );

            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
            }

        }).start();
    }

    private static void get(
            String endpoint,
            ApiCallback callback
    ) {

        new Thread(() -> {

            HttpURLConnection connection = null;

            try {

                URL url = new URL(
                        API_BASE_URL + endpoint
                );

                connection =
                        (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");

                connection.setConnectTimeout(TIMEOUT);
                connection.setReadTimeout(TIMEOUT);

                connection.setRequestProperty(
                        "Accept",
                        "application/json"
                );

                int responseCode =
                        connection.getResponseCode();

                InputStream stream;

                if (responseCode >= 200 &&
                        responseCode < 300) {

                    stream =
                            connection.getInputStream();

                } else {

                    stream =
                            connection.getErrorStream();
                }

                String response =
                        readStream(stream);

                if (responseCode >= 200 &&
                        responseCode < 300) {

                    runCallbackSuccess(
                            callback,
                            response
                    );

                } else {

                    runCallbackError(
                            callback,
                            "HTTP " +
                            responseCode +
                            ": " +
                            response
                    );
                }

            } catch (Exception e) {

                runCallbackError(
                        callback,
                        e.getMessage() != null
                                ? e.getMessage()
                                : "Connection error"
                );

            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
            }

        }).start();
    }

    private static String readStream(
            InputStream stream
    ) throws Exception {

        if (stream == null) {
            return "";
        }

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                stream,
                                "UTF-8"
                        )
                );

        StringBuilder result =
                new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        reader.close();

        return result.toString();
    }

    private static String escape(
            String value
    ) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }

    private static void runCallbackSuccess(
            ApiCallback callback,
            String response
    ) {

        if (callback == null) {
            return;
        }

        new android.os.Handler(
                android.os.Looper.getMainLooper()
        ).post(() ->
                callback.onSuccess(response)
        );
    }

    private static void runCallbackError(
            ApiCallback callback,
            String error
    ) {

        if (callback == null) {
            return;
        }

        new android.os.Handler(
                android.os.Looper.getMainLooper()
        ).post(() ->
                callback.onError(error)
        );
    }
}
