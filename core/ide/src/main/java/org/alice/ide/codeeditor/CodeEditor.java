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
package org.alice.ide.codeeditor;

import org.alice.ide.ast.draganddrop.BlockStatementIndexPair;
import org.alice.ide.x.components.StatementListPropertyView;

/**
 * @author Dennis Cosgrove
 */
public class CodeEditor extends org.alice.ide.codedrop.CodePanelWithDropReceptor {
	public CodeEditor( org.alice.ide.x.AbstractProjectEditorAstI18nFactory factory, org.lgna.project.ast.AbstractCode code ) {
		this.code = code;
		assert this.code instanceof org.lgna.project.ast.UserCode : this.code;

		org.lgna.project.ast.UserCode userCode = (org.lgna.project.ast.UserCode)this.code;

		org.lgna.project.ast.BlockStatement body = userCode.getBodyProperty().getValue();

		this.rootStatementListPropertyPane = new StatementListPropertyView( factory, body.statements, 32 );

		org.lgna.croquet.views.SwingComponentView<?> statementListComponent = null;
		if( body instanceof org.lgna.project.ast.ConstructorBlockStatement ) {
			org.lgna.project.ast.ConstructorBlockStatement constructorBlockStatement = (org.lgna.project.ast.ConstructorBlockStatement)body;
			org.lgna.project.ast.ConstructorInvocationStatement constructorInvocationStatement = constructorBlockStatement.constructorInvocationStatement.getValue();
			if( constructorInvocationStatement != null ) {
				org.lgna.croquet.views.SwingComponentView<?> superComponent = org.alice.ide.x.ProjectEditorAstI18nFactory.getInstance().createStatementPane( constructorInvocationStatement );
				statementListComponent = new org.lgna.croquet.views.PageAxisPanel( new org.lgna.croquet.views.LineAxisPanel( org.lgna.croquet.views.BoxUtilities.createHorizontalSliver( 8 ), superComponent ), this.rootStatementListPropertyPane );
			}
		}

		if( statementListComponent != null ) {
			//pass
		} else {
			statementListComponent = this.rootStatementListPropertyPane;
		}

		this.bodyPane = new org.alice.ide.common.BodyPane( statementListComponent );

		this.scrollPane.getAwtComponent().getViewport().setOpaque( false );
		this.scrollPane.setAlignmentX( javax.swing.JComponent.LEFT_ALIGNMENT );

		this.header = (org.lgna.croquet.views.Panel)factory.createCodeHeader( (org.lgna.project.ast.UserCode)this.code );
		this.addPageStartComponent( this.header );

		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		java.awt.Color color = org.alice.ide.ThemeUtilities.getActiveTheme().getCodeColor( this.code );
		this.setBackgroundColor( color );
	}

	@Override
	public org.lgna.croquet.views.TrackableShape getTrackableShape( org.lgna.croquet.DropSite potentialDropSite ) {
		if( potentialDropSite instanceof BlockStatementIndexPair ) {
			BlockStatementIndexPair blockStatementIndexPair = (BlockStatementIndexPair)potentialDropSite;
			org.lgna.project.ast.StatementListProperty statementListProperty = blockStatementIndexPair.getBlockStatement().statements;
			int index = Math.max( 0, blockStatementIndexPair.getIndex() );
			return this.getTrackableShapeAtIndexOf( statementListProperty, index, false );
		} else {
			return null;
		}
	}

	@Override
	public org.lgna.project.ast.AbstractCode getCode() {
		return this.code;
	}

