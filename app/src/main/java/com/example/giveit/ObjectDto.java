package com.example.giveit;


public class ObjectDto {
    String selectedImage, name, description, adresse;

    public ObjectDto() {
    }

// création d'un Dto de type Object

    public ObjectDto(String adresse, String description, String image, String name) {
        this.description = description;
        this.selectedImage = image;
        this.name = name;
        this.adresse=adresse;
    }


    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getImage() {
        return selectedImage;
    }

    public void setImage(String image) {
        this.selectedImage = image;
    }

    public  String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


