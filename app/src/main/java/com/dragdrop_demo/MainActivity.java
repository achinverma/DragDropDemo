package com.dragdrop_demo;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnDragListener, View.OnLongClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String IMAGE_VIEW_TAG = "LAUNCHER LOGO";
    private static final String TEXT_VIEW_TAG = "DRAG TEXT";
    private static final String BUTTON_VIEW_TAG = "DRAG BUTTON";

    ListView listView;
    List<RowItem> rowItems;
    List<Integer> gridRowItems;
    CustomListViewAdapter adapter;
    ImageAdapter gridImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drag_activity_main);

        findViews();

        implementEvents();

        setGridView();
    }

    private void setGridView() {
        GridView gridView = (GridView) findViewById(R.id.grid_view);

        gridRowItems = new ArrayList<>();
        gridRowItems.add(R.drawable.dddd);
        gridRowItems.add(R.drawable.lady);
        gridRowItems.add(R.drawable.profile_picture_round);
        gridRowItems.add(R.drawable.small_circle);
        gridRowItems.add(R.mipmap.ic_launcher);
        gridRowItems.add(R.mipmap.ic_launcher);
        gridRowItems.add(R.mipmap.ic_launcher);
        gridRowItems.add(R.mipmap.ic_launcher);
        gridRowItems.add(R.mipmap.ic_launcher);
        gridRowItems.add(R.mipmap.ic_launcher);

        // Instance of ImageAdapter Class
        gridImageAdapter = new ImageAdapter(this,gridRowItems);
        gridView.setAdapter(gridImageAdapter);

        rowItems = new ArrayList<RowItem>();

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListViewAdapter(this,R.layout.list_item, rowItems);
        listView.setAdapter(adapter);
    }

    //Find all views and set Tag to all draggable views
    private void findViews() {
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);
    }


    //Implement long click and drag listener
    private void implementEvents() {
        //add or remove any layout view that you don't want to accept dragged view
        findViewById(R.id.top_layout).setOnDragListener(this);
        findViewById(R.id.left_layout).setOnDragListener(this);
        findViewById(R.id.right_layout).setOnDragListener(this);
    }

    @Override
    public boolean onLongClick(View view) {
        // Create a new ClipData.
        // This is done in two steps to provide clarity. The convenience method
        // ClipData.newPlainText() can create a plain text ClipData in one step.

        // Create a new ClipData.Item from the ImageView object's tag
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

    // This is the method that the system calls when it dispatches a drag event to the
    // listener.
    @SuppressLint("LongLogTag")
    @Override
    public boolean onDrag(View view, DragEvent event) {
        //Log.e("onDrag","onDrag");
        // Defines a variable to store the action type for the incoming event
        int action = event.getAction();
        // Handles each of the expected events
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                // Determines if this View can accept the dragged data
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    // if you want to apply color when drag started to your view you can uncomment below lines
                    // to give any color tint to the View to indicate that it can accept
                    // data.

                    //  view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);//set background color to your view

                    // Invalidate the view to force a redraw in the new tint
                    //  view.invalidate();

                    // returns true to indicate that the View can accept the dragged data.
                    return true;

                }
                Log.e("---ACTION_DRAG_STARTED---","---ACTION_DRAG_STARTED---");
                // Returns false. During the current drag and drop operation, this View will
                // not receive events again until ACTION_DRAG_ENDED is sent.
                return false;

            case DragEvent.ACTION_DRAG_ENTERED:

                String clipData = event.getClipDescription().getLabel().toString();

                Log.e("---ACTION_DRAG_ENTERED---","---ACTION_DRAG_ENTERED---"+clipData);
                // Applies a YELLOW or any color tint to the View, when the dragged view entered into drag acceptable view
                // Return true; the return value is ignored.

                //view.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);

                // Invalidate the view to force a redraw in the new tint
                view.invalidate();


                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                // Ignore the event
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                // Re-sets the color tint to blue, if you had set the BLUE color or any color in ACTION_DRAG_STARTED. Returns true; the return value is ignored.
                Log.e("---ACTION_DRAG_EXITED---","---ACTION_DRAG_EXITED---");
                //  view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

                //If u had not provided any color in ACTION_DRAG_STARTED then clear color filter.
                //view.getBackground().clearColorFilter();
                // Invalidate the view to force a redraw in the new tint
                view.invalidate();
                restoreGridViewData();

                return true;
            case DragEvent.ACTION_DROP:
                // TODO Auto-generated method stub
                View dragView = (View) event.getLocalState();
                //ADD THIS LINE
                int mDragResoruceId = dragView.getId();
                // Gets the item containing the dragged data
                ClipData.Item item = event.getClipData().getItemAt(0);

                // Gets the text data from the item.
                String dragData = item.getText().toString();

                // Displays a message containing the dragged data.
                //Toast.makeText(this, "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();

                // Turns off any color tints
                //view.getBackground().clearColorFilter();

                // Invalidates the view to force a redraw
                view.invalidate();

                View v = (View) event.getLocalState();

                ViewGroup owner = (ViewGroup) v.getParent();
                owner.removeView(v);//remove the dragged view

                //LinearLayout container = (LinearLayout) view;//caste the view into LinearLayout as our drag acceptable layout is LinearLayout
                //Log.e("LinearLayout="+container.getTag(),""+container.getId());
                //container.addView(v);//Add the dragged view
                v.setVisibility(View.VISIBLE);//finally set Visibility to VISIBLE
                try {
                    // We drag the item on top of the one which is at itemPosition - list-view
                    int itemPosition = listView.pointToPosition((int)event.getX(), (int)event.getY());
                    // We can even get the view at itemPosition thanks to get/setid
                    View itemView = listView.findViewById(itemPosition);

                    Log.e("Action_drop="+mDragResoruceId,""+dragData);
                    Log.e("itemPosition=","="+itemPosition); // listview item position

                    if(itemPosition == -1){
                        addDataToListViaDragDrop(""+dragData,"position "+mDragResoruceId,mDragResoruceId);
                    }else{
                        gridRowItems.add(adapter.getItem(itemPosition).getImageId());
                        gridImageAdapter.notifyDataSetChanged();
                        replaceItemViaDragDrop(itemPosition);
                        addDataToListViaDragDrop(""+dragData,"position "+mDragResoruceId,mDragResoruceId);

                    }


                    /* If you try the same thing in ACTION_DRAG_LOCATION, itemView
                     * is sometimes null; if you need this view, just return if null.
                     * As the same event is then fired later, only process the event
                     * when itemView is not null.
                     * It can be more problematic in ACTION_DRAG_DROP but for now
                     * I never had itemView null in this event. */
                    // Handle the drop as you like
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Returns true. DragEvent.getResult() will return true.
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                // Turns off any color tinting
                //view.getBackground().clearColorFilter();
                //Log.e("ACTION_DRAG_ENDED","ACTION_DRAG_ENDED");
                // Invalidates the view to force a redraw
                view.invalidate();

                // Does a getResult(), and displays what happened.
                if (event.getResult()){
                    Toast.makeText(this, "The drop was handled.", Toast.LENGTH_SHORT).show();

                } else{
                    Toast.makeText(this, "The drop didn't work.", Toast.LENGTH_SHORT).show();
                    restoreGridViewData();
                }


                // returns true; the value is ignored.
                return true;

            // An unknown action type was received.
            default:
                Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                break;
        }
        return false;
    }

    void addDataToListViaDragDrop(String title, String description, int position){
        RowItem item = new RowItem(gridRowItems.get(position), title, description);
        rowItems.add(item);
        adapter.notifyDataSetChanged();
        Log.e("---list size---",""+rowItems.size());

        gridRowItems.remove(position);
        gridImageAdapter.notifyDataSetChanged();
    }

    void replaceItemViaDragDrop(int position){
        rowItems.remove(position);
    }

    void restoreGridViewData(){
        gridImageAdapter.notifyDataSetChanged();
    }

}
