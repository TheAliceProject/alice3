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
package org.alice.ide.common;

import org.alice.ide.codeeditor.EmptyStatementListAffordance;

/**
 * @author Dennis Cosgrove
 */
public class StatementListPropertyPane extends AbstractListPropertyPane< edu.cmu.cs.dennisc.alice.ast.StatementListProperty > {
	public static final int INTRASTICIAL_PAD = 4;
//	private static final int INTRASTICIAL_PAD = 0;
	public StatementListPropertyPane( Factory factory, final edu.cmu.cs.dennisc.alice.ast.StatementListProperty property ) {
		super( factory, javax.swing.BoxLayout.PAGE_AXIS, property );		
//		this.addMouseListener( new java.awt.event.MouseListener() {
//			public void mouseClicked( final java.awt.event.MouseEvent e ) {
//				final alice.ide.IDE ide = alice.ide.IDE.getSingleton();
//				if( ide != null ) {
//					//final StatementListPropertyPane statementListPropertyPane = getStatementListPropertyPaneUnder( e, createStatementListPropertyPaneInfos( null ) );
//					final StatementListPropertyPane statementListPropertyPane = StatementListPropertyPane.this;
//					if( statementListPropertyPane != null ) {
//						ide.promptUserForStatement( e, new edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Statement >() {
//							public void handleCompletion( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
//								java.awt.Point p = e.getPoint();
//								//p = javax.swing.SwingUtilities.convertPoint( e.getComponent(), p, statementListPropertyPane );
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
	}
	
	public int getAvailableDropProxyHeight() {
//		int heightAvailable = this.getHeight();
//		if( this.isFigurativelyEmpty() ) {
//			return heightAvailable;
//		} else {
//			return Math.min( dropSize.height, heightAvailable/2 );
//		}
		return -1;
	}
//	@Override
//	protected javax.swing.JPanel createJPanel() {
//		class StatementListJPanel extends edu.cmu.cs.dennisc.croquet.Panel.DefaultJPanel {
//			@Override
//			protected void paintChildren( java.awt.Graphics g ) {
//				if( StatementListPropertyPane.this.currentPotentialDropIndex != -1 ) {
//					java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
//					final int N = this.getComponentCount();
//					int heightAvailable = this.getHeight();
//					int heightAllocatedToDrop = StatementListPropertyPane.this.getAvailableDropProxyHeight();
//					int heightAllocatedToThis = heightAvailable - heightAllocatedToDrop;
//					double yScale = heightAllocatedToThis/(double)heightAvailable;
//					java.awt.geom.AffineTransform m = g2.getTransform();
//					try {
//						int i = StatementListPropertyPane.this.currentPotentialDropIndex;
//						if( i == 0 || i == N ) {
//							if( i == 0 ) {
//								g2.translate( 0, heightAllocatedToDrop );
//							}
//							g2.scale( 1.0, yScale );
//							super.paintChildren( g );
//						} else {
//							super.paintChildren( g );
//						}
//					} finally {
//						g2.setTransform( m );
//					}
//				} else {
//					super.paintChildren( g );
//				}
//			}
//		}
//		return new StatementListJPanel();
//	}
	private java.awt.Dimension dropSize = new java.awt.Dimension( 0,0 );
	private int currentPotentialDropIndex = -1;
	public void setIsCurrentUnder( boolean isCurrentUnder ) {
		if( isCurrentUnder ) {
			//pass
		} else {
			this.setCurrentPotentialDropIndex( -1 );
		}
		if( this.getComponentCount() > 0 ) {
			edu.cmu.cs.dennisc.croquet.Component<?> component0 = this.getComponent( 0 );
			if( component0 instanceof EmptyStatementListAffordance ) {
				EmptyStatementListAffordance emptyStatementListAfforance = (EmptyStatementListAffordance)component0;
				emptyStatementListAfforance.setDrawingDesired( isCurrentUnder == false );
			}
		}
	}
	public void setCurrentPotentialDropIndex( int currentPotentialDropIndex ) {
		if( this.currentPotentialDropIndex != currentPotentialDropIndex ) {
			this.currentPotentialDropIndex = currentPotentialDropIndex;
			this.repaint();
		}
	}
	public void setDropSize( java.awt.Dimension dropSize ) {
		if( dropSize != null ) {
			this.dropSize.setSize( dropSize );
		}
	}
	
