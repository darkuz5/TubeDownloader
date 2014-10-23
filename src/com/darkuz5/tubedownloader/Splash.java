package com.darkuz5.tubedownloader;

import com.darkuz5.tubedonwloader.R;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils; 
import android.widget.ImageView; 
import android.widget.TextView;

public class Splash extends ActionBarActivity implements AnimationListener{ 
    Animation animFadein;
	 private Typeface youTube;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		youTube = Typeface.createFromAsset(getAssets(), "fonts/youtube.otf");
		
		ImageView favicon = (ImageView) findViewById(R.id.gear); 
		
		 animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
	                R.anim.rotate);

	        // set animation listener
	        animFadein.setAnimationListener(this);
	        favicon.startAnimation(animFadein);

	        ((TextView) findViewById(R.id.name1)).setTypeface(youTube);
	        ((TextView) findViewById(R.id.name2)).setTypeface(youTube);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
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
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
        // check for fade in animation
        if (animation == animFadein) {
        	Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			finish();
            
        }
		
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}
}
