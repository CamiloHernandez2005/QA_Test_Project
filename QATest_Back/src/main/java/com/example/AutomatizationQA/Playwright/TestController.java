package com.example.AutomatizationQA.Playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/tests")
public class TestController {

    @PostMapping("/pct")
    public ResponseEntity<String> runRtrTest(@RequestBody TestDTO request) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();

            page.navigate(request.getUrl());

            page.fill("input[name=\"username\"]", request.getUsername());
            page.fill("input[name=\"password\"]", request.getPassword());
            page.click("button[type=\"submit\"]");

            page.waitForSelector("text=My Dashboard");

            page.click("a.icon.wb-search[role=\"button\"]");

            page.fill("input[placeholder='Search Products']", request.getProduct());
            page.click("#autocomplete-result-0");

            Locator modal = page.locator(".modal-body:visible").filter(new Locator.FilterOptions().setHasText(""));
            modal.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

            if (request.isPhoneNumberEnabled()) {
                Locator phoneNumber = modal.locator("input[name=\"phoneNumber\"]");
                if (phoneNumber.isVisible()) {
                    phoneNumber.focus();
                    page.waitForTimeout(100);
                    phoneNumber.fill(request.getPhoneNumber());
                }
                Locator phoneNumberConfirm = modal.locator("input[name=\"phoneNumberConfirm\"]");
                if (phoneNumberConfirm.isVisible()) {
                    phoneNumberConfirm.focus();
                    page.waitForTimeout(100);
                    phoneNumberConfirm.fill(request.getPhoneNumber());
                }
            }
            if (request.isPhoneNumberEnabled()) {
                Locator amount = modal.locator("input[name=\"amount\"]");
                if (amount.isVisible()) {
                    amount.focus();
                    page.waitForTimeout(100);
                    amount.fill(request.getAmount());
                }
            }
            if (request.isClerkIdEnabled()) {
                Locator clerkId = modal.locator("input[name=\"clerkId\"]");
                if (clerkId.isVisible()) {
                    clerkId.focus();
                    page.waitForCondition(() -> {
                        String readonlyAttr = clerkId.getAttribute("readonly");
                        return readonlyAttr == null;
                    }, new Page.WaitForConditionOptions().setTimeout(2000));

                    clerkId.fill(request.getClerkId());
                }
            }
            Locator purchaseBtn = modal.locator("a", new Locator.LocatorOptions().setHasText("Purchase"));
            purchaseBtn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            purchaseBtn.click();

            Locator pagePrintLocator = modal.locator(".page-print");
            pagePrintLocator.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

            List<String> texts = pagePrintLocator.allInnerTexts();
            List<String> result_transaction = texts.stream().distinct().toList();

            for (String texto : result_transaction) {
                System.out.println("Resultado transacción: " + texto);
            }

            Locator finish = modal.locator("a", new Locator.LocatorOptions().setHasText("Finish"));
            finish.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            finish.click();

            return ResponseEntity.ok("Transacción completada: " + result_transaction);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error durante ejecución: " + e.getMessage());
        }
    }
}
