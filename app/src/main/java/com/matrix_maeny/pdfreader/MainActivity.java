package com.matrix_maeny.pdfreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.matrix_maeny.pdfreader.pdfs.PDFAdapter;
import com.matrix_maeny.pdfreader.pdfs.PDFModel;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int STORAGE_PERMISSION_CODE = 1;
    private RecyclerView recyclerView;
    private PDFAdapter adapter = null;
    ProgressBar progressBar;

    ArrayList<PDFModel> list;
    TextView emptyTextView;

    final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission();
        }

        initialize();
    }

    // for initialization purpose
    private void initialize() {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        emptyTextView = findViewById(R.id.emptyTextView);

        list = new ArrayList<>();
        adapter = new PDFAdapter(MainActivity.this, list);

        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);


        new Thread() {
            public void run() {
                File file = Environment.getExternalStorageDirectory();
                getPdfFiles(file);

                handler.post(() -> {
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);

                });
            }
        }.start();
    }

    // request permissions
    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Permission needed")
                    .setMessage("Storage permission needed to read flies")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override // result of permission granted or denied
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showToast("Permission denied.. Enable manually..", 1);
            }
        }
    }

    // for showing toast
    private void showToast(String s, int i) {
        if (i == 0) {
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, s, Toast.LENGTH_LONG).show();

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getPdfFiles(@NonNull File file) {

        File[] files = file.listFiles();

        if (files != null) {
            for (File f : files) {

                if (f.isDirectory() && !f.isHidden()) {
                    getPdfFiles(f);
                } else {

                    if (f.getName().endsWith(".pdf")) {

                        list.add(new PDFModel(f.getName(), f.getPath()));

                    }
                }
            }
        }


        handler.post(()->{
            if(!list.isEmpty()){
                emptyTextView.setVisibility(View.GONE);
            }else{
                emptyTextView.setVisibility(View.VISIBLE);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        startActivity(new Intent(MainActivity.this, AboutActivity.class));
        return super.onOptionsItemSelected(item);
    }

    //    private Bitmap getBitmap(String path) {
//
//        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
//        mediaMetadataRetriever.setDataSource(path);
//
//        byte[] byteArray = mediaMetadataRetriever.getEmbeddedPicture();
//        Bitmap bitmap = null;
//
//        if (byteArray != null) {
//            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//        }
//        return bitmap;
//    }
}