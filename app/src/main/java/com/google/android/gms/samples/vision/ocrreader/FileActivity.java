package com.google.android.gms.samples.vision.ocrreader;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.IOException;
import java.util.Locale;

public class FileActivity extends AppCompatActivity {

    private TextToSpeech textToSpeech;
    private TextView outputTextView;
    private static final int READ_REQUEST_CODE = 42;
    private static final String PRIMARY = "primary";
    private static final String LOCAL_STORAGE = "/storage/self/primary/";
    private static final String EXT_STORAGE = "/storage/7764-A034/";
    private static final String COLON = ":";
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        outputTextView = findViewById(R.id.output_text);
        outputTextView.setMovementMethod(new ScrollingMovementMethod());

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                textToSpeech.setLanguage(Locale.US);
            }
        });

        /* getting user permission for external storage */
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("*/*");
                Intent chooserIntent = Intent.createChooser(intent, "Open Report");
                try{
                startActivityForResult(intent, READ_REQUEST_CODE);
            }
                catch(ActivityNotFoundException e)
                {
                    Toast.makeText(FileActivity.this, "Not found", Toast.LENGTH_SHORT).show();
                }
                }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if(requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if(resultData != null) {
                Uri uri = resultData.getData();
//                Toast.makeText(this, uri.getPath(), Toast.LENGTH_SHORT).show();
                Log.v("URI", uri.getPath());
                String a=uri.getPath();
                Log.v("PATH",a);
                readPdfFile(uri);
            }
        }
    }
    String fileName;
    public String getPDFPath(Uri uri){

        final String id = DocumentsContract.getDocumentId(uri);
        final Uri contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = this.getContentResolver().query(contentUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String r=cursor.getString(column_index);
        cursor.close();
        return r;

    }

    public void readPdfFile(Uri uri) {
        String fullPath;String a;
        //convert from uri to full path

        /*if(uri.getPath().contains(PRIMARY)) {
            fullPath = LOCAL_STORAGE + uri.getPath().split(COLON)[1];
        }
        else {

            a=uri.getPath();
            fullPath = EXT_STORAGE + uri.getPath().split(COLON)[1];
            Log.v("URI", uri.getPath()+" "+fullPath);
        }*/


        //fullPath=getPDFPath(uri);
        fullPath=uri.getPath();
        Log.v("URI", uri.getPath()+" "+fullPath);
        String stringParser;

        try {
            PdfReader pdfReader = new PdfReader(fullPath);
            stringParser = PdfTextExtractor.getTextFromPage(pdfReader, 1).trim();
            pdfReader.close();
            outputTextView.setText(stringParser);
            textToSpeech.speak(stringParser, TextToSpeech.QUEUE_FLUSH,null, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
