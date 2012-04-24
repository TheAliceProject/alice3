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
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import org.alice.ide.ProjectApplication;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.DefaultListSelectionState;
import org.lgna.croquet.ItemCodec;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.TabComposite;
import org.lgna.croquet.components.BorderPanel;
import org.lgna.croquet.components.BorderPanel.Constraint;
import org.lgna.croquet.components.CheckBox;
import org.lgna.croquet.components.Component;
import org.lgna.croquet.components.GridPanel;
import org.lgna.croquet.components.HorizontalAlignment;
import org.lgna.croquet.components.Label;
import org.lgna.croquet.components.LineAxisPanel;
import org.lgna.croquet.components.ScrollPane;
import org.lgna.croquet.components.ScrollPane.HorizontalScrollbarPolicy;
import org.lgna.croquet.components.View;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractUserMethod;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.UserMethod;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.awt.DimensionUtilities;
import edu.cmu.cs.dennisc.java.util.Collections;

/**
 * @author Matt May
 */
public class MethodFrequencyTab extends TabComposite<View<?,?>> {

	View view;
	private Map<UserMethod,InvocationCounts> mapMethodToInvocationCounts = Collections.newHashMap();

	private DefaultListSelectionState<UserMethod> listSelectionState;
	private UserMethod dummy = new UserMethod();
	private BorderPanel returnPanel = new BorderPanel();
	private final BooleanState showFunctionsState = this.createBooleanState( true, this.createKey( "areFunctionsShowing" ) );
	private final BooleanState showProceduresState = this.createBooleanState( true, this.createKey( "areProceduresShowing" ) );

	private static class MethodCountPair {
		private final AbstractMethod method;
		private int count;

		public MethodCountPair( AbstractMethod method ) {
			this.method = method;
			this.count = 1;
		}
		public void bumpItUpANotch() {
			this.count++;
		}
		public AbstractMethod getMethod() {
			return this.method;
		}
		public int getCount() {
			return this.count;
		}
	}

	private static class InvocationCounts {
		private List<MethodCountPair> methodCountPairs = Collections.newLinkedList();

		public void addInvocation( MethodInvocation invocation ) {
			addMethod( invocation.method.getValue() );
		}
		public List<MethodCountPair> getMethodCountPairs() {
			return this.methodCountPairs;
		}
		public void addMethod( AbstractMethod method ) {
			if( get( method ) != null ) {
				get( method ).bumpItUpANotch();
			} else {
				methodCountPairs.add( new MethodCountPair( method ) );
				sort();
			}
		}
		private void sort() {
			java.util.Collections.sort( methodCountPairs, new Comparator<MethodCountPair>() {

				public int compare( MethodCountPair o1, MethodCountPair o2 ) {
					return o1.getMethod().getName().compareTo( o2.getMethod().getName() );
				}
			} );
		}
		public int size() {
			return methodCountPairs.size();
		}
		public AbstractMethod get( int i ) {
			return methodCountPairs.get( i ).getMethod();
		}
		public MethodCountPair get( AbstractMethod method ) {
			for( MethodCountPair methodCountPair : this.methodCountPairs ) {
				if( methodCountPair.getMethod().equals( method ) ) {
					return methodCountPair;
				}
			}
			return null;
		}
	}

