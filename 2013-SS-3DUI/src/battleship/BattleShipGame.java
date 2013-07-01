package battleship;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.TransformGroup;

import com.sun.j3d.utils.geometry.Box;

import de.tum.in.far.threedui.ex4.solution.PoseReceiverButton;
import de.tum.in.far.threedui.general.BackgroundObject;
import de.tum.in.far.threedui.general.BinaryEnv;
import de.tum.in.far.threedui.general.BlueAppearance;
import de.tum.in.far.threedui.general.ImageReceiver;
import de.tum.in.far.threedui.general.PoseReceiver;
import de.tum.in.far.threedui.general.UbitrackFacade;
import de.tum.in.far.threedui.general.ViewerUbitrack;

public class BattleShipGame {

	private UbitrackFacade ubitrackFacade;

	private ViewerUbitrack viewer;

	private PoseReceiver boardPoseReceiver;
	private PoseReceiver handlerPoseReceiver;
	private ImageReceiver imageReceiver1;
	
	private Board board;
	

	public static final String EXERCISE = "3DBattleship";
	
	static {
		BinaryEnv.init();
	}
		
	public BattleShipGame() {
		// TODO Auto-generated constructor stub
		ubitrackFacade = new UbitrackFacade();
		board = new Board();
	}

	public static void main(String[] args) {
		BattleShipGame game = new BattleShipGame();
		game.initializeJava3D();
		game.initializeUbitrack();
	}
	
	private void initializeUbitrack() {
		ubitrackFacade.initUbitrack();
		
		boardPoseReceiver = new PoseReceiver();	
		if (!ubitrackFacade.setPoseCallback("posesink", boardPoseReceiver)) {
			return;
		}
		
		handlerPoseReceiver = new PoseReceiver();	
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
		
		BranchGroup bg = new BranchGroup();
		TransformGroup tg = new TransformGroup();
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Box box = new Box(0.023f, 0.023f, 0.023f, new BlueAppearance());
		bg.addChild(tg);
		tg.addChild(box);
		handlerPoseReceiver.setTransformGroup(tg);
		
		viewer.addObject(board.getBranchGroup());
		viewer.addObject(bg);
		
	}

	private void initializeJava3D() {
		System.out.println("Creating Viewer - " + EXERCISE);
		viewer = new ViewerUbitrack(EXERCISE, ubitrackFacade);
		System.out.println("Done");
	}
}
