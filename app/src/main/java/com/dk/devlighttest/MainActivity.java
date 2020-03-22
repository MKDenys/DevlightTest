package com.dk.devlighttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dk.devlighttest.adapters.MarvelCharacterAdapter;
import com.dk.devlighttest.model.ChangeType;
import com.dk.devlighttest.model.MainActivityViewModel;
import com.dk.devlighttest.model.MarvelCharacter;
import com.dk.devlighttest.services.SaveToDBService;
import com.dk.devlighttest.utils.EndlessScrollEventListener;
import com.dk.devlighttest.utils.ImageLazyLoader;
import com.dk.devlighttest.utils.InternetStatusChangeReceiver;
import com.dk.devlighttest.utils.PicassoImageLazyLoader;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int START_LOAD_LIMIT = 20;
    private static final int START_LOAD_OFFSET = 500;
    private static final int LOAD_LIMIT = 10;
    private static final String INTERNET_IS_NOT_AVAILABLE_MESSAGE = "Internet connection is not available. Characters will be display characters from local database";
    private static final String INTERNET_IS_AVAILABLE_MESSAGE = "Internet connection is available. Characters will be display characters from internet";
    private int firstItemOffsetInTotalList = START_LOAD_OFFSET;
    private int lastItemOffsetInTotalList = START_LOAD_OFFSET + START_LOAD_LIMIT;
    private LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    private RecyclerView recyclerView;
    private ImageLazyLoader imageLazyLoader = new PicassoImageLazyLoader(this);
    private MarvelCharacterAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MainActivityViewModel mainActivityViewModel;
    private MenuItem searchItem;
    private ConstraintLayout emptyLayout;
    private InternetStatusChangeReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        receiver = new InternetStatusChangeReceiver(this);
        initViewElements();
        showEmptyState();
        setupAdapter();
        setupRecyclerView();
        setupSwipeRefresh();
        setupViewModel();
        if (!isInternetAvailable()){
            turnOffEndless();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerConnectionReceiver(receiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private void initViewElements(){
        recyclerView = findViewById(R.id.RecyclerView_characters);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        emptyLayout = findViewById(R.id.empty);
    }

    private void setupAdapter(){
        adapter = new MarvelCharacterAdapter(imageLazyLoader, Collections.<MarvelCharacter>emptyList(), onItemClickListener);
        adapter.registerAdapterDataObserver(adapterDataObserver);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(scrollListener);
    }

    private boolean isInternetAvailable(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void setupViewModel(){
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mainActivityViewModel.init(isInternetAvailable());
        mainActivityViewModel.setChangeType(ChangeType.STANDARD_LOAD);
        mainActivityViewModel.loadCharacters(START_LOAD_LIMIT, START_LOAD_OFFSET);
        mainActivityViewModel.getCharactersRepository().observe(this, observer);
    }

    private void setupSwipeRefresh(){
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    private void registerConnectionReceiver(BroadcastReceiver receiver){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver, intentFilter);
    }

    private void showEmptyState(){
        swipeRefreshLayout.setVisibility(View.INVISIBLE);
        emptyLayout.setVisibility(View.VISIBLE);
    }

    private void hideEmptyState(){
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.INVISIBLE);
    }

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

    private void upScrollOnHalfItemHeight(int position) {
        int offsetScrollInPixel = 0;
        if (layoutManager.getChildCount() > 0) {
            offsetScrollInPixel = Objects.requireNonNull(layoutManager.getChildAt(0)).getMeasuredHeight() / 2;
        }
        layoutManager.scrollToPositionWithOffset(position, offsetScrollInPixel);
    }

    private void resetEndlessScroll(){
        scrollListener.reset();
        adapter.showBottomProgressBar();
    }

    private void hideSoftKeyboard(){
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
                hideSoftKeyboard();
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mainActivityViewModel.setChangeType(ChangeType.SEARCH_LOAD);
                mainActivityViewModel.loadCharacterByName(query);
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

    private void turnOffEndless(){
        adapter.hideBottomProgressBar();
        scrollListener.turnOffEndless();
    }

    public void internetStatusChangeReceiverCallBack(boolean connectionStatus){
        if (mainActivityViewModel.isInternetConnectionStatus() != connectionStatus) {
            mainActivityViewModel.setInternetConnectionStatus(connectionStatus);
            mainActivityViewModel.setChangeType(ChangeType.STANDARD_LOAD);
            mainActivityViewModel.loadCharacters(START_LOAD_LIMIT, START_LOAD_OFFSET);
            if (!connectionStatus) {
                Toast.makeText(this, INTERNET_IS_NOT_AVAILABLE_MESSAGE, Toast.LENGTH_LONG).show();
                turnOffEndless();
            } else {
                Toast.makeText(this, INTERNET_IS_AVAILABLE_MESSAGE, Toast.LENGTH_LONG).show();
                resetEndlessScroll();
            }
        }
    }

    private MarvelCharacterAdapter.OnItemClickListener onItemClickListener = new MarvelCharacterAdapter.OnItemClickListener() {
        @Override
        public void onItemClickListener(int position) {
            Intent intent =  CharacterDetailsActivity.getIntentForCharacterInfo(
                    MainActivity.this,
                    adapter.getCharacterByPosition(position).getId(),
                    adapter.getCharacterByPosition(position).getName());
            startActivity(intent);
        }
    };

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (adapter.isSearch()){
                searchItem.collapseActionView();
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
            if (mainActivityViewModel.isInternetConnectionStatus()){
                mainActivityViewModel.setChangeType(ChangeType.TOP_LOAD);
                mainActivityViewModel.loadCharacters(LOAD_LIMIT, firstItemOffsetInTotalList - LOAD_LIMIT);
            } else {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    };

    private EndlessScrollEventListener scrollListener = new EndlessScrollEventListener(layoutManager) {
        @Override
        public void onLoadMore(int pageNum, RecyclerView recyclerView) {
            mainActivityViewModel.setChangeType(ChangeType.BOTTOM_LOAD);
            mainActivityViewModel.loadCharacters(LOAD_LIMIT, lastItemOffsetInTotalList);
        }
    };

    private Observer<List<MarvelCharacter>> observer = new Observer<List<MarvelCharacter>>() {
        @Override
        public void onChanged(List<MarvelCharacter> characters) {
            switch (mainActivityViewModel.getChangeType()){
                case STANDARD_LOAD:
                    swipeRefreshLayout.setRefreshing(false);
                    adapter.setItems(characters);
                    firstItemOffsetInTotalList = START_LOAD_OFFSET;
                    lastItemOffsetInTotalList = START_LOAD_OFFSET + characters.size();
                    break;
                case TOP_LOAD:
                    swipeRefreshLayout.setRefreshing(false);
                    adapter.addListToTop(characters);
                    firstItemOffsetInTotalList = firstItemOffsetInTotalList - characters.size();
                    upScrollOnHalfItemHeight(characters.size());
                    break;
                case BOTTOM_LOAD:
                    int beforeAddedItemCount = adapter.getItemCount();
                    adapter.addListToBottom(characters);
                    int afterAddedItemCount = adapter.getItemCount();
                    lastItemOffsetInTotalList = lastItemOffsetInTotalList + (afterAddedItemCount - beforeAddedItemCount);
                    break;
                case SEARCH_LOAD:
                    adapter.setFilteredList(characters);
            }
            if (mainActivityViewModel.isInternetConnectionStatus() && !characters.isEmpty()) {
                startService(SaveToDBService.getIntentForCharacters(MainActivity.this, characters));
            }
        }
    };
}
