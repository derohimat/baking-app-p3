package net.derohimat.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RecipeDao extends RealmObject implements Parcelable {

    @PrimaryKey
    @SerializedName("id") private long id;
    @SerializedName("name") private String name;
    @SerializedName("servings") private int servings;
    @SerializedName("image") private String image;
    @SerializedName("ingredients") private RealmList<IngredientsDao> ingredients;
    @SerializedName("steps") private RealmList<StepsDao> steps;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public RealmList<IngredientsDao> getIngredients() {
        return ingredients;
    }

    public void setIngredients(RealmList<IngredientsDao> ingredients) {
        this.ingredients = ingredients;
    }

    public RealmList<StepsDao> getSteps() {
        return steps;
    }

    public void setSteps(RealmList<StepsDao> steps) {
        this.steps = steps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.servings);
        dest.writeString(this.image);
        dest.writeTypedList(this.ingredients);
        dest.writeTypedList(this.steps);
    }

    public RecipeDao() {
    }

    protected RecipeDao(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.servings = in.readInt();
        this.image = in.readString();
        this.ingredients = new RealmList<>();
        this.ingredients.addAll(in.createTypedArrayList(IngredientsDao.CREATOR));
        this.steps = new RealmList<>();
        this.steps.addAll(in.createTypedArrayList(StepsDao.CREATOR));
    }

    public static final Creator<RecipeDao> CREATOR = new Creator<RecipeDao>() {
        @Override
        public RecipeDao createFromParcel(Parcel source) {
            return new RecipeDao(source);
        }

        @Override
        public RecipeDao[] newArray(int size) {
            return new RecipeDao[size];
        }
    };
}