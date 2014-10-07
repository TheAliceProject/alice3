@Deprecated
public class ProjectProgram extends org.lgna.story.SProgram {
	private final org.lgna.project.Project project;
	private final org.lgna.project.virtualmachine.VirtualMachine vm = new org.lgna.project.virtualmachine.ReleaseVirtualMachine();
	private final org.lgna.project.ast.NamedUserType typeScene;
	private final org.lgna.project.virtualmachine.UserInstance instanceScene;

	public ProjectProgram( org.lgna.project.Project project ) {
		this.vm.registerAbstractClassAdapter( org.lgna.story.SScene.class, org.alice.stageide.ast.SceneAdapter.class );
		this.vm.registerAbstractClassAdapter( org.lgna.story.event.SceneActivationListener.class, org.alice.stageide.apis.story.event.SceneActivationAdapter.class );
		this.project = project;
		this.typeScene = (org.lgna.project.ast.NamedUserType)this.project.getProgramType().getDeclaredFields().get( 0 ).getValueType();
		this.instanceScene = (org.lgna.project.virtualmachine.UserInstance)this.vm.ENTRY_POINT_createInstance( typeScene );
	}

	protected org.lgna.project.Project getProject() {
		return this.project;
	}

	protected org.lgna.project.virtualmachine.VirtualMachine getVM() {
		return this.vm;
	}

	protected org.lgna.project.ast.AbstractType<?, ?, ?> getTypeScene() {
		return this.typeScene;
	}

	protected org.lgna.project.virtualmachine.UserInstance getInstanceScene() {
		return this.instanceScene;
	}

	public static void main( String[] args ) throws Exception {
		String path;
		if( args.length > 0 ) {
			path = args[ 0 ];
		} else {
			path = org.alice.ide.croquet.models.ui.preferences.UserProjectsDirectoryState.getInstance().getDirectoryEnsuringExistance().getAbsolutePath() + "/a.a3p";
		}
		java.io.File file = new java.io.File( path );
		assert file.exists() : path;

		org.lgna.project.Project project = org.lgna.project.io.IoUtilities.readProject( file );
		final boolean IS_MEND_DESIRED = true;
		if( IS_MEND_DESIRED ) {
			org.alice.ide.ast.type.merge.core.MergeUtilities.mendMethodInvocationsAndFieldAccesses( project.getProgramType() );
		}
		ProjectProgram projectProgram = new ProjectProgram( project );
		projectProgram.initializeInFrame( args );
		projectProgram.setActiveScene( projectProgram.instanceScene.getJavaInstance( org.lgna.story.SScene.class ) );
	}
}
