package com.example.lab5_rimantek;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CurrencyRateAdapter extends ArrayAdapter<CurrencyRate> {

    private final List<CurrencyRate> allItems;     // original list
    private final List<CurrencyRate> visibleItems; // filtered list

    public CurrencyRateAdapter(Context context, List<CurrencyRate> data) {
        super(context, 0, new ArrayList<>(data));
        this.allItems = new ArrayList<>(data);
        this.visibleItems = new ArrayList<>(data);
    }

    @Override
    public int getCount() {
        return visibleItems.size();
    }

    @Override
    public CurrencyRate getItem(int position) {
        return visibleItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item_rate, parent, false);
        }

        CurrencyRate item = getItem(position);

        TextView codeView = row.findViewById(R.id.textCurrencyCode);
        TextView rateView = row.findViewById(R.id.textCurrencyRate);
        TextView nameView = row.findViewById(R.id.textCurrencyName);

        if (item != null) {
            codeView.setText(item.getCode());
            rateView.setText(String.format(Locale.US, "%.3f", item.getRate())); // e.g. 1.235
            nameView.setText(item.getName());
        }

        return row;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint == null
                        ? ""
                        : constraint.toString().trim().toLowerCase(Locale.US);

                List<CurrencyRate> filtered = new ArrayList<>();

                if (query.isEmpty()) {
                    filtered.addAll(allItems);
                } else {
                    for (CurrencyRate item : allItems) {
                        if (item.getCode().toLowerCase(Locale.US).contains(query)
                                || item.getName().toLowerCase(Locale.US).contains(query)) {
                            filtered.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filtered;
                results.count = filtered.size();
                return results;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence constraint, FilterResults results) {
                visibleItems.clear();
                if (results.values instanceof List) {
                    visibleItems.addAll((List<CurrencyRate>) results.values);
                }
                notifyDataSetChanged();
            }
        };
    }

    public void replaceAll(List<CurrencyRate> newItems) {
        allItems.clear();
        allItems.addAll(newItems);
        visibleItems.clear();
        visibleItems.addAll(newItems);
        notifyDataSetChanged();
    }
}
