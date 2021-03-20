package com.example.simpleinstagram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.simpleinstagram.fragments.ComposeFragment;
import com.example.simpleinstagram.fragments.PostsFragment;
import com.example.simpleinstagram.fragments.ProfileFragment;
import com.example.simpleinstagram.fragments.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

// TODO add app icon

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    // used to swap the fragments to replace the FrameLayout with the fragment
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // listener for the items selected in the bottomNavBar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            // item is the MenuItem that was selected
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                // switch between layouts
                int itemId = item.getItemId();

                if (itemId == R.id.action_home) { // add RV of posts
                    // TODO update for action home
//                    Toast.makeText(MainActivity.this, "Home!", Toast.LENGTH_SHORT).show();
                    fragment = new PostsFragment();
                } else if (itemId == R.id.action_post) {// move to the posts
//                    Toast.makeText(MainActivity.this, "Post!", Toast.LENGTH_SHORT).show();
                    fragment = new ComposeFragment();
                }
                else if(itemId == R.id.action_setting) {
                       fragment = new SettingFragment();
                } else {// for default and the action profile case
//                    Toast.makeText(MainActivity.this, "Profile!", Toast.LENGTH_SHORT).show();
                    fragment = new ProfileFragment();
                    // do not need to call finish()
                }
                // replace the frame layout with the user selected fragment
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // set default selection (when loading up the app)
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }
}