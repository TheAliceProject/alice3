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
public class CodeEditor extends edu.cmu.cs.dennisc.croquet.ViewController< javax.swing.JPanel, edu.cmu.cs.dennisc.croquet.Model > implements edu.cmu.cs.dennisc.croquet.DropReceptor {
	private edu.cmu.cs.dennisc.alice.ast.AbstractCode code;
	private StatementListPropertyPaneInfo[] statementListPropertyPaneInfos;
	private edu.cmu.cs.dennisc.croquet.ScrollPane scrollPane;

	private edu.cmu.cs.dennisc.croquet.Application.LocaleObserver localeObserver = new edu.cmu.cs.dennisc.croquet.Application.LocaleObserver() {
		public void localeChanging( java.util.Locale previousLocale, java.util.Locale nextLocale ) {
		}
		public void localeChanged( java.util.Locale previousLocale, java.util.Locale nextLocale ) {
			CodeEditor.this.refresh();
			CodeEditor.this.repaint();
		}
	};
	
	public CodeEditor( edu.cmu.cs.dennisc.alice.ast.AbstractCode code ) {
		super( null );
		this.code = code;
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		java.awt.Color color = getIDE().getCodeDeclaredInAliceColor( this.code );
		color = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( color, 1.0f, 1.1f, 1.1f );
		this.setBackgroundColor( color );
		//this.setDoubleBuffered( true );
		this.refresh();
	}

	@Override
	protected javax.swing.JPanel createAwtComponent() {
		javax.swing.JPanel rv = new javax.swing.JPanel();
		rv.setLayout( new javax.swing.BoxLayout( rv, javax.swing.BoxLayout.PAGE_AXIS ) );
		return rv;
	}
	private edu.cmu.cs.dennisc.croquet.BooleanState.ValueObserver typeFeedbackObserver = new edu.cmu.cs.dennisc.croquet.BooleanState.ValueObserver() {
		public void changing(boolean nextValue) {
		}
		public void changed(boolean nextValue) {
			CodeEditor.this.refresh();
		}
	};
	@Override
	protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		super.handleAddedTo(parent);
		edu.cmu.cs.dennisc.croquet.Application.getSingleton().addLocaleObserver( this.localeObserver );
		org.alice.ide.IDE.getSingleton().getExpressionTypeFeedbackDesiredState().addAndInvokeValueObserver( this.typeFeedbackObserver );
	}
	@Override
	protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		org.alice.ide.IDE.getSingleton().getExpressionTypeFeedbackDesiredState().removeValueObserver( this.typeFeedbackObserver );
		edu.cmu.cs.dennisc.croquet.Application.getSingleton().removeLocaleObserver( this.localeObserver );
		super.handleRemovedFrom( parent );
	}

	//todo: croquet switch
