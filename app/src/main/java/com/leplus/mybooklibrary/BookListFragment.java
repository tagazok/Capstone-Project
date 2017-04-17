package com.leplus.mybooklibrary;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.leplus.mybooklibrary.model.Book;
import com.leplus.mybooklibrary.widget.WishlistWidget;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public abstract class BookListFragment extends Fragment {

    private static final String TAG = "BookListFragment";

    private DatabaseReference mDatabase;

    private FirebaseRecyclerAdapter<Book, BookViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;

    private Query mQuery;

    public BookListFragment() {
        // Required empty public constructor

        mFirebaseDatabase = Utils.getDatabase();
        //mFirebaseDatabase.setPersistenceEnabled(true);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_book_list, container, false);

        mRecycler = (RecyclerView) rootView.findViewById(R.id.messages_list);
        mRecycler.setHasFixedSize(true);
        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    onSignedInInitialize();
                } else {
                    onSignedOutCleanup();
                }
            }
        };
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    private void onSignedInInitialize() {

        // Set up FirebaseRecyclerAdapter with the Query
        mDatabase = mFirebaseDatabase.getReference().child(mFirebaseAuth.getCurrentUser().getUid());
        if (mQuery == null) {
            mQuery = getQuery(mDatabase);
            mAdapter = new FirebaseRecyclerAdapter<Book, BookViewHolder>(Book.class, R.layout.book_list_content,
                    BookViewHolder.class, mQuery) {
                @Override
                protected void populateViewHolder(final BookViewHolder viewHolder, final Book model, final int position) {
                    final DatabaseReference postRef = getRef(position);

                    // Set click listener for the whole post view
                    final String postKey = postRef.getKey();
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Context context = v.getContext();
                            Intent intent = new Intent(getActivity(), BookDetailActivity.class);
                            intent.putExtra(BookDetailFragment.ARG_ITEM, model);
                            context.startActivity(intent);
                        }
                    });


                    viewHolder.bind(model);
                }

                @Override
                protected void onDataChanged() {
                    super.onDataChanged();
                    updateWidgets();
                }
            };
            mRecycler.setAdapter(mAdapter);
        }
    }

    public void updateWidgets() {
        ComponentName name = new ComponentName(getContext(), WishlistWidget.class);
        int[] ids = AppWidgetManager.getInstance(getContext()).getAppWidgetIds(name);
        Intent intent = new Intent(getContext(), WishlistWidget.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        intent.putExtra(AppWidgetManager.ACTION_APPWIDGET_UPDATE, ids);
        getContext().sendBroadcast(intent);
    }

    private void onSignedOutCleanup() {
        if (mQuery != null) {
            mQuery = null;
            mRecycler.setAdapter(null);
        }
    }


    public abstract Query getQuery(DatabaseReference databaseReference);
}
