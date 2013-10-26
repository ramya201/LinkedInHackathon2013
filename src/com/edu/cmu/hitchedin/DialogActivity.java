package com.edu.cmu.hitchedin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class DialogActivity extends Activity {
	private RadioGroup grp;
	private RadioButton recruiterBtn;
	private RadioButton jobseekerBtn;
	private Activity dialog = this;
	private String profileId;

	private class SendJR extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			HttpClient client = new DefaultHttpClient();
			URI uri = null;
			try {
				uri = new URI("http://hitchedin.herokuapp.com/changetypes.json");
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			HttpPost request = new HttpPost(uri.toASCIIString());
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			
            nameValuePairs.add(new BasicNameValuePair ("profile", params[0]));
            nameValuePairs.add(new BasicNameValuePair ("comment", params[1]));
           try {
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
			
			try {
				request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Execute HTTP Post Request
			HttpResponse response;
			try {
				response = client.execute(request);
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.roledialog);

		RadioGroup group= (RadioGroup) findViewById(R.id.radioGroup1);
		recruiterBtn = (RadioButton) findViewById(R.id.radio0);
		jobseekerBtn = (RadioButton) findViewById(R.id.radio1);
		
		Intent intent = getIntent();
		profileId = intent.getStringExtra("ProfileName");
		
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(dialog.getApplicationContext()).edit();
		editor.putBoolean("Login", true);
		editor.commit();

		group.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{

			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				// TODO Auto-generated method stub
				if(recruiterBtn.isChecked())
				{
					try {
						new SendJR().execute(profileId,"R").get();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(dialog.getApplicationContext()).edit();
					editor.putString("Role", "R");
					editor.commit();
					
					Intent intent = new Intent(dialog, ViewList.class);
					intent.putExtra("Role", "R");
					startActivity(intent);
				}
				else if(jobseekerBtn.isChecked())
				{
					try {
						new SendJR().execute(profileId,"J").get();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(dialog.getApplicationContext()).edit();
					editor.putString("Role", "J");
					editor.commit();
					
					Intent intent = new Intent(dialog, ViewList.class);
					intent.putExtra("Role", "J");
					startActivity(intent);
				}
			}
		});


	}

}
