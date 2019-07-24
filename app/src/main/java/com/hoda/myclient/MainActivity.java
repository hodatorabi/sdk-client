package com.hoda.myclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.hoda.cpserver.ICalService;

public class MainActivity extends Activity {
    EditText editName, editVal1, editVal2;
    TextView resultView;
    protected ICalService calService = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (calService == null) {
            System.out.println("it's null");
            Intent it = new Intent(ICalService.class.getName());
            it.setAction("com.hoda.cpserver.multiplyservice");

            it.setPackage("com.hoda.myclient");
//            Intent it = new Intent(this, ICalService.class);
            getApplicationContext().bindService(it, new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    calService = ICalService.Stub.asInterface(service);
                    Toast.makeText(getApplicationContext(),	"Service Connected", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onServiceDisconnected(ComponentName name) {
                    calService = null;
                    Toast.makeText(getApplicationContext(), "Service Disconnected", Toast.LENGTH_SHORT).show();
                }
            }, Context.BIND_AUTO_CREATE);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        editName = (EditText)findViewById(R.id.name);
        editVal1 = (EditText) findViewById(R.id.num1);
        editVal2 = (EditText) findViewById(R.id.num2);
        resultView = (TextView) findViewById(R.id.result);
        System.out.println("STARTED");
    }

    public void multiply(View v) {
        System.out.println("clicking");
        if (calService != null) {
            System.out.println("not null");
            switch (v.getId()) {
                case R.id.multiply_btn: {
                    try {
                        String msg = calService.getMessage(editName.getText().toString());
                        resultView.setText(msg );
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

}
