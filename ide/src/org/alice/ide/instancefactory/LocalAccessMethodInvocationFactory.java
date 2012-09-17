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

package org.alice.ide.instancefactory;

/**
 * @author Dennis Cosgrove
 */
public class LocalAccessMethodInvocationFactory extends MethodInvocationFactory {
	private static edu.cmu.cs.dennisc.map.MapToMap<org.lgna.project.ast.UserLocal, org.lgna.project.ast.AbstractMethod, LocalAccessMethodInvocationFactory> mapToMap = edu.cmu.cs.dennisc.map.MapToMap.newInstance();

	public static synchronized LocalAccessMethodInvocationFactory getInstance( org.lgna.project.ast.UserLocal local, org.lgna.project.ast.AbstractMethod method ) {
		assert local != null;
		LocalAccessMethodInvocationFactory rv = mapToMap.get( local, method );
		if( rv != null ) {
			//pass
		} else {
			rv = new LocalAccessMethodInvocationFactory( local, method );
			mapToMap.put( local, method, rv );
		}
		return rv;
	}

	private final org.lgna.project.ast.UserLocal local;

	private LocalAccessMethodInvocationFactory( org.lgna.project.ast.UserLocal local, org.lgna.project.ast.AbstractMethod method ) {
		super( method, local.name );
		this.local = local;
	}

	@Override
	protected org.lgna.project.ast.AbstractType<?, ?, ?> getValidInstanceType( org.lgna.project.ast.AbstractType<?, ?, ?> type, org.lgna.project.ast.AbstractCode code ) {
		if( code != null ) {
			if( this.local.getFirstAncestorAssignableTo( org.lgna.project.ast.AbstractCode.class ) == code ) {
				return this.local.getValueType();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	protected org.lgna.croquet.resolvers.Resolver<LocalAccessMethodInvocationFactory> createResolver() {
		return new org.alice.ide.croquet.resolvers.NodeStaticGetInstanceKeyedResolver<LocalAccessMethodInvocationFactory>(
				this,
				new Class[] { org.lgna.project.ast.UserParameter.class, org.lgna.project.ast.AbstractMethod.class },
				new Object[] { this.local, this.getMethod() } );
	}

	public org.lgna.project.ast.UserLocal getLocal() {
		return this.local;
	}

	@Override
	protected org.lgna.project.ast.Expression createTransientExpressionForMethodInvocation() {
		//todo?
		return new org.lgna.project.ast.LocalAccess( this.local );
	}

	@Override
	protected org.lgna.project.ast.Expression createExpressionForMethodInvocation() {
		return new org.lgna.project.ast.LocalAccess( this.local );
	}

	@Override
	protected java.lang.StringBuilder addAccessRepr( java.lang.StringBuilder rv ) {
		rv.append( "this." );
		rv.append( this.local.getName() );
		return rv;
	}
}
