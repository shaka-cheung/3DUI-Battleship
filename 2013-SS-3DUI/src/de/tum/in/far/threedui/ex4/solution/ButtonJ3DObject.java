package de.tum.in.far.threedui.ex4.solution;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.Switch;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;

import de.tum.in.far.threedui.general.BlueAppearance;
import de.tum.in.far.threedui.general.TransformableObject;

public class ButtonJ3DObject extends TransformableObject {

	private static final float HEIGHT = 0.046f;

	private Switch buttonSwitch;
	
	private int previousState;
	
	public ButtonJ3DObject() {
		Appearance blueAppearance = new BlueAppearance();
		Appearance redAppearance = new Appearance();
		Appearance greenAppearance = new Appearance();
		
		Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
		Color3f red = new Color3f(0.8f, 0.3f, 0.3f);
		Color3f specular = new Color3f(0.9f, 0.5f, 0.5f);
		Material redMat = new Material(red, black, red, specular, 25.0f);
		redMat.setLightingEnable(true);
		redAppearance.setMaterial(redMat);

		Color3f green = new Color3f(0.3f, 0.8f, 0.3f);
		Material greenMat = new Material(green, black, green, specular, 25.0f);
		greenMat.setLightingEnable(true);
		greenAppearance.setMaterial(greenMat);
		
		buttonSwitch = new Switch();
		buttonSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
		
		Cylinder buttonReleased = new Cylinder(0.005f, HEIGHT, blueAppearance);
		Cylinder buttonPressed = new Cylinder(0.005f, HEIGHT, redAppearance);
		Cylinder buttonPressable = new Cylinder(0.005f, HEIGHT, greenAppearance);
		Cone co = new Cone(0.01f, 0.02f, redAppearance);
		TransformGroup coneTGroup = new TransformGroup();
		Transform3D tf3d = new Transform3D();
		tf3d.set(new Vector3f(0.0f, (HEIGHT / 2 + 0.02f / 2), 0.0f));
		coneTGroup.setTransform(tf3d);
		coneTGroup.addChild(co);
		
		TransformGroup tgCone = new TransformGroup();
		Transform3D t3d = new Transform3D();
		t3d.setTranslation(new Vector3d(0.0, 0.0, 0.005/2));
		tgCone.addChild(coneTGroup);
		
		transGroup.addChild(buttonSwitch);
		transGroup.addChild(tgCone);
		
		buttonSwitch.addChild(buttonReleased);
		buttonSwitch.addChild(buttonPressed);
		buttonSwitch.addChild(buttonPressable);
		buttonSwitch.setWhichChild(0);
	}
	
	public void buttonPressed(boolean flag) {
		if (flag) {
			buttonSwitch.setWhichChild(1);
		} else {
			buttonSwitch.setWhichChild(0);
		}
	}
	
	public void buttonPressable(boolean flag) {
		if (flag) {
			previousState = buttonSwitch.getWhichChild();
			buttonSwitch.setWhichChild(2);
		} else {
			buttonSwitch.setWhichChild(previousState);
		}
	}
	
}
