package com.leplus.mybooklibrary.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by olivier on 02/04/2017.
 */

public class Book implements Serializable {
    private String id;
    private String title;
    private String subtitle;
    private String publisher;
    private String publishedDate;
    private String description;
    private Integer pageCount;
    private List<String> authors;
    private String imageUrl;
    private String textSnippet;

    public Book() {
    }

    public Book(String id, String title, String subtitle, String publisher, String publishedDate, String description, Integer pageCount, List<String> authors, String imageUrl, String textSnippet) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.description = description;
        this.pageCount = pageCount;
        this.authors = authors;
        this.imageUrl = imageUrl;
        this.textSnippet = textSnippet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTextSnippet() {
        return textSnippet;
    }

    public void setTextSnippet(String textSnippet) {
        this.textSnippet = textSnippet;
    }

    public String getAuthorsString() {
        if (authors == null) {
            return "";
        }
        return TextUtils.join(", ", authors);
    }

    @Override
    public boolean equals(Object obj) {
        return (this.id.equals(((Book) obj).getId()));
    }


    /*
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(subtitle);
        parcel.writeString(publisher);
        parcel.writeString(publishedDate);
        parcel.writeString(description);
        parcel.writeInt(pageCount);
        parcel.writeStringList(authors);
        parcel.writeString(imageUrl);
        parcel.writeString(textSnippet);
    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    // "De-parcel object
    public Book(Parcel in) {
        id = in.readString();
        title = in.readString();
        subtitle = in.readString();
        publisher = in.readString();
        publishedDate = in.readString();
        description = in.readString();
        pageCount = in.readInt();
        //authors = in.readStringList();
        imageUrl = in.readString();
        textSnippet = in.readString();
    }
    */
}
