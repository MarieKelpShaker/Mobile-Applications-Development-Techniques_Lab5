package com.example.lab5_rimantek;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class DataLoader extends AsyncTask<String, Void, List<CurrencyRate>> {


    public interface Callback {
        void onRatesLoaded(List<CurrencyRate> rates);
        void onLoadFailed(Exception error);
    }


    private final Callback callback;
    private Exception failure;


    public DataLoader(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected List<CurrencyRate> doInBackground(String... urls) {
        String urlString = (urls != null && urls.length > 0) ? urls[0] : null;
        if (urlString == null) return Collections.emptyList();


        HttpURLConnection connection = null;
        InputStream in = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10_000);
            connection.setReadTimeout(10_000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);


            int code = connection.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP " + code);
            }


            in = new BufferedInputStream(connection.getInputStream());
            return Parser.parse(in);
        } catch (Exception e) {
            failure = e;
            return Collections.emptyList();
        } finally {
            if (in != null) try { in.close(); } catch (IOException ignored) {}
            if (connection != null) connection.disconnect();
        }
    }

    @Override
    protected void onPostExecute(List<CurrencyRate> currencyRates) {
        if (failure != null) {
            callback.onLoadFailed(failure);
        } else {
            callback.onRatesLoaded(currencyRates);
        }
    }
}