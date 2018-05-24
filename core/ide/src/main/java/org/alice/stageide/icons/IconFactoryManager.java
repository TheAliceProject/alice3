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
package org.alice.stageide.icons;

import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.Sets;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.javax.swing.IconUtilities;
import org.alice.ide.croquet.models.gallerybrowser.GalleryDragModel;
import org.alice.ide.typemanager.ConstructorArgumentUtilities;
import org.alice.nonfree.NebulousIde;
import org.alice.stageide.StageIDE;
import org.alice.stageide.sceneeditor.viewmanager.MarkerUtilities;
import org.lgna.croquet.icon.EmptyIconFactory;
import org.lgna.croquet.icon.IconFactory;
import org.lgna.croquet.icon.ImageIconFactory;
import org.lgna.croquet.icon.MultipleSourceImageIconFactory;
import org.lgna.croquet.icon.TrimmedImageIconFactory;
import org.lgna.project.ast.AbstractConstructor;
import org.lgna.project.ast.AbstractParameter;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.ConstructorInvocationStatement;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.JavaField;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.NamedUserConstructor;
import org.lgna.project.ast.SimpleArgument;
import org.lgna.project.ast.SimpleArgumentListProperty;
import org.lgna.project.ast.UserConstructor;
import org.lgna.project.ast.UserField;
import org.lgna.story.Color;
import org.lgna.story.implementation.alice.AliceResourceUtilties;
import org.lgna.story.resources.AircraftResource;
import org.lgna.story.resources.AutomobileResource;
import org.lgna.story.resources.BipedResource;
import org.lgna.story.resources.FishResource;
import org.lgna.story.resources.FlyerResource;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resources.MarineMammalResource;
import org.lgna.story.resources.ModelResource;
import org.lgna.story.resources.PropResource;
import org.lgna.story.resources.QuadrupedResource;
import org.lgna.story.resources.SlithererResource;
import org.lgna.story.resources.SwimmerResource;
import org.lgna.story.resources.TrainResource;
import org.lgna.story.resources.TransportResource;
import org.lgna.story.resources.WatercraftResource;

import javax.swing.ImageIcon;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Dennis Cosgrove
 */
public class IconFactoryManager {
	private static interface ResourceDeclaration {
		public IconFactory createIconFactory();
	}

	// @formatter:off
	private static Set<Class<? extends JointedModelResource>> setOfClassesWithIcons = Sets.newHashSet(
			BipedResource.class,
			FishResource.class,
			FlyerResource.class,
			PropResource.class,
			QuadrupedResource.class,
			SwimmerResource.class,
			SlithererResource.class,
			MarineMammalResource.class,
			TransportResource.class,
			AutomobileResource.class,
			AircraftResource.class,
			WatercraftResource.class,
			TrainResource.class );

	// @formatter:on

	public static Set<Class<? extends JointedModelResource>> getSetOfClassesWithIcons() {
		return setOfClassesWithIcons;
	}

