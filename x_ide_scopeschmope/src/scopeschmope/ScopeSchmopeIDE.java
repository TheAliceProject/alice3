package scopeschmope;

class ScopeSchmopeVirtualMachine extends edu.cmu.cs.dennisc.alice.virtualmachine.ReleaseVirtualMachine {
	private Object ths = null; 
	@Override
	protected Object getThis() {
		//it just so happens that the first call to this is the scene
		//since this whole thing is a hack we just roll with it
		if( this.ths != null ) {
			//pass
		} else {
			this.ths = super.getThis();
		}
		return this.ths;
	}
}

public class ScopeSchmopeIDE extends org.alice.stageide.StageIDE {
	public ScopeSchmopeIDE() {
		this.setLocale( new java.util.Locale( "en", "US", "scopeschmope" ) );
	}
	@Override
	public String getApplicationName() {
		return "ScopeSchmope";
	}
	@Override
	public edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine createVirtualMachineForRuntimeProgram() {
		return new ScopeSchmopeVirtualMachine();
	}
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
