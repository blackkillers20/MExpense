package com.example.m_expense_final.JSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Payload {
    public static class Data {
        public String name;
        public String description;

        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description == null ? "" : description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String toJson() {
            return "{" + "\"name\": " + "\"" +getName() + "\"" + ", \"description\": " + "\"" + getDescription() + "\"" +"}";
        }
    }
    public String userId;
    public List<Data> detailList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Data> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<Data> detailList) {
        this.detailList = detailList;
    }
    public String detailListJson() {
        String json = "[";
        for (Data data : detailList) {
            json += data.toJson() + ",";
        }
        json = json.substring(0, json.length() - 1);
        json += "]";
        return json;
    }
    public String toJson() throws JSONException {
        return new JSONObject("{\"userId\":\"" + userId + "\",\"detailList\":" + detailListJson() + "}")
                .toString(4);
    }
}
