package vn.poly.jeanshop.src.module.register.model;

import org.json.JSONException;
import org.json.JSONObject;

public class DataDistrict {
    String id, title;


    public DataDistrict() {
    }

    public DataDistrict(JSONObject jsonObject) throws JSONException {
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
