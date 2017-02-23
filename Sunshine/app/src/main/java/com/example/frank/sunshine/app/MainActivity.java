package com.example.frank.sunshine.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {
//
    //private static int START_LOCATION = 1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
        Log.d("start", "!!!!!!!!!!!!!!!!!!!!!!!!");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.setposition, menu);
        //getMenuInflater().inflate(R.menu.forecastfragment, menu);
        getMenuInflater().inflate(R.menu.add_clothes, menu);
        getMenuInflater().inflate(R.menu.menu_cloth , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            // Show home screen when pressing "back" button,
            //  so that this app won't be closed accidentally
//            Intent intentHome = new Intent(Intent.ACTION_MAIN);
//            intentHome.addCategory(Intent.CATEGORY_HOME);
//            intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intentHome);
            AlertDialog.Builder ab = new AlertDialog.Builder(this);

            ab.setTitle(R.string.exit_message)
                         .setCancelable(true);

            ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intentHome = new Intent(Intent.ACTION_MAIN);
                    intentHome.addCategory(Intent.CATEGORY_HOME);
                    intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentHome);

                }
            });
            ab.setNegativeButton(android.R.string.cancel, null);

            ab.show();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
