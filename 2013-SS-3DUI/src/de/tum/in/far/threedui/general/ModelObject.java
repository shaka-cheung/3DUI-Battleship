package de.tum.in.far.threedui.general;

import javax.media.j3d.BranchGroup;


public class ModelObject extends TransformableObject {
	
	public ModelObject(BranchGroup model) {
		super.transGroup.addChild(model);
	}
	
}
