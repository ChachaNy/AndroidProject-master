package com.betrisey.suzanne.androidproject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.betrisey.suzanne.dondesang.backend.cDonneurApi.CDonneurApi;
import com.betrisey.suzanne.dondesang.backend.cDonneurApi.model.CDonneurBack;
import com.betrisey.suzanne.dondesang.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import db.object.CDonneur;

/**
 * Created by Suzanne on 26.12.2015.
 */
public class EndpointsAsyncTask extends AsyncTask<Void, Void, List<CDonneurBack>> {
    private static CDonneurApi cDonneurApi = null;
    private static final String TAG = EndpointsAsyncTask.class.getName();
    private CDonneurBack donneur;

    EndpointsAsyncTask(){}

    EndpointsAsyncTask(CDonneurBack donneur){
        this.donneur = donneur;
    }

    @Override
    protected List<CDonneurBack> doInBackground(Void... params) {

        if(cDonneurApi == null){
            // Only do this once
           CDonneurApi.Builder builder = new CDonneurApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    // if you deploy on the cloud backend, use your app name
                    // such as https://<your-app-id>.appspot.com
                    .setRootUrl("https://dondesang-2222.appspot.com/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            cDonneurApi = builder.build();
        }

        try{
            // Call here the wished methods on the Endpoints
            // For instance insert
            if(donneur != null){
                cDonneurApi.insert(donneur).execute();
                Log.i(TAG, "insert employee");
            }
            // and for instance return the list of all employees
            return cDonneurApi.list().execute().getItems();

        } catch (IOException e){
            Log.e(TAG, e.toString());
            return new ArrayList<CDonneurBack>();
        }
    }

    //This method gets executed on the UI thread - The UI can be manipulated directly inside
    //of this method
    @Override
    protected void onPostExecute(List<CDonneurBack> result){

        if(result != null) {
            for (CDonneurBack donneur : result) {
                Log.i(TAG, "First name: " + donneur.getPrenom() + " Last name: "
                        + donneur.getNom());

                //Voir ex ci-dessous pour les pochettes
                /*for (Phone phone : employee.getPhones()) {
                    Log.i(TAG, "Phone number: " + phone.getNumber() + " Type: " + phone.getType());
                }*/
            }
        }
    }
/*class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
    private static MyApi myApiService = null;
    private Context context;

    @Override
    protected String doInBackground(Pair<Context, String>... params) {
        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://dondesang-1111.appspot.com/_ah/api/");
            // end options for devappserver

            myApiService = builder.build();
        }

        context = params[0].first;
        String name = params[0].second;

        try {
            return myApiService.sayHi(name).execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }*/
}
