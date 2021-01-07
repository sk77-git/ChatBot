package com.example.chatbot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextToSpeech textToSpeech;
    EditText messageBox;
    Button confirmBtn;
    ImageButton micBtn;
    TextView helpTv;

    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageBox=findViewById(R.id.messageBox);
        confirmBtn=findViewById(R.id.confirmBtn);
        micBtn=findViewById(R.id.micBtn);
        helpTv=findViewById(R.id.helpTV);

        //softkey color
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }

        textToSpeech= new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                textToSpeech.setLanguage(Locale.ENGLISH);
                String message="Hello I am Bot. How may I help you ?";
                textToSpeech.speak(message,textToSpeech.QUEUE_FLUSH,null);
            }
        });

        // Mic Button
        micBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent micIntent= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                micIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                micIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
                startActivityForResult(micIntent,77);
            }
        });

        // Confirm Button
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task=messageBox.getText().toString().trim();

                if (task.contains("calculator") || task.contains("Calculator"))
                {
                    Intent intent=getPackageManager().getLaunchIntentForPackage("com.android.calculator2");
                    startActivity(intent);
                    String message="Opening Calculator";
                    textToSpeech.speak(message,TextToSpeech.QUEUE_FLUSH,null);
                }
                else if (task.contains("help") || task.contains("Help"))
                {
                    String message="1) Type something in the  Box\n OR \n Speak something in the mic\n 2) Finally press confirm Button";
                    helpTv.setText(message);
                    textToSpeech.speak(message,TextToSpeech.QUEUE_FLUSH,null);
                }
                else
                {
                    String message="No apps found";
                    textToSpeech.speak(message,TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });




    } // on Create ends here...
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==77 && resultCode==RESULT_OK && data!=null)
        {
            arrayList=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            messageBox.setText(arrayList.get(0));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String message="Bye";
        textToSpeech.speak(message,TextToSpeech.QUEUE_FLUSH,null);
    }
} // last closing tag