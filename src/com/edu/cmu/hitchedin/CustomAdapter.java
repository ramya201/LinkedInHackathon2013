package com.edu.cmu.hitchedin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<String> names;
	private ArrayList<String> profile_titles;
	private ArrayList<String> pic_urls;
    private LayoutInflater inflater;
	
	public CustomAdapter(Context context, ArrayList<String> names, ArrayList<String> profile_titles, ArrayList<String> pic_urls) {
		this.context = context;
		this.names = names;
		this.profile_titles = profile_titles;
		this.pic_urls = pic_urls;
	}

	@Override
	public int getCount() {
		return names.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        TextView name;
        TextView profile_title;
        ImageView icon;
 
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
        View itemView = inflater.inflate(R.layout.listitem, parent, false);
 
        name = (TextView) itemView.findViewById(R.id.name);
        profile_title = (TextView) itemView.findViewById(R.id.profileTitle);
        icon = (ImageView) itemView.findViewById(R.id.icon);
 
        name.setText(names.get(position));
        profile_title.setText(profile_titles.get(position));
        
        try {
			URL pic_url = new URL(pic_urls.get(position));
			Bitmap profilePic = BitmapFactory.decodeStream(pic_url.openConnection().getInputStream());
			icon.setImageBitmap(profilePic);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return itemView;
	}

}
