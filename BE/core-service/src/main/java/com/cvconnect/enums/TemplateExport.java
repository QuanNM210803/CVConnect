package com.cvconnect.enums;

import lombok.Getter;

@Getter
public enum TemplateExport {
    ORG_EXPORT_TEMPLATE("Danh sách doanh nghiệp.xlsx", "export/org-export-template.xlsx")
    ;

    private final String fileName;
    private final String path;

    TemplateExport(String fileName, String path) {
        this.fileName = fileName;
        this.path = path;
    }
}
