package com.golf.golfnation.game.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by NgocNQ on 8/21/2015.
 */
public class GameType implements Serializable{
    @SerializedName("game_type_id")
    private String typeId;
    @SerializedName("game_type_name")
    private String typeName;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
