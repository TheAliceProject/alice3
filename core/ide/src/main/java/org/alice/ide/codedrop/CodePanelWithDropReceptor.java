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

package org.alice.ide.codedrop;

import org.alice.ide.ast.draganddrop.BlockStatementIndexPair;
import org.alice.ide.codeeditor.StatementListPropertyPaneInfo;
import org.alice.ide.common.DefaultStatementPane;
import org.alice.ide.x.components.StatementListPropertyView;

/**
 * @author Dennis Cosgrove
 */
public abstract class CodePanelWithDropReceptor extends org.lgna.croquet.views.BorderPanel {
	protected class InternalDropReceptor extends org.lgna.croquet.AbstractDropReceptor {
		private org.alice.ide.cascade.ExpressionCascadeContext pushedContext;

		@Override
		public final boolean isPotentiallyAcceptingOf( org.lgna.croquet.DragModel dragModel ) {
			org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
			if( org.alice.ide.meta.DeclarationMeta.getDeclaration() == getCode() ) {
				if( dragModel instanceof org.alice.ide.ast.draganddrop.statement.AbstractStatementDragModel ) {
					return true;
				} else if( dragModel instanceof org.alice.ide.ast.draganddrop.expression.AbstractExpressionDragModel ) {
					org.alice.ide.ast.draganddrop.expression.AbstractExpressionDragModel expressionDragModel = (org.alice.ide.ast.draganddrop.expression.AbstractExpressionDragModel)dragModel;
					return expressionDragModel.isPotentialStatementCreator();
				} else {
					return false;
				}
			} else {
				return false;
			}
		}

		public StatementListPropertyView currentUnder;

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

		@Override
		public final void dragStarted( org.lgna.croquet.history.DragStep step ) {
		}

		@Override
		public final void dragEntered( org.lgna.croquet.history.DragStep step ) {
			org.lgna.croquet.views.DragComponent source = step.getDragSource();
			statementListPropertyPaneInfos = createStatementListPropertyPaneInfos( step.getModel(), source );
			repaint();
		}

		public StatementListPropertyPaneInfo[] createStatementListPropertyPaneInfos( org.lgna.croquet.DragModel dragModel, org.lgna.croquet.views.AwtContainerView<?> source ) {
			java.util.List<StatementListPropertyView> statementListPropertyPanes = org.lgna.croquet.views.HierarchyUtilities.findAllMatches( CodePanelWithDropReceptor.this, StatementListPropertyView.class );

			boolean isAddEvent;
			if( dragModel instanceof org.alice.ide.ast.draganddrop.statement.AbstractStatementDragModel ) {
				org.alice.ide.ast.draganddrop.statement.AbstractStatementDragModel statementDragModel = (org.alice.ide.ast.draganddrop.statement.AbstractStatementDragModel)dragModel;
				isAddEvent = statementDragModel.isAddEventListenerLikeSubstance();
			} else {
				isAddEvent = false;
			}

			java.util.ListIterator<StatementListPropertyView> listIterator = statementListPropertyPanes.listIterator();
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
				if( ( source != null ) && source.isAncestorOf( statementListPropertyPane ) ) {
					continue;
				}
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( statementListPropertyPane );
				DefaultStatementPane statementAncestor = statementListPropertyPane.getFirstAncestorAssignableTo( DefaultStatementPane.class );
				java.awt.Rectangle bounds;
				if( statementAncestor != null ) {
					bounds = statementAncestor.convertRectangle( statementListPropertyPane.getDropBounds( statementAncestor ), getAsSeenBy() );
				} else {
					bounds = statementListPropertyPane.getParent().getBounds( getAsSeenBy() );
				}
				bounds.x = 0;
				bounds.width = getAsSeenBy().getWidth() - bounds.x;
				rv[ i ] = new StatementListPropertyPaneInfo( statementListPropertyPane, bounds );

				i++;
			}
			return rv;

		}

