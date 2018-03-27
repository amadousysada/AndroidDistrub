package com.example.sy.myapplication;


import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.speech.RecognizerIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.AttributeInfo;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.SoapEnvelope;

import org.ksoap2.transport.HttpTransportSE;

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
    private List<String> reponse= null;
    private String requete;
    private TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        t= findViewById(R.id.moi93);
        t.setText("salut les nuls");
        ImageButton micro = (ImageButton) findViewById(R.id.mSpeakBtn);
        micro.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startVoiceInput();
            }
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
    class Mytask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {
            return callWebService();
            //return null;
        }

        @Override
        protected void onPostExecute(List<String> l) {
            super.onPostExecute(l);
            if(l !=null){
                t.setText("Action :"+l.get(0)+"\n"+"Objet :"+l.get(1)+"\n");
                Toast.makeText(LoginActivity.this,"SUPER",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(LoginActivity.this,"NOTHING BUT TROUBLE",Toast.LENGTH_LONG).show();
            }
        }
    }

    public List<String>  callWebService(){


        String result = "0";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,30000);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        request.addProperty("speech",requete);
        //envelope.dotNet=true;
        envelope.setOutputSoapObject(request);


        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);

            SoapObject resu= (SoapObject)envelope.bodyIn;

            Object resul1= resu.getProperty(0);
            Object resul2= resu.getProperty(1);
            reponse= new ArrayList<String>();
            reponse.add(resul1.toString());
            reponse.add(resul2.toString());
            System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRR"+reponse);
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

