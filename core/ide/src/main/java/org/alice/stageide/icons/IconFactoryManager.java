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
package org.alice.stageide.icons;

/**
 * @author Dennis Cosgrove
 */
public class IconFactoryManager {
	private static interface ResourceDeclaration {
		public org.lgna.croquet.icon.IconFactory createIconFactory();
	}

	private static java.util.Set<Class<? extends org.lgna.story.resources.JointedModelResource>> setOfClassesWithIcons = edu.cmu.cs.dennisc.java.util.Sets.newHashSet(
			org.lgna.story.resources.BipedResource.class,
			org.lgna.story.resources.FishResource.class,
			org.lgna.story.resources.FlyerResource.class,
			org.lgna.story.resources.PropResource.class,
			org.lgna.story.resources.QuadrupedResource.class,
			org.lgna.story.resources.SwimmerResource.class,
			org.lgna.story.resources.MarineMammalResource.class,
			org.lgna.story.resources.TransportResource.class,
			org.lgna.story.resources.AutomobileResource.class,
			org.lgna.story.resources.AircraftResource.class,
			org.lgna.story.resources.WatercraftResource.class,
			org.lgna.story.resources.TrainResource.class
			);

	public static java.util.Set<Class<? extends org.lgna.story.resources.JointedModelResource>> getSetOfClassesWithIcons() {
		return setOfClassesWithIcons;
	}

