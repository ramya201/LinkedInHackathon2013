package com.edu.cmu.hitchedin;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Login extends Activity {

	private static final String APIKEY = "tvek0pqrd9ro";
	private static final String APISECRET = "iCbFs93seTADNPEw";
	private static final String OAUTH_CALLBACK_SCHEME = "x-oauthflow-linkedin";
	private static final String OAUTH_CALLBACK_HOST = "callback";
	private static final String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME
			+ "://" + OAUTH_CALLBACK_HOST;
	private static final String PROTECTED_RESOURCE_URL = "http://api.linkedin.com/v1/people/~:(first-name,last-name,public-profile-url,picture-url,member-url-resources)";
	private static final String URL = "http://hitchedin.herokuapp.com/maptolinkedins.json";
	private static final int TIMEOUT_MILLISEC = 10000;
	private int eventType;
	private static Response response;
	private static OAuthService oas_linkedin;
	private static Token requestToken;
	private static String authURL;
	private ArrayList<String> urlList = new ArrayList<String>();
	private static LinearLayout resumeHolder;
	private static EditText edt_name;
	private static EditText edt_email;
	private static EditText edt_urls;
	private static EditText edt_position;
	private static EditText edt_id;
	private static EditText edt_bluetooth;
	private String bluetooth_name = null;
	private Activity login = this;

	private class OauthStart extends AsyncTask<Void, Void, WebView> {


		@Override
		protected WebView doInBackground(Void... params) {

			oas_linkedin = new ServiceBuilder().provider(LinkedInApi.class)
					.apiKey(APIKEY).apiSecret(APISECRET)
					.callback(OAUTH_CALLBACK_URL).build();

			requestToken = oas_linkedin.getRequestToken();
			authURL = oas_linkedin.getAuthorizationUrl(requestToken);

			final WebView webview = (WebView) findViewById(R.id.webView);
			return webview;
		}

		@Override
		protected void onPostExecute(final WebView result) {
			result.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					if (url.startsWith(OAUTH_CALLBACK_URL)) {
						result.setVisibility(View.GONE);
						RelativeLayout webviewHolder = (RelativeLayout) findViewById(R.id.webviewHolder);
						webviewHolder.removeView(result);
						result.removeAllViews();
						result.destroy();
						new OauthEnd().execute(url);

						return true;
					}
					return super.shouldOverrideUrlLoading(view, url);
				}
			});

			result.loadUrl(authURL);
			super.onPostExecute(result);
		}
	}

	private class OauthEnd extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... params) {
			final Uri uri = Uri.parse(params[0]);
			final String verifier = uri.getQueryParameter("oauth_verifier");
			final Verifier v = new Verifier(verifier);

			final Token accessToken = oas_linkedin.getAccessToken(requestToken,
					v);

			final OAuthRequest request = new OAuthRequest(Verb.GET,
					PROTECTED_RESOURCE_URL);
			oas_linkedin.signRequest(accessToken, request);
			response = request.send();

			new XmlWorker().execute(response);
			return null;
		}
	}

	private class XmlWorker extends AsyncTask<Response, Void, Void> {

		private XmlPullParserFactory xmlfactory;
		private XmlPullParser xpp;
		private String temp = null;
		private String firstname = null;
		private String lastname = null;
		private String position = null;
		private String pic_url = null;
		private String title = null;
		@Override
		protected Void doInBackground(Response... params) {
			// START XML PROCESSING

			try {
				xmlfactory = XmlPullParserFactory.newInstance();
				xmlfactory.setNamespaceAware(true);
				xpp = xmlfactory.newPullParser();
				xpp.setInput(new StringReader(params[0].getBody()));
				eventType = xpp.getEventType();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			while (eventType != XmlPullParser.END_DOCUMENT) {

				if (eventType == XmlPullParser.START_TAG) {
					temp = xpp.getName();
					Log.i("test", xpp.toString());
					if (temp.equals("first-name")) {
						try {
							firstname = xpp.nextText();
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (temp.equals("last-name")) {
						try {
							lastname = xpp.nextText();
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}else if (temp.equals("public-profile-url")) {
						try {
							title = xpp.nextText();
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if (temp.equals("picture-url")) {
						try {
							pic_url = xpp.nextText();
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if (temp.equals("url")) {
						try {
							urlList.add(xpp.nextText());
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				try {
					eventType = xpp.next();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;

			// Manually add PDF resume because LinkedIn only allows 3 custom
			// websites on the profile
			// END XML PROCESSING
		}

		@Override
		protected void onPostExecute(Void result) {

			// TODO: Declare at the top, initialize in onCreate()
			edt_bluetooth.setText(pic_url);
			edt_id.setText(title);
			edt_name.setText(firstname + " " + lastname);
			new SendInfo().execute(bluetooth_name, edt_id.getText().toString(), edt_name.getText().toString(), "Student", "C, C++, Java", edt_bluetooth.getText().toString(), "J");
			//			edt_bluetooth.setText(bluetooth_name);
			resumeHolder.setVisibility(View.INVISIBLE);

			Intent intent = new Intent(login,DialogActivity.class);
			intent.putExtra("ProfileName", edt_id.getText().toString());
			startActivity(intent);
		}
	}


	private class SendInfo extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			HttpClient client = new DefaultHttpClient();
			URI uri = null;
			try {
				uri = new URI("http://hitchedin.herokuapp.com/maptolinkedins.json");
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			HttpPost request = new HttpPost(uri.toASCIIString());
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair ("[maptolinkedin][bluetooth]", params[0]));
			nameValuePairs.add(new BasicNameValuePair ("[maptolinkedin][linkedinprofile]", params[1]));
			nameValuePairs.add(new BasicNameValuePair ("[maptolinkedin][name]", params[2]));
			nameValuePairs.add(new BasicNameValuePair ("[maptolinkedin][profiletitle]", params[3]));
			nameValuePairs.add(new BasicNameValuePair ("[maptolinkedin][skills]", params[4]));
			nameValuePairs.add(new BasicNameValuePair ("[maptolinkedin][picurl]", params[5]));
			nameValuePairs.add(new BasicNameValuePair ("[maptolinkedin][comment]", params[6]));
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
		setContentView(R.layout.login);
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// Device does not support Bluetooth
		}
		bluetooth_name = mBluetoothAdapter.getName();
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			int REQUEST_ENABLE_BT = 1;
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		resumeHolder = (LinearLayout) findViewById(R.id.resumeHolder);
		edt_bluetooth = (EditText) findViewById(R.id.edt_bluetooth);
		edt_id = (EditText) findViewById(R.id.editText_id);
		edt_name = (EditText) findViewById(R.id.editText_name);
		edt_urls = (EditText) findViewById(R.id.editText_urls);
		new OauthStart().execute();

	}

}