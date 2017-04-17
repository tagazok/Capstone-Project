package com.leplus.mybooklibrary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leplus.mybooklibrary.model.Book;

/**
 * Created by olivier on 02/04/2017.
 */

public class BookRecyclerViewAdapter extends RecyclerView.Adapter<BookRecyclerViewAdapter.ViewHolder> {

    private final java.util.List<Book> mValues;
    private boolean mTwoPane;
    protected Context context;

    public BookRecyclerViewAdapter(Context context, boolean mTwoPane, java.util.List<Book> items) {
        mValues = items;
        this.mTwoPane = mTwoPane;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);


        if (mValues.get(position).getImageUrl() != null) {
            //new DownloadImage(holder.mCoverView).execute(mValues.get(position).getImageUrl());
            Glide.with(context)
                    .load(mValues.get(position).getImageUrl())
                    .centerCrop()
                    .into(holder.mCoverView);
        }

        holder.mIdView.setText(mValues.get(position).getTitle());
        //holder.mContentView.setText(mValues.get(position).getAuthorsString());
        holder.mContentView.setText(mValues.get(position).getAuthorsString());
        holder.mPublisherView.setText(mValues.get(position).getPublisher() + " - " + mValues.get(position).getPublishedDate());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putSerializable(BookDetailFragment.ARG_ITEM, mValues.get(position));
                    BookDetailFragment fragment = new BookDetailFragment();
                    fragment.setArguments(arguments);

                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.book_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, BookDetailActivity.class);
                    //intent.putExtra(BookDetailFragment.ARG_ITEM_ID, holder.mItem.getTitle());
                    intent.putExtra(BookDetailFragment.ARG_ITEM, mValues.get(position));
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mCoverView;
        public final TextView mPublisherView;

        public Book mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCoverView = (ImageView) view.findViewById(R.id.book_cover);
            mIdView = (TextView) view.findViewById(R.id.book_title);
            mContentView = (TextView) view.findViewById(R.id.book_authors);
            mPublisherView = (TextView) view.findViewById(R.id.book_publisher);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
