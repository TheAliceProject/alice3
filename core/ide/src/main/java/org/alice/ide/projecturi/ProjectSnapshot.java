package org.alice.ide.projecturi;

import edu.cmu.cs.dennisc.image.ImageUtilities;
import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import org.alice.ide.projecturi.views.NotAvailableIcon;
import org.alice.ide.projecturi.views.SnapshotIcon;
import org.alice.ide.uricontent.StarterProjectUtilities;
import org.alice.stageide.openprojectpane.models.TemplateUriState;
import org.alice.tweedle.file.ManifestEncoderDecoder;
import org.alice.tweedle.file.ProjectManifest;
import org.apache.commons.io.IOUtils;
import org.lgna.project.Project;
import org.lgna.project.io.ProjectIo;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.lgna.project.Project.SceneCameraType.VRHeadset;

public class ProjectSnapshot {

  private static final Icon SNAPSHOT_NOT_AVAILABLE_ICON = new NotAvailableIcon("snapshot not available");
  private static final Icon FILE_DOES_NOT_EXIST_ICON = new NotAvailableIcon("snapshot does not exist");

  final URI uri;
  Project.SceneCameraType sceneCamera;
  String text;
  Icon icon = FILE_DOES_NOT_EXIST_ICON;

  public ProjectSnapshot(URI uri) {
    this.uri = uri;
    initialize();
  }

  public ProjectSnapshot(URI uri, String text, Icon icon) {
    this.uri = uri;
    sceneCamera = Project.SceneCameraType.WindowCamera;
    this.text = text;
    this.icon = icon;
  }

  public URI getUri() {
    return uri;
  }

  public String getText() {
    return text;
  }

  public Icon getIcon() {
    return icon;
  }

  public boolean hasUri() {
    return uri != null;
  }

  public boolean hasValidUri() {
    return uri != null && TemplateUriState.SCHEME.equals(uri.getScheme());
  }

  public TemplateUriState.Template getUriFragment() {
    if (hasValidUri()) {
      String fragment = getUri().getFragment();
      if (fragment != null) {
        return TemplateUriState.Template.valueOf(fragment);
      }
    }
    return null;
  }

  private void initialize() {
    boolean isStarterProject = uri != null && uri.isAbsolute() && !"file".equals(uri.getScheme());
    if (uri == null) {
      text = null;
    } else {
      if (uri.isAbsolute()) {
        File file = new File(isStarterProject ? StarterProjectUtilities.toFileUriFromStarterUri(uri) : uri);
        text = file.getName();
        if (isStarterProject) {
          final String SUFFIX = ".a3p";
          if (text.endsWith(SUFFIX)) {
            text = text.substring(0, text.length() - SUFFIX.length());
          }
          //Localize the text to display if possible
          text = findLocalizedFileNameText(text);
        }
        if (file.exists()) {
          readFile(file);
        }
      } else {
        text = uri.toString();
      }
    }
  }

  private void readFile(File file) {
    try {
      ZipFile zipFile = new ZipFile(file);
      readManifest(zipFile);
      icon = readThumbnail(zipFile);
      zipFile.close();
    } catch (Throwable t) {
      icon = SNAPSHOT_NOT_AVAILABLE_ICON;
    }
  }

  private void readManifest(ZipFile zipFile) throws IOException {
    ZipEntry zipEntry = zipFile.getEntry(ProjectIo.MANIFEST_ENTRY_NAME);
    if (zipEntry != null) {
      InputStream is = zipFile.getInputStream(zipEntry);
      ProjectManifest manifest = ManifestEncoderDecoder.fromJson(IOUtils.toString(is), ProjectManifest.class);
      sceneCamera = manifest.projectStructure.sceneCameraType;
      if (sceneCamera.equals(VRHeadset)) {
        text += " (VR)";
      }
    }

  }

  private Icon readThumbnail(ZipFile zipFile) throws IOException {
    ZipEntry zipEntry = zipFile.getEntry("thumbnail.png");
    if (zipEntry != null) {
      InputStream is = zipFile.getInputStream(zipEntry);
      Image image = ImageUtilities.read(ImageUtilities.PNG_CODEC_NAME, is);
      return new SnapshotIcon(image);
    } else {
      return SNAPSHOT_NOT_AVAILABLE_ICON;
    }
  }
  private static final String PROJECT_LOCALIZATION_BUNDLE_NAME = "org.lgna.story.resources.ProjectNames";

  private static String findLocalizedFileNameText(String key) {
    if (key != null) {
      try {
        ResourceBundle resourceBundle =
            ResourceBundleUtilities.getUtf8Bundle(ProjectSnapshot.PROJECT_LOCALIZATION_BUNDLE_NAME, JComponent.getDefaultLocale());
        return resourceBundle.getString(key);
      } catch (MissingResourceException ignored) {
      }
    }
    return key;
  }
}
