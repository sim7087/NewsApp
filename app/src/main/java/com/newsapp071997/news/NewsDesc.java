package com.newsapp071997.news;

public class NewsDesc {
    private String mTitle;
    private String mSectionName;
    private String mDatePub;
    private String mUrl;
    private String mAuthor;

    public NewsDesc(String title, String sectionName, String datePub,String url) {
        mTitle = title;
        mDatePub = datePub;
        mSectionName = sectionName;
        mUrl = url;
    }
    public NewsDesc(String title, String sectionName, String datePub,String url,String author) {
        mTitle = title;
        mDatePub = datePub;
        mSectionName = sectionName;
        mUrl = url;
        mAuthor = author;
    }

    public NewsDesc(String title, String sectionName) {
        mTitle = title;
        mSectionName = sectionName;
    }

    public String getTitleNews() {
        return mTitle;
    }
    public String getUrl() {
        return mUrl;
    }
    public String getAuthor() {
        return mAuthor;
    }
    public String getSectionName() {
        return mSectionName;
    }

    public String getDatePub() {
        return mDatePub;
    }

}
