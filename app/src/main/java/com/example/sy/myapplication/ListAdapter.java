package com.example.sy.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sy.myapplication.R;

import PlayerMp3.Son;

import java.util.ArrayList;

/**
 * Created by sy on 28/04/18.
 */


/*cette classe est un adapter pour la list view de musiques affichee au dans l'onglet accueil de mon application*/
public class ListAdapter extends ArrayAdapter<Son> implements View.OnClickListener{

    private ArrayList<Son> dataSet;
    Context mContext;
    private int lastPosition = -1;

    public ListAdapter(ArrayList<Son> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;
    }

    private static class ViewHolder {
        TextView txtName;
        TextView txtSinger;
        TextView txtAuthor;
        TextView txtAlbum;
        TextView txtPath;
        ImageView info;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Son dataModel=(Son)object;




        switch (v.getId())
        {

            case R.id.item_info:

                Snackbar snackbar =  Snackbar.make(v, "Titre: " +dataModel.name+"\n"+"Artiste : "+dataModel.singer+"\n"+"Album : "+dataModel.album+"\n"+"Chemin : "+dataModel.path, Snackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();

                snackbarView.setBackgroundColor(Color.GRAY);
                TextView tv= (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(4);
                snackbar.show();

                break;


        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // pour une position donnee on recupere le son qui y est associe
        Son dataModel = getItem(position);

        ViewHolder viewHolder;

        final View result;
        // on verifie si le view existe en cache on le retourne sinon on fait une inflate
        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtName   = convertView.findViewById(R.id.name);

            viewHolder.txtAuthor = convertView.findViewById(R.id.author);

            viewHolder.info= convertView.findViewById(R.id.item_info);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtName.setText(dataModel.name);

        viewHolder.txtAuthor.setText(dataModel.author);

        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        return convertView;
    }
}
