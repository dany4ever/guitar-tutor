package it.puccetti.GuitarTutor_En;

import it.puccetti.GuitarTutor_En.R;
import it.puccetti.GuitarTutor_En.R.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class YTRetriverActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.webview);

		WebView webview = (WebView) findViewById(R.id.webview);
		WebChromeClient m_WebView = new WebChromeClient();
		WebSettings wset = webview.getSettings();

		wset.setJavaScriptEnabled(true);
		//wset.setBlockNetworkLoads(false);
		wset.setPluginsEnabled(true);
		webview.setWebChromeClient(m_WebView);

		final Button button = (Button) findViewById(R.id.backButton);
		button.setOnClickListener(buttonBackOnClickListener);

		Bundle b = getIntent().getExtras();
		String serchString = "";
		serchString = b.getString("keysearch");
		webview.loadUrl("http://www.youtube.com/results?search_query=justin+guitar+"
				+ serchString + "&aq=f");
		// webview.loadData("<html><body background-color='black'>PROVA!!</body></html>","text/html",
		// "UTF-8");

	}

	Button.OnClickListener buttonBackOnClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub

			finish();
		}
	};

	public void onDestroy() {
		super.onDestroy();
	}

}
