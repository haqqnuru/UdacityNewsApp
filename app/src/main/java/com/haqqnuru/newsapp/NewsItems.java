package com.haqqnuru.newsapp;

import java.util.ArrayList;

// contains news title, section, category(type), publication date, url and author which is an array
public class NewsItems {

    // variables for newsItems
    private String newsTitle;
    private String section;
    private String category;
    private String publicationDate;
    private String url;
    private ArrayList<String> author;

    // constractor for newsItems
    public NewsItems(String newsTitle, String section, String category, String publicationDate, String url,
                     ArrayList<String> author) {
        this.newsTitle = newsTitle;
        this.section = section;
        this.category = category;
        this.publicationDate = publicationDate;
        this.url = url;
        this.author = author;
    }

    // get news title
    public String getNewsTitle() {
        return newsTitle;
    }

    // get section
    public String getSection() {
        return section;
    }

    // get category
    public String getCategory() {
        return category;
    }

    // get publication date and time
    public String getPublicationDate() {
        return publicationDate;
    }

    // get url
    public String getUrl() {
        return url;
    }

    // get author
    ArrayList<String> getAuthor() {
        return author;
    }
}