package ats.baseitems;

import android.app.Notification;
import android.app.NotificationManager;

public class led extends TestItemActivity
{  
	@Override
	public void onTestInit() {
		this.setTitle("Promptimg Led Test");
		Led_on();
    }
	
	@Override
	public void onTestStop() {
		led_off();
	}
	
	public void Led_on() {
		// TODO Auto-generated method stub
		final int ID_LED=19871103; 

		
        NotificationManager nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE); 

        Notification notification = new Notification(); 
        notification.ledARGB = 0xFFFFFF;  //这里是颜色，我们可以尝试改变，理论上0xFF0000是红色，0x00FF00是绿色
        notification.ledOnMS = 100; 
        notification.ledOffMS = 100; 
        notification.flags = Notification.FLAG_SHOW_LIGHTS; 
        nm.notify(ID_LED, notification); 
        nm.cancel(ID_LED);
	}

	public void led_off() {
		// TODO Auto-generated method stub
		
	}
}
