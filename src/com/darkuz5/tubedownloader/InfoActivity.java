package com.darkuz5.tubedownloader;

import com.darkuz5.tubedonwloader.R;

import android.R.string;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle; 
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class InfoActivity extends ActionBarActivity {
	 private Typeface youTube;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		youTube = Typeface.createFromAsset(getAssets(), "fonts/youtube.otf");
        
        int titleId = getResources().getIdentifier("action_bar_title", "id",
                "android");
        TextView yourTextView = (TextView) findViewById(titleId); 
        yourTextView.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_DeviceDefault_Large);
        yourTextView.setTypeface(youTube);
        
        TextView textView = (TextView) findViewById(R.id.about);
        String source = "TubeDownloader by <a href='https://twitter.com/darkuz5'>@darkuz5 </a><br><br>Stream your favorite videos from a popular video site<br><br><br><br><br><br>Version 1.1.0<br>";
        textView.setText(Html.fromHtml(source));
        //textView.setMovementMethod(LinkMovementMethod.getInstance());
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		
		int id = item.getItemId(); 
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
	
 
}
