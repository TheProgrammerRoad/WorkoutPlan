package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TrainingAuswahlScreen extends AppCompatActivity {
    private static final String TAG = "TrainingAuswahlScreen";
    ListView lvTrainingAuswahl;
    DatabaseHelper myDB;
    ListView listViewTrainingAuswahl;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    public BaseAdapter listAdapter;
    ArrayList<String> listTrainings;
    public static TinyDB tinyDB;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_auswahl);
        Log.d(TAG, "onCreate: Starting");

        if (tinyDB == null)
        {
            tinyDB = new TinyDB(this);
        }
        FloatingActionButton btnAddTraining = (FloatingActionButton) findViewById(R.id.btnAddTraining);


        btnAddTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tinyDB.putListTrainingNames("TrainingNames", listTrainings);
                Intent intent = new Intent(TrainingAuswahlScreen.this, EnterNewTraining.class);
                startActivity(intent);
            }
        });


        listViewTrainingAuswahl = (ListView) findViewById(R.id.listViewTrainingAuswahl);

        listTrainings = new ArrayList<>();
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listTrainings);
        listViewTrainingAuswahl.setAdapter(listAdapter);

        myDB = new DatabaseHelper(this);
        Cursor data = myDB.getListContents();

        if (data.getCount() == 0)
        {
            toastMessage("Noch kein Training eingetragen");
        }
        else
        {
            while(data.moveToNext())
            {
                listTrainings.add(data.getString(1));

            }

            listAdapter.notifyDataSetChanged();

        }

        listViewTrainingAuswahl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String sTraining = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "onItemClick: Du hast " + sTraining + " ausgewählt");

                mPreferences = PreferenceManager.getDefaultSharedPreferences(TrainingAuswahlScreen.this);
                mEditor = mPreferences.edit();
                mEditor.putString("trainingDisplay", sTraining);
                mEditor.commit();


                Intent intent = new Intent(TrainingAuswahlScreen.this, MainActivity.class);
                //intent.putExtra("idTrainingName", sTraining);
                startActivity(intent);
            }
        });

        listViewTrainingAuswahl.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog diaBox = AskOption(parent, position);
                diaBox.show();

                return true;
            }
        });
    }

    public AlertDialog AskOption(AdapterView<?> parent, final int position)
    {
        final String sTraining = parent.getItemAtPosition(position).toString();

        final String st = MainActivity.getTrainingShortName(sTraining);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(TrainingAuswahlScreen.this);
        View mView = getLayoutInflater().inflate(R.layout.training_edit_delete_dialog, null);
        TextView tVDialogTitle = (TextView) mView.findViewById(R.id.etDialogTitle);
        final EditText eTTrainingName = (EditText) mView.findViewById(R.id.eTTainingNameDialog);
        Button btnDialogLoeschen = (Button) mView.findViewById(R.id.btnDialogLoeschen);
        Button btnDialogAndern = (Button) mView.findViewById(R.id.btnDialogAendern);

        tVDialogTitle.setText(sTraining);
        eTTrainingName.setText(sTraining);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

        btnDialogAndern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myDB.updateTraining(st, eTTrainingName.getText().toString(), (position + 1));
                buildNewList(); //todo funktioniert, aber auch mit customAdapter ausprobieren
                dialog.dismiss();
            }
        });

        btnDialogLoeschen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.sqLiteHelperExcersice.queryData("DROP TABLE IF EXISTS " + st);
                if (myDB != null)
                {
                    Cursor data = myDB.getItemID(sTraining);

                    int itemId = -1;

                    while(data.moveToNext())
                    {
                        itemId = data.getInt(0);
                    }
                    if (itemId < 0)
                    {
                        toastMessage("Löschen fehlgeschlagen");
                    }
                    else
                    {
                        myDB.deleteTraining(itemId);
                    }

                }

                buildNewList(); //todo funktioniert, aber auch mit customAdapter ausprobieren
                dialog.dismiss();
            }
        });

        return dialog;
    }

//    public AlertDialog AskOption(AdapterView<?> parent, int position)
//    {
//        final String sTraining = parent.getItemAtPosition(position).toString();
//        final String st = sTraining.replaceAll("\\s+","");
//
//        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
//                // set message, title, and icon
//                .setTitle("Delete")
//                .setMessage("Do you want to Delete")
//                //.setIcon(R.drawable.delete)
//
//                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        Log.d(TAG, "onClick: Löschen Ja");
//                        MainActivity.sqLiteHelperExcersice.queryData("DROP TABLE IF EXISTS " + st);
//                        if (myDB != null)
//                        {
//                            Cursor data = myDB.getItemID(sTraining);
//
//                            int itemId = -1;
//
//                            while(data.moveToNext())
//                            {
//                                itemId = data.getInt(0);
//                            }
//                            if (itemId < 0)
//                            {
//                                toastMessage("Löschen fehlgeschlagen");
//                            }
//                            else
//                            {
//                                myDB.deleteTraining(itemId);
//                            }
//
//                        }
//
//
//
//                        dialog.dismiss();
//                    }
//
//                })
//                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.d(TAG, "onClick: Löschen Nein");
//
//
//                        dialog.dismiss();
//
//                    }
//                })
//                .create();
//
//        return myQuittingDialogBox;
//    }

    private void toastMessage(String message)
    {
        Toast.makeText(TrainingAuswahlScreen.this, message, Toast.LENGTH_SHORT).show();
    }

    private void buildNewList()
    {
        myDB = new DatabaseHelper(this);
        Cursor data = myDB.getListContents();

        listTrainings.clear();

        if (data.getCount() == 0)
        {
            toastMessage("Noch kein Training eingetragen");
        }
        else
        {
            while(data.moveToNext())
            {
                listTrainings.add(data.getString(1));
            }

        }
        listAdapter.notifyDataSetChanged();
    }



}


