package edu.uta.mavbuddy.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.widget.EditText;
import edu.uta.mavbuddy.common.Constant;

public class ValidationEvent {

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
	
	public Boolean isValidLocation(EditText editText) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			editText.setError("Location must not be empty.");
			return false;
		} else if (editText.getText().toString().length() > Constant.LOCATION_LENGHT) {
			editText.setError("Location must not be more than "+Constant.LOCATION_LENGHT+" characters.");
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
	
	public Boolean isValidCost(EditText editText) throws NumberFormatException
	{
		if (editText.getText().toString().length() > 0 && editText.getText().toString().length() > Constant.COST_LENGHT) {
			editText.setError("Cost must not be more than "+Constant.COST_LENGHT+" digits.");
			return false;
		}
		return true;
	}
	
	public Boolean isValidDescription(EditText editText) throws NumberFormatException
	{
		if (editText.getText().toString().length() > Constant.DESCRIPTION_LENGHT) {
			editText.setError("Description must not be more than "+Constant.DESCRIPTION_LENGHT+" characters.");
			return false;
		}
		return true;
	}
}
