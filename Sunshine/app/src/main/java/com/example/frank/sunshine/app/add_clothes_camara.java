package com.example.frank.sunshine.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.provider.MediaStore.Images;

/**
 * Created by bob on 2015/5/31.
 */
public class add_clothes_camara extends ActionBarActivity{
    private Button button01 , button02, button03;
    private SeekBar seekbar;
    private TextView textView;
    ImageView imageView = null;
    private int level ;
    private Bitmap photo = null;
    Uri selectedImageUri = null;
    int progress = 0;
    int OK = 0;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camara);

        button01 = (Button)findViewById(R.id.button1) ;
        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_Camera(v);
            }
        });
        button02 = (Button)findViewById(R.id.button2) ;
        button02.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onClick_check(v);
            }
        });
        button03 = (Button)findViewById(R.id.buttonFromFile);
        button03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_File(v);
            }
        });
        seekbar = (SeekBar)findViewById(R.id.seekbar);
        textView = (TextView)findViewById(R.id.bartext);
        textView.setText(seekbar.getProgress() + "/" + seekbar.getMax());
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged (SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText(progress + "/" + seekBar.getMax());
            }
        });
            //onClick_Camera();
    }

    private static final int CAMERA_REQUEST = 1888;
    private static final int FILE_REQUEST = 1889;
    private static final int DBHELPER_REQUEST = 1880 ;

    public void onClick_File(View view) {
        Intent FromFileIntent = new Intent();
        FromFileIntent.setType("image/*");
        FromFileIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(FromFileIntent, "file request"), FILE_REQUEST);
    }
    public void onClick_Camera(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            OK = 1;
            if (requestCode == CAMERA_REQUEST) {
                photo = (Bitmap) data.getExtras().get("data");

                imageView = (ImageView) this.findViewById(R.id.imageView1);
                imageView.setImageBitmap(photo);
            } else if (requestCode == FILE_REQUEST) {
                selectedImageUri = data.getData();
                try {
                    photo = (Bitmap) Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                }
                    catch(IOException e){
                    Log.e("!!!ioexception","oh no  !!!!!");
                }
                imageView = (ImageView) this.findViewById(R.id.imageView1);
                imageView.setImageURI(selectedImageUri);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public void onClick_check(View v){
        RadioButton upperCloth , pants , allInOne ;
        String category = new String();
        ClothesDBHelper info = new ClothesDBHelper(this);;
        byte[] byteArray;

        if (progress != 0)
            level = progress;
        else
            Log.d("trimtrimtrim", "!!!!!!!!!");
        Log.d("!!!!!!!!!! 3", "" + level);
        upperCloth = (RadioButton) findViewById(R.id.radio_upperCloth);
        if (upperCloth.isChecked()) {
            category = "Top";
        }
        pants = (RadioButton) findViewById(R.id.radio_bottomPants);
        if (pants.isChecked()) {
            category = "Bottom";
        }
        allInOne = (RadioButton) findViewById(R.id.radio_allInOne);
        if (allInOne.isChecked()) {
            category = "AllInOne";
        }
        if ((!upperCloth.isChecked() && !pants.isChecked() && !allInOne.isChecked()) || OK != 1 || level < 1 || level > 5) {
            Toast.makeText(getApplicationContext(), "fucking insertion!!!!!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byteArray = stream.toByteArray();

        if(info.addClothes(category , level , byteArray)){
            Toast.makeText(add_clothes_camara.this , "add is done" , Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(add_clothes_camara.this , "add is shut down!" , Toast.LENGTH_LONG).show();
        }

        finish();
    }
}
