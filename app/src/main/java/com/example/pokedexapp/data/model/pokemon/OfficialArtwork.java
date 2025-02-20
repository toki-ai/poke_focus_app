package com.example.pokedexapp.data.model.pokemon;
import com.google.gson.annotations.SerializedName;

public class OfficialArtwork {
    @SerializedName("front_default")
    private String frontDefault;

    public String getFrontDefault() {
        return frontDefault;
    }
}
