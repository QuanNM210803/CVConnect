package com.cvconnect.constant;

public class Constants {
    public static class BeanName {
        public static final String EMAIL_EXECUTOR = "emailExecutor";
        public static final String TASK_SCHEDULER = "taskScheduler";
        public static final String ASYNC_CONFIG = "asyncConfig";
    }

    public static class JobName {
        public static final String EMAIL_RESEND = "email_resend";
    }

    public static class SocketTopic {
        public static final String NOTIFY = "notify";
        public static final String UNREAD_NOTIFY = "unread_notify";
        public static final String UNREAD_NOTIFY_EVENT = "unread_notify_event";
    }
}
