package com.example.AutomatizationQA.Playwright;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestDTO {
    private String url;
    private String username;
    private String password;
    private String product;
    private boolean phoneNumberEnabled;
    private boolean clerkIdEnabled;
    private String phoneNumber;
    private String amount;
    private String clerkId;

}
