package com.pieapple.qeerobot;

//import com.joshclemm.android.tabs.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
//import android.provider.Contacts;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class PhoneActivity extends Activity implements OnClickListener{

	String mPhoneNumberStr = "";
	Button mPhoneNumberLbl; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone);
		
		findViewById(R.id.open_contacts_picker_btn).setOnClickListener(this);
		findViewById(R.id.dial_num_1_btn).setOnClickListener(this);
		findViewById(R.id.dial_num_2_btn).setOnClickListener(this);
		findViewById(R.id.dial_num_3_btn).setOnClickListener(this);
		findViewById(R.id.dial_num_4_btn).setOnClickListener(this);
		findViewById(R.id.dial_num_5_btn).setOnClickListener(this);
		findViewById(R.id.dial_num_6_btn).setOnClickListener(this);
		findViewById(R.id.dial_num_7_btn).setOnClickListener(this);
		findViewById(R.id.dial_num_8_btn).setOnClickListener(this);
		findViewById(R.id.dial_num_9_btn).setOnClickListener(this);
		findViewById(R.id.dial_num_star_btn).setOnClickListener(this);
		findViewById(R.id.dial_num_0_btn).setOnClickListener(this);
		findViewById(R.id.dial_num_sharp_btn).setOnClickListener(this);
		
		findViewById(R.id.dial_call_btn).setOnClickListener(this);
		findViewById(R.id.dial_del_btn).setOnClickListener(this);
		
		mPhoneNumberLbl = (Button) findViewById(R.id.phone_number_text_view);
	
	}
	
	
	@Override
	protected void onResume() {
		
		super.onResume();
		try {
		this.autoResize();
		}
		catch(Exception e) {
			
		}
	}
	
	
	void autoResize () {
		
		/*
		 *
		 * Samsung Core
		 */
		 
		/*
		 * 紅米機 view info
		01-30 09:09:32.620: I/DEBUG_TAG(20790): PhoneActivity activity_phone_framelayout (w,h) = (1099, 720)
		01-30 09:09:32.620: I/DEBUG_TAG(20790): subview(x,y,w,h) = (0.0, 0.0, 720, 1099) 
		01-30 09:09:32.620: I/DEBUG_TAG(20790): subview(x,y,w,h) = (62.0, 58.0, 170, 166) 
		01-30 09:09:32.630: I/DEBUG_TAG(20790): subview(x,y,w,h) = (276.0, 58.0, 170, 166) 
		01-30 09:09:32.630: I/DEBUG_TAG(20790): subview(x,y,w,h) = (490.0, 58.0, 170, 166) 
		01-30 09:09:32.630: I/DEBUG_TAG(20790): subview(x,y,w,h) = (62.0, 248.0, 170, 168) 
		01-30 09:09:32.630: I/DEBUG_TAG(20790): subview(x,y,w,h) = (276.0, 248.0, 170, 168) 
		01-30 09:09:32.630: I/DEBUG_TAG(20790): subview(x,y,w,h) = (490.0, 248.0, 170, 168) 
		01-30 09:09:32.630: I/DEBUG_TAG(20790): subview(x,y,w,h) = (62.0, 438.0, 170, 168) 
		01-30 09:09:32.630: I/DEBUG_TAG(20790): subview(x,y,w,h) = (276.0, 438.0, 170, 168) 
		01-30 09:09:32.630: I/DEBUG_TAG(20790): subview(x,y,w,h) = (490.0, 438.0, 170, 168) 
		01-30 09:09:32.630: I/DEBUG_TAG(20790): subview(x,y,w,h) = (62.0, 628.0, 170, 168) 
		01-30 09:09:32.630: I/DEBUG_TAG(20790): subview(x,y,w,h) = (276.0, 628.0, 170, 168) 
		01-30 09:09:32.640: I/DEBUG_TAG(20790): subview(x,y,w,h) = (490.0, 628.0, 170, 168) 
		01-30 09:09:32.640: I/DEBUG_TAG(20790): subview(x,y,w,h) = (0.0, 979.0, 720, 120) 
		01-30 09:09:32.640: I/DEBUG_TAG(20790): subview(x,y,w,h) = (0.0, 979.0, 720, 120) 
		01-30 09:09:32.640: I/DEBUG_TAG(20790): subview(x,y,w,h) = (0.0, 859.0, 360, 120) 
		01-30 09:09:32.640: I/DEBUG_TAG(20790): subview(x,y,w,h) = (360.0, 859.0, 360, 120) 
		*/
		
		/**
		 * samsung core prime
		 * 
			01-30 20:59:42.079: I/DEBUG_TAG(22003): PhoneActivity activity_phone_framelayout (w,h) = (480, 664)
			01-30 20:59:42.079: I/DEBUG_TAG(22003): subview(x,y,w,h)=(0.0,0.0,480,664)
			01-30 20:59:42.079: I/DEBUG_TAG(22003): subview(x,y,w,h)=(47.0,44.0,128,125)
			01-30 20:59:42.079: I/DEBUG_TAG(22003): subview(x,y,w,h)=(207.0,44.0,128,125)
			01-30 20:59:42.079: I/DEBUG_TAG(22003): subview(x,y,w,h)=(368.0,44.0,128,125)
			01-30 20:59:42.079: I/DEBUG_TAG(22003): subview(x,y,w,h)=(47.0,186.0,128,126)
			01-30 20:59:42.079: I/DEBUG_TAG(22003): subview(x,y,w,h)=(207.0,186.0,128,126)
			01-30 20:59:42.079: I/DEBUG_TAG(22003): subview(x,y,w,h)=(368.0,186.0,128,126)
			01-30 20:59:42.079: I/DEBUG_TAG(22003): subview(x,y,w,h)=(47.0,329.0,128,126)
			01-30 20:59:42.079: I/DEBUG_TAG(22003): subview(x,y,w,h)=(207.0,329.0,128,126)
			01-30 20:59:42.079: I/DEBUG_TAG(22003): subview(x,y,w,h)=(368.0,329.0,128,126)
			01-30 20:59:42.079: I/DEBUG_TAG(22003): subview(x,y,w,h)=(47.0,471.0,128,126)
			01-30 20:59:42.079: I/DEBUG_TAG(22003): subview(x,y,w,h)=(207.0,471.0,128,126)
			01-30 20:59:42.089: I/DEBUG_TAG(22003): subview(x,y,w,h)=(368.0,471.0,128,126)
			01-30 20:59:42.089: I/DEBUG_TAG(22003): subview(x,y,w,h)=(0.0,574.0,480,90)
			01-30 20:59:42.089: I/DEBUG_TAG(22003): subview(x,y,w,h)=(0.0,574.0,480,90)
			01-30 20:59:42.089: I/DEBUG_TAG(22003): subview(x,y,w,h)=(0.0,484.0,270,90)
			01-30 20:59:42.089: I/DEBUG_TAG(22003): subview(x,y,w,h)=(270.0,484.0,270,90)
			
		 */
		
		View parentV = findViewById(R.id.activity_phone_framelayout);
		Log.i("DEBUG_TAG", "PhoneActivity activity_phone_framelayout (x,y,w,h) = ("+parentV.getX()+","+parentV.getY()+","+parentV.getWidth()+", "+parentV.getHeight()+")");
		
		
		/*
		 * 1:3 = 2:6
		 * x:width = new_x:new_width
		 *   
		 */
		
		ViewGroup g = (ViewGroup)parentV;
		double xScale = 720 / parentV.getWidth(); 
		double yScale = 1099 / parentV.getHeight(); 
		int i = 0;
		for ( i = 0 ; i < g.getChildCount(); i++ ) {
			View v = g.getChildAt(i);
			try {
			Log.i("DEBUG_TAG", "subview(x,y,w,h)=("+v.getX()+","+v.getY()+","+v.getWidth()+","+v.getHeight()+")");
			}
			catch(Exception e) {
				
			}
			
			//v.layout(l, t, r, b)
			if(i==1) {
				
				//v.layout(100, 150,100,100);
				
				//big_orig (720, 1099)
				//big_new (480, 664)
				//orig(62.0, 58.0, 170, 166)
				//new (47.0,44.0,128,125)
				
				
				xScale = (480/720);
				yScale = (664/1099);
				double x = 62.0 * xScale;
				double y = 58.0 * yScale;
				double w = 170 * xScale;
				double h = 166 * yScale;
				//v.layout((int)x,(int)y,(int)w,(int)h);
				//v.layout(47,44,(int)w,(int)h);
				//v.layout(100, 50,350,350);
				//v.layout(x, y,128,125);
				//v.layout(x,y,350,350);
				
				
				
				
			}
		}
		
		
		
		
	}

	@Override
	public void onClick(View v) {
		
		
		int vid = v.getId();
		switch(vid) {
		case R.id.open_contacts_picker_btn:
			openContactsPicker();
			break;
		case R.id.dial_num_1_btn:
			this.mPhoneNumberStr = this.mPhoneNumberStr + "1";
			break;
		case R.id.dial_num_2_btn:
			this.mPhoneNumberStr = this.mPhoneNumberStr + "2";
			break;
		case R.id.dial_num_3_btn:
			this.mPhoneNumberStr = this.mPhoneNumberStr + "3";
			break;
		case R.id.dial_num_4_btn:
			this.mPhoneNumberStr = this.mPhoneNumberStr + "4";
			break;
		case R.id.dial_num_5_btn:
			this.mPhoneNumberStr = this.mPhoneNumberStr + "5";
			break;
		case R.id.dial_num_6_btn:
			this.mPhoneNumberStr = this.mPhoneNumberStr + "6";
			break;
		case R.id.dial_num_7_btn:
			this.mPhoneNumberStr = this.mPhoneNumberStr + "7";
			break;
		case R.id.dial_num_8_btn:
			this.mPhoneNumberStr = this.mPhoneNumberStr + "8";
			break;
		case R.id.dial_num_9_btn:
			this.mPhoneNumberStr = this.mPhoneNumberStr + "9";
			break;
		case R.id.dial_num_star_btn:
			this.mPhoneNumberStr = this.mPhoneNumberStr + "*";
			break;
		case R.id.dial_num_0_btn:
			this.mPhoneNumberStr = this.mPhoneNumberStr + "0";
			break;
		case R.id.dial_num_sharp_btn:
			this.mPhoneNumberStr = this.mPhoneNumberStr + "#";
			break;
		case R.id.dial_call_btn:
			this.dialNumber(mPhoneNumberStr);
			break;
		case R.id.dial_del_btn:
			this.mPhoneNumberStr = "";
			break;
		}
		
		
		if(mPhoneNumberStr == "") {
			mPhoneNumberLbl.setVisibility(View.INVISIBLE);
		}
		else {
			mPhoneNumberLbl.setVisibility(View.VISIBLE);
			mPhoneNumberLbl.setText(mPhoneNumberStr);
		}
		
		// TODO Auto-generated method stub
	}
	
	public void openContactsPicker() {
		this.doLaunchContactPicker();
	}
	
	private static final int CONTACT_PICKER_RESULT = 1001;	
	public void doLaunchContactPicker() {		
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setData(ContactsContract.Contacts.CONTENT_URI);
		startActivityForResult(intent, CONTACT_PICKER_RESULT);	
	}
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == CONTACT_PICKER_RESULT && resultCode == RESULT_OK) {
    		Uri selectedContact = data.getData();
    		Log.w("DEBUG_TAG", selectedContact.toString());
    		String phoneNumber = this.retrieveContactNumber(selectedContact);
    		this.dialNumber(phoneNumber);
    	}    	
    }	
    
    public void dialNumber(String phoneNumber) {
    	Uri number = Uri.parse("tel:"+phoneNumber);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);    	
    }
    
    
    private String retrieveContactNumber(Uri uriContact) {
    	 
        String contactNumber = null;
        String contactID = "";
        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact, 
        		new String[]{ContactsContract.Contacts._ID},
                null, null, null);
 
        if (cursorID.moveToFirst()) {
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }
 
        cursorID.close();
 
        Log.d("DEBUG_TAG", "Contact ID: " + contactID);
 
        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
 
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
 
                new String[]{contactID},
                null);
 
        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
 
        cursorPhone.close();
 
        Log.d("DEBUG_TAG", "Contact Phone Number: " + contactNumber);
        return contactNumber;
    }    
	
}
