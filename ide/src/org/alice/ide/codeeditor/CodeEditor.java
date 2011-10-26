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
public class CodeEditor extends org.lgna.croquet.components.BorderPanel implements org.lgna.croquet.DropReceptor, java.awt.print.Printable {
	private StatementListPropertyView EPIC_HACK_desiredStatementListPropertyPane = null;
	private int EPIC_HACK_desiredIndex = -1;

	private org.lgna.project.ast.AbstractCode code;
	private StatementListPropertyPaneInfo[] statementListPropertyPaneInfos;
	private org.lgna.croquet.components.ScrollPane scrollPane;

	private static java.util.Map< org.lgna.project.ast.AbstractCode, CodeEditor > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static synchronized CodeEditor getInstance( org.lgna.project.ast.AbstractCode code ) {
		CodeEditor rv = map.get( code );
		if( rv != null ) {
			//pass
		} else {
			rv = new CodeEditor( code );
			map.put( code, rv );
		}
		return rv;
	}
	private CodeEditor( org.lgna.project.ast.AbstractCode code ) {
		this.code = code;
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		java.awt.Color color = getIDE().getTheme().getCodeDeclaredInAliceColor( this.code );
		color = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( color, 1.0f, 1.1f, 1.1f );
		this.setBackgroundColor( color );
	}

