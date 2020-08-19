package merak.jaya.abadi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StorageActivity extends AppCompatActivity {
    ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
/*

        try {
            includesForDownloadFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        imageview = findViewById(R.id.imageView);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);

/*
        try {
            includesForUploadFiles();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
*/
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Bundle extras = imageReturnedIntent.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imageview.setImageBitmap(imageBitmap);
                    try {
                        includesForUploadFiles();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    imageview.setImageURI(selectedImage);
                }
                break;
        }
    }

    public void tescreat() {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images");
        StorageReference spaceRef = storageRef.child("images/space.jpg");
        imagesRef = spaceRef.getParent();
        StorageReference rootRef = spaceRef.getRoot();
        StorageReference earthRef = spaceRef.getParent().child("earth.jpg");
        StorageReference nullRef = spaceRef.getRoot().getParent();
        spaceRef.getPath();
        spaceRef.getName();

        spaceRef.getBucket();
        storageRef = storage.getReference();

        imagesRef = storageRef.child("images");
        String fileName = "space.jpg";
        spaceRef = imagesRef.child(fileName);
        String path = spaceRef.getPath();
        String name = spaceRef.getName();
        imagesRef = spaceRef.getParent();

    }

    public void includesForUploadFiles() throws FileNotFoundException {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // [START upload_create_reference]
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        @SuppressLint("SimpleDateFormat") String s = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());

        // Create a reference to "mountains.jpg"
        StorageReference mountainsRef = storageRef.child(s + ".jpg");


        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        // [START upload_memory]
        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        // Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle successful uploads on complete
                // ...
                try {
                    includesForDownloadFiles();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        // [END upload_complete_example]

    }

    public void includesForDownloadFiles() throws IOException {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // [START download_create_reference]
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // [START download_to_memory]
        StorageReference islandRef = storageRef.child("tes.jpg");
//        Glide.with(StorageActivity.this).load(islandRef).into(imageView);

        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                // Convert bytes data into a Bitmap
                Log.e("onFailure: ", "Sukses");
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                Glide.with(StorageActivity.this).load(bytes).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.e("onFailure: ", "Eror");
            }
        });
        // [END download_to_memory]

    }
}