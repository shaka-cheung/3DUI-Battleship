package battleship;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Box;

import de.tum.in.far.threedui.general.BlueAppearance;

public class Board {

	private static int size = 6;
	private Cell cells[][][];
	private BranchGroup board3D;
	private TransformGroup boardTG;
	
	public Board() {
		cells = new Cell[size][size][size];
		board3D = new BranchGroup();
		boardTG = new TransformGroup();
		
		boardTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		board3D.addChild(boardTG);
		
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
					cellTransform.setTranslation(new Vector3d(0.03f * (i - 4),
															  0.03f * (j - 4),
															  0.03f * k + 0.008f));
					

					cellTG.setTransform(cellTransform);
					cellTG.addChild(cells[i][j][k].getCellGraph());
					cellTG.addChild(cells[i][j][k].getDetector());
					boardTG.addChild(cellTG);
				}
			}
		}
	}
	
	public BranchGroup getBranchGroup() {
		return board3D;
	}
	
	public TransformGroup getTransformGroup() {
		return boardTG;
	}
}
