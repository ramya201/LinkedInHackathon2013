package com.edu.cmu.hitchedin;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;

public class ViewList extends Activity {
	private ArrayList<String> Rnames = new ArrayList<String>();
	private ArrayList<String> Rprofile_titles = new ArrayList<String>();
	private ArrayList<String> Rpic_urls = new ArrayList<String>();
	private ArrayList<String> Rskills = new ArrayList<String>();
	private ArrayList<String> Rprofileid = new ArrayList<String>();
	private ArrayList<String> Jnames = new ArrayList<String>();
	private ArrayList<String> Jprofile_titles = new ArrayList<String>();
	private ArrayList<String> Jpic_urls = new ArrayList<String>();
	private ArrayList<String> Jskills = new ArrayList<String>();
	private ArrayList<String> Jprofileid = new ArrayList<String>();

	private ListView list;
	private CustomAdapter adapter;
	private Activity viewList = this;
	private String bluetooth_name = null;
	private static ArrayList<String> mArrayAdapter = new ArrayList<String>();
	private String role;
	private SearchView searchView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(viewList.getApplicationContext());
		role = prefs.getString("Role",null);

		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// Device does not support Bluetooth
		}
		bluetooth_name = mBluetoothAdapter.getName();

		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			mBluetoothAdapter.startDiscovery();
			int REQUEST_ENABLE_BT = 1;
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
			// Loop through paired devices
			for (BluetoothDevice device : pairedDevices) {
				// Add the name and address to an array adapter to show in a ListView
				mArrayAdapter.add(device.getName());
			}
		}
		// Create a BroadcastReceiver for ACTION_FOUND
		final BroadcastReceiver mReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				// When discovery finds a device
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					// Get the BluetoothDevice object from the Intent
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					// Add the name and address to an array adapter to show in a ListView
					mArrayAdapter.add(device.getName());
				}
			}
		};
		HttpResponse response = null;
		try {
			new SendBluetoothDevices().execute(mArrayAdapter).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
		Intent discoverableIntent = new
				Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
		startActivity(discoverableIntent);

		/*try {
			new FetchData().execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}*/

		list = (ListView) findViewById(R.id.mainList);

		if (role.equalsIgnoreCase("J"))
		{
			adapter = new CustomAdapter(this.getApplicationContext(),Rnames,Rprofile_titles,Rpic_urls);
		} else if (role.equalsIgnoreCase("R"))
		{
			adapter = new CustomAdapter(this.getApplicationContext(),Jnames,Jprofile_titles,Jpic_urls);
		}
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(viewList, ViewDetails.class);
				if (role.equalsIgnoreCase("J"))
				{
					intent.putExtra("ProfileURL", Rprofileid.get(arg2));
				} else if (role.equalsIgnoreCase("R"))
				{
					intent.putExtra("ProfileURL", Jprofileid.get(arg2));
				}
				startActivity(intent);
			} 
		});

		
	}

	public void fetchData()
	{
		/*names.add("Ramya Balaraman");
		names.add("Nandhita Kumar");
		names.add("Anvitha Jaishankar");
		names.add("Devika Nair");

		profile_titles.add("Graduate Teaching Assistant at Carnegie Mellon University");
		profile_titles.add("CMU + Bank of America - MHCI Capstone Project Research Lead");
		profile_titles.add("Student at Carnegie Mellon University");
		profile_titles.add("Teaching Assistant at Carnegie Mellon University");

		pic_urls.add("https://www.facebook.com/photo.php?fbid=433201930081511&set=a.114187241982983.15210.100001751247726&type=1&theater");
		pic_urls.add("https://www.facebook.com/photo.php?fbid=517547918314910&set=a.114869198582786.15659.100001791209212&type=1&theater");

		pic_urls.add("https://www.facebook.com/photo.php?fbid=10201428223880140&set=a.1512838373522.2068226.1011271970&type=1&theater");
		pic_urls.add("https://www.facebook.com/photo.php?fbid=10151604245847680&set=p.10151604245847680&type=1&theater");
		 */

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.options_menu, menu);
		MenuItem searchViewItem = menu.findItem(R.id.menu_search);
		searchView = (SearchView) searchViewItem.getActionView();
		//searchView.setIconifiedByDefault(false);
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener( ) {
			@Override
			public boolean onQueryTextChange( String newText ) {
				return false;

			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				try {
					ArrayList<String> queries = new ArrayList<String>();
					queries.add(query);
					new SendInfo().execute(mArrayAdapter,queries).get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;		        
			}
		});
		return true;
	}

	public class FetchData extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			HttpClient client = new DefaultHttpClient();
			URI uri;
			try {
				uri = new URI("http://hitchedin.herokuapp.com/maptolinkedins.json");
				HttpGet request = new HttpGet(uri.toASCIIString());

				HttpResponse response = client.execute(request);
				HttpEntity entity = response.getEntity();
				String responseString = new BasicResponseHandler().handleResponse(response);
				JSONArray allProfiles = new JSONArray(responseString);
				for(int i = 0 ; i < allProfiles.length(); i++){
					JSONObject profile = (JSONObject) allProfiles.get(i);
					if("R".equals(profile.get("comment"))){
						Rnames.add((String) profile.get("name"));
						Rprofile_titles.add((String) profile.get("profiletitle"));
						Rskills.add((String) profile.get("skills"));
						Rprofileid.add("https://www.linkedin.com/in/"+(String) profile.get("linkedinprofile"));
						Rpic_urls.add((String) profile.get("picurl"));
					}
					else
					{
						Jnames.add((String) profile.get("name"));
						Jprofile_titles.add((String) profile.get("profiletitle"));
						Jskills.add((String) profile.get("skills"));
						Jprofileid.add("https://www.linkedin.com/in/"+(String) profile.get("linkedinprofile"));
						Jpic_urls.add((String) profile.get("picurl"));
					}
				}
			}
			catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}

	private class SendBluetoothDevices extends AsyncTask<ArrayList<String>, Void, Void> {

		@Override
		protected Void doInBackground(ArrayList<String>... params) {
			Log.i("Test bluetooth",params[0].toString());
			HttpClient client = new DefaultHttpClient();
			URI uri = null;
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			try {
				uri = new URI("http://hitchedin.herokuapp.com/mappings.json");
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			HttpPost request = new HttpPost(uri.toASCIIString());
			StringBuffer devices_send = new StringBuffer();
			for (String s: params[0])
			{
				devices_send.append(s);
				devices_send.append(",");
			}
			nameValuePairs.add(new BasicNameValuePair ("bluetooth", devices_send.toString()));
			try {
				request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			HttpResponse response = null;
			// Execute HTTP Post Request
			try {
				response = client.execute(request);
				String responseString = null;
				try {
					responseString = new BasicResponseHandler().handleResponse(response);
					Log.i("Test Response", responseString);
				} catch (HttpResponseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					JSONArray allProfiles = new JSONArray(responseString);
					for(int i = 0 ; i < allProfiles.length(); i++){
						JSONObject profile = (JSONObject) allProfiles.get(i);
						if("R".equals(profile.get("comment"))){
							Rnames.add((String) profile.get("name"));
							Rprofile_titles.add((String) profile.get("profiletitle"));
							Rskills.add((String) profile.get("skills"));
							Rprofileid.add((String) profile.get("linkedinprofile"));
							Rpic_urls.add((String) profile.get("picurl"));
						}
						else
						{
							Jnames.add((String) profile.get("name"));
							Jprofile_titles.add((String) profile.get("profiletitle"));
							Jskills.add((String) profile.get("skills"));
							Jprofileid.add((String) profile.get("linkedinprofile"));
							Jpic_urls.add((String) profile.get("picurl"));
						}
					}
				}
				catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}

	private class SendInfo extends AsyncTask<ArrayList<String>, Void, String> {

		@Override
		protected String doInBackground(ArrayList<String>... params) {
			HttpClient client = new DefaultHttpClient();
			URI uri = null;
			try {
				uri = new URI("http://hitchedin.herokuapp.com/recruiterfilters.json");
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpPost request = new HttpPost(uri.toASCIIString());
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			StringBuffer devices_send = new StringBuffer();
			for (String s: params[0])
			{
				devices_send.append(s);
				devices_send.append(",");
			}
			nameValuePairs.add(new BasicNameValuePair ("bluetooth", devices_send.toString()));
			nameValuePairs.add(new BasicNameValuePair ("filters", params[1].get(0)));
			try {
				request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Execute HTTP Post Request
			HttpResponse response = null;
			try {
				response = client.execute(request);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpEntity entity = response.getEntity();
			String responseString = null;
			try {
				responseString = new BasicResponseHandler().handleResponse(response);
			} catch (HttpResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JSONArray allProfiles = null;
			try {
				allProfiles = new JSONArray(responseString);
				Log.i("Test",responseString);
				for(int i = 0 ; i < allProfiles.length(); i++){
					JSONObject profile = (JSONObject) allProfiles.get(i);
					if("R".equals(profile.get("comment"))){
						Rnames.add((String) profile.get("name"));
						Rprofile_titles.add((String) profile.get("profiletitle"));
						Rskills.add((String) profile.get("skills"));
						Rprofileid.add((String) profile.get("linkedinprofile"));
						Rpic_urls.add((String) profile.get("picurl"));
					}
					else
					{
						Jnames.add((String) profile.get("name"));
						Jprofile_titles.add((String) profile.get("profiletitle"));
						Jskills.add((String) profile.get("skills"));
						Jprofileid.add((String) profile.get("linkedinprofile"));
						Jpic_urls.add((String) profile.get("picurl"));
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*for(int i = 0 ; i < allProfiles.length(); i++){
				JSONObject profile = null;
				try {
					profile = (JSONObject) allProfiles.get(i);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					System.out.println(profile.get("name"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}*/
			return null;

		}
		protected void onPostExecute(String result) {
			viewList.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					list.invalidateViews();
					
					for (int i = 0; i < adapter.getCount(); i++)
					{
						adapter.remove(i);
					}

					if (role.equalsIgnoreCase("J"))
					{
						adapter = new CustomAdapter(viewList.getApplicationContext(),Rnames,Rprofile_titles,Rpic_urls);
					} else if (role.equalsIgnoreCase("R"))
					{
						adapter = new CustomAdapter(viewList.getApplicationContext(),Jnames,Jprofile_titles,Jpic_urls);
					}
					list.setAdapter(adapter);



					list.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {
							Intent intent = new Intent(viewList, ViewDetails.class);
							if (role.equalsIgnoreCase("J"))
							{
								intent.putExtra("ProfileURL", Rprofileid.get(arg2));
							} else if (role.equalsIgnoreCase("R"))
							{
								intent.putExtra("ProfileURL", Jprofileid.get(arg2));
							}
							startActivity(intent);
						} 
					});					
				}					
			});
			super.onPostExecute(result);
		}
	}

}
