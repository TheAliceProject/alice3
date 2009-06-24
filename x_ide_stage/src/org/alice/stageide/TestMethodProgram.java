package org.alice.stageide;


public class TestMethodProgram extends org.alice.apis.moveandturn.Program {
	class TestVirtualMachine extends edu.cmu.cs.dennisc.alice.virtualmachine.ReleaseVirtualMachine {
		@Override
		public Object get( edu.cmu.cs.dennisc.alice.ast.AbstractField field, Object instance ) {
			return super.get( field, instance );
		}
		@Override
		public java.lang.Object[] evaluateArguments( java.util.ArrayList< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractParameter > parameters, edu.cmu.cs.dennisc.alice.ast.NodeListProperty< edu.cmu.cs.dennisc.alice.ast.Argument > arguments ) {
			return super.evaluateArguments( parameters, arguments );
		}
		@Override
		public void pushFrame( edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice instance, java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractParameter, java.lang.Object > map ) {
			super.pushFrame( instance, map );
		}
		@Override
		public void popFrame() {
			super.popFrame();
		}
		@Override
		public void pushCurrentThread( java.lang.Thread parentThread ) {
			super.pushCurrentThread( parentThread );
		}
		@Override
		public void popCurrentThread() {
			super.popCurrentThread();
		}
	}
	private static java.awt.Dimension preferredSize = new java.awt.Dimension( 400, 300 );
	private edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType;
	private edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice sceneInstance;
	private TestVirtualMachine vm;
	private edu.cmu.cs.dennisc.alice.ast.AbstractField field;
	private edu.cmu.cs.dennisc.alice.ast.MethodInvocation emptyExpressionMethodInvocation;
	public TestMethodProgram( edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType, edu.cmu.cs.dennisc.alice.ast.AbstractField field, edu.cmu.cs.dennisc.alice.ast.MethodInvocation emptyExpressionMethodInvocation ) {
		this.sceneType = sceneType;
		this.field = field;
		this.emptyExpressionMethodInvocation = emptyExpressionMethodInvocation;
		this.vm = new TestVirtualMachine();
		this.vm.registerAnonymousAdapter( org.alice.apis.moveandturn.event.MouseButtonListener.class, org.alice.stageide.apis.moveandturn.event.MouseButtonAdapter.class );
		this.addComponentListener( new java.awt.event.ComponentListener() {
			public void componentShown( java.awt.event.ComponentEvent e ) {
			}
			public void componentHidden( java.awt.event.ComponentEvent e ) {
			}
			public void componentMoved( java.awt.event.ComponentEvent e ) {
			}
			public void componentResized( java.awt.event.ComponentEvent e ) {
				e.getComponent().getSize( TestMethodProgram.preferredSize );
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
		Object instance;
		if( this.field != null ) {
			instance = this.vm.get( this.field, this.sceneInstance );
		} else {
			instance = this.sceneInstance;
		}
		Object[] arguments;
		this.vm.pushCurrentThread( null );
		try {
			this.vm.pushFrame( this.sceneInstance, null );
			try {
				arguments = this.vm.evaluateArguments( this.emptyExpressionMethodInvocation.method.getValue().getParameters(), this.emptyExpressionMethodInvocation.arguments );
			} finally {
				this.vm.popFrame();
			}
		} finally {
			this.vm.popCurrentThread();
		}
		this.vm.invokeEntryPoint( this.emptyExpressionMethodInvocation.method.getValue(), instance, arguments );
		edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 500 );
		java.awt.Component root = javax.swing.SwingUtilities.getRoot( this );
		root.setVisible( false );
	}
	
	@Override
	protected boolean isRestartSupported() {
		return false;
	}
	@Override
	protected void restart( java.util.EventObject e ) {
	}
	
	@Override
	public java.awt.Dimension getPreferredSize() {
		return TestMethodProgram.preferredSize;
	}
}
