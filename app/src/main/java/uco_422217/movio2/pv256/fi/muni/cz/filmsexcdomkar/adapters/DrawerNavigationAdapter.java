package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.R;
import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model.Genre;

/**
 * Created by Petr on 23. 10. 2016.
 */

public class DrawerNavigationAdapter extends BaseAdapter {

        private static final int HEADER = 0, GENRE_ITEM = 1;
    private List<Genre> mGenreList;
        private Context mContext;

    public DrawerNavigationAdapter(List<Genre> list, Context context) {
            if(list != null) {
                mGenreList = list;
            } else {
                mGenreList = new ArrayList<>();
            }
            mContext = context;
        }


    public void setList(List<Genre> list) {
        if (list != null) {
            mGenreList = list;
        }
    }

        @Override
        public int getCount() {
            return mGenreList.size();
        }

        @Override
        public Object getItem(int i) {
            return mGenreList.get(i);
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
            return position == 0 ? HEADER : GENRE_ITEM;
        }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int rowType = getItemViewType(position);
        if (convertView == null) {
            Log.i("inf", "new view");
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            switch (rowType) {
                case HEADER:
                    convertView = inflater.inflate(R.layout.view_header_navigation_drawer, parent, false);
                    DrawerNavigationAdapter.HeaderViewHolder headerViewHolder = new DrawerNavigationAdapter.HeaderViewHolder(convertView);
                    convertView.setTag(R.layout.view_header_navigation_drawer, headerViewHolder);
                    break;
                case GENRE_ITEM:
                    convertView = inflater.inflate(R.layout.view_holder_navigation_drawer, parent, false);
                    DrawerNavigationAdapter.GenreItemViewHolder genreItemViewHolder = new DrawerNavigationAdapter.GenreItemViewHolder(convertView);

                    convertView.setTag(R.layout.view_holder_navigation_drawer, genreItemViewHolder);
                    break;
            }
        }
        Log.i("inf", "change view");

        switch (rowType) {
            case HEADER:
                DrawerNavigationAdapter.HeaderViewHolder headerViewHolder = (DrawerNavigationAdapter.HeaderViewHolder) convertView.getTag(R.layout.view_header_navigation_drawer);
                headerViewHolder.getNameTV().setText("Zobrazované žánry");
                break;
            case GENRE_ITEM:
                final  DrawerNavigationAdapter.GenreItemViewHolder genreItemViewHolder = ( DrawerNavigationAdapter.GenreItemViewHolder) convertView.getTag(R.layout.view_holder_navigation_drawer);
                genreItemViewHolder.getGenreTV().setText(mGenreList.get(position).getName());
                genreItemViewHolder.getShowGenre().setChecked(mGenreList.get(position).isShow());
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGenreList.get(position).setShow(!mGenreList.get(position).isShow());
                        genreItemViewHolder.getShowGenre().setChecked(mGenreList.get(position).isShow());
                    }
                });
                break;
        }
        return convertView;
    }

    private static class GenreItemViewHolder {
        private TextView mGenreTV;
        private CheckBox mShowGenreCB;

        GenreItemViewHolder(View itemView) {
            mGenreTV = (TextView)itemView.findViewById(R.id.genreTV);
            mShowGenreCB = (CheckBox) itemView.findViewById(R.id.showGenreCB);
        }

        TextView getGenreTV() {
            return mGenreTV;
        }

        CheckBox getShowGenre() {
            return mShowGenreCB;
        }
    }


    private static class HeaderViewHolder {
        private TextView mNameTV;

        HeaderViewHolder(View itemView) {
            mNameTV = (TextView)itemView.findViewById(R.id.nameTV);
        }

        TextView getNameTV() {
            return mNameTV;
        }
    }
}
