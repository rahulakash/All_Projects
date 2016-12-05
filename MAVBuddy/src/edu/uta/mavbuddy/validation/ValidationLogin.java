package edu.uta.mavbuddy.validation;

import android.widget.EditText;

public class ValidationLogin {

	public Boolean isValidUsername(EditText editText) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			editText.setError("Username must not be empty.");
			return false;
		}
		return true;
	}
	
	public Boolean isValidPassword(EditText editText) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			editText.setError("Password must not be empty.");
			return false;
		} 
		return true;
	}
}
