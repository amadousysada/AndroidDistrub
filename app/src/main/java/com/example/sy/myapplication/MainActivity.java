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

    // les variables suivants sont utilises pour communiquer avec mon Analyseur de requete en SOAP.
    private static final String METHOD_NAME = "requete";
    private static final String NAMESPACE = "http://localhost:10048";
    private static final String URL = "http://192.168.43.210:10080/WSAR/Analyseur";//192.168.1.2
    private static final String SOAP_ACTION = "http://192.168.43.210:10080/requete";//192.168.1.2
    private String [] reponse= null;
    private String requete;
    //attributs pour communiquer avec Ice
    private Communicator ic=null;
    private static PlayerPrx remotePlayer;
    // attributs pour l'utilisation de la librairie LibVLC
    private LibVLC mlibvlc;
    private Media media;
    private MediaPlayer m;

    //ce attribut nous indique dans quel etat se trouve notre player.
    private static String status="stop";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //appel de la fonction init() pour initialiser la connection avec notre serveur de stream Ice
        init();
        // ce bloc initie nos trois Tabs.
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
        super.onPause();

    }
    @Override
    public void onStop() {
        super.onStop();

    }
    @Override
    public void onDestroy(){
        super.onDestroy();

    }

    // la methode init sert d'initialiser les variables pour la commication avec notre serveur Ice et l'initialisation de LibVLC.
    public  void init(){
        mlibvlc= new LibVLC(this);
        try {
            ic = com.zeroc.Ice.Util.initialize();
            // cette ligne clone une proxy ,un objet distant c'est a dire cote serveur.
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
    //cette methode gere les evenement sur les elements de notre ToolBar.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //on recupere l'id de l'item selectionne.
        int id = item.getItemId();
        /*on verifie si l'id est identique a celui de notre boutton de speech
        si oui on lance android speech */
        if (id == R.id.mSpeakBtn) {
            startVoiceInput();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // lancement d'android speech.
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

    /* c'est un callback qui se declanche des que android speech fini la transcription de notre message.
    c'est a ce niveau que l'on recupere le message texte brut.*/
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

    /* cette methode lance l'Analyse de requete en declanchant un thread(background process)
    pour eviter tout conflit avec le thread principal*/
    public void analyser(){
        new Mytask().execute();
    }
    // le thread lance par la methdoe Analyseur().
    class Mytask extends AsyncTask<Void, Void, String[]> {
        // un callback qui appel le web Service(SOAP) pour analyser notre requte.
        @Override
        protected String[] doInBackground(Void... voids) {
            return callWebService();
        }

        // ce callback est declanche juste a l'issue de celui en dessu.
        @Override
        protected void onPostExecute(String[] l) {
            super.onPostExecute(l);
            // on teste si le tableau de String(requete rafinee) renvoyer par notre analyser n'est pas nul.
            if(l !=null){

                try {

                    /* appel synchrone de notre factory methode qui prend en argument la requete et puis
                    nous renvoi le status de la requte("RESUME STREAM EN COURS","STREAM EN PAUSE","STREAM EN ARRET","MediaNotFound").*/
                    String r=remotePlayer.MafactoryMethode(l);
                    Toast.makeText(MainActivity.this,r,Toast.LENGTH_LONG).show();
                    //on attribut a notre media l'URL sur lequel se fait le streaming.
                    media= new Media(mlibvlc, Uri.parse("rtp://192.168.43.255:5004"));
                    //on initialize notre playrer.puis on l'associe a notre media qui est a l'ecoute de stream.
                    m=new MediaPlayer(mlibvlc);
                    m.setMedia(media);
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
    // Cette methode sert de communication avec notre web service qui est notre Analyseur de requete.
    public String[]  callWebService(){


        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,30000);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        request.addProperty("speech",requete);
        envelope.setOutputSoapObject(request);


        try {
            // a ce niveau l'appel http est effectue.
            androidHttpTransport.call(SOAP_ACTION, envelope);
            // on recupere la reponse soap de notre web service
            SoapObject resu= (SoapObject)envelope.bodyIn;
            // notre variable reponse est un tableau dont la taille est fixee par la longuer de la reponse soap.
            reponse= new String[resu.getPropertyCount()];
            // on fait une itteration pour alimenter notre variable reponse.
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


    // cette static pour retourner les chansons disponibles sur notre serveur distant.
    public static ArrayList<Son> getSons(){

        Son[] sons=remotePlayer.getSons();

        ArrayList<Son> tracks= new ArrayList<>();


        for (Son s: sons) {

            tracks.add(s);
        }

        return tracks;
    }
    // methode static qui renvoie la chanson en cours .
    public static Son getSonEnCours(){

        return remotePlayer.getCurrentSon();
    }
    // methode static qui renvoie le status de notre player.
    public static String getStatus(){
        return status;
    }


}
