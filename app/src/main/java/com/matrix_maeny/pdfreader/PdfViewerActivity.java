package com.matrix_maeny.pdfreader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class PdfViewerActivity extends AppCompatActivity {

    ImageView playPauseBtn;

    TextToSpeech textToSpeech;
    boolean clicked = false;
    Set<String> a = new HashSet<String>();          // to store voice

   // female: en-us-x-tpf-local
    //female: en-IN-language ***
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Reader");

        playPauseBtn = findViewById(R.id.playPauseBtn);

        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(clicked){
                    playPauseBtn.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                    clicked = false;

                    if(textToSpeech != null && textToSpeech.isSpeaking()){

                        textToSpeech.stop();
                    }

                }else{
                    playPauseBtn.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                    if(DataCentre.pdfText != null){
                        speakTheWords(DataCentre.pdfText);
                    }
                    clicked = true;
                }

//                if(DataCentre.pdfText != null){
//                    speakTheWords(DataCentre.pdfText,1.0f,1.0f);
//                }
            }
        });

    }


    private void speakTheWords(String words) {

        textToSpeech = new TextToSpeech(PdfViewerActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {


                    int checkLanguageStatus = textToSpeech.setLanguage(Locale.US);

//                    Voice voice = new Voice("en-us-x-tpf-local", new Locale("en", "us"), 400, 200, false, a);
                    Voice voice = new Voice("en-IN-language", new Locale("en", "IN"), 400, 200, false, a);
                    textToSpeech.setVoice(voice);

                } else {
                    Toast.makeText(PdfViewerActivity.this, "Text To Speed Engine Initialization Failed: processHelper", Toast.LENGTH_SHORT).show();
                    return;
                }

                textToSpeech.setPitch((float) 1.0);
                textToSpeech.setSpeechRate((float) 1.0);


                textToSpeech.stop();
                Toast.makeText(PdfViewerActivity.this, "Speaking started, wait few seconds to deliver", Toast.LENGTH_LONG).show();
                textToSpeech.speak(words, TextToSpeech.QUEUE_FLUSH, null, "matrix");

            }
        });



        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!textToSpeech.isSpeaking()){
                    textToSpeech.stop();
                    textToSpeech.speak(words, TextToSpeech.QUEUE_FLUSH, null, "matrix");
                    //Toast.makeText(context.getApplicationContext(), "Wait few seconds...", Toast.LENGTH_SHORT).show();
                    handler1.postDelayed(this,5);

                }else{
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!textToSpeech.isSpeaking()){
                                //Toast.makeText(context.getApplicationContext(), "speech stopped", Toast.LENGTH_SHORT).show();
                                playPauseBtn.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                                clicked = true;
                            }else {
                                handler.postDelayed(this,5);
                            }
                        }
                    },50);
                }
            }
        },1);


    }

}