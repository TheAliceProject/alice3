/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.ide.resource.manager;

class ResourceTableModel extends javax.swing.table.AbstractTableModel {
	public static final int IS_REFERENCED_COLUMN_INDEX = 2;
	public static final int NAME_COLUMN_INDEX = 0;
	public static final int TYPE_COLUMN_INDEX = 1;
	private org.alice.virtualmachine.Resource[] resources;
	private java.util.Set<org.alice.virtualmachine.Resource> referencedResources;

	public ResourceTableModel( org.alice.virtualmachine.Resource[] resources, java.util.Set<org.alice.virtualmachine.Resource> referencedResources ) {
		this.resources = resources;
		this.referencedResources = referencedResources;
	}
	public int getColumnCount() {
		return 3;
	}
	public int getRowCount() {
		if( this.resources != null ) {
			return this.resources.length;
		} else {
			return 0;
		}
	}
	@Override
	public String getColumnName( int columnIndex ) {
		switch( columnIndex ) {
		case IS_REFERENCED_COLUMN_INDEX:
			return "is referenced?";
		case NAME_COLUMN_INDEX:
			return "name";
		case TYPE_COLUMN_INDEX:
			return "type";
		default:
			return null;
		}
	}
	public Object getValueAt( int rowIndex, int columnIndex ) {
		switch( columnIndex ) {
		case IS_REFERENCED_COLUMN_INDEX:
			return this.referencedResources.contains( this.resources[ rowIndex ] );
		case NAME_COLUMN_INDEX:
			return this.resources[ rowIndex ];
		case TYPE_COLUMN_INDEX:
			return this.resources[ rowIndex ].getClass();
		default:
			return null;
		}
	}
}

abstract class ResourceTableCellRenderer<E> extends edu.cmu.cs.dennisc.croquet.swing.TableCellRenderer<E> {
	@Override
	protected javax.swing.JLabel getTableCellRendererComponent( javax.swing.JLabel rv, javax.swing.JTable table, E value, boolean isSelected, boolean hasFocus, int row, int column ) {
		rv.setHorizontalAlignment( javax.swing.SwingConstants.CENTER );
		rv.setBorder( null );
		return rv;
	}
}

class ResourceIsReferencedTableCellRenderer extends ResourceTableCellRenderer<Boolean> {
	@Override
	protected javax.swing.JLabel getTableCellRendererComponent( javax.swing.JLabel rv, javax.swing.JTable table, Boolean value, boolean isSelected, boolean hasFocus, int row, int column ) {
		rv = super.getTableCellRendererComponent( rv, table, value, isSelected, hasFocus, row, column );
		String text;
		java.awt.Color foreground;
		if( value ) {
			foreground = java.awt.Color.BLACK;
			text = "yes";
		} else {
			foreground = new java.awt.Color( 255, 0, 0 );
			text = "NO";
		}
		rv.setText( text );
		rv.setForeground( foreground );
		return rv;
	}
}

class ResourceTypeTableCellRenderer extends ResourceTableCellRenderer<Class<? extends org.alice.virtualmachine.Resource>> {
	@Override
	protected javax.swing.JLabel getTableCellRendererComponent( javax.swing.JLabel rv, javax.swing.JTable table, Class<? extends org.alice.virtualmachine.Resource> value, boolean isSelected, boolean hasFocus, int row, int column ) {
		rv = super.getTableCellRendererComponent( rv, table, value, isSelected, hasFocus, row, column );
		String text;
		java.awt.Color foreground;
		if( value != null ) {
			foreground = java.awt.Color.BLACK;
			text = value.getSimpleName();
			final String RESOURCE_TEXT = "Resource";
			if( text.endsWith( RESOURCE_TEXT ) ) {
				text = text.substring( 0, text.length() - RESOURCE_TEXT.length() );
			}
			//			text += " (";
			//			text += value.getContentType();
			//			text += ")";
		} else {
			foreground = new java.awt.Color( 255, 0, 0 );
			text = "ERROR";
		}
		rv.setText( text );
		rv.setForeground( foreground );
		return rv;
	}
}

