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
	private java.util.Set< org.alice.virtualmachine.Resource > referencedResources;

	public ResourceTableModel( org.alice.virtualmachine.Resource[] resources, java.util.Set< org.alice.virtualmachine.Resource > referencedResources ) {
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

abstract class ResourceTableCellRenderer<E> extends edu.cmu.cs.dennisc.croquet.swing.TableCellRenderer< E > {
	@Override
	protected javax.swing.JLabel getTableCellRendererComponent( javax.swing.JLabel rv, javax.swing.JTable table, E value, boolean isSelected, boolean hasFocus, int row, int column ) {
		rv.setHorizontalAlignment( javax.swing.SwingConstants.CENTER );
		rv.setBorder( null );
		return rv;
	}
}

class ResourceIsReferencedTableCellRenderer extends ResourceTableCellRenderer< Boolean > {
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

class ResourceTypeTableCellRenderer extends ResourceTableCellRenderer< Class< ? extends org.alice.virtualmachine.Resource >> {
	@Override
	protected javax.swing.JLabel getTableCellRendererComponent( javax.swing.JLabel rv, javax.swing.JTable table, Class< ? extends org.alice.virtualmachine.Resource > value, boolean isSelected, boolean hasFocus, int row, int column ) {
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

class ResourceNameTableCellRenderer extends ResourceTableCellRenderer< org.alice.virtualmachine.Resource > {
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
		public ResourceOperation() {
			super( edu.cmu.cs.dennisc.alice.Project.GROUP_UUID );
		}
		protected abstract edu.cmu.cs.dennisc.zoot.Edit createEdit( org.alice.virtualmachine.Resource resource );

		//todo: better name
		protected abstract org.alice.virtualmachine.Resource selectResource();
		public final void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
			org.alice.virtualmachine.Resource resource = this.selectResource();
			if( resource != null ) {
				actionContext.commitAndInvokeDo( this.createEdit( resource ) );
			} else {
				actionContext.cancel();
			}
		}
	}

	abstract class AddOrRemoveResourceEdit extends edu.cmu.cs.dennisc.zoot.AbstractEdit {
		protected void addResource( org.alice.virtualmachine.Resource resource ) {
			org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
			if( ide != null ) {
				edu.cmu.cs.dennisc.alice.Project project = ide.getProject();
				if( ide != null ) {
					project.addResource( resource );
					ResourceManagerPane.this.resetModel();
				}
			}
		}
		protected void removeResource( org.alice.virtualmachine.Resource resource ) {
			org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
			if( ide != null ) {
				edu.cmu.cs.dennisc.alice.Project project = ide.getProject();
				if( ide != null ) {
					project.removeResource( resource );
					ResourceManagerPane.this.resetModel();
				}
			}
		}
	}

	class ImportResourceOperation extends ResourceOperation {
		public ImportResourceOperation() {
			this.putValue( javax.swing.Action.NAME, "Import..." );
		}
		@Override
		public org.alice.virtualmachine.Resource selectResource() {
			int result = javax.swing.JOptionPane.showOptionDialog( this.getIDE(), "What type of resource would you like to import?", "Select Type", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, null, new String[] {
					"Import Audio...", "Import Image..." }, null );
			switch( result ) {
			case javax.swing.JOptionPane.YES_OPTION:
				try {
					return org.alice.ide.resource.prompter.AudioResourcePrompter.getSingleton().promptUserForResource( this.getIDE() );
				} catch( java.io.IOException ioe ) {
					throw new RuntimeException( ioe );
				}
			case javax.swing.JOptionPane.NO_OPTION:
				try {
					return org.alice.ide.resource.prompter.ImageResourcePrompter.getSingleton().promptUserForResource( this.getIDE() );
				} catch( java.io.IOException ioe ) {
					throw new RuntimeException( ioe );
				}
			default:
				return null;
			}
		}
		@Override
		public edu.cmu.cs.dennisc.zoot.Edit createEdit( final org.alice.virtualmachine.Resource resource ) {
			return new AddOrRemoveResourceEdit() {
				@Override
				public void doOrRedo( boolean isDo ) {
					this.addResource( resource );
				}
				@Override
				public void undo() {
					this.removeResource( resource );
				}
				@Override
				protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
					rv.append( "add resource" );
					rv.append( edu.cmu.cs.dennisc.pattern.NameableUtilities.safeGetName( resource ) );
					return rv;
				}
			};
		}
	}

	class RemoveResourceOperation extends ResourceOperation {
		public RemoveResourceOperation() {
			this.putValue( javax.swing.Action.NAME, "Remove" );
		}
		@Override
		public org.alice.virtualmachine.Resource selectResource() {
			return ResourceManagerPane.this.getSelectedResource();
		}
		@Override
		public edu.cmu.cs.dennisc.zoot.Edit createEdit( final org.alice.virtualmachine.Resource resource ) {
			return new AddOrRemoveResourceEdit() {
				@Override
				public void doOrRedo( boolean isDo ) {
					this.removeResource( resource );
				}
				@Override
				public void undo() {
					this.addResource( resource );
				}
				@Override
				protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
					rv.append( "remove resource" );
					rv.append( edu.cmu.cs.dennisc.pattern.NameableUtilities.safeGetName( resource ) );
					return rv;
				}
			};
		}
	}

	abstract class RenameWithPreviewPane<E extends org.alice.virtualmachine.Resource> extends org.alice.ide.name.RenamePane {
		private E resource;

		public RenameWithPreviewPane( E resource ) {
			super( new org.alice.ide.name.validators.ResourceNameValidator( resource ) );
			this.resource = resource;
		}
		protected E getResource() {
			return this.resource;
		}
		protected abstract java.awt.Component createPreviewComponent();
		@Override
		protected java.util.List< java.awt.Component[] > createComponentRows() {
			java.util.List< java.awt.Component[] > rv = super.createComponentRows();
			javax.swing.JLabel label = edu.cmu.cs.dennisc.croquet.LabelUtilities.createLabel( "preview:" );
			rv.add( new java.awt.Component[] { label, this.createPreviewComponent() } );
			return rv;
		}
		//		@Override
		//		public java.awt.Dimension getPreferredSize() {
		//			return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumWidth( super.getPreferredSize(), 320 );
		//		}
	}

	class RenameWithImagePreviewPane extends RenameWithPreviewPane< org.alice.virtualmachine.resources.ImageResource > {
		public RenameWithImagePreviewPane( org.alice.virtualmachine.resources.ImageResource imageResource ) {
			super( imageResource );
		}
		@Override
		protected java.awt.Component createPreviewComponent() {
			java.awt.image.BufferedImage bufferedImage = edu.cmu.cs.dennisc.image.ImageFactory.getBufferedImage( this.getResource() );
			return new edu.cmu.cs.dennisc.croquet.swing.ImageView( bufferedImage, 256 );
		}
	}

	class RenameWithAudioPreviewPane extends RenameWithPreviewPane< org.alice.virtualmachine.resources.AudioResource > {
		public RenameWithAudioPreviewPane( org.alice.virtualmachine.resources.AudioResource audioResource ) {
			super( audioResource );
		}
		@Override
		protected java.awt.Component createPreviewComponent() {
			final edu.cmu.cs.dennisc.croquet.swing.BorderPane rv = new edu.cmu.cs.dennisc.croquet.swing.BorderPane() {
				@Override
				public java.awt.Dimension getPreferredSize() {
					return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumSize( super.getPreferredSize(), 256, 32 );
				}
			};
			new Thread() {
				@Override
				public void run() {
					edu.cmu.cs.dennisc.media.Player player = edu.cmu.cs.dennisc.media.jmf.MediaFactory.getSingleton().createPlayer( RenameWithAudioPreviewPane.this.getResource() );
					java.awt.Component component = player.getControlPanelComponent();
					if( component != null ) {
						rv.add( component );
						rv.getRootPane().revalidate();
					}
				}
			}.start();
			return rv;
		}
	}

	class RenameResourceOperation extends org.alice.ide.operations.AbstractActionOperation {
		public RenameResourceOperation() {
			super( edu.cmu.cs.dennisc.alice.Project.GROUP_UUID );
			this.putValue( javax.swing.Action.NAME, "Rename..." );
		}
		public final void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
			final org.alice.virtualmachine.Resource resource = ResourceManagerPane.this.getSelectedResource();
			if( resource != null ) {
				org.alice.ide.name.RenamePane renamePane;
				if( resource instanceof org.alice.virtualmachine.resources.ImageResource ) {
					org.alice.virtualmachine.resources.ImageResource imageResource = (org.alice.virtualmachine.resources.ImageResource)resource;
					renamePane = new RenameWithImagePreviewPane( imageResource );
				} else if( resource instanceof org.alice.virtualmachine.resources.AudioResource ) {
					org.alice.virtualmachine.resources.AudioResource audioResource = (org.alice.virtualmachine.resources.AudioResource)resource;
					renamePane = new RenameWithAudioPreviewPane( audioResource );
				} else {
					renamePane = new org.alice.ide.name.RenamePane( new org.alice.ide.name.validators.ResourceNameValidator( resource ) );
				}
				renamePane.setAndSelectNameText( resource.getName() );
				final String nextName = renamePane.showInJDialog( this.getIDE(), "Rename Resource" );
				if( nextName != null && nextName.length() > 0 ) {
					final String prevName = resource.getName();
					actionContext.commitAndInvokeDo( new edu.cmu.cs.dennisc.zoot.AbstractEdit() {
						@Override
						public void doOrRedo( boolean isDo ) {
							resource.setName( nextName );
							ResourceManagerPane.this.table.repaint();
						}
						@Override
						public void undo() {
							resource.setName( prevName );
							ResourceManagerPane.this.table.repaint();
						}
						@Override
						protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
							rv.append( "rename resource: " );
							rv.append( prevName );
							rv.append( " ===> " );
							rv.append( nextName );
							return rv;
						}
					} );
				} else {
					actionContext.cancel();
				}
			} else {
				actionContext.cancel();
			}
		}
	}

	class ReloadResourceOperation extends org.alice.ide.operations.AbstractActionOperation {
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
				rv.setContent( this.contentType, this.data );
				return rv;
			}
		}

		class ImageCapsule extends Capsule< org.alice.virtualmachine.resources.ImageResource > {
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

		class AudioCapsule extends Capsule< org.alice.virtualmachine.resources.AudioResource > {
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

		public ReloadResourceOperation() {
			super( edu.cmu.cs.dennisc.alice.Project.GROUP_UUID );
			this.putValue( javax.swing.Action.NAME, "Reload Content..." );
		}
		public final void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
			final org.alice.virtualmachine.Resource resource = ResourceManagerPane.this.getSelectedResource();
			if( resource != null ) {
				final Capsule prevCapsule;
				final Capsule nextCapsule;
				if( resource instanceof org.alice.virtualmachine.resources.ImageResource ) {
					org.alice.virtualmachine.resources.ImageResource prevImageResource = (org.alice.virtualmachine.resources.ImageResource)resource;
					try {
						org.alice.virtualmachine.resources.ImageResource nextImageResource = org.alice.ide.resource.prompter.ImageResourcePrompter.getSingleton().promptUserForResource( this.getIDE() );
						if( nextImageResource != null ) {
							prevCapsule = new ImageCapsule( prevImageResource );
							nextCapsule = new ImageCapsule( nextImageResource );
						} else {
							prevCapsule = null;
							nextCapsule = null;
						}
					} catch( java.io.IOException ioe ) {
						throw new RuntimeException( ioe );
					}
				} else if( resource instanceof org.alice.virtualmachine.resources.AudioResource ) {
					org.alice.virtualmachine.resources.AudioResource prevAudioResource = (org.alice.virtualmachine.resources.AudioResource)resource;
					try {
						org.alice.virtualmachine.resources.AudioResource nextAudioResource = org.alice.ide.resource.prompter.AudioResourcePrompter.getSingleton().promptUserForResource( this.getIDE() );
						if( nextAudioResource != null ) {
							prevCapsule = new AudioCapsule( prevAudioResource );
							nextCapsule = new AudioCapsule( nextAudioResource );
						} else {
							prevCapsule = null;
							nextCapsule = null;
						}
					} catch( java.io.IOException ioe ) {
						throw new RuntimeException( ioe );
					}
				} else {
					prevCapsule = null;
					nextCapsule = null;
				}
				if( prevCapsule != null && nextCapsule != null ) {
					actionContext.commitAndInvokeDo( new edu.cmu.cs.dennisc.zoot.AbstractEdit() {
						@Override
						public void doOrRedo( boolean isDo ) {
							nextCapsule.update( resource );
						}
						@Override
						public void undo() {
							prevCapsule.update( resource );
						}
						@Override
						protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
							rv.append( "reload content" );
							return rv;
						}
					} );
				} else {
					actionContext.cancel();
				}
			} else {
				actionContext.cancel();
			}
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
	private ReloadResourceOperation replaceResourceOperation = new ReloadResourceOperation();
	private ImportResourceOperation addResourceOperation = new ImportResourceOperation();
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

		java.awt.Component pane = new edu.cmu.cs.dennisc.croquet.swing.SingleColumnPane( edu.cmu.cs.dennisc.zoot.ZManager.createButton( this.addResourceOperation ),edu.cmu.cs.dennisc.zoot.ZManager.createButton( this.removeResourceOperation ), javax.swing.Box
				.createVerticalStrut( 8 ), edu.cmu.cs.dennisc.zoot.ZManager.createButton( this.renameResourceOperation ), edu.cmu.cs.dennisc.zoot.ZManager.createButton( this.replaceResourceOperation ) );
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

				ide.ensureProjectCodeUpToDate();

				org.alice.virtualmachine.Resource[] resources = edu.cmu.cs.dennisc.util.CollectionUtilities.createArray( project.getResources(), org.alice.virtualmachine.Resource.class, true );
				java.util.Set< org.alice.virtualmachine.Resource > referencedResources = edu.cmu.cs.dennisc.alice.project.ProjectUtilities.getReferencedResources( project );
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
		boolean isSelected = rowIndex >= 0;
		String renameAndReplaceToolTipText;

		String removeToolTipText;
		boolean isReferenced;
		if( isSelected ) {
			isReferenced = (Boolean)this.table.getModel().getValueAt( rowIndex, ResourceTableModel.IS_REFERENCED_COLUMN_INDEX );
			renameAndReplaceToolTipText = null;
			if( isReferenced ) {
				removeToolTipText = null;
			} else {
				removeToolTipText = "cannot remove resources that are referenced";
			}
		} else {
			isReferenced = false;
			renameAndReplaceToolTipText = "select resource";
			removeToolTipText = renameAndReplaceToolTipText;
		}
		this.renameResourceOperation.setEnabled( isSelected );
		this.renameResourceOperation.setToolTipText( renameAndReplaceToolTipText );
		this.replaceResourceOperation.setEnabled( isSelected );
		this.replaceResourceOperation.setToolTipText( renameAndReplaceToolTipText );

		this.removeResourceOperation.setEnabled( isReferenced==false );
		this.removeResourceOperation.setToolTipText( removeToolTipText );
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