		private StatementListPropertyView getStatementListPropertyPaneUnder( java.awt.event.MouseEvent e, StatementListPropertyPaneInfo[] statementListPropertyPaneInfos ) {
			StatementListPropertyView rv = null;
			for( StatementListPropertyPaneInfo statementListPropertyPaneInfo : statementListPropertyPaneInfos ) {
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

		@Override
		public final BlockStatementIndexPair dragUpdated( org.lgna.croquet.history.DragStep step ) {
			java.awt.ComponentOrientation componentOrientation = getComponentOrientation();
			org.lgna.croquet.views.DragComponent source = step.getDragSource();
			if( source != null ) {
				java.awt.event.MouseEvent eSource = step.getLatestMouseEvent();
				java.awt.event.MouseEvent eAsSeenBy = source.convertMouseEvent( eSource, getAsSeenBy() );
				StatementListPropertyView nextUnder = getStatementListPropertyPaneUnder( eAsSeenBy, statementListPropertyPaneInfos );
				this.setCurrentUnder( nextUnder, source.getDropProxySize() );
				if( this.currentUnder != null ) {
					boolean isDropProxyAlreadyUpdated = false;
					if( edu.cmu.cs.dennisc.java.awt.event.InputEventUtilities.isQuoteControlUnquoteDown( eSource ) ) {
						//pass
					} else {
						org.lgna.croquet.views.AwtComponentView<?> subject = source.getSubject();
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
									if( ( prevIndex == nextIndex ) || ( prevIndex == ( nextIndex - 1 ) ) ) {
										java.awt.Point p = new java.awt.Point( 0, 0 );
										source.setDropProxyLocationAndShowIfNecessary( p, source, null, -1 );
										isDropProxyAlreadyUpdated = true;
										currentPotentialDropIndex = -1;
									}
								}
								this.currentUnder.setCurrentPotentialDropIndexAndDragStep( currentPotentialDropIndex, step );
							}
						}
					}
					if( isDropProxyAlreadyUpdated ) {
						//pass
					} else {
						java.awt.event.MouseEvent eUnder = getAsSeenBy().convertMouseEvent( eAsSeenBy, this.currentUnder );
						Integer height = 0;
						java.awt.Insets insets = this.currentUnder.getBorder().getBorderInsets( this.currentUnder.getAwtComponent() );
						java.awt.Point p = new java.awt.Point( 0, 0 );

						int availableHeight = this.currentUnder.getAvailableDropProxyHeight();

						org.alice.ide.codeeditor.StatementListBorder statementListBorder = this.currentUnder.getStatementListBorder();
						int N = this.currentUnder.getComponentCount();
						if( N == 0 ) {
							p.y = insets.top;
							height = null;
						} else {
							int index = this.currentUnder.calculateIndex( eUnder.getPoint() );
							this.currentUnder.setCurrentPotentialDropIndexAndDragStep( index, step );
							final boolean IS_SQUISHING_DESIRED = false;
							if( index == 0 ) {
								p.y = 0;
								if( IS_SQUISHING_DESIRED ) {
									height = null;
								}
							} else if( ( index == statementListBorder.getMinimum() ) && ( N == 1 ) ) {
								p.y = this.currentUnder.getHeight() - insets.bottom;
								height = null;
							} else if( index < N ) {
								p.y = this.currentUnder.getAwtComponent().getComponent( index ).getY();
							} else {
								java.awt.Component lastComponent = this.currentUnder.getAwtComponent().getComponent( N - 1 );
								p.y = lastComponent.getY() + lastComponent.getHeight();
								if( IS_SQUISHING_DESIRED ) {
									p.y -= availableHeight;
									height = null;
								} else {
									p.y += StatementListPropertyView.INTRASTICIAL_PAD;
									if( this.currentUnder.getProperty() == ( (org.lgna.project.ast.UserCode)getCode() ).getBodyProperty().getValue().statements ) {
										height = null;
									}
								}
							}
						}
						if( componentOrientation.isLeftToRight() ) {
							p.x = insets.left;
						} else {
							p.x = this.currentUnder.getWidth() - insets.right - step.getDragSource().getDropProxy().getWidth();

						}
						source.setDropProxyLocationAndShowIfNecessary( p, this.currentUnder, height, availableHeight );
					}
				} else {
					//					source.hideDropProxyIfNecessary();
				}
			}
			repaint();

