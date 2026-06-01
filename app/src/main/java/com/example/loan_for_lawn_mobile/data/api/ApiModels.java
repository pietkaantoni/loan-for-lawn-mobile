package com.example.loan_for_lawn_mobile.data.api;

import java.util.List;

public class ApiModels {

    public static class NbpResponse {
        public String table;
        public String no;
        public String effectiveDate;
        public List<NbpRate> rates;
    }

    public static class NbpRate {
        public String currency;
        public String code;
        public double mid;
    }
}
