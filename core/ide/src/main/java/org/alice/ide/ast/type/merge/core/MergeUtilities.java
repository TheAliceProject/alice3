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
package org.alice.ide.ast.type.merge.core;

import org.lgna.project.ast.UserMethod;
import org.lgna.project.code.CodeGenerator;

/**
 * @author Dennis Cosgrove
 */
public class MergeUtilities {
	private MergeUtilities() {
		throw new AssertionError();
	}

	public static org.lgna.project.ast.NamedUserType findMatchingTypeInExistingTypes( org.lgna.project.ast.NamedUserType srcType, java.util.Collection<org.lgna.project.ast.NamedUserType> dstTypes ) {
		for( org.lgna.project.ast.NamedUserType dstType : dstTypes ) {
			//todo
			if( dstType.getName().contentEquals( srcType.getName() ) ) {
				return dstType;
			}
		}
		return null;
	}

	public static org.lgna.project.ast.NamedUserType findMatchingTypeInExistingTypes( org.lgna.project.ast.NamedUserType type ) {
		org.lgna.project.Project project = org.alice.ide.ProjectStack.peekProject();
		if( project != null ) {
			java.util.Set<org.lgna.project.ast.NamedUserType> dstTypes = project.getNamedUserTypes();
			return findMatchingTypeInExistingTypes( type, dstTypes );
		}
		return null;
	}

	public static org.lgna.project.ast.UserMethod findMethodWithMatchingName( org.lgna.project.ast.UserMethod srcMethod, org.lgna.project.ast.NamedUserType dstType ) {
		for( org.lgna.project.ast.UserMethod dstMethod : dstType.methods ) {
			if( dstMethod.getName().contentEquals( srcMethod.getName() ) ) {
				return dstMethod;
			}
		}
		return null;
	}

	private static org.lgna.project.ast.JavaCodeGenerator createJavaCodeGeneratorForEquivalence() {
		return new org.lgna.project.ast.JavaCodeGenerator.Builder()
				.isLambdaSupported( true ) //don't care
				.build();
	}

	public static boolean isHeaderEquivalent( UserMethod a, UserMethod b ) {
		String aText = a.generateHeaderJavaCode( createJavaCodeGeneratorForEquivalence() );
		String bText = b.generateHeaderJavaCode( createJavaCodeGeneratorForEquivalence() );
		return aText.contentEquals( bText );
	}

	public static boolean isEquivalent( CodeGenerator a, CodeGenerator b ) {
		String aText = a.generateCode( createJavaCodeGeneratorForEquivalence() );
		String bText = b.generateCode( createJavaCodeGeneratorForEquivalence() );
		return aText.contentEquals( bText );
	}

	public static boolean isValueTypeEquivalent( org.lgna.project.ast.UserField a, org.lgna.project.ast.UserField b ) {
		return a.getValueType().getName().contentEquals( b.getValueType().getName() ); //todo
	}

	private static org.lgna.project.ast.AbstractMethod findMatch( org.lgna.project.ast.AbstractType<?, ?, ?> type, org.lgna.project.ast.AbstractMethod original ) {
		if( type != null ) {
			for( org.lgna.project.ast.AbstractMethod candidate : type.getDeclaredMethods() ) {
				if( candidate.getName().contentEquals( original.getName() ) ) { //todo
					return candidate;
				}
			}
			return findMatch( type.getSuperType(), original );
		} else {
			return null;
		}
	}

	private static org.lgna.project.ast.AbstractField findMatch( org.lgna.project.ast.AbstractType<?, ?, ?> type, org.lgna.project.ast.AbstractField original ) {
		if( type != null ) {
			for( org.lgna.project.ast.AbstractField candidate : type.getDeclaredFields() ) {
				if( candidate.getName().contentEquals( original.getName() ) ) { //todo
					return candidate;
				}
			}
			return findMatch( type.getSuperType(), original );
		} else {
			return null;
		}
	}

