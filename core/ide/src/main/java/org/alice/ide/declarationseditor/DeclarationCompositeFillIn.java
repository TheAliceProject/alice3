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
package org.alice.ide.declarationseditor;

/**
 * @author Dennis Cosgrove
 */
public class DeclarationCompositeFillIn extends org.lgna.croquet.ImmutableCascadeFillIn<DeclarationComposite, Void> {
	private static final java.util.Map<DeclarationComposite, DeclarationCompositeFillIn> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	public static synchronized DeclarationCompositeFillIn getInstance( DeclarationComposite declarationComposite ) {
		DeclarationCompositeFillIn rv = map.get( declarationComposite );
		if( rv != null ) {
			//pass
		} else {
			rv = new DeclarationCompositeFillIn( declarationComposite );
			map.put( declarationComposite, rv );
		}
		return rv;
	}

	private final DeclarationComposite declarationComposite;

	public DeclarationCompositeFillIn( DeclarationComposite declarationComposite ) {
		super( java.util.UUID.fromString( "7d731332-2dd6-4861-b08e-386c50c7a580" ) );
		this.declarationComposite = declarationComposite;
	}

	@Override
	public org.alice.ide.declarationseditor.DeclarationComposite createValue( org.lgna.croquet.imp.cascade.ItemNode<? super org.alice.ide.declarationseditor.DeclarationComposite, Void> node, org.lgna.croquet.history.TransactionHistory transactionHistory ) {
		return this.declarationComposite;
	}

	@Override
	public org.alice.ide.declarationseditor.DeclarationComposite getTransientValue( org.lgna.croquet.imp.cascade.ItemNode<? super org.alice.ide.declarationseditor.DeclarationComposite, Void> node ) {
		return this.declarationComposite;
	}

	@Override
	protected javax.swing.JComponent createMenuItemIconProxy( org.lgna.croquet.imp.cascade.ItemNode<? super org.alice.ide.declarationseditor.DeclarationComposite, Void> node ) {
		throw new AssertionError();
	}

	@Override
	public String getMenuItemText( org.lgna.croquet.imp.cascade.ItemNode<? super DeclarationComposite, Void> node ) {
		org.lgna.project.ast.AbstractDeclaration declaration = this.declarationComposite.getDeclaration();
		if( declaration instanceof org.lgna.project.ast.AbstractType<?, ?, ?> ) {
			org.lgna.project.ast.AbstractType<?, ?, ?> type = (org.lgna.project.ast.AbstractType<?, ?, ?>)declaration;
			return null;
		} else if( declaration instanceof org.lgna.project.ast.AbstractCode ) {
			org.lgna.project.ast.AbstractCode code = (org.lgna.project.ast.AbstractCode)declaration;
			return code.getName();
		} else {
			return null;
		}
	}

	@Override
	public javax.swing.Icon getMenuItemIcon( org.lgna.croquet.imp.cascade.ItemNode<? super DeclarationComposite, Void> node ) {
		org.lgna.project.ast.AbstractDeclaration declaration = this.declarationComposite.getDeclaration();
		if( declaration instanceof org.lgna.project.ast.AbstractType<?, ?, ?> ) {
			org.lgna.project.ast.AbstractType<?, ?, ?> type = (org.lgna.project.ast.AbstractType<?, ?, ?>)declaration;
			return org.alice.ide.common.TypeIcon.getInstance( type );
		} else if( declaration instanceof org.lgna.project.ast.AbstractCode ) {
			org.lgna.project.ast.AbstractCode code = (org.lgna.project.ast.AbstractCode)declaration;
			return org.alice.ide.common.TypeIcon.getInstance( code.getDeclaringType() );
		} else {
			return null;
		}
	}
}
