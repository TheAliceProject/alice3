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
package org.alice.ide.croquet.models.project.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import org.alice.ide.croquet.models.project.MethodFrequencyTabComposite;
import org.alice.ide.croquet.models.project.MethodFrequencyTabComposite.InvocationCounts;
import org.alice.ide.croquet.models.project.MethodFrequencyTabComposite.MethodCountPair;
import org.alice.ide.croquet.models.project.StatisticsOperation;
import org.lgna.croquet.ListSelectionState;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.components.BorderPanel;
import org.lgna.croquet.components.CheckBox;
import org.lgna.croquet.components.Component;
import org.lgna.croquet.components.GridPanel;
import org.lgna.croquet.components.HorizontalAlignment;
import org.lgna.croquet.components.Label;
import org.lgna.croquet.components.LineAxisPanel;
import org.lgna.croquet.components.ScrollPane;
import org.lgna.croquet.components.ScrollPane.HorizontalScrollbarPolicy;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.UserMethod;

import edu.cmu.cs.dennisc.java.awt.DimensionUtilities;
import edu.cmu.cs.dennisc.java.util.Collections;

/**
 * @author Matt May
 */
public class MethodFrequencyView extends BorderPanel {

	ListSelectionState<UserMethod> listSelectionState;
	
	public MethodFrequencyView( MethodFrequencyTabComposite composite ) {
		
		GridPanel gridPanel = GridPanel.createGridPane( 2, 1 );
		listSelectionState = composite.getUserMethodList();
		
		ControlDisplay statsDisplay = new ControlDisplay( composite.getUserMethodList() );
		statsDisplay.setMaximum();
		listSelectionState.addValueListener( statsDisplay );
		listSelectionState.setSelectedIndex( 0 );
		gridPanel.addComponent( statsDisplay.getLayout() );
		org.lgna.croquet.components.List<UserMethod> list = new org.lgna.croquet.components.List<UserMethod>( composite.getUserMethodList() );
		list.setCellRenderer( new ListCellRenderer() );

		ScrollPane scrollPane = new ScrollPane( list );
		gridPanel.addComponent( scrollPane );

		scrollPane.setMaximumPreferredHeight( StatisticsOperation.BOTTOM_SIZE );
		scrollPane.setMinimumPreferredHeight( StatisticsOperation.BOTTOM_SIZE );
		statsDisplay.scroll.setMaximumPreferredHeight( StatisticsOperation.TOP_SIZE );
		statsDisplay.scroll.setMinimumPreferredHeight( StatisticsOperation.TOP_SIZE );
		statsDisplay.scroll.setHorizontalScrollbarPolicy( HorizontalScrollbarPolicy.NEVER );
		this.addComponent( gridPanel, Constraint.CENTER );
	}

