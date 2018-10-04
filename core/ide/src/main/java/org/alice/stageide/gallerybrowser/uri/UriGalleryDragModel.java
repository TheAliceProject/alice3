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
package org.alice.stageide.gallerybrowser.uri;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.java.util.zip.ZipUtilities;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.xml.XMLUtilities;
import org.alice.ide.ast.export.type.FieldInfo;
import org.alice.ide.ast.export.type.FunctionInfo;
import org.alice.ide.ast.export.type.ResourceInfo;
import org.alice.ide.ast.export.type.TypeSummary;
import org.alice.ide.ast.export.type.TypeSummaryDataSource;
import org.alice.ide.ast.export.type.TypeXmlUtitlities;
import org.alice.ide.ast.type.merge.croquet.MembersToolPalette;
import org.alice.ide.croquet.models.ui.formatter.FormatterState;
import org.alice.ide.formatter.Formatter;
import org.alice.nonfree.NebulousIde;
import org.alice.stageide.icons.IconFactoryManager;
import org.alice.stageide.modelresource.AddFieldCascade;
import org.alice.stageide.modelresource.ClassResourceKey;
import org.alice.stageide.modelresource.EnumConstantResourceKey;
import org.alice.stageide.modelresource.InstanceCreatorKey;
import org.alice.stageide.modelresource.ResourceGalleryDragModel;
import org.alice.stageide.modelresource.ResourceKey;
import org.alice.stageide.modelresource.ResourceNode;
import org.lgna.croquet.DropSite;
import org.lgna.croquet.Triggerable;
import org.lgna.croquet.history.DragStep;
import org.lgna.croquet.icon.EmptyIconFactory;
import org.lgna.croquet.icon.IconFactory;
import org.lgna.project.VersionNotSupportedException;
import org.lgna.project.ast.JavaType;
import org.lgna.story.resources.ModelResource;
import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities.getStringForKey;

/**
 * @author Dennis Cosgrove
 */
public final class UriGalleryDragModel extends ResourceGalleryDragModel {
	//public static final java.awt.Dimension URI_LARGE_ICON_SIZE = new java.awt.Dimension( ( getDefaultLargeIconSize().width * 3 ) / 2, getDefaultLargeIconSize().height );

	private static InitializingIfAbsentMap<URI, UriGalleryDragModel> map = Maps.newInitializingIfAbsentHashMap();

	public static UriGalleryDragModel getInstance( URI uri ) {
		return map.getInitializingIfAbsent( uri, new InitializingIfAbsentMap.Initializer<URI, UriGalleryDragModel>() {
			@Override
			public UriGalleryDragModel initialize( URI uri ) {
				return new UriGalleryDragModel( uri );
			}
		} );
	}

	private final URI uri;
	private String text;
	private Map<String, byte[]> mapFilenameToExtractedData;
	private TypeSummary typeSummary;
	private InstanceCreatorKey resourceKey;
	private Class<?> thingCls;
	private IconFactory iconFactory;

	private UriGalleryDragModel( URI uri ) {
		super( UUID.fromString( "9b784c07-6857-4f3f-83c4-ef6f2334c62a" ) );
		this.uri = uri;
	}

	private Map<String, byte[]> getFilenameToExtractedData() {
		if( this.mapFilenameToExtractedData != null ) {
			//pass
		} else {
			try {
				this.mapFilenameToExtractedData = ZipUtilities.extract( new File( this.uri ) );
			} catch( IOException ioe ) {
				throw new RuntimeException( ioe );
			}
		}
		return this.mapFilenameToExtractedData;
	}

	private TypeSummary getTypeSummary() {
		if( this.typeSummary != null ) {
			//pass
		} else {
			Map<String, byte[]> mapFilenameToExtractedData = this.getFilenameToExtractedData();
			byte[] data = mapFilenameToExtractedData.get( TypeSummaryDataSource.FILENAME );
			if( data != null ) {
				Document xmlDocument = XMLUtilities.read( new ByteArrayInputStream( data ) );
				try {
					this.typeSummary = TypeXmlUtitlities.decode( xmlDocument );
				} catch( VersionNotSupportedException vnse ) {
					throw new RuntimeException( vnse );
				}
			}
		}
		return this.typeSummary;
	}

