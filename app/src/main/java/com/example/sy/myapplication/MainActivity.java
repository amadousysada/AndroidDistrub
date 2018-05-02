package com.example.sy.myapplication; /**
 * Created by sy on 27/04/18.
 */

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import PlayerMp3.PlayerPrx;
import PlayerMp3.Son;

import com.zeroc.Ice.Communicator;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private static final String METHOD_NAME = "requete";
    private static final String NAMESPACE = "http://localhost:10048";
    private static final String URL = "http://192.168.43.210:10080/WSAR/Analyseur";//192.168.1.2
    private static final String SOAP_ACTION = "http://192.168.43.210:10080/requete";//192.168.1.2
    private String [] reponse= null;
    private String requete;
    private Communicator ic=null;
    private LibVLC mlibvlc;
    private Media media;
    private MediaPlayer m;
    private static PlayerPrx remotePlayer;
    private static String status="stop";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        String s= "play hotel california";
        System.out.println(s.substring("play".length()+1,s.length()));


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.home2));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.musicplayer));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.about2));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.

    }
    @Override
    public void onStop() {
        super.onStop();  // Always call the superclass method first

    }
    @Override
    public void onDestroy(){
        super.onDestroy();

    }
    public  void init(){
        mlibvlc= new LibVLC(this);
        try {
            ic = com.zeroc.Ice.Util.initialize();
            com.zeroc.Ice.ObjectPrx base = ic.stringToProxy("PlayerMp3:default -h 192.168.43.210 -p 10000");
            remotePlayer = PlayerPrx.checkedCast(base);
            if (remotePlayer == null)
                throw new Error("Invalid proxy");

        } catch (com.zeroc.Ice.LocalException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mSpeakBtn) {
            startVoiceInput();

            System.out.println("WAOOOOOU LA PRIERE !");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Je vous ecoute !");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            a.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    requete=result.get(0);
                    analyser();
                    Toast.makeText(MainActivity.this,requete,Toast.LENGTH_LONG).show();
                }
                break;
            }

        }
    }

    public void analyser(){
        new Mytask().execute();
    }
    class Mytask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... voids) {
            return callWebService();
        }

        @Override
        protected void onPostExecute(String[] l) {
            super.onPostExecute(l);
            if(l !=null){
                //String o=(l[1]!=null)?l[1]:"null";
                //t.setText("Action :"+l[0]+"\n"+"Objet :"+o+"\n");

                try {

                    String r=remotePlayer.MafactoryMethode(l);
                    Toast.makeText(MainActivity.this,r,Toast.LENGTH_LONG).show();
                    media= new Media(mlibvlc, Uri.parse("rtp://192.168.43.255:5004"));
                    m=new MediaPlayer(mlibvlc);
                    m.setMedia(media);
                    System.out.println("duree du media :"+media.getDuration());
                    System.out.println("duree du player :"+m.getAudioDelay());
                    switch (r){
                        case "RESUME STREAM EN COURS":
                            m.play();
                            status="playing";
                            Toast.makeText(MainActivity.this,"Lecture en cours ....",Toast.LENGTH_LONG).show();
                            break;
                        case "STREAM EN PAUSE":
                            m.pause();
                            status="pause";
                            break;
                        case "STREAM EN ARRET":
                            m.stop();
                            m.release();
                            status="stop";
                            break;
                        case "MediaNotFound":

                            Toast.makeText(MainActivity.this,"AUCUNE CHANSON ASSOCIEE A CE NOM !",Toast.LENGTH_LONG).show();
                            break;
                        default:
                            break;

                    }

                } catch (com.zeroc.Ice.LocalException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            else{
                Toast.makeText(MainActivity.this,"AUCUNE REPONSE",Toast.LENGTH_LONG).show();
            }
        }
    }

    public String[]  callWebService(){


        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,30000);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        request.addProperty("speech",requete);
        //envelope.dotNet=true;
        envelope.setOutputSoapObject(request);


        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);

            SoapObject resu= (SoapObject)envelope.bodyIn;
            reponse= new String[resu.getPropertyCount()];
            for(int i=0;i<resu.getPropertyCount();i++){

                reponse[i]=resu.getProperty(i).toString();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Log.e("erreur",e.toString());
        }
        return reponse;
    }



    public static ArrayList<Son> getSons(){

        Son[] sons=remotePlayer.getSons();

        ArrayList<Son> tracks= new ArrayList<>();


        for (Son s: sons) {

            tracks.add(s);
        }

        return tracks;
    }

    public static Son getSonEnCours(){

        return remotePlayer.getCurrentSon();
    }

    public static String getStatus(){
        return status;
    }


}
