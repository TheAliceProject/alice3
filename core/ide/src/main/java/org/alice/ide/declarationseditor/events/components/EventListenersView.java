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
package org.alice.ide.declarationseditor.events.components;

import edu.cmu.cs.dennisc.property.event.AddListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.ListPropertyListener;
import edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.SetListPropertyEvent;
import org.alice.ide.controlflow.ControlFlowComposite;
import org.alice.ide.croquet.models.ui.preferences.IsJavaCodeOnTheSideState;
import org.alice.ide.declarationseditor.CodeComposite;
import org.alice.ide.declarationseditor.code.components.AbstractCodeDeclarationView;
import org.alice.ide.declarationseditor.events.AddEventListenerCascade;
import org.alice.ide.project.ProjectChangeOfInterestManager;
import org.alice.ide.project.events.ProjectChangeOfInterestListener;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.LineAxisPanel;
import org.lgna.croquet.views.PopupButton;
import org.lgna.croquet.views.ScrollPane;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.UserCode;
import org.lgna.project.ast.UserMethod;

import javax.swing.BorderFactory;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;

public class EventListenersView extends AbstractCodeDeclarationView {
  public EventListenersView(CodeComposite composite) {
    super(composite, new EventsContentPanel((UserMethod) composite.getDeclaration()));
    PopupButton button = AddEventListenerCascade.getInstance().getRoot().getPopupPrepModel().createPopupButton();
    LineAxisPanel bottom = new LineAxisPanel(button);
    this.stickyBottomPanel = new StickyBottomPanel();
    this.stickyBottomPanel.setBottomView(bottom);
    //    this.scrollPane.getAwtComponent().getViewport().addChangeListener( new javax.swing.event.ChangeListener() {
    //      public void stateChanged( javax.swing.event.ChangeEvent e ) {
    //        Object src = e.getSource();
    //        if( src instanceof java.awt.Component ) {
    //          java.awt.Component awtComponent = (java.awt.Component)src;
    //          if( awtComponent.isValid() ) {
    //            //pass
    //          } else {
    //            stickyBottomPanel.revalidateAndRepaint();
    //          }
    //        }
    //      }
    //    } );

    this.stickyBottomPanel.setBackgroundColor(this.getBackgroundColor());
    this.stickyBottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
    this.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
    this.scrollPane.setBackgroundColor(this.getBackgroundColor());
    this.addPageEndComponent(ControlFlowComposite.getInstance(composite.getDeclaration()).getView());
  }

  @Override
  protected AwtComponentView<?> getMainComponent() {
    return this.stickyBottomPanel;
  }

  private JScrollBar getJVerticalScrollBar() {
    if (IsJavaCodeOnTheSideState.getInstance().getValue()) {
      return this.getSideBySideScrollPane().getAwtComponent().getVerticalScrollBar();
    } else {
      return this.scrollPane.getAwtComponent().getVerticalScrollBar();
    }
  }

  private void handleStatementsChanged(boolean isScrollDesired) {
    this.revalidateAndRepaint();
    if (isScrollDesired) {
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          JScrollBar verticalScrollBar = getJVerticalScrollBar();
          verticalScrollBar.setValue(verticalScrollBar.getMaximum());
        }
      });
    }
  }

  @Override
  protected void handleDisplayable() {
    super.handleDisplayable();
    CodeComposite codeComposite = (CodeComposite) this.getComposite();
    UserCode userCode = (UserCode) codeComposite.getDeclaration();
    userCode.getBodyProperty().getValue().statements.addListPropertyListener(this.statementsListener);

    //todo: remove
    ProjectChangeOfInterestManager.SINGLETON.addProjectChangeOfInterestListener(this.projectChangeOfInterestListener);
    //
  }

  @Override
  protected void handleUndisplayable() {
    //todo: remove
    ProjectChangeOfInterestManager.SINGLETON.removeProjectChangeOfInterestListener(this.projectChangeOfInterestListener);
    //

    CodeComposite codeComposite = (CodeComposite) this.getComposite();
    UserCode userCode = (UserCode) codeComposite.getDeclaration();
    userCode.getBodyProperty().getValue().statements.removeListPropertyListener(this.statementsListener);
    super.handleUndisplayable();
  }

  @Override
  protected void setJavaCodeOnTheSide(boolean value, boolean isFirstTime) {
    super.setJavaCodeOnTheSide(value, isFirstTime);
    SwingComponentView<?> codePanel = this.getCodePanelWithDropReceptor();
    if (value) {
      this.scrollPane.setViewportView(null);
      this.stickyBottomPanel.setTopView(codePanel);
    } else {
      if (isFirstTime) {
        //pass
      } else {
        this.stickyBottomPanel.removeComponent(codePanel);
      }
      this.scrollPane.setViewportView(codePanel);
      this.stickyBottomPanel.setTopView(this.scrollPane);
    }
  }

  private final ListPropertyListener<Statement> statementsListener = new ListPropertyListener<Statement>() {
    @Override
    public void adding(AddListPropertyEvent<Statement> e) {
    }

    @Override
    public void added(AddListPropertyEvent<Statement> e) {
      EventListenersView.this.handleStatementsChanged(true);
    }

    @Override
    public void clearing(ClearListPropertyEvent<Statement> e) {
    }

    @Override
    public void cleared(ClearListPropertyEvent<Statement> e) {
      EventListenersView.this.handleStatementsChanged(false);
    }

    @Override
    public void removing(RemoveListPropertyEvent<Statement> e) {
    }

    @Override
    public void removed(RemoveListPropertyEvent<Statement> e) {
      EventListenersView.this.handleStatementsChanged(false);
    }

    @Override
    public void setting(SetListPropertyEvent<Statement> e) {
    }

    @Override
    public void set(SetListPropertyEvent<Statement> e) {
      EventListenersView.this.handleStatementsChanged(false);
    }

  };

  private final ProjectChangeOfInterestListener projectChangeOfInterestListener = new ProjectChangeOfInterestListener() {
    @Override
    public void projectChanged() {
      revalidateAndRepaint();
    }
  };

  private final ScrollPane scrollPane = new ScrollPane();
  private final StickyBottomPanel stickyBottomPanel;

}
