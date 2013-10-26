package com.edu.cmu.hitchedin;

import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;

public class Home extends Activity {
	private Activity home = this;
	private static int SPLASH_TIME_OUT = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(home.getApplicationContext());;
				Boolean hasLoggedIn = prefs.getBoolean("Login", false);

				if (!hasLoggedIn)
				{
					Intent i = new Intent(home, Login.class);
					startActivity(i);
					finish();
				}
				else 
				{
					Intent i = new Intent(home, ViewList.class);
					startActivity(i);
					finish();
				}
			}
		}, SPLASH_TIME_OUT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}
