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
package org.alice.ide;

/**
 * @author Dennis Cosgrove
 */
public enum IdeApp {
	INSTANCE;

	public org.lgna.croquet.BooleanState getMemoryUsageFrameIsShowingState() {
		return this.memoryUsageFrameIsShowingState;
	}

	public org.lgna.croquet.BooleanState getLocalizeReviewIsShowingState() {
		return this.localizeReviewIsShowingState;
	}

	public org.lgna.croquet.MenuModel getContributorMenuModel() {
		return this.contributorMenuModel;
	}

	public org.lgna.croquet.Operation getHelpDialogLaunchOperation() {
		return this.helpDialogLaunchOperation;
	}

	public org.lgna.croquet.Operation getGraphicsHelpDialogLaunchOperation() {
		return this.graphicsHelpDialogLaunchOperation;
	}

	public org.lgna.croquet.Operation getReportBugLaunchOperation() {
		return this.reportIssueComposite.getReportBugLaunchOperation();
	}

	public org.lgna.croquet.Operation getRequestNewFeatureLaunchOperation() {
		return this.reportIssueComposite.getRequestNewFeatureLaunchOperation();
	}

	public org.lgna.croquet.Operation getSuggestImprovementLaunchOperation() {
		return this.reportIssueComposite.getSuggestImprovementLaunchOperation();
	}

	public org.lgna.croquet.Operation getWarningDialogLaunchOperation() {
		return this.warningDialogLaunchOperation;
	}

	public org.alice.ide.croquet.models.help.ShowClassPathPropertyComposite getShowClassPathPropertyComposite() {
		return this.showClassPathPropertyComposite;
	}

	public org.alice.ide.croquet.models.help.ShowLibraryPathPropertyComposite getShowLibraryPathPropertyComposite() {
		return this.showLibraryPathPropertyComposite;
	}

	public org.alice.ide.croquet.models.help.ShowAllSystemPropertiesComposite getShowAllSystemPropertiesComposite() {
		return this.showAllSystemPropertiesComposite;
	}

	public org.lgna.croquet.Operation getShowSystemPropertiesDialogLaunchOperation() {
		return this.showSystemPropertiesDialogLaunchOperation;
	}

	public org.lgna.croquet.Operation getBrowseReleaseNotesOperation() {
		return this.browseReleaseNotesOperation;
	}

	public org.lgna.croquet.Operation getSystemEulaDialogLaunchOperation() {
		return this.systemEulaDialogLaunchOperation;
	}

	public org.lgna.croquet.Operation getSimsArtEulaDialogLaunchOperation() {
		return this.simsArtEulaDialogLaunchOperation;
	}

	public org.lgna.croquet.Operation getCreditsDialogLaunchOperation() {
		return this.creditsDialogLaunchOperation;
	}

	public org.lgna.croquet.Operation getAboutDialogLaunchOperation() {
		return this.aboutDialogLaunchOperation;
	}

	public org.alice.ide.croquet.models.menubar.HelpMenuModel getHelpMenu() {
		return this.helpMenu;
	}

	private final org.lgna.croquet.BooleanState memoryUsageFrameIsShowingState = org.lgna.croquet.imp.frame.LazyIsFrameShowingState.createInstance(
			org.lgna.croquet.Application.INFORMATION_GROUP,
			org.alice.ide.croquet.models.ui.MemoryUsageComposite.class,
			new edu.cmu.cs.dennisc.pattern.Lazy<org.alice.ide.croquet.models.ui.MemoryUsageComposite>() {
				@Override
				protected org.alice.ide.croquet.models.ui.MemoryUsageComposite create() {
					return new org.alice.ide.croquet.models.ui.MemoryUsageComposite();
				}
			} );

	private final org.lgna.croquet.BooleanState localizeReviewIsShowingState = org.lgna.croquet.imp.frame.LazyIsFrameShowingState.createInstance(
			org.lgna.croquet.Application.INFORMATION_GROUP,
			org.alice.ide.localize.review.croquet.LocalizeReviewFrame.class,
			new edu.cmu.cs.dennisc.pattern.Lazy<org.alice.ide.localize.review.croquet.LocalizeReviewFrame>() {
				@Override
				protected org.alice.ide.localize.review.croquet.LocalizeReviewFrame create() {
					return new org.alice.ide.localize.review.croquet.LocalizeReviewFrame();
				}
			} );

	private final org.lgna.croquet.MenuModel contributorMenuModel = new org.alice.ide.croquet.models.menubar.ContributorMenuModel( localizeReviewIsShowingState );

	private final org.lgna.croquet.Operation helpDialogLaunchOperation = org.lgna.croquet.imp.launch.LazySimpleLaunchOperationFactory.createInstance(
			org.alice.ide.help.HelpComposite.class,
			new edu.cmu.cs.dennisc.pattern.Lazy<org.alice.ide.help.HelpComposite>() {
				@Override
				protected org.alice.ide.help.HelpComposite create() {
					return new org.alice.ide.help.HelpComposite();
				}
			}, org.lgna.croquet.Application.INFORMATION_GROUP ).getLaunchOperation();

