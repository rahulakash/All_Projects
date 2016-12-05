package edu.uta.mavbuddy.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.widget.EditText;
import edu.uta.mavbuddy.common.Constant;

public class ValidationHousing {

	private Pattern pattern;
	private Matcher matcher;
	
	public Boolean isValidName(EditText editText) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			editText.setError("Name must not be empty.");
			return false;
		} else if (editText.getText().toString().length() > Constant.NAME_LENGHT) {
			editText.setError("Name must not be more than "+Constant.NAME_LENGHT+" characters.");
			return false;
		}
		return true;
	}
	
	public Boolean isValidTitle(EditText editText) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			editText.setError("Title must not be empty.");
			return false;
		} else if (editText.getText().toString().length() > Constant.TITLE_LENGHT) {
			editText.setError("Title must not be more than "+Constant.TITLE_LENGHT+" characters.");
			return false;
		}
		return true;
	}
	
	public Boolean isValidAptName(EditText editText) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			editText.setError("Appartment Name must not be empty.");
			return false;
		} else if (editText.getText().toString().length() > Constant.APT_NAME_LENGHT) {
			editText.setError("Appartment Name must not be more than "+Constant.APT_NAME_LENGHT+" characters.");
			return false;
		}
		return true;
	}
	
	public Boolean isValidNoOfPerson(EditText editText) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			editText.setError("Number of Person must not be empty.");
			return false;
		} else if (editText.getText().toString().length() > Constant.NO_OF_PERSON_LENGHT) {
			editText.setError("Number of Person must not be more than "+Constant.NO_OF_PERSON_LENGHT+" digits.");
			return false;
		}
		return true;
	}
	
	@SuppressLint("SimpleDateFormat")
	public Boolean isValidMoveInDate(EditText editText, EditText editText1) throws NumberFormatException
	{
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);
		if (editText.getText().toString().length() <= 0) {
			editText.setError("Move in Date must not be empty.");
			return false;
		} else {
		    try {
		        sdf.parse(editText.getText().toString());
		    }
		    catch(ParseException ex) {
		    	editText.setError("Move in Date must be in specified format - "+Constant.DATE_FORMAT+".");
		        return false;
		    }
		    
		    try {
		    	Date today = sdf.parse(sdf.format(new Date()));
		    	Date moveInDate = sdf.parse(editText.getText().toString());
		    	
		    	if(moveInDate.before(today))
			    {
		    		editText.setError("Move in Date must be greater than or equal to current date.");
					return false;
			    }
			} catch (ParseException e) {
			}
		}
		
		if (editText1.getText().toString().length() > 0)
		{
			try {
				Date moveInDate = sdf.parse(editText.getText().toString());
				Date moveOutDate = sdf.parse(editText1.getText().toString());
				
				if(moveInDate.after(moveOutDate))
				{
					editText.setError("Move in Date must not be greater than move out date.");
					return false;
				}
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	@SuppressLint("SimpleDateFormat")
	public Boolean isValidMoveOutDate(EditText editText, EditText editText1) throws NumberFormatException
	{
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);
		if (editText.getText().toString().length() <= 0) {
			editText.setError("Move out Date must not be empty.");
			return false;
		} else {
		    try {
		        sdf.parse(editText.getText().toString());
		    }
		    catch(ParseException ex) {
		    	editText.setError("Move out Date must be in specified format - "+Constant.DATE_FORMAT+".");
		        return false;
		    }
		    
		    try {
		    	Date today = sdf.parse(sdf.format(new Date()));
		    	Date moveOutDate = sdf.parse(editText.getText().toString());
		    	
		    	if(moveOutDate.before(today))
			    {
		    		editText.setError("Move out Date must be greater than or equal to current date.");
					return false;
			    }
			} catch (ParseException e) {
			}
		}
		
		if (editText1.getText().toString().length() > 0)
		{
			try {
				Date moveInDate = sdf.parse(editText1.getText().toString());
				Date moveOutDate = sdf.parse(editText.getText().toString());
				
				if(moveInDate.after(moveOutDate))
				{
					editText.setError("Move out Date must be greater than or equal to move in date.");
					return false;
				}
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public Boolean isValidRent(EditText editText) throws NumberFormatException
	{
		if (editText.getText().toString().length() > Constant.RENT_LENGHT) {
			editText.setError("Rent must not be more than "+Constant.RENT_LENGHT+" digits.");
			return false;
		}
		return true;
	}
	
	public Boolean isValidMobNo(EditText editText) throws NumberFormatException
	{
		if (editText.getText().toString().length() > 0 && editText.getText().toString().length() != Constant.MOB_NO_LENGHT) {
			editText.setError("Mobile Number must be exact "+Constant.MOB_NO_LENGHT+" digits.");
			return false;
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
	
	public Boolean isValidComments(EditText editText) throws NumberFormatException
	{
		if (editText.getText().toString().length() > Constant.COMMENTS_LENGHT) {
			editText.setError("Comments must not be more than "+Constant.COMMENTS_LENGHT+" characters.");
			return false;
		}
		return true;
	}
}
