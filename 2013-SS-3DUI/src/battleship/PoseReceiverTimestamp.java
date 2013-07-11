package battleship;

import javax.media.j3d.TransformGroup;

import ubitrack.SimplePose;
import de.tum.in.far.threedui.general.PoseReceiver;

public class PoseReceiverTimestamp extends PoseReceiver {
	public static final long TIME_OUT = 1000;
	
	public long timeStamp = System.currentTimeMillis();
	public boolean initialized = false;
	
	public PoseReceiverTimestamp() {
	}
	
	public void receivePose(SimplePose pose) {
		super.receivePose(pose);
		initialized =true;
		timeStamp = System.currentTimeMillis();
	}
	
	public TransformGroup getTG(){
		return markerTransGroup;
	}
}
