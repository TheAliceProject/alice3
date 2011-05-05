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

/**
 * @author Dennis Cosgrove
 */
public class StatisticsOperation extends edu.cmu.cs.dennisc.croquet.InformationDialogOperation<edu.cmu.cs.dennisc.croquet.JComponent<?>> {
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
	protected edu.cmu.cs.dennisc.croquet.Container<?> createContentPane(org.lgna.croquet.steps.InformationDialogOperationStep<edu.cmu.cs.dennisc.croquet.JComponent<?>> context, edu.cmu.cs.dennisc.croquet.Dialog dialog) {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice programType = ide.getStrippedProgramType();
		if( programType != null ) {
			class StatementCountCrawler implements edu.cmu.cs.dennisc.pattern.Crawler {
				private java.util.Map< Class<? extends edu.cmu.cs.dennisc.alice.ast.Statement>, Integer > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap(); 
				public void visit(edu.cmu.cs.dennisc.pattern.Crawlable crawlable) {
					if( crawlable instanceof edu.cmu.cs.dennisc.alice.ast.Statement ) {
						edu.cmu.cs.dennisc.alice.ast.Statement statement = (edu.cmu.cs.dennisc.alice.ast.Statement)crawlable;
						Class<? extends edu.cmu.cs.dennisc.alice.ast.Statement> cls = statement.getClass();
						Integer count = this.map.get( cls );
						if( count != null ) {
							count += 1;
						} else {
							count = 1;
						}
						this.map.put( cls, count );
					}
				}
				public int getCount( Class<? extends edu.cmu.cs.dennisc.alice.ast.Statement> cls ) {
					Integer count = this.map.get( cls );
					if( count != null ) {
						return count;
					} else {
						return 0;
					}
				}
			}
			StatementCountCrawler crawler = new StatementCountCrawler();
			programType.crawl(crawler, true);
			
			Class[] clses = { 
				edu.cmu.cs.dennisc.alice.ast.DoInOrder.class,
				edu.cmu.cs.dennisc.alice.ast.CountLoop.class,
				edu.cmu.cs.dennisc.alice.ast.WhileLoop.class,
				edu.cmu.cs.dennisc.alice.ast.ForEachInArrayLoop.class,
				edu.cmu.cs.dennisc.alice.ast.ConditionalStatement.class,
				edu.cmu.cs.dennisc.alice.ast.DoTogether.class,
				edu.cmu.cs.dennisc.alice.ast.EachInArrayTogether.class,
				edu.cmu.cs.dennisc.alice.ast.DoInThread.class,
				edu.cmu.cs.dennisc.alice.ast.Comment.class,
				edu.cmu.cs.dennisc.alice.ast.VariableDeclarationStatement.class,
				edu.cmu.cs.dennisc.alice.ast.ConstantDeclarationStatement.class,
				edu.cmu.cs.dennisc.alice.ast.ExpressionStatement.class
			};
			
			StringBuilder sb = new StringBuilder();
			sb.append( "<html>" );
			sb.append( "<em>todo: improve this dialog dramatically</em><br><br>" );
			for( Class cls : clses ) {
				int count = crawler.getCount( cls );
				if( count > 0 ) {
					sb.append( cls.getSimpleName() );
					sb.append( ": " );
					sb.append( count );
					sb.append( "<br>" );
				}
			}
			sb.append( "</html>" );
			return new edu.cmu.cs.dennisc.croquet.Label( sb.toString() );
		} else {
			//todo
			return new edu.cmu.cs.dennisc.croquet.Label( "open a project first" );
		}
	}
	@Override
	protected void releaseContentPane(org.lgna.croquet.steps.InformationDialogOperationStep<edu.cmu.cs.dennisc.croquet.JComponent<?>> context, edu.cmu.cs.dennisc.croquet.Dialog dialog, edu.cmu.cs.dennisc.croquet.Container<?> contentPane) {
	}
}
