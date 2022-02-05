package com.example.gallerypridukhasousov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<smallImage> allFilesPaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
        } else {
            showImages();
        }
    }
    private void showImages() {
        //папка со всеми изображениями
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera";
        allFilesPaths = new ArrayList<>();
        allFilesPaths = listAllFiles(path);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.gallery);
        recyclerView.setHasFixedSize(true);
        // список в три колонки
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        //оптимизация
        recyclerView.setItemViewCacheSize(100);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        ArrayList<smallImage> smallImages = prepareDate();
        photoAdapter adapter = new photoAdapter(getApplicationContext(), smallImages);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<smallImage> prepareDate() {
        ArrayList<smallImage> allImages = new ArrayList<>();
        for (smallImage c : allFilesPaths) {
            smallImage smallimage = new smallImage();
            smallimage.setTitle(c.getTitle());
            smallimage.setPath(c.getPath());
            allImages.add(smallimage);
        }
        return allImages;
    }


    /**
     * Загружает список файлов из папки
     *
     * @param pathName имя папки
     * @return список
     */
    private List<smallImage> listAllFiles(String pathName) {
        List<smallImage> allFiles = new ArrayList<>();
        File file = new File(pathName);
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                smallImage smallimage = new smallImage();
                smallimage.setTitle(f.getName());
                smallimage.setPath(f.getAbsolutePath());
                allFiles.add(smallimage);
            }
        }
        return allFiles;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImages();
            } else {
                Toast.makeText(this, "Разрешения на чтение нет", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}