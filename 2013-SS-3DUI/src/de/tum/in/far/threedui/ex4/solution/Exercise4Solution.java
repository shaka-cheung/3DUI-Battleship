package de.tum.in.far.threedui.ex4.solution;

import java.io.File;
import java.io.FileNotFoundException;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Vector3d;

import org.jdesktop.j3d.loaders.vrml97.VrmlLoader;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;

import de.tum.in.far.threedui.general.BackgroundObject;
import de.tum.in.far.threedui.general.BinaryEnv;
import de.tum.in.far.threedui.general.ImageReceiver;
import de.tum.in.far.threedui.general.ModelObject;
import de.tum.in.far.threedui.general.PoseReceiver;
import de.tum.in.far.threedui.general.UbitrackFacade;
import de.tum.in.far.threedui.general.ViewerUbitrack;

public class Exercise4Solution {

	public static final String EXERCISE = "Exercise 4 Solution";
	
	static {
		BinaryEnv.init();
	}
	
	private UbitrackFacade ubitrackFacade;

	private ViewerUbitrack viewer;
	private Button button;
	private ModelObject sheepObject;
	private SheepMotion sheepMotion;
	
	private PoseReceiverButton poseReceiverButton;
	private PoseReceiver poseReceiver2;
	private PoseReceiver poseReceiver3;
	private ImageReceiver imageReceiver;
	
	private CollisionDetector detector;
		
	public Exercise4Solution() {
		ubitrackFacade = new UbitrackFacade();
		button = new Button();
	}

	public static void main(String[] args) {
		Exercise4Solution exercise4 = new Exercise4Solution();
		exercise4.initializeJava3D();
		exercise4.loadSheep();
		exercise4.initializeUbitrack();
	}
	
	private void initializeUbitrack() {
		ubitrackFacade.initUbitrack();
		
		poseReceiverButton = new PoseReceiverButton();	
		if (!ubitrackFacade.setPoseCallback("posesink", poseReceiverButton)) {
			return;
		}
		poseReceiver2 = new PoseReceiver();
		if (!ubitrackFacade.setPoseCallback("posesink2", poseReceiver2)) {
			return;
		}
		poseReceiver3 = new PoseReceiver();
		if (!ubitrackFacade.setPoseCallback("posesink3", poseReceiver3)) {
			return;
		}
		imageReceiver = new ImageReceiver();
		if (!ubitrackFacade.setImageCallback("imgsink", imageReceiver)) {
			return;
		}
		ubitrackFacade.startDataflow();
		
		BackgroundObject backgroundObject = new BackgroundObject();
		viewer.addObject(backgroundObject);
		imageReceiver.setBackground(backgroundObject.getBackground());
		
		button.setPoseReceiver(poseReceiverButton);		
		
		poseReceiver2.setTransformGroup(sheepMotion.getMarkerTransformGroup(0));
		poseReceiver3.setTransformGroup(sheepMotion.getMarkerTransformGroup(1));
		
	}
		
	protected void loadSheep() {
		VrmlLoader loader = new VrmlLoader();
		Scene myScene = null;
		try {
			myScene = loader.load( "models" + File.separator + "Sheep.wrl");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IncorrectFormatException e) {
			e.printStackTrace();
		} catch (ParsingErrorException e) {
			e.printStackTrace();
		}

		BranchGroup bg = new BranchGroup();
		TransformGroup offset = new TransformGroup();
		Transform3D t3d = new Transform3D();
		t3d.setRotation(new AxisAngle4d(1.0, 0.0, 0.0, Math.PI/2));
		t3d.setTranslation(new Vector3d(0.0, 0.0, 0.025));
		offset.setTransform(t3d);
		bg.addChild(offset);
		offset.addChild(myScene.getSceneGroup());

		
		bg.setCapability(TransformGroup.ENABLE_COLLISION_REPORTING);
		bg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		detector = new CollisionDetector(bg);
		bg.addChild(detector);
		
		sheepObject = new ModelObject(bg);
		viewer.addObject(sheepObject);
		
		sheepMotion = new SheepMotion(sheepObject);
		sheepMotion.registerWithButton(button);
		button.registerWithCollisionDetector(detector);
		
		System.out.println("Sheep loaded");
	}

	private void initializeJava3D() {
		System.out.println("Creating Viewer - " + EXERCISE);
		viewer = new ViewerUbitrack(EXERCISE, ubitrackFacade);

		viewer.addObject(button.getJ3dObject());
				
		System.out.println("Done");
	}

}
