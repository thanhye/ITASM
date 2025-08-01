package com.example.asm_final;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private BudgetManager budgetManager;
    private ExpenseManager expenseManager;
    private ReportManager reportManager;
    
    private int currentUserId = 1; // Default to test user
    
    // UI Components
    private TextView tvBudgetInfo, tvTotalExpenses;
    private Button btnSetBudget, btnViewReports, btnAddExpense, btnSortExpenses;
    private ListView lvExpenses;
    private Spinner spinnerCategory, spinnerSortBy;
    
    private String[] categories = {"All", "Food", "Transport", "Books", "Entertainment", "Other"};
    private String[] sortOptions = {"Date (Newest)", "Date (Oldest)", "Amount (High to Low)", "Amount (Low to High)"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        initializeManagers();
        initializeViews();
        setupSpinners();
        setupListeners();
        loadData();
    }

    private void initializeManagers() {
        dbHelper = new DatabaseHelper(this);
        budgetManager = new BudgetManager(this, currentUserId);
        expenseManager = new ExpenseManager(this, currentUserId);
        reportManager = new ReportManager(this, currentUserId);
    }

    private void initializeViews() {
        tvBudgetInfo = findViewById(R.id.tvBudgetInfo);
        tvTotalExpenses = findViewById(R.id.tvTotalExpenses);
        btnSetBudget = findViewById(R.id.btnSetBudget);
        btnViewReports = findViewById(R.id.btnViewReports);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        btnSortExpenses = findViewById(R.id.btnSortExpenses);
        lvExpenses = findViewById(R.id.lvExpenses);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSortBy = findViewById(R.id.spinnerSortBy);
    }

    private void setupSpinners() {
        // Setup category spinner
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Setup sort options spinner
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sortOptions);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortBy.setAdapter(sortAdapter);
    }

    private void setupListeners() {
        btnSetBudget.setOnClickListener(v -> openSetBudgetActivity());
        btnViewReports.setOnClickListener(v -> openSpendingReportsActivity());
        btnAddExpense.setOnClickListener(v -> openAddExpenseActivity());
        btnSortExpenses.setOnClickListener(v -> sortExpenses());
    }

    private void openSetBudgetActivity() {
        Intent intent = new Intent(this, SetBudgetActivity.class);
        intent.putExtra("userId", currentUserId);
        startActivityForResult(intent, 1001);
    }

    private void openAddExpenseActivity() {
        Intent intent = new Intent(this, AddExpenseActivity.class);
        intent.putExtra("userId", currentUserId);
        startActivityForResult(intent, 1002);
    }

    private void openSpendingReportsActivity() {
        Intent intent = new Intent(this, SpendingReportsActivity.class);
        intent.putExtra("userId", currentUserId);
        startActivity(intent);
    }

    public void loadData() {
        updateBudgetInfo();
        loadExpenses();
    }

    public void updateBudgetInfo() {
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        int currentYear = cal.get(Calendar.YEAR);
        
        double budget = budgetManager.getBudget(currentMonth, currentYear);
        double totalExpenses = budgetManager.getTotalExpenses(currentMonth, currentYear);
        double remaining = budget - totalExpenses;
        double percentage = budget > 0 ? (totalExpenses / budget) * 100 : 0;
        
        String budgetInfo = String.format("Budget: ₫%,.0f\nSpent: ₫%,.0f\nRemaining: ₫%,.0f\nUsage: %.1f%%", 
                budget, totalExpenses, remaining, percentage);
        
        tvBudgetInfo.setText(budgetInfo);
        tvTotalExpenses.setText(String.format("Total Expenses: ₫%,.0f", totalExpenses));
    }

    private void loadExpenses() {
        expenseManager.loadExpenses(lvExpenses);
    }

    private void sortExpenses() {
        String sortBy = spinnerSortBy.getSelectedItem().toString();
        String selectedCategory = spinnerCategory.getSelectedItem().toString();
        expenseManager.sortExpenses(lvExpenses, sortBy, selectedCategory);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Refresh data when returning from any activity
            loadData();
        }
    }
}