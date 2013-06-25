package de.tum.in.far.threedui.ex4.solution;

import java.util.Vector;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import de.tum.in.far.threedui.general.ModelObject;

public class SheepMotion {

	private ModelObject sheep;
	
	// For validation
	private Button button = null;
	
	private Vector<NotifyTransformGroup> markerTGs;
	private Vector<Transform3D> markerOffsets;
	private Transform3D sheepToButtonOffset;
	private int sheepMarkerIndex = 0;
	
	enum SheepState { ON_MARKER, ON_BUTTON };
	SheepState state = SheepState.ON_MARKER;

	private Notifiable poseUpdater;
		
	public SheepMotion(ModelObject m) {		
		sheep = m;

		poseUpdater = new Notifiable() {			
			public void update(Object notifier, Object aData) {
				updateSheep();
			}
		};

		
		markerTGs = new Vector<NotifyTransformGroup>();
		markerTGs.add(new NotifyTransformGroup()); // Marker 1
		markerTGs.add(new NotifyTransformGroup()); // Marker 2		
		markerTGs.get(0).addObserver(poseUpdater);
		markerTGs.get(1).addObserver(poseUpdater);
		
		markerOffsets = new Vector<Transform3D>();
		markerOffsets.add(new Transform3D());
		markerOffsets.add(new Transform3D());
		
		sheepToButtonOffset = new Transform3D();
	}
	
	public TransformGroup getMarkerTransformGroup(int idx) {
		return markerTGs.get(idx);
	}

	public void registerWithButton(Button b) {
		button = b;
		button.addObserver(new Notifiable() {			
			public void update(Object notifier, Object aData) {
				if (true == (Boolean)aData) {
					handleButtonInteraction();
				}				
			}
		});
				
		button.addButtonPoseObserver(poseUpdater);
	}

	private void updateSheep() {
		computeSheepOffsets();
		Transform3D copy = new Transform3D();

		switch (state) {
		case ON_MARKER:
			// copy marker transform to model
			markerTGs.get(sheepMarkerIndex).getTransform(copy);
			sheep.getTransformGroup().setTransform(copy);			
			break;
		case ON_BUTTON:
			// copy button transform + offset to model
			button.getTransform(copy);
			copy.mul(sheepToButtonOffset);
			sheep.getTransformGroup().setTransform(copy);			
			break;
		default:
			break;	
		}
	}

	private void computeSheepOffsets() {
		Transform3D buttonTransform = new Transform3D();
		Transform3D markerTransform = new Transform3D();
		Transform3D offset = new Transform3D();
		
		button.getTransform(buttonTransform);
		for (int i=0; i<markerTGs.size(); i++) {			
			markerTGs.get(i).getTransform(markerTransform);		
			offset.set(buttonTransform);
			offset.invert();
			offset.mul(markerTransform);
			markerOffsets.get(i).set(offset);
		}
	}

	private void handleButtonInteraction() {
		int idx = getMarkerInVicinity();
		
		// no marker in vicinity
		if (idx == -1)
			return;
		
		switch (state) {
		case ON_MARKER:
			if (idx != sheepMarkerIndex)
				break;			
			// move sheep to button
			state = SheepState.ON_BUTTON;
			sheepToButtonOffset.set(markerOffsets.get(idx));
			break;
		case ON_BUTTON:
			// move sheep to marker
			state = SheepState.ON_MARKER;
			sheepMarkerIndex = idx;
			break;
		default:
			break;
		}
	}

	private int getMarkerInVicinity() {
		int result = -1;

		Vector3f translation = new Vector3f();
		float minDist = 1.0f;
		
		for (int i=0; i<markerOffsets.size(); ++i) {
			markerOffsets.get(i).get(translation);
			float dist = translation.length();
			if (dist > 0.10)
				continue;
			
			if (result == -1 || dist < minDist) {
				minDist = dist;
				result = i;
			}
		}
		return result;
	}
}
