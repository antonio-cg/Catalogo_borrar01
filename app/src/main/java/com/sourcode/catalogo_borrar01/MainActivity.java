package com.sourcode.catalogo_borrar01;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sourcode.catalogo_borrar01.model.Flower;
import com.sourcode.catalogo_borrar01.model.Generos;
import com.sourcode.catalogo_borrar01.parsers.FlowerJSONParser;
import com.sourcode.catalogo_borrar01.parsers.FlowerXMLParser;
import com.sourcode.catalogo_borrar01.utils.PersistentData;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ListActivity {

    TextView output;
    ProgressBar pb;
    List<MyTask> tasks;

    List<Flower> flowerList;

    List<Generos> generosList;

    private String apiURL = "http://192.168.1.70/api/v2/get_api?key=12345&tipo=generos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // output = (TextView) findViewById(R.id.textView);
        // output.setMovementMethod(new ScrollingMovementMethod());

        pb = (ProgressBar) findViewById(R.id.progressBar1);
        pb.setVisibility(View.INVISIBLE);

        tasks = new ArrayList<>();

        if (isOnline()) {
            requestData("http://services.hanselandpetal.com/feeds/flowers.json");
            // requestData(apiURL);
        }else{
            Toast.makeText(this,"Network isn't available",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent i = new Intent(this, DetalleActivity.class);
        startActivity(i);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if (isOnline()) {
                requestData("http://services.hanselandpetal.com/feeds/flowers.json");
                // requestData(apiURL);
            }else{
                Toast.makeText(this,"Network isn't available",Toast.LENGTH_LONG).show();
            }
        }
        return false;

    }

    private void requestData(String... uri) {
        MyTask task = new MyTask();
        // task.execute("Param 1","Param 2","Param 3"); // LLAMADA SERIAL
        // task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "Param 1","Param 2","Param 3"); // LLAMADA PARALELA

        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uri[0]);
    }

    protected void updateDisplay(String message){
        // output.append(message+"\n");

        /*
        if (flowerList != null){
            for (Flower flower : flowerList){
                output.append(flower.getName() + " - $" + flower.getPrice() + "\n");
            }
        }
        */

        /*
        if (generosList != null){
            for (Generos genero : generosList){
                output.append(genero.getId() + " - " + genero.getNombre() + "\n");
            }
        }
        */

        FlowerAdapter adapter = new FlowerAdapter(this, R.layout.item_flower, flowerList);
        setListAdapter(adapter);
    }

    /**
     * VERIFICAR CONEXION A INTERNET
     * @return true/false;
     */
    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }else{
            return false;
        }
    }

    private class MyTask extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            // updateDisplay("Starting task");

            if (tasks.size() == 0) {
                pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected String doInBackground(String... params) {
            /*
            for (int i=0; i<params.length; i++){
                publishProgress("Working with " + params[i]);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            return "Task complete";
            */


            String content = HttpManager.getData(params[0]);
            return content;

        }

        @Override
        protected void onPostExecute(String s) {

            tasks.remove(this);
            if (tasks.size() == 0) {
                pb.setVisibility(View.INVISIBLE);
            }

            if (s == null){
                Toast.makeText(MainActivity.this, "Can't connect to web service", Toast.LENGTH_LONG).show();
                return;
            }

            // generosList = FlowerJSONParser.parseFeedGeneros(s);
            // updateDisplay(s);
            if(PersistentData.getInstance().getFlowers()==null)
            {
                FlowerJSONParser.parseFeed(s);
            };

            flowerList = PersistentData.getInstance().getFlowers();

            updateDisplay(s);

        }

        @Override
        protected void onProgressUpdate(String... values) {
            // updateDisplay(values[0]);
        }
    }
}