	public InstanceCreatorKey getResourceKey() {
		if( this.resourceKey != null ) {
			//pass
		} else {
			try {
				TypeSummary typeSummary = this.getTypeSummary();
				if( typeSummary != null ) {
					ResourceInfo resourceInfo = typeSummary.getResourceInfo();
					if( resourceInfo != null ) {
						String resourceClassName = resourceInfo.getClassName();
						String resourceFieldName = resourceInfo.getFieldName();
						Class<? extends ModelResource> resourceCls = (Class<? extends ModelResource>)Class.forName( resourceClassName );
						if( resourceFieldName != null ) {
							Field fld = resourceCls.getField( resourceFieldName );
							Enum<? extends ModelResource> enumConstant = (Enum<? extends ModelResource>)fld.get( null );
							this.resourceKey = new EnumConstantResourceKey( enumConstant );
						} else {
							if( NebulousIde.nonfree.isPersonResourceAssignableFrom( resourceCls ) ) {
								this.resourceKey = NebulousIde.nonfree.getPersonResourceKeyInstanceForResourceClass( resourceCls );
							} else {
								this.resourceKey = new ClassResourceKey( resourceCls );
							}
						}
					}
				} else {
					Logger.severe( this );
				}
			} catch( Throwable t ) {
				Logger.throwable( t, this );
			}
		}
		return this.resourceKey;
	}

	private Class<?> getThingCls() {
		if( this.thingCls != null ) {
			//pass
		} else {
			try {
				TypeSummary typeSummary = this.getTypeSummary();
				if( typeSummary != null ) {
					List<String> hierarchyClsNames = typeSummary.getHierarchyClassNames();
					if( hierarchyClsNames.size() > 0 ) {
						String clsName = hierarchyClsNames.get( hierarchyClsNames.size() - 1 );
						this.thingCls = Class.forName( clsName );
					}
				} else {
					Logger.severe( this );
				}
			} catch( Throwable t ) {
				Logger.throwable( t, this );
			}
		}
		return this.thingCls;
	}

	private void appendStartIfNecessary( StringBuilder sb ) {
		if( sb.length() > 0 ) {
			//pass
		} else {
			sb.append( "<html>" );
			File file = new File( this.uri );
			if( file.exists() ) {
				sb.append( "add from file: <strong>" );
				sb.append( file.getName() );
				sb.append( "</strong><p><p>" );
			} else {
				//todo
			}
		}
	}

	public String getTypeSummaryToolTipText() {
		TypeSummary typeSummary = getTypeSummary();
		if( typeSummary != null ) {
			StringBuilder sb = new StringBuilder();
			List<String> procedureNames = typeSummary.getProcedureNames();
			if( procedureNames.size() > 0 ) {
				this.appendStartIfNecessary( sb );
				sb.append( "<em>procedures:</em><ul>" );
				for( String procedureName : procedureNames ) {
					sb.append( "<li><strong>" );
					sb.append( procedureName );
					sb.append( "</strong>" );
				}
				sb.append( "</ul>" );
			}

			List<FunctionInfo> functionInfos = typeSummary.getFunctionInfos();
			if( functionInfos.size() > 0 ) {
				this.appendStartIfNecessary( sb );
				sb.append( "<em>functions:</em><ul>" );
				for( FunctionInfo functionInfo : functionInfos ) {
					sb.append( "<li>" );
					sb.append( functionInfo.getReturnClassName() );
					sb.append( " <strong>" );
					sb.append( functionInfo.getName() );
					sb.append( "</strong>" );
				}
				sb.append( "</ul>" );
			}
			List<FieldInfo> fieldInfos = typeSummary.getFieldInfos();
			if( fieldInfos.size() > 0 ) {
				this.appendStartIfNecessary( sb );
				sb.append( "<em>properties:</em><ul>" );
				for( FieldInfo fieldInfo : fieldInfos ) {
					sb.append( "<li>" );
					sb.append( fieldInfo.getValueClassName() );
					sb.append( " <strong>" );
					sb.append( fieldInfo.getName() );
					sb.append( "</strong>" );
				}
				sb.append( "</ul>" );
			}
			if( sb.length() > 0 ) {
				//pass
			} else {
				sb.append( "<html>nothing of note" );
			}
			sb.append( "</html>" );
			return sb.toString();
		} else {
			return "unknown";
		}
	}

	@Override
	protected void localize() {
		TypeSummary typeSummary = getTypeSummary();
		String typeName = typeSummary != null ? typeSummary.getTypeName() : "???";

		InstanceCreatorKey resourceKey = this.getResourceKey();
		if( resourceKey != null ) {
			Class<?> modelResourceCls = resourceKey.getModelResourceCls();
			if( ( modelResourceCls != null ) && modelResourceCls.isInterface() ) {
				this.text = typeName;
			} else {
				this.text = resourceKey.getLocalizedDisplayText();
			}
		} else {
			Formatter formatter = FormatterState.getInstance().getValue();
			this.text = String.format(formatter.getNewFormat(), typeName, "");
		}

		if( typeName != null ) {
			File file = new File( this.uri );
			if( file.exists() ) {
				String baseName = FileUtilities.getBaseName( file );
				if (!typeName.contentEquals( baseName )) {
					// Use existing l18n string and remove trailing :
					String fromFormat = ResourceBundleUtilities.getStringForKey( "MembersToolPalette.fromImportHeader", MembersToolPalette.class );
					fromFormat = fromFormat.replaceFirst(":", "" );
					fromFormat = fromFormat.replaceFirst("</filename/>", file.getName() );
					this.text = "<html>" + this.text + " <em>" + fromFormat + "</em></html>";
				}
			}
		}
	}

