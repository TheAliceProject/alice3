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

package org.alice.stageide.ast;

import org.lgna.project.annotations.ArrayTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.ManagementLevel;
import org.lgna.project.ast.UserMethod;
import org.lgna.story.SJoint;

import java.lang.reflect.Method;
import java.util.Locale;

/**
 * @author Dennis Cosgrove
 */
public class JointMethodUtilities {
	private static final JavaType JOINT_TYPE = JavaType.getInstance( SJoint.class );
	private static final JavaType JOINT_ARRAY_TYPE = JavaType.getInstance( SJoint[].class );
	private static final String GETTER_PREFIX = "get";

	public static boolean isJointGetter( AbstractMethod method ) {
		if( method.isPublicAccess() ) {
			if( method.getReturnType() == JOINT_TYPE ) {
				if( ( method.getVisibility() == Visibility.PRIME_TIME ) || ( method.getVisibility() == null ) ) {
					if( method.getName().startsWith( GETTER_PREFIX ) ) {
						if( method instanceof JavaMethod ) {
							return true; //isNotAnnotatedOtherwise
						} else if( method instanceof UserMethod ) {
							UserMethod userMethod = (UserMethod)method;
							return userMethod.managementLevel.getValue() == ManagementLevel.GENERATED;
						} else {
							//throw new AssertionError();
							return false;
						}
					}
				}
			}
		}
		return false;
	}

	public static boolean isJointArrayGetter( AbstractMethod method ) {
		if( method.isPublicAccess() ) {
			if( method.getReturnType() == JOINT_ARRAY_TYPE ) {
				if( ( method.getVisibility() == Visibility.PRIME_TIME ) || ( method.getVisibility() == null ) ) {
					if( method.getName().startsWith( GETTER_PREFIX ) ) {
						if( method instanceof JavaMethod ) {
							return true; //isNotAnnotatedOtherwise
						} else if( method instanceof UserMethod ) {
							UserMethod userMethod = (UserMethod)method;
							return userMethod.managementLevel.getValue() == ManagementLevel.GENERATED;
						} else {
							//throw new AssertionError();
							return false;
						}
					}
				}
			}
		}
		return false;
	}

	public static int getJointArrayLength( AbstractMethod method ) {
		if( isJointArrayGetter( method ) ) {
			if( method instanceof JavaMethod ) {
				Method mthd = ( (JavaMethod)method ).getMethodReflectionProxy().getReification();
				if( mthd != null ) {
					if( mthd.isAnnotationPresent( ArrayTemplate.class ) ) {
						ArrayTemplate arrayTemplate = mthd.getAnnotation( ArrayTemplate.class );
						return arrayTemplate.length();
					} else {
						return -1;
					}
				} else {
					return -1;
				}
			} else if( method instanceof UserMethod ) {
				return -1;
			} else {
				return -1;
			}
		}
		return -1;
	}

	public static String getJointName( AbstractMethod method, Locale locale ) {
		String name = method.getName();
		if( name.startsWith( GETTER_PREFIX ) ) {
			return name.substring( GETTER_PREFIX.length() );
		} else {
			return name;
		}
	}

	//	public static org.lgna.project.ast.AbstractMethod getParentMethod( org.lgna.project.ast.AbstractMethod method ) {
	//		//todo
	//		return null;
	//	}
}
