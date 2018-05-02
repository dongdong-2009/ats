package ats.baseitems;

import android.os.Vibrator;

public class vib extends TestItemActivity
{  
	@Override
	public void onTestInit() {
		this.setTitle("Vibrator Test");

		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(5000);
    }
}
