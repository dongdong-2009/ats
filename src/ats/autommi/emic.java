package ats.autommi;

import android.content.Context;
import android.media.AudioManager;

public class emic extends ats.baseitems.mic
{
	AudioManager am = null;
	@Override
	public void onTestInit()
	{
		super.onTestInit();
		am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		this.am.setParameters("ForceUseSpecificMic=2");
	}
	
	@Override
	public void onTestStop()
	{
		super.onTestStop();
		this.am.setParameters("ForceUseSpecificMic=1");
	}
}
