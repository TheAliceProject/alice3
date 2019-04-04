package org.alice.tweedle.file;

import org.lgna.common.Resource;

public abstract class ResourceReference {

  public String name;
  public String format;
  public String file;
  //Unitialized provenance is used to add additional info
  public Manifest.Provenance provenance;
  // Held in a variable so it is serialized
  public final String type;

  public ResourceReference() {
    type = getContentType();
  }

  ResourceReference(String name, String fileName, String format) {
    this.name = name;
    this.file = fileName;
    this.format = format;
    type = getContentType();
  }

  ResourceReference(Resource resource) {
    name = resource.getName();
    file = resource.getOriginalFileName();
    format = resource.getContentType();
    type = getContentType();
  }

  public abstract String getContentType();

}
