package edu.uta.mavbuddy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link AdvanceSearchFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the {@link AdvanceSearchFragment#newInstance}
 * factory method to create an instance of this fragment.
 * 
 */
public class AdvanceSearchFragment extends DialogFragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	

	private OnFragmentInteractionListener mListener;
	EditText mEditText;
	Spinner rent;
	Spinner numOfPersons;
	Spinner roomFor;
	protected OnClickListener listener;
	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment AdvanceSearchFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static AdvanceSearchFragment newInstance() {
		AdvanceSearchFragment fragment = new AdvanceSearchFragment();
		Bundle args = new Bundle();
		
		fragment.setArguments(args);
		return fragment;
	}
	

	public AdvanceSearchFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view= inflater.inflate(R.layout.fragment_advance_search, container);
		mEditText = (EditText) view.findViewById(R.id.searchDate);
		numOfPersons = (Spinner) view.findViewById(R.id.numOfPerson);
		rent = (Spinner) view.findViewById(R.id.rent);
		roomFor = (Spinner) view.findViewById(R.id.roomFor);
        getDialog().setTitle("Advance Search");

        return view;

	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

	    return new AlertDialog.Builder(getActivity())
	            .setTitle("Advance Search")
	            .setIcon(android.R.drawable.ic_menu_search)
	            .setPositiveButton(R.string.search_button,
	                    new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int whichButton) {
	                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
	                        }
	                    }
	            )
	            .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());
	                }
	            })
	            .create();
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

}
