package com.kelc.plbtw_n.plbtw_n.Main.Olahraga;

/**
 * Created by 12070 on 11/26/2016.
 */

public class modelOlahraga {

    private String id_news;
    private String title;
    private String date;
    private String content;
    private String category;
    private String sub_category;
    private String location;
    private String news_web;
    private String news_url;
    private String keyword;
    private String image;

    public modelOlahraga (){


    }

    public modelOlahraga(String id_news, String title, String date, String content, String category, String sub_category, String location, String news_web, String news_url, String keyword, String image){

        this.setId_news(id_news);
        this.setTitle(title);
        this.setDate(date);
        this.setContent(content);
        this.setCategory(category);
        this.setSub_category(sub_category);
        this.setLocation(location);
        this.setNews_web(news_web);
        this.setNews_url(news_url);
        this.setKeyword(keyword);
        this.setImage(image);


    }

    public String getId_news() {
        return id_news;
    }

    public void setId_news(String id_news) {
        this.id_news = id_news;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNews_web() {
        return news_web;
    }

    public void setNews_web(String news_web) {
        this.news_web = news_web;
    }

    public String getNews_url() {
        return news_url;
    }

    public void setNews_url(String news_url) {
        this.news_url = news_url;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
