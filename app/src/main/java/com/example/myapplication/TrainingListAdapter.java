package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class TrainingListAdapter extends RecyclerView.Adapter<TrainingListAdapter.ViewHolder> {
    private static final String TAG = "TrainingListAdapter";
    private Context cContext;
    private ArrayList<Training> m_TrainingList;
    int iResource;
    View.OnClickListener onClickListener;
    ImageLoader imageLoader;
    DisplayImageOptions options;


    public TrainingListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Training> objects) {

        this.cContext = context;
        //super(context, resource, objects);
        iResource = resource;
        m_TrainingList = objects;

        setupImageLoader();

        int defaultImage = cContext.getResources().getIdentifier("@drawable/image_failed", null, cContext.getPackageName());

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(defaultImage)
                .considerExifParams(true)
                .showImageOnFail(defaultImage)
                .showImageOnLoading(defaultImage).build();
    }



    private class MyTextWatcher implements TextWatcher
    {
        private transient EditText editText = null;
        private int position;
        private String sqlTableName;

        private Timer timer = new Timer();
        private final long DELAY = 1500; // in ms
        DatabaseHelperExcersices db;

        TimerTask timerTask;

        public MyTextWatcher(EditText editText, int position)
        {
            super();
            this.editText = editText;
            this.position = position;
        }
        @Override
        public void afterTextChanged(final Editable s) {

            if(!editText.hasFocus())
                return;
//            SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(cContext);
//            SharedPreferences.Editor editor = mPreferences.edit();
//
//            String sTraining = mPreferences.getString("trainingDisplay", "Wähle ein Training");

            sqlTableName = MainActivity.getTrainingTableName(cContext);

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    String st = "addTextChangedListener: " + position + " "; // nur für log
                    String updateQuery;
                    switch (editText.getId())
                    {
                        case R.id.etNamelist:
                            st += "etNamelist";
                            Log.d(TAG, st);
                            m_TrainingList.get(position).setNameTraining(editText.getText().toString());
                            updateQuery = "UPDATE " + sqlTableName + " SET name = '" + editText.getText().toString() + "' WHERE ID = '" + (position + 1) + "'";
                            MainActivity.sqLiteHelperExcersice.updateData(updateQuery);
                            break;
                        case R.id.etEinstellungenlist:
                            st += "etEinstellungenlist";
                            Log.d(TAG, st);
                            m_TrainingList.get(position).setEinstellungen(editText.getText().toString());
                            updateQuery = "UPDATE " + sqlTableName + " SET einstellungen = '" + editText.getText().toString() + "' WHERE ID = '" + (position + 1) + "'";
                            MainActivity.sqLiteHelperExcersice.updateData(updateQuery);
                            break;
                        case R.id.etkg1list:
                            st += "etkg1list";
                            Log.d(TAG, st);
                            m_TrainingList.get(position).setKg1(editText.getText().toString());
                            updateQuery = "UPDATE " + sqlTableName + " SET kg1 = '" + editText.getText().toString() + "' WHERE ID = '" + (position + 1) + "'";
                            MainActivity.sqLiteHelperExcersice.updateData(updateQuery);
                            break;
                        case R.id.etkg2list:
                            st += "etkg2list";
                            Log.d(TAG, st);
                            m_TrainingList.get(position).setKg2(editText.getText().toString());
                            updateQuery = "UPDATE " + sqlTableName + " SET kg2 = '" + editText.getText().toString() + "' WHERE ID = '" + (position + 1) + "'";
                            MainActivity.sqLiteHelperExcersice.updateData(updateQuery);
                            break;
                        case R.id.etkg3list:
                            st += "etkg3list";
                            Log.d(TAG, st);
                            m_TrainingList.get(position).setKg3(editText.getText().toString());
                            updateQuery = "UPDATE " + sqlTableName + " SET kg3 = '" + editText.getText().toString() + "' WHERE ID = '" + (position + 1) + "'";
                            MainActivity.sqLiteHelperExcersice.updateData(updateQuery);
                            break;
                        case R.id.etkg4list:
                            st += "etkg4list";
                            Log.d(TAG, st);
                            m_TrainingList.get(position).setKg4(editText.getText().toString());
                            updateQuery = "UPDATE " + sqlTableName + " SET kg4 = '" + editText.getText().toString() + "' WHERE ID = '" + (position + 1) + "'";
                            MainActivity.sqLiteHelperExcersice.updateData(updateQuery);
                            break;
                        case R.id.etwdh1list:
                            st += "etwdh1list";
                            Log.d(TAG, st);
                            m_TrainingList.get(position).setWdh1(editText.getText().toString());
                            updateQuery = "UPDATE " + sqlTableName + " SET wdh1 = '" + editText.getText().toString() + "' WHERE ID = '" + (position + 1) + "'";
                            MainActivity.sqLiteHelperExcersice.updateData(updateQuery);
                            break;
                        case R.id.etwdh2list:
                            st += "etwdh2list";
                            Log.d(TAG, st);
                            m_TrainingList.get(position).setWdh2(editText.getText().toString());
                            updateQuery = "UPDATE " + sqlTableName + " SET wdh2 = '" + editText.getText().toString() + "' WHERE ID = '" + (position + 1) + "'";
                            MainActivity.sqLiteHelperExcersice.updateData(updateQuery);
                            break;
                        case R.id.etwdh3list:
                            st += "etwdh3list";
                            Log.d(TAG, st);
                            m_TrainingList.get(position).setWdh3(editText.getText().toString());
                            updateQuery = "UPDATE " + sqlTableName + " SET wdh3 = '" + editText.getText().toString() + "' WHERE ID = '" + (position + 1) + "'";
                            MainActivity.sqLiteHelperExcersice.updateData(updateQuery);
                            break;
                        case R.id.etwdh4list:
                            st += "etwdh4list";
                            Log.d(TAG, st);
                            m_TrainingList.get(position).setWdh4(editText.getText().toString());
                            updateQuery = "UPDATE " + sqlTableName + " SET wdh4 = '" + editText.getText().toString() + "' WHERE ID = '" + (position + 1) + "'";
                            MainActivity.sqLiteHelperExcersice.updateData(updateQuery);
                            break;
                    }
                }

            }, DELAY);
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
        {
        }

        @Override
        public void onTextChanged(final CharSequence s, int start, int before, int count) {
            if(timer != null)
                timer.cancel();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        EditText edTName, edTEinstellungen, edTKg1,  edTKg2, edTKg3, edTKg4,  edTWdh1, edTWdh2, edTWdh3, edTWdh4;
        MyTextWatcher textWatcher;
        HashMap<String, MyTextWatcher> textWatcherObjects;
        String keyET;

        public ViewHolder(@NonNull View view)
        {
            super(view);
        textWatcherObjects = new HashMap<String, MyTextWatcher>();


            this.imageView = (ImageView) view.findViewById(R.id.iVUebunglist);
            this.edTName = (EditText) view.findViewById(R.id.etNamelist);
            this.edTEinstellungen = (EditText) view.findViewById(R.id.etEinstellungenlist);
            this.edTKg1= (EditText) view.findViewById(R.id.etkg1list);
            this.edTKg2= (EditText) view.findViewById(R.id.etkg2list);
            this.edTKg3= (EditText) view.findViewById(R.id.etkg3list);
            this.edTKg4= (EditText) view.findViewById(R.id.etkg4list);
            this.edTWdh1= (EditText) view.findViewById(R.id.etwdh1list);
            this.edTWdh2= (EditText) view.findViewById(R.id.etwdh2list);
            this.edTWdh3= (EditText) view.findViewById(R.id.etwdh3list);
            this.edTWdh4= (EditText) view.findViewById(R.id.etwdh4list);

        }

        public void addTextWatcher(EditText eT, int position)
        {
            textWatcher = new MyTextWatcher(eT, position);
            eT.addTextChangedListener(textWatcher);
            keyET = eT.getResources().getResourceName(eT.getId());
            textWatcherObjects.put(keyET, textWatcher);
        }

        public void removeTextWatcher(EditText eT)
        {
            keyET = eT.getResources().getResourceName(eT.getId());
            textWatcher = textWatcherObjects.get(keyET);
            eT.removeTextChangedListener(textWatcher);
            textWatcherObjects.remove(keyET);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        ViewHolder holder = new ViewHolder(LayoutInflater.from(cContext)
//                .inflate(iResource, parent, false));

        return new ViewHolder(
                LayoutInflater.from(cContext)
                .inflate(iResource, parent, false)
        );
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final int listPosition = position;
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s;
                s = "onClick: iVUebunglist" + listPosition;
                Log.d(TAG, s);

                Intent intent = new Intent(cContext, EditDeleteUebung.class);
                intent.putParcelableArrayListExtra("TrainingListKey", m_TrainingList);
                intent.putExtra("TrainingListPosition", listPosition);
                cContext.startActivity(intent);
            }
        };

        //todo abfragen, ob textwatcher schon gesetzt wurde oder ausprobieren ob removeTextwatcher einfach weggelassen werden kann
        holder.removeTextWatcher(holder.edTName);
        holder.removeTextWatcher(holder.edTEinstellungen);
        holder.removeTextWatcher(holder.edTKg1);
        holder.removeTextWatcher(holder.edTKg2);
        holder.removeTextWatcher(holder.edTKg3);
        holder.removeTextWatcher(holder.edTKg4);
        holder.removeTextWatcher(holder.edTWdh1);
        holder.removeTextWatcher(holder.edTWdh2);
        holder.removeTextWatcher(holder.edTWdh3);
        holder.removeTextWatcher(holder.edTWdh4);

        Training training = m_TrainingList.get(position);

        String decodedImgUri = Uri.fromFile(new File(training.getTrainingImage())).toString();
        ImageLoader.getInstance().displayImage(decodedImgUri, holder.imageView, options);


        holder.edTName.setText(training.getNameTraining());
        holder.edTEinstellungen.setText(training.getEinstellungen());
        holder.edTKg1.setText(training.getKg1());
        holder.edTKg2.setText(training.getKg2());
        holder.edTKg3.setText(training.getKg3());
        holder.edTKg4.setText(training.getKg4());
        holder.edTWdh1.setText(training.getWdh1());
        holder.edTWdh2.setText(training.getWdh2());
        holder.edTWdh3.setText(training.getWdh3());
        holder.edTWdh4.setText(training.getWdh4());

        holder.imageView.setOnClickListener(onClickListener);

        holder.addTextWatcher(holder.edTName, position);
        holder.addTextWatcher(holder.edTEinstellungen, position);
        holder.addTextWatcher(holder.edTKg1, position);
        holder.addTextWatcher(holder.edTKg2, position);
        holder.addTextWatcher(holder.edTKg3, position);
        holder.addTextWatcher(holder.edTKg4, position);
        holder.addTextWatcher(holder.edTWdh1, position);
        holder.addTextWatcher(holder.edTWdh2, position);
        holder.addTextWatcher(holder.edTWdh3, position);
        holder.addTextWatcher(holder.edTWdh4, position);
    }

