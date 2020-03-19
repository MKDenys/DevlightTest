package com.dk.devlighttest;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dk.devlighttest.adapters.MarvelCharacterAdapter;
import com.dk.devlighttest.model.CharactersViewModel;
import com.dk.devlighttest.model.json.objects.MarvelCharacter;
import com.dk.devlighttest.model.ChangeType;
import com.dk.devlighttest.utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int START_LOAD_LIMIT = 20;
    private static final int START_LOAD_OFFSET = 1000;
    private static final int LOAD_LIMIT = 10;
    private int firstItemOffsetInTotalList = START_LOAD_OFFSET;
    private int lastItemOffsetInTotalList = START_LOAD_OFFSET + START_LOAD_LIMIT;
    private LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    private RecyclerView recyclerView;
    private List<MarvelCharacter> marvelCharacters = new ArrayList<>();
    private MarvelCharacterAdapter adapter = new MarvelCharacterAdapter(this, marvelCharacters);
    private SwipeRefreshLayout swipeRefreshLayout;
    private CharactersViewModel charactersViewModel;
    private MenuItem searchItem;
    private ConstraintLayout emptyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewElements();
        showEmptyState();
        setupRecyclerView();
        setupAdapter();
        setupSwipeRefresh();
        setupViewModel();
    }

    private void initViewElements(){
        recyclerView = findViewById(R.id.RecyclerView_characters);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        emptyLayout = findViewById(R.id.empty);
    }

    private void showEmptyState(){
        swipeRefreshLayout.setVisibility(View.INVISIBLE);
        emptyLayout.setVisibility(View.VISIBLE);
    }

    private void hideEmptyState(){
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.INVISIBLE);
    }

    private void setupAdapter(){
        adapter.registerAdapterDataObserver(adapterDataObserver);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(scrollListener);
    }

    private void setupViewModel(){
        charactersViewModel = new ViewModelProvider(this).get(CharactersViewModel.class);
        charactersViewModel.init();
        charactersViewModel.setChangeType(ChangeType.STANDARD_LOAD);
        charactersViewModel.loadCharacters(START_LOAD_LIMIT, START_LOAD_OFFSET);
        charactersViewModel.getCharactersRepository().observe(this, observer);
    }

    private void setupSwipeRefresh(){
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    private void setRecyclerViewScrollPosition(int position) {
        int offsetScrollInPixel = 0;
        if (layoutManager.getChildCount() > 0) {
            offsetScrollInPixel = Objects.requireNonNull(layoutManager.getChildAt(0)).getHeight() / 2;
        }
        layoutManager.scrollToPositionWithOffset(position, offsetScrollInPixel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                adapter.setSearch(true);
                recyclerView.removeOnScrollListener(scrollListener);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                adapter.setSearch(false);
                recyclerView.addOnScrollListener(scrollListener);
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                charactersViewModel.setChangeType(ChangeType.SEARCH_LOAD);
                charactersViewModel.loadCharacterByName(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });
        return true;
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (adapter.isSearch()){
                searchItem.collapseActionView();
                swipeRefreshLayout.setRefreshing(false);
            } else {
                charactersViewModel.setChangeType(ChangeType.TOP_LOAD);
                charactersViewModel.loadCharacters(LOAD_LIMIT, firstItemOffsetInTotalList - LOAD_LIMIT);
            }
        }
    };

    private EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
        @Override
        public void onLoadMore(final RecyclerView view) {
            charactersViewModel.setChangeType(ChangeType.BOTTOM_LOAD);
            charactersViewModel.loadCharacters(LOAD_LIMIT, lastItemOffsetInTotalList);
        }
    };

    private RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            if(adapter.getItemCount() == 0){
                showEmptyState();
            } else {
                hideEmptyState();
            }
        }
    };

    private Observer<List<MarvelCharacter>> observer = new Observer<List<MarvelCharacter>>() {
        @Override
        public void onChanged(List<MarvelCharacter> characters) {
            switch (charactersViewModel.getChangeType()){
                case STANDARD_LOAD:
                    adapter.setItems(characters);
                    firstItemOffsetInTotalList = START_LOAD_OFFSET;
                    lastItemOffsetInTotalList = START_LOAD_OFFSET + characters.size();
                    break;
                case TOP_LOAD:
                    adapter.addListToTop(characters);
                    firstItemOffsetInTotalList = firstItemOffsetInTotalList - characters.size();
                    setRecyclerViewScrollPosition(characters.size());
                    swipeRefreshLayout.setRefreshing(false);
                    break;
                case BOTTOM_LOAD:
                    adapter.addListToBottom(characters);
                    lastItemOffsetInTotalList = lastItemOffsetInTotalList + characters.size();
                    break;
                case SEARCH_LOAD:
                    adapter.setFilteredList(characters);
            }
        }
    };
}
