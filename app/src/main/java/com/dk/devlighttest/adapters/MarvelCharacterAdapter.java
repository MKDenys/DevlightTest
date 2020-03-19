package com.dk.devlighttest.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dk.devlighttest.R;
import com.dk.devlighttest.model.json.objects.MarvelCharacter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

public class MarvelCharacterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<MarvelCharacter> characters;
    private List<MarvelCharacter> copyCharactersListForSearch;
    private Context context;
    private final static int VIEW_ITEMS = 0;
    private final static int VIEW_PROGRESS_BAR = 1;
    private boolean isSearch = false;

    public MarvelCharacterAdapter(Context context, List<MarvelCharacter> characters) {
        this.characters = characters;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if(viewType == VIEW_ITEMS) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.characters_list_item, parent, false);
            viewHolder = new ItemViewHolder(v);
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
            itemViewHolder.comicsCount.setText(String.valueOf(item.getComics().getCount()));
            itemViewHolder.seriesCount.setText(String.valueOf(item.getSeries().getCount()));
            Picasso.with(context).load(item.getImage().getUrlSmall())
                    .transform(new CircleTransform())
                    .into(itemViewHolder.image);
        }
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isSearch){
            return VIEW_ITEMS;
        } else if (characters.size() > 0) {
            if (position == characters.size() - 1) {
                return VIEW_PROGRESS_BAR;
            } else {
                return VIEW_ITEMS;
            }
        } else {
            return VIEW_PROGRESS_BAR;
        }
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
        notifyDataSetChanged();
    }

    public void addListToTop(List<MarvelCharacter> marvelCharacters){
        characters.addAll(0, marvelCharacters);
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

    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView seriesCount;
        private TextView comicsCount;
        private ImageView image;

        public ItemViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView_characterName);
            seriesCount = itemView.findViewById(R.id.textView_seriesCount);
            comicsCount = itemView.findViewById(R.id.textView_comicsCount);
            image = itemView.findViewById(R.id.imageView_characterImage);
        }

    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public ProgressViewHolder(View view) {
            super(view);
            this.progressBar = view.findViewById(R.id.progress);
        }
    }

    public static class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
