package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.ArrayList;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmDetailFragment;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.MainActivity;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.R;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;

/**
 * Created by Petr on 6. 10. 2016.
 */

public class FilmAdapter extends BaseAdapter {
    private Context mAppContext;
    private ArrayList<Object> mFilmArrayList;
    private static final int CATEGORY = 0, FILM = 1;

    public FilmAdapter(ArrayList<Object> filmArrayList, Context context) {
        mAppContext = context.getApplicationContext();
        mFilmArrayList = filmArrayList;
    }


    @Override
    public int getCount() {
        return mFilmArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return mFilmArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return mFilmArrayList.get(position) instanceof Film ? FILM : CATEGORY;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int rowType = getItemViewType(position);
        if (convertView == null) {
            Log.i("INF", "new");
            LayoutInflater inflater = (LayoutInflater) mAppContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            switch (rowType) {
                case CATEGORY:
                    convertView = inflater.inflate(R.layout.view_holder_category, parent, false);
                    CategoryViewHolder categoryViewHolder = new CategoryViewHolder(convertView);
                    convertView.setTag(R.layout.view_holder_category, categoryViewHolder);
                    break;
                case FILM:
                    convertView = inflater.inflate(R.layout.view_holder_film, parent, false);
                    FilmViewHolder filmViewHolder = new FilmViewHolder(convertView);
                    convertView.setTag(R.layout.view_holder_film, filmViewHolder);
                    break;
            }
        }
        Log.i("INF", "change");
        switch (rowType) {
            case CATEGORY:
                CategoryViewHolder categoryViewHolder = (CategoryViewHolder) convertView.getTag(R.layout.view_holder_category);
                categoryViewHolder.setCategory(mFilmArrayList.get(position).toString());
                break;
            case FILM:
                final FilmViewHolder filmViewHolder = (FilmViewHolder) convertView.getTag(R.layout.view_holder_film);
                filmViewHolder.setPoster(Uri.EMPTY);
                filmViewHolder.setTitle(((Film) mFilmArrayList.get(position)).getTitle());
                filmViewHolder.setRating( Float.toString( ((Film) mFilmArrayList.get(position)).getPopularity() ) );
                Bitmap image = BitmapFactory.decodeResource(mAppContext.getResources(), R.drawable.tmpimage);
                Palette.from(image).generate(new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                        if (vibrantSwatch != null) {
                            int alpha = (vibrantSwatch.getRgb() & 0x00ffffff) | (128 << 24);
                            GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                                    new int[] {alpha, vibrantSwatch.getRgb()});

                            filmViewHolder.getTitle().setTextColor(vibrantSwatch.getTitleTextColor());
                            filmViewHolder.getBottomBar().setBackgroundDrawable(gradient);
                        }
                    }
                });
                break;
        }
        return convertView;
    }


    private static class FilmViewHolder {
        private RelativeLayout mButtonBarRL;
        private ImageView mPosterIV;
        private TextView mTitleTV;
        private TextView mRatingTV;

        public FilmViewHolder(View itemView) {
            mButtonBarRL = (RelativeLayout) itemView.findViewById(R.id.bottomBar);
            mPosterIV = (ImageView)itemView.findViewById(R.id.posterIV);
            mTitleTV = (TextView)itemView.findViewById(R.id.titleTV);
            mRatingTV = (TextView)itemView.findViewById(R.id.ratingTV);
        }

        public RelativeLayout getBottomBar() {
            return mButtonBarRL;
        }

        public ImageView getPoster() {
            return mPosterIV;
        }

        public void setPoster(Uri poster) {
            mPosterIV.setImageResource(R.drawable.tmpimage);
        }

        public TextView getTitle() {
            return mTitleTV;
        }

        public void setTitle(String title) {
            mTitleTV.setText(title);
        }

        public TextView getRating() {
            return mRatingTV;
        }

        public void setRating(String rating) {
            mRatingTV.setText(rating);
        }
    }

    private static class CategoryViewHolder {
        private TextView mCategoryTV;

        public CategoryViewHolder(View itemView) {
            mCategoryTV = (TextView)itemView.findViewById(R.id.categoryTV);
        }

        public TextView getCategory() {
            return mCategoryTV;
        }

        public void setCategory(String category) {
            mCategoryTV.setText(category);
        }

    }

}
