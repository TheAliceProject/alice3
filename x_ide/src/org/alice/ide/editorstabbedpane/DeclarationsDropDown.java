package org.alice.ide.editorstabbedpane;

//class EditFieldOperation extends org.alice.ide.operations.AbstractActionOperation {
//	private edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field;
//
//	public EditFieldOperation( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field ) {
//		this.field = field;
//		this.putValue( javax.swing.Action.NAME, "Edit " + this.field.getName() + "..." );
//	}
//	public void perform( zoot.ActionContext actionContext ) {
//	}
//}

abstract class EditMembersOperation< E extends edu.cmu.cs.dennisc.alice.ast.MemberDeclaredInAlice > extends org.alice.ide.operations.AbstractActionOperation {
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType;
	private String presentation;
	public EditMembersOperation( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, String presentation ) {
		super( edu.cmu.cs.dennisc.alice.Project.GROUP_UUID );
		this.declaringType = declaringType;
		this.presentation = presentation;
		this.putValue( javax.swing.Action.NAME, this.presentation + "..." );
	}
	protected abstract EditMembersPane< E > createEditMembersPane( java.util.UUID groupUUID, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType );
	public final void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		java.util.UUID groupUUID = java.util.UUID.randomUUID();
		edu.cmu.cs.dennisc.history.HistoryManager historyManager = edu.cmu.cs.dennisc.history.HistoryManager.getInstance( groupUUID );
		EditMembersPane< E > editMembersPane = this.createEditMembersPane( groupUUID, this.declaringType );
		Boolean isAccepted = editMembersPane.showInJDialog( getIDE() );
		if( isAccepted != null ) {
			edu.cmu.cs.dennisc.zoot.CompositeEdit compositeEdit = historyManager.createDoIgnoringCompositeEdit( this.presentation + " " + this.declaringType.getName() );
			if( compositeEdit != null ) {
				actionContext.commitAndInvokeDo( compositeEdit );
			} else {
				actionContext.cancel();
			}
		} else {
			historyManager.setInsertionIndex( 0 );
			actionContext.cancel();
		}
	}
}
abstract class EditMethodsOperation< E extends edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice > extends EditMembersOperation< E > {
	public EditMethodsOperation( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type, String presentation ) {
		super( type, presentation );
	}
}
class EditProceduresOperation extends EditMethodsOperation< edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice > {
	public EditProceduresOperation( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( type, "Edit Procedures" );
	}
	@Override
	protected EditProceduresPane createEditMembersPane( java.util.UUID groupUUID, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
		return new EditProceduresPane( declaringType );
	}
}
class EditFunctionsOperation extends EditMethodsOperation< edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice > {
	public EditFunctionsOperation( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( type, "Edit Functions" );
	}
	@Override
	protected EditFunctionsPane createEditMembersPane( java.util.UUID groupUUID, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
		return new EditFunctionsPane( declaringType );
	}
}

class EditFieldsOperation extends EditMembersOperation< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > {
	public EditFieldsOperation( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( type, "Edit Properties" );
	}
	@Override
	protected EditFieldsPane createEditMembersPane( java.util.UUID groupUUID, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
		return new EditFieldsPane( declaringType );
	}
}

abstract class MembersFillIn extends edu.cmu.cs.dennisc.cascade.MenuFillIn< edu.cmu.cs.dennisc.zoot.ActionOperation > {
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type;

	public MembersFillIn( String text, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( text );
		this.type = type;
	}
	protected abstract int getMemberCount();
	@Override
	protected String getLabelText() {
		return super.getLabelText() + " (" + this.getMemberCount() + ")";
	}
	protected edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getType() {
		return this.type;
	}
}

abstract class MethodsFillIn extends MembersFillIn {
	public MethodsFillIn( String text, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( text, type );
	}
	protected abstract boolean isDesired( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method );
	@Override
	public int getMemberCount() {
		int rv = 0;
		for( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method : this.getType().methods ) {
			if( this.isDesired( method ) ) {
				rv++;
			}
		}
		return rv;
	}
	@Override
	protected void addChildrenToBlank( edu.cmu.cs.dennisc.cascade.Blank blank ) {
		for( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method : this.getType().methods ) {
			if( this.isDesired( method ) ) {
				blank.addFillIn( new OperatorFillIn( new EditMethodOperation( method ) ) );
			}
		}
		blank.addSeparator();
	}
}

class ProceduresFillIn extends MethodsFillIn {
	public ProceduresFillIn( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( "Procedures", type );
	}
	@Override
	protected boolean isDesired( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method ) {
		return method.isProcedure();
	}
	@Override
	protected void addChildrenToBlank( edu.cmu.cs.dennisc.cascade.Blank blank ) {
		super.addChildrenToBlank( blank );
		blank.addFillIn( new OperatorFillIn( new org.alice.ide.operations.ast.DeclareProcedureOperation( this.getType() ) ) );
		blank.addSeparator();
		blank.addFillIn( new OperatorFillIn( new EditProceduresOperation( this.getType() ) ) );
	}
}

