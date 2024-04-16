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
package org.alice.ide.croquet.models.project.find.croquet.views.renderers;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer;
import org.alice.ide.croquet.models.project.find.core.SearchResult;
import org.alice.ide.croquet.models.project.find.croquet.AbstractFindComposite;

import javax.swing.JLabel;
import javax.swing.JList;

/**
 * @author Dennis Cosgrove
 */
public class SearchResultListCellRenderer extends ListCellRenderer<SearchResult> {

  private final AbstractFindComposite composite;

  public SearchResultListCellRenderer(AbstractFindComposite composite) {
    this.composite = composite;
  }

  @Override
  protected JLabel getListCellRendererComponent(JLabel rv, JList list, SearchResult value, int index, boolean isSelected, boolean cellHasFocus) {
    StringBuilder sb = new StringBuilder();
    sb.append("<HTML>");
    sb.append(getHighlightedName(value.getName(), composite.getSearchTerms()));
    sb.append(" (");
    sb.append(value.getReferences().size());
    sb.append(")");
    sb.append("</HTML>");
    rv.setText(sb.toString());
    rv.setIcon(value.getIcon());
    return rv;
  }

  private String getHighlightedName(String name, String[] terms) {
    TreeMap<Integer, Integer> highlights = findAllTerms(terms, name.toLowerCase());
    collapseOverlaps(highlights);
    return highlightedTerms(name, highlights);
  }

  private static TreeMap<Integer, Integer> findAllTerms(String[] terms, String toSearch) {
    TreeMap<Integer, Integer> highlights = new TreeMap<>();
    for (String term : terms) {
      // Each term was already found by search in FindContentManager, so they will be in the string.
      int start = toSearch.indexOf(term.toLowerCase());
      int end = start + term.length();
      highlights.put(start, end);
    }
    return highlights;
  }

  private static void collapseOverlaps(TreeMap<Integer, Integer> highlights) {
    // Copy keys to avoid ConcurrentModificationException
    List<Integer> starts = new ArrayList<>(highlights.navigableKeySet());
    int lastEnd = -1;
    int lastStart = -1;
    for (Integer start : starts) {
      if (start > lastEnd) {
        // No overlap. Keep checking
        lastStart = start;
        lastEnd = highlights.get(start);
      } else {
        // The two strings found overlap.
        // Collapse into last highlight.
        Integer end = highlights.get(start);
        lastEnd = lastEnd > end ? lastEnd : end;
        highlights.put(lastStart, lastEnd);
        // And remove the one just checked.
        highlights.remove(start);
      }
    }
  }

  private static String highlightedTerms(String name, TreeMap<Integer, Integer> highlights) {
    int lastStop = 0;
    StringBuilder html = new StringBuilder();
    for (Integer start : highlights.navigableKeySet()) {
      html.append(name, lastStop, start)
          .append("<strong>")
          .append(name, start, highlights.get(start))
          .append("</strong>");
      lastStop = highlights.get(start);
    }
    html.append(name, lastStop, name.length());
    return html.toString();
  }
}
