package com.haqqnuru.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<NewsItems>> {


    private static final String NEWS_REQUEST_URL = "https://content.guardianapis.com/search?";
    private static final int NEWS_LOADER_ID = 0;

    // list of variables
    private NewsItemsAdapter Adapter;
    private TextView mEmptyStateTextView;
    private View mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find reference to variables called
        mProgressBar = findViewById(R.id.progressBar);
        ListView newsItemsListView = findViewById(R.id.newslist);
        mEmptyStateTextView = findViewById(R.id.empty_view);

        // called when listview is empty
        newsItemsListView.setEmptyView(mEmptyStateTextView);

        // list view adapter
        Adapter = new NewsItemsAdapter(this, new ArrayList<NewsItems>());

        // Set the adapter
        // so the list can be populated in the user interface
        newsItemsListView.setAdapter(Adapter);

        // setting onItemClickListener to the listView so as to pass an intent to
        // news URL when an item is clicked
        newsItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Find the current news item that was clicked
                NewsItems currentNews = Adapter.getItem(i);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNews.getUrl());

                // Create a new intent to view the news URI
                Intent newsiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                //check if user's device has an appthat can handle the intent
                if (newsiteIntent.resolveActivity(getPackageManager()) != null) {
                    // Send the intent to launch the site
                    startActivity(newsiteIntent);
                }
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            getSupportLoaderManager().initLoader(NEWS_LOADER_ID, null, this);
        } else {

            // hide progressbar so error message will be visible
            mProgressBar.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(getResources().getString(R.string.no_internet));
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    @Override
    public Loader<List<NewsItems>> onCreateLoader(int id, @Nullable Bundle args) {

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(NEWS_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value.
        uriBuilder.appendQueryParameter("api-key", "61285157-09c3-49c4-87b3-a1431a314989");
        uriBuilder.appendQueryParameter("show-tags", "contributor");

        //Return the completed uri
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsItems>> loader, List<NewsItems> data) {

        // Hide loading progressBar because the data has been loaded
        mProgressBar.setVisibility(View.GONE);

        // Set empty state text to display "No news found."
        mEmptyStateTextView.setText(getResources().getString(R.string.no_news));

        // Clear the adapter of previous news data
        Adapter.clear();

        // If there is a valid list of news, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            Adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<NewsItems>> loader) {

        // Loader reset, so we can clear out our existing data.
        Adapter.clear();
    }
}
