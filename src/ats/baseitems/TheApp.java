package ats.baseitems;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class TheApp extends Application {

	@SuppressLint("SdCardPath")
	final String default_work_dir = "/sdcard/Android/data/";
	final String home_dir_name = "ats-autommi/";
	final String result_file_name = "result.txt";

	private String work_dir = "";

	private File main_dir;
	private File result_file;

	ArrayList<TestResult> result_list = new ArrayList<TestResult>();

	//当前启动的Activity 实例,用于 init 清除这些实例 
	List<Activity> activity_list = new LinkedList<Activity>();
	
	class TestResult {
		public TestResult(String key, String value) {
			this.key = key;
			this.value = value;
		}

		String key;
		String value;
	}

	public void InitWorkDir(String path) {
		work_dir = path;
	}

	public String getWorkDir() {
		return work_dir + home_dir_name;
	}
	
	@SuppressLint("SdCardPath")
	public TheApp() {
		if (work_dir.isEmpty())
			InitWorkDir(default_work_dir);

		String work_dir_path = work_dir + home_dir_name;

		main_dir = new File(work_dir_path);
		if (main_dir.exists() == false) {
			if (main_dir.mkdirs() == false)
				;
			// 如果无法创建,
		}
		main_dir.setReadable(true, false);
		main_dir.setWritable(true, false);

		String result_file_path = work_dir_path + result_file_name;
		result_file = new File(result_file_path);
		if (result_file.exists()) {
			result_file.delete();
		}
		try {
			result_file.createNewFile();
			result_file.setReadable(true, false);
			result_file.setWritable(true, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	@Override
	public void onCreate() 
	{
		//启动蓝牙
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if(adapter.isEnabled()==false){
			try{
				adapter.enable();
			}catch(Exception e){};
		};
		//启动wifi
		WifiManager wm = (WifiManager)getSystemService(Context.WIFI_SERVICE);
	    if(wm.isWifiEnabled()==false){
	    	try{
	    		wm.setWifiEnabled(true);
	    	}catch(Exception e){}
	    }
    }
	synchronized public void RecordResult(String key, String value) {
		// TODO Auto-generated method stub
		for(TestResult i : result_list){
			if(i.key.equals(key)){
				i.key = key;
				i.value = value;
				return;
			}
		}
		result_list.add(new TestResult(key, value));
	}

	synchronized public void SyncResult() {
		// TODO Auto-generated method stub
		StringBuilder s = new StringBuilder();
		s.append("[test_result]\r\n");
		for (TestResult i : result_list) {
			s.append(i.key + "=" + i.value + "\r\n");
		}
		try {
			FileOutputStream o = new FileOutputStream(result_file);
			o.write(s.toString().getBytes("ASCII"), 0, s.length());
			o.flush();
			o.close();
			o = null;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addThisInstance(BaseActivity baseActivity) {
		// TODO Auto-generated method stub
		activity_list.add(baseActivity);
	}

	public void clearTestResult() {
		// TODO Auto-generated method stub
		this.result_list.clear();
		this.SyncResult();
	}

	public void RecordResultString(String string, String version) {
		// TODO Auto-generated method stub
		RecordResult(string,"\""+version+"\"");
	}



}
