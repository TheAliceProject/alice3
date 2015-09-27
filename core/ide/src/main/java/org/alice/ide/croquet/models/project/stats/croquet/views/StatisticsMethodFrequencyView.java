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
package org.alice.ide.croquet.models.project.stats.croquet.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import org.alice.ide.croquet.models.project.stats.croquet.StatisticsFrameComposite;
import org.alice.ide.croquet.models.project.stats.croquet.StatisticsMethodFrequencyTabComposite;
import org.lgna.croquet.MutableDataSingleSelectListState;
import org.lgna.croquet.SingleSelectListState;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.CheckBox;
import org.lgna.croquet.views.GridPanel;
import org.lgna.croquet.views.HorizontalAlignment;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.LineAxisPanel;
import org.lgna.croquet.views.ScrollPane;
import org.lgna.croquet.views.ScrollPane.HorizontalScrollbarPolicy;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.UserMethod;

import edu.cmu.cs.dennisc.java.awt.DimensionUtilities;
import edu.cmu.cs.dennisc.java.util.Maps;

/**
 * @author Matt May
 */
public class StatisticsMethodFrequencyView extends BorderPanel {

	MutableDataSingleSelectListState<UserMethod> listSelectionState;

	public StatisticsMethodFrequencyView( StatisticsMethodFrequencyTabComposite composite ) {
		super( composite );
		GridPanel gridPanel = GridPanel.createGridPane( 2, 1 );
		listSelectionState = composite.getUserMethodList();

		ControlDisplay statsDisplay = new ControlDisplay( composite.getUserMethodList() );
		statsDisplay.setMaximum();
		listSelectionState.addValueListener( statsDisplay );
		listSelectionState.setSelectedIndex( 0 );
		gridPanel.addComponent( statsDisplay.getLayout() );
		org.lgna.croquet.views.List<UserMethod> list = new org.lgna.croquet.views.List<UserMethod>( composite.getUserMethodList() );
		list.setCellRenderer( new ListCellRenderer() );

		ScrollPane scrollPane = new ScrollPane( list );
		gridPanel.addComponent( scrollPane );

		scrollPane.setMaximumPreferredHeight( StatisticsFrameComposite.BOTTOM_SIZE );
		scrollPane.setMinimumPreferredHeight( StatisticsFrameComposite.BOTTOM_SIZE );
		statsDisplay.scroll.setMaximumPreferredHeight( StatisticsFrameComposite.TOP_SIZE );
		statsDisplay.scroll.setMinimumPreferredHeight( StatisticsFrameComposite.TOP_SIZE );
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
			if( !value.equals( StatisticsMethodFrequencyTabComposite.root ) ) {
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
		private Map<Integer, Map<Integer, AwtComponentView>> componentMap = Maps.newHashMap();
		private boolean showFunctions;
		private boolean showProcedures;
		private int numRows = 6;
		private int numCols = 2;
		private int maximum = 10;
		private ScrollPane scroll = new ScrollPane();
		private int minSize = 6;

		private ValueListener<Boolean> booleanListener = new ValueListener<Boolean>() {

			@Override
			public void changing( State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			}

			@Override
			public void changed( State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
				update( listSelectionState.getValue() );
			}

		};

		public ControlDisplay( SingleSelectListState<UserMethod, ?> listSelectionState ) {
			initGridPanel();
			populateGridPanel();
			this.gridPanel.setBackgroundColor( Color.WHITE );
		}

		private void initGridPanel() {
			this.gridPanel = GridPanel.createGridPane( minSize, numCols, 5, 5 );
			for( int i = 0; i != minSize; ++i ) {
				componentMap.put( i, new HashMap<Integer, AwtComponentView>() );
				for( int j = 0; j != numCols; ++j ) {
					Label label;
					if( ( j == 1 ) && ( i != 0 ) ) {
						label = new BarLabel();
					} else {
						label = new Label();
						label.setHorizontalAlignment( HorizontalAlignment.RIGHT );
					}
					gridPanel.addComponent( label );
					componentMap.get( i ).put( j, label );
				}
			}
			( (StatisticsMethodFrequencyTabComposite)getComposite() ).getShowFunctionsState().addValueListener( booleanListener );
			( (StatisticsMethodFrequencyTabComposite)getComposite() ).getShowProceduresState().addValueListener( booleanListener );
			scroll.setViewportView( gridPanel );
			CheckBox hideFunctionsBox = ( (StatisticsMethodFrequencyTabComposite)getComposite() ).getShowFunctionsState().createCheckBox();
			LineAxisPanel child = new LineAxisPanel( hideFunctionsBox, ( (StatisticsMethodFrequencyTabComposite)getComposite() ).getShowProceduresState().createCheckBox() );

			StatisticsMethodFrequencyView.this.addComponent( child, Constraint.PAGE_START );
		}

		public void setMaximum() {
			( (StatisticsMethodFrequencyTabComposite)getComposite() ).getMaximum();
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

						int w = (int)( this.getWidth() * ( count / (double)maximum ) ) + 1;
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
			if( gridPanel != null ) {
				scroll.setViewportView( null );
			}
			this.gridPanel = GridPanel.createGridPane( numRows, numCols, 5, 5 );
			for( int i = 0; i != numRows; ++i ) {
				componentMap.put( i, new HashMap<Integer, AwtComponentView>() );
				for( int j = 0; j != numCols; ++j ) {
					Label label;
					if( ( j == 1 ) && ( i != 0 ) ) {
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
			if( selected == null ) {
				selected = StatisticsMethodFrequencyTabComposite.root;
			}
			setHeight( ( (StatisticsMethodFrequencyTabComposite)getComposite() ).getSize( selected ) );
			populateLeftCol( selected );
			populateRightCol( selected );
		}

		private void setHeight( int size ) {
			numRows = size > minSize ? size : minSize;
			populateGridPanel();
		}

		private void populateRightCol( UserMethod selected ) {
			( (Label)getCell( 0, 0 ) ).setText( "<HTML><Strong>" + selected.getName() + "</Strong></HTML>" );
			List<Integer> rightColVals = ( (StatisticsMethodFrequencyTabComposite)getComposite() ).getRightColVals( selected );
			int index = 1;
			for( Integer i : rightColVals ) {
				setCell( 1, index, i );
				++index;
			}
		}

		private void populateLeftCol( UserMethod selected ) {
			int index = 1;
			LinkedList<String> leftColVals = ( (StatisticsMethodFrequencyTabComposite)getComposite() ).getLeftColVals( selected );
			for( String str : leftColVals ) {
				setCell( 0, index, str );
				++index;
			}
		}

		private void setCell( int col, int row, int count ) {
			AwtComponentView component = getCell( col, row );
			if( component instanceof BarLabel ) {
				BarLabel label = (BarLabel)component;
				label.setCount( count );
			}
		}

		private void setCell( int col, int row, String name ) {
			AwtComponentView component = getCell( col, row );
			if( component instanceof Label ) {
				Label label = (Label)component;
				label.setText( name );
			}
		}

		private AwtComponentView getCell( int col, int row ) {
			return componentMap.get( row ).get( col );
		}

		public int getCount( AbstractMethod method, AbstractMethod methodTwo ) {
			return ( (StatisticsMethodFrequencyTabComposite)getComposite() ).getCount( method, methodTwo );
		}

		@Override
		public void changing( State<UserMethod> state, UserMethod prevValue, UserMethod nextValue, boolean isAdjusting ) {
		}

		@Override
		public void changed( State<UserMethod> state, UserMethod prevValue, UserMethod nextValue, boolean isAdjusting ) {
			update( nextValue );
		}
	}

}
