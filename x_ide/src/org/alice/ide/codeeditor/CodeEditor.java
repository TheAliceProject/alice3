/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.ide.codeeditor;

import org.alice.ide.common.StatementListPropertyPane;

/**
 * @author Dennis Cosgrove
 */
class StatementListPropertyPaneInfo {
	private StatementListPropertyPane statementListPropertyPane;
	private java.awt.Rectangle bounds;

	public StatementListPropertyPaneInfo( StatementListPropertyPane statementListPropertyPane, java.awt.Rectangle bounds ) {
		this.statementListPropertyPane = statementListPropertyPane;
		this.bounds = bounds;
	}
	public boolean contains( java.awt.event.MouseEvent e ) {
		return this.bounds.contains( e.getPoint() );
	}
	public StatementListPropertyPane getStatementListPropertyPane() {
		return this.statementListPropertyPane;
	}
	public void setStatementListPropertyPane( StatementListPropertyPane statementListPropertyPane ) {
		this.statementListPropertyPane = statementListPropertyPane;
	}
	public java.awt.Rectangle getBounds() {
		return this.bounds;
	}
	public void setBounds( java.awt.Rectangle bounds ) {
		this.bounds = bounds;
	}
}

/**
 * @author Dennis Cosgrove
 */
public class CodeEditor extends edu.cmu.cs.dennisc.croquet.swing.PageAxisPane implements org.alice.ide.event.IDEListener, edu.cmu.cs.dennisc.zoot.DropReceptor {
	public void fieldSelectionChanged( org.alice.ide.event.FieldSelectionEvent e ) {
	}
	public void fieldSelectionChanging( org.alice.ide.event.FieldSelectionEvent e ) {
	}
	public void focusedCodeChanged( org.alice.ide.event.FocusedCodeChangeEvent e ) {
	}
	public void focusedCodeChanging( org.alice.ide.event.FocusedCodeChangeEvent e ) {
	}
	public void localeChanged( org.alice.ide.event.LocaleEvent e ) {
		this.refresh();
		this.repaint();
	}
	public void localeChanging( org.alice.ide.event.LocaleEvent e ) {
	}
	public void projectOpened( org.alice.ide.event.ProjectOpenEvent e ) {
	}
	public void projectOpening( org.alice.ide.event.ProjectOpenEvent e ) {
	}
	public void transientSelectionChanged( org.alice.ide.event.TransientSelectionEvent e ) {
	}
	public void transientSelectionChanging( org.alice.ide.event.TransientSelectionEvent e ) {
	}

	private edu.cmu.cs.dennisc.alice.ast.AbstractCode code;
	private StatementListPropertyPaneInfo[] statementListPropertyPaneInfos;
	private StatementListPropertyPane currentUnder;
	private javax.swing.JScrollPane scrollPane;

	public CodeEditor( edu.cmu.cs.dennisc.alice.ast.AbstractCode code ) {
		this.getIDE().addIDEListener( this );
		this.code = code;
		this.setOpaque( true );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		this.setBackground( getIDE().getCodeDeclaredInAliceColor( this.code ) );
		this.setDoubleBuffered( true );
		this.refresh();
		//		this.addMouseListener( new java.awt.event.MouseListener() {
		//			public void mouseClicked( final java.awt.event.MouseEvent e ) {
		//				final alice.ide.IDE ide = alice.ide.IDE.getSingleton();
		//				if( ide != null ) {
		//					final StatementListPropertyPane statementListPropertyPane = getStatementListPropertyPaneUnder( e, createStatementListPropertyPaneInfos( null ) );
		//					if( statementListPropertyPane != null ) {
		//						ide.promptUserForStatement( e, new edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Statement >() {
		//							public void handleCompletion( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
		//								java.awt.Point p = javax.swing.SwingUtilities.convertPoint( e.getComponent(), e.getPoint(), statementListPropertyPane );
		//								statementListPropertyPane.getProperty().add( statementListPropertyPane.calculateIndex( p ), statement );
		//								ide.markChanged( "statement" );
		//							}
		//							public void handleCancelation() {
		//							}
		//						} );
		//					}
		//				}
		//			}
		//			public void mouseEntered( java.awt.event.MouseEvent e ) {
		//			}
		//			public void mouseExited( java.awt.event.MouseEvent e ) {
		//			}
		//			public void mousePressed( java.awt.event.MouseEvent e ) {
		//			}
		//			public void mouseReleased( java.awt.event.MouseEvent e ) {
		//			}
		//		} );

		//		this.addHierarchyListener( new java.awt.event.HierarchyListener() {
		//			public void hierarchyChanged( java.awt.event.HierarchyEvent e ) {
		//				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "hierarchyChanged", getIDE().isAncestorOf( CodeEditor.this ), CodeEditor.this.isShowing(), CodeEditor.this.getCode() );
		//				if( getIDE().isAncestorOf( CodeEditor.this ) && CodeEditor.this.isShowing() ) {
		//					getIDE().addDropReceptorIfNecessary( CodeEditor.this );
		//				} else {
		//					getIDE().removeDropReceptorIfNecessary( CodeEditor.this );
		//				}
		//			}
		//		} );
	}

