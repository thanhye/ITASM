package com.example.asm_final;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class SpendingReportsActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private TextView tvMonthlyReport, tvCategoryReport;
    private Button btnClose;
    private int currentUserId;
    
    private String[] categories = {"Food", "Transport", "Books", "Entertainment", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending_reports);
        
        // Get user ID from intent
        currentUserId = getIntent().getIntExtra("userId", 1);
        
        dbHelper = new DatabaseHelper(this);
        initializeViews();
        loadReports();
        setupListeners();
    }

    private void initializeViews() {
        tvMonthlyReport = findViewById(R.id.tvMonthlyReport);
        tvCategoryReport = findViewById(R.id.tvCategoryReport);
        btnClose = findViewById(R.id.btnClose);
    }

    private void loadReports() {
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        int currentYear = cal.get(Calendar.YEAR);
        
        loadMonthlyReport(currentMonth, currentYear);
        loadCategoryReport(currentMonth, currentYear);
    }

    private void loadMonthlyReport(int month, int year) {
        double totalExpenses = dbHelper.getTotalExpenses(currentUserId, month, year);
        double budget = dbHelper.getBudget(currentUserId, month, year);
        double remaining = budget - totalExpenses;
        double percentage = budget > 0 ? (totalExpenses / budget) * 100 : 0;
        
        String monthlyReport = String.format(
                "📊 Monthly Summary (%02d/%d)\n\n" +
                "💰 Budget: ₫%,.0f\n" +
                "💸 Total Spent: ₫%,.0f\n" +
                "💵 Remaining: ₫%,.0f\n" +
                "📈 Usage: %.1f%%\n\n" +
                "%s", 
                month, year, budget, totalExpenses, remaining, percentage,
                getUsageStatus(percentage)
        );
        
        tvMonthlyReport.setText(monthlyReport);
    }

    private void loadCategoryReport(int month, int year) {
        StringBuilder categoryReport = new StringBuilder("📋 Category Breakdown:\n\n");
        double totalExpenses = 0;
        
        for (String category : categories) {
            double categoryTotal = dbHelper.getTotalExpensesByCategory(currentUserId, category, month, year);
            if (categoryTotal > 0) {
                totalExpenses += categoryTotal;
                String emoji = getCategoryEmoji(category);
                categoryReport.append(String.format("%s %s: ₫%,.0f\n", emoji, category, categoryTotal));
            }
        }
        
        if (totalExpenses == 0) {
            categoryReport.append("No expenses recorded for this month.");
        } else {
            categoryReport.append("\n💯 Total: ₫").append(String.format("%,.0f", totalExpenses));
        }
        
        tvCategoryReport.setText(categoryReport.toString());
    }

    private String getUsageStatus(double percentage) {
        if (percentage <= 50) {
            return "✅ Great! You're within budget.";
        } else if (percentage <= 80) {
            return "⚠️ Be careful, you're approaching your limit.";
        } else if (percentage <= 100) {
            return "🚨 Warning! Almost at budget limit.";
        } else {
            return "❌ Over budget! Consider reducing expenses.";
        }
    }

    private String getCategoryEmoji(String category) {
        switch (category) {
            case "Food": return "🍽️";
            case "Transport": return "🚌";
            case "Books": return "📚";
            case "Entertainment": return "🎬";
            case "Other": return "📦";
            default: return "📝";
        }
    }

    private void setupListeners() {
        btnClose.setOnClickListener(v -> finish());
    }
}