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
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.edu.cmu.hitchedin.CustomAdapter.FetchBitmaps;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		
		try {
			new FetchData().execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		list = (ListView) findViewById(R.id.mainList);
		
		adapter = new CustomAdapter(this.getApplicationContext(),names,profile_titles,pic_urls);
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(viewList, ViewDetails.class);
				intent.putExtra("ProfileURL", profileid.get(arg2));
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
		getMenuInflater().inflate(R.menu.view_list, menu);
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
					if("J".equals(profile.get("comment"))){
						Jnames.add((String) profile.get("name"));
						Jprofile_titles.add((String) profile.get("profiletitle"));
						Jskills.add((String) profile.get("skills"));
						Jprofileid.add((String) profile.get("linkedinprofile"));
						Jpic_urls.add((String) profile.get("picurl"));
					}
					else
					{
						Rnames.add((String) profile.get("name"));
						Rprofile_titles.add((String) profile.get("profiletitle"));
						Rskills.add((String) profile.get("skills"));
						Rprofileid.add((String) profile.get("linkedinprofile"));
						Rpic_urls.add((String) profile.get("picurl"));
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

}
