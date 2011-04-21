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

package ecard;

import java.awt.event.WindowEvent;
import java.util.EventObject;

import edu.cmu.cs.dennisc.croquet.Component;
import edu.cmu.cs.dennisc.croquet.DropReceptor;
import edu.cmu.cs.dennisc.croquet.DropSite;

/**
 * @author Dennis Cosgrove
 */
public class ECardApplication extends edu.cmu.cs.dennisc.croquet.Application {
	public static final edu.cmu.cs.dennisc.croquet.Group HISTORY_GROUP = edu.cmu.cs.dennisc.croquet.Group.getInstance( java.util.UUID.fromString( "303e94ca-64ef-4e3a-b95c-038468c68438" ), "HISTORY_GROUP" );
	public static final edu.cmu.cs.dennisc.croquet.Group URI_GROUP = edu.cmu.cs.dennisc.croquet.Group.getInstance( java.util.UUID.fromString( "79bf8341-61a4-4395-9469-0448e66d9ac6" ), "URI_GROUP" );
	
	protected ECardApplication() {
	}
	
	private static ECardApplication singleton;
	public static ECardApplication getSingleton() {
		if (singleton == null) {
			singleton = new ECardApplication();
		}
		
		return ECardApplication.singleton;
	}
	
	@Override
	public void initialize(java.lang.String[] args) {
		// TODO Auto-generated method stub
		super.initialize(args);
		
		getFrame().setMenuBarModel( org.alice.ide.croquet.models.MenuBarModel.getInstance() );	
	}
	
	@Override
	protected Component<?> createContentPane() {
		edu.cmu.cs.dennisc.croquet.BorderPanel rv = new edu.cmu.cs.dennisc.croquet.BorderPanel();
		//rv.addComponent( this.root, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER );

		return rv;
	}
	@Override
	public DropReceptor getDropReceptor(DropSite dropSite) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected void handleWindowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void handleAbout(EventObject e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void handlePreferences(EventObject e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void handleQuit(EventObject e) {
		// TODO Auto-generated method stub
		
	}

}
