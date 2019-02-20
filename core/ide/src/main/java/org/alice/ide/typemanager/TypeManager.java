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

package org.alice.ide.typemanager;

import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.pattern.Criterion;
import org.alice.ide.IDE;
import org.alice.ide.ProjectStack;
import org.lgna.project.Project;
import org.lgna.project.ast.AbstractConstructor;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractParameter;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.BlockStatement;
import org.lgna.project.ast.ConstructorBlockStatement;
import org.lgna.project.ast.ConstructorInvocationStatement;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.JavaField;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.ManagementLevel;
import org.lgna.project.ast.NamedUserConstructor;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.ParameterAccess;
import org.lgna.project.ast.SimpleArgument;
import org.lgna.project.ast.SuperConstructorInvocationStatement;
import org.lgna.project.ast.ThisExpression;
import org.lgna.project.ast.TypeExpression;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserParameter;
import org.lgna.story.SBiped;
import org.lgna.story.SJointedModel;
import org.lgna.story.resources.JointedModelResource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Dennis Cosgrove
 */
public class TypeManager {
	public TypeManager() {
		throw new AssertionError();
	}

	private static final Expression[] USE_PARAMETER_ACCESSES_AS_ARGUMENTS_TO_SUPER = null;

	private static NamedUserType createTypeFor( AbstractType<?, ?, ?> superType, String typeName, AbstractType<?, ?, ?>[] parameterTypes,
			Expression[] argumentExpressions ) {
		NamedUserType rv = new NamedUserType();
		rv.name.setValue( typeName );
		rv.superType.setValue( superType );

		for( AbstractConstructor superConstructor : superType.getDeclaredConstructors() ) {
			List<? extends AbstractParameter> javaParameters = superConstructor.getRequiredParameters();

			NamedUserConstructor userConstructor = new NamedUserConstructor();
			ConstructorBlockStatement body = new ConstructorBlockStatement();
			SuperConstructorInvocationStatement superConstructorInvocationStatement = new SuperConstructorInvocationStatement();

			superConstructorInvocationStatement.constructor.setValue( superConstructor );

			final int N = javaParameters.size();
			for( int i = 0; i < N; i++ ) {
				AbstractParameter javaParameterI = javaParameters.get( i );
				Expression argumentExpressionI;
				if( argumentExpressions != USE_PARAMETER_ACCESSES_AS_ARGUMENTS_TO_SUPER ) {
					argumentExpressionI = argumentExpressions[ i ];
				} else {
					String parameterName = javaParameterI.getName(); //todo?
					if( parameterName != null ) {
						//pass
					} else {
						parameterName = "p" + i;
					}
					AbstractType<?, ?, ?> parameterTypeI;
					if( parameterTypes != null ) {
						parameterTypeI = parameterTypes[ i ];
					} else {
						parameterTypeI = javaParameterI.getValueType();
					}
					UserParameter userParameterI = new UserParameter( parameterName, parameterTypeI );
					userConstructor.requiredParameters.add( userParameterI );
					argumentExpressionI = new ParameterAccess( userParameterI );
				}
				superConstructorInvocationStatement.requiredArguments.add( new SimpleArgument( javaParameterI, argumentExpressionI ) );
			}

			body.constructorInvocationStatement.setValue( superConstructorInvocationStatement );
			userConstructor.body.setValue( body );

			rv.constructors.add( userConstructor );
		}

		IDE ide = IDE.getActiveInstance();
		if( ide != null ) {
			ide.getApiConfigurationManager().augmentTypeIfNecessary( rv );
		}
		return rv;
	}

	private static class MatchesNameTypeCriterion implements Criterion<NamedUserType> {
		private final String typeName;

		public MatchesNameTypeCriterion( String typeName ) {
			this.typeName = typeName;
		}

		@Override
		public boolean accept( NamedUserType userType ) {
			String userTypeName = userType.getName();
			return userTypeName.equals(this.typeName);
		}
	}

	private static abstract class ExtendsTypeCriterion implements Criterion<NamedUserType> {
		private final AbstractType<?, ?, ?> superType;

		public ExtendsTypeCriterion( AbstractType<?, ?, ?> superType ) {
			assert superType != null;
			this.superType = superType;
		}

		@Override
		public boolean accept( NamedUserType userType ) {
			return userType.superType.getValue() == this.superType;
		}
	}

	private static final class DefaultConstructorExtendsTypeCriterion extends ExtendsTypeCriterion {
		public DefaultConstructorExtendsTypeCriterion( AbstractType<?, ?, ?> superType ) {
			super( superType );
		}
	}

