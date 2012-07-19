/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
package org.alice.stageide.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
public class ResourceManager {
//	private static final int SMALL_ICON_SIZE = 24;
//	private static final String PACKAGE_NAME_PREFIX = ResourceManager.class.getPackage().getName();
////	private static java.util.Map< org.lgna.project.ast.JavaType, javax.swing.Icon > typeToIconMap = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
//	public static final javax.swing.Icon NULL_SMALL_ICON = new edu.cmu.cs.dennisc.javax.swing.icons.ShapeIcon( new java.awt.geom.Ellipse2D.Float( 0, 0, SMALL_ICON_SIZE - 8, SMALL_ICON_SIZE - 8 ), java.awt.Color.LIGHT_GRAY, java.awt.Color.DARK_GRAY, 4, 4, 4, 4 );
//
	private static interface ResourceDeclaration {
		public org.lgna.croquet.icon.IconFactory createIconFactory();
	}
	private static abstract class UrlResourceDeclaration implements ResourceDeclaration {
		protected abstract Class< ? extends org.lgna.story.resources.ModelResource > getModelResourceClass();
		protected abstract String getModelResourceName();
		public final org.lgna.croquet.icon.IconFactory createIconFactory() {
			Class< ? extends org.lgna.story.resources.ModelResource > modelResourceCls = this.getModelResourceClass();
			String modelResourceName = this.getModelResourceName();
			if( modelResourceName != null ) {
				//pass
			} else {
				if( org.alice.ide.croquet.models.gallerybrowser.TypeGalleryNode.getSetOfClassesWithIcons().contains( modelResourceCls ) ) {
					javax.swing.ImageIcon imageIcon = org.alice.ide.croquet.models.gallerybrowser.TypeGalleryNode.getIcon( modelResourceCls );
					return new org.lgna.croquet.icon.ImageIconFactory( imageIcon );
				}
			}
			java.net.URL url = org.lgna.story.implementation.alice.AliceResourceUtilties.getThumbnailURL( modelResourceCls, modelResourceName );
			if( url != null ) {
				return new org.lgna.croquet.icon.ImageIconFactory( url ); 
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( modelResourceCls, modelResourceName );
				return org.lgna.croquet.icon.EmptyIconFactory.SINGLETON;
			}
		}
	}
	private static final class ResourceEnumConstant extends UrlResourceDeclaration {
		private final Enum< ? extends org.lgna.story.resources.ModelResource > enm;
		public ResourceEnumConstant( Enum< ? extends org.lgna.story.resources.ModelResource > enm ) {
			assert enm != null;
			this.enm = enm;
		}
		@Override
		protected Class<? extends org.lgna.story.resources.ModelResource> getModelResourceClass() {
			//todo?
			return (Class< ? extends org.lgna.story.resources.ModelResource >)this.enm.getClass();
		}
		@Override
		protected String getModelResourceName() {
			return this.enm.name();
		}
		@Override
		public boolean equals( Object obj ) {
			if( this == obj ) {
				return true;
			}
			if( obj instanceof ResourceEnumConstant ) {
				ResourceEnumConstant other = (ResourceEnumConstant)obj;
				return this.enm.equals( other.enm );
			}
			return false;
		}
		@Override
		public int hashCode() {
			return this.enm.hashCode();
		}
	}
	private static final class ResourceType extends UrlResourceDeclaration {
		private final Class< ? extends org.lgna.story.resources.ModelResource > cls;
		public ResourceType( Class< ? extends org.lgna.story.resources.ModelResource > cls ) {
			assert cls != null;
			this.cls = cls;
		}
		@Override
		protected Class<? extends org.lgna.story.resources.ModelResource> getModelResourceClass() {
			return this.cls;
		}
		@Override
		protected String getModelResourceName() {
			return null;
		}
		@Override
		public boolean equals( Object obj ) {
			if( this == obj ) {
				return true;
			}
			if( obj instanceof ResourceType ) {
				ResourceType other = (ResourceType)obj;
				return this.cls.equals( other.cls );
			}
			return false;
		}
		@Override
		public int hashCode() {
			return this.cls.hashCode();
		}
	}
	
