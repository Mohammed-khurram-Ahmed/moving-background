package com.screen.shots;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	MovingSurface surface;
	Animation fadeIn, fadeOut;
	TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7;
	ArrayList<TextView> tvs;
	int i = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		surface = new MovingSurface(this);
		setContentView(R.layout.screen_shots);
		FrameLayout toplayout = (FrameLayout) findViewById(R.id.LinearLayout01);
		FrameLayout topbg = (FrameLayout) toplayout.findViewById(R.id.bgLayout);
		topbg.addView(surface);
		tvs = new ArrayList<TextView>();
		tv1 = (TextView) findViewById(R.id.textView1);
		tvs.add(tv1);
		tv2 = (TextView) findViewById(R.id.textView2);
		tvs.add(tv2);
		tv3 = (TextView) findViewById(R.id.textView3);
		tvs.add(tv3);
		tv4 = (TextView) findViewById(R.id.textView4);
		tvs.add(tv4);
		tv5 = (TextView) findViewById(R.id.textView5);
		tvs.add(tv5);
		tv6 = (TextView) findViewById(R.id.textView6);
		tvs.add(tv6);
		tv7 = (TextView) findViewById(R.id.textView7);
		tvs.add(tv7);
		new TextAnimator(tvs.get(i++), "Alhamdulillah").execute();

	}

	class TextAnimator extends AsyncTask<Void, Void, Void> {
		TextView view;
		String text;

		public TextAnimator(TextView view, String text) {

			this.view = view;
			this.text = text;
		}

		@Override
		protected void onPreExecute() {
			fadeIn = new AlphaAnimation(0.0f, 1.0f);
			fadeIn.setDuration(3000);

			fadeIn.setFillAfter(true);
			view.setVisibility(View.VISIBLE);
			view.setText(text);
			view.startAnimation(fadeIn);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(3000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			fadeOut = new AlphaAnimation(1.0f, 0.0f);
			fadeOut.setDuration(3000);
			fadeOut.setFillAfter(true);
			fadeOut.setStartOffset(3000);
			view.setText(text);
			view.startAnimation(fadeOut);
			view.setVisibility(View.INVISIBLE);
fadeOut.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animation arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation arg0) {
					new TextAnimator(tvs.get(i), "Subhannallah").execute();
					i++;
					if (i > 6) {
						i = 0;
					}		
				}
			});
			
		}

	}

	class TextAnimator1 extends AsyncTask<Void, Void, Void> {
		TextView view;
		String text;

		public TextAnimator1(TextView view, String text) {
			this.view = view;
			this.text = text;
		}

		@Override
		protected void onPreExecute() {
			fadeIn = new AlphaAnimation(0.0f, 1.0f);
			fadeIn.setDuration(3000);
			fadeIn.setFillAfter(true);
			view.setVisibility(View.VISIBLE);
			view.setText(text);
			view.startAnimation(fadeIn);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(3000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			view.setText(text);
			fadeOut = new AlphaAnimation(1.0f, 0.0f);
			fadeOut.setDuration(3000);
			fadeOut.setFillAfter(true);
			view.startAnimation(fadeOut);
			view.setVisibility(View.INVISIBLE);
			fadeOut.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animation arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation arg0) {
					new TextAnimator(tvs.get(i), "Subhannallah").execute();
					i++;
					if (i > 6) {
						i = 0;
					}		
				}
			});
			
			
		}
	}
}
