package com.mintsnap;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.ImageView;
        import android.widget.TextView;
        import com.squareup.picasso.Picasso;

        import static com.mintsnap.feed.EXTRA_CREATOR;
        import static com.mintsnap.feed.EXTRA_LIKES;
        import static com.mintsnap.feed.EXTRA_URL;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_URL);
        String creatorName = intent.getStringExtra(EXTRA_CREATOR);
        int likeCount = intent.getIntExtra(EXTRA_LIKES, 0);

        ImageView imageView = findViewById(R.id.image_view_detail);
        TextView textViewCreator = findViewById(R.id.text_view_creator);
        TextView textViewLikes = findViewById(R.id.text_view_likes_detail);
        Picasso.get().load(imageUrl).fit().centerCrop().into(imageView);
//      textViewCreator.setText(creatorName);
        textViewLikes.setText("Likes: " + likeCount);
        if (textViewCreator !=null & textViewLikes != null)
        {
            textViewCreator.setText(creatorName);
            textViewLikes.setText("Likes: " + likeCount);
        }
    }
}