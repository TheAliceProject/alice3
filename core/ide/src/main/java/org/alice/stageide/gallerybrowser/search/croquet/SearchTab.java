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
package org.alice.stageide.gallerybrowser.search.croquet;

import org.alice.stageide.gallerybrowser.GalleryTab;
import org.alice.stageide.gallerybrowser.search.core.SearchGalleryWorker;
import org.alice.stageide.gallerybrowser.search.croquet.views.SearchTabView;
import org.lgna.croquet.PlainStringValue;
import org.lgna.croquet.StringState;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class SearchTab extends GalleryTab {
  public SearchTab() {
    super(UUID.fromString("4e3e7dc2-c8ed-4e8c-9028-9493a19ba50d"));
  }

  public StringState getFilterState() {
    return this.filterState;
  }

  public PlainStringValue getNoMatchesLabel() {
    return this.noMatchesLabel;
  }

  public PlainStringValue getNoEntryLabel() {
    return this.noEntryLabel;
  }

  @Override
  protected SearchTabView createView() {
    return new SearchTabView(this);
  }

  @Override
  public void handlePreActivation() {
    super.handlePreActivation();
    this.getFilterState().addAndInvokeNewSchoolValueListener(this.filterListener);
  }

  @Override
  public void handlePostDeactivation() {
    this.getFilterState().removeNewSchoolValueListener(this.filterListener);
    this.cancelWorkerIfNecessary();
    super.handlePostDeactivation();
  }

  private void cancelWorkerIfNecessary() {
    if (this.worker != null) {
      if (this.worker.isDone()) {
        //pass
      } else {
        this.worker.cancel(false);
      }
      this.worker = null;
    }
  }

  private void handleFilterChanged(String filter) {
    this.cancelWorkerIfNecessary();
    SearchTabView view = (SearchTabView) this.getView();
    synchronized (view.getTreeLock()) {
      view.removeAllGalleryDragComponents();
    }
    this.worker = new SearchGalleryWorker(filter, view);
    this.worker.execute();
  }

  @Override
  public void modelUpdated() {
    handleFilterChanged(getFilterState().getValue());
    super.modelUpdated();
  }

  private SearchGalleryWorker worker;
  private final StringState filterState = this.createStringState("filterState");
  private final PlainStringValue noMatchesLabel = this.createStringValue("noMatchesLabel");
  private final PlainStringValue noEntryLabel = this.createStringValue("noEntryLabel");
  private final ValueListener<String> filterListener = new ValueListener<String>() {
    @Override
    public void valueChanged(ValueEvent<String> e) {
      SearchTab.this.handleFilterChanged(e.getNextValue());
    }
  };
}