	private final org.lgna.croquet.Operation graphicsHelpDialogLaunchOperation = org.lgna.croquet.imp.launch.LazySimpleLaunchOperationFactory.createInstance(
			org.alice.ide.croquet.models.help.GraphicsHelpComposite.class,
			new edu.cmu.cs.dennisc.pattern.Lazy<org.alice.ide.croquet.models.help.GraphicsHelpComposite>() {
				@Override
				protected org.alice.ide.croquet.models.help.GraphicsHelpComposite create() {
					return new org.alice.ide.croquet.models.help.GraphicsHelpComposite();
				}
			}, org.lgna.croquet.Application.INFORMATION_GROUP ).getLaunchOperation();

	private final org.alice.ide.croquet.models.help.ReportIssueComposite reportIssueComposite = new org.alice.ide.croquet.models.help.ReportIssueComposite();

	private final org.lgna.croquet.Operation warningDialogLaunchOperation = org.lgna.croquet.imp.launch.LazySimpleLaunchOperationFactory.createInstance(
			org.alice.ide.warning.WarningDialogComposite.class,
			new edu.cmu.cs.dennisc.pattern.Lazy<org.alice.ide.warning.WarningDialogComposite>() {
				@Override
				protected org.alice.ide.warning.WarningDialogComposite create() {
					return new org.alice.ide.warning.WarningDialogComposite();
				}
			}, org.lgna.croquet.Application.INFORMATION_GROUP ).getLaunchOperation();

	private final org.alice.ide.croquet.models.help.ShowClassPathPropertyComposite showClassPathPropertyComposite = new org.alice.ide.croquet.models.help.ShowClassPathPropertyComposite();

	private final org.alice.ide.croquet.models.help.ShowLibraryPathPropertyComposite showLibraryPathPropertyComposite = new org.alice.ide.croquet.models.help.ShowLibraryPathPropertyComposite();

	private final org.alice.ide.croquet.models.help.ShowAllSystemPropertiesComposite showAllSystemPropertiesComposite = new org.alice.ide.croquet.models.help.ShowAllSystemPropertiesComposite();

	private final org.lgna.croquet.Operation showSystemPropertiesDialogLaunchOperation = org.lgna.croquet.imp.launch.LazySimpleLaunchOperationFactory.createInstance(
			org.alice.ide.croquet.models.help.ShowSystemPropertiesComposite.class,
			new edu.cmu.cs.dennisc.pattern.Lazy<org.alice.ide.croquet.models.help.ShowSystemPropertiesComposite>() {
				@Override
				protected org.alice.ide.croquet.models.help.ShowSystemPropertiesComposite create() {
					return new org.alice.ide.croquet.models.help.ShowSystemPropertiesComposite();
				}
			}, org.lgna.croquet.Application.INFORMATION_GROUP ).getLaunchOperation();

	private final org.alice.ide.croquet.models.help.BrowseReleaseNotesOperation browseReleaseNotesOperation = new org.alice.ide.croquet.models.help.BrowseReleaseNotesOperation();

	private final org.lgna.croquet.Operation systemEulaDialogLaunchOperation = org.lgna.croquet.imp.launch.LazySimpleLaunchOperationFactory.createInstance(
			org.alice.stageide.about.SystemEulaComposite.class,
			new edu.cmu.cs.dennisc.pattern.Lazy<org.alice.stageide.about.SystemEulaComposite>() {
				@Override
				protected org.alice.stageide.about.SystemEulaComposite create() {
					return new org.alice.stageide.about.SystemEulaComposite();
				}
			}, org.lgna.croquet.Application.INFORMATION_GROUP ).getLaunchOperation();

	private final org.lgna.croquet.Operation simsArtEulaDialogLaunchOperation = org.lgna.croquet.imp.launch.LazySimpleLaunchOperationFactory.createInstance(
			org.alice.stageide.about.SimsArtAssetsEulaComposite.class,
			new edu.cmu.cs.dennisc.pattern.Lazy<org.alice.stageide.about.SimsArtAssetsEulaComposite>() {
				@Override
				protected org.alice.stageide.about.SimsArtAssetsEulaComposite create() {
					return new org.alice.stageide.about.SimsArtAssetsEulaComposite();
				}
			}, org.lgna.croquet.Application.INFORMATION_GROUP ).getLaunchOperation();

	private final org.lgna.croquet.Operation creditsDialogLaunchOperation = org.lgna.croquet.imp.launch.LazySimpleLaunchOperationFactory.createInstance(
			org.alice.stageide.about.CreditsComposite.class,
			new edu.cmu.cs.dennisc.pattern.Lazy<org.alice.stageide.about.CreditsComposite>() {
				@Override
				protected org.alice.stageide.about.CreditsComposite create() {
					return new org.alice.stageide.about.CreditsComposite();
				}
			}, org.lgna.croquet.Application.INFORMATION_GROUP ).getLaunchOperation();

	private final org.lgna.croquet.Operation aboutDialogLaunchOperation = org.lgna.croquet.imp.launch.LazySimpleLaunchOperationFactory.createInstance(
			org.alice.stageide.about.AboutComposite.class,
			new edu.cmu.cs.dennisc.pattern.Lazy<org.alice.stageide.about.AboutComposite>() {
				@Override
				protected org.alice.stageide.about.AboutComposite create() {
					return new org.alice.stageide.about.AboutComposite();
				}
			}, org.lgna.croquet.Application.INFORMATION_GROUP ).getLaunchOperation();

	private final org.alice.ide.croquet.models.menubar.HelpMenuModel helpMenu = new org.alice.ide.croquet.models.menubar.HelpMenuModel( this );
}
