package com.kelc.plbtw_n.plbtw_n.Main.TambahBerita;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.kelc.plbtw_n.plbtw_n.Main.TambahBerita.TambahBerita;
import com.kelc.plbtw_n.plbtw_n.Main.API_KEY;
import com.kelc.plbtw_n.plbtw_n.Main.URLList;
import com.kelc.plbtw_n.plbtw_n.Main.modelNews;
import com.kelc.plbtw_n.plbtw_n.R;
import com.kelc.plbtw_n.plbtw_n.Main.MainActivity;


import cn.pedant.SweetAlert.SweetAlertDialog;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.R.attr.data;
import static com.kelc.plbtw_n.plbtw_n.R.id.imageView;

public class TambahBerita extends AppCompatActivity {

    private EditText input_addberita_activity_judul,input_addberita_activity_lokasi, input_addberita_activity_newsweb, input_addberita_activity_URL
            ,input_addberita_activity_keyword, input_addberita_activity_nama_gambar,input_addberita_activity_isi_berita, txt_addberita_activity_isi_berita;
    private Button button_addberita_activity_image, button_addtaskberita_activity_simpan;
    private ImageView image_addberita_activity_image;
    private Spinner spinner_addberita_activity_category, spinner_addberita_activity_subcategory;

    private ArrayList<String> list_add_berita_category = new ArrayList<>();
    ArrayAdapter<String> adp;


    private Bitmap bitmap;

    //Get Url Link---------------------------------------------------------
    URLList url = new URLList();

    //Get API_KEY----------------------------------------------------------
    API_KEY api_key = new API_KEY();

    //Pick Image Request
    private int PICK_IMAGE_REQUEST = 1;

    private String URL_GAMBAR = url.getUrl_Image_Upload();
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";

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

        //Insert to Database-------------------------------------
        button_addberita_activity_image =  (Button)findViewById(R.id.button_addberita_activity_image);
        button_addberita_activity_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == button_addberita_activity_image){
                    showFileChooserOnGallery();
                }
            }
        });

        button_addtaskberita_activity_simpan = (Button)findViewById(R.id.button_addtaskberita_activity_simpan);
        button_addtaskberita_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void setSpinner(){
        spinner_addberita_activity_category = (Spinner)findViewById(R.id.spinner_addberita_activity_category);
        spinner_addberita_activity_category.setPrompt("Category Berita");
        final String[] dataCategoryBerita= {"Pilih Jenis Category","News","Olahraga","Entertainment"};
        ArrayAdapter adapterCategoryBerita= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,dataCategoryBerita);
        spinner_addberita_activity_category.setAdapter(adapterCategoryBerita);

        spinner_addberita_activity_subcategory = (Spinner)findViewById(R.id.spinner_addberita_activity_subcategory);
        spinner_addberita_activity_subcategory.setPrompt("Category Sub Berita");
        final String[] dataSubCategoryBerita= {"Pilih Jenis Sub Category","News","Politik","Bencana Alam", "Kriminal", "Olahraga", "Ekonomi", "Entertainment"};
        ArrayAdapter adapterSubCategoryBerita= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,dataSubCategoryBerita);
        spinner_addberita_activity_subcategory.setAdapter(adapterSubCategoryBerita);

    }

    private boolean checkForm(){
        boolean value = true;
        if(TextUtils.isEmpty(input_addberita_activity_judul.getText().toString())){
            value= false;
            input_addberita_activity_judul.setError("Judul harus diisi ");
        }
        if(TextUtils.isEmpty(input_addberita_activity_lokasi.getText().toString())){
            value= false;
            input_addberita_activity_lokasi.setError("Lokasi harus diisi");
        }
        if(TextUtils.isEmpty(input_addberita_activity_newsweb.getText().toString())){
            value= false;
            input_addberita_activity_newsweb.setError("Sumber harus diisi");
        }
        if(TextUtils.isEmpty(input_addberita_activity_URL.getText().toString())){
            value= false;
            input_addberita_activity_URL.setError("URL harus diisi");
        }
        if(TextUtils.isEmpty(input_addberita_activity_keyword.getText().toString())){
            value= false;
            input_addberita_activity_keyword.setError("Keyword harus diisi");
        }
        if(TextUtils.isEmpty(input_addberita_activity_nama_gambar.getText().toString())){
            value= false;
            input_addberita_activity_nama_gambar.setError("nama gambar harus diisi");
        }
        if(TextUtils.isEmpty(input_addberita_activity_isi_berita.getText().toString())){
            value= false;
            input_addberita_activity_isi_berita.setError("Isikan isi berita");
        }

        return value;
    }

    private void showFileChooserOnGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
