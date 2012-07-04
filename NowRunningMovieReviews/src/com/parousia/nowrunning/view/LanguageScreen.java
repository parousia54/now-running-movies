package com.parousia.nowrunning.view;

import java.util.ArrayList;
import java.util.Hashtable;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.parousia.nowrunning.R;
import com.parousia.nowrunning.common.ApplicationConstants;
import com.parousia.nowrunning.common.ApplicationConstants.Language;

public class LanguageScreen extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.langaugeviewpager);

		ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
		final MyPagerAdapter adapter = new MyPagerAdapter(this);
		viewPager.setAdapter(adapter);

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int page) {
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				adapter.addPage(position);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	private class MyPagerAdapter extends PagerAdapter {

		private ArrayList<LinearLayout> views;
		
		Hashtable<Integer, Language> languageList = new Hashtable<Integer, ApplicationConstants.Language>();
		Context context;

		public MyPagerAdapter(Context c) {
			context = c;
			views = new ArrayList<LinearLayout>();
			languageList.put(0, Language.MALAYALAM);
			languageList.put(0, Language.TAMIL);
			languageList.put(0, Language.TELUGU);
			languageList.put(0, Language.KANNADA);
			languageList.put(0, Language.HINDI);
			addPage(0);
//			views.add(new MessageList(context, Language.TAMIL));
//			views.add(new MessageList(context, Language.TELUGU));
//			views.add(new MessageList(context, Language.HINDI));
//			views.add(new MessageList(context, Language.KANNADA));
		}

		public void addPage(int position) {
			Log.d(ApplicationConstants.LOGTAG, "Adding page : "+position);
			views.add(new MessageList(context, (Language)languageList.get(position)));
		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		@Override
		public Object instantiateItem(View view, int position) {
			Log.d(ApplicationConstants.LOGTAG,String.valueOf(position));
			View myView = (View)views.get(position);
			((ViewPager) view).addView(myView);
			return myView;
		}
		
		@Override
		public void destroyItem(View view, int arg1, Object object) {
			((ViewPager) view).removeView((LinearLayout) object);
		}
		
	}
}
