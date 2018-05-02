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
	SoundRecord th_record = new SoundRecord();		//¼����Ƶ�߳�
	SoundPlay th_play = new SoundPlay();			//������Ƶ�߳�	
	LinkedList<byte[]> pipe = new LinkedList<byte[]>();;	//��Ƶ���ݶ���
	private boolean running = true;	//¼����־
	@Override
	public void onTestInit()
	{
		this.setTitle("Main Mic - Reciver Loopback");
		this.setInfo("Please input sound to the main mic,then listen from reviver.");
		//���ò���ģʽ ��Ƶ���ͨ��Ϊ��Ͳ
		am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		am.setMode(AudioManager.MODE_IN_CALL);
	}
	@Override
	public void onStart()
	{
		super.onStart();
		//�����߳�
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
		AudioRecord recorder;	//¼����Ƶ����
		
		@Override
		public void run()
		{
			// AudioRecord �õ�¼����С�������Ĵ�С
			int buffer_size = AudioRecord.getMinBufferSize(
					sample_rate,							//������
					AudioFormat.CHANNEL_IN_MONO,			//����ͨ������ 
					encoding);								//��ʽ
			
			// ʵ����¼����Ƶ����
			recorder = new AudioRecord(
					MediaRecorder.AudioSource.MIC,					//����Դ
					sample_rate,									//������
					AudioFormat.CHANNEL_IN_MONO,					//ͨ������
					encoding,										//�����ʽ
					buffer_size);									//�����С
			
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
		AudioTrack tracker;		//������Ƶ����	
		
		@Override
		public void run()
		{		
			// AudioTrack �õ�������С�������Ĵ�С
			int buffer_size = AudioTrack.getMinBufferSize(
					sample_rate,
					AudioFormat.CHANNEL_OUT_MONO,
					encoding);
			
			// ʵ����������Ƶ����
			tracker = new AudioTrack(
					AudioManager.STREAM_VOICE_CALL,//.STREAM_MUSIC, 	//����豸
					sample_rate,										//������
					AudioFormat.CHANNEL_OUT_MONO,						//ͨ������
					encoding, 											//�����ʽ
					buffer_size,										//�������
					AudioTrack.MODE_STREAM);							//ģʽ
		
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