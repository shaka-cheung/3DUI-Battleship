package battleship;

import java.io.File;
import java.io.FileNotFoundException;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Vector3d;

import org.jdesktop.j3d.loaders.vrml97.VrmlLoader;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.utils.geometry.Box;

import de.tum.in.far.threedui.ex4.solution.PoseReceiverButton;
import de.tum.in.far.threedui.general.BackgroundObject;
import de.tum.in.far.threedui.general.BinaryEnv;
import de.tum.in.far.threedui.general.BlueAppearance;
import de.tum.in.far.threedui.general.ImageReceiver;
import de.tum.in.far.threedui.general.ModelObject;
import de.tum.in.far.threedui.general.PoseReceiver;
import de.tum.in.far.threedui.general.UbitrackFacade;
import de.tum.in.far.threedui.general.ViewerUbitrack;

public class BattleShipGame {

	private UbitrackFacade ubitrackFacade;

	private ViewerUbitrack viewer;

	private PoseReceiver boardPoseReceiver;
	private PoseReceiverTimestamp handlerPoseReceiver;
	private ImageReceiver imageReceiver1;
	
	private Board board;
	private ModelObject ufo;
	

	public static final String EXERCISE = "3DBattleship";
	
	static {
		BinaryEnv.init();
	}
		
	public BattleShipGame() {
		ubitrackFacade = new UbitrackFacade();
		board = new Board();
	}

	public static void main(String[] args) {
		BattleShipGame game = new BattleShipGame();
		game.initializeJava3D();
		game.loadUfo();
		game.initializeUbitrack();
	}
	
	private void initializeUbitrack() {
		ubitrackFacade.initUbitrack();
		
		boardPoseReceiver = new PoseReceiver();	
		if (!ubitrackFacade.setPoseCallback("posesink", boardPoseReceiver)) {
			return;
		}
		
		handlerPoseReceiver = new PoseReceiverTimestamp();	
		if (!ubitrackFacade.setPoseCallback("posesink2", handlerPoseReceiver)) {
			return;
		}

		imageReceiver1 = new ImageReceiver();
		if (!ubitrackFacade.setImageCallback("imgsink", imageReceiver1)) {
			return;
		}
		ubitrackFacade.startDataflow();
		
		BackgroundObject backgroundObject = new BackgroundObject();
		viewer.addObject(backgroundObject);
		imageReceiver1.setBackground(backgroundObject.getBackground());
		
		boardPoseReceiver.setTransformGroup(board.getTransformGroup());
		
		TransformGroup tg = ufo.getTransformGroup();
		//TransformGroup tg = new TransformGroup();
		//tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		//tg.addChild(tg);
		
		handlerPoseReceiver.setTransformGroup(tg);
		board.setPlayerPoseReceiver(handlerPoseReceiver);
		
		viewer.addObject(board.getBranchGroup());
		
	}
	
	protected void loadUfo() {
		VrmlLoader loader = new VrmlLoader();
		Scene myScene = null;
		try {
			myScene = loader.load( "models" + File.separator + "Ufo.wrl");
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
		//t3d.setRotation(new AxisAngle4d(1.0, 0.0, 0.0, Math.PI/2));
		t3d.setScale(0.005);
		t3d.setTranslation(new Vector3d(0.0, -0.03, 0.05));
		offset.setTransform(t3d);
		bg.addChild(offset);
		offset.addChild(myScene.getSceneGroup());

		ufo = new ModelObject(bg);
		viewer.addObject(ufo);
		
		System.out.println("Sheep loaded");
	}

	private void initializeJava3D() {
		System.out.println("Creating Viewer - " + EXERCISE);
		viewer = new ViewerUbitrack(EXERCISE, ubitrackFacade);
		System.out.println("Done");
	}
}
