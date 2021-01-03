package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.chrono.MinguoChronology;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button btnChangeTraining;
    ArrayList<Training> trainingList;
    TrainingListAdapter trainingListAdapter;
    String tableName = "";
    Boolean reallyMoved = false;

    public static DatabaseHelperExcersices sqLiteHelperExcersice;

    @Override
    protected void onPause() {
        super.onPause();
        beforeIntent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reallyMoved = false;
        Log.d(TAG, "onCreate: Started");

        btnChangeTraining = (Button) findViewById(R.id.btnChangeTraining);
        FloatingActionButton fabAddExcercise = (FloatingActionButton) findViewById(R.id.btnAddExcercise);

        RecyclerView lVTrainingListe = (RecyclerView) findViewById(R.id.lvTrainingList);
        lVTrainingListe.setHasFixedSize(true);

        btnChangeTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on btnChangeTraining");

                Intent intent = new Intent(MainActivity.this, TrainingAuswahlScreen.class);
                startActivity(intent);
            }
        });

        fabAddExcercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sTraining = MainActivity.getTrainingTableName(MainActivity.this);

                if(!sTraining.equals("WähleeinTraining")) {

                    Intent intent = new Intent(MainActivity.this, EnterNewExcercise.class);
                    startActivity(intent);
                }
                else
                {
                    toastMessage("Kein Training erstellt.");
                }


            }
        });

        if (sqLiteHelperExcersice == null)
        {
            sqLiteHelperExcersice = new DatabaseHelperExcersices(this, "ExcersiceDB.sqlite", null, 1);
        }

        trainingList = new ArrayList<Training>();


        trainingListAdapter = new TrainingListAdapter(this, R.layout.layout_uebung_in_list_all_exercises, trainingList);
        //trainingListAdapter.setHasStableIds(true);
        lVTrainingListe.setAdapter(trainingListAdapter);
        lVTrainingListe.setLayoutManager(new LinearLayoutManager(this));

        tableName = MainActivity.getTrainingTableName(this);


        if(!tableName.equals("WähleeinTraining")) {
            Cursor cursor = sqLiteHelperExcersice.getData(
                    "SELECT * FROM " + tableName);


            if (cursor != null &&
                    cursor.getCount() != 0) {

                trainingList.clear();
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String einstellungen = cursor.getString(2);
                    String kg1 = cursor.getString(3);
                    String kg2 = cursor.getString(4);
                    String kg3 = cursor.getString(5);
                    String kg4 = cursor.getString(6);
                    String wdh1 = cursor.getString(7);
                    String wdh2 = cursor.getString(8);
                    String wdh3 = cursor.getString(9);
                    String wdh4 = cursor.getString(10);
                    //byte[] image = cursor.getBlob(11);
                    String imagePath = cursor.getString(11);

                    trainingList.add(new Training(name, einstellungen, kg1, kg2, kg3, kg4, wdh1, wdh2, wdh3, wdh4, imagePath, id));
                }
            }
            trainingListAdapter.notifyDataSetChanged();
        }


        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP| ItemTouchHelper.DOWN, 0) {

            int dragFrom = -1;
            int dragTo = -1;

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {

                int position_dragged = dragged.getAdapterPosition();
                int position_target = target.getAdapterPosition();

                Collections.swap(trainingList, position_dragged, position_target);

                trainingListAdapter.notifyItemMoved(position_dragged, position_target);

                if (dragFrom == -1) // If Abfrage abgeschaut, nicht sicher, was sie bewirkt, aber einfach mal so übernommen
                    dragFrom = position_dragged;

                dragTo = position_target;

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                String i;
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);

                if(dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
                    reallyMoved = true;
                    trainingListAdapter.notifyDataSetChanged();
                }

                dragFrom = dragTo = -1;
            }
        });

        helper.attachToRecyclerView(lVTrainingListe);



        checkSharedPreferences();

    }

    private void checkSharedPreferences()
    {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mPreferences.edit();

        String sTraining = mPreferences.getString("trainingDisplay", "Wähle ein Training");
        btnChangeTraining.setText(sTraining);
    }

    private void beforeIntent()
    {
        if (reallyMoved == true)
        {

            String tableName = MainActivity.getTrainingTableName(MainActivity.this);

            MainActivity.sqLiteHelperExcersice.updateOrder(tableName, trainingList);
        }

    }

    private void toastMessage(String message)
    {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public static String getTrainingTableName(Context context)
    {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPreferences.edit();

        String sTraining = mPreferences.getString("trainingDisplay", "Wähle ein Training");
        String sTableName = sTraining.replaceAll("\\s+","");
        sTableName = sTableName.replaceAll("/", "");

        return sTableName;
    }

    public static String getTrainingShortName(String trainingItem)
    {
        String sTableName = trainingItem.replaceAll("\\s+","");
        sTableName = sTableName.replaceAll("/", "");

        return  sTableName;
    }
}
