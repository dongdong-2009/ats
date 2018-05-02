package ats.baseitems;

import java.util.LinkedList;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;

public class mic extends AudioActivity
{
	static final int sample_rate = 8000;
	static final int encoding = AudioFormat.ENCODING_PCM_16BIT;
	AudioManager am;
	SoundRecord th_record = new SoundRecord();		//录制音频线程
	SoundPlay th_play = new SoundPlay();			//播放音频线程	
	LinkedList<byte[]> pipe = new LinkedList<byte[]>();;	//音频数据队列
	private boolean running = true;	//录音标志
	@Override
	public void onTestInit()
	{
		this.setTitle("Main Mic - Reciver Loopback");
		this.setInfo("Please input sound to the main mic,then listen from reviver.");
		//设置播放模式 音频输出通道为听筒
		am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		am.setMode(AudioManager.MODE_IN_CALL);
	}
	@Override
	public void onStart()
	{
		super.onStart();
		//启动线程
		th_record.start();
		th_play.start();
	}
	@Override
	public void onTestStop() 
	{
		super.onTestStop();
		running = false;
		if(th_record != null){
			th_record.recorder.stop();
			th_record = null;
		}
		if(th_play != null){
			th_play.tracker.stop();
			th_play.tracker.release();
			th_play = null;
		}
		am.setMode(AudioManager.MODE_NORMAL);
	}
	class SoundRecord extends Thread
	{
		AudioRecord recorder;	//录制音频对象
		
		@Override
		public void run()
		{
			// AudioRecord 得到录制最小缓冲区的大小
			int buffer_size = AudioRecord.getMinBufferSize(
					sample_rate,							//采样率
					AudioFormat.CHANNEL_IN_MONO,			//采样通道配置 
					encoding);								//格式
			
			// 实例化录音音频对象
			recorder = new AudioRecord(
					MediaRecorder.AudioSource.MIC,					//声音源
					sample_rate,									//采样率
					AudioFormat.CHANNEL_IN_MONO,					//通道配置
					encoding,										//编码格式
					buffer_size);									//缓存大小
			
			recorder.startRecording();
			
			while (running){
				byte[] data = new byte[buffer_size];
				recorder.read(data, 0, buffer_size);	
				if (pipe.size() >= 2){
					pipe.removeFirst();
				}
				pipe.add(data);
			}
		}
	}
	class SoundPlay extends Thread
	{
		AudioTrack tracker;		//播放音频对象	
		
		@Override
		public void run()
		{		
			// AudioTrack 得到播放最小缓冲区的大小
			int buffer_size = AudioTrack.getMinBufferSize(
					sample_rate,
					AudioFormat.CHANNEL_OUT_MONO,
					encoding);
			
			// 实例化播放音频对象
			tracker = new AudioTrack(
					AudioManager.STREAM_VOICE_CALL,//.STREAM_MUSIC, 	//输出设备
					sample_rate,										//采样率
					AudioFormat.CHANNEL_OUT_MONO,						//通道配置
					encoding, 											//编码格式
					buffer_size,										//输出缓冲
					AudioTrack.MODE_STREAM);							//模式
		
			tracker.play();
			while (running){
				try{
					byte[] data = new byte[buffer_size];
					data = pipe.getFirst();
					tracker.write(data, 0, data.length);
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}
	}

}