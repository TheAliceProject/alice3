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
package org.alice.stageide.gallerybrowser;

class CreatePersonActionOperation extends org.alice.ide.operations.AbstractActionOperation {
	public CreatePersonActionOperation() {
		this.putValue( javax.swing.Action.NAME, "Create Person..." );
	}
	public void perform( zoot.ActionContext actionContext ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo CreatePersonActionOperation" );
		zoot.ZFrame frame = new zoot.ZFrame() {
			@Override
			protected void handleWindowOpened( java.awt.event.WindowEvent e ) {
			}
			@Override
			protected void handleQuit( java.util.EventObject e ) {
				this.dispose();
			}
		};
		frame.setSize( new java.awt.Dimension( 1024, 768 ) );
		frame.getContentPane().add( new org.alice.stageide.personeditor.PersonEditor() );
		frame.setVisible( true );
	}
}
class CreateTextActionOperation extends org.alice.ide.operations.AbstractActionOperation {
	public CreateTextActionOperation() {
		this.putValue( javax.swing.Action.NAME, "Create Text..." );
	}
	public void perform( zoot.ActionContext actionContext ) {
	}
}
class CreateInstanceFromFileActionOperation extends org.alice.ide.operations.AbstractActionOperation {
	public void perform( zoot.ActionContext actionContext ) {
	}
}
class CreateMyInstance extends CreateInstanceFromFileActionOperation {
	public CreateMyInstance() {
		this.putValue( javax.swing.Action.NAME, "My Classes..." );
	}
}
class CreateTextbookInstance extends CreateInstanceFromFileActionOperation {
	public CreateTextbookInstance() {
		this.putValue( javax.swing.Action.NAME, "Textbook Classes..." );
	}
}


class CreateInstancePane extends zoot.ZInputPane< Object > {
	private java.io.File rootDirectory;
	private java.io.File file;
	private edu.cmu.cs.dennisc.alice.ast.AbstractType type;
	
	private edu.cmu.cs.dennisc.alice.ast.AbstractType adjustTypeIfNecessary( edu.cmu.cs.dennisc.alice.ast.AbstractType rv, StringBuffer out ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractType superType;
		if( rv != null ) {
			superType = rv.getFirstTypeEncounteredDeclaredInJava();
		} else { 
			String superClsName = this._getSuperClassName();
			Class cls = edu.cmu.cs.dennisc.lang.ClassUtilities.forName( superClsName );
			superType = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( cls );
		}
		boolean isAlreadyReferenced = false;

//		referencedTypes = _getReferencedTypesDeclaredInAlice()
//		
//		rv = None
//		for referencedType in referencedTypes:
//			if referencedType.getFirstTypeEncounteredDeclaredInJava() is typeDeclaredInJava:
//				rv = ( referencedType, True )
//				break
//			
//		if rv:
//			pass
//		else:
//			name = _getUniqueTypeName( typeDeclaredInJava, referencedTypes )
//			constructor = alice.ast.ConstructorDeclaredInAlice( [], alice.ast.BlockStatement( [] ) )
//			
//			constructors = [ constructor ]
//			methods = []
//			fields = []
//		
//			rv = ( alice.ast.TypeDeclaredInAlice( name, None, typeDeclaredInJava, constructors, methods, fields ), False )

//		_type, isAlreadyReferenced = _getTypeForTypeDeclaredInJava( superType );
//		if( isAlreadyReferenced ) {
//			self.type = _type;
//		} else {
//			if( type != null ) {
//				this.type = type;
//			} else {
//				this.type = _type;
//			}
//		}
		
		out.append( "Create a new instance of " );
		if( isAlreadyReferenced ) {
			if( type != null ) {
				out.append( "already existing (ignoring previously saved file) class " );
			} else {
				out.append( "already existing class " );
			}
		} else {
			if( type != null ) {
				out.append( "(from previously saved file) class " );
			} else {
				out.append( "(brand-new) class " );
			}
		}
		return rv;
	}
	public CreateInstancePane( java.io.File rootDirectory, java.io.File file, edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		this.rootDirectory = rootDirectory;
		this.file = file;

		

		StringBuffer sb = new StringBuffer();
		this.type = adjustTypeIfNecessary( type, sb );
		//sb.append( this.type.getName() );
		sb.append( "?" );
		
		zoot.ZLabel label = new zoot.ZLabel();
		label.setText( sb.toString() );
		label.setIcon( new javax.swing.ImageIcon( this.file.getAbsolutePath() ) );
		label.setHorizontalTextPosition( javax.swing.SwingConstants.CENTER );
		label.setVerticalTextPosition( javax.swing.SwingConstants.TOP );
		
//		IdentifierPane identifierPane = new IdentifierPane( None, getSceneEditor().getSceneType().fields );
//		identifierPane._textVC.setText( this._getAvailableFieldName() );
//		identifierPane._textVC.selectAll();
//		
//		identifierPane.setInputPane( this );
//		this.addOKButtonValidator( identifierPane );

		this.setLayout( new java.awt.BorderLayout() );
		this.add( label, java.awt.BorderLayout.CENTER );
//		this.add( identifierPane, java.awt.BorderLayout.SOUTH );
	}
		
