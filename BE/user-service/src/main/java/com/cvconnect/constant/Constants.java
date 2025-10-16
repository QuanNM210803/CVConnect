package com.cvconnect.constant;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class Constants {

    @Value("${frontend.url}")
    public static String FRONTEND_URL;

    public static class RoleCode {
        private RoleCode() {}
        public static final String CANDIDATE = "CANDIDATE";
        public static final String SYSTEM_ADMIN = "SYSTEM_ADMIN";
        public static final String ORG_ADMIN = "ORG_ADMIN";
        public static final String HR = "HR";
        public static final String INTERVIEWER = "INTERVIEWER";

        @SneakyThrows
        public static List<String> getAllRoleCodes() {
            List<String> roles = new ArrayList<>();
            for (Field field : RoleCode.class.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()) && field.getType().equals(String.class)) {
                        roles.add((String) field.get(null));
                }
            }
            return roles;
        }
    }

    public static class Pattern {
        private Pattern() {}
        public static final String USERNAME = "^[A-Za-z0-9@.]{8,40}$";
        public static final String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]{8,20}$";
    }

    public static class NotificationType {
        private NotificationType() {}
        public static final String USER = "USER";
        public static final String SYSTEM = "SYSTEM";
    }

    public static class Path {
        private Path() {}
        public static final String FRONTEND_URL = Constants.FRONTEND_URL;
        public static final String VERIFY_EMAIL = FRONTEND_URL + "/account/verify-email";
        public static final String RESET_PASSWORD = FRONTEND_URL + "/account/recovery";
        public static final String INVITE_JOIN_ORG = FRONTEND_URL + "/invite-join-org";
        public static final String LOGIN_ERROR = FRONTEND_URL + "/login?error=true";
        public static final String ORG_MEMBER = FRONTEND_URL + "/org-admin/org-member";
    }

    public static class KafkaTopic {
        private KafkaTopic() {}
        public static final String NOTIFICATION = "notification-topic";
    }
}
