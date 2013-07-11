package de.tum.in.far.threedui.general;

import java.io.File;

import ubitrack.SimpleFacade;
import ubitrack.SimpleImageReceiver;
import ubitrack.SimplePoseReceiver;
import ubitrack.ubitrack;

public class UbitrackFacade {

//	public final static String COMPONENT_DIRECTORY = System.getProperty("user.dir") + File.separator + "libs" + File.separator + "ubitrack" + File.separator + "bin" + File.separator + "ubitrack";
	public final static String COMPONENT_DIRECTORY = "/home/alfonso/tum/ubitrack/UbiTrack/lib32/ubitrack";
	public final static String DATAFLOW_PATH = System.getProperty("user.dir") + File.separator + "dataflow" + File.separator + "3D-UI-Markertracker_HighGui.dfg";	
	
	static {
		
		System.load("/usr/lib/libutCore_x86.so");
		System.load("/usr/lib/libutDataflow_x86.so");
		System.load("/usr/lib/libutVision_x86.so");
		System.load("/usr/lib/libUbitrack_x86.so");
		System.load("/usr/lib/libubitrack_java.so");
		
		
	}

	private SimpleFacade facade;

	public void initUbitrack() {
		ubitrack.initLogging();
		
		facade = new SimpleFacade(COMPONENT_DIRECTORY);
		
		if (facade.getLastError() != null) {
			return;
		}
		if (!facade.loadDataflow(DATAFLOW_PATH)) {
			return;
		}
	}
	
	public boolean setPoseCallback(String name, SimplePoseReceiver poseReceiver){
		return facade.setPoseCallback(name, poseReceiver);
	}
	
	public boolean setImageCallback(String name, SimpleImageReceiver imageReceiver){
		return facade.setImageCallback(name, imageReceiver);
	}

	public void startDataflow(){
		facade.startDataflow();
	}
	public void stopUbitrack() {
		System.out.println("stopUbitrack");
		facade.stopDataflow();
		 
		// Garbage collection for cleanup of native Ubitrack stuff
		System.gc();
		System.runFinalization();
	}
	
}
