package com.test.foore.foore2;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ResultActivity extends Activity {

    TextView foodNameText;
    String foodName;
    String englishName;
    String taste;
    TextView tasteText;
    ImageView foodImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        foodImage = (ImageView) findViewById(R.id.imageView);
        foodNameText = (TextView) findViewById(R.id.foodName);
        tasteText = (TextView) findViewById(R.id.flavor);
        foodName = new String();
        englishName = new String();
        taste = new String();


        final String searchName = intent.getStringExtra("searchFood");
        foodName = intent.getStringExtra("foodName");
        englishName = intent.getStringExtra("englishName");
        taste = intent.getStringExtra("taste");


        foodNameText.setText(foodName + '[' + englishName + ']');

        tasteText.setText("Flavor: " + taste);

        switch (foodName){
            case "海带汤":
                foodImage.setImageResource(R.drawable.haidaitang);
                break;
            case "酱排骨 ":
                foodImage.setImageResource(R.drawable.jianggupaigu);
                break;
            case "兰州拉面":
                foodImage.setImageResource(R.drawable.lanzhoulamian);
                break;
            case "红烧排骨":
                foodImage.setImageResource(R.drawable.hongshaopaigu);
                break;

        }







    }
}