package com.cvconnect.enums;

import lombok.Getter;

@Getter
public enum TemplateExport {
    USER_EXPORT_TEMPLATE("Người dùng hệ thống.xlsx", "export/user-export-template.xlsx")
    ;

    private final String fileName;
    private final String path;

    TemplateExport(String fileName, String path) {
        this.fileName = fileName;
        this.path = path;
    }
}
