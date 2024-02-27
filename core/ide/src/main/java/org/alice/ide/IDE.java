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

import edu.cmu.cs.dennisc.crash.CrashDetector;
import edu.cmu.cs.dennisc.java.lang.ClassUtilities;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.java.util.Sets;
import edu.cmu.cs.dennisc.javax.swing.option.Dialogs;
import edu.cmu.cs.dennisc.pattern.Crawler;
import edu.cmu.cs.dennisc.pattern.Criterion;
import edu.cmu.cs.dennisc.pattern.IsInstanceCrawler;
import org.alice.ide.cascade.ExpressionCascadeManager;
import org.alice.ide.croquet.models.projecturi.ClearanceCheckingExitOperation;
import org.alice.ide.croquet.models.projecturi.OpenProjectFromOsOperation;
import org.alice.ide.croquet.models.ui.locale.LocaleState;
import org.alice.ide.issue.DefaultExceptionHandler;
import org.alice.ide.perspectives.ProjectPerspective;
import org.alice.ide.sceneeditor.AbstractSceneEditor;
import org.alice.ide.stencil.PotentialDropReceptorsFeedbackView;
import org.alice.ide.uricontent.FileProjectLoader;
import org.lgna.croquet.Application;
import org.lgna.croquet.Group;
import org.lgna.croquet.Operation;
import org.lgna.croquet.Perspective;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.preferences.PreferenceManager;
import org.lgna.croquet.triggers.WindowEventTrigger;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.DragComponent;
import org.lgna.project.ProgramTypeUtilities;
import org.lgna.project.Project;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractNode;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.Comment;
import org.lgna.project.ast.CrawlPolicy;
import org.lgna.project.ast.Declaration;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.Node;
import org.lgna.project.ast.ResourceExpression;
import org.lgna.project.ast.SimpleArgumentListProperty;
import org.lgna.project.ast.StatementListProperty;
import org.lgna.project.ast.TypeExpression;
import org.lgna.project.ast.UserCode;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.code.ProcessableNode;
import org.lgna.project.virtualmachine.ReleaseVirtualMachine;
import org.lgna.project.virtualmachine.VirtualMachine;

import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.prefs.BackingStoreException;

/**
 * @author Dennis Cosgrove
 */
public abstract class IDE extends ProjectApplication {
  public static final Group RUN_GROUP = Group.getInstance(UUID.fromString("f7a87645-567c-42c6-bf5f-ab218d93a226"), "RUN_GROUP");
  public static final Group EXPORT_GROUP = Group.getInstance(UUID.fromString("624d4db6-2e1a-43c2-b1df-c0bfd6407b35"), "EXPORT_GROUP");

  private static DefaultExceptionHandler exceptionHandler;

  static {
    IDE.exceptionHandler = new DefaultExceptionHandler();

    if (SystemUtilities.isPropertyTrue("org.alice.ide.IDE.isSupressionOfExceptionHandlerDesired")) {
      //pass
    } else {
      Thread.setDefaultUncaughtExceptionHandler(IDE.exceptionHandler);
    }
  }

  public static IDE getActiveInstance() {
    return ClassUtilities.getInstance(Application.getActiveInstance(), IDE.class);
  }

  private final ValueListener<ProjectPerspective> perspectiveListener = new ValueListener<ProjectPerspective>() {
    @Override
    public void valueChanged(ValueEvent<ProjectPerspective> e) {
      IDE.this.setPerspective(e.getNextValue());
    }
  };

  private PotentialDropReceptorsFeedbackView potentialDropReceptorsStencil;

  private File projectFileToLoadOnWindowOpened;

  private final IdeConfiguration ideConfiguration;
  private final CrashDetector crashDetector;

  public IDE(IdeConfiguration ideConfiguration, ApiConfigurationManager apiConfigurationManager, CrashDetector crashDetector) {
    super(ideConfiguration, apiConfigurationManager);
    this.ideConfiguration = ideConfiguration;
    this.crashDetector = crashDetector;
    //TODO I18n
    IDE.exceptionHandler.setTitle("Please Submit Bug Report: " + getApplicationName());
    IDE.exceptionHandler.setApplicationName(getApplicationName());
    //initialize locale
    //Checks the org.alice.ide.locale property to see if there is a specified locale to initialize Alice to
    //If so, it sets that locale and adds the locale listener to the locale state
    //If not, it adds and invokes the listener on the locale state which has been initialized based on saved preferences

    ValueListener<Locale> localeListener = new ValueListener<Locale>() {
      @Override
      public void valueChanged(ValueEvent<Locale> e) {
        setLocale(e.getNextValue());
      }
    };
    String forcedLocaleString = System.getProperty("org.alice.ide.locale");
    Locale forcedLocale = null;
    if (forcedLocaleString != null) {
      forcedLocale = new Locale(forcedLocaleString);
    }
    if (forcedLocale != null) {
      Application.getActiveInstance().setLocale(forcedLocale);
      LocaleState.getInstance().addNewSchoolValueListener(localeListener);

    } else {
      LocaleState.getInstance().addAndInvokeNewSchoolValueListener(localeListener);
    }

  }

