package com.example.bookstore.API_Service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Book {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("imageLink")
    @Expose
    private String imageLink;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("publisher")
    @Expose
    private String publisher;

    @SerializedName("releaseYear")
    @Expose
    private int releaseYear;

    @SerializedName("numOfPage")
    @Expose
    private int numOfPage;

    @SerializedName("price")
    @Expose
    private long price;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("categoty")
    @Expose
    private String categoty;

    @SerializedName("rateStar")
    @Expose
    private int rateStar;

    @SerializedName("numOfReView")
    @Expose
    private int numOfReview;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", imageLink='" + imageLink + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", releaseYear=" + releaseYear +
                ", numOfPage=" + numOfPage +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", categoty='" + categoty + '\'' +
                ", rateStar=" + rateStar +
                ", numOfReview=" + numOfReview +
                ", amount=" + amount +
                '}';
    }

    @SerializedName("amount")
    @Expose
    private int amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisherl) {
        this.publisher = publisher;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getNumOfPage() {
        return numOfPage;
    }

    public void setNumOfPage(int numOfPage) {
        this.numOfPage = numOfPage;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoty() {
        return categoty;
    }

    public void setCategoty(String categoty) {
        this.categoty = categoty;
    }

    public int getRateStar() {
        return rateStar;
    }

    public void setRateStar(int rateStar) {
        this.rateStar = rateStar;
    }

    public int getNumOfReview() {
        return numOfReview;
    }

    public void setNumOfReview(int numOfReview) {
        this.numOfReview = numOfReview;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Book(int id, String imageLink, String title, String author, String publisher, int releaseYear, int numOfPage, long price, String description, String categoty, int rateStar, int numOfReview, int amount) {
        this.id = id;
        this.imageLink = imageLink;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.releaseYear = releaseYear;
        this.numOfPage = numOfPage;
        this.price = price;
        this.description = description;
        this.categoty = categoty;
        this.rateStar = rateStar;
        this.numOfReview = numOfReview;
        this.amount = amount;
    }
}
