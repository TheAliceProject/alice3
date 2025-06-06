package org.alice.tweedle.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.lgna.common.Resource;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TypeReference.class, name = "Class"),
        @JsonSubTypes.Type(value = AliceTextureReference.class, name = "aliceTexture"),
        @JsonSubTypes.Type(value = AudioReference.class, name = "audio"),
        @JsonSubTypes.Type(value = StructureReference.class, name = "skeletonMesh"),
        @JsonSubTypes.Type(value = ModelReference.class, name = "model"),
        @JsonSubTypes.Type(value = ImageReference.class, name = "image") }
)
public abstract class ResourceReference implements Cloneable {

  public String name;
  public String format;
  public String file;
  //Uninitialized provenance is used to add additional info
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

  @Override
  public ResourceReference clone() throws CloneNotSupportedException {
    return (ResourceReference) super.clone();
  }
}
