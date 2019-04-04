/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */

import edu.cmu.cs.dennisc.image.ImageUtilities;
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.java.util.zip.DataSource;
import edu.cmu.cs.dennisc.pattern.IsInstanceCrawler;
import edu.cmu.cs.dennisc.render.OnscreenRenderTarget;
import org.alice.stageide.program.RunProgramContext;
import org.lgna.project.Project;
import org.lgna.project.ProjectVersion;
import org.lgna.project.Version;
import org.lgna.project.VersionNotSupportedException;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.CrawlPolicy;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.FieldReflectionProxy;
import org.lgna.project.ast.JavaField;
import org.lgna.project.io.IoUtilities;
import org.lgna.story.implementation.ProgramImp;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Dennis Cosgrove
 */
public class MigrateProjects extends Batch {
  private static final int WIDTH = 200;
  private static final int HEIGHT = 150 + 40;

  private void outputProblematicFiles() {
    Logger.errln();
    Logger.errln();
    Logger.errln();
    Logger.errln("problematic files:", this.problematicFiles.size());
    for (File problematicFile : this.problematicFiles) {
      Logger.errln(problematicFile);
    }

    Logger.errln("problematic java fields:", this.problematicJavaFields.size());
    for (JavaField javaField : this.problematicJavaFields) {
      FieldReflectionProxy fieldReflectionProxy = javaField.getFieldReflectionProxy();
      Logger.errln(fieldReflectionProxy.getDeclaringClassReflectionProxy().getName() + " " + fieldReflectionProxy.getName());
    }
    Logger.errln();
    Logger.errln();
    Logger.errln();
  }

  @Override
  protected void handle(File inFile, File outFile) {
    if (Logger.getLevel().intValue() < Level.SEVERE.intValue()) {
      Logger.outln(inFile);
    }
    try {
      Project project = IoUtilities.readProject(inFile);

      final boolean IS_DISPLAY_DESIRED = false;
      if (IS_DISPLAY_DESIRED) {
        final JFrame frame = new JFrame();
        frame.setLocation(x, y);
        frame.setSize(WIDTH, HEIGHT);
        x += WIDTH;
        if (x > 1600) {
          x = 0;
          y += HEIGHT;
        }
        frame.setVisible(true);

        RunProgramContext runProgramContext = new RunProgramContext(project.getProgramType());
        runProgramContext.initializeInContainer(new ProgramImp.AwtContainerInitializer() {
          @Override
          public void addComponents(OnscreenRenderTarget<?> onscreenRenderTarget, JPanel controlPanel) {
            frame.getContentPane().add(onscreenRenderTarget.getAwtComponent());
          }
        });
        runProgramContext.setActiveScene();
        runProgramContext.cleanUpProgram();
        frame.dispose();
      }

      IsInstanceCrawler<FieldAccess> crawler = IsInstanceCrawler.createInstance(FieldAccess.class);
      project.getProgramType().crawl(crawler, CrawlPolicy.COMPLETE);
      for (FieldAccess node : crawler.getList()) {
        if (node.isValid()) {
          //pass
        } else {
          AbstractField field = node.field.getValue();
          if (field instanceof JavaField) {
            JavaField javaField = (JavaField) field;
            this.problematicJavaFields.add(javaField);
          } else {
            assert false : node;
          }
        }
      }

      final boolean IS_WRITE_DESIRED = true;
      if (IS_WRITE_DESIRED) {
        ZipFile zipFile = new ZipFile(inFile);
        ZipEntry zipEntry = zipFile.getEntry("thumbnail.png");
        DataSource[] dataSources;
        if (zipEntry != null) {
          InputStream is = zipFile.getInputStream(zipEntry);
          Image image = ImageUtilities.read(ImageUtilities.PNG_CODEC_NAME, is);
          final byte[] data = ImageUtilities.writeToByteArray(ImageUtilities.PNG_CODEC_NAME, image);
          dataSources = new DataSource[] {new DataSource() {
            @Override
            public String getName() {
              return "thumbnail.png";
            }

            @Override
            public void write(OutputStream os) throws IOException {
              os.write(data);
            }
          }};
        } else {
          dataSources = new DataSource[] {};
        }
        IoUtilities.writeProject(outFile, project, dataSources);
      }

      if (Logger.getLevel().intValue() < Level.SEVERE.intValue()) {
        Logger.outln("success", inFile);
      }
    } catch (VersionNotSupportedException vnse) {
      throw new RuntimeException(inFile.toString(), vnse);
    } catch (IOException ioe) {
      throw new RuntimeException(inFile.toString(), ioe);
    } catch (Throwable t) {
      problematicFiles.add(inFile);
      this.outputProblematicFiles();
      t.printStackTrace();
    }
    Logger.outln();
    Logger.outln();
    Logger.outln();
    Logger.outln();
  }

  @Override
  protected boolean isSkipExistingOutFilesDesirable() {
    return false;
  }

  private int x = 0;
  private int y = 0;

  private final List<File> problematicFiles = Lists.newLinkedList();
  private final List<JavaField> problematicJavaFields = Lists.newLinkedList();

  public static void main(String[] args) {
    String inRootPath;
    String outRootPath;
    switch (args.length) {
    case 0:
      Version prevVersion = new Version("3.1.92.0.0");
      inRootPath = FileUtilities.getDefaultDirectory() + "/GalleryTest/" + prevVersion;
      outRootPath = inRootPath + "_FixedTo_" + ProjectVersion.getCurrentVersionText();
      break;
    case 1:
      inRootPath = args[0];
      outRootPath = inRootPath + "_migrated";
      break;
    case 2:
      inRootPath = args[0];
      outRootPath = args[1];
      break;
    default:
      throw new RuntimeException(Arrays.toString(args));
    }
    String ext = "a3p";
    MigrateProjects migrateProjects = new MigrateProjects();
    migrateProjects.process(inRootPath, outRootPath, ext, ext);
    migrateProjects.outputProblematicFiles();
    System.exit(0);
  }
}
