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

package org.alice.ide.croquet.models.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
public abstract class DeclarationGalleryNode<D extends org.lgna.project.ast.AbstractDeclaration> extends GalleryNode {
	public static DeclarationGalleryNode<?> getDeclarationNodeInstance( org.lgna.project.ast.AbstractDeclaration declaration ) {
		if( declaration instanceof org.lgna.project.ast.AbstractType<?, ?, ?> ) {
			org.lgna.project.ast.AbstractType<?, ?, ?> type = (org.lgna.project.ast.AbstractType<?, ?, ?>)declaration;
			//			org.lgna.project.ast.JavaField enumConstantField = org.alice.ide.typemanager.TypeManager.getEnumConstantFieldIfOneAndOnly( type );
			//			if( enumConstantField != null ) {
			//				return FieldGalleryNode.getInstance( enumConstantField );
			//			} else {
			return ArgumentTypeGalleryNode.getInstance( type );
			//			}
		} else if( declaration instanceof org.lgna.project.ast.AbstractField ) {
			org.lgna.project.ast.AbstractField field = (org.lgna.project.ast.AbstractField)declaration;
			return FieldGalleryNode.getInstance( field );
		} else if( declaration == null ) {
			return null;
		} else {
			throw new AssertionError();
		}
	}

	private final D declaration;

	public DeclarationGalleryNode( java.util.UUID id, D declaration ) {
		super( id );
		this.declaration = declaration;
	}

	public D getDeclaration() {
		return this.declaration;
	}

	protected abstract void appendClassName( StringBuilder sb );

	@Override
	public final String getText() {
		StringBuilder sb = new StringBuilder();
		sb.append( "<html>class <strong>" );
		this.appendClassName( sb );
		sb.append( "</strong></html>" );
		return sb.toString();
	}

	@Override
	public String getSearchText() {
		return this.declaration.getName();
	}
}
