package com.example.simpleinstagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.simpleinstagram.Post;
import com.example.simpleinstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

// this extends the Posts Fragment, getting all their methods/overrides from
// the PostsFragment's call Fragment using inheritance
public class ProfileFragment extends PostsFragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void queryPosts() {
        // specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // we want to also get the User who made the post
        query.include(Post.KEY_USER);
        //
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        // limit the posts to 20
        query.setLimit(20);
        // order by recent lists using the createdAt key
        // most recent posts on top
        query.addDescendingOrder(Post.KEY_CREATED_AT);
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
                // add the posts fetched from the call to our data source
                allPosts.addAll(posts);
                // notify adapter
                postsAdapter.notifyDataSetChanged();
            }
        });
    }
}