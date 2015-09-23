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

package org.alice.ide.typemanager;

/**
 * @author Dennis Cosgrove
 */
public class TypeManager {
	public TypeManager() {
		throw new AssertionError();
	}

	private static final org.lgna.project.ast.Expression[] USE_PARAMETER_ACCESSES_AS_ARGUMENTS_TO_SUPER = null;

	private static org.lgna.project.ast.NamedUserType createTypeFor( org.lgna.project.ast.AbstractType<?, ?, ?> superType, String typeName, org.lgna.project.ast.AbstractType<?, ?, ?>[] parameterTypes,
			org.lgna.project.ast.Expression[] argumentExpressions ) {
		org.lgna.project.ast.NamedUserType rv = new org.lgna.project.ast.NamedUserType();
		rv.name.setValue( typeName );
		rv.superType.setValue( superType );

		for( org.lgna.project.ast.AbstractConstructor superConstructor : superType.getDeclaredConstructors() ) {
			java.util.List<? extends org.lgna.project.ast.AbstractParameter> javaParameters = superConstructor.getRequiredParameters();

			org.lgna.project.ast.NamedUserConstructor userConstructor = new org.lgna.project.ast.NamedUserConstructor();
			org.lgna.project.ast.ConstructorBlockStatement body = new org.lgna.project.ast.ConstructorBlockStatement();
			org.lgna.project.ast.SuperConstructorInvocationStatement superConstructorInvocationStatement = new org.lgna.project.ast.SuperConstructorInvocationStatement();

			superConstructorInvocationStatement.constructor.setValue( superConstructor );

			final int N = javaParameters.size();
			for( int i = 0; i < N; i++ ) {
				org.lgna.project.ast.AbstractParameter javaParameterI = javaParameters.get( i );
				org.lgna.project.ast.Expression argumentExpressionI;
				if( argumentExpressions != USE_PARAMETER_ACCESSES_AS_ARGUMENTS_TO_SUPER ) {
					argumentExpressionI = argumentExpressions[ i ];
				} else {
					String parameterName = javaParameterI.getName(); //todo?
					if( parameterName != null ) {
						//pass
					} else {
						parameterName = "p" + i;
					}
					org.lgna.project.ast.AbstractType<?, ?, ?> parameterTypeI;
					if( parameterTypes != null ) {
						parameterTypeI = parameterTypes[ i ];
					} else {
						parameterTypeI = javaParameterI.getValueType();
					}
					org.lgna.project.ast.UserParameter userParameterI = new org.lgna.project.ast.UserParameter( parameterName, parameterTypeI );
					userConstructor.requiredParameters.add( userParameterI );
					argumentExpressionI = new org.lgna.project.ast.ParameterAccess( userParameterI );
				}
				superConstructorInvocationStatement.requiredArguments.add( new org.lgna.project.ast.SimpleArgument( javaParameterI, argumentExpressionI ) );
			}

			body.constructorInvocationStatement.setValue( superConstructorInvocationStatement );
			userConstructor.body.setValue( body );

			rv.constructors.add( userConstructor );
		}

		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		if( ide != null ) {
			ide.getApiConfigurationManager().augmentTypeIfNecessary( rv );
		}
		return rv;
	}

	private static abstract class ExtendsTypeCriterion implements edu.cmu.cs.dennisc.pattern.Criterion<org.lgna.project.ast.NamedUserType> {
		private final org.lgna.project.ast.AbstractType<?, ?, ?> superType;

		public ExtendsTypeCriterion( org.lgna.project.ast.AbstractType<?, ?, ?> superType ) {
			assert superType != null;
			this.superType = superType;
		}

		@Override
		public boolean accept( org.lgna.project.ast.NamedUserType userType ) {
			return userType.superType.getValue() == this.superType;
		}
	}

	private static final class DefaultConstructorExtendsTypeCriterion extends ExtendsTypeCriterion {
		public DefaultConstructorExtendsTypeCriterion( org.lgna.project.ast.AbstractType<?, ?, ?> superType ) {
			super( superType );
		}
	}

	private static final class ExtendsTypeWithConstructorParameterTypeCriterion extends ExtendsTypeCriterion {
		private final org.lgna.project.ast.AbstractType<?, ?, ?> parameterType;

		public ExtendsTypeWithConstructorParameterTypeCriterion( org.lgna.project.ast.AbstractType<?, ?, ?> superType, org.lgna.project.ast.AbstractType<?, ?, ?> parameterType ) {
			super( superType );
			assert parameterType != null;
			this.parameterType = parameterType;
		}