	public String getTutorialNoteText( org.lgna.croquet.Model model, org.lgna.croquet.edits.Edit< ? > edit, org.lgna.croquet.UserInformation userInformation ) {
		return "Drop...";
	}
	public org.lgna.croquet.resolvers.CodableResolver< CodeEditor > getCodableResolver() {
		return new org.alice.ide.croquet.resolvers.NodeStaticGetInstanceKeyedResolver<CodeEditor>( this, this.code, org.lgna.project.ast.AbstractCode.class );
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
			CodeEditor.this.refresh();
		}
	};
	private org.lgna.croquet.ListSelectionState.ValueObserver< org.alice.ide.formatter.Formatter > formatterSelectionObserver = new org.lgna.croquet.ListSelectionState.ValueObserver< org.alice.ide.formatter.Formatter >() {
		public void changing( org.lgna.croquet.State< org.alice.ide.formatter.Formatter > state, org.alice.ide.formatter.Formatter prevValue, org.alice.ide.formatter.Formatter nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.alice.ide.formatter.Formatter > state, org.alice.ide.formatter.Formatter prevValue, org.alice.ide.formatter.Formatter nextValue, boolean isAdjusting ) {
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
		if( this.code instanceof org.lgna.project.ast.UserCode ) {
			final org.lgna.project.ast.UserCode codeDeclaredInAlice = (org.lgna.project.ast.UserCode)this.code;
			ParametersPane parametersPane = new ParametersPane( org.alice.ide.x.EditableAstI18Factory.getProjectGroupInstance(), codeDeclaredInAlice );
			AbstractCodeHeaderPane header;
//			org.lgna.croquet.components.Component< ? > superInvocationPane = null;
			if( code instanceof org.lgna.project.ast.UserMethod ) {
				org.lgna.project.ast.UserMethod methodDeclaredInAlice = (org.lgna.project.ast.UserMethod)code;
				header = new MethodHeaderPane( methodDeclaredInAlice, parametersPane, false );
			} else if( code instanceof org.lgna.project.ast.NamedUserConstructor ) {
				org.lgna.project.ast.NamedUserConstructor constructorDeclaredInAlice = (org.lgna.project.ast.NamedUserConstructor)code;
				header = new ConstructorHeaderPane( constructorDeclaredInAlice, parametersPane, false );
//				superInvocationPane = new org.lgna.croquet.components.Label( "super()" );
			} else {
				throw new RuntimeException();
			}
			class RootStatementListPropertyPane extends StatementListPropertyView {
				private final org.lgna.croquet.components.Component< ? > superInvocationComponent;
				public RootStatementListPropertyPane() {
					super( org.alice.ide.x.EditableAstI18Factory.getProjectGroupInstance(), codeDeclaredInAlice.getBodyProperty().getValue().statements );
					this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 0, 48, 0 ) );
					org.lgna.project.ast.BlockStatement body = codeDeclaredInAlice.getBodyProperty().getValue();
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
			
//			org.lgna.croquet.components.Component< ? > scrollView;
			org.alice.ide.common.BodyPane bodyPane = new org.alice.ide.common.BodyPane( new RootStatementListPropertyPane() );
			this.internalAddComponent( header, java.awt.BorderLayout.NORTH );
//			if( superInvocationPane != null ) {
//				this.internalAddComponent( header, java.awt.BorderLayout.NORTH );
//				org.lgna.croquet.components.BorderPanel borderPanel = new org.lgna.croquet.components.BorderPanel();
//				borderPanel.addComponent( superInvocationPane, org.lgna.croquet.components.BorderPanel.Constraint.PAGE_START );
//				borderPanel.addComponent( bodyPane, org.lgna.croquet.components.BorderPanel.Constraint.CENTER );
//				scrollView = borderPanel;
//			} else {
//				scrollView = bodyPane;
//			}
			this.scrollPane = new org.lgna.croquet.components.ScrollPane( bodyPane );
			this.scrollPane.getAwtComponent().getVerticalScrollBar().setUnitIncrement( 12 );
			this.scrollPane.setBorder( null );
			this.scrollPane.setBackgroundColor( null );
			this.scrollPane.getAwtComponent().getViewport().setOpaque( false );
			this.scrollPane.setAlignmentX( javax.swing.JComponent.LEFT_ALIGNMENT );
			this.internalAddComponent( this.scrollPane, java.awt.BorderLayout.CENTER );

			this.addComponent( new org.alice.ide.controlflow.ControlFlowPanel(), Constraint.PAGE_END );
		}

		this.revalidateAndRepaint();
	}
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getActiveInstance();
	}
	public java.util.List< org.lgna.croquet.DropReceptor > addPotentialDropReceptors( java.util.List< org.lgna.croquet.DropReceptor > rv, final org.lgna.project.ast.AbstractType<?,?,?> type ) {
		if( type == org.lgna.project.ast.JavaType.VOID_TYPE ) {
			rv.add( this );
		} else {
			java.util.List< ExpressionPropertyDropDownPane > list = org.lgna.croquet.components.HierarchyUtilities.findAllMatches( this, ExpressionPropertyDropDownPane.class, new edu.cmu.cs.dennisc.pattern.Criterion< ExpressionPropertyDropDownPane >() {
				public boolean accept( ExpressionPropertyDropDownPane expressionPropertyDropDownPane ) {
					org.lgna.project.ast.AbstractType<?,?,?> expressionType = expressionPropertyDropDownPane.getExpressionProperty().getExpressionType();
					assert expressionType != null : expressionPropertyDropDownPane.getExpressionProperty();
					if( expressionType.isAssignableFrom( type ) ) {
						return true;
					} else {
						if( type.isArray() ) {
							if( expressionType.isAssignableFrom( type.getComponentType() ) ) {
								return true;
							} else {
								for( org.lgna.project.ast.JavaType integerType : org.lgna.project.ast.JavaType.INTEGER_TYPES ) {
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
			rv.addAll( list );
		}
		return rv;
	}
	public final boolean isPotentiallyAcceptingOf( org.lgna.croquet.DragModel dragModel ) {
		if( dragModel instanceof org.alice.ide.ast.draganddrop.statement.AbstractStatementDragModel ) {
			return getIDE().getFocusedCode() == this.code;
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
		return this.scrollPane.getViewportView();
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
					step.cancelTransaction();
					return null;
				}
			}
			
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
					
					System.err.println( "todo: investigate pushContext" );
					org.alice.ide.IDE.getActiveInstance().getCascadeManager().pushContext( null, blockStatementIndexPair );
					System.err.println( "todo: handle finally" );
				}
			} else if( dragModel instanceof org.alice.ide.clipboard.ClipboardDragModel ) {
				org.alice.ide.clipboard.ClipboardDragModel clipboardDragModel = (org.alice.ide.clipboard.ClipboardDragModel)dragModel;
				if( this.currentUnder != null ) {
					edu.cmu.cs.dennisc.property.PropertyOwner propertyOwner = statementListPropertyPane.getProperty().getOwner();
					if( propertyOwner instanceof org.lgna.project.ast.BlockStatement ) {
						BlockStatementIndexPair blockStatementIndexPair = new BlockStatementIndexPair( (org.lgna.project.ast.BlockStatement)propertyOwner, index );
						rv = clipboardDragModel.getDropModel( step, blockStatementIndexPair );
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
							org.lgna.project.ast.Statement copy = getIDE().createCopy( statement );
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
	private void resetScrollPane( final java.awt.Point viewPosition ) {
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				CodeEditor.this.scrollPane.getAwtComponent().getViewport().setViewPosition( viewPosition );
				CodeEditor.this.repaint();
			}
		} );
	}
	public final void dragExited( org.lgna.croquet.history.DragStep step, boolean isDropRecipient ) {
		this.statementListPropertyPaneInfos = null;
		//todo: listen to step
		StatementListPropertyView.EPIC_HACK_ignoreDrawingDesired = true;
		this.setCurrentUnder( null, null );
		StatementListPropertyView.EPIC_HACK_ignoreDrawingDesired = false;
		this.repaint();
	}
	public final void dragStopped( org.lgna.croquet.history.DragStep step ) {
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

					if( EPIC_HACK_isDropConstraintDesired ) {
						CodeEditor.this.EPIC_HACK_desiredStatementListPropertyPane = statementListPropertyPane;
						CodeEditor.this.EPIC_HACK_desiredIndex = index;
					}
					
					return new StatementListIndexTrackableShape(statementListProperty, index, statementListPropertyPane, boundsAtIndex);
//					return new org.lgna.croquet.TrackableShape() {
//						public java.awt.Shape getShape( org.lgna.croquet.ScreenElement asSeenBy, java.awt.Insets insets ) {
//							java.awt.Rectangle rv = CodeEditor.this.getAsSeenBy().convertRectangle( boundsAtIndex, asSeenBy );
//							//note: ignore insets
//							return rv;
//						}
//						public java.awt.Shape getVisibleShape( org.lgna.croquet.ScreenElement asSeenBy, java.awt.Insets insets ) {
//							org.lgna.croquet.Component<?> src = CodeEditor.this.getAsSeenBy();
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
//						public org.lgna.croquet.ScrollPane getScrollPaneAncestor() {
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
	
//	public org.lgna.croquet.CascadePopupPrepModel< org.lgna.project.ast.Expression > getOperation( org.lgna.project.ast.ExpressionProperty expressionProperty ) {
//		java.util.List< ExpressionPropertyDropDownPane > expressionPropertyDropDownPanes = org.lgna.croquet.components.HierarchyUtilities.findAllMatches( this, ExpressionPropertyDropDownPane.class );
//		for( final ExpressionPropertyDropDownPane expressionPropertyDropDownPane : expressionPropertyDropDownPanes ) {
//			if( expressionPropertyDropDownPane.getExpressionProperty() == expressionProperty ) {
//				return expressionPropertyDropDownPane.getModel();
//			}
//		}
//		return null;
//	}
//	public org.alice.ide.croquet.models.ast.cascade.MoreCascade getMoreOperation( org.lgna.project.ast.MethodInvocation methodInvocation ) {
//		if( methodInvocation != null ) {
//			return org.alice.ide.croquet.models.ast.cascade.MoreCascade.getInstance( methodInvocation );
////			java.util.List< org.alice.ide.common.ExpressionStatementPane > statementPanes = org.lgna.croquet.HierarchyUtilities.findAllMatches( this, org.alice.ide.common.ExpressionStatementPane.class );
////			for( org.alice.ide.common.ExpressionStatementPane statementPane : statementPanes ) {
////				if( statementPane.getStatement() == methodInvocation.getParent() ) {
////					return statementPane.getMoreOperation();
////				}
////			}
//		}
//		return null;
//	}
//	public org.lgna.croquet.PopupPrepModel getPopupMenuOperationForStatement( org.lgna.project.ast.Statement statement ) {
//		if( statement != null ) {
//			java.util.List< org.alice.ide.common.AbstractStatementPane > statementPanes = org.lgna.croquet.components.HierarchyUtilities.findAllMatches( this, org.alice.ide.common.AbstractStatementPane.class );
//			for( org.alice.ide.common.AbstractStatementPane statementPane : statementPanes ) {
//				if( statementPane.getStatement() == statement ) {
//					return statementPane.getPopupPrepModel();
//				}
//			}
//		}
//		return null;
//	}
//	public org.lgna.croquet.DragModel getDragAndDropOperationForStatement( org.lgna.project.ast.Statement statement ) {
//		if( statement != null ) {
//			java.util.List< org.alice.ide.common.AbstractStatementPane > statementPanes = org.lgna.croquet.components.HierarchyUtilities.findAllMatches( this, org.alice.ide.common.AbstractStatementPane.class );
//			for( org.alice.ide.common.AbstractStatementPane statementPane : statementPanes ) {
//				if( statementPane.getStatement() == statement ) {
//					return statementPane.getModel();
//				}
//			}
//		}
//		return null;
//	}
//	public org.lgna.croquet.DragModel getDragAndDropOperationForTransient( org.lgna.project.ast.AbstractTransient trans ) {
//		if( trans != null ) {
//			java.util.List< org.alice.ide.common.TransientPane > transientPanes = org.lgna.croquet.components.HierarchyUtilities.findAllMatches( this, org.alice.ide.common.TransientPane.class );
//			for( org.alice.ide.common.TransientPane transientPane : transientPanes ) {
//				if( transientPane.getTransient() == trans ) {
//					return transientPane.getModel();
//				}
//			}
//		}
//		return null;
//	}
	
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
