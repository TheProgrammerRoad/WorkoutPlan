package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.File;
import java.util.ArrayList;

public class EditDeleteUebung extends AppCompatActivity {
    private static final String TAG = "EditDeleteUebung";

    final int REQUEST_CODE_GALLERY = 999;
    
    private ArrayList<Training> newTrainingList;
    private int tableId;
    Button btnDeleteUebung;
    Button btnAenderungenUebernehmen;
    ImageView imageViewEditDialog;
    String picturePath = "";
    ImageLoader imageLoader;
    DisplayImageOptions options;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_edit_item);

        btnDeleteUebung = (Button) findViewById(R.id.btnUebungloeschen);
        btnDeleteUebung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: delete clicked");

                String st = MainActivity.getTrainingTableName(EditDeleteUebung.this);

                if(!st.equals("WähleeinTraining")) {
                    int deleteSuceeded = MainActivity.sqLiteHelperExcersice.deleteData(st, tableId);
                    if (deleteSuceeded != 0)
                    {
                        String newTableName = "newTableName";
                        MainActivity.sqLiteHelperExcersice.queryData("DELETE FROM sqlite_sequence WHERE NAME='" + st + "';");
                        MainActivity.sqLiteHelperExcersice.queryData("CREATE TABLE " + newTableName + " AS SELECT * FROM " + st);
                        MainActivity.sqLiteHelperExcersice.queryData("DELETE FROM " + st);
                        MainActivity.sqLiteHelperExcersice.queryData("INSERT INTO " + st + " (name, einstellungen, kg1, kg2, kg3, kg4, wdh1, wdh2, wdh3, wdh4, bildUebung) SELECT name, einstellungen, kg1, kg2, kg3, kg4, wdh1, wdh2, wdh3, wdh4, bildUebung FROM " + newTableName);
                        MainActivity.sqLiteHelperExcersice.queryData("DROP TABLE IF EXISTS " + newTableName);
                        toastMessage("Erfolgreich gelöscht");
                    }
                    else
                    {
                        toastMessage("Löschen fehlgeschlagen");
                    }

                    Intent intent = new Intent(EditDeleteUebung.this, MainActivity.class);
                    startActivity(intent);

                }

            }
        });
        
        Intent intent = getIntent();
        newTrainingList = intent.getParcelableArrayListExtra("TrainingListKey");
        tableId = intent.getIntExtra("TrainingListPosition", -1) + 1;

        picturePath = newTrainingList.get(tableId - 1).getTrainingImage();
        imageViewEditDialog = findViewById(R.id.imageViewEditDialog);

        setupImageLoader();

        int defaultImage = EditDeleteUebung.this.getResources().getIdentifier("@drawable/image_failed", null, EditDeleteUebung.this.getPackageName());

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(defaultImage)
                .considerExifParams(true)
                .showImageOnFail(defaultImage)
                .showImageOnLoading(defaultImage).build();

        String decodedImgUri = Uri.fromFile(new File(picturePath)).toString();
        ImageLoader.getInstance().displayImage(decodedImgUri, imageViewEditDialog, options);

        //imageViewEditDialog.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        imageViewEditDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ImageView clicked");
                ActivityCompat.requestPermissions(
                        EditDeleteUebung.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );

            }
        });

        btnAenderungenUebernehmen = findViewById(R.id.btnAenderungenUebernehmen);

        btnAenderungenUebernehmen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(EditDeleteUebung.this);
//                SharedPreferences.Editor editor = mPreferences.edit();
//                String sTraining = mPreferences.getString("trainingDisplay", "Wähle ein Training");
//                String sqlTableName = sTraining.replaceAll("\\s+","");

                String sqlTableName = MainActivity.getTrainingTableName(EditDeleteUebung.this);

                String updateQuery = "UPDATE " + sqlTableName + " SET bildUebung = '" + picturePath + "' WHERE ID = '" + (tableId) + "'";
                MainActivity.sqLiteHelperExcersice.updateData(updateQuery);
                Intent intent = new Intent(EditDeleteUebung.this, MainActivity.class);
                startActivity(intent);
            }
        });





    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else
            {
                toastMessage("Du hast kein Zugriff auf Bilder");
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null)
        {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            String decodedImgUri = Uri.fromFile(new File(picturePath)).toString();
            ImageLoader.getInstance().displayImage(decodedImgUri, imageViewEditDialog, options);

            //imageViewEditDialog.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setupImageLoader()
    {
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                EditDeleteUebung.this)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }

    private void toastMessage(String message)
    {
        Toast.makeText(EditDeleteUebung.this, message, Toast.LENGTH_SHORT).show();
    }
}
