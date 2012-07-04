package com.parousia.nowrunning.view;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Layout;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parousia.nowrunning.R;
import com.parousia.nowrunning.common.ApplicationConstants;
import com.parousia.nowrunning.common.ApplicationConstants.Language;
import com.parousia.nowrunning.data.AndroidSaxFeedParser;
import com.parousia.nowrunning.data.FeedParser;
import com.parousia.nowrunning.data.Message;

public class MessageList extends LinearLayout {

	private List<Message> messages;
	private ProgressDialog progress;
	private List<String> titles;
	private Context context;
	private static String feedUrl;
	private ListView listview;
	private TextView languageTitle;
	private Language language;
	
	public MessageList(Context c, Language lang) {
		super(c);
		this.context = c;
		this.language = lang;
		feedUrl = ApplicationConstants.feed + language.toString()+".xml";
		init();
		
	}


	public enum ParserType{
		SAX, DOM, ANDROID_SAX, XML_PULL;
	}

	
	public void init() {
		listview = new ListView(getContext());
		languageTitle = new TextView(getContext());
		progress = new ProgressDialog(getContext());
		progress.setMessage("Loading....");	
		progress.show();
		
		new LoadFeedTask().execute("");
	}


	private List<String> loadFeed(ParserType type) {
		try {
			Log.i(ApplicationConstants.LOGTAG, "ParserType=" + type.name());
			Log.i(ApplicationConstants.LOGTAG, "URL=" + feedUrl);
			FeedParser parser = new AndroidSaxFeedParser(feedUrl);//FeedParserFactory.getParser(type);
			long start = System.currentTimeMillis();
			messages = parser.parse();
			long duration = System.currentTimeMillis() - start;
			Log.i(ApplicationConstants.LOGTAG, "Parser duration=" + duration);
			String xml = writeXml();
			Log.i(ApplicationConstants.LOGTAG, xml);
			List<String> titles = new ArrayList<String>(messages.size());
			for (Message msg : messages) {
				titles.add(msg.getTitle());
			}
			return titles;
			
		} catch (Throwable t) {
			Log.e(ApplicationConstants.LOGTAG, t.getMessage(), t);
			return null;
		}
	}

	private String writeXml() {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "messages");
			serializer.attribute("", "number", String.valueOf(messages.size()));
			for (Message msg : messages) {
				serializer.startTag("", "message");
				serializer.attribute("", "date", msg.getDate());
				serializer.startTag("", "title");
				serializer.text(msg.getTitle());
				serializer.endTag("", "title");
				serializer.startTag("", "url");
				serializer.text(msg.getLink().toExternalForm());
				serializer.endTag("", "url");
				serializer.startTag("", "body");
				serializer.text(msg.getDescription());
				serializer.endTag("", "body");
				serializer.endTag("", "message");
			}
			serializer.endTag("", "messages");
			serializer.endDocument();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	private class LoadFeedTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			for (String url : urls) {
				titles = loadFeed(ParserType.ANDROID_SAX);
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			if(progress.isShowing())
			{
				progress.hide();
			}
			
			setOrientation(1);
			languageTitle.setText(language.toString().toUpperCase());
			languageTitle.setGravity(Gravity.CENTER_HORIZONTAL);
//			languageTitle.setTextColor(R.color.cyan);
			LayoutParams titleParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			addView(languageTitle,titleParams);
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
					R.layout.row, titles);
			listview.setAdapter(adapter);
			LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, (float) 1.0);
			addView(listview, params);
			listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> l, View v,
						int position, long id) {
					Intent viewMessage = new Intent(Intent.ACTION_VIEW, Uri.parse(messages
							.get(position).getLink().toExternalForm()));
					getContext().startActivity(viewMessage);
					
				}
			});
		}
		
	}
}