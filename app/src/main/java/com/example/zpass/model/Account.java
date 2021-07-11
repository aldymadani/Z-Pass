package com.example.zpass.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public class Account implements Parcelable {
    @Exclude
    public static final String FIELD_NAME = "NAME";
    @Exclude
    public static final String FIELD_EMAIL = "EMAIL";
    @Exclude
    public static final String FIELD_PASSWORD = "PASSWORD";
    @Exclude
    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Exclude
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Exclude
        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    String id, name, email, password, imageUrl, owner;

    public Account() {
        // Empty Constructor
    }

    public Account(String name, String email, String password, String imageURL) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.imageUrl = imageURL;
    }

    protected Account(Parcel in) {
        name = in.readString();
        email = in.readString();
        password = in.readString();
        imageUrl = in.readString();
        owner = in.readString();
        id = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // -------
    @Exclude
    public boolean checkEmptyData() {
        return (name == null || name.isEmpty())
                || (email == null || email.isEmpty())
                || (password == null || password.isEmpty());
    }

    @Exclude
    public String checkEmptyInput() {
        if (name == null || name.isEmpty()) {
            return FIELD_NAME;
        } else if (email == null || email.isEmpty()) {
            return FIELD_EMAIL;
        } else if (password == null || password.isEmpty()) {
            return FIELD_PASSWORD;
        } else {
            return null;
        }
    }

    @Exclude
    public HashMap<String, Object> toMap() {
        final HashMap<String, Object> map = new HashMap<>();

        map.put("id", id);
        map.put("name", name);
        map.put("email", email);
        map.put("password", password);
        map.put("imageUrl", imageUrl);
        map.put("owner", owner);

        return map;
    }
    // -------


    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", userUUID='" + owner + '\'' +
                ", accountId='" + id + '\'' +
                '}';
    }

    @Exclude
    @Override
    public int describeContents() {
        return 0;
    }

    @Exclude
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(imageUrl);
        dest.writeString(owner);
        dest.writeString(id);
    }
}
