package com.htfs.contentproviders;

import android.content.SearchRecentSuggestionsProvider;


/**
 * Created by Vinoth on 27-05-2015.
 */
public class SearchSuggestionContent extends SearchRecentSuggestionsProvider {

    public final static String AUTHORITY = "com.htfs.contentproviders.SearchSuggestionContent";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SearchSuggestionContent() {
        setupSuggestions(AUTHORITY, MODE);
    }

}
