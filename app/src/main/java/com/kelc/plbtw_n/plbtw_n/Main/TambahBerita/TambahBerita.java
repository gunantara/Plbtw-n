package com.kelc.plbtw_n.plbtw_n.Main.TambahBerita;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.kelc.plbtw_n.plbtw_n.Main.Connection;
import com.kelc.plbtw_n.plbtw_n.Main.TambahBerita.TambahBerita;
import com.kelc.plbtw_n.plbtw_n.Main.API_KEY;
import com.kelc.plbtw_n.plbtw_n.Main.URLList;
import com.kelc.plbtw_n.plbtw_n.Main.modelNews;
import com.kelc.plbtw_n.plbtw_n.R;
import com.kelc.plbtw_n.plbtw_n.Main.MainActivity;


import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.data;
import static com.kelc.plbtw_n.plbtw_n.R.id.imageView;
import static com.kelc.plbtw_n.plbtw_n.R.id.input_addberita_activity_tanggal;

public class TambahBerita extends AppCompatActivity {

    private EditText input_addberita_activity_judul,input_addberita_activity_lokasi, input_addberita_activity_newsweb, input_addberita_activity_URL
            ,input_addberita_activity_keyword, input_addberita_activity_nama_gambar,input_addberita_activity_isi_berita, input_addberita_activity_tanggal;
    private Button button_addberita_activity_image, button_addtaskberita_activity_simpan;
    private ImageView image_addberita_activity_image;
    private Spinner spinner_addberita_activity_category, spinner_addberita_activity_subcategory;
    private SimpleDateFormat dateFormat;

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

    //Uri to store the image uri
    private Uri filePath;

    ArrayList<modelNews> items = new ArrayList<modelNews>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_berita);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set Date Format and Initialize Date Picker
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        SweetAlertDialog pDialog = new SweetAlertDialog(TambahBerita.this, SweetAlertDialog.PROGRESS_TYPE);

        //Set Spinner Value--------------------------------------
        setSpinner();

        //Set Input Text Panjang,Berat,dll-----------------------
        input_addberita_activity_judul = (EditText)findViewById(R.id.input_addberita_activity_judul);
        input_addberita_activity_tanggal = (EditText)findViewById(R.id.input_addberita_activity_tanggal);
        input_addberita_activity_lokasi = (EditText)findViewById(R.id.input_addberita_activity_lokasi);
        input_addberita_activity_newsweb = (EditText)findViewById(R.id.input_addberita_activity_newsweb);
        input_addberita_activity_URL = (EditText)findViewById(R.id.input_addberita_activity_URL);
        input_addberita_activity_keyword = (EditText)findViewById(R.id.input_addberita_activity_keyword);
        input_addberita_activity_nama_gambar = (EditText) findViewById(R.id.input_addberita_activity_text_image);
        input_addberita_activity_isi_berita = (EditText) findViewById(R.id.input_addberita_activity_isi_berita);
        image_addberita_activity_image = (ImageView) findViewById(R.id.image_addberita_activity_image);
        spinner_addberita_activity_category = (Spinner) findViewById(R.id.spinner_addberita_activity_category);
        spinner_addberita_activity_subcategory = (Spinner) findViewById(R.id.spinner_addberita_activity_subcategory);

        //Show DatePickerDialog On Input Tanggal Click
        input_addberita_activity_tanggal.setInputType(InputType.TYPE_NULL);
        input_addberita_activity_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentDate = Calendar.getInstance();
                new DatePickerDialog(v.getContext(), mDateSetListener, currentDate.get(Calendar.YEAR),
                        currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Insert to Database-------------------------------------
        button_addberita_activity_image =  (Button)findViewById(R.id.button_addberita_activity_image);
        button_addberita_activity_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == button_addberita_activity_image) {
                    showFileChooserOnGallery();
                }
            }
        });

        button_addtaskberita_activity_simpan = (Button)findViewById(R.id.button_addtaskberita_activity_simpan);
        button_addtaskberita_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String urlParameters = "title=" + URLEncoder.encode(input_addberita_activity_judul.getText().toString(), "UTF-8")
                            + "&date=" + URLEncoder.encode(input_addberita_activity_tanggal.getText().toString(), "UTF-8")
                            + "&content=" + URLEncoder.encode(input_addberita_activity_isi_berita.getText().toString(), "UTF-8")
                            + "&category=" + URLEncoder.encode(spinner_addberita_activity_category.getSelectedItem().toString(), "UTF-8")
                            + "&sub_category=" + URLEncoder.encode(spinner_addberita_activity_subcategory.getSelectedItem().toString(), "UTF-8")
                            + "&location=" + URLEncoder.encode(input_addberita_activity_lokasi.getText().toString(), "UTF-8")
                            + "&news_web=" + URLEncoder.encode(input_addberita_activity_newsweb.getText().toString(), "UTF-8")
                            + "&news_url=" + URLEncoder.encode(input_addberita_activity_URL.getText().toString(), "UTF-8")
                            + "&keyword=" + URLEncoder.encode(input_addberita_activity_keyword.getText().toString(), "UTF-8")
                            + "&img_name=" + URLEncoder.encode(input_addberita_activity_nama_gambar.getText().toString(), "UTF-8")
                            + "&image=" + URLEncoder.encode(getStringImage(bitmap), "UTF-8")
                            + "&api_key=" + URLEncoder.encode(api_key.getApi_key(), "UTF-8");
                    new UploadNewsTask().execute(url.getUrl_InsertNews(), urlParameters);
                    Log.d("HASH", "image" + getStringImage(bitmap));
                } catch (UnsupportedEncodingException u) {
                    u.printStackTrace();
                }
            }
        });
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            try {
                input_addberita_activity_tanggal.setText(
                    String.valueOf(dayOfMonth) + "-" +
                    String.valueOf(month + 1) + "-" +
                    String.valueOf(year)
                );
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    };

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image_addberita_activity_image.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private class UploadNewsTask extends AsyncTask<String, Integer, String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(TambahBerita.this, SweetAlertDialog.PROGRESS_TYPE);

        @Override
        protected void onPreExecute(){
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#fa6900"));
            pDialog.setTitleText("Tunggu Sebentar");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(urls[0],urls[1]);
            return json;
        }


        protected void onPostExecute(String result) {
            Log.d("RES", result);
            try {
                JSONObject jobj = new JSONObject(result);
                if (jobj.get("result").toString().equalsIgnoreCase("Failed Insertion")) {
                    new SweetAlertDialog(TambahBerita.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Peringatan!")
                            .setContentText("Terjadi Kesalahan, Data Tidak Berhasil Dimasukkan")
                            .show();
                    pDialog.dismiss();
                } else {
                    Toast.makeText(getApplication(), "Selamat, Berita Anda Sudah Masuk", Toast.LENGTH_LONG).show();
                    finish();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplication(), "Terjadi Kesalahan..", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }
    }
}
