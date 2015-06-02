package com.chatt.demo.wadii.maps;

import java.util.List;

import com.chatt.demo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ComponentAdapter extends ArrayAdapter<Component> {


	public ComponentAdapter(Context context, int textViewResourceId, List<Component> objects) 
	{
		super(context, textViewResourceId, objects);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View curView = convertView;
        if (curView == null) 
        {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            curView = vi.inflate(R.layout.component, null);
        }    
        
        Component cp = getItem(position);
        TextView title = (TextView) curView.findViewById (R.id.title);
        TextView subtitle = (TextView) curView.findViewById (R.id.subtitle);

        
        title.setText(cp.getTitle());
        subtitle.setText(cp.getSubtitle());
        
        return curView;
        
	}
	
}