class FunctionsFillIn extends MethodsFillIn {
	public FunctionsFillIn( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( "Functions", type );
	}
	@Override
	protected boolean isDesired( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method ) {
		return method.isFunction();
	}
	@Override
	protected void addChildrenToBlank( edu.cmu.cs.dennisc.cascade.Blank blank ) {
		super.addChildrenToBlank( blank );
		blank.addFillIn( new OperatorFillIn( new org.alice.ide.operations.ast.DeclareFunctionOperation( this.getType() ) ) );
		blank.addSeparator();
		blank.addFillIn( new OperatorFillIn( new EditFunctionsOperation( this.getType() ) ) );
	}
}

class FieldsFillIn extends MembersFillIn {
	public FieldsFillIn( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( "Properties", type );
	}
	@Override
	public int getMemberCount() {
		return this.getType().fields.size();
	}
	@Override
	protected void addChildrenToBlank( edu.cmu.cs.dennisc.cascade.Blank blank ) {
		for( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field : this.getType().fields ) {
			blank.addFillIn( new OperatorFillIn( new org.alice.ide.operations.ast.EditFieldOperation( field ) ) );
		}
		blank.addSeparator();
		blank.addFillIn( new OperatorFillIn( new org.alice.ide.operations.ast.DeclareFieldOperation( this.getType() ) ) );
		blank.addSeparator();
		blank.addFillIn( new OperatorFillIn( new EditFieldsOperation( this.getType() ) ) );
	}
}

class TypeFillIn extends edu.cmu.cs.dennisc.cascade.MenuFillIn< edu.cmu.cs.dennisc.zoot.ActionOperation > {
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type;

	public TypeFillIn( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( type.getName() );
		this.type = type;
	}
	@Override
	protected javax.swing.JComponent createMenuProxy() {
		return new org.alice.ide.common.TypeComponent( this.type );
	}
	@Override
	protected void addChildrenToBlank( edu.cmu.cs.dennisc.cascade.Blank blank ) {
		if( this.type != null ) {
			blank.addFillIn( new ProceduresFillIn( this.type ) );
			blank.addFillIn( new FunctionsFillIn( this.type ) );
			blank.addFillIn( new OperatorFillIn( new EditConstructorOperation( (edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice)this.type.getDeclaredConstructor() ) ) );
			blank.addSeparator();
			blank.addFillIn( new FieldsFillIn( this.type ) );
			blank.addSeparator();
			blank.addFillIn( new OperatorFillIn( new org.alice.ide.operations.ast.RenameTypeOperation( this.type ) ) );
			blank.addFillIn( new OperatorFillIn( new org.alice.ide.operations.file.SaveAsTypeOperation( this.type ) ) );
		} else {
			blank.addFillIn( new edu.cmu.cs.dennisc.cascade.CancelFillIn( "type is not set.  canceling." ) );
		}
	}
}

class ProjectBlank extends edu.cmu.cs.dennisc.cascade.Blank {
	private edu.cmu.cs.dennisc.alice.Project project;

	public ProjectBlank( edu.cmu.cs.dennisc.alice.Project project ) {
		assert project != null;
		this.project = project;
	}
	@Override
	protected void addChildren() {
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice >( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice.class );
		final edu.cmu.cs.dennisc.alice.ast.AbstractType programType = this.project.getProgramType();
		programType.crawl( crawler, true );
		for( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type : crawler.getList() ) {
			if( type == programType ) {
				//pass
			} else {
				this.addFillIn( new TypeFillIn( type ) );
			}
		}
	}
}

class RootOperation extends org.alice.ide.operations.InconsequentialActionOperation {
	public RootOperation() {
		this.putValue( javax.swing.Action.NAME, "All" );
	}
	@Override
	protected void performInternal(edu.cmu.cs.dennisc.zoot.ActionContext actionContext) {
		java.awt.Component component = (java.awt.Component)actionContext.getEvent().getSource();
		int x = 0;//component.getWidth();
		int y = component.getHeight();
		edu.cmu.cs.dennisc.alice.Project project = getIDE().getProject();
		if( project != null ) {
			new ProjectBlank( project ).showPopupMenu( component, x, y, new edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.zoot.ActionOperation >() {
				public void handleCompletion( edu.cmu.cs.dennisc.zoot.ActionOperation actionOperation ) {
					edu.cmu.cs.dennisc.zoot.ZManager.performIfAppropriate( actionOperation, null, edu.cmu.cs.dennisc.zoot.ZManager.CANCEL_IS_WORTHWHILE );
				}
				public void handleCancelation() {
				}
			} );
		} else {
			javax.swing.JOptionPane.showMessageDialog( this.getIDE(), "Open a project first (via the File Menu)", "No Project", javax.swing.JOptionPane.INFORMATION_MESSAGE );
		}
	}
}

