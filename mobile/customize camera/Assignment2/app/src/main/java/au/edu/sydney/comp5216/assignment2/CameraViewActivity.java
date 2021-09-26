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

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Uses camera 2 api to config custom camera interface.
 */
public class CameraViewActivity extends AppCompatActivity {
    //TAG to identify log information
    private static final String APP_TAG = "Assignment_2";

    //Button for picture capture
    private Button mTakePictureButton;

    //Back to gallery button
    private Button mBackButton;

    //View to display camera preview
    private TextureView mTextureView;

    //Permission number for accessing camera
    private static final int MY_PERMISSIONS_REQUEST_OPEN_CAMERA = 101;

    //ImageView to display captured photo
    private ImageView mImageView;

    //Camera device managing
    private CameraDevice mCameraDevice;

    //Camera capture session
    private CameraCaptureSession mCameraCaptureSession;

    //Camera request after build up
    private CaptureRequest.Builder mCaptureRequestBuilder;

    //Read image in textureView
    private ImageReader mImageReader;

    //Location document reference of fire storage
    private StorageReference mStorageRef;

    //Image dimension
    private Size mImageDimensions;

    //Define image store location
    private File mFile;

    //Background handler
    private Handler mBackgroundHandler;

    //Background thread
    private HandlerThread mBackgroundThread;

    //Bitmap convert image
    private Bitmap mBitmap;

    //Use to check orientation's of the phone
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    /**
     * Listen user click on this button then direct to image gallery page.
     * @param view this activity
     */
    public void backClick(View view) {
        Intent intent = new Intent(CameraViewActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Config page when this activity start.
     * @param savedInstanceState this page state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_view);
        mImageView = findViewById(R.id.photoPreview);
        mBackButton = findViewById(R.id.btnBack);
        mTextureView = findViewById(R.id.textureView);
        mTakePictureButton = findViewById(R.id.capture);
        mTextureView.setSurfaceTextureListener(textureListener);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();
        mTakePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
                mImageView.setVisibility(View.VISIBLE);
                mImageView.setImageBitmap(mBitmap);
                mTextureView.setVisibility(View.INVISIBLE);
                mBackButton.setVisibility(View.VISIBLE);
                mTakePictureButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * Asks use if they allow this app to access camera.
     * @param requestCode request code
     * @param permissions camera permission
     * @param grantResults result after agreement
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_OPEN_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(CameraViewActivity.this, "Sorry!!!, you can't use" +
                        " this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    /**
     * Show previewing of the camera in TextureView
     */
    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
            try {
                openCamera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {
        }
    };

    /**
     * Uses to check a camera device state. It is required to open a camera.
     */
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            mCameraDevice = camera;
            try {
                createCameraPreview();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            mCameraDevice = camera;
            closeCameraDevice();
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            mCameraDevice = camera;
            closeCameraDevice();
        }
    };

    @MainThread
    private void closeCameraDevice() {
        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }

    /**
     * Create preview of camera
     * @throws CameraAccessException Exception throw if preview create fail
     */
    private void createCameraPreview() throws CameraAccessException {
        SurfaceTexture texture = mTextureView.getSurfaceTexture();
        texture.setDefaultBufferSize(mImageDimensions.getWidth(), mImageDimensions.getHeight());
        Surface surface = new Surface(texture);
        mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        mCaptureRequestBuilder.addTarget(surface);
        mCameraDevice.createCaptureSession(Collections.singletonList(surface), new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(CameraCaptureSession session) {
                if (mCameraDevice == null) {
                    return;
                }
                mCameraCaptureSession = session;
                try {
                    updatePreview();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onConfigureFailed(CameraCaptureSession session) {
                Toast.makeText(getApplicationContext(), "Configuration changed", Toast.LENGTH_LONG).show();
            }
        }, null);
    }

    /**
     * Control preview
     * @throws CameraAccessException Exception throw if preview create fail
     */
    private void updatePreview() throws CameraAccessException {
        if (mCameraDevice == null) {
            return;
        }
        mCaptureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        mCameraCaptureSession.setRepeatingRequest(mCaptureRequestBuilder.build(), null, mBackgroundHandler);
    }

    /**
     * Open camera
     * @throws CameraAccessException Exception throw if preview create fail
     */
    private void openCamera() throws CameraAccessException {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String cameraId = manager.getCameraIdList()[0];
        CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

        mImageDimensions = map.getOutputSizes(SurfaceTexture.class)[0];

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CameraViewActivity.this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_OPEN_CAMERA);
            return;
        }
        manager.openCamera(cameraId, stateCallback, null);
    }

    /**
     * Take a picture and save in local folder and upload on cloud
     */
    private void takePicture() {
        if (null == mCameraDevice) {
            return;
        }
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        File mediaStorageDir = new
                File(this.getExternalFilesDir(null).getAbsolutePath(), "/images/IMG_");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.getParentFile().exists() &&
                !mediaStorageDir.getParentFile().mkdirs()) {
            Log.d(APP_TAG, "failed to create directory");
        }

        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraDevice.getId());
            Size[] jpegSizes = null;
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).
                        getOutputSizes(ImageFormat.JPEG);
            }
            int width = 640;
            int height = 480;
            if (jpegSizes != null && 0 < jpegSizes.length) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }

            mImageReader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<>(2);
            outputSurfaces.add(mImageReader.getSurface());
            outputSurfaces.add(new Surface(mTextureView.getSurfaceTexture()));
            final CaptureRequest.Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.
                    TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            // Orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            mFile = new File(mediaStorageDir.getParentFile().getPath() + File.separator +
                    "/IMG_" + timeStamp + ".jpg");
            mBitmap = mTextureView.getBitmap();
            //Start reference of cloud storage
            StorageReference imagesRef = mStorageRef.child("images/IMG_" + timeStamp + ".jpg");
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request,
                                               TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Toast.makeText(CameraViewActivity.this, "Saved:" + mFile, Toast.LENGTH_SHORT).show();
                    try {
                        createCameraPreview();
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            };

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = imagesRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                }
            });

            imageRead();
            mCameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, mBackgroundHandler);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Image reader listener
     */
    public void imageRead(){
        ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                try (Image image = reader.acquireLatestImage()) {
                    ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                    byte[] bytes = new byte[buffer.capacity()];
                    buffer.get(bytes); //!!!!!!!!!!!
                    save(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mImageReader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
    }

    /**
     * Save byte array in a folder.
     * @param bytes image bytes array
     * @throws IOException exception for IO
     */
    public void save(byte[] bytes) throws IOException {
        try (OutputStream output = new FileOutputStream(mFile)) {
            output.write(bytes);
        }
    }

    /**
     * Start camera session
     */
    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();
        if (mTextureView.isAvailable()) {
            try {
                openCamera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } else {
            mTextureView.setSurfaceTextureListener(textureListener);
        }
    }

    /**
     * Start background thread to run camera session
     */
    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    /**
     * Stop camera session and background thread
     */
    @Override
    protected void onPause() {
        try {
            stopBackgroundThread();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    /**
     * stop background thread
     * @throws InterruptedException InterruptedException
     */
    protected void stopBackgroundThread() throws InterruptedException {
        mBackgroundThread.quitSafely();
        mBackgroundThread.join();
        mBackgroundThread = null;
        mBackgroundHandler = null;
    }

}