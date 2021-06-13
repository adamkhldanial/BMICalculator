package com.example.bmicalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import static com.example.bmicalculator.BMICalcUtil.BMI_CATEGORY_HEALTHY;
import static com.example.bmicalculator.BMICalcUtil.BMI_CATEGORY_MODERATE;
import static com.example.bmicalculator.BMICalcUtil.BMI_CATEGORY_OBESE;
import static com.example.bmicalculator.BMICalcUtil.BMI_CATEGORY_OVERWEIGHT;
import static com.example.bmicalculator.BMICalcUtil.BMI_CATEGORY_SEVERELY;
import static com.example.bmicalculator.BMICalcUtil.BMI_CATEGORY_UNDERWEIGHT;

public class MainActivity extends AppCompatActivity {

    private EditText weightKgEditText, heightCmEditText;
    private EditText weightLbsEditText, heightFtEditText, heightInEditText;
    private Button calculateButton;
    private TextView bmiTextView, categoryTextView, riskTextView;
    private ToggleButton toggleUnitsButton;
    private CardView bmiResultCardView;
    private boolean inMetricUnits;
    private Button btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weightKgEditText = findViewById(R.id.activity_main_weightkgs);
        heightCmEditText = findViewById(R.id.activity_main_heightcm);

        weightLbsEditText = findViewById(R.id.activity_main_weightlbs);
        heightFtEditText = findViewById(R.id.activity_main_heightfeet);
        heightInEditText = findViewById(R.id.activity_main_heightinches);

        calculateButton = findViewById(R.id.activity_main_calculate);
        toggleUnitsButton = findViewById(R.id.activity_main_toggleunits);

        bmiTextView = findViewById(R.id.activity_main_bmi);
        categoryTextView = findViewById(R.id.activity_main_category);
        riskTextView = findViewById(R.id.activity_main_risk);
        bmiResultCardView = findViewById(R.id.activity_main_resultcard);

        inMetricUnits = true;
        updateInputsVisibility();
        bmiResultCardView.setVisibility(View.GONE);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inMetricUnits) {
                    if (weightKgEditText.length() == 0 || heightCmEditText.length() == 0) {
                        Toast.makeText(MainActivity.this, "Populate Weight and Height to Calculate BMI", Toast.LENGTH_SHORT).show();
                    } else {
                        double heightInCms = Double.parseDouble(heightCmEditText.getText().toString());
                        double weightInKgs = Double.parseDouble(weightKgEditText.getText().toString());
                        double bmi = BMICalcUtil.getInstance().calculateBMIMetric(heightInCms, weightInKgs);
                        displayBMI(bmi);
                    }
                } else {
                    if (weightLbsEditText.length() == 0 || heightFtEditText.length() == 0 || heightInEditText.length() == 0) {
                        Toast.makeText(MainActivity.this, "Populate Weight and Height to Calculate BMI", Toast.LENGTH_SHORT).show();
                    } else {
                        double heightFeet = Double.parseDouble(heightFtEditText.getText().toString());
                        double heightInches = Double.parseDouble(heightInEditText.getText().toString());
                        double weightLbs = Double.parseDouble(weightLbsEditText.getText().toString());
                        double bmi = BMICalcUtil.getInstance().calculateBMIImperial(heightFeet, heightInches, weightLbs);
                        displayBMI(bmi);
                    }
                }

                weightKgEditText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                heightCmEditText.onEditorAction(EditorInfo.IME_ACTION_DONE);

                weightLbsEditText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                heightFtEditText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                heightInEditText.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });

        toggleUnitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inMetricUnits = !inMetricUnits;
                updateInputsVisibility();
            }
        });

        btnClear=findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                weightKgEditText.getText().clear();
                heightCmEditText.getText().clear();

                weightLbsEditText.getText().clear();
                heightFtEditText.getText().clear();
                heightInEditText.getText().clear();

                bmiResultCardView.setVisibility(View.GONE);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Intent intent = new Intent(this, About.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateInputsVisibility() {
        if (inMetricUnits) {
            heightCmEditText.setVisibility(View.VISIBLE);
            weightKgEditText.setVisibility(View.VISIBLE);
            heightFtEditText.setVisibility(View.GONE);
            heightInEditText.setVisibility(View.GONE);
            weightLbsEditText.setVisibility(View.GONE);
        } else {
            heightCmEditText.setVisibility(View.GONE);
            weightKgEditText.setVisibility(View.GONE);
            heightFtEditText.setVisibility(View.VISIBLE);
            heightInEditText.setVisibility(View.VISIBLE);
            weightLbsEditText.setVisibility(View.VISIBLE);
        }
    }

    private void displayBMI(double bmi) {
        bmiResultCardView.setVisibility(View.VISIBLE);

        bmiTextView.setText(String.format("%.2f", bmi));

        String bmiCategory = BMICalcUtil.getInstance().classifyBMI(bmi);
        categoryTextView.setText(bmiCategory);

        switch (bmiCategory) {
            case BMI_CATEGORY_UNDERWEIGHT:
                bmiResultCardView.setCardBackgroundColor(Color.YELLOW);
                riskTextView.setText("Malnutrition Risk");
                break;
            case BMI_CATEGORY_HEALTHY:
                bmiResultCardView.setCardBackgroundColor(Color.GREEN);
                riskTextView.setText("Low Risk");
                break;
            case BMI_CATEGORY_OVERWEIGHT:
                bmiResultCardView.setCardBackgroundColor(Color.LTGRAY);
                riskTextView.setText("Enhanced Risk");
                break;
            case BMI_CATEGORY_MODERATE:
                bmiResultCardView.setCardBackgroundColor(Color.GRAY);
                riskTextView.setText("Medium Risk");
                break;
            case BMI_CATEGORY_SEVERELY:
                bmiResultCardView.setCardBackgroundColor(Color.DKGRAY);
                riskTextView.setText("High Risk");
                break;
            case BMI_CATEGORY_OBESE:
                bmiResultCardView.setCardBackgroundColor(Color.RED);
                riskTextView.setText("Very High Risk");
                break;
        }
    }
}