	private static final class ExtendsTypeWithConstructorParameterTypeCriterion extends ExtendsTypeCriterion {
		private final AbstractType<?, ?, ?> parameterType;

		public ExtendsTypeWithConstructorParameterTypeCriterion( AbstractType<?, ?, ?> superType, AbstractType<?, ?, ?> parameterType ) {
			super( superType );
			assert parameterType != null;
			this.parameterType = parameterType;
		}

		@Override
		public boolean accept( NamedUserType userType ) {
			if( super.accept( userType ) ) {
				AbstractConstructor constructor = userType.getDeclaredConstructor( this.parameterType );
				if( constructor != null ) {
					AbstractParameter parameter0 = constructor.getRequiredParameters().get( 0 );
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
		private final AbstractField superArgumentField;

		public ExtendsTypeWithSuperArgumentFieldCriterion( AbstractType<?, ?, ?> superType, AbstractField superArgumentField ) {
			super( superType );
			assert superArgumentField != null;
			this.superArgumentField = superArgumentField;
		}

		@Override
		public boolean accept( NamedUserType userType ) {
			if( super.accept( userType ) ) {
				NamedUserConstructor constructor = userType.getDeclaredConstructor();
				if( constructor != null ) {
					ConstructorInvocationStatement constructorInvocationStatement = constructor.body.getValue().constructorInvocationStatement.getValue();
					if( constructorInvocationStatement instanceof SuperConstructorInvocationStatement ) {
						if( constructorInvocationStatement.requiredArguments.size() == 1 ) {
							Expression argumentExpression = constructorInvocationStatement.requiredArguments.get( 0 ).expression.getValue();
							if( argumentExpression instanceof FieldAccess ) {
								FieldAccess fieldAccess = (FieldAccess)argumentExpression;
								return fieldAccess.field.getValue() == this.superArgumentField;
							}
						}
					}
				}
			}
			return false;
		}
	}

	private static final class ExtendsTypeWithSuperArgumentExpressionsCriterion extends ExtendsTypeCriterion {
		private final Expression[] superArgumentExpressions;

		public ExtendsTypeWithSuperArgumentExpressionsCriterion( AbstractType<?, ?, ?> superType, Expression[] argumentExpressions ) {
			super( superType );
			assert argumentExpressions != null;
			this.superArgumentExpressions = argumentExpressions;
		}

		@Override
		public boolean accept( NamedUserType userType ) {
			if( super.accept( userType ) ) {
				NamedUserConstructor constructor = userType.getDeclaredConstructor();
				if( constructor != null ) {
					ConstructorInvocationStatement constructorInvocationStatement = constructor.body.getValue().constructorInvocationStatement.getValue();
					if( constructorInvocationStatement instanceof SuperConstructorInvocationStatement ) {
						if( constructorInvocationStatement.requiredArguments.size() == superArgumentExpressions.length ) {
							for (int i=0; i<constructorInvocationStatement.requiredArguments.size(); i++) {
								Expression requiredArgumentExpression = constructorInvocationStatement.requiredArguments.get( i ).expression.getValue();
								Expression passedArgumentExpression = superArgumentExpressions[i];
								if( !requiredArgumentExpression.isEquivalentTo(passedArgumentExpression)) {
									return false;
								}
							}
							return true;
						}
					}
				}
			}
			return false;
		}
	}

	private static List<AbstractType<?, ?, ?>> updateArgumentTypes( List<AbstractType<?, ?, ?>> rv, AbstractType<?, ?, ?> rootArgumentType,
			AbstractType<?, ?, ?> argumentType ) {
		rv.add( argumentType );
		if( argumentType == rootArgumentType ) {
			//pass
		} else {
			AbstractType<?, ?, ?>[] interfaces = argumentType.getInterfaces();
			AbstractType<?, ?, ?> nextType;
			if( interfaces.length == 1 ) {
				nextType = interfaces[ 0 ];
			} else {
				nextType = argumentType.getSuperType();
			}
			updateArgumentTypes( rv, rootArgumentType, nextType );
		}
		return rv;
	}

	private static AbstractType<?, ?, ?>[] getArgumentTypes( AbstractType<?, ?, ?> ancestorType, AbstractType<?, ?, ?> resourceType ) {
		List<AbstractType<?, ?, ?>> types = Lists.newLinkedList();
		updateArgumentTypes( types, ConstructorArgumentUtilities.getContructor0Parameter0Type( ancestorType ), resourceType );
		AbstractType<?, ?, ?>[] rv = new AbstractType<?, ?, ?>[ types.size() ];
		types.toArray( rv );
		return rv;
	}

	private static AbstractType<?, ?, ?>[] getArgumentTypes( AbstractType<?, ?, ?> ancestorType, AbstractField field ) {
		return getArgumentTypes( ancestorType, field.getDeclaringType() );
	}

	private static void appendTypeName( StringBuilder sb, String name ) {
		sb.append( name );
	}

	private static String createClassNameFromResourceType( AbstractType<?, ?, ?> resourceType ) {
		StringBuilder sb = new StringBuilder();
		appendTypeName( sb, resourceType.getName().replace( "Resource", "" ) );
		return sb.toString();
	}

	public static String createClassNameFromArgumentField( AbstractType<?, ?, ?> ancestorType, AbstractField argumentField ) {
		AbstractType<?, ?, ?>[] argumentTypes = getArgumentTypes( ancestorType, argumentField );
		return createClassNameFromResourceType( argumentTypes[ 0 ] );
	}

	public static String createClassNameFromSuperType( AbstractType<?, ?, ?> superType ) {
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

	private static final JavaMethod SET_JOINTED_MODEL_RESOURCE_METHOD = JavaMethod.getInstance( SJointedModel.class, "setJointedModelResource", JointedModelResource.class );

	private static NamedUserType getNamedUserTypeFor( JavaType ancestorType, AbstractType<?, ?, ?>[] argumentTypes, int i, AbstractField argumentField ) {
		return getNamedUserTypeFor(ancestorType, argumentTypes, i, argumentField, null, null);
	}

	private static NamedUserType getNamedUserTypeFor( JavaType ancestorType, AbstractType<?, ?, ?>[] argumentTypes, int i, AbstractField argumentField, Expression[] argumentExpressions, String className ) {
		AbstractType<?, ?, ?> superType;
		final int LAST_INDEX = argumentTypes.length - 1;
		if( i < LAST_INDEX ) {
			superType = getNamedUserTypeFor( ancestorType, argumentTypes, i + 1, null, null, null );
		} else {
			superType = ancestorType;
		}

		//Use the classname passed in to create a new named user type. If no class name is passed in, generate one
		String name = className;
		if (name == null) {
			name = createClassNameFromResourceType(argumentTypes[i]);
		}

		//Find a previously created matching type
		//Dave: Why aren't we just checking names? All of our types need to be unique names, so this should be an easy check
		//These criterions are checking type and referenced fields and at no point are names checked. Sigh.
		//Let's try just checking names
		Criterion<NamedUserType> criterion = new MatchesNameTypeCriterion(name);
//		if( argumentField != null ) {
//			criterion = new ExtendsTypeWithSuperArgumentFieldCriterion( superType, argumentField );
//		} else if (argumentExpressions != null ) {
//			criterion = new ExtendsTypeWithSuperArgumentExpressionsCriterion(superType, argumentExpressions);
//		}
//		else {
//			criterion = new ExtendsTypeWithConstructorParameterTypeCriterion( superType, argumentTypes[ i ] );
//		}
		Project project = ProjectStack.peekProject();
		if( project != null ) {
			Set<NamedUserType> existingTypes = project.getNamedUserTypes();
			for( NamedUserType existingType : existingTypes ) {
				if( criterion.accept( existingType ) ) {
					return existingType;
				}
			}
		}
		Expression[] expressions;
		if (argumentExpressions != null) {
			expressions = argumentExpressions;
		}
		else {
			if (argumentField != null) {
				expressions = new Expression[]{
						new FieldAccess(
								new TypeExpression(argumentField.getDeclaringType()),
								argumentField)
				};
			} else {
				expressions = USE_PARAMETER_ACCESSES_AS_ARGUMENTS_TO_SUPER;
			}
		}
		NamedUserType rv = createTypeFor( superType, name, new AbstractType[] { argumentTypes[ i ] }, expressions );
		if( argumentTypes[ i ] instanceof JavaType ) {
			JavaType javaArgumentTypeI = (JavaType)argumentTypes[ i ];
			Class<?> cls = javaArgumentTypeI.getClassReflectionProxy().getReification();
			if( ReflectionUtilities.isFinal( cls ) ) {
				boolean isSetResourceMethodDesired;
				if( cls.isEnum() ) {
					isSetResourceMethodDesired = cls.getEnumConstants().length > 1;
				} else {
					isSetResourceMethodDesired = true;
				}
				if( isSetResourceMethodDesired ) {
					String simpleClsName = cls.getSimpleName();
					UserMethod setResourceMethod = new UserMethod();
					setResourceMethod.managementLevel.setValue( ManagementLevel.GENERATED );
					setResourceMethod.name.setValue( "set" + simpleClsName );
					setResourceMethod.returnType.setValue( JavaType.VOID_TYPE );
					BlockStatement body = new BlockStatement();
					setResourceMethod.body.setValue( body );

					StringBuilder parameterNameSB = new StringBuilder();
					parameterNameSB.append( Character.toLowerCase( simpleClsName.charAt( 0 ) ) );
					parameterNameSB.append( simpleClsName.substring( 1 ) );
					UserParameter parameter = new UserParameter();
					parameter.name.setValue( parameterNameSB.toString() );
					parameter.valueType.setValue( javaArgumentTypeI );

					setResourceMethod.requiredParameters.add( parameter );

					body.statements.add(
							AstUtilities.createMethodInvocationStatement(
									new ThisExpression(),
									SET_JOINTED_MODEL_RESOURCE_METHOD,
									new ParameterAccess( parameter ) ) );

					setResourceMethod.isSignatureLocked.setValue( true );
					rv.methods.add( setResourceMethod );
				}
			}
		}
		return rv;
	}

	public static JavaField getEnumConstantFieldIfOneAndOnly( AbstractType<?, ?, ?> type ) {
		JavaField rv = null;
		if( type instanceof JavaType ) {
			JavaType javaType = (JavaType)type;
			if( type.isAssignableTo( Enum.class ) ) {
				Class<Enum<?>> cls = (Class<Enum<?>>)javaType.getClassReflectionProxy().getReification();
				Enum<?>[] constants = cls.getEnumConstants();
				if( constants.length == 1 ) {
					Enum<?> constant = constants[ 0 ];
					rv = JavaField.getInstance( constant.getClass(), constant.name() );
				}
			}
		}
		return rv;
	}

	public static NamedUserType getNamedUserTypeFromArgumentField( JavaType ancestorType, JavaField argumentField ) {
		AbstractType<?, ?, ?>[] argumentTypes = getArgumentTypes( ancestorType, argumentField );
		return getNamedUserTypeFor( ancestorType, argumentTypes, 0, getEnumConstantFieldIfOneAndOnly( argumentTypes[ 0 ] ) );
	}

	public static NamedUserType getNamedUserTypeFromDynamicResourceInstanceCreation( JavaType resourceType, InstanceCreation instanceCreation, String className ) {
		AbstractType<?, ?, ?>[] argumentTypes = getArgumentTypes( resourceType, instanceCreation.getType() );
		return getNamedUserTypeFor( resourceType, argumentTypes, 0, null, new Expression[] {instanceCreation}, className );
	}

	public static NamedUserType getNamedUserTypeFromPersonResourceInstanceCreation( InstanceCreation instanceCreation ) {
		JavaType bipedType = JavaType.getInstance( SBiped.class );
		AbstractType<?, ?, ?>[] argumentTypes = getArgumentTypes( bipedType, instanceCreation.getType() );
		return getNamedUserTypeFor( bipedType, argumentTypes, 0, null );
	}

	public static NamedUserType getNamedUserTypeFromSuperType( JavaType superType ) {
		AbstractType<?, ?, ?> parameter0Type = ConstructorArgumentUtilities.getContructor0Parameter0Type( superType );
		ExtendsTypeCriterion criterion;
		if( parameter0Type != null ) {
			criterion = new ExtendsTypeWithConstructorParameterTypeCriterion( superType, ConstructorArgumentUtilities.getContructor0Parameter0Type( superType ) );
		} else {
			criterion = new DefaultConstructorExtendsTypeCriterion( superType );
		}
		IDE ide = IDE.getActiveInstance();
		if( ide != null ) {
			Project project = ide.getProject();
			if( project != null ) {
				Set<NamedUserType> existingTypes = project.getNamedUserTypes();
				for( NamedUserType existingType : existingTypes ) {
					if( criterion.accept( existingType ) ) {
						return existingType;
					}
				}
			}
		}
		return createTypeFor( superType, createClassNameFromSuperType( superType ), null, null );
	}

	public static List<NamedUserType> getNamedUserTypesFromSuperTypes( Collection<JavaType> superTypes ) {
		ArrayList<NamedUserType> rv = Lists.newArrayListWithInitialCapacity( superTypes.size() );
		for( JavaType superType : superTypes ) {
			rv.add( getNamedUserTypeFromSuperType( superType ) );
		}
		return rv;
	}
}
