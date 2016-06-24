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

import org.alice.ide.controlflow.ControlFlowComposite;
import org.alice.ide.declarationseditor.events.AddEventListenerCascade;
import org.lgna.croquet.views.LineAxisPanel;
import org.lgna.croquet.views.PopupButton;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.UserCode;

public class EventListenersView extends org.alice.ide.declarationseditor.code.components.AbstractCodeDeclarationView {
	public EventListenersView( org.alice.ide.declarationseditor.CodeComposite composite ) {
		super( composite, new EventsContentPanel( (org.lgna.project.ast.UserMethod)composite.getDeclaration() ) );
		PopupButton button = AddEventListenerCascade.getInstance().getRoot().getPopupPrepModel().createPopupButton();
		LineAxisPanel bottom = new LineAxisPanel( button );
		this.stickyBottomPanel = new StickyBottomPanel();
		this.stickyBottomPanel.setBottomView( bottom );
		//		this.scrollPane.getAwtComponent().getViewport().addChangeListener( new javax.swing.event.ChangeListener() {
		//			public void stateChanged( javax.swing.event.ChangeEvent e ) {
		//				Object src = e.getSource();
		//				if( src instanceof java.awt.Component ) {
		//					java.awt.Component awtComponent = (java.awt.Component)src;
		//					if( awtComponent.isValid() ) {
		//						//pass
		//					} else {
		//						stickyBottomPanel.revalidateAndRepaint();
		//					}
		//				}
		//			}
		//		} );

		this.stickyBottomPanel.setBackgroundColor( this.getBackgroundColor() );
		this.stickyBottomPanel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 0, 12, 0 ) );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 8, 4, 8 ) );
		this.scrollPane.setBackgroundColor( this.getBackgroundColor() );
		if( org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().getValue() ) {
			this.addPageEndComponent( ControlFlowComposite.getInstance( composite.getDeclaration() ).getView() );
		}
	}

	@Override
	protected org.lgna.croquet.views.AwtComponentView<?> getMainComponent() {
		return this.stickyBottomPanel;
	}

	private javax.swing.JScrollBar getJVerticalScrollBar() {
		if( org.alice.ide.croquet.models.ui.preferences.IsJavaCodeOnTheSideState.getInstance().getValue() ) {
			return this.getSideBySideScrollPane().getAwtComponent().getVerticalScrollBar();
		} else {
			return this.scrollPane.getAwtComponent().getVerticalScrollBar();
		}
	}

	private void handleStatementsChanged( boolean isScrollDesired ) {
		this.revalidateAndRepaint();
		if( isScrollDesired ) {
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					javax.swing.JScrollBar verticalScrollBar = getJVerticalScrollBar();
					verticalScrollBar.setValue( verticalScrollBar.getMaximum() );
				}
			} );
		}
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		org.alice.ide.declarationseditor.CodeComposite codeComposite = (org.alice.ide.declarationseditor.CodeComposite)this.getComposite();
		UserCode userCode = (UserCode)codeComposite.getDeclaration();
		userCode.getBodyProperty().getValue().statements.addListPropertyListener( this.statementsListener );

		//todo: remove
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.addProjectChangeOfInterestListener( this.projectChangeOfInterestListener );
		//
	}

	@Override
	protected void handleUndisplayable() {
		//todo: remove
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.removeProjectChangeOfInterestListener( this.projectChangeOfInterestListener );
		//

		org.alice.ide.declarationseditor.CodeComposite codeComposite = (org.alice.ide.declarationseditor.CodeComposite)this.getComposite();
		UserCode userCode = (UserCode)codeComposite.getDeclaration();
		userCode.getBodyProperty().getValue().statements.removeListPropertyListener( this.statementsListener );
		super.handleUndisplayable();
	}

	@Override
	protected void setJavaCodeOnTheSide( boolean value, boolean isFirstTime ) {
		super.setJavaCodeOnTheSide( value, isFirstTime );
		org.lgna.croquet.views.SwingComponentView<?> codePanel = this.getCodePanelWithDropReceptor();
		if( value ) {
			this.scrollPane.setViewportView( null );
			this.stickyBottomPanel.setTopView( codePanel );
		} else {
			if( isFirstTime ) {
				//pass
			} else {
				this.stickyBottomPanel.removeComponent( codePanel );
			}
			this.scrollPane.setViewportView( codePanel );
			this.stickyBottomPanel.setTopView( this.scrollPane );
		}
	}

	private final edu.cmu.cs.dennisc.property.event.ListPropertyListener<Statement> statementsListener = new edu.cmu.cs.dennisc.property.event.ListPropertyListener<Statement>() {
		@Override
		public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent<Statement> e ) {
		}

		@Override
		public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent<Statement> e ) {
			EventListenersView.this.handleStatementsChanged( true );
		}

		@Override
		public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent<Statement> e ) {
		}

		@Override
		public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent<Statement> e ) {
			EventListenersView.this.handleStatementsChanged( false );
		}

		@Override
		public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent<Statement> e ) {
		}

		@Override
		public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent<Statement> e ) {
			EventListenersView.this.handleStatementsChanged( false );
		}

		@Override
		public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent<Statement> e ) {
		}

		@Override
		public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent<Statement> e ) {
			EventListenersView.this.handleStatementsChanged( false );
		}

	};

	private final org.alice.ide.project.events.ProjectChangeOfInterestListener projectChangeOfInterestListener = new org.alice.ide.project.events.ProjectChangeOfInterestListener() {
		@Override
		public void projectChanged() {
			revalidateAndRepaint();
		}
	};

	private final org.lgna.croquet.views.ScrollPane scrollPane = new org.lgna.croquet.views.ScrollPane();
	private final StickyBottomPanel stickyBottomPanel;

}
