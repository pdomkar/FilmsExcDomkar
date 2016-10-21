package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmDetailFragment;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.MainActivity;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.R;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;

/**
 * Created by Petr on 6. 10. 2016.
 */

public class FilmAdapter extends BaseAdapter {
    private Context mAppContext;
    private boolean mTwoPane;
    private ArrayList<Object> mFilmArrayList;
    private static final int CATEGORY = 0, FILM = 1;
    private FragmentManager mFragmentManager;

    public FilmAdapter(ArrayList<Object> filmArrayList, Context context, boolean twoPane, FragmentManager fragmentManager) {
        mAppContext = context.getApplicationContext();
        mTwoPane = twoPane;
        mFilmArrayList = filmArrayList;
        mFragmentManager = fragmentManager;
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
            LayoutInflater inflater = (LayoutInflater) mAppContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            switch (rowType) {
                case CATEGORY:
                    convertView = inflater.inflate(R.layout.view_holder_category, parent, false);
                    CategoryViewHolder categoryViewHolder = new CategoryViewHolder(convertView, mTwoPane);
                    convertView.setTag(R.layout.view_holder_category, categoryViewHolder); //spravne otagujeme viewholder
                    break;
                case FILM:
                    convertView = inflater.inflate(R.layout.view_holder_film, parent, false);
                    FilmViewHolder filmViewHolder = new FilmViewHolder(convertView, mTwoPane, mAppContext, position, mFragmentManager);
                    convertView.setTag(R.layout.view_holder_film, filmViewHolder);
                    break;
            }
        }
        switch (rowType) {
            case CATEGORY:
                CategoryViewHolder categoryViewHolder = (CategoryViewHolder) convertView.getTag(R.layout.view_holder_category);
                categoryViewHolder.setCategory(mFilmArrayList.get(position).toString());
                break;
            case FILM:
                FilmViewHolder filmViewHolder = (FilmViewHolder) convertView.getTag(R.layout.view_holder_film);
                filmViewHolder.setPoster(Uri.EMPTY);
                filmViewHolder.setTitle(((Film) mFilmArrayList.get(position)).getTitle());
                filmViewHolder.setRating( Float.toString( ((Film) mFilmArrayList.get(position)).getPopularity() ) );
                break;
        }
        return convertView;
    }


    private static class FilmViewHolder implements View.OnClickListener{
        private ImageView mPosterIV;
        private TextView mTitleTV;
        private TextView mRatingTV;
        private boolean mTwoPane;
        private Context mAppContext;
        private int mPosition;
        private FragmentManager mFragmentManager;

        public FilmViewHolder(View itemView, boolean twoPane, Context context, int position, FragmentManager fragmentManager) {
            mPosterIV = (ImageView)itemView.findViewById(R.id.posterIV);
            mTitleTV = (TextView)itemView.findViewById(R.id.titleTV);
            mRatingTV = (TextView)itemView.findViewById(R.id.ratingTV);
            mTwoPane = twoPane;
            mAppContext = context;
            mPosition = position;
            mFragmentManager = fragmentManager;
            itemView.setOnClickListener(this);
        }

        public ImageView getPoster() {
            return mPosterIV;
        }

        public void setPoster(Uri poster) {
            mPosterIV.setImageURI(poster);
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

        @Override
        public void onClick(View view) {
            Log.i("sd", "clickkce");

            if (mTwoPane) {
                Bundle args = new Bundle();
                args.putInt(FilmDetailFragment.SELECTED_FILM, mPosition);
                FilmDetailFragment fragment = new FilmDetailFragment();
                fragment.setArguments(args);
                ((AppCompatActivity) mAppContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, fragment)
                        .commit();
            } else {
                FilmDetailFragment newFilmDetailFragment = new FilmDetailFragment();
                Bundle args = new Bundle();
                args.putParcelable(FilmDetailFragment.SELECTED_FILM, (Film)MainActivity.mFilmList.get(mPosition));

                newFilmDetailFragment.setArguments(args);
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

                //change fragment
                fragmentTransaction.replace(R.id.movie_detail_container, newFilmDetailFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                Intent intent = new Intent(mAppContext, FilmDetailFragment.class);
                intent.putExtra(FilmDetailFragment.SELECTED_FILM, mPosition);
                mAppContext.startActivity(intent);
            }
        }
    }

    private static class CategoryViewHolder {
        private boolean mTwoPane;
        private TextView mCategoryTV;

        public CategoryViewHolder(View itemView, boolean twoPane) {
            mCategoryTV = (TextView)itemView.findViewById(R.id.categoryTV);
            mTwoPane = twoPane;
        }

        public TextView getCategory() {
            return mCategoryTV;
        }

        public void setCategory(String category) {
            mCategoryTV.setText(category);
        }

    }

}
