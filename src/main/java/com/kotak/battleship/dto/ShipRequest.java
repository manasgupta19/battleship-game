package com.kotak.battleship.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ShipRequest {
    private String id;
    private int size;

    @JsonProperty("xA") // Explicitly map JSON "xA" to this field
    private int xA;

    @JsonProperty("yA")
    private int yA;

    @JsonProperty("xB")
    private int xB;

    @JsonProperty("yB")
    private int yB;
}