package ats.baseitems;

import android.hardware.Camera;

public class flash extends TestItemActivity
{  
	Camera camera = null;
	@Override
	public void onTestInit() 
	{
		this.setTitle("Flash Led Test");
		led_on();
    }
	
	@Override
	public void onTestStop() 
	{
		led_off();
	}
	private void led_on()
	{
	    if (null == camera){  
	    	camera = Camera.open();      
	    }
	    
	    Camera.Parameters parameters = camera.getParameters();               
	    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);    
	    camera.setParameters(parameters);
	    
	    /*
	    m_Camera.autoFocus(new Camera.AutoFocusCallback (){    
	    	public void onAutoFocus(boolean success, Camera camera)
			{
	        }
	    });
	    m_Camera.startPreview();
	    */
	}
	
	private void led_off()
	{  
	    if(camera != null){
	    	camera.stopPreview();
	    	camera.release();
	    	camera = null;
	    }
	}  
}