			if( this.currentUnder != null ) {
				org.lgna.project.ast.BlockStatement blockStatement = (org.lgna.project.ast.BlockStatement)this.currentUnder.getProperty().getOwner();
				java.awt.event.MouseEvent eSource = step.getLatestMouseEvent();
				java.awt.event.MouseEvent eAsSeenBy = source.convertMouseEvent( eSource, getAsSeenBy() );
				java.awt.event.MouseEvent eUnder = getAsSeenBy().convertMouseEvent( eAsSeenBy, this.currentUnder );
				int index = this.currentUnder.calculateIndex( eUnder.getPoint() );
				BlockStatementIndexPair blockStatementIndexPair = new BlockStatementIndexPair( blockStatement, index );
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "blockStatementIndexPair", blockStatementIndexPair );
				return blockStatementIndexPair;
			} else {
				return null;
			}
		}

		@Override
		protected org.lgna.croquet.Model dragDroppedPostRejectorCheck( org.lgna.croquet.history.DragStep step ) {
			org.lgna.croquet.Model rv = null;
			final org.lgna.croquet.DragModel dragModel = step.getModel();
			org.lgna.croquet.views.DragComponent dragSource = step.getDragSource();
			final java.awt.event.MouseEvent eSource = step.getLatestMouseEvent();
			final StatementListPropertyView statementListPropertyPane = this.currentUnder;
			if( statementListPropertyPane != null ) {
				final int index = statementListPropertyPane.calculateIndex( dragSource.convertPoint( eSource.getPoint(), statementListPropertyPane ) );
				if( dragModel instanceof org.alice.ide.ast.draganddrop.statement.StatementTemplateDragModel ) {
					if( org.alice.ide.preferences.recursion.IsRecursionAllowedState.getInstance().getValue() ) {
						//pass
					} else {
						if( dragModel instanceof org.alice.ide.ast.draganddrop.statement.ProcedureInvocationTemplateDragModel ) {
							org.alice.ide.ast.draganddrop.statement.ProcedureInvocationTemplateDragModel procedureInvocationTemplateDragModel = (org.alice.ide.ast.draganddrop.statement.ProcedureInvocationTemplateDragModel)dragModel;
							org.lgna.project.ast.AbstractMethod method = procedureInvocationTemplateDragModel.getMethod();
							if( method == getCode() ) {
								StringBuilder sb = new StringBuilder();
								sb.append( "<html>" );
								sb.append( "The code you have just dropped would create a <strong><em>recursive</em></strong> method call.<p><p>Recursion is disabled by default because otherwise many users unwittingly and mistakenly make recursive calls." );
								final boolean IS_POINTING_USER_TO_RECURSION_PREFERENCE_DESIRED = true;
								if( IS_POINTING_USER_TO_RECURSION_PREFERENCE_DESIRED ) {
									sb.append( "<p><p>For more information on recursion see the Window -> Preferences menu." );
								}
								sb.append( "</html>" );
								new edu.cmu.cs.dennisc.javax.swing.option.OkDialog.Builder( sb.toString() )
										.title( "Recursion is disabled." )
										.messageType( edu.cmu.cs.dennisc.javax.swing.option.MessageType.INFORMATION )
										.buildAndShow();
								return null;
							}
						}
					}
					if( this.currentUnder != null ) {
						edu.cmu.cs.dennisc.property.InstancePropertyOwner propertyOwner = statementListPropertyPane.getProperty().getOwner();
						BlockStatementIndexPair blockStatementIndexPair;
						if( propertyOwner instanceof org.lgna.project.ast.BlockStatement ) {
							blockStatementIndexPair = new BlockStatementIndexPair( (org.lgna.project.ast.BlockStatement)propertyOwner, index );
						} else {
							blockStatementIndexPair = null;
						}
						rv = dragModel.getDropModel( step, blockStatementIndexPair );

						this.pushedContext = new org.alice.ide.cascade.BlockStatementIndexPairContext( blockStatementIndexPair );
						org.alice.ide.IDE.getActiveInstance().getExpressionCascadeManager().pushContext( this.pushedContext );
					}
				} else if( dragModel == org.alice.ide.clipboard.Clipboard.SINGLETON.getDragModel() ) {
					if( this.currentUnder != null ) {
						edu.cmu.cs.dennisc.property.InstancePropertyOwner propertyOwner = statementListPropertyPane.getProperty().getOwner();
						if( propertyOwner instanceof org.lgna.project.ast.BlockStatement ) {
							BlockStatementIndexPair blockStatementIndexPair = new BlockStatementIndexPair( (org.lgna.project.ast.BlockStatement)propertyOwner, index );
							boolean isCopy = edu.cmu.cs.dennisc.java.awt.event.InputEventUtilities.isQuoteControlUnquoteDown( eSource );
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
							if( edu.cmu.cs.dennisc.java.awt.event.InputEventUtilities.isQuoteControlUnquoteDown( eSource ) ) {
								org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
								org.lgna.project.ast.Statement copy = ide.createCopy( statement );
								rv = new org.alice.ide.code.InsertCopiedStatementOperation( nextBlockStatement, nextIndex, copy );
							} else {
								if( ( prevOwner == nextOwner ) && ( ( prevIndex == nextIndex ) || ( prevIndex == ( nextIndex - 1 ) ) ) ) {
									rv = null;
								} else {
									boolean isMultiple = eSource.isShiftDown();
									BlockStatementIndexPair fromLocation = new BlockStatementIndexPair( prevBlockStatement, prevIndex );
									BlockStatementIndexPair toLocation = new BlockStatementIndexPair( nextBlockStatement, nextIndex );
									if( isMultiple && org.alice.ide.ast.code.ShiftDragStatementUtilities.isCandidateForEnvelop( statementDragModel ) ) {
										rv = org.alice.ide.ast.code.EnvelopStatementsOperation.getInstance( fromLocation, toLocation );
									} else {
										int count;
										if( isMultiple ) {
											count = org.alice.ide.ast.code.ShiftDragStatementUtilities.calculateShiftMoveCount( fromLocation, toLocation );
										} else {
											count = 1;
										}
										if( count > 0 ) {
											rv = org.alice.ide.ast.code.MoveStatementOperation.getInstance( fromLocation, statement, toLocation, isMultiple );
										} else {
											rv = null;
										}
									}
								}
							}
						}
					}
				} else if( dragModel instanceof org.alice.ide.ast.draganddrop.expression.AbstractExpressionDragModel ) {
					if( this.currentUnder != null ) {
						edu.cmu.cs.dennisc.property.InstancePropertyOwner propertyOwner = statementListPropertyPane.getProperty().getOwner();
						BlockStatementIndexPair blockStatementIndexPair;
						if( propertyOwner instanceof org.lgna.project.ast.BlockStatement ) {
							blockStatementIndexPair = new BlockStatementIndexPair( (org.lgna.project.ast.BlockStatement)propertyOwner, index );
						} else {
							blockStatementIndexPair = null;
						}
						rv = dragModel.getDropModel( step, blockStatementIndexPair );
					}
				}
			}
			return rv;
		}

		@Override
		public final void dragExited( org.lgna.croquet.history.DragStep step, boolean isDropRecipient ) {
			statementListPropertyPaneInfos = null;
			//todo: listen to step
			this.setCurrentUnder( null, null );
			repaint();
		}

		@Override
		public final void dragStopped( org.lgna.croquet.history.DragStep step ) {
			if( this.pushedContext != null ) {
				org.alice.ide.IDE.getActiveInstance().getExpressionCascadeManager().popAndCheckContext( this.pushedContext );
				this.pushedContext = null;
			}
		}

		@Override
		public org.lgna.croquet.views.TrackableShape getTrackableShape( org.lgna.croquet.DropSite potentialDropSite ) {
			return CodePanelWithDropReceptor.this.getTrackableShape( potentialDropSite );
		}

		@Override
		public org.lgna.croquet.views.SwingComponentView<?> getViewController() {
			return CodePanelWithDropReceptor.this;
		}
	}

	public CodePanelWithDropReceptor( org.lgna.croquet.Composite<?> composite ) {
		super( composite );
	}

	public CodePanelWithDropReceptor() {
		this( null );
	}

	public InternalDropReceptor getDropReceptor() {
		return this.dropReceptor;
	}

	@Override
	protected final javax.swing.JPanel createJPanel() {
		final boolean IS_FEEDBACK_DESIRED = false;
		javax.swing.JPanel rv;
		if( IS_FEEDBACK_DESIRED ) {
			rv = new javax.swing.JPanel() {
				@Override
				public void paint( java.awt.Graphics g ) {
					super.paint( g );
					if( CodePanelWithDropReceptor.this.statementListPropertyPaneInfos != null ) {
						java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
						int i = 0;
						for( StatementListPropertyPaneInfo statementListPropertyPaneInfo : CodePanelWithDropReceptor.this.statementListPropertyPaneInfos ) {
							if( statementListPropertyPaneInfo != null ) {
								java.awt.Color color;
								if( CodePanelWithDropReceptor.this.dropReceptor.currentUnder == statementListPropertyPaneInfo.getStatementListPropertyPane() ) {
									color = new java.awt.Color( 0, 0, 0, 127 );
								} else {
									color = null;
									//color = new java.awt.Color( 255, 0, 0, 31 );
								}
								java.awt.Rectangle bounds = statementListPropertyPaneInfo.getBounds();
								bounds = javax.swing.SwingUtilities.convertRectangle( CodePanelWithDropReceptor.this.getAsSeenBy().getAwtComponent(), bounds, this );
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
			//todo: super.createJPanel() ?
			rv = new javax.swing.JPanel();
		}
		return rv;
	}

	public abstract void setJavaCodeOnTheSide( boolean value, boolean isFirstTime );

	protected abstract org.lgna.croquet.views.AwtComponentView<?> getAsSeenBy();

	public abstract org.lgna.project.ast.AbstractCode getCode();

	public abstract org.lgna.croquet.views.TrackableShape getTrackableShape( org.lgna.croquet.DropSite potentialDropSite );

	public abstract java.awt.print.Printable getPrintable();

	private StatementListPropertyPaneInfo[] statementListPropertyPaneInfos;
	private final InternalDropReceptor dropReceptor = new InternalDropReceptor();
}
