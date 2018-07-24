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
package org.alice.ide.croquet.models.menubar;

import edu.cmu.cs.dennisc.java.lang.ArrayUtilities;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import org.alice.ide.ProjectDocumentFrame;
import org.alice.ide.croquet.models.projecturi.ExitOperation;
import org.alice.ide.croquet.models.projecturi.ExportProjectOperation;
import org.alice.ide.croquet.models.projecturi.RevertProjectOperation;
import org.alice.ide.croquet.models.projecturi.SaveAsProjectOperation;
import org.alice.ide.croquet.models.projecturi.SaveProjectOperation;
import org.alice.ide.recentprojects.RecentProjectsMenuModel;
import org.alice.ide.resource.manager.ImportGalleryResourceOperation;
import org.lgna.croquet.MenuModel;
import org.lgna.croquet.Operation;
import org.lgna.croquet.StandardMenuItemPrepModel;
import org.lgna.croquet.StaticMenuModel;

import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class FileMenuModel extends StaticMenuModel {

	// Set to true to enable export feature that is still in development
	private static final boolean ENABLE_EXPORT = true;

	public FileMenuModel( ProjectDocumentFrame projectDocumentFrame ) {
		super( UUID.fromString( "121c8088-7297-43d4-b7b7-61416f1d4eb0" ) );
		this.projectDocumentFrame = projectDocumentFrame;
	}

	@Override
	protected StandardMenuItemPrepModel[] createModels() {
		List<StandardMenuItemPrepModel> list = Lists.newLinkedList(
				projectDocumentFrame.getNewProjectOperation().getMenuItemPrepModel(),
				projectDocumentFrame.getOpenProjectOperation().getMenuItemPrepModel(),
				MenuModel.SEPARATOR,
				RecentProjectsMenuModel.getInstance(),
				MenuModel.SEPARATOR,
				new ImportGalleryResourceOperation().getMenuItemPrepModel(),
				MenuModel.SEPARATOR,
				SaveProjectOperation.getInstance().getMenuItemPrepModel(),
				SaveAsProjectOperation.getInstance().getMenuItemPrepModel());
		if ( ENABLE_EXPORT ) {
			list.add( new ExportProjectOperation().getMenuItemPrepModel() );
		}
		list.add( MenuModel.SEPARATOR );
		list.add( RevertProjectOperation.getInstance().getMenuItemPrepModel() );

		Operation[] uploadOperations = this.projectDocumentFrame.getUploadOperations();
		if( uploadOperations.length > 0 ) {
			list.add( MenuModel.SEPARATOR );
			for( Operation operation : uploadOperations ) {
				list.add( operation.getMenuItemPrepModel() );
			}
		}
		list.add( MenuModel.SEPARATOR );
		list.add( PrintMenuModel.getInstance() );
		list.add( CaptureMenuModel.getInstance() );
		if (!SystemUtilities.isMac()) {
			list.add( MenuModel.SEPARATOR );
			list.add( ExitOperation.getInstance().getMenuItemPrepModel() );
		}
		return ArrayUtilities.createArray( list, StandardMenuItemPrepModel.class );
	}

	private final ProjectDocumentFrame projectDocumentFrame;
}
