package com.docupanda.connector.internal;

import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.Alias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DocuPandaOperations {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocuPandaOperations.class);

    // Document Operations
    @MediaType(value = MediaType.APPLICATION_JSON)
    public String createDocument(
            @Config DocuPandaConfiguration configuration,
            @Content(primary = true) @Summary("Document content") String content,
            @Summary("Document title") String title) throws IOException {
        return executeRequest(configuration, "/documents", "POST", 
            String.format("{\"title\":\"%s\",\"content\":\"%s\"}", 
                title.replace("\"", "\\\""), 
                content.replace("\"", "\\\"")));
    }

    @MediaType(value = MediaType.APPLICATION_JSON)
    public String getDocument(
            @Config DocuPandaConfiguration configuration,
            @Summary("Document ID") String documentId) throws IOException {
        return executeRequest(configuration, "/documents/" + documentId, "GET", null);
    }

    @MediaType(value = MediaType.APPLICATION_JSON)
    public String updateDocument(
            @Config DocuPandaConfiguration configuration,
            @Summary("Document ID") String documentId,
            @Content(primary = true) @Summary("Updated content") String content,
            @Summary("Updated title") String title) throws IOException {
        return executeRequest(configuration, "/documents/" + documentId, "PUT",
            String.format("{\"title\":\"%s\",\"content\":\"%s\"}", 
                title.replace("\"", "\\\""), 
                content.replace("\"", "\\\"")));
    }

    @MediaType(value = MediaType.APPLICATION_JSON)
    public String deleteDocument(
            @Config DocuPandaConfiguration configuration,
            @Summary("Document ID") String documentId) throws IOException {
        return executeRequest(configuration, "/documents/" + documentId, "DELETE", null);
    }

    // Schema Operations
    @MediaType(value = MediaType.APPLICATION_JSON)
    public String createSchema(
            @Config DocuPandaConfiguration configuration,
            @Content(primary = true) @Summary("Schema definition") String schemaDefinition,
            @Summary("Schema name") String name) throws IOException {
        return executeRequest(configuration, "/schemas", "POST",
            String.format("{\"name\":\"%s\",\"definition\":%s}", 
                name.replace("\"", "\\\""), 
                schemaDefinition));
    }

    @MediaType(value = MediaType.APPLICATION_JSON)
    public String getSchema(
            @Config DocuPandaConfiguration configuration,
            @Summary("Schema ID") String schemaId) throws IOException {
        return executeRequest(configuration, "/schemas/" + schemaId, "GET", null);
    }

    @MediaType(value = MediaType.APPLICATION_JSON)
    public String listSchemas(
            @Config DocuPandaConfiguration configuration) throws IOException {
        return executeRequest(configuration, "/schemas", "GET", null);
    }

    // Review Operations
    @MediaType(value = MediaType.APPLICATION_JSON)
    public String createReview(
            @Config DocuPandaConfiguration configuration,
            @Summary("Document ID") String documentId,
            @Content(primary = true) @Summary("Review comments") String comments) throws IOException {
        return executeRequest(configuration, "/reviews", "POST",
            String.format("{\"document_id\":\"%s\",\"comments\":\"%s\"}", 
                documentId.replace("\"", "\\\""), 
                comments.replace("\"", "\\\"")));
    }

    @MediaType(value = MediaType.APPLICATION_JSON)
    public String getReview(
            @Config DocuPandaConfiguration configuration,
            @Summary("Review ID") String reviewId) throws IOException {
        return executeRequest(configuration, "/reviews/" + reviewId, "GET", null);
    }

    @MediaType(value = MediaType.APPLICATION_JSON)
    public String listDocumentReviews(
            @Config DocuPandaConfiguration configuration,
            @Summary("Document ID") String documentId) throws IOException {
        return executeRequest(configuration, "/documents/" + documentId + "/reviews", "GET", null);
    }

    // Analysis Operations
    @MediaType(value = MediaType.APPLICATION_JSON)
    public String analyzeDocument(
            @Config DocuPandaConfiguration configuration,
            @Summary("Document ID") String documentId,
            @Content(primary = true) @Summary("Analysis type") String analysisType) throws IOException {
        return executeRequest(configuration, "/analysis", "POST",
            String.format("{\"document_id\":\"%s\",\"type\":\"%s\"}", 
                documentId.replace("\"", "\\\""), 
                analysisType.replace("\"", "\\\"")));
    }

    @MediaType(value = MediaType.APPLICATION_JSON)
    public String getAnalysisResult(
            @Config DocuPandaConfiguration configuration,
            @Summary("Analysis ID") String analysisId) throws IOException {
        return executeRequest(configuration, "/analysis/" + analysisId, "GET", null);
    }

    @MediaType(value = MediaType.APPLICATION_JSON)
    public String listDocumentAnalyses(
            @Config DocuPandaConfiguration configuration,
            @Summary("Document ID") String documentId) throws IOException {
        return executeRequest(configuration, "/documents/" + documentId + "/analysis", "GET", null);
    }

    // Document Summary Operations
    @MediaType(value = MediaType.APPLICATION_JSON)
    @DisplayName("Get Document Summary")
    @Alias("get-document-summary")
    @Summary("Get a summary of your documents, including total number and unique datasets")
    public String getDocumentSummary(
            @Config DocuPandaConfiguration configuration) throws IOException {
        return executeRequest(configuration, "/documents/summary", "GET", null);
    }

    @MediaType(value = MediaType.APPLICATION_JSON)
    @DisplayName("List Documents")
    @Alias("list-documents")
    @Summary("List all documents that have been submitted to DocuPanda for processing")
    public String listDocuments(
            @Config DocuPandaConfiguration configuration,
            @Optional @Summary("Filter results by dataset name") String dataset) throws IOException {
        String path = "/documents";
        if (dataset != null && !dataset.isEmpty()) {
            path += "?dataset=" + dataset;
        }
        return executeRequest(configuration, path, "GET", null);
    }

    @MediaType(value = MediaType.APPLICATION_JSON)
    @DisplayName("Submit Document")
    @Alias("submit-document")
    @Summary("Submit a document to DocuPanda for processing")
    public String submitDocument(
            @Config DocuPandaConfiguration configuration,
            @Content(primary = true) @Summary("Document content") String content,
            @Optional @Summary("Document URL") String url,
            @Optional @Summary("Workflow ID") String workflowId) throws IOException {
        
        StringBuilder jsonBuilder = new StringBuilder("{");
        if (content != null && !content.isEmpty()) {
            jsonBuilder.append("\"content\":\"").append(content.replace("\"", "\\\"")).append("\"");
        }
        if (url != null && !url.isEmpty()) {
            if (jsonBuilder.length() > 1) jsonBuilder.append(",");
            jsonBuilder.append("\"url\":\"").append(url.replace("\"", "\\\"")).append("\"");
        }
        if (workflowId != null && !workflowId.isEmpty()) {
            if (jsonBuilder.length() > 1) jsonBuilder.append(",");
            jsonBuilder.append("\"workflowId\":\"").append(workflowId.replace("\"", "\\\"")).append("\"");
        }
        jsonBuilder.append("}");
        
        return executeRequest(configuration, "/document", "POST", jsonBuilder.toString());
    }

    // Helper method to execute HTTP requests
    private String executeRequest(DocuPandaConfiguration configuration, String path, String method, String jsonBody) throws IOException {
        URL url = new URL(configuration.getBaseUrl() + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("X-API-KEY", configuration.getApiKey());

        if (jsonBody != null) {
            conn.setDoOutput(true);
            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
        }

        // Handle error responses
        try {
            StringBuilder response = new StringBuilder();
            try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }
            return response.toString();
        } catch (IOException e) {
            // Log error details
            LOGGER.error("Error calling DocuPanda API: {} {}", conn.getResponseCode(), conn.getResponseMessage());
            if (conn.getErrorStream() != null) {
                try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        errorResponse.append(responseLine.trim());
                    }
                    LOGGER.error("Error response: {}", errorResponse.toString());
                }
            }
            throw e;
        }
    }
}