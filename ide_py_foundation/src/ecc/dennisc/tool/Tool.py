import java
import javax

class Tool( javax.swing.JFrame, java.awt.event.WindowListener ):
	def __init__( self, pane, args, argDefaults ):
		javax.swing.JFrame.__init__( self )
		self._pane = pane
		self.getContentPane().setLayout( java.awt.BorderLayout() )
		self.getContentPane().add( self._pane, java.awt.BorderLayout.CENTER )
		self.setDefaultCloseOperation( javax.swing.JFrame.DO_NOTHING_ON_CLOSE )
		self.addWindowListener( self )
		
		if argDefaults.has_key( "size" ):
			pass
		else:
			argDefaults[ "size" ] = "None"
		
		self._processArgs( args[1:], argDefaults )

	def _handleArg( self, key, value ):
		if key=="size":
			size = eval( value )
			if size:
				self.setSize( java.awt.Dimension( *size ) )
			else:
				self.pack()
		else:
			print "WARNING: argument not handled:", key, value

	def _processArgs( self, args, argDefaults ):
		map = argDefaults.copy()
		for arg in args:
			key, value = arg.split( "=" )
			map[ key ] = value
		for key, value in map.items():
			self._handleArg( key, value )

	def windowOpened( self, e ):
		pass
	def windowClosing( self, e ):
		self.dispose()
	def windowClosed( self, e ):
		pass
	def windowActivated( self, e ):
		pass
	def windowDeactivated( self, e ):
		pass
	def windowIconified( self, e ):
		pass
	def windowDeiconified( self, e ):
		pass