	public MethodFrequencyTab() {
		super( java.util.UUID.fromString( "93b531e2-69a3-4721-b2c8-d2793181a41c" ) );
		final GridPanel rv = GridPanel.createGridPane( 2, 1 );

		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		org.lgna.project.ast.NamedUserType programType = ide.getStrippedProgramType();

		MethodInvocationCrawler crawler = new MethodInvocationCrawler();
		//		StatementCountCrawler crawler = new StatementCountCrawler();
		programType.crawl( crawler, true );
		listSelectionState = new DefaultListSelectionState<UserMethod>( ProjectApplication.UI_STATE_GROUP, java.util.UUID.fromString( "06b77424-763b-4fdc-a1cb-1404eaefa1d2" ), new ItemCodec<UserMethod>() {

			public Class<UserMethod> getValueClass() {
				return UserMethod.class;
			}

			public UserMethod decodeValue( BinaryDecoder binaryDecoder ) {
				return null;
			}

			public void encodeValue( BinaryEncoder binaryEncoder, UserMethod value ) {
			}

			public StringBuilder appendRepresentation( StringBuilder rv, UserMethod value, Locale locale ) {
				return rv.append( value.getName() );
			}
		} );

		for( AbstractMethod method : crawler.getMethods() ) {
			List<MethodInvocation> invocations = crawler.getInvocationsFor( method );
			for( MethodInvocation invocation : invocations ) {
				UserMethod invocationOwner = invocation.getFirstAncestorAssignableTo( UserMethod.class );

				InvocationCounts invocationCounts = this.mapMethodToInvocationCounts.get( invocationOwner );
				if( invocationCounts != null ) {
					//pass
				} else {
					invocationCounts = new InvocationCounts();
					this.mapMethodToInvocationCounts.put( invocationOwner, invocationCounts );
				}
				invocationCounts.addInvocation( invocation );
			}
		}

		List<UserMethod> a = new LinkedList<UserMethod>();
		for( UserMethod method : mapMethodToInvocationCounts.keySet() ) {
			a.add( method );
		}
		sort( a );
		listSelectionState.addItem( dummy );
		for( UserMethod method : a ) {
			listSelectionState.addItem( method );
		}
		org.lgna.croquet.components.List<UserMethod> list = new org.lgna.croquet.components.List<UserMethod>( listSelectionState );
		ControlDisplay statsDisplay = new ControlDisplay( listSelectionState );
		dummy.setName( "Project" );
		statsDisplay.setMaximum();
		listSelectionState.addValueListener( statsDisplay );
		listSelectionState.setSelectedIndex( 0 );
		rv.addComponent( statsDisplay.getLayout() );
		list.setCellRenderer( new ListCellRenderer() );

		ScrollPane scrollPane = new ScrollPane( list );
		rv.addComponent( scrollPane );

		scrollPane.setMaximumPreferredHeight( StatisticsOperation.BOTTOM_SIZE );
		scrollPane.setMinimumPreferredHeight( StatisticsOperation.BOTTOM_SIZE );
		statsDisplay.scroll.setMaximumPreferredHeight( StatisticsOperation.TOP_SIZE );
		statsDisplay.scroll.setMinimumPreferredHeight( StatisticsOperation.TOP_SIZE );
		statsDisplay.scroll.setHorizontalScrollbarPolicy( HorizontalScrollbarPolicy.NEVER );
		returnPanel.addComponent( rv, Constraint.CENTER );
		this.view = returnPanel;
	}

	private void sort( List<? extends AbstractMethod> a ) {
		java.util.Collections.sort( a, new Comparator<AbstractMethod>() {

			public int compare( AbstractMethod o1, AbstractMethod o2 ) {
				return o1.getName().compareTo( o2.getName() );
			}
		} );
	}

	@Override
	public boolean isCloseable() {
		return false;
	}

	@Override
	protected View<?,?> createView() {
		return view;
	}

