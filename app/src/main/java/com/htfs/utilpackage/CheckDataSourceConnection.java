package com.htfs.utilpackage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.htfs.assettracking.IActivityConnector;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Vinoth on 16-06-2015.
 */
public class CheckDataSourceConnection {

    //<editor-fold desc="Variable">
    Context context;
    String _ip, _port, baseaddress;
    IActivityConnector activityConnector;
    boolean newIntent;
    //</editor-fold>

    /**
     * Constructor
     *
     * @param activity Called activity object
     * @param context  Activity context
     * @param _ip      IP address
     * @param _port    Port number
     */
    public CheckDataSourceConnection(IActivityConnector activity, Context context, String _ip, String _port, boolean newIntent) {
        this.context = context;
        this._ip = _ip;
        this._port = _port;
        activityConnector = activity;
        this.newIntent = newIntent;
    }

    public CheckDataSourceConnection(Context context, String address, boolean newIntent) {
        this.context = context;
        this.baseaddress = address;
        this.newIntent = newIntent;
    }

    /**
     * Check function if the network is active &
     * configuration is correct
     */
    public void check() {
        new AsyncTask<String, Void, String>() {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(context);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Checking connection...");
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
                if (aVoid == null) {
                    Log.d("Connect", "null");
                } else if (aVoid.equals("int")) {
                    DialogBoxDisplay.checkConnect(context);
                } else if (aVoid.equals("success")) {
                    Toast.makeText(context, "The configuration test is successful", Toast.LENGTH_SHORT).show();
                    if (newIntent) activityConnector.startIntentTo();
                }
            }

            @Override
            protected String doInBackground(String... strings) {
                String urlStr =
                        newIntent ? "http://" + strings[0] + ":" + strings[1] + "/AssetManagement/Services/Test"
                                : baseaddress + "/AssetManagement/Services/Test";
                try {
                    URL url = new URL(urlStr);
                    Log.d("CONNECT", url.toString());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(10000);
                    connection.setConnectTimeout(15000);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.connect();
                    int HttpRes = connection.getResponseCode();
                    Log.d("CONNECT", String.valueOf(HttpRes));
                    if (HttpRes == HttpURLConnection.HTTP_OK) {
                        return "success";
                    } else if (HttpRes == HttpURLConnection.HTTP_NOT_FOUND) {
                        return "int";
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "int";
                }
                return null;
            }
        }.execute(_ip, _port);

    }

}
