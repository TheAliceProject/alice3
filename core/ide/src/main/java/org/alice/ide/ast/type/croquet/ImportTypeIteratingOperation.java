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
package org.alice.ide.ast.type.croquet;

/**
 * @author Dennis Cosgrove
 */
public final class ImportTypeIteratingOperation extends org.lgna.croquet.SingleThreadIteratingOperation {
	private final org.lgna.project.ast.NamedUserType dstType;

	public ImportTypeIteratingOperation( org.lgna.project.ast.NamedUserType dstType ) {
		super( org.lgna.croquet.Application.PROJECT_GROUP, java.util.UUID.fromString( "bae897e2-63cb-481a-8ff6-41c99052a026" ) );
		this.dstType = dstType;
		this.setButtonIcon( new edu.cmu.cs.dennisc.javax.swing.icons.LineAxisIcon( org.alice.ide.icons.Icons.FOLDER_ICON_SMALL, org.alice.stageide.icons.PlusIconFactory.getInstance().getIcon( new java.awt.Dimension( org.alice.ide.icons.Icons.FOLDER_ICON_SMALL.getIconWidth(), org.alice.ide.icons.Icons.FOLDER_ICON_SMALL.getIconHeight() ) ) ) );
	}

	@Override
	protected boolean hasNext( org.lgna.croquet.history.CompletionStep<?> step, java.util.List<org.lgna.croquet.history.Step<?>> subSteps, Object iteratingData ) {
		return subSteps.size() < 2;
	}

	@Override
	protected org.lgna.croquet.Model getNext( org.lgna.croquet.history.CompletionStep<?> step, java.util.List<org.lgna.croquet.history.Step<?>> subSteps, Object iteratingData ) {
		switch( subSteps.size() ) {
		case 0:
			return ImportTypeFileDialogValueCreator.getInstance();
		case 1:
			org.lgna.croquet.history.Step<?> prevSubStep = subSteps.get( 0 );
			if( prevSubStep.containsEphemeralDataFor( org.lgna.croquet.ValueCreator.VALUE_KEY ) ) {
				java.io.File file = (java.io.File)prevSubStep.getEphemeralDataFor( org.lgna.croquet.ValueCreator.VALUE_KEY );
				try {
					edu.cmu.cs.dennisc.pattern.Tuple2<org.lgna.project.ast.NamedUserType, java.util.Set<org.lgna.common.Resource>> tuple = org.lgna.project.io.IoUtilities.readType( file );
					org.lgna.project.ast.NamedUserType importedType = tuple.getA();
					java.util.Set<org.lgna.common.Resource> importedResources = tuple.getB();
					org.lgna.project.ast.NamedUserType srcType;
					if( importedType.getName().contentEquals( this.dstType.getName() ) ) {
						srcType = importedType;
					} else {
						srcType = null;
						edu.cmu.cs.dennisc.pattern.IsInstanceCrawler<org.lgna.project.ast.NamedUserType> crawler = edu.cmu.cs.dennisc.pattern.IsInstanceCrawler.createInstance( org.lgna.project.ast.NamedUserType.class );
						importedType.crawl( crawler, org.lgna.project.ast.CrawlPolicy.COMPLETE );
						for( org.lgna.project.ast.NamedUserType type : crawler.getList() ) {
							if( type.getName().contentEquals( this.dstType.getName() ) ) {
								srcType = type;
								break;
							}
						}
					}
					if( srcType != null ) {
						return new ImportTypeWizard( file.toURI(), importedType, importedResources, srcType, this.dstType ).getLaunchOperation();
					} else {
						new edu.cmu.cs.dennisc.javax.swing.option.OkDialog.Builder( "Cannot find class " + this.dstType.getName() + " in " + file )
								.buildAndShow();
						return null;
					}
				} catch( java.io.IOException ioe ) {
					throw new RuntimeException( ioe );
				} catch( org.lgna.project.VersionNotSupportedException vnse ) {
					new edu.cmu.cs.dennisc.javax.swing.option.OkDialog.Builder( "version not supported " + vnse.getVersion() )
							.buildAndShow();
				}
				return null;
			} else {
				return null;
			}
		default:
			return null;
		}
	}

	@Override
	protected void handleSuccessfulCompletionOfSubModels( org.lgna.croquet.history.CompletionStep<?> step, java.util.List<org.lgna.croquet.history.Step<?>> subSteps ) {
		step.finish();
	}
}