	private static final class ResourceInstance implements ResourceDeclaration {
		private final org.lgna.story.resources.sims2.PersonResource instance;
		public ResourceInstance( org.lgna.story.resources.sims2.PersonResource instance ) {
			assert instance != null;
			this.instance = instance;
		}
		public org.lgna.croquet.icon.IconFactory createIconFactory() {
			try {
				java.awt.image.BufferedImage image = org.lgna.story.resourceutilities.ThumbnailMaker.getInstance().createThumbnailFromPersonResource( this.instance );
				return new org.lgna.croquet.icon.ImageIconFactory( image );
			} catch( Exception e ) {
				return org.lgna.croquet.icon.EmptyIconFactory.SINGLETON;
			}
		}
		@Override
		public boolean equals( Object obj ) {
			if( this == obj ) {
				return true;
			}
			if( obj instanceof ResourceInstance ) {
				ResourceInstance other = (ResourceInstance)obj;
				return this.instance.equals( other.instance );
			}
			return false;
		}
		@Override
		public int hashCode() {
			return this.instance.hashCode();
		}
	}

	private static java.util.Map< org.lgna.project.ast.JavaType, org.lgna.croquet.icon.IconFactory > mapTypeToIconFactory = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private static java.util.Map< ResourceDeclaration, org.lgna.croquet.icon.IconFactory > mapResourceDeclarationToIconFactory = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private static java.util.Map< org.lgna.story.Color, org.lgna.croquet.icon.IconFactory > mapColorToCameraMarkerIconFactory = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private static java.util.Map< org.lgna.story.Color, org.lgna.croquet.icon.IconFactory > mapColorToObjectMarkerIconFactory = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	
	private ResourceManager() {
	}
	public static void registerIconFactory( org.lgna.project.ast.JavaType javaType, org.lgna.croquet.icon.IconFactory iconFactory ) {
		mapTypeToIconFactory.put( javaType, iconFactory );
	}
	public static void registerIconFactory( Class< ? > cls, org.lgna.croquet.icon.IconFactory iconFactory ) {
		registerIconFactory( org.lgna.project.ast.JavaType.getInstance( cls ), iconFactory );
	}

//	private static javax.swing.Icon getSmallIconFor( javax.swing.Icon largeIcon ) {
//		if( largeIcon != null ) {
//			//return new edu.cmu.cs.dennisc.javax.swing.icons.ScaledImageIcon( ((javax.swing.ImageIcon)largeIcon).getImage(), SMALL_ICON_SIZE, SMALL_ICON_SIZE );
//			return new edu.cmu.cs.dennisc.javax.swing.icons.ScaledIcon( largeIcon, SMALL_ICON_SIZE, SMALL_ICON_SIZE );
//		} else {
//			return NULL_SMALL_ICON;
//		}
//	}
//	private static java.net.URL getLargeIconResource( edu.cmu.cs.dennisc.javax.swing.models.TreeNode< String > treeNode ) {
//		if( treeNode != null ) {
//			String path = treeNode.getValue();
//			String resourceName = path.substring( PACKAGE_NAME_PREFIX.length() + 1 );
//			//			if( resourceName.startsWith( "images/org/alice/apis/moveandturn/gallery/environments/grounds/" ) ) {
//			//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: handle grounds" );
//			//				return null;
//			//			} else {
//			java.net.URL rv = ResourceManager.class.getResource( resourceName );
//			if( rv != null ) {
//				//pass
//			} else {
//				edu.cmu.cs.dennisc.java.util.logging.Logger.warning( "failed to getResource:", resourceName );
//			}
//			return rv;
//			//			}
//		} else {
//			return null;
//		}
//	}
//	public static javax.swing.Icon getLargeIcon( edu.cmu.cs.dennisc.javax.swing.models.TreeNode< String > treeNode ) {
//		return edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( getLargeIconResource( treeNode ) );
//	}
//	public static javax.swing.Icon getSmallIcon( edu.cmu.cs.dennisc.javax.swing.models.TreeNode< String > treeNode ) {
//		return getSmallIconFor( getLargeIcon( treeNode ) );
//		//		javax.swing.Icon largeIcon = getLargeIcon(treeNode);
//		//		if( largeIcon != null ) {
//		//			return new edu.cmu.cs.dennisc.javax.swing.icons.ScaledIcon(largeIcon, SMALL_ICON_SIZE, SMALL_ICON_SIZE );
//		//		} else {
//		//			return null;
//		//		}
//	}
//
//	private static String getLargeIconResourceNameForClassName( String className ) {
//		return "images/" + className.replace( '.', '/' ) + ".png";
//	}
//
//	private static java.net.URL getLargeIconResourceForGalleryClassName( String className ) {
//		if( className != null ) {
//			//			if( className.startsWith( "org.alice.apis.moveandturn.gallery.environments.grounds." ) ) {
//			//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: handle grounds" );
//			//				return null;
//			//			} else {
//			return ResourceManager.class.getResource( getLargeIconResourceNameForClassName( className ) );
//			//			}
//		} else {
//			return null;
//		}
//	}
//	public static javax.swing.Icon getLargeIconForGalleryClassName( String className ) {
//		return edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( getLargeIconResourceForGalleryClassName( className ) );
//	}
//	public static javax.swing.Icon getSmallIconForGalleryClassName( String className ) {
//		return getSmallIconFor( getLargeIconForGalleryClassName( className ) );
//	}
//
//	public static java.net.URL getLargeIconResourceForType( org.lgna.project.ast.AbstractType< ?, ?, ? > type ) {
//		String className;
//		if( type != null ) {
//			org.lgna.project.ast.JavaType javaType = type.getFirstEncounteredJavaType();
//			className = javaType.getClassReflectionProxy().getName();
//		} else {
//			className = null;
//		}
//		return getLargeIconResourceForGalleryClassName( className );
//	}
//
//	public static javax.swing.Icon getLargeIconForType( org.lgna.project.ast.AbstractType< ?, ?, ? > type ) {
//		java.net.URL url = getLargeIconResourceForType( type );
////		if( url != null ) {
////			//pass
////		} else {
////			//url = ModelResourceUtilities.getThumbnailURL( modelResource );
////		}
//		return edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( url );
//	}
//
//	public static javax.swing.Icon getSmallIconForType( org.lgna.project.ast.AbstractType< ?, ?, ? > type ) {
//		if( type != null ) {
//			org.lgna.project.ast.JavaType javaType = type.getFirstEncounteredJavaType();
//			if( mapTypeToIconFactory.containsKey( javaType ) ) {
//				return mapTypeToIconFactory.get( javaType ).getIcon( org.lgna.croquet.icon.IconSize.SMALL.getSize() );
//			} else {
//				return getSmallIconFor( getLargeIconForType( type ) );
//			}
//		} else {
//			return null;
//		}
//	}
//
//	private static final java.awt.Dimension SMALL_SIZE = new java.awt.Dimension( 32, 24 );
//	public static javax.swing.Icon getLargeIconForField( org.lgna.project.ast.AbstractField field ) {
//		if( field instanceof org.lgna.project.ast.UserField ) {
//			org.lgna.project.ast.UserField userField = (org.lgna.project.ast.UserField)field;
//			org.lgna.project.ast.Expression initializer = userField.initializer.getValue();
//			if( initializer instanceof org.lgna.project.ast.InstanceCreation ) {
//				org.lgna.project.ast.InstanceCreation instanceCreation = (org.lgna.project.ast.InstanceCreation)initializer;
//				org.lgna.project.ast.JavaField argumentField = org.alice.ide.typemanager.ConstructorArgumentUtilities.getArgumentField( instanceCreation );
//				if( argumentField != null ) {
//					if( argumentField.isStatic() ) {
//						java.lang.reflect.Field fld = argumentField.getFieldReflectionProxy().getReification();
//						try {
//							Object o = fld.get( null );
//							if( o != null ) {
//								if( o.getClass().isEnum() ) {
//									Enum< ? > e = (Enum< ? >)o;
//									return edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( org.lgna.story.implementation.alice.AliceResourceUtilties.getThumbnailURL( e.getClass(), e.name() ) );
//								}
//							}
//						} catch( IllegalAccessException iae ) {
//							iae.printStackTrace();
//							return null;
//						}
//					}
//				}
//			}
//		}
//		return null;
//	}
//	public static javax.swing.Icon getSmallIconForField( org.lgna.project.ast.AbstractField field ) {
//		if( field != null ) {
//			org.lgna.project.ast.AbstractType<?,?,?> type = field.getValueType();
//			if( type != null ) {
//				org.lgna.project.ast.JavaType javaType = type.getFirstEncounteredJavaType();
//				if( mapTypeToIconFactory.containsKey( javaType ) ) {
//					return mapTypeToIconFactory.get( javaType ).getIcon( SMALL_SIZE );
//				}
//			}
//		}
//		return getSmallIconFor( getLargeIconForField( field ) );
//	}
	
