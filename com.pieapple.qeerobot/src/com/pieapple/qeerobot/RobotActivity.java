package com.pieapple.qeerobot;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

//import com.joshclemm.android.tabs.R;




//import com.blueserial.MainActivity.DisConnectBT;



import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class RobotActivity extends Activity implements OnClickListener, OnTouchListener {


	
	
	public View mForwardBtn;
	public View mBackwardBtn;
	boolean mIsOnForward = false;
	boolean mIsOnBackward = false;
	public CountDownTimer mForwardTimer;
	public CountDownTimer mBackwardTimer;
	
	
	
	
	
	
	private static final String TAG = "RobotActivity";
	private int mMaxChars = 50000;//Default
	public UUID mDeviceUUID;
	public BluetoothSocket mBTSocket;
	private ReadInput mReadThread = null;

	private boolean mIsUserInitiatedDisconnect = false;
	private boolean mIsBluetoothConnected = false;
	private BluetoothDevice mDevice;
	private ProgressDialog progressDialog;
	
	
	static public BluetoothDevice sDevice;
	static public String sDeviceUUID;
	static public int sMaxChars;
	static private RobotActivity sRobotActivity;
	
	static public RobotActivity getRobotActivity() {
		return sRobotActivity;
	}
	static public void refreshIntentData () {
		 RobotActivity act = getRobotActivity();
		 if(act == null) {
			 Log.i("DEBUG_TAG", "RobotActivity is null");
		 }
		 else {
			 act.mDevice = sDevice;
			 act.mDeviceUUID = UUID.fromString(sDeviceUUID);
			 act.mMaxChars = sMaxChars;
		 }
		
	}
	static public void saveIntentData(BluetoothDevice device, String deviceUUID, int maxChars ){
		 sDevice = device;
		 sDeviceUUID = deviceUUID;
		 sMaxChars = maxChars;
		 refreshIntentData();
	}
	
	public void bluetoothDisconnect() {
		mIsUserInitiatedDisconnect = true;
		new DisConnectBT().execute();		
	}
	


	
	public static void requestStartBluetoothConnectProcess() {
		RobotActivity act = getRobotActivity();
		if(act!= null) {
			Log.i("DEBUG_TAG","getRobotActivity() is work in requestStartBluetoothConnectProcess" );
			act.tryStartBluetoothConnectProcess22();
		}
		else {
		}
	}
	
	public void tryStartBluetoothConnectProcess22() {
		if (mBTSocket == null || !mIsBluetoothConnected) {
			new ConnectBT().execute();
			Log.i("DEBUG_TAG","RobotActivity onResume()");
		}
		Log.d(TAG, "tryStartBluetoothConnectProcess22");		
		
	}
	public void tryStartBluetoothConnectProcess() {
		if (mBTSocket == null || !mIsBluetoothConnected) {
			//new ConnectBT().execute();
			Log.i("DEBUG_TAG","RobotActivity onResume()");
		}
		Log.d(TAG, "Resumed");		
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_robot);
		
		RobotActivity.sRobotActivity = this; 
		findViewById(R.id.back_to_tab_btn).setOnClickListener(this);
		mBackwardBtn = findViewById(R.id.backward_move_btn);
		mForwardBtn = findViewById(R.id.forward_move_btn);
		
		setRobotCommand();
	}
	
	
	public void setRobotCommand() {
		this.findViewById(R.id.head_left_btn).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//CustomTabActivity.getTabActivity().sendSpp("1L");				
				CustomTabActivity.getTabActivity().sendSppByHexStr("FFAA05300081B6");
				
			}
		});
		this.findViewById(R.id.head_right_btn).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//CustomTabActivity.getTabActivity().sendSpp("1R");
				CustomTabActivity.getTabActivity().sendSppByHexStr("FFAA0530004176");
			}
		});
		
		this.findViewById(R.id.left_hand_up_btn).setOnClickListener(new OnClickListener(){
			
			
			@Override
			public void onClick(View arg0) {
				Log.i("DEBUG_TAG","left_hand_up_btn click ");
				//CustomTabActivity.getTabActivity().sendSpp("2U");
				CustomTabActivity.getTabActivity().sendSppByHexStr("FFAA0530400176");
			}
		});
		this.findViewById(R.id.left_hand_down_btn).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Log.i("DEBUG_TAG","left_hand_down_btn click ");
				//CustomTabActivity.getTabActivity().sendSpp("2D");				
				CustomTabActivity.getTabActivity().sendSppByHexStr("FFAA05308001B6");
			}
		});

		
		this.findViewById(R.id.right_hand_up_btn).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Log.i("DEBUG_TAG","right_hand_up_btn click ");
				//CustomTabActivity.getTabActivity().sendSpp("3U");
				CustomTabActivity.getTabActivity().sendSppByHexStr("FFAA0530100146");
			}
		});
		this.findViewById(R.id.right_hand_down_btn).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//CustomTabActivity.getTabActivity().sendSpp("3D");
				CustomTabActivity.getTabActivity().sendSppByHexStr("FFAA0530200156");
			}
		});
		

		
