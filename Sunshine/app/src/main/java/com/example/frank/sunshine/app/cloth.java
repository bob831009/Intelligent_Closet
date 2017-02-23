package com.example.frank.sunshine.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;


public class cloth extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloth);
        Log.d("a", "test") ;
        Bundle bundle = this.getIntent().getExtras();
        double low = bundle.getDouble("low");
        double high = bundle.getDouble("high");
        String description = bundle.getString("description");
        Log.d("a", "l:" + low + "\nhigh:" + high + "\ndes:" + description);
        ImageView upcloth = (ImageView)findViewById(R.id.up);
        ImageView lowcloth = (ImageView)findViewById(R.id.low);
        if(low == -1000 && high == -1000) {
            upcloth.setImageResource(R.drawable.error);
        }else if (low > 25) {
            upcloth.setImageResource(R.drawable.polo);
            lowcloth.setImageResource(R.drawable.shortpant);
        } else if (low > 20){
            upcloth.setImageResource(R.drawable.longshort);
            lowcloth.setImageResource(R.drawable.pant);
        } else if (low <= 20){
            upcloth.setImageResource(R.drawable.coat);
            lowcloth.setImageResource(R.drawable.pant);
        }

        //finish() ;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
