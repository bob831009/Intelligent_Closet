package com.example.frank.sunshine.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by frank on 2015/5/31.
 */
public class SwipeImage extends ActionBarActivity {

    public ArrayList<Bitmap> ImageBmpList1 = new ArrayList<Bitmap>(); //top
    public ArrayList<Bitmap> ImageBmpList2 = new ArrayList<Bitmap>(); //bottom

    public ArrayList<Map<String, Object>> UpperImage = new ArrayList<Map<String, Object>>();
    public ArrayList<Map<String, Object>> BottomImage = new ArrayList<Map<String, Object>>();
    public ArrayList<Map<String, Object>> AllInOneImage = new ArrayList<Map<String, Object>>();


    private ArrayList<Integer> UpperIdList = new ArrayList<Integer>();
    private ArrayList<Integer> BottomIdList = new ArrayList<Integer>();

    private static final String UPPER = "Top";
    private static final String BOTTOM = "Bottom";
    private static final String AllInOne = "AllInOne";
    private int id_temp, upper_id, bottom_id;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.main);
        Bundle bundle = this.getIntent().getExtras();
        int level = bundle.getInt("level");
        //int level = 2;

        Button button  = (Button)findViewById(R.id.confirmButton) ;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_Confirm(v);
            }
        });

        Gallery gallery1 = (Gallery) findViewById(R.id.gallery01);
        Gallery gallery2 = (Gallery) findViewById(R.id.gallery02);

        ImageAdapter imageAdapter1 = new ImageAdapter(this);
        ImageAdapter imageAdapter2 = new ImageAdapter(this);

        //設定圖片來源
        ClothesDBHelper clothesDBHelper = new ClothesDBHelper(this);
        byte[] bytearray;
        Bitmap bmp = null;

        UpperImage = clothesDBHelper.getClothes(level, UPPER);
        BottomImage = clothesDBHelper.getClothes(level, BOTTOM);
        AllInOneImage = clothesDBHelper.getClothes(level, AllInOne);

        if (UpperImage.size() == 0 && BottomImage.size() == 0) {
            /* size == 0 */
        } else {  /*from camera*/
            for (int pos = 0; pos < UpperImage.size(); pos++) {
                bytearray = (byte[]) UpperImage.get(pos).get("image");
                bmp = BitmapFactory.decodeByteArray(bytearray, 0, bytearray.length);
                ImageBmpList1.add(bmp);
                id_temp = (Integer) UpperImage.get(pos).get("id");
                UpperIdList.add(id_temp);
            }
            for (int pos = 0; pos < BottomImage.size(); pos++) {
                bytearray = (byte[]) BottomImage.get(pos).get("image");
                bmp = BitmapFactory.decodeByteArray(bytearray, 0, bytearray.length);
                ImageBmpList2.add(bmp);
                id_temp = (Integer) BottomImage.get(pos).get("id");
                BottomIdList.add(id_temp);
                Log.d("!!!!!!!!!!!", "I have bottom");
            }
        }
        //                      http://stackoverflow.com/questions/13854742/byte-array-of-image-into-imageview
        //如何從path找到image    http://stackoverflow.com/questions/4181774/show-image-view-from-file-path-in-android
        //設定圖片的位置
        imageAdapter1.setmImageIds(ImageBmpList1);
        imageAdapter2.setmImageIds(ImageBmpList2);
        //圖片高度
        //imageAdapter.setHeight(600);
        //圖片寬度
        //imageAdapter.setWidth(450);
        gallery1.setAdapter(imageAdapter1);
        gallery1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                upper_id = position;
                Log.d("!!!!!!!!!!!", "upper = " + upper_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
        gallery2.setAdapter(imageAdapter2);
        gallery2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bottom_id = position;
                Log.d("!!!!!!!!!!!!!", "bottom = " + bottom_id);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }

    public void onClick_Confirm(View v){
        //Toast.makeText(SwipeImage.this,  "I am in Confirm" + upper_id + " " + bottom_id, Toast.LENGTH_SHORT).show();
        int up = UpperIdList.get(upper_id);
        int bot = BottomIdList.get(bottom_id);
        Toast.makeText(SwipeImage.this,  "I am in Confirm" + up + " " + bot, Toast.LENGTH_SHORT).show();

        socket_thread t = new socket_thread(up+"_"+bot+"_");
        t.start();
        finish();
    }

    public class socket_thread extends Thread {
        String led_number;
        public socket_thread(String led_number) {
            this.led_number = led_number;
        }
        public void run() {
            SocketClient client = new SocketClient("140.112.30.34", 5566, led_number);
        }
    }


    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private Integer width;
        private Integer height;
        private ArrayList<Bitmap> ImageBmpList = new ArrayList<Bitmap>();

        public ImageAdapter(Context c) {
            context = c;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(context);
            imageView.setImageBitmap(ImageBmpList.get(position));
            //設定圖片的寬、高
            //imageView.setLayoutParams(new Gallery.LayoutParams(width, height));
            imageView.setLayoutParams(new Gallery.LayoutParams(450, 600));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }
        public Integer getHeight() {
            return height;
        }
        public void setHeight(Integer height) {
            this.height = height;
        }
        public Integer getWidth() {
            return width;
        }
        public void setWidth(Integer width) {
            this.width = width;
        }
        public ArrayList<Bitmap> getImageIds() {return ImageBmpList;}
        public void setmImageIds(ArrayList<Bitmap> ImageBmpList) {this.ImageBmpList = ImageBmpList;}
        public void addImage(Bitmap bmp) {ImageBmpList.add(bmp);}
        public void deleteImage(int position) {ImageBmpList.remove(position);}
        public int getCount() {return ImageBmpList.size();}
        public Object getItem(int position) {
            return position;
        }
        public long getItemId(int position) {
            return position;
        }
    }
}
