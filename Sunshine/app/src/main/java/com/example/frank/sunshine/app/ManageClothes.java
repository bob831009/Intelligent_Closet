package com.example.frank.sunshine.app; /**
 * Created by kaku on 5/31/15.
 */

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class ManageClothes extends ActionBarActivity {

    ArrayList<Map<String, Object>> all;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        final ArrayList<Integer> idList = new ArrayList<>();
        super.onCreate(savedInstanceState);
        final ClothesDBHelper helper = new ClothesDBHelper(this);
        setContentView(R.layout.manager_main); // HEY YO UI PEOPLE !!!!!
        ListView listView = (ListView) findViewById(R.id.list);
        all = helper.getAllClothes();
        Log.e("!!!","got all");
        if (all.size() > 0) {
           for(int i=0;i < all.size();i++){
                idList.add((int) (all.get(0).get(ClothesDBHelper.CLOTHES_COLUMN_ID)));
            }
            Log.e("!!!!!","idList");
            final cAdapter adapter = new cAdapter(this);
            listView.setAdapter(adapter);

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ManageClothes.this);
                    Log.e("!!!!","boxxxxx");
                    builder.setMessage("Your selected data will be deleted");
                    builder.setTitle("Confirm Delete");
                    builder.setIcon(android.R.drawable.ic_delete);
                    builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            int idToDelete = idList.get(position);
                            Log.e("ready to delete", "!!!!!!!");
                            boolean isDel = helper.deleteClothes(idToDelete);
                            Toast.makeText(getApplicationContext(), isDel ? "Delete Success" : "Delete Failed", Toast.LENGTH_SHORT).show();
                            Log.e("after delete", "!!!!!!!");
                            idList.remove(position);
                            all.clear();
                            all.addAll(helper.getAllClothes());
                            if (all.size() > 0)
                                adapter.notifyDataSetChanged();
                            else{
                                Intent intent = new Intent(ManageClothes.this,ManageClothes.class);
                                startActivity(intent);
                            }
                        }
                    });
                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Log.e("!!!!!", "canceled");
                        }
                    });
                    builder.create().show();
                    return true;
                }
            });

        }
        else {
            Log.e("!!!", "empty closet");
        }
    }

    static class ViewHolder {
        public ImageView img;
        public TextView category;
        public TextView warmth;
    }
    public class cAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private cAdapter(Context context) {
            // 根据context上下文加载布局
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // 在此适配器中所代表的数据集中的条目数
            return all.size();
        }

        @Override
        public Object getItem(int position) {

            // 获取数据集中与指定索引对应的数据项
            return position;
        }

        @Override
        public long getItemId(int position) {

            // 获取在列表中与指定索引对应的行id
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.vlist, null);
                holder.img = (ImageView) convertView.findViewById(R.id.img);


                //holder.img.getLayoutParams().width = 225;
                //holder.img.getLayoutParams().height = 300;
                holder.category = (TextView) convertView.findViewById(R.id.category);
                holder.warmth = (TextView) convertView.findViewById(R.id.warmth);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            byte[] bytearray = (byte[])all.get(position).get("image");
            Bitmap bmp = BitmapFactory.decodeByteArray(bytearray, 0, bytearray.length);
            holder.img.setImageBitmap(bmp);
            holder.category.setText((String) all.get(position).get(ClothesDBHelper.CATEGORY));
            Integer warmth = (Integer)all.get(position).get(ClothesDBHelper.WARMTH_INDEX);
            holder.warmth.setText("warmth level : " + warmth.toString()) ;

            switch ((Integer)all.get(position).get(ClothesDBHelper.WARMTH_INDEX)) {
                case 1:
                    holder.warmth.setTextColor(Color.parseColor("#1c1cc3"));
                    break;
                case 2:
                    holder.warmth.setTextColor(Color.parseColor("#1d7ef5"));
                    break;
                case 3:
                    holder.warmth.setTextColor(Color.parseColor("#32ec76"));
                    break;
                case 4:
                    holder.warmth.setTextColor(Color.parseColor("#f47c0c"));
                    break;
                case 5:
                    holder.warmth.setTextColor(Color.parseColor("#f0531f"));
                    break;
                default:
                    holder.warmth.setTextColor(Color.parseColor("#000000"));
                    break;
            }

            return convertView;
        }
    }
}