package com.example.sy.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import PlayerMp3.Son;

import java.util.ArrayList;

/**
 * Created by sy on 27/04/18.
 */

public class TabHome extends Fragment {

    private ArrayList<Son> dataModels;
    private ListView listView;
    private static ListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tabhome,container,false);
        populateliste(rootView);
        return rootView;
    }
    public void populateliste(View v){
        listView=v.findViewById(R.id.mylist);


        dataModels= new ArrayList<>();
        dataModels=MainActivity.getSons();
        /*dataModels.add(new Son("Abc","Alpha","Alpha","2.0","hdhdndjj"));
        dataModels.add(new Son("Abc","Alpha","Alpha","2.0","hdhdndjj"));
        dataModels.add(new Son("Abc","Alpha","Alpha","2.0","hdhdndjj"));
        dataModels.add(new Son("Abc","Alpha","Alpha","2.0","hdhdndjj"));
        dataModels.add(new Son("Abc","Alpha","Alpha","2.0","hdhdndjj"));
        dataModels.add(new Son("Abc","Alpha","Alpha","2.0","hdhdndjj"));
        dataModels.add(new Son("Abc","Alpha","Alpha","2.0","hdhdndjj"));
        dataModels.add(new Son("Abc","Alpha","Alpha","2.0","hdhdndjj"));
        dataModels.add(new Son("Abc","Alpha","Alpha","2.0","hdhdndjj"));
        dataModels.add(new Son("Abc","Alpha","Alpha","2.0","hdhdndjj"));
        dataModels.add(new Son("Abc","Alpha","Alpha","2.0","hdhdndjj"));
        dataModels.add(new Son("Abc","Alpha","Alpha","2.0","hdhdndjj"));
        dataModels.add(new Son("Abc","Alpha","Alpha","2.0","hdhdndjj"));
*/
        adapter= new ListAdapter(dataModels,getContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Son dataModel= dataModels.get(position);


                Snackbar snackbar =  Snackbar.make(v, "Titre: " +dataModel.name+"\n"+"Artiste : "+dataModel.singer+"\n"+"Album : "+dataModel.album+"\n"+"Chemin : "+dataModel.path, Snackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();

                snackbarView.setBackgroundColor(Color.GRAY);
                TextView tv= (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(4);
                snackbar.show();



                        //.setAction("No action", null).show();
                //Snackbar.make(view, dataModel.name+"\n"+dataModel.author+" API: "+dataModel.singer, Snackbar.LENGTH_LONG)
                       // .setAction("No action", null).show();
            }
        });
    }
}