  public IdeConfiguration getIdeConfiguration() {
    return this.ideConfiguration;
  }

  public final ApiConfigurationManager getApiConfigurationManager() {
    return this.getDocumentFrame().getApiConfigurationManager();
  }

  @Override
  public void initialize(String[] args) {
    super.initialize(args);
    ProjectDocumentFrame documentFrame = this.getDocumentFrame();
    documentFrame.getPerspectiveState().addNewSchoolValueListener(this.perspectiveListener);
    documentFrame.initialize();
  }

  public abstract AbstractSceneEditor getSceneEditor();

  private Theme theme;

  protected Theme createTheme() {
    return new DefaultTheme();
  }

  public final Theme getTheme() {
    if (this.theme == null) {
      this.theme = this.createTheme();
    }
    return this.theme;
  }

  @Override
  public Operation getPreferencesOperation() {
    return null;
  }

  public enum AccessorAndMutatorDisplayStyle {
    GETTER_AND_SETTER, ACCESS_AND_ASSIGNMENT
  }

  public AccessorAndMutatorDisplayStyle getAccessorAndMutatorDisplayStyle(AbstractField field) {
    if (field != null) {
      AbstractType<?, ?, ?> declaringType = field.getDeclaringType();
      if ((declaringType != null) && declaringType.isUserAuthored()) {
        return AccessorAndMutatorDisplayStyle.ACCESS_AND_ASSIGNMENT;
      } else {
        //return AccessorAndMutatorDisplayStyle.GETTER_AND_SETTER;
        return AccessorAndMutatorDisplayStyle.ACCESS_AND_ASSIGNMENT;
      }
    } else {
      return AccessorAndMutatorDisplayStyle.ACCESS_AND_ASSIGNMENT;
    }
  }

  public abstract UserMethod getPerformEditorGeneratedSetUpMethod();

  protected abstract Criterion<Declaration> getDeclarationFilter();

  public void crawlFilteredProgramType(Crawler crawler) {
    NamedUserType programType = this.getProgramType();
    if (programType != null) {
      programType.crawl(crawler, CrawlPolicy.COMPLETE, this.getDeclarationFilter());
    }
  }

  private static class UnacceptableFieldAccessCrawler extends IsInstanceCrawler<FieldAccess> {
    private final Set<UserField> unacceptableFields;

    public UnacceptableFieldAccessCrawler(Set<UserField> unacceptableFields) {
      super(FieldAccess.class);
      this.unacceptableFields = unacceptableFields;
    }

    @Override
    protected boolean isAcceptable(FieldAccess fieldAccess) {
      return this.unacceptableFields.contains(fieldAccess.field.getValue());
    }
  }

  private String reorganizeTypeFieldsIfNecessary(NamedUserType namedUserType, int startIndex, Set<UserField> alreadyMovedFields) {
    List<UserField> fields = namedUserType.fields.getValue().subList(startIndex, namedUserType.fields.size());
    Set<UserField> unacceptableFields = Sets.newHashSet(fields);
    UserField fieldToMoveToTheEnd = null;
    List<FieldAccess> accessesForFieldToMoveToTheEnd = null;
    for (UserField field : fields) {
      Expression initializer = field.initializer.getValue();
      UnacceptableFieldAccessCrawler crawler = new UnacceptableFieldAccessCrawler(unacceptableFields);
      initializer.crawl(crawler, CrawlPolicy.EXCLUDE_REFERENCES_ENTIRELY);
      List<FieldAccess> fieldAccesses = crawler.getList();
      if (fieldAccesses.size() > 0) {
        fieldToMoveToTheEnd = field;
        accessesForFieldToMoveToTheEnd = fieldAccesses;
        break;
      }
      unacceptableFields.remove(field);
    }
    if (fieldToMoveToTheEnd != null) {
      if (alreadyMovedFields.contains(fieldToMoveToTheEnd)) {
        //todo: better cycle detection?
        StringBuilder sb = new StringBuilder();
        // TODO I18n
        sb.append("<html>Possible cycle detected.<br>The field <strong>\"");
        sb.append(fieldToMoveToTheEnd.getName());
        sb.append("\"</strong> on type <strong>\"");
        sb.append(fieldToMoveToTheEnd.getDeclaringType().getName());
        sb.append("\"</strong> is referencing: ");
        String prefix = "<strong>\"";
        for (FieldAccess fieldAccess : accessesForFieldToMoveToTheEnd) {
          AbstractField accessedField = fieldAccess.field.getValue();
          sb.append(prefix);
          sb.append(accessedField.getName());
          prefix = "\"</strong>, <strong>\"";
        }
        sb.append("\"</strong><br>");
        sb.append(getApplicationName());
        sb.append(" already attempted to move it once.");
        sb.append("<br><br><strong>Your program may fail.</strong></html>");
        return sb.toString();
      } else {
        for (FieldAccess fieldAccess : accessesForFieldToMoveToTheEnd) {
          AbstractField accessedField = fieldAccess.field.getValue();
          if (accessedField == fieldToMoveToTheEnd) {
            StringBuilder sb = new StringBuilder();
            // TODO I18n
            sb.append("<html>The field <strong>\"");
            sb.append(fieldToMoveToTheEnd.getName());
            sb.append("\"</strong> on type <strong>\"");
            sb.append(fieldToMoveToTheEnd.getDeclaringType().getName());
            sb.append("\"</strong> is referencing <strong>itself</strong>.");
            sb.append("<br><br><strong>Your program may fail.</strong></html>");
            return sb.toString();
          }
        }
        int prevIndex = namedUserType.fields.indexOf(fieldToMoveToTheEnd);
        int nextIndex = namedUserType.fields.size() - 1;
        namedUserType.fields.slide(prevIndex, nextIndex);
        alreadyMovedFields.add(fieldToMoveToTheEnd);
        return this.reorganizeTypeFieldsIfNecessary(namedUserType, prevIndex, alreadyMovedFields);
      }
    } else {
      return null;
    }
  }

