package com.cvconnect.constant;

public class Constants {
    public static class BeanName {
        public static final String EMAIL_EXECUTOR = "emailExecutor";
        public static final String TASK_SCHEDULER = "taskScheduler";
        public static final String ASYNC_CONFIG = "async_config";
    }

    public static class JobName {
        public static final String EMAIL_RESEND = "email_resend";
    }

    public static class SocketTopic {
        public static final String NOTIFY = "notify";
        public static final String UNREAD_NOTIFY = "unread_notify";
        public static final String NEW_CONVERSATION = "new_conversation";
        public static final String READ_ALL_MESSAGES = "read_all_messages";
        public static final String RECEIVE_MESSAGE = "receive_message";
        public static final String NEW_MESSAGE = "new_message";
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
