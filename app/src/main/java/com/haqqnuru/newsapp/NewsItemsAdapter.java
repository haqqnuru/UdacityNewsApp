package com.haqqnuru.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NewsItemsAdapter extends ArrayAdapter<NewsItems> {


    public NewsItemsAdapter(@NonNull Context context, ArrayList<NewsItems> newsItems) {
        super(context, 0, newsItems);
    }


    // get and positions the view to be populated on listnews_items.xml
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listnews_items, parent, false);
        }

        // Get the current news object located at this position in the list
        NewsItems currentNews = getItem(position);

        // Find the TextView with newsTitle
        TextView newsTitle = convertView.findViewById(R.id.newsTitle);
        // Display the news title of the current news in that TextView
        newsTitle.setText(currentNews.getNewsTitle());

        // Find the TextView with section
        TextView newsSection = convertView.findViewById(R.id.section);
        // Display the section of the current news in that TextView
        newsSection.setText(currentNews.getSection());

        // Find the textView with date
        TextView publicationDate = convertView.findViewById(R.id.publicationDate);
        // Display the date of the current news in that TextView
        String newsDate = currentNews.getPublicationDate();
        // if date is found, display, else set textview visibility to gone
        if (newsDate != null) {
            String date = newsPublicationDate(newsDate);
            publicationDate.setText(date);
            publicationDate.setVisibility(View.VISIBLE);
        } else {
            publicationDate.setVisibility(View.GONE);
        }

        // Find the textView with time
        TextView publicationTime = convertView.findViewById(R.id.publicationTime);
        // Display the time of the current news in that TextView
        String newsTime = currentNews.getPublicationDate();
        // if time is found, display time, else set textview visibility to gone
        if (newsTime != null) {
            String time = newsPublicationTime(newsTime);
            publicationTime.setText(time);
            publicationTime.setVisibility(View.VISIBLE);
        } else {

            publicationTime.setVisibility(View.GONE);
        }

        // Find the textView with author
        TextView authorTextView = convertView.findViewById(R.id.author);
        // Display the author array of the current news in that TextView
        ArrayList<String> authorArray = currentNews.getAuthor();
        // if author is not found set textview visibility to gone
        // else check number of authors and display
        if (authorArray == null) {
            authorTextView.setVisibility(View.GONE);
        } else {
            StringBuilder authorString = new StringBuilder();
            for (int i = 0; i < authorArray.size(); i++) {
                authorString.append(authorArray.get(i));
                if ((i + 1) < authorArray.size()) {
                    authorString.append(",\n");
                }
            }
            authorTextView.setText(authorString.toString());
            authorTextView.setVisibility(View.VISIBLE);
        }

        // Find the TextView with category
        TextView newCategory = convertView.findViewById(R.id.category);
        // Display the category of the current news in that textview
        newCategory.setText(currentNews.getCategory());

        // setting different row colors to list view (Customizing list view)
        if (position % 2 == 0) {
            convertView.setBackgroundResource(R.color.red2);

        } else {
            convertView.setBackgroundResource(R.color.red);
        }

        // Return the list item view that is now showing the appropriate data
        return convertView;
    }

    // Return the news publication date string (i.e. "Mar 3, 1984") from a currentDate object.
    private String newsPublicationDate(String currentDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getContext().getResources()
                .getString(R.string.guardian_date_time));
        SimpleDateFormat newSimpleDateFormat = new SimpleDateFormat(getContext().getResources()
                .getString(R.string.date));
        try {
            return newSimpleDateFormat.format(simpleDateFormat.parse(currentDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    // Return the news publication time  string (i.e. "4:30 PM") from a time object.
    private String newsPublicationTime(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getContext().getResources()
                .getString(R.string.guardian_date_time));
        SimpleDateFormat newSimpleTimeFormat = new SimpleDateFormat(getContext().getResources()
                .getString(R.string.time));
        try {
            return newSimpleTimeFormat.format(simpleDateFormat.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
