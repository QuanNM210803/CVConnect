package com.cvconnect.enums;

import lombok.Getter;

@Getter
public enum InviteJoinStatus {
    PENDING("Đang chờ"),
    ACCEPTED("Đồng ý"),
    REJECTED("Từ chối");

    private final String description;

    InviteJoinStatus(String description) {
        this.description = description;
    }

    public static InviteJoinStatus getInviteJoinStatus(String name) {
        for (InviteJoinStatus inviteJoinStatus : InviteJoinStatus.values()) {
            if (inviteJoinStatus.name().equalsIgnoreCase(name)) {
                return inviteJoinStatus;
            }
        }
        return null;
    }
}
