package ats.baseitems;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class bcam extends BaseActivity implements SurfaceHolder.Callback,
		AutoFocusCallback, PreviewCallback, PictureCallback {

	int CameraId = 0;
	String recoreName = "bcam";
	Camera cam = null;
	SurfaceHolder holder;
	SurfaceView v;
	private boolean pause_flag = true;
	private boolean preview_flag = false;

	// =====================================================================

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle("Back Camera Test");

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);

		v = new SurfaceView(this);

		holder = v.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 设置类型

		setContentView(v);

		CameraId = 0; // 主相机
		
		//初始设置 
		this.recordResult("device", "UNKNOW");
		this.recordResult("focus", "UNKNOW");
		this.recordResult("noise", 0 + "");
		app.SyncResult();
	}

	@SuppressLint("SdCardPath")
	@Override
	public void onStart() {

		File file = new File("/sdcard/" + recoreName + ".png");
		file.delete();

		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		pause_flag = false;
	}

	@Override
	public void onPause() {
		pause_flag = true;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stopPreview();
		super.onPause();
	}

	@Override
	public void onStop() {
		pause_flag = true;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stopPreview();
		super.onStop();
	}

	// ==================================================================================
	synchronized private void stopPreview() {
		if (preview_flag == true) {
			preview_flag = false;
			if (cam != null) {
				cam.stopPreview();
				cam.setPreviewCallback(null);
				cam.release();
				cam = null;
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void startPreview() {

		ensureCameraDevice();

		if (cam != null) {

			cam.setPreviewCallback(this);

			cam.startPreview();

			Camera.Parameters params = cam.getParameters();
			params.setPictureFormat(PixelFormat.JPEG);
			params.setPreviewSize(640, 480);
			params.setPictureSize(640, 480);
			List<Integer> rate = params.getSupportedPreviewFrameRates();
			params.setPreviewFrameRate(Collections.min(rate));

			cam.setParameters(params);

			preview_flag = true;

			this.recordResult("device", "PASS");
		} else {
			preview_flag = false;
			this.recordResult("device", "FAIL");
		}

		//app.SyncResult();
	}

	@SuppressLint("NewApi")
	private void ensureCameraDevice() {
		if (cam == null) {
			int n = 10;
			do {
				try {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					cam = Camera.open(CameraId);
					if (cam != null) {
						setCameraDisplayOrientation(CameraId, cam);
						cam.setPreviewDisplay(holder);
						return;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} while (cam == null && --n > 0);
		}
	}

	@SuppressLint("NewApi")
	private void setCameraDisplayOrientation(int cameraId,
			android.hardware.Camera camera) {
		android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(cameraId, info);
		int rotation = getWindowManager().getDefaultDisplay().getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}
		int result;
		if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else {
			// back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		camera.setDisplayOrientation(result);
	}

	// ================================================================================
	@Override
	synchronized public void surfaceDestroyed(SurfaceHolder arg0) {
		stopPreview();
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

		startPreview();

		if (preview_flag == true && cam != null) {

			focus_success = false;
			focus_count = 0;
			cam.autoFocus(this);
		}

	}

	// ======================================================================

	boolean focus_success = false;
	int focus_count = 0;
	int focus_count_max = 5;

	@Override
	synchronized public void onAutoFocus(boolean ok, Camera cam) {
		if (pause_flag == true || preview_flag == false)
			return;
		if (ok == false && focus_count < focus_count_max) {
			focus_count++;
			cam.autoFocus(this);
			return;
		}
		if (ok) {
			this.recordResult("focus", "PASS");
		} else {
			this.recordResult("focus", "FAIL");
		}
		//app.SyncResult();
		cam.takePicture(null, null, this);

	}

	// ===========================================================================
	ArrayList<byte[]> photos = new ArrayList<byte[]>();
	boolean cacle_complete = false;

	@Override
	synchronized public void onPreviewFrame(byte[] data, Camera cam) {
		if (pause_flag == true || preview_flag == false)
			return;
		if (data != null && false == cacle_complete) {
			if (photos.size() < 2) {
				byte[] pic = new byte[data.length];
				for (int i = 0; i < data.length; i++) {
					pic[i] = data[i];
				}
				photos.add(pic);
			} else {
				if (photos.get(0).length == photos.get(1).length) {
					try{
					cacle_complete = cacle_noise(photos);
					}catch(Exception e){
						e.printStackTrace();
					}
				} else {
					photos.clear();
				}
			}
		}

	}

	volatile boolean noise_calc_complete = false;
	private boolean cacle_noise(ArrayList<byte[]> photos) {
		if (photos.size() < 2)
			return false;
		// cacle diff & zero
		byte[] a = photos.get(0);
		byte[] b = photos.get(1);
		int n = a.length;
		if(n==0)
			return false;
		byte[] res = new byte[n];
		boolean calc_en = false;
		int zero_count = 0;
		int zero_max = 0;
		for (int i = 0; i < n; i++) {
			res[i] = (byte) (a[i] - b[i]);
			if (res[i] < 0)
				res[i] = (byte) -res[i];

			if (res[i] == 0) {
				if (calc_en == false)
					zero_count = 0;
				calc_en = true;
			} else {
				calc_en = false;
			}
			if (calc_en) {
				zero_count++;
				if (zero_count > zero_max) {
					zero_max = zero_count;
				}
			}
		}
		int noise = (n - zero_max) * 100 / n;
		this.recordResult("noise", noise + "");
		//app.SyncResult();
		this.noise_calc_complete = true;
		
		if(take_photo_complete || noise_calc_complete)
			app.SyncResult();
		return true;
	}

	// ==========================================================================
	private void recordResult(String key, String value) {
		app.RecordResult(recoreName + "_" + key, value);
	}

	volatile boolean take_photo_complete = false;
	@Override
	synchronized public void onPictureTaken(byte[] data, Camera cam) {
		if (pause_flag == true || preview_flag == false)
			return;
		try {
			Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
			String path = app.getWorkDir();
			File file = new File(path + recoreName + ".png");
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
			bos.flush();
			bos.close();
			bos = null;
			file = null;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		cam.startPreview();

		this.take_photo_complete = true;
		
		if(take_photo_complete || noise_calc_complete)
			app.SyncResult();
		
	}

	
	// static public void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width,
		// int height) {
		// final int frameSize = width * height;
		//
		// for (int j = 0, yp = 0; j < height; j++) {
		// int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
		// for (int i = 0; i < width; i++, yp++) {
		// int y = (0xff & ((int) yuv420sp[yp])) - 16;
		// if (y < 0)
		// y = 0;
		// if ((i & 1) == 0) {
		// v = (0xff & yuv420sp[uvp++]) - 128;
		// u = (0xff & yuv420sp[uvp++]) - 128;
		// }
		//
		// int y1192 = 1192 * y;
		// int r = (y1192 + 1634 * v);
		// int g = (y1192 - 833 * v - 400 * u);
		// int b = (y1192 + 2066 * u);
		//
		// if (r < 0)
		// r = 0;
		// else if (r > 262143)
		// r = 262143;
		// if (g < 0)
		// g = 0;
		// else if (g > 262143)
		// g = 262143;
		// if (b < 0)
		// b = 0;
		// else if (b > 262143)
		// b = 262143;
		//
		// rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000)
		// | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
		// }
		// }
		// }
}