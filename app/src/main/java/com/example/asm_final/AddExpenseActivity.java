package com.example.asm_final;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText etTitle, etAmount, etDescription;
    private Spinner spinnerCategory;
    private Button btnDate, btnAdd, btnCancel;
    private int currentUserId;
    private String selectedDate;
    
    private String[] categories = {"Food", "Transport", "Books", "Entertainment", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        
        // Get user ID from intent
        currentUserId = getIntent().getIntExtra("userId", 1);
        
        dbHelper = new DatabaseHelper(this);
        initializeViews();
        setupDefaults();
        setupListeners();
    }

    private void initializeViews() {
        etTitle = findViewById(R.id.etExpenseTitle);
        etAmount = findViewById(R.id.etExpenseAmount);
        etDescription = findViewById(R.id.etExpenseDescription);
        spinnerCategory = findViewById(R.id.spinnerExpenseCategory);
        btnDate = findViewById(R.id.btnExpenseDate);
        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void setupDefaults() {
        // Setup category spinner
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);
        
        // Set default date
        selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        btnDate.setText("📅 " + selectedDate);
    }

    private void setupListeners() {
        btnDate.setOnClickListener(v -> showDatePicker());
        btnAdd.setOnClickListener(v -> addExpense());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    btnDate.setText("📅 " + selectedDate);
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void addExpense() {
        try {
            String title = etTitle.getText().toString().trim();
            String amountStr = etAmount.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString();
            String description = etDescription.getText().toString().trim();
            
            if (title.isEmpty()) {
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (amountStr.isEmpty()) {
                Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show();
                return;
            }
            
            double amount = Double.parseDouble(amountStr);
            
            if (amount <= 0) {
                Toast.makeText(this, "Amount must be greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (dbHelper.addExpense(currentUserId, title, amount, category, selectedDate, description)) {
                Toast.makeText(this, "Expense added successfully!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Failed to add expense", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
        }
    }
}