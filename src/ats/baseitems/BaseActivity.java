package ats.baseitems;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.WindowManager;

/*
 * Dont use Activity,the BaseActivity instead.
 * BaseActivity contain the global all config of activitys.
 */

@SuppressWarnings("deprecation")
public class BaseActivity extends Activity {
	
	TheApp app;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		app= (TheApp)getApplication();
		app.addThisInstance(this);
		super.onCreate(savedInstanceState);
		// Disable the Lock of Screen.
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		unlockScreen();
	}
	@SuppressLint("Wakelock")
	private void unlockScreen() {
		// TODO Auto-generated method stub
		KeyguardManager km = (KeyguardManager)this.getSystemService(Context.KEYGUARD_SERVICE);
		if(km.inKeyguardRestrictedInputMode()){

			PowerManager mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE); 
			KeyguardLock mKeyguardLock = km.newKeyguardLock("");  
			PowerManager.WakeLock mWakeLock = mPowerManager.newWakeLock 
	        (PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "Tag"); 
	        
		    mWakeLock.acquire(); 
	        //mKeyguardLock = mKeyguardManager.newKeyguardLock(""); 
	        mKeyguardLock.disableKeyguard(); 
		}
	}
	public static String shell_exec(String cmd)
			throws IOException
	{
		return shell_exec(cmd,"/");
	}
	public static String shell_exec(String cmd,String wd)
			throws IOException
	{
		return shell_exec(cmd.split("\\s+"),wd);
		
	}

	public static synchronized String shell_exec(String[] cmd, String workdirectory)
			throws IOException
	{
		StringBuffer result = new StringBuffer();
		try {
			// ��������ϵͳ���̣�Ҳ������Runtime.exec()������
			// Runtime runtime = Runtime.getRuntime();
			// Process proc = runtime.exec(cmd);
			// InputStream inputstream = proc.getInputStream();
			ProcessBuilder builder = new ProcessBuilder(cmd);
			InputStream in = null;
			// ����һ��·��������·���˾Ͳ�һ����Ҫ��
			if (workdirectory != null) {
				// ���ù���Ŀ¼��ͬ�ϣ�
				builder.directory(new File(workdirectory));
				// �ϲ���׼����ͱ�׼���
				builder.redirectErrorStream(true);
				// ����һ���½���
				Process process = builder.start();
				// ��ȡ���̱�׼�����
				in = process.getInputStream();
				byte[] re = new byte[1024];
				while (in.read(re) != -1) {
					result = result.append(new String(re));
				}
			}
			// �ر�������
			if (in != null) {
				in.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result.toString();
	}
}
