package org.alice.ide.declarationseditor;
//class MemberFillIn extends cascade.SimpleFillIn<edu.cmu.cs.dennisc.alice.ast.AbstractMember> {
//	public MemberFillIn( edu.cmu.cs.dennisc.alice.ast.AbstractMember member ) {
//		super( member );
//	}
//	@Override
//	protected String getMenuProxyText() {
//		return this.getModel().getName();
//	}
//}

class OperatorFillIn extends cascade.SimpleFillIn< zoot.ActionOperation > {
//	private zoot.ActionOperation actionOperation;
	public OperatorFillIn( zoot.ActionOperation actionOperation ) {
		super( actionOperation );
//		this.actionOperation = actionOperation;
	}
	@Override
	protected String getMenuProxyText() {
		return (String)this.getModel().getActionForConfiguringSwing().getValue( javax.swing.Action.NAME );
	}
//	@Override
//	protected void addChildren() {
//	}
//	@Override
//	public zoot.ActionOperation getValue() {
//		return this.actionOperation;
//	}
}

class EditMethodOperation extends org.alice.ide.operations.AbstractActionOperation {
	private edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method;
	public EditMethodOperation( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method ) {
		this.method = method;
		this.putValue( javax.swing.Action.NAME, "Edit " + this.method.getName() );
	}
	public void perform( zoot.ActionContext actionContext ) {
		this.getIDE().setFocusedCode( this.method );
	}
}
class EditFieldOperation extends org.alice.ide.operations.AbstractActionOperation {
	private edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field;
	public EditFieldOperation( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field ) {
		this.field = field;
		this.putValue( javax.swing.Action.NAME, "Edit " + this.field.getName() + "..." );
	}
	public void perform( zoot.ActionContext actionContext ) {
	}
}

