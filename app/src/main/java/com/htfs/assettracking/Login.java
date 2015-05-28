package com.htfs.assettracking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.htfs.utilpackage.DialogBoxDisplay;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Login extends Activity {

    private Button button1, loginAuthBtn;
    private final String AUTH_URL = "http://192.168.1.250:8080/CyberFreight/Services/Authentication/Login";
    public final static String USER_SP = "com.htfs.Login.USERDETAILS";
    public final static String USERNAME = "USERNAME";
    public final static String USEREMAIL = "USEREMAIL";
    private JSONObject userObject;
    private SharedPreferences sharedPreferences;
    private TextView usernameTv, passTv;
    private String username_val, password_val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button1 = (Button) findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, AssetTracking.class);
                startActivity(i);
            }
        });

        loginAuthBtn = (Button) findViewById(R.id.astloginauth);
        loginAuthBtn.setOnClickListener(authUser);

        usernameTv = (TextView) findViewById(R.id.astusername);
        passTv = (TextView) findViewById(R.id.astpassword);

        sharedPreferences = getSharedPreferences(USER_SP, MODE_PRIVATE);
    }

    View.OnClickListener authUser = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!usernameTv.getText().toString().isEmpty() && !passTv.getText().toString().isEmpty()) {
                username_val = usernameTv.getText().toString();
                password_val = passTv.getText().toString();
                new AuthendicatedUser().execute(username_val, password_val);
            } else {
                if (usernameTv.getText().toString().isEmpty())
                    usernameTv.setError("Username is required");
                if (passTv.getText().toString().isEmpty())
                    passTv.setError("Password is required");
            }
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPreferences.contains(USERNAME)) {
            if (sharedPreferences.contains(USEREMAIL)) {
                Intent i = new Intent(Login.this, AssetTracking.class);
                startActivity(i);
            }
        }
    }

    private void setUsertoSP(String aVoid) {
        Log.d("LOGIN > USERNAME", aVoid);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME, aVoid);
        editor.putString(USEREMAIL, username_val);
        editor.commit();
        Intent i = new Intent(Login.this, AssetTracking.class);
        startActivity(i);
    }

    class AuthendicatedUser extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Login.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating user...");
            progressDialog.setCancelable(true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    cancel(true);
                }
            });
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            Log.d("TAG", aVoid);
            if (aVoid == null) {

            } else if (aVoid.equals("int")) {
                DialogBoxDisplay.noInte(Login.this);
            } else if (aVoid.equals("res")) {
                DialogBoxDisplay.wrongUser(Login.this);
            } else {
                setUsertoSP(aVoid);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(Login.this, "Task Canceled", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String _username = strings[0];
            String _password = strings[1];
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(AUTH_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.connect();
                JSONObject userPram = new JSONObject();
//                userPram.put("username", _username);
//                userPram.put("password", _password);
                userPram.put("username", "chinkwan@gmail.com");
                userPram.put("password", "asdfghjk");
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(userPram.toString());
                outputStream.flush();
                outputStream.close();
                int HttpRes = connection.getResponseCode();
                if (HttpRes == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            connection.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    Log.d("LOGIN", sb.toString());
                    userObject = new JSONObject(sb.toString());
                    if (userObject.has("firstName"))
                        return userObject.getString("firstName");
                    else if (userObject.has("reason"))
                        return "res";

                } else if (HttpRes == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                    return "int";
                } else if (HttpRes == HttpURLConnection.HTTP_NOT_FOUND) {
                    return "int";
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                return "int";
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


}
