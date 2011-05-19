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

import org.alice.ide.common.DefaultStatementPane;
import org.alice.ide.common.StatementListPropertyPane;

/**
 * @author Dennis Cosgrove
 */
public class CodeEditor extends org.lgna.croquet.components.BorderPanel implements edu.cmu.cs.dennisc.croquet.DropReceptor, java.awt.print.Printable {
	private StatementListPropertyPane EPIC_HACK_desiredStatementListPropertyPane = null;
	private int EPIC_HACK_desiredIndex = -1;

	private edu.cmu.cs.dennisc.alice.ast.AbstractCode code;
	private StatementListPropertyPaneInfo[] statementListPropertyPaneInfos;
	private org.lgna.croquet.components.ScrollPane scrollPane;

	private static java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractCode, CodeEditor > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static synchronized CodeEditor getInstance( edu.cmu.cs.dennisc.alice.ast.AbstractCode code ) {
		CodeEditor rv = map.get( code );
		if( rv != null ) {
			//pass
		} else {
			rv = new CodeEditor( code );
			map.put( code, rv );
		}
		return rv;
	}
	private CodeEditor( edu.cmu.cs.dennisc.alice.ast.AbstractCode code ) {
		this.code = code;
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		java.awt.Color color = getIDE().getTheme().getCodeDeclaredInAliceColor( this.code );
		color = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( color, 1.0f, 1.1f, 1.1f );
		this.setBackgroundColor( color );
	}

	public String getTutorialNoteText( edu.cmu.cs.dennisc.croquet.CompletionModel completionModel, edu.cmu.cs.dennisc.croquet.Edit< ? > edit, edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
		return "Drop...";
	}
	public edu.cmu.cs.dennisc.croquet.CodableResolver< CodeEditor > getCodableResolver() {
		return new org.alice.ide.croquet.resolvers.NodeStaticGetInstanceKeyedResolver<CodeEditor>( this, this.code, edu.cmu.cs.dennisc.alice.ast.AbstractCode.class );
	}
	public edu.cmu.cs.dennisc.croquet.TrackableShape getTrackableShape( edu.cmu.cs.dennisc.croquet.DropSite potentialDropSite ) {
		if( potentialDropSite instanceof BlockStatementIndexPair ) {
			BlockStatementIndexPair blockStatementIndexPair = (BlockStatementIndexPair)potentialDropSite;
			edu.cmu.cs.dennisc.alice.ast.StatementListProperty statementListProperty = blockStatementIndexPair.getBlockStatement().statements;
			int index = Math.max( 0, blockStatementIndexPair.getIndex() );
			return this.getTrackableShapeAtIndexOf( statementListProperty, index, false );
		} else {
			return null;
		}
	}
	
