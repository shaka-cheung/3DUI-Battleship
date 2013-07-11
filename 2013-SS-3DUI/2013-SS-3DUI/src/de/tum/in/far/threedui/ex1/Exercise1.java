package de.tum.in.far.threedui.ex1;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

import de.tum.in.far.threedui.general.BinaryEnv;
import de.tum.in.far.threedui.general.BlueAppearance;
import de.tum.in.far.threedui.general.Viewer;

public class Exercise1 {

	public static final String EXERCISE = "Exercise 1";

	static {
		BinaryEnv.init();
	}
	
	public static void main(String[] args) {
		Exercise1 exercise1 = new Exercise1();
		exercise1.initializeJava3D();
	}
	
	private void initializeJava3D() {
		System.out.println("Creating Viewer - " + EXERCISE);
		Viewer viewer = new Viewer(EXERCISE);

		System.out.println("Create Sphere Object");
		SphereObject sphereObject = new SphereObject(0.1f);
		viewer.addObject(sphereObject);
		
		Transform3D sphereT3D = new Transform3D();
		sphereT3D.setTranslation(new Vector3d(0.0, 0.2, 0.0));
		sphereObject.getTransformGroup().setTransform(sphereT3D);
		
		System.out.println("Move Camera backwards and a little bit up");
		Transform3D cameraTransform = new Transform3D();
		cameraTransform.setTranslation(new Vector3d(0.0, 0.15, 1.0));
		TransformGroup cameraTG = viewer.getCameraTransformGroup();
		cameraTG.setTransform(cameraTransform);
		
		System.out.println("Create Blue Sphere Object");
		BlueAppearance blueAppearance = new BlueAppearance();
		SphereObject blueSphereObject = new SphereObject(0.04f, blueAppearance);
		
		Transform3D blueSphereT3D = new Transform3D();
		blueSphereT3D.setTranslation(new Vector3d(0.2, 0.2, 0.0));
		blueSphereObject.getTransformGroup().setTransform(blueSphereT3D);
								
		System.out.println("Animation");
		AnimationRotation animationRotation = new AnimationRotation(blueSphereObject);
		viewer.addObject(animationRotation);
		
		System.out.println("Done - Enjoy");
	}
	

}