	private static ResourceDeclaration createResourceDeclarationFromRequiredArguments( org.lgna.project.ast.SimpleArgumentListProperty requiredArguments ) {
		if( requiredArguments.size() == 1 ) {
			org.lgna.project.ast.SimpleArgument arg0 = requiredArguments.get( 0 );
			org.lgna.project.ast.Expression expression0 = arg0.expression.getValue();
			if( expression0 instanceof org.lgna.project.ast.InstanceCreation ) {
				Object instance = org.alice.stageide.StageIDE.getActiveInstance().getSceneEditor().getInstanceInJavaVMForExpression( expression0 );
				if( instance instanceof org.lgna.story.resources.sims2.PersonResource ) {
					org.lgna.story.resources.sims2.PersonResource personResource = (org.lgna.story.resources.sims2.PersonResource)instance;
					return new ResourceInstance( personResource );
				}
			}
		}
		org.lgna.project.ast.JavaField argumentField = org.alice.ide.typemanager.ConstructorArgumentUtilities.getField( requiredArguments );
		if( argumentField != null ) {
			if( argumentField.isStatic() ) {
				java.lang.reflect.Field fld = argumentField.getFieldReflectionProxy().getReification();
				try {
					Object o = fld.get( null );
					if( o != null ) {
						if( o instanceof org.lgna.story.resources.ModelResource ) {
							if( o.getClass().isEnum() ) {
								Enum< ? extends org.lgna.story.resources.ModelResource > e = (Enum< ? extends org.lgna.story.resources.ModelResource >)o;
								return new ResourceEnumConstant( e );
							}
						}
					}
				} catch( IllegalAccessException iae ) {
					iae.printStackTrace();
					return null;
				}
			}
		}

		return null;
	}
	private static ResourceDeclaration createResourceDeclarationFromField( org.lgna.project.ast.UserField userField ) {
		org.lgna.project.ast.Expression initializer = userField.initializer.getValue();
		if( initializer instanceof org.lgna.project.ast.InstanceCreation ) {
			org.lgna.project.ast.InstanceCreation instanceCreation = (org.lgna.project.ast.InstanceCreation)initializer;
			return createResourceDeclarationFromRequiredArguments( instanceCreation.requiredArguments );
		}
		return null;
	}
	
