package com.example.bookstore;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private int id;
    private String imageLink;
    private String title;
    private String author;
    private int numOfPage;
    private String description;
    private int rateStar;
    private int numberOfView;
    private long price;
    private String category;
    private int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Book(int id, String imageLink, String title, String author, int numOfPage,
                String description, int rateStar, int numberOfView, long price, String category, int amount) {
        this.id= id;
        this.imageLink = imageLink;
        this.title = title;
        this.author = author;
        this.numOfPage = numOfPage;
        this.description = description;
        this.rateStar = rateStar;
        this.numberOfView = numberOfView;
        this.price = price;
        this.category = category;
        this.amount= amount;
    }

    public Book() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    protected Book(Parcel in) {
        imageLink = in.readString();
        title = in.readString();
        author = in.readString();
        numOfPage = in.readInt();
        description = in.readString();
        rateStar = in.readInt();
        numberOfView = in.readInt();
        price = in.readLong();
        category = in.readString();
        amount= in.readInt();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getNumOfPage() {
        return numOfPage;
    }

    public void setNumOfPage(int numOfPage) {
        this.numOfPage = numOfPage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRateStar() {
        return rateStar;
    }

    public void setRateStar(int rateStar) {
        this.rateStar = rateStar;
    }

    public int getNumberOfView() {
        return numberOfView;
    }

    public void setNumberOfView(int numberOfView) {
        this.numberOfView = numberOfView;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageLink);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeInt(numOfPage);
        dest.writeString(description);
        dest.writeInt(rateStar);
        dest.writeInt(numberOfView);
        dest.writeLong(price);
        dest.writeString(category);
        dest.writeInt(amount);
    }
}
