/*******************************************************************************
 * Copyright (c) 2006, 2016, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/

package org.alice.netbeans;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;

import java.awt.Component;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;

import org.alice.netbeans.options.Alice3OptionsPanelController;
import org.alice.netbeans.project.ProjectCodeGenerator;
import org.lgna.project.migration.ast.MigrationException;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.templates.TemplateRegistration;
import org.netbeans.spi.project.ui.support.ProjectChooser;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.xml.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

// TODO define position attribute
@TemplateRegistration(folder = "Project/Standard", displayName = "Java Project from Existing Alice Project", description = "Alice3ProjectTemplateDescription.html", iconBase = "org/alice/netbeans/aliceIcon.png", content = "ProjectTemplate.zip") public class Alice3ProjectTemplateWizardIterator implements WizardDescriptor.ProgressInstantiatingIterator {

  public static Alice3ProjectTemplateWizardIterator createIterator() {
    return new Alice3ProjectTemplateWizardIterator();
  }

  public Alice3ProjectTemplateWizardIterator() {
  }

  private WizardDescriptor.Panel[] createPanels() {
    return new WizardDescriptor.Panel[] {
        new Alice3ProjectTemplateWizardPanel()
    };
  }

  private String[] createSteps() {
    return new String[] {NbBundle.getMessage(Alice3ProjectTemplateWizardIterator.class, "LBL_CreateProjectStep")};
  }

  private void cleanSlateIfAppropriate() {
    if (Alice3OptionsPanelController.isOfferingCleanSlateDesired()) {
      final List<TopComponent> componentsToClose = Lists.newLinkedList();
      Set<TopComponent> comps = TopComponent.getRegistry().getOpened();
      for (TopComponent comp : comps) {
        //IoLoggingHandler.errln( comp.getClass(), comp );
        if (comp.getClass().getName().equals("org.netbeans.modules.welcome.WelcomeComponent")) {
          componentsToClose.add(comp);
        } else {
          Node[] nodes = comp.getActivatedNodes();
          if (nodes != null && nodes.length > 0) {
            for (Node node : nodes) {
              if (node.getClass().getName().equals("org.netbeans.modules.java.JavaNode")) {
                componentsToClose.add(comp);
              }
            }
          }
        }
      }
      if (componentsToClose.size() > 0) {
        // TODO I18n
        String title = "Clean Slate?";

        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        // TODO I18n
        sb.append("<h1>Would you like to start with a clean slate?</h1>");
        sb.append("<h1>Close the following tabs:</h1>");
        sb.append("<ul>");
        for (TopComponent topComponent : componentsToClose) {
          sb.append("<li>");
          sb.append(topComponent.getName());
          sb.append("</li>");
        }
        sb.append("</ul>");
        sb.append("</html>");

        String message = sb.toString();
        int result = JOptionPane.showConfirmDialog(this.panels[0].getComponent(), message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
          SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
              for (TopComponent topComponent : componentsToClose) {
                topComponent.close();
              }
            }
          });
        }
      }
    }
  }

  @Override
  public void initialize(WizardDescriptor wizardDescriptor) {
    this.wizardDescriptor = wizardDescriptor;
    this.index = 0;
    this.panels = this.createPanels();
    // Make sure list of steps is accurate.
    String[] steps = this.createSteps();
    for (int i = 0; i < panels.length; i++) {
      Component c = panels[i].getComponent();
      if (steps[i] == null) {
        // Default step name to component name of panel.
        // Mainly useful for getting the name of the target
        // chooser to appear in the list of steps.
        steps[i] = c.getName();
      }
      if (c instanceof JComponent) { // assume Swing components
        JComponent jc = (JComponent) c;
        // Step #.
        // TODO if using org.openide.dialogs >= 7.8, can use WizardDescriptor.PROP_*:
        jc.putClientProperty("WizardPanel_contentSelectedIndex", i);
        // Step name (actually the whole list for reference).
        jc.putClientProperty("WizardPanel_contentData", steps);
      }
    }
  }

  @Override
  public void uninitialize(WizardDescriptor wiz) {
    this.wizardDescriptor.putProperty("projdir", null);
    this.wizardDescriptor.putProperty("name", null);
    this.wizardDescriptor = null;
    this.panels = null;
  }

  @Override
  public Set<FileObject> instantiate() throws IOException {
    return instantiate(null);
  }

  @Override
  public Set<FileObject> instantiate(ProgressHandle progressHandle) throws IOException {
    if (progressHandle != null) {
      progressHandle.start();
    }
    try {
      Set<FileObject> resultSet = new LinkedHashSet<FileObject>();
      File projectDirectory = FileUtil.normalizeFile((File) wizardDescriptor.getProperty("projdir"));
      projectDirectory.mkdirs();

      FileObject template = Templates.getTemplate(wizardDescriptor);
      FileObject projectDirectoryObject = FileUtil.toFileObject(projectDirectory);
      unZipFile(template.getInputStream(), projectDirectoryObject);

      // Always open top dir as a project:
      resultSet.add(projectDirectoryObject);

      File parent = projectDirectory.getParentFile();
      if (parent != null && parent.exists()) {
        ProjectChooser.setProjectsFolder(parent);
      }

      File aliceProjectFile = (File) wizardDescriptor.getProperty("aliceProjectFile");
      File javaSrcDirectory = new File(projectDirectory, "src");

      if (!javaSrcDirectory.exists()) {
        javaSrcDirectory.mkdirs();
      }

      //open source folder: does not seem to work when there are no existing open projects
      FileObject javaSrcDirectoryFileObject = (FileUtil.toFileObject(javaSrcDirectory));
      assert javaSrcDirectoryFileObject != null : javaSrcDirectory;
      resultSet.add(javaSrcDirectoryFileObject);

      try {
        Collection<FileObject> filesToOpen = ProjectCodeGenerator.generateCode(aliceProjectFile, javaSrcDirectory, progressHandle);
        resultSet.addAll(filesToOpen);
      } catch (MigrationException me) {
        Logger.throwable(me);
        notifyUserOfMigrationFailure();
        return null;
      } catch (Exception e) {
        Logger.throwable(e);
        notifyUserOfFailure();
        return null;
      }

      this.cleanSlateIfAppropriate();
      return resultSet;
    } finally {
      if (progressHandle != null) {
        progressHandle.finish();
      }
    }
  }

  private void notifyUserOfFailure() {
    // TODO I18n
    final String title = "Unable to Import Project";
    final String msg = "<html><h1>There was an error when loading this project.</h1><p>For details look in <i>Output - Alice3 Plugin</i></p></html>";
    JOptionPane.showMessageDialog(panels[0].getComponent(), msg, title, JOptionPane.ERROR_MESSAGE);
  }

  private void notifyUserOfMigrationFailure() {
    // TODO I18n
    final String title = "Unable to Migrate Project";
    final String msg = "<html><h1>There was an error when attempting to migrate this project from an earlier version of Alice.</h1>"
        + "<p>Use an up to date version of Alice to load and save a copy and then try this again in NetBeans.</p></html>";
    JOptionPane.showMessageDialog(panels[0].getComponent(), msg, title, JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public String name() {
    return MessageFormat.format("{0} of {1}", index + 1, panels.length);
  }

  @Override
  public boolean hasNext() {
    return this.index < this.panels.length - 1;
  }

  @Override
  public boolean hasPrevious() {
    return this.index > 0;
  }

  @Override
  public void nextPanel() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    this.index++;
  }

  @Override
  public void previousPanel() {
    if (!hasPrevious()) {
      throw new NoSuchElementException();
    }
    this.index--;
  }

  @Override
  public WizardDescriptor.Panel current() {
    return this.panels[index];
  }

  // If nothing unusual changes in the middle of the wizard, simply:
  @Override
  public final void addChangeListener(ChangeListener l) {
  }

  @Override
  public final void removeChangeListener(ChangeListener l) {
  }

  private static void unZipFile(InputStream source, FileObject projectRoot) throws IOException {
    try {
      ZipInputStream str = new ZipInputStream(source);
      ZipEntry entry;
      while ((entry = str.getNextEntry()) != null) {
        if (entry.isDirectory()) {
          FileUtil.createFolder(projectRoot, entry.getName());
        } else {
          FileObject fo = FileUtil.createData(projectRoot, entry.getName());
          if ("nbproject/project.xml".equals(entry.getName())) {
            // Special handling for setting name of Ant-based projects; customize as needed:
            filterProjectXML(fo, str, projectRoot.getName());
          } else {
            writeFile(str, fo);
          }
        }
      }
    } finally {
      source.close();
    }
  }

  private static void writeFile(ZipInputStream str, FileObject fo) throws IOException {
    OutputStream out = fo.getOutputStream();
    try {
      FileUtil.copy(str, out);
    } finally {
      out.close();
    }
  }

  private static void filterProjectXML(FileObject fo, ZipInputStream str, String name) throws IOException {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      FileUtil.copy(str, baos);
      Document doc = XMLUtil.parse(new InputSource(new ByteArrayInputStream(baos.toByteArray())), false, false, null, null);
      NodeList nl = doc.getDocumentElement().getElementsByTagName("name");
      if (nl != null) {
        for (int i = 0; i < nl.getLength(); i++) {
          Element el = (Element) nl.item(i);
          if (el.getParentNode() != null && "data".equals(el.getParentNode().getNodeName())) {
            NodeList nl2 = el.getChildNodes();
            if (nl2.getLength() > 0) {
              nl2.item(0).setNodeValue(name);
            }
            break;
          }
        }
      }
      OutputStream out = fo.getOutputStream();
      try {
        XMLUtil.write(doc, out, "UTF-8");
      } finally {
        out.close();
      }
    } catch (Exception ex) {
      Exceptions.printStackTrace(ex);
      writeFile(str, fo);
    }

  }

  private int index;
  private WizardDescriptor.Panel[] panels;
  private WizardDescriptor wizardDescriptor;
}
