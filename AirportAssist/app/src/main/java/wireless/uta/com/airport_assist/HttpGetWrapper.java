package wireless.uta.com.airport_assist;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Rahul on 5/2/2015.
 */
public class HttpGetWrapper extends AsyncTask<HttpGet, Void, JSONObject> {

    private BufferedReader reader;
    private StringBuilder sb;
    private String line, result1;
    private InputStream inputStream = null;
    private JSONObject jsonObject = null;
    private ArrayList<NameValuePair> postParameters;
    private FlightActivity flightActivity;
    private String TAG = "httpGet_wrapper";

    @Override
    protected JSONObject doInBackground(HttpGet... httpGets) {
        InputStream is = null;
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = httpGets[0];

            Log.i(TAG, httpGet.getURI().toString());
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        }
        catch(Exception e){
            Log.e(TAG, "Error in http connection " + e.toString());
        }
        this.inputStream = is;
        String result = responseToString(this.inputStream);
        try{
            this.jsonObject = new JSONObject(result);
        }catch (Exception e){
            Log.i(TAG,"Error converting string to JSON inside doInBackground");
        }

        return this.jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject is) {
        //String result1 = responseToString(is);
        this.flightActivity.flightData(is);
    }

    public String responseToString(InputStream ins)
    {
        //convert response to string
        try{
            reader = new BufferedReader(new InputStreamReader(ins,"iso-8859-1"),8);
            sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            ins.close();

            result1 = sb.toString();
            return result1;

        }

        catch(Exception e)
        {
            Log.e(TAG, "Error converting result " + e.toString());
            return "Error converting result "+e.toString();
        }
    }

    public FlightActivity getFlightActivity() {
        return flightActivity;
    }

    public void setFlightActivity(FlightActivity flightActivity) {
        this.flightActivity = flightActivity;
    }

    public ArrayList<NameValuePair> getPostParameters() {
        return postParameters;
    }

    public void setPostParameters(ArrayList<NameValuePair> postParameters) {
        this.postParameters = postParameters;
    }
}
}