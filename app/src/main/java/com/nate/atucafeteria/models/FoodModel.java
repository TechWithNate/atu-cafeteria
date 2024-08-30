package com.nate.atucafeteria.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

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



    public FoodModel() {
    }

    public FoodModel(String id, String imageUrl, String name, String price, String oldPrice, String readyTime) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
        this.oldPrice = oldPrice;
        this.readyTime = readyTime;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
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

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setReadyTime(String readyTime) {
        this.readyTime = readyTime;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    @Override
    public String toString() {
        return "FoodModel{" +
                "id='" + id + '\'' +
                ", buyerId='" + buyerId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", oldPrice='" + oldPrice + '\'' +
                ", readyTime='" + readyTime + '\'' +
                ", deliveryStatus='" + deliveryStatus + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", location='" + location + '\'' +
                ", deliveryType='" + deliveryType + '\'' +
                '}';
    }
}