//class TypeOperation extends org.alice.ide.operations.AbstractActionOperation {
//	public void perform( zoot.ActionContext actionContext ) {
//		java.awt.Component component = (java.awt.Component)actionContext.getEvent().getSource();
//		int x = component.getWidth();
//		int y = component.getHeight();
//		
//		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type;
//		edu.cmu.cs.dennisc.alice.ast.AbstractCode focusedCode = getIDE().getFocusedCode();
//		if( focusedCode != null ) {
//			type = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)focusedCode.getDeclaringType();
//		} else {
//			type = null;
//		}
//		
//		new TypeFillIn( type ).showPopupMenu( component, x, y, new edu.cmu.cs.dennisc.task.TaskObserver< zoot.ActionOperation >() {
//			public void handleCompletion( zoot.ActionOperation actionOperation ) {
//				zoot.ZManager.performIfAppropriate( actionOperation, null, zoot.ZManager.CANCEL_IS_WORTHWHILE );
//			}
//			public void handleCancelation() {
//			}
//		} );
//	}
//}

class OperationDropDown extends org.alice.ide.common.AbstractDropDownPane {
	private javax.swing.JLabel label = edu.cmu.cs.dennisc.croquet.LabelUtilities.createLabel();

	public OperationDropDown( edu.cmu.cs.dennisc.zoot.ActionOperation leftButtonPressOperation ) {
		this.setLeftButtonPressOperation( leftButtonPressOperation );
		this.add( this.label );
		this.updateLabel();
		leftButtonPressOperation.getActionForConfiguringSwing().addPropertyChangeListener( new java.beans.PropertyChangeListener() {
			public void propertyChange( java.beans.PropertyChangeEvent e ) {
				if( javax.swing.Action.NAME.equals( e.getPropertyName() ) || javax.swing.Action.SMALL_ICON.equals( e.getPropertyName() ) ) {
					OperationDropDown.this.updateLabel();
				}
			}
		} );
		//this.setForeground( java.awt.Color.DARK_GRAY );
		this.label.setHorizontalTextPosition( javax.swing.SwingConstants.LEADING );
	}
	
	private static final java.awt.Color TOP_COLOR = new java.awt.Color( 255, 255, 191 );
	private static final java.awt.Color BOTTOM_COLOR = new java.awt.Color( 191, 191, 160 );
	@Override
	protected java.awt.Paint getBackgroundPaint( int x, int y, int width, int height ) {
		return new java.awt.GradientPaint( 0, 0, TOP_COLOR, 0, height, BOTTOM_COLOR );
	}

	protected void updateLabel() {
		javax.swing.Action action = this.getLeftButtonPressOperation().getActionForConfiguringSwing();
		this.label.setText( "class:" );
		this.label.setIcon( (javax.swing.Icon)action.getValue( javax.swing.Action.SMALL_ICON ) );
	}
	@Override
	protected boolean isInactiveFeedbackDesired() {
		return true;
	}
}

public class DeclarationsDropDown extends OperationDropDown {
	private org.alice.ide.event.IDEListener ideAdapter = new org.alice.ide.event.IDEListener() {
		public void fieldSelectionChanging( org.alice.ide.event.FieldSelectionEvent e ) {
		}
		public void fieldSelectionChanged( org.alice.ide.event.FieldSelectionEvent e ) {
		}
		public void focusedCodeChanging( org.alice.ide.event.FocusedCodeChangeEvent e ) {
		}
		public void focusedCodeChanged( org.alice.ide.event.FocusedCodeChangeEvent e ) {
			DeclarationsDropDown.this.updateOperation( e.getNextValue() );
		}
		public void localeChanging( org.alice.ide.event.LocaleEvent e ) {
		}
		public void localeChanged( org.alice.ide.event.LocaleEvent e ) {
		}
		public void projectOpening( org.alice.ide.event.ProjectOpenEvent e ) {
		}
		public void projectOpened( org.alice.ide.event.ProjectOpenEvent e ) {
		}
		public void transientSelectionChanging( org.alice.ide.event.TransientSelectionEvent e ) {
		}
		public void transientSelectionChanged( org.alice.ide.event.TransientSelectionEvent e ) {
		}
	};
	public DeclarationsDropDown() {
		super( new RootOperation() );
	}
	@Override
	public void addNotify() {
		super.addNotify();
		this.updateOperation( org.alice.ide.IDE.getSingleton().getFocusedCode() );
		getIDE().addIDEListener( this.ideAdapter );
	}
	@Override
	public void removeNotify() {
		getIDE().removeIDEListener( this.ideAdapter );
		super.removeNotify();
	}
	@Override
	protected int getInsetTop() {
		return 0;
	}
	@Override
	protected int getInsetBottom() {
		return 0;
	}
	private void updateOperation( edu.cmu.cs.dennisc.alice.ast.AbstractCode code ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractType type;
//		StringBuffer sb = new StringBuffer();
//		sb.append( "class: " );
		if( code != null ) {
//			sb.append( code.getDeclaringType().getName() );
			type = code.getDeclaringType();
		} else {
//			sb.append( "<unset>" );
			type = null;
		}
//		this.rootOperationDropDown.getLeftButtonPressOperation().getActionForConfiguringSwing().putValue( javax.swing.Action.NAME, sb.toString() );
		this.getLeftButtonPressOperation().getActionForConfiguringSwing().putValue( javax.swing.Action.SMALL_ICON, new org.alice.ide.common.TypeIcon( type ) );
		this.revalidate();
		this.repaint();
	}
}
