package wireless.uta.com.airport_assist;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *  Wrapper class to send Http requests to the server
 */
public class HttpWrapper extends AsyncTask<HttpPost, Void, InputStream> {

    private BufferedReader reader;
    private StringBuilder sb;
    private String line, result1;
    private InputStream inputStream = null;
    private ArrayList<NameValuePair> postParameters;
    private FlightActivity flightActivity;
    private String TAG = "http_wrapper";

    @Override
    protected InputStream doInBackground(HttpPost... httpPosts) {
        InputStream is = null;
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost1 = httpPosts[0];
            httpPost1.setEntity(new UrlEncodedFormEntity(postParameters));
            Log.i(TAG, httpPost1.getURI().toString());
            HttpResponse response = httpclient.execute(httpPost1);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        }
        catch(Exception e){
            Log.e(TAG, "Error in http connection " + e.toString());
        }
        this.inputStream = is;
        return is;
    }

    @Override
    protected void onPostExecute(InputStream is) {
        String result1 = responseToString(is);
        this.flightActivity.restaurantsData(result1);
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
            Log.e("log_tag", "Error converting result " + e.toString());
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