/*
		this.findViewById(R.id.backward_move_btn).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				CustomTabActivity.getTabActivity().sendSpp("BB");				
			}
		});
*/
		
		this.findViewById(R.id.backward_move_btn).setOnTouchListener(this);
/*		
		this.findViewById(R.id.forward_move_btn).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				CustomTabActivity.getTabActivity().sendSpp("FF");				
			}
		});
*/
		this.findViewById(R.id.forward_move_btn).setOnTouchListener(this);
		
		

		this.findViewById(R.id.turn_left_btn).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//CustomTabActivity.getTabActivity().sendSpp("TL");
				CustomTabActivity.getTabActivity().sendSppByHexStr("FFAA053004013A");
			}
		});
		this.findViewById(R.id.turn_right_btn).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//CustomTabActivity.getTabActivity().sendSpp("TR");
				CustomTabActivity.getTabActivity().sendSppByHexStr("FFAA0530010137");
			}
		});
		
	}
	
	
	
	

	@Override
	public void onClick(View v) {
		int vid = v.getId();
		switch(vid) {
		case R.id.back_to_tab_btn:
			Log.i("DEBUG_TAG", "back_to_tab_btn click");
			bluetoothDisconnect();			
			CustomTabActivity.getTabActivity().showTabActivity();
			break;
		}
	}
	
	
	

	private class ReadInput implements Runnable {

		private boolean bStop = false;
		private Thread t;

		public ReadInput() {
			t = new Thread(this, "Input Thread");
			t.start();
		}

		public boolean isRunning() {
			return t.isAlive();
		}

		@Override
		public void run() {
			InputStream inputStream;

			try {
				inputStream = mBTSocket.getInputStream();
				while (!bStop) {
					byte[] buffer = new byte[256];
					if (inputStream.available() > 0) {
						inputStream.read(buffer);
						int i = 0;
						/*
						 * This is needed because new String(buffer) is taking the entire buffer i.e. 256 chars on Android 2.3.4 http://stackoverflow.com/a/8843462/1287554
						 */
						for (i = 0; i < buffer.length && buffer[i] != 0; i++) {
						}
						final String strInput = new String(buffer, 0, i);

						/*
						 * If checked then receive text, better design would probably be to stop thread if unchecked and free resources, but this is a quick fix
						 */

						
						/*
						 * presume there is no chkReceiveText by milochen
						if (chkReceiveText.isChecked()) {
							mTxtReceive.post(new Runnable() {
								@Override
								public void run() {
									mTxtReceive.append(strInput);
									//Uncomment below for testing
									//mTxtReceive.append("\n");
									//mTxtReceive.append("Chars: " + strInput.length() + " Lines: " + mTxtReceive.getLineCount() + "\n");
									
									int txtLength = mTxtReceive.getEditableText().length();  
									if(txtLength > mMaxChars){
										mTxtReceive.getEditableText().delete(0, txtLength - mMaxChars);
									}

									if (chkScroll.isChecked()) { // Scroll only if this is checked
										scrollView.post(new Runnable() { // Snippet from http://stackoverflow.com/a/4612082/1287554
													@Override
													public void run() {
														scrollView.fullScroll(View.FOCUS_DOWN);
													}
												});
									}
								}
							});
						}
						*/

					}
					Thread.sleep(500);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public void stop() {
			bStop = true;
		}

	}

	private class DisConnectBT extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(Void... params) {

			if (mReadThread != null) {
				mReadThread.stop();
				while (mReadThread.isRunning())
					; // Wait until it stops
				mReadThread = null;

			}

			try {
				mBTSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			mIsBluetoothConnected = false;
			if (mIsUserInitiatedDisconnect) {
				finish();
			}
		}

	}

	private void msg(String s) {
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onPause() {
		if (mBTSocket != null && mIsBluetoothConnected) {
			new DisConnectBT().execute();
		}
		Log.d(TAG, "Paused");
		super.onPause();
	}

	
	
	
	@Override
	protected void onResume() {
		this.tryStartBluetoothConnectProcess(); 
		/*
		if (mBTSocket == null || !mIsBluetoothConnected) {
			//new ConnectBT().execute();
			Log.i("DEBUG_TAG","RobotActivity onResume()");
		}
		*/
		Log.d(TAG, "Resumed");
		super.onResume();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "Stopped");
		super.onStop();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	private class ConnectBT extends AsyncTask<Void, Void, Void> {
		private boolean mConnectSuccessful = true;

		@Override
		protected void onPreExecute() {
			//progressDialog = ProgressDialog.show(MainActivity.this, "Hold on", "Connecting");// http://stackoverflow.com/a/11130220/1287554
			progressDialog = ProgressDialog.show(RobotActivity.this, "Hold on", "Connecting");// http://stackoverflow.com/a/11130220/1287554
		}

		@Override
		protected Void doInBackground(Void... devices) {
			
			//add by milochen
			if(mDevice == null) {
				RobotActivity.refreshIntentData();
			}
			
			try {
				if (mBTSocket == null || !mIsBluetoothConnected) {
					mBTSocket = mDevice.createInsecureRfcommSocketToServiceRecord(mDeviceUUID);
					BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
					mBTSocket.connect();
				}
			} catch (IOException e) {
				// Unable to connect to device
				e.printStackTrace();
				mConnectSuccessful = false;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if (!mConnectSuccessful) {
				//Toast.makeText(getApplicationContext(), "Could not connect to device. Is it a Serial device? Also check if the UUID is correct in the settings", Toast.LENGTH_LONG).show();
				Toast.makeText(getApplicationContext(), "請先確認 Qee Robot 已通電，並至設定 Settings 頁面，對 Qee Robot 重新作藍芽配對 Bluetooth Pairing", Toast.LENGTH_LONG).show();
				finish();
			} else {
				msg("Connected to device");
				mIsBluetoothConnected = true;
				mReadThread = new ReadInput(); // Kick off input reader
			}

			progressDialog.dismiss();
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(v == mForwardBtn) {
			if(event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.i("DEBUG_TAG", "mForwardBtn down");
				mIsOnForward = true;
				//mForwardTimer = new CountDownTimer(100, 100) {
				mForwardTimer = new CountDownTimer(60, 60) {
				
					@Override
					public void onTick(long millisUntilFinished) {}
					@Override
					public void onFinish() {
						//Log.i("DEBUG_TAG", "mForwardTimer count");
						if(mIsOnForward == true ) {
							//CustomTabActivity.getTabActivity().sendSpp("FF");
							CustomTabActivity.getTabActivity().sendSppByHexStr("FFAA053005013B");
							mForwardTimer.start();
						}
					}
				};
				mForwardTimer.start();
				
				//CustomTabActivity.getTabActivity().sendSpp("FF");
				CustomTabActivity.getTabActivity().sendSppByHexStr("FFAA053005013B");
				
			}
			else if(event.getAction() == MotionEvent.ACTION_UP) {
				Log.i("DEBUG_TAG", "mForwardBtn up");
				mIsOnForward = false; 
			}
		}
		else if(v == mBackwardBtn) {
			if(event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.i("DEBUG_TAG", "mBackwardBtn down");
				mIsOnBackward = true;
				//mBackwardTimer = new CountDownTimer(100, 100) {
				mBackwardTimer = new CountDownTimer(60, 60) {	
					@Override
					public void onTick(long millisUntilFinished) {}
					@Override
					public void onFinish() {
						//Log.i("DEBUG_TAG", "mBackwardTimer count");
						if(mIsOnBackward == true ) {
							//CustomTabActivity.getTabActivity().sendSpp("BB");
							CustomTabActivity.getTabActivity().sendSppByHexStr("FFAA05300A0140");
							mBackwardTimer.start();
						}
					}
				};
				mBackwardTimer.start();
				
				//CustomTabActivity.getTabActivity().sendSpp("BB");
				CustomTabActivity.getTabActivity().sendSppByHexStr("FFAA05300A0140");
				
			}
			else if(event.getAction() == MotionEvent.ACTION_UP) {
				Log.i("DEBUG_TAG", "mBackwardBtn up");
				mIsOnBackward = false;
			}			
		}
		return false;
	}	
	
	
	
	
	
	/*
			this.findViewById(R.id.forward_move_btn).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				CustomTabActivity.getTabActivity().sendSpp("FF");				
			}
		});

	*/
}
