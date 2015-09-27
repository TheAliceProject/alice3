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

package org.alice.ide.instancefactory.croquet;

/**
 * @author Dennis Cosgrove
 */
public class TypeCascadeMenuModel extends org.lgna.croquet.CascadeMenuModel<org.alice.ide.instancefactory.InstanceFactory> {
	private final org.alice.ide.ast.fieldtree.TypeNode typeNode;
	private final org.alice.ide.ApiConfigurationManager apiConfigurationManager;

	public TypeCascadeMenuModel( org.alice.ide.ast.fieldtree.TypeNode typeNode, org.alice.ide.ApiConfigurationManager apiConfigurationManager ) {
		super( java.util.UUID.fromString( "2c7247f3-d788-4155-9676-97f643d15f62" ) );
		this.typeNode = typeNode;
		this.apiConfigurationManager = apiConfigurationManager;
	}

	@Override
	protected boolean isBackedByIconProxy() {
		return false;
	}

	@Override
	public javax.swing.Icon getMenuItemIcon( org.lgna.croquet.imp.cascade.ItemNode<? super org.alice.ide.instancefactory.InstanceFactory, org.alice.ide.instancefactory.InstanceFactory> node ) {
		return org.alice.ide.common.TypeIcon.getInstance( this.typeNode.getDeclaration() );
	}

	@Override
	protected void updateBlankChildren( java.util.List<org.lgna.croquet.CascadeBlankChild> blankChildren, org.lgna.croquet.imp.cascade.BlankNode<org.alice.ide.instancefactory.InstanceFactory> blankNode ) {
		for( org.alice.ide.ast.fieldtree.FieldNode fieldNode : this.typeNode.getFieldNodes() ) {
			blankChildren.add( InstanceFactoryState.createFillInMenuComboIfNecessaryForField( this.apiConfigurationManager, fieldNode.getDeclaration() ) );
		}
		for( org.alice.ide.ast.fieldtree.TypeNode typeNode : this.typeNode.getTypeNodes() ) {
			blankChildren.add( new TypeCascadeMenuModel( typeNode, this.apiConfigurationManager ) );
		}
	}
}
