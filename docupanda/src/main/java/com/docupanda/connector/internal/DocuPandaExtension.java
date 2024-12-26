package com.docupanda.connector.internal;

import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.Configurations;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;

@Xml(prefix = "docupanda")
@Extension(name = "DocuPanda")
@Configurations(DocuPandaConfiguration.class)
public class DocuPandaExtension {
} 