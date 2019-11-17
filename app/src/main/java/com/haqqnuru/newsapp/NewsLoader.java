package com.haqqnuru.newsapp;

import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

//Loads a list of news by using an AsyncTask to perform the
// network request to the given URL.
public class NewsLoader extends AsyncTaskLoader<List<NewsItems>> {

    // Query URL
    private String mUrl;

    // Newsloader constructors
    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsItems> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of news.
        return QueryUtils.fetchNewsData(mUrl);
    }
}