	public edu.cmu.cs.dennisc.alice.ast.AbstractCode getCode() {
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
	private edu.cmu.cs.dennisc.croquet.State.ValueObserver<Boolean> typeFeedbackObserver = new edu.cmu.cs.dennisc.croquet.State.ValueObserver<Boolean>() {
		public void changing( edu.cmu.cs.dennisc.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}
		public void changed( edu.cmu.cs.dennisc.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			CodeEditor.this.refresh();
		}
	};
	private edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver< org.alice.ide.formatter.Formatter > formatterSelectionObserver = new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver< org.alice.ide.formatter.Formatter >() {
		public void changing( edu.cmu.cs.dennisc.croquet.State< org.alice.ide.formatter.Formatter > state, org.alice.ide.formatter.Formatter prevValue, org.alice.ide.formatter.Formatter nextValue, boolean isAdjusting ) {
		}
		public void changed( edu.cmu.cs.dennisc.croquet.State< org.alice.ide.formatter.Formatter > state, org.alice.ide.formatter.Formatter prevValue, org.alice.ide.formatter.Formatter nextValue, boolean isAdjusting ) {
			//CodeEditor.this.refresh();
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
		org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.getInstance().addValueObserver( formatterSelectionObserver );
		super.handleUndisplayable();
	}

	private void refresh() {
		if( this.scrollPane != null ) {
			final java.awt.Point viewPosition = this.scrollPane.getAwtComponent().getViewport().getViewPosition();
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: reset view position:", viewPosition );
		}

		this.forgetAndRemoveAllComponents();
		if( this.code instanceof edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice ) {
			final edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice codeDeclaredInAlice = (edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice)this.code;
			ParametersPane parametersPane = new ParametersPane( this.getIDE().getCodeFactory(), codeDeclaredInAlice );
			AbstractCodeHeaderPane header;
			if( code instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
				edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice methodDeclaredInAlice = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)code;
				header = new MethodHeaderPane( methodDeclaredInAlice, parametersPane, false );
			} else if( code instanceof edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice ) {
				edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice constructorDeclaredInAlice = (edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice)code;
				header = new ConstructorHeaderPane( constructorDeclaredInAlice, parametersPane, false );
			} else {
				throw new RuntimeException();
			}
			class RootStatementListPropertyPane extends StatementListPropertyPane {
				public RootStatementListPropertyPane() {
					super( getIDE().getCodeFactory(), codeDeclaredInAlice.getBodyProperty().getValue().statements );
					this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 0, 48, 0 ) );
				}
			}
			org.alice.ide.common.BodyPane bodyPane = new org.alice.ide.common.BodyPane( new RootStatementListPropertyPane() );
			this.scrollPane = new org.lgna.croquet.components.ScrollPane( bodyPane );
			this.scrollPane.getAwtComponent().getVerticalScrollBar().setUnitIncrement( 12 );
			this.scrollPane.setBorder( null );
			this.scrollPane.setBackgroundColor( null );
			this.scrollPane.getAwtComponent().getViewport().setOpaque( false );
			this.scrollPane.setAlignmentX( javax.swing.JComponent.LEFT_ALIGNMENT );
			this.internalAddComponent( header, java.awt.BorderLayout.NORTH );
			this.internalAddComponent( scrollPane, java.awt.BorderLayout.CENTER );
		}

