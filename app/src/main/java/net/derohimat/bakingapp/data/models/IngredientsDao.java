package net.derohimat.bakingapp.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class IngredientsDao extends RealmObject implements Parcelable {

    @SerializedName("quantity") private float quantity;
    @SerializedName("measure") private String measure;
    @SerializedName("ingredient") private String ingredient;

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.quantity);
        dest.writeString(this.measure);
        dest.writeString(this.ingredient);
    }

    public IngredientsDao() {
    }

    protected IngredientsDao(Parcel in) {
        this.quantity = in.readFloat();
        this.measure = in.readString();
        this.ingredient = in.readString();
    }

    public static final Creator<IngredientsDao> CREATOR = new Creator<IngredientsDao>() {
        @Override
        public IngredientsDao createFromParcel(Parcel source) {
            return new IngredientsDao(source);
        }

        @Override
        public IngredientsDao[] newArray(int size) {
            return new IngredientsDao[size];
        }
    };
}

    