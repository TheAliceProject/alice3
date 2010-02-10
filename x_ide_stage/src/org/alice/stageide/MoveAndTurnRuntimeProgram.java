package org.alice.stageide;

public class MoveAndTurnRuntimeProgram extends org.alice.apis.moveandturn.Program {
	private static java.awt.Dimension preferredSize = new java.awt.Dimension( 400, 300 );
	private edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType;
	private edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice sceneInstance;
	private edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vm;
	public MoveAndTurnRuntimeProgram( edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType, edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vm ) {
		this.sceneType = sceneType;
		this.vm = vm;
		this.addComponentListener( new java.awt.event.ComponentListener() {
			public void componentShown( java.awt.event.ComponentEvent e ) {
			}
			public void componentHidden( java.awt.event.ComponentEvent e ) {
			}
			public void componentMoved( java.awt.event.ComponentEvent e ) {
			}
			public void componentResized( java.awt.event.ComponentEvent e ) {
				e.getComponent().getSize( MoveAndTurnRuntimeProgram.preferredSize );
			}
		} );
	}
	
	public edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine getVM() {
		return this.vm;
	}
	public edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice getSceneInstance() {
		return this.sceneInstance;
	}
	@Override
	protected void initialize() {
		//this.vm.setResources( org.alice.ide.IDE.getSingleton().getResources() );
		this.sceneInstance = (edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice)this.vm.createInstanceEntryPoint( this.sceneType );
		org.alice.apis.moveandturn.Scene scene = (org.alice.apis.moveandturn.Scene)this.sceneInstance.getInstanceInJava();
		this.setScene( scene );
	}
	@Override
	protected void run() {
		this.getOnscreenLookingGlass().getAWTComponent().requestFocus();
		this.vm.invokeEntryPoint( this.sceneType.getDeclaredMethod( "run" ), this.sceneInstance );
	}
	
	private edu.cmu.cs.dennisc.zoot.ActionOperation getRestartOperation() {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		if( ide != null ) {
			return ide.getRestartOperation();
		} else {
			return null;
		}
	}
	@Override
	protected boolean isRestartSupported() {
		return getRestartOperation() != null;
	}
	@Override
	protected void restart( java.util.EventObject e ) {
		super.restart( e );
		javax.swing.SwingUtilities.getRoot( this ).setVisible( false );
		edu.cmu.cs.dennisc.zoot.ZManager.performIfAppropriate( this.getRestartOperation(), null, edu.cmu.cs.dennisc.zoot.ZManager.CANCEL_IS_WORTHWHILE );
	}
	
	@Override
	public java.awt.Dimension getPreferredSize() {
		return MoveAndTurnRuntimeProgram.preferredSize;
	}
}
