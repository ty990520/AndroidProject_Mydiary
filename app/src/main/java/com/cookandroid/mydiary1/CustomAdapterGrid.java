package com.cookandroid.mydiary1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterGrid extends ArrayAdapter implements AdapterView.OnItemClickListener {

    private Context context;
    private List list;  //ArrayList

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
    }

    class ViewHolder {
        public ImageView iv_thumb_grid;
    }

    public CustomAdapterGrid(Context context, ArrayList list){
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.grid_item, parent, false);
        }

        viewHolder = new ViewHolder();
        viewHolder.iv_thumb_grid = (ImageView) convertView.findViewById(R.id.imageViewGrid);

        final GridImage image = (GridImage) list.get(position);
        Glide
                .with(context)
                .load(image.getImgSrc())
                .centerCrop()
                .apply(new RequestOptions().override(350, 350))
                .into(viewHolder.iv_thumb_grid);

        //viewHolder.tv_name.setTag(image.getImgId());


//        //아이템 클릭 방법2 - 클릭시 아이템 반전 효과가 안 먹힘
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, " " + actor.getName(), Toast.LENGTH_SHORT).show();
//            }
//        });

        //Return the completed view to render on screen
        return convertView;
    }
}