/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.stageide.gallerybrowser.uri.merge;

import org.alice.stageide.gallerybrowser.uri.merge.data.IsDeclarationImportDesiredState;

/**
 * @author Dennis Cosgrove
 */
public class ImportTypeComposite extends org.lgna.croquet.OperationInputDialogCoreComposite<org.alice.stageide.gallerybrowser.uri.merge.views.MergeTypeView> {
	private final java.net.URI uriForDescriptionPurposesOnly;

	private final org.lgna.project.ast.NamedUserType importedRootType;
	private final java.util.Set<org.lgna.common.Resource> importedResources;

	private final org.lgna.project.ast.NamedUserType srcType;
	private final org.lgna.project.ast.NamedUserType dstType;

	private final org.alice.stageide.gallerybrowser.uri.merge.data.TypeMethodCategorization procedureCategorization;
	private final org.alice.stageide.gallerybrowser.uri.merge.data.TypeMethodCategorization functionCategorization;

	private final java.util.List<IsDeclarationImportDesiredState<org.lgna.project.ast.UserField>> isFieldImportDesiredStates;
	private final java.util.List<org.lgna.project.ast.UserField> fieldsToWarnAboutValueTypeDifference;
	private final java.util.List<org.lgna.project.ast.UserField> fieldsToChooseInitializer;
	private final java.util.List<org.lgna.project.ast.UserField> fieldsToIgnore;

	private final org.lgna.croquet.PlainStringValue proceduresHeader = this.createStringValue( this.createKey( "proceduresHeader" ) );
	private final org.lgna.croquet.PlainStringValue functionsHeader = this.createStringValue( this.createKey( "functionsHeader" ) );
	private final org.lgna.croquet.PlainStringValue fieldsHeader = this.createStringValue( this.createKey( "fieldsHeader" ) );

	private boolean isManagementLevelAppropriate( org.lgna.project.ast.UserMethod method ) {
		org.lgna.project.ast.ManagementLevel managementLevel = method.getManagementLevel();
		return ( managementLevel == null ) || ( managementLevel == org.lgna.project.ast.ManagementLevel.NONE );
	}

	public ImportTypeComposite( java.net.URI uriForDescriptionPurposesOnly, org.lgna.project.ast.NamedUserType importedRootType, java.util.Set<org.lgna.common.Resource> importedResources, org.lgna.project.ast.NamedUserType srcType, org.lgna.project.ast.NamedUserType dstType ) {
		super( java.util.UUID.fromString( "d00d925e-0a2c-46c7-b6c8-0d3d1189bc5c" ), org.alice.ide.IDE.PROJECT_GROUP );
		this.uriForDescriptionPurposesOnly = uriForDescriptionPurposesOnly;
		this.importedRootType = importedRootType;
		this.importedResources = importedResources;
		this.srcType = srcType;
		this.dstType = dstType;

		if( this.dstType != null ) {
			java.util.List<org.lgna.project.ast.UserMethod> projectProcedures = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			java.util.List<org.lgna.project.ast.UserMethod> projectFunctions = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			for( org.lgna.project.ast.UserMethod projectMethod : this.dstType.methods ) {
				if( isManagementLevelAppropriate( projectMethod ) ) {
					if( projectMethod.isProcedure() ) {
						projectProcedures.add( projectMethod );
					} else {
						projectFunctions.add( projectMethod );
					}
				}
			}
			this.procedureCategorization = new org.alice.stageide.gallerybrowser.uri.merge.data.TypeMethodCategorization( projectProcedures );
			this.functionCategorization = new org.alice.stageide.gallerybrowser.uri.merge.data.TypeMethodCategorization( projectFunctions );
			for( org.lgna.project.ast.UserMethod importMethod : this.srcType.methods ) {
				if( isManagementLevelAppropriate( importMethod ) ) {
					org.alice.stageide.gallerybrowser.uri.merge.data.TypeMethodCategorization methodCategorization = importMethod.isProcedure() ? this.procedureCategorization : this.functionCategorization;
					org.lgna.project.ast.UserMethod projectMethod = MergeUtilities.findMethodWithMatchingName( importMethod, dstType );
					if( projectMethod != null ) {
						if( MergeUtilities.isEquivalent( projectMethod, importMethod ) ) {
							methodCategorization.addIdenticalMethods( projectMethod, importMethod );
						} else {
							if( MergeUtilities.isHeaderEquivalent( importMethod, projectMethod ) ) {
								methodCategorization.addDifferentImplementationMethods( projectMethod, importMethod );
							} else {
								methodCategorization.addDifferentSignatureMethods( projectMethod, importMethod );
							}
						}
					} else {
						methodCategorization.addImportOnlyMethod( importMethod );
					}
				}
			}
			this.procedureCategorization.reifyProjectOnlyMethods();
			this.functionCategorization.reifyProjectOnlyMethods();

			this.isFieldImportDesiredStates = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			this.fieldsToWarnAboutValueTypeDifference = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			this.fieldsToChooseInitializer = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			this.fieldsToIgnore = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();

			for( org.lgna.project.ast.UserField srcField : this.srcType.fields ) {
				org.lgna.project.ast.UserField dstField = dstType.getDeclaredField( srcField.getName() );
				java.util.List<org.lgna.project.ast.UserField> list;
				if( dstField != null ) {
					if( MergeUtilities.isEquivalent( srcField, dstField ) ) {
						list = this.fieldsToIgnore;
					} else {
						if( MergeUtilities.isValueTypeEquivalent( srcField, dstField ) ) {
							list = this.fieldsToChooseInitializer;
						} else {
							list = this.fieldsToWarnAboutValueTypeDifference;
						}
					}
				} else {
					this.isFieldImportDesiredStates.add( new IsDeclarationImportDesiredState<org.lgna.project.ast.UserField>( srcField ) );
					list = null;
				}
				if( list != null ) {
					list.add( srcField );
				}
			}

		} else {
			this.procedureCategorization = null;
			this.functionCategorization = null;
			this.isFieldImportDesiredStates = null;
			this.fieldsToWarnAboutValueTypeDifference = null;
			this.fieldsToChooseInitializer = null;
			this.fieldsToIgnore = null;
		}
	}

