package com.htfs.assettracking;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.provider.SearchRecentSuggestions;
import android.os.Bundle;
import android.widget.Toast;

import com.htfs.contentproviders.SearchSuggestionContent;


public class SearchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent serIntent = getIntent();
        if (serIntent.ACTION_SEARCH.equals(serIntent.getAction())) {
            String queryText = serIntent.getStringExtra(SearchManager.QUERY);
            getResultforQuery(queryText);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SearchSuggestionContent.AUTHORITY, SearchSuggestionContent.MODE);
            suggestions.saveRecentQuery(queryText, null);
        }
    }

    private void getResultforQuery(String queryText) {

        Toast.makeText(this, queryText, Toast.LENGTH_SHORT).show();

    }

}
