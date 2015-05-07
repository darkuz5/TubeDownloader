package com.darkuz5.tubedownloader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import yt.sdk.access.YTSDK;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject; 

import com.darkuz5.tubedonwloader.R;   

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView; 
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends ActionBarActivity {
	 private Typeface youTube;
	 private Typeface opensans;
	 private Typeface opensansCds;
	 String urlx="";
	 SearchView searchView;
	 ProgressDialog dialog;
	 private YTSDK ytsdk; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		youTube = Typeface.createFromAsset(getAssets(), "fonts/youtube.otf");
		opensans = Typeface.createFromAsset(getAssets(), "fonts/opensans.ttf");
		opensansCds = Typeface.createFromAsset(getAssets(), "fonts/opensanscondensed.ttf");
		dialog = new ProgressDialog(this);
        int titleId = getResources().getIdentifier("action_bar_title", "id",
                "android");
        TextView yourTextView = (TextView) findViewById(titleId); 
        yourTextView.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_DeviceDefault_Large);
        yourTextView.setTypeface(youTube);
		YTSDKUtils.initilizeYTSDK(this);
		ytsdk = YTSDKUtils.getYTSDK();
		
		 
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		
		//SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		    searchView = (SearchView) menu.findItem(R.id.menu_search)
		            .getActionView();

		    searchView.setQuery("",true);
		    searchView.setFocusable(true);
		    searchView.setIconified(false);
		    searchView.requestFocusFromTouch();
		    
		    if (null != searchView) {
		       // searchView.setSearchableInfo(searchManager
		         //       .getSearchableInfo(getComponentName()));
		        //searchView.setIconifiedByDefault(false);
		    }

		    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
		        public boolean onQueryTextChange(String newText) {
		            // this is your adapter that will be filtered
		            return true;
		        }

		        public boolean onQueryTextSubmit(String query) {
		        	//Toast.makeText(getApplicationContext(), "Buscar "+query, Toast.LENGTH_LONG).show();
		        	if (query.length()>0){
		        	//urlx = "http://gdata.youtube.com/feeds/api/videos?q="+Uri.encode(query)+"&alt=json";
		        	urlx = "https://www.googleapis.com/youtube/v3/search?part=snippet&q="+Uri.encode(query)+"&key=AIzaSyBaMPl9JhX7innu7f2tvmMnfq0AiqJGzi4";
		        	//urlx = "https://gdata.youtube.com/feeds/api/videos?v=2&q="+Uri.encode(query)+"&alt=jsonc&max-results=50&orderby=viewCount&safeSearch=none";
		        	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			        descarga(urlx);
			        searchView.clearFocus();
		        	} else {
		        		
		        	
		        	}
		        	
					return true;
		            //Here u can get the value "query" which is entered in the search box.

		        }
		    };
		    searchView.setOnQueryTextListener(queryTextListener);

		    return super.onCreateOptionsMenu(menu);
		    //return true;
		 
	}


	public void descarga(String url) {
		 ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

			if (networkInfo != null && networkInfo.isConnected()) { 
			     
				  dialog.setMessage(getResources().getString(R.string.donwloadText)); 
	              dialog.setCancelable(false);
				new DownloadVideos().execute(url);
				//Toast.makeText(this, "URL "+url, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Conexión no disponible", Toast.LENGTH_SHORT).show();
			}
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		
		
		int id = item.getItemId();
		//Toast.makeText(getApplicationContext(), "Buscar "+id, Toast.LENGTH_LONG).show();
		
		if (id == R.id.action_settings) {
			return true;
		}
		

		if (id == android.R.id.home) {

        	Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.right_out);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	// Uses an AsyncTask to download a Twitter user's timeline
	private class DownloadVideos extends AsyncTask<String, Void, String> {
		 
		protected void onPreExecute() {
			dialog.setProgress(0);
			dialog.setMax(100);
			dialog.show();  
      }
		

		@Override
		protected String doInBackground(String... screenNames) {
			String result = null;
			String urlp = screenNames[0];
			//Log.i("url",urlp);
			HttpClient httpclient = new DefaultHttpClient(); 
	        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
	        HttpGet request = new HttpGet(urlp);
	        try
	        {
	        HttpResponse response = httpclient.execute(request);
	        HttpEntity resEntity = response.getEntity();
	        result=EntityUtils.toString(resEntity); 
	        } 
	        catch(Exception e1)
	            {
	                e1.printStackTrace();
	            } 

			 return result;
		}

		// onPostExecute convert the JSON results into a Twitter object (which is an Array list of tweets
		@Override
		protected void onPostExecute(String result) { 
			
			try {
				llenaVideo(result); 
			} catch (JSONException e) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (NullPointerException e){
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}

			dialog.dismiss();
			
			try {
				
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			} catch(IndexOutOfBoundsException e){ Log.i("error",e+""); }
		}
	}
 
	
	public void llenaVideo(String contenido) throws JSONException, UnsupportedEncodingException, NullPointerException{
		
		
		try { 
		SQLiteHelper gbd = new SQLiteHelper(getApplicationContext(), "YouTubeDwn.db", null, 1); 		
		SQLiteDatabase db = gbd.getWritableDatabase();	
		Log.i("texto",contenido);
		db.execSQL("delete from video");	
		JSONObject json = new JSONObject(contenido);
        JSONArray jsonArray = json.getJSONObject("data").getJSONArray("items");

		LayoutInflater inflater  = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		LinearLayout linearPadre = (LinearLayout) findViewById(R.id.layoutPadre);  
		linearPadre.removeAllViewsInLayout();
		
		 
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String id = jsonObject.getString("id"); 
            String ds = jsonObject.getString("description");
            ds = ds.replace("'", " ");
            String dr = jsonObject.getString("duration");
            String fecha = jsonObject.getString("uploaded"); 
            fecha = fecha.replace("-", "");
            fecha = fecha.replace("T", "");
            fecha = fecha.replace(":", "");
            fecha = fecha.replace(".000Z", "");            
            String title1 = jsonObject.getString("title"); 
            title1=title1.replace("'", " ");
            String thumbUrl = jsonObject.getJSONObject("thumbnail").getString("sqDefault");  
            String url = jsonObject.getJSONObject("player").getString("default"); 
            

			 View adapter = inflater.inflate(R.layout.adapter_video, null); 
			 ((TextView) adapter.findViewById(R.id.tittle)).setText(title1);
			 ((TextView) adapter.findViewById(R.id.tittle)).setTypeface(opensans);
			 ((TextView) adapter.findViewById(R.id.tittle)).setTextColor(Color.BLACK);
			 ((TextView) adapter.findViewById(R.id.textP)).setTypeface(opensansCds);
			 ((TextView) adapter.findViewById(R.id.textD)).setTypeface(opensansCds);
			 int dura = 0;
			try {
			 dura = Integer.parseInt(dr); 
			} catch (Exception e){
				e.printStackTrace();
			}
			 String durat = getDurationString(dura);
			  
			 
			 ((TextView) adapter.findViewById(R.id.lenghtInfo)).setText(durat);
			 ((TextView) adapter.findViewById(R.id.lenghtInfo)).setTypeface(opensans);
			 ((TextView) adapter.findViewById(R.id.userInfo)).setText(url);

			 ((TextView) adapter.findViewById(R.id.idDwn)).setText(id);
			 ((TextView) adapter.findViewById(R.id.idPlyx)).setText(url);
			 
		  	//new DownloadImageTask((ImageView) adapter.findViewById(R.id.adapterfoto)).execute(videos.getString(4)); 
		  	
		  	
		  	 ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

				 
		  	 
	     
	    	 
	    	 if (networkInfo != null && networkInfo.isConnected()) { 
	    		 
	    		 String[] urls = {thumbUrl,id,"nota" };
	    		 
	    	    new DownloadImageTaskBitmap((ImageView) adapter.findViewById(R.id.imgInfo)).execute(urls);
	    	 }  
	    	  
			 
	    	 LinearLayout toAction = (LinearLayout) adapter.findViewById(R.id.playVideo);
			 toAction.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					View parent = (View) v.getParent();
					Intent i = new Intent(Intent.ACTION_VIEW);
					TextView idnotax= (TextView) parent.findViewById(R.id.idPlyx);
					String idnotanotas = "";
					try {
					idnotanotas = (String) idnotax.getText(); 
					} catch (Exception e){
						Log.i("ERRORR",e.toString());
					}
					//Log.i("Idnota",idnotanotas);
					String url = idnotanotas;
					i.setData(Uri.parse(url));
					startActivity(i);
					
				}
				 
			 });
			 LinearLayout descargaVideo = (LinearLayout) adapter.findViewById(R.id.toHiddenOne);
			 descargaVideo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					View parent = (View) v.getParent(); 
					TextView idnotax= (TextView) parent.findViewById(R.id.idDwn);
					String idnotanotas = "";
					try {
					idnotanotas = (String) idnotax.getText(); 
					} catch (Exception e){
						Log.i("ERRORR",e.toString());
					}
					//Log.i("descarga el id",idnotanotas);
					//ytsdk.setCustomDialogLayoutId(-1, -1);
					ytsdk.setVideoPreview(false);
					ytsdk.download(SearchActivity.this, idnotanotas);
				}
				 
			 });
			
			 linearPadre.addView(adapter);
            
            
         
            
        }
        
		} catch (Exception e){
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
		}
		 
		
	}
	
 
  	class DownloadImageTaskBitmap extends AsyncTask<String, Void, Bitmap> {
  		ImageView bmImage;
  		String idnotaFAT, tablaFAT;
  		Context context;  
  	 

  		public DownloadImageTaskBitmap(ImageView bmImage) {
  			this.bmImage = bmImage;
  		}

  		@Override
  		protected Bitmap doInBackground(String... urls) {
  			String urldisplay = "https://i.ytimg.com/vi/"+urls[1]+"/mqdefault.jpg";
  			idnotaFAT=urls[1];
  			tablaFAT=urls[2];
  			Bitmap mIcon11 = null;
  			try {
  				InputStream in = new java.net.URL(urldisplay).openStream();
  				mIcon11 = BitmapFactory.decodeStream(in);
  			} catch (Exception e) {
  				Log.e("Error", e.getMessage());
  				e.printStackTrace();
  			}
  			return mIcon11;
  		}

  		@Override
  		protected void onPostExecute(Bitmap result) {
  			if (result != null){
  			bmImage.setImageBitmap(result);
  			//guarda(result,idnotaFAT);
  			}
  		}
  	}
  	
  	public void guarda(Bitmap image, String nota){
  		//Log.i("nota",nota);
		    SQLiteHelper gbd = new SQLiteHelper(getApplicationContext(), "YouTubeDwn.db", null, 1); 		
			SQLiteDatabase dbx = gbd.getWritableDatabase();	
		

			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.PNG, 100, bos); 
			
			ByteArrayOutputStream boas = new ByteArrayOutputStream();  
			image.compress(Bitmap.CompressFormat.JPEG, 100, boas ); //bm is the bitmap object   
			byte[] byteArrayImage = boas .toByteArray(); 
			String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
			//Log.i("Imagen", encodedImage);
			//Log.i("Img byte",byteArrayImage.toString());
			dbx.execSQL("update video set imagen='"+encodedImage+"' where id='"+nota+"'");
			
			dbx.close();
  	}
  	
  	public String getDurationString(int seconds) {

  	    int hours = seconds / 3600;
  	    int minutes = (seconds % 3600) / 60;
  	    seconds = seconds % 60;

  	    return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds);
  	}

  	public String twoDigitString(int number) {

  	    if (number == 0) {
  	        return "00";
  	    }

  	    if (number / 10 == 0) {
  	        return "0" + number;
  	    }

  	    return String.valueOf(number);
  	}
	 
}
