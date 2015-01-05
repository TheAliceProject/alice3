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
package org.alice.ide.croquet.models.menubar;

/**
 * @author Dennis Cosgrove
 */
public class WindowMenuModel extends org.lgna.croquet.StaticMenuModel {
	private static java.util.List<org.lgna.croquet.StandardMenuItemPrepModel> createModels( org.alice.stageide.perspectives.PerspectiveState perspectiveState ) {
		java.util.List<org.lgna.croquet.StandardMenuItemPrepModel> rv = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		if( perspectiveState != null ) {
			rv.add( perspectiveState.getMenuModel() );
			rv.add( org.lgna.croquet.MenuModel.SEPARATOR );
		}
		rv.add( org.alice.ide.croquet.models.history.ProjectHistoryComposite.getInstance().getIsFrameShowingState().getMenuItemPrepModel() );
		rv.add( org.alice.ide.IdeApp.INSTANCE.getMemoryUsageFrameIsShowingState().getMenuItemPrepModel() );
		rv.add( org.lgna.croquet.MenuModel.SEPARATOR );
		rv.add( PreferencesMenuModel.getInstance() );
		rv.add( org.lgna.croquet.MenuModel.SEPARATOR );
		rv.add( org.alice.ide.IdeApp.INSTANCE.getContributorMenuModel() );
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( "org.alice.ide.internalTesting" ) ) {
			rv.add( org.lgna.croquet.MenuModel.SEPARATOR );
			rv.add( InternalTestingMenuModel.getInstance() );
		}
		return rv;
	}

	public WindowMenuModel( org.alice.ide.ProjectDocumentFrame projectDocumentFrame ) {
		super( java.util.UUID.fromString( "58a7297b-a5f8-499a-abd1-db6fca4083c8" ) );
		this.projectDocumentFrame = projectDocumentFrame;
	}

	@Override
	protected org.lgna.croquet.StandardMenuItemPrepModel[] createModels() {
		org.alice.stageide.perspectives.PerspectiveState perspectiveState = this.projectDocumentFrame != null ? this.projectDocumentFrame.getPerspectiveState() : null;
		return edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( createModels( perspectiveState ), org.lgna.croquet.StandardMenuItemPrepModel.class );
	}

	private final org.alice.ide.ProjectDocumentFrame projectDocumentFrame;
}
