package com.kelc.plbtw_n.plbtw_n.Main.TambahBerita;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.kelc.plbtw_n.plbtw_n.Main.TambahBerita.TambahBerita;
import com.kelc.plbtw_n.plbtw_n.Main.API_KEY;
import com.kelc.plbtw_n.plbtw_n.Main.URLList;
import com.kelc.plbtw_n.plbtw_n.Main.modelNews;
import com.kelc.plbtw_n.plbtw_n.R;
import com.kelc.plbtw_n.plbtw_n.Main.MainActivity;


import cn.pedant.SweetAlert.SweetAlertDialog;


import java.util.ArrayList;

public class TambahBerita extends AppCompatActivity {

    private EditText input_addberita_activity_judul,input_addberita_activity_lokasi, input_addberita_activity_newsweb, input_addberita_activity_URL
            ,input_addberita_activity_keyword, input_addberita_activity_nama_gambar, input_addberita_activity_,input_addberita_activity_isi_berita, txt_addberita_activity_isi_berita;
    private Button button_addberita_activity_image, button_addtaskberita_activity_simpan;
    private ImageView image_addberita_activity_image;
    private Spinner spinner_addberita_activity_category, spinner_addberita_activity_subcategory;

    private ArrayList<String> list_add_berita_category = new ArrayList<>();
    ArrayAdapter<String> adp;

    //Get Url Link---------------------------------------------------------
    URLList url = new URLList();

    //Get API_KEY----------------------------------------------------------
    API_KEY api_key = new API_KEY();

    ArrayList<modelNews> items = new ArrayList<modelNews>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_berita);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SweetAlertDialog pDialog = new SweetAlertDialog(TambahBerita.this, SweetAlertDialog.PROGRESS_TYPE);

        //Set Spinner Value--------------------------------------
        setSpinner();

        //Set Input Text Panjang,Berat,dll-----------------------
        input_addberita_activity_judul = (EditText)findViewById(R.id.input_addberita_activity_judul);
        input_addberita_activity_lokasi = (EditText)findViewById(R.id.input_addberita_activity_lokasi);
        input_addberita_activity_newsweb = (EditText)findViewById(R.id.input_addberita_activity_newsweb);
        input_addberita_activity_URL = (EditText)findViewById(R.id.input_addberita_activity_URL);
        input_addberita_activity_keyword = (EditText)findViewById(R.id.input_addberita_activity_keyword);
        input_addberita_activity_nama_gambar = (EditText) findViewById(R.id.input_addberita_activity_text_image);
        image_addberita_activity_image = (ImageView) findViewById(R.id.image_addberita_activity_image);


    }

    private void setSpinner(){
        spinner_addberita_activity_category = (Spinner)findViewById(R.id.spinner_addberita_activity_category);
        spinner_addberita_activity_category.setPrompt("Category Berita");
        final String[] dataCategoryBerita= {"Pilih Jenis Category","News","Sedang","Rendah"};
        ArrayAdapter adapterCategoryBerita= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,dataCategoryBerita);
        spinner_addberita_activity_category.setAdapter(adapterCategoryBerita);

        spinner_addberita_activity_subcategory = (Spinner)findViewById(R.id.spinner_addberita_activity_subcategory);
        spinner_addberita_activity_subcategory.setPrompt("Category Sub Berita");
        final String[] dataSubCategoryBerita= {"Pilih Jenis Sub Category","Olahraga","Sedang","Rendah"};
        ArrayAdapter adapterSubCategoryBerita= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,dataSubCategoryBerita);
        spinner_addberita_activity_subcategory.setAdapter(adapterSubCategoryBerita);

    }

}
