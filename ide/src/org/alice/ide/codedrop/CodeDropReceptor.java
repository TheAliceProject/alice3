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

package org.alice.ide.codedrop;

import org.alice.ide.ast.draganddrop.BlockStatementIndexPair;
import org.alice.ide.x.components.StatementListPropertyView;
import org.alice.ide.codeeditor.*;
import org.alice.ide.common.*;

/**
 * @author Dennis Cosgrove
 */
public abstract class CodeDropReceptor extends org.lgna.croquet.components.BorderPanel implements org.lgna.croquet.DropReceptor, java.awt.print.Printable {
	protected StatementListPropertyPaneInfo[] statementListPropertyPaneInfos;
	
	public CodeDropReceptor( org.lgna.croquet.Composite< ? > composite ) {
		super( composite );
	}
	public CodeDropReceptor() {
		this( null );
	}
	
	public abstract org.lgna.project.ast.AbstractCode getCode();
	
	public final boolean isPotentiallyAcceptingOf( org.lgna.croquet.DragModel dragModel ) {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		if( dragModel instanceof org.alice.ide.ast.draganddrop.statement.AbstractStatementDragModel ) {
			org.alice.ide.ast.draganddrop.statement.AbstractStatementDragModel statementDragModel = (org.alice.ide.ast.draganddrop.statement.AbstractStatementDragModel)dragModel;
			return ide.getFocusedCode() == this.getCode();
		} else {
			return false;
		}
	}
	
	protected StatementListPropertyView currentUnder;
	
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
		this.statementListPropertyPaneInfos = createStatementListPropertyPaneInfos( step.getModel(), source );
		this.repaint();
	}
	protected abstract org.lgna.croquet.components.Component< ? > getAsSeenBy();
	protected StatementListPropertyPaneInfo[] createStatementListPropertyPaneInfos( org.lgna.croquet.DragModel dragModel, org.lgna.croquet.components.Container<?> source ) {
		java.util.List< StatementListPropertyView > statementListPropertyPanes = org.lgna.croquet.components.HierarchyUtilities.findAllMatches( this, StatementListPropertyView.class );
		
		boolean isAddEvent;
		if( dragModel instanceof org.alice.ide.ast.draganddrop.statement.AbstractStatementDragModel ) {
			org.alice.ide.ast.draganddrop.statement.AbstractStatementDragModel statementDragModel = (org.alice.ide.ast.draganddrop.statement.AbstractStatementDragModel)dragModel;
			isAddEvent = statementDragModel.isAddEventListenerLikeSubstance();
		} else {
			isAddEvent = false;
		}
		
		java.util.ListIterator< StatementListPropertyView > listIterator = statementListPropertyPanes.listIterator();
		while( listIterator.hasNext() ) {
			StatementListPropertyView view = listIterator.next();
			if( view.isAcceptingOfAddEventListenerMethodInvocationStatements() == isAddEvent ) {
				//pass
			} else {
				listIterator.remove();
			}
		}
		
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
									if( this.currentUnder.getProperty() == ((org.lgna.project.ast.UserCode)this.getCode()).getBodyProperty().getValue().statements ) {
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
		final StatementListPropertyView statementListPropertyPane = this.currentUnder;
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
}
