package edu.uta.mavbuddy;

import java.util.Locale;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import edu.uta.mavbuddy.validation.ValidationEvent;

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
import android.widget.Toast;

public class EventTab extends FragmentActivity implements ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;
	EditText title;
	EditText location;
	EditText startTime;
	EditText endTime;
	EditText cost;
	EditText description;
	ValidationEvent validationEvent = new ValidationEvent();
	String username;
	SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Parse.initialize(this, "NMTwPKAweEH0MrGH5UAIdE8nKC9mc3GkMlvCGALE", "I35KGgtQ2giCz0Ig3QqraJOiorDuxVhdKlUL4Jd5");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_tab);
		
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
		getMenuInflater().inflate(R.menu.event_tab, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		ListView list = (ListView) findViewById(R.id.eventlistView);
		
		ParseQueryAdapter.QueryFactory<ParseObject> factory =
			     new ParseQueryAdapter.QueryFactory<ParseObject>() {
			       public ParseQuery create() {
			         ParseQuery query = new ParseQuery("events");
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
		
		ListView list = (ListView) findViewById(R.id.eventlistView);
		
		ParseQueryAdapter.QueryFactory<ParseObject> factory =
			     new ParseQueryAdapter.QueryFactory<ParseObject>() {
			       public ParseQuery create() {
			         ParseQuery query = new ParseQuery("events");
			         query.whereEqualTo("username", username);
			         query.orderByDescending("createdAt");
			         return query;
			       }
			     };
		
		if(tab.getPosition() == 1)
		{
			
			ParseQueryAdapter<ParseObject> adapter = new ParseQueryAdapter<ParseObject>(this, factory);
			adapter.setTextKey("title");
			adapter.setImageKey("image");
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
				fragment = new AddEventPost();
			else
				fragment = new MyEventPostFragment();
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
				return getString(R.string.title_event_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_event_section2).toUpperCase(l);
			}
			return null;
		}
	}
	
	public void saveEventPost(View view)
	{
		Intent intent = new Intent(EventTab.this,Tab1.class);
		
		title = (EditText) findViewById(R.id.title);
		location = (EditText) findViewById(R.id.location);
		startTime = (EditText) findViewById(R.id.startTime);
		endTime = (EditText) findViewById(R.id.endTime);
		cost = (EditText) findViewById(R.id.cost);
		description = (EditText) findViewById(R.id.description);
		
		if(validationEvent.isValidTitle(title) && validationEvent.isValidLocation(location)
				&& validationEvent.isValidStartTime(startTime, endTime) && validationEvent.isValidEndTime(endTime, startTime)
				&& validationEvent.isValidCost(cost) && validationEvent.isValidDescription(description))
		{
			String Title = title.getText().toString();
			String Location = location.getText().toString();
			String StartTime = startTime.getText().toString();
			String EndTime = endTime.getText().toString();
			String Cost = cost.getText().toString();
			String Description = description.getText().toString();
			
			ParseObject events = new ParseObject("events");
			events.put("username",username);
			events.put("title", Title);
			events.put("location", Location);
			events.put("start_time", StartTime);
			events.put("end_time", EndTime);
			events.put("cost", Cost);
			events.put("description", Description);
			events.put("image",R.drawable.mavbuddy);
			events.saveInBackground();
			
			
			Bundle b=new Bundle();
			b.putInt("condition",3);
			intent.putExtras(b);
			startActivity(intent);
			
			Context context = getApplicationContext();
			CharSequence text = "Post added successfully!";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}
	
	public void cancelEventPost(View view)
	{
		Intent intent = new Intent(EventTab.this,Tab1.class);
		Bundle b=new Bundle();
		b.putInt("condition",3);
		intent.putExtras(b);
		startActivity(intent);
	}
}
