package com.newsapp071997.news;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<NewsDesc> {
    public NewsAdapter(Activity context, ArrayList<NewsDesc> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsDesc newsItem = getItem(position);
        View listItemView = convertView;
        if (listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_layout, parent, false);
        TextView title = listItemView.findViewById(R.id.text_title);
        title.setText(newsItem.getTitleNews());
        TextView sectionName = listItemView.findViewById(R.id.text_section_name);
        sectionName.setText(newsItem.getSectionName());
        TextView date = listItemView.findViewById(R.id.text_date);
        date.setText(newsItem.getDatePub());
        TextView author = listItemView.findViewById(R.id.author_text_view);
        author.setText(newsItem.getAuthor());
        return listItemView;
    }
}
