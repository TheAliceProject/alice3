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
package org.alice.ide.codeeditor;

import org.alice.ide.ast.draganddrop.BlockStatementIndexPair;
import org.alice.ide.x.components.StatementListPropertyView;

/**
 * @author Dennis Cosgrove
 */
public class CodeEditor extends org.alice.ide.codedrop.CodeDropReceptor {
	private static class RootStatementListPropertyPane extends StatementListPropertyView {
		private final org.lgna.croquet.components.Component< ? > superInvocationComponent;
		public RootStatementListPropertyPane( org.lgna.project.ast.UserCode userCode ) {
			super( org.alice.ide.x.EditableAstI18Factory.getProjectGroupInstance(), userCode.getBodyProperty().getValue().statements );
			this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 0, 48, 0 ) );
			org.lgna.project.ast.BlockStatement body = userCode.getBodyProperty().getValue();
			if( body instanceof org.lgna.project.ast.ConstructorBlockStatement ) {
				org.lgna.project.ast.ConstructorBlockStatement constructorBlockStatement = (org.lgna.project.ast.ConstructorBlockStatement)body;
				org.lgna.project.ast.ConstructorInvocationStatement	constructorInvocationStatement = constructorBlockStatement.constructorInvocationStatement.getValue();
				assert constructorInvocationStatement != null;
				superInvocationComponent = org.alice.ide.x.PreviewAstI18nFactory.getInstance().createStatementPane( constructorInvocationStatement );
			} else {
				superInvocationComponent = null;
			}
		}
		@Override
		protected void addPrefixComponents() {
			super.addPrefixComponents();
			if( this.superInvocationComponent != null ) {
				this.addComponent( this.superInvocationComponent );
			}
		}
	}

	private final org.lgna.project.ast.AbstractCode code;
	//private final org.lgna.croquet.components.ScrollPane scrollPane;
	private final RootStatementListPropertyPane rootStatementListPropertyPane;

	@Deprecated
	public static class Resolver implements org.lgna.croquet.resolvers.RetargetableResolver< CodeEditor > {
		private org.lgna.project.ast.AbstractCode code;
		public Resolver( org.lgna.project.ast.AbstractCode code ) {
			this.code = code;
		}
		public Resolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			java.util.UUID id = binaryDecoder.decodeId();
			org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
			this.code = org.lgna.project.ProgramTypeUtilities.lookupNode( ide.getProject(), id );
		}
		public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
			// TODO Auto-generated method stub
			binaryEncoder.encode( this.code.getId() );
		}
		public org.alice.ide.codeeditor.CodeEditor getResolved() {
			return (org.alice.ide.codeeditor.CodeEditor)((org.alice.ide.declarationseditor.code.components.CodeDeclarationView)org.alice.ide.declarationseditor.DeclarationComposite.getInstance( this.code ).getView()).getCodeDropReceptor();
		}
		public void retarget( org.lgna.croquet.Retargeter retargeter ) {
			this.code = retargeter.retarget( this.code );
		}
	}

	public CodeEditor( org.lgna.project.ast.AbstractCode code ) {
		this.code = code;
		assert this.code instanceof org.lgna.project.ast.UserCode;
		this.rootStatementListPropertyPane = new RootStatementListPropertyPane( (org.lgna.project.ast.UserCode)this.code );
		org.alice.ide.common.BodyPane bodyPane = new org.alice.ide.common.BodyPane( this.rootStatementListPropertyPane );

		org.lgna.croquet.components.ScrollPane scrollPane = this.getScrollPane();
		scrollPane.setViewportView( bodyPane );
		scrollPane.setBothScrollBarIncrements( 12, 24 );
		scrollPane.setBorder( null );
		scrollPane.setBackgroundColor( null );
		scrollPane.getAwtComponent().getViewport().setOpaque( false );
		scrollPane.setAlignmentX( javax.swing.JComponent.LEFT_ALIGNMENT );

		final org.lgna.project.ast.UserCode userCode = (org.lgna.project.ast.UserCode)this.code;
		ParametersPane parametersPane = new ParametersPane( org.alice.ide.x.EditableAstI18Factory.getProjectGroupInstance(), userCode );
		AbstractCodeHeaderPane header;
		if( code instanceof org.lgna.project.ast.UserMethod ) {
			org.lgna.project.ast.UserMethod userMethod = (org.lgna.project.ast.UserMethod)code;
			header = new MethodHeaderPane( userMethod, parametersPane, false );
		} else if( code instanceof org.lgna.project.ast.NamedUserConstructor ) {
			org.lgna.project.ast.NamedUserConstructor userConstructor = (org.lgna.project.ast.NamedUserConstructor)code;
			header = new ConstructorHeaderPane( userConstructor, parametersPane, false );
		} else {
			throw new RuntimeException();
		}
		this.addComponent( header, Constraint.PAGE_START );
		if( org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().getValue() ) {
			this.addComponent( org.alice.ide.controlflow.ControlFlowComposite.getInstance( code ).getView(), Constraint.PAGE_END );
		}

		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		java.awt.Color color = ide.getTheme().getCodeColor( this.code );
		color = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( color, 1.0f, 1.1f, 1.1f );
		this.setBackgroundColor( color );
	}

	public String getTutorialNoteText( org.lgna.croquet.Model model, org.lgna.croquet.edits.Edit< ? > edit, org.lgna.croquet.UserInformation userInformation ) {
		return "Drop...";
	}
	
	public org.lgna.croquet.resolvers.CodableResolver< CodeEditor > getCodableResolver() {
		return new Resolver( this.code );
	}
	public org.lgna.croquet.components.TrackableShape getTrackableShape( org.lgna.croquet.DropSite potentialDropSite ) {
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
	public org.lgna.croquet.components.JComponent<?> getViewController() {
		return this;
	}

	@Override
	protected javax.swing.JPanel createJPanel() {
		final boolean IS_FEEDBACK_DESIRED = false;
		javax.swing.JPanel rv;
		if( IS_FEEDBACK_DESIRED ) {
			rv = new javax.swing.JPanel() {
				@Override
				public void paint( java.awt.Graphics g ) {
					super.paint( g );
					if( CodeEditor.this.statementListPropertyPaneInfos != null ) {
						java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
						int i = 0;
						for( StatementListPropertyPaneInfo statementListPropertyPaneInfo : CodeEditor.this.statementListPropertyPaneInfos ) {
							if( statementListPropertyPaneInfo != null ) {
								java.awt.Color color;
								if( CodeEditor.this.currentUnder == statementListPropertyPaneInfo.getStatementListPropertyPane() ) {
									color = new java.awt.Color( 0, 0, 0, 127 );
								} else {
									color = null;
									//color = new java.awt.Color( 255, 0, 0, 31 );
								}
								java.awt.Rectangle bounds = statementListPropertyPaneInfo.getBounds();
								bounds = javax.swing.SwingUtilities.convertRectangle( CodeEditor.this.getAsSeenBy().getAwtComponent(), bounds, this );
								if( color != null ) {
									g2.setColor( color );
									g2.fill( bounds );
									g2.setColor( new java.awt.Color( 255, 255, 0, 255 ) );
									g2.draw( bounds );
								}
								g2.setColor( java.awt.Color.BLACK );
								edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText( g2, Integer.toString( i ), bounds.x, bounds.y, 32, bounds.height );
							}
							i++;
						}
					}
				}
			};
		} else {
			rv = new javax.swing.JPanel();
		}
		rv.setLayout( new java.awt.BorderLayout() );
		return rv;
	}
	private org.lgna.croquet.State.ValueListener<Boolean> typeFeedbackObserver = new org.lgna.croquet.State.ValueListener<Boolean>() {
		public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			CodeEditor.this.rootStatementListPropertyPane.refreshLater();
		}
	};
	private org.lgna.croquet.ListSelectionState.ValueListener< org.alice.ide.formatter.Formatter > formatterSelectionObserver = new org.lgna.croquet.ListSelectionState.ValueListener< org.alice.ide.formatter.Formatter >() {
		public void changing( org.lgna.croquet.State< org.alice.ide.formatter.Formatter > state, org.alice.ide.formatter.Formatter prevValue, org.alice.ide.formatter.Formatter nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.alice.ide.formatter.Formatter > state, org.alice.ide.formatter.Formatter prevValue, org.alice.ide.formatter.Formatter nextValue, boolean isAdjusting ) {
			CodeEditor.this.rootStatementListPropertyPane.refreshLater();
		}
	};
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.getInstance().addValueListener( formatterSelectionObserver );
		org.alice.ide.croquet.models.ui.preferences.IsIncludingTypeFeedbackForExpressionsState.getInstance().addAndInvokeValueListener( this.typeFeedbackObserver );
	}
	@Override
	protected void handleUndisplayable() {
		org.alice.ide.croquet.models.ui.preferences.IsIncludingTypeFeedbackForExpressionsState.getInstance().removeValueListener( this.typeFeedbackObserver );
		org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.getInstance().removeValueListener( formatterSelectionObserver );
		super.handleUndisplayable();
	}


	private static int convertY( org.lgna.croquet.components.Component<?> from, int y, org.lgna.croquet.components.Component<?> to ) {
		java.awt.Point pt = from.convertPoint( new java.awt.Point( 0, y ), to);
		return pt.y;
	}
	private static int capMinimum( int yPotentialMinimumBound, int y, StatementListPropertyPaneInfo[] statementListPropertyPaneInfos, int index ) {
		int rv = yPotentialMinimumBound;
		final int N = statementListPropertyPaneInfos.length;
		for( int i=0; i<N; i++ ) {
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
		for( int i=0; i<N; i++ ) {
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
	
	public class StatementListIndexTrackableShape implements org.lgna.croquet.components.TrackableShape {
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
		
		public java.awt.Shape getShape( org.lgna.croquet.components.ScreenElement asSeenBy, java.awt.Insets insets ) {
			java.awt.Rectangle rv = CodeEditor.this.getAsSeenBy().convertRectangle( this.boundsAtIndex, asSeenBy );
			//note: ignore insets
			return rv;
		}
		public java.awt.Shape getVisibleShape( org.lgna.croquet.components.ScreenElement asSeenBy, java.awt.Insets insets ) {
			org.lgna.croquet.components.Component<?> src = CodeEditor.this.getAsSeenBy();
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
		public boolean isInView() {
			if( isWarningAlreadyPrinted ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.info( "getTrackableShapeAtIndexOf" );
				isWarningAlreadyPrinted = true;
			}
			return true;
		}
		public org.lgna.croquet.components.ScrollPane getScrollPaneAncestor() {
			return this.statementListPropertyPane.getScrollPaneAncestor();
		}
		public void addComponentListener(java.awt.event.ComponentListener listener) {
			this.statementListPropertyPane.addComponentListener(listener);
		}
		public void removeComponentListener(java.awt.event.ComponentListener listener) {
			this.statementListPropertyPane.removeComponentListener(listener);
		}
		public void addHierarchyBoundsListener(java.awt.event.HierarchyBoundsListener listener) {
			this.statementListPropertyPane.addHierarchyBoundsListener(listener);
		}
		public void removeHierarchyBoundsListener(java.awt.event.HierarchyBoundsListener listener) {
			this.statementListPropertyPane.removeHierarchyBoundsListener(listener);
		}
	}
	public org.lgna.croquet.components.TrackableShape getTrackableShapeAtIndexOf( org.lgna.project.ast.StatementListProperty statementListProperty, int index, boolean EPIC_HACK_isDropConstraintDesired ) {
		if( statementListProperty != null ) {
			//choose any non-ancestor
			
			org.lgna.croquet.components.Container< ? > arbitrarilyChosenSource = org.alice.ide.IDE.getActiveInstance().getSceneEditor();
			StatementListPropertyPaneInfo[] statementListPropertyPaneInfos = this.createStatementListPropertyPaneInfos( arbitrarilyChosenSource );
			final int N = statementListPropertyPaneInfos.length;
			for( int i=0; i<N; i++ ) {
				StatementListPropertyPaneInfo statementListPropertyPaneInfo = statementListPropertyPaneInfos[ i ];
				StatementListPropertyView statementListPropertyPane = statementListPropertyPaneInfo.getStatementListPropertyPane();
				if( statementListPropertyPane.getProperty() == statementListProperty ) {
					StatementListPropertyView.BoundInformation yBounds = statementListPropertyPane.calculateYBounds( index );
					java.awt.Rectangle bounds = statementListPropertyPaneInfo.getBounds();
					
					int yMinimum;
					if( yBounds.yMinimum != null ) {
						yMinimum = convertY( statementListPropertyPane, yBounds.yMinimum, CodeEditor.this.getAsSeenBy() );
						int y = convertY( statementListPropertyPane, yBounds.y, CodeEditor.this.getAsSeenBy() );
						yMinimum = capMinimum( yMinimum, y, statementListPropertyPaneInfos, index );
					} else {
						yMinimum = bounds.y;
					}
					int yMaximum;
					if( yBounds.yMaximum != null ) {
						yMaximum = convertY( statementListPropertyPane, yBounds.yMaximum, CodeEditor.this.getAsSeenBy() );
						int yPlusHeight = convertY( statementListPropertyPane, yBounds.yPlusHeight, CodeEditor.this.getAsSeenBy() );
						yMaximum = capMaximum( yMaximum, yPlusHeight, statementListPropertyPaneInfos, index );
					} else {
						yMaximum = bounds.y + bounds.height - 1;
					}
					
					java.awt.Rectangle boundsAtIndex = new java.awt.Rectangle( bounds.x, yMinimum, bounds.width, yMaximum - yMinimum + 1 );

					return new StatementListIndexTrackableShape(statementListProperty, index, statementListPropertyPane, boundsAtIndex);
				}
			}
//			org.lgna.project.ast.Node a = ((org.lgna.project.ast.BlockStatement)statementListProperty.getOwner()).getParent();
//			org.lgna.project.ast.Node b = ((org.lgna.project.ast.BlockStatement)statementListPropertyPaneInfos[0].getStatementListPropertyPane().getProperty().getOwner()).getParent();
//			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( a, b, a.hashCode(), b.hashCode(), a.getId(), b.getId() );
		}
		
		return null;
	}

//	public int print(java.awt.Graphics g, java.awt.print.PageFormat pageFormat, int pageIndex) throws java.awt.print.PrinterException {
//		if( pageIndex > 0 ) {
//			return NO_SUCH_PAGE;
//		} else {
//			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
//			org.lgna.croquet.components.Component<?> component0 = this.getComponent( 0 );
//			int width = Math.max( component0.getAwtComponent().getPreferredSize().width, this.scrollPane.getViewportView().getAwtComponent().getPreferredSize().width );
//			int height = this.scrollPane.getY() + this.scrollPane.getViewportView().getAwtComponent().getPreferredSize().height;
//			double scale = edu.cmu.cs.dennisc.java.awt.print.PageFormatUtilities.calculateScale(pageFormat, width, height);
//			g2.translate( pageFormat.getImageableX(), pageFormat.getImageableY() );
//			if( scale > 1.0 ) {
//				g2.scale( 1.0/scale, 1.0/scale );
//			}
//			component0.getAwtComponent().printAll( g2 );
//			g2.translate( this.scrollPane.getX(), this.scrollPane.getY() );
//			this.scrollPane.getViewportView().getAwtComponent().printAll( g2 );
//			return PAGE_EXISTS;
//		}
//	}
}