class ResourceNameTableCellRenderer extends ResourceTableCellRenderer<org.alice.virtualmachine.Resource> {
	@Override
	protected javax.swing.JLabel getTableCellRendererComponent( javax.swing.JLabel rv, javax.swing.JTable table, org.alice.virtualmachine.Resource value, boolean isSelected, boolean hasFocus, int row, int column ) {
		rv = super.getTableCellRendererComponent( rv, table, value, isSelected, hasFocus, row, column );
		String text;
		java.awt.Color foreground;
		if( value != null ) {
			foreground = java.awt.Color.BLACK;
			text = value.getName();
		} else {
			foreground = new java.awt.Color( 255, 0, 0 );
			text = "ERROR";
		}
		rv.setText( text );
		rv.setForeground( foreground );
		return rv;
	}
}

/**
 * @author Dennis Cosgrove
 */
public class ResourceManagerPane extends edu.cmu.cs.dennisc.croquet.swing.BorderPane {
	abstract class ResourceOperation extends org.alice.ide.operations.AbstractActionOperation {
		protected org.alice.virtualmachine.Resource resource;

		//todo: better name
		protected abstract org.alice.virtualmachine.Resource selectResource();
		public final void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
			this.resource = this.selectResource();
			if( resource != null ) {
				actionContext.commitAndInvokeRedoIfAppropriate();
			} else {
				actionContext.cancel();
			}
		}
		@Override
		public boolean isSignificant() {
			return true;
		}
	}

	abstract class AddOrRemoveResourceOperation extends ResourceOperation {
		protected void addResource() {
			org.alice.ide.IDE ide = this.getIDE();
			if( ide != null ) {
				edu.cmu.cs.dennisc.alice.Project project = ide.getProject();
				if( ide != null ) {
					project.addResource( this.resource );
					ResourceManagerPane.this.resetModel();
				}
			}
		}
		protected void removeResource() {
			org.alice.ide.IDE ide = this.getIDE();
			if( ide != null ) {
				edu.cmu.cs.dennisc.alice.Project project = ide.getProject();
				if( ide != null ) {
					project.removeResource( this.resource );
					ResourceManagerPane.this.resetModel();
				}
			}
		}
	}

	abstract class AddResourceOperation extends AddOrRemoveResourceOperation {
		public AddResourceOperation() {
		}
		@Override
		public void doOrRedo() throws javax.swing.undo.CannotRedoException {
			this.addResource();
		}
		@Override
		public void undo() throws javax.swing.undo.CannotUndoException {
			this.removeResource();
		}

	}

	class AddAudioResourceOperation extends AddResourceOperation {
		public AddAudioResourceOperation() {
			this.putValue( javax.swing.Action.NAME, "Add Audio..." );
		}
		@Override
		public org.alice.virtualmachine.Resource selectResource() {
			try {
				return org.alice.ide.resource.prompter.AudioResourcePrompter.getSingleton().promptUserForResource( this.getIDE() );
			} catch( java.io.IOException ioe ) {
				throw new RuntimeException( ioe );
			}
		}
	}

	class AddImageResourceOperation extends AddResourceOperation {
		public AddImageResourceOperation() {
			this.putValue( javax.swing.Action.NAME, "Add Image..." );
		}
		@Override
		public org.alice.virtualmachine.Resource selectResource() {
			try {
				return org.alice.ide.resource.prompter.ImageResourcePrompter.getSingleton().promptUserForResource( this.getIDE() );
			} catch( java.io.IOException ioe ) {
				throw new RuntimeException( ioe );
			}
		}
	}

	class RemoveResourceOperation extends AddOrRemoveResourceOperation {
		public RemoveResourceOperation() {
			this.putValue( javax.swing.Action.NAME, "Remove" );
		}
		@Override
		public org.alice.virtualmachine.Resource selectResource() {
			return ResourceManagerPane.this.getSelectedResource();
		}
		@Override
		public void doOrRedo() throws javax.swing.undo.CannotRedoException {
			this.removeResource();
		}
		@Override
		public void undo() throws javax.swing.undo.CannotUndoException {
			this.addResource();
		}
	}

	abstract class RenameWithPreviewPane extends org.alice.ide.name.RenamePane {
		public RenameWithPreviewPane( org.alice.virtualmachine.Resource resource ) {
			super( new org.alice.ide.name.validators.ResourceNameValidator( resource ) );
		}
		protected abstract java.awt.Component createPreviewComponent();
		@Override
		protected java.util.List< java.awt.Component[] > createComponentRows() {
			java.util.List< java.awt.Component[] > rv = super.createComponentRows();
			edu.cmu.cs.dennisc.zoot.ZLabel label = edu.cmu.cs.dennisc.zoot.ZLabel.acquire( "preview:" );
			rv.add( new java.awt.Component[] { label, this.createPreviewComponent() } );
			return rv;
		}
		@Override
		public java.awt.Dimension getPreferredSize() {
			return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumWidth( super.getPreferredSize(), 320 );
		}
	}
	

	class RenameWithImagePreviewPane extends RenameWithPreviewPane {
		public RenameWithImagePreviewPane( org.alice.virtualmachine.resources.ImageResource imageResource ) {
			super( imageResource );
		}
		@Override
		protected java.awt.Component createPreviewComponent() {
			return new javax.swing.JButton( "TODO: image preview" );
		}
	}

	class RenameWithAudioPreviewPane extends RenameWithPreviewPane {
		public RenameWithAudioPreviewPane( org.alice.virtualmachine.resources.AudioResource audioResource ) {
			super( audioResource );
		}
		@Override
		protected java.awt.Component createPreviewComponent() {
			return new javax.swing.JButton( "TODO: audio preview" );
		}
	}

	class RenameResourceOperation extends org.alice.ide.operations.AbstractActionOperation {
		protected org.alice.virtualmachine.Resource resource;
		private String prevName;
		private String nextName;

		public RenameResourceOperation() {
			this.putValue( javax.swing.Action.NAME, "Rename/Preview..." );
		}
		public final void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
			this.resource = ResourceManagerPane.this.getSelectedResource();
			if( this.resource != null ) {
				org.alice.ide.name.RenamePane renamePane;
				if( this.resource instanceof org.alice.virtualmachine.resources.ImageResource ) {
					org.alice.virtualmachine.resources.ImageResource imageResource = (org.alice.virtualmachine.resources.ImageResource)this.resource;
					renamePane = new RenameWithImagePreviewPane( imageResource );
				} else if( this.resource instanceof org.alice.virtualmachine.resources.AudioResource ) {
					org.alice.virtualmachine.resources.AudioResource audioResource = (org.alice.virtualmachine.resources.AudioResource)this.resource;
					renamePane = new RenameWithAudioPreviewPane( audioResource );
				} else {
					renamePane = new org.alice.ide.name.RenamePane( new org.alice.ide.name.validators.ResourceNameValidator( this.resource ) );
				}
				renamePane.setAndSelectNameText( this.resource.getName() );
				this.nextName = renamePane.showInJDialog( this.getIDE(), "Rename Resource" );
				if( this.nextName != null && this.nextName.length() > 0 ) {
					this.prevName = this.resource.getName();
					actionContext.commitAndInvokeRedoIfAppropriate();
				} else {
					actionContext.cancel();
				}
			} else {
				actionContext.cancel();
			}
		}
		@Override
		public void doOrRedo() throws javax.swing.undo.CannotRedoException {
			this.resource.setName( this.nextName );
			ResourceManagerPane.this.table.repaint();
		}
		@Override
		public void undo() throws javax.swing.undo.CannotUndoException {
			this.resource.setName( this.prevName );
			ResourceManagerPane.this.table.repaint();
		}
		@Override
		public boolean isSignificant() {
			return true;
		}
	}

	class ReplaceResourceContentOperation extends org.alice.ide.operations.AbstractActionOperation {
		class Capsule<E extends org.alice.virtualmachine.Resource> {
			private String originalFileName;
			//private String name;
			private String contentType;
			private byte[] data;

			public Capsule( E resource ) {
				this.originalFileName = resource.getOriginalFileName();
				//this.name = resource.getName();
				this.contentType = resource.getContentType();
				this.data = resource.getData();
			}

			public E update( E rv ) {
				rv.setOriginalFileName( this.originalFileName );
				//rv.setName( this.name );
				rv.setContentType( this.contentType );
				rv.setData( this.data );
				return rv;
			}
		}

		class ImageCapsule extends Capsule<org.alice.virtualmachine.resources.ImageResource> {
			private int width;
			private int height;

			public ImageCapsule( org.alice.virtualmachine.resources.ImageResource imageResource ) {
				super( imageResource );
				this.width = imageResource.getWidth();
				this.height = imageResource.getHeight();
			}
			@Override
			public org.alice.virtualmachine.resources.ImageResource update( org.alice.virtualmachine.resources.ImageResource rv ) {
				rv = super.update( rv );
				rv.setWidth( this.width );
				rv.setHeight( this.height );
				return rv;
			}
		}

		class AudioCapsule extends Capsule<org.alice.virtualmachine.resources.AudioResource> {
			private double duration;

			public AudioCapsule( org.alice.virtualmachine.resources.AudioResource audioResource ) {
				super( audioResource );
				this.duration = audioResource.getDuration();
			}
			@Override
			public org.alice.virtualmachine.resources.AudioResource update( org.alice.virtualmachine.resources.AudioResource rv ) {
				rv = super.update( rv );
				rv.setDuration( this.duration );
				return rv;
			}
		}

		protected org.alice.virtualmachine.Resource resource;
		private Capsule prevCapsule;
		private Capsule nextCapsule;

		public ReplaceResourceContentOperation() {
			this.putValue( javax.swing.Action.NAME, "Replace Content..." );
		}
		public final void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
			this.resource = ResourceManagerPane.this.getSelectedResource();
			if( this.resource != null ) {
				if( this.resource instanceof org.alice.virtualmachine.resources.ImageResource ) {
					org.alice.virtualmachine.resources.ImageResource prevImageResource = (org.alice.virtualmachine.resources.ImageResource)resource;
					try {
						org.alice.virtualmachine.resources.ImageResource nextImageResource = org.alice.ide.resource.prompter.ImageResourcePrompter.getSingleton().promptUserForResource( this.getIDE() );
						if( nextImageResource != null ) {
							this.prevCapsule = new ImageCapsule( prevImageResource );
							this.nextCapsule = new ImageCapsule( nextImageResource );
						}
					} catch( java.io.IOException ioe ) {
						throw new RuntimeException( ioe );
					}
				} else if( this.resource instanceof org.alice.virtualmachine.resources.ImageResource ) {
					org.alice.virtualmachine.resources.AudioResource prevAudioResource = (org.alice.virtualmachine.resources.AudioResource)resource;
					try {
						org.alice.virtualmachine.resources.AudioResource nextAudioResource = org.alice.ide.resource.prompter.AudioResourcePrompter.getSingleton().promptUserForResource( this.getIDE() );
						if( nextAudioResource != null ) {
							this.prevCapsule = new AudioCapsule( prevAudioResource );
							this.nextCapsule = new AudioCapsule( nextAudioResource );
						}
					} catch( java.io.IOException ioe ) {
						throw new RuntimeException( ioe );
					}
				}
				if( this.prevCapsule != null && this.nextCapsule != null ) {
					actionContext.commitAndInvokeRedoIfAppropriate();
				} else {
					actionContext.cancel();
				}
			} else {
				actionContext.cancel();
			}
		}
		@Override
		public void doOrRedo() throws javax.swing.undo.CannotRedoException {
			this.nextCapsule.update( this.resource );
		}
		@Override
		public void undo() throws javax.swing.undo.CannotUndoException {
			this.prevCapsule.update( this.resource );
		}
		@Override
		public boolean isSignificant() {
			return true;
		}
	}

	private javax.swing.JTable table = new javax.swing.JTable();
	private javax.swing.event.ListSelectionListener listSelectionAdapter = new javax.swing.event.ListSelectionListener() {
		public void valueChanged( javax.swing.event.ListSelectionEvent e ) {
			if( e.getValueIsAdjusting() ) {
				//pass
			} else {
				ResourceManagerPane.this.handleSelection();
			}
		}
	};
	private RenameResourceOperation renameResourceOperation = new RenameResourceOperation();
	private ReplaceResourceContentOperation replaceResourceOperation = new ReplaceResourceContentOperation();
	private AddImageResourceOperation addImageResourceOperation = new AddImageResourceOperation();
	private AddAudioResourceOperation addAudioResourceOperation = new AddAudioResourceOperation();
	private RemoveResourceOperation removeResourceOperation = new RemoveResourceOperation();

	private edu.cmu.cs.dennisc.awt.event.LenientMouseClickAdapter mouseAdapter = new edu.cmu.cs.dennisc.awt.event.LenientMouseClickAdapter() {
		@Override
		protected void mouseQuoteClickedUnquote( java.awt.event.MouseEvent e, int quoteClickUnquoteCount ) {
			if( quoteClickUnquoteCount == 2 ) {
				if( table.getSelectedRow() >= 0 ) {
					edu.cmu.cs.dennisc.zoot.ZManager.performIfAppropriate( renameResourceOperation, e, true );
				}
			}
		}
	};

	public ResourceManagerPane() {
		super( 8, 8 );
		this.resetModel();
		//this.table.setRowSelectionAllowed( true );

		javax.swing.table.JTableHeader tableHeader = this.table.getTableHeader();
		tableHeader.setReorderingAllowed( false );
		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( this.table );
		scrollPane.setPreferredSize( new java.awt.Dimension( 320, 240 ) );
		//this.table.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
		scrollPane.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
		this.add( scrollPane, java.awt.BorderLayout.CENTER );

		java.awt.Component pane = new edu.cmu.cs.dennisc.croquet.swing.SingleColumnPane( new edu.cmu.cs.dennisc.zoot.ZButton( this.addImageResourceOperation ), new edu.cmu.cs.dennisc.zoot.ZButton( this.addAudioResourceOperation ),
				new edu.cmu.cs.dennisc.zoot.ZButton( this.removeResourceOperation ), javax.swing.Box.createVerticalStrut( 8 ), new edu.cmu.cs.dennisc.zoot.ZButton( this.renameResourceOperation ), new edu.cmu.cs.dennisc.zoot.ZButton(
						this.replaceResourceOperation ) );
		this.add( new edu.cmu.cs.dennisc.croquet.swing.PageAxisPane( pane, javax.swing.Box.createGlue() ), java.awt.BorderLayout.EAST );
		this.handleSelection();
	}

	private org.alice.virtualmachine.Resource getSelectedResource() {
		int rowIndex = this.table.getSelectedRow();
		if( rowIndex >= 0 ) {
			return (org.alice.virtualmachine.Resource)this.table.getValueAt( rowIndex, ResourceTableModel.NAME_COLUMN_INDEX );
		} else {
			return null;
		}
	}
	private void resetModel() {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		if( ide != null ) {
			edu.cmu.cs.dennisc.alice.Project project = ide.getProject();
			if( project != null ) {
				org.alice.virtualmachine.Resource[] resources = edu.cmu.cs.dennisc.util.CollectionUtilities.createArray( project.getResources(), org.alice.virtualmachine.Resource.class, true );
				java.util.Set<org.alice.virtualmachine.Resource> referencedResources = edu.cmu.cs.dennisc.alice.project.ProjectUtilities.getReferencedResources( project );
				javax.swing.table.TableModel tableModel = new ResourceTableModel( resources, referencedResources );
				this.table.setModel( tableModel );
				this.table.getColumn( this.table.getColumnName( ResourceTableModel.IS_REFERENCED_COLUMN_INDEX ) ).setCellRenderer( new ResourceIsReferencedTableCellRenderer() );
				this.table.getColumn( this.table.getColumnName( ResourceTableModel.NAME_COLUMN_INDEX ) ).setCellRenderer( new ResourceNameTableCellRenderer() );
				this.table.getColumn( this.table.getColumnName( ResourceTableModel.TYPE_COLUMN_INDEX ) ).setCellRenderer( new ResourceTypeTableCellRenderer() );
			}
		}
	}
	private void handleSelection() {
		int rowIndex = this.table.getSelectedRow();
		this.renameResourceOperation.setEnabled( rowIndex >= 0 );
		this.replaceResourceOperation.setEnabled( rowIndex >= 0 );
		boolean isRemoveSupported;
		if( rowIndex >= 0 ) {
			isRemoveSupported = (false == (Boolean)this.table.getModel().getValueAt( rowIndex, ResourceTableModel.IS_REFERENCED_COLUMN_INDEX ));
		} else {
			isRemoveSupported = false;
		}
		this.removeResourceOperation.setEnabled( isRemoveSupported );
	}

	@Override
	public void addNotify() {
		super.addNotify();
		this.table.getSelectionModel().addListSelectionListener( this.listSelectionAdapter );
		this.table.addMouseListener( this.mouseAdapter );
		this.table.addMouseMotionListener( this.mouseAdapter );
		//this.table.getColumnModel().getSelectionModel().addListSelectionListener( this.listSelectionAdapter );
	}

	@Override
	public void removeNotify() {
		this.table.removeMouseMotionListener( this.mouseAdapter );
		this.table.removeMouseListener( this.mouseAdapter );
		this.table.getSelectionModel().removeListSelectionListener( this.listSelectionAdapter );
		//this.table.getColumnModel().getSelectionModel().removeListSelectionListener( this.listSelectionAdapter );
		super.removeNotify();
	}

	@Override
	public java.awt.Dimension getPreferredSize() {
		return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumWidth( super.getPreferredSize(), 400 );
	}
}
