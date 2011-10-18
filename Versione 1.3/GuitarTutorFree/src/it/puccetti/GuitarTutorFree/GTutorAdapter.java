package it.puccetti.GuitarTutorFree;

import it.puccetti.GuitarTutorFree.R;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/*
 * Adapter per lista multilinea
 * */
public class GTutorAdapter extends ArrayAdapter<Elemento> {

	private int m_LayoutResource;
	private ArrayList<Elemento> m_ElementiLista = null;
	private Context m_poContext;

	public GTutorAdapter(Context context, int textViewResourceId,
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
		LayoutInflater inflater;

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

		if ((sTitolo != null) && (sDescr != null)) {
			TextView tt = (TextView) v.findViewById(R.id.label_top);
			TextView bt = (TextView) v.findViewById(R.id.label_bottom);
			if (tt != null) {
				tt.setText(sTitolo);
			}
			if (bt != null) {
				bt.setText(sDescr);
			}
		}
		return v;
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
