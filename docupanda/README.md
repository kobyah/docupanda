# DocuPanda Connector

## Overview
The DocuPanda Connector for Mulesoft enables seamless integration with the DocuPanda API platform. This connector provides capabilities for document processing, schema management, reviews, and analysis directly within your Mulesoft applications.

## Prerequisites
- Mulesoft Enterprise/Community Edition
- DocuPanda API Key
- Java 8 or later

## Installation

### Maven Dependency
Add this dependency to your application's pom.xml:
```xml
<dependency>
    <groupId>com.docupanda.connector</groupId>
    <artifactId>mule-docupanda-connector</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <classifier>mule-plugin</classifier>
</dependency>
```

## Configuration

### Global Configuration
```xml
<docupanda:config name="DocuPanda_Config" 
    apiKey="${docupanda.apikey}" 
    baseUrl="${docupanda.baseurl}"/>
```

### Configuration Properties
| Parameter | Required | Default Value | Description |
|-----------|----------|---------------|-------------|
| apiKey    | Yes      | -            | DocuPanda API Key |
| baseUrl   | No       | https://app.docupanda.io | DocuPanda API Base URL |

## Available Operations

### Document Operations

#### Create Document
Creates a new document in DocuPanda.
```xml
<docupanda:create-document 
    config-ref="DocuPanda_Config" 
    content="Your document content" 
    title="Document Title"/>
```
Parameters:
- content (required): Document content
- title (required): Document title

#### Get Document
Retrieves a specific document by ID.
```xml
<docupanda:get-document 
    config-ref="DocuPanda_Config" 
    documentId="your-document-id"/>
```
Parameters:
- documentId (required): ID of the document to retrieve

#### Update Document
Updates an existing document.
```xml
<docupanda:update-document 
    config-ref="DocuPanda_Config"
    documentId="your-document-id"
    content="Updated content"
    title="Updated Title"/>
```
Parameters:
- documentId (required): ID of the document to update
- content (required): Updated document content
- title (required): Updated document title

#### Delete Document
Deletes a document.
```xml
<docupanda:delete-document 
    config-ref="DocuPanda_Config"
    documentId="your-document-id"/>
```
Parameters:
- documentId (required): ID of the document to delete

### Schema Operations

#### Create Schema
Creates a new schema.
```xml
<docupanda:create-schema 
    config-ref="DocuPanda_Config"
    schemaDefinition='{...your schema JSON...}'
    name="Schema Name"/>
```
Parameters:
- schemaDefinition (required): JSON schema definition
- name (required): Schema name

#### Get Schema
Retrieves a specific schema.
```xml
<docupanda:get-schema 
    config-ref="DocuPanda_Config"
    schemaId="your-schema-id"/>
```
Parameters:
- schemaId (required): ID of the schema to retrieve

#### List Schemas
Lists all available schemas.
```xml
<docupanda:list-schemas 
    config-ref="DocuPanda_Config"/>
```

### Review Operations

#### Create Review
Creates a new review for a document.
```xml
<docupanda:create-review 
    config-ref="DocuPanda_Config"
    documentId="your-document-id"
    comments="Review comments"/>
```
Parameters:
- documentId (required): ID of the document to review
- comments (required): Review comments

#### Get Review
Retrieves a specific review.
```xml
<docupanda:get-review 
    config-ref="DocuPanda_Config"
    reviewId="your-review-id"/>
```
Parameters:
- reviewId (required): ID of the review to retrieve

#### List Document Reviews
Lists all reviews for a document.
```xml
<docupanda:list-document-reviews 
    config-ref="DocuPanda_Config"
    documentId="your-document-id"/>
```
Parameters:
- documentId (required): ID of the document to list reviews for

### Analysis Operations

#### Analyze Document
Starts document analysis.
```xml
<docupanda:analyze-document 
    config-ref="DocuPanda_Config"
    documentId="your-document-id"
    analysisType="analysis-type"/>
```
Parameters:
- documentId (required): ID of the document to analyze
- analysisType (required): Type of analysis to perform

#### Get Analysis Result
Retrieves analysis results.
```xml
<docupanda:get-analysis-result 
    config-ref="DocuPanda_Config"
    analysisId="your-analysis-id"/>
```
Parameters:
- analysisId (required): ID of the analysis to retrieve

#### List Document Analyses
Lists all analyses for a document.
```xml
<docupanda:list-document-analyses 
    config-ref="DocuPanda_Config"
    documentId="your-document-id"/>
```
Parameters:
- documentId (required): ID of the document to list analyses for

## Error Handling

### HTTP Status Codes
| Status Code | Description |
|-------------|-------------|
| 400 | Bad Request - Check your request parameters |
| 401 | Unauthorized - Invalid or missing API key |
| 403 | Forbidden - Insufficient permissions |
| 404 | Not Found - Resource doesn't exist |
| 500 | Internal Server Error |

### Error Handling Example
```xml
<flow name="process-document">
    <docupanda:create-document config-ref="DocuPanda_Config" 
        content="#[payload.content]" 
        title="#[payload.title]"/>
    
    <error-handler>
        <on-error-continue type="DOCUPANDA:UNAUTHORIZED">
            <logger message="Invalid API key: #[error.description]"/>
        </on-error-continue>
        <on-error-continue type="DOCUPANDA:NOT_FOUND">
            <logger message="Resource not found: #[error.description]"/>
        </on-error-continue>
        <on-error-continue type="DOCUPANDA:BAD_REQUEST">
            <logger message="Invalid request: #[error.description]"/>
        </on-error-continue>
    </error-handler>
</flow>
```

## Best Practices

1. **Security**
   - Store API keys in secure configuration properties
   - Use encryption for sensitive data
   - Implement proper access controls

2. **Performance**
   - Implement caching where appropriate
   - Use batch processing for multiple documents
   - Monitor API rate limits

3. **Error Handling**
   - Implement comprehensive error handling
   - Log errors appropriately
   - Use retry mechanisms for transient failures

4. **Configuration**
   - Use property placeholders for configuration values
   - Maintain separate configurations for different environments
   - Document all custom configurations

## Example Use Cases

### 1. Document Processing Flow
```xml
<flow name="process-document-flow">
    <http:listener path="/document" method="POST"/>
    
    <!-- Create document -->
    <docupanda:create-document 
        config-ref="DocuPanda_Config" 
        content="#[payload.content]" 
        title="#[payload.title]"/>
    
    <!-- Analyze document -->
    <docupanda:analyze-document 
        config-ref="DocuPanda_Config"
        documentId="#[payload.documentId]"
        analysisType="sentiment"/>
    
    <!-- Get analysis results -->
    <docupanda:get-analysis-result 
        config-ref="DocuPanda_Config"
        analysisId="#[payload.analysisId]"/>
</flow>
```

### 2. Document Review Flow
```xml
<flow name="review-document-flow">
    <http:listener path="/review" method="POST"/>
    
    <!-- Create review -->
    <docupanda:create-review 
        config-ref="DocuPanda_Config"
        documentId="#[payload.documentId]"
        comments="#[payload.comments]"/>
    
    <!-- List all reviews -->
    <docupanda:list-document-reviews 
        config-ref="DocuPanda_Config"
        documentId="#[payload.documentId]"/>
</flow>
```

## Support
For support and further information:
- DocuPanda API Documentation: https://docupanda.readme.io/reference
- Report issues: [GitHub Issues](https://github.com/docupanda/mule-connector/issues)
- Contact: support@docupanda.io

## License
This connector is licensed under the terms of [License Name]. See LICENSE.md for more details. 