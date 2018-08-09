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

import com.sun.tools.internal.ws.processor.model.java.JavaParameter;
import org.lgna.project.ast.*;
import org.lgna.story.resources.ModelResource;
import org.lgna.story.resources.ModelStructure;

import java.util.List;

/**
 * @author Dennis Cosgrove
 */
@Deprecated
public class ConstructorArgumentUtilities {
	private ConstructorArgumentUtilities() {
		throw new AssertionError();
	}

	public static AbstractConstructor getContructor0( AbstractType<?, ?, ?> type ) {
		if( type != null ) {
			List<? extends AbstractConstructor> constructors = type.getDeclaredConstructors();
			if( constructors.size() > 0 ) {
				AbstractConstructor constructor0 = constructors.get( 0 );
				return constructor0;
			}
		}
		return null;
	}

	public static AbstractType<?, ?, ?> getParameter0Type( AbstractConstructor constructor ) {
		if( constructor != null ) {
			List<? extends AbstractParameter> requiredParameters = constructor.getRequiredParameters();
			if( requiredParameters.size() > 0 ) {
				AbstractParameter parameter0 = requiredParameters.get( 0 );
				return parameter0.getValueType();
			}
		}
		return null;
	}

	public static AbstractType<?, ?, ?> getContructor0Parameter0Type( AbstractType<?, ?, ?> type ) {
		return getParameter0Type( getContructor0( type ) );
	}

	public static JavaField getField( SimpleArgumentListProperty arguments ) {
		if( arguments.size() > 0 ) {
			Expression expression = arguments.get( 0 ).expression.getValue();
			if( expression instanceof FieldAccess ) {
				FieldAccess fieldAccess = (FieldAccess)expression;
				return (JavaField)fieldAccess.field.getValue();
			}
		}
		return null;
	}

	public static JavaField getArgumentField( AbstractConstructor constructor ) {
		if( constructor instanceof NamedUserConstructor ) {
			NamedUserConstructor namedUserConstructor = (NamedUserConstructor)constructor;
			ConstructorInvocationStatement constructorInvocationStatement = namedUserConstructor.body.getValue().constructorInvocationStatement.getValue();
			return getField( constructorInvocationStatement.requiredArguments );
		}
		return null;
	}

	public static ModelResource getModelResourcePassedToSuperConstructor(AbstractConstructor constructor) {
		if( constructor instanceof NamedUserConstructor ) {
			NamedUserConstructor namedUserConstructor = (NamedUserConstructor)constructor;
			ConstructorInvocationStatement superConstructorInvocationStatement = namedUserConstructor.body.getValue().constructorInvocationStatement.getValue();
			SimpleArgumentListProperty arguments = superConstructorInvocationStatement.requiredArguments;
			if( arguments.size() > 0 ) {
				Expression expression = arguments.get( 0 ).expression.getValue();
				if( expression instanceof FieldAccess ) {
					FieldAccess fieldAccess = (FieldAccess)expression;
					JavaField javaField = (JavaField)fieldAccess.field.getValue();
					try {
						Object value = javaField.getFieldReflectionProxy().getReification().get(null);
						return (ModelResource)value;
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				//This handles the case of a DynamicResource
				//DynamicResources use a constructor that looks like this:
				//	public DyanamicBipedResource(String modelName, String resourceName)
				//And the InstanceCreation looks like this:
				//	AstUtilities.createInstanceCreation(
				//		DynamicBipedResource.class,
				//		new Class<?>[] {
				//			String.class,
				//			String.class
				//		},
				//		new StringLiteral( dynamicResource.getModelClassName() ),
				//		new StringLiteral( dynamicResource.getModelVariantName() ));
				//This code pulls this information out of the InstanceCreation and then uses it to construct a new DynamicResource
				else if (expression instanceof InstanceCreation) {
					InstanceCreation instanceCreation = (InstanceCreation)expression;
					if (instanceCreation.constructor.getValue() instanceof JavaConstructor) {
						JavaConstructor instanceConstructor = (JavaConstructor)instanceCreation.constructor.getValue();
						Object[] constructorArguments = new Object[instanceConstructor.getRequiredParameters().size()];
						int index = 0;
						boolean canEvaluate = true;
						for (SimpleArgument argument : instanceCreation.requiredArguments.getValue()) {
							if (argument.expression.getValue() instanceof StringLiteral) {
								StringLiteral literal = (StringLiteral)argument.expression.getValue();
								String argumentValue = literal.getValueProperty().getValue();
								constructorArguments[index++] = argumentValue;
							}
							else {
								canEvaluate = false;
								break;
							}
						}
						if (canEvaluate) {
							Object instanceValue = instanceConstructor.evaluate(null, null, constructorArguments);
							if (instanceValue instanceof ModelResource) {
								return (ModelResource)instanceValue;
							}
						}
					}
				}
			}
		}
		return null;
	}

	public static JavaField getArgumentField( InstanceCreation instanceCreation ) {
		if( instanceCreation != null ) {
			AbstractConstructor constructor = instanceCreation.constructor.getValue();
			if( instanceCreation.requiredArguments.size() == 1 ) {
				return getField( instanceCreation.requiredArguments );
			} else {
				return getArgumentField( constructor );
			}
		} else {
			return null;
		}
	}
}
