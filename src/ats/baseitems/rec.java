package ats.baseitems;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class rec extends AudioActivity
{
	static final int sample_rate = 8000;
	static final int encoding = AudioFormat.ENCODING_PCM_16BIT;
	
	AudioManager am;
	//SoundRecord th_record = new SoundRecord();		//录制音频线程
	SoundPlay th_play = new SoundPlay();			//播放音频线程	
	//LinkedList<byte[]> pipe = new LinkedList<byte[]>();;	//音频数据队列
	private boolean running = true;	//录音标志
	@Override
	public void onTestInit()
	{
		this.setTitle("Reciver Test");
		this.setInfo("Reciver output 1KHz sine wave.");
		//设置播放模式 音频输出通道为听筒
		am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		am.setMode(AudioManager.MODE_IN_CALL);	
	}
	@Override
	public void onStart()
	{
		super.onStart();
		//启动线程
		//th_record.start();
		th_play.start();
	}
	@Override
	public void onTestStop() 
	{
		super.onTestStop();
		
		running = false;
		if(th_play != null){
			th_play.tracker.stop();
			th_play.tracker.release();
			th_play = null;
		}
		am.setMode(AudioManager.MODE_NORMAL);	
	}
	
	class SoundPlay extends Thread
	{
		AudioTrack tracker;		//播放音频对象	
		
		@Override
		public void run()
		{		
			// AudioTrack 得到播放最小缓冲区的大小
			int read_buffer_size = AudioTrack.getMinBufferSize(
					sample_rate,
					AudioFormat.CHANNEL_OUT_MONO,
					encoding);
			
			// 实例化播放音频对象
			tracker = new AudioTrack(
					AudioManager.STREAM_VOICE_CALL,//.STREAM_MUSIC, 	//输出设备
					sample_rate,										//采样率
					AudioFormat.CHANNEL_OUT_MONO,						//通道配置
					encoding, 											//编码格式
					read_buffer_size * 64,								//输出缓冲
					AudioTrack.MODE_STREAM);							//模式
		
			
			
			//if (sample_rate!=AudioFormat.ENCODING_PCM_16BIT)
				//continue;
			double out_frequen = 1000.0;
			//单个音频周期采样数
			double sample_num_in_one_cycle = 
					(double)sample_rate/(double)out_frequen;
			//播放长度,s
			int play_len = 60;
			int buffer_size = sample_rate * play_len;
			byte[] data = new byte[buffer_size];
			//缓存容纳采样总数:
			int sample_sum = buffer_size/2;
			
			for(int i=0;i<sample_sum;i++){
				short n = (short)(32767 * Math.sin(
						(double)i*2*Math.PI/sample_num_in_one_cycle));
				data[i*2] = (byte)(n/256);
				data[i*2+1] = (byte)(n%256);
			}
			
			tracker.play();
			try{
				//data = pipe.getFirst();
				int frame_size = read_buffer_size * 64;
				if(data.length<frame_size)
					frame_size = data.length;
				for(int i=0;i<data.length;i+= frame_size){
					tracker.write(data, 0, frame_size);
				}
				//dont flush !!!
				//tracker.flush();
				
			} catch (Exception e){
				e.printStackTrace();
			}
			while (running){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}