package Model;

import java.io.Serializable;

/**
 * Created by Billy on 9/14/2017.
 */

public class Restaurant implements Serializable{
    private int id;
    String title;
    String address;
    String restype;
    String food;

    byte[] image;
    String imageString;
    double lati,longti;

    public Restaurant(String title, String address, String restype, byte[] image) {
        this.title = title;
        this.address = address;
        this.restype = restype;
        this.image = image;
    }

    //in readData to list
    public Restaurant(int id, String title, String address, byte[] image, double lati, double longti) {
        this.lati = lati;
        this.longti = longti;
        this.id = id;
        this.title = title;
        this.address = address;
        this.image = image;
    }

    //link image
    public Restaurant(String title, String address, String imageString, double lati, double longti) {
        this.title = title;
        this.address = address;
        this.lati = lati;
        this.longti = longti;
        this.imageString = imageString;
    }

    public Restaurant(String title, String address, String restype, String food, double lati, double longti, byte[] image) {
        this.title = title;
        this.address = address;
        this.restype = restype;
        this.food = food;
        this.lati = lati;
        this.longti = longti;
        this.image = image;
    }


    //Getter setter
    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageString() {
        return imageString;
    }
    public void setImageString(String imageString) {
        this.imageString = imageString;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getRestype() {
        return restype;
    }

    public void setRestype(String restype) {
        this.restype = restype;
    }

    public String getFood() {
        return food;
    }
    public void setFood(String food) {
        this.food = food;
    }

    public double getLati() {
        return lati;
    }
    public void setLati(double lati) {
        this.lati = lati;
    }

    public double getLongti() {
        return longti;
    }
    public void setLongti(double longti) {
        this.longti = longti;
    }

}
