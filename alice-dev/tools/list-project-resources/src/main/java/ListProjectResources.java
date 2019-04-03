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

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.common.Resource;
import org.lgna.project.Project;
import org.lgna.project.VersionNotSupportedException;
import org.lgna.project.io.IoUtilities;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * @author Dennis Cosgrove
 */
public class ListProjectResources {
  protected void handle(File inFile) {
    try {
      Project project = IoUtilities.readProject(inFile);
      Set<Resource> resources = project.getResources();
      if (resources.size() > 0) {
        Logger.outln(inFile);
        for (Resource resource : resources) {
          Logger.outln("\t", resource.getData().length, resource);
        }
      }
    } catch (VersionNotSupportedException vnse) {
      throw new RuntimeException(inFile.toString(), vnse);
    } catch (IOException ioe) {
      throw new RuntimeException(inFile.toString(), ioe);
    }
  }

  public void process(String inRootPath, String inExt, String outExt) {
    File inRoot = new File(inRootPath);
    File[] inFiles = FileUtilities.listDescendants(inRoot, inExt);
    for (File inFile : inFiles) {
      handle(inFile);
    }
  }

  public static void main(String[] args) {
    String inRootPath = args[0];
    String ext = "a3p";
    ListProjectResources migrateProjects = new ListProjectResources();
    migrateProjects.process(inRootPath, ext, ext);
    System.exit(0);
  }
}
