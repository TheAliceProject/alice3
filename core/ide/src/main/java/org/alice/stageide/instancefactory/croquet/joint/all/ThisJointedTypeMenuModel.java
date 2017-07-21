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

package org.alice.stageide.instancefactory.croquet.joint.all;

/**
 * @author Dennis Cosgrove
 */
public class ThisJointedTypeMenuModel extends JointedTypeMenuModel {
	private static edu.cmu.cs.dennisc.map.MapToMap<org.lgna.project.ast.AbstractType<?, ?, ?>, Integer, ThisJointedTypeMenuModel> mapToMap = edu.cmu.cs.dennisc.map.MapToMap.newInstance();

	public static ThisJointedTypeMenuModel getInstance( org.lgna.project.ast.AbstractType<?, ?, ?> value ) {
		java.util.List<org.alice.stageide.ast.JointedTypeInfo> jointedTypeInfos = org.alice.stageide.ast.JointedTypeInfo.getInstances( value );
		return getInstance( value, jointedTypeInfos, 0 );
	}

	private static ThisJointedTypeMenuModel getInstance( org.lgna.project.ast.AbstractType<?, ?, ?> value, java.util.List<org.alice.stageide.ast.JointedTypeInfo> jointedTypeInfos, int index ) {
		//todo
		synchronized( mapToMap ) {
			ThisJointedTypeMenuModel rv = mapToMap.get( value, index );
			if( rv != null ) {
				//pass
			} else {
				rv = new ThisJointedTypeMenuModel( value, jointedTypeInfos, index );
				mapToMap.put( value, index, rv );
			}
			return rv;
		}
	}

	private final org.lgna.project.ast.AbstractType<?, ?, ?> type;

	private ThisJointedTypeMenuModel( org.lgna.project.ast.AbstractType<?, ?, ?> type, java.util.List<org.alice.stageide.ast.JointedTypeInfo> jointedTypeInfos, int index ) {
		super( java.util.UUID.fromString( "f6e1f5de-56d7-45ea-a9b3-f8585cf2d01c" ), jointedTypeInfos, index );
		this.type = type;
	}

	@Override
	protected org.alice.stageide.instancefactory.croquet.joint.all.JointedTypeMenuModel getInstance( java.util.List<org.alice.stageide.ast.JointedTypeInfo> jointedTypeInfos, int index ) {
		return getInstance( this.type, jointedTypeInfos, index );
	}

	@Override
	protected org.lgna.croquet.CascadeFillIn<org.alice.ide.instancefactory.InstanceFactory, ?> getFillIn( org.lgna.project.ast.AbstractMethod method ) {
		//todo: use this.type?
		org.alice.ide.instancefactory.InstanceFactory instanceFactory = org.alice.ide.instancefactory.ThisMethodInvocationFactory.getInstance( method );
		if( instanceFactory != null ) {
			return org.alice.ide.instancefactory.croquet.InstanceFactoryFillIn.getInstance( instanceFactory );
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.info( "no instance factory for", method );
			return null;
		}
	}
}