	private static boolean isAcceptableType( org.lgna.project.ast.AbstractType<?, ?, ?> declaringType, java.util.List<org.lgna.project.ast.NamedUserType> types ) {
		if( declaringType != null ) {
			if( declaringType instanceof org.lgna.project.ast.NamedUserType ) {
				org.lgna.project.ast.NamedUserType namedUserType = (org.lgna.project.ast.NamedUserType)declaringType;
				return types.contains( namedUserType );
			} else {
				return true;
			}
		} else {
			return false;
		}

	}

	private static void mendMethodInvocations( org.lgna.project.ast.Node node, java.util.List<org.lgna.project.ast.NamedUserType> types ) {
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler<org.lgna.project.ast.MethodInvocation> crawler = edu.cmu.cs.dennisc.pattern.IsInstanceCrawler.createInstance( org.lgna.project.ast.MethodInvocation.class );
		node.crawl( crawler, org.lgna.project.ast.CrawlPolicy.COMPLETE, null );

		java.util.Map<org.lgna.project.ast.AbstractMethod, org.lgna.project.ast.AbstractMethod> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
		for( org.lgna.project.ast.MethodInvocation methodInvocation : crawler.getList() ) {
			org.lgna.project.ast.AbstractMethod method = methodInvocation.method.getValue();
			if( isAcceptableType( method.getDeclaringType(), types ) ) {
				//pass
			} else {
				org.lgna.project.ast.AbstractMethod replacement;
				if( map.containsKey( method ) ) {
					replacement = map.get( method );
				} else {
					org.lgna.project.ast.Expression expression = methodInvocation.expression.getValue();
					org.lgna.project.ast.AbstractType<?, ?, ?> type = expression.getType();
					replacement = findMatch( type, method );
					if( replacement != null ) {
						map.put( method, replacement );
					}
				}
				if( replacement != null ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "mending", methodInvocation, method );
					methodInvocation.method.setValue( replacement );
					org.lgna.project.ast.AstUtilities.fixRequiredArgumentsIfNecessary( methodInvocation );
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "cannot find replacement:", method );
				}
			}
		}
	}

	private static void mendFieldAccesses( org.lgna.project.ast.Node node, java.util.List<org.lgna.project.ast.NamedUserType> types ) {
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler<org.lgna.project.ast.FieldAccess> crawler = edu.cmu.cs.dennisc.pattern.IsInstanceCrawler.createInstance( org.lgna.project.ast.FieldAccess.class );
		node.crawl( crawler, org.lgna.project.ast.CrawlPolicy.COMPLETE, null );

		java.util.Map<org.lgna.project.ast.AbstractField, org.lgna.project.ast.AbstractField> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
		for( org.lgna.project.ast.FieldAccess fieldAccess : crawler.getList() ) {
			org.lgna.project.ast.AbstractField field = fieldAccess.field.getValue();
			if( isAcceptableType( field.getDeclaringType(), types ) ) {
				//pass
			} else {
				org.lgna.project.ast.AbstractField replacement;
				if( map.containsKey( field ) ) {
					replacement = map.get( field );
				} else {
					org.lgna.project.ast.Expression expression = fieldAccess.expression.getValue();
					org.lgna.project.ast.AbstractType<?, ?, ?> type = expression.getType();
					replacement = findMatch( type, field );
					if( replacement != null ) {
						map.put( field, replacement );
					}
				}
				if( replacement != null ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "mending", fieldAccess, field );
					fieldAccess.field.setValue( replacement );
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "cannot find replacement:", field );
				}
			}
		}
	}

	public static void mendMethodInvocationsAndFieldAccesses( org.lgna.project.ast.Node node ) {
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler<org.lgna.project.ast.NamedUserType> crawler = edu.cmu.cs.dennisc.pattern.IsInstanceCrawler.createInstance( org.lgna.project.ast.NamedUserType.class );
		node.crawl( crawler, org.lgna.project.ast.CrawlPolicy.COMPLETE, null );
		for( org.lgna.project.ast.NamedUserType namedUserType : crawler.getList() ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( namedUserType );
		}
		mendMethodInvocations( node, crawler.getList() );
		mendFieldAccesses( node, crawler.getList() );
	}
}
