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
package org.alice.ide.croquet.models.project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;

import org.lgna.croquet.DefaultListSelectionState;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.components.Component;
import org.lgna.croquet.components.GridPanel;
import org.lgna.croquet.components.HorizontalAlignment;
import org.lgna.croquet.components.Label;
import org.lgna.project.ast.AbstractEachInTogether;
import org.lgna.project.ast.AbstractForEachLoop;
import org.lgna.project.ast.ConditionalStatement;
import org.lgna.project.ast.CountLoop;
import org.lgna.project.ast.DoInOrder;
import org.lgna.project.ast.DoTogether;
import org.lgna.project.ast.LocalDeclarationStatement;
import org.lgna.project.ast.ReturnStatement;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.WhileLoop;

import edu.cmu.cs.dennisc.java.awt.DimensionUtilities;
import edu.cmu.cs.dennisc.java.util.Collections;

/**
 * @author Matt May
 */
public class StatisticsDisplay implements ValueListener<UserMethod> {

	private GridPanel gridPanel;
	private DefaultListSelectionState<UserMethod> listState;
	private Map<UserMethod,List<Statement>> methodToConstructMap;
	private UserMethod dummy;
	private Map<Integer,Map<Integer,Component>> componentMap = Collections.newHashMap();
	private int numRows = 10;
	private int numCols = 2;
	private static int maximum = 10;
	private static Class[] clsArr = { ConditionalStatement.class, CountLoop.class, WhileLoop.class, AbstractForEachLoop.class, AbstractEachInTogether.class, ReturnStatement.class, LocalDeclarationStatement.class, DoInOrder.class, DoTogether.class };

	public StatisticsDisplay( DefaultListSelectionState<UserMethod> listSelectionState ) {
		this.gridPanel = GridPanel.createGridPane( numRows, numCols, 5, 5 );
		this.listState = listSelectionState;
		listState.addValueListener( this );
		populateGridPanel();
		populateLeftCol();
		this.gridPanel.setBackgroundColor( Color.WHITE );
	}

	public void setMap( Map<UserMethod,List<Statement>> methodToConstructMap ) {
		this.methodToConstructMap = methodToConstructMap;
	}

	public void setDummy( UserMethod method ) {
		this.dummy = method;
		dummy.setName( "Project" );
		update( dummy );
		setMaximum( methodToConstructMap );
	}

	public void setMaximum( Map<UserMethod,List<Statement>> methodToConstructMap ) {
		int maxCount = 0;
		for( Class cls : clsArr ) {
			int count = getCount( dummy, cls );
			if( count > maxCount ) {
				maxCount = count;
			}
		}
		maximum = maxCount;
	}

	private static class BarLabel extends Label {

		private int count;

		public BarLabel() {
			this.setBackgroundColor( null );
			this.setForegroundColor( Color.BLACK );
		}
		public int getCount() {
			return this.count;
		}
		public void setCount( int count ) {
			this.count = count;
			this.setText( "" + this.count );
		}
		@Override
		protected JLabel createAwtComponent() {
			return new JLabel() {
				@Override
				protected void paintComponent( Graphics g ) {
					Graphics2D g2 = (Graphics2D)g;
					//g2.setPaint( this.getBackground() );
					g2.setPaint( new Color( 150, 150, 255 ) );

					int w = (int)(this.getWidth() * (count / (double)maximum)) + 1;
					g2.fillRect( 0, 0, w, this.getHeight() );
					g2.setPaint( this.getForeground() );
					super.paintComponent( g );
				}
				@Override
				public Dimension getPreferredSize() {
					return DimensionUtilities.constrainToMinimumWidth( super.getPreferredSize(), 320 );
				}
			};
		}
	}

	private void populateGridPanel() {
		for( int i = 0; i != numRows; ++i ) {
			componentMap.put( i, new HashMap<Integer,Component>() );
			for( int j = 0; j != numCols; ++j ) {
				Label label;
				if( j == 1 && i != 0 ) {
					label = new BarLabel();
				} else {
					label = new Label();
					label.setHorizontalAlignment( HorizontalAlignment.RIGHT );
				}
				gridPanel.addComponent( label );
				componentMap.get( i ).put( j, label );
			}
		}
	}
	public GridPanel getLayout() {
		return gridPanel;
	}

	private void update( UserMethod selected ) {
		((Label)getCell( 0, 0 )).setText( "<HTML><Strong>" + selected.getName() + "</Strong></HTML>" );
		for( int i = 1; i != numRows; ++i ) {
			setCell( 1, i, getCount( selected, clsArr[ i - 1 ] ) );
		}
	}

	private void populateLeftCol() {
		((Label)getCell( 0, 1 )).setText( "If" );
		((Label)getCell( 0, 2 )).setText( "CountLoop" );
		((Label)getCell( 0, 3 )).setText( "WhileLoop" );
		((Label)getCell( 0, 4 )).setText( "ForEach" );
		((Label)getCell( 0, 5 )).setText( "EachTogether" );
		((Label)getCell( 0, 6 )).setText( "Return" );
		((Label)getCell( 0, 7 )).setText( "LocalDeclaration" );
		((Label)getCell( 0, 8 )).setText( "DoInOrder" );
		((Label)getCell( 0, 9 )).setText( "DoTogether" );
	}

	private void setCell( int col, int row, int count ) {
		Component component = getCell( col, row );
		if( component instanceof BarLabel ) {
			BarLabel label = (BarLabel)component;
			label.setCount( count );
		}
	}
	private Component getCell( int col, int row ) {
		return componentMap.get( row ).get( col );
	}

	public int getCount( UserMethod method, Class<? extends org.lgna.project.ast.Statement> cls ) {
		int count = 0;
		if( !method.equals( dummy ) ) {
			for( Statement statement : methodToConstructMap.get( method ) ) {
				if( statement.getClass().isAssignableFrom( cls ) ) {
					++count;
				}
			}
		} else {
			for( UserMethod userMethod : methodToConstructMap.keySet() ) {
				for( Statement statement : methodToConstructMap.get( userMethod ) ) {
					if( statement.getClass().isAssignableFrom( cls ) ) {
						++count;
					}
				}
			}
		}
		return count;
	}

	public void changing( State<UserMethod> state, UserMethod prevValue, UserMethod nextValue, boolean isAdjusting ) {
	}

	public void changed( State<UserMethod> state, UserMethod prevValue, UserMethod nextValue, boolean isAdjusting ) {
		update( nextValue );
	}
}
