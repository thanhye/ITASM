package com.example.asm_final;

import android.content.Context;

public class BudgetManager {
    private DatabaseHelper dbHelper;
    private Context context;
    private int currentUserId;

    public BudgetManager(Context context, int currentUserId) {
        this.context = context;
        this.currentUserId = currentUserId;
        this.dbHelper = new DatabaseHelper(context);
    }



    public double getBudget(int month, int year) {
        return dbHelper.getBudget(currentUserId, month, year);
    }

    public double getTotalExpenses(int month, int year) {
        return dbHelper.getTotalExpenses(currentUserId, month, year);
    }
} 