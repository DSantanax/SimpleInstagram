package com.example.simpleinstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
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
                    return;
                }
                // desc. is valid
                // get the current user & save the Post
                ParseUser currUser = ParseUser.getCurrentUser();
                savePost(description, currUser);
            }
        });
    }

    private void launchCamera() {
        // create intent to take a picture and return control to the calling app.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // create a file reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        Uri fileProvider = FileProvider.getUriForFile(MainActivity.this, "com.example.simpleinstagram", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // if you call startActivityForResult() using an intent that no app can handle, the app will crash
        // so as long as the result is not null, its safe to use the intent
        if (intent.resolveActivity(getPackageManager()) != null) {
        // request code is unique to use for the Result sent
            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
        }


    }

    // return the file for a photo stored on a disk given the filename
    private File getPhotoFileUri(String photoFileName) {
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
    private void savePost(String description, ParseUser currUser) {
        // create a new Post and set the description/user
        Post post = new Post();
        post.setDescription(description);
//        post.setImage();
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
                // clear the description once it was saved
                etDescription.setText("");
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