	private class ListCellRenderer extends DefaultListCellRenderer {
		@Override
		public java.awt.Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
			Label rv = new Label();
			if( isSelected ) {
				rv.setBackgroundColor( Color.BLUE );
				rv.setForegroundColor( Color.WHITE );
			}
			if( !value.equals( MethodFrequencyTabComposite.dummy ) ) {
				if( value instanceof AbstractMethod ) {
					AbstractMethod userMethod = (AbstractMethod)value;
					rv.setText( userMethod.getName() );
					return rv.getAwtComponent();
				}
			}
			rv.setText( "<HTML><Strong>MyProject</Strong></HTML>" );
			return rv.getAwtComponent();
		}
	}

	public class ControlDisplay implements ValueListener<UserMethod> {

		private GridPanel gridPanel;
		private Map<Integer,Map<Integer,Component>> componentMap = Collections.newHashMap();
		private int numRows = 6;
		private int numCols = 2;
		private int maximum = 10;
		private ScrollPane scroll = new ScrollPane();
		private int minSize = 6;

		private ValueListener<Boolean> booleanListener = new ValueListener<Boolean>() {

			public void changing( State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			}

			public void changed( State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
				update( listSelectionState.getSelectedItem() );
			}

		};

		public ControlDisplay( ListSelectionState<UserMethod> listSelectionState ) {
			initGridPanel();
			populateGridPanel();
			this.gridPanel.setBackgroundColor( Color.WHITE );
		}

		private void initGridPanel() {
			CheckBox hideFunctionsBox = ((MethodFrequencyTabComposite)getComposite()).getShowFunctionsState().createCheckBox();
			LineAxisPanel child = new LineAxisPanel( hideFunctionsBox, ((MethodFrequencyTabComposite)getComposite()).getShowProceduresState().createCheckBox() );

			MethodFrequencyView.this.addComponent( child, Constraint.PAGE_START );
			this.gridPanel = GridPanel.createGridPane( minSize, numCols, 5, 5 );
			for( int i = 0; i != minSize; ++i ) {
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
			((MethodFrequencyTabComposite)getComposite()).getShowFunctionsState().addValueListener( booleanListener );
			((MethodFrequencyTabComposite)getComposite()).getShowProceduresState().addValueListener( booleanListener );
			scroll.setViewportView( gridPanel );
		}

		public void setMaximum() {
			InvocationCounts invocationCounts = new InvocationCounts();
			for( UserMethod method : ((MethodFrequencyTabComposite)getComposite()).getMapMethodToInvocationCounts().keySet() ) {
				for( MethodCountPair pair : ((MethodFrequencyTabComposite)getComposite()).getMapMethodToInvocationCounts().get( method ).getMethodCountPairs() ) {
					for( int i = 0; i != pair.getCount(); ++i ) {
						invocationCounts.addMethod( pair.getMethod() );
					}
				}
			}
			((MethodFrequencyTabComposite)getComposite()).getMapMethodToInvocationCounts().put(MethodFrequencyTabComposite.dummy, invocationCounts );
			maximum = getCount( MethodFrequencyTabComposite.dummy, null );
		}

		private class BarLabel extends Label {

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
						g2.setPaint( new Color( 150, 255, 150 ) );

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
			if( gridPanel != null )
				scroll.setViewportView( null );
			this.gridPanel = GridPanel.createGridPane( numRows, numCols, 5, 5 );
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
			gridPanel.setBackgroundColor( Color.WHITE );
			scroll.setViewportView( gridPanel );
		}

		public ScrollPane getLayout() {
			return scroll;
		}

		private void update( UserMethod selected ) {
			setHeight( getSize( ((MethodFrequencyTabComposite)getComposite()).getMapMethodToInvocationCounts().get( selected ) ) + 1 );
			populateLeftCol( selected );
			populateRightCol( selected );
		}

		private void setHeight( int size ) {
			numRows = size > minSize ? size : minSize;
			populateGridPanel();
		}

		private void populateRightCol( UserMethod selected ) {
			((Label)getCell( 0, 0 )).setText( "<HTML><Strong>" + selected.getName() + "</Strong></HTML>" );
			InvocationCounts invocationCount = ((MethodFrequencyTabComposite)getComposite()).getMapMethodToInvocationCounts().get( selected );
			int index = 1;
			for( MethodCountPair pair : invocationCount.getMethodCountPairs() ) {
				if( !pair.getMethod().isFunction() || ((MethodFrequencyTabComposite)getComposite()).getShowFunctionsState().getValue() ) {
					if( !pair.getMethod().isProcedure() || ((MethodFrequencyTabComposite)getComposite()).getShowProceduresState().getValue() ) {
						setCell( 1, index, getCount( selected, pair.getMethod() ) );
						++index;
					}
				}
			}
		}

		private int getSize( InvocationCounts invocationCounts ) {
			int count = 0;
			for( MethodCountPair pair : invocationCounts.getMethodCountPairs() ) {
				if( !pair.getMethod().isFunction() || ((MethodFrequencyTabComposite)getComposite()).getShowFunctionsState().getValue() ) {
					if( !pair.getMethod().isProcedure() || ((MethodFrequencyTabComposite)getComposite()).getShowProceduresState().getValue() ) {
						++count;
					}
				}
			}
			return count;
		}

		private void populateLeftCol( UserMethod selected ) {
			int index = 1;
			InvocationCounts invocationsCount = ((MethodFrequencyTabComposite)getComposite()).getMapMethodToInvocationCounts().get( selected );
			for( MethodCountPair pair : invocationsCount.getMethodCountPairs() ) {
				if( !pair.getMethod().isFunction() || ((MethodFrequencyTabComposite)getComposite()).getShowFunctionsState().getValue() ) {
					if( !pair.getMethod().isProcedure() || ((MethodFrequencyTabComposite)getComposite()).getShowProceduresState().getValue() ) {
						setCell( 0, index, pair.getMethod().getName() );
						++index;
					}
				}
			}
		}

		private void setCell( int col, int row, int count ) {
			Component component = getCell( col, row );
			if( component instanceof BarLabel ) {
				BarLabel label = (BarLabel)component;
				label.setCount( count );
			}
		}
		private void setCell( int col, int row, String name ) {
			Component component = getCell( col, row );
			if( component instanceof Label ) {
				Label label = (Label)component;
				label.setText( name );
			}
		}
		private Component getCell( int col, int row ) {
			return componentMap.get( row ).get( col );
		}

		public int getCount( AbstractMethod method, AbstractMethod methodTwo ) {
			int count = 0;
			if( methodTwo != null ) {
				count = ((MethodFrequencyTabComposite)getComposite()).getMapMethodToInvocationCounts().get( method ).get( methodTwo ).getCount();
			} else {
				if( ((MethodFrequencyTabComposite)getComposite()).getMapMethodToInvocationCounts().get( method ) != null ) {
					for( MethodCountPair pair : ((MethodFrequencyTabComposite)getComposite()).getMapMethodToInvocationCounts().get( method ).getMethodCountPairs() ) {
						if( pair.getMethod() != MethodFrequencyTabComposite.dummy ) {
							count += pair.getCount();
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

}
