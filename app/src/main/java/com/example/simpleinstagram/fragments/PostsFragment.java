package com.example.simpleinstagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.simpleinstagram.Post;
import com.example.simpleinstagram.PostsAdapter;
import com.example.simpleinstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostsFragment extends Fragment {

    private RecyclerView rvPosts;
    public static final String TAG = "PostsFragment";
    // for RV, used also in ProfileFragment (child inherited class)
    protected PostsAdapter postsAdapter;
    // for Data source, protected (for inheritance)
    protected List<Post> allPosts;
    private SwipeRefreshLayout swipeRefreshLayout;

    public PostsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment PostsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostsFragment newInstance(String param1, String param2) {
        PostsFragment fragment = new PostsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    // setup
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // reference the views
        rvPosts = view.findViewById(R.id.rvPosts);
        swipeRefreshLayout = view.findViewById(R.id.swipeContainer);
        allPosts = new ArrayList<>();
        postsAdapter = new PostsAdapter(getContext(), allPosts);

        // refresh container
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // clear the data set first (call to clear data/notify adapter)
                postsAdapter.clear();
                // update adapter
                queryPosts();
                // not refreshing
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // 0. create layout for one row in list (item_post.xml)
        // 1. create adapter (PostsAdapter class)
        // 2. create data source (Posts class)
        // 3, set the adapter on rv
        rvPosts.setAdapter(postsAdapter);
        // 4. set the layout manager on rv (Vertical linear layout by default for Linear)
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        // getting all the posts from the Post class in B4A
        queryPosts();

    }
    //  query the posts in the B4A
    protected void queryPosts() {
        // specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // we want to also get the User who made the post
        query.include(Post.KEY_USER);
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