		this.revalidateAndRepaint();
	}
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getSingleton();
	}
	public java.util.List< ? extends ExpressionPropertyDropDownPane > createListOfPotentialDropReceptors( final edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type ) {
		return org.lgna.croquet.components.HierarchyUtilities.findAllMatches( this, ExpressionPropertyDropDownPane.class, new edu.cmu.cs.dennisc.pattern.Criterion< ExpressionPropertyDropDownPane >() {
			public boolean accept( ExpressionPropertyDropDownPane expressionPropertyDropDownPane ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> expressionType = expressionPropertyDropDownPane.getExpressionProperty().getExpressionType();
				if( expressionType.isAssignableFrom( type ) ) {
					return true;
				} else {
					if( type.isArray() ) {
						if( expressionType.isAssignableFrom( type.getComponentType() ) ) {
							return true;
						} else {
							for( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava integerType : edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_TYPES ) {
								if( expressionType == integerType ) {
									return true;
								}
							}
						}
					}
				}
				return false;
			}
		} );
	}
	public final boolean isPotentiallyAcceptingOf( org.lgna.croquet.components.DragComponent source ) {
		if( source instanceof org.alice.ide.templates.StatementTemplate ) {
			return getIDE().getFocusedCode() == this.code;
		} else {
			return false;
		}
	}
	
	private StatementListPropertyPane currentUnder;
	
	private void setCurrentUnder( StatementListPropertyPane nextUnder, java.awt.Dimension dropSize ) {
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
	public final void dragStarted( org.lgna.croquet.steps.DragStep context ) {
	}

	public final void dragEntered( org.lgna.croquet.steps.DragStep context ) {
		org.lgna.croquet.components.DragComponent source = context.getDragSource();
		this.statementListPropertyPaneInfos = createStatementListPropertyPaneInfos( source );
		this.repaint();
	}
	private org.lgna.croquet.components.Component< ? > getAsSeenBy() {
		return this.scrollPane.getViewportView();
	}
	private StatementListPropertyPaneInfo[] createStatementListPropertyPaneInfos( org.lgna.croquet.components.Container<?> source ) {
		java.util.List< StatementListPropertyPane > statementListPropertyPanes = org.lgna.croquet.components.HierarchyUtilities.findAllMatches( this, StatementListPropertyPane.class );
		StatementListPropertyPaneInfo[] rv = new StatementListPropertyPaneInfo[ statementListPropertyPanes.size() ];
		int i = 0;
		for( StatementListPropertyPane statementListPropertyPane : statementListPropertyPanes ) {
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
	private StatementListPropertyPane getStatementListPropertyPaneUnder( java.awt.event.MouseEvent e, StatementListPropertyPaneInfo[] statementListPropertyPaneInfos ) {
		StatementListPropertyPane rv = null;
		for( StatementListPropertyPaneInfo statementListPropertyPaneInfo : this.statementListPropertyPaneInfos ) {
			if( statementListPropertyPaneInfo != null ) {
				if( statementListPropertyPaneInfo.contains( e ) ) {
					StatementListPropertyPane slpp = statementListPropertyPaneInfo.getStatementListPropertyPane();
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
	public final BlockStatementIndexPair dragUpdated( org.lgna.croquet.steps.DragStep context ) {
		org.lgna.croquet.components.DragComponent source = context.getDragSource();
		if( source != null ) {
			java.awt.event.MouseEvent eSource = context.getLatestMouseEvent();
			java.awt.event.MouseEvent eAsSeenBy = source.convertMouseEvent( eSource, this.getAsSeenBy() );
			StatementListPropertyPane nextUnder = getStatementListPropertyPaneUnder( eAsSeenBy, this.statementListPropertyPaneInfos );
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
							edu.cmu.cs.dennisc.alice.ast.Statement statement = abstractStatementPane.getStatement();
							edu.cmu.cs.dennisc.alice.ast.StatementListProperty prevOwner = abstractStatementPane.getOwner();
							edu.cmu.cs.dennisc.alice.ast.StatementListProperty nextOwner = this.currentUnder.getProperty();

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
									p.y += StatementListPropertyPane.INTRASTICIAL_PAD;
									if( this.currentUnder.getProperty() == ((edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice)this.code).getBodyProperty().getValue().statements ) {
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
			edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatement = (edu.cmu.cs.dennisc.alice.ast.BlockStatement)this.currentUnder.getProperty().getOwner();
			java.awt.event.MouseEvent eSource = context.getLatestMouseEvent();
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
	public final edu.cmu.cs.dennisc.croquet.Operation<?> dragDropped( final org.lgna.croquet.steps.DragStep context ) {
		edu.cmu.cs.dennisc.croquet.Operation<?> rv = null;
		final org.lgna.croquet.components.DragComponent source = context.getDragSource();
		final java.awt.event.MouseEvent eSource = context.getLatestMouseEvent();
		final StatementListPropertyPane statementListPropertyPane = CodeEditor.this.currentUnder;
		if( statementListPropertyPane != null ) {
			final int index = statementListPropertyPane.calculateIndex( source.convertPoint( eSource.getPoint(), statementListPropertyPane ) );

			if( EPIC_HACK_desiredStatementListPropertyPane != null && EPIC_HACK_desiredIndex != -1 ) {
				int desiredIndex;
				if( EPIC_HACK_desiredIndex == Short.MAX_VALUE ) {
					desiredIndex = statementListPropertyPane.getProperty().size();
				} else {
					desiredIndex = EPIC_HACK_desiredIndex;
				}
				if( EPIC_HACK_desiredStatementListPropertyPane != statementListPropertyPane || desiredIndex != index ) {
//					EPIC_HACK_desiredStatementListPropertyPane = null;
//					EPIC_HACK_desiredIndex = -1;
//					source.hideDropProxyIfNecessary();
					context.cancelTransaction();
					return null;
				}
			}
			
			if( source instanceof org.alice.ide.templates.StatementTemplate ) {
				final org.alice.ide.templates.StatementTemplate statementTemplate = (org.alice.ide.templates.StatementTemplate)source;
				if( org.alice.ide.croquet.models.recursion.IsRecursionAllowedState.getInstance().getValue() ) {
					//pass
				} else {
					edu.cmu.cs.dennisc.alice.ast.AbstractMethod method;
					if( statementTemplate instanceof org.alice.ide.memberseditor.templates.ProcedureInvocationTemplate ) {
						org.alice.ide.memberseditor.templates.ProcedureInvocationTemplate procedureInvocationTemplate = (org.alice.ide.memberseditor.templates.ProcedureInvocationTemplate)statementTemplate;
						method = procedureInvocationTemplate.getMethod();
					} else {
						method = null;
					}
					if( method != null ) {
						if( method == this.getCode() ) {
							StringBuilder sb = new StringBuilder();
							sb.append( "<html>" );
							sb.append( "The code you have just dropped would create a <strong><em>recursive</em></strong> method call.<p><p>Recursion is disabled by default because otherwise many users unwittingly and mistakenly make recursive calls." );
							final boolean IS_POINTING_USER_TO_RECURSION_PREFERENCE_DESIRED = true;
							if( IS_POINTING_USER_TO_RECURSION_PREFERENCE_DESIRED ) {
								sb.append( "<p><p>For more information on recursion see the Window -> Preferences menu." );
							}
							sb.append( "</html>" );
							org.alice.ide.IDE.getSingleton().showMessageDialog( sb.toString(), "Recursion is disabled.", edu.cmu.cs.dennisc.croquet.MessageType.ERROR );
							return null;
						}
					}
				}
				if( this.currentUnder != null ) {
					edu.cmu.cs.dennisc.property.PropertyOwner propertyOwner = statementListPropertyPane.getProperty().getOwner();
					edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatement;
					if( propertyOwner instanceof edu.cmu.cs.dennisc.alice.ast.BlockStatement ) {
						blockStatement = (edu.cmu.cs.dennisc.alice.ast.BlockStatement)propertyOwner;
					} else {
						blockStatement = null;
						//index = -1;
					}
					rv = statementTemplate.getDropOperation( context, blockStatement, index );
				}
			} else if( source != null && source.getSubject() instanceof org.alice.ide.common.AbstractStatementPane ) {
				if( this.currentUnder != null ) {
					org.alice.ide.common.AbstractStatementPane abstractStatementPane = (org.alice.ide.common.AbstractStatementPane)source.getSubject();
					final edu.cmu.cs.dennisc.alice.ast.Statement statement = abstractStatementPane.getStatement();
					final edu.cmu.cs.dennisc.alice.ast.StatementListProperty prevOwner = abstractStatementPane.getOwner();
					final edu.cmu.cs.dennisc.alice.ast.StatementListProperty nextOwner = this.currentUnder.getProperty();
					final int prevIndex = prevOwner.indexOf( statement );
					final int nextIndex = this.currentUnder.calculateIndex( source.convertPoint( eSource.getPoint(), this.currentUnder ) );

					edu.cmu.cs.dennisc.alice.ast.BlockStatement prevBlockStatement = (edu.cmu.cs.dennisc.alice.ast.BlockStatement)prevOwner.getOwner();
					edu.cmu.cs.dennisc.alice.ast.BlockStatement nextBlockStatement = (edu.cmu.cs.dennisc.alice.ast.BlockStatement)nextOwner.getOwner();
					if( edu.cmu.cs.dennisc.javax.swing.SwingUtilities.isQuoteControlUnquoteDown( eSource ) ) {
						edu.cmu.cs.dennisc.alice.ast.Statement copy = (edu.cmu.cs.dennisc.alice.ast.Statement)getIDE().createCopy( statement );
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
		return rv;
	}
	private void resetScrollPane( final java.awt.Point viewPosition ) {
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				CodeEditor.this.scrollPane.getAwtComponent().getViewport().setViewPosition( viewPosition );
				CodeEditor.this.repaint();
			}
		} );
	}
	public final void dragExited( org.lgna.croquet.steps.DragStep context, boolean isDropRecipient ) {
		this.statementListPropertyPaneInfos = null;
		//todo: listen to context
		StatementListPropertyPane.EPIC_HACK_ignoreDrawingDesired = true;
		this.setCurrentUnder( null, null );
		StatementListPropertyPane.EPIC_HACK_ignoreDrawingDesired = false;
		this.repaint();
	}
	public final void dragStopped( org.lgna.croquet.steps.DragStep context ) {
		EPIC_HACK_desiredStatementListPropertyPane = null;
		EPIC_HACK_desiredIndex = -1;
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
	
	public class StatementListIndexTrackableShape implements edu.cmu.cs.dennisc.croquet.TrackableShape {
		private edu.cmu.cs.dennisc.alice.ast.StatementListProperty statementListProperty;
		private int index;
		private StatementListPropertyPane statementListPropertyPane;
		private java.awt.Rectangle boundsAtIndex;
		private StatementListIndexTrackableShape( edu.cmu.cs.dennisc.alice.ast.StatementListProperty statementListProperty, int index, StatementListPropertyPane statementListPropertyPane, java.awt.Rectangle boundsAtIndex ) {
			this.statementListProperty = statementListProperty;
			this.index = index;
			this.statementListPropertyPane = statementListPropertyPane;
			this.boundsAtIndex = boundsAtIndex;
		}
		
		private edu.cmu.cs.dennisc.alice.ast.StatementListProperty getStatementListProperty() {
			return this.statementListProperty;
		}
		public edu.cmu.cs.dennisc.alice.ast.BlockStatement getBlockStatement() {
			return (edu.cmu.cs.dennisc.alice.ast.BlockStatement)this.statementListProperty.getOwner();
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
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: getTrackableShapeAtIndexOf isInView" );
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
	public edu.cmu.cs.dennisc.croquet.TrackableShape getTrackableShapeAtIndexOf( edu.cmu.cs.dennisc.alice.ast.StatementListProperty statementListProperty, int index, boolean EPIC_HACK_isDropConstraintDesired ) {
		if( statementListProperty != null ) {
			//choose any non-ancestor
			
			org.lgna.croquet.components.Container< ? > arbitrarilyChosenSource = org.alice.ide.IDE.getSingleton().getSceneEditor();
			StatementListPropertyPaneInfo[] statementListPropertyPaneInfos = this.createStatementListPropertyPaneInfos( arbitrarilyChosenSource );
			final int N = statementListPropertyPaneInfos.length;
			for( int i=0; i<N; i++ ) {
				StatementListPropertyPaneInfo statementListPropertyPaneInfo = statementListPropertyPaneInfos[ i ];
				StatementListPropertyPane statementListPropertyPane = statementListPropertyPaneInfo.getStatementListPropertyPane();
				if( statementListPropertyPane.getProperty() == statementListProperty ) {
					StatementListPropertyPane.BoundInformation yBounds = statementListPropertyPane.calculateYBounds( index );
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

					if( EPIC_HACK_isDropConstraintDesired ) {
						CodeEditor.this.EPIC_HACK_desiredStatementListPropertyPane = statementListPropertyPane;
						CodeEditor.this.EPIC_HACK_desiredIndex = index;
					}
					
					return new StatementListIndexTrackableShape(statementListProperty, index, statementListPropertyPane, boundsAtIndex);
//					return new edu.cmu.cs.dennisc.croquet.TrackableShape() {
//						public java.awt.Shape getShape( edu.cmu.cs.dennisc.croquet.ScreenElement asSeenBy, java.awt.Insets insets ) {
//							java.awt.Rectangle rv = CodeEditor.this.getAsSeenBy().convertRectangle( boundsAtIndex, asSeenBy );
//							//note: ignore insets
//							return rv;
//						}
//						public java.awt.Shape getVisibleShape( edu.cmu.cs.dennisc.croquet.ScreenElement asSeenBy, java.awt.Insets insets ) {
//							edu.cmu.cs.dennisc.croquet.Component<?> src = CodeEditor.this.getAsSeenBy();
//							if( src != null ) {
//								java.awt.Rectangle bounds = src.convertRectangle( boundsAtIndex, asSeenBy );
//								//note: ignore insets
////									java.awt.Rectangle visibleBounds = statementListPropertyPane.getVisibleRectangle( asSeenBy );
////									return bounds.intersection( visibleBounds );
//								return bounds;
//							} else {
//								return null;
//							}
//						}
//						public boolean isInView() {
//							if( isWarningAlreadyPrinted ) {
//								//pass
//							} else {
//								edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: getTrackableShapeAtIndexOf isInView" );
//								isWarningAlreadyPrinted = true;
//							}
//							return true;
//						}
//						public edu.cmu.cs.dennisc.croquet.ScrollPane getScrollPaneAncestor() {
//							return statementListPropertyPane.getScrollPaneAncestor();
//						}
//						public void addComponentListener(java.awt.event.ComponentListener listener) {
//							statementListPropertyPane.addComponentListener(listener);
//						}
//						public void removeComponentListener(java.awt.event.ComponentListener listener) {
//							statementListPropertyPane.removeComponentListener(listener);
//						}
//						public void addHierarchyBoundsListener(java.awt.event.HierarchyBoundsListener listener) {
//							statementListPropertyPane.addHierarchyBoundsListener(listener);
//						}
//						public void removeHierarchyBoundsListener(java.awt.event.HierarchyBoundsListener listener) {
//							statementListPropertyPane.removeHierarchyBoundsListener(listener);
//						}
//					};
				}
			}
		}
		return null;
	}
	
	public edu.cmu.cs.dennisc.croquet.CascadePopupOperation< edu.cmu.cs.dennisc.alice.ast.Expression > getOperation( edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty ) {
		java.util.List< ExpressionPropertyDropDownPane > expressionPropertyDropDownPanes = org.lgna.croquet.components.HierarchyUtilities.findAllMatches( this, ExpressionPropertyDropDownPane.class );
		for( final ExpressionPropertyDropDownPane expressionPropertyDropDownPane : expressionPropertyDropDownPanes ) {
			if( expressionPropertyDropDownPane.getExpressionProperty() == expressionProperty ) {
				return expressionPropertyDropDownPane.getModel();
			}
		}
		return null;
	}
	public org.alice.ide.croquet.models.ast.cascade.FillInMoreOperation getMoreOperation( edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation ) {
		if( methodInvocation != null ) {
			return org.alice.ide.croquet.models.ast.cascade.FillInMoreOperation.getInstance( methodInvocation );
//			java.util.List< org.alice.ide.common.ExpressionStatementPane > statementPanes = edu.cmu.cs.dennisc.croquet.HierarchyUtilities.findAllMatches( this, org.alice.ide.common.ExpressionStatementPane.class );
//			for( org.alice.ide.common.ExpressionStatementPane statementPane : statementPanes ) {
//				if( statementPane.getStatement() == methodInvocation.getParent() ) {
//					return statementPane.getMoreOperation();
//				}
//			}
		}
		return null;
	}
	public edu.cmu.cs.dennisc.croquet.StandardPopupOperation getPopupMenuOperationForStatement( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
		if( statement != null ) {
			java.util.List< org.alice.ide.common.AbstractStatementPane > statementPanes = org.lgna.croquet.components.HierarchyUtilities.findAllMatches( this, org.alice.ide.common.AbstractStatementPane.class );
			for( org.alice.ide.common.AbstractStatementPane statementPane : statementPanes ) {
				if( statementPane.getStatement() == statement ) {
					return statementPane.getPopupMenuOperation();
				}
			}
		}
		return null;
	}
	public edu.cmu.cs.dennisc.croquet.DragAndDropModel getDragAndDropOperationForStatement( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
		if( statement != null ) {
			java.util.List< org.alice.ide.common.AbstractStatementPane > statementPanes = org.lgna.croquet.components.HierarchyUtilities.findAllMatches( this, org.alice.ide.common.AbstractStatementPane.class );
			for( org.alice.ide.common.AbstractStatementPane statementPane : statementPanes ) {
				if( statementPane.getStatement() == statement ) {
					return statementPane.getDragModel();
				}
			}
		}
		return null;
	}
	public edu.cmu.cs.dennisc.croquet.DragAndDropModel getDragAndDropOperationForTransient( edu.cmu.cs.dennisc.alice.ast.AbstractTransient trans ) {
		if( trans != null ) {
			java.util.List< org.alice.ide.common.TransientPane > transientPanes = org.lgna.croquet.components.HierarchyUtilities.findAllMatches( this, org.alice.ide.common.TransientPane.class );
			for( org.alice.ide.common.TransientPane transientPane : transientPanes ) {
				if( transientPane.getTransient() == trans ) {
					return transientPane.getDragModel();
				}
			}
		}
		return null;
	}
	
	public int print(java.awt.Graphics g, java.awt.print.PageFormat pageFormat, int pageIndex) throws java.awt.print.PrinterException {
		if( pageIndex > 0 ) {
			return NO_SUCH_PAGE;
		} else {
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
			org.lgna.croquet.components.Component<?> component0 = this.getComponent( 0 );
			int width = Math.max( component0.getAwtComponent().getPreferredSize().width, this.scrollPane.getViewportView().getAwtComponent().getPreferredSize().width );
			int height = this.scrollPane.getY() + this.scrollPane.getViewportView().getAwtComponent().getPreferredSize().height;
			double scale = edu.cmu.cs.dennisc.java.awt.print.PageFormatUtilities.calculateScale(pageFormat, width, height);
			g2.translate( pageFormat.getImageableX(), pageFormat.getImageableY() );
			if( scale > 1.0 ) {
				g2.scale( 1.0/scale, 1.0/scale );
			}
			component0.getAwtComponent().printAll( g2 );
			g2.translate( this.scrollPane.getX(), this.scrollPane.getY() );
			this.scrollPane.getViewportView().getAwtComponent().printAll( g2 );
			return PAGE_EXISTS;
		}
	}
}
