package com.example.AutomatizationQA.Playwright;

import com.example.AutomatizationQA.Models.Region;
import com.example.AutomatizationQA.Repositorys.RegionRepository;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class SalesPortalService {
    private final RegionRepository regionRepository;
    private final PlaywrightUtils util;

    public SalesPortalService(RegionRepository regionRepository, PlaywrightUtils util) {
        this.regionRepository = regionRepository;
        this.util = util;
    }


    public String runSalesPortalTest(TestDTO request) {
        log.info("Iniciando test para región ID: {}, Usuario: {}",
                request.getRegionId(), request.getUsername());

        Region region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new RuntimeException("Región no encontrada con ID: " + request.getRegionId()));

        String url = util.buildUrl(region);
        log.debug("URL de destino: {}", url);

        try (Playwright playwright = Playwright.create()) {
            Browser browser = util.createBrowser(playwright);
            BrowserContext context = browser.newContext(util.configureContext());
            Page page = context.newPage();

            util.configurePageDefaults(page);

            try {
                performLogin(page, url, request);

                handleModalDialog(page);

                verifyLoginSuccess(page);

                String receipt = processTransaction(page, request);

                log.info("Transacción completada exitosamente para usuario: {}", request.getUsername());
                return buildSuccessResponse(receipt);

            } catch (Exception e) {
                throw new RuntimeException("Error durante la ejecución del test: " + e.getMessage(), e);
            } finally {
                closeResources(context, browser);
            }

        } catch (Exception e) {
            log.error("Error en test automatizado: {}", e.getMessage(), e);
            throw new RuntimeException("Error en test automatizado: " + e.getMessage(), e);
        }
    }




    private void performLogin(Page page, String url, TestDTO request) {
        log.debug("Realizando login en: {}", url);

        page.navigate(url, new Page.NavigateOptions()
                .setWaitUntil(WaitUntilState.NETWORKIDLE));

        fillFieldWithRetry(page, "input[name=\"userId\"]", request.getUsername(), "Usuario");
        fillFieldWithRetry(page, "input[name=\"password\"]", request.getPassword(), "Contraseña");

        clickWithRetry(page, "button[type=\"submit\"]", "Botón de login");

        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    private void handleModalDialog(Page page) {
        try {
            Locator dialog = page.locator("div[role=\"dialog\"]");
            dialog.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.ATTACHED)
                    .setTimeout(util.SHORT_TIMEOUT.toMillis()));

            Locator closeButton = page.locator("button[aria-label=\"Close\"], button[aria-label=\"Cerrar\"]");
            closeButton.click(new Locator.ClickOptions()
                    .setTimeout(util.SHORT_TIMEOUT.toMillis()));


            log.debug("Diálogo modal cerrado exitosamente");
        } catch (Exception e) {
            log.debug("No se encontró diálogo modal o ya estaba cerrado");
        }
    }

    private void verifyLoginSuccess(Page page) {
        String[] welcomeMessages = {"Bienvenido,", "Welcome,"};

        for (String message : welcomeMessages) {
            try {
                page.getByText("Bienvenido,", new Page.GetByTextOptions().setExact(false))
                        .waitFor(new Locator.WaitForOptions()
                                .setState(WaitForSelectorState.VISIBLE)
                                .setTimeout(util.DEFAULT_TIMEOUT.toMillis()));
                log.debug("Login verificado con mensaje: {}", message);
                return;
            } catch (Exception e) {
                // Continuar con el siguiente mensaje
            }
        }

        throw new RuntimeException("No se pudo verificar el login exitoso");
    }

    private String processTransaction(Page page, TestDTO request) {
        searchProduct(page, request.getProductType());

        selectCarrier(page, request.getCarrier());

        selectProduct(page, request.getProduct());

        confirmButton(page);

        enterClerkId(page, request.getClerkId());

        confirmButton(page);

        return getTransactionReceipt(page);
    }

    private void searchProduct(Page page, String productType) {
        log.debug("Buscando producto: {}", productType);

        String[] searchButtonSelectors = {
                "button[aria-label=\"Open search\"]",
                "button[aria-label=\"Abrir búsqueda\"]",
                "button[title*=\"search\"]",
                "button[title*=\"buscar\"]",
                "[role=\"button\"]:has(svg.search-icon)"
        };

        clickWithSelectorOptions(page, searchButtonSelectors, "Botón de búsqueda");

        String[] searchInputSelectors = {
                "input[placeholder*='Buscar servicios']",
                "input[placeholder*='Search for services']",
                "input[type='search']",
                "input[name*='search']"
        };

        fillWithSelectorOptions(page, searchInputSelectors, productType, "Campo de búsqueda");

        clickWithRetry(page, "button[tabindex=\"0\"], button[type=\"submit\"]", "Acción de búsqueda");

        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    private void selectCarrier(Page page, String carrier) {
        log.debug("Seleccionando carrier: {}", carrier);

        waitForAnyText(page,
                new String[]{"Selecciona el operador", "Select the carrier"},
                "Texto de selección de carrier");

        clickCarrierOrProduct(page, carrier, "Carrier");
    }

    private void selectProduct(Page page, String product) {
        log.debug("Seleccionando producto: {}", product);

        waitForAnyText(page,
                new String[]{"Selecciona el producto", "Select the product"},
                "Texto de selección de producto");

        clickCarrierOrProduct(page, product, "Producto");
    }

    private void clickCarrierOrProduct(Page page, String item, String type) {
        String[] selectors = {
                STR."button[data-pr-tooltip=\"\{item}\"]",
                STR."button[title=\"\{item}\"]",
                STR."button:has-text(\"\{item}\")",
                STR."div[data-pr-tooltip=\"\{item}\"]",
                STR."[data-tooltip*=\"\{item}\"]"
        };

        clickWithSelectorOptions(page, selectors, type + ": " + item);
    }


    private void enterClerkId(Page page, String clerkId) {
        log.debug("Ingresando ID de empleado: {}", clerkId);

        waitForAnyText(page,
                new String[]{"ID del empleado", "Clerk ID"},
                "Texto de ID de Empleado");

        String[] passwordSelectors = {
                "input[type=\"password\"]",
                "input[name*=\"clerk\"]",
                "input[name*=\"employee\"]",
                "input[placeholder*=\"ID\"]"
        };

        fillWithSelectorOptions(page, passwordSelectors, clerkId, "Campo de ID de empleado");
    }

    private void confirmButton(Page page) {
        log.debug("Confirmando transacción");

        String[] confirmSelectors = {
                "button:has-text(\"Confirmar\")",
                "button:has-text(\"Confirm\")",
                "button[name=\"confirm\"]",
                "button[type=\"submit\"]"
        };

        clickWithSelectorOptions(page, confirmSelectors, "Botón de confirmar transacción");

        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    private String getTransactionReceipt(Page page) {
        log.debug("Obteniendo recibo de transacción");

        Locator receiptElement = page.locator("#TransactionDetails");
        receiptElement.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(util.LONG_TIMEOUT.toMillis()));

        String receipt = "";
        int attempts = 0;
        while (attempts < 10 && (receipt == null || receipt.trim().length() < 10)) {
            receipt = receiptElement.innerText();
            if (receipt.trim().length() < 10) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                attempts++;
            }
        }

        if (receipt == null || receipt.trim().length() < 10) {
            throw new RuntimeException("Recibo no contiene información suficiente");
        }

        log.debug("Recibo obtenido exitosamente");
        return receipt;
    }

    // ============ MÉTODOS DE UTILIDAD ============

    private void fillFieldWithRetry(Page page, String selector, String value, String fieldName) {
        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                page.locator(selector).fill(value);
                log.debug("{} completado exitosamente", fieldName);
                return;
            } catch (Exception e) {
                if (attempt == maxAttempts) {
                    throw new RuntimeException("Error al completar " + fieldName + ": " + e.getMessage(), e);
                }
                log.warn("Intento {} fallado para {}, reintentando...", attempt, fieldName);
                waitBriefly();
            }
        }
    }

    private void clickWithRetry(Page page, String selector, String buttonName) {
        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                page.locator(selector).click(new Locator.ClickOptions()
                        .setTimeout(util.SHORT_TIMEOUT.toMillis()));
                log.debug("{} clickeado exitosamente", buttonName);
                return;
            } catch (Exception e) {
                if (attempt == maxAttempts) {
                    throw new RuntimeException("Error al hacer click en " + buttonName + ": " + e.getMessage(), e);
                }
                log.warn("Intento {} fallado para {}, reintentando...", attempt, buttonName);
                waitBriefly();
            }
        }
    }

    private void clickWithSelectorOptions(Page page, String[] selectors, String elementName) {
        for (String selector : selectors) {
            try {
                page.locator(selector).first().click(new Locator.ClickOptions()
                        .setTimeout(util.SHORT_TIMEOUT.toMillis()));
                log.debug("{} encontrado con selector: {}", elementName, selector);
                return;
            } catch (Exception e) {
                // Continuar con siguiente selector
            }
        }
        throw new RuntimeException("No se pudo encontrar " + elementName + " con ningún selector");
    }

    private void fillWithSelectorOptions(Page page, String[] selectors, String value, String fieldName) {
        for (String selector : selectors) {
            try {
                page.locator(selector).first().fill(value);
                log.debug("{} completado con selector: {}", fieldName, selector);
                return;
            } catch (Exception e) {
                // Continuar con siguiente selector
            }
        }
        throw new RuntimeException("No se pudo completar " + fieldName + " con ningún selector");
    }

    private void waitForAnyText(Page page, String[] texts, String description) {
        for (String text : texts) {
            try {
                page.getByText(text, new Page.GetByTextOptions().setExact(false))
                        .waitFor(new Locator.WaitForOptions()
                                .setState(WaitForSelectorState.VISIBLE)
                                .setTimeout(util.DEFAULT_TIMEOUT.toMillis()));
                log.debug("{} encontrado con texto: {}", description, text);
                return;
            } catch (Exception e) {
                // Continuar con siguiente texto
            }
        }
        throw new RuntimeException("No se encontró " + description);
    }

    private void waitBriefly() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void closeResources(BrowserContext context, Browser browser) {
        try {
            if (context != null) {
                context.close();
            }
        } catch (Exception e) {
            log.warn("Error al cerrar contexto: {}", e.getMessage());
        }

        try {
            if (browser != null) {
                browser.close();
            }
        } catch (Exception e) {
            log.warn("Error al cerrar navegador: {}", e.getMessage());
        }
    }

    private String buildSuccessResponse(String receipt) {
        return String.format("""
            ✅ TRANSACCIÓN COMPLETADA EXITOSAMENTE
            📋 RECIBO:
            %s
            ⏰ FECHA: %s
            """,
                receipt,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}