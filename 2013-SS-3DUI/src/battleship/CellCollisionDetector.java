package battleship;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Node;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnCollisionEntry;
import javax.media.j3d.WakeupOnCollisionExit;
import javax.media.j3d.WakeupOnCollisionMovement;
import javax.media.j3d.WakeupOr;
import javax.vecmath.Point3d;

import javax.media.j3d.Switch;

public class CellCollisionDetector extends Behavior {
	
	private WakeupCriterion[] wakeups;
	private WakeupOr oredCriteria;
	
	private Node collidingShape;
	
	public CellCollisionDetector(Node n) {
		collidingShape = n;
		setSchedulingBounds(new BoundingSphere(new Point3d(), 200.0));
	}
	
	public void initialize() {
		wakeups = new WakeupCriterion[3];
		wakeups[0] = new WakeupOnCollisionEntry(collidingShape, WakeupOnCollisionEntry.USE_GEOMETRY);
		wakeups[1] = new WakeupOnCollisionExit(collidingShape, WakeupOnCollisionExit.USE_GEOMETRY);
		wakeups[2] = new WakeupOnCollisionMovement(collidingShape, WakeupOnCollisionMovement.USE_GEOMETRY);
		oredCriteria = new WakeupOr(wakeups);
		wakeupOn(oredCriteria);
	}

	public void processStimulus(Enumeration e) {
		WakeupCriterion criterion = (WakeupCriterion) e.nextElement();
		Switch cellSwitch = (Switch)collidingShape;
		
		if (criterion instanceof WakeupOnCollisionEntry || criterion instanceof WakeupOnCollisionExit) {
			if (criterion instanceof WakeupOnCollisionEntry) {
				cellSwitch.setWhichChild(1);
			}
			else {
				cellSwitch.setWhichChild(0);
			}
		}			
				
		wakeupOn(oredCriteria);
	}
}
