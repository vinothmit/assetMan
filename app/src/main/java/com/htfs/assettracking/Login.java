package com.htfs.assettracking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    /**
     * UI objects
     */
    private Button loginAuthBtn;
    private TextView usernameTv, passTv;

    /**
     * Configuration and Shared Preference Variables
     */
    public final static String USER_SP = "com.htfs.Login.USERDETAILS";
    public final static String DB_CONFIG = "com.htfs.Login.DBCONFIG";
    public final static String DBSTRING = "DBSTRING";
    public final static String HASCONFIG = "HASCONFIG";
    public final static String USERNAME = "USERNAME";
    public final static String USEREMAIL = "USEREMAIL";
    private SharedPreferences sharedPreferencesUser, sharedPreferencesDB;

    /**
     * Basic Variable
     */
    private String AUTH_URL = null;
    private String username_val, password_val;
    private JSONObject userObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferencesDB = getSharedPreferences(DB_CONFIG, MODE_PRIVATE);
        sharedPreferencesUser = getSharedPreferences(USER_SP, MODE_PRIVATE);

        loginAuthBtn = (Button) findViewById(R.id.astloginauth);
        loginAuthBtn.setOnClickListener(authUser);

        usernameTv = (TextView) findViewById(R.id.astusername);
        passTv = (TextView) findViewById(R.id.astpassword);


    }

    /**
     * Action to check username and password empty
     * and preform Async authentication task
     */
    private View.OnClickListener authUser = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!sharedPreferencesDB.contains(DBSTRING)) {
                Toast.makeText(Login.this,"No Configuration found",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Login.this, Configuration.class);
                startActivity(i);
            } else {
                AUTH_URL = sharedPreferencesDB.getString(DBSTRING, "") + "AssetManagement/Services/Authentication/Login";
                if (!usernameTv.getText().toString().isEmpty() && !passTv.getText().toString().isEmpty()) {
                    username_val = usernameTv.getText().toString();
                    password_val = passTv.getText().toString();
                    /**
                     * Execute the authentication with username and password
                     * @see A
                     */
                    new AuthenticatedUser().execute(username_val, password_val);
                } else {
                    if (usernameTv.getText().toString().isEmpty())
                        usernameTv.setError("Username is required");
                    if (passTv.getText().toString().isEmpty())
                        passTv.setError("Password is required");
                }
            }
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * Check if app has the required configuration
         * else
         * it will prompt user to configuration activity
         */

        /**
         * Check if the user has previously logged in
         */
        if (sharedPreferencesUser.contains(USERNAME)) {
            if (sharedPreferencesUser.contains(USEREMAIL)) {
                Intent i = new Intent(Login.this, AssetTracking.class);
                startActivity(i);
                finish();
            }
        }
    }

    /**
     * Save the user detail after successful login
     * <p>
     * On  successful authentication of user , save their data and
     * make intent to next activity
     * </p>
     *
     * @param userData
     * @return
     */
    private void setUsertoSP(String userData) {
        Log.d("LOGIN > USERNAME", userData);
        SharedPreferences.Editor editor = sharedPreferencesUser.edit();
        editor.putString(USERNAME, userData);
        editor.putString(USEREMAIL, username_val);
        editor.commit();
        Intent i = new Intent(Login.this, AssetTracking.class);
        startActivity(i);
        this.finish();
    }

    /**
     * Inner Async class preform user authentication
     */
    class AuthenticatedUser extends AsyncTask<String, Void, String> {

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
                        sb.append(line).append("\n");
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

            } catch (MalformedURLException | JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                return "int";
            }

            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (R.id.VConfig == item.getItemId()) {
            Intent i = new Intent(this, Configuration.class);
            i.putExtra("ip", sharedPreferencesDB.getString(DBSTRING, ""));
            startActivity(i);
        }
        return true;
    }
}
