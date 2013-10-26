package edu.cmu.linkedin.samples;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;



public class CreateMappings {
public static void main(String[] args) throws Exception{
	HttpClient client = new DefaultHttpClient();

	/*
	 * {"bluetooth": "sample2",
		"linkedinprofile": "Anvi"} 
*/
		//try {
			URI uri = new URI("http://localhost:3000/mappings.json");
			HttpPost request = new HttpPost(uri.toASCIIString());
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair ("[mapping][bluetooth]", "sampleb"));
	        nameValuePairs.add(new BasicNameValuePair ("[mapping][linkedinprofile]", "anvi"));
	        //nameValuePairs.add(new BasicNameValuePair("linkedinprofle", "anvi"));
	        request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = client.execute(request);
	   
			
			
			//String param = "\"bluetooth\"=>\"sample2\", \"linkedinprofile\"=>\"ramya\", \"mapping\"=>{\"bluetooth\"=>\"sample2\", \"linkedinprofile\"=>\"ramya\"}";
			
			
	//{"bluetooth"=>"sample2", "linkedinprofile"=>"nandita2", "mapping"=>{"bluetooth"=>"sample2", "linkedinprofile"=>"nandita2"}}
			//System.out.println(param);
			
			/*JSONObject jsonObj = new JSONObject();

		    jsonObj.put("bluetooth", "sampleb");
		    jsonObj.put("linkedinprofle", "anvi");
		    StringEntity entity = new StringEntity(jsonObj.toString());
		    
			StringEntity params = 
					new StringEntity(jsonObj.toString(), HTTP.UTF_8);*/
			
			//StringEntity params = new StringEntity("\"bluetooth\"=>\"sample2\", \"linkedinprofile\"=>\"ramya\", \"mapping\"=>{\"bluetooth\"=>\"sample2\", \"linkedinprofile\"=>\"ramya\"}", HTTP.UTF_8);
			//params.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			/*JSONObject jsonObj = new JSONObject();

		    jsonObj.put("bluetooth", "sampleb");
		    jsonObj.put("linkedinprofle", "anvi");
		    StringEntity entity = new StringEntity(jsonObj.toString());*/
/*
			request.addHeader("content-type", "application/x-www-form-urlencoded");
			request.setEntity(params);
			HttpResponse response = client.execute(request);
			System.out.println(response);*/
		/*} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (URISyntaxException e1) {
			e1.printStackTrace();
		}	*/						
	}
}
