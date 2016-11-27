package com.kelc.plbtw_n.plbtw_n.Main;

/**
 * Created by 12070 on 11/14/2016.
 */

public class URLList {

    private String Url_Utama = "http://mavesite.ddns.net/plbtw-rest/";

    //Login========================================
    private String Url_Login = Url_Utama + "index.php/api/user/login";
    private String Url_News = Url_Utama + "index.php/api/news/category_news";
    private String Url_OlahragaNews = Url_Utama + "index.php/api/news/category_sport";
    private String Url_Entertainment = Url_Utama + "index.php/api/news/category_entertainment";
    private String Url_Register = Url_Utama + "index.php/api/user/user";

    public String getUrl_Login() {
        return Url_Login;
    }

    public String getUrl_News() {
        return Url_News;
    }

    public String getUrl_Register() {
        return Url_Register;
    }

    public String getUrl_OlahragaNews() {
        return Url_OlahragaNews;
    }
    public String getUrl_Entertainment() {
        return Url_Entertainment;
    }

}
