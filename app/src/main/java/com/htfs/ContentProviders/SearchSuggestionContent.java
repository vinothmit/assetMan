package com.htfs.ContentProviders;

import android.content.SearchRecentSuggestionsProvider;


/**
 * Created by Vinoth on 27-05-2015.
 */
public class SearchSuggestionContent extends SearchRecentSuggestionsProvider {

    public final static String AUTHORITY = "com.htfs.ContentProviders.SearchSuggestionContent";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SearchSuggestionContent() {
        setupSuggestions(AUTHORITY, MODE);
    }

}
