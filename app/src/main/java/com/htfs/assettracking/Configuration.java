package com.htfs.assettracking;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.htfs.utilpackage.CheckDataSourceConnection;


public class Configuration extends Activity implements IActivityConnector {

    private EditText ipET, portET;
    private Button testConnBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        ipET = (EditText) findViewById(R.id.dbipaddress);
        portET = (EditText) findViewById(R.id.dbport);
        testConnBtn = (Button) findViewById(R.id.testConnection);
        Bundle geti = getIntent().getExtras();
        if (geti != null && geti.containsKey("ip")) {
            if (!geti.getString("ip").equals("")) {
                String val[] = geti.getString("ip").replace("/", "").split(":");
                ipET.setText(val[1]);
                portET.setText(val[2]);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        testConnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckDataSourceConnection c =
                        new CheckDataSourceConnection(Configuration.this, Configuration.this, ipET.getText().toString(), portET.getText().toString(), true);
                c.check();
            }
        });
    }


    @Override
    public void startIntentTo() {
        SharedPreferences sharedPreferencesDb = getSharedPreferences(Login.DB_CONFIG, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesDb.edit();
        editor.putString(Login.DBSTRING, "http://" + ipET.getText().toString() + ":" + portET.getText().toString() + "/");
        editor.putBoolean(Login.HASCONFIG, true);
        editor.commit();
        Intent i = new Intent(this, Login.class);
        startActivity(i);
        finish();
    }
}