	private static org.lgna.croquet.icon.IconFactory getRegisteredIconFactory( org.lgna.project.ast.AbstractType<?,?,?> type ) {
		if( type != null ) {
			org.lgna.project.ast.JavaType javaType = type.getFirstEncounteredJavaType();
			if( mapTypeToIconFactory.containsKey( javaType ) ) {
				return mapTypeToIconFactory.get( javaType );
			}
		}
		return null;
	}
	public static org.lgna.croquet.icon.IconFactory getIconFactoryForType( org.lgna.project.ast.AbstractType<?,?,?> type ) {
		org.lgna.croquet.icon.IconFactory iconFactory = getRegisteredIconFactory( type );
		if( iconFactory != null ) {
			return iconFactory;
		} else {
			ResourceDeclaration resourceDeclaration = null;
			org.lgna.project.ast.AbstractConstructor constructor0 = org.alice.ide.typemanager.ConstructorArgumentUtilities.getContructor0( type );
			java.util.ArrayList<? extends org.lgna.project.ast.AbstractParameter> parameters = constructor0.getRequiredParameters();
			switch( parameters.size() ) {
			case 0:
				if( constructor0 instanceof org.lgna.project.ast.UserConstructor ) {
					org.lgna.project.ast.NamedUserConstructor userConstructor0 = (org.lgna.project.ast.NamedUserConstructor)constructor0;
					org.lgna.project.ast.ConstructorInvocationStatement constructorInvocationStatement = userConstructor0.body.getValue().constructorInvocationStatement.getValue();
					resourceDeclaration = createResourceDeclarationFromRequiredArguments( constructorInvocationStatement.requiredArguments );
				}
				break;
			case 1:
				org.lgna.project.ast.AbstractParameter parameter0 = parameters.get( 0 );
				org.lgna.project.ast.AbstractType<?,?,?> parameter0Type = parameter0.getValueType();
				if( parameter0Type != null ) {
					if( parameter0Type.isAssignableTo( org.lgna.story.resources.ModelResource.class ) ) {
						Class<? extends org.lgna.story.resources.ModelResource> cls = (Class<? extends org.lgna.story.resources.ModelResource>)parameter0Type.getFirstEncounteredJavaType().getClassReflectionProxy().getReification();
						resourceDeclaration = new ResourceType( cls );
					}
				}
				break;
			}
			if( resourceDeclaration != null ) {
				iconFactory = mapResourceDeclarationToIconFactory.get( resourceDeclaration );
				if( iconFactory != null ) {
					//pass
				} else {
					iconFactory = resourceDeclaration.createIconFactory();
					mapResourceDeclarationToIconFactory.put( resourceDeclaration, iconFactory );
				}
				return iconFactory;
			}
		}
		return org.lgna.croquet.icon.EmptyIconFactory.SINGLETON;
	}
	public static org.lgna.croquet.icon.IconFactory getIconFactoryForField( org.lgna.project.ast.AbstractField field ) {
		if( field != null ) {
			org.lgna.croquet.icon.IconFactory iconFactory = getRegisteredIconFactory( field.getValueType() );
			if( iconFactory != null ) {
				return iconFactory;
			}
			if( field instanceof org.lgna.project.ast.UserField ) {
				org.lgna.project.ast.UserField userField = (org.lgna.project.ast.UserField)field;
				ResourceDeclaration resourceDeclaration = createResourceDeclarationFromField( userField );
				if( resourceDeclaration != null ) {
					iconFactory = mapResourceDeclarationToIconFactory.get( resourceDeclaration );
					if( iconFactory != null ) {
						//pass
					} else {
						iconFactory = resourceDeclaration.createIconFactory();
						mapResourceDeclarationToIconFactory.put( resourceDeclaration, iconFactory );
					}
					return iconFactory;
				}
			}
		}
		return org.lgna.croquet.icon.EmptyIconFactory.SINGLETON;
	}
	
	public static org.lgna.croquet.icon.IconFactory getIconFactoryForCameraMarker( org.lgna.story.Color color ) {
		org.lgna.croquet.icon.IconFactory rv = mapColorToCameraMarkerIconFactory.get( color );
		if( rv != null ) {
			//pass
		} else {
			javax.swing.ImageIcon imageIcon = null; //todo
			rv = new org.lgna.croquet.icon.ImageIconFactory( imageIcon );
			mapColorToCameraMarkerIconFactory.put( color, rv );
		}
		return rv;
	}
	public static org.lgna.croquet.icon.IconFactory getIconFactoryForObjectMarker( org.lgna.story.Color color ) {
		org.lgna.croquet.icon.IconFactory rv = mapColorToObjectMarkerIconFactory.get( color );
		if( rv != null ) {
			//pass
		} else {
			javax.swing.ImageIcon imageIcon = null; //todo
			rv = new org.lgna.croquet.icon.ImageIconFactory( imageIcon );
			mapColorToObjectMarkerIconFactory.put( color, rv );
		}
		return rv;
	}
}
