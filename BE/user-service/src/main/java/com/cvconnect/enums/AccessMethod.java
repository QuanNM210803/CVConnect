package com.cvconnect.enums;

import com.cvconnect.dto.user.AccessMethodDto;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum AccessMethod {
    LOCAL("LOCAL"),
    GOOGLE("GOOGLE"),;

    private final String label;
    AccessMethod(String label) {
        this.label = label;
    }

    public static AccessMethod getAccessMethod(String name) {
        for (AccessMethod accessMethod : AccessMethod.values()) {
            if (accessMethod.name().equalsIgnoreCase(name)) {
                return accessMethod;
            }
        }
        return null;
    }

    public static AccessMethodDto getAccessMethodDto(AccessMethod accessMethod) {
        if (accessMethod == null) {
            return null;
        }
        return AccessMethodDto.builder()
                .name(accessMethod.name())
                .label(accessMethod.getLabel())
                .build();
    }

    public static AccessMethodDto getAccessMethodDto(String name) {
        AccessMethod accessMethod = getAccessMethod(name);
        return getAccessMethodDto(accessMethod);
    }

    public static List<AccessMethodDto> getAll() {
        return Arrays.stream(AccessMethod.values())
                .map(AccessMethod::getAccessMethodDto)
                .toList();
    }
}
