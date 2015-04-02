package com.powermoves.aleckson.stuckrn;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ListActivity {

    private ProgressDialog pDialog;

    // URL to get contacts JSON
    private static String url = "https://www.kimonolabs.com/api/e7c6345o?apikey=QxPN6k3UB6aVCCjnmOzY5YdiTzugcpWw";

    // JSON Node names

    private static final String TAG_COLLECTION = "collection1";
    private static final String TAG_RESULTS = "results";
    private static final String TAG_GAME = "game";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_GAME_TEXT = "text";
    private static final String TAG_DAY = "day";
    private static final String TAG_DATE = "date";
    private static final String TAG_SHOW_TIME = "show_time";
    private static final String TAG_NETWORK = "network";

    // contacts JSONArray
    JSONObject results = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactList = new ArrayList<HashMap<String, String>>();

        ListView lv = getListView();

        // Listview on item click listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String name = ((TextView) view.findViewById(R.id.name))
                        .getText().toString();
                String cost = ((TextView) view.findViewById(R.id.email))
                        .getText().toString();
                String network = ((TextView) view.findViewById(R.id.mobile))
                        .getText().toString();

                /*

                // Starting single contact activity

                // Intent in = new Intent(getApplicationContext(),
                        SingleContactActivity.class);
                in.putExtra(TAG_GAME_TEXT, name);
                in.putExtra(TAG_DATE, cost);
                in.putExtra(TAG_NETWORK, network);
                startActivity(in);
                */




            }
        });

        // Calling async task to get json
        new GetContacts().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject jb = jsonObj.getJSONObject("results");
                    JSONArray jsonArray = jb.getJSONArray("collection1");

                    // Getting JSON Array node
                    results = jsonObj.getJSONObject("results");

                    // looping through All Contacts
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);

                        String id = c.getString(TAG_DESCRIPTION);
                        String name = c.getString(TAG_DAY);
                        String email = c.getString(TAG_DATE);
                        String address = c.getString(TAG_SHOW_TIME);
                        String gender = c.getString(TAG_NETWORK);

                        // Game node is JSON Object
                        JSONObject phone = c.getJSONObject(TAG_GAME);
                        String mobile = phone.getString(TAG_GAME_TEXT);


                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        contact.put(TAG_GAME_TEXT, id);
                        contact.put(TAG_DATE, name);
                        contact.put(TAG_SHOW_TIME, email);
                        contact.put(TAG_NETWORK, mobile);

                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, contactList,
                    R.layout.list_item, new String[] { TAG_GAME_TEXT, TAG_DATE,
                    TAG_NETWORK }, new int[] { R.id.name,
                    R.id.email, R.id.mobile });

            setListAdapter(adapter);
        }

    }

}