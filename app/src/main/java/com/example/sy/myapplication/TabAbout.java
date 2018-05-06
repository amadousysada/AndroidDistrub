package com.example.sy.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sy.myapplication.R;

/**
 * Created by sy on 27/04/18.
 */
// l'onglet About de notre application.
public class TabAbout extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tababout,container,false);
        return rootView;
    }
}
