package com.leplus.mybooklibrary.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leplus.mybooklibrary.R;
import com.leplus.mybooklibrary.model.Book;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by olivier on 15/04/2017.
 */

public class WidgetListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<Book> mChoreList = new ArrayList<Book>();
    private Context mContext = null;
    private Intent mIntent;

    WidgetListRemoteViewsFactory(Context context, Intent intent) {
        this.mIntent = intent;
        this.mContext = context;
    }

    private void populateChoreWidgetList() {
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(mFirebaseAuth.getCurrentUser().getUid()).child("wishlist");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mChoreList.clear();
                for (DataSnapshot choreSnapshot : dataSnapshot.getChildren()) {
                    mChoreList.add(choreSnapshot.getValue(Book.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public int getCount() {
        return mChoreList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(
                mContext.getPackageName(), R.layout.widget_list_item);

        Book book = mChoreList.get(position);

        boolean isPhoto = book.getImageUrl() != null;
        if (isPhoto) {
            Bitmap bitmap = null;
            try {

                bitmap = Glide.with(mContext)
                        .load(book.getImageUrl())
                        .asBitmap()
                        .into(40, 40)
                        .get();

                remoteView.setImageViewBitmap(R.id.book_cover,bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        //remoteView.setTextViewText(R.id.book_publisher, book.getPublisher());

        remoteView.setTextViewText(R.id.book_title, book.getTitle());
        //remoteView.setTextViewText(R.id.book_authors, book.getAuthorsString());


        mIntent.putExtra("title", book.getTitle());
        mIntent.putExtra("authors", book.getPublisher());

        remoteView.setOnClickFillInIntent(R.id.widget_single_chore_linear_layout, mIntent);

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        populateChoreWidgetList();
    }

    @Override
    public void onDestroy() {

    }
}