	public org.alice.stageide.gallerybrowser.uri.merge.data.TypeMethodCategorization getProcedureCategorization() {
		return this.procedureCategorization;
	}

	public org.alice.stageide.gallerybrowser.uri.merge.data.TypeMethodCategorization getFunctionCategorization() {
		return this.functionCategorization;
	}

	@Override
	protected org.alice.stageide.gallerybrowser.uri.merge.views.MergeTypeView createView() {
		return new org.alice.stageide.gallerybrowser.uri.merge.views.MergeTypeView( this );
	}

	public org.lgna.project.ast.NamedUserType getImportedType() {
		return this.importedRootType;
	}

	public java.util.Set<org.lgna.common.Resource> getImportedResources() {
		return this.importedResources;
	}

	public org.lgna.project.ast.NamedUserType getDstType() {
		return this.dstType;
	}

	public java.util.List<IsDeclarationImportDesiredState<org.lgna.project.ast.UserField>> getIsFieldImportDesiredStates() {
		return this.isFieldImportDesiredStates;
	}

	private static void appendDeclarations( StringBuilder sb, java.util.List<? extends org.lgna.project.ast.Declaration> declarations ) {
		for( org.lgna.project.ast.Declaration declaration : declarations ) {
			sb.append( "<li>" );
			sb.append( declaration.getName() );
		}
	}

	//	@Deprecated
	//	public String getLabelText() {
	//		StringBuilder sb = new StringBuilder();
	//		sb.append( "<html><em>" );
	//		if( this.methodsToWarnAboutOverload.size() > 0 ) {
	//			sb.append( "TODO: not yet adding procedures/functions that would overload:<ul>" );
	//			appendDeclarations( sb, this.methodsToWarnAboutOverload );
	//			sb.append( "</ul>" );
	//		}
	//		if( this.methodsToMerge.size() > 0 ) {
	//			sb.append( "TODO: not yet merging procedures/functions (different implementations):<ul>" );
	//			appendDeclarations( sb, this.methodsToMerge );
	//			sb.append( "</ul>" );
	//		}
	//		if( this.methodsToIgnore.size() > 0 ) {
	//			sb.append( "NOTE: not adding the following procedures/functions (identical):<ul>" );
	//			appendDeclarations( sb, this.methodsToIgnore );
	//			sb.append( "</ul>" );
	//		}
	//		if( this.fieldsToWarnAboutValueTypeDifference.size() > 0 ) {
	//			sb.append( "TODO: not yet adding properties that would result in multiple properties with same name:<ul>" );
	//			appendDeclarations( sb, this.fieldsToWarnAboutValueTypeDifference );
	//			sb.append( "</ul>" );
	//		}
	//		if( this.fieldsToChooseInitializer.size() > 0 ) {
	//			sb.append( "TODO: not yet choosing property initializers (different):<ul>" );
	//			appendDeclarations( sb, this.fieldsToChooseInitializer );
	//			sb.append( "</ul>" );
	//		}
	//		if( this.fieldsToIgnore.size() > 0 ) {
	//			sb.append( "NOTE: not adding the following properties (identical):<ul>" );
	//			appendDeclarations( sb, this.fieldsToIgnore );
	//			sb.append( "</ul>" );
	//		}
	//		sb.append( "</em></html>" );
	//		return sb.toString();
	//	}