//    @NonNull
//    @Override
//    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//        //ViewHolder holder = new ViewHolder();
//
////        onClickListener = new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                String s;
////                s = "onClick: iVUebunglist" + position;
////                Log.d(TAG, s);
////
////                Intent intent = new Intent(cContext, EditDeleteUebung.class);
////                intent.putParcelableArrayListExtra("TrainingListKey", m_TrainingList);
////                intent.putExtra("TrainingListPosition", position);
////                cContext.startActivity(intent);
////            }
////        };
//
//        if (convertView == null) {
//
//
//
//
////            LayoutInflater inflater = LayoutInflater.from(cContext);
////            convertView = inflater.inflate(iResource, parent, false);
////
////            holder.imageView = (ImageView) convertView.findViewById(R.id.iVUebunglist);
////            holder.edTName = (EditText) convertView.findViewById(R.id.etNamelist);
////            holder.edTEinstellungen = (EditText) convertView.findViewById(R.id.etEinstellungenlist);
////            holder.edTKg1= (EditText) convertView.findViewById(R.id.etkg1list);
////            holder.edTKg2= (EditText) convertView.findViewById(R.id.etkg2list);
////            holder.edTKg3= (EditText) convertView.findViewById(R.id.etkg3list);
////            holder.edTKg4= (EditText) convertView.findViewById(R.id.etkg4list);
////            holder.edTWdh1= (EditText) convertView.findViewById(R.id.etwdh1list);
////            holder.edTWdh2= (EditText) convertView.findViewById(R.id.etwdh2list);
////            holder.edTWdh3= (EditText) convertView.findViewById(R.id.etwdh3list);
////            holder.edTWdh4= (EditText) convertView.findViewById(R.id.etwdh4list);
////
////            convertView.setTag(holder);
//        }
//        else {
//            holder = (ViewHolder) convertView.getTag();
//
//            holder.removeTextWatcher(holder.edTName);
//            holder.removeTextWatcher(holder.edTEinstellungen);
//            holder.removeTextWatcher(holder.edTKg1);
//            holder.removeTextWatcher(holder.edTKg2);
//            holder.removeTextWatcher(holder.edTKg3);
//            holder.removeTextWatcher(holder.edTKg4);
//            holder.removeTextWatcher(holder.edTWdh1);
//            holder.removeTextWatcher(holder.edTWdh2);
//            holder.removeTextWatcher(holder.edTWdh3);
//            holder.removeTextWatcher(holder.edTWdh4);
//        }
//
////        String decodedImgUri = Uri.fromFile(new File(getItem(position).getTrainingImage())).toString();
////        ImageLoader.getInstance().displayImage(decodedImgUri, holder.imageView, options);
////
////
////        holder.edTName.setText(getItem(position).getNameTraining());
////        holder.edTEinstellungen.setText(getItem(position).getEinstellungen());
////        holder.edTKg1.setText(getItem(position).getKg1());
////        holder.edTKg2.setText(getItem(position).getKg2());
////        holder.edTKg3.setText(getItem(position).getKg3());
////        holder.edTKg4.setText(getItem(position).getKg4());
////        holder.edTWdh1.setText(getItem(position).getWdh1());
////        holder.edTWdh2.setText(getItem(position).getWdh2());
////        holder.edTWdh3.setText(getItem(position).getWdh3());
////        holder.edTWdh4.setText(getItem(position).getWdh4());

