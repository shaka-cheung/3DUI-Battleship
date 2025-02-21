package de.tum.in.far.threedui.general;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.TransformGroup;

public class TransformableObject extends BranchGroup {

	protected final TransformGroup transGroup;
	
	public TransformableObject() {
		transGroup = new TransformGroup();
		transGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		transGroup.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		transGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		this.setCapability(ALLOW_DETACH);
		addChild(transGroup);
	}
	
	public TransformGroup getTransformGroup() {
		return transGroup;
	}
}
