#print "-->", __name__

from AbstractIDE import AbstractIDE

from IDEListenerPane import IDEListenerPane

from ProgramWithSceneMixin import ProgramWithSceneMixin
from SceneAutomaticSetUpMethodFillerInner import SceneAutomaticSetUpMethodFillerInner

def getIDE():
	import edu
	return edu.cmu.cs.dennisc.alice.ide.IDE.getSingleton()

#print "<--", __name__