	private String _getSuperClassName() {
		String prefix = this.rootDirectory.getAbsolutePath();
		String path = this.file.getAbsolutePath();
		edu.cmu.cs.dennisc.print.PrintUtilities.println( prefix );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( path );
		path = path.substring( prefix.length() + 1, path.length()-4 );
		path = path.replace( "\\", "/" );
		return path.replace( "/", "." );
	}
	
	private String _getSuperClassBaseName() {
		return edu.cmu.cs.dennisc.io.FileUtilities.getBaseName( this.file );
	}

	private String _getAvailableFieldName() {
		//return getSceneEditor()._getAvailableFieldName( this._getSuperClassBaseName() );
		return "jorgeJones";
	}
	
	private org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor getSceneEditor() {
		return null;
	}
	
	@Override
	public Object getActualInputValue() {
		return null;
//		rv = getSceneEditor().createInstance( self._type )
//		#rv = getSceneEditor().createInstance( self._getClassName(), self._getSuperClassName() )
//		ecc.dennisc.alice.vm.getInstanceInJava( rv ).setName( self._identifierPane.getText() )
//		return rv
	}
}

class GalleryFileActionOperation extends org.alice.ide.operations.AbstractActionOperation {
	private java.io.File rootDirectory;
	private java.io.File file;
	public GalleryFileActionOperation( java.io.File rootDirectory, java.io.File file ) {
		this.rootDirectory = rootDirectory;
		this.file = file;
	}
	public void perform( zoot.ActionContext actionContext ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( actionContext );
		CreateInstancePane createInstancePane = new CreateInstancePane( this.rootDirectory, file, null );
		Object instance = createInstancePane.showInJDialog( getIDE(), "Create Instance", true );
		if( instance != null ) {
			//getSceneEditor().addInstance( instance )
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
public class GalleryBrowser extends org.alice.ide.gallerybrowser.AbstractGalleryBrowser {
	private java.util.Map<String, String> map;
	public GalleryBrowser( java.io.File thumbnailRoot, java.util.Map<String, String> map ) {
		this.map = map;
		this.initialize( thumbnailRoot );
		zoot.ZButton createPersonButton = new zoot.ZButton( new CreatePersonActionOperation() );
		zoot.ZButton createTextButton = new zoot.ZButton( new CreateTextActionOperation() );
		zoot.ZButton createMyInstanceButton = new zoot.ZButton( new CreateMyInstance() );
		zoot.ZButton createTextbookInstanceButton = new zoot.ZButton( new CreateTextbookInstance() );

		swing.Pane fromFilePane = new swing.Pane();
		fromFilePane.setLayout( new java.awt.GridLayout( 2, 1, 0, 4 ) );
		fromFilePane.add( createMyInstanceButton );
		fromFilePane.add( createTextbookInstanceButton );
		
		swing.BorderPane buttonPane = new swing.BorderPane();
		buttonPane.add( fromFilePane, java.awt.BorderLayout.NORTH );
		buttonPane.add( createTextButton, java.awt.BorderLayout.SOUTH );
		
		//this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		this.setBackground( new java.awt.Color( 220, 220, 255 ) );
		this.add( createPersonButton, java.awt.BorderLayout.WEST );
		this.add( buttonPane, java.awt.BorderLayout.EAST );
	}
	@Override
	protected String getAdornedTextFor( String name, boolean isDirectory, boolean isRequestedByPath ) {
		if( this.map != null ) {
			if( this.map.containsKey( name ) ) {
				name = this.map.get( name );
			}
		}
		return super.getAdornedTextFor( name, isDirectory, isRequestedByPath );
	}
	@Override
	protected void handleFileActivation( java.io.File file ) {
		assert file.isFile();
		zoot.ZManager.performIfAppropriate( new GalleryFileActionOperation( this.getRootDirectory(), file ), null, zoot.ZManager.CANCEL_IS_WORTHWHILE );
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumHeight( super.getPreferredSize(), 256 );
	}
	
	public static void main( String[] args ) {
		java.io.File thumbnailRoot = new java.io.File( org.alice.apis.moveandturn.gallery.GalleryModel.getGalleryRootDirectory(), "thumbnails" );
		zoot.ZFrame frame = new zoot.ZFrame() {
			@Override
			protected void handleWindowOpened( java.awt.event.WindowEvent e ) {
			}
			@Override
			protected void handleQuit( java.util.EventObject e ) {
				System.exit( 0 );
			}
		};
		frame.setSize( new java.awt.Dimension( 1024, 256 ) );
		frame.getContentPane().add( new GalleryBrowser( thumbnailRoot, null ) );
		frame.setVisible( true );
	}
}
