package com.pieapple.qeerobot;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

//import com.joshclemm.android.tabs.R;





import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;

public class CustomTabActivity extends TabActivity implements OnClickListener{

	
	//Bluetooth code
	private BluetoothAdapter mBTAdapter;
	private static final int BT_ENABLE_REQUEST = 10; // This is the code we use for BT Enable
	private static final int SETTINGS = 20;

	private UUID mDeviceUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Standard SPP UUID
	// (http://developer.android.com/reference/android/bluetooth/BluetoothDevice.html#createInsecureRfcommSocketToServiceRecord%28java.util.UUID%29)

	private int mBufferSize = 50000; //Default
	public static final String DEVICE_EXTRA = "com.blueserial.SOCKET";
	public static final String DEVICE_UUID = "com.blueserial.uuid";
	private static final String DEVICE_LIST = "com.blueserial.devicelist";
	private static final String DEVICE_LIST_SELECTED = "com.blueserial.devicelistselected";
	public static final String BUFFER_SIZE = "com.blueserial.buffersize";
	private static final String TAG = "BlueTest5-Homescreen";
	
	
	
	
// copy from RobotActivity
	private int mMaxChars = 50000;//Default
	//public UUID mDeviceUUID;
	public BluetoothSocket mBTSocket;
	private ReadInput mReadThread = null;

	private boolean mIsUserInitiatedDisconnect = false;
	private boolean mIsBluetoothConnected = false;
	private BluetoothDevice mDevice;
	private ProgressDialog progressDialog;
	
	
	static public BluetoothDevice sDevice;
	static public String sDeviceUUID;
	static public int sMaxChars;
//
	
	
	protected static CustomTabActivity sCustomTabActivity = null; 
	public static CustomTabActivity getTabActivity() {
		return sCustomTabActivity;
	}
	
	private TabHost mTabHost;

	private void setupTabHost() {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// construct the tabhost
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);		
		if(sCustomTabActivity == null) sCustomTabActivity = this;
		
		
        setContentView(R.layout.main);
        
        findViewById(R.id.hide_tabhost).setOnClickListener(this);
        findViewById(R.id.music_picker).setOnClickListener(this);
        findViewById(R.id.bluetooth_btn).setOnClickListener(this);
		setupTabHost();
		
		mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
		
	

		
		TabSpec tab1 = mTabHost.newTabSpec("Tab Qeerobot");
		tab1.setIndicator(
			createTabView(mTabHost.getContext(), "", R.drawable.tab_qeerobot_selector)				
		);
	
		
		tab1.setContent(new Intent(this,RobotActivity.class));
		mTabHost.addTab(tab1);
		
		
		
		TabSpec tab2 = mTabHost.newTabSpec("Tab Qeerobot");
		tab2.setIndicator(
			createTabView(mTabHost.getContext(), "", R.drawable.tab_dial_selector)				
		);
	
		tab2.setContent(new Intent(this,PhoneActivity.class));
		mTabHost.addTab(tab2);
		

		TabSpec tab3 = mTabHost.newTabSpec("Tab Music");
		tab3.setIndicator(				
			createTabView(mTabHost.getContext(), "", R.drawable.tab_music_selector)
		);
        //tab3.setContent(new Intent(this,TestTabActivity.class));
		tab3.setContent(new Intent(this, MediaListActivity.class));
		mTabHost.addTab(tab3);
		
