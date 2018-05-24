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

package org.alice.ide.resource.manager;

import edu.cmu.cs.dennisc.java.lang.ArrayUtilities;
import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import org.alice.ide.croquet.codecs.ResourceCodec;
import org.lgna.common.Resource;
import org.lgna.croquet.Application;
import org.lgna.croquet.SingleSelectTableRowState;
import org.lgna.project.ProgramTypeUtilities;
import org.lgna.project.Project;

import javax.swing.JComponent;
import javax.swing.table.AbstractTableModel;
import java.util.Collections;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;

class ResourceTableModel extends AbstractTableModel {
	private Resource[] resources;
	private Set<Resource> referencedResources;

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public int getRowCount() {
		if( this.resources != null ) {
			return this.resources.length;
		} else {
			return 0;
		}
	}

	private String findLocalizedText( String subKey, String defaultValue ) {
		String bundleName = ResourceSingleSelectTableRowState.class.getPackage().getName() + ".croquet";
		try {
			ResourceBundle resourceBundle = ResourceBundleUtilities.getUtf8Bundle( bundleName, JComponent.getDefaultLocale() );
			String key = ResourceSingleSelectTableRowState.class.getSimpleName();

			if( subKey != null ) {
				StringBuilder sb = new StringBuilder();
				sb.append( key );
				sb.append( "." );
				sb.append( subKey );
				key = sb.toString();
			}
			String rv = resourceBundle.getString( key );
			return rv;
		} catch( MissingResourceException mre ) {
			return defaultValue;
		}
	}

	@Override
	public String getColumnName( int columnIndex ) {
		switch( columnIndex ) {
		case ResourceSingleSelectTableRowState.IS_REFERENCED_COLUMN_INDEX:
			return findLocalizedText( "isReferenced", "is referenced?" );
		case ResourceSingleSelectTableRowState.NAME_COLUMN_INDEX:
			return findLocalizedText( "name", "name" );
		case ResourceSingleSelectTableRowState.TYPE_COLUMN_INDEX:
			return findLocalizedText( "type", "type" );
		default:
			return null;
		}
	}

	@Override
	public Object getValueAt( int rowIndex, int columnIndex ) {
		switch( columnIndex ) {
		case ResourceSingleSelectTableRowState.IS_REFERENCED_COLUMN_INDEX:
			return this.referencedResources.contains( this.resources[ rowIndex ] );
		case ResourceSingleSelectTableRowState.NAME_COLUMN_INDEX:
			return this.resources[ rowIndex ];
		case ResourceSingleSelectTableRowState.TYPE_COLUMN_INDEX:
			return this.resources[ rowIndex ].getClass();
		default:
			return null;
		}
	}

	public Resource[] getResources() {
		return this.resources;
	}

	public void reload( Project project ) {
		if( project != null ) {
			// Calling getReferencedResources will update project with all currently used resources.
			this.referencedResources = ProgramTypeUtilities.getReferencedResources( project );
			this.resources = ArrayUtilities.createArray( project.getResources(), Resource.class, true );
		} else {
			this.resources = new Resource[] {};
			this.referencedResources = Collections.emptySet();
		}
		this.fireTableDataChanged();
	}
}

/**
 * @author Dennis Cosgrove
 */
public class ResourceSingleSelectTableRowState extends SingleSelectTableRowState<Resource> {
	public static final int NAME_COLUMN_INDEX = 0;
	public static final int TYPE_COLUMN_INDEX = 1;
	public static final int IS_REFERENCED_COLUMN_INDEX = 2;

	public ResourceSingleSelectTableRowState() {
		super( Application.DOCUMENT_UI_GROUP, UUID.fromString( "2b630438-6852-4b4d-b234-a1fba69f81f8" ), null, ResourceCodec.getInstance( Resource.class ), new ResourceTableModel() );
	}

	@Override
	public Resource getItemAt( int index ) {
		ResourceTableModel resourceTableModel = (ResourceTableModel)this.getSwingModel().getTableModel();
		return resourceTableModel.getResources()[ index ];
	}

	@Override
	protected void setSwingValue( Resource nextValue ) {
		//todo
	}

	public void reloadTableModel( Project project ) {
		ResourceTableModel resourceTableModel = (ResourceTableModel)this.getSwingModel().getTableModel();
		resourceTableModel.reload( project );
	}
}