	private final org.lgna.croquet.event.ValueListener<Boolean> typeFeedbackListener = new org.lgna.croquet.event.ValueListener<Boolean>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<Boolean> e ) {
			CodeEditor.this.rootStatementListPropertyPane.refreshLater();
		}
	};
	private final org.lgna.croquet.event.ValueListener<org.alice.ide.formatter.Formatter> formatterListener = new org.lgna.croquet.event.ValueListener<org.alice.ide.formatter.Formatter>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.ide.formatter.Formatter> e ) {
			CodeEditor.this.header.refreshLater();
			CodeEditor.this.rootStatementListPropertyPane.refreshLater();
		}
	};

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		org.alice.ide.croquet.models.ui.formatter.FormatterState.getInstance().addNewSchoolValueListener( this.formatterListener );
		org.alice.ide.croquet.models.ui.preferences.IsIncludingTypeFeedbackForExpressionsState.getInstance().addAndInvokeNewSchoolValueListener( this.typeFeedbackListener );
	}

	@Override
	protected void handleUndisplayable() {
		org.alice.ide.croquet.models.ui.preferences.IsIncludingTypeFeedbackForExpressionsState.getInstance().removeNewSchoolValueListener( this.typeFeedbackListener );
		org.alice.ide.croquet.models.ui.formatter.FormatterState.getInstance().removeNewSchoolValueListener( this.formatterListener );
		super.handleUndisplayable();
	}

	private static int convertY( org.lgna.croquet.views.AwtComponentView<?> from, int y, org.lgna.croquet.views.AwtComponentView<?> to ) {
		java.awt.Point pt = from.convertPoint( new java.awt.Point( 0, y ), to );
		return pt.y;
	}

	private static int capMinimum( int yPotentialMinimumBound, int y, StatementListPropertyPaneInfo[] statementListPropertyPaneInfos, int index ) {
		int rv = yPotentialMinimumBound;
		final int N = statementListPropertyPaneInfos.length;
		for( int i = 0; i < N; i++ ) {
			if( i == index ) {
				//pass
			} else {
				java.awt.Rectangle boundsI = statementListPropertyPaneInfos[ i ].getBounds();
				int yI = boundsI.y + boundsI.height;
				if( yI < y ) {
					rv = Math.max( rv, yI );
				}
			}
		}
		return rv;
	}

	private static int capMaximum( int yMaximum, int yPlusHeight, StatementListPropertyPaneInfo[] statementListPropertyPaneInfos, int index ) {
		int rv = yMaximum;
		final int N = statementListPropertyPaneInfos.length;
		for( int i = 0; i < N; i++ ) {
			if( i == index ) {
				//pass
			} else {
				java.awt.Rectangle boundsI = statementListPropertyPaneInfos[ i ].getBounds();
				int yI = boundsI.y;
				if( yI > yPlusHeight ) {
					rv = Math.min( rv, yI );
				}
			}
		}
		return rv;
	}

	private static boolean isWarningAlreadyPrinted = false;

	public class StatementListIndexTrackableShape implements org.lgna.croquet.views.TrackableShape {
		private org.lgna.project.ast.StatementListProperty statementListProperty;
		private int index;
		private StatementListPropertyView statementListPropertyPane;
		private java.awt.Rectangle boundsAtIndex;

		private StatementListIndexTrackableShape( org.lgna.project.ast.StatementListProperty statementListProperty, int index, StatementListPropertyView statementListPropertyPane, java.awt.Rectangle boundsAtIndex ) {
			this.statementListProperty = statementListProperty;
			this.index = index;
			this.statementListPropertyPane = statementListPropertyPane;
			this.boundsAtIndex = boundsAtIndex;
		}

		private org.lgna.project.ast.StatementListProperty getStatementListProperty() {
			return this.statementListProperty;
		}

		public org.lgna.project.ast.BlockStatement getBlockStatement() {
			return (org.lgna.project.ast.BlockStatement)this.statementListProperty.getOwner();
		}

		public int getIndex() {
			return this.index;
		}

		@Override
		public java.awt.Shape getShape( org.lgna.croquet.views.ScreenElement asSeenBy, java.awt.Insets insets ) {
			org.lgna.croquet.views.AwtComponentView<?> src = CodeEditor.this.getAsSeenBy();
			if( src != null ) {
				java.awt.Rectangle rv = src.convertRectangle( this.boundsAtIndex, asSeenBy );
				//note: ignore insets
				return rv;
			} else {
				return null;
			}
		}

		@Override
		public java.awt.Shape getVisibleShape( org.lgna.croquet.views.ScreenElement asSeenBy, java.awt.Insets insets ) {
			org.lgna.croquet.views.AwtComponentView<?> src = CodeEditor.this.getAsSeenBy();
			if( src != null ) {
				java.awt.Rectangle bounds = src.convertRectangle( this.boundsAtIndex, asSeenBy );
				//note: ignore insets
				//					java.awt.Rectangle visibleBounds = statementListPropertyPane.getVisibleRectangle( asSeenBy );
				//					return bounds.intersection( visibleBounds );
				return bounds;
			} else {
				return null;
			}
		}

		@Override
		public boolean isInView() {
			if( isWarningAlreadyPrinted ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.info( "getTrackableShapeAtIndexOf" );
				isWarningAlreadyPrinted = true;
			}
			return true;
		}

		@Override
		public org.lgna.croquet.views.ScrollPane getScrollPaneAncestor() {
			return this.statementListPropertyPane.getScrollPaneAncestor();
		}

		@Override
		public void addComponentListener( java.awt.event.ComponentListener listener ) {
			this.statementListPropertyPane.addComponentListener( listener );
		}

		@Override
		public void removeComponentListener( java.awt.event.ComponentListener listener ) {
			this.statementListPropertyPane.removeComponentListener( listener );
		}

		@Override
		public void addHierarchyBoundsListener( java.awt.event.HierarchyBoundsListener listener ) {
			this.statementListPropertyPane.addHierarchyBoundsListener( listener );
		}

		@Override
		public void removeHierarchyBoundsListener( java.awt.event.HierarchyBoundsListener listener ) {
			this.statementListPropertyPane.removeHierarchyBoundsListener( listener );
		}
	}

	public org.lgna.croquet.views.TrackableShape getTrackableShapeAtIndexOf( org.lgna.project.ast.StatementListProperty statementListProperty, int index, boolean EPIC_HACK_isDropConstraintDesired ) {
		if( statementListProperty != null ) {
			//choose any non-ancestor

			org.lgna.croquet.views.AwtContainerView<?> arbitrarilyChosenSource = org.alice.ide.IDE.getActiveInstance().getSceneEditor();
			org.lgna.croquet.DragModel dragModel = null;
			edu.cmu.cs.dennisc.java.util.logging.Logger.todo( dragModel );
			StatementListPropertyPaneInfo[] statementListPropertyPaneInfos = this.getDropReceptor().createStatementListPropertyPaneInfos( dragModel, arbitrarilyChosenSource );
			final int N = statementListPropertyPaneInfos.length;
			for( int i = 0; i < N; i++ ) {
				StatementListPropertyPaneInfo statementListPropertyPaneInfo = statementListPropertyPaneInfos[ i ];
				StatementListPropertyView statementListPropertyPane = statementListPropertyPaneInfo.getStatementListPropertyPane();
				if( statementListPropertyPane.getProperty() == statementListProperty ) {
					StatementListPropertyView.BoundInformation yBounds = statementListPropertyPane.calculateYBounds( index );
					java.awt.Rectangle bounds = statementListPropertyPaneInfo.getBounds();

					int yMinimum;
					if( ( yBounds.yMinimum != null ) && ( yBounds.y != null ) ) {
						yMinimum = convertY( statementListPropertyPane, yBounds.yMinimum, CodeEditor.this.getAsSeenBy() );
						int y = convertY( statementListPropertyPane, yBounds.y, CodeEditor.this.getAsSeenBy() );
						yMinimum = capMinimum( yMinimum, y, statementListPropertyPaneInfos, index );
					} else {
						yMinimum = bounds.y;
					}
					int yMaximum;
					if( ( yBounds.yMaximum != null ) && ( yBounds.yPlusHeight != null ) ) {
						yMaximum = convertY( statementListPropertyPane, yBounds.yMaximum, CodeEditor.this.getAsSeenBy() );
						int yPlusHeight = convertY( statementListPropertyPane, yBounds.yPlusHeight, CodeEditor.this.getAsSeenBy() );
						yMaximum = capMaximum( yMaximum, yPlusHeight, statementListPropertyPaneInfos, index );
					} else {
						yMaximum = ( bounds.y + bounds.height ) - 1;
					}

					java.awt.Rectangle boundsAtIndex = new java.awt.Rectangle( bounds.x, yMinimum, bounds.width, ( yMaximum - yMinimum ) + 1 );

					return new StatementListIndexTrackableShape( statementListProperty, index, statementListPropertyPane, boundsAtIndex );
				}
			}
			//			org.lgna.project.ast.Node a = ((org.lgna.project.ast.BlockStatement)statementListProperty.getOwner()).getParent();
			//			org.lgna.project.ast.Node b = ((org.lgna.project.ast.BlockStatement)statementListPropertyPaneInfos[0].getStatementListPropertyPane().getProperty().getOwner()).getParent();
			//			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( a, b, a.hashCode(), b.hashCode(), a.getId(), b.getId() );
		}

		return null;
	}

	@Override
	protected org.lgna.croquet.views.AwtComponentView<?> getAsSeenBy() {
		return this.bodyPane;
	}

	@Override
	public java.awt.print.Printable getPrintable() {
		return new edu.cmu.cs.dennisc.java.awt.PrintHelper.Builder( this.getInsets(), this.getBackgroundColor() )
				.center( this.getCenterComponent().getAwtComponent() )
				.pageStart( this.getPageStartComponent().getAwtComponent() )
				.build();
	}

	@Override
	public void setJavaCodeOnTheSide( boolean value, boolean isFirstTime ) {
		if( value ) {
			if( isFirstTime ) {
				//pass
			} else {
				this.removeComponent( this.scrollPane );
			}
			this.scrollPane.setViewportView( null );
			this.addCenterComponent( this.bodyPane );
		} else {
			if( isFirstTime ) {
				//pass
			} else {
				this.removeComponent( this.bodyPane );
			}
			this.scrollPane.setViewportView( this.bodyPane );
			this.addCenterComponent( this.scrollPane );
		}
	}

	private final org.lgna.project.ast.AbstractCode code;
	private final org.lgna.croquet.views.Panel header;
	private final StatementListPropertyView rootStatementListPropertyPane;
	private final org.alice.ide.common.BodyPane bodyPane;
	private final org.lgna.croquet.views.ScrollPane scrollPane = new org.lgna.croquet.views.ScrollPane();
}
