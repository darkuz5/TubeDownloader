package com.darkuz5.tubedownloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.darkuz5.tubedonwloader.R;
import com.darkuz5.tubedownloader.SearchActivity.DownloadImageTaskBitmap;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	 private Typeface youTube; 
	    ListView videolist;
	    int count;   
	    String[] fileList = null;
	    GridView gridView; 
	    String MiME_TYPE = "video/*";
		 private Typeface opensans;
		 private Typeface opensansCds;
		

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		youTube = Typeface.createFromAsset(getAssets(), "fonts/youtube.otf"); 
		opensans = Typeface.createFromAsset(getAssets(), "fonts/opensans.ttf");
		opensansCds = Typeface.createFromAsset(getAssets(), "fonts/opensanscondensed.ttf");
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        int titleId = getResources().getIdentifier("action_bar_title", "id",
                "android");
        TextView yourTextView = (TextView) findViewById(titleId); 
        yourTextView.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_DeviceDefault_Large);
        yourTextView.setTypeface(youTube);
        
       // init_phone_video_grid();
        
         

        try {
        	dumpVideos(getApplicationContext());
        } catch (Exception e) {
        	
        	e.printStackTrace();
        }
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
        
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_info) {
        	Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }
        if (id == R.id.action_search) {

        	Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }
        return super.onOptionsItemSelected(item);
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
  			idnotaFAT=urls[0]; 
  			Bitmap mIcon11 = null;
  			try {

  	  			  mIcon11 = ThumbnailUtils.createVideoThumbnail(idnotaFAT, MediaStore.Video.Thumbnails.MICRO_KIND);
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
  			guarda(result,idnotaFAT);
  			}
  		}
  	}
  	
  	public void guarda(Bitmap image, String nota){
  		Log.i("nota",nota);
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
			dbx.execSQL("update videolocal set imagen='"+encodedImage+"' where titulo='"+nota+"'");
			
			dbx.close();
  	}
  	
  	
  	public  void dumpVideos(Context context) {
        LayoutInflater inflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		LinearLayout linearPadre = (LinearLayout) findViewById(R.id.layoutPadre);  
		linearPadre.removeAllViewsInLayout();
		SQLiteHelper gbd = new SQLiteHelper(context, "YouTubeDwn.db", null, 1); 		
		SQLiteDatabase db = gbd.getWritableDatabase();	
		String videoL = "CREATE TABLE if not exists videolocal (id VARCHAR(100), fecha TEXT, titulo TEXT, link VARCHAR(500), imagen BLOB)";
		db.execSQL(videoL);
		db.execSQL("update videolocal set link='0'");
  		//Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
		//String[] projection = { MediaStore.Video.VideoColumns.DATA };
		System.gc();
	    String[] proj = { MediaStore.Video.Media._ID,
	                MediaStore.Video.Media.DATA,
	                MediaStore.Video.Media.DISPLAY_NAME,
	                MediaStore.Video.Media.SIZE};
	        
	    String selection=MediaStore.Video.Media.DATA +" like?";
	    String[] selectionArgs=new String[]{"%download%"};
	    Cursor c = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
	        		proj, selection, selectionArgs, MediaStore.Video.Media.DATE_TAKEN + " DESC");
		 
		int vidsCount = 0;
		if (c != null) {
		    c.moveToFirst();
		    vidsCount = c.getCount();
		    do {
		    	 View adapter = inflater.inflate(R.layout.adapter_video, null); 
                 String tittle = c.getString(2).replace("_", " ");
                 tittle  = tittle.replace(".mp4", "");
				 ((TextView) adapter.findViewById(R.id.tittle)).setText(tittle);
				 ((TextView) adapter.findViewById(R.id.tittle)).setTypeface(opensans);
				 ((TextView) adapter.findViewById(R.id.textP)).setTypeface(opensansCds);
				 ((TextView) adapter.findViewById(R.id.tittle)).setTextColor(Color.BLACK);
				  
				  

				 ((TextView) adapter.findViewById(R.id.lenghtInfo)).setVisibility(View.INVISIBLE);
				 ((TextView) adapter.findViewById(R.id.lenghtInfo)).setTypeface(opensans);
				 ((TextView) adapter.findViewById(R.id.userInfo)).setText(c.getString(1));
				 ((TextView) adapter.findViewById(R.id.idPlyx)).setText(c.getString(1));

				// ((ImageView) adapter.findViewById(R.id.imgInfo)).setImageBitmap(bMap);
				 
				 ((LinearLayout) adapter.findViewById(R.id.toHiddenOne)).setVisibility(View.INVISIBLE);
				 Cursor busca  = db.rawQuery("select * from videolocal where titulo='"+c.getString(1)+"'", null);
	     			if (busca.getCount()>0){
	     				busca.moveToFirst();
	     				db.execSQL("update videolocal set link='1' where titulo='"+c.getString(1)+"'");
	     				 if (busca.getString(4)!=null){
	       	    		  byte [] encodeByte=Base64.decode(busca.getBlob(4),Base64.DEFAULT);
	       	    		 Bitmap bm = BitmapFactory.decodeByteArray(encodeByte, 0 ,encodeByte.length);
	       	    		 ((ImageView) adapter.findViewById(R.id.imgInfo)).setImageBitmap(bm);
	       	    	 } else {
	       	    		new DownloadImageTaskBitmap((ImageView) adapter.findViewById(R.id.imgInfo)).execute(c.getString(1));
	       	    	 }
	     				
	     			} else {
	     				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
	     				String currentDate = sdf.format(new Date());
	     				
	     				db.execSQL("insert into videolocal (id, fecha, titulo, link) values ('"+currentDate+"','"+tittle+"','"+c.getString(1)+"', '1') ");
	     				 
	     				new DownloadImageTaskBitmap((ImageView) adapter.findViewById(R.id.imgInfo)).execute(c.getString(1));
	     			}
				 
	     			LinearLayout toAction = (LinearLayout) adapter.findViewById(R.id.playVideo);
					 toAction.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							View parent = (View) v.getParent(); 
							TextView idnotax= (TextView) parent.findViewById(R.id.idPlyx);
							String idnotanotas = "";
							try {
							idnotanotas = (String) idnotax.getText(); 
							} catch (Exception e){
								Log.i("ERRORR",e.toString());
							}
							Log.i("Idnota",idnotanotas);
							String url = idnotanotas; 
							Intent viewIntent = new Intent(Intent.ACTION_VIEW);
				            File file = new File(idnotanotas);
				            viewIntent.setDataAndType(Uri.fromFile(file), "video/*");
				            startActivity(Intent.createChooser(viewIntent, null));
							
						}
						 
					 });
					 linearPadre.addView(adapter);
		    	
		    	
		      /*  Log.d("VIDEO ID", c.getString(0));
		        Log.d("VIDEO DATA", c.getString(1));
		        Log.d("VIDEO NAME", c.getString(2));
		        Log.d("VIDEO SIZE", c.getString(3));*/
		    }while (c.moveToNext());
		    c.close();
		}
		Log.d("VIDEO", "Total count of videos: " + vidsCount);
		db.execSQL("delete from videolocal where link = '0'");
	  	db.close();
		}
	
}
