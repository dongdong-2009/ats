package ats.baseitems;

import android.os.StatFs;

class SDStat{

	String path;
	public SDStat(String string) {
		path = string;
	}
	private StatFs getStatFs() {
		try {
			return new StatFs(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public float getTotalSize() {
		StatFs fs = getStatFs();
		if(fs==null)
			return 0;
		return fs.getBlockCount()*(fs.getBlockSize()/(1024f*1024f));
	}
	@SuppressWarnings("deprecation")
	public float getFreeSize() {
		StatFs fs = getStatFs();
		if(fs==null)
			return 0;
		return fs.getAvailableBlocks()*(fs.getBlockSize()/(1024f*1024f));
	}
	
}

public class sd extends TestItemActivity {

	@Override
	public void onTestInit() {
		this.setTitle("SD Card Test");

		String info="";

		SDStat[] sd_list = new SDStat[3];
		sd_list[0] = new SDStat("/mnt/sdcard");
		sd_list[1] = new SDStat("/mnt/sdcard1");
		sd_list[2] = new SDStat("/mnt/sdcard2");
		
		for(int i=0;i<sd_list.length;i++){
			
			info += "-------------------\n";
			
			long size = (long) sd_list[i].getTotalSize();
			if(size>0){
				info += "SD¿¨"+i+"×´Ì¬: ´æÔÚ\n";
				info += " ->¹ÒÔØÄ¿Â¼: "+sd_list[i].path+"\n";
				long free = (long) sd_list[i].getFreeSize();
				info += " ->ÈÝÁ¿: "+ size +"MB\n"
					+ " ->Ê£Óà¿Õ¼ä: "+ free +"MB\n";
			}else{
				info += "SD¿¨"+i+"×´Ì¬: ²»´æÔÚ\n";
			}
		}
		
		info += "=========================\n";
		
		for(int i=0;i<sd_list.length;i++){
			
			info += "-------------------\n";
			
			String state_key = "sd_" + i + "_state";
			String mount_key = "sd_" + i + "_mount_directory";
			String size_key = "sd_" + i + "_size";
			String free_key = "sd_" + i + "_free";
			
			long size = (long) sd_list[i].getTotalSize();
			
			if(size>0){
				long free = (long) sd_list[i].getFreeSize();

				app.RecordResultString(state_key, "exist");
				app.RecordResultString(mount_key, sd_list[i].path);
				app.RecordResult(size_key, size+"");
				app.RecordResult(free_key, free+"");
				
				info += state_key + "=\"exist\"\n";
				info += mount_key + "=\"" + sd_list[i].path + "\"\n";
				info += size_key + "=" + size + "\n";
				info += free_key + "=" + free + "\n";
				
			}else{
				app.RecordResultString(state_key, "non-exist");
				app.RecordResultString(mount_key, "");
				app.RecordResult(size_key, "");
				app.RecordResult(free_key, "");
				
				info += state_key + "=\"non-exist\"\n";
				info += mount_key + "=\"\"\n";
				info += size_key + "=0\n";
				info += free_key + "=0\n";
				
			}
		}
		
		
		addInfo(info);
		app.SyncResult();
		
	}

}
