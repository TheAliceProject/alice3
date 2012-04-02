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
import org.lgna.croquet.DefaultListSelectionState;
import org.lgna.croquet.ItemCodec;
import org.lgna.croquet.Model;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.TabComposite;
import org.lgna.croquet.components.Component;
import org.lgna.croquet.components.GridPanel;
import org.lgna.croquet.components.HorizontalAlignment;
import org.lgna.croquet.components.Label;
import org.lgna.croquet.components.ScrollPane;
import org.lgna.croquet.components.ScrollPane.HorizontalScrollbarPolicy;
import org.lgna.croquet.components.View;
import org.lgna.project.ast.AbstractMethod;
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

	private Map<UserMethod,List<AbstractMethod>> methodToConstructMap = Collections.newHashMap();
	private Map<UserMethod,Map<AbstractMethod,Integer>> methodCountMap = Collections.newHashMap();
	private DefaultListSelectionState<UserMethod> listSelectionState;
	private UserMethod dummy = new UserMethod();

	
	private static class MethodCountPair {
		private final AbstractMethod method;
		private int count;
		public MethodCountPair( AbstractMethod method ) {
			this.method = method;
			this.count = 1;
		}
		public void bumpItUpANotch() {
			this.count ++;
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
			AbstractMethod method = invocation.method.getValue();
			for( MethodCountPair methodCountPair : this.methodCountPairs ) {
				if( methodCountPair.getMethod().equals( method ) ) {
					methodCountPair.bumpItUpANotch();
					return;
				}
			}
			this.methodCountPairs.add( new MethodCountPair( method ) );
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
		for( UserMethod method : methodToConstructMap.keySet() ) {
			a.add( method );
			sort( methodToConstructMap.get( method ) );
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
		this.view = rv;
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

	@Override
	public boolean contains( Model model ) {
		return false;
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

//	private class StatementCountCrawler implements edu.cmu.cs.dennisc.pattern.Crawler {
//		private java.util.Map<Class<? extends org.lgna.project.ast.Statement>,Integer> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
//
//		public void visit( edu.cmu.cs.dennisc.pattern.Crawlable crawlable ) {
//			if( crawlable instanceof org.lgna.project.ast.Statement ) {
//				org.lgna.project.ast.Statement statement = (org.lgna.project.ast.Statement)crawlable;
//				final AbstractMethod parentMethod = statement.getFirstAncestorAssignableTo( AbstractMethod.class );
//				if( parentMethod != null ) {
//					if( parentMethod instanceof UserMethod ) {
//						final UserMethod parent = (UserMethod)parentMethod;
//						//						if( !parent.getManagementLevel().isGenerated() ) {
//
//						methodToConstructMap.put( parent, new LinkedList<AbstractMethod>() );
//						methodCountMap.put( parent, new HashMap<AbstractMethod,Integer>() );
//						parent.body.getValue().crawl( new Crawler() {
//
//							public void visit( Crawlable crawlable ) {
//								if( crawlable instanceof MethodInvocation ) {
//									MethodInvocation invocation = (MethodInvocation)crawlable;
//									if( invocation.method.getValue() instanceof AbstractMethod ) {
//										AbstractMethod method = (AbstractMethod)invocation.method.getValue();
//										if( !methodToConstructMap.keySet().contains( parent ) ) {
//											methodToConstructMap.put( parent, new LinkedList<AbstractMethod>() );
//										}
//										if( !methodToConstructMap.get( parent ).contains( method ) ) {
//											methodCountMap.get( parent ).put( method, 1 );
//											methodToConstructMap.get( parent ).add( method );
//										} else {
//											methodCountMap.get( parent ).put( method, methodCountMap.get( parent ).get( method ) + 1 );
//										}
//									}
//								}
//							}
//						}, false );
//					} else {
//						if( !(parentMethod instanceof UserLambda) ) {
//							System.out.println( "hello " + parentMethod.getClass() );
//							System.out.println( " bye: " + parentMethod );
//						}
//						//						System.out.println( "filtred: " + parentMethod.getName() );
//					}
//				}
//			}
//		}
//	}
	
	
	private static class MethodInvocationCrawler implements edu.cmu.cs.dennisc.pattern.Crawler {
		private final Map<AbstractMethod,List<MethodInvocation>> mapMethodToInvocations = Collections.newHashMap();
		public void visit( edu.cmu.cs.dennisc.pattern.Crawlable crawlable ) {
			if( crawlable instanceof MethodInvocation ) {
				MethodInvocation methodInvocation = (MethodInvocation)crawlable;
				AbstractMethod method = methodInvocation.method.getValue();
				
				List<MethodInvocation> list = this.mapMethodToInvocations.get( method );
				if( list != null ) {
					list.add( methodInvocation );
				} else {
					list = Collections.newLinkedList( methodInvocation );
					this.mapMethodToInvocations.put( method, list );
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

		public ControlDisplay( DefaultListSelectionState<UserMethod> listSelectionState ) {
			initGridPanel();
			populateGridPanel();
			this.gridPanel.setBackgroundColor( Color.WHITE );
		}

		private void initGridPanel() {
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
			scroll.setViewportView( gridPanel );
		}

		public void setMaximum() {
			maximum = getCount( dummy, null );
			methodToConstructMap.put( dummy, new LinkedList<AbstractMethod>() );
			methodCountMap.put( dummy, new HashMap<AbstractMethod,Integer>() );
			for( UserMethod method : methodToConstructMap.keySet() ) {
				for( AbstractMethod childMethod : methodToConstructMap.get( method ) ) {
					if( !method.equals( dummy ) ) {
						if( !methodToConstructMap.get( dummy ).contains( childMethod ) ) {
							methodCountMap.get( dummy ).put( childMethod, 0 );
							methodToConstructMap.get( dummy ).add( childMethod );
						}
						methodCountMap.get( dummy ).put( childMethod, methodCountMap.get( method ).get( childMethod ) + methodCountMap.get( dummy ).get( childMethod ) );
					}
				}
			}
			sort( methodToConstructMap.get( dummy ) );
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
			setHeight( methodToConstructMap.get( selected ).size() + 1 );
			populateLeftCol( selected );
			populateRightCol( selected );
		}

		private void setHeight( int size ) {
			numRows = size > minSize ? size : minSize;
			populateGridPanel();
		}

		private void populateRightCol( UserMethod selected ) {
			((Label)getCell( 0, 0 )).setText( "<HTML><Strong>" + selected.getName() + "</Strong></HTML>" );
			for( int i = 0; i != methodToConstructMap.get( selected ).size(); ++i ) {
				setCell( 1, i + 1, getCount( selected, methodToConstructMap.get( selected ).get( i ) ) );
			}
		}

		private void populateLeftCol( UserMethod selected ) {
			for( int i = 0; i != methodToConstructMap.get( selected ).size(); ++i ) {
				setCell( 0, i + 1, methodToConstructMap.get( selected ).get( i ).getName() );
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
				count = methodCountMap.get( method ).get( methodTwo );
			} else {
				for( UserMethod userMethod : methodToConstructMap.keySet() ) {
					for( AbstractMethod child : methodToConstructMap.get( userMethod ) ) {
						if( !userMethod.equals( dummy ) ) {
							count += methodCountMap.get( userMethod ).get( child );
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