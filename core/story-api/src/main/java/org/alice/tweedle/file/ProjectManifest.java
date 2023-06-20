package org.alice.tweedle.file;

import org.lgna.project.Project;

public class ProjectManifest extends Manifest {


  public ProjectStructure projectStructure = new ProjectStructure();


  public static class ProjectStructure {
    public Project.SceneCameraType sceneCameraType;
  }
}
