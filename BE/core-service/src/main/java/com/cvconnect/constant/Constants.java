package com.cvconnect.constant;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final int MAX_INDUSTRY_PER_ORG = 5;

    public static class RoleCode {
        private RoleCode() {}
        public static final String CANDIDATE = "CANDIDATE";
        public static final String SYSTEM_ADMIN = "SYSTEM_ADMIN";
        public static final String ORG_ADMIN = "ORG_ADMIN";
        public static final String HR = "HR";
        public static final String INTERVIEWER = "INTERVIEWER";
        public static String ANONYMOUS = "anonymous";

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

    public static class NotificationType {
        private NotificationType() {}
        public static final String USER = "USER";
        public static final String SYSTEM = "SYSTEM";
    }

    public static class KafkaTopic {
        private KafkaTopic() {}
        public static final String NOTIFICATION = "notification-topic";
    }
}
