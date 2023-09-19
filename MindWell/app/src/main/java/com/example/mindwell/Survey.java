package com.example.mindwell;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Survey extends AppCompatActivity {

    TextView question,questionNumberText;
    RadioGroup options;
    Button submit;
    String question_text = "",jsonFile,currentSelectedOption;
    int totalOptions,currentOptionIndex,questionNumber=0,currentScore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        questionNumberText = findViewById(R.id.currentQuestionText);
        question = findViewById(R.id.questionTextView);
        options = findViewById(R.id.optionsRadioGroup);
        submit = findViewById(R.id.submitButton);

        ReadJsonFile();
        getCurrentQuestion(questionNumber);

        options.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = findViewById(checkedId);
            radioButton.setTextColor(Color.WHITE);
            radioButton.setBackgroundResource(R.drawable.option_button);

            currentSelectedOption = radioButton.getText().toString();
            currentOptionIndex = checkedId;
        });

        final RadioButton[] previousCheckedRadioButton = {null}; // Initialize as null initially

        options.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton newCheckedRadioButton = findViewById(checkedId);

            if (previousCheckedRadioButton[0] != null) {
                // A previously checked RadioButton exists
                if (previousCheckedRadioButton[0] != newCheckedRadioButton) {
                    // The new RadioButton is different from the previously checked one,
                    // so the previous one is being unchecked.
                    previousCheckedRadioButton[0].setTextColor(Color.BLACK);
                    previousCheckedRadioButton[0].setBackgroundResource(R.drawable.stroke_background);
                }
            }

            // Update the reference to the newly checked RadioButton
            previousCheckedRadioButton[0] = newCheckedRadioButton;

            // Perform actions for the newly checked RadioButton
            newCheckedRadioButton.setTextColor(Color.WHITE);
            newCheckedRadioButton.setBackgroundResource(R.drawable.option_button);

            currentSelectedOption = newCheckedRadioButton.getText().toString();
            currentOptionIndex = checkedId;
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(currentSelectedOption))
                {
                    Toast.makeText(Survey.this, "please Select Option", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    currentScore += getCurrentScore(questionNumber,currentOptionIndex);
                    Toast.makeText(Survey.this, "" + currentScore, Toast.LENGTH_SHORT).show();
                    questionNumber++;
                    getCurrentQuestion(questionNumber);
                }
            }
        });
    }

    int getCurrentScore(int questionIndex,int optionIndex) {
        int score=0;
        try {
            JSONArray jsonArray = new JSONArray(jsonFile);
            JSONObject questionObject = jsonArray.getJSONObject(questionIndex);
            JSONObject optionObject = questionObject.getJSONObject("option"+optionIndex);
            score = optionObject.getInt("score");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return score;
    }

    void  getCurrentQuestion(int index)
    {
        try {
            questionNumberText.setText("Q"+(index+1));
            // If your JSON contains an array
            currentSelectedOption=null;
            options.removeAllViews();
            JSONArray jsonArray = new JSONArray(jsonFile);
            JSONObject questionObject = jsonArray.getJSONObject(index);
            question.setText(questionObject.getString("question"));
            totalOptions = questionObject.getInt("totalOptions");

            for (int i=1;i<=totalOptions;i++)
            {
                RadioButton radioButton = new RadioButton(this);
                JSONObject option1Object = questionObject.getJSONObject("option"+i);
                radioButton.setText(option1Object.getString("text"));
                radioButton.setTextColor(Color.BLACK);
                radioButton.setBackgroundResource(R.drawable.stroke_background);
                radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                radioButton.setPaddingRelative(20,0,0,0);
                radioButton.setTypeface(null, Typeface.BOLD);
                radioButton.setButtonDrawable(android.R.color.transparent);
                radioButton.setTextSize(18);

                int marginTopInDp = 18;
                float scale = getResources().getDisplayMetrics().density;
                int marginTopInPixels = (int) (marginTopInDp * scale + 0.5f);
                int desiredHeightInDp = 40;
                int desiredHeightInPixels = (int) (desiredHeightInDp * scale + 0.5f);
                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                        RadioGroup.LayoutParams.MATCH_PARENT,
                        desiredHeightInPixels
                );
                layoutParams.setMargins(0, marginTopInPixels, 0, 0);
                radioButton.setLayoutParams(layoutParams);
                radioButton.setId(i);
                options.addView(radioButton);
            }

            // Access individual options
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void ReadJsonFile()
    {
        try {
            // Initialize the AssetManager
            AssetManager assetManager = getApplicationContext().getAssets();

            // Open the JSON file using InputStream
            InputStream inputStream = assetManager.open("question.json");

            // Read the JSON data into a string
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonFile = new String(buffer, StandardCharsets.UTF_8);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}