package test.revolut.revolut.Currency;

import android.util.Log;

import com.google.gson.JsonElement;

import io.reactivex.Observable;
import test.revolut.revolut.utils.ApiCallInterface;
import test.revolut.revolut.utils.Constant;


public class Repository {

    private ApiCallInterface apiCallInterface;

    public Repository(ApiCallInterface apiCallInterface) {
        this.apiCallInterface = apiCallInterface;
    }

    /*
     * method to call Currency api
     * */
    public Observable<JsonElement> executeCurrencyApi(String base) {
        Log.e("ascas","base: "+base);
        return apiCallInterface.CurrencyApi(Constant.BASE_CURRENCY_SELECTED);
    }

}