  private void reorganizeFieldsIfNecessary() {
    Project project = this.getProject();
    if (project != null) {
      for (NamedUserType namedUserType : project.getNamedUserTypes()) {
        Set<UserField> alreadyMovedFields = Sets.newHashSet();
        String message = this.reorganizeTypeFieldsIfNecessary(namedUserType, 0, alreadyMovedFields);
        if (message != null) {
          //TODO I18n
          Dialogs.showError("Unable to Recover", message);
        }
      }
    }
  }

  @Override
  public void ensureProjectCodeUpToDate() {
    Project project = this.getProject();
    if (project != null) {
      if (!isProjectUpToDateWithSceneSetUp()) {
        updateProject(project);
      }
    }
  }

  @Override
  public void forceProjectCodeUpToDate() {
    Project project = getProject();
    if (project != null) {
      updateProject(project);
    }
  }

  private void updateProject(Project project) {
    synchronized (project.getLock()) {
      generateCodeForSceneSetUp();
      reorganizeFieldsIfNecessary();
      updateHistoryIndexSceneSetUpSync();
    }
  }

  public NamedUserType getUpToDateProgramType() {
    Project project = this.getUpToDateProject();
    if (project != null) {
      return project.getProgramType();
    } else {
      return null;
    }
  }

  public List<FieldAccess> getFieldAccesses(final AbstractField field) {
    return ProgramTypeUtilities.getFieldAccesses(this.getProgramType(), field, this.getDeclarationFilter());
  }

  public List<MethodInvocation> getMethodInvocations(final AbstractMethod method) {
    return ProgramTypeUtilities.getMethodInvocations(this.getProgramType(), method, this.getDeclarationFilter());
  }

  public List<SimpleArgumentListProperty> getArgumentLists(final UserCode code) {
    return ProgramTypeUtilities.getArgumentLists(this.getProgramType(), code, this.getDeclarationFilter());
  }

  public boolean isDropDownDesiredFor(Expression expression) {
    if (AstUtilities.isKeywordExpression(expression)) {
      return false;
    }
    return ((expression instanceof TypeExpression) || (expression instanceof ResourceExpression)) == false;
  }

  public abstract ExpressionCascadeManager getExpressionCascadeManager();

  public PotentialDropReceptorsFeedbackView getPotentialDropReceptorsFeedbackView() {
    if (this.potentialDropReceptorsStencil == null) {
      this.potentialDropReceptorsStencil = new PotentialDropReceptorsFeedbackView(this.getDocumentFrame().getFrame());
    }
    return this.potentialDropReceptorsStencil;
  }

  public void showDropReceptorsStencilOver(DragComponent potentialDragSource, final AbstractType<?, ?, ?> type) {
    this.getPotentialDropReceptorsFeedbackView().showStencilOver(potentialDragSource, type);
  }

  public void hideDropReceptorsStencil() {
    this.getPotentialDropReceptorsFeedbackView().hideStencil();
  }

