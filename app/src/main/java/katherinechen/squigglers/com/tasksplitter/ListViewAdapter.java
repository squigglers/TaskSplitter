package katherinechen.squigglers.com.tasksplitter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lindsaycampbell on 10/24/14.
 */
public class ListViewAdapter extends ArrayAdapter<ListViewItem> {

    private Context context;
    private boolean useList = true;


    public ListViewAdapter(Context context, List<ListViewItem> items) {
        super(context, android.R.layout.simple_list_item_1, items);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        ListViewItem item = (ListViewItem) getItem(position);
        View viewToUse = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            // inflate the GridView item layout
            if (useList) {
                viewToUse = mInflater.inflate(R.layout.view_list_item, null);
            } else {
                viewToUse = mInflater.inflate(R.layout.view_grid_item, null);
            }
            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) viewToUse.findViewById(R.id.titleTextView);
            viewToUse.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewToUse = convertView;
            viewHolder = (ViewHolder) viewToUse.getTag();
        }

        // update the item view
        //ListViewItem item = getItem(position);
        viewHolder.title.setText(item.getItemTitle());

        return viewToUse;
    }


    private static class ViewHolder {
        TextView title;
    }
}

