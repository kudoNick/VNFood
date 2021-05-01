package vn.poly.jeanshop.src.module.register.model;

import org.json.JSONException;
import org.json.JSONObject;

public class DataCity {
    String id, title;


    public DataCity() {
    }

    public DataCity(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("ID")){
            id = jsonObject.getString("ID");
        }
        if (jsonObject.has("Title")) {
            title = jsonObject.getString("Title");
        }
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
