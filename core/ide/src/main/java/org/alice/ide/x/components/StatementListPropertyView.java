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
package org.alice.ide.x.components;

import edu.cmu.cs.dennisc.java.awt.GraphicsUtilities;
import edu.cmu.cs.dennisc.property.InstancePropertyOwner;
import org.alice.ide.ast.code.ShiftDragStatementUtilities;
import org.alice.ide.ast.draganddrop.statement.StatementDragModel;
import org.alice.ide.codeeditor.StatementListBorder;
import org.alice.ide.common.DefaultStatementPane;
import org.alice.ide.croquet.components.AbstractListPropertyPane;
import org.alice.ide.croquet.models.ui.formatter.FormatterState;
import org.alice.ide.x.AstI18nFactory;
import org.alice.ide.x.MutableAstI18nFactory;
import org.lgna.croquet.DragModel;
import org.lgna.croquet.history.DragStep;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.project.ast.BlockStatement;
import org.lgna.project.ast.BooleanExpressionBodyPair;
import org.lgna.project.ast.ConditionalStatement;
import org.lgna.project.ast.DoInOrder;
import org.lgna.project.ast.DoTogether;
import org.lgna.project.ast.Node;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.StatementListProperty;

import javax.swing.BoxLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

/**
 * @author Dennis Cosgrove
 */
public class StatementListPropertyView extends AbstractListPropertyPane<StatementListProperty, Statement> {
	private static final int INDENT = 8;
	private static final int INTRASTICIAL_MIDDLE = 1;
	public static final int INTRASTICIAL_PAD = ( INTRASTICIAL_MIDDLE * 2 ) + 1;

	private final StatementListBorder statementListBorder;

	public StatementListPropertyView( AstI18nFactory factory, StatementListProperty property, int bottom ) {
		super( factory, property, BoxLayout.PAGE_AXIS );

		Node owningNode = this.getOwningBlockStatementOwningNode();

		//boolean isIf = isOwnedByIf( owningNode );
		boolean isElse = isOwnedByElse( owningNode );
		boolean isDoInOrder = owningNode instanceof DoInOrder;
		boolean isDoTogether = owningNode instanceof DoTogether;

		Insets insets;
		if( bottom != 0 ) {
			insets = new Insets( INTRASTICIAL_PAD, this.getLeftInset(), bottom, 0 );
		} else {
			if( /* isIf || */isElse || isDoInOrder || isDoTogether ) {
				bottom = 8;
			}
			insets = new Insets( INTRASTICIAL_PAD, this.getLeftInset(), bottom, this.getRightInset() );
		}

		StatementListProperty alternateListProperty;
		if( owningNode instanceof BooleanExpressionBodyPair ) {
			ConditionalStatement conditionalStatement = (ConditionalStatement)owningNode.getParent();
			alternateListProperty = conditionalStatement.elseBody.getValue().statements;
		} else if( owningNode instanceof ConditionalStatement ) {
			ConditionalStatement conditionalStatement = (ConditionalStatement)owningNode;
			alternateListProperty = conditionalStatement.booleanExpressionBodyPairs.get( 0 ).body.getValue().statements;
		} else {
			alternateListProperty = null;
		}

		boolean isMutable;

		if( factory instanceof MutableAstI18nFactory ) {
			MutableAstI18nFactory mutableFactory = (MutableAstI18nFactory)factory;
			isMutable = mutableFactory.isStatementListPropertyMutable( property );
		} else {
			isMutable = false;
		}
		this.statementListBorder = new StatementListBorder( isMutable, alternateListProperty, insets, ( isDoInOrder || isDoTogether ) ? 1 : 0 );

		this.setBorder( this.statementListBorder );
	}

	public StatementListPropertyView( AstI18nFactory factory, final StatementListProperty property ) {
		this( factory, property, 0 );
	}

	public StatementListBorder getStatementListBorder() {
		return this.statementListBorder;
	}

	public boolean isAcceptingOfAddEventListenerMethodInvocationStatements() {
		return false;
	}

