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

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Database table construct and also used in custom list view
 */
@Entity(tableName = "ToDoItem")
public class ToDoItem {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "dateTime")
    private String datetime;

    @ColumnInfo(name = "isEdit")
    private boolean isEdit = false;

    public ToDoItem(String title, String datetime) {
        this.title = title;
        this.datetime = datetime;
    }

    public int getId() {
        return id;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }


}
