package com.example.tboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TelecomManager tm;

    AudioManager am;
    DatabaseReference firebase=FirebaseDatabase.getInstance().getReference();
    static boolean first_time=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tm = (TelecomManager) this.getSystemService(Context.TELECOM_SERVICE);

        am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);


        ValueEventListener data_change_listener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int command=Integer.parseInt(dataSnapshot.child("data").getValue(String.class).split(" ")[0]);
                ((TextView)findViewById(R.id.textView2)).setText(Integer.toString(command));
                if(!first_time) {
                    switch (command) {
                        case 0:
                            speed_dial();
                            break;
                        case 1:
                            call_answer();
                            break;
                        case 2:
                            call_decline();
                            break;
                        case 3:
                            music_toggle();
                            break;
                        case 4:
                            music_next();
                            break;
                        case 5:
                            music_prev();
                            break;
                        case 6:
                            volume_up();
                            break;
                        case 7:
                            volume_down();
                            break;
                        case 8:
                            toggle_mute();
                            break;

                    }
                }
                else
                {
                    first_time=false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        firebase.addValueEventListener(data_change_listener);
    }

    public void speed_dial() {
        EditText speed_dial = (EditText) findViewById(R.id.speed_dial);
        String phone_no = speed_dial.getText().toString();
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone_no));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }

    public void call_answer() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        tm.acceptRingingCall();
    }


    public void call_decline() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        tm.endCall();

    }
    public void music_toggle()
    {
        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "togglepause");
        sendBroadcast(i);
    }
    public void music_next()
    {
        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "next");
        sendBroadcast(i);
    }
    public void music_prev()
    {
        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "previous");
        sendBroadcast(i);
    }
    public void volume_up()
    {
        for(int i=0;i<5;i++) {
            am.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
        }
    }
    public void volume_down() {
        for (int i = 0; i < 5; i++) {
            am.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
        }
    }
    public void toggle_mute()
    {
        am.adjustVolume(AudioManager.ADJUST_TOGGLE_MUTE,AudioManager.FLAG_PLAY_SOUND);
    }

}
