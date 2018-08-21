package com.dragdrop_demo;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    LayoutInflater inflter;
    List<Integer> gridRowItems;


    // Constructor
    public ImageAdapter(Context c,List<Integer> gridRowItemslist){
        mContext = c;
        gridRowItems = gridRowItemslist;
        inflter = (LayoutInflater.from(mContext));
    }

    @Override
    public int getCount() {
        return gridRowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return gridRowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_gridview, null); // inflate the layout

        ImageView icon = (ImageView) view.findViewById(R.id.icon); // get the reference of ImageView
        icon.setImageResource(gridRowItems.get(position)); // set logo images
        icon.setId(position);
        icon.setTag("Item_"+position);

        icon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());

                // Create a new ClipData using the tag as a label, the plain text MIME type, and
                // the already-created item. This will create a new ClipDescription object within the
                // ClipData, and set its MIME type entry to "text/plain"
                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

                ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);

                // Instantiates the drag shadow builder.
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

                // Starts the drag
                view.startDrag(data//data to be dragged
                        , shadowBuilder //drag shadow
                        , view//local data about the drag and drop operation
                        , 0//no needed flags
                );

                //Set view visibility to INVISIBLE as we are going to drag the view
                view.setVisibility(View.INVISIBLE);

                return true;
            }
        });

        return view;
    }

}