  @Override
  public void setProject(Project project) {
    boolean isScenePerspectiveDesiredByDefault = SystemUtilities.getBooleanProperty("org.alice.ide.IDE.isScenePerspectiveDesiredByDefault", false);
    ProjectDocumentFrame documentFrame = this.getDocumentFrame();
    ProjectPerspective defaultPerspective = isScenePerspectiveDesiredByDefault ? documentFrame.getSetupScenePerspective() : documentFrame.getCodePerspective();
    documentFrame.getPerspectiveState().setValueTransactionlessly(defaultPerspective);
    super.setProject(project);
    Perspective perspective = this.getPerspective();
    if ((perspective == null) || (perspective == documentFrame.getNoProjectPerspective())) {
      this.setPerspective(documentFrame.getPerspectiveState().getValue());
    }
  }

  public <N extends AbstractNode & ProcessableNode> N createCopy(N original) {
    NamedUserType root = this.getProgramType();
    return AstUtilities.createCopy(original, root);
  }

  private Comment commentThatWantsFocus = null;

  public Comment getCommentThatWantsFocus() {
    return this.commentThatWantsFocus;
  }

  public void setCommentThatWantsFocus(Comment commentThatWantsFocus) {
    this.commentThatWantsFocus = commentThatWantsFocus;
  }

  protected abstract void promptForLicenseAgreements();

  public void setProjectFileToLoadOnWindowOpened(File projectFileToLoadOnWindowOpened) {
    this.projectFileToLoadOnWindowOpened = projectFileToLoadOnWindowOpened;
  }

  @Override
  protected void handleWindowOpened(WindowEvent e) {
    promptForLicenseAgreements();
    UserActivity activity = getOverallUserActivity().getLatestActivity().newChildActivity();
    if (projectFileToLoadOnWindowOpened != null) {
      this.loadProject(activity, new FileProjectLoader(projectFileToLoadOnWindowOpened, false));
      projectFileToLoadOnWindowOpened = null;
    }
    if (getUri() == null) {
      setPerspective(getDocumentFrame().getNoProjectPerspective());
      WindowEventTrigger.setOnUserActivity(activity, e);
      getDocumentFrame().getNewProjectOperation().fire(activity);
    }
  }

  @Override
  protected void handleOpenFiles(List<File> files) {
    if (files != null && !files.isEmpty()) {
      File file = files.get(0);
      if (file.exists()) {
        UserActivity activity = getOverallUserActivity().getLatestActivity().newChildActivity();
        new OpenProjectFromOsOperation(file).fire(activity);
      }
    }
  }

  protected void preservePreferences() {
    try {
      PreferenceManager.preservePreferences();
    } catch (BackingStoreException bse) {
      bse.printStackTrace();
    }
  }

  private final ClearanceCheckingExitOperation clearanceCheckingExitOperation = new ClearanceCheckingExitOperation();

  @Override
  public final void handleQuit(UserActivity activity) {
    this.preservePreferences();
    if (this.crashDetector != null) {
      this.crashDetector.close();
    }
    clearanceCheckingExitOperation.fire(activity);
  }

  protected VirtualMachine createVirtualMachineForSceneEditor() {
    return new ReleaseVirtualMachine();
  }

  protected abstract void registerAdaptersForSceneEditorVm(VirtualMachine vm);

  public final VirtualMachine createRegisteredVirtualMachineForSceneEditor() {
    VirtualMachine vm = this.createVirtualMachineForSceneEditor();
    this.registerAdaptersForSceneEditorVm(vm);
    return vm;
  }

  protected abstract String getInnerCommentForMethodName(String methodName);

  private void generateCodeForSceneSetUp() {
    UserMethod userMethod = this.getPerformEditorGeneratedSetUpMethod();
    StatementListProperty bodyStatementsProperty = userMethod.body.getValue().statements;
    bodyStatementsProperty.clear();
    String innerComment = getInnerCommentForMethodName(userMethod.getName());
    if (innerComment != null) {
      bodyStatementsProperty.add(new Comment(innerComment));
    }
    this.getSceneEditor().generateCodeForSetUp(bodyStatementsProperty);
  }

  public NamedUserType getProgramType() {
    Project project = this.getProject();
    if (project != null) {
      return project.getProgramType();
    } else {
      return null;
    }
  }

  protected static <E extends Node> E getAncestor(Node node, Class<E> cls) {
    Node ancestor = node.getParent();
    while (ancestor != null) {
      if (cls.isAssignableFrom(ancestor.getClass())) {
        break;
      } else {
        ancestor = ancestor.getParent();
      }
    }
    return (E) ancestor;
  }

  public AwtComponentView<?> getPrefixPaneForFieldAccessIfAppropriate(FieldAccess fieldAccess) {
    return null;
  }

  public AwtComponentView<?> getPrefixPaneForInstanceCreationIfAppropriate(InstanceCreation instanceCreation) {
    return null;
  }

  public abstract boolean isInstanceCreationAllowableFor(NamedUserType userType);
}
