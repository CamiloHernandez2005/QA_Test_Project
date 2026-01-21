package com.example.AutomatizationQA.Playwright;

import com.example.AutomatizationQA.Models.Region;
import com.example.AutomatizationQA.Repositorys.RegionRepository;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    private final RegionRepository regionRepository;

    public TestService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    public String runPctTest(TestDTO request) {
        Region region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new RuntimeException("Region not found with id " + request.getRegionId()));

        String url = "http://" + region.getIp() + ":" + region.getPort();

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium()
                    .launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();

            page.navigate(url);
            page.fill("input[name=\"username\"]", request.getUsername());
            page.fill("input[name=\"password\"]", request.getPassword());
            page.click("button[type=\"submit\"]");

            page.waitForSelector("text=My Dashboard");

            page.click("a.icon.wb-search[role=\"button\"]");
            page.fill("input[placeholder='Search Products']", request.getProduct());
            page.click("#autocomplete-result-0");

            Locator modal = page.locator(".modal-body:visible");
            modal.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

            if (request.isPhoneNumberEnabled()) {
                Locator phoneNumber = modal.locator("input[name=\"phoneNumber\"]");
                if (phoneNumber.isVisible()) {
                    phoneNumber.fill(request.getPhoneNumber());
                }
                Locator phoneNumberConfirm = modal.locator("input[name=\"phoneNumberConfirm\"]");
                if (phoneNumberConfirm.isVisible()) {
                    phoneNumberConfirm.fill(request.getPhoneNumber());
                }
            }
            

            if (request.isPhoneNumberEnabled()) {
                Locator amount = modal.locator("input[name=\"amount\"]");
                if (amount.isVisible()) {
                    amount.fill(request.getAmount());
                }
            }

            if (request.isClerkIdEnabled()) {
                Locator clerkId = modal.locator("input[name=\"clerkId\"]");
                if (clerkId.isVisible()) {
                    clerkId.focus();
                    clerkId.fill(request.getClerkId());
                }
            }

            Locator purchaseBtn = modal.locator("a", new Locator.LocatorOptions().setHasText("Purchase"));
            purchaseBtn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            purchaseBtn.click();

            Locator pagePrintLocator = modal.locator(".page-print");
            pagePrintLocator.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

            List<String> resultTransaction = pagePrintLocator.allInnerTexts().stream()
                    .distinct()
                    .toList();

            Locator finish = modal.locator("a", new Locator.LocatorOptions().setHasText("Finish"));
            finish.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            finish.click();

            return "Transaction completed: " + resultTransaction;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public String runSalesPortalTest(TestDTO request) {
        Region region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new RuntimeException("Region not found with id " + request.getRegionId()));

        String url = "http://" + region.getIp() + ":" + region.getPort() + region.getPath();

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium()
                    .launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();

            page.navigate(url);
            page.fill("input[name=\"userId\"]", request.getUsername());
            page.fill("input[name=\"password\"]", request.getPassword());
            page.click("button[type=\"submit\"]");

            page.locator("div[role=\"dialog\"]").waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.ATTACHED));
            page.locator("button[aria-label=\"Close\"]").click();

            page.getByText("Bienvenido,", new Page.GetByTextOptions().setExact(false))
                    .waitFor(new Locator.WaitForOptions()
                            .setState(WaitForSelectorState.VISIBLE));

            page.locator("button[aria-label=\"Open search\"]").click();
            page.locator("input[placeholder='Buscar servicios...']").fill(request.getProductType());
            page.locator("button[tabindex=\"0\"]").click();

            Locator carrierText = page.getByText("Selecciona el operador\n", new Page.GetByTextOptions().setExact(false));
            carrierText.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(15000));

            String carrier = request.getCarrier();
            page.locator(STR."button[data-pr-tooltip=\"\{carrier}\"]")
                    .click(new Locator.ClickOptions());

            Locator productText = page.getByText("Selecciona el producto\n", new Page.GetByTextOptions().setExact(false));
            productText.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(15000));

            String product = request.getProduct();
            page.locator(STR."button[data-pr-tooltip=\"\{product}\"]")
                    .click(new Locator.ClickOptions());
            page.locator("text=Confirm").click();

            page.getByText("ID del empleado", new Page.GetByTextOptions().setExact(false))
                    .waitFor(new Locator.WaitForOptions()
                            .setState(WaitForSelectorState.VISIBLE)
                            .setTimeout(15000));

            page.locator("input[type=\"password\"]").fill(request.getClerkId());
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirmar")).click();

            Locator transactionDetails = page.locator("#TransactionDetails");
            transactionDetails.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(20000));

            String receipt = transactionDetails.innerText();
            return "Transaction completed: \n" + receipt;

        } catch (Exception e) {
            throw new RuntimeException("Error:" + e.getMessage(), e);
        }
    }
}