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
package org.alice.stageide.croquet.models.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
public class DeclareFieldFromImportedTypeOperation extends org.lgna.croquet.IteratingOperation {
	private static void showMessageDialog( java.io.File file, boolean isValidZip ) {
		String applicationName = org.alice.ide.IDE.getActiveInstance().getApplicationName();
		StringBuffer sb = new StringBuffer();
		sb.append( "Unable to create instance from file " );
		sb.append( edu.cmu.cs.dennisc.java.io.FileUtilities.getCanonicalPathIfPossible( file ) );
		sb.append( ".\n\n" );
		sb.append( applicationName );
		sb.append( " is able to create instances from class files saved by " );
		sb.append( applicationName );
		sb.append( ".\n\nLook for files with an " );
		sb.append( org.lgna.project.io.IoUtilities.TYPE_EXTENSION );
		sb.append( " extension." );
		org.lgna.croquet.Application.getActiveInstance().showMessageDialog( sb.toString(), "Cannot read file", org.lgna.croquet.MessageType.ERROR );
	}

	private static final org.lgna.croquet.history.Step.Key< Stage > STAGE_KEY = org.lgna.croquet.history.Step.Key.createInstance( "DeclareFieldFromImportedTypeOperation.STAGE_KEY" );
	private static enum Stage {
		REQUESTING_URI {
			@Override
			public Stage getNextStage() {
				return DECLARING_FIELD;
			}
			@Override
			public org.lgna.croquet.Model getModel( org.lgna.croquet.history.OperationStep step ) {
				return TypeUriProducer.getInstance();
			}
		},
		DECLARING_FIELD {
			@Override
			public Stage getNextStage() {
				return null;
			}
			@Override
			public org.lgna.croquet.Model getModel( org.lgna.croquet.history.OperationStep step ) {
				org.lgna.croquet.history.ValueProducerStep<java.net.URI> valueProducerStep = step.getFirstStepOfEquivalentModel( REQUESTING_URI.getModel( step ), org.lgna.croquet.history.ValueProducerStep.class );
				java.net.URI uri = valueProducerStep.getModel().getValue( valueProducerStep );
				if( uri != null ) {
					java.io.File file = edu.cmu.cs.dennisc.java.net.UriUtilities.getFile( uri );
					String lcName = file.getName().toLowerCase();
					if( lcName.endsWith( ".a2c" ) ) {
						org.lgna.croquet.Application.getActiveInstance().showMessageDialog( 
								"Alice3 does not load Alice2 characters", 
								"Incorrect File Type", 
								org.lgna.croquet.MessageType.ERROR 
						);
					} else if( lcName.endsWith( org.lgna.project.io.IoUtilities.PROJECT_EXTENSION.toLowerCase() ) ) {
						org.lgna.croquet.Application.getActiveInstance().showMessageDialog( 
								file.getAbsolutePath() + " appears to be a project file and not a class file.\n\nLook for files with an " + org.lgna.project.io.IoUtilities.TYPE_EXTENSION + " extension.",
								"Incorrect File Type", 
								org.lgna.croquet.MessageType.INFORMATION 
						);
					} else {
						boolean isWorthyOfException = lcName.endsWith( org.lgna.project.io.IoUtilities.TYPE_EXTENSION.toLowerCase() );
						java.util.zip.ZipFile zipFile;
						try {
							zipFile = new java.util.zip.ZipFile( file );
						} catch( java.io.IOException ioe ) {
							if( isWorthyOfException ) {
								throw new RuntimeException( file.getAbsolutePath(), ioe );
							} else {
								showMessageDialog( file, false );
								zipFile = null;
							}
						}
						if( zipFile != null ) {
							org.lgna.project.ast.AbstractType<?,?,?> type;
							try {
								edu.cmu.cs.dennisc.pattern.Tuple2< ? extends org.lgna.project.ast.AbstractType<?,?,?>, java.util.Set< org.lgna.common.Resource > > tuple = org.lgna.project.io.IoUtilities.readType( zipFile );
								type = tuple.getA();
								edu.cmu.cs.dennisc.print.PrintUtilities.println( "TODO: add in resources" );
							} catch( org.lgna.project.VersionNotSupportedException vnse ) {
								type = null;
								org.alice.ide.IDE.getActiveInstance().handleVersionNotSupported( file, vnse );
							} catch( java.io.IOException ioe ) {
								if( isWorthyOfException ) {
									throw new RuntimeException( file.getAbsolutePath(), ioe );
								} else {
									showMessageDialog( file, true );
									type = null;
								}
							}
							if( type != null ) {
								org.lgna.project.ast.AbstractConstructor constructor = type.getDeclaredConstructors().get( 0 );
								java.util.ArrayList< ? extends org.lgna.project.ast.AbstractParameter > requiredParameters = constructor.getRequiredParameters();
								org.lgna.croquet.DropSite dropSite = null;
								if( requiredParameters.size() > 0 ) {
									org.lgna.project.ast.AbstractType< ?,?,? > parameterType = requiredParameters.get( 0 ).getValueType();
									return org.alice.ide.croquet.models.gallerybrowser.ResourceCascade.getInstance( parameterType, dropSite );
								} else {
									org.lgna.project.ast.JavaField argumentField = org.alice.ide.typemanager.ConstructorArgumentUtilities.getArgumentField( constructor );
									return org.alice.ide.croquet.models.declaration.ArgumentFieldSpecifiedManagedFieldDeclarationOperation.getInstance( argumentField, dropSite );
								}
							}
						}
					}
				}
				return null;
			}
		};
		public abstract Stage getNextStage();
		public abstract org.lgna.croquet.Model getModel( org.lgna.croquet.history.OperationStep step );
	}
	
	private static class SingletonHolder {
		private static DeclareFieldFromImportedTypeOperation instance = new DeclareFieldFromImportedTypeOperation();
	}
	public static DeclareFieldFromImportedTypeOperation getInstance() {
		return SingletonHolder.instance;
	}
	private DeclareFieldFromImportedTypeOperation() {
		super( org.alice.ide.IDE.PROJECT_GROUP, java.util.UUID.fromString( "d5578a1f-2d43-4d42-802f-62016d82e92b" ) );
	}
	@Override
	protected boolean hasNext( org.lgna.croquet.history.OperationStep step ) {
		Stage nextStage;
		if( step.containsEphemeralDataFor( STAGE_KEY ) ) {
			Stage prevStage = step.getEphemeralDataFor( STAGE_KEY );
			nextStage = prevStage.getNextStage();
		} else {
			nextStage = Stage.REQUESTING_URI;
		}
		step.putEphemeralDataFor( STAGE_KEY, nextStage );
		return nextStage != null;
	}
	@Override
	protected org.lgna.croquet.Model getNext( org.lgna.croquet.history.OperationStep step ) {
		Stage stage = step.getEphemeralDataFor( STAGE_KEY );
		if( stage != null ) {
			return stage.getModel( step );
		} else {
			return null;
		}
	}
}
