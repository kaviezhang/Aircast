package com.example.administrator.aircast;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.aircast.R;
import com.example.administrator.aircast.BluetoothChatUtil;


public class MainActivity extends Activity   {
	private Button playit;
	private Button mBtnClient;
	private Button mBtnServer;
	private AlertDialog.Builder builder;
	private DialogInterface dialog;
	private LocationManager locationManager;
	private final static String TAG = "ServerActivity";
	private BluetoothAdapter mBluetoothAdapter;
	private int REQUEST_ENABLE_BT = 1;
	private Context mContext;

	private Button mBtnBluetoothVisibility;
	private Button mBtnBluetoohDisconnect;
	private Button mBtnSendMessage;
	private EditText mEdttMessage;

	private TextView mBtConnectState;
	private TextView mTvChat;
	private ProgressDialog mProgressDialog;
	private BluetoothChatUtil mBlthChatUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);



        playit = (Button)findViewById(R.id.playit);
        playit.setOnClickListener(
                new OnClickListener() {
                    public void onClick(View v){
                        // TODO Auto-generated method stub
                        Log.e("打开了","一个自动弹出单元");
                        analysemessage();//分析消息内容
                    }
                }

        );
        mBtnBluetoothVisibility = (Button)findViewById(R.id.btn_blth_visiblity);
        mBtnBluetoothVisibility.setOnClickListener(
                new OnClickListener() {
                    public void onClick(View v){
                        if (mBluetoothAdapter.isEnabled()) {
                            if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                                Intent discoveryIntent = new Intent(
                                        BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                                discoveryIntent.putExtra(
                                        BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                                startActivity(discoveryIntent);
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.bluetooth_unopened), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

        );
        mBtnBluetoohDisconnect = (Button)findViewById(R.id.btn_blth_disconnect);
        mBtnBluetoohDisconnect.setOnClickListener(
                new OnClickListener() {
                    public void onClick(View v){
                        if (mBlthChatUtil.getState() != BluetoothChatUtil.STATE_CONNECTED) {
                            Toast.makeText(mContext, "蓝牙未连接", Toast.LENGTH_SHORT).show();
                        }else {
                            mBlthChatUtil.disconnect();
                        }
                    }
                }

        );

        mBtnSendMessage = (Button)findViewById(R.id.btn_sendmessage);
        mBtnSendMessage.setOnClickListener(
                new OnClickListener() {
                    public void onClick(View v){
                        String messagesend = mEdttMessage.getText().toString();
                        if(null == messagesend || messagesend.length() == 0){
                            return;
                        }
                        mBlthChatUtil.write(messagesend.getBytes());
                    }
                }

        );
        mEdttMessage = (EditText)findViewById(R.id.edt_message);
        mBtConnectState = (TextView)findViewById(R.id.tv_connect_state);
        mTvChat = (TextView)findViewById(R.id.tv_chat);
        mProgressDialog = new ProgressDialog(this);








		final int PERMISSION_REQUEST_COARSE_LOCATION=1001;
		final int PERMISSION_REQUEST_FINE_LOCATION=1002;
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)

		{

			if(this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
			{
				final AlertDialog.Builder builder=new AlertDialog.Builder(this);
				builder.setPositiveButton(android.R.string.ok,null);
				builder.setTitle("获取ACCESS_COARSE_LOCATION权限");
				builder.setOnDismissListener(new DialogInterface.OnDismissListener(){
				public void onDismiss(DialogInterface dialog){
					requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_REQUEST_COARSE_LOCATION);
					Log.e("ACCESS_COARSE_LOCATION","ACCESS_COARSE_LOCATION+ACCESS_COARSE_LOCATION");
				}
			});
				builder.show();
			}

			if(this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)

			{
				final AlertDialog.Builder builder=new AlertDialog.Builder(this);
				builder.setPositiveButton(android.R.string.ok,null);
				builder.setTitle("获取ACCESS_FINE_LOCATION权限");
				builder.setOnDismissListener(new DialogInterface.OnDismissListener(){
					public void onDismiss(DialogInterface dialog){
						requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_FINE_LOCATION);
						Log.e("ACCESS_FINE_LOCATION","ACCESS_FINE_LOCATION+ACCESS_FINE_LOCATION");
					}
				});
				builder.show();
			}
		}







//		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//			if(isGpsEnable(this)){
//				showListView();
//			}else {
//				liteble.enableLocation(this,PERMISSION_REQUEST_COARSE_LOCATION);
//				Toast.makeText(this,R.string.openGps,Toast.LENGTH_LONG).show();
//			}
//
//		}


		mContext = this;
		//initView();
		initBluetooth();
		mBlthChatUtil = BluetoothChatUtil.getInstance(mContext);
		mBlthChatUtil.registerHandler(mHandler);
//




}



	private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1001;
	private static final int PERMISSION_REQUEST_FINE_LOCATION=1002;

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[],
										   int[] grantResults) {
		switch (requestCode) {
			case PERMISSION_REQUEST_COARSE_LOCATION: {
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Log.d("yijinghuode", "coarse location permission granted");
				} else {
					final AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Functionality limited");
					builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background./n COARSE_LOCATION");
					builder.setPositiveButton(android.R.string.ok, null);
					builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
						}
					});
					builder.show();
				}
				return;
			}

			case PERMISSION_REQUEST_FINE_LOCATION:{
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Log.d("已经获得", "coarse location permission granted");
				} else {
					final AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Functionality limited");
					builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background./n FINE_LOCATION");
					builder.setPositiveButton(android.R.string.ok, null);
					builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
						}
					});
					builder.show();
				}
				return;




			}


		}
	}

	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
				case BluetoothChatUtil.STATE_CONNECTED:
					String deviceName = msg.getData().getString(BluetoothChatUtil.DEVICE_NAME);
					mBtConnectState.setText("已成功连接到设备" + deviceName);
					if(mProgressDialog.isShowing()){
						mProgressDialog.dismiss();
					}
					break;
				case BluetoothChatUtil.STATAE_CONNECT_FAILURE:
					if(mProgressDialog.isShowing()){
						mProgressDialog.dismiss();
					}
					Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_SHORT).show();
					break;
				case BluetoothChatUtil.MESSAGE_DISCONNECTED:
					if(mProgressDialog.isShowing()){
						mProgressDialog.dismiss();
					}
					mBtConnectState.setText("与设备断开连接");
					mBlthChatUtil.startListen();
					break;
				case BluetoothChatUtil.MESSAGE_READ:{
					byte[] buf = msg.getData().getByteArray(BluetoothChatUtil.READ_MSG);
					String str = new String(buf,0,buf.length);
					Toast.makeText(getApplicationContext(), "读成功" + str, Toast.LENGTH_SHORT).show();
					mTvChat.setText(mTvChat.getText().toString()+"\n"+str);
					analysemessage();//分析消息内容
					break;
				}
				case BluetoothChatUtil.MESSAGE_WRITE:{
					byte[] buf = (byte[]) msg.obj;
					String str = new String(buf,0,buf.length);
					Toast.makeText(getApplicationContext(), "发送成功" + str, Toast.LENGTH_SHORT).show();
					break;
				}
				default:
					break;
			}
		};
	};



	private void analysemessage(){
		Intent vp = new Intent(MainActivity.this, VideoPlayer.class);
		startActivity(vp);
	}


	private void initBluetooth() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {//设备不支持蓝牙
			Toast.makeText(getApplicationContext(), "设备不支持蓝牙", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		//判断蓝牙是否开启
		if (!mBluetoothAdapter.isEnabled()) {//蓝牙未开启
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			//mBluetoothAdapter.enable();此方法直接开启蓝牙，不建议这样用。
		}
		//设置蓝牙可见性
		if (mBluetoothAdapter.isEnabled()) {
			if (mBluetoothAdapter.getScanMode() !=
					BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
				Intent discoverableIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				discoverableIntent.putExtra(
						BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
				startActivity(discoverableIntent);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "onActivityResult request="+requestCode+" result="+resultCode);
		if(requestCode == 1){
			if(resultCode == RESULT_OK){

			}else if(resultCode == RESULT_CANCELED){
				finish();
			}
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		if (!mBluetoothAdapter.isEnabled())return;
		if (mBlthChatUtil != null) {
			// 只有国家是state_none，我们知道，我们还没有开始
			if (mBlthChatUtil.getState() == BluetoothChatUtil.STATE_NONE) {
				// 启动蓝牙聊天服务
				mBlthChatUtil.startListen();
			}else if (mBlthChatUtil.getState() == BluetoothChatUtil.STATE_CONNECTED){
				BluetoothDevice device = mBlthChatUtil.getConnectedDevice();
				if(null != device && null != device.getName()){
					mBtConnectState.setText("已成功连接到设备" + device.getName());
				}else {
					mBtConnectState.setText("已成功连接到设备");
				}
			}
		}
	}


	/////////////////






	// Storage Permissions
	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static String[] PERMISSIONS_STORAGE = {
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE
	};

	/**
	 * Checks if the app has permission to write to device storage
	 *
	 * If the app does not has permission then the user will be prompted to grant permissions
	 *
	 * @param activity
	 */
	public static void verifyStoragePermissions(Activity activity) {
		// Check if we have write permission
		int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

		if (permission != PackageManager.PERMISSION_GRANTED) {
			// We don't have permission so prompt the user
			ActivityCompat.requestPermissions(
					activity,
					PERMISSIONS_STORAGE,
					REQUEST_EXTERNAL_STORAGE
			);
		}
	}
	// Storage Permissions


}
