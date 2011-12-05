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

import org.lgna.story.resourceutilities.ModelResourceUtilities;

/**
 * @author Dennis Cosgrove
 */
public class ResourceManager {
	private static final int SMALL_ICON_SIZE = 24;
	private static final String PACKAGE_NAME_PREFIX = ResourceManager.class.getPackage().getName();
	private static java.util.Map< org.lgna.project.ast.JavaType, javax.swing.Icon > typeToIconMap = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

	private ResourceManager() {
	}
	public static void registerSmallIcon( org.lgna.project.ast.JavaType typeInJava, javax.swing.Icon icon ) {
		typeToIconMap.put( typeInJava, icon );
	}
	public static void registerSmallIcon( Class< ? > cls, javax.swing.Icon icon ) {
		registerSmallIcon( org.lgna.project.ast.JavaType.getInstance( cls ), icon );
	}

	private static javax.swing.Icon getSmallIconFor( javax.swing.Icon largeIcon ) {
		if( largeIcon != null ) {
			return new edu.cmu.cs.dennisc.javax.swing.icons.ScaledImageIcon( ((javax.swing.ImageIcon)largeIcon).getImage(), SMALL_ICON_SIZE, SMALL_ICON_SIZE );
			//return new edu.cmu.cs.dennisc.javax.swing.icons.ScaledIcon( largeIcon, SMALL_ICON_SIZE, SMALL_ICON_SIZE );
		} else {
			//todo
			return new edu.cmu.cs.dennisc.javax.swing.icons.ShapeIcon( new java.awt.geom.Ellipse2D.Float( 0, 0, SMALL_ICON_SIZE - 8, SMALL_ICON_SIZE - 8 ), java.awt.Color.LIGHT_GRAY, java.awt.Color.DARK_GRAY, 4, 4, 4, 4 );
		}
	}
	private static java.net.URL getLargeIconResource( edu.cmu.cs.dennisc.javax.swing.models.TreeNode< String > treeNode ) {
		if( treeNode != null ) {
			String path = treeNode.getValue();
			String resourceName = path.substring( PACKAGE_NAME_PREFIX.length() + 1 );
			//			if( resourceName.startsWith( "images/org/alice/apis/moveandturn/gallery/environments/grounds/" ) ) {
			//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: handle grounds" );
			//				return null;
			//			} else {
			java.net.URL rv = ResourceManager.class.getResource( resourceName );
			if( rv != null ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "failed to getResource:", resourceName );
			}
			return rv;
			//			}
		} else {
			return null;
		}
	}
	public static javax.swing.Icon getLargeIcon( edu.cmu.cs.dennisc.javax.swing.models.TreeNode< String > treeNode ) {
		return edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( getLargeIconResource( treeNode ) );
	}
	public static javax.swing.Icon getSmallIcon( edu.cmu.cs.dennisc.javax.swing.models.TreeNode< String > treeNode ) {
		return getSmallIconFor( getLargeIcon( treeNode ) );
		//		javax.swing.Icon largeIcon = getLargeIcon(treeNode);
		//		if( largeIcon != null ) {
		//			return new edu.cmu.cs.dennisc.javax.swing.icons.ScaledIcon(largeIcon, SMALL_ICON_SIZE, SMALL_ICON_SIZE );
		//		} else {
		//			return null;
		//		}
	}

	//	private static String convertLargeIconResourceNameToShortIconResourceName( String largeIconResourceName ) {
	//		int i = largeIconResourceName.lastIndexOf( "/" );
	//		if( i != -1 ) {
	//			return largeIconResourceName.substring( 0, i+1 ) + "small_" + largeIconResourceName.substring( i+1 );
	//		} else {
	//			return null;
	//		}
	//	}
	//	public static java.net.URL getSmallIconResource(edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> treeNode ) {
	//		if( treeNode != null ) {
	//			String path = treeNode.getValue();
	//			String resourceName = path.substring( PACKAGE_NAME_PREFIX.length()+1 );
	//			
	//			resourceName = convertLargeIconResourceNameToShortIconResourceName( resourceName );
	//			if( resourceName != null ) {
	//				java.net.URL rv = ResourceManager.class.getResource( resourceName );
	//				if( rv != null ) {
	//					//pass
	//				} else {
	//					System.err.println( resourceName );
	//				}
	//				return rv;
	//			} else {
	//				return null;
	//			}
	//		} else {
	//			return null;
	//		}
	//	}
	private static String getLargeIconResourceNameForClassName( String className ) {
		return "images/" + className.replace( '.', '/' ) + ".png";
	}

	private static java.net.URL getLargeIconResourceForGalleryClassName( String className ) {
		if( className != null ) {
			//			if( className.startsWith( "org.alice.apis.moveandturn.gallery.environments.grounds." ) ) {
			//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: handle grounds" );
			//				return null;
			//			} else {
			return ResourceManager.class.getResource( getLargeIconResourceNameForClassName( className ) );
			//			}
		} else {
			return null;
		}
	}
	public static javax.swing.Icon getLargeIconForGalleryClassName( String className ) {
		return edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( getLargeIconResourceForGalleryClassName( className ) );
	}
	public static javax.swing.Icon getSmallIconForGalleryClassName( String className ) {
		return getSmallIconFor( getLargeIconForGalleryClassName( className ) );
	}

	private static java.net.URL getLargeIconResourceForType( org.lgna.project.ast.AbstractType< ?, ?, ? > type ) {
		String className;
		if( type != null ) {
			org.lgna.project.ast.JavaType typeInJava = type.getFirstTypeEncounteredDeclaredInJava();
			className = typeInJava.getClassReflectionProxy().getName();
		} else {
			className = null;
		}
		return getLargeIconResourceForGalleryClassName( className );
	}

	public static javax.swing.Icon getLargeIconForType( org.lgna.project.ast.AbstractType< ?, ?, ? > type ) {
		java.net.URL url = getLargeIconResourceForType( type );
//		if( url != null ) {
//			//pass
//		} else {
//			//url = ModelResourceUtilities.getThumbnailURL( modelResource );
//		}
		return edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( url );
	}

	public static javax.swing.Icon getSmallIconForType( org.lgna.project.ast.AbstractType< ?, ?, ? > type ) {
		if( type != null ) {
			org.lgna.project.ast.JavaType typeInJava = type.getFirstTypeEncounteredDeclaredInJava();
			if( typeToIconMap.containsKey( typeInJava ) ) {
				return typeToIconMap.get( typeInJava );
			} else {
				return getSmallIconFor( getLargeIconForType( type ) );
			}
		} else {
			return null;
		}
	}

	public static javax.swing.Icon getLargeIconForField( org.lgna.project.ast.AbstractField field ) {
		if( field instanceof org.lgna.project.ast.UserField ) {
			org.lgna.project.ast.UserField userField = (org.lgna.project.ast.UserField)field;
			org.lgna.project.ast.Expression initializer = userField.initializer.getValue();
			if( initializer instanceof org.lgna.project.ast.InstanceCreation ) {
				org.lgna.project.ast.InstanceCreation instanceCreation = (org.lgna.project.ast.InstanceCreation)initializer;
				org.lgna.project.ast.JavaField argumentField = org.alice.ide.typemanager.ConstructorArgumentUtilities.getArgumentField( instanceCreation );
				if( argumentField != null ) {
					if( argumentField.isStatic() ) {
						java.lang.reflect.Field fld = argumentField.getFieldReflectionProxy().getReification();
						try {
							Object o = fld.get( null );
							if( o != null ) {
								if( o.getClass().isEnum() ) {
									Enum< ? > e = (Enum< ? >)o;
									return edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( ModelResourceUtilities.getThumbnailURL( e.getClass(), e.name() ) );
								}
							}
						} catch( IllegalAccessException iae ) {
							iae.printStackTrace();
							return null;
						}
					}
				}
			}
		}
		return null;
	}
	public static javax.swing.Icon getSmallIconForField( org.lgna.project.ast.AbstractField field ) {
		return getSmallIconFor( getLargeIconForField( field ) );
	}

