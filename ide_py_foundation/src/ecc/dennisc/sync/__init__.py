from synchronize import make_synchronized
def waitForSignal( o ):
	o.wait()
waitForSignal = make_synchronized( waitForSignal )
def notifySignal( o ):
	o.notifyAll()
notifySignal = make_synchronized( notifySignal )
