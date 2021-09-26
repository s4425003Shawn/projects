/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package comp5216.sydney.edu.au.a1;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * list view adapter to store item in row
 */
public class ItemAdapter extends ArrayAdapter<ToDoItem> {
    public ItemAdapter(Context context, ArrayList<ToDoItem> users) {
        super(context, 0, users);
    }

    /**
     * access to each row in the list view
     * @param position the item position in the list view.
     * @param convertView
     * @param parent the whole list view
     * @return completed view to render on screen
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ToDoItem item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent,
                    false);
        }

        // Lookup view for data population
        TextView tvName = convertView.findViewById(R.id.tvTitle);
        TextView tvHome = convertView.findViewById(R.id.tvDatetime);
        // Populate the data into the template view using the data object
        tvName.setText(item.getTitle());
        tvHome.setText(item.getDatetime());

        if (item.isEdit()) {
            tvHome.setTextColor(Color.GREEN);
        } else {
            tvHome.setTextColor(Color.RED);
        }

        // Return the completed view to render on screen
        return convertView;
    }

}