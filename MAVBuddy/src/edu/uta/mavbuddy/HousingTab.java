package edu.uta.mavbuddy;

import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import edu.uta.mavbuddy.validation.ValidationHousing;

public class HousingTab extends FragmentActivity implements
		ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	
	String gender;
	
	EditText name;
	EditText title;
	EditText aptName;
	Spinner typeOfApt;
	Spinner typeOfAccom;
	EditText noOfPerson;
	EditText moveInDate;
	EditText moveOutDate;
	RadioGroup rg;
	EditText rent;
	EditText mobileNo;
	EditText emailId;
	EditText comments;
	
	ValidationHousing validationHousing = new ValidationHousing();
	String username;
	SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_housing_tab);
		
		//for getting the username from the shared preferences
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		username = sharedPreferences.getString("userdetails","username");

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.housing_tab, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		ListView list = (ListView) findViewById(R.id.housinglistView);
		
		ParseQueryAdapter.QueryFactory<ParseObject> factory =
			     new ParseQueryAdapter.QueryFactory<ParseObject>() {
			       public ParseQuery create() {
			         ParseQuery query = new ParseQuery("housing");
			         query.whereEqualTo("username", username);
			         query.orderByDescending("createdAt");
			         return query;
			       }
			     };
		
		if(tab.getPosition() == 1)
		{
			
			ParseQueryAdapter<ParseObject> adapter = new ParseQueryAdapter<ParseObject>(this, factory);
			adapter.setTextKey("title");
			//adapter.setTextKey("comments");
		    list.setAdapter(adapter);
		}
		else
		{
			list.setVisibility(0);
			list.setAdapter(null);
		}
	    
		mViewPager.setCurrentItem(tab.getPosition());
	}
	

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		ListView list = (ListView) findViewById(R.id.housinglistView);
		
		ParseQueryAdapter.QueryFactory<ParseObject> factory =
			     new ParseQueryAdapter.QueryFactory<ParseObject>() {
			       public ParseQuery create() {
			         ParseQuery query = new ParseQuery("housing");
			         query.whereEqualTo("username", username);
			         query.orderByDescending("createdAt");
			         return query;
			       }
			     };
		
		if(tab.getPosition() == 1)
		{
			ParseQueryAdapter<ParseObject> adapter = new ParseQueryAdapter<ParseObject>(this, factory);
			adapter.setTextKey("title");
			
		    list.setAdapter(adapter);
		}
		else
		{
			list.setVisibility(0);
			list.setAdapter(null);
		}
	    mViewPager.setCurrentItem(tab.getPosition());
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment;
			if(position == 0)
				fragment = new AddHousePost();
			else
				fragment = new MyHousePostFragment();
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_housing_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_housing_section2).toUpperCase(l);
			}
			return null;
		}
	}
	
	public void saveHousePost(View view)
	{
		Intent intent = new Intent(HousingTab.this,Tab1.class);
		
		
		name = (EditText) findViewById(R.id.name);
		title = (EditText) findViewById(R.id.title);
		aptName = (EditText) findViewById(R.id.aptName);
		typeOfApt = (Spinner) findViewById(R.id.typeOfApt);
		typeOfAccom = (Spinner) findViewById(R.id.typeOfAccom);
		noOfPerson = (EditText) findViewById(R.id.noOfPerson);
		moveInDate = (EditText) findViewById(R.id.moveInDate);
		moveOutDate = (EditText) findViewById(R.id.moveOutDate);
		rg = (RadioGroup) findViewById(R.id.radioGroup1);
		rent = (EditText) findViewById(R.id.rent);
		mobileNo = (EditText) findViewById(R.id.mobileNo);
		emailId = (EditText) findViewById(R.id.emailId);
		comments = (EditText) findViewById(R.id.comments);
		
		if(validationHousing.isValidName(name) && validationHousing.isValidTitle(title) && validationHousing.isValidAptName(aptName)
				&& validationHousing.isValidNoOfPerson(noOfPerson) && validationHousing.isValidMoveInDate(moveInDate, moveOutDate)
				&& validationHousing.isValidMoveOutDate(moveOutDate, moveInDate) && validationHousing.isValidRent(rent)
				&& validationHousing.isValidMobNo(mobileNo) && validationHousing.isValidEmail(emailId) && validationHousing.isValidComments(comments))
		{
			String Name = name.getText().toString();
			String Title = title.getText().toString();
			String AptName = aptName.getText().toString();
			String TypeOfApt = typeOfApt.getSelectedItem().toString();
			String TypeOfAccom = typeOfAccom.getSelectedItem().toString();
			int NoOfPerson = Integer.parseInt(noOfPerson.getText().toString());
			String MoveInDate = moveInDate.getText().toString();
			String MoveOutDate = moveOutDate.getText().toString();
			int RoomFor = rg.getCheckedRadioButtonId();
			int Rent = Integer.parseInt(rent.getText().toString());
			long MobileNo = Long.parseLong(mobileNo.getText().toString());
			String EmailId = emailId.getText().toString();
			String Comments = comments.getText().toString();
			
			RadioButton btn = (RadioButton) findViewById(RoomFor);
			gender = (String) btn.getText();
			
			ParseObject housing = new ParseObject("housing");
			housing.put("username",username);
			housing.put("name", Name);
			housing.put("title", Title);
			housing.put("apt_name", AptName);
			housing.put("type_of_apt", TypeOfApt);
			housing.put("type_of_acc", TypeOfAccom);
			housing.put("persons_req", NoOfPerson);
			housing.put("move_in_date", MoveInDate);
			housing.put("move_out_date", MoveOutDate);
			housing.put("room_for", gender);
			housing.put("rent", Rent);
			housing.put("mob_no", MobileNo);
			housing.put("email", EmailId);
			housing.put("comments", Comments);
			housing.saveInBackground();
			
			
			Bundle b=new Bundle();
			b.putInt("condition",1);
			intent.putExtras(b);
			startActivity(intent);
			
			Context context = getApplicationContext();
			CharSequence text = "Post added successfully!";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		
	}
	
	public void cancelHousePost(View view)
	{
		Intent intent = new Intent(HousingTab.this,Tab1.class);
		Bundle b=new Bundle();
		b.putInt("condition",1);
		intent.putExtras(b);
		startActivity(intent);
	}
}
