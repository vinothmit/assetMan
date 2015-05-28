package com.htfs.assettracking;

import android.app.Activity;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toolbar;

import com.htfs.Adapters.AssetRecyclerAdapter;
import com.htfs.ContentProviders.SearchSuggestionContent;
import com.htfs.model.AssetLocationHistory;

import java.util.ArrayList;
import java.util.List;


public class AllAssetTracking extends Activity {

    private RecyclerView recyclerView;
    AssetRecyclerAdapter recyclerAdapter;
    private List<AssetLocationHistory> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_asset_tracking);
        recyclerView = (RecyclerView) findViewById(R.id.assetRecyclerView);
        recyclerAdapter = new AssetRecyclerAdapter(this, getData());
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    List<AssetLocationHistory> getData() {
        AssetLocationHistory a;
        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            a = new AssetLocationHistory();
            a.setAssetID(String.valueOf(randomWithRange(10000, 15000)));
            a.setAssetDesc("RAND" + String.valueOf(randomWithRange(10000, 15000)));
            list.add(a);
        }
        return list;
    }

    int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_all_asset_tracking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.assetSearch) {
            onSearchRequested();
        }
        if (id == R.id.clearhistory) {
            clearHistory();
        }

        return super.onOptionsItemSelected(item);
    }

    private void clearHistory() {
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                SearchSuggestionContent.AUTHORITY, SearchSuggestionContent.MODE);
        suggestions.clearHistory();
    }


}