		@Override
		public boolean accept( org.lgna.project.ast.NamedUserType userType ) {
			if( super.accept( userType ) ) {
				org.lgna.project.ast.AbstractConstructor constructor = userType.getDeclaredConstructor( this.parameterType );
				if( constructor != null ) {
					org.lgna.project.ast.AbstractParameter parameter0 = constructor.getRequiredParameters().get( 0 );
					return parameter0.getValueType() == this.parameterType;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	private static final class ExtendsTypeWithSuperArgumentFieldCriterion extends ExtendsTypeCriterion {
		private final org.lgna.project.ast.AbstractField superArgumentField;

		public ExtendsTypeWithSuperArgumentFieldCriterion( org.lgna.project.ast.AbstractType<?, ?, ?> superType, org.lgna.project.ast.AbstractField superArgumentField ) {
			super( superType );
			assert superArgumentField != null;
			this.superArgumentField = superArgumentField;
		}

		@Override
		public boolean accept( org.lgna.project.ast.NamedUserType userType ) {
			if( super.accept( userType ) ) {
				org.lgna.project.ast.NamedUserConstructor constructor = userType.getDeclaredConstructor();
				if( constructor != null ) {
					org.lgna.project.ast.ConstructorInvocationStatement constructorInvocationStatement = constructor.body.getValue().constructorInvocationStatement.getValue();
					if( constructorInvocationStatement instanceof org.lgna.project.ast.SuperConstructorInvocationStatement ) {
						if( constructorInvocationStatement.requiredArguments.size() == 1 ) {
							org.lgna.project.ast.Expression argumentExpression = constructorInvocationStatement.requiredArguments.get( 0 ).expression.getValue();
							if( argumentExpression instanceof org.lgna.project.ast.FieldAccess ) {
								org.lgna.project.ast.FieldAccess fieldAccess = (org.lgna.project.ast.FieldAccess)argumentExpression;
								return fieldAccess.field.getValue() == this.superArgumentField;
							}
						}
					}
				}
			}
			return false;
		}
	}

	private static java.util.List<org.lgna.project.ast.AbstractType<?, ?, ?>> updateArgumentTypes( java.util.List<org.lgna.project.ast.AbstractType<?, ?, ?>> rv, org.lgna.project.ast.AbstractType<?, ?, ?> rootArgumentType,
			org.lgna.project.ast.AbstractType<?, ?, ?> argumentType ) {
		rv.add( argumentType );
		if( argumentType == rootArgumentType ) {
			//pass
		} else {
			org.lgna.project.ast.AbstractType<?, ?, ?>[] interfaces = argumentType.getInterfaces();
			org.lgna.project.ast.AbstractType<?, ?, ?> nextType;
			if( interfaces.length == 1 ) {
				nextType = interfaces[ 0 ];
			} else {
				nextType = argumentType.getSuperType();
			}
			updateArgumentTypes( rv, rootArgumentType, nextType );
		}
		return rv;
	}

	private static org.lgna.project.ast.AbstractType<?, ?, ?>[] getArgumentTypes( org.lgna.project.ast.AbstractType<?, ?, ?> ancestorType, org.lgna.project.ast.AbstractType<?, ?, ?> resourceType ) {
		java.util.List<org.lgna.project.ast.AbstractType<?, ?, ?>> types = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		updateArgumentTypes( types, ConstructorArgumentUtilities.getContructor0Parameter0Type( ancestorType ), resourceType );
		org.lgna.project.ast.AbstractType<?, ?, ?>[] rv = new org.lgna.project.ast.AbstractType<?, ?, ?>[ types.size() ];
		types.toArray( rv );
		return rv;
	}

	private static org.lgna.project.ast.AbstractType<?, ?, ?>[] getArgumentTypes( org.lgna.project.ast.AbstractType<?, ?, ?> ancestorType, org.lgna.project.ast.AbstractField field ) {
		return getArgumentTypes( ancestorType, field.getDeclaringType() );
	}

	private static void appendTypeName( StringBuilder sb, String name ) {
		sb.append( name );
	}

	private static String createClassNameFromResourceType( org.lgna.project.ast.AbstractType<?, ?, ?> resourceType ) {
		StringBuilder sb = new StringBuilder();
		appendTypeName( sb, resourceType.getName().replace( "Resource", "" ) );
		return sb.toString();
	}

	public static String createClassNameFromArgumentField( org.lgna.project.ast.AbstractType<?, ?, ?> ancestorType, org.lgna.project.ast.AbstractField argumentField ) {
		org.lgna.project.ast.AbstractType<?, ?, ?>[] argumentTypes = getArgumentTypes( ancestorType, argumentField );
		return createClassNameFromResourceType( argumentTypes[ 0 ] );
	}

	public static String createClassNameFromSuperType( org.lgna.project.ast.AbstractType<?, ?, ?> superType ) {
		String superTypeName = superType.getName();
		if( superTypeName.length() > 1 ) {
			if( superTypeName.charAt( 0 ) == 'S' ) {
				if( Character.isUpperCase( superTypeName.charAt( 1 ) ) ) {
					return superTypeName.substring( 1 );
				}
			}
		}
		return superTypeName;
	}

	private static final org.lgna.project.ast.JavaMethod SET_JOINTED_MODEL_RESOURCE_METHOD = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.SJointedModel.class, "setJointedModelResource", org.lgna.story.resources.JointedModelResource.class );

	private static org.lgna.project.ast.NamedUserType getNamedUserTypeFor( org.lgna.project.ast.JavaType ancestorType, org.lgna.project.ast.AbstractType<?, ?, ?>[] argumentTypes, int i, org.lgna.project.ast.AbstractField argumentField ) {
		org.lgna.project.ast.AbstractType<?, ?, ?> superType;
		final int LAST_INDEX = argumentTypes.length - 1;
		if( i < LAST_INDEX ) {
			superType = getNamedUserTypeFor( ancestorType, argumentTypes, i + 1, null );
		} else {
			superType = ancestorType;
		}
		edu.cmu.cs.dennisc.pattern.Criterion<org.lgna.project.ast.NamedUserType> criterion;
		if( argumentField != null ) {
			criterion = new ExtendsTypeWithSuperArgumentFieldCriterion( superType, argumentField );
		} else {
			criterion = new ExtendsTypeWithConstructorParameterTypeCriterion( superType, argumentTypes[ i ] );
		}

		//		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		//		if( ide != null ) {
		//			org.alice.ide.ProjectDocument projectDocument = ide.getDocument();
		//			if( projectDocument != null ) {
		//				org.alice.ide.type.TypeCache typeCache = projectDocument.getTypeCache();
		//				typeCache.getNamedUserTypeFor( ancestorType, argumentTypes, i, argumentField );
		//			}
		//		}

		org.lgna.project.Project project = org.alice.ide.ProjectStack.peekProject();
		if( project != null ) {
			java.util.Set<org.lgna.project.ast.NamedUserType> existingTypes = project.getNamedUserTypes();
			for( org.lgna.project.ast.NamedUserType existingType : existingTypes ) {
				if( criterion.accept( existingType ) ) {
					return existingType;
				}
			}
		}
		org.lgna.project.ast.Expression[] expressions;
		if( argumentField != null ) {
			expressions = new org.lgna.project.ast.Expression[] {
					new org.lgna.project.ast.FieldAccess(
							new org.lgna.project.ast.TypeExpression( argumentField.getDeclaringType() ),
							argumentField )
			};
		} else {
			expressions = USE_PARAMETER_ACCESSES_AS_ARGUMENTS_TO_SUPER;
		}
		String name = createClassNameFromResourceType( argumentTypes[ i ] );
		org.lgna.project.ast.NamedUserType rv = createTypeFor( superType, name, new org.lgna.project.ast.AbstractType[] { argumentTypes[ i ] }, expressions );
		if( argumentTypes[ i ] instanceof org.lgna.project.ast.JavaType ) {
			org.lgna.project.ast.JavaType javaArgumentTypeI = (org.lgna.project.ast.JavaType)argumentTypes[ i ];
			Class<?> cls = javaArgumentTypeI.getClassReflectionProxy().getReification();
			if( edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.isFinal( cls ) ) {
				boolean isSetResourceMethodDesired;
				if( cls.isEnum() ) {
					isSetResourceMethodDesired = cls.getEnumConstants().length > 1;
				} else {
					isSetResourceMethodDesired = true;
				}
				if( isSetResourceMethodDesired ) {
					String simpleClsName = cls.getSimpleName();
					org.lgna.project.ast.UserMethod setResourceMethod = new org.lgna.project.ast.UserMethod();
					setResourceMethod.managementLevel.setValue( org.lgna.project.ast.ManagementLevel.GENERATED );
					setResourceMethod.name.setValue( "set" + simpleClsName );
					setResourceMethod.returnType.setValue( org.lgna.project.ast.JavaType.VOID_TYPE );
					org.lgna.project.ast.BlockStatement body = new org.lgna.project.ast.BlockStatement();
					setResourceMethod.body.setValue( body );

					StringBuilder parameterNameSB = new StringBuilder();
					parameterNameSB.append( Character.toLowerCase( simpleClsName.charAt( 0 ) ) );
					parameterNameSB.append( simpleClsName.substring( 1 ) );
					org.lgna.project.ast.UserParameter parameter = new org.lgna.project.ast.UserParameter();
					parameter.name.setValue( parameterNameSB.toString() );
					parameter.valueType.setValue( javaArgumentTypeI );

					setResourceMethod.requiredParameters.add( parameter );

					body.statements.add(
							org.lgna.project.ast.AstUtilities.createMethodInvocationStatement(
									new org.lgna.project.ast.ThisExpression(),
									SET_JOINTED_MODEL_RESOURCE_METHOD,
									new org.lgna.project.ast.ParameterAccess( parameter ) ) );

					setResourceMethod.isSignatureLocked.setValue( true );
					rv.methods.add( setResourceMethod );
				}
			}
		}
		return rv;
	}

	public static org.lgna.project.ast.JavaField getEnumConstantFieldIfOneAndOnly( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		org.lgna.project.ast.JavaField rv = null;
		if( type instanceof org.lgna.project.ast.JavaType ) {
			org.lgna.project.ast.JavaType javaType = (org.lgna.project.ast.JavaType)type;
			if( type.isAssignableTo( Enum.class ) ) {
				Class<Enum<?>> cls = (Class<Enum<?>>)javaType.getClassReflectionProxy().getReification();
				Enum<?>[] constants = cls.getEnumConstants();
				if( constants.length == 1 ) {
					Enum<?> constant = constants[ 0 ];
					rv = org.lgna.project.ast.JavaField.getInstance( constant.getClass(), constant.name() );
				}
			}
		}
		return rv;
	}

	public static org.lgna.project.ast.NamedUserType getNamedUserTypeFromArgumentField( org.lgna.project.ast.JavaType ancestorType, org.lgna.project.ast.JavaField argumentField ) {
		org.lgna.project.ast.AbstractType<?, ?, ?>[] argumentTypes = getArgumentTypes( ancestorType, argumentField );
		return getNamedUserTypeFor( ancestorType, argumentTypes, 0, getEnumConstantFieldIfOneAndOnly( argumentTypes[ 0 ] ) );
	}

	// Method not used
	//	public static org.lgna.project.ast.NamedUserType getNamedUserTypeFromPersonResource( org.lgna.story.resources.sims2.PersonResource personResource ) {
	//		org.lgna.project.ast.JavaType bipedType = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SBiped.class );
	//		org.lgna.project.ast.AbstractType<?, ?, ?>[] argumentTypes = getArgumentTypes( bipedType, org.lgna.project.ast.JavaType.getInstance( personResource.getClass() ) );
	//		return getNamedUserTypeFor( bipedType, argumentTypes, 0, null );
	//	}

	public static org.lgna.project.ast.NamedUserType getNamedUserTypeFromPersonResourceInstanceCreation( org.lgna.project.ast.InstanceCreation instanceCreation ) {
		org.lgna.project.ast.JavaType bipedType = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SBiped.class );
		org.lgna.project.ast.AbstractType<?, ?, ?>[] argumentTypes = getArgumentTypes( bipedType, instanceCreation.getType() );
		return getNamedUserTypeFor( bipedType, argumentTypes, 0, null );
	}

	public static org.lgna.project.ast.NamedUserType getNamedUserTypeFromSuperType( org.lgna.project.ast.JavaType superType ) {
		org.lgna.project.ast.AbstractType<?, ?, ?> parameter0Type = ConstructorArgumentUtilities.getContructor0Parameter0Type( superType );
		ExtendsTypeCriterion criterion;
		if( parameter0Type != null ) {
			criterion = new ExtendsTypeWithConstructorParameterTypeCriterion( superType, ConstructorArgumentUtilities.getContructor0Parameter0Type( superType ) );
		} else {
			criterion = new DefaultConstructorExtendsTypeCriterion( superType );
		}
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		if( ide != null ) {
			org.lgna.project.Project project = ide.getProject();
			if( project != null ) {
				java.util.Set<org.lgna.project.ast.NamedUserType> existingTypes = project.getNamedUserTypes();
				for( org.lgna.project.ast.NamedUserType existingType : existingTypes ) {
					if( criterion.accept( existingType ) ) {
						return existingType;
					}
				}
			}
		}
		return createTypeFor( superType, createClassNameFromSuperType( superType ), null, null );
	}

	public static java.util.List<org.lgna.project.ast.NamedUserType> getNamedUserTypesFromSuperTypes( java.util.List<org.lgna.project.ast.JavaType> superTypes ) {
		java.util.ArrayList<org.lgna.project.ast.NamedUserType> rv = edu.cmu.cs.dennisc.java.util.Lists.newArrayListWithInitialCapacity( superTypes.size() );
		for( org.lgna.project.ast.JavaType superType : superTypes ) {
			rv.add( getNamedUserTypeFromSuperType( superType ) );
		}
		return rv;
	}
}
