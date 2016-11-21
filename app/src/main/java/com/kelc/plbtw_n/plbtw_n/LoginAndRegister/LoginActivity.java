package com.kelc.plbtw_n.plbtw_n.LoginAndRegister;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kelc.plbtw_n.plbtw_n.Main.Connection;
import com.kelc.plbtw_n.plbtw_n.Main.URLList;
import com.kelc.plbtw_n.plbtw_n.LoginAndRegister.LoginActivity;
import com.kelc.plbtw_n.plbtw_n.LoginAndRegister.RegisterActivity;
import com.kelc.plbtw_n.plbtw_n.Main.MainActivity;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.kelc.plbtw_n.plbtw_n.R;

public class LoginActivity extends AppCompatActivity {

    private EditText input_login_activity_username,input_login_activity_password;
    private Button button_login_activity_masuk,button_login_activity_linktoregister;
    private int attempt = 0;
    private String api_key = "cd8feb5574ee9dac92411ae2b475a889a784fa5dca665caa44e276055fcb91565ed6d94360b037808f736267b266832dd11fd00fb946366fbb25155849794cb0";

    //Get Url Link---------------------------------------------------------
    URLList url = new URLList();


    private SharedPreferences shr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shr = getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE);
        if(!shr.contains("keyUsername") || shr.getString("keyUsername",null)==null) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
            setContentView(R.layout.activity_login);

            SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            input_login_activity_username = (EditText) findViewById(R.id.input_login_activity_username);
            input_login_activity_password = (EditText) findViewById(R.id.input_login_activity_password);
            button_login_activity_masuk = (Button) findViewById(R.id.button_login_activity_masuk);
            button_login_activity_linktoregister = (Button) findViewById(R.id.button_login_activity_linktoregister);

            button_login_activity_linktoregister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                    startActivity(i);
                }
            });

            button_login_activity_masuk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(checkForm()){
                        try {
                            String password = input_login_activity_password.getText().toString();
                            String urlParameters = "username=" + URLEncoder.encode(input_login_activity_username.getText().toString(), "UTF-8")
                                    + "&password=" + URLEncoder.encode(password, "UTF-8")
                                    + "&api_key=" + URLEncoder.encode(api_key, "UTF-8");
                            new LoginTask().execute(url.getUrl_Login(), urlParameters);
                            Log.d("HASH","username=" + input_login_activity_username.getText().toString()+ "password=" + input_login_activity_password.getText().toString() + "api_key=" + api_key);

                        } catch (UnsupportedEncodingException u) {
                            u.printStackTrace();
                        }
                    }else{
                        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Peringatan!")
                                .setContentText("Isikan Semua Data")
                                .show();
                    }
                }
            });
        }
        else{
            Intent i = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }
    private class LoginTask extends AsyncTask<String, Integer, String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);

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
            Log.d("RES",result);
            pDialog.dismiss();
            try {
                JSONObject jobj = new JSONObject(result);
                if (jobj.get("result").toString().equalsIgnoreCase("Wrong Username or Password")) {
                    if (attempt < 3) {
                        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Peringatan!")
                                .setContentText("Password Yang Anda Masukkan Salah")
                                .show();
                        attempt++;
                    } else {
                        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Password Salah!")
                                .setContentText("Apakah Anda Ingin Menggunakan Fitur Lupa Password?")
                                .setConfirmText("Ya")
                                .setCancelText("Tidak")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();

                                    }
                                })
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.cancel();
                                    }
                                })
                                .show();
                        attempt = 0;
                    }

                } else {
                        if (jobj.get("result").toString().equalsIgnoreCase("true")) {
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            savetoLocal(result);
                            finish();
                        }
                }
            }
            catch (JSONException e) {
                Toast.makeText(getApplication(), "Terjadi Kesalahan..", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }
    }

    private void savetoLocal(String user)
    {
        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE);
        try {
            JSONArray jArray = new JSONArray(user);
            JSONObject jObj = jArray.getJSONObject(0);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("keyUsername",jObj.get("username").toString());
            editor.putString("keyRole",jObj.get("role").toString());
            editor.putString("keyPassword",jObj.get("password").toString());
            editor.apply();
        }
        catch (JSONException e){e.printStackTrace();}
    }

    public String generateHash(String password) {
        //String toHash = "someRandomString";
        MessageDigest md = null;
        byte[] hash = null;
        try {
            md = MessageDigest.getInstance("MD5");
            hash = md.digest(password.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return convertToHex(hash);
    }


    private String convertToHex(byte[] raw) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < raw.length; i++) {
            sb.append(Integer.toString((raw[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    private boolean checkForm(){
        boolean value = true;
        if(TextUtils.isEmpty(input_login_activity_username.getText().toString())){
            value= false;
            input_login_activity_username.setError("Isikan Username Anda");
        }
        if(TextUtils.isEmpty(input_login_activity_password.getText().toString())){
            value= false;
            input_login_activity_password.setError("Isikan Password Anda");
        }
        return value;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }


}
