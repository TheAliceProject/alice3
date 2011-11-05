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

package org.lgna.project;

/**
 * @author Dennis Cosgrove
 */
public class ProgramTypeUtilities {
	private ProgramTypeUtilities() {
		throw new AssertionError();
	}
	private static Iterable< org.lgna.project.ast.UserVariable > getVariables( org.lgna.project.ast.AbstractCode codeInFocus ) {
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.UserVariable > crawler = edu.cmu.cs.dennisc.pattern.IsInstanceCrawler.createInstance( org.lgna.project.ast.UserVariable.class );
		codeInFocus.crawl( crawler, false );
		return crawler.getList();
	}
	private static Iterable< org.lgna.project.ast.UserConstant > getConstants( org.lgna.project.ast.AbstractCode codeInFocus ) {
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.UserConstant > crawler = edu.cmu.cs.dennisc.pattern.IsInstanceCrawler.createInstance( org.lgna.project.ast.UserConstant.class );
		codeInFocus.crawl( crawler, false );
		return crawler.getList();
	}

	public static java.util.List< org.lgna.project.ast.FieldAccess > getFieldAccesses( org.lgna.project.ast.NamedUserType programType, final org.lgna.project.ast.AbstractField field ) {
		assert programType != null;
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.FieldAccess > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.FieldAccess >( org.lgna.project.ast.FieldAccess.class ) {
			@Override
			protected boolean isAcceptable( org.lgna.project.ast.FieldAccess fieldAccess ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( fieldAccess.field.getValue() );
				return fieldAccess.field.getValue() == field;
			}
		};
		programType.crawl( crawler, true );
		return crawler.getList();
	}
	public static java.util.List< org.lgna.project.ast.MethodInvocation > getMethodInvocations( org.lgna.project.ast.NamedUserType programType, final org.lgna.project.ast.AbstractMethod method ) {
		assert programType != null;
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.MethodInvocation > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.MethodInvocation >(
				org.lgna.project.ast.MethodInvocation.class ) {
			@Override
			protected boolean isAcceptable( org.lgna.project.ast.MethodInvocation methodInvocation ) {
				return methodInvocation.method.getValue() == method;
			}
		};
		programType.crawl( crawler, true );
		return crawler.getList();
	}
	public static java.util.List< org.lgna.project.ast.SimpleArgumentListProperty > getArgumentLists( org.lgna.project.ast.NamedUserType programType, final org.lgna.project.ast.UserCode code ) {
		assert programType != null;
		class ArgumentListCrawler implements edu.cmu.cs.dennisc.pattern.Crawler {
			private final java.util.List< org.lgna.project.ast.SimpleArgumentListProperty > list = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			public void visit( edu.cmu.cs.dennisc.pattern.Crawlable crawlable ) {
				if( crawlable instanceof org.lgna.project.ast.MethodInvocation ) {
					org.lgna.project.ast.MethodInvocation methodInvocation = (org.lgna.project.ast.MethodInvocation)crawlable;
					if( methodInvocation.method.getValue() == code ) {
						this.list.add( methodInvocation.requiredArguments );
					}
				} else if( crawlable instanceof org.lgna.project.ast.InstanceCreation ) {
					org.lgna.project.ast.InstanceCreation instanceCreation = (org.lgna.project.ast.InstanceCreation)crawlable;
					if( instanceCreation.constructor.getValue() == code ) {
						this.list.add( instanceCreation.requiredArguments );
					}
				}
			}
			public java.util.List< org.lgna.project.ast.SimpleArgumentListProperty > getList() {
				return this.list;
			}
		}
		ArgumentListCrawler crawler = new ArgumentListCrawler();
		programType.crawl( crawler, true );
		return crawler.getList();
	}

	public static java.util.Set< org.alice.virtualmachine.Resource > getReferencedResources( org.lgna.project.Project project ) {
		org.lgna.project.ast.AbstractType<?,?,?> programType = project.getProgramType();
		java.util.Set< org.alice.virtualmachine.Resource > resources = project.getResources();
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.ResourceExpression > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.ResourceExpression >( org.lgna.project.ast.ResourceExpression.class ) {
			@Override
			protected boolean isAcceptable( org.lgna.project.ast.ResourceExpression resourceExpression ) {
				return true;
			}
		};
		programType.crawl( crawler, true );
		
		java.util.Set< org.alice.virtualmachine.Resource > rv = new java.util.HashSet< org.alice.virtualmachine.Resource >();
		for( org.lgna.project.ast.ResourceExpression resourceExpression : crawler.getList() ) {
			org.alice.virtualmachine.Resource resource = resourceExpression.resource.getValue();
			if( resources.contains( resource ) ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: adding missing resource", resource );
				resources.add( resource );
			}
			rv.add( resource );
		}
		return rv;
	}
	public static <N extends org.lgna.project.ast.Node > N lookupNode( org.lgna.project.Project project, final java.util.UUID id ) {
		final org.lgna.project.ast.Node[] buffer = { null };
		org.lgna.project.ast.NamedUserType programType = project.getProgramType();
		edu.cmu.cs.dennisc.pattern.Crawler crawler = new edu.cmu.cs.dennisc.pattern.Crawler() {
			public void visit( edu.cmu.cs.dennisc.pattern.Crawlable crawlable ) {
				if( crawlable instanceof org.lgna.project.ast.Node ) {
					org.lgna.project.ast.Node node = (org.lgna.project.ast.Node)crawlable;
					if( id.equals( node.getUUID() ) ) {
						buffer[ 0 ] = node;
					}
				}
			}
		};
		programType.crawl( crawler, true );
		return (N)buffer[ 0 ];
	}
	public static <R extends org.alice.virtualmachine.Resource > R lookupResource( org.lgna.project.Project project, java.util.UUID id ) {
		for( org.alice.virtualmachine.Resource resource : project.getResources() ) {
			if( resource.getId() == id ) {
				return (R)resource;
			}
		}
		return null;
	}
	
	private static edu.cmu.cs.dennisc.tree.DefaultNode< org.lgna.project.ast.NamedUserType > getNode( org.lgna.project.ast.NamedUserType type, edu.cmu.cs.dennisc.tree.DefaultNode< org.lgna.project.ast.NamedUserType > root ) {
		edu.cmu.cs.dennisc.tree.DefaultNode< org.lgna.project.ast.NamedUserType > rv = root.get( type );
		if( rv != null ) {
			//pass
		} else {
			rv = edu.cmu.cs.dennisc.tree.DefaultNode.createUnsafeInstance( type );
			org.lgna.project.ast.AbstractType< ?,?,? > superType = type.getSuperType();
			if( superType instanceof org.lgna.project.ast.NamedUserType ) {
				edu.cmu.cs.dennisc.tree.DefaultNode< org.lgna.project.ast.NamedUserType > superNode = getNode( (org.lgna.project.ast.NamedUserType)superType, root );
				superNode.addChild( rv );
			} else {
				root.addChild( rv );
			}
		}
		return rv;
	}

	public static edu.cmu.cs.dennisc.tree.DefaultNode< org.lgna.project.ast.NamedUserType > getNamedUserTypesAsTree( org.lgna.project.Project project ) {
		edu.cmu.cs.dennisc.tree.DefaultNode< org.lgna.project.ast.NamedUserType > root = edu.cmu.cs.dennisc.tree.DefaultNode.createSafeInstance( null );
		Iterable< org.lgna.project.ast.NamedUserType > types = project.getNamedUserTypes();
		for( org.lgna.project.ast.NamedUserType type : types ) {
			getNode( type, root );
		}
		return root;
	}
}
