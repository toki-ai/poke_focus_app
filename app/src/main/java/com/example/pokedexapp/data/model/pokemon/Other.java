package com.example.pokedexapp.data.model.pokemon;

import com.google.gson.annotations.SerializedName;

public class Other {
    @SerializedName("official-artwork")
    private OfficialArtwork officialArtwork;

    public OfficialArtwork getOfficialArtwork() {
        return officialArtwork;
    }
}