		mTabHost.setCurrentTab(0);
		
		
		//add by milochen for development music player
		//mTabHost.setCurrentTab(2);
		//hideTabActivity();
		
		
	}
	
	public void bluetoothSearchProcess () {
		mBTAdapter = BluetoothAdapter.getDefaultAdapter();

		if (mBTAdapter == null) {
			Toast.makeText(getApplicationContext(), "Bluetooth not found", Toast.LENGTH_SHORT).show();
		} else if (!mBTAdapter.isEnabled()) {
			Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBT, BT_ENABLE_REQUEST);
		} else {
			new SearchDevices().execute();
		}
		
	}

	private void setupTab(final View view, final String tag) {
		View tabview = createTabView(mTabHost.getContext(), tag);
		TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(
			new TabContentFactory() {
				public View createTabContent(String tag) {
					return view;
				}
			}
		);
		mTabHost.addTab(setContent);
	}
	
	
	private void setupTab(final View view, final String tag, final int backgroundSelector) {
		View tabview = createTabView(mTabHost.getContext(), tag, backgroundSelector);

		
		TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(new TabContentFactory() {
			public View createTabContent(String tag) {return view;}
		});
		
		
		
		mTabHost.addTab(setContent);
		
		
		/*
		Intent call = new Intent(Intent.ACTION_DIAL);
		call.setData(Uri.parse("tel:" + findViewByid(R.id.textView4).getText());
		startActivity(call);
		*/
	}
	
	private static View createTabView(final Context context, final String text, final int backgroundSelector) {

		View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.tabsLayout);
		linearLayout.setBackground(	
				context.getResources().getDrawable(backgroundSelector)
		);
		tv.setText(text);
		return view;
	}
	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}

	@Override
	public void onClick(View v) {
		int vid = v.getId();
		View layout;
		switch(vid) {
		case R.id.hide_tabhost:
			this.showTabActivity();
			break;
		case R.id.music_picker:
			final Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
			intent2.setType("audio/*");
			startActivityForResult(intent2, 1);
			break;
		
		case R.id.bluetooth_btn:
			
			Log.i("DEBUG_TAG", "bluetooth_btn is click");
			bluetoothSearchProcess();			
			//this.hideTabActivity();
			break;			
		}
	}
	
	public void showTabActivity() {
		View layout;
		this.mTabHost.setVisibility(View.INVISIBLE);
		layout = findViewById(R.id.bluetooth_connect_layout);
		if(layout!=null) {
			layout.setVisibility(View.VISIBLE);
		}	
	}
	
	public void hideTabActivity() {
		View layout;
		layout = findViewById(R.id.bluetooth_connect_layout);
		if(layout!=null) {
			layout.setVisibility(View.GONE);
			this.mTabHost.setVisibility(View.VISIBLE);
		}		
	}
	
	
	
	
	
	private class SearchDevices extends AsyncTask<Void, Void, List<BluetoothDevice>> {

		@Override
		protected List<BluetoothDevice> doInBackground(Void... params) {
			Set<BluetoothDevice> pairedDevices = mBTAdapter.getBondedDevices();
			List<BluetoothDevice> listDevices = new ArrayList<BluetoothDevice>();
			for (BluetoothDevice device : pairedDevices) {
				//only Select Qee Robot device name 
				//if(device.getName().trim().equals( "Qee Robot")) {
				if(device.getName().trim().equals( "Qee Robot") || device.getName().trim().equals( "Choicee Qee Robot") ) {
				//change by milochen for debug when there is no Qee Robot
				//if(device.getName().trim().equals( "HC-06")) {
					Log.i("DEBUG_TAG", "ADD" + device.getName());
					listDevices.add(device);
					
					//BluetoothDevice device = ((MyAdapter) (mLstDevices.getAdapter())).getSelectedItem();
					

				}
			}
			if(!listDevices.isEmpty()) {
				
				final BluetoothDevice device = listDevices.get(0);
				/*
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				intent.putExtra(DEVICE_EXTRA, device);
				intent.putExtra(DEVICE_UUID, mDeviceUUID.toString());
				intent.putExtra(BUFFER_SIZE, mBufferSize);
				startActivity(intent);
				*/
				//RobotActivity.saveIntentData(device, mDeviceUUID.toString(), mBufferSize);
				CustomTabActivity.saveIntentData(device, mDeviceUUID.toString(), mBufferSize);
				
			}
			return listDevices;

		}

		@Override
		protected void onPostExecute(List<BluetoothDevice> listDevices) {
			super.onPostExecute(listDevices);
			if (listDevices.size() > 0) {
				
				//MyAdapter adapter = (MyAdapter) mLstDevices.getAdapter();
				//adapter.replaceItems(listDevices);
				
				hideTabActivity();
				//RobotActivity.requestStartBluetoothConnectProcess();
				CustomTabActivity.requestStartBluetoothConnectProcess();
			} else {
				//msg("No paired devices found, please pair your serial BT device and try again");
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case BT_ENABLE_REQUEST:
			if (resultCode == RESULT_OK) {
				Log.i("DEBUG_TAG", "Bluetooth Enabled successfully");
				new SearchDevices().execute();
			} else {
				Log.i("DEBUG_TAG", "Bluetooth couldn't be enabled");
			}

			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}	

	//milochen add for Chanceux's changed firmware 2015-04-25
	//The code is for changed command
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	public void sendSppByHexStr(String hexStr) {
		if (mBTSocket == null || !mIsBluetoothConnected) {
			new ConnectBT().execute();
			Log.i("DEBUG_TAG","TabActivity onResume()");
		}
		else {
			try {
				//mBTSocket.getOutputStream().write("TL".getBytes());
				//mBTSocket.getOutputStream().write(bs.getBytes());
				mBTSocket.getOutputStream().write(hexStringToByteArray(hexStr));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				
		}
		
	}

	
	
	//milochen add from RobotActivity
	public void sendSpp(String bs) {
		
		if (mBTSocket == null || !mIsBluetoothConnected) {
			new ConnectBT().execute();
			Log.i("DEBUG_TAG","TabActivity onResume()");
		}
		else {
			try {
				//mBTSocket.getOutputStream().write("TL".getBytes());
				mBTSocket.getOutputStream().write(bs.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				

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
			//progressDialog = ProgressDialog.show(RobotActivity.this, "Hold on", "Connecting");// http://stackoverflow.com/a/11130220/1287554
			progressDialog = ProgressDialog.show(CustomTabActivity.this, "Hold on", "Connecting");// http://stackoverflow.com/a/11130220/1287554
		}

		@Override
		protected Void doInBackground(Void... devices) {
			
			//add by milochen
			if(mDevice == null) {
				//RobotActivity.refreshIntentData();
				CustomTabActivity.refreshIntentData();
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
		
	
	
	public void bluetoothDisconnect() {
		mIsUserInitiatedDisconnect = true;
		new DisConnectBT().execute();		
	}
	


	
	public static void requestStartBluetoothConnectProcess() {
		//RobotActivity act = getRobotActivity();
		CustomTabActivity act = CustomTabActivity.getTabActivity();
		if(act!= null) {
			Log.i("DEBUG_TAG","getTabActivity() is work in requestStartBluetoothConnectProcess" );
			act.tryStartBluetoothConnectProcess22();
		}
		else {
		}
	}
	
	public void tryStartBluetoothConnectProcess22() {
		if (mBTSocket == null || !mIsBluetoothConnected) {
			new ConnectBT().execute();
			Log.i("DEBUG_TAG","TabActivity onResume()");
		}
		Log.d(TAG, "tryStartBluetoothConnectProcess22");		
		
	}
	public void tryStartBluetoothConnectProcess() {
		if (mBTSocket == null || !mIsBluetoothConnected) {
			//new ConnectBT().execute();
			Log.i("DEBUG_TAG","TabActivity onResume()");
		}
		Log.d(TAG, "Resumed");		
	}

	static public void refreshIntentData () {
		 //RobotActivity act = getRobotActivity();
		CustomTabActivity act = getTabActivity();
		 if(act == null) {
			 Log.i("DEBUG_TAG", "TabActivity is null");
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

}