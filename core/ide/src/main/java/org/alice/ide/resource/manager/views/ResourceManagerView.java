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

package org.alice.ide.resource.manager.views;

import edu.cmu.cs.dennisc.java.awt.event.LenientMouseClickAdapter;
import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import edu.cmu.cs.dennisc.javax.swing.renderers.TableCellRenderer;
import org.alice.ide.resource.manager.ResourceManagerComposite;
import org.alice.ide.resource.manager.ResourceSingleSelectTableRowState;
import org.lgna.common.Resource;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.triggers.MouseEventTrigger;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.GridPanel;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.Panel;
import org.lgna.croquet.views.ScrollPane;
import org.lgna.croquet.views.Table;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.JTableHeader;
import java.awt.Color;
import java.awt.event.MouseEvent;

abstract class ResourceTableCellRenderer<E> extends TableCellRenderer<E> {

	static final String BUNDLE_NAME = "org.alice.ide.resource.manager.croquet";

	@Override
	protected JLabel getTableCellRendererComponent( JLabel rv, JTable table, E value, boolean isSelected, boolean hasFocus, int row, int column ) {
		rv.setHorizontalAlignment( SwingConstants.CENTER );
		rv.setBorder( null );
		return rv;
	}

	protected Color getForegroundColor( boolean isGoodToGo, boolean isSelected ) {
		if( isGoodToGo ) {
			if( isSelected ) {
				return Color.WHITE;
			} else {
				return Color.BLACK;
			}
		} else {
			if( isSelected ) {
				return new Color( 255, 127, 127 );
			} else {
				return Color.RED.darker();
			}
		}
	}
}

class ResourceIsReferencedTableCellRenderer extends ResourceTableCellRenderer<Boolean> {
	@Override
	protected JLabel getTableCellRendererComponent( JLabel rv, JTable table, Boolean value, boolean isSelected, boolean hasFocus, int row, int column ) {
		rv = super.getTableCellRendererComponent( rv, table, value, isSelected, hasFocus, row, column );
		String key = value ? "yes" : "no";
		rv.setText( ResourceBundleUtilities.getStringForKey( key, BUNDLE_NAME ) );
		rv.setForeground( this.getForegroundColor( value, isSelected ) );
		return rv;
	}
}

class ResourceTypeTableCellRenderer extends ResourceTableCellRenderer<Class<? extends Resource>> {
	@Override
	protected JLabel getTableCellRendererComponent( JLabel rv, JTable table, Class<? extends Resource> value, boolean isSelected, boolean hasFocus, int row, int column ) {
		rv = super.getTableCellRendererComponent( rv, table, value, isSelected, hasFocus, row, column );
		String text = value != null ?
						ResourceBundleUtilities.getStringForKey( value.getSimpleName(), BUNDLE_NAME ) :
						"ERROR";
		rv.setText( text );
		rv.setForeground( this.getForegroundColor( value != null, isSelected ) );
		return rv;
	}
}

class ResourceNameTableCellRenderer extends ResourceTableCellRenderer<Resource> {
	@Override
	protected JLabel getTableCellRendererComponent( JLabel rv, JTable table, Resource value, boolean isSelected, boolean hasFocus, int row, int column ) {
		rv = super.getTableCellRendererComponent( rv, table, value, isSelected, hasFocus, row, column );
		String text;
		if( value != null ) {
			text = value.getName();
		} else {
			text = "ERROR";
		}
		rv.setText( text );
		rv.setForeground( this.getForegroundColor( value != null, isSelected ) );
		return rv;
	}
}

/**
 * @author Dennis Cosgrove
 */
public class ResourceManagerView extends BorderPanel {
	public ResourceManagerView( ResourceManagerComposite composite ) {
		super( composite, 8, 8 );

		this.table = composite.getResourcesState().createTable();
		JTable jTable = this.table.getAwtComponent();
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setReorderingAllowed( false );
		jTable.getColumn( jTable.getColumnName( ResourceSingleSelectTableRowState.IS_REFERENCED_COLUMN_INDEX ) ).setCellRenderer( new ResourceIsReferencedTableCellRenderer() );
		jTable.getColumn( jTable.getColumnName( ResourceSingleSelectTableRowState.NAME_COLUMN_INDEX ) ).setCellRenderer( new ResourceNameTableCellRenderer() );
		jTable.getColumn( jTable.getColumnName( ResourceSingleSelectTableRowState.TYPE_COLUMN_INDEX ) ).setCellRenderer( new ResourceTypeTableCellRenderer() );

		ScrollPane scrollPane = new ScrollPane( this.table );
		this.addCenterComponent( scrollPane );

		Panel lineEndPanel = GridPanel.createSingleColumnGridPane(
				composite.getImportAudioResourceOperation().createButton(),
				composite.getImportImageResourceOperation().createButton(),
				composite.getRemoveResourceOperation().createButton(),
				new Label(),
				composite.getRenameResourceComposite().getLaunchOperation().createButton(),
				composite.getReloadContentOperation().createButton()
				);
		this.addLineEndComponent( new BorderPanel.Builder()
				.pageStart( lineEndPanel )
				.build() );

		this.setBorder( BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.table.addMouseListener( this.mouseAdapter );
		this.table.addMouseMotionListener( this.mouseAdapter );
	}

	@Override
	protected void handleUndisplayable() {
		this.table.removeMouseMotionListener( this.mouseAdapter );
		this.table.removeMouseListener( this.mouseAdapter );
		super.handleUndisplayable();
	}

	private LenientMouseClickAdapter mouseAdapter = new LenientMouseClickAdapter() {
		@Override
		protected void mouseQuoteClickedUnquote( MouseEvent e, int quoteClickUnquoteCount ) {
			if( quoteClickUnquoteCount == 2 ) {
				ResourceManagerComposite composite = (ResourceManagerComposite)getComposite();
				if( composite.getResourcesState().getValue() != null ) {
					final UserActivity activity = composite.getOpeningActivity().newChildActivity();
					MouseEventTrigger.setOnUserActivity( activity, e );
					composite.getRenameResourceComposite().getLaunchOperation().fire( activity );
				}
			}
		}
	};

	private final Table<Resource> table;
}
