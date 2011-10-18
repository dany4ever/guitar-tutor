package it.puccetti.GuitarTutor;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/*
 * Adapter per lista multilinea
 * */
public class GTutorYtubeAdapter extends ArrayAdapter<Elemento> {

	private int m_LayoutResource;
	private ArrayList<Elemento> m_ElementiLista = null;
	private Context m_poContext;

	public GTutorYtubeAdapter(Context context, int textViewResourceId,
			ArrayList<Elemento> poElementiLista) {
		super(context, textViewResourceId, poElementiLista);
		this.m_ElementiLista = poElementiLista;
		this.m_LayoutResource = textViewResourceId;
		this.m_poContext = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View v = null;
		String sTitolo = null;
		String sDescr = null;
		String sImageURI = null;
		float fRating = 0;
		LayoutInflater inflater;
		RatingBar rating = null;
		TextView tt = null;
		TextView bt = null;
		inflater = (LayoutInflater) m_poContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (v == null) {
			v = inflater.inflate(this.m_LayoutResource, null);
		} else {
			v = convertView;
			inflater.inflate(this.m_LayoutResource, null);
		}

		sTitolo = m_ElementiLista.get(position).m_sTitolo;
		sDescr = m_ElementiLista.get(position).m_sDescrizione;
		sImageURI = m_ElementiLista.get(position).m_sImage;
		
		if ((sTitolo != null) && (sDescr != null)) {
			tt = (TextView) v.findViewById(R.id.label_top);
			bt = (TextView) v.findViewById(R.id.label_bottom);
			if (tt != null) {
				tt.setText(sTitolo);
			}
			if (bt != null) {
				bt.setText(sDescr);
			}
		}

		//THUMBNAIL
		if(sImageURI!=null){
			attachThumbnail(v, sImageURI);
		}
		
		rating = null;
		//Ratings@+id/ratingBar1
		if(m_ElementiLista.get(position).m_fRating >0) {
			fRating = m_ElementiLista.get(position).m_fRating;
			rating = (RatingBar) v.findViewById(R.id.ratingBar1);
			rating.setRating(fRating);
		}
		
		return v;
	}

	private boolean attachThumbnail(View v, String sImageURI){
		Boolean bRet = false;
		InputStream is = null;
		BufferedInputStream bis = null;
		Bitmap bm = null;
		URL imageUrl = null;
		ImageView curThumb = null;
		URLConnection uConn = null;
		try {
			imageUrl = new URL(sImageURI);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			uConn = imageUrl.openConnection();
			uConn.connect();
			is = uConn.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bis = new BufferedInputStream(is);
		bm = BitmapFactory.decodeStream(bis);
		
		try {
			bis.close();
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//curThumb = (ImageView) v.findViewById(R.id.thumb_video);
		curThumb.setImageBitmap(bm);
		
		
		is = null;
		bis = null;
		bm = null;
		imageUrl = null;
		curThumb = null;
		uConn = null;
		
		bRet = true;
		return bRet;
	}
	
	
	public int getCount() {
		return m_ElementiLista.size();
	}

	public Elemento getItem(int position) {
		return m_ElementiLista.get(position);
	}

	public boolean isEmpty() {

		return m_ElementiLista.isEmpty();
	}

	public void update() {
		notifyDataSetChanged();

	}

	public void clear() {
		super.clear();
		m_ElementiLista.clear();
	}
}
