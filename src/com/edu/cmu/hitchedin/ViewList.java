package com.edu.cmu.hitchedin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class ViewList extends Activity {
	private ArrayList<String> names = new ArrayList<String>();
	private ArrayList<String> profile_titles = new ArrayList<String>();
	private ArrayList<String> pic_urls = new ArrayList<String>();
	private ArrayList<String> type = new ArrayList<String>();
	private ArrayList<String> skills = new ArrayList<String>();
	private ArrayList<String> profileid = new ArrayList<String>();
	
	private ListView list;
	private CustomAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		
		fetchData(); 
		
		list = (ListView) findViewById(R.id.mainList);
		
		adapter = new CustomAdapter(this.getApplicationContext(),names,profile_titles,pic_urls);
		list.setAdapter(adapter);
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
		HttpClient client = new DefaultHttpClient();
		URI uri;
		try {
			uri = new URI("http://hitchedin.herokuapp.com/mappings.json");
			HttpPost request = new HttpPost(uri.toASCIIString());
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			//TODO: This is where you pass list of bluetooth ids
	        nameValuePairs.add(new BasicNameValuePair ("bluetooth", "DevikasIPhone,RamyasAndroid,Anvi"));
	        request.setEntity(new UrlEncodedFormEntity(nameValuePairs));   
	        // Execute HTTP Post Request
	        HttpResponse response = client.execute(request);
	        HttpEntity entity = response.getEntity();
	        String responseString = new BasicResponseHandler().handleResponse(response);
			JSONArray allProfiles = new JSONArray(responseString);
			for(int i = 0 ; i < allProfiles.length(); i++){
				JSONObject profile = (JSONObject) allProfiles.get(i);
				names.add((String) profile.get("name"));
				profile_titles.add((String) profile.get("profiletitle"));
				skills.add((String) profile.get("skills"));
				profileid.add((String) profile.get("linkedinprofile"));
				pic_urls.add((String) profile.get("picurl"));
				type.add((String) profile.get("comment"));
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_list, menu);
		return true;
	}

}
