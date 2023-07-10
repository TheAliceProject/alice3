package org.alice.tweedle.file;

import java.io.File;
import java.net.URI;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;

public class Manifest {
  private static final String DEFAULT_ICON = "thumbnail.png";
  private static final String DEFAULT_VERSION = "1.0";

  public Description description = new Description();
  public Provenance provenance = new Provenance();
  public MetaData metadata = new MetaData();
  public List<ProjectIdentifier> prerequisites = new ArrayList<>();
  public List<ResourceReference> resources = new ArrayList<>();

  private File rootFile;

  public String getId() {
    return metadata.identifier.name;
  }

  public String getName() {
    return description.name;
  }

  protected void copyForExport(Manifest copy) {
    copy.description = description;
    copy.provenance = provenance;
    copy.metadata = new MetaData();
    copy.metadata.identifier.name = description.name;
    copy.metadata.identifier.type = ProjectType.Model;
    copy.prerequisites = new ArrayList<>(prerequisites);
    copy.resources = new ArrayList<>();
    for (ResourceReference resource : resources) {
      if (resource instanceof StructureReference) {
        try {
          copy.resources.add(resource.clone());
        } catch (CloneNotSupportedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static class Description {
    public String name;
    public String icon = DEFAULT_ICON;
    public List<String> tags = new ArrayList<>();
    public List<String> groupTags = new ArrayList<>();
    public List<String> themeTags = new ArrayList<>();
  }

  public static class Provenance {
    public String aliceVersion;
    public Temporal created = ZonedDateTime.now();
    public String creator = "Anonymous";
  }

  public static class MetaData {
    public String fileType;
    public String formatVersion = "0.1";
    public ProjectIdentifier identifier = new ProjectIdentifier();
  }

  public static class ProjectIdentifier {
    public String name;
    public String version = DEFAULT_VERSION;
    public ProjectType type;
  }

  public enum ProjectType {
    Library, World, Model
  }

  public ResourceReference getResource(String resourceName) {
    for (ResourceReference resource : resources) {
      if (resource.name.equals(resourceName)) {
        return resource;
      }
    }
    return null;
  }

  public void setRootFile(File rootFile) {
    this.rootFile = rootFile;
  }

  public URI getUri(String manifestRelativeFileName) {
    if (manifestRelativeFileName == null) {
      return null;
    }
    if (rootFile != null) {
      return new File(rootFile, manifestRelativeFileName).toURI();
    }
    return new File(manifestRelativeFileName).toURI();
  }
}
