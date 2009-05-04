package scopeschmope;

class ScopeSchmopeVirtualMachine extends edu.cmu.cs.dennisc.alice.virtualmachine.ReleaseVirtualMachine {
//	private edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField;
//	public ScopeSchmopeVirtualMachine( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField ) {
//		this.sceneField = sceneField;
//	}
//	
	private Object ths = null; 
	@Override
	protected Object getThis() {
		if( this.ths != null ) {
			//pass
		} else {
			this.ths = super.getThis();
		}
		return this.ths;
	}
}

//class ScopeSchmopeSceneEditor extends org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor {
//	@Override
//	protected void updateSceneBasedOnScope() {
//	}
//}

public class ScopeSchmopeIDE extends org.alice.stageide.StageIDE {
	public ScopeSchmopeIDE() {
		this.setLocale( new java.util.Locale( "en", "US", "scopeschmope" ) );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine createVirtualMachineForRuntimeProgram() {
//		return new ScopeSchmopeVirtualMachine( this.getSceneField() );
		return new ScopeSchmopeVirtualMachine();
	}
//	@Override
//	protected org.alice.ide.sceneeditor.AbstractSceneEditor createSceneEditor() {
//		return new ScopeSchmopeSceneEditor();
//	}
	
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getTypeInScope() {
		return this.getSceneType();
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.Expression createInstanceExpression( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		edu.cmu.cs.dennisc.alice.ast.ThisExpression thisExpression = new edu.cmu.cs.dennisc.alice.ast.ThisExpression();
		if( field.getValueType() == this.getSceneType() ) {
			return thisExpression;
		} else {
			return new edu.cmu.cs.dennisc.alice.ast.FieldAccess( thisExpression, field );
		}
	}
	@Override
	public boolean isFieldInScope( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		return true;
	}
	@Override
	public String getInstanceTextForField( edu.cmu.cs.dennisc.alice.ast.AbstractField field, boolean isOutOfScopeTagDesired ) {
		if( field.getValueType() == this.getSceneType() ) {
			return this.getTextForThis();
		} else {
			return field.getName();
		}
	}
	@Override
	protected org.alice.ide.common.DeclarationNameLabel createDeclarationNameLabel( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		return new org.alice.ide.common.DeclarationNameLabel( field );
	}
	
	@Override
	public boolean isInstanceLineDesired() {
		return false;
	}
	public static void main( String[] args ) {
		org.alice.ide.LaunchUtilities.launch( ScopeSchmopeIDE.class, null, args );
	}
}
