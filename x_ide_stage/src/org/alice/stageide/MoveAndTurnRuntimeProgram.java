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
		this.sceneInstance = (edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice)this.vm.createInstanceEntryPoint( this.sceneType );
		org.alice.apis.moveandturn.Scene scene = (org.alice.apis.moveandturn.Scene)this.sceneInstance.getInstanceInJava();
		this.setScene( scene );
	}
	@Override
	protected void run() {
		this.vm.invokeEntryPoint( this.sceneType.getDeclaredMethod( "run" ), this.sceneInstance );
	}
	
	@Override
	public java.awt.Dimension getPreferredSize() {
		return MoveAndTurnRuntimeProgram.preferredSize;
	}
	
}
