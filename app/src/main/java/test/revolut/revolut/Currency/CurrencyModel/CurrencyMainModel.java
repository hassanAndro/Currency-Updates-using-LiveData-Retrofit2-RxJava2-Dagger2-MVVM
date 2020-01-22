package test.revolut.revolut.Currency.CurrencyModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class CurrencyMainModel {

    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("rates")
    @Expose
    private HashMap<String, Float> result;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public HashMap<String, Float> getResult() {
        return result;
    }

    public void setResult(HashMap<String, Float> result) {
        this.result = result;
    }
}