//	@Override
//	public String getName() {
//		if( this.code instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
//			edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)this.code;
//			return method.getName();
//		} else if( this.code instanceof edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice ) {
//			//edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice constructor = (edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice)this.code;
//			return "constructor";
//		} else {
//			return super.getName();
//		}
//	}

	protected edu.cmu.cs.dennisc.croquet.Component< ? > createParametersPane( edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code ) {
		return new ParametersPane( this.getIDE().getCodeFactory(), code );
	}

	public edu.cmu.cs.dennisc.alice.ast.AbstractCode getCode() {
		return this.code;
	}
	public edu.cmu.cs.dennisc.croquet.ViewController<?,?> getViewController() {
		return this;
	}
	public void refresh() {
		this.forgetAndRemoveAllComponents();
		if( this.code instanceof edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice ) {
			final edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice codeDeclaredInAlice = (edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice)this.code;
			edu.cmu.cs.dennisc.croquet.Component< ? > parametersPane = createParametersPane( codeDeclaredInAlice );
			AbstractCodeHeaderPane header;
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
				//@Override
				//protected boolean isMaximumSizeClampedToPreferredSize() {
				//	return false;
				//}
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

			this.scrollPane = new edu.cmu.cs.dennisc.croquet.ScrollPane( bodyPane );
			this.scrollPane.getAwtComponent().getVerticalScrollBar().setUnitIncrement( 12 );
			this.scrollPane.setBorder( null );
			this.scrollPane.setBackgroundColor( null );
			this.scrollPane.getAwtComponent().getViewport().setOpaque( false );
			//this.scrollPane.setBackground( java.awt.Color.RED );
			this.scrollPane.setAlignmentX( javax.swing.JComponent.LEFT_ALIGNMENT );
			this.internalAddComponent( header );
			if( this.getIDE().isEmphasizingClasses() || this.getIDE().isInstanceLineDesired() == false ) {
				//pass
			} else {
				header.addComponent( new InstanceLine( this.code ) );
			}
			//			this.add( javax.swing.Box.createVerticalStrut( 8 ) );
			this.internalAddComponent( scrollPane );
		}
		this.revalidateAndRepaint();
	}
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getSingleton();
	}
	public java.util.List< ? extends ExpressionPropertyDropDownPane > createListOfPotentialDropReceptors( final edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		return edu.cmu.cs.dennisc.croquet.HierarchyUtilities.findAllMatches( this, ExpressionPropertyDropDownPane.class, new edu.cmu.cs.dennisc.pattern.Criterion< ExpressionPropertyDropDownPane >() {
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
	public final boolean isPotentiallyAcceptingOf( edu.cmu.cs.dennisc.croquet.DragComponent source ) {
		if( source instanceof org.alice.ide.templates.StatementTemplate ) {
			return getIDE().getFocusedCode() == this.code;
		} else {
			return false;
		}
	}
	
	private StatementListPropertyPane currentUnder;
	private void setCurrentUnder( StatementListPropertyPane nextUnder ) {
		if( this.currentUnder != nextUnder ) {
			if( this.currentUnder != null ) {
				this.currentUnder.setIsCurrentUnder( false );
			}
			this.currentUnder = nextUnder;
			if( this.currentUnder != null ) {
				this.currentUnder.setIsCurrentUnder( true );
			}
		}
	}
	public final void dragStarted( edu.cmu.cs.dennisc.croquet.DragAndDropContext context ) {
	}

	public final void dragEntered( edu.cmu.cs.dennisc.croquet.DragAndDropContext context ) {
		edu.cmu.cs.dennisc.croquet.DragComponent source = context.getDragSource();
		this.statementListPropertyPaneInfos = createStatementListPropertyPaneInfos( source );
		this.repaint();
	}
	private edu.cmu.cs.dennisc.croquet.Component< ? > getAsSeenBy() {
		return this.scrollPane.getViewportView();
	}
	private StatementListPropertyPaneInfo[] createStatementListPropertyPaneInfos( edu.cmu.cs.dennisc.croquet.DragComponent source ) {
		java.util.List< StatementListPropertyPane > statementListPropertyPanes = edu.cmu.cs.dennisc.croquet.HierarchyUtilities.findAllMatches( this, StatementListPropertyPane.class );
		StatementListPropertyPaneInfo[] rv = new StatementListPropertyPaneInfo[ statementListPropertyPanes.size() ];
		int i = 0;
		for( StatementListPropertyPane statementListPropertyPane : statementListPropertyPanes ) {
			if( source != null && source.isAncestorOf( statementListPropertyPane ) ) {
				continue;
			}
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( statementListPropertyPane );
			java.awt.Rectangle bounds = statementListPropertyPane.convertRectangle( statementListPropertyPane.getDropBounds(), this.getAsSeenBy() );
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
	public final void dragUpdated( edu.cmu.cs.dennisc.croquet.DragAndDropContext context ) {
		edu.cmu.cs.dennisc.croquet.DragComponent source = context.getDragSource();
		if( source != null ) {
			java.awt.event.MouseEvent eSource = context.getLatestMouseEvent();
			java.awt.event.MouseEvent eAsSeenBy = source.convertMouseEvent( eSource, this.getAsSeenBy() );
			StatementListPropertyPane nextUnder = getStatementListPropertyPaneUnder( eAsSeenBy, this.statementListPropertyPaneInfos );
			this.setCurrentUnder( nextUnder );
			if( this.currentUnder != null ) {
				boolean isDropProxyAlreadyUpdated = false;
				if( edu.cmu.cs.dennisc.javax.swing.SwingUtilities.isQuoteControlUnquoteDown( eSource ) ) {
					//pass
				} else {
					edu.cmu.cs.dennisc.croquet.Component< ? > subject = source.getSubject();
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
					java.awt.event.MouseEvent eUnder = this.getAsSeenBy().convertMouseEvent( eAsSeenBy, this.currentUnder );
					Integer height = 0;
					java.awt.Insets insets = this.currentUnder.getBorder().getBorderInsets( this.currentUnder.getAwtComponent() );
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
								p.y = this.currentUnder.getAwtComponent().getComponent( index ).getY();
							} else {
								java.awt.Component lastComponent = this.currentUnder.getAwtComponent().getComponent( n - 1 );
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
	public final edu.cmu.cs.dennisc.croquet.Operation<?> dragDropped( edu.cmu.cs.dennisc.croquet.DragAndDropContext context ) {
		edu.cmu.cs.dennisc.croquet.Operation<?> rv = null;
		final java.awt.Point viewPosition = this.scrollPane.getAwtComponent().getViewport().getViewPosition();
		final edu.cmu.cs.dennisc.croquet.DragComponent source = context.getDragSource();
		final java.awt.event.MouseEvent eSource = context.getLatestMouseEvent();
		final StatementListPropertyPane statementListPropertyPane = CodeEditor.this.currentUnder;
		if( statementListPropertyPane != null ) {
			final int index = statementListPropertyPane.calculateIndex( source.convertPoint( eSource.getPoint(), statementListPropertyPane ) );
			if( source instanceof org.alice.ide.templates.StatementTemplate ) {
				final org.alice.ide.templates.StatementTemplate statementTemplate = (org.alice.ide.templates.StatementTemplate)source;
				if( this.currentUnder != null ) {
					class DropOperation extends edu.cmu.cs.dennisc.croquet.ActionOperation {
						public DropOperation() {
							super( edu.cmu.cs.dennisc.alice.Project.GROUP, java.util.UUID.fromString( "ad0e5d93-8bc2-4ad8-8dd5-37768eaa5319" ) );
						}
						@Override
						protected void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
							final java.awt.event.MouseEvent mouseEvent = context.getMouseEvent();
							class DropEdit extends org.alice.ide.ToDoEdit {
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
							context.pend( new edu.cmu.cs.dennisc.croquet.Resolver< DropEdit, edu.cmu.cs.dennisc.alice.ast.Statement >() {
								public DropEdit createEdit() {
									return new DropEdit();
								}
								public DropEdit initialize(DropEdit rv, edu.cmu.cs.dennisc.croquet.ModelContext context, java.util.UUID id, edu.cmu.cs.dennisc.task.TaskObserver<edu.cmu.cs.dennisc.alice.ast.Statement> taskObserver) {
									edu.cmu.cs.dennisc.property.PropertyOwner propertyOwner = statementListPropertyPane.getProperty().getOwner();
									if( propertyOwner instanceof edu.cmu.cs.dennisc.alice.ast.BlockStatement ) {
										edu.cmu.cs.dennisc.alice.ast.BlockStatement block = (edu.cmu.cs.dennisc.alice.ast.BlockStatement)propertyOwner;
										statementTemplate.createStatement( mouseEvent, block, taskObserver );
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
					rv = new DropOperation();
				} else {
					source.hideDropProxyIfNecessary();
				}
			} else if( source != null && source.getSubject() instanceof org.alice.ide.common.AbstractStatementPane ) {
				source.hideDropProxyIfNecessary();
				if( this.currentUnder != null ) {
					org.alice.ide.common.AbstractStatementPane abstractStatementPane = (org.alice.ide.common.AbstractStatementPane)source.getSubject();
					final edu.cmu.cs.dennisc.alice.ast.Statement statement = abstractStatementPane.getStatement();
					final edu.cmu.cs.dennisc.alice.ast.StatementListProperty prevOwner = abstractStatementPane.getOwner();
					final edu.cmu.cs.dennisc.alice.ast.StatementListProperty nextOwner = this.currentUnder.getProperty();
					final int prevIndex = prevOwner.indexOf( statement );
					final int nextIndex = this.currentUnder.calculateIndex( source.convertPoint( eSource.getPoint(), this.currentUnder ) );

					
					abstract class CodeEdit extends org.alice.ide.ToDoEdit {
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
					
					if( edu.cmu.cs.dennisc.javax.swing.SwingUtilities.isQuoteControlUnquoteDown( eSource ) ) {
						class CopyOperation extends edu.cmu.cs.dennisc.croquet.ActionOperation {
							public CopyOperation() {
								super( edu.cmu.cs.dennisc.alice.Project.GROUP, java.util.UUID.fromString( "ef6143be-5de3-4a55-aed3-f61d8ebbbef2" ) );
							}
							@Override
							protected void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
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
								context.commitAndInvokeDo( new CopyEdit() );
							}
						}
						rv = new CopyOperation();
					} else {
						if( prevOwner == nextOwner ) {
							if( prevIndex == nextIndex || prevIndex == nextIndex - 1 ) {
								rv = null;
							} else {
								class ReorderOperation extends edu.cmu.cs.dennisc.croquet.ActionOperation {
									public ReorderOperation() {
										super( edu.cmu.cs.dennisc.alice.Project.GROUP, java.util.UUID.fromString( "e2cffe11-be24-4b5c-9ca4-ac0d71ecd16c" ) );
									}
									@Override
									protected void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
										class ReorderEdit extends CodeEdit {
											private edu.cmu.cs.dennisc.alice.ast.StatementListProperty owner;
											private int aIndex;
											private int bIndex;
											
											public ReorderEdit() {
												assert prevOwner == nextOwner;
												this.owner = prevOwner;
												this.aIndex = prevIndex;
												if( prevIndex < nextIndex ) {
													this.bIndex = nextIndex - 1;
												} else {
													this.bIndex = nextIndex;
												}
											}
											@Override
											protected void redoInternal() {
												this.owner.remove( this.aIndex );
												this.owner.add( this.bIndex, statement );
											}
											@Override
											protected void undoInternal() {
												this.owner.remove( this.bIndex );
												this.owner.add( this.aIndex, statement );
											}
											@Override
											protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
												rv.append( "move code" );
												return rv;
											}
										}
										context.commitAndInvokeDo( new ReorderEdit() );
									}
								}
								rv = new ReorderOperation();
							}
						} else {
							class ReparentOperation extends edu.cmu.cs.dennisc.croquet.ActionOperation {
								public ReparentOperation() {
									super( edu.cmu.cs.dennisc.alice.Project.GROUP, java.util.UUID.fromString( "6049a378-2972-4672-a211-1f3fcda45025" ) );
								}
								@Override
								protected void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
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
									context.commitAndInvokeDo( new ReparentEdit() );
								}
							}
							rv = new ReparentOperation();
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
	public final void dragExited( edu.cmu.cs.dennisc.croquet.DragAndDropContext context, boolean isDropRecipient ) {
		this.statementListPropertyPaneInfos = null;
		this.setCurrentUnder( null );
		this.repaint();
		if( isDropRecipient ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.croquet.DragComponent source = context.getDragSource();
			if( source != null ) {
				source.hideDropProxyIfNecessary();
			}
		}
	}
	public final void dragStopped( edu.cmu.cs.dennisc.croquet.DragAndDropContext context ) {
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

//	public enum Location {
//		AT_FRONT( false ),
//		AT_END( false ),
//		BEFORE( true ),
//		AFTER( true );
//		private boolean isLookingForParent;
//		private Location( boolean isLookingForParent ) {
//			this.isLookingForParent = isLookingForParent;
//		}
//		public boolean isTheOne( StatementListPropertyPane statementListPropertyPane, edu.cmu.cs.dennisc.alice.ast.BlockStatement statement ) {
//			edu.cmu.cs.dennisc.alice.ast.StatementListProperty property = statementListPropertyPane.getProperty();
//			if( this.isLookingForParent ) {
//				if( property.contains( statement ) ) {
//					return true;
//				}
//			} else {
//				if( property.getOwner() == statement ) {
//					return true;
//				}
//			}
//			return false;
//		}
//	}
	
	public edu.cmu.cs.dennisc.croquet.TrackableShape getTrackableShapeAtIndexOf( edu.cmu.cs.dennisc.alice.ast.StatementListProperty statementListProperty, int index ) {
		if( statementListProperty != null ) {
			java.util.List< StatementListPropertyPane > statementListPropertyPanes = edu.cmu.cs.dennisc.croquet.HierarchyUtilities.findAllMatches( this, StatementListPropertyPane.class );
			for( final StatementListPropertyPane statementListPropertyPane : statementListPropertyPanes ) {
				if( statementListPropertyPane.getProperty() == statementListProperty ) {
					return new edu.cmu.cs.dennisc.croquet.TrackableShape() {;
						public java.awt.Shape getShape( edu.cmu.cs.dennisc.croquet.Component< ? > asSeenBy, java.awt.Insets insets ) {
							java.awt.Rectangle bounds = statementListPropertyPane.getBounds( asSeenBy );
							bounds.height = Math.min( bounds.height, 100 );
							return bounds;
						}
						public java.awt.Shape getVisibleShape( edu.cmu.cs.dennisc.croquet.Component< ? > asSeenBy, java.awt.Insets insets ) {
							java.awt.Rectangle bounds = statementListPropertyPane.getVisibleRectangle( asSeenBy );
							bounds.height = Math.min( bounds.height, 100 );
							return bounds;
						}
					};
				}
			}
		}
		return null;
	}
	public edu.cmu.cs.dennisc.croquet.DragComponent getDragComponent( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
		if( statement != null ) {
			java.util.List< org.alice.ide.common.AbstractStatementPane > statementPanes = edu.cmu.cs.dennisc.croquet.HierarchyUtilities.findAllMatches( this, org.alice.ide.common.AbstractStatementPane.class );
			for( org.alice.ide.common.AbstractStatementPane statementPane : statementPanes ) {
				if( statementPane.getStatement() == statement ) {
					return statementPane;
				}
			}
		}
		return null;
	}
}
