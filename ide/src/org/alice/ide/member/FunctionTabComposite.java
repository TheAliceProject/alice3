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
package org.alice.ide.member;

/**
 * @author Dennis Cosgrove
 */
public final class FunctionTabComposite extends MemberTabComposite {
	private static class SingletonHolder {
		private static FunctionTabComposite instance = new FunctionTabComposite();
	}

	public static FunctionTabComposite getInstance() {
		return SingletonHolder.instance;
	}

	private FunctionTabComposite() {
		super( java.util.UUID.fromString( "a2a01f20-37ba-468f-b35b-2b6a2ed94ac7" ) );
	}

	@Override
	public java.util.List<MethodsSubComposite> getSubComposites() {
		java.util.Map<org.lgna.project.ast.AbstractType<?, ?, ?>, java.util.List<org.lgna.project.ast.AbstractMethod>> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

		org.alice.ide.instancefactory.InstanceFactory instanceFactory = org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().getValue();
		if( instanceFactory != null ) {
			org.lgna.project.ast.AbstractType<?, ?, ?> type = instanceFactory.getValueType();
			while( type != null ) {
				for( org.lgna.project.ast.AbstractMethod method : type.getDeclaredMethods() ) {
					org.lgna.project.ast.AbstractType<?, ?, ?> returnType = method.getReturnType();
					if( returnType == org.lgna.project.ast.JavaType.VOID_TYPE ) {
						//pass
					} else {
						if( isInclusionDesired( method ) ) {
							java.util.List<org.lgna.project.ast.AbstractMethod> list = map.get( returnType );
							if( list != null ) {
								//pass
							} else {
								list = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
								map.put( returnType, list );
							}
							list.add( method );
						}
					}
				}
				if( type.isFollowToSuperClassDesired() ) {
					type = type.getSuperType();
				} else {
					break;
				}
			}
		}

		java.util.List<org.lgna.project.ast.AbstractType<?, ?, ?>> types = edu.cmu.cs.dennisc.java.util.Collections.newArrayList( map.keySet() );
		java.util.Collections.sort( types, org.alice.ide.IDE.getActiveInstance().getApiConfigurationManager().getTypeComparator() );
		java.util.List<MethodsSubComposite> rv = edu.cmu.cs.dennisc.java.util.Collections.newArrayListWithMinimumCapacity( types.size() );
		for( org.lgna.project.ast.AbstractType<?, ?, ?> type : types ) {
			FunctionsOfReturnTypeSubComposite subComposite = FunctionsOfReturnTypeSubComposite.getInstance( type );
			subComposite.setMethods( map.get( type ) );
			rv.add( subComposite );
		}
		return rv;
	}

	@Override
	protected org.alice.ide.member.views.MemberTabView createView() {
		return new org.alice.ide.member.views.FunctionTabView( this );
	}
	//todo
	//	@Override
	//	public void handlePreActivation() {
	//		super.handlePreActivation();
	//		org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().addAndInvokeValueListener( this.instanceFactorySelectionObserver );
	//	}
	//	@Override
	//	public void handlePostDeactivation() {
	//		org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().removeValueListener( this.instanceFactorySelectionObserver );
	//		super.handlePostDeactivation();
	//	}
}
