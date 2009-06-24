import java

class ApplyRunnable( java.lang.Runnable ):
	def __init__( self, fcn, args=(), kws={} ):
		self._fcn = fcn
		self._args = args
		self._kws = kws
	def run(self):
		apply( self._fcn, self._args, self._kws )
