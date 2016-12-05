package wireless.uta.com.airport_assist;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Intent;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;


public class FlightActivity extends Activity {

    private String TAG = this.getClass().getSimpleName();
    private BluetoothAdapter mBluetoothAdapter;
    private static final long SCAN_PERIOD = 100000;
    private boolean mScanning;
    private Handler mHandler;
    private boolean deviceFound = false;
    private ArrayList<BluetoothDevice> mFoundDevices;
    private String deviceUID;
    AlertDialog alertDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);

        //final EditText flightNo = (EditText)findViewById(R.id.flightNo);
        //final EditText depDate = (EditText)findViewById(R.id.depDate);

        final Button start= (Button)findViewById(R.id.start);
        mHandler = new Handler();
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start scanning for ble sensor tag
                scanLeDevice(true);
                start.setEnabled(false);
            }

        });
       // start.setEnabled(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_flight, menu);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Initializes found devices list
        mFoundDevices = new ArrayList<BluetoothDevice>();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
        //scanLeDevice(true);
    }

    /*protected void onStop()
    {
        super.onStop();
        scanLeDevice(true);
    }*/
    @Override
    protected void onPause() {
        super.onPause();
        //scanLeDevice(false);
        mFoundDevices.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String deviceAddress = device.getAddress();
                            if(deviceAddress.equals("78:A5:04:8C:2D:AD")
                                    || deviceAddress.equals("B4:99:4C:64:B9:46")
                                    || deviceAddress.equals("B4:99:4C:64:33:DC")) {
                                if(!mFoundDevices.contains(device)) {
                                   // mFoundDevices.clear();
                                    mFoundDevices.add(device);
                                    notifyDataSetChanged();
                                }
                            }
                        }
                    });
                }
            };

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    public void notifyDataSetChanged() {
        for(BluetoothDevice b: mFoundDevices) {
            ImageView image;
            AlertDialog.Builder alertDialogBuilder;
            //AlertDialog alertDialog = null;
            final Context context=getApplicationContext();
            deviceUID = b.getAddress();
            Log.i(TAG,"Device found: "+deviceUID);

            if(alertDialog!=null) {
                Log.i(TAG,"cancelling alert");
                alertDialog.cancel();
            }
            switch (deviceUID) {
                case "78:A5:04:8C:2D:AD":
                    //Log.i(TAG,"Device found: "+deviceUID);
                    image= new ImageView(this);
                    image.setImageResource(R.drawable.airportmap1);

                    alertDialogBuilder = new AlertDialog.Builder(FlightActivity.this);
                    // set title
                    alertDialogBuilder.setTitle("You are here ");
                    // set dialog message
                    alertDialogBuilder.setView(image);

                    alertDialogBuilder.setCancelable(false)
                            .setPositiveButton("View Restaurants",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, close
                                    dialog.cancel();
                                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

                                    //define parameters
                                    postParameters.add(new BasicNameValuePair("sid",deviceUID));

                                    HttpWrapper httpWrapper = new HttpWrapper();
                                    httpWrapper.setPostParameters(postParameters);
                                    httpWrapper.setFlightActivity(FlightActivity.this);
                                    String url = "http://omega.uta.edu/~sxa1001/getRestaurants.php";
                                    HttpPost httpPost = new HttpPost(url);
                                    try{
                                        httpWrapper.execute(httpPost);
                                    } catch (Exception e) {
                                        Log.e(TAG,"Error in http connection " + e.toString());
                                    }

                                }
                            })
                            .setNegativeButton("View Flights",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                    Calendar c = Calendar.getInstance();
                                    int date = c.get(Calendar.DATE);
                                    int month= c.get(Calendar.MONTH)+1;
                                    int year= c.get(Calendar.YEAR);
                                    int hour = c.get(Calendar.HOUR);
                                    System.out.println("date -"+ date);
                                    System.out.println("month -"+ month);
                                    System.out.println("year -"+ year);
                                    System.out.println("Hour -"+ hour);

                                    HttpGetWrapper httpGetWrapper = new HttpGetWrapper();
                                    httpGetWrapper.setFlightActivity(FlightActivity.this);
                                    //String url = "https://api.flightstats.com/flex/flightstatus/rest/v2/json/flight/status/"+flightCode+"/"+flightNumber+"/dep/"+year+"/"+month+"/"+date+"?appId=dbe625fa&appKey=ea8936ca56fd7b6c9812ed2c88fa9ec4&utc=false";
                                    String url = "https://api.flightstats.com/flex/flightstatus/rest/v2/json/airport/status/DAL/dep/"+year+"/"+month+"/"+date+"/"+hour+"?appId=dbe625fa&appKey=ea8936ca56fd7b6c9812ed2c88fa9ec4&utc=false&numHours=1&maxFlights=5";
                                    //String url= "https://api.flightstats.com/flex/flightstatus/rest/v2/json/airport/status/DAL/dep/2015/5/5/10?appId=dbe625fa&appKey=ea8936ca56fd7b6c9812ed2c88fa9ec4&utc=false&numHours=1&maxFlights=5";
                                    //String url= "https://api.flightstats.com/flex/flightstatus/rest/v2/json/airport/status/DAL/dep/2015/5/7/12?appId=dbe625fa&appKey=ea8936ca56fd7b6c9812ed2c88fa9ec4&utc=false&numHours=1&maxFlights=5";
                                    HttpGet httpGet = new HttpGet(url);
                                    httpGetWrapper.execute(httpGet);
                                }
                            });
                    // create alert dialog
                    alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                    break;

                case "B4:99:4C:64:B9:46":
                    //Log.i(TAG,"Device found: "+deviceUID);
                    image= new ImageView(this);
                    image.setImageResource(R.drawable.airportmap2);

                    alertDialogBuilder = new AlertDialog.Builder(FlightActivity.this);
                    // set title
                    alertDialogBuilder.setTitle("You are here ");
                    // set dialog message
                    alertDialogBuilder.setView(image);
                    // .setMessage("Delete this profile?")

                    alertDialogBuilder.setCancelable(false)
                            .setPositiveButton("View Restaurants",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, close
                                    dialog.cancel();
                                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

                                    //define parameters
                                    postParameters.add(new BasicNameValuePair("sid",deviceUID));

                                    HttpWrapper httpWrapper = new HttpWrapper();
                                    httpWrapper.setPostParameters(postParameters);
                                    httpWrapper.setFlightActivity(FlightActivity.this);
                                    String url = "http://omega.uta.edu/~sxa1001/getRestaurants.php";
                                    HttpPost httpPost = new HttpPost(url);
                                    try{
                                        httpWrapper.execute(httpPost);
                                    } catch (Exception e) {
                                        Log.e(TAG,"Error in http connection " + e.toString());
                                    }

                                }
                            })
                            .setNegativeButton("View Flights",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                    Calendar c = Calendar.getInstance();
                                    int date = c.get(Calendar.DATE);
                                    int month= c.get(Calendar.MONTH)+1;
                                    int year= c.get(Calendar.YEAR);
                                    int hour = c.get(Calendar.HOUR);
                                    System.out.println("date -"+ date);
                                    System.out.println("month -"+ month);
                                    System.out.println("year -"+ year);
                                    System.out.println("Hour -"+ hour);

                                    HttpGetWrapper httpGetWrapper = new HttpGetWrapper();
                                    httpGetWrapper.setFlightActivity(FlightActivity.this);
                                    //String url = "https://api.flightstats.com/flex/flightstatus/rest/v2/json/flight/status/"+flightCode+"/"+flightNumber+"/dep/"+year+"/"+month+"/"+date+"?appId=dbe625fa&appKey=ea8936ca56fd7b6c9812ed2c88fa9ec4&utc=false";
                                    String url = "https://api.flightstats.com/flex/flightstatus/rest/v2/json/airport/status/DAL/dep/"+year+"/"+month+"/"+date+"/"+hour+"?appId=dbe625fa&appKey=ea8936ca56fd7b6c9812ed2c88fa9ec4&utc=false&numHours=1&maxFlights=5";
                                    //String url= "https://api.flightstats.com/flex/flightstatus/rest/v2/json/airport/status/DAL/dep/2015/5/5/10?appId=dbe625fa&appKey=ea8936ca56fd7b6c9812ed2c88fa9ec4&utc=false&numHours=1&maxFlights=5";
                                    //String url= "https://api.flightstats.com/flex/flightstatus/rest/v2/json/airport/status/DAL/dep/2015/5/7/12?appId=dbe625fa&appKey=ea8936ca56fd7b6c9812ed2c88fa9ec4&utc=false&numHours=1&maxFlights=5";

                                    HttpGet httpGet = new HttpGet(url);
                                    httpGetWrapper.execute(httpGet);

                                }
                            });
                    // create alert dialog
                    alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                    break;

                case "B4:99:4C:64:33:DC":
                    //Log.i(TAG,"Device found: "+deviceUID);
                    image= new ImageView(this);
                    image.setImageResource(R.drawable.airportmap3);

                    alertDialogBuilder = new AlertDialog.Builder(FlightActivity.this);
                    // set title
                    alertDialogBuilder.setTitle("You are here ");
                    // set dialog message
                    alertDialogBuilder.setView(image);
                    // .setMessage("Delete this profile?")

                    alertDialogBuilder.setCancelable(false)
                            .setPositiveButton("View Restaurants",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, close
                                    dialog.cancel();
                                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

                                    //define parameters
                                    postParameters.add(new BasicNameValuePair("sid",deviceUID));

                                    HttpWrapper httpWrapper = new HttpWrapper();
                                    httpWrapper.setPostParameters(postParameters);
                                    httpWrapper.setFlightActivity(FlightActivity.this);
                                    String url = "http://omega.uta.edu/~sxa1001/getRestaurants.php";
                                    HttpPost httpPost = new HttpPost(url);
                                    try{
                                        httpWrapper.execute(httpPost);
                                    } catch (Exception e) {
                                        Log.e(TAG,"Error in http connection " + e.toString());
                                    }

                                }
                            })
                            .setNegativeButton("View Flights",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                    Calendar c = Calendar.getInstance();
                                    int date = c.get(Calendar.DATE);
                                    int month= c.get(Calendar.MONTH)+1;
                                    int year= c.get(Calendar.YEAR);
                                    int hour = c.get(Calendar.HOUR);
                                    System.out.println("date -"+ date);
                                    System.out.println("month -"+ month);
                                    System.out.println("year -"+ year);
                                    System.out.println("Hour -"+ hour);

                                    HttpGetWrapper httpGetWrapper = new HttpGetWrapper();
                                    httpGetWrapper.setFlightActivity(FlightActivity.this);
                                    //String url = "https://api.flightstats.com/flex/flightstatus/rest/v2/json/flight/status/"+flightCode+"/"+flightNumber+"/dep/"+year+"/"+month+"/"+date+"?appId=dbe625fa&appKey=ea8936ca56fd7b6c9812ed2c88fa9ec4&utc=false";
                                    String url = "https://api.flightstats.com/flex/flightstatus/rest/v2/json/airport/status/DAL/dep/"+year+"/"+month+"/"+date+"/"+hour+"?appId=dbe625fa&appKey=ea8936ca56fd7b6c9812ed2c88fa9ec4&utc=false&numHours=1&maxFlights=5";
                                    //String url= "https://api.flightstats.com/flex/flightstatus/rest/v2/json/airport/status/DAL/dep/2015/5/7/12?appId=dbe625fa&appKey=ea8936ca56fd7b6c9812ed2c88fa9ec4&utc=false&numHours=1&maxFlights=5";
                                    HttpGet httpGet = new HttpGet(url);
                                    httpGetWrapper.execute(httpGet);

                                }
                            });
                    // create alert dialog
                    alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                    break;

                default: break;
            }
        }
    }


    public void restaurantsData(String result){
        String[] restaurants = result.split(",");

        Intent intent=new Intent(getApplicationContext(),ViewRestaurantsActivity.class);
        intent.putExtra("restaurants",restaurants);
        intent.putExtra("sid",deviceUID);
        startActivity(intent);
    }

    public void flightData(JSONObject result) {
        ArrayList<Flight> flightsList = new ArrayList<Flight>();

        try {
            JSONObject object = result;
            JSONArray flightArray= object.getJSONArray("flightStatuses");
            Log.i(TAG,flightArray.toString());
            for(int i=0;i<flightArray.length();i++){
                Log.i(TAG,"inside for loop");
                Flight flight;
                JSONObject flightObject = flightArray.getJSONObject(i);
                String flightNo = flightObject.getString("flightNumber");
                String carrierCode= flightObject.getString("carrierFsCode");
                String status = flightObject.getString("status");
                String departureTerminal = null;
                String departureGate = null;

                if(flightObject.has("airportResources")){
                    JSONObject airportResources = flightObject.getJSONObject("airportResources");
                    if(airportResources.has("departureTerminal")) {
                        departureTerminal = airportResources.getString("departureTerminal");
                    }
                    if(airportResources.has("departureGate")) {
                        departureGate = airportResources.getString("departureGate");
                    }
                    flight = new Flight(flightNo,carrierCode,status,departureTerminal,departureGate);
                }else {
                    flight = new Flight(flightNo,carrierCode,status);
                }

                Log.i(TAG,"Creating flight");
                flightsList.add(flight);
            }
            Intent intent= new Intent(getApplicationContext(),ViewFlightsActivity.class);
            intent.putExtra("flightsList",flightsList);
            startActivity(intent);
        }
        catch(Exception e){
            Log.i(TAG, e.getMessage());
        }

    }
}
