package com.example.childmetredetector;

import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Vibrator;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{


    TextView textStatTxt;
    TextView rngTxt;
    Button buttonActBtn,buttonPairBtn,buttonStopBtn,buttonCnclBtn;
    BluetoothAdapter bluetoothAdapter;
    private MediaPlayer mp;
    final static int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        textStatTxt = findViewById(R.id.statTxt);
        buttonActBtn = findViewById(R.id.actBtn);
        buttonPairBtn = findViewById(R.id.actBtn2);
        buttonStopBtn = findViewById(R.id.actBtn3);
        buttonCnclBtn = findViewById(R.id.actBtn4);
        ImageView img= findViewById(R.id.blInd);
        rngTxt = findViewById(R.id.rangeTxt);
        mp = MediaPlayer.create(this, R.raw.hfbeep);
        mp.setLooping(true);

        IntentFilter stfl1 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        IntentFilter stfl2 = new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        IntentFilter stfl3 = new IntentFilter();
        stfl3.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        stfl3.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(stbr1, stfl1);
        registerReceiver(stbr2, stfl2);
        registerReceiver(stbr3, stfl3);

        //Bluetooth Starting State Catcher
       if (bluetoothAdapter == null) {
           textStatTxt.setText("Status: Device doesn't support Bluetooth");
           buttonActBtn.setEnabled(false);
           buttonPairBtn.setEnabled(false);
           buttonStopBtn.setEnabled(false);}
       else{
            if (bluetoothAdapter.isEnabled()) {
                if (bluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP) == BluetoothProfile.STATE_CONNECTED){
                    //catches paired high quality stereo (AD2P) devices
                    buttonActBtn.setEnabled(false);
                    buttonPairBtn.setEnabled(true);
                    buttonStopBtn.setEnabled(false);
                    textStatTxt.setText("Status: Paired. Press button to start Monitoring");
                    img.setImageResource(R.drawable.bt_1);
                }
                else if (bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEALTH) == BluetoothProfile.STATE_CONNECTED){
                    //catches paired health devices
                    buttonActBtn.setEnabled(false);
                    buttonPairBtn.setEnabled(true);
                    buttonStopBtn.setEnabled(false);
                    textStatTxt.setText("Status: Paired. Press button to start Monitoring");
                    img.setImageResource(R.drawable.bt_1);
                }
                else if (bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET) == BluetoothProfile.STATE_CONNECTED){
                    //catches paired bluetooth headsets
                    buttonActBtn.setEnabled(false);
                    buttonPairBtn.setEnabled(true);
                    buttonStopBtn.setEnabled(false);
                    textStatTxt.setText("Status: Paired. Press button to start Monitoring");
                    img.setImageResource(R.drawable.bt_1);
                }
                else if (bluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP) == BluetoothProfile.STATE_DISCONNECTED){
                    buttonActBtn.setEnabled(false);
                    buttonPairBtn.setEnabled(false);
                    buttonStopBtn.setEnabled(false);
                    textStatTxt.setText("Status: Awaiting device pairing");
                    img.setImageResource(R.drawable.bt_1);
                }
                else if (bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEALTH) == BluetoothProfile.STATE_DISCONNECTED){
                    buttonActBtn.setEnabled(false);
                    buttonPairBtn.setEnabled(false);
                    buttonStopBtn.setEnabled(false);
                    textStatTxt.setText("Status: Awaiting device pairing");
                    img.setImageResource(R.drawable.bt_1);
                }
                else if (bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET) == BluetoothProfile.STATE_DISCONNECTED){
                    buttonActBtn.setEnabled(false);
                    buttonPairBtn.setEnabled(false);
                    buttonStopBtn.setEnabled(false);
                    textStatTxt.setText("Status: Awaiting device pairing");
                    img.setImageResource(R.drawable.bt_1);}}
            else{
                buttonActBtn.setEnabled(true);
                buttonPairBtn.setEnabled(false);
                buttonStopBtn.setEnabled(false);
                textStatTxt.setText("Status: Bluetooth disabled, press button to turn on"); }}

        buttonPairBtn.setOnClickListener(
        //Functions for Monitoring button
            new OnClickListener(){
                @Override
                public void onClick(View v) {
                    IntentFilter filter1 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                    IntentFilter filter2 = new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
                    IntentFilter filter3 = new IntentFilter();
                    filter3.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
                    filter3.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
                    unregisterReceiver(stbr1);
                    unregisterReceiver(stbr2);
                    unregisterReceiver(stbr3);
                    registerReceiver(mBroadcastReceiver1, filter1);
                    registerReceiver(mBroadcastReceiver2, filter2);
                    registerReceiver(mBroadcastReceiver3, filter3);

                    buttonActBtn.setEnabled(false);
                    buttonPairBtn.setEnabled(false);
                    buttonStopBtn.setEnabled(false);
                    buttonCnclBtn.setEnabled(true);
                    buttonStopBtn.setVisibility(View.GONE);
                    buttonPairBtn.setVisibility(View.GONE);
                    buttonCnclBtn.setVisibility(View.VISIBLE);
                }});
        buttonCnclBtn.setOnClickListener(
        //Code for Cancel Button
            new OnClickListener(){
                @Override
                public void onClick(View v) {
                    unregisterReceiver(mBroadcastReceiver1);
                    unregisterReceiver(mBroadcastReceiver2);
                    unregisterReceiver(mBroadcastReceiver3);
                    IntentFilter stfl1 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                    IntentFilter stfl2 = new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
                    IntentFilter stfl3 = new IntentFilter();
                    stfl3.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
                    stfl3.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

                    buttonActBtn.setEnabled(false);
                    buttonPairBtn.setEnabled(true);
                    buttonCnclBtn.setEnabled(false);
                    buttonStopBtn.setEnabled(false);
                    buttonCnclBtn.setVisibility(View.GONE);
                    buttonStopBtn.setVisibility(View.GONE);
                    buttonPairBtn.setVisibility(View.VISIBLE);
                    registerReceiver(stbr1, stfl1);
                    registerReceiver(stbr2, stfl2);
                    registerReceiver(stbr3, stfl3);
                }});

        buttonStopBtn.setOnClickListener(
                //Code for Turning Off Listeners, Vibrators and Players
                new OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        unregisterReceiver(mBroadcastReceiver1);
                        unregisterReceiver(mBroadcastReceiver2);
                        unregisterReceiver(mBroadcastReceiver3);
                        mp.stop();
                        mp.release();
                        buttonStopBtn.setVisibility(View.GONE);
                        buttonPairBtn.setVisibility(View.VISIBLE);
                        buttonActBtn.setEnabled(false);
                        buttonPairBtn.setEnabled(true);
                        buttonStopBtn.setEnabled(false);
                        textStatTxt.setText("Status: Alarm Reset!");

                        IntentFilter stfl1 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                        IntentFilter stfl2 = new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
                        IntentFilter stfl3 = new IntentFilter();
                        stfl3.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
                        stfl3.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
                        registerReceiver(stbr1, stfl1);
                        registerReceiver(stbr2, stfl2);
                        registerReceiver(stbr3, stfl3);
                    }});
        buttonActBtn.setOnClickListener(
                //Code for Turning On Bluetooth
                new OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);}});
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if(requestCode == REQUEST_ENABLE_BT){
            if(resultCode==RESULT_OK){
                Toast.makeText(MainActivity.this, "BlueTooth Turned On", Toast.LENGTH_LONG).show();
                buttonPairBtn.setEnabled(true);
                buttonStopBtn.setEnabled(false);
                mp.start();}
            else{Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_LONG).show();}}
        if (bluetoothAdapter.isEnabled()) {
                ImageView img = findViewById(R.id.blInd);
                img.setImageResource(R.drawable.bt_1);
                buttonActBtn.setEnabled(false);
                buttonPairBtn.setEnabled(true);
                buttonStopBtn.setEnabled(false);
                textStatTxt.setText("Status: Awaiting device pairing");}
            else{
                buttonActBtn.setEnabled(true);
                textStatTxt.setText("Status: Bluetooth disabled, press button to turn on");}}
    //broadcast receiver for bluetooth states
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        @Override

        public void onReceive(Context context, Intent intent) {
            ImageView img= findViewById(R.id.blInd);
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch(state) {
                    case BluetoothAdapter.STATE_OFF:
                        buttonActBtn.setEnabled(true);
                        buttonPairBtn.setEnabled(false);
                        textStatTxt.setText("Status: Bluetooth disabled, press button to turn on");
                        img.setImageResource(R.drawable.bt_0);
                        rngTxt.setText("Distance: N/A");
                        break;

                    case BluetoothAdapter.STATE_ON:

                        break;
                }}}};

    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ImageView img= findViewById(R.id.blInd);
            Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100, 100};
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, BluetoothAdapter.ERROR);
                switch(state) {

                    case BluetoothAdapter.STATE_DISCONNECTED:
                        buttonActBtn.setEnabled(false);
                        buttonPairBtn.setEnabled(false);
                        buttonStopBtn.setEnabled(true);
                        textStatTxt.setText("Status: Disconnected! Sounding the Alarm!");
                        img.setImageResource(R.drawable.bt_0);
                        rngTxt.setText("Distance: N/A");
                        mp.start();
                        vb.vibrate(pattern, -1);
                        buttonPairBtn.setVisibility(View.GONE);
                        buttonCnclBtn.setVisibility(View.GONE);
                        buttonStopBtn.setVisibility(View.VISIBLE);
                        break;

                    case BluetoothAdapter.STATE_CONNECTED:
                        buttonActBtn.setEnabled(false);
                        buttonPairBtn.setEnabled(false);
                        buttonStopBtn.setEnabled(false);
                        buttonCnclBtn.setEnabled(true);
                        textStatTxt.setText("Status: Paired and Monitored");
                        buttonPairBtn.setVisibility(View.GONE);
                        buttonCnclBtn.setVisibility(View.VISIBLE);
                        img.setImageResource(R.drawable.bt_1);
                        rngTxt.setText("Distance: Close");
                        break;}}}};

    //broadcast receiver for Low Level (ACL) broadcast connections
    private final BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            ImageView img= findViewById(R.id.blInd);
            long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100, 100};
            String action = intent.getAction();

            switch (action){
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    buttonActBtn.setEnabled(false);
                    buttonPairBtn.setEnabled(false);
                    buttonStopBtn.setEnabled(false);
                    textStatTxt.setText("Status: Paired and Monitored");
                    img.setImageResource(R.drawable.bt_1);
                    rngTxt.setText("Distance: Close");
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    buttonActBtn.setEnabled(false);
                    buttonPairBtn.setEnabled(false);
                    buttonStopBtn.setEnabled(true);
                    textStatTxt.setText("Status: Disconnected! Sounding the Alarm!");
                    img.setImageResource(R.drawable.bt_0);
                    rngTxt.setText("Distance: N/A");
                    mp.start();
                    vb.vibrate(pattern, -1);
                    buttonPairBtn.setVisibility(View.GONE);
                    buttonCnclBtn.setVisibility(View.GONE);
                    buttonStopBtn.setVisibility(View.VISIBLE);
                    break;}
        }
    };

    //broadcast receiver for bluetooth states
    private final BroadcastReceiver stbr1 = new BroadcastReceiver() {
        @Override

        public void onReceive(Context context, Intent intent) {

            ImageView img= findViewById(R.id.blInd);
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch(state) {
                    case BluetoothAdapter.STATE_OFF:
                        buttonActBtn.setEnabled(true);
                        buttonPairBtn.setEnabled(false);
                        textStatTxt.setText("Status: Bluetooth disabled, press button to turn on");
                        img.setImageResource(R.drawable.bt_0);
                        rngTxt.setText("Distance: N/A");
                        break;

                    case BluetoothAdapter.STATE_ON:
                        buttonActBtn.setEnabled(false);
                        buttonPairBtn.setEnabled(false);
                        textStatTxt.setText("Status: Awaiting device pairing");
                        img.setImageResource(R.drawable.bt_1);
                        rngTxt.setText("Distance: N/A");
                        break;
                }}}};

    private final BroadcastReceiver stbr2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ImageView img= findViewById(R.id.blInd);
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, BluetoothAdapter.ERROR);
                switch(state) {

                    case BluetoothAdapter.STATE_DISCONNECTED:
                        buttonActBtn.setEnabled(false);
                        buttonPairBtn.setEnabled(true);
                        buttonStopBtn.setEnabled(false);
                        textStatTxt.setText("Status: Disconnected");
                        img.setImageResource(R.drawable.bt_0);
                        rngTxt.setText("Distance: N/A");
                        break;

                    case BluetoothAdapter.STATE_CONNECTED:
                        buttonActBtn.setEnabled(false);
                        buttonPairBtn.setEnabled(true);
                        buttonStopBtn.setEnabled(false);
                        textStatTxt.setText("Status: Paired. Press button to start Monitoring");
                        img.setImageResource(R.drawable.bt_1);
                        rngTxt.setText("Distance: Close");
                        break;
                }}}};

    private final BroadcastReceiver stbr3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ImageView img= findViewById(R.id.blInd);
            String action = intent.getAction();
            switch (action){
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    buttonActBtn.setEnabled(false);
                    buttonPairBtn.setEnabled(true);
                    buttonStopBtn.setEnabled(false);
                    textStatTxt.setText("Status: Paired. Press button to start Monitoring");
                    img.setImageResource(R.drawable.bt_1);
                    rngTxt.setText("Distance: Close");
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    buttonActBtn.setEnabled(false);
                    buttonPairBtn.setEnabled(false);
                    buttonStopBtn.setEnabled(false);
                    textStatTxt.setText("Status: Disconnected");
                    img.setImageResource(R.drawable.bt_0);
                    rngTxt.setText("Distance: N/A");
                    break;}}};

    @Override
    public void onDestroy() {
        super.onDestroy();
      // Unregister broadcast listeners
        unregisterReceiver(stbr1);
        unregisterReceiver(stbr2);
        unregisterReceiver(stbr3);
        unregisterReceiver(mBroadcastReceiver1);
        unregisterReceiver(mBroadcastReceiver2);
        unregisterReceiver(mBroadcastReceiver3);}
}