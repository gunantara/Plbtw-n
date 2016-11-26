package com.kelc.plbtw_n.plbtw_n.LoginAndRegister;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kelc.plbtw_n.plbtw_n.Main.Connection;
import com.kelc.plbtw_n.plbtw_n.Main.URLList;
import com.kelc.plbtw_n.plbtw_n.Main.API_KEY;
import com.kelc.plbtw_n.plbtw_n.Main.MainActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import com.kelc.plbtw_n.plbtw_n.R;


public class RegisterActivity extends AppCompatActivity {

    private EditText input_register_activity_username, input_register_activity_password;
    private Button button_register_activity_masuk;


    //Get Url Link---------------------------------------------------------
    URLList url = new URLList();

    //Get API_KEY----------------------------------------------------------
    API_KEY api_key = new API_KEY();

    //Role User Mobile-----------------------------------------------------
    private String RoleUser = "User";

    private SharedPreferences shr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shr = getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE);

        if (!shr.contains("keyUsername") || shr.getString("keyUsername", null) == null) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
            setContentView(R.layout.activity_register);
            input_register_activity_username = (EditText) findViewById(R.id.input_register_activity_username);
            input_register_activity_password = (EditText) findViewById(R.id.input_register_activity_password);
            button_register_activity_masuk = (Button) findViewById(R.id.button_register_activity_masuk);


            button_register_activity_masuk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkForm()) {
                        try {
                            String password = input_register_activity_password.getText().toString();
                            String urlParameters = "username=" + URLEncoder.encode(input_register_activity_username.getText().toString(), "UTF-8")
                                    + "&password=" + URLEncoder.encode(password, "UTF-8")
                                    + "&roles=" + URLEncoder.encode(RoleUser, "UTF-8")
                                    + "&api_key=" + URLEncoder.encode(api_key.getApi_key(), "UTF-8");
                            new RegisterActivity.RegisterTask().execute(url.getUrl_Register(), urlParameters);
                            Log.d("HASH", "username= " + input_register_activity_username.getText().toString() + " password= " + input_register_activity_password.getText().toString() + " roles=  " + RoleUser + " api_key= " + api_key);

                        } catch (UnsupportedEncodingException u) {
                            u.printStackTrace();
                        }
                    } else {
                        new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Peringatan!")
                                .setContentText("Isikan Semua Data")
                                .show();
                    }
                }
            });
        } else {
            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
    private boolean checkForm(){
        boolean value = true;
        if(TextUtils.isEmpty(input_register_activity_username.getText().toString())){
            value= false;
            input_register_activity_username.setError("Isikan Username Anda");
        }
        if(TextUtils.isEmpty(input_register_activity_password.getText().toString())){
            value= false;
            input_register_activity_password.setError("Isikan Password Anda");
        }
        return value;
    }

    private class RegisterTask extends AsyncTask<String, Integer, String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.PROGRESS_TYPE);

        @Override
        protected void onPreExecute() {
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#fa6900"));
            pDialog.setTitleText("Tunggu Sebentar");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(urls[0], urls[1]);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("RES", result);
            try {
                JSONObject jobj = new JSONObject(result);
                if (jobj.get("result").toString().equalsIgnoreCase("Username Is Registered")) {
                    new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Peringatan!")
                            .setContentText("Username Sudah Ada")
                            .show();
                    pDialog.dismiss();
                } else {
                    Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(i);
                    Toast.makeText(getApplication(), "Selamat Anda Berhasil Terdaftar", Toast.LENGTH_LONG).show();
                    finish();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplication(), "Terjadi Kesalahan..", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }
    }
}


