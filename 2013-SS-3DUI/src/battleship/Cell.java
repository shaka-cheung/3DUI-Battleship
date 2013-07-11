package battleship;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.Switch;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;

import com.sun.j3d.utils.geometry.Box;

import de.tum.in.far.threedui.general.BlueAppearance;

public class Cell {
	private static float boxSize = 0.008f;
	private Switch cell;
	private int status;
	//private CellCollisionDetector detector;
	
	public enum State {
		NOT_SELECTED, SELECTED, EMPTY, SHIP
	}
	
	public Cell() {
		Box c;
		cell = new Switch();
		
		/**
		 * Transparent appearance
		 */
		Appearance blueTransparent = new Appearance();
		TransparencyAttributes ta = new TransparencyAttributes();
		ColoringAttributes ca = new ColoringAttributes();
		ca.setColor(0.3f, 0.3f, 0.8f);
		ta.setTransparencyMode(TransparencyAttributes.FASTEST);
		ta.setTransparency(0.75f);
		blueTransparent.setTransparencyAttributes(ta);
		blueTransparent.setColoringAttributes(ca);
		
		c = new Box(boxSize, boxSize, boxSize, blueTransparent);
		cell.addChild(c);
		
		/**
		 * Yellow appearance
		 */
		Appearance yellowAppearance = new Appearance();
		Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
		Color3f yellow = new Color3f(0.9f, 0.8f, 0.1f);
		Color3f specular = new Color3f(0.9f, 0.9f, 0.9f);
		
		// Ambient,emissive,diffuse,specular,shininess
		Material yelMat = new Material(yellow, black, yellow, specular,25.0f);
		
		//Switch on light
		yelMat.setLightingEnable(true);
		
		yellowAppearance.setMaterial(yelMat);
		
		c = new Box(boxSize, boxSize, boxSize, yellowAppearance);
		cell.addChild(c);
		
		cell.setWhichChild(0);
		cell.setCapability(Switch.ALLOW_SWITCH_WRITE);
		//detector = new CellCollisionDetector(cell);
	}
	
	public Switch getCellGraph() {
		return cell;
	}
	
	public void changeCellState(State s) {
		switch (s) {
		case NOT_SELECTED:
			cell.setWhichChild(0);
			break;
		case SELECTED:
			cell.setWhichChild(1);
			break;
		default:
			break;
		}
	}
	//public CellCollisionDetector getDetector() {
	//	return detector;
	//}
	
}
