package edu.uta.mavbuddy.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.widget.EditText;
import edu.uta.mavbuddy.common.Constant;

public class ValidationRides {

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
	
	public Boolean isValidDestination(EditText editText) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			editText.setError("Destination must not be empty.");
			return false;
		} else if (editText.getText().toString().length() > Constant.DESTINATION_LENGHT) {
			editText.setError("Destination must not be more than "+Constant.DESTINATION_LENGHT+" characters.");
			return false;
		}
		return true;
	}
	
	@SuppressLint("SimpleDateFormat")
	public Boolean isValidStartTime(EditText editText, EditText editText1) throws NumberFormatException
	{
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_TIME_FORMAT);
		if (editText.getText().toString().length() <= 0) {
			editText.setError("Start Time must not be empty.");
			return false;
		} else {
		    try {
		        sdf.parse(editText.getText().toString());
		    }
		    catch(ParseException ex) {
		    	editText.setError("Start Time must be in specified format - "+Constant.DATE_TIME_FORMAT+".");
		        return false;
		    }
		    
		    try {
		    	Date today = sdf.parse(sdf.format(new Date()));
		    	Date startTime = sdf.parse(editText.getText().toString());
		    	
		    	if(startTime.before(today))
			    {
		    		editText.setError("Start Time must be greater than or equal to current time.");
					return false;
			    }
			} catch (ParseException e) {
			}
		}
		
		if (editText1.getText().toString().length() > 0)
		{
			try {
				Date startTime = sdf.parse(editText.getText().toString());
				Date endTime = sdf.parse(editText1.getText().toString());
				
				if(startTime.after(endTime))
				{
					editText.setError("Start Time must not be greater than End Time.");
					return false;
				}
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	@SuppressLint("SimpleDateFormat")
	public Boolean isValidEndTime(EditText editText, EditText editText1) throws NumberFormatException
	{
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_TIME_FORMAT);
		if (editText.getText().toString().length() <= 0) {
			editText.setError("End Time must not be empty.");
			return false;
		} else {
		    try {
		        sdf.parse(editText.getText().toString());
		    }
		    catch(ParseException ex) {
		    	editText.setError("End Time must be in specified format - "+Constant.DATE_TIME_FORMAT+".");
		        return false;
		    }
		    
		    try {
		    	Date today = sdf.parse(sdf.format(new Date()));
		    	Date endTime = sdf.parse(editText.getText().toString());
		    	
		    	if(endTime.before(today))
			    {
		    		editText.setError("End Time must be greater than or equal to current time.");
					return false;
			    }
			} catch (ParseException e) {
			}
		}
		
		if (editText1.getText().toString().length() > 0)
		{
			try {
				Date startTime = sdf.parse(editText1.getText().toString());
				Date endTime = sdf.parse(editText.getText().toString());
				
				if(startTime.after(endTime))
				{
					editText.setError("End Time must be greater than or equal to Start Time.");
					return false;
				}
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
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
	
	public Boolean isValidComments(EditText editText) throws NumberFormatException
	{
		if (editText.getText().toString().length() > Constant.COMMENTS_LENGHT) {
			editText.setError("Comments must not be more than "+Constant.COMMENTS_LENGHT+" characters.");
			return false;
		}
		return true;
	}
}
