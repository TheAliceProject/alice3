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
package org.alice.ide.localize.review.core;

import edu.cmu.cs.dennisc.classpath.ClassPathUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.pattern.Criterion;
import org.alice.ide.IDE;
import org.alice.imageeditor.croquet.ImageEditorFrame;
import org.alice.interact.handle.HandleStyle;
import org.lgna.croquet.Element;
import org.lgna.project.ast.AbstractNode;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dennis Cosgrove
 */
public class LocalizeUtils {
  private LocalizeUtils() {
    throw new Error();
  }

  public static List<String> getTags(String value) {
    Pattern pattern = Pattern.compile("</(.+?)/>");
    Matcher matcher = pattern.matcher(value);

    List<String> tags = Lists.newLinkedList();
    while (matcher.find()) {
      tags.add(matcher.group(0));
    }
    return Collections.unmodifiableList(tags);
  }

  public static List<Item> getItems(Class<? extends IDE> specificIdeCls, String specificIdeProjectName) {
    final String SUFFIX = ".properties";

    List<ClassProjectNamePair> clsProjectNamePairs = Lists.newLinkedList();

    clsProjectNamePairs.add(new ClassProjectNamePair(Element.class, "croquet"));
    clsProjectNamePairs.add(new ClassProjectNamePair(AbstractNode.class, "ast"));
    clsProjectNamePairs.add(new ClassProjectNamePair(HandleStyle.class, "story-api"));
    clsProjectNamePairs.add(new ClassProjectNamePair(ImageEditorFrame.class, "image-editor"));
    clsProjectNamePairs.add(new ClassProjectNamePair(IDE.class, "ide"));

    clsProjectNamePairs.add(new ClassProjectNamePair(specificIdeCls, specificIdeProjectName));

    List<Item> _allItems = Lists.newLinkedList();

    Locale prevLocale = Locale.getDefault();
    Locale.setDefault(new Locale("en", "US"));

    try {

      for (ClassProjectNamePair clsProjectNamePair : clsProjectNamePairs) {
        List<String> classPathEntries;
        try {
          classPathEntries = ClassPathUtilities.getClassPathEntries(clsProjectNamePair.getCls(), new Criterion<String>() {
            @Override
            public boolean accept(String path) {
              if (path.startsWith("META-INF/")) {
                return false;
              } else {
                if (path.endsWith(SUFFIX)) {
                  return path.contains("_") == false;
                } else {
                  return false;
                }
              }
            }
          });
        } catch (IOException ioe) {
          throw new RuntimeException(ioe);
        }
        for (String classPathEntry : classPathEntries) {
          String bundleName = classPathEntry.substring(0, classPathEntry.length() - SUFFIX.length());
          try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName);
            for (String key : resourceBundle.keySet()) {
              _allItems.add(new Item(clsProjectNamePair.getProjectName(), bundleName, key, resourceBundle.getString(key)));
            }
          } catch (Throwable t) {
            Logger.severe("unable to get resource bundle for:", bundleName);
          }
        }
      }
    } finally {
      Locale.setDefault(prevLocale);
    }

    return Collections.unmodifiableList(_allItems);
  }
}
