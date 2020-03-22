package com.dk.devlighttest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dk.devlighttest.R;
import com.dk.devlighttest.model.MarvelCharacter;
import com.dk.devlighttest.utils.ImageLazyLoader;

import java.util.ArrayList;
import java.util.List;

public class MarvelCharacterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<MarvelCharacter> characters;
    private List<MarvelCharacter> copyCharactersListForSearch;
    private ImageLazyLoader imageLazyLoader;
    private OnItemClickListener onClickListener;
    private final static int VIEW_ITEMS = 0;
    private final static int VIEW_PROGRESS_BAR = 1;
    private boolean isSearch = false;
    private boolean listEnded = false;

    public MarvelCharacterAdapter(ImageLazyLoader imageLazyLoader, List<MarvelCharacter> characters,
                                  OnItemClickListener onClickListener) {
        this.characters = characters;
        this.imageLazyLoader = imageLazyLoader;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if(viewType == VIEW_ITEMS) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.characters_list_item, parent, false);
            viewHolder = new ItemViewHolder(v, onClickListener);
        } else if (viewType == VIEW_PROGRESS_BAR){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_bar, parent, false);
            viewHolder = new ProgressViewHolder(v);
        }
        assert viewHolder != null;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ProgressViewHolder) {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }else if(holder instanceof ItemViewHolder){
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final MarvelCharacter item = characters.get(position);

            itemViewHolder.name.setText(item.getName());
            itemViewHolder.comicsCount.setText(String.valueOf(item.getComicsCount()));
            itemViewHolder.seriesCount.setText(String.valueOf(item.getSeriesCount()));
            imageLazyLoader.loadCircleImageFromUrl(item.getImage().getUrlSmall(), itemViewHolder.image);
        }
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isSearch || listEnded){
            return VIEW_ITEMS;
        } else if (position == characters.size() - 1) {
            return VIEW_PROGRESS_BAR;
        } else {
            return VIEW_ITEMS;
        }
    }

    public void hideBottomProgressBar() {
        this.listEnded = true;
    }

    public void showBottomProgressBar() {
        this.listEnded = false;
    }

    public MarvelCharacter getCharacterByPosition(int position){
        return characters.get(position);
    }

    public boolean isSearch() {
        return isSearch;
    }

    public void setSearch(boolean search) {
        isSearch = search;
        if (!isSearch){
            setItems(copyCharactersListForSearch);
        }
    }

    public void setItems(List<MarvelCharacter> marvelCharacters) {
        copyCharactersListForSearch = new ArrayList<>();
        copyCharactersListForSearch.addAll(marvelCharacters);
        characters = new ArrayList<>();
        characters.addAll(marvelCharacters);
        notifyDataSetChanged();
    }

    public void setFilteredList(List<MarvelCharacter> marvelCharacters){
        characters = new ArrayList<>();
        characters.addAll(marvelCharacters);
        notifyDataSetChanged();
    }

    public void addListToBottom(List<MarvelCharacter> marvelCharacters){
        characters.addAll(marvelCharacters);
        copyCharactersListForSearch.addAll(marvelCharacters);
        notifyDataSetChanged();
    }

    public void addListToTop(List<MarvelCharacter> marvelCharacters){
        characters.addAll(0, marvelCharacters);
        copyCharactersListForSearch.addAll(0, marvelCharacters);
        notifyDataSetChanged();
    }


    public void filter(String text) {
        List<MarvelCharacter> tempList = new ArrayList<>();
        if(text.isEmpty()){
            tempList.addAll(copyCharactersListForSearch);
        } else{
            text = text.toLowerCase();
            for(MarvelCharacter item: copyCharactersListForSearch){
                if(item.getName().toLowerCase().startsWith(text)){
                    tempList.add(item);
                }
            }
        }
        setFilteredList(tempList);
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private TextView seriesCount;
        private TextView comicsCount;
        private ImageView image;
        private OnItemClickListener onItemClickListener;

        private ItemViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            name = itemView.findViewById(R.id.textView_characterName);
            seriesCount = itemView.findViewById(R.id.textView_seriesCount);
            comicsCount = itemView.findViewById(R.id.textView_comicsCount);
            image = itemView.findViewById(R.id.imageView_characterSmallImage);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.onItemClickListener.onItemClickListener(getAdapterPosition());
        }
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        private ProgressViewHolder(View view) {
            super(view);
            this.progressBar = view.findViewById(R.id.progress);
        }
    }

    public interface OnItemClickListener{
        void onItemClickListener(int position);
    }
}
