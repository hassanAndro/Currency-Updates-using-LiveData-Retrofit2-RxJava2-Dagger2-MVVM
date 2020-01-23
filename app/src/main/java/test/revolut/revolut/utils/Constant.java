package test.revolut.revolut.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;

import test.revolut.revolut.Currency.CurrencyModel.Data;


public class Constant {

    public static float mInputValue = 1;
    public static String BASE_CURRENCY_SELECTED = "EUR";

    public static ProgressDialog getProgressDialog(Context context, String msg) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    public static ArrayList<Data> convertCurrency(ArrayList<Data> arrayList) {
        ArrayList<Data> dataArrayList = new ArrayList<>();
        for (int i = 0; i <= arrayList.size() - 1; i++) {
            Data d = new Data();
            d.setName(arrayList.get(i).getName());
            d.setValue(arrayList.get(i).getValue() * mInputValue);
            dataArrayList.add(d);
        }
        return dataArrayList;
    }


    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static float round(float d, int decimalPlace) {
        return BigDecimal.valueOf(d).setScale(decimalPlace, BigDecimal.ROUND_HALF_UP).floatValue();
    }

}
