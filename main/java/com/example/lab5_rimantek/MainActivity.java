package com.example.lab5_rimantek;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DataLoader.Callback {

    private static final String FEED_URL = "https://www.floatrates.com/daily/usd.xml"; // base USD

    private CurrencyRateAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listRates);
        EditText editFilter = findViewById(R.id.editFilter);

        adapter = new CurrencyRateAdapter(this, new ArrayList<>());
        listView.setAdapter(adapter);

        // Live filter as the user types
        editFilter.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }
            @Override public void afterTextChanged(Editable s) { }
        });

        // Kick off background load
        new DataLoader(this).execute(FEED_URL);
    }

    @Override
    public void onRatesLoaded(List<CurrencyRate> rates) {
        adapter.replaceAll(rates);
        Toast.makeText(this, "Loaded " + rates.size() + " rates", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadFailed(Exception error) {
        Toast.makeText(this, "Failed to load rates: " + error.getMessage(),
                Toast.LENGTH_LONG).show();
    }
}