//          holder.imageView.setOnClickListener(onClickListener);
//
////        byte[] trainingImage = getItem(position).getTrainingImage();
////        Bitmap bitmap = BitmapFactory.decodeByteArray(trainingImage, 0, trainingImage.length);
//        //holder.imageView.setImageBitmap(BitmapFactory.decodeFile(getItem(position).getTrainingImage()));
////        if (bitmapArray.length > 0)
////        {
////            holder.imageView.setImageBitmap(bitmapArray[position]);
////        }
//
//
//
//
//
//        holder.addTextWatcher(holder.edTName, position);
//        holder.addTextWatcher(holder.edTEinstellungen, position);
//        holder.addTextWatcher(holder.edTKg1, position);
//        holder.addTextWatcher(holder.edTKg2, position);
//        holder.addTextWatcher(holder.edTKg3, position);
//        holder.addTextWatcher(holder.edTKg4, position);
//        holder.addTextWatcher(holder.edTWdh1, position);
//        holder.addTextWatcher(holder.edTWdh2, position);
//        holder.addTextWatcher(holder.edTWdh3, position);
//        holder.addTextWatcher(holder.edTWdh4, position);
//
//
//        return convertView;
//
//    }


    @Override
    public int getItemCount() {
        return this.m_TrainingList.size();
    }

    private void setupImageLoader()
    {
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                cContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }
}
