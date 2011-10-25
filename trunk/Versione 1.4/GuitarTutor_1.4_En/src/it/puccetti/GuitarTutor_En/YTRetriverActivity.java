package it.puccetti.GuitarTutor_En;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;


public class YTRetriverActivity extends Activity {

	private static String YOU_TUBE_HEADER = "http://www.youtube.com/";
	private static final String QUERY_REQ = "results?search_query=";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.webview);
		final Activity activity = this;

		// Adds Progrss bar Support
		this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
		 
		// Makes Progress bar Visible
		getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
		WebView webview = (WebView) findViewById(R.id.webview);

		WebSettings wset = webview.getSettings();
		wset.setJavaScriptEnabled(true);
		wset.setBlockNetworkLoads(false);
		wset.setPluginsEnabled(true);

		
		webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different
				// scales.
				// The progress meter will automatically disappear when we reach
				// 100%
				if (progress <= 100) {
					activity.setProgress(progress * 100);
				}
			}
		});
		webview.setWebViewClient(new GTutorWebViewClient());

		final Button button = (Button) findViewById(R.id.backButton);
		button.setOnClickListener(buttonBackOnClickListener);

		Bundle b = getIntent().getExtras();
		String serchString = "";
		serchString = b.getString("keysearch");
		webview.loadUrl(YOU_TUBE_HEADER + QUERY_REQ + "guitar+" + serchString
				+ "+paul+guilbert&aq=f");
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

	private class GTutorWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

}
