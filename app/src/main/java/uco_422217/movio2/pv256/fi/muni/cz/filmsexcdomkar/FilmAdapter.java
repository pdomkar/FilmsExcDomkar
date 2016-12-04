package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Film;

/**
 * Created by Petr on 6. 10. 2016.
 */

public class FilmAdapter extends BaseAdapter {
    private Context mAppContext;
    private ArrayList<Film> mFilmArrayList;

    public FilmAdapter(ArrayList<Film> filmArrayList, Context context) {
        if(filmArrayList != null) {
            mFilmArrayList = filmArrayList;
        } else {
            mFilmArrayList = new ArrayList<>();
        }
        mAppContext = context.getApplicationContext();
    }

    @Override
    public int getCount() {
        return mFilmArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        if(position >= 0 && position < mFilmArrayList.size()) {
            return mFilmArrayList.get(position);
        } else {
            throw new IllegalArgumentException("position out of bounds");
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View cView, ViewGroup parent) {
        if (cView == null) {
            cView = LayoutInflater.from(mAppContext).inflate(R.layout.list_item_films, parent, false);
            ViewHolder holder = new ViewHolder(cView);
            cView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) cView.getTag();
        holder.bindView(mAppContext, mFilmArrayList.get(position));

        return cView;
    }

    private class ViewHolder {
        private TextView mTitle;

        public ViewHolder(View view) {
            mTitle = (TextView) view.findViewById(R.id.titleTV);
        }

        public void bindView(Context context, Film film) {
            if (film == null)  return;
            mTitle.setText(film.getTitle());
        }

    }

}
