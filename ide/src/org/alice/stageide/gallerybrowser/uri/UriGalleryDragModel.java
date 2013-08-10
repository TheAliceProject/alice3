/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.stageide.gallerybrowser.uri;

/**
 * @author Dennis Cosgrove
 */
public class UriGalleryDragModel extends org.alice.ide.croquet.models.gallerybrowser.GalleryDragModel {
	private static edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap<java.net.URI, UriGalleryDragModel> map = edu.cmu.cs.dennisc.java.util.Collections.newInitializingIfAbsentHashMap();

	public static UriGalleryDragModel getInstance( java.net.URI uri ) {
		return map.getInitializingIfAbsent( uri, new edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap.Initializer<java.net.URI, UriGalleryDragModel>() {
			public UriGalleryDragModel initialize( java.net.URI uri ) {
				return new UriGalleryDragModel( uri );
			}
		} );
	}

	private final java.net.URI uri;
	private String text;
	private java.util.Map<String, byte[]> mapFilenameToExtractedData;
	private org.alice.ide.ast.export.type.TypeSummary typeSummary;
	private org.alice.stageide.modelresource.ResourceKey resourceKey;

	private UriGalleryDragModel( java.net.URI uri ) {
		super( java.util.UUID.fromString( "9b784c07-6857-4f3f-83c4-ef6f2334c62a" ) );
		this.uri = uri;
	}

	private java.util.Map<String, byte[]> getFilenameToExtractedData() {
		if( this.mapFilenameToExtractedData != null ) {
			//pass
		} else {
			try {
				this.mapFilenameToExtractedData = edu.cmu.cs.dennisc.java.util.zip.ZipUtilities.extract( new java.io.File( this.uri ) );
			} catch( java.io.IOException ioe ) {
				throw new RuntimeException( ioe );
			}
		}
		return this.mapFilenameToExtractedData;
	}

	private org.alice.ide.ast.export.type.TypeSummary getTypeSummary() {
		if( this.typeSummary != null ) {
			//pass
		} else {
			java.util.Map<String, byte[]> mapFilenameToExtractedData = this.getFilenameToExtractedData();
			byte[] data = mapFilenameToExtractedData.get( org.alice.ide.ast.export.type.TypeSummaryDataSource.FILENAME );
			if( data != null ) {
				org.w3c.dom.Document xmlDocument = edu.cmu.cs.dennisc.xml.XMLUtilities.read( new java.io.ByteArrayInputStream( data ) );
				try {
					this.typeSummary = org.alice.ide.ast.export.type.TypeXmlUtitlities.decode( xmlDocument );
				} catch( org.lgna.project.VersionNotSupportedException vnse ) {
					throw new RuntimeException( vnse );
				}
			}
		}
		return this.typeSummary;
	}

	private org.alice.stageide.modelresource.ResourceKey getResourceKey() {
		if( this.resourceKey != null ) {
			//pass
		} else {
			try {
				org.alice.ide.ast.export.type.TypeSummary typeSummary = this.getTypeSummary();
				org.alice.ide.ast.export.type.ResourceInfo resourceInfo = typeSummary.getResourceInfo();
				if( resourceInfo != null ) {
					String resourceClassName = resourceInfo.getClassName();
					String resourceFieldName = resourceInfo.getFieldName();
					Class<? extends org.lgna.story.resources.ModelResource> resourceCls = (Class<? extends org.lgna.story.resources.ModelResource>)Class.forName( resourceClassName );
					if( resourceFieldName != null ) {
						java.lang.reflect.Field fld = resourceCls.getField( resourceFieldName );
						Enum<? extends org.lgna.story.resources.ModelResource> enumConstant = (Enum<? extends org.lgna.story.resources.ModelResource>)fld.get( null );
						this.resourceKey = new org.alice.stageide.modelresource.EnumConstantResourceKey( enumConstant );
					} else {
						this.resourceKey = new org.alice.stageide.modelresource.ClassResourceKey( resourceCls );
					}
				}
			} catch( Throwable t ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( t, this );
			}
		}
		return this.resourceKey;
	}

	@Override
	protected void localize() {
		super.localize();
		org.alice.ide.ast.export.type.TypeSummary typeSummary = getTypeSummary();
		String typeName = typeSummary != null ? typeSummary.getTypeName() : "???";
		this.text = "new " + typeName + "()";
	}

	@Override
	public final String getText() {
		return this.text;
	}

	@Override
	public boolean isInstanceCreator() {
		return true;
	}

	@Override
	public edu.cmu.cs.dennisc.math.AxisAlignedBox getBoundingBox() {
		return edu.cmu.cs.dennisc.math.AxisAlignedBox.createNaN();
	}

	@Override
	public boolean placeOnGround() {
		return false;
	}

	@Override
	public org.lgna.croquet.Model getLeftButtonClickModel() {
		return org.alice.stageide.ast.declaration.AddTorusManagedFieldComposite.getInstance().getOperation();
	}

	@Override
	public org.lgna.croquet.Model getDropModel( org.lgna.croquet.history.DragStep step, org.lgna.croquet.DropSite dropSite ) {
		return this.getLeftButtonClickModel();
	}

	@Override
	public org.lgna.croquet.icon.IconFactory getIconFactory() {
		org.alice.stageide.modelresource.ResourceKey resourceKey = this.getResourceKey();
		if( resourceKey != null ) {
			return resourceKey.getIconFactory();
		} else {
			return org.lgna.croquet.icon.EmptyIconFactory.getInstance();
		}
	}
}
