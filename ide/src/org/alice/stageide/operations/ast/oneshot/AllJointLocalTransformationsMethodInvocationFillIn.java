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
package org.alice.stageide.operations.ast.oneshot;

/**
 * @author Dennis Cosgrove
 */
public class AllJointLocalTransformationsMethodInvocationFillIn extends MethodInvocationFillIn {
	private static edu.cmu.cs.dennisc.map.MapToMap<org.alice.ide.instancefactory.InstanceFactory, org.lgna.project.ast.AbstractMethod, AllJointLocalTransformationsMethodInvocationFillIn> map = edu.cmu.cs.dennisc.map.MapToMap.newInstance();

	public static AllJointLocalTransformationsMethodInvocationFillIn getInstance( org.alice.ide.instancefactory.InstanceFactory instanceFactory, org.lgna.project.ast.AbstractMethod method ) {
		synchronized( map ) {
			AllJointLocalTransformationsMethodInvocationFillIn rv = map.get( instanceFactory, method );
			if( rv != null ) {
				//pass
			} else {
				rv = new AllJointLocalTransformationsMethodInvocationFillIn( instanceFactory, method );
				map.put( instanceFactory, method, rv );
			}
			return rv;
		}
	}

	public static AllJointLocalTransformationsMethodInvocationFillIn getInstance( org.alice.ide.instancefactory.InstanceFactory instanceFactory, org.lgna.project.ast.AbstractType<?, ?, ?> type, String methodName, Class<?>... parameterClses ) {
		org.lgna.project.ast.AbstractMethod method = type.getDeclaredMethod( methodName, parameterClses );
		assert method != null : methodName;
		return getInstance( instanceFactory, method );
	}

	public static AllJointLocalTransformationsMethodInvocationFillIn getInstance( org.alice.ide.instancefactory.InstanceFactory instanceFactory, Class<?> cls, String methodName, Class<?>... parameterClses ) {
		return getInstance( instanceFactory, org.lgna.project.ast.JavaType.getInstance( cls ), methodName, parameterClses );
	}

	private AllJointLocalTransformationsMethodInvocationFillIn( org.alice.ide.instancefactory.InstanceFactory instanceFactory, org.lgna.project.ast.AbstractMethod method ) {
		super( java.util.UUID.fromString( "1931250e-3f00-4d95-85e9-f8c06922eb17" ), instanceFactory, method );
	}

	@Override
	protected org.alice.stageide.operations.ast.oneshot.MethodInvocationEditFactory createMethodInvocationEditFactory( org.alice.ide.instancefactory.InstanceFactory instanceFactory, org.lgna.project.ast.AbstractMethod method, org.lgna.project.ast.Expression[] argumentExpressions ) {
		return new AllJointLocalTransformationsMethodInvocationEditFactory( instanceFactory, method, argumentExpressions );
	}
}
