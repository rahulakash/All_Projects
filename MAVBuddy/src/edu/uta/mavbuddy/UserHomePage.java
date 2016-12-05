package edu.uta.mavbuddy;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;

public class UserHomePage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_home_page);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_home_page, menu);
		return true;
	}

	public void searchHousePage(View view)
	{
		Intent intent = new Intent(UserHomePage.this,Tab1.class);
		Bundle b=new Bundle();
		b.putInt("condition",1);
		intent.putExtras(b);
		startActivity(intent);
	}
	
	public void searchRidesPage(View view)
	{
		Intent intent = new Intent(UserHomePage.this,Tab1.class);
		Bundle b=new Bundle();
		b.putInt("condition",2);
		intent.putExtras(b);
		startActivity(intent);
	}
	
	public void searchEventPage(View view)
	{
		Intent intent = new Intent(UserHomePage.this,Tab1.class);
		Bundle b=new Bundle();
		b.putInt("condition",3);
		intent.putExtras(b);
		startActivity(intent);
	}
	
	public void myPost(View view)
	{
		Intent intent = new Intent(UserHomePage.this,MyPostActivity.class);
		startActivity(intent);
	}
	
	public void signOut(View view)
	{
		Intent intent = new Intent(UserHomePage.this,HomeScreen.class);
		startActivity(intent);
	}
}