	@Override
	protected int getBoxLayoutPad() {
		if( FormatterState.isJava() ) {
			Node owningNode = this.getOwningBlockStatementOwningNode();
			if( owningNode instanceof DoTogether ) {
				Graphics g = GraphicsUtilities.getGraphics();
				//todo:
				//java.awt.Font font = this.getFont();
				Font font = g.getFont();
				FontMetrics fm = g.getFontMetrics( font );
				return fm.getHeight() + 8;
			}
		}
		return INTRASTICIAL_PAD;
	}

	@Override
	protected final AwtComponentView<?> createInterstitial( int i, int N ) {
		return null;
	}

	private static final Color FEEDBACK_COLOR = Color.GREEN.darker().darker();

	public class FeedbackJPanel extends DefaultJPanel {
		@Override
		public void paint( Graphics g ) {
			super.paint( g );
			int i = StatementListPropertyView.this.currentPotentialDropIndex;
			if( i != -1 ) {
				final int N = StatementListPropertyView.this.getProperty().size();
				if( ( N == this.getComponentCount() ) && ( i >= 0 ) && ( i < N ) ) {
					if( ( i != -1 ) && ( N > 0 ) ) {
						int y;
						if( i == N ) {
							Component lastComponent = this.getComponent( N - 1 );
							y = lastComponent.getY();
							y += lastComponent.getHeight();
						} else {
							Component iComponent = this.getComponent( i );
							y = iComponent.getY();
							y -= INTRASTICIAL_PAD;
						}

						ComponentOrientation componentOrientation = this.getComponentOrientation();
						int x0;
						int x1;
						if( componentOrientation.isLeftToRight() ) {
							x0 = 0;
							x1 = x0 + INDENT;
						} else {
							x0 = this.getWidth();
							x1 = x0 - INDENT;
						}

						int yC = Math.max( y + INTRASTICIAL_MIDDLE, 1 );
						int y0 = yC - INDENT;
						int y1 = yC + INDENT;

						int w = this.getWidth();
						int[] xPoints = new int[] { x1, x0, x0 };
						int[] yPoints = new int[] { yC, y1, y0 };
						g.setColor( FEEDBACK_COLOR );

						if( isShiftDown && ShiftDragStatementUtilities.isCandidateForEnvelop( currentDragModel ) ) {
							Component lastComponent = this.getComponent( N - 1 );
							final int INDENT = 2;
							final int BRACKET_A_WIDTH = 4;
							final int BRACKET_B_WIDTH = 8;
							final int BRACKET_B_HEIGHT = 4;
							int yMax = lastComponent.getY() + lastComponent.getHeight() + INTRASTICIAL_PAD;
							if( componentOrientation.isLeftToRight() ) {
								final int X = INDENT;
								g.fillRect( X, y, BRACKET_A_WIDTH, yMax - y );
								g.fillRect( X, y, BRACKET_B_WIDTH, BRACKET_B_HEIGHT );
								g.fillRect( X, yMax - BRACKET_B_HEIGHT, BRACKET_B_WIDTH, BRACKET_B_HEIGHT );
							} else {
								final int X = this.getWidth() - INDENT - BRACKET_A_WIDTH;
								g.fillRect( X, y, BRACKET_A_WIDTH, yMax - y );
								g.fillRect( X - BRACKET_B_WIDTH, y, BRACKET_B_WIDTH, BRACKET_B_HEIGHT );
								g.fillRect( X - BRACKET_B_WIDTH, yMax - BRACKET_B_HEIGHT, BRACKET_B_WIDTH, BRACKET_B_HEIGHT );
							}
						} else {
							g.fillRect( 0, y, w, INTRASTICIAL_PAD );
							g.fillPolygon( xPoints, yPoints, 3 );
						}

						//g.setColor( java.awt.Color.YELLOW );
						//g.fillRect( 1, yC, w-2, 1 );
					}
				}
			}
		}

		@Override
		public Dimension getMaximumSize() {
			return this.getPreferredSize();
		}
	};

