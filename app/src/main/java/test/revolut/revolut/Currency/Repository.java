package test.revolut.revolut.Currency;

import com.google.gson.JsonElement;

import io.reactivex.Observable;
import test.revolut.revolut.utils.ApiCallInterface;


public class Repository {

    private ApiCallInterface apiCallInterface;

    public Repository(ApiCallInterface apiCallInterface) {
        this.apiCallInterface = apiCallInterface;
    }

    /*
     * method to call Currency api
     * */
    public Observable<JsonElement> executeCurrencyApi() {
        return apiCallInterface.CurrencyApi();
    }

}
