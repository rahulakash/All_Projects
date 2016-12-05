package edu.uta.mavbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class EventsFragment extends Fragment {
	
	int mStackLevel = 0;
	ListView list;
	
	public EventsFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState != null) {
	        mStackLevel = savedInstanceState.getInt("level");
	    }
		list=(ListView) getActivity().findViewById(R.id.list1);
		list.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view,
	                int position, long id) {
	        	
	              // When clicked, show a toast with the TextView text
	                 
	        		 Intent myIntent = new Intent(view.getContext(), View_housing_post.class);
	        		 myIntent.putExtra("postID", list.getItemAtPosition(position).toString());
	                 startActivityForResult(myIntent, 0);
	                 

	            }

			
	          });
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_events, container, false);
	}
}
