package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Training implements Parcelable {

    private int id;
    private String nameTraining;
    private String einstellungen;
    private String kg1;
    private String kg2;
    private String kg3;
    private String kg4;
    private String wdh1;
    private String wdh2;
    private String wdh3;
    private String wdh4;
    private String imagePath;
    //private byte[] trainingImage;

    public Training(String nameTraining, String einstellungen, String kg1, String kg2, String kg3, String kg4, String wdh1, String wdh2, String wdh3, String wdh4, String imagePath, int id) {
        this.id = id;
        this.nameTraining = nameTraining;
        this.einstellungen = einstellungen;
        this.kg1 = kg1;
        this.kg2 = kg2;
        this.kg3 = kg3;
        this.kg4 = kg4;
        this.wdh1 = wdh1;
        this.wdh2 = wdh2;
        this.wdh3 = wdh3;
        this.wdh4 = wdh4;
        this.imagePath = imagePath;
        //this.trainingImage = image;
    }

    protected Training(Parcel in) {
        id = in.readInt();
        nameTraining = in.readString();
        einstellungen = in.readString();
        kg1 = in.readString();
        kg2 = in.readString();
        kg3 = in.readString();
        kg4 = in.readString();
        wdh1 = in.readString();
        wdh2 = in.readString();
        wdh3 = in.readString();
        wdh4 = in.readString();
        imagePath = in.readString();
        //trainingImage = in.createByteArray();
    }

    public static final Creator<Training> CREATOR = new Creator<Training>() {
        @Override
        public Training createFromParcel(Parcel in) {
            return new Training(in);
        }

        @Override
        public Training[] newArray(int size) {
            return new Training[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrainingImage()
    {
        return imagePath;
    }

//    public byte[] getTrainingImage() {
//        return trainingImage;
//    }

    public void setTrainingImage(String imagePath)
    {
        this.imagePath = imagePath;
    }

//    public void setTrainingImage(byte[] trainingImage) {
//        this.trainingImage = trainingImage;
//    }

    public String getNameTraining() {
        return nameTraining;
    }

    public void setNameTraining(String nameTraining) {
        this.nameTraining = nameTraining;
    }

    public String getEinstellungen() {
        return einstellungen;
    }

    public void setEinstellungen(String einstellungen) {
        this.einstellungen = einstellungen;
    }

    public String getKg1() {
        return kg1;
    }

    public void setKg1(String kg1) {
        this.kg1 = kg1;
    }

    public String getKg2() {
        return kg2;
    }

    public void setKg2(String kg2) {
        this.kg2 = kg2;
    }

    public String getKg3() {
        return kg3;
    }

    public void setKg3(String kg3) {
        this.kg3 = kg3;
    }

    public String getKg4() {
        return kg4;
    }

    public void setKg4(String kg4) {
        this.kg4 = kg4;
    }

    public String getWdh1() {
        return wdh1;
    }

    public void setWdh1(String wdh1) {
        this.wdh1 = wdh1;
    }

    public String getWdh2() {
        return wdh2;
    }

    public void setWdh2(String wdh2) {
        this.wdh2 = wdh2;
    }

    public String getWdh3() {
        return wdh3;
    }

    public void setWdh3(String wdh3) {
        this.wdh3 = wdh3;
    }

    public String getWdh4() {
        return wdh4;
    }

    public void setWdh4(String wdh4) {
        this.wdh4 = wdh4;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nameTraining);
        dest.writeString(einstellungen);
        dest.writeString(kg1);
        dest.writeString(kg2);
        dest.writeString(kg3);
        dest.writeString(kg4);
        dest.writeString(wdh1);
        dest.writeString(wdh2);
        dest.writeString(wdh3);
        dest.writeString(wdh4);
        dest.writeString(imagePath);
        //dest.writeByteArray(trainingImage);
    }
}
