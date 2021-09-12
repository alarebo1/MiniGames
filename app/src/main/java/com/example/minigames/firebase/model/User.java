package com.example.minigames.firebase.model;

import com.google.firebase.database.Exclude;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import com.example.minigames.ultis.Constants;

public class User {

    private String id;
    private String email;
    private String name;
    private long scoreGame1, scoreGame2, scoreGame3, scoreGame4, scoreGame5;
    private int Currency;
    public int item1;
    public int item2;
    public int item3;
    public int item4;
    public int item5;
    public int item6;
    public int item7;
    public int item8;

    public User() {
    }

    public User(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getScoreGame1() {
        return scoreGame1;
    }

    public void setScoreGame1(long scoreGame1) {
        this.scoreGame1 = scoreGame1;
    }

    public long getScoreGame2() {
        return scoreGame2;
    }

    public void setScoreGame2(long scoreGame2) {
        this.scoreGame2 = scoreGame2;
    }

    public long getScoreGame3() {
        return scoreGame3;
    }

    public void setScoreGame3(long scoreGame3) {
        this.scoreGame3 = scoreGame3;
    }

    public long getScoreGame4() {
        return scoreGame4;
    }

    public void setScoreGame4(long scoreGame4) {
        this.scoreGame4 = scoreGame4;
    }

    public long getScoreGame5() {
        return scoreGame5;
    }

    public void setScoreGame5(long scoreGame5) {
        this.scoreGame5 = scoreGame5;
    }

    public int getCurrency() {
        return Currency;
    }

    public void setCurrency(int Currency) {
        this.Currency = Currency;
    }

    public int getItem8() {
        return item8;
    }

    public void setItem8(int item0) {
        this.item8 = item8;
    }

    public int getItem1() {
        return item1;
    }

    public void setItem1(int item1) {
        this.item1 = item1;
    }

    public int getItem2() {
        return item2;
    }

    public void setItem2(int item2) {
        this.item2 = item2;
    }

    public int getItem3() {
        return item3;
    }

    public void setItem3(int item3) {
        this.item3 = item3;
    }

    public int getItem4() {
        return item4;
    }

    public void setItem4(int item4) {
        this.item4 = item4;
    }

    public int getItem5() {
        return item5;
    }

    public void setItem5(int item5) {
        this.item5 = item5;
    }

    public int getItem6() {
        return item6;
    }

    public void setItem6(int item6) {
        this.item6 = item6;
    }

    public int getItem7() {
        return item7;
    }

    public void setItem7(int item7) {
        this.item7 = item7;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(Constants.User.ID, id);
        result.put(Constants.User.EMAIL, email);
        result.put(Constants.User.NAME, name);
        return result;
    }

}
