package it.puccetti.GuitarTutor;

import java.util.StringTokenizer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class GTutorWebChromeClient extends WebChromeClient {
	private Context m_context;

	public GTutorWebChromeClient(Context poContext) {

		this.m_context = poContext;
	}

	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// super.shouldOverrideUrlLoading(view, url);

		/*
		 * if (!(url.contains("http://www.youtube.com/watch"))) {
		 * view.loadUrl(url); } else { m_context.startActivity(new
		 * Intent(Intent.ACTION_VIEW, Uri .parse(url))); }
		 */

		return true;
	}

	public void onLoadResource(WebView view, String url) {
		Log.i("onLoadResource()", "url = " + url);
		/*
		 * if (url.contains("m.youtube.com/watch")) { StringTokenizer stok = new
		 * StringTokenizer(url); stok.nextToken("&"); stok.nextToken("&");
		 * stok.nextToken("&"); stok.nextToken("&"); Intent intent = new
		 * Intent(Intent.ACTION_VIEW, Uri.parse(url));
		 * view.getContext().startActivity(intent); }
		 * 
		 * if (url.contains("3gp")) { Intent intent = new
		 * Intent(Intent.ACTION_VIEW, Uri.parse(url));
		 * view.getContext().startActivity(intent); }
		 */
	}

}
