package com.example.AutomatizationQA.Playwright;

import lombok.Getter;

@Getter
public enum ProductFlowType {

    TOP_UP("TOP UP"),
    WIRELESS("WIRELESS"),
    BILL_PAYMENT("BILL PAYMENT"),
    CRYPTO("CRYPTO"),
    ACTIVATION("ACTIVATION"),
    PORT_IN("PORT IN"),
    TOOL("TOOL");

    private final String displayName;

    ProductFlowType(String displayName) {
        this.displayName = displayName;
    }

}
