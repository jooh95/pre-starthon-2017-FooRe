package com.test.foore.foore2;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.JarException;

public class StartActivity extends Activity implements View.OnClickListener{

    AutoCompleteTextView searchFood;
    RadioGroup selectedCountry;
    ImageButton btnSearch;
    Spinner foodCountry;
    String foodCountryName;
    OutputStream os = null;
    String country2;
    String englishName;
    String foodName;
    String taste;
    String ingridient;
    InputStream is   = null;
    ByteArrayOutputStream baos = null;
    String response;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        setTitle("Foreign Food Recommender through Food Similarity Algorithm");

        setId();
        setAutoText();
        //foodCountryName = "KOREA";
        englishName = new String();
        response = new String();
        taste = new String();
        foodName = new String();

        foodCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                foodCountryName = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        country2 = new String();
        btnSearch.setOnClickListener(this);


    }


    void setAutoText(){
        String[] items = {"김치찌개", "미역국", "갈비찜", "잔치국수", "찜닭"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,items);
        searchFood.setAdapter(adapter);
    }

    void setId(){
        btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        searchFood = (AutoCompleteTextView) findViewById(R.id.searchFood);
        selectedCountry = (RadioGroup) findViewById(R.id.country);
        foodCountry = (Spinner) findViewById(R.id.foodCountry);
    }

    @Override
    public void onClick(View v) {
        //Toast.makeText(getApplicationContext(),"클릭",Toast.LENGTH_SHORT).show();
       // Toast.makeText(getApplicationContext(),foodCountryName ,Toast.LENGTH_SHORT).show();

        //서버에게 요청

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // All your networking logic
                // should be here
                try {
                    URL dbServer = new URL("http://192.168.43.129:8008/search");
                    // Create connection
                    HttpURLConnection myConnection = (HttpURLConnection) dbServer.openConnection();
                    myConnection.setRequestMethod("POST");

                    myConnection.setDoOutput(true);
                    myConnection.setDoInput(true);


                    JSONObject job = new JSONObject();

                    job.put("COUNTRY", foodCountryName);
                    job.put("FOODNAME", searchFood.getText().toString());

                    switch (selectedCountry.getCheckedRadioButtonId()) {
                        case R.id.korea:
                            break;
                        case R.id.china:
                            country2 = "CHINA";
                            break;
                        case R.id.japan:
                            break;
                    }
                    job.put("COUNTRY2", country2);

                    os = myConnection.getOutputStream();
                    os.write(job.toString().getBytes());
                    os.flush();


                    int responseCode = myConnection.getResponseCode();

                    if(responseCode == HttpURLConnection.HTTP_OK) {

                        is = myConnection.getInputStream();
                        baos = new ByteArrayOutputStream();
                        byte[] byteBuffer = new byte[1024];
                        byte[] byteData = null;
                        int nLength = 0;
                        while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                            baos.write(byteBuffer, 0, nLength);
                        }
                        byteData = baos.toByteArray();

                        response = new String(byteData);

                        JSONObject responseJSON = new JSONObject();
                        JSONArray jsonArray = new JSONArray(response);
                        JSONArray jsonArray1 = new JSONArray();
                        responseJSON = jsonArray.getJSONObject(0);
                        englishName = responseJSON.getString("ENGLISH");
                        foodName = responseJSON.getString("FOODNAME");
                        taste = responseJSON.getString("TASTE");
                        ingridient = responseJSON.getString("INGRIDIENTS");




                       // Toast.makeText(getApplicationContext(),englishName,Toast.LENGTH_LONG).show();
                       //Toast.makeText(getApplicationContext(),ingridient,Toast.LENGTH_LONG).show();

                       Log.i("tag","DATA response = " + response);
                    }
                }catch (MalformedURLException e){


                }
                catch(IOException e) {

                }
                catch (JSONException e){

                }

            }
        });

        try {
            Thread.sleep(1500);
        }
        catch (InterruptedException e){

        }

        //Toast.makeText(getApplicationContext(),englishName,Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(),ingridient,Toast.LENGTH_LONG).show();
        Log.i("tag","DATA response = " + response);
        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
        intent.putExtra("searchFood", searchFood.getText().toString());
        intent.putExtra("searchFoodCountry",foodCountryName);
        intent.putExtra("englishName", englishName);
        intent.putExtra("taste", taste);
        intent.putExtra("foodName", foodName);

        startActivityForResult(intent,0);
    }
}
//json[0].FOODNAME