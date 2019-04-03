/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lgna.story.resourceutilities.exporterutils;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.cmu.cs.dennisc.java.io.FileUtilities;

/**
 *
 * @author Administrator
 */
public class AssetReference {
  private String name;
  private String assetReference;

  public static String getAssetName(String assetFileName) {
    assetFileName = normalizeSeparators(assetFileName);
    int separatorIndex = assetFileName.lastIndexOf(File.separator);
    if (separatorIndex == -1) {
      separatorIndex = 0;
    }
    int dotIndex = assetFileName.lastIndexOf(".");
    if (dotIndex == -1) {
      dotIndex = assetFileName.length();
    }
    return assetFileName.substring(separatorIndex + 1, dotIndex);
  }

  public AssetReference(String name, String assetReference) {
    this.name = name;
    this.assetReference = assetReference;
  }

  public AssetReference(Element e) {
    this.name = e.getAttribute("name");
    Element fileElement = (Element) e.getElementsByTagName("File").item(0);
    this.assetReference = fileElement.getTextContent();
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getReference(boolean absolute) {
    if (absolute) {
      return this.getAbsoluteReference();
    } else {
      return this.getRelativeReference();
    }
  }

  public File getAssetFile() {
    return new File(this.assetReference);
  }

  public String getAbsoluteReference() {
    return this.assetReference;
  }

  public String getRelativeReference() {
    File referenceFile = new File(this.assetReference);
    return referenceFile.getName();
  }

  public void setReference(String reference) {
    this.assetReference = reference;
  }

  public static String normalizeSeparators(String path) {
    return path.replace("/", File.separator);
  }

  public String getAbsoluteResourceReference() {
    return normalizeSeparators(this.getReference(true));
  }

  protected void setAttributes(Element element) {
    element.setAttribute("name", this.getName());
  }

  public Element createAbsoluteXMLElement(String elementName, Document doc) {
    Element element = doc.createElement(elementName);
    Element fileElement = doc.createElement("File");
    fileElement.setTextContent(getAbsoluteResourceReference());
    element.appendChild(fileElement);
    setAttributes(element);
    return element;
  }

  public String getRelativeResourceReference(String relativePath) {
    relativePath = relativePath.replace('\\', '/');
    if (relativePath.length() > 0) {
      if (!relativePath.endsWith("/")) {
        relativePath += "/";
      }
    }
    if (relativePath.startsWith("/")) {
      relativePath = relativePath.substring(1);
    }
    String assetReferenceString = relativePath + normalizeSeparators(this.getReference(false));
    return assetReferenceString;
  }

  public Element createRelativeXMLElement(String elementName, Document doc, String relativePath) {
    Element element = doc.createElement(elementName);
    Element fileElement = doc.createElement("File");
    String assetReferenceString = getRelativeResourceReference(relativePath);
    fileElement.setTextContent(assetReferenceString);
    element.appendChild(fileElement);
    setAttributes(element);
    return element;
  }

  public void setResourcePath(File resourcePath) {
    File assetFile = new File(this.assetReference);
    if (!assetFile.exists()) {
      assetFile = new File(resourcePath, assetFile.getName());
      if (assetFile.exists()) {
        this.assetReference = assetFile.getAbsolutePath();
      }
    }
  }

  public boolean referenceExists() {
    return FileUtilities.exists(this.assetReference);
  }

}
