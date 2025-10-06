package com.cvconnect.enums;

import com.cvconnect.dto.enums.CurrencyTypeDto;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum CurrencyType {
    VND("Việt Nam Đồng"),
    USD("Đô la Mỹ"),
    EUR("Euro"),
    JPY("Yên Nhật"),
    GBP("Bảng Anh"),
    AUD("Đô la Úc"),
    CHF("Franc Thụy Sĩ"),
    CNY("Nhân dân tệ Trung Quốc"),
    INR("Rupi Ấn Độ"),
    SGD("Đô la Singapore"),;

    private final String description;

    CurrencyType(String description) {
        this.description = description;
    }

    public static CurrencyType getCurrencyType(String name) {
        for (CurrencyType currencyType : CurrencyType.values()) {
            if (currencyType.name().equalsIgnoreCase(name)) {
                return currencyType;
            }
        }
        return null;
    }

    public static CurrencyTypeDto getCurrencyTypeDto(CurrencyType currencyType) {
        if (currencyType == null) {
            return null;
        }
        return CurrencyTypeDto.builder()
                .name(currencyType.name())
                .description(currencyType.getDescription())
                .build();
    }

    public static CurrencyTypeDto getCurrencyTypeDto(String name) {
        CurrencyType currencyType = getCurrencyType(name);
        return getCurrencyTypeDto(currencyType);
    }

    public static List<CurrencyTypeDto> getAll() {
        return Arrays.stream(CurrencyType.values())
                .map(CurrencyType::getCurrencyTypeDto)
                .toList();
    }
}
