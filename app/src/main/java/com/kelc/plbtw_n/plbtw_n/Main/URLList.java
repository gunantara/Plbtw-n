package com.kelc.plbtw_n.plbtw_n.Main;

/**
 * Created by 12070 on 11/14/2016.
 */

public class URLList {

    private String Url_Utama = "http://mavesite.ddns.net/plbtw-rest/";

    //Login========================================
    private String Url_Login = Url_Utama + "index.php/api/user/login";

    //Newa=========================================
    private String Url_ALLNews = Url_Utama + "index.php/api/news/all_news";

    //Register=====================================
    private String Url_Register = Url_Utama + "index.php/api/user/user";

    public String getUrl_Login() {
        return Url_Login;
    }

    public String getUrl_ALLNews() {
        return Url_ALLNews;
    }

    public String getUrl_Register() {
        return Url_Register;
    }


}
