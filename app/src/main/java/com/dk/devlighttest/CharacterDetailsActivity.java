package com.dk.devlighttest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.dk.devlighttest.adapters.MarvelExpandableListAdapter;
import com.dk.devlighttest.fragments.WebFragment;
import com.dk.devlighttest.model.CharacterDetailViewModel;
import com.dk.devlighttest.model.MarvelCharacter;
import com.dk.devlighttest.utils.App;
import com.dk.devlighttest.utils.ImageLazyLoader;
import com.dk.devlighttest.utils.PicassoImageLazyLoader;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Callback;

import java.util.List;
import java.util.Objects;

public class CharacterDetailsActivity extends AppCompatActivity {
    private static final String ID_KEY = "id";
    private static final String NAME_KEY = "name";
    private ImageView imageView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ExpandableListView expandableListView;
    private TextView descriptionTextView;
    private long characterId;
    private String characterName;
    private WebFragment webFragment;

    public static Intent getIntentForCharacterInfo(Context context, long characterId, String characterName){
        Intent intent = new Intent(context, CharacterDetailsActivity.class);
        intent.putExtra(ID_KEY, characterId);
        intent.putExtra(NAME_KEY, characterName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_detail);
        if (getIntent().hasExtra(NAME_KEY)){
            characterName = Objects.requireNonNull(getIntent().getExtras()).getString(NAME_KEY);
        }
        if (getIntent().hasExtra(ID_KEY)){
            characterId = Objects.requireNonNull(getIntent().getExtras()).getLong(ID_KEY);
        }
        initViewElements();
        setupAppBar();
        setupViewModel();
        setupExpandableListView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViewElements(){
        imageView = findViewById(R.id.imageView_characterLargeImage);
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        expandableListView = findViewById(R.id.expandableListView);
        descriptionTextView = findViewById(R.id.textView_description);
    }

    private void setupExpandableListView(){
        expandableListView.setOnGroupClickListener(onGroupClickListener);
    }

    private void setupViewModel(){
        CharacterDetailViewModel viewModel = new ViewModelProvider(this).get(CharacterDetailViewModel.class);
        viewModel.init(App.getInstance().isInternetAvailable());
        viewModel.loadCharacterById(characterId);
        viewModel.getCharacterRepository().observe(this, observer);
    }

    private void setupAppBar(){
        if (getSupportActionBar() != null){
            if (!characterName.isEmpty()) {
                getSupportActionBar().setTitle(characterName);
            }
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.semi_transparent)));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += groupItem.getMeasuredHeight();
            if (((listView.isGroupExpanded(i)) && (i != group)) || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height + 200;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private ExpandableListView.OnGroupClickListener onGroupClickListener = new ExpandableListView.OnGroupClickListener() {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            setListViewHeight(parent, groupPosition);
            return false;
        }
    };

    private int getColorFromOnePxImage(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        return newBitmap.getPixel(0, 0);
    }

    private void setColorCollapsingAppBar(int color){
        collapsingToolbarLayout.setStatusBarScrimColor(color);
        collapsingToolbarLayout.setContentScrimColor(color);
    }

    private Callback imageLoadCallback =  new Callback() {
        @Override
        public void onSuccess() {
            imageView.invalidate();
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            setColorCollapsingAppBar(getColorFromOnePxImage(bitmap));
        }

        @Override
        public void onError() {

        }
    };

    private MarvelExpandableListAdapter.OnItemClickListener onItemClickListener = new MarvelExpandableListAdapter.OnItemClickListener() {
        @Override
        public void onItemClickListener(String url) {
            if (webFragment == null || webFragment.isDetached()){
                webFragment = WebFragment.newInstance(url);
                getFragmentManager().beginTransaction().add(R.id.fragment_container_view_tag,  webFragment).commit();
            } else {
                webFragment.loadUrl(url);
            }
        }
    };

    private Observer<List<MarvelCharacter>> observer = new Observer<List<MarvelCharacter>>() {
        @Override
        public void onChanged(List<MarvelCharacter> marvelCharacters) {
            if (!marvelCharacters.isEmpty()) {
                MarvelCharacter marvelCharacter = marvelCharacters.get(0);
                ImageLazyLoader imageLazyLoader = new PicassoImageLazyLoader(CharacterDetailsActivity.this);
                imageLazyLoader.loadImageFromUrlWithCallback(marvelCharacter.getImage().getUrlLarge(),
                        imageView, imageLoadCallback);
                descriptionTextView.setText(marvelCharacter.getDescription());
                MarvelExpandableListAdapter adapter = new MarvelExpandableListAdapter(
                        CharacterDetailsActivity.this, marvelCharacter, onItemClickListener);
                expandableListView.setAdapter(adapter);
            }
        }
    };
}