abstract class MembersFillIn extends cascade.MenuFillIn< zoot.ActionOperation > {
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type;
	public MembersFillIn( String text, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( text );
		this.type = type;
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
	protected void addChildrenToBlank( cascade.Blank blank ) {
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
		super( "procedures", type );
	}
	@Override
	protected boolean isDesired( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method ) {
		return method.isProcedure();
	}
	@Override
	protected void addChildrenToBlank( cascade.Blank blank ) {
		super.addChildrenToBlank( blank );
		blank.addFillIn( new OperatorFillIn( new org.alice.ide.operations.ast.DeclareProcedureOperation( this.getType() ) ) );
	}
}
class FunctionsFillIn extends MethodsFillIn {
	public FunctionsFillIn( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( "functions", type );
	}
	@Override
	protected boolean isDesired( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method ) {
		return method.isFunction();
	}
	@Override
	protected void addChildrenToBlank( cascade.Blank blank ) {
		super.addChildrenToBlank( blank );
		blank.addFillIn( new OperatorFillIn( new org.alice.ide.operations.ast.DeclareFunctionOperation( this.getType() ) ) );
	}
}

class FieldsFillIn extends MembersFillIn {
	public FieldsFillIn( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( "properties", type );
	}
	@Override
	protected void addChildrenToBlank( cascade.Blank blank ) {
		for( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field : this.getType().fields ) {
			blank.addFillIn( new OperatorFillIn( new EditFieldOperation( field ) ) );
		}
		blank.addSeparator();
		blank.addFillIn( new OperatorFillIn( new org.alice.ide.operations.ast.DeclareFieldOperation( this.getType() ) ) );
	}
}

class TypeFillIn extends cascade.MenuFillIn< zoot.ActionOperation > {
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type;
	public TypeFillIn( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		super( type.getName() );
		this.type = type;
	}
	@Override
	protected void addChildrenToBlank( cascade.Blank blank ) {
		blank.addFillIn( new ProceduresFillIn( this.type ) );
		blank.addFillIn( new FunctionsFillIn( this.type ) );
		blank.addFillIn( new FieldsFillIn( this.type ) );
	}
}

class ProjectBlank extends cascade.Blank {
	private edu.cmu.cs.dennisc.alice.Project project;
	public ProjectBlank( edu.cmu.cs.dennisc.alice.Project project ) {
		this.project = project;
	}
	@Override
	protected void addChildren() {
		edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice >( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice.class );
		this.project.getProgramType().crawl( crawler, true );
		for( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type : crawler.getList() ) {
			this.addFillIn( new TypeFillIn( type ) );
		}
	}
}

class RootOperation extends org.alice.ide.operations.AbstractActionOperation {
	public RootOperation() {
		this.putValue( javax.swing.Action.NAME, "Project >>" );
	}
	public void perform( zoot.ActionContext actionContext ) {
		java.awt.Component component = (java.awt.Component)actionContext.getEvent().getSource();
		int x = component.getWidth();
		int y = component.getHeight();
		new ProjectBlank( getIDE().getProject() ).showPopupMenu( component, x, y, new edu.cmu.cs.dennisc.task.TaskObserver< zoot.ActionOperation >() {
			public void handleCompletion( zoot.ActionOperation actionOperation ) {
				zoot.ZManager.performIfAppropriate( actionOperation, null, zoot.ZManager.CANCEL_IS_WORTHWHILE );
			}
			public void handleCancelation() {
			}
		} );
	}
}
class TypeOperation extends org.alice.ide.operations.AbstractActionOperation {
	public void perform( zoot.ActionContext actionContext ) {
		java.awt.Component component = (java.awt.Component)actionContext.getEvent().getSource();
		int x = component.getWidth();
		int y = component.getHeight();
		new TypeFillIn( (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)getIDE().getFocusedCode().getDeclaringType() ).showPopupMenu( component, x, y, new edu.cmu.cs.dennisc.task.TaskObserver< zoot.ActionOperation >() {
			public void handleCompletion( zoot.ActionOperation actionOperation ) {
				zoot.ZManager.performIfAppropriate( actionOperation, null, zoot.ZManager.CANCEL_IS_WORTHWHILE );
			}
			public void handleCancelation() {
			}
		} );
	}
}

public class DeclarationsEditor extends swing.LineAxisPane implements org.alice.ide.event.IDEListener {
	private RootOperation rootOperation = new RootOperation();
	private TypeOperation typeOperation = new TypeOperation();
	public DeclarationsEditor() {
		org.alice.ide.IDE.getSingleton().addIDEListener( this );
		this.add( new zoot.ZButton( rootOperation ) );
		this.add( new zoot.ZButton( typeOperation ) );
	}

	public void fieldSelectionChanging( org.alice.ide.event.FieldSelectionEvent e ) {
	}
	public void fieldSelectionChanged( org.alice.ide.event.FieldSelectionEvent e ) {
	}
	public void focusedCodeChanging( org.alice.ide.event.FocusedCodeChangeEvent e ) {
	}
	public void focusedCodeChanged( org.alice.ide.event.FocusedCodeChangeEvent e ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractCode code = e.getNextValue();
		this.typeOperation.getActionForConfiguringSwing().putValue( javax.swing.Action.NAME, code.getDeclaringType().getName() + " >>" );
		this.revalidate();
		this.repaint();
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
	
	public static void main( String[] args ) {
		org.alice.ide.IDE ide = new org.alice.ide.FauxIDE();
		
		DeclarationsEditor declarationPane = new DeclarationsEditor();
		
		ide.loadProjectFrom( "C:/Users/estrian/Documents/Alice3/MyProjects/a.a3p" );
		
		zoot.ZFrame frame = new zoot.ZFrame() {
			@Override
			protected void handleWindowOpened( java.awt.event.WindowEvent e ) {
			}
			@Override
			protected void handleQuit( java.util.EventObject e ) {
				System.exit( 0 );
			}
		};
		
		frame.getContentPane().add( declarationPane );
		frame.pack();
		frame.setVisible( true );
	}
}
