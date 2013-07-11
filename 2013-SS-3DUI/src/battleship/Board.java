package battleship;

import java.util.Timer;
import java.util.TimerTask;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3b;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Box;

import de.tum.in.far.threedui.general.BlueAppearance;

public class Board {

	private static final int TIME_OUT = 1000;
	
	private static int size = 6;
	private static float cellSpacing = 0.04f;
	private Cell cells[][][];
	private BranchGroup board3D;
	private TransformGroup boardTG;
    private PoseReceiverTimestamp playerPR;
	
	public Board() {
		cells = new Cell[size][size][size];
		board3D = new BranchGroup();
		boardTG = new TransformGroup();
		
		boardTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		board3D.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		board3D.addChild(boardTG);
		playerPR = new PoseReceiverTimestamp();
		
		BlueAppearance blue = new BlueAppearance();
		Box cell;
		TransformGroup cellTG;
		Transform3D cellTransform;
		
		// Create the board 3D cell boxes 
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				for (int k = 0; k < size; k++) {
					cellTG = new TransformGroup();
					cellTransform = new Transform3D();
					cells[i][j][k] = new Cell();
					cellTransform.setTranslation(new Vector3d(cellSpacing * (i - (size / 2)),
															  cellSpacing * (j - (size / 2)),
															  cellSpacing * k + 0.008f));


					cellTG.setTransform(cellTransform);
					cellTG.addChild(cells[i][j][k].getCellGraph());
					//cellTG.addChild(cells[i][j][k].getDetector());
					boardTG.addChild(cellTG);
				}
			}
		}
		
		Timer t = new Timer();
		TimerTask tt = new TimerTask(){
			
			public void run() {
				
				if(!playerPR.initialized)
					return;
				long currTime = System.currentTimeMillis();
				
				TransformGroup tg = playerPR.getTG();
				Transform3D trans = new Transform3D();
				tg.getTransform(trans);
				Vector3d markerPos = new Vector3d();
				trans.get(markerPos);
				Point3d markerPoint = new Point3d((Tuple3d)markerPos);
				
				LineArray la = new LineArray(2, LineArray.COORDINATES);
				la.setCoordinate(0, new Point3d());
				la.setCoordinate(1, markerPoint);
				
				BranchGroup line = new BranchGroup();
				line.addChild(new Shape3D(la));
				board3D.addChild(line);
			
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						for (int k = 0; k < size; k++) {
							Point3d cellPos = getPositionOfCell(i, j, k);
							double distance = cellPos.distance(markerPoint);
							
							if (distance < 0.03) {
								cells[i][j][k].changeCellState(Cell.State.SELECTED);
							}
							else {
								cells[i][j][k].changeCellState(Cell.State.NOT_SELECTED);
							}
						}
					}
				}
			}
		};
		t.scheduleAtFixedRate(tt, 0, TIME_OUT/10);
	}

	
	
	public BranchGroup getBranchGroup() {
		return board3D;
	}
	
	public TransformGroup getTransformGroup() {
		return boardTG;
	}
	
	public Point3d getPositionOfCell(int i, int j, int k) {
		
		// Get the vector from the board marker to the cell position
		Point3d cellVec = new Point3d(cellSpacing * (i - (size / 2)),
									  cellSpacing * (j - (size / 2)),
									  cellSpacing * k + 0.008f);
		
		// Get the vector from the origin to the board marker
		Transform3D boardT = new Transform3D();
		boardTG.getTransform(boardT);
		boardT.transform(cellVec);
		
		return cellVec;
	}
	
	public void setPlayerPoseReceiver(PoseReceiverTimestamp pPR) {
		playerPR = pPR;
	}
}
