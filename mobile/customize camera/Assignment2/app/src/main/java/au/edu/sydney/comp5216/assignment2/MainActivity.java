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

package au.edu.sydney.comp5216.assignment2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.exifinterface.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Displays image gallery
 */
public class MainActivity extends AppCompatActivity {
    private GridView mGridView;
    private FirebaseStorage mStorage;
    private File mMediaStorageDir;
    public static final String APP_TAG = "Assignment_2";

    /**
     * Button direct to custom camera
     * @param view
     */
    public void takePhotoClick(View view) {
        Intent intent = new Intent(MainActivity.this, CameraViewActivity.class);
        startActivity(intent);
    }

    /**
     * Download missing images from cloud
     */
    public void synchronisationClick(View view) {
        StorageReference listRef = mStorage.getReference().child("images");
        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (final StorageReference item : listResult.getItems()) {
                            if (!imageNameList().contains(item.getName())) {
                                final long ONE_MEGABYTE = 1024 * 1024;
                                item.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        File file = new File(mMediaStorageDir.getAbsolutePath() +
                                                File.separator + item.getName());
                                        try {
                                            save(bytes, file);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        showGallery();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                    }
                                });
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    /**
     * Start when this activity open
     * @param savedInstanceState this state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStorage = FirebaseStorage.getInstance();
        mGridView = findViewById(R.id.photo_view);
        mMediaStorageDir = new
                File(Objects.requireNonNull(this.getExternalFilesDir(null)).getAbsolutePath(),
                "/images/");
        showGallery();
    }

    /**
     * Shows images in local storage to display in gridView
     */
    private void showGallery() {
        List<Drawable> list = new ArrayList<>();
        if (!Objects.requireNonNull(mMediaStorageDir.getParentFile()).exists() &&
                !mMediaStorageDir.getParentFile().mkdirs()) {
            Log.d(APP_TAG, "failed to create directory");
        }
        if (mMediaStorageDir.length() > 0) {
            for (File f : Objects.requireNonNull(mMediaStorageDir.listFiles())) {
                try {
                    ExifInterface exif = new ExifInterface(f.getPath());
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);
                    int angle = 0;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                        angle = 90;
                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                        angle = 180;
                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                        angle = 270;
                    }
                    Matrix mat = new Matrix();
                    mat.postRotate(angle);
                    Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f), null, null);
                    assert bmp != null;
                    Bitmap correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat,
                            true);
                    Drawable drawable = new BitmapDrawable(getResources(), correctBmp);
                    list.add(drawable);
                } catch (IOException e) {
                    Log.w("TAG", "-- Error in setting image");
                } catch (OutOfMemoryError oom) {
                    Log.w("TAG", "-- OOM Error in setting image");
                }
            }
        }
        mGridView.setAdapter(new ImageAdapter(this, list));
    }

    /**
     * Save bytes array to particular file
     * @param bytes image bytes array
     * @param file file location
     * @throws IOException exception
     */
    private void save(byte[] bytes, File file) throws IOException {
        try (OutputStream output = new FileOutputStream(file)) {
            output.write(bytes);
        }
    }

    /**
     * Check difference between local and cloud by name
     * @return return set of missing name of images.
     */
    public Set imageNameList() {
        Set nameSet = new HashSet();
        if (!mMediaStorageDir.getParentFile().exists() &&
                !mMediaStorageDir.getParentFile().mkdirs()) {
            Log.d(APP_TAG, "failed to create directory");
        }
        if (mMediaStorageDir.length() > 0) {
            for (File file : Objects.requireNonNull(mMediaStorageDir.listFiles())) {
                nameSet.add(file.getName());
            }
        }
        return nameSet;
    }
}