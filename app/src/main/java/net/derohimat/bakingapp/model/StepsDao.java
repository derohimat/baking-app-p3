package net.derohimat.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class StepsDao extends RealmObject implements Parcelable {

    @PrimaryKey
    @SerializedName("id") private long id;
    @SerializedName("shortDescription") private String shortDescription;
    @SerializedName("description") private String description;
    @SerializedName("videoURL") private String videoURL;
    @SerializedName("thumbnailURL") private String thumbnailURL;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.shortDescription);
        dest.writeString(this.description);
        dest.writeString(this.videoURL);
        dest.writeString(this.thumbnailURL);
    }

    public StepsDao() {
    }

    protected StepsDao(Parcel in) {
        this.id = in.readLong();
        this.shortDescription = in.readString();
        this.description = in.readString();
        this.videoURL = in.readString();
        this.thumbnailURL = in.readString();
    }

    public static final Creator<StepsDao> CREATOR = new Creator<StepsDao>() {
        @Override
        public StepsDao createFromParcel(Parcel source) {
            return new StepsDao(source);
        }

        @Override
        public StepsDao[] newArray(int size) {
            return new StepsDao[size];
        }
    };
}