package com.leplus.mybooklibrary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.leplus.mybooklibrary.model.Book;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     NewbookDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 * <p>You activity (or fragment) needs to implement {@link NewbookDialogFragment.Listener}.</p>
 */
public class NewbookDialogFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    public static final String ARG_ITEM = "newitem";
    private Book mBook;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mBooksDatabaseReference;

    // TODO: Customize parameters
    public static NewbookDialogFragment newInstance(int itemCount) {
        final NewbookDialogFragment fragment = new NewbookDialogFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_newbook_dialog, container, false);

        if (getArguments().containsKey(ARG_ITEM)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mBook = (Book) getArguments().getSerializable(ARG_ITEM);

            if (mBook != null) {
                ((TextView) rootView.findViewById(R.id.book_title)).setText(mBook.getTitle());
                ((TextView) rootView.findViewById(R.id.book_description)).setText(mBook.getDescription() + mBook.getDescription());
                ((TextView) rootView.findViewById(R.id.book_authors)).setText(mBook.getAuthorsString());
                Glide.with(this)
                        .load(mBook.getImageUrl())
                        .centerCrop()
                        .into((ImageView)rootView.findViewById(R.id.book_cover));
            }
        }

        mFirebaseAuth = FirebaseAuth.getInstance();

        Button addToLibraryButton = (Button)rootView.findViewById((R.id.button_add_library));
        addToLibraryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mFirebaseDatabase = Utils.getDatabase();

                mBooksDatabaseReference = mFirebaseDatabase.getReference().child(mFirebaseAuth.getCurrentUser().getUid()).child("books");

                mBooksDatabaseReference.child(mBook.getId()).setValue(mBook);
                Toast.makeText(getActivity(), "Book added to your library", Toast.LENGTH_LONG).show();
                dismiss();
            }
        });

        Button addToWishlistButton = (Button)rootView.findViewById((R.id.button_add_wishlist));
        addToWishlistButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mFirebaseDatabase = Utils.getDatabase();

                mBooksDatabaseReference = mFirebaseDatabase.getReference().child(mFirebaseAuth.getCurrentUser().getUid()).child("wishlist");

                mBooksDatabaseReference.child(mBook.getId()).setValue(mBook);
                Toast.makeText(getActivity(), "Book added to your wishlist", Toast.LENGTH_LONG).show();
                dismiss();
            }
        });

        return rootView;
    }
}
