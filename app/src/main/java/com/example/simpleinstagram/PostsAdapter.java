package com.example.simpleinstagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
    }

    // create ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a View using the LayoutInflater for the item_post and do not attach to root
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    // binds data to the certain ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // view holder for the views
    class ViewHolder extends RecyclerView.ViewHolder {

        // references to the views
        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvDescription;

        public ViewHolder(@NonNull View viewItem){
            super(viewItem);
            // assign views
            tvUsername = viewItem.findViewById(R.id.tvUsername);
            ivImage = viewItem.findViewById(R.id.ivImage);
            tvDescription = viewItem.findViewById(R.id.tvDescription);
        }

        public void bind(Post post) {
            // bind the View elements to the model Posts' data
            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            ParseFile image = post.getImage();
            // load image if it has one
            if(image != null) {
                // use Glide to load image URL
                Glide.with(context).load(post.getImage().getUrl()).into(ivImage);
            }
        }
    }

}
