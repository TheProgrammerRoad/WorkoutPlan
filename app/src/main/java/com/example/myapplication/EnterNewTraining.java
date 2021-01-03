package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EnterNewTraining extends AppCompatActivity {

    DatabaseHelper myDB;

    private static final String TAG = "EnterNewTraining";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_new_training);
        Log.d(TAG, "onCreate: Starting");

        final EditText eTEnterNewTraining = (EditText) findViewById(R.id.editTextEnterNewTraining);
        Button btnEnterNewTraining = (Button) findViewById(R.id.buttonEnterNewTrainings);
        myDB = new DatabaseHelper(this);

        btnEnterNewTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean trainingAlreadyExists = false;
                String newEntry  = eTEnterNewTraining.getText().toString();
                ArrayList<String> trainingNames = TrainingAuswahlScreen.tinyDB.getListTrainingNames("TrainingNames");

                for (String trainingName : trainingNames)
                {
                    if (trainingName.equals(newEntry))
                    {
                        trainingAlreadyExists = true;
                    }
                }

                if (newEntry.length() != 0)
                {
                    if (!trainingAlreadyExists)
                    {
                        AddData(newEntry);
                        Intent intent = new Intent(EnterNewTraining.this, TrainingAuswahlScreen.class);
                        startActivity(intent);
                    }
                    else
                    {
                        toastMessage("Training existiert bereits");
                    }

                } else
                {
                    toastMessage("Ein leerer Name ist nicht gültig");
                }



            }
        });

    }

    public void AddData(String newEntry)
    {
        boolean insertData = myDB.addData(newEntry);

        if (insertData == true)
        {
            if (MainActivity.sqLiteHelperExcersice != null)
            {
                String tableName = newEntry.replaceAll("\\s+","");
                tableName = tableName.replaceAll("/", "");
                MainActivity.sqLiteHelperExcersice.queryData("CREATE TABLE IF NOT EXISTS " + tableName + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, einstellungen TEXT, kg1 TEXT, kg2 TEXT, kg3 TEXT, kg4 TEXT, wdh1 TEXT, wdh2 TEXT, wdh3 TEXT, wdh4 TEXT, bildUebung TEXT)");
            }

            toastMessage("Training wurde eingetragen");
        }
        else
        {
            toastMessage("etwas ist schiefgelaufen");
        }
    }

    private void toastMessage(String message)
    {
        Toast.makeText(EnterNewTraining.this, message, Toast.LENGTH_SHORT).show();
    }
}