//	public static javax.swing.Icon getLargeIconForImplementation( org.lgna.story.implementation.EntityImp imp ) {
//		if( imp != null ) {
//			if( imp instanceof org.lgna.story.implementation.JointedModelImp< ?, ? > ) {
//				org.lgna.story.implementation.JointedModelImp< ?, ? > jointedImp = (org.lgna.story.implementation.JointedModelImp< ?, ? >)imp;
//				Class< ? > resourceCls = jointedImp.getResource().getClass();
//				if( resourceCls.isEnum() ) {
//					String instanceName = null;
//					for( Object e : resourceCls.getEnumConstants() ) {
//						System.out.println( "Resource: " + e );
//						if( e == jointedImp.getResource() ) {
//							instanceName = e.toString();
//						}
//					}
//					return edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( ModelResourceUtilities.getThumbnailURL( resourceCls, instanceName ) );
//				} else {
//					return null;
//				}
//			} else {
//				return getLargeIconForType( org.lgna.project.ast.JavaType.getInstance( imp.getAbstraction().getClass() ) );
//			}
//		}
//		return null;
//	}
//
//	public static javax.swing.Icon getSmallIconForImplementation( org.lgna.story.implementation.EntityImp imp ) {
//		return getSmallIconFor( getLargeIconForImplementation( imp ) );
//	}

}
