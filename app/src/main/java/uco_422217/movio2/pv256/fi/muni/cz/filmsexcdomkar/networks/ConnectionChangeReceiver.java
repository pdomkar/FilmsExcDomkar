package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.networks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.FilmsListFragment;

/**
 * Created by Petr on 30. 10. 2016.
 */

public class ConnectionChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.sendBroadcast(new Intent(FilmsListFragment.ACTION_INTERNET_CHANGE));
    }
}