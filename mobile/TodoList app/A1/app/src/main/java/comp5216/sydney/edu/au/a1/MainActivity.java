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

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *  Display a list view in the home page and each row in list will contain a title and timestamp
 *  People can add and edit one of item in the list view.
 */
public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    private ArrayList<ToDoItem> mItems; //use to store element in custom list view
    public final int EDIT_ITEM_REQUEST_CODE = 647; //differentiate if the request is editing
    public final int ADD_ITEM_REQUEST_CODE = 658; //differentiate if the request is adding new
    private ItemAdapter mAdapter;
    private ToDoItemDao mToDoItemDao;//Access database instance

    /**
     *
     * @param view
     */
    public void onAddItemClick(View view) {
        Intent intent = new Intent(MainActivity.this, EditToDoItemActivity.class);
        // put "extras" into the bundle for access in the edit activity
        // brings up the second activity
        startActivityForResult(intent, ADD_ITEM_REQUEST_CODE);
        mAdapter.notifyDataSetChanged();

    }

    /**
     * read data from data base and reflect data to the list view.
     */
    @SuppressLint("StaticFieldLeak")
    private void readItemsFromDatabase() {
        //Use asynchronous task to run query on the background and wait for result
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    //read items from database
                    List<ToDoItem> itemsFromDB = mToDoItemDao.getItemSortByDescDateTime();
                    mItems = new ArrayList<ToDoItem>();
                    if (itemsFromDB != null & itemsFromDB.size() > 0) {
                        mItems.addAll(itemsFromDB);
                    }
                    return null;
                }
            }.execute().get();
        } catch (Exception ex) {
            Log.e("readItemsFromDatabase", ex.getStackTrace().toString());
        }
    }

    /**
     * save new changed list view element into the database
     */
    @SuppressLint("StaticFieldLeak")
    private void saveItemsToDatabase() {
        //Use asynchronous task to run query on the background to avoid locking UI
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    //delete all items and re-insert
                    mToDoItemDao.deleteAll();
                    for (ToDoItem todo : mItems) {
                        mToDoItemDao.insertItem(todo);
                    }
                    mItems.clear();
                    mItems.addAll(mToDoItemDao.getItemSortByDescDateTime());
                    return null;
                }
            }.execute().get();
        } catch (Exception e) {
            Log.e("saveItemsToDatabase", e.getStackTrace().toString());
        }
    }

    /**
     * retrieve the data transfer from Edit page
     * save transfer data to database
     * update list view
     * @param requestCode differentiate if the request is edit or add new
     * @param resultCode now the edit page is end.
     * @param data data transfer from Edit page.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_ITEM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Extract name value from result extras
                String editedItem = data.getExtras().getString("item");
                String dateTime = new SimpleDateFormat("yyyy.MM.dd" +
                        " 'at' HH:mm:ss", Locale.getDefault()).format(new Date());
                int position = data.getIntExtra("position", -1);
                ToDoItem item = new ToDoItem(editedItem, dateTime);
                item.setEdit(true);
                mItems.set(position, item);
                saveItemsToDatabase();
                Log.i("Updated Item in list:", editedItem + ",position:"
                        + position);
                Toast.makeText(this, "updated:" +
                        editedItem, Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == ADD_ITEM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // Extract name value from result extras
                String editedItem = data.getExtras().getString("item");
                String dateTime = new SimpleDateFormat("yyyy.MM.dd" +
                        " 'at' HH:mm:ss", Locale.getDefault()).format(new Date());


                mItems.add(new ToDoItem(editedItem, dateTime));
                saveItemsToDatabase();
                Toast.makeText(this, "updated:" +
                        editedItem, Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * click shortly direct to edit page and send item title and time stamp to edit page
     * click in quite long time will pop up a delete window to ask to delete an item.
     */
    private void setupListViewListener() {
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int
                    position, long rowId) {
                Log.i("MainActivity", "Long Clicked item " + position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.dialog_delete_title)
                        .setMessage(R.string.dialog_delete_msg)
                        .setPositiveButton(R.string.yes, new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        mItems.remove(position);
                                        saveItemsToDatabase();
                                        // Notify listView adapt to update the list
                                        mAdapter.notifyDataSetChanged();
                                    }
                                })
                        .setNegativeButton(R.string.cancel, new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // User cancelled the dialog
// Nothing happens
                                    }
                                });
                builder.create().show();
                return true;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long
                    id) {
                ToDoItem updateItem = mAdapter.getItem(position);
                Log.i("MainActivity", "Clicked item " +
                        position + ": " + updateItem.getTitle());
                Intent intent = new Intent(MainActivity.this,
                        EditToDoItemActivity.class);
                if (intent != null) {
                    // put "extras" into the bundle for access in the edit activity
                    intent.putExtra("title", updateItem.getTitle());
                    intent.putExtra("dateTime", updateItem.getDatetime());
                    intent.putExtra("position", position);
                    // brings up the second activity
                    startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * load database and list view when the app start.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToDoItemDB db = ToDoItemDB.getDatabase(this.getApplication().getApplicationContext());
        mToDoItemDao = db.toDoItemDao();

        readItemsFromDatabase();
        // Create the adapter to convert the array to views
        mAdapter = new ItemAdapter(this, mItems);
        // Attach the adapter to a ListView
        mListView = findViewById(R.id.lstView);
        mListView.setAdapter(mAdapter);

        setupListViewListener();
    }
}