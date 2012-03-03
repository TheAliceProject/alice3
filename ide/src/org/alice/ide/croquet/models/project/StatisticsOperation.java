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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lgna.croquet.components.BorderPanel;
import org.lgna.croquet.components.BorderPanel.Constraint;
import org.lgna.croquet.components.ScrollPane;
import org.lgna.croquet.components.TextField;
import org.lgna.croquet.components.Tree;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.UserMethod;

import edu.cmu.cs.dennisc.java.util.Collections;

/**
 * @author Dennis Cosgrove
 */
public class StatisticsOperation extends org.lgna.croquet.InformationDialogOperation {

	private Map<UserMethod,LinkedList<MethodInvocation>> methodParentMap = Collections.newHashMap();

	private static class SingletonHolder {
		private static StatisticsOperation instance = new StatisticsOperation();
	}

	public static StatisticsOperation getInstance() {
		return SingletonHolder.instance;
	}
	private StatisticsOperation() {
		super( java.util.UUID.fromString( "b34e805e-e6ef-4f08-af53-df98e1653732" ) );
	}
	@Override
	protected org.lgna.croquet.components.Container<?> createContentPane( org.lgna.croquet.history.OperationStep step, org.lgna.croquet.components.Dialog dialog ) {
		methodParentMap.clear();

		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		org.lgna.project.ast.NamedUserType programType = ide.getStrippedProgramType();
		if( programType != null ) {
			class StatementCountCrawler implements edu.cmu.cs.dennisc.pattern.Crawler {
				private java.util.Map<Class<? extends org.lgna.project.ast.Statement>,Integer> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

				public void visit( edu.cmu.cs.dennisc.pattern.Crawlable crawlable ) {
					if( crawlable instanceof org.lgna.project.ast.Statement ) {
						org.lgna.project.ast.Statement statement = (org.lgna.project.ast.Statement)crawlable;
						Class<? extends org.lgna.project.ast.Statement> cls = statement.getClass();
						if( cls.equals( org.lgna.project.ast.ExpressionStatement.class ) ) {
							ExpressionStatement expressionStatement = (ExpressionStatement)statement;
							if( expressionStatement.expression.getValue() instanceof MethodInvocation ) {
								MethodInvocation methodInvocation = (MethodInvocation)expressionStatement.expression.getValue();
								UserMethod method = statement.getFirstAncestorAssignableTo( UserMethod.class );
								if( methodParentMap.get( method ) == null ) {
									methodParentMap.put( method, new LinkedList<MethodInvocation>() );
								}
								methodParentMap.get( method ).add( methodInvocation );
							}
						}
						Integer count = this.map.get( cls );
						if( count != null ) {
							count += 1;
						} else {
							count = 1;
						}
						this.map.put( cls, count );
					}
				}
				public int getCount( Class<? extends org.lgna.project.ast.Statement> cls ) {
					Integer count = this.map.get( cls );
					if( count != null ) {
						return count;
					} else {
						return 0;
					}
				}
			}
			StatementCountCrawler crawler = new StatementCountCrawler();
			programType.crawl( crawler, true );

			Class[] clses = { org.lgna.project.ast.DoInOrder.class, org.lgna.project.ast.CountLoop.class, org.lgna.project.ast.WhileLoop.class, org.lgna.project.ast.ForEachInArrayLoop.class, org.lgna.project.ast.ConditionalStatement.class,
					org.lgna.project.ast.DoTogether.class, org.lgna.project.ast.EachInArrayTogether.class, org.lgna.project.ast.Comment.class, org.lgna.project.ast.LocalDeclarationStatement.class, org.lgna.project.ast.ExpressionStatement.class };

			final BorderPanel rv = new BorderPanel();
			SearchDialogManager manager = new SearchDialogManager();
			TextField textField = new TextField( manager.getStringState() );
			textField.getAwtComponent().setTextForBlankCondition( "search; *=wildcard" );
			rv.addComponent( textField, Constraint.PAGE_START );
			Tree<SearchTreeNode> tree = new Tree<SearchTreeNode>( manager );
			manager.setOwner( tree );
			rv.addComponent( new ScrollPane( tree ), Constraint.CENTER );

			for( Class cls : clses ) {
				int count = crawler.getCount( cls );
				if( count > 0 ) {
					if( cls.equals( org.lgna.project.ast.ExpressionStatement.class ) ) {
						for( UserMethod method : methodParentMap.keySet() ) {
							SearchTreeNode parent = manager.addNode( null, method );
							List<SearchTreeNode> list = Collections.newLinkedList();
							for( MethodInvocation methodInvocation : methodParentMap.get( method ) ) {
								SearchTreeNode child = manager.addNode( parent, methodInvocation.method.getValue() );
								list.add( child );
							}
							manager.addParentWithChildren( parent, list );
						}
					}
				}
			}
			manager.refreshAll();
			tree.setRootVisible( false );
			tree.expandAllRows();
			//			searchState.addPanel( rv );
			return rv;
		} else {
			//todo
			return new org.lgna.croquet.components.Label( "open a project first" );
		}
	}
	@Override
	protected void releaseContentPane( org.lgna.croquet.history.OperationStep step, org.lgna.croquet.components.Dialog dialog, org.lgna.croquet.components.Container<?> contentPane ) {
	}
}
