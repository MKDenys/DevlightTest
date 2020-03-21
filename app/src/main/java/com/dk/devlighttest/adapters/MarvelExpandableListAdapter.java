package com.dk.devlighttest.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.dk.devlighttest.R;
import com.dk.devlighttest.model.MarvelCharacter;
import com.dk.devlighttest.model.json.arrays.Comics;
import com.dk.devlighttest.model.json.arrays.MarvelUrl;
import com.dk.devlighttest.model.json.arrays.Series;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MarvelExpandableListAdapter extends BaseExpandableListAdapter {
    private static final String SEPARATOR = "##//##";
    private static final String HTTP = "http";
    private static final String LINK_TEMPLATE = "<a href='%s'> %s </a>";
    private static final String COMICS_TITLE = "Comics";
    private static final String SERIES_TITLE = "Series";
    private static final String LINKS_TITLE = "Links";
    private Context context;
    private List<String> groupTitlesList = new ArrayList<>();
    private HashMap<String, List<String>> childTitlesMap = new HashMap<>();


    public MarvelExpandableListAdapter(Context context, MarvelCharacter character) {
        this.context = context;
        groupTitlesList.add(COMICS_TITLE);
        groupTitlesList.add(SERIES_TITLE);
        groupTitlesList.add(LINKS_TITLE);
        List<String> comicsName = new ArrayList<>();
        for (Comics comics : character.getComics()){
            comicsName.add(comics.getName());
        }
        List<String> seriesName = new ArrayList<>();
        for (Series series : character.getSeries()){
            seriesName.add(series.getName());
        }
        List<String> links = new ArrayList<>();
        for (MarvelUrl marvelUrl : character.getLinks()){
            links.add(marvelUrl.getType() + SEPARATOR + marvelUrl.getUrl());
        }
        childTitlesMap.put(groupTitlesList.get(0), comicsName);
        childTitlesMap.put(groupTitlesList.get(1), seriesName);
        childTitlesMap.put(groupTitlesList.get(2), links);
    }

    @Override
    public int getGroupCount() {
        return groupTitlesList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return Objects.requireNonNull(childTitlesMap.get(groupTitlesList.get(groupPosition))).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return groupTitlesList.get(groupPosition);
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
       return Objects.requireNonNull(childTitlesMap.get(groupTitlesList.get(groupPosition))).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView groupTextView = convertView.findViewById(R.id.expandableListView_group);
        groupTextView.setText(headerTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childText = getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }


        TextView itemTextView = convertView.findViewById(R.id.expandableListView_Item);
        if (childText.contains(HTTP)) {
            itemTextView.setMovementMethod(LinkMovementMethod.getInstance());
            String[] urlAndTitle = childText.split(SEPARATOR);
            itemTextView.setText(Html.fromHtml(String.format(LINK_TEMPLATE, urlAndTitle[1], urlAndTitle[0])));
            itemTextView.setLinkTextColor(Color.BLACK);
            itemTextView.setLinksClickable(true);

        } else {
            itemTextView.setText(childText);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
