package test.revolut.revolut.Currency.CurrencyModel;

import java.util.ArrayList;

public class Rates {
    private String base;
    private ArrayList<Data> data;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }
}
