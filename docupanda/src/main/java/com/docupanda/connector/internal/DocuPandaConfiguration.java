package com.docupanda.connector.internal;

import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

@Operations(DocuPandaOperations.class)
public class DocuPandaConfiguration {
    
    @Parameter
    @Placement(order = 1)
    @Summary("The API Key for DocuPanda authentication")
    @Password
    private String apiKey;

    @Parameter
    @Placement(order = 2)
    @DisplayName("Base URL")
    @Summary("The base URL for DocuPanda API (e.g., https://app.docupanda.io)")
    private String baseUrl;

    public String getApiKey() {
        return apiKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
} 