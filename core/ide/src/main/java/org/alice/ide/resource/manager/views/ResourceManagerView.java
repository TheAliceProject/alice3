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

abstract class ResourceTableCellRenderer<E> extends edu.cmu.cs.dennisc.javax.swing.renderers.TableCellRenderer<E> {
	@Override
	protected javax.swing.JLabel getTableCellRendererComponent( javax.swing.JLabel rv, javax.swing.JTable table, E value, boolean isSelected, boolean hasFocus, int row, int column ) {
		rv.setHorizontalAlignment( javax.swing.SwingConstants.CENTER );
		rv.setBorder( null );
		return rv;
	}

	protected java.awt.Color getForegroundColor( boolean isGoodToGo, boolean isSelected ) {
		if( isGoodToGo ) {
			if( isSelected ) {
				return java.awt.Color.WHITE;
			} else {
				return java.awt.Color.BLACK;
			}
		} else {
			if( isSelected ) {
				return new java.awt.Color( 255, 127, 127 );
			} else {
				return java.awt.Color.RED.darker();
			}
		}
	}
}

class ResourceIsReferencedTableCellRenderer extends ResourceTableCellRenderer<Boolean> {
	@Override
	protected javax.swing.JLabel getTableCellRendererComponent( javax.swing.JLabel rv, javax.swing.JTable table, Boolean value, boolean isSelected, boolean hasFocus, int row, int column ) {
		rv = super.getTableCellRendererComponent( rv, table, value, isSelected, hasFocus, row, column );
		String text;
		if( value ) {
			text = "yes";
		} else {
			text = "NO";
		}
		rv.setText( text );
		rv.setForeground( this.getForegroundColor( value, isSelected ) );
		return rv;
	}
}

class ResourceTypeTableCellRenderer extends ResourceTableCellRenderer<Class<? extends org.lgna.common.Resource>> {
	@Override
	protected javax.swing.JLabel getTableCellRendererComponent( javax.swing.JLabel rv, javax.swing.JTable table, Class<? extends org.lgna.common.Resource> value, boolean isSelected, boolean hasFocus, int row, int column ) {
		rv = super.getTableCellRendererComponent( rv, table, value, isSelected, hasFocus, row, column );
		String text;
		if( value != null ) {
			text = value.getSimpleName();
			final String RESOURCE_TEXT = "Resource";
			if( text.endsWith( RESOURCE_TEXT ) ) {
				text = text.substring( 0, text.length() - RESOURCE_TEXT.length() );
			}
			//			text += " (";
			//			text += value.getContentType();
			//			text += ")";
		} else {
			text = "ERROR";
		}
		rv.setText( text );
		rv.setForeground( this.getForegroundColor( value != null, isSelected ) );
		return rv;
	}
}

class ResourceNameTableCellRenderer extends ResourceTableCellRenderer<org.lgna.common.Resource> {
	@Override
	protected javax.swing.JLabel getTableCellRendererComponent( javax.swing.JLabel rv, javax.swing.JTable table, org.lgna.common.Resource value, boolean isSelected, boolean hasFocus, int row, int column ) {
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
public class ResourceManagerView extends org.lgna.croquet.views.BorderPanel {
	public ResourceManagerView( org.alice.ide.resource.manager.ResourceManagerComposite composite ) {
		super( composite, 8, 8 );

		this.table = composite.getResourcesState().createTable();
		javax.swing.JTable jTable = this.table.getAwtComponent();
		javax.swing.table.JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setReorderingAllowed( false );
		jTable.getColumn( jTable.getColumnName( org.alice.ide.resource.manager.ResourceSingleSelectTableRowState.IS_REFERENCED_COLUMN_INDEX ) ).setCellRenderer( new ResourceIsReferencedTableCellRenderer() );
		jTable.getColumn( jTable.getColumnName( org.alice.ide.resource.manager.ResourceSingleSelectTableRowState.NAME_COLUMN_INDEX ) ).setCellRenderer( new ResourceNameTableCellRenderer() );
		jTable.getColumn( jTable.getColumnName( org.alice.ide.resource.manager.ResourceSingleSelectTableRowState.TYPE_COLUMN_INDEX ) ).setCellRenderer( new ResourceTypeTableCellRenderer() );

		org.lgna.croquet.views.ScrollPane scrollPane = new org.lgna.croquet.views.ScrollPane( this.table );
		this.addCenterComponent( scrollPane );

		org.lgna.croquet.views.Panel lineEndPanel = org.lgna.croquet.views.GridPanel.createSingleColumnGridPane(
				composite.getImportAudioResourceOperation().createButton(),
				composite.getImportImageResourceOperation().createButton(),
				composite.getRemoveResourceOperation().createButton(),
				new org.lgna.croquet.views.Label(),
				composite.getRenameResourceComposite().getLaunchOperation().createButton(),
				composite.getReloadContentOperation().createButton()
				);
		this.addLineEndComponent( new org.lgna.croquet.views.BorderPanel.Builder()
				.pageStart( lineEndPanel )
				.build() );

		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
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

	private edu.cmu.cs.dennisc.java.awt.event.LenientMouseClickAdapter mouseAdapter = new edu.cmu.cs.dennisc.java.awt.event.LenientMouseClickAdapter() {
		@Override
		protected void mouseQuoteClickedUnquote( java.awt.event.MouseEvent e, int quoteClickUnquoteCount ) {
			if( quoteClickUnquoteCount == 2 ) {
				org.alice.ide.resource.manager.ResourceManagerComposite composite = (org.alice.ide.resource.manager.ResourceManagerComposite)getComposite();
				if( composite.getResourcesState().getValue() != null ) {
					composite.getRenameResourceComposite().getLaunchOperation().fire( org.lgna.croquet.triggers.MouseEventTrigger.createUserInstance( e ) );
				}
			}
		}
	};

	private final org.lgna.croquet.views.Table<org.lgna.common.Resource> table;
}