	@Override
	protected DefaultJPanel createJPanel() {
		return new FeedbackJPanel();
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

	private Dimension dropSize = new Dimension( 0, 0 );
	private int currentPotentialDropIndex = -1;
	private DragModel currentDragModel;
	private boolean isShiftDown;

	public static boolean EPIC_HACK_ignoreDrawingDesired = false;

	public void setIsCurrentUnder( boolean isCurrentUnder ) {
		if( isCurrentUnder ) {
			//pass
		} else {
			this.setCurrentPotentialDropIndexAndDragStep( -1, null );
		}
		this.statementListBorder.setDrawingDesired( isCurrentUnder == false );
		//		if( this.getComponentCount() > 0 ) {
		//			org.lgna.croquet.components.Component<?> component0 = this.getComponent( 0 );
		//			if( component0 instanceof org.alice.ide.codeeditor.EmptyStatementListAffordance ) {
		//				org.alice.ide.codeeditor.EmptyStatementListAffordance emptyStatementListAfforance = (org.alice.ide.codeeditor.EmptyStatementListAffordance)component0;
		//
		//				boolean isDrawingDesired = isCurrentUnder == false;
		//
		//				if( EPIC_HACK_ignoreDrawingDesired ) {
		//					edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: EmptyStatementListAffordance isDrawingDisabled" );
		//					isDrawingDesired = false;
		//				}
		//
		//				emptyStatementListAfforance.setDrawingDesired( isDrawingDesired );
		//			}
		//		}
	}

	public int getCurrentPotentialDropIndex() {
		return this.currentPotentialDropIndex;
	}

	public void setCurrentPotentialDropIndexAndDragStep( int currentPotentialDropIndex, DragStep dragStep ) {
		if( dragStep != null ) {
			this.currentDragModel = dragStep.getModel();
			MouseEvent e = dragStep.getLatestMouseEvent();
			if( e != null ) {
				this.isShiftDown = e.isShiftDown();
			} else {
				this.isShiftDown = false;
			}
		} else {
			this.currentDragModel = null;
			this.isShiftDown = false;
		}
		if( this.currentPotentialDropIndex != currentPotentialDropIndex ) {
			this.currentPotentialDropIndex = currentPotentialDropIndex;
			this.repaint();
		}
	}

	public void setDropSize( Dimension dropSize ) {
		if( dropSize != null ) {
			this.dropSize.setSize( dropSize );
		}
	}

	private Node getOwningBlockStatementOwningNode() {
		InstancePropertyOwner owner = this.getProperty().getOwner();
		if( owner instanceof BlockStatement ) {
			BlockStatement blockStatement = (BlockStatement)owner;
			return blockStatement.getParent();
		} else {
			return null;
		}
	}

	private static boolean isOwnedByIf( Node owningNode ) {
		return owningNode instanceof BooleanExpressionBodyPair;
	}

	private static boolean isOwnedByElse( Node owningNode ) {
		return owningNode instanceof ConditionalStatement;
	}

	public Rectangle getDropBounds( DefaultStatementPane statementAncestor ) {
		Node owningNode = this.getOwningBlockStatementOwningNode();
		boolean isIf = isOwnedByIf( owningNode );
		boolean isElse = isOwnedByElse( owningNode );
		Rectangle rv = this.getBounds( statementAncestor );

		if( isIf || isElse ) {
			final int IF_ELSE_PAD = this.getFont().getSize() / 2;
			if( isIf ) {
				rv.height += rv.y;
				rv.y = 0;
				rv.height += IF_ELSE_PAD;
				statementAncestor.setMaxYForIfBlock( rv.y + rv.height );
				rv.y += IF_ELSE_PAD;
			} else {
				rv.y = statementAncestor.getMaxYForIfBlock();
				rv.height = statementAncestor.getHeight() - rv.y;
			}
			rv.height -= IF_ELSE_PAD;

		} else {
			int spaceOnTop = rv.y;
			int spaceOnBottom = statementAncestor.getHeight() - ( rv.y + rv.height );
			int spaceOnTopToLeave = spaceOnTop / 2;
			int spaceOnBottomToLeave = spaceOnBottom / 2;
			rv.y = spaceOnTopToLeave;
			rv.height = statementAncestor.getHeight() - spaceOnTopToLeave - spaceOnBottomToLeave;
		}
		return rv;
	}

	@Override
	protected AwtComponentView<?> createComponent( Statement statement ) {
		return this.getFactory().createStatementPane( StatementDragModel.getInstance( statement ), statement, getProperty() );
	}

	//	@Override
	//	protected void internalRefresh() {
	//		super.internalRefresh();
	//		int bottom;
	//		if( this.getComponentCount() == 0 ) {
	//			org.lgna.project.ast.Node owningNode = this.getOwningBlockStatementOwningNode();
	//			org.lgna.project.ast.StatementListProperty alternateListProperty;
	//			if( owningNode instanceof org.lgna.project.ast.BooleanExpressionBodyPair ) {
	//				org.lgna.project.ast.ConditionalStatement conditionalStatement = (org.lgna.project.ast.ConditionalStatement)owningNode.getParent();
	//				alternateListProperty = conditionalStatement.elseBody.getValue().statements;
	//			} else if( owningNode instanceof org.lgna.project.ast.ConditionalStatement ) {
	//				org.lgna.project.ast.ConditionalStatement conditionalStatement = (org.lgna.project.ast.ConditionalStatement)owningNode;
	//				alternateListProperty = conditionalStatement.booleanExpressionBodyPairs.get( 0 ).body.getValue().statements;
	//			} else {
	//				alternateListProperty = null;
	//			}
	//			this.addComponent( new org.alice.ide.codeeditor.EmptyStatementListAffordance( this.getFactory(), this.getProperty(), alternateListProperty ) );
	//			bottom = 0;
	//		} else {
	//			org.lgna.project.ast.Node owningNode = this.getOwningBlockStatementOwningNode();
	//			//boolean isIf = isOwnedByIf( owningNode );
	//			boolean isElse = isOwnedByElse( owningNode );
	//			boolean isDoInOrder = owningNode instanceof org.lgna.project.ast.DoInOrder;
	//			boolean isDoTogether = owningNode instanceof org.lgna.project.ast.DoTogether;
	//			if( /* isIf || */isElse || isDoInOrder || isDoTogether ) {
	//				bottom = 8;
	//			} else {
	//				bottom = 0;
	//			}
	//		}
	//		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( INTRASTICIAL_PAD, this.getLeftInset(), bottom, this.getRightInset() ) );
	//	}

	protected int getLeftInset() {
		return INDENT;
	}

	protected int getRightInset() {
		return 16;
	}

	public boolean isEmpty() {
		return this.getComponentCount() == 0;
	}

	private Integer getCenterYOfComponentAt( int i ) {
		if( ( i >= 0 ) && ( i < this.getComponentCount() ) ) {
			Component componentI = this.getAwtComponent().getComponent( i );
			return componentI.getY() + ( componentI.getHeight() / 2 );
		} else {
			return null;
		}
	}

	public int calculateIndex( Point p ) {
		if( isEmpty() ) {
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

	public static class BoundInformation {
		public Integer yMinimum;
		public Integer yMaximum;
		public Integer y;
		public Integer yPlusHeight;
	}

	public BoundInformation calculateYBounds( int index ) {
		final int N;
		if( isEmpty() ) {
			N = 0;
		} else {
			N = this.getComponentCount();
		}
		if( index == Short.MAX_VALUE ) {
			index = N;
		}
		BoundInformation rv = new BoundInformation();
		if( index == 0 ) {
			rv.yMinimum = null;
		} else {
			rv.yMinimum = this.getCenterYOfComponentAt( index - 1 );
		}
		if( index == N ) {
			rv.yMaximum = null;
		} else {
			rv.yMaximum = this.getCenterYOfComponentAt( index );
		}

		if( N == 0 ) {
			rv.y = null;
			rv.yPlusHeight = null;
		} else {
			if( index == 0 ) {
				rv.y = null;
				rv.yPlusHeight = 0;
			} else if( index == N ) {
				AwtComponentView<?> lastComponent = this.getComponent( N - 1 );
				rv.y = lastComponent.getY() + lastComponent.getHeight();
				rv.yPlusHeight = null;
			} else {
				if( index < this.getComponentCount() ) {
					AwtComponentView<?> component = this.getComponent( index );
					rv.y = component.getY();
					rv.yPlusHeight = rv.y + component.getHeight();
				}
			}
		}
		return rv;
	}

}
