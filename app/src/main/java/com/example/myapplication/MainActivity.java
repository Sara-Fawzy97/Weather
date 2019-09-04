package com.example.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    TextView textView;


    class Weather extends AsyncTask<String,Void,String>{



        @Override
        protected String doInBackground(String... address) {
            try {
                URL url= new URL(address[0]);
                HttpURLConnection connection=(HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream inputStream= connection.getInputStream();
                InputStreamReader isr =new InputStreamReader(inputStream);

                int data =isr.read();
                String content="";
                 char ch;
                 while (data != -1)
                 {
                     ch= (char) data;
                     content= content+ ch;
                     data=isr.read();
                 }
                 return  content;
            }
            catch (MalformedURLException e )
            {     e.fillInStackTrace();
            }
            catch(IOException e )
            {e.printStackTrace();}



            return null;
        }
    }

    public void search(View view){

        editText= findViewById(R.id.editText);
        button= findViewById(R.id.button);
        textView= findViewById(R.id.textView);

        String cityName= editText.getText().toString();
        String content;

        Weather weather =new Weather();

        try {
            content= weather.execute("https://samples.openweathermap.org/data/2.5/weather?q="+cityName+"&appid=b6907d289e10d714a6e88b30761fae22").get();

            Log.i ("ContentData", content);

            //Json

            JSONObject isonObject = new JSONObject(content);
            String weatherData = isonObject.getString("weather");
            String temp= isonObject.getString("main");
            Log.i("weatherData",weatherData);

            JSONArray array= new JSONArray(weatherData);

            String main="";
            String description="";
            String tempreture="";
            for (int i=0; i<array.length();i++)
            {
                JSONObject weatherPart= array.getJSONObject(i);

                main=weatherPart.getString("main");
                description= weatherPart.getString("description");
            }

            JSONObject mainPart= new JSONObject(temp);
            tempreture= mainPart.getString("temp");

            Log.i("tempreture",tempreture);

            Log.i("main",main);
            Log.i("description",description);

            textView.setText("Main:"+main+ "\n"+"Description: "+ description + "\n"+"Tempreture:" +tempreture);
        } catch (Exception e){}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}
