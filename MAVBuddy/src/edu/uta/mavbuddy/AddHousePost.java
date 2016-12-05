package edu.uta.mavbuddy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import edu.uta.mavbuddy.validation.ValidationHousing;

public class AddHousePost extends Fragment {

	EditText name;
	EditText title;
	EditText aptName;
	EditText noOfPerson;
	EditText moveInDate;
	EditText moveOutDate;
	EditText rent;
	EditText mobileNo;
	EditText emailId;
	EditText comments;
	
	ValidationHousing validationHousing = new ValidationHousing();
	
	public AddHousePost() {
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
		validationHousing(inflater,container);
		return inflater.inflate(R.layout.fragment_add_house_post, container,
				false);
	}
	
	public void validationHousing(LayoutInflater inflater,ViewGroup container){
		
		View view = inflater.inflate(R.layout.fragment_add_house_post,container,false);
        
		name= (EditText) view.findViewById(R.id.name);
		
		name.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				validationHousing.isValidName(name);
			}
		});
	}
}