	private static ImageIcon getIcon( Class<?> cls, boolean isSmall ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "images/" );
		sb.append( cls.getName().replace( ".", "/" ) );
		if( isSmall ) {
			sb.append( "_small" );
		}
		sb.append( ".png" );
		return IconUtilities.createImageIcon( GalleryDragModel.class.getResource( sb.toString() ) );
	}

	private static abstract class UrlResourceDeclaration implements ResourceDeclaration {
		protected abstract Class<? extends ModelResource> getModelResourceClass();

		protected abstract String getModelResourceName();

		@Override
		public final IconFactory createIconFactory() {
			Class<? extends ModelResource> modelResourceCls = this.getModelResourceClass();
			String modelResourceName = this.getModelResourceName();
			if( modelResourceName != null ) {
				//pass
			} else {
				if( getSetOfClassesWithIcons().contains( modelResourceCls ) ) {
					ImageIcon smallIcon = getIcon( modelResourceCls, true );
					ImageIcon largeIcon = getIcon( modelResourceCls, false );
					return new MultipleSourceImageIconFactory( 1, smallIcon, largeIcon );
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
			URL url;
			if( modelResourceCls != null ) {
				url = AliceResourceUtilties.getThumbnailURL( modelResourceCls, modelResourceName );
			} else {
				url = null;
			}
			if( url != null ) {
				return new TrimmedImageIconFactory( url, 160, 120 );
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
				Logger.severe( this, this.getClass(), modelResourceCls, modelResourceName );
				return EmptyIconFactory.getInstance();
				//				}
			}
		}
	}

	private static final class ResourceEnumConstant extends UrlResourceDeclaration {
		private final Enum<? extends ModelResource> enm;

		public ResourceEnumConstant( Enum<? extends ModelResource> enm ) {
			assert enm != null;
			this.enm = enm;
		}

		@Override
		protected Class<? extends ModelResource> getModelResourceClass() {
			//todo?
			return (Class<? extends ModelResource>)this.enm.getClass();
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
		private final Class<? extends ModelResource> cls;

		public ResourceType( Class<? extends ModelResource> cls ) {
			assert cls != null;
			this.cls = cls;
		}

		@Override
		protected Class<? extends ModelResource> getModelResourceClass() {
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
		private final ModelResource instance;

		public ResourceInstance( ModelResource instance ) {
			assert instance != null;
			this.instance = instance;
		}

		@Override
		public IconFactory createIconFactory() {
			return NebulousIde.nonfree.createIconFactory( this.instance );
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

	private static Map<JavaType, IconFactory> mapTypeToIconFactory = Maps.newHashMap();
	private static Map<ResourceDeclaration, IconFactory> mapResourceDeclarationToIconFactory = Maps.newHashMap();
	private static Map<Color, IconFactory> mapColorToCameraMarkerIconFactory = Maps.newHashMap();
	private static Map<Color, IconFactory> mapColorToObjectMarkerIconFactory = Maps.newHashMap();

	private IconFactoryManager() {
	}

	public static void registerIconFactory( JavaType javaType, IconFactory iconFactory ) {
		mapTypeToIconFactory.put( javaType, iconFactory );
	}

	public static void registerIconFactory( Class<?> cls, IconFactory iconFactory ) {
		registerIconFactory( JavaType.getInstance( cls ), iconFactory );
	}

	private static ResourceDeclaration createResourceDeclarationFromRequiredArguments( SimpleArgumentListProperty requiredArguments ) {
		if( requiredArguments.size() == 1 ) {
			SimpleArgument arg0 = requiredArguments.get( 0 );
			Expression expression0 = arg0.expression.getValue();
			if( expression0 instanceof InstanceCreation ) {
				Object instance = StageIDE.getActiveInstance().getSceneEditor().getInstanceInJavaVMForExpression( expression0 );
				if( instance instanceof ModelResource ) {
					ModelResource modelResource = (ModelResource)instance;
					return new ResourceInstance( modelResource );
				}
			}
		}
		JavaField argumentField = ConstructorArgumentUtilities.getField( requiredArguments );
		if( argumentField != null ) {
			if( argumentField.isStatic() ) {
				Field fld = argumentField.getFieldReflectionProxy().getReification();
				try {
					Object o = fld.get( null );
					if( o != null ) {
						if( o instanceof ModelResource ) {
							if( o.getClass().isEnum() ) {
								Enum<? extends ModelResource> e = (Enum<? extends ModelResource>)o;
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

	private static int getRequiredArgumentsInInitializer( UserField userField ) {
		Expression initializer = userField.initializer.getValue();
		if( initializer instanceof InstanceCreation ) {
			InstanceCreation instanceCreation = (InstanceCreation)initializer;
			return instanceCreation.requiredArguments.size();
		}
		return -1;
	}

	private static ResourceDeclaration createResourceDeclarationFromField( UserField userField ) {
		Expression initializer = userField.initializer.getValue();
		if( initializer instanceof InstanceCreation ) {
			InstanceCreation instanceCreation = (InstanceCreation)initializer;
			return createResourceDeclarationFromRequiredArguments( instanceCreation.requiredArguments );
		}
		return null;
	}

	public static IconFactory getRegisteredIconFactory( AbstractType<?, ?, ?> type ) {
		if( type != null ) {
			JavaType javaType = type.getFirstEncounteredJavaType();
			if( mapTypeToIconFactory.containsKey( javaType ) ) {
				return mapTypeToIconFactory.get( javaType );
			}
		}
		return null;
	}

	public static IconFactory getIconFactoryForResourceCls( Class<? extends ModelResource> cls ) {
		ResourceType resourceType = new ResourceType( cls );
		IconFactory iconFactory = mapResourceDeclarationToIconFactory.get( resourceType );
		if( iconFactory != null ) {
			//pass
		} else {
			iconFactory = resourceType.createIconFactory();
			mapResourceDeclarationToIconFactory.put( resourceType, iconFactory );
		}
		return iconFactory;
	}

	public static IconFactory getIconFactoryForResourceInstance( ModelResource modelResource ) {
		ResourceDeclaration resourceDeclaration;
		if( modelResource.getClass().isEnum() ) {
			Enum<? extends ModelResource> e = (Enum<? extends ModelResource>)modelResource;
			resourceDeclaration = new ResourceEnumConstant( e );
		} else {
			resourceDeclaration = new ResourceInstance( modelResource );
		}
		IconFactory iconFactory = mapResourceDeclarationToIconFactory.get( resourceDeclaration );
		if( iconFactory != null ) {
			//pass
		} else {
			iconFactory = resourceDeclaration.createIconFactory();
			mapResourceDeclarationToIconFactory.put( resourceDeclaration, iconFactory );
		}
		return iconFactory;
	}

	public static IconFactory getIconFactoryForType( AbstractType<?, ?, ?> type ) {
		IconFactory iconFactory = getRegisteredIconFactory( type );
		if( iconFactory != null ) {
			return iconFactory;
		} else {
			ResourceDeclaration resourceDeclaration = null;
			AbstractConstructor constructor0 = ConstructorArgumentUtilities.getContructor0( type );
			if( constructor0 != null ) {
				List<? extends AbstractParameter> parameters = constructor0.getRequiredParameters();
				switch( parameters.size() ) {
				case 0:
					if( constructor0 instanceof UserConstructor ) {
						NamedUserConstructor userConstructor0 = (NamedUserConstructor)constructor0;
						ConstructorInvocationStatement constructorInvocationStatement = userConstructor0.body.getValue().constructorInvocationStatement.getValue();
						resourceDeclaration = createResourceDeclarationFromRequiredArguments( constructorInvocationStatement.requiredArguments );
					}
					break;
				case 1:
					AbstractParameter parameter0 = parameters.get( 0 );
					AbstractType<?, ?, ?> parameter0Type = parameter0.getValueType();
					if( parameter0Type != null ) {
						if( parameter0Type.isAssignableTo( ModelResource.class ) ) {
							Class<? extends ModelResource> cls = (Class<? extends ModelResource>)parameter0Type.getFirstEncounteredJavaType().getClassReflectionProxy().getReification();
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
		return EmptyIconFactory.getInstance();
	}

	public static IconFactory getIconFactoryForField( UserField userField ) {
		if( userField != null ) {
			IconFactory iconFactory = getRegisteredIconFactory( userField.getValueType() );
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
				Logger.outln( "Note: non-zero initializer detected, but no resource specific icon found. Falling back to type for icon for", userField );
			}
			return getIconFactoryForType( userField.getValueType() );
		}
		return EmptyIconFactory.getInstance();
	}

	public static IconFactory getIconFactoryForCameraMarker( Color color ) {
		IconFactory rv = mapColorToCameraMarkerIconFactory.get( color );
		if( rv != null ) {
			//pass
		} else {
			ImageIcon imageIcon = MarkerUtilities.getCameraMarkIconForColor( color ); //todo
			rv = new ImageIconFactory( imageIcon );
			mapColorToCameraMarkerIconFactory.put( color, rv );
		}
		return rv;
	}

	public static IconFactory getIconFactoryForObjectMarker( Color color ) {
		IconFactory rv = mapColorToObjectMarkerIconFactory.get( color );
		if( rv != null ) {
			//pass
		} else {
			ImageIcon imageIcon = MarkerUtilities.getObjectMarkIconForColor( color );
			//todo
			rv = new ImageIconFactory( imageIcon );
			mapColorToObjectMarkerIconFactory.put( color, rv );
		}
		return rv;
	}
}
