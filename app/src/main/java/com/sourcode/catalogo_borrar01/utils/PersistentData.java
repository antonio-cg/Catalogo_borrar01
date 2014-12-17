package com.sourcode.catalogo_borrar01.utils;

import com.sourcode.catalogo_borrar01.model.Flower;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SourCodeV on 17/12/14.
 */
public class PersistentData {
    private static PersistentData instance;

    private PersistentData()
    {}

    public List<Flower> getFlowers() {
        return flowers;
    }

    public void setFlowers(List<Flower> flowers) {
        this.flowers = flowers;
    }

    private List<Flower> flowers = null;




    public static synchronized PersistentData getInstance()
    {
        if(instance == null)
        {
            instance = new PersistentData();

        }
        return instance;
    }



}
