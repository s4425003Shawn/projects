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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Edit page contain one text view, one save button and one cancel button to
 * edit or add new item in the main page.
 */
public class EditToDoItemActivity extends Activity {
    private int mPosition = 0; //save item index position in the list will to specific a item
    private EditText mEditItem;

    /**
     * get data from main page when this page start.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //populate the screen using the layout
        setContentView(R.layout.activity_edit_item);

        //Get the data from the main screen
        String title = getIntent().getStringExtra("title");
        String dateTime = getIntent().getStringExtra("dateTime");
        String newItem = title + " " + dateTime;
        mPosition = getIntent().getIntExtra("position", -1);


        // show original content in the text field
        mEditItem = findViewById(R.id.etEditItem);
        if (title != null) {
            mEditItem.setText(newItem);
        }

    }

    /**
     * send data in the text view and save position information to main page
     * @param v
     */
    public void onSubmit(View v) {
        mEditItem = findViewById(R.id.etEditItem);

        // Prepare data intent for sending it back
        Intent data = new Intent();

        // Pass relevant data back as a result
        data.putExtra("item", mEditItem.getText().toString());
        data.putExtra("position", mPosition);

        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }

    /**
     * cancel the edit or add action and return to main page
     * @param v
     */
    public void onCancel(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditToDoItemActivity.this);
        builder.setTitle(R.string.cancel)
                .setMessage(R.string.dialog_cancel_msg)
                .setPositiveButton(R.string.yes, new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
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

    }
}
