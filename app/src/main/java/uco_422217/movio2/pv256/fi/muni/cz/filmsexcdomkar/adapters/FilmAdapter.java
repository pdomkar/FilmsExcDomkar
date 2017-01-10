package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.Consts;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.R;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;

/**
 * Created by Petr on 6. 10. 2016.
 */

public class FilmAdapter extends BaseAdapter {
    private Context mAppContext;
    private List<Object> mFilmArrayList;
    private static final int CATEGORY = 0, FILM = 1;

    public FilmAdapter(List<Object> filmArrayList, Context context) {
        mAppContext = context.getApplicationContext();
        if(filmArrayList != null) {
            this.mFilmArrayList = filmArrayList;
        } else {
            this.mFilmArrayList = new ArrayList<>();
        }
    }

    public void addList(List<Object> list)
    {
        if(list !=  null ) {
            this.mFilmArrayList.addAll(list);
        }
    }

    public void clearList()
    {
        this.mFilmArrayList.clear();
    }

    public List<Object> getList()
    {
        return this.mFilmArrayList;
    }


    @Override
    public int getCount() {
        return this.mFilmArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.mFilmArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).hashCode();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position) instanceof Film ? FILM : CATEGORY;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int a = getCount();
        if(a > 0) {
            int rowType = getItemViewType(position);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mAppContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                switch (rowType) {
                    case CATEGORY:
                        convertView = inflater.inflate(R.layout.view_holder_category, parent, false);
                        CategoryViewHolder categoryViewHolder = new CategoryViewHolder(convertView);
                        convertView.setTag(R.layout.view_holder_category, categoryViewHolder);
                        break;
                    case FILM:
                        convertView = inflater.inflate(R.layout.view_holder_film, parent, false);
                        FilmViewHolder filmViewHolder = new FilmViewHolder(convertView, mAppContext);
                        convertView.setTag(R.layout.view_holder_film, filmViewHolder);
                        break;
                }
            }

            switch (rowType) {
                case CATEGORY:
                    CategoryViewHolder categoryViewHolder = (CategoryViewHolder) convertView.getTag(R.layout.view_holder_category);
                    categoryViewHolder.setCategory(getItem(position).toString());
                    break;
                case FILM:
                    final FilmViewHolder filmViewHolder = (FilmViewHolder) convertView.getTag(R.layout.view_holder_film);
                    filmViewHolder.setTitle(((Film) getItem(position)).getTitle());
                    filmViewHolder.setVoteAverage(((Film) getItem(position)).getVoteAverage());
                    String imagePath = Consts.IMAGE_BASE_PATH + ((Film) getItem(position)).getBackdropPath();
                    if (((Film) getItem(position)).getBackdropPath() != null) {
                        filmViewHolder.setBackdrop(imagePath);
                    } else {
                        ImageLoader imageLoader = ImageLoader.getInstance();
                        imageLoader.displayImage("drawable://" + R.drawable.image_not_found, filmViewHolder.getBackdrop());
                    }
                    break;
            }
        }
        return convertView;
    }


    private static class FilmViewHolder {
        private RelativeLayout mButtonBarRL;
        private ImageView mBackdropIV;
        private TextView mTitleTV;
        private TextView mVoteAverageTV;
        private Context mAppContext;

        FilmViewHolder(View itemView, Context context) {
            mButtonBarRL = (RelativeLayout) itemView.findViewById(R.id.bottomBar);
            mBackdropIV = (ImageView)itemView.findViewById(R.id.backdropIV);
            mTitleTV = (TextView)itemView.findViewById(R.id.titleTV);
            mVoteAverageTV = (TextView)itemView.findViewById(R.id.voteAverageTV);
            mAppContext = context;
        }

        RelativeLayout getBottomBar() {
            return mButtonBarRL;
        }

        public ImageView getBackdrop() {
            return mBackdropIV;
        }

        void setBackdrop(String backdropPath) {
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.loadImage(backdropPath, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    mBackdropIV.setImageBitmap(loadedImage);
                    Palette.from(loadedImage).generate(new Palette.PaletteAsyncListener() {
                        public void onGenerated(Palette palette) {
                            Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                            if (vibrantSwatch != null) {
                                int alpha = (vibrantSwatch.getRgb() & 0x00ffffff) | (128 << 24);
                                GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                                        new int[]{alpha, vibrantSwatch.getRgb()});
                                getTitle().setTextColor(vibrantSwatch.getTitleTextColor());
                                getBottomBar().setBackgroundDrawable(gradient);
                            }
                        }
                    });
                }
            });
        }

        TextView getTitle() {
            return mTitleTV;
        }

        void setTitle(String title) {
            mTitleTV.setText(title);
        }

        public TextView getVoteAverage() {
            return mVoteAverageTV;
        }

        void setVoteAverage(Float voteAverage) {
            mVoteAverageTV.setText(new DecimalFormat("#.#").format(voteAverage));
        }
    }

    private static class CategoryViewHolder {
        private TextView mCategoryTV;

        CategoryViewHolder(View itemView) {
            mCategoryTV = (TextView)itemView.findViewById(R.id.categoryTV);
        }

        public TextView getCategory() {
            return mCategoryTV;
        }

        void setCategory(String category) {
            mCategoryTV.setText(category);
        }

    }
}
