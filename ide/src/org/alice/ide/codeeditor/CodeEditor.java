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
import org.alice.ide.common.DefaultStatementPane;
import org.alice.ide.x.components.StatementListPropertyView;

/**
 * @author Dennis Cosgrove
 */
public class CodeEditor extends org.alice.ide.codedrop.CodeDropReceptor implements org.lgna.croquet.DropReceptor {
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
	private StatementListPropertyPaneInfo[] statementListPropertyPaneInfos;

	@Deprecated
	public static class Resolver implements org.lgna.croquet.resolvers.CodableResolver< CodeEditor > {
		private final org.lgna.project.ast.AbstractCode code;
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
	private org.lgna.croquet.State.ValueObserver<Boolean> typeFeedbackObserver = new org.lgna.croquet.State.ValueObserver<Boolean>() {
		public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			CodeEditor.this.rootStatementListPropertyPane.refreshLater();
		}
	};
	private org.lgna.croquet.ListSelectionState.ValueObserver< org.alice.ide.formatter.Formatter > formatterSelectionObserver = new org.lgna.croquet.ListSelectionState.ValueObserver< org.alice.ide.formatter.Formatter >() {
		public void changing( org.lgna.croquet.State< org.alice.ide.formatter.Formatter > state, org.alice.ide.formatter.Formatter prevValue, org.alice.ide.formatter.Formatter nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.alice.ide.formatter.Formatter > state, org.alice.ide.formatter.Formatter prevValue, org.alice.ide.formatter.Formatter nextValue, boolean isAdjusting ) {
			CodeEditor.this.rootStatementListPropertyPane.refreshLater();
		}
	};
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.getInstance().addValueObserver( formatterSelectionObserver );
		org.alice.ide.croquet.models.ui.preferences.IsIncludingTypeFeedbackForExpressionsState.getInstance().addAndInvokeValueObserver( this.typeFeedbackObserver );
	}
	@Override
	protected void handleUndisplayable() {
		org.alice.ide.croquet.models.ui.preferences.IsIncludingTypeFeedbackForExpressionsState.getInstance().removeValueObserver( this.typeFeedbackObserver );
		org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.getInstance().removeValueObserver( formatterSelectionObserver );
		super.handleUndisplayable();
	}

	public final boolean isPotentiallyAcceptingOf( org.lgna.croquet.DragModel dragModel ) {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		if( dragModel instanceof org.alice.ide.ast.draganddrop.statement.AbstractStatementDragModel ) {
			return ide.getFocusedCode() == this.code;
		} else {
			return false;
		}
	}
	
	private StatementListPropertyView currentUnder;
	
	private void setCurrentUnder( StatementListPropertyView nextUnder, java.awt.Dimension dropSize ) {
		if( this.currentUnder != nextUnder ) {
			if( this.currentUnder != null ) {
				this.currentUnder.setIsCurrentUnder( false );
			}
			this.currentUnder = nextUnder;
			if( this.currentUnder != null ) {
				this.currentUnder.setIsCurrentUnder( true );
				this.currentUnder.setDropSize( dropSize );
			}
		}
	}
	public final void dragStarted( org.lgna.croquet.history.DragStep step ) {
	}

	public final void dragEntered( org.lgna.croquet.history.DragStep step ) {
		org.lgna.croquet.components.DragComponent source = step.getDragSource();
		this.statementListPropertyPaneInfos = createStatementListPropertyPaneInfos( source );
		this.repaint();
	}
	private org.lgna.croquet.components.Component< ? > getAsSeenBy() {
		return this.getScrollPane().getViewportView();
	}
	private StatementListPropertyPaneInfo[] createStatementListPropertyPaneInfos( org.lgna.croquet.components.Container<?> source ) {
		java.util.List< StatementListPropertyView > statementListPropertyPanes = org.lgna.croquet.components.HierarchyUtilities.findAllMatches( this, StatementListPropertyView.class );
		StatementListPropertyPaneInfo[] rv = new StatementListPropertyPaneInfo[ statementListPropertyPanes.size() ];
		int i = 0;
		for( StatementListPropertyView statementListPropertyPane : statementListPropertyPanes ) {
			if( source != null && source.isAncestorOf( statementListPropertyPane ) ) {
				continue;
			}
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( statementListPropertyPane );
			DefaultStatementPane statementAncestor = statementListPropertyPane.getFirstAncestorAssignableTo( DefaultStatementPane.class );
			java.awt.Rectangle bounds;
			if( statementAncestor != null ) {
				bounds = statementAncestor.convertRectangle( statementListPropertyPane.getDropBounds( statementAncestor ), this.getAsSeenBy() );
			} else {
				bounds = statementListPropertyPane.getParent().getBounds( this.getAsSeenBy() );
			}
			bounds.x = 0;
			bounds.width = this.getAsSeenBy().getWidth() - bounds.x;
			rv[ i ] = new StatementListPropertyPaneInfo( statementListPropertyPane, bounds );
			
			i++;
		}
		return rv;

	}
	private StatementListPropertyView getStatementListPropertyPaneUnder( java.awt.event.MouseEvent e, StatementListPropertyPaneInfo[] statementListPropertyPaneInfos ) {
		StatementListPropertyView rv = null;
		for( StatementListPropertyPaneInfo statementListPropertyPaneInfo : this.statementListPropertyPaneInfos ) {
			if( statementListPropertyPaneInfo != null ) {
				if( statementListPropertyPaneInfo.contains( e ) ) {
					StatementListPropertyView slpp = statementListPropertyPaneInfo.getStatementListPropertyPane();
					if( rv != null ) {
						if( rv.getHeight() > slpp.getHeight() ) {
							rv = slpp;
						} else {
							//pass
						}
					} else {
						rv = slpp;
					}
				}
			}
		}
		return rv;
	}
	public final BlockStatementIndexPair dragUpdated( org.lgna.croquet.history.DragStep step ) {
		org.lgna.croquet.components.DragComponent source = step.getDragSource();
		if( source != null ) {
			java.awt.event.MouseEvent eSource = step.getLatestMouseEvent();
			java.awt.event.MouseEvent eAsSeenBy = source.convertMouseEvent( eSource, this.getAsSeenBy() );
			StatementListPropertyView nextUnder = getStatementListPropertyPaneUnder( eAsSeenBy, this.statementListPropertyPaneInfos );
			this.setCurrentUnder( nextUnder, source.getDropProxySize() );
			if( this.currentUnder != null ) {
				boolean isDropProxyAlreadyUpdated = false;
				if( edu.cmu.cs.dennisc.javax.swing.SwingUtilities.isQuoteControlUnquoteDown( eSource ) ) {
					//pass
				} else {
					org.lgna.croquet.components.Component< ? > subject = source.getSubject();
					if( subject instanceof org.alice.ide.common.AbstractStatementPane ) {
						org.alice.ide.common.AbstractStatementPane abstractStatementPane = (org.alice.ide.common.AbstractStatementPane)subject;
						if( source instanceof org.alice.ide.templates.StatementTemplate ) {
							//pass
						} else {
							org.lgna.project.ast.Statement statement = abstractStatementPane.getStatement();
							org.lgna.project.ast.StatementListProperty prevOwner = abstractStatementPane.getOwner();
							org.lgna.project.ast.StatementListProperty nextOwner = this.currentUnder.getProperty();

							int prevIndex = prevOwner.indexOf( statement );
							int nextIndex = this.currentUnder.calculateIndex( source.convertPoint( eSource.getPoint(), this.currentUnder ) );
							int currentPotentialDropIndex = nextIndex;
							if( prevOwner == nextOwner ) {
								if( prevIndex == nextIndex || prevIndex == nextIndex - 1 ) {
									source.setDropProxyLocationAndShowIfNecessary( new java.awt.Point( 0, 0 ), source, null, -1 );
									isDropProxyAlreadyUpdated = true;
									currentPotentialDropIndex = -1;
								}
							}
							this.currentUnder.setCurrentPotentialDropIndex( currentPotentialDropIndex );
						}
					}
				}
				if( isDropProxyAlreadyUpdated ) {
					//pass
				} else {
					java.awt.event.MouseEvent eUnder = this.getAsSeenBy().convertMouseEvent( eAsSeenBy, this.currentUnder );
					Integer height = 0;
					java.awt.Insets insets = this.currentUnder.getBorder().getBorderInsets( this.currentUnder.getAwtComponent() );
					int x = insets.left;
					java.awt.Point p = new java.awt.Point( x, 0 );
					
					int availableHeight = this.currentUnder.getAvailableDropProxyHeight();
					
					if( this.currentUnder.isFigurativelyEmpty() ) {
						height = null;
						p.y = insets.top;
					} else {
						int n = this.currentUnder.getComponentCount();
						if( n > 0 ) {
							int index = this.currentUnder.calculateIndex( eUnder.getPoint() );
							this.currentUnder.setCurrentPotentialDropIndex( index );
							final boolean IS_SQUISHING_DESIRED = false;
							if( index == 0 ) {
								//java.awt.Component firstComponent = this.currentUnder.getComponent( 0 );
								p.y = 0;
								if( IS_SQUISHING_DESIRED ) {
									height = null;
								}
							} else if( index < n ) {
								p.y = this.currentUnder.getAwtComponent().getComponent( index ).getY();
							} else {
								java.awt.Component lastComponent = this.currentUnder.getAwtComponent().getComponent( n - 1 );
								p.y = lastComponent.getY() + lastComponent.getHeight();
								if( IS_SQUISHING_DESIRED ) {
									p.y -= availableHeight;
									height = null;
								} else {
									p.y += StatementListPropertyView.INTRASTICIAL_PAD;
									if( this.currentUnder.getProperty() == ((org.lgna.project.ast.UserCode)this.code).getBodyProperty().getValue().statements ) {
										height = null;
									}
								}
							}
						}
					}
					source.setDropProxyLocationAndShowIfNecessary( p, this.currentUnder, height, availableHeight );
				}
			} else {
//				source.hideDropProxyIfNecessary();
			}
		}
		this.repaint();

		if( this.currentUnder != null ) {
			org.lgna.project.ast.BlockStatement blockStatement = (org.lgna.project.ast.BlockStatement)this.currentUnder.getProperty().getOwner();
			java.awt.event.MouseEvent eSource = step.getLatestMouseEvent();
			java.awt.event.MouseEvent eAsSeenBy = source.convertMouseEvent( eSource, this.getAsSeenBy() );
			java.awt.event.MouseEvent eUnder = this.getAsSeenBy().convertMouseEvent( eAsSeenBy, this.currentUnder );
			int index = this.currentUnder.calculateIndex( eUnder.getPoint() );
			BlockStatementIndexPair blockStatementIndexPair = new BlockStatementIndexPair( blockStatement, index );
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "blockStatementIndexPair", blockStatementIndexPair );
			return blockStatementIndexPair;
		} else {
			return null;
		}
	}
	public final org.lgna.croquet.Model dragDropped( final org.lgna.croquet.history.DragStep step ) {
		org.lgna.croquet.Model rv = null;
		final org.lgna.croquet.DragModel dragModel = step.getModel();
		org.lgna.croquet.components.DragComponent dragSource = step.getDragSource();
		final java.awt.event.MouseEvent eSource = step.getLatestMouseEvent();
		final StatementListPropertyView statementListPropertyPane = CodeEditor.this.currentUnder;
		if( statementListPropertyPane != null ) {
			final int index = statementListPropertyPane.calculateIndex( dragSource.convertPoint( eSource.getPoint(), statementListPropertyPane ) );
			if( dragModel instanceof org.alice.ide.ast.draganddrop.statement.StatementTemplateDragModel ) {
				if( org.alice.ide.croquet.models.recursion.IsRecursionAllowedState.getInstance().getValue() ) {
					//pass
				} else {
					if( dragModel instanceof org.alice.ide.ast.draganddrop.statement.ProcedureInvocationTemplateDragModel ) {
						org.alice.ide.ast.draganddrop.statement.ProcedureInvocationTemplateDragModel procedureInvocationTemplateDragModel = (org.alice.ide.ast.draganddrop.statement.ProcedureInvocationTemplateDragModel)dragModel;
						org.lgna.project.ast.AbstractMethod method = procedureInvocationTemplateDragModel.getMethod();
						if( method == this.getCode() ) {
							StringBuilder sb = new StringBuilder();
							sb.append( "<html>" );
							sb.append( "The code you have just dropped would create a <strong><em>recursive</em></strong> method call.<p><p>Recursion is disabled by default because otherwise many users unwittingly and mistakenly make recursive calls." );
							final boolean IS_POINTING_USER_TO_RECURSION_PREFERENCE_DESIRED = true;
							if( IS_POINTING_USER_TO_RECURSION_PREFERENCE_DESIRED ) {
								sb.append( "<p><p>For more information on recursion see the Window -> Preferences menu." );
							}
							sb.append( "</html>" );
							org.alice.ide.IDE.getActiveInstance().showMessageDialog( sb.toString(), "Recursion is disabled.", org.lgna.croquet.MessageType.ERROR );
							return null;
						}
					}
				}
				if( this.currentUnder != null ) {
					edu.cmu.cs.dennisc.property.PropertyOwner propertyOwner = statementListPropertyPane.getProperty().getOwner();
					BlockStatementIndexPair blockStatementIndexPair;
					if( propertyOwner instanceof org.lgna.project.ast.BlockStatement ) {
						blockStatementIndexPair = new BlockStatementIndexPair( (org.lgna.project.ast.BlockStatement)propertyOwner, index );
					} else {
						blockStatementIndexPair = null;
					}
					rv = dragModel.getDropModel( step, blockStatementIndexPair );
					
					edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "investigate pushContext" );
					org.alice.ide.IDE.getActiveInstance().getCascadeManager().pushContext( null, blockStatementIndexPair );
					edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "handle finally" );
				}
			} else if( dragModel instanceof org.alice.ide.clipboard.ClipboardDragModel ) {
				org.alice.ide.clipboard.ClipboardDragModel clipboardDragModel = (org.alice.ide.clipboard.ClipboardDragModel)dragModel;
				if( this.currentUnder != null ) {
					edu.cmu.cs.dennisc.property.PropertyOwner propertyOwner = statementListPropertyPane.getProperty().getOwner();
					if( propertyOwner instanceof org.lgna.project.ast.BlockStatement ) {
						BlockStatementIndexPair blockStatementIndexPair = new BlockStatementIndexPair( (org.lgna.project.ast.BlockStatement)propertyOwner, index );
						boolean isCopy = edu.cmu.cs.dennisc.javax.swing.SwingUtilities.isQuoteControlUnquoteDown( eSource );
						if( isCopy ) {
							rv = org.alice.ide.clipboard.CopyFromClipboardOperation.getInstance( blockStatementIndexPair );
						} else {
							rv = org.alice.ide.clipboard.PasteFromClipboardOperation.getInstance( blockStatementIndexPair );
						}
					}
				}
			} else if( dragModel instanceof org.alice.ide.ast.draganddrop.statement.StatementDragModel ) {
				if( this.currentUnder != null ) {
					org.alice.ide.ast.draganddrop.statement.StatementDragModel statementDragModel = (org.alice.ide.ast.draganddrop.statement.StatementDragModel)dragModel;
					final org.lgna.project.ast.Statement statement = statementDragModel.getStatement();
					
					org.lgna.project.ast.Node parent = statement.getParent();
					if( parent instanceof org.lgna.project.ast.BlockStatement ) {
						org.lgna.project.ast.BlockStatement blockStatement = (org.lgna.project.ast.BlockStatement)parent;
						final org.lgna.project.ast.StatementListProperty prevOwner = blockStatement.statements;
						final org.lgna.project.ast.StatementListProperty nextOwner = this.currentUnder.getProperty();
						final int prevIndex = prevOwner.indexOf( statement );
						final int nextIndex = this.currentUnder.calculateIndex( dragSource.convertPoint( eSource.getPoint(), this.currentUnder ) );

						org.lgna.project.ast.BlockStatement prevBlockStatement = (org.lgna.project.ast.BlockStatement)prevOwner.getOwner();
						org.lgna.project.ast.BlockStatement nextBlockStatement = (org.lgna.project.ast.BlockStatement)nextOwner.getOwner();
						if( edu.cmu.cs.dennisc.javax.swing.SwingUtilities.isQuoteControlUnquoteDown( eSource ) ) {
							org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
							org.lgna.project.ast.Statement copy = ide.createCopy( statement );
							rv = new org.alice.ide.croquet.models.ast.InsertStatementActionOperation( nextBlockStatement, nextIndex, copy );
						} else {
							if( prevOwner == nextOwner && ( prevIndex == nextIndex || prevIndex == nextIndex - 1 ) ) {
								rv = null;
							} else {
								rv = new org.alice.ide.croquet.models.ast.MoveStatementActionOperation( prevBlockStatement, prevIndex, statement, nextBlockStatement, nextIndex );
							}
						}
					}
				}
			}
		}
		return rv;
	}
	public final void dragExited( org.lgna.croquet.history.DragStep step, boolean isDropRecipient ) {
		this.statementListPropertyPaneInfos = null;
		//todo: listen to step
		this.setCurrentUnder( null, null );
		this.repaint();
	}
	public final void dragStopped( org.lgna.croquet.history.DragStep step ) {
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
