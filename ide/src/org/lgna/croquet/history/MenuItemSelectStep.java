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

package org.lgna.croquet.history;

/**
 * @author Dennis Cosgrove
 */
public class MenuItemSelectStep extends PrepStep< org.lgna.croquet.MenuItemPrepModel > {
	public static MenuItemSelectStep createAndAddToTransaction( Transaction parent, org.lgna.croquet.MenuBarComposite menuBarComposite, org.lgna.croquet.MenuItemPrepModel[] menuItemPrepModels, org.lgna.croquet.triggers.ChangeEventTrigger trigger ) {
		return new MenuItemSelectStep( parent, menuBarComposite, menuItemPrepModels, trigger );
	}
	private final org.lgna.croquet.resolvers.Resolver< org.lgna.croquet.MenuBarComposite > menuBarCompositeResolver;
	private final org.lgna.croquet.MenuItemPrepModel[] menuItemPrepModels;
	private MenuItemSelectStep( Transaction parent, org.lgna.croquet.MenuBarComposite menuBarComposite, org.lgna.croquet.MenuItemPrepModel[] menuItemPrepModels, org.lgna.croquet.triggers.ChangeEventTrigger trigger ) {
		super( parent, menuItemPrepModels[ menuItemPrepModels.length-1 ], trigger );
		if( menuBarComposite != null ) {
			this.menuBarCompositeResolver = menuBarComposite.getResolver();
		} else {
			this.menuBarCompositeResolver = new org.lgna.croquet.resolvers.NullResolver< org.lgna.croquet.MenuBarComposite >();
		}
		this.menuItemPrepModels = menuItemPrepModels;
	}
	public MenuItemSelectStep( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
		this.menuBarCompositeResolver = binaryDecoder.decodeBinaryEncodableAndDecodable();
		this.menuItemPrepModels = new org.lgna.croquet.MenuItemPrepModel[ 0 ];
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( this.menuItemPrepModels );
	}
	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		binaryEncoder.encode( this.menuBarCompositeResolver );
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( this.menuItemPrepModels );
	}
	public org.lgna.croquet.MenuBarComposite getMenuBarComposite() {
		return this.menuBarCompositeResolver.getResolved();
	}
	public org.lgna.croquet.MenuItemPrepModel[] getMenuItemPrepModels() {
		return this.menuItemPrepModels;
	}
}
