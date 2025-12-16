package com.cvconnect.constant;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class Constants {

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
        public static final String PHONE_NUMBER = "^0\\d{9}$";
    }

    public static class NotificationType {
        private NotificationType() {}
        public static final String USER = "USER";
        public static final String SYSTEM = "SYSTEM";
    }

    public static class Path {
        private Path() {}
        public static final String HOME = "/";
        public static final String HOME_ORG = "/org-info";
        public static final String VERIFY_EMAIL = "/account/verify-email";
        public static final String RESET_PASSWORD = "/account/recovery";
        public static final String INVITE_JOIN_ORG = "/invite-join-org";
        public static final String LOGIN_ERROR = "/auth/login?error=true";
        public static final String ORG_MEMBER = "/org-admin/org-member";
        public static final String LOGIN_SUCCESS_METHOD = "?method=google";
        public static final String ORG_LIST = "/system-admin/organizations";
    }

    public static class KafkaTopic {
        private KafkaTopic() {}
        public static final String NOTIFICATION = "notification-topic";
    }

    public static class BeanName {
        public static final String TASK_SCHEDULER = "taskScheduler";
    }

    public static class JobName {
        public static final String FAILED_ROLLBACK_RETRY = "failed_rollback_retry";
    }
}
