package com.leplus.mybooklibrary;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leplus.mybooklibrary.model.Book;

/**
 * Created by olivier on 07/04/2017.
 */

public class BookViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public TextView publisherView;
    public ImageView coverView;

    public BookViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView) itemView.findViewById(R.id.book_title);
        authorView = (TextView) itemView.findViewById(R.id.book_authors);
        publisherView = (TextView) itemView.findViewById(R.id.book_publisher);
        coverView = (ImageView) itemView.findViewById(R.id.book_cover);
    }

    public void bind(Book book) {
        titleView.setText(book.getTitle());
        authorView.setText(book.getAuthorsString());
        publisherView.setText(book.getPublisher());

        boolean isPhoto = book.getImageUrl() != null;
        if (isPhoto) {
            Glide.with(coverView.getContext())
                    .load(book.getImageUrl())
                    .centerCrop()
                    .into(coverView);
        }
    }
}