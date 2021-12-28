package com.example.buildup.ui.Products.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.buildup.R;

import java.util.ArrayList;

public class AutoCompleteQueryAdapterJava extends ArrayAdapter<String> {

    public AutoCompleteQueryAdapterJava(@NonNull Context context, ArrayList<String> arrayList) {

        // pass the context and arrayList for the super
        // constructor of the ArrayAdapter class
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_recent_searched_products, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        String text = getItem(position);


        // then according to the position of the view assign the desired TextView 1 for the same
        TextView textView1 = currentItemView.findViewById(R.id.title);
        textView1.setText(text);


        // then return the recyclable view
        return currentItemView;
    }
}
