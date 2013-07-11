package de.tum.in.far.threedui.ex4.solution;

import java.util.Vector;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

public class Button implements Notifiable {
	
	private ButtonJ3DObject j3dObject;
	private PoseReceiverButton buttonReceiver;
	
	enum ButtonState { RELEASED, PRESSED };	
	private ButtonState buttonState = ButtonState.RELEASED;
	
	enum InteractionState { INACTIVE, PRESSABLE };
	private InteractionState interactionState = InteractionState.INACTIVE;
	
	private BranchGroup buttonBranchGroup;
	private NotifyTransformGroup buttonTransformGroup;
	
	private Vector<Notifiable> observer;
	
	public BranchGroup getJ3dObject() {
		return buttonBranchGroup;
	}

	public Button() {
		j3dObject = new ButtonJ3DObject();
		buttonReceiver = null;
		
		buttonBranchGroup = new BranchGroup();
		buttonTransformGroup = new NotifyTransformGroup();
		buttonTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		buttonTransformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		buttonTransformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);

		
		buttonBranchGroup.addChild(buttonTransformGroup);
		buttonTransformGroup.addChild(j3dObject);
		
		observer = new Vector<Notifiable>();
	}
	
	public void setPoseReceiver(PoseReceiverButton p) {
		buttonReceiver = p;
		p.setObserver(this);
		
		p.setTransformGroup(buttonTransformGroup);
	}

	public void update(Object o, Object aData) {
		if (o.equals((Object)buttonReceiver) && aData instanceof Boolean) {
			ButtonState tempState = ((Boolean)aData).booleanValue() ? ButtonState.PRESSED : ButtonState.RELEASED;
			if (buttonState != tempState) {
				buttonState = tempState;
				setJ3DState();
				notifyObserver();
			}
		}
	}

	public void addObserver(Notifiable n) {
		observer.add(n);
	}

	public void addButtonPoseObserver(Notifiable n) {
		buttonTransformGroup.addObserver(n);
	}
	
	private void notifyObserver() {
		Boolean b = new Boolean(buttonState==ButtonState.PRESSED ? true : false);
		for (Notifiable n: observer) {
			n.update(this, b);
		}
	}

	private void setJ3DState() {
		switch (buttonState){
		case RELEASED:
			if (InteractionState.INACTIVE == interactionState)
				j3dObject.buttonPressed(false);
			else
				j3dObject.buttonPressable(true);
			break;
		case PRESSED:
			j3dObject.buttonPressed(true);
			break;
		default:
			break;
		}
	}

	public void getTransform(Transform3D buttonTransform) {
		buttonTransformGroup.getTransform(buttonTransform);
	}

	
	// what happens if collision occurs while pressed?
	private void handleCollisionInteraction(boolean flag) {
		interactionState = flag ? InteractionState.PRESSABLE : InteractionState.INACTIVE;
		setJ3DState();
	}
	
	public void registerWithCollisionDetector(CollisionDetector detector) {
		detector.addObserver(new Notifiable() {
			public void update(Object notifier, Object aData) {
				if (aData instanceof Boolean)
				handleCollisionInteraction(((Boolean)aData).booleanValue());
			}
		});
	}
}
