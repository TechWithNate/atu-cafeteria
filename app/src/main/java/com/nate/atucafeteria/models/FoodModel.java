package com.nate.atucafeteria.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodModel implements Parcelable {
    private String id;
    private String buyerId;
    private String imageUrl;
    private String name;
    private String price;
    private String oldPrice;
    private String readyTime;
    private String deliveryStatus;
    private String orderTime;
    private String location;
    private String deliveryType;
    private String ingred;
//    private Map<String, List<Ingredient>> foodIngredientsMap;

    public FoodModel() {
    }

    public FoodModel(String id, String imageUrl, String name, String price, String oldPrice, String readyTime, String deliveryStatus, String orderTime, String location, String deliveryType, String ingred) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
        this.oldPrice = oldPrice;
        this.readyTime = readyTime;
        this.deliveryStatus = deliveryStatus;
        this.orderTime = orderTime;
        this.location = location;
        this.deliveryType = deliveryType;
        this.ingred = ingred;
    }

    public FoodModel(String id, String imageUrl, String name, String price, String oldPrice, String readyTime, String ingred) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
        this.oldPrice = oldPrice;
        this.readyTime = readyTime;
        this.ingred = ingred;
    }

    protected FoodModel(Parcel in) {
        id = in.readString();
        buyerId = in.readString();
        imageUrl = in.readString();
        name = in.readString();
        price = in.readString();
        oldPrice = in.readString();
        readyTime = in.readString();
        deliveryStatus = in.readString();
        orderTime = in.readString();
        location = in.readString();
        deliveryType = in.readString();
        ingred = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(buyerId);
        dest.writeString(imageUrl);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(oldPrice);
        dest.writeString(readyTime);
        dest.writeString(deliveryStatus);
        dest.writeString(orderTime);
        dest.writeString(location);
        dest.writeString(deliveryType);
        dest.writeString(ingred);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FoodModel> CREATOR = new Creator<FoodModel>() {
        @Override
        public FoodModel createFromParcel(Parcel in) {
            return new FoodModel(in);
        }

        @Override
        public FoodModel[] newArray(int size) {
            return new FoodModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getReadyTime() {
        return readyTime;
    }

    public void setReadyTime(String readyTime) {
        this.readyTime = readyTime;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getIngred() {
        return ingred;
    }

    public void setIngred(String ingred) {
        this.ingred = ingred;
    }
}