	@Override
	public final String getText() {
		return this.text;
	}

	@Override
	public boolean isInstanceCreator() {
		return true;
		//		org.alice.stageide.modelresource.InstanceCreatorKey resourceKey = this.getResourceKey();
		//		if( resourceKey != null ) {
		//			Class<?> modelResourceCls = resourceKey.getModelResourceCls();
		//			if( ( modelResourceCls != null ) && modelResourceCls.isInterface() ) {
		//				return false;
		//			} else {
		//				return true;
		//			}
		//		} else {
		//			return true;
		//		}
	}

	@Override
	public AxisAlignedBox getBoundingBox() {
		InstanceCreatorKey resourceKey = getResourceKey();
		if( resourceKey != null ) {
			return resourceKey.getBoundingBox();
		} else {
			return null;
		}
	}

	@Override
	public boolean placeOnGround() {
		InstanceCreatorKey resourceKey = this.getResourceKey();
		if( resourceKey != null ) {
			return resourceKey.getPlaceOnGround();
		} else {
			return false;
		}
	}

	@Override
	public List<ResourceNode> getNodeChildren() {
		ResourceKey resourceKey = this.getResourceKey();
		if( resourceKey instanceof ClassResourceKey ) {
			ClassResourceKey classResourceKey = (ClassResourceKey)resourceKey;
			Class<? extends ModelResource> modelResourceClass = classResourceKey.getModelResourceCls();
			Class<?> thingCls = this.getThingCls();
			if( modelResourceClass != null ) {
				if( modelResourceClass.isEnum() ) {
					List<ResourceNode> rv = Lists.newLinkedList();
					for( ModelResource modelResource : modelResourceClass.getEnumConstants() ) {
						rv.add( new UriBasedResourceNode( new EnumConstantResourceKey( (Enum)modelResource ), thingCls, this.uri ) );
					}
					return rv;
				} else {
					return Collections.emptyList();
				}
			} else {
				return Collections.emptyList();
			}
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public Triggerable getLeftButtonClickOperation() {
		ResourceKey resourceKey = this.getResourceKey();
		Class<?> thingCls = this.getThingCls();
		return ResourceKeyUriIteratingOperation.getInstance( resourceKey, thingCls, this.uri );
	}

	@Override
	public Triggerable getDropOperation( DragStep step, DropSite dropSite ) {
		ResourceKey resourceKey = this.getResourceKey();
		if( resourceKey instanceof EnumConstantResourceKey ) {
			return this.getLeftButtonClickOperation();
		} else if( resourceKey instanceof ClassResourceKey ) {
			ClassResourceKey classResourceKey = (ClassResourceKey)resourceKey;
			if( classResourceKey.isLeaf() ) {
				//should not happen
				return null;
			} else {
				return new AddFieldCascade( this, dropSite );
			}
		} else if( NebulousIde.nonfree.isInstanceOfPersonResourceKey( resourceKey ) ) {
			return this.getLeftButtonClickOperation();
		} else {
			Class<?> thingCls = this.getThingCls();
			if( thingCls != null ) {
				return this.getLeftButtonClickOperation();
			} else {
				return null;
			}
		}
	}

	@Override
	public IconFactory getIconFactory() {
		if( this.iconFactory != null ) {
			//pass
		} else {
			IconFactory base;
			ResourceKey resourceKey = this.getResourceKey();
			if( resourceKey != null ) {
				base = resourceKey.getIconFactory();
			} else {
				Class<?> thingCls = this.getThingCls();
				if( thingCls != null ) {
					base = IconFactoryManager.getIconFactoryForType( JavaType.getInstance( thingCls ) );
				} else {
					base = EmptyIconFactory.getInstance();
				}
			}
			//this.iconFactory = new org.alice.stageide.icons.UriGalleryIconFactory( this.uri, base, getDefaultLargeIconSize(), this.getIconSize() );
			this.iconFactory = base;
		}
		return this.iconFactory;
	}

	//	@Override
	//	public java.awt.Dimension getIconSize() {
	//		return URI_LARGE_ICON_SIZE;
	//	}

	@Override
	protected void appendRepr( StringBuilder sb ) {
		super.appendRepr( sb );
		sb.append( this.uri );
	}
}
