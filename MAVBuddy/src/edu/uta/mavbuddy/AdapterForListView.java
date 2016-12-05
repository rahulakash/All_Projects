package edu.uta.mavbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterForListView extends ArrayAdapter<String>{

	private final Context context;
	private final String[] values;

	  public AdapterForListView(Context context, String[] values) {
	    super(context, R.layout.list_view_layout, values);
	    this.context = context;
	    this.values = values;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
		  
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.list_view_layout, parent, false);
	    TextView textView1 = (TextView) rowView.findViewById(R.id.firstLine);
	    TextView textView2 = (TextView) rowView.findViewById(R.id.secondLine);
	    ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
	    textView1.setText(values[position]);
	    textView2.setText(values[position]+"extra content");
	    imageView.setImageResource(R.drawable.mavbuddy);

	    return rowView;
	  }
}
