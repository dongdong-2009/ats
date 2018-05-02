package ats.baseitems;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;

public class AudioActivity extends TestItemActivity {
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		
		int streamType = AudioManager.STREAM_RING 
				| 	AudioManager.STREAM_MUSIC
				| 	AudioManager.STREAM_SYSTEM
				| 	AudioManager.STREAM_VOICE_CALL
				| 	AudioManager.STREAM_ALARM
				|	AudioManager.STREAM_DTMF
				|	AudioManager.STREAM_NOTIFICATION;
		am.setStreamMute(streamType, false);
		
		int maxVolume;
		streamType = AudioManager.STREAM_RING;
		maxVolume = am.getStreamMaxVolume(streamType);
		am.setStreamVolume(streamType, maxVolume, 0);
		
		streamType = AudioManager.STREAM_MUSIC;
		maxVolume = am.getStreamMaxVolume(streamType);
		am.setStreamVolume(streamType, maxVolume, 0);
		
		streamType = AudioManager.STREAM_SYSTEM;
		maxVolume = am.getStreamMaxVolume(streamType);
		am.setStreamVolume(streamType, maxVolume, 0);
		
		streamType = AudioManager.STREAM_VOICE_CALL;
		maxVolume = am.getStreamMaxVolume(streamType);
		am.setStreamVolume(streamType, maxVolume, 0);
		
		streamType = AudioManager.STREAM_ALARM;
		maxVolume = am.getStreamMaxVolume(streamType);
		am.setStreamVolume(streamType, maxVolume, 0);
		
		streamType = AudioManager.STREAM_DTMF;
		maxVolume = am.getStreamMaxVolume(streamType);
		am.setStreamVolume(streamType, maxVolume, 0);
		
		streamType = AudioManager.STREAM_NOTIFICATION;
		maxVolume = am.getStreamMaxVolume(streamType);
		am.setStreamVolume(streamType, maxVolume, 0);
	}
}