	@Override
	protected int getBoxLayoutPad() {
		int rv;
		if( this.getProperty().getOwner() instanceof edu.cmu.cs.dennisc.alice.ast.DoTogether ) {
			rv = 0;
		} else {
			rv = INTRASTICIAL_PAD;
		}
		return rv;
	}	
	public java.awt.Rectangle getDropBounds() {
		edu.cmu.cs.dennisc.alice.ast.BlockStatement owner = (edu.cmu.cs.dennisc.alice.ast.BlockStatement)this.getProperty().getOwner();
		DefaultStatementPane statementAncestor = this.getFirstAncestorAssignableTo( DefaultStatementPane.class );
		if( statementAncestor != null ) {
			java.awt.Rectangle rv = this.getBounds( statementAncestor );
			final int IF_ELSE_PAD = 6;
			if( owner.getParent() instanceof edu.cmu.cs.dennisc.alice.ast.BooleanExpressionBodyPair ) {
				//if case
				rv.height += rv.y;
				rv.y = -rv.y;

				rv.y += IF_ELSE_PAD;
				rv.height -= IF_ELSE_PAD;

			} else if( owner.getParent() instanceof edu.cmu.cs.dennisc.alice.ast.ConditionalStatement ) {
				//note: since the if case is checked first, it is currently okay that the else rectangle overlaps (consumes) it
				
				//else case
				rv.height += rv.y;
				rv.y = -rv.y;
				
				rv.y += IF_ELSE_PAD;
				rv.height -= IF_ELSE_PAD;
			} else {
				int spaceOnTop = rv.y;
				int spaceOnBottom = statementAncestor.getHeight()-(rv.y+rv.height);
				int spaceOnTopToTake = 2*spaceOnTop/3;
				int spaceOnBottomToTake = 2*spaceOnBottom/3;
				rv.x = 0;
				rv.y = -spaceOnTopToTake;
				rv.height += spaceOnTopToTake;
				rv.height += spaceOnBottomToTake;
			}
			return rv;
		} else {
			return this.getLocalBounds();
		}
	}

	
	@Override
	protected edu.cmu.cs.dennisc.croquet.Component< ? > createComponent( Object instance ) {
		edu.cmu.cs.dennisc.alice.ast.Statement statement = (edu.cmu.cs.dennisc.alice.ast.Statement)instance;
		return this.getFactory().createStatementPane( statement, getProperty() );
	}
	
	@Override
	protected void refresh() {
		super.refresh();
		int bottom;
		if( this.getComponentCount() == 0 ) {
			this.addComponent( new EmptyStatementListAffordance() );
			bottom = 0;
		} else {
			bottom = 4;
		}
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 0, bottom, 16 ) );
		repaint();
	}
	public boolean isFigurativelyEmpty() {
		return this.getComponentCount() == 0 || this.getComponent( 0 ) instanceof EmptyStatementListAffordance;
	}

	private int getCenterYOfComponentAt( int i ) {
		java.awt.Component componentI = this.getAwtComponent().getComponent( i );
		return componentI.getY() + componentI.getHeight() / 2;
	}
	public int calculateIndex( java.awt.Point p ) {
		if( isFigurativelyEmpty() ) {
			return 0;
		} else {
			for( int i = 0; i < this.getComponentCount(); i++ ) {
				int yCenterI = this.getCenterYOfComponentAt( i );
				if( p.y < yCenterI ) {
					return i;
				}
			}
			return this.getComponentCount();
		}
	}

}