	private class ListCellRenderer extends DefaultListCellRenderer {
		@Override
		public java.awt.Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
			Label rv = new Label();
			if( isSelected ) {
				rv.setBackgroundColor( Color.BLUE );
				rv.setForegroundColor( Color.WHITE );
			}
			if( !value.equals( dummy ) ) {
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

	private static class MethodInvocationCrawler implements edu.cmu.cs.dennisc.pattern.Crawler {
		private final Map<AbstractMethod,List<MethodInvocation>> mapMethodToInvocations = Collections.newHashMap();

		public void visit( edu.cmu.cs.dennisc.pattern.Crawlable crawlable ) {
			if( crawlable instanceof MethodInvocation ) {
				MethodInvocation methodInvocation = (MethodInvocation)crawlable;
				AbstractMethod method = methodInvocation.method.getValue();
				if( method instanceof AbstractUserMethod ) {
					AbstractUserMethod userMethod = (AbstractUserMethod)method;
					if( !userMethod.getManagementLevel().isGenerated() ) {
						List<MethodInvocation> list = this.mapMethodToInvocations.get( userMethod );
						if( list != null ) {
							list.add( methodInvocation );
						} else {
							list = Collections.newLinkedList( methodInvocation );
							this.mapMethodToInvocations.put( userMethod, list );
						}
					}
				}
			}
		}

		public java.util.Set<AbstractMethod> getMethods() {
			return this.mapMethodToInvocations.keySet();
		}
		public List<MethodInvocation> getInvocationsFor( AbstractMethod method ) {
			return this.mapMethodToInvocations.get( method );
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

		public ControlDisplay( DefaultListSelectionState<UserMethod> listSelectionState ) {
			initGridPanel();
			populateGridPanel();
			this.gridPanel.setBackgroundColor( Color.WHITE );
		}

		private void initGridPanel() {
			CheckBox hideFunctionsBox = showFunctionsState.createCheckBox();
			LineAxisPanel child = new LineAxisPanel( hideFunctionsBox, showProceduresState.createCheckBox() );

			returnPanel.addComponent( child, Constraint.PAGE_START );
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
			showFunctionsState.addValueListener( booleanListener );
			showProceduresState.addValueListener( booleanListener );
			scroll.setViewportView( gridPanel );
		}

		public void setMaximum() {
			InvocationCounts invocationCounts = new InvocationCounts();
			for( UserMethod method : mapMethodToInvocationCounts.keySet() ) {
				for( MethodCountPair pair : mapMethodToInvocationCounts.get( method ).getMethodCountPairs() ) {
					for( int i = 0; i != pair.getCount(); ++i ) {
						invocationCounts.addMethod( pair.getMethod() );
					}
				}
			}
			mapMethodToInvocationCounts.put( dummy, invocationCounts );
			maximum = getCount( dummy, null );
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
			setHeight( getSize( mapMethodToInvocationCounts.get( selected ) ) + 1 );
			populateLeftCol( selected );
			populateRightCol( selected );
		}

		private void setHeight( int size ) {
			numRows = size > minSize ? size : minSize;
			populateGridPanel();
		}

		private void populateRightCol( UserMethod selected ) {
			((Label)getCell( 0, 0 )).setText( "<HTML><Strong>" + selected.getName() + "</Strong></HTML>" );
			InvocationCounts invocationCount = mapMethodToInvocationCounts.get( selected );
			int index = 1;
			for( MethodCountPair pair : invocationCount.getMethodCountPairs() ) {
				if( !pair.getMethod().isFunction() || showFunctionsState.getValue() ) {
					if( !pair.getMethod().isProcedure() || showProceduresState.getValue() ) {
						setCell( 1, index, getCount( selected, pair.getMethod() ) );
						++index;
					}
				}
			}
		}

		private int getSize( InvocationCounts invocationCounts ) {
			int count = 0;
			for( MethodCountPair pair : invocationCounts.getMethodCountPairs() ) {
				if( !pair.getMethod().isFunction() || showFunctionsState.getValue() ) {
					if( !pair.getMethod().isProcedure() || showProceduresState.getValue() ) {
						++count;
					}
				}
			}
			return count;
		}

		private void populateLeftCol( UserMethod selected ) {
			int index = 1;
			InvocationCounts invocationsCount = mapMethodToInvocationCounts.get( selected );
			for( MethodCountPair pair : invocationsCount.getMethodCountPairs() ) {
				if( !pair.getMethod().isFunction() || showFunctionsState.getValue() ) {
					if( !pair.getMethod().isProcedure() || showProceduresState.getValue() ) {
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
				count = mapMethodToInvocationCounts.get( method ).get( methodTwo ).getCount();
			} else {
				if( mapMethodToInvocationCounts.get( method ) != null ) {
					for( MethodCountPair pair : mapMethodToInvocationCounts.get( method ).methodCountPairs ) {
						if( pair.getMethod() != dummy ) {
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