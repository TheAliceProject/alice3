package org.alice.apis.moveandturn.project;

public class ProjectProgram extends org.alice.apis.moveandturn.Program {
	private edu.cmu.cs.dennisc.alice.Project project;
	private edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vm = new edu.cmu.cs.dennisc.alice.virtualmachine.ReleaseVirtualMachine();
	private edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> typeScene;
	private edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice instanceScene;
	
	public ProjectProgram( String path ) {
		java.io.File file = new java.io.File( path );
		assert file.exists();
		this.project = edu.cmu.cs.dennisc.alice.project.ProjectUtilities.readProject(file);
		assert this.project != null;
	}
	@Override
	protected void initialize() {
		this.typeScene = this.project.getProgramType().getDeclaredFields().get( 0 ).getValueType();
		this.instanceScene = (edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice)this.vm.createInstanceEntryPoint( typeScene );
		this.setScene( this.instanceScene.getInstanceInJava( org.alice.apis.moveandturn.Scene.class ) );
	}
	@Override
	protected void run() {
		this.vm.invokeEntryPoint(this.typeScene.getDeclaredMethod("run"), this.instanceScene);
	}
	
	protected edu.cmu.cs.dennisc.alice.Project getProject() {
		return this.project;
	}
	protected edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine getVM() {
		return this.vm;
	}
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> getTypeScene() {
		return this.typeScene;
	}
	protected edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice getInstanceScene() {
		return this.instanceScene;
	}
	
	public static void main(String[] args) {
		String path;
		if( args.length > 0 ) {
			path = args[ 0 ];
		} else {
			path = edu.cmu.cs.dennisc.alice.project.ProjectUtilities.getMyProjectsDirectory().getAbsolutePath() + "/a.a3p";
		}
		ProjectProgram projectProgram = new ProjectProgram( path );
		projectProgram.showInJFrame(args, true);
	}
}
