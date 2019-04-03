/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
package edu.cmu.cs.dennisc.classpath;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.pattern.Criterion;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Dennis Cosgrove
 */
public class ClassPathUtilities {
  public static List<String> getClassPathEntries(URL url, final Criterion<String> filter) throws IOException {
    try {
      File root = new File(url.toURI());
      if (root.isDirectory()) {
        File[] descendants = FileUtilities.listDescendants(root, new FileFilter() {
          @Override
          public boolean accept(File f) {
            if (f.isDirectory()) {
              return false;
            } else {
              return filter.accept(f.getAbsolutePath());
            }
          }
        });
        List<String> rv = Lists.newArrayListWithInitialCapacity(descendants.length);
        for (File descendant : descendants) {
          String path = descendant.getAbsolutePath().substring(root.getAbsolutePath().length() + 1).replaceAll("\\\\", "/");
          rv.add(path);
        }
        return rv;
      } else {
        List<String> rv = Lists.newLinkedList();
        ZipInputStream zis = new ZipInputStream(new FileInputStream(root.getAbsoluteFile()));
        for (ZipEntry entry = zis.getNextEntry(); entry != null; entry = zis.getNextEntry()) {
          if (entry.isDirectory()) {
            //pass
          } else {
            String path = entry.getName();
            if (filter.accept(path)) {
              rv.add(path);
            }
          }
        }
        zis.close();
        return rv;
      }
    } catch (URISyntaxException urise) {
      throw new RuntimeException(urise);
    }
  }

  public static List<String> getClassPathEntries(CodeSource codeSource, final Criterion<String> filter) throws IOException {
    URL url = codeSource.getLocation();
    assert url != null : codeSource;
    return getClassPathEntries(url, filter);
  }

  public static List<String> getClassPathEntries(ProtectionDomain protectionDomain, final Criterion<String> filter) throws IOException {
    CodeSource codeSource = protectionDomain.getCodeSource();
    assert codeSource != null : protectionDomain;
    return getClassPathEntries(codeSource, filter);
  }

  public static List<String> getClassPathEntries(Class<?> cls, final Criterion<String> filter) throws IOException {
    ProtectionDomain protectionDomain = cls.getProtectionDomain();
    assert protectionDomain != null : cls;
    return getClassPathEntries(protectionDomain, filter);
  }
}
