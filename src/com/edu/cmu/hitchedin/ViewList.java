package com.edu.cmu.hitchedin;

import java.util.ArrayList;
import java.util.Arrays;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class ViewList extends Activity {
	private ArrayList<String> names = new ArrayList<String>();
	private ArrayList<String> profile_titles = new ArrayList<String>();
	private ArrayList<String> pic_urls = new ArrayList<String>();
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
		names.add("Ramya Balaraman");
		names.add("Nandhita Kumar");
		names.add("Anvitha Jaishankar");
		names.add("Devika Nair");
		
		profile_titles.add("Graduate Teaching Assistant at Carnegie Mellon University");
		profile_titles.add("CMU + Bank of America - MHCI Capstone Project Research Lead");
		profile_titles.add("Student at Carnegie Mellon University");
		profile_titles.add("Teaching Assistant at Carnegie Mellon University");
		
		pic_urls.add("https://www.facebook.com/photo.php?fbid=433201930081511&set=a.114187241982983.15210.100001751247726&type=1&theater");
		pic_urls.add("https://www.facebook.com/photo.php?fbid=517547918314910&set=a.114869198582786.15659.100001791209212&type=1&theater");
		pic_urls.add("http://m.c.lnkd.licdn.com/mpr/mprx/0_LPiHRo1tG8xztJySFKTBRerjC3EsrJySFtcBReC7gbYEfgwDWv1XvH9Tu2o5POgTXK39qfrXBHmo");
		pic_urls.add("https://www.facebook.com/photo.php?fbid=10151604245847680&set=p.10151604245847680&type=1&theater");		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_list, menu);
		return true;
	}

}
