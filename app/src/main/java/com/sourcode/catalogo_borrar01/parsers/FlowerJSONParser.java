package com.sourcode.catalogo_borrar01.parsers;

import com.sourcode.catalogo_borrar01.model.Flower;
import com.sourcode.catalogo_borrar01.model.Generos;
import com.sourcode.catalogo_borrar01.utils.PersistentData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SourCode on 17/12/2014.
 */
public class FlowerJSONParser {

    public static List<Flower> parseFeed(String content){
        try {
            JSONArray ar = new JSONArray(content);
            List<Flower> flowerList = new ArrayList<>();

            for(int i=0; i<ar.length(); i++){

                JSONObject obj = ar.getJSONObject(i);
                Flower flower = new Flower();

                flower.setProductID(obj.getInt("productId"));
                flower.setName(obj.getString("name"));
                flower.setCategory(obj.getString("category"));
                flower.setInstructions(obj.getString("instructions"));
                flower.setPrice(obj.getDouble("price"));
                flower.setPhoto(obj.getString("photo"));

                flowerList.add(flower);
            }
            PersistentData.getInstance().setFlowers(flowerList);
            return flowerList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static List<Generos> parseFeedGeneros(String content) {
        try {

            JSONObject jsonObject = new JSONObject(content).getJSONObject("generos");
            JSONArray ar =  (JSONArray) jsonObject.get("item");

            List<Generos> generosList = new ArrayList<>();

            for(int i=0; i<ar.length(); i++){

                JSONObject obj = ar.getJSONObject(i);
                Generos generos = new Generos();

                generos.setId(obj.getInt("id"));
                generos.setNombre(obj.getString("nombre"));
                generos.setDescripcion(obj.getString("descripcion"));

                generosList.add(generos);
            }

            return generosList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
