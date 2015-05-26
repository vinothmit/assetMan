package com.htfs.assettracking;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class SearchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent serIntent = getIntent();
        if (serIntent.ACTION_SEARCH.equals(serIntent.getAction())) {
            String queryText = serIntent.getStringExtra(SearchManager.QUERY);
            getResultforQuery(queryText);
        }
    }

    private void getResultforQuery(String queryText) {

    }

}
