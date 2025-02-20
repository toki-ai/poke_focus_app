package com.example.pokedexapp.data.model.pokemon;
import com.google.gson.annotations.SerializedName;

public class Pokemon {
    @SerializedName("name")
    private String name;

    @SerializedName("sprites")
    private Sprites sprites;

    public String getName() {
        return name;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public String getImage(){
        return sprites != null ? sprites.getFrontDefault() : null;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "name='" + name + '\'' +
                ", sprites=" + sprites +
                '}';
    }
}
