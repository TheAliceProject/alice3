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
package org.alice.ide.ast.type.croquet;

import edu.cmu.cs.dennisc.javax.swing.icons.LineAxisIcon;
import edu.cmu.cs.dennisc.javax.swing.option.OkDialog;
import edu.cmu.cs.dennisc.pattern.IsInstanceCrawler;
import org.alice.ide.icons.Icons;
import org.alice.stageide.StageIDE;
import org.alice.stageide.icons.PlusIconFactory;
import org.lgna.common.Resource;
import org.lgna.croquet.Application;
import org.lgna.croquet.FileDialogValueCreator;
import org.lgna.croquet.SingleThreadIteratingOperation;
import org.lgna.croquet.Triggerable;
import org.lgna.croquet.history.UserActivity;
import org.lgna.project.VersionNotSupportedException;
import org.lgna.project.ast.CrawlPolicy;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.io.IoUtilities;
import org.lgna.project.io.TypeResourcesPair;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public final class ImportTypeIteratingOperation extends SingleThreadIteratingOperation {
	private final NamedUserType dstType;

	public ImportTypeIteratingOperation( NamedUserType dstType ) {
		super( Application.PROJECT_GROUP, UUID.fromString( "bae897e2-63cb-481a-8ff6-41c99052a026" ) );
		this.dstType = dstType;
		this.setButtonIcon( new LineAxisIcon( Icons.FOLDER_ICON_SMALL, PlusIconFactory.getInstance().getIcon( new Dimension( Icons.FOLDER_ICON_SMALL.getIconWidth(), Icons.FOLDER_ICON_SMALL.getIconHeight() ) ) ) );
	}

	@Override
	protected boolean hasNext( List<UserActivity> finishedSteps ) {
		return finishedSteps.size() < 2;
	}

	@Override
	protected Triggerable getNext( List<UserActivity> finishedSteps ) {
		switch( finishedSteps.size() ) {
		case 0:
			return new FileDialogValueCreator( null, StageIDE.getActiveInstance().getTypesDirectory(), IoUtilities.TYPE_EXTENSION );
		case 1:
			UserActivity prevSubStep = finishedSteps.get( 0 );
			if( prevSubStep.getProducedValue() != null ) {
				File file = (File)prevSubStep.getProducedValue();
				try {
					TypeResourcesPair typeResourcesPair = IoUtilities.readType( file );
					NamedUserType importedType = typeResourcesPair.getType();
					Set<Resource> importedResources = typeResourcesPair.getResources();
					NamedUserType srcType;
					if( importedType.getName().contentEquals( this.dstType.getName() ) ) {
						srcType = importedType;
					} else {
						srcType = null;
						IsInstanceCrawler<NamedUserType> crawler = IsInstanceCrawler.createInstance( NamedUserType.class );
						importedType.crawl( crawler, CrawlPolicy.COMPLETE );
						for( NamedUserType type : crawler.getList() ) {
							if( type.getName().contentEquals( this.dstType.getName() ) ) {
								srcType = type;
								break;
							}
						}
					}
					if( srcType != null ) {
						return new ImportTypeWizard( file.toURI(), importedType, importedResources, srcType, this.dstType ).getLaunchOperation();
					} else {
						new OkDialog.Builder( "Cannot find class " + this.dstType.getName() + " in " + file ).buildAndShow();
						return null;
					}
				} catch( IOException ioe ) {
					new OkDialog.Builder( "Unable to read " + file.getName() ).buildAndShow();
				} catch( VersionNotSupportedException vnse ) {
					new OkDialog.Builder( "version not supported " + vnse.getVersion() ).buildAndShow();
				}
				return null;
			} else {
				return null;
			}
		default:
			return null;
		}
	}
}
