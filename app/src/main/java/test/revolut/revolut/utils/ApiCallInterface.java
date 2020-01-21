package test.revolut.revolut.utils;

import com.google.gson.JsonElement;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;




public interface ApiCallInterface {

    @GET(Urls.Currency)
    Observable<JsonElement> CurrencyApi();
}
