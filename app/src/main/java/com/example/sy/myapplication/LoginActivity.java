package com.example.sy.myapplication;


import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.speech.RecognizerIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;

import android.util.Log;
import android.widget.Toast;

import com.example.sy.myapplication.PlayerMp3.PlayerPrx;
import com.zeroc.Ice.Communicator;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.SoapEnvelope;

import org.ksoap2.transport.HttpTransportSE;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.LibVLC;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private static final String METHOD_NAME = "requete";
    private static final String NAMESPACE = "http://localhost:10048";
    private static final String URL = "http://192.168.1.2:10080/WSAR/Analyseur";
    private static final String SOAP_ACTION = "http://192.168.1.2:10080/requete";
    private static ListView lv;
    private String [] reponse= null;
    private String requete;
    private TextView t;
    private Communicator ic=null;
    private LibVLC mlibvlc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mlibvlc= new LibVLC(this);
        // Set up the login form.
        t= findViewById(R.id.moi93);

        t.setText("Veillez cliquez sur le microphone ");
        ImageButton micro = findViewById(R.id.mSpeakBtn);
        micro.setOnClickListener((View v) -> {
            startVoiceInput();
        });
        
        
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
            //return null;
        }

        @Override
        protected void onPostExecute(String[] l) {
            super.onPostExecute(l);
            if(l !=null){
                //String o=(l[1]!=null)?l[1]:"null";
                //t.setText("Action :"+l[0]+"\n"+"Objet :"+o+"\n");
                Toast.makeText(LoginActivity.this,"SUPER",Toast.LENGTH_LONG).show();
                try {
					ic = com.zeroc.Ice.Util.initialize();
					com.zeroc.Ice.ObjectPrx base = ic.stringToProxy("PlayerMp3:default -h 192.168.1.2 -p 10000");
					PlayerPrx remotePlayer = PlayerPrx.checkedCast(base);
					if (remotePlayer == null)
						throw new Error("Invalid proxy");
					remotePlayer.MafactoryMethode(l);

					Media media= new Media(mlibvlc,Uri.parse("rtp://192.168.1.255:5004"));

					MediaPlayer m=new MediaPlayer(mlibvlc);
					m.setMedia(media);
					m.play();



				} catch (com.zeroc.Ice.LocalException e) {
					e.printStackTrace();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
            }
            else{
                Toast.makeText(LoginActivity.this,"AUCUNE REPONSE",Toast.LENGTH_LONG).show();
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

            System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRR   "+reponse);
        }
        catch (Exception e){
            e.printStackTrace();
            Log.e("erreur",e.toString());
        }
        return reponse;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }


}

