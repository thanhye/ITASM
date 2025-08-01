package com.example.asm_final;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.widget.TextView;
import java.util.Calendar;

public class ReportManager {
    private DatabaseHelper dbHelper;
    private Context context;
    private int currentUserId;
    private String[] categories = {"Food", "Transport", "Books", "Entertainment", "Other"};

    public ReportManager(Context context, int currentUserId) {
        this.context = context;
        this.currentUserId = currentUserId;
        this.dbHelper = new DatabaseHelper(context);
    }

    // Dialog method removed - now using SpendingReportsActivity instead
    // public void showSpendingReports() { ... }

    // Helper methods for safe cursor access (if needed in future)
    private String getStringFromCursor(Cursor cursor, String columnName, String defaultValue) {
        try {
            int columnIndex = cursor.getColumnIndex(columnName);
            if (columnIndex >= 0) {
                return cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            // Log error if needed
        }
        return defaultValue;
    }

    private double getDoubleFromCursor(Cursor cursor, String columnName, double defaultValue) {
        try {
            int columnIndex = cursor.getColumnIndex(columnName);
            if (columnIndex >= 0) {
                return cursor.getDouble(columnIndex);
            }
        } catch (Exception e) {
            // Log error if needed
        }
        return defaultValue;
    }

    private int getIntFromCursor(Cursor cursor, String columnName, int defaultValue) {
        try {
            int columnIndex = cursor.getColumnIndex(columnName);
            if (columnIndex >= 0) {
                return cursor.getInt(columnIndex);
            }
        } catch (Exception e) {
            // Log error if needed
        }
        return defaultValue;
    }
} 