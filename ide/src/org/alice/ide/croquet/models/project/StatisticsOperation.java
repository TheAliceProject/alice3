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
import java.awt.Component;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.alice.ide.ProjectApplication;
import org.lgna.croquet.DefaultListSelectionState;
import org.lgna.croquet.InformationDialogOperation;
import org.lgna.croquet.ItemCodec;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.components.Container;
import org.lgna.croquet.components.Dialog;
import org.lgna.croquet.components.GridPanel;
import org.lgna.croquet.components.Label;
import org.lgna.croquet.components.ScrollPane;
import org.lgna.croquet.history.CompletionStep;
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

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.util.Collections;

/**
 * @author Matt May
 */
public class StatisticsOperation extends InformationDialogOperation implements ValueListener<UserMethod> {

	Map<UserMethod,List<Statement>> methodToConstructMap;
	private Label child = new Label();
	private StatementCountCrawler crawler;
	private DefaultListSelectionState<UserMethod> listSelectionState;
	private UserMethod dummy = new UserMethod();

	public StatisticsOperation() {
		super( java.util.UUID.fromString( "d17d2d7c-ecae-4869-98e6-cc2d4c2fe517" ) );
	}

	private static class SingletonHolder {
		private static StatisticsOperation instance = new StatisticsOperation();

	}

	public static StatisticsOperation getInstance() {
		return SingletonHolder.instance;
	}

	@Override
	protected Container<?> createContentPane( CompletionStep<?> step, Dialog dialog ) {
		GridPanel rv = GridPanel.createGridPane( 2, 1 );
		methodToConstructMap = Collections.newHashMap();
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		org.lgna.project.ast.NamedUserType programType = ide.getStrippedProgramType();

		crawler = new StatementCountCrawler();
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
		List<UserMethod> a = new LinkedList<UserMethod>();
		for( UserMethod method : methodToConstructMap.keySet() ) {
			a.add( method );
		}
		java.util.Collections.sort( a, new Comparator<UserMethod>() {

			public int compare( UserMethod o1, UserMethod o2 ) {
				return o1.getName().compareTo( o2.getName() );
			}
		} );
		listSelectionState.addItem( dummy );
		for( UserMethod method : a ) {
			listSelectionState.addItem( method );
		}
		listSelectionState.addValueListener( this );
		changed( listSelectionState, null, listSelectionState.getItemAt( 0 ), true );
		org.lgna.croquet.components.List<UserMethod> list = new org.lgna.croquet.components.List<UserMethod>( listSelectionState );
		StatisticsDisplay statsDisplay = new StatisticsDisplay( listSelectionState );
		statsDisplay.setMap( methodToConstructMap );
		statsDisplay.setDummy( dummy );
		rv.addComponent( statsDisplay.getLayout() );
		//		rv.addComponent( statsDisplay.getLayout(), Constraint.PAGE_START );
		list.setCellRenderer( new Blah() );

		ScrollPane scrollPane = new ScrollPane( list );
		rv.addComponent( scrollPane );
		//		rv.addComponent( scrollPane, Constraint.CENTER );
		return rv;
	}
	@Override
	protected void releaseContentPane( CompletionStep<?> step, Dialog dialog, Container<?> contentPane ) {
	}

	private class Blah extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
			Label rv = new Label();
			if( isSelected ) {
				rv.setBackgroundColor( Color.BLUE );
				rv.setForegroundColor( Color.WHITE );
			}
			if( !value.equals( dummy ) ) {
				if( value instanceof UserMethod ) {
					UserMethod userMethod = (UserMethod)value;
					rv.setText( userMethod.getName() );
					return rv.getAwtComponent();
				}
			}
			rv.setText( "<HTML><Strong>MyProject</Strong></HTML>" );
			return rv.getAwtComponent();
		}
	}

	class StatementCountCrawler implements edu.cmu.cs.dennisc.pattern.Crawler {
		private java.util.Map<Class<? extends org.lgna.project.ast.Statement>,Integer> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

		public void visit( edu.cmu.cs.dennisc.pattern.Crawlable crawlable ) {
			if( crawlable instanceof org.lgna.project.ast.Statement ) {
				org.lgna.project.ast.Statement statement = (org.lgna.project.ast.Statement)crawlable;
				UserMethod method = statement.getFirstAncestorAssignableTo( UserMethod.class );
				if( method != null ) {
					if( !methodToConstructMap.keySet().contains( method ) ) {
						methodToConstructMap.put( method, new LinkedList<Statement>() );
					}
					methodToConstructMap.get( method ).add( statement );
				}
			}
			//					}
			//					Integer count = this.map.get( cls );
			//					if( count != null ) {
			//						count += 1;
			//					} else {
			//						count = 1;
			//					}
			//					this.map.put( cls, count );
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
	}

	public void changing( State<UserMethod> state, UserMethod prevValue, UserMethod nextValue, boolean isAdjusting ) {
	}

	public void changed( State<UserMethod> state, UserMethod prevValue, UserMethod nextValue, boolean isAdjusting ) {
		String str = "<HTML><Strong>";
		str += nextValue.equals( dummy ) ? "project</Strong><br>" : nextValue.getName() + "</Strong><br>";
		str += "conditional:      " + crawler.getCount( nextValue, ConditionalStatement.class ) + "<br>";
		str += "countLoop:        " + crawler.getCount( nextValue, CountLoop.class ) + "<br>";
		str += "whileLoop:        " + crawler.getCount( nextValue, WhileLoop.class ) + "<br>";
		str += "forEachLoop:      " + crawler.getCount( nextValue, AbstractForEachLoop.class ) + "<br>";
		str += "forEachTogether:  " + crawler.getCount( nextValue, AbstractEachInTogether.class ) + "<br>";
		str += "returnStatement:  " + crawler.getCount( nextValue, ReturnStatement.class ) + "<br>";
		str += "localDeclaration: " + crawler.getCount( nextValue, LocalDeclarationStatement.class ) + "<br>";
		str += "doInOrder:        " + crawler.getCount( nextValue, DoInOrder.class ) + "<br>";
		str += "doTogether:       " + crawler.getCount( nextValue, DoTogether.class ) + "<br>";
		str += "</HTML>";
		child.setText( str );
	}

}
