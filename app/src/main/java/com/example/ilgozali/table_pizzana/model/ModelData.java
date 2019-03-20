package com.example.ilgozali.table_pizzana.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelData {
    @SerializedName("custno")
    @Expose
    private String custno;
    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("finish")
    @Expose
    private String finish;
    @SerializedName("status")
    @Expose
    private String status;

    public String getCustno() {
        return custno;
    }

    public void setCustno(String custno) {
        this.custno = custno;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}