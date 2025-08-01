package com.example.asm_final;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class SetBudgetActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText etAmount, etMonth, etYear;
    private Button btnSet, btnCancel;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_budget);
        
        // Get user ID from intent
        currentUserId = getIntent().getIntExtra("userId", 1);
        
        dbHelper = new DatabaseHelper(this);
        initializeViews();
        setupDefaultValues();
        setupListeners();
    }

    private void initializeViews() {
        etAmount = findViewById(R.id.etAmount);
        etMonth = findViewById(R.id.etMonth);
        etYear = findViewById(R.id.etYear);
        btnSet = findViewById(R.id.btnSet);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void setupDefaultValues() {
        // Set current month and year
        Calendar cal = Calendar.getInstance();
        etMonth.setText(String.valueOf(cal.get(Calendar.MONTH) + 1));
        etYear.setText(String.valueOf(cal.get(Calendar.YEAR)));
    }

    private void setupListeners() {
        btnSet.setOnClickListener(v -> setBudget());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void setBudget() {
        try {
            double amount = Double.parseDouble(etAmount.getText().toString());
            int month = Integer.parseInt(etMonth.getText().toString());
            int year = Integer.parseInt(etYear.getText().toString());
            
            if (dbHelper.setBudget(currentUserId, amount, month, year)) {
                Toast.makeText(this, "Budget set successfully!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Failed to set budget", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }
}