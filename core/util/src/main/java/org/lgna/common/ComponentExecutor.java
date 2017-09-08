package org.lgna.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ComponentExecutor implements Runnable{
	private static ExecutorService executor = Executors.newCachedThreadPool();

	private final Runnable target;

	public ComponentExecutor( Runnable target, String description ) {
		this.target = target;
	}

	@Override
	public void run() {
		org.lgna.common.ProgramClosedException.invokeAndCatchProgramClosedException( this.target );
	}
	
	public synchronized void start() {
		executor.submit(this);
	}
}
