package ra.securitycam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomLogListViewAdapter extends ArrayAdapter<LogData> {
    private ArrayList<LogData> logDataset;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView tvRowMessage;
        TextView tvRowTime;
    }


    public CustomLogListViewAdapter(ArrayList<LogData> logData, Context context) {
        super(context, R.layout.row_log, logData);
        this.logDataset = logData;
        this.mContext = context;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        LogData logData = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_log, parent, false);
            viewHolder.tvRowMessage = (TextView) convertView.findViewById(R.id.tvRowMessage);
            viewHolder.tvRowTime = (TextView) convertView.findViewById(R.id.tvRowTime);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        viewHolder.tvRowMessage.setText(logData.getMessage());
        viewHolder.tvRowTime.setText(logData.getTime());
        // Return the completed view to render on screen
        return convertView;
    }
}