	@Override
	public String getName() {
		if( this.code instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)this.code;
			return method.getName();
		} else if( this.code instanceof edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice ) {
			//edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice constructor = (edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice)this.code;
			return "constructor";
		} else {
			return super.getName();
		}
	}

	protected javax.swing.JComponent createParametersPane( edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code ) {
		return new ParametersPane( this.getIDE().getCodeFactory(), code );
	}

	public edu.cmu.cs.dennisc.alice.ast.AbstractCode getCode() {
		return this.code;
	}
	public java.awt.Component getAWTComponent() {
		return this;
	}
	public void refresh() {
		edu.cmu.cs.dennisc.swing.ForgetUtilities.forgetAndRemoveAllComponents( this );
		if( this.code instanceof edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice ) {
			final edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice codeDeclaredInAlice = (edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice)this.code;
			javax.swing.JComponent parametersPane = createParametersPane( codeDeclaredInAlice );
			javax.swing.JComponent header;
			if( code instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
				edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice methodDeclaredInAlice = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)code;
				header = new MethodHeaderPane( methodDeclaredInAlice, parametersPane );
			} else if( code instanceof edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice ) {
				edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice constructorDeclaredInAlice = (edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice)code;
				header = new ConstructorHeaderPane( constructorDeclaredInAlice, parametersPane );
			} else {
				throw new RuntimeException();
			}

			class RootStatementListPropertyPane extends StatementListPropertyPane {
				public RootStatementListPropertyPane() {
					super( getIDE().getCodeFactory(), codeDeclaredInAlice.getBodyProperty().getValue().statements );
				}
				@Override
				protected boolean isMaximumSizeClampedToPreferredSize() {
					return false;
				}
				//				@Override
				//				protected void paintComponent( java.awt.Graphics g ) {
				//					super.paintComponent( g );
				//					int x = 0;
				//					int y = 0;
				//					int width = this.getWidth() -1;
				//					int height = this.getHeight() -1;
				//					int arcWidth = 8;
				//					int arcHeight = 8;
				//					g.setColor( org.alice.ide.IDE.getColorForASTClass( edu.cmu.cs.dennisc.alice.ast.DoInOrder.class ) );
				//					g.fillRoundRect( x, y, width, height, arcWidth, arcHeight );
				//					g.setColor( java.awt.Color.GRAY );
				//					g.drawRoundRect( x, y, width, height, arcWidth, arcHeight );
				//
				////					if( getIDE().isJava() ) {
				////						//pass
				////					} else {
				////						java.awt.FontMetrics fm = g.getFontMetrics();
				////					    int ascent = fm.getMaxAscent ();
				////					    int descent= fm.getMaxDescent ();
				////					    
				////						g.setColor( java.awt.Color.BLACK );
				////					    x = 4;
				////					    y = 4;
				////						g.drawString( "do in order", x, y + ascent-descent );
				////					}
				//				}
			}
			//			RootStatementListPropertyPane bodyPane = new RootStatementListPropertyPane();
			//bodyPane.setFont( bodyPane.getFont().deriveFont( java.awt.Font.BOLD ) );
			//bodyPane.setBorder( javax.swing.BorderFactory.createEmptyBorder( bodyPane.getFont().getSize() + 8, 16, 4, 4 ) );

			org.alice.ide.common.BodyPane bodyPane = new org.alice.ide.common.BodyPane( new RootStatementListPropertyPane() );

			//			javax.swing.JPanel panel = new javax.swing.JPanel();
			//			panel.setLayout( new java.awt.GridLayout() );
			//			panel.add( bodyPane );

			this.scrollPane = new javax.swing.JScrollPane( bodyPane );
			this.scrollPane.getVerticalScrollBar().setUnitIncrement( 12 );
			this.scrollPane.setBorder( null );
			this.scrollPane.setOpaque( false );
			this.scrollPane.getViewport().setOpaque( false );
			//this.scrollPane.setBackground( java.awt.Color.RED );
			this.scrollPane.setAlignmentX( javax.swing.JComponent.LEFT_ALIGNMENT );
			this.add( header );
			if( this.getIDE().isEmphasizingClasses() || this.getIDE().isInstanceLineDesired() == false ) {
				//pass
			} else {
				header.add( new InstanceLine( this.code ) );
			}
			//			this.add( javax.swing.Box.createVerticalStrut( 8 ) );
			this.add( scrollPane );
		}
		this.revalidate();
	}
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getSingleton();
	}
	public java.util.List< ? extends ExpressionPropertyDropDownPane > createListOfPotentialDropReceptors( final edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		return edu.cmu.cs.dennisc.awt.ComponentUtilities.findAllMatches( this, ExpressionPropertyDropDownPane.class, new edu.cmu.cs.dennisc.pattern.Criterion< ExpressionPropertyDropDownPane >() {
			public boolean accept( ExpressionPropertyDropDownPane expressionPropertyDropDownPane ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractType expressionType = expressionPropertyDropDownPane.getExpressionProperty().getExpressionType();
				if( expressionType.isAssignableFrom( type ) ) {
					return true;
				} else {
					if( type.isArray() ) {
						if( expressionType.isAssignableFrom( type.getComponentType() ) ) {
							return true;
						} else {
							for( edu.cmu.cs.dennisc.alice.ast.AbstractType integerType : edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_TYPES ) {
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
	public boolean isPotentiallyAcceptingOf( edu.cmu.cs.dennisc.zoot.ZDragComponent source ) {
		if( source instanceof org.alice.ide.templates.StatementTemplate ) {
			return getIDE().getFocusedCode() == this.code;
		} else {
			return false;
		}
	}
	public void dragStarted( edu.cmu.cs.dennisc.zoot.DragAndDropContext dragAndDropContext ) {
	}

	public void dragEntered( edu.cmu.cs.dennisc.zoot.DragAndDropContext dragAndDropContext ) {
		edu.cmu.cs.dennisc.zoot.ZDragComponent source = dragAndDropContext.getDragSource();
		this.statementListPropertyPaneInfos = createStatementListPropertyPaneInfos( source );
		this.repaint();
	}
	private java.awt.Component getAsSeenBy() {
		return this.scrollPane.getViewport().getView();
	}
	private StatementListPropertyPaneInfo[] createStatementListPropertyPaneInfos( edu.cmu.cs.dennisc.zoot.ZDragComponent source ) {
		java.util.List< StatementListPropertyPane > statementListPropertyPanes = edu.cmu.cs.dennisc.awt.ComponentUtilities.findAllMatches( this, StatementListPropertyPane.class );
		StatementListPropertyPaneInfo[] rv = new StatementListPropertyPaneInfo[ statementListPropertyPanes.size() ];
		int i = 0;
		for( StatementListPropertyPane statementListPropertyPane : statementListPropertyPanes ) {
			if( source != null && source.isAncestorOf( statementListPropertyPane ) ) {
				continue;
			}
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( statementListPropertyPane );
			java.awt.Rectangle bounds = javax.swing.SwingUtilities.convertRectangle( statementListPropertyPane, statementListPropertyPane.getDropBounds(), this.getAsSeenBy() );
			bounds.x = 0;
			bounds.width = this.getAsSeenBy().getWidth() - bounds.x;
			rv[ i ] = new StatementListPropertyPaneInfo( statementListPropertyPane, bounds );
			i++;
		}
		return rv;

	}
	private StatementListPropertyPane getStatementListPropertyPaneUnder( java.awt.event.MouseEvent e, StatementListPropertyPaneInfo[] statementListPropertyPaneInfos ) {
		StatementListPropertyPane rv = null;
		for( StatementListPropertyPaneInfo statementListPropertyPaneInfo : statementListPropertyPaneInfos ) {
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
	public void dragUpdated( edu.cmu.cs.dennisc.zoot.DragAndDropContext dragAndDropContext ) {
		edu.cmu.cs.dennisc.zoot.ZDragComponent source = dragAndDropContext.getDragSource();
		if( source != null ) {
			java.awt.event.MouseEvent eSource = dragAndDropContext.getLatestMouseEvent();
			java.awt.event.MouseEvent eAsSeenBy = edu.cmu.cs.dennisc.swing.SwingUtilities.convertMouseEvent( source, eSource, this.getAsSeenBy() );
			StatementListPropertyPane nextUnder = getStatementListPropertyPaneUnder( eAsSeenBy, this.statementListPropertyPaneInfos );
			this.currentUnder = nextUnder;
			if( this.currentUnder != null ) {
				boolean isDropProxyAlreadyUpdated = false;
				if( edu.cmu.cs.dennisc.swing.SwingUtilities.isQuoteControlUnquoteDown( eSource ) ) {
					//pass
				} else {
					java.awt.Component subject = source.getSubject();
					if( subject instanceof org.alice.ide.common.AbstractStatementPane ) {
						org.alice.ide.common.AbstractStatementPane abstractStatementPane = (org.alice.ide.common.AbstractStatementPane)subject;
						if( source instanceof org.alice.ide.templates.StatementTemplate ) {
							//pass
						} else {
							edu.cmu.cs.dennisc.alice.ast.Statement statement = abstractStatementPane.getStatement();
							edu.cmu.cs.dennisc.alice.ast.StatementListProperty prevOwner = abstractStatementPane.getOwner();
							edu.cmu.cs.dennisc.alice.ast.StatementListProperty nextOwner = this.currentUnder.getProperty();

							int prevIndex = prevOwner.indexOf( statement );
							int nextIndex = this.currentUnder.calculateIndex( javax.swing.SwingUtilities.convertPoint( source, eSource.getPoint(), this.currentUnder ) );

							if( prevOwner == nextOwner ) {
								if( prevIndex == nextIndex || prevIndex == nextIndex - 1 ) {
									source.setDropProxyLocationAndShowIfNecessary( new java.awt.Point( 0, 0 ), source, null );
									isDropProxyAlreadyUpdated = true;
								}
							}
						}
					}
				}
				if( isDropProxyAlreadyUpdated ) {
					//pass
				} else {
					java.awt.event.MouseEvent eUnder = edu.cmu.cs.dennisc.swing.SwingUtilities.convertMouseEvent( this.getAsSeenBy(), eAsSeenBy, this.currentUnder );
					Integer height = 0;
					java.awt.Insets insets = this.currentUnder.getBorder().getBorderInsets( this.currentUnder );
					int x = insets.left;
					java.awt.Point p = new java.awt.Point( x, 0 );
					if( this.currentUnder.isFigurativelyEmpty() ) {
						height = null;
						p.y = insets.top;
					} else {
						int n = this.currentUnder.getComponentCount();
						if( n > 0 ) {
							int index = this.currentUnder.calculateIndex( eUnder.getPoint() );
							if( index == 0 ) {
								//java.awt.Component firstComponent = this.currentUnder.getComponent( 0 );
								p.y = 0;
							} else if( index < n ) {
								p.y = this.currentUnder.getComponent( index ).getY();
							} else {
								java.awt.Component lastComponent = this.currentUnder.getComponent( n - 1 );
								p.y = lastComponent.getY() + lastComponent.getHeight();
								p.y += StatementListPropertyPane.INTRASTICIAL_PAD;
								if( this.currentUnder.getProperty() == ((edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice)this.code).getBodyProperty().getValue().statements ) {
									height = null;
								}
							}
						}
					}
					source.setDropProxyLocationAndShowIfNecessary( p, this.currentUnder, height );
				}
			} else {
				source.hideDropProxyIfNecessary();
			}
		}
		this.repaint();

	}
	public void dragDropped( edu.cmu.cs.dennisc.zoot.DragAndDropContext dragAndDropContext ) {
		final java.awt.Point viewPosition = this.scrollPane.getViewport().getViewPosition();
		final edu.cmu.cs.dennisc.zoot.ZDragComponent source = dragAndDropContext.getDragSource();
		final java.awt.event.MouseEvent eSource = dragAndDropContext.getLatestMouseEvent();
		final StatementListPropertyPane statementListPropertyPane = CodeEditor.this.currentUnder;
		if( statementListPropertyPane != null ) {
			final int index = statementListPropertyPane.calculateIndex( javax.swing.SwingUtilities.convertPoint( source, eSource.getPoint(), statementListPropertyPane ) );
			if( source instanceof org.alice.ide.templates.StatementTemplate ) {
				final org.alice.ide.templates.StatementTemplate statementTemplate = (org.alice.ide.templates.StatementTemplate)source;
				if( this.currentUnder != null ) {
					final edu.cmu.cs.dennisc.zoot.event.DragAndDropEvent dragAndDropEvent = new edu.cmu.cs.dennisc.zoot.event.DragAndDropEvent( source, CodeEditor.this, eSource );
					class DropOperation extends org.alice.ide.operations.AbstractActionOperation {


						public DropOperation() {
							super( edu.cmu.cs.dennisc.alice.Project.GROUP_UUID );
						}
						public void perform( final edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
							class DropEdit extends edu.cmu.cs.dennisc.zoot.AbstractEdit {
								private edu.cmu.cs.dennisc.alice.ast.Statement statement;
								@Override
								public void doOrRedo( boolean isDo ) {
									statementListPropertyPane.getProperty().add( index, statement );
									CodeEditor.this.refresh();
									CodeEditor.this.resetScrollPane( viewPosition );
								}

								@Override
								public void undo() {
									if( statementListPropertyPane.getProperty().get( index ) == statement ) {
										statementListPropertyPane.getProperty().remove( index );
										CodeEditor.this.refresh();
										CodeEditor.this.resetScrollPane( viewPosition );
									} else {
										throw new javax.swing.undo.CannotUndoException();
									}
								}
								
								@Override
								protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
									//super.updatePresentation( rv, locale );
									rv.append( "drop: " );
									edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr( rv, statement, locale );
									return rv;
								}
							}
							actionContext.pend( new edu.cmu.cs.dennisc.zoot.Resolver< DropEdit, edu.cmu.cs.dennisc.alice.ast.Statement >() {
								public DropEdit createEdit() {
									return new DropEdit();
								}
								public DropEdit initialize(DropEdit rv, edu.cmu.cs.dennisc.zoot.Context<? extends edu.cmu.cs.dennisc.zoot.Operation> context, edu.cmu.cs.dennisc.task.TaskObserver<edu.cmu.cs.dennisc.alice.ast.Statement> taskObserver) {
									edu.cmu.cs.dennisc.property.PropertyOwner propertyOwner = statementListPropertyPane.getProperty().getOwner();
									if( propertyOwner instanceof edu.cmu.cs.dennisc.alice.ast.BlockStatement ) {
										edu.cmu.cs.dennisc.alice.ast.BlockStatement block = (edu.cmu.cs.dennisc.alice.ast.BlockStatement)propertyOwner;
										statementTemplate.createStatement( dragAndDropEvent, block, taskObserver );
									}
									return rv;
								}
								
								public DropEdit handleCompletion(DropEdit rv, edu.cmu.cs.dennisc.alice.ast.Statement statement) {
									rv.statement = statement;
									source.hideDropProxyIfNecessary();
									return rv;
								}
								public void handleCancelation() {
									source.hideDropProxyIfNecessary();
								}
							} );
						}
					}
					dragAndDropContext.perform( new DropOperation(), dragAndDropEvent, edu.cmu.cs.dennisc.zoot.ZManager.CANCEL_IS_WORTHWHILE );
				} else {
					source.hideDropProxyIfNecessary();
				}
			} else if( source != null && source.getSubject() instanceof org.alice.ide.common.AbstractStatementPane ) {
				source.hideDropProxyIfNecessary();
				if( this.currentUnder != null ) {
					final edu.cmu.cs.dennisc.zoot.event.DragAndDropEvent dragAndDropEvent = new edu.cmu.cs.dennisc.zoot.event.DragAndDropEvent( source, CodeEditor.this, eSource );
					org.alice.ide.common.AbstractStatementPane abstractStatementPane = (org.alice.ide.common.AbstractStatementPane)source.getSubject();
					final edu.cmu.cs.dennisc.alice.ast.Statement statement = abstractStatementPane.getStatement();
					final edu.cmu.cs.dennisc.alice.ast.StatementListProperty prevOwner = abstractStatementPane.getOwner();
					final edu.cmu.cs.dennisc.alice.ast.StatementListProperty nextOwner = this.currentUnder.getProperty();
					final int prevIndex = prevOwner.indexOf( statement );
					final int nextIndex = this.currentUnder.calculateIndex( javax.swing.SwingUtilities.convertPoint( source, eSource.getPoint(), this.currentUnder ) );

					
					abstract class CodeEdit extends edu.cmu.cs.dennisc.zoot.AbstractEdit {
						protected abstract void redoInternal();
						protected abstract void undoInternal();

						protected void refreshAndResetScrollPane() {
							CodeEditor.this.refresh();
							CodeEditor.this.resetScrollPane( viewPosition );
						}
						@Override
						public final void doOrRedo( boolean isFirstTime ) {
							this.redoInternal();
							this.refreshAndResetScrollPane();
						}
						@Override
						public final void undo() {
							this.undoInternal();
							this.refreshAndResetScrollPane();
						}
					}
					
					edu.cmu.cs.dennisc.zoot.ActionOperation operation;
					if( edu.cmu.cs.dennisc.swing.SwingUtilities.isQuoteControlUnquoteDown( eSource ) ) {
						class CopyOperation extends org.alice.ide.operations.AbstractActionOperation {
							public CopyOperation() {
								super( edu.cmu.cs.dennisc.alice.Project.GROUP_UUID );
							}
							public void perform(edu.cmu.cs.dennisc.zoot.ActionContext actionContext) {
								final edu.cmu.cs.dennisc.alice.ast.Statement copy = (edu.cmu.cs.dennisc.alice.ast.Statement)getIDE().createCopy( statement );
								class CopyEdit extends CodeEdit {
									@Override
									protected void redoInternal() {
										nextOwner.add( nextIndex, copy );
									}
									@Override
									protected void undoInternal() {
										if( nextOwner.get( nextIndex ) == copy ) {
											nextOwner.remove( nextIndex );
										} else {
											throw new javax.swing.undo.CannotUndoException();
										}
									}
									@Override
									protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
										rv.append( "copy code" );
										return rv;
									}
									
								}
								actionContext.commitAndInvokeDo( new CopyEdit() );
							}
						}
						operation = new CopyOperation();
					} else {
						if( prevOwner == nextOwner ) {
							if( prevIndex == nextIndex || prevIndex == nextIndex - 1 ) {
								operation = null;
							} else {
								class ReorderOperation extends org.alice.ide.operations.AbstractActionOperation {
									public ReorderOperation() {
										super( edu.cmu.cs.dennisc.alice.Project.GROUP_UUID );
									}
									public void perform(edu.cmu.cs.dennisc.zoot.ActionContext actionContext) {
										class ReorderEdit extends CodeEdit {
											@Override
											protected void redoInternal() {
												prevOwner.remove( prevIndex );
												int index;
												if( prevIndex < nextIndex ) {
													index = nextIndex - 1;
												} else {
													index = nextIndex;
												}
												nextOwner.add( index, statement );
											}
											@Override
											protected void undoInternal() {
												nextOwner.remove( nextIndex );
												int index;
												if( nextIndex < prevIndex ) {
													index = prevIndex - 1;
												} else {
													index = prevIndex;
												}
												prevOwner.add( index, statement );
											}
											@Override
											protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
												rv.append( "move code" );
												return rv;
											}
										}
										actionContext.commitAndInvokeDo( new ReorderEdit() );
									}
								}
								operation = new ReorderOperation();
							}
						} else {
							class ReparentOperation extends org.alice.ide.operations.AbstractActionOperation {
								public ReparentOperation() {
									super( edu.cmu.cs.dennisc.alice.Project.GROUP_UUID );
								}
								public void perform(edu.cmu.cs.dennisc.zoot.ActionContext actionContext) {
									class ReparentEdit extends CodeEdit {
										@Override
										protected void redoInternal() {
											prevOwner.remove( prevIndex );
											nextOwner.add( nextIndex, statement );
										}
										@Override
										protected void undoInternal() {
											prevOwner.add( prevIndex, statement );
											nextOwner.remove( nextIndex );
										}
										@Override
										protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
											rv.append( "move code" );
											return rv;
										}
									}
									actionContext.commitAndInvokeDo( new ReparentEdit() );
								}
							}
							operation = new ReparentOperation();
						}
					}
					if( operation != null ) {
						dragAndDropContext.perform( operation, dragAndDropEvent, edu.cmu.cs.dennisc.zoot.ZManager.CANCEL_IS_WORTHWHILE );
					}
				}
			}
		}
	}
	private void resetScrollPane( final java.awt.Point viewPosition ) {
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				CodeEditor.this.scrollPane.getViewport().setViewPosition( viewPosition );
				CodeEditor.this.repaint();
			}
		} );
	}
	public void dragExited( edu.cmu.cs.dennisc.zoot.DragAndDropContext dragAndDropContext, boolean isDropRecipient ) {
		this.statementListPropertyPaneInfos = null;
		this.currentUnder = null;
		this.repaint();
		if( isDropRecipient ) {
			//pass
		} else {
			final edu.cmu.cs.dennisc.zoot.ZDragComponent source = dragAndDropContext.getDragSource();
			if( source != null ) {
				source.hideDropProxyIfNecessary();
			}
		}
	}
	public void dragStopped( edu.cmu.cs.dennisc.zoot.DragAndDropContext dragAndDropContext ) {
	}
	//	@Override
	//	public void paint( java.awt.Graphics g ) {
	//		super.paint( g );
	//		if( CodeEditor.this.statementListPropertyPaneInfos != null ) {
	//			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
	//			for( StatementListPropertyPaneInfo statementListPropertyPaneInfo : this.statementListPropertyPaneInfos ) {
	//				if( statementListPropertyPaneInfo != null ) {
	//					java.awt.Color color;
	//					if( this.currentUnder == statementListPropertyPaneInfo.getStatementListPropertyPane() ) {
	//						color = new java.awt.Color( 0, 0, 0, 127 );
	//					} else {
	//						color = null;
	//						//color = new java.awt.Color( 255, 0, 0, 31 );
	//					}
	//					if( color != null ) {
	//						java.awt.Rectangle bounds = statementListPropertyPaneInfo.getBounds();
	//						bounds = javax.swing.SwingUtilities.convertRectangle( this.getAsSeenBy(), bounds, this );
	//						g2.setColor( color );
	//						g2.fill( bounds );
	//						g2.setColor( new java.awt.Color( 255, 255, 0, 255 ) );
	//						g2.draw( bounds );
	//					}
	//				}
	//			}
	//		}
	//	}
}
