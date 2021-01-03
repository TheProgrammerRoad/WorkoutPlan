package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class EnterNewExcercise extends AppCompatActivity {

    private static final String TAG = "EnterNewExcercise";
    EditText eTName, eTEinstellungen, etKg1, etKg2, etKg3, etKg4, eTWdh1, eTWdh2, eTWdh3, eTWdh4;
    ImageView iVBildUebung;
    private String picturePath = "";
    final int REQUEST_CODE_GALLERY = 999;

    //public static DatabaseHelperExcersices sqLiteHelperExcersice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_new_excercise);
        init();
        Log.d(TAG, "onCreate: ");


        iVBildUebung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ImageView clicked");
                ActivityCompat.requestPermissions(
                        EnterNewExcercise.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        Button btnAddNewExcercise = (Button) findViewById(R.id.btnAddNewExcercise);

        btnAddNewExcercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: buttonAddNewExercise");
                try
                {
//                    SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(EnterNewExcercise.this);
//                    SharedPreferences.Editor editor = mPreferences.edit();
//
//                    String sTraining = mPreferences.getString("trainingDisplay", "Wähle ein Training");
//
//                    String tableName = sTraining.replaceAll("\\s+","");

                    String tableName = MainActivity.getTrainingTableName(EnterNewExcercise.this);

                    MainActivity.sqLiteHelperExcersice.insertData(
                            tableName,
                            eTName.getText().toString().trim(),
                            eTEinstellungen.getText().toString().trim(),
                            etKg1.getText().toString().trim(),
                            etKg2.getText().toString().trim(),
                            etKg3.getText().toString().trim(),
                            etKg4.getText().toString().trim(),
                            eTWdh1.getText().toString().trim(),
                            eTWdh2.getText().toString().trim(),
                            eTWdh3.getText().toString().trim(),
                            eTWdh4.getText().toString().trim(),
                            picturePath
                    );
                    toastMessage("Übung hinzugefügt");

                    //eTName.setText("");
                    eTEinstellungen.setText("");
                    etKg1.setText("");
                    etKg2.setText("");
                    etKg3.setText("");
                    etKg4.setText("");
                    eTWdh1.setText("");
                    eTWdh2.setText("");
                    eTWdh3.setText("");
                    eTWdh4.setText("");
                    picturePath = "";
                    iVBildUebung.setImageResource(R.drawable.ic_launcher_foreground);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        Button btnZurueckZuMain = (Button) findViewById(R.id.btnFromEnterNewExcerciseToMain);
        btnZurueckZuMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Button zurück zu main gedrückt");
                Intent intent = new Intent(EnterNewExcercise.this, MainActivity.class);
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

            //picturePath = picturePath.replace(':', '/');
            //picturePath = picturePath.replace('/', '_');
            cursor.close();

            iVBildUebung.setImageBitmap(BitmapFactory.decodeFile(picturePath));


//            try {
//                InputStream inputStream = getContentResolver().openInputStream(uri);
//
//                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                iVBildUebung.setImageBitmap(bitmap);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init()
    {
        eTName = (EditText) findViewById(R.id.etName);
        eTEinstellungen = (EditText) findViewById(R.id.etEinstellungen);
        etKg1 = (EditText) findViewById(R.id.etkg1);
        etKg2 = (EditText) findViewById(R.id.etkg2);
        etKg3 = (EditText) findViewById(R.id.etkg3);
        etKg4 = (EditText) findViewById(R.id.etkg4);
        eTWdh1 = (EditText) findViewById(R.id.etwdh1);
        eTWdh2 = (EditText) findViewById(R.id.etwdh2);
        eTWdh3 = (EditText) findViewById(R.id.etwdh3);
        eTWdh4 = (EditText) findViewById(R.id.etwdh4);
        iVBildUebung = (ImageView) findViewById(R.id.iVUebung);


        eTName.setText("");
        eTEinstellungen.setText("");
        etKg1.setText("");
        etKg2.setText("");
        etKg3.setText("");
        etKg4.setText("");
        eTWdh1.setText("");
        eTWdh2.setText("");
        eTWdh3.setText("");
        eTWdh4.setText("");
        picturePath = "";
        iVBildUebung.setImageResource(R.drawable.ic_launcher_foreground);
    }


    private void imageViewToByte(ImageView image)
    {

//        Drawable drawable = image.getDrawable();
//
//        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
//                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//        drawable.draw(canvas);
//
//        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
//        byte[] byteArray = oStream.toByteArray();
//        return byteArray;
    }

    private void toastMessage(String message)
    {
        Toast.makeText(EnterNewExcercise.this, message, Toast.LENGTH_SHORT).show();
    }
}
