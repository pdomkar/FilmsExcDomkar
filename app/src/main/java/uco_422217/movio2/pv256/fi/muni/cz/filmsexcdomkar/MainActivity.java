package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mlistFilmsRV;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //change theme
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        boolean highScore = sharedPref.getBoolean("themePrimary", true);
        if(highScore == true) {
            setTheme(R.style.MyPrimaryTheme);
        } else {
            setTheme(R.style.MySecondaryTheme);
        }
        //change theme end

        setContentView(R.layout.activity_main);
        mlistFilmsRV = (RecyclerView) findViewById(R.id.listFilmsRV);
        mlistFilmsRV.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mlistFilmsRV.setLayoutManager(mLayoutManager);

//        // specify an adapter (see also next example)
//        mAdapter = new MyAdapter(myDataset);
//        mlistFilmsRV.setAdapter(mAdapter);

        //change theme
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                boolean themePrimary = sharedPref.getBoolean("themePrimary", new Boolean(true));
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("themePrimary",  !themePrimary);
                editor.commit();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        //change theme end
    }
}
