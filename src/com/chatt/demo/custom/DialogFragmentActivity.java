package com.chatt.demo.custom;

import com.chatt.demo.R;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DialogFragmentActivity extends DialogFragment implements
OnItemClickListener {

String[] listitems = { "item01", "item02", "item03", "item04" };

ListView mylist;

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {

View view = inflater.inflate(R.layout.showmap_dialog_fragment, null, false);
mylist = (ListView) view.findViewById(R.id.list);

getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
return view;
}

@Override
public void onActivityCreated(Bundle savedInstanceState) {

super.onActivityCreated(savedInstanceState);

ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
        android.R.layout.simple_list_item_1, listitems);

mylist.setAdapter(adapter);

mylist.setOnItemClickListener(this);

}

@Override
public void onItemClick(AdapterView<?> parent, View view, int position,
    long id) {

dismiss();
Toast.makeText(getActivity(), listitems[position], Toast.LENGTH_SHORT)
        .show();
}



}