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

class ImageView extends javax.swing.JComponent {
	private java.awt.image.BufferedImage bufferedImage;
	private int desiredSize;
	public ImageView( java.awt.image.BufferedImage bufferedImage, int desiredSize ) {
		this.bufferedImage = bufferedImage;
		this.desiredSize = desiredSize;
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		if( this.bufferedImage != null ) {
			double aspectRatio = this.bufferedImage.getWidth() / (double)this.bufferedImage.getHeight();
			int width;
			int height;
			if( this.bufferedImage.getWidth() > (double)this.bufferedImage.getHeight() ) {
				width = this.desiredSize;
				height = (int)(width / aspectRatio);
			} else {
				height = this.desiredSize;
				width = (int)(height * aspectRatio);
			}
			return new java.awt.Dimension( width, height );
		} else {
			return super.getPreferredSize();
		}
	}
//	@Override
//	public java.awt.Dimension getMaximumSize() {
//		return this.getPreferredSize();
//	}

	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		//super.paintComponent( g );
		if( this.bufferedImage != null ) {
			java.awt.Dimension preferredSize = this.getPreferredSize();

//			int w;
//			int h;
//			double componentAspectRatio = this.getWidth() / (double)this.getHeight();
//			double imageAspectRatio = this.bufferedImage.getWidth() / (double)this.bufferedImage.getHeight();
//			if( componentAspectRatio > imageAspectRatio ) {
//				if( this.getWidth() > this.getHeight() ) {
//					w = this.getWidth();
//					h = (int)(w / imageAspectRatio);
//				} else {
//					h = this.getHeight();
//					w = (int)(h * imageAspectRatio);
//				}
//			} else {
//				if( this.getWidth() > this.getHeight() ) {
//					h = this.getHeight();
//					w = (int)(h * imageAspectRatio);
//				} else {
//					w = this.getWidth();
//					h = (int)(w / imageAspectRatio);
//				}
//			}
			int w = (int)preferredSize.getWidth();
			int h = (int)preferredSize.getHeight();
//			int x = (this.getWidth() - w)/2;
//			int y = (this.getHeight() - h)/2;
			
			int x = 0;
			int y = 0;
			
//			g.setColor( java.awt.Color.BLACK );
//			g.drawRect( x, y, w-1, h-1 );
//			g.drawRect( 0, 0, this.getWidth()-1, this.getHeight()-1 );
			g.drawImage( this.bufferedImage, x, y, x+w, y+h, 0, 0, this.bufferedImage.getWidth(), this.bufferedImage.getHeight(), this );
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
class CreateFieldFromBillboardPane extends org.alice.ide.createdeclarationpanes.CreateLargelyPredeterminedFieldPane {
	public CreateFieldFromBillboardPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, org.alice.apis.moveandturn.Billboard billboard ) {
		super( declaringType, billboard.getClass(), null );
		org.alice.apis.moveandturn.ImageSource frontImageSource = billboard.getFrontImageSource();
		if( frontImageSource != null ) {
			org.alice.virtualmachine.resources.ImageResource imageResource = frontImageSource.getImageResource();
			if( imageResource != null ) {
				java.awt.image.BufferedImage bufferedImage = edu.cmu.cs.dennisc.image.ImageFactory.getBufferedImage( imageResource );
				if( bufferedImage != null ) {
					ImageView imageView = new ImageView( bufferedImage, 240 );
					this.add( new edu.cmu.cs.dennisc.croquet.swing.LineAxisPane( javax.swing.Box.createHorizontalStrut( 8 ), imageView ), java.awt.BorderLayout.EAST );
				} else {
					//todo?
				}
			}
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
class CreateBillboardActionOperation extends AbstractDeclareFieldOperation {
	public CreateBillboardActionOperation() {
		this.putValue( javax.swing.Action.NAME, "Create Billboard..." );
	}
	@Override
	protected edu.cmu.cs.dennisc.pattern.Tuple2< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, Object > createFieldAndInstance( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType ) {
		org.alice.ide.resource.ImageResourcePrompter imageResourcePrompter = org.alice.ide.resource.ImageResourcePrompter.getSingleton();
		try {
			org.alice.virtualmachine.resources.ImageResource frontImageResource = imageResourcePrompter.promptUserForResource( this.getIDE() );
			if( frontImageResource != null ) {
				edu.cmu.cs.dennisc.alice.Project project = this.getIDE().getProject();
				if( project != null ) {
					project.addResource( frontImageResource );
				}
				org.alice.apis.moveandturn.ImageSource frontImageSource = new org.alice.apis.moveandturn.ImageSource( frontImageResource );
				org.alice.apis.moveandturn.Billboard billboard = new org.alice.apis.moveandturn.Billboard();
				billboard.setFrontImageSource( frontImageSource );

				edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType = this.getIDE().getSceneType();
				CreateFieldFromBillboardPane createFieldFromBillboardPane = new CreateFieldFromBillboardPane( declaringType, billboard );
				edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = createFieldFromBillboardPane.showInJDialog( this.getIDE() );
				if( field != null ) {
					//ide.getSceneEditor().handleFieldCreation( declaringType, field, person );
					return new edu.cmu.cs.dennisc.pattern.Tuple2< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, Object >( field, billboard );
				} else {
					return null;
				}

				//				String name = "billboard";
				//				edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type = this.getIDE().getTypeDeclaredInAliceFor(org.alice.apis.moveandturn.Billboard.class);
				//				edu.cmu.cs.dennisc.alice.ast.Expression initializer = org.alice.ide.ast.NodeUtilities.createInstanceCreation(type);
				//				edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = new edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice(name, type, initializer);
				//				field.finalVolatileOrNeither.setValue(edu.cmu.cs.dennisc.alice.ast.FieldModifierFinalVolatileOrNeither.FINAL);
				//				field.access.setValue(edu.cmu.cs.dennisc.alice.ast.Access.PRIVATE);
				//				return new edu.cmu.cs.dennisc.pattern.Tuple2<edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, Object>( field, billboard );
			} else {
				return null;
			}
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
}
