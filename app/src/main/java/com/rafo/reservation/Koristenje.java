package com.rafo.reservation;

/**
 * Created by Ja sam Rafoooo on 6.1.2016..
 */
public class Koristenje {

    public int id;
    public String time;
    public int item_id;
    public int user_id;
    public String createdAt;
    public String deletedAt;

    public Koristenje(String createdAt, String deletedAt, int id, int item_id, String time, int user_id) {
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.id = id;
        this.item_id = item_id;
        this.time = time;
        this.user_id = user_id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
