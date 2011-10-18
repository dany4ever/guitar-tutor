package it.puccetti.GuitarTutor_En;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.google.gdata.data.youtube.VideoEntry;
public class YTRetriverActivity extends ListActivity implements
OnItemSelectedListener {

	/* Costanti per la ricerca */
	private static String YOUTUBE_URI_HEADER = "http://gdata.youtube.com/feeds/api/videos?orderby=updated&vq=";
	private static String CATEGORY_FILTER_1 = "chitarra";
	private static String CATEGORY_FILTER_2 = "guitar";
	private static int DEBUG_MODE = 0;
	private List<VideoEntry> m_videoEntries = null;
	//Elementi per adapter
	private ArrayList<Elemento> m_ListaElementi = null;
	private GTutorYtubeAdapter m_adapter = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle b = getIntent().getExtras();
		String serchString = "";
		serchString = b.getString("keysearch");

		serchString = YOUTUBE_URI_HEADER + serchString + "+"
				+ CATEGORY_FILTER_1 + "+" + CATEGORY_FILTER_2;

		// Carico la lista dei video
		loadVideoFeed(serchString);

		if (m_adapter == null) {

			m_adapter = new GTutorYtubeAdapter(this, R.layout.list_video,
					m_ListaElementi);
		}

		setListAdapter(m_adapter);

		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// DISPATCH ALL'ACTIVITY DI VISUALIZZAZIONE VIDEO
				Intent poIntent;
				String sUrlVideo = null;

				sUrlVideo = m_ListaElementi.get(position).m_sUrlVideo;
				poIntent = new Intent(view.getContext(), watchVideo.class);

				poIntent.putExtra("videoUrl", sUrlVideo);
				startActivityForResult(poIntent, 0);
			}

		});

	}

	public void onDestroy() {
		super.onDestroy();
		if(m_videoEntries!=null)
		{
			m_videoEntries.clear();
		}
		
		if(m_ListaElementi!=null)
		{
			m_ListaElementi.clear();
		}
		m_adapter.clear();
	}

	/*
	 * Carica i video nella list view con le rispettive thumbnais
	 */
	private void loadVideoFeed(String Url) {
		Elemento curEl = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document doc = null;
		URL url = null;
		InputStream isConn = null;
		XPathFactory fac = XPathFactory.newInstance();
		XPath xpath = fac.newXPath();
		XPathExpression expr = null;
		boolean bContinue = false;
		NodeList mediagroups = null;
		NodeList mediaTitles = null;
		NodeList mediaDescrs = null;
		NodeList mediaUrlRstps = null;
		NodeList mediaThumbnails = null;
		NodeList mediaRating = null;
		
		factory.setNamespaceAware(true); // never forget this!

		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			URLEncoder.encode(url.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		try {
			url = new URL(Url);

		} catch (MalformedURLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		if (url != null) {
			// Ottengo lo stream dell'url
			try {
				isConn = url.openStream();
			} catch (IOException e1) {
				bContinue = false;
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				doc = builder.parse(isConn);
				bContinue = true;
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (bContinue) {
			/************************************************/
			/*********** PARSING XML E COSTRUZIONE LISTA *****/
			mediagroups = doc.getElementsByTagName("media:group");
			mediaTitles = null;
			mediaDescrs = null;
			mediaUrlRstps = null;
			mediaThumbnails = null;
			mediaRating = null;
			
			// Ciclo per ogni oggetto media group e reperisco le sezioni utili
			for (int i = 0; i < mediagroups.getLength(); i++) {
				curEl = new Elemento();

				try {
					expr = xpath.compile("//media:title/");
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					mediaTitles = (NodeList) expr.evaluate(doc,
							XPathConstants.NODESET);
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// NODI MEDIA GROUP
				/* TITOLO */
				// TITOLO DEL MEDIA GROUP CORRENTE
				if (mediaTitles != null) {
					curEl.m_sTitolo = mediaTitles.item(i).getNodeValue();
				}

				/* DESCRIZIONE */
				try {
					expr = xpath.compile("//media:description");
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					mediaDescrs = (NodeList) expr.evaluate(doc,
							XPathConstants.NODE);
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if ((mediaDescrs != null) && (mediaDescrs.getLength() > 0)) {
					curEl.m_sDescrizione = mediaDescrs.item(i).getNodeValue();
				}

				/* URL RSTP media:content[@yt:format='1']/url */
				try {
					expr = xpath.compile("//media:content[@yt:format='1']/url");
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					mediaUrlRstps = (NodeList) expr.evaluate(doc,
							XPathConstants.NODE);
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if ((mediaUrlRstps != null) && (mediaUrlRstps.getLength() > 0)) {
					curEl.m_sUrlVideo = mediaUrlRstps.item(i).getNodeValue();
				}

				/* THUMBNAIL media:thumbnail[@height='90']/url */
				try {
					expr = xpath.compile("//media:thumbnail[@height='90']");
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					mediaThumbnails = (NodeList) expr.evaluate(doc,
							XPathConstants.NODE);
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if ((mediaThumbnails != null)
						&& (mediaThumbnails.getLength() > 0)) {
					curEl.m_sUrlVideo = mediaThumbnails.item(i).getNodeValue();
				}
				
				/* RATING */
				try {
					expr = xpath.compile("//media:rating[@country='IT']");
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					mediaRating = (NodeList) expr.evaluate(doc,
							XPathConstants.NODE);
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if ((mediaRating != null)
						&& (mediaRating.getLength() > 0)) {
					curEl.m_fRating = Float.parseFloat(mediaRating.item(i).getNodeValue());
				}
				
				
				m_ListaElementi.add(curEl);

				// Pulizia
				mediaTitles = null;
				mediaDescrs = null;
				mediaUrlRstps = null;
				mediaThumbnails = null;
				mediaRating = null;
			}

		}

		url = null;
		fac = null;
		xpath = null;
		expr = null;
		doc = null;
		factory = null;
		builder = null;
		curEl = null;
		url = null;

		mediagroups = null;
		mediaTitles = null;
		mediaDescrs = null;
		mediaUrlRstps = null;
		mediaThumbnails = null;
		mediaRating = null;
		try {
			isConn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isConn = null;

	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		m_adapter.update();
	}	
}
