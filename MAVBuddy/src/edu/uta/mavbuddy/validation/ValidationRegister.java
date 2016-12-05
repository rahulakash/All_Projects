package edu.uta.mavbuddy.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uta.mavbuddy.common.Constant;

import android.widget.EditText;

public class ValidationRegister {

	private Pattern pattern;
	private Matcher matcher;
 
	public Boolean isValidUsername(EditText editText) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			editText.setError("Username must not be empty.");
			return false;
		} else if (editText.getText().toString().length() > Constant.USERNAME_LENGHT) {
			editText.setError("Username must not be more than "+Constant.USERNAME_LENGHT+" characters.");
			return false;
		} else {
			pattern = Pattern.compile(Constant.USERNAME_PATTERN);
			matcher = pattern.matcher(editText.getText().toString());
			if(! matcher.matches())
			{
				editText.setError("Username must contain any one of lower case letters, digits, special symbol such as _- and length must be between 3 and 15 characters.");
				return false;
			}
		}
		return true;
	}
	
	public Boolean isValidPassword(EditText editText,EditText editText1) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			editText.setError("Password must not be empty.");
			return false;
		} else if (editText.getText().toString().length() > 0 && editText1.getText().toString().length() > 0){
			if(!editText.getText().toString().equals(editText1.getText().toString()))
			{
				editText.setError("Password and Verify Password does not match.");
				return false;
			}
		} else if (editText.getText().toString().length() > Constant.MAX_PASSWORD_LENGHT || editText.getText().toString().length() < Constant.MIN_PASSWORD_LENGHT) {
			editText.setError("Password must be between "+Constant.MIN_PASSWORD_LENGHT+" and "+Constant.MAX_PASSWORD_LENGHT+" characters.");
			return false;
		} else {
			pattern = Pattern.compile(Constant.PASSWORD_PATTERN);
			matcher = pattern.matcher(editText.getText().toString());
			if(! matcher.matches())
			{
				editText.setError("Password must contain two or more lower case followed by two or more upper case followed by two or more digits followed by two or more special characters such as @#$%&.");
				return false;
			}
		}
		return true;
	}
	
	public Boolean isValidVerifyPassword(EditText editText,EditText editText1) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			editText.setError("Verify Password must not be empty.");
			return false;
		} else if (editText.getText().toString().length() > 0 && editText1.getText().toString().length() > 0){
			if(!editText.getText().toString().equals(editText1.getText().toString()))
			{
				editText.setError("Password and Verify Password does not match.");
				return false;
			}
		} else if (editText.getText().toString().length() > Constant.MAX_PASSWORD_LENGHT || editText.getText().toString().length() < Constant.MIN_PASSWORD_LENGHT) {
			editText.setError("Password must be between "+Constant.MIN_PASSWORD_LENGHT+" and "+Constant.MAX_PASSWORD_LENGHT+" characters.");
			return false;
		} else {
			pattern = Pattern.compile(Constant.PASSWORD_PATTERN);
			matcher = pattern.matcher(editText.getText().toString());
			if(! matcher.matches())
			{
				editText.setError("Password must contain two or more lower case followed by two or more upper case followed by two or more digits followed by two or more special characters such as @#$%&.");
				return false;
			}
		}
		return true;
	}
	
	public Boolean isValidEmail(EditText editText) throws NumberFormatException
	{
		pattern = Pattern.compile(Constant.EMAIL_PATTERN);
		
		if (editText.getText().toString().length() <= 0) {
			editText.setError("Email must not be empty.");
			return false;
		} else {
			matcher = pattern.matcher(editText.getText().toString());
			if(!matcher.matches())
			{
				editText.setError("Email is not in proper format.");
				return false;
			}
		}
		
		if (editText.getText().toString().length() > Constant.EMAIL_LENGHT) {
			editText.setError("Email must not be more than "+Constant.EMAIL_LENGHT+" characters.");
			return false;
		}
		return true;
	}
}