	private static javax.swing.ImageIcon getIcon( Class<?> cls, boolean isSmall ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "images/" );
		sb.append( cls.getName().replace( ".", "/" ) );
		if( isSmall ) {
			sb.append( "_small" );
		}
		sb.append( ".png" );
		return edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( org.alice.ide.croquet.models.gallerybrowser.GalleryDragModel.class.getResource( sb.toString() ) );
	}

	private static abstract class UrlResourceDeclaration implements ResourceDeclaration {
		protected abstract Class<? extends org.lgna.story.resources.ModelResource> getModelResourceClass();

		protected abstract String getModelResourceName();

		@Override
		public final org.lgna.croquet.icon.IconFactory createIconFactory() {
			Class<? extends org.lgna.story.resources.ModelResource> modelResourceCls = this.getModelResourceClass();
			String modelResourceName = this.getModelResourceName();
			if( modelResourceName != null ) {
				//pass
			} else {
				if( getSetOfClassesWithIcons().contains( modelResourceCls ) ) {
					javax.swing.ImageIcon smallIcon = getIcon( modelResourceCls, true );
					javax.swing.ImageIcon largeIcon = getIcon( modelResourceCls, false );
					return new org.lgna.croquet.icon.MultipleSourceImageIconFactory( 1, smallIcon, largeIcon );
					//return new org.lgna.croquet.icon.TrimmedImageIconFactory( imageIcon, 160, 120 );
				}
			}
			//			if( modelResourceName != null ) {
			//				//pass
			//			} else {
			//				if( modelResourceCls.isEnum() ) {
			//					org.lgna.story.resources.ModelResource[] constants = modelResourceCls.getEnumConstants();
			//					if( constants.length > 1 ) {
			//						int MAXIMUM_CONSTANTS_TO_USE = 5;
			//						java.util.List<org.lgna.croquet.icon.IconFactory> iconFactories = edu.cmu.cs.dennisc.java.util.Lists.newArrayListWithInitialCapacity( Math.min( constants.length, MAXIMUM_CONSTANTS_TO_USE ) );
			//
			//						java.util.List<org.lgna.story.resources.ModelResource> sortedConstants = edu.cmu.cs.dennisc.java.util.Lists.newArrayList( constants );
			//						java.util.Collections.sort( sortedConstants, new Comparator<org.lgna.story.resources.ModelResource>() {
			//							public int compare( org.lgna.story.resources.ModelResource o1, org.lgna.story.resources.ModelResource o2 ) {
			//								return o1.toString().compareTo( o2.toString() );
			//							}
			//						} );
			//						int i = 0;
			//						for( org.lgna.story.resources.ModelResource constant : sortedConstants ) {
			//							iconFactories.add( getIconFactoryForResourceInstance( constant ) );
			//							i += 1;
			//							if( i == MAXIMUM_CONSTANTS_TO_USE ) {
			//								break;
			//							}
			//						}
			//						return new EnumConstantsIconFactory( iconFactories );
			//					}
			//				}
			//			}
			java.net.URL url;
			if( modelResourceCls != null ) {
				url = org.lgna.story.implementation.alice.AliceResourceUtilties.getThumbnailURL( modelResourceCls, modelResourceName );
			} else {
				url = null;
			}
			if( url != null ) {
				return new org.lgna.croquet.icon.TrimmedImageIconFactory( url, 160, 120 );
			} else {
				//				if( org.lgna.story.resources.sims2.PersonResource.class.isAssignableFrom( modelResourceCls ) ) {
				//					org.alice.stageide.modelresource.PersonResourceKey personResourceKey = org.alice.stageide.modelresource.PersonResourceKey.getInstanceForResourceClass( modelResourceCls );
				//					if( personResourceKey != null ) {
				//						return personResourceKey.getIconFactory();
				//					} else {
				//						edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "no icon factory for:", modelResourceCls, modelResourceName );
				//						return org.lgna.croquet.icon.EmptyIconFactory.getInstance();
				//					}
				//				} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this, this.getClass(), modelResourceCls, modelResourceName );
				return org.lgna.croquet.icon.EmptyIconFactory.getInstance();
				//				}
			}
		}
	}

	private static final class ResourceEnumConstant extends UrlResourceDeclaration {
		private final Enum<? extends org.lgna.story.resources.ModelResource> enm;

		public ResourceEnumConstant( Enum<? extends org.lgna.story.resources.ModelResource> enm ) {
			assert enm != null;
			this.enm = enm;
		}

		@Override
		protected Class<? extends org.lgna.story.resources.ModelResource> getModelResourceClass() {
			//todo?
			return (Class<? extends org.lgna.story.resources.ModelResource>)this.enm.getClass();
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
		private final Class<? extends org.lgna.story.resources.ModelResource> cls;

		public ResourceType( Class<? extends org.lgna.story.resources.ModelResource> cls ) {
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
		private final org.lgna.story.resources.ModelResource instance;

		public ResourceInstance( org.lgna.story.resources.ModelResource instance ) {
			assert instance != null;
			this.instance = instance;
		}

		@Override
		public org.lgna.croquet.icon.IconFactory createIconFactory() {
			if( this.instance instanceof org.lgna.story.resources.sims2.PersonResource ) {
				org.lgna.story.resources.sims2.PersonResource personResource = (org.lgna.story.resources.sims2.PersonResource)this.instance;
				try {
					org.lgna.story.resourceutilities.AliceThumbnailMaker thumbnailMaker = org.lgna.story.resourceutilities.AliceThumbnailMaker.getInstance();
					java.awt.image.BufferedImage image = thumbnailMaker.createThumbnailFromPersonResource( personResource );
					int width = thumbnailMaker.getWidth();
					int height = thumbnailMaker.getHeight();

					//Used for saving out gallery thumbnails for the sims lifestages
					//					java.io.File outputFile = new java.io.File( "C:/Users/dculyba/Documents/Alice/simThumbs/thumb_" + personResource.getGender().toString() + "_" + personResource.getLifeStage().toString() + "_" + Integer.toString( personResource.hashCode() ) + ".png" );
					//					edu.cmu.cs.dennisc.image.ImageUtilities.write( outputFile, org.lgna.story.resourceutilities.AliceThumbnailMaker.getInstance( 240, 180 ).createGalleryThumbnailFromPersonResource( personResource ) );

					if( ( width == image.getWidth() ) && ( height == image.getHeight() ) ) {
						return new org.lgna.croquet.icon.ImageIconFactory( image );
					} else {
						return new org.lgna.croquet.icon.TrimmedImageIconFactory( image, width, height );
					}
				} catch( Throwable t ) {
					t.printStackTrace();
					return org.lgna.croquet.icon.EmptyIconFactory.getInstance();
				}
			} else {
				return null;
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

	private static java.util.Map<org.lgna.project.ast.JavaType, org.lgna.croquet.icon.IconFactory> mapTypeToIconFactory = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private static java.util.Map<ResourceDeclaration, org.lgna.croquet.icon.IconFactory> mapResourceDeclarationToIconFactory = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private static java.util.Map<org.lgna.story.Color, org.lgna.croquet.icon.IconFactory> mapColorToCameraMarkerIconFactory = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private static java.util.Map<org.lgna.story.Color, org.lgna.croquet.icon.IconFactory> mapColorToObjectMarkerIconFactory = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private IconFactoryManager() {
	}

	public static void registerIconFactory( org.lgna.project.ast.JavaType javaType, org.lgna.croquet.icon.IconFactory iconFactory ) {
		mapTypeToIconFactory.put( javaType, iconFactory );
	}

	public static void registerIconFactory( Class<?> cls, org.lgna.croquet.icon.IconFactory iconFactory ) {
		registerIconFactory( org.lgna.project.ast.JavaType.getInstance( cls ), iconFactory );
	}

	private static ResourceDeclaration createResourceDeclarationFromRequiredArguments( org.lgna.project.ast.SimpleArgumentListProperty requiredArguments ) {
		if( requiredArguments.size() == 1 ) {
			org.lgna.project.ast.SimpleArgument arg0 = requiredArguments.get( 0 );
			org.lgna.project.ast.Expression expression0 = arg0.expression.getValue();
			if( expression0 instanceof org.lgna.project.ast.InstanceCreation ) {
				Object instance = org.alice.stageide.StageIDE.getActiveInstance().getSceneEditor().getInstanceInJavaVMForExpression( expression0 );
				if( instance instanceof org.lgna.story.resources.ModelResource ) {
					org.lgna.story.resources.ModelResource modelResource = (org.lgna.story.resources.ModelResource)instance;
					return new ResourceInstance( modelResource );
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
								Enum<? extends org.lgna.story.resources.ModelResource> e = (Enum<? extends org.lgna.story.resources.ModelResource>)o;
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

	private static int getRequiredArgumentsInInitializer( org.lgna.project.ast.UserField userField ) {
		org.lgna.project.ast.Expression initializer = userField.initializer.getValue();
		if( initializer instanceof org.lgna.project.ast.InstanceCreation ) {
			org.lgna.project.ast.InstanceCreation instanceCreation = (org.lgna.project.ast.InstanceCreation)initializer;
			return instanceCreation.requiredArguments.size();
		}
		return -1;
	}

	private static ResourceDeclaration createResourceDeclarationFromField( org.lgna.project.ast.UserField userField ) {
		org.lgna.project.ast.Expression initializer = userField.initializer.getValue();
		if( initializer instanceof org.lgna.project.ast.InstanceCreation ) {
			org.lgna.project.ast.InstanceCreation instanceCreation = (org.lgna.project.ast.InstanceCreation)initializer;
			return createResourceDeclarationFromRequiredArguments( instanceCreation.requiredArguments );
		}
		return null;
	}

	public static org.lgna.croquet.icon.IconFactory getRegisteredIconFactory( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		if( type != null ) {
			org.lgna.project.ast.JavaType javaType = type.getFirstEncounteredJavaType();
			if( mapTypeToIconFactory.containsKey( javaType ) ) {
				return mapTypeToIconFactory.get( javaType );
			}
		}
		return null;
	}

	public static org.lgna.croquet.icon.IconFactory getIconFactoryForResourceCls( Class<? extends org.lgna.story.resources.ModelResource> cls ) {
		ResourceType resourceType = new ResourceType( cls );
		org.lgna.croquet.icon.IconFactory iconFactory = mapResourceDeclarationToIconFactory.get( resourceType );
		if( iconFactory != null ) {
			//pass
		} else {
			iconFactory = resourceType.createIconFactory();
			mapResourceDeclarationToIconFactory.put( resourceType, iconFactory );
		}
		return iconFactory;
	}

	public static org.lgna.croquet.icon.IconFactory getIconFactoryForResourceInstance( org.lgna.story.resources.ModelResource modelResource ) {
		ResourceDeclaration resourceDeclaration;
		if( modelResource.getClass().isEnum() ) {
			Enum<? extends org.lgna.story.resources.ModelResource> e = (Enum<? extends org.lgna.story.resources.ModelResource>)modelResource;
			resourceDeclaration = new ResourceEnumConstant( e );
		} else {
			resourceDeclaration = new ResourceInstance( modelResource );
		}
		org.lgna.croquet.icon.IconFactory iconFactory = mapResourceDeclarationToIconFactory.get( resourceDeclaration );
		if( iconFactory != null ) {
			//pass
		} else {
			iconFactory = resourceDeclaration.createIconFactory();
			mapResourceDeclarationToIconFactory.put( resourceDeclaration, iconFactory );
		}
		return iconFactory;
	}

	public static org.lgna.croquet.icon.IconFactory getIconFactoryForType( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		org.lgna.croquet.icon.IconFactory iconFactory = getRegisteredIconFactory( type );
		if( iconFactory != null ) {
			return iconFactory;
		} else {
			ResourceDeclaration resourceDeclaration = null;
			org.lgna.project.ast.AbstractConstructor constructor0 = org.alice.ide.typemanager.ConstructorArgumentUtilities.getContructor0( type );
			if( constructor0 != null ) {
				java.util.List<? extends org.lgna.project.ast.AbstractParameter> parameters = constructor0.getRequiredParameters();
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
					org.lgna.project.ast.AbstractType<?, ?, ?> parameter0Type = parameter0.getValueType();
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
		}
		return org.lgna.croquet.icon.EmptyIconFactory.getInstance();
	}

	public static org.lgna.croquet.icon.IconFactory getIconFactoryForField( org.lgna.project.ast.UserField userField ) {
		if( userField != null ) {
			org.lgna.croquet.icon.IconFactory iconFactory = getRegisteredIconFactory( userField.getValueType() );
			if( iconFactory != null ) {
				return iconFactory;
			}
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
			int requiredArgumentCount = getRequiredArgumentsInInitializer( userField );
			if( requiredArgumentCount != 0 ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "Note: non-zero initializer detected, but no resource specific icon found. Falling back to type for icon for", userField );
			}
			return getIconFactoryForType( userField.getValueType() );
		}
		return org.lgna.croquet.icon.EmptyIconFactory.getInstance();
	}

	public static org.lgna.croquet.icon.IconFactory getIconFactoryForCameraMarker( org.lgna.story.Color color ) {
		org.lgna.croquet.icon.IconFactory rv = mapColorToCameraMarkerIconFactory.get( color );
		if( rv != null ) {
			//pass
		} else {
			javax.swing.ImageIcon imageIcon = org.alice.stageide.sceneeditor.viewmanager.MarkerUtilities.getCameraMarkIconForColor( color ); //todo
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
			javax.swing.ImageIcon imageIcon = org.alice.stageide.sceneeditor.viewmanager.MarkerUtilities.getObjectMarkIconForColor( color );
			//todo
			rv = new org.lgna.croquet.icon.ImageIconFactory( imageIcon );
			mapColorToObjectMarkerIconFactory.put( color, rv );
		}
		return rv;
	}
}
