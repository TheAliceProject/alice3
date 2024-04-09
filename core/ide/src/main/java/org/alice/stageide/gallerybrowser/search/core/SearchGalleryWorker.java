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
package org.alice.stageide.gallerybrowser.search.core;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.worker.WorkerWithProgress;
import org.alice.stageide.gallerybrowser.search.croquet.views.SearchTabView;
import org.alice.stageide.modelresource.ResourceNode;
import org.alice.stageide.modelresource.TreeUtilities;

import java.util.Collections;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class SearchGalleryWorker extends WorkerWithProgress<List<ResourceNode>, ResourceNode> {

  public SearchGalleryWorker(String filter, SearchTabView searchTabView) {
    // Split into lowercase terms by spaces and any non word characters.
    // Search for each term then AND the results together.
    terms = filter.toLowerCase().replaceAll("\\W|_", " ").split("\\s+");
    this.searchTabView = searchTabView;
  }

  @Override
  protected List<ResourceNode> do_onBackgroundThread() {
    if (terms.length == 0) {
      return Collections.emptyList();
    }
    List<ResourceNode> matchingNodes = Lists.newLinkedList();
    appendMatches(matchingNodes, TreeUtilities.getTreeBasedOnClassHierarchy());
    return matchingNodes;
  }

  @Override
  protected void handleProcess_onEventDispatchThread(List<ResourceNode> chunks) {
    searchTabView.addGalleryDragComponents(chunks);
  }

  @Override
  protected void handleDone_onEventDispatchThread(List<ResourceNode> matchingNodes) {
    searchTabView.setComponentsToGalleryDragComponents(terms, matchingNodes);
  }

  private void appendMatches(List<ResourceNode> matches, ResourceNode node) {
    if (isCancelled()) {
      return;
    }
    if (!matches.contains(node) && allTermsMatch(node)) {
      matches.add(node);
      publish(node);
    }
    if (!node.getResourceKey().isLeaf()) {
      for (ResourceNode child : node.getNodeChildren()) {
        appendMatches(matches, child);
      }
    }
  }

  private boolean allTermsMatch(ResourceNode node) {
    for (String term : terms) {
      boolean termFound = false;
      String[] tags = node.getResourceKey().getTags();
      if (tags != null) {
        for (String tag : tags) {
          if (tag.toLowerCase().contains(term)) {
            termFound = true;
            break;
          }
        }
      }
      if (termFound) {
        // A tag matched. Go to the next term.
        continue;
      }
      // No tag matched. Check in search text.
      String searchText = node.getResourceKey().getSearchText();
      if (searchText == null || searchText.isEmpty() || !searchText.toLowerCase().contains(term)) {
        // This term was not found, so this node does not match.
        return false;
      }
    }
    // All terms found on this node.
    return true;
  }

  private final String[] terms;
  private final SearchTabView searchTabView;
}
