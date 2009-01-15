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
package edu.cmu.cs.dennisc.alice.ide.editors.code;

import edu.cmu.cs.dennisc.alice.ide.editors.common.*;
import edu.cmu.cs.dennisc.alice.ide.event.FieldSelectionEvent;
import edu.cmu.cs.dennisc.alice.ide.event.FocusedCodeChangeEvent;
import edu.cmu.cs.dennisc.alice.ide.event.LocaleEvent;
import edu.cmu.cs.dennisc.alice.ide.event.ProjectOpenEvent;
import edu.cmu.cs.dennisc.alice.ide.event.TransientSelectionEvent;

/**
 * @author Dennis Cosgrove
 */
class AbstractCodeHeaderPane extends edu.cmu.cs.dennisc.zoot.ZLineAxisPane {
	public AbstractCodeHeaderPane( edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice codeDeclarationInAlice ) {
		//this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 12, 4, 0, 4 ) );
	}
}

/**
 * @author Dennis Cosgrove
 */
class MethodHeaderPane extends AbstractCodeHeaderPane {
	public MethodHeaderPane( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice methodDeclaredInAlice, javax.swing.JComponent parametersPane ) {
		super( methodDeclaredInAlice );
		if( "java".equals( edu.cmu.cs.dennisc.alice.ide.IDE.getSingleton().getLocale().getVariant() ) ) {
			this.add( new edu.cmu.cs.dennisc.alice.ide.editors.common.TypePane( methodDeclaredInAlice.getReturnType() ) );
			edu.cmu.cs.dennisc.alice.ide.editors.common.Label nameLabel = new edu.cmu.cs.dennisc.alice.ide.editors.common.NodeNameLabel( methodDeclaredInAlice );
			nameLabel.scaleFont( 2.0f );
			this.add( nameLabel );
			this.add( parametersPane );
			//this.add( new edu.cmu.cs.dennisc.zoot.ZLabel( " {" ) );
		} else {
			StringBuffer sb = new StringBuffer();
			if( methodDeclaredInAlice.isOverride() ) {
				sb.append( "override " );
			} else {
				//pass
			}
			if( methodDeclaredInAlice.isProcedure() ) {
				sb.append( "procedure " );
			} else {
				sb.append( "function " );
			}
			this.add( new edu.cmu.cs.dennisc.alice.ide.editors.common.Label( sb.toString() ) );		
			edu.cmu.cs.dennisc.alice.ide.editors.common.Label nameLabel = new edu.cmu.cs.dennisc.alice.ide.editors.common.NodeNameLabel( methodDeclaredInAlice );
			nameLabel.scaleFont( 2.0f );
			this.add( nameLabel );
			this.add( parametersPane );
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
class ConstructorHeaderPane extends AbstractCodeHeaderPane {
	public ConstructorHeaderPane( edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice constructorDeclaredInAlice, javax.swing.JComponent parametersPane ) {
		super( constructorDeclaredInAlice );
		if( "java".equals( edu.cmu.cs.dennisc.alice.ide.IDE.getSingleton().getLocale().getVariant() ) ) {
			this.add( new edu.cmu.cs.dennisc.alice.ide.editors.common.TypePane( constructorDeclaredInAlice.getDeclaringType() ) );
			this.add( new edu.cmu.cs.dennisc.zoot.ZLabel( "()" ) );
		} else {
			this.add( new edu.cmu.cs.dennisc.alice.ide.editors.common.Label( "declare " ) );
			edu.cmu.cs.dennisc.alice.ide.editors.common.Label label = new edu.cmu.cs.dennisc.alice.ide.editors.common.Label( "constructor" );
			label.scaleFont( 1.5f );
			this.add( label );
			this.add( new edu.cmu.cs.dennisc.alice.ide.editors.common.Label( " on class " ) );
			this.add( new edu.cmu.cs.dennisc.alice.ide.editors.common.TypePane( constructorDeclaredInAlice.getDeclaringType() ) );
			this.add( parametersPane );
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
class InstanceLine extends edu.cmu.cs.dennisc.zoot.ZLineAxisPane {
	public InstanceLine( edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code ) {
		Label a = new edu.cmu.cs.dennisc.alice.ide.editors.common.Label( "current instance of " );
		a.italicizeFont();
		this.add( a );
		this.add( new edu.cmu.cs.dennisc.alice.ide.editors.common.TypePane( code.getDeclaringType() ) );
		Label b = new edu.cmu.cs.dennisc.alice.ide.editors.common.Label( " is referred to as: " );
		b.italicizeFont();
		this.add( b );
		this.add( new ThisPane( code.getDeclaringType() ) );
	}
}

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
public abstract class CodeEditor extends edu.cmu.cs.dennisc.zoot.ZPageAxisPane implements edu.cmu.cs.dennisc.alice.ide.event.IDEListener, edu.cmu.cs.dennisc.alice.ide.editors.common.DropReceptor {
	public void fieldSelectionChanged( FieldSelectionEvent e ) {
	}
	public void fieldSelectionChanging( FieldSelectionEvent e ) {
	}
	public void focusedCodeChanged( FocusedCodeChangeEvent e ) {
	}
	public void focusedCodeChanging( FocusedCodeChangeEvent e ) {
	}
	public void localeChanged( LocaleEvent e ) {
		this.refresh();
	}
	public void localeChanging( LocaleEvent e ) {
	}
	public void projectOpened( ProjectOpenEvent e ) {
	}
	public void projectOpening( ProjectOpenEvent e ) {
	}
	public void transientSelectionChanged( TransientSelectionEvent e ) {
	}
	public void transientSelectionChanging( TransientSelectionEvent e ) {
	}
	private edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code;
	private StatementListPropertyPaneInfo[] statementListPropertyPaneInfos;
	private StatementListPropertyPane currentUnder;
	

	public CodeEditor( edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code ) {
		this.getIDE().addIDEListener( this );
		this.code = code;
		this.setOpaque( true );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		this.setBackground( edu.cmu.cs.dennisc.alice.ide.IDE.getSingleton().getCodeDeclaredInAliceColor( this.code ) );
		this.refresh();
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
	
	protected abstract javax.swing.JComponent createParametersPane( edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code );
	
	public edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice getCode() {
		return this.code;
	}
	public java.awt.Component getAWTComponent() {
		return this;
	}
	private void refresh() {
		this.removeAll();
		javax.swing.JComponent parametersPane = createParametersPane( this.code );
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
		this.add( header );
		this.add( new InstanceLine( this.code ) );
		this.add( new TemplatePane( code.getBodyProperty().getValue() ) );

		//this.add( javax.swing.Box.createVerticalGlue() );
		this.revalidate();
		this.repaint();
	}
	protected edu.cmu.cs.dennisc.alice.ide.IDE getIDE() {
		return edu.cmu.cs.dennisc.alice.ide.IDE.getSingleton();
	}
	public java.util.List< ? extends ExpressionPropertyDropDownPane > findAllPotentialAcceptors( final edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		return edu.cmu.cs.dennisc.awt.ComponentUtilities.findAllMatches( this, ExpressionPropertyDropDownPane.class, new edu.cmu.cs.dennisc.pattern.Criterion< ExpressionPropertyDropDownPane >() {
			public boolean accept( ExpressionPropertyDropDownPane expressionPropertyDropDownPane ) {
				return expressionPropertyDropDownPane.getExpressionProperty().getExpressionType().isAssignableFrom( type );
			}
		} );
	}
	//	@Override
	//	public void addNotify() {
	//		super.addNotify();
	//		getIDE().addDropReceptor( this );
	//	}
	//	@Override
	//	public void removeNotify() {
	//		super.removeNotify();
	//		getIDE().removeDropReceptor( this );
	//	}
	public boolean isPotentiallyAcceptingOf( PotentiallyDraggablePane source ) {
		if( source instanceof edu.cmu.cs.dennisc.alice.ide.editors.type.StatementTemplatePane ) {
			return getIDE().getFocusedCode() == this.code;
		} else {
			return false;
		}
	}
	public void dragStarted( PotentiallyDraggablePane source, java.awt.event.MouseEvent e ) {
	}

	private java.awt.Rectangle getBoundsFor( StatementListPropertyPane statementListPropertyPane ) {
		java.awt.Rectangle rv = statementListPropertyPane.getBounds();
		final int DELTA = this.getFont().getSize() + 4;
		rv.y -= DELTA;
		rv.height += DELTA;
		return rv;
	}

	public void dragEntered( PotentiallyDraggablePane source, java.awt.event.MouseEvent e ) {
		StatementListPropertyPane topStatementListPropertyPane = edu.cmu.cs.dennisc.awt.ComponentUtilities.findFirstMatch( this, StatementListPropertyPane.class );
		java.util.List< StatementListPropertyPane > statementListPropertyPanes = edu.cmu.cs.dennisc.awt.ComponentUtilities.findAllMatches( this, StatementListPropertyPane.class );
		this.statementListPropertyPaneInfos = new StatementListPropertyPaneInfo[ statementListPropertyPanes.size() ];
		int i = 0;
		for( StatementListPropertyPane statementListPropertyPane : statementListPropertyPanes ) {
			if( source.isAncestorOf( statementListPropertyPane ) ) {
				continue;
			}
			//			if( source instanceof AbstractStatementPane ) {
			//			}
			java.awt.Rectangle bounds = javax.swing.SwingUtilities.convertRectangle( statementListPropertyPane, this.getBoundsFor( statementListPropertyPane ), this );
			//			bounds.x = Math.max( bounds.x, 0 );
			bounds.x = 0;
			bounds.width = this.getWidth() - bounds.x;
			if( statementListPropertyPane == topStatementListPropertyPane ) {
				bounds.y = Math.max( bounds.y, 0 );
				bounds.height = this.getHeight() - bounds.y;
			}
			this.statementListPropertyPaneInfos[ i ] = new StatementListPropertyPaneInfo( statementListPropertyPane, bounds );
			i++;
		}
		this.repaint();
	}
	private StatementListPropertyPane getStatementListPropertyPaneUnder( java.awt.event.MouseEvent e ) {
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
	public void dragUpdated( PotentiallyDraggablePane source, java.awt.event.MouseEvent eSource ) {

		java.awt.event.MouseEvent eThis = edu.cmu.cs.dennisc.swing.SwingUtilities.convertMouseEvent( source, eSource, this );
		StatementListPropertyPane nextUnder = getStatementListPropertyPaneUnder( eThis );
		this.currentUnder = nextUnder;

		if( this.currentUnder != null ) {
			boolean isDropProxyAlreadyUpdated = false;
			if( edu.cmu.cs.dennisc.swing.SwingUtilities.isQuoteControlUnquoteDown( eSource ) ) {
				//pass
			} else {
				if( source instanceof AbstractStatementPane ) {
					AbstractStatementPane abstractStatementPane = (AbstractStatementPane)source;
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
			if( isDropProxyAlreadyUpdated ) {
				//pass
			} else {
				java.awt.event.MouseEvent eUnder = edu.cmu.cs.dennisc.swing.SwingUtilities.convertMouseEvent( this, eThis, this.currentUnder );
				Integer height = 0;
				java.awt.Point p = new java.awt.Point( 0, 0 );
				if( this.currentUnder.isFigurativelyEmpty() ) {
					height = null;
				} else {
					int n = this.currentUnder.getComponentCount();
					if( n > 0 ) {
						int index = this.currentUnder.calculateIndex( eUnder.getPoint() );
						if( index < n ) {
							p.y = this.currentUnder.getComponent( index ).getY();
						} else {
							java.awt.Component lastComponent = this.currentUnder.getComponent( n - 1 );
							p.y = lastComponent.getY() + lastComponent.getHeight();
							if( this.currentUnder.getProperty() == this.code.getBodyProperty().getValue().statements ) {
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
		this.repaint();

	}
	public void dragDropped( final PotentiallyDraggablePane source, final java.awt.event.MouseEvent e ) {
		if( source instanceof edu.cmu.cs.dennisc.alice.ide.editors.type.StatementTemplatePane ) {
			class Worker extends org.jdesktop.swingworker.SwingWorker< edu.cmu.cs.dennisc.alice.ast.Statement, Object > {
				private edu.cmu.cs.dennisc.alice.ide.editors.common.DropAndDropEvent dragAndDropEvent;
				private StatementListPropertyPane statementListPropertyPane;
				private int index;

				public Worker() {
					this.dragAndDropEvent = new edu.cmu.cs.dennisc.alice.ide.editors.common.DropAndDropEvent( source, CodeEditor.this, e );
					this.statementListPropertyPane = CodeEditor.this.currentUnder;
					this.index = this.statementListPropertyPane.calculateIndex( javax.swing.SwingUtilities.convertPoint( source, e.getPoint(), this.statementListPropertyPane ) );
				}
				@Override
				protected edu.cmu.cs.dennisc.alice.ast.Statement doInBackground() throws java.lang.Exception {
					PotentiallyDraggablePane potentiallyDraggablePane = this.dragAndDropEvent.getTypedSource();
					if( potentiallyDraggablePane instanceof edu.cmu.cs.dennisc.alice.ide.editors.type.StatementTemplatePane ) {
						edu.cmu.cs.dennisc.alice.ide.editors.type.StatementTemplatePane statementTemplatePane = (edu.cmu.cs.dennisc.alice.ide.editors.type.StatementTemplatePane)potentiallyDraggablePane;
//						try {
							edu.cmu.cs.dennisc.alice.ast.Statement statement = statementTemplatePane.createStatement( this.dragAndDropEvent );
							return statement;
//						} catch( Throwable t ) {
//							this.cancel( true );
//							throw new RuntimeException( t );
//						}
					} else {
						return null;
					}
				}
				@Override
				protected void done() {
					try {
						edu.cmu.cs.dennisc.alice.ast.Statement statement = this.get();
						if( statement != null ) {
							this.statementListPropertyPane.getProperty().add( this.index, statement );
							getIDE().markChanged( "drop statement template" );
							CodeEditor.this.refresh();
						} else {
							//todo?
						}
					} catch( InterruptedException ie ) {
						throw new RuntimeException( ie );
					} catch( java.util.concurrent.ExecutionException ee ) {
						throw new RuntimeException( ee );
					} finally {
						source.hideDropProxyIfNecessary();
					}
				}
			}
			//this.codeDeclarationInAlice.getBodyProperty().getValue().statements.add( expressionStatement );
			if( this.currentUnder != null ) {
				Worker worker = new Worker();
				worker.execute();
			} else {
				source.hideDropProxyIfNecessary();
			}
		} else if( source instanceof AbstractStatementPane ) {
			source.hideDropProxyIfNecessary();
			if( this.currentUnder != null ) {
				AbstractStatementPane abstractStatementPane = (AbstractStatementPane)source;
				edu.cmu.cs.dennisc.alice.ast.Statement statement = abstractStatementPane.getStatement();
				edu.cmu.cs.dennisc.alice.ast.StatementListProperty prevOwner = abstractStatementPane.getOwner();
				edu.cmu.cs.dennisc.alice.ast.StatementListProperty nextOwner = this.currentUnder.getProperty();

				int prevIndex = prevOwner.indexOf( statement );
				int nextIndex = this.currentUnder.calculateIndex( javax.swing.SwingUtilities.convertPoint( source, e.getPoint(), this.currentUnder ) );

				if( edu.cmu.cs.dennisc.swing.SwingUtilities.isQuoteControlUnquoteDown( e ) ) {
					nextOwner.add( nextIndex, (edu.cmu.cs.dennisc.alice.ast.Statement)getIDE().createCopy( statement ) );
				} else {
					if( prevOwner == nextOwner ) {
						if( prevIndex == nextIndex || prevIndex == nextIndex - 1 ) {
							//pass
						} else {
							prevOwner.remove( prevIndex );
							if( prevIndex < nextIndex ) {
								nextIndex--;
							}
							nextOwner.add( nextIndex, statement );
						}
					} else {
						prevOwner.remove( prevIndex );
						nextOwner.add( nextIndex, statement );
					}
				}
				getIDE().markChanged( "drop statement" );
				this.refresh();
				this.repaint();
			}
		}
	}
	public void dragExited( PotentiallyDraggablePane source, java.awt.event.MouseEvent e, boolean isDropRecipient ) {
		this.statementListPropertyPaneInfos = null;
		this.currentUnder = null;
		this.repaint();
		if( isDropRecipient ) {
			//pass
		} else {
			source.hideDropProxyIfNecessary();
		}
	}
	public void dragStopped( PotentiallyDraggablePane source, java.awt.event.MouseEvent e ) {
	}

//	@Override
//	public void paint( java.awt.Graphics g ) {
//		super.paint( g );
//		if( this.statementListPropertyPaneInfos != null ) {
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
//						g2.setColor( color );
//						g2.fill( statementListPropertyPaneInfo.getBounds() );
//						g2.setColor( new java.awt.Color( 0, 0, 0, 255 ) );
//						g2.draw( statementListPropertyPaneInfo.getBounds() );
//					}
//				}
//			}
//		}
//	}
}
