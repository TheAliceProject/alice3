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
package org.alice.ide.editorstabbedpane;

class TypeMenuModel extends edu.cmu.cs.dennisc.croquet.DefaultMenuModel {
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type;

	public TypeMenuModel( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( java.util.UUID.fromString( "90a5b2c2-1182-4c05-ac90-a1dc405a7a2f" ) );
		this.type = type;
		//this.setName( type.getName() );
		this.setSmallIcon( org.alice.ide.common.TypeIcon.getInstance( type ) );
	}
	
	@Override
	protected void handleMenuSelected( javax.swing.event.MenuEvent e, edu.cmu.cs.dennisc.croquet.Menu< edu.cmu.cs.dennisc.croquet.MenuModel > menu ) {
		super.handleMenuSelected( e, menu );
		
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: handleMenuSelected" );
		
		edu.cmu.cs.dennisc.croquet.Application.addMenuElement( menu, new org.alice.ide.operations.ast.EditTypeOperation( this.type ) );
		edu.cmu.cs.dennisc.croquet.Application.addMenuElement( menu, new org.alice.ide.operations.ast.RenameTypeOperation( this.type ) );
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		if( ide.isInstanceCreationAllowableFor( this.type ) ) {
			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType = ide.getSceneType();
			edu.cmu.cs.dennisc.croquet.Application.addMenuElement( menu, new org.alice.ide.operations.ast.DeclareFieldOfPredeterminedTypeOperation( ownerType, this.type ) );
		}
		edu.cmu.cs.dennisc.croquet.Application.addMenuElement( menu, new org.alice.ide.operations.file.SaveAsTypeOperation( this.type ) );
		menu.addSeparator();
		edu.cmu.cs.dennisc.croquet.Application.addMenuElement( menu, new EditConstructorOperation( this.type.getDeclaredConstructor() ) );
		menu.addSeparator();
		edu.cmu.cs.dennisc.croquet.Application.addMenuElement( menu, org.alice.ide.operations.ast.DeclareProcedureOperation.getInstance( this.type ) );
		for( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method : this.type.methods ) {
			if( method.isProcedure() ) {
				edu.cmu.cs.dennisc.croquet.Application.addMenuElement( menu, new EditMethodOperation( method ) );
			}
		}
		menu.addSeparator();
		for( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method : this.type.methods ) {
			if( method.isFunction() ) {
				edu.cmu.cs.dennisc.croquet.Application.addMenuElement( menu, new EditMethodOperation( method ) );
			}
		}
		edu.cmu.cs.dennisc.croquet.Application.addMenuElement( menu, org.alice.ide.operations.ast.DeclareFunctionOperation.getInstance( this.type ) );
		menu.addSeparator();
		edu.cmu.cs.dennisc.croquet.Application.addMenuElement( menu, org.alice.ide.operations.ast.DeclareFieldOperation.getInstance( this.type ) );
		for( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field : this.type.fields ) {
			edu.cmu.cs.dennisc.croquet.Application.addMenuElement( menu, new org.alice.ide.operations.ast.EditFieldOperation( field ) );
		}
	}
	@Override
	protected void handleMenuDeselected( javax.swing.event.MenuEvent e, edu.cmu.cs.dennisc.croquet.Menu< edu.cmu.cs.dennisc.croquet.MenuModel > menu ) {
		menu.getAwtComponent().removeAll();
		super.handleMenuDeselected( e, menu );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class RootOperation extends edu.cmu.cs.dennisc.croquet.PopupMenuOperation {
	public RootOperation() {
		super( java.util.UUID.fromString( "259dfcc5-dd20-4890-8104-a34a075734d0" ) );
	}
	
	@Override
	protected void handlePopupMenuWillBecomeVisible( edu.cmu.cs.dennisc.croquet.PopupMenu popupMenu, javax.swing.event.PopupMenuEvent e ) {
		super.handlePopupMenuWillBecomeVisible( popupMenu, e );
		edu.cmu.cs.dennisc.alice.Project project = org.alice.ide.IDE.getSingleton().getProject();
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice >( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice.class );
		final edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> programType = project.getProgramType();
		programType.crawl( crawler, true );
		for( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type : crawler.getList() ) {
			if( type == programType ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.croquet.Application.addMenuElement( popupMenu, new TypeMenuModel( type ) );
			}
		}
	}
	@Override
	protected void handlePopupMenuWillBecomeInvisible( edu.cmu.cs.dennisc.croquet.PopupMenu popupMenu, javax.swing.event.PopupMenuEvent e ) {
		popupMenu.getAwtComponent().removeAll();
		super.handlePopupMenuWillBecomeInvisible( popupMenu, e );
	}
}
