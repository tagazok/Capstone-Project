package com.leplus.mybooklibrary;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.leplus.mybooklibrary.model.Book;

/**
 * A fragment representing a single Book detail screen.
 * This fragment is either contained in a {@link BookListActivity}
 * in two-pane mode (on tablets) or a {@link BookDetailActivity}
 * on handsets.
 */
public class BookDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM = "item";

    /**
     * The dummy content this fragment is presenting.
     */
    private Book mBook;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mBooksDatabaseReference;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseDatabase = Utils.getDatabase();

        mBooksDatabaseReference = mFirebaseDatabase.getReference().child("books");

        if (getArguments().containsKey(ARG_ITEM)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mBook = (Book)getArguments().getSerializable(ARG_ITEM);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mBook.getTitle());

                Glide.with(this)
                        .load(mBook.getImageUrl())
                        .centerCrop()
                        .into((ImageView)appBarLayout.findViewById(R.id.book_cover));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.book_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mBook != null) {
            ((TextView) rootView.findViewById(R.id.book_title)).setText(mBook.getTitle());
            ((TextView) rootView.findViewById(R.id.book_description)).setText(mBook.getDescription());
            ((TextView) rootView.findViewById(R.id.book_authors)).setText(mBook.getAuthorsString());
            ((TextView) rootView.findViewById(R.id.book_publisher)).setText(mBook.getPublisher());
            ((TextView) rootView.findViewById(R.id.book_publishedDate)).setText(mBook.getPublishedDate());
            /*Glide.with(this)
                    .load(mBook.getImageUrl())
                    .centerCrop()
                    .into((ImageView)rootView.findViewById(R.id.book_cover));*/
        }


        Button removeButton = (Button) rootView.findViewById(R.id.button_remove_book);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBooksDatabaseReference.child(mBook.getId()).removeValue();
                getActivity().finish();
            }
        });

        return rootView;
    }
}
