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
package org.alice.ide.croquet.models.ast;

//class CheckMarkIcon implements javax.swing.Icon {
//	@Override
//	public int getIconWidth() {
//		return 16;
//	}
//	@Override
//	public int getIconHeight() {
//		return 16;
//	}
//	@Override
//	public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
//		g.setColor( java.awt.Color.RED );
//		g.fillRect( x, y, 16, 16 );
//	}
//}
/**
 * @author Dennis Cosgrove
 */
public class EditMethodOperation extends EditCodeOperation<org.lgna.project.ast.UserMethod> {
	private static enum IsLocalizedToEdit {
		TRUE,
		FALSE;
		private final java.util.Map<org.lgna.project.ast.UserMethod, EditMethodOperation> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	}

	//todo

	public static final javax.swing.Icon TYPE_ICON = new org.alice.ide.icons.TabIcon( new java.awt.Dimension( 16, 16 ), new java.awt.Color( 0xe2ba84 ) );
	private static final javax.swing.Icon PROCEDURE_ICON = new org.alice.ide.icons.TabIcon( new java.awt.Dimension( 16, 16 ), new java.awt.Color( 0xb2b7d9 ) );
	private static final javax.swing.Icon FUNCTION_ICON = new org.alice.ide.icons.TabIcon( new java.awt.Dimension( 16, 16 ), new java.awt.Color( 0xb0c9a4 ) );

	public static synchronized EditMethodOperation getInstance( org.lgna.project.ast.UserMethod method, IsLocalizedToEdit isLocalizedToEdit ) {
		EditMethodOperation rv = isLocalizedToEdit.map.get( method );
		if( rv != null ) {
			//pass
		} else {
			rv = new EditMethodOperation( method, isLocalizedToEdit );
			isLocalizedToEdit.map.put( method, rv );
		}
		return rv;
	}

	public static EditMethodOperation getLocalizedToEditInstance( org.lgna.project.ast.UserMethod method ) {
		return getInstance( method, IsLocalizedToEdit.TRUE );
	}

	public static EditMethodOperation getLocalizedToNameInstance( org.lgna.project.ast.UserMethod method ) {
		return getInstance( method, IsLocalizedToEdit.FALSE );
	}

	private final IsLocalizedToEdit isLocalizedToEdit;

	private EditMethodOperation( org.lgna.project.ast.UserMethod method, IsLocalizedToEdit isLocalizedToEdit ) {
		super( java.util.UUID.fromString( "4a6e51f7-630a-4f36-b7db-5fa37c62eb54" ), method );
		this.isLocalizedToEdit = isLocalizedToEdit;
	}

	@Override
	protected void localize() {
		super.localize();
		if( this.isLocalizedToEdit == IsLocalizedToEdit.TRUE ) {
			//pass
		} else {
			this.setName( this.getCode().getName() );
		}
		this.setSmallIcon( this.getCode().isProcedure() ? PROCEDURE_ICON : FUNCTION_ICON );
	}

	//	private static CheckMarkIcon checkMarkIcon = new CheckMarkIcon();

	@Override
	protected org.alice.ide.croquet.resolvers.NodeStaticGetInstanceKeyedResolver<EditMethodOperation> createResolver() {
		return new org.alice.ide.croquet.resolvers.NodeStaticGetInstanceKeyedResolver<EditMethodOperation>( this, org.lgna.project.ast.UserMethod.class, this.getCode() );
	}
}
