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
package org.alice.ide;

import org.alice.ide.croquet.models.help.BrowseReleaseNotesOperation;
import org.alice.ide.croquet.models.help.GraphicsHelpComposite;
import org.alice.ide.croquet.models.help.ReportIssueComposite;
import org.alice.ide.croquet.models.help.ShowAllSystemPropertiesComposite;
import org.alice.ide.croquet.models.help.ShowClassPathPropertyComposite;
import org.alice.ide.croquet.models.help.ShowLibraryPathPropertyComposite;
import org.alice.ide.croquet.models.help.ShowSystemPropertiesComposite;
import org.alice.ide.croquet.models.menubar.ContributorMenuModel;
import org.alice.ide.croquet.models.menubar.HelpMenuModel;
import org.alice.ide.croquet.models.ui.MemoryUsageComposite;
import org.alice.ide.help.HelpComposite;
import org.alice.ide.localize.review.croquet.LocalizeReviewFrame;
import org.alice.ide.preferences.recursion.IsRecursionAllowedPreferenceDialogComposite;
import org.alice.ide.warning.WarningDialogComposite;
import org.alice.nonfree.NebulousIde;
import org.alice.stageide.about.AboutComposite;
import org.alice.stageide.about.CreditsComposite;
import org.alice.stageide.about.SystemEulaComposite;
import org.lgna.croquet.Application;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.MenuModel;
import org.lgna.croquet.Operation;
import org.lgna.croquet.imp.frame.LazyIsFrameShowingState;
import org.lgna.croquet.imp.launch.LazySimpleLaunchOperationFactory;

/**
 * @author Dennis Cosgrove
 */
public enum IdeApp {
  INSTANCE;

  public BooleanState getMemoryUsageFrameIsShowingState() {
    return this.memoryUsageFrameIsShowingState;
  }

  public BooleanState getLocalizeReviewIsShowingState() {
    return this.localizeReviewIsShowingState;
  }

  public Operation getIsRecursionAllowedPreferenceDialogLaunchOperation() {
    return this.isRecursionAllowedPreferenceDialogLaunchOperation;
  }

  public MenuModel getContributorMenuModel() {
    return this.contributorMenuModel;
  }

  public Operation getHelpDialogLaunchOperation() {
    return this.helpDialogLaunchOperation;
  }

  public Operation getGraphicsHelpDialogLaunchOperation() {
    return this.graphicsHelpDialogLaunchOperation;
  }

  public Operation getReportBugLaunchOperation() {
    return this.reportIssueComposite.getReportBugLaunchOperation();
  }

  public Operation getRequestNewFeatureLaunchOperation() {
    return this.reportIssueComposite.getRequestNewFeatureLaunchOperation();
  }

  public Operation getSuggestImprovementLaunchOperation() {
    return this.reportIssueComposite.getSuggestImprovementLaunchOperation();
  }

  public Operation getWarningDialogLaunchOperation() {
    return this.warningDialogLaunchOperation;
  }

  public ShowClassPathPropertyComposite getShowClassPathPropertyComposite() {
    return this.showClassPathPropertyComposite;
  }

  public ShowLibraryPathPropertyComposite getShowLibraryPathPropertyComposite() {
    return this.showLibraryPathPropertyComposite;
  }

  public ShowAllSystemPropertiesComposite getShowAllSystemPropertiesComposite() {
    return this.showAllSystemPropertiesComposite;
  }

  public Operation getShowSystemPropertiesDialogLaunchOperation() {
    return this.showSystemPropertiesDialogLaunchOperation;
  }

  public Operation getBrowseReleaseNotesOperation() {
    return this.browseReleaseNotesOperation;
  }

  public Operation getSystemEulaDialogLaunchOperation() {
    return this.systemEulaDialogLaunchOperation;
  }

  public Operation getSimsArtEulaDialogLaunchOperation() {
    return this.simsArtEulaDialogLaunchOperation;
  }

  public Operation getCreditsDialogLaunchOperation() {
    return this.creditsDialogLaunchOperation;
  }

  public Operation getAboutDialogLaunchOperation() {
    return this.aboutDialogLaunchOperation;
  }

  public HelpMenuModel getHelpMenu() {
    return this.helpMenu;
  }

  private final BooleanState memoryUsageFrameIsShowingState = LazyIsFrameShowingState.createNoArgumentConstructorInstance(Application.INFORMATION_GROUP, MemoryUsageComposite.class);

  private final BooleanState localizeReviewIsShowingState = LazyIsFrameShowingState.createNoArgumentConstructorInstance(Application.INFORMATION_GROUP, LocalizeReviewFrame.class);

  private final Operation isRecursionAllowedPreferenceDialogLaunchOperation = LazySimpleLaunchOperationFactory.createNoArgumentConstructorInstance(IsRecursionAllowedPreferenceDialogComposite.class, Application.APPLICATION_UI_GROUP).getLaunchOperation();

  private final MenuModel contributorMenuModel = new ContributorMenuModel(localizeReviewIsShowingState);

  private final Operation helpDialogLaunchOperation = LazySimpleLaunchOperationFactory.createNoArgumentConstructorInstance(HelpComposite.class, Application.INFORMATION_GROUP).getLaunchOperation();

  private final Operation graphicsHelpDialogLaunchOperation = LazySimpleLaunchOperationFactory.createNoArgumentConstructorInstance(GraphicsHelpComposite.class, Application.INFORMATION_GROUP).getLaunchOperation();

  private final ReportIssueComposite reportIssueComposite = new ReportIssueComposite();

  private final Operation warningDialogLaunchOperation = LazySimpleLaunchOperationFactory.createNoArgumentConstructorInstance(WarningDialogComposite.class, Application.INFORMATION_GROUP).getLaunchOperation();

  private final ShowClassPathPropertyComposite showClassPathPropertyComposite = new ShowClassPathPropertyComposite();

  private final ShowLibraryPathPropertyComposite showLibraryPathPropertyComposite = new ShowLibraryPathPropertyComposite();

  private final ShowAllSystemPropertiesComposite showAllSystemPropertiesComposite = new ShowAllSystemPropertiesComposite();

  private final Operation showSystemPropertiesDialogLaunchOperation = LazySimpleLaunchOperationFactory.createNoArgumentConstructorInstance(ShowSystemPropertiesComposite.class, Application.INFORMATION_GROUP).getLaunchOperation();

  private final BrowseReleaseNotesOperation browseReleaseNotesOperation = new BrowseReleaseNotesOperation();

  private final Operation systemEulaDialogLaunchOperation = LazySimpleLaunchOperationFactory.createNoArgumentConstructorInstance(SystemEulaComposite.class, Application.INFORMATION_GROUP).getLaunchOperation();

  private final Operation simsArtEulaDialogLaunchOperation = NebulousIde.nonfree.newSimsArtEulaDialogLaunchOperation();

  private final Operation creditsDialogLaunchOperation = LazySimpleLaunchOperationFactory.createNoArgumentConstructorInstance(CreditsComposite.class, Application.INFORMATION_GROUP).getLaunchOperation();

  private final Operation aboutDialogLaunchOperation = LazySimpleLaunchOperationFactory.createNoArgumentConstructorInstance(AboutComposite.class, Application.INFORMATION_GROUP).getLaunchOperation();

  private final HelpMenuModel helpMenu = new HelpMenuModel(this);
}
