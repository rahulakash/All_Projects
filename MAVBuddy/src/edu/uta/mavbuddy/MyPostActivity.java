package edu.uta.mavbuddy;

import java.util.Locale;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.ListView;

public class MyPostActivity extends FragmentActivity implements
		ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	String username;
	SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_post);
		
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
		getMenuInflater().inflate(R.menu.my_post, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		ListView list = (ListView) findViewById(R.id.myPostlistView);
		
		ParseQueryAdapter.QueryFactory<ParseObject> factory1 =
			     new ParseQueryAdapter.QueryFactory<ParseObject>() {
			       public ParseQuery create() {
			         ParseQuery query = new ParseQuery("housing");
			         query.whereEqualTo("username", username);
			         query.orderByDescending("createdAt");
			         return query;
			       }
		};
		ParseQueryAdapter.QueryFactory<ParseObject> factory2 =
				 new ParseQueryAdapter.QueryFactory<ParseObject>() {
					public ParseQuery create() {
					  ParseQuery query = new ParseQuery("rides");
					  query.whereEqualTo("username", username);
					  query.orderByDescending("createdAt");
					  return query;
					}
		};
		ParseQueryAdapter.QueryFactory<ParseObject> factory3 =
			     new ParseQueryAdapter.QueryFactory<ParseObject>() {
			       public ParseQuery create() {
			         ParseQuery query = new ParseQuery("events");
			         query.whereEqualTo("username", username);
			         query.orderByDescending("createdAt");
			         return query;
			       }
			     };
			     
			     
		
		
		if(tab.getPosition() == 0)
		{	
			ParseQueryAdapter<ParseObject> adapter = new ParseQueryAdapter<ParseObject>(this, factory1);
			adapter.setTextKey("title");
		
			list.setAdapter(adapter);
		}
		else if(tab.getPosition() == 1)
		{
			ParseQueryAdapter<ParseObject> adapter = new ParseQueryAdapter<ParseObject>(this, factory2);
			adapter.setTextKey("title");
		
			list.setAdapter(adapter);
		}
		else
		{
			ParseQueryAdapter<ParseObject> adapter = new ParseQueryAdapter<ParseObject>(this, factory3);
			adapter.setTextKey("title");
		
			list.setAdapter(adapter);
		}
			    
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		
		ListView list = (ListView) findViewById(R.id.myPostlistView);
		
		ParseQueryAdapter.QueryFactory<ParseObject> factory1 =
			     new ParseQueryAdapter.QueryFactory<ParseObject>() {
			       public ParseQuery create() {
			         ParseQuery query = new ParseQuery("housing");
			         query.whereEqualTo("username", username);
			         query.orderByDescending("createdAt");
			         return query;
			       }
		};
		ParseQueryAdapter.QueryFactory<ParseObject> factory2 =
				 new ParseQueryAdapter.QueryFactory<ParseObject>() {
					public ParseQuery create() {
					  ParseQuery query = new ParseQuery("rides");
					  query.whereEqualTo("username", username);
					  query.orderByDescending("createdAt");
					  return query;
					}
		};
		ParseQueryAdapter.QueryFactory<ParseObject> factory3 =
			     new ParseQueryAdapter.QueryFactory<ParseObject>() {
			       public ParseQuery create() {
			         ParseQuery query = new ParseQuery("events");
			         query.whereEqualTo("username", username);
			         query.orderByDescending("createdAt");
			         return query;
			       }
			     };
			     
			     
		
		
		if(tab.getPosition() == 0)
		{	
			ParseQueryAdapter<ParseObject> adapter = new ParseQueryAdapter<ParseObject>(this, factory1);
			adapter.setTextKey("title");
		
			list.setAdapter(adapter);
		}
		else if(tab.getPosition() == 1)
		{
			ParseQueryAdapter<ParseObject> adapter = new ParseQueryAdapter<ParseObject>(this, factory2);
			adapter.setTextKey("title");
		
			list.setAdapter(adapter);
		}
		else
		{
			ParseQueryAdapter<ParseObject> adapter = new ParseQueryAdapter<ParseObject>(this, factory3);
			adapter.setTextKey("title");
		
			list.setAdapter(adapter);
		}
	    
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
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
				fragment = new MyHousePostFragment();
			else if(position == 1)
				fragment = new MyRidesPostFragment();
			else
				fragment = new MyEventPostFragment();
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}
}
