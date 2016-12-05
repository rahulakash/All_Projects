package edu.uta.mavbuddy;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class HousingFragment extends Fragment implements OnClickListener{

	int mStackLevel = 0;
	ListView list;
	
	public HousingFragment() {
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
		return inflater.inflate(R.layout.fragment_housing, container, false);
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    outState.putInt("level", mStackLevel);
	}
	
	/*public void advanceSearch(View view)
	{
		mStackLevel++;

	   /* android.app.FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
	    android.app.Fragment prev = getActivity().getFragmentManager().findFragmentByTag("dialog");
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    ft.addToBackStack(null);*/
		/*AdvanceSearchFragment dialog = AdvanceSearchFragment.newInstance();
		dialog.show(getActivity().getFragmentManager().beginTransaction(), null);
	}*/

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        

	                if (resultCode == Activity.RESULT_OK) {
	                    // After Ok code.
	                } else if (resultCode == Activity.RESULT_CANCELED){
	                    // After Cancel code.
	                }

	       
	    }
}
