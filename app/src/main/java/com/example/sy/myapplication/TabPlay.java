package com.example.sy.myapplication;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.sy.myapplication.R;

import PlayerMp3.Son;

/**
 * Created by sy on 27/04/18.
 */

// cette classe correspond a notre Onglet de Musique , c'est la ou s'affiche notre player .
public class TabPlay extends Fragment implements SeekBar.OnSeekBarChangeListener{

    private ImageButton btnPlay;
    private ImageButton btnForward;
    private ImageButton btnBackward;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private ImageButton btnPlaylist;
    private ImageButton btnRepeat;
    private ImageButton btnShuffle;
    private SeekBar songProgressBar;
    private TextView songTitleLabel;
    private Handler mHandler = new Handler();
    private TextView songCurrentDurationLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tabplay,container,false);
        init(rootView);
        return rootView;
    }
    // methode pour initialiser nos composants(boutton,TextView etc....).
    public void init(View v){
        btnPlay = (ImageButton) v.findViewById(R.id.btnPlay);
        btnForward = (ImageButton) v.findViewById(R.id.btnForward);
        btnBackward = (ImageButton) v.findViewById(R.id.btnBackward);
        btnNext = (ImageButton) v.findViewById(R.id.btnNext);
        btnPrevious = (ImageButton) v.findViewById(R.id.btnPrevious);
        songProgressBar = (SeekBar) v.findViewById(R.id.songProgressBar);
        songTitleLabel = (TextView) v.findViewById(R.id.songTitle);
        songProgressBar.setOnSeekBarChangeListener(this);
        songCurrentDurationLabel = (TextView) v.findViewById(R.id.songCurrentDurationLabel);
        songProgressBar.setProgress(0);
        songProgressBar.setMax(180);
        // mis a jour de notre progressBar.
        updateProgressBar();
    }

    /**
     * Cette fonction met a jour notre progressBar.
     * */
    public void updateProgressBar() {
        // chaque 1000 ms notre handler appel le Runnable quimet a jour notre progressBar.
        mHandler.postDelayed(mUpdateTimeTask, 1000);
    }

    /**
     * Ici se trouve notre Runnable qui met a jour notre progressBar et notre player en general.
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            // on recupere le status de notre player, grace a notre static methode defini sur notre class MainActivity.
            String status=MainActivity.getStatus();
            // on fait un switch sur le status recupere , puis on fait l'action necessaire selon le cas.
            switch (status){
                case "stop":
                    songTitleLabel.setText("");
                    songProgressBar.setProgress(0);
                    songCurrentDurationLabel.setText("00:00");
                    ((TextView)(getActivity().findViewById(R.id.detailsson))).setText("AUCUNE CHANSON EN COURS DE MUSIQUE !");
                    btnPlay.setImageDrawable(getResources().getDrawable(R.drawable.btn_play));
                    break;
                case "playing":
                    Son s=MainActivity.getSonEnCours();
                    if(!s.name.equals(songTitleLabel.getText())){
                        songTitleLabel.setText(s.name);
                        songProgressBar.setProgress(0);
                        songCurrentDurationLabel.setText(milliSecondsToTimer(songProgressBar.getProgress()));
                        String str="Titre : "+s.name+"\n"+"Artiste : "+s.singer+"\n"+"Album : "+s.album;
                        ((TextView)(getActivity().findViewById(R.id.detailsson))).setText(str);

                    }
                    else {
                        songProgressBar.setProgress(songProgressBar.getProgress() + 1);
                        songCurrentDurationLabel.setText(milliSecondsToTimer(songProgressBar.getProgress()));
                    }
                    btnPlay.setImageDrawable(getResources().getDrawable(R.drawable.btn_pause));
                    break;
                case "pause":
                    btnPlay.setImageDrawable(getResources().getDrawable(R.drawable.btn_play));
                    break;
                default:
                    break;
            }

            // Repeter le processus chanque 1000 ms(1 s).
            mHandler.postDelayed(this, 1000);
        }
    };

    /**
     *
     * */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    /**
     * When user starts moving the progress handler
     * */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * When user stops moving the progress hanlder
     * */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);

        // update timer progress again
        updateProgressBar();
    }
    // cette methode converti le temps sous le format "hh:mn:ss:ms"
    public String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";


        int hours = (int)( milliseconds / (60*60));
        int minutes = (int)(milliseconds / (60));
        int seconds = (int)(milliseconds % (60));
        // si l'heure est >0 on l'affiche dans notre player.
        if(hours > 0){
            finalTimerString = hours + ":";

        }

        // si les seconds son inferieurs a 10 on  y ajoute un zero avant.
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        return finalTimerString;
    }
}
