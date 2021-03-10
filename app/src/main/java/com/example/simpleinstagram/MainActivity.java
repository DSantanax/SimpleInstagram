package com.example.simpleinstagram;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final int CAPTURE_IMAGE_REQUEST_CODE = 42;
    private EditText etDescription;
    private ImageView ivPostImage;
    private Button btnCaptureImage;
    private Button btnSubmit;
    // used for the file reference at the location
    // photoFile will be used to save Post
    private File photoFile;
    public String photoFileName = "photo.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDescription = findViewById(R.id.etDescription);
        btnCaptureImage = findViewById(R.id.btnCaptureImage);
        ivPostImage = findViewById(R.id.ivPostImage);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });


        // test getting all the posts from the Post class in B4A
        // queryPosts();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // collect info. to create post
                String description = etDescription.getText().toString();
                // check if et is empty or null (required)
                if (description.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Description cannot be empty.", Toast.LENGTH_SHORT).show();
                    // return to end
                    return;
                }
                // if photo has not been taken (empty) or the ImageView preview is not being
                // shown(user submitting a post w/out data)
                if (photoFile == null || ivPostImage.getDrawable() == null) {
                    Toast.makeText(MainActivity.this, "There is no image!", Toast.LENGTH_SHORT).show();
                    // return to end
                    return;
                }
                // desc. is valid
                // get the current user & save the Post
                ParseUser currUser = ParseUser.getCurrentUser();
                savePost(description, currUser, photoFile);
            }
        });
    }

    private void launchCamera() {
        // create Implicit intent to take a picture and return control to the calling app.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // create a file reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24, fileProvider wraps the photoFile
        Uri fileProvider = FileProvider.getUriForFile(MainActivity.this, "com.example.simpleinstagram", photoFile);
        // where do we want the output image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // if you call startActivityForResult() using an intent that no app can handle, the app will crash
        // so as long as the result is not null, its safe to use the intent
        // here we check if the phone has a camera intent which we can use
        if (intent.resolveActivity(getPackageManager()) != null) {
        // request code is unique to use for the Result sent
            // start the image capture intent to take a photo
            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
        }


    }

//    In our case when the child application returns back to the parent application cam -> app
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if the request code came from the camera
        if (requestCode == CAPTURE_IMAGE_REQUEST_CODE) {
            // and the result was OK
            if (resultCode == RESULT_OK) {
                // get the image taken by the user
                // we have the camera photo on file, we want to get the file as a Bitmap
                // to decode the file
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // resize the Bitmap, since we may run out of memory (older emulators or large image)

                // load the taken image into a ImageView preview
                ivPostImage.setImageBitmap(takenImage);
            }
            // result was not successful
            else {
                Toast.makeText(MainActivity.this, "Picture was not taken!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    // return the file for a photo stored on a disk given the filename
    private File getPhotoFileUri(String photoFileName) {
        // URI - uniform resource identifier - a string which unambiguously identifies a resource
        // in our case the file

        // get safe storage directory for photos
        // use getExternalFilesDir on context to access package-specific directories
        // this way, we do not need to request external read/write runtime permission
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // create the storage directory if it DNE
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "Failed to create storage directory");
        }
        // return the file target for the photo based on the filename
        return new File(mediaStorageDir.getPath() + File.separator + photoFileName);

    }

    // Save post method
    private void savePost(String description, ParseUser currUser, File photoFile) {
        // create a new Post and set the description/user
        Post post = new Post();
        // set the description
        post.setDescription(description);
        // setImage takes in a ParseFile which in this case will have the File photoFile
        post.setImage(new ParseFile(photoFile));
        // set the current logged in user
        post.setUser(currUser);
        // call Post to save it in the B4A with a callback method
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                // check if an error occurred while saving
                if (e != null) {
                    Log.e(TAG, "Error while saving.", e);
                    Toast.makeText(MainActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                // saved successfully
                Log.i(TAG, "Post was saved successfully!");
                Toast.makeText(MainActivity.this, "Post saved and uploaded successfully!", Toast.LENGTH_SHORT).show();
                // clear the description once it was saved
                etDescription.setText("");
                // clear the ImageView using setImageResource(0) 0 - empty resource ID
                ivPostImage.setImageResource(0);
            }
        });
    }

    private void queryPosts() {
        // specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // we want to also get the User who made the post
        query.include(Post.KEY_USER);
        // find the Post objects using the query
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // error if not null
                if(e != null) {
                    Log.e(TAG, "Issue with getting posts: ", e);
                    return;
                }
                // data was received successfully
                for(Post post : posts){
                    // getUser returns the ParseUser which we would need to get the actual Username
                    Log.i(TAG, "Posts received: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
            }
        });

    }
}