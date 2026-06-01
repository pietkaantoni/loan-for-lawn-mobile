package com.example.loan_for_lawn_mobile.utils;

import java.util.Calendar;

public class LoanCalculator {

    public static double calculateMonthlyPayment(double principal, double annualRate, int months) {
        double monthlyRate = annualRate / 100.0 / 12.0;
        if (monthlyRate == 0) {
            return principal / months;
        }
        double factor = Math.pow(1 + monthlyRate, months);
        return (principal * monthlyRate * factor) / (factor - 1);
    }

    public static double calculateTotalPayment(double monthlyPayment, int months) {
        return monthlyPayment * months;
    }

    public static String calculateDueDate(int months) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, months);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return String.format("%04d-%02d-%02d", year, month, day);
    }
}
