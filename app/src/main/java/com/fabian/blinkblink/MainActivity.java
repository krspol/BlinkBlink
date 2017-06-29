package com.fabian.blinkblink;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.nfc.NdefRecord.createMime;

public class MainActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback {

    NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check for NFC support
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not supported", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Register callback
        nfcAdapter.setNdefPushMessageCallback(this, this);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        EditText editText = (EditText) findViewById(R.id.editText);
        String text = editText.getText().toString();
        NdefMessage ndefMessage = new NdefMessage(
                new NdefRecord[]{createMime(
                        "application/vnd.com.peterleow.androidconnection", text.getBytes())
                });
        return ndefMessage;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check to see that the Activity started by an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            parseNdefMessage(getIntent());
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // / Make sure the latest Intent will be used in OnResume() that follows
        setIntent(intent);
    }

    void parseNdefMessage(Intent intent) {
        Parcelable[] ndefMessageArray = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage ndefMessage = (NdefMessage) ndefMessageArray[0];
        Toast.makeText(this, new String(ndefMessage.getRecords()[0].getPayload()), Toast.LENGTH_LONG).show();
    }

}