	@Override
	protected org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		if( this.dstType != null ) {
			java.util.List<org.lgna.project.ast.UserMethod> methods = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();

			for( org.alice.stageide.gallerybrowser.uri.merge.data.TypeMethodCategorization categorization : new org.alice.stageide.gallerybrowser.uri.merge.data.TypeMethodCategorization[] { this.procedureCategorization, this.functionCategorization } ) {
				for( org.alice.stageide.gallerybrowser.uri.merge.data.ImportOnlyMethod importOnlyMethod : categorization.getImportOnlyMethods() ) {
					if( importOnlyMethod.getState().getValue() ) {
						methods.add( org.lgna.project.ast.AstUtilities.createCopy( importOnlyMethod.getImportMethod(), this.importedRootType ) );
					}
				}
			}
			java.util.List<org.lgna.project.ast.UserField> fields = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			for( IsDeclarationImportDesiredState<org.lgna.project.ast.UserField> fieldState : this.isFieldImportDesiredStates ) {
				if( fieldState.getValue() ) {
					fields.add( org.lgna.project.ast.AstUtilities.createCopy( fieldState.getDeclaration(), this.importedRootType ) );
				}
			}
			return new org.alice.stageide.gallerybrowser.uri.merge.edits.ImportTypeEdit( completionStep, this.uriForDescriptionPurposesOnly, this.dstType, methods, fields );
		} else {
			return null;
		}
	}

	@Override
	protected Status getStatusPreRejectorCheck( org.lgna.croquet.history.CompletionStep<?> step ) {
		return IS_GOOD_TO_GO_STATUS;
	}

	public org.lgna.croquet.PlainStringValue getProceduresHeader() {
		return this.proceduresHeader;
	}

	public org.lgna.croquet.PlainStringValue getFunctionsHeader() {
		return this.functionsHeader;
	}

	public org.lgna.croquet.PlainStringValue getFieldsHeader() {
		return this.fieldsHeader;
	}

	public static void main( String[] args ) throws Exception {
		javax.swing.UIManager.LookAndFeelInfo lookAndFeelInfo = edu.cmu.cs.dennisc.javax.swing.plaf.PlafUtilities.getInstalledLookAndFeelInfoNamed( "Nimbus" );
		if( lookAndFeelInfo != null ) {
			javax.swing.UIManager.setLookAndFeel( lookAndFeelInfo.getClassName() );
		}

		new org.lgna.croquet.simple.SimpleApplication();

		java.io.File projectFile = new java.io.File( args[ 0 ] );

		org.lgna.project.Project project = org.lgna.project.io.IoUtilities.readProject( projectFile );
		org.alice.ide.ProjectStack.pushProject( project );

		java.io.File typeFile = new java.io.File( args[ 1 ] );

		edu.cmu.cs.dennisc.pattern.Tuple2<org.lgna.project.ast.NamedUserType, java.util.Set<org.lgna.common.Resource>> tuple = org.lgna.project.io.IoUtilities.readType( typeFile );
		org.lgna.project.ast.NamedUserType importedRootType = tuple.getA();
		java.util.Set<org.lgna.common.Resource> importedResources = tuple.getB();
		org.lgna.project.ast.NamedUserType srcType = importedRootType;
		org.lgna.project.ast.NamedUserType dstType = org.alice.stageide.gallerybrowser.uri.merge.MergeUtilities.findMatchingTypeInExistingTypes( srcType );
		org.alice.stageide.gallerybrowser.uri.merge.ImportTypeComposite mergeTypeComposite = new org.alice.stageide.gallerybrowser.uri.merge.ImportTypeComposite( typeFile.toURI(), importedRootType, importedResources, srcType, dstType );
		mergeTypeComposite.getOperation().fire();
	}
}
