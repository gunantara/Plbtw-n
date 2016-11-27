package com.kelc.plbtw_n.plbtw_n.Main.Top;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kelc.plbtw_n.plbtw_n.LoginAndRegister.LoginActivity;
import com.kelc.plbtw_n.plbtw_n.Main.API_KEY;
import com.kelc.plbtw_n.plbtw_n.Main.Connection;
import com.kelc.plbtw_n.plbtw_n.Main.MainActivity;
import com.kelc.plbtw_n.plbtw_n.Main.URLList;
import com.ramotion.foldingcell.FoldingCell;


import com.kelc.plbtw_n.plbtw_n.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.wangyuwei.loadingview.LoadingView;

public class TopFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView listViewcoba1;
    private LoadingView loading_view;
    API_KEY api_key = new API_KEY();
    URLList url = new URLList();
    ArrayList<modelTopNews> items = new ArrayList<modelTopNews>();
    FoldingCellListAdapter adapter  ;

    public TopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_top, container, false);
        listViewcoba1 = (ListView) view.findViewById(R.id.mainListView);
        loading_view = (LoadingView) view.findViewById(R.id.loading_view);

        initiate();
        String urlParameters = "api_key=" + api_key.getApi_key() ;
        new NewsTopTask().execute(url.getUrl_News(), urlParameters);


        // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
        adapter = new FoldingCellListAdapter(getContext(), items);

        // add default btn handler for each request btn on each item if custom handler not found
        adapter.setDefaultRequestBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "DEFAULT HANDLER FOR ALL BUTTONS", Toast.LENGTH_SHORT).show();
            }
        });

        // set elements to adapter
        listViewcoba1.setAdapter(adapter);

        // set on click event listener to list view
        listViewcoba1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // toggle clicked cell state
                ((FoldingCell) view).toggle(false);
                // register in adapter that state for selected cell is toggled
                adapter.registerToggle(pos);
            }
        });


        return view;
    }

    private void initiate(){

        loading_view.setVisibility(View.VISIBLE);
        listViewcoba1.setVisibility(View.GONE);
    }

    private void refresh(){

        loading_view.setVisibility(View.GONE);
        listViewcoba1.setVisibility(View.VISIBLE);
    }

    private class NewsTopTask extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute(){
           loading_view.start();
        }

        @Override
        protected String doInBackground(String... urls) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(urls[0],urls[1]);
            return json;
        }


        protected void onPostExecute(String result) {
            Log.d("RES", result);
            showNews(result);
            loading_view.stop();
            refresh();
        }
    }
    private void showNews(String result){

        try {
            JSONObject getObjNews = new JSONObject(result);
            JSONArray jAryNews = getObjNews.getJSONArray("result");
            Log.d("COUNT", String.valueOf(jAryNews.length()));
            for (int i = 0; i < jAryNews.length(); i++) {
                JSONObject jObj = jAryNews.getJSONObject(i);
                modelTopNews news = new modelTopNews();
                    news.setId_news(jObj.getString("id_news"));
                    news.setTitle(jObj.getString("title"));
                    news.setDate(jObj.getString("date"));
                    news.setContent(jObj.getString("content"));
                    news.setCategory(jObj.getString("category"));
                    news.setSub_category(jObj.getString("sub_category"));
                    news.setLocation(jObj.getString("location"));
                    news.setNews_web(jObj.getString("news_web"));
                    news.setNews_url(jObj.getString("news_url"));
                    news.setKeyword(jObj.getString("keyword"));
                    news.setImage(jObj.getString("image"));
                    items.add(news);
            }
        } catch (JSONException e){e.printStackTrace();}
    }
}
