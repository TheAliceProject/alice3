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
public abstract class JointedTypeMenuModel extends org.lgna.croquet.CascadeMenuModel<org.alice.ide.instancefactory.InstanceFactory> {
	private final java.util.List<org.alice.stageide.ast.JointedTypeInfo> jointedTypeInfos;
	private final int index;

	public JointedTypeMenuModel( java.util.UUID id, java.util.List<org.alice.stageide.ast.JointedTypeInfo> jointedTypeInfos, int index ) {
		super( id );
		this.jointedTypeInfos = jointedTypeInfos;
		this.index = index;
	}

	protected abstract org.lgna.croquet.CascadeFillIn<org.alice.ide.instancefactory.InstanceFactory, ?> getFillIn( org.lgna.project.ast.AbstractMethod method );

	protected abstract JointedTypeMenuModel getInstance( java.util.List<org.alice.stageide.ast.JointedTypeInfo> jointedTypeInfos, int index );

	@Override
	protected final void updateBlankChildren( java.util.List<org.lgna.croquet.CascadeBlankChild> blankChildren, org.lgna.croquet.imp.cascade.BlankNode<org.alice.ide.instancefactory.InstanceFactory> blankNode ) {
		org.alice.stageide.ast.JointedTypeInfo info = jointedTypeInfos.get( this.index );
		org.alice.stageide.cascade.JointedModelTypeSeparator separator = org.alice.stageide.cascade.JointedModelTypeSeparator.getInstance( info.getType() );
		org.lgna.croquet.CascadeBlankChild child;
		if( jointedTypeInfos.size() > ( this.index + 1 ) ) {
			child = new org.lgna.croquet.CascadeItemMenuCombo( separator, this.getInstance( this.jointedTypeInfos, this.index + 1 ) );
		} else {
			child = separator;
		}
		blankChildren.add( child );

		org.alice.stageide.joint.JointsSubMenu<org.alice.ide.instancefactory.InstanceFactory>[] subMenus = org.alice.stageide.joint.JointsSubMenuManager.getSubMenusForType( info.getType() );

		for( org.lgna.project.ast.AbstractMethod method : info.getJointGetters() ) {
			org.lgna.croquet.CascadeFillIn<org.alice.ide.instancefactory.InstanceFactory, ?> fillIn = this.getFillIn( method );
			if( fillIn != null ) {
				boolean isConsumed = false;
				for( org.alice.stageide.joint.JointsSubMenu<org.alice.ide.instancefactory.InstanceFactory> subMenu : subMenus ) {
					if( subMenu.consumeIfAppropriate( method, fillIn ) ) {
						isConsumed = true;
						break;
					}
				}
				if( isConsumed ) {
					//pass
				} else {
					blankChildren.add( fillIn );
				}
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.info( "no fillIn for", method );
			}
		}
		for( org.alice.stageide.joint.JointsSubMenu<org.alice.ide.instancefactory.InstanceFactory> subMenu : subMenus ) {
			blankChildren.add( subMenu );
		}
	}
}
