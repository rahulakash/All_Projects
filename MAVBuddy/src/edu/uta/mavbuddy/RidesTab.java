package edu.uta.mavbuddy;

import java.util.Locale;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import edu.uta.mavbuddy.validation.ValidationRides;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class RidesTab extends FragmentActivity implements ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	RadioGroup rg;
	EditText name;
	EditText title;
	EditText destination;
	EditText startTime;
	EditText endTime;
	EditText mobileNo;
	EditText emailId;
	EditText noOfPeople;
	EditText comments;
	ValidationRides validationRides = new ValidationRides();
	String username;
	SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rides_tab);

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
		getMenuInflater().inflate(R.menu.rides_tab, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		ListView list = (ListView) findViewById(R.id.rideslistView);
		
		ParseQueryAdapter.QueryFactory<ParseObject> factory =
			     new ParseQueryAdapter.QueryFactory<ParseObject>() {
			       public ParseQuery create() {
			         ParseQuery query = new ParseQuery("rides");
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

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		ListView list = (ListView) findViewById(R.id.rideslistView);
		ParseQueryAdapter.QueryFactory<ParseObject> factory =
			     new ParseQueryAdapter.QueryFactory<ParseObject>() {
			       public ParseQuery create() {
			         ParseQuery query = new ParseQuery("rides");
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
				fragment = new AddRidesPost();
			else
				fragment = new MyRidesPostFragment();
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
				return getString(R.string.title_rides_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_rides_section2).toUpperCase(l);
			}
			return null;
		}
	}
	
	public void saveRidesPost(View view)
	{
		Intent intent = new Intent(RidesTab.this,Tab1.class);
		
		
		rg = (RadioGroup) findViewById(R.id.radioGroup1);
		name = (EditText) findViewById(R.id.name);
		title = (EditText) findViewById(R.id.title);
		destination = (EditText) findViewById(R.id.destination);
		startTime = (EditText) findViewById(R.id.startTime);
		endTime = (EditText) findViewById(R.id.endTime);
		mobileNo = (EditText) findViewById(R.id.mobileNo);
		emailId = (EditText) findViewById(R.id.emailId);
		noOfPeople = (EditText) findViewById(R.id.noOfPerson);
		comments = (EditText) findViewById(R.id.comments);
		
		if(validationRides.isValidName(name) && validationRides.isValidTitle(title) && validationRides.isValidDestination(destination)
				&& validationRides.isValidStartTime(startTime, endTime) && validationRides.isValidEndTime(endTime, startTime)
				&& validationRides.isValidMobNo(mobileNo) && validationRides.isValidEmail(emailId)
				&& validationRides.isValidNoOfPerson(noOfPeople) && validationRides.isValidComments(comments))
		{
			int TypeofPost = rg.getCheckedRadioButtonId();
			String Name = name.getText().toString();
			String Title = title.getText().toString();
			String Destination = destination.getText().toString();
			String StartTime = startTime.getText().toString();
			String EndTime = endTime.getText().toString();
			long MobileNo = Long.parseLong(mobileNo.getText().toString());
			String EmailId = emailId.getText().toString();
			int NoOfPeople = Integer.parseInt(noOfPeople.getText().toString());
			String Comments = comments.getText().toString();
			
			RadioButton btn = (RadioButton) findViewById(TypeofPost);
			String TypeOfPost = (String) btn.getText();
			
			ParseObject rides = new ParseObject("rides");
			rides.put("username",username);
			rides.put("type_of_post", TypeOfPost);
			rides.put("name", Name);
			rides.put("title", Title);
			rides.put("destination", Destination);
			rides.put("start_time", StartTime);
			rides.put("end_time", EndTime);
			rides.put("mob_no", MobileNo);
			rides.put("email", EmailId);
			rides.put("no_of_people", NoOfPeople);
			rides.put("comments", Comments);
			rides.saveInBackground();
			
			
			Bundle b=new Bundle();
			b.putInt("condition",2);
			intent.putExtras(b);
			startActivity(intent);
			
			Context context = getApplicationContext();
			CharSequence text = "Post added successfully!";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}
	
	public void cancelRidesPost(View view)
	{
		Intent intent = new Intent(RidesTab.this,Tab1.class);
		Bundle b=new Bundle();
		b.putInt("condition",2);
		intent.putExtras(b);
		startActivity(intent);
	}
}
