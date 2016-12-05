package edu.uta.mavbuddy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import edu.uta.mavbuddy.validation.ValidationLogin;


public class HomeScreen extends Activity {

	String Username;
	String Password;
	String pwdsalt = "*%S6ge#9nt";
	int Pwdhash;
	TextView myText;
	ValidationLogin validationLogin = new ValidationLogin();
	EditText username;
	EditText password;
	SharedPreferences sharedPreferences;
	Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Parse.initialize(this, "NMTwPKAweEH0MrGH5UAIdE8nKC9mc3GkMlvCGALE", "I35KGgtQ2giCz0Ig3QqraJOiorDuxVhdKlUL4Jd5");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
		
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = sharedPreferences.edit();
		
		loginValidation();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_screen, menu);
		
		return true;
	}

	public void login(final View view)
	{		
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		
		final Intent intent = new Intent(HomeScreen.this,UserHomePage.class);
		
		if(validationLogin.isValidUsername(username) && validationLogin.isValidPassword(password))
		{
			Username = username.getText().toString();
			Password = password.getText().toString();
			editor.putString("userdetails", Username);
			editor.commit();
			Password = Password + pwdsalt;
			Pwdhash = Password.hashCode();
			
			ParseQuery<ParseObject> query = ParseQuery.getQuery("userDetails");
			query.whereEqualTo("username", Username);
			query.getFirstInBackground(new GetCallback<ParseObject>() {
				public void done(ParseObject object, ParseException e) {
					if (object == null) {
						// Invalid username error
						TextView error = (TextView)findViewById(R.id.textView1); 
						error.setTextColor(Color.parseColor("#BC1212"));
						error.setText("Invalid Username/Password!");    	
					} 
					
					else {
						// Valid username found
						int Orighash = object.getInt("password");
						if(Orighash == Pwdhash)
						{
							// Password matched for given username
							startActivity(intent);
						}
						else
						{
							// Invalid Password for given username
							TextView error = (TextView)findViewById(R.id.textView1); 
							error.setTextColor(Color.parseColor("#BC1212"));
							error.setText("Invalid Username/Password!");
						}
					}
				}
			});
		}
		
	}
	
	public void openRegisterPage(View view)
	{
		Intent intent = new Intent(HomeScreen.this,Register.class);
		startActivity(intent);
	}
	
	public void loginValidation(){
		
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		
		username.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				validationLogin.isValidUsername(username);
			}
		});
		
		password.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				validationLogin.isValidPassword(password);
			}
		});
	}
}