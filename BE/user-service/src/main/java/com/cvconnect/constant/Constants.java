package com.cvconnect.constant;

public class Constants {
    public static class RoleCode {
        private RoleCode() {}
        public static final String CANDIDATE = "CANDIDATE";
        public static final String SYSTEM_ADMIN = "SYSTEM_ADMIN";
        public static final String ORG_ADMIN = "ORG_ADMIN";
        public static final String HR = "HR";
        public static final String INTERVIEWER = "INTERVIEWER";
    }

    public static class Pattern {
        private Pattern() {}
        public static final String USERNAME = "^[A-Za-z0-9@.]{8,40}$";
        public static final String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,20}$";
    }
}
