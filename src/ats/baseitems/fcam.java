package ats.baseitems;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.os.Bundle;

public class fcam extends bcam
{
	@SuppressLint("NewApi")
	@Override    
	public void onCreate(Bundle savedInstanceState) {    
		super.onCreate(savedInstanceState);
		if(Camera.getNumberOfCameras()>1){
			super.CameraId = 1;
			super.recoreName = "fcam";
			this.setTitle("Fore Camera Test"); 
		}
	}
}