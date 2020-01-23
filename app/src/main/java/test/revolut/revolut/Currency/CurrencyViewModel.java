package test.revolut.revolut.Currency;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import test.revolut.revolut.utils.ApiResponse;
import test.revolut.revolut.utils.Constant;


public class CurrencyViewModel extends ViewModel {

    private Repository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();


    public CurrencyViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<ApiResponse> currencyResponse() {
        return responseLiveData;
    }

    /*
     * method to call normal currency api
     * */
    public void hitCurrencyApi() {

        disposables.add(
                Observable
                        .defer(() -> Observable.just(repository.executeCurrencyApi(Constant.BASE_CURRENCY_SELECTED)))
//                Repeat After every second
                        .delay(1000, TimeUnit.MILLISECONDS)
                        .repeat()
//                Repeat After every second When previous call is completed
//                        .repeatWhen(completed -> completed.delay(1, TimeUnit.SECONDS))
                        .flatMap(user -> repository.executeCurrencyApi(Constant.BASE_CURRENCY_SELECTED))
                        .repeatWhen(done -> done.delay(1, TimeUnit.SECONDS))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe((d) -> responseLiveData.setValue(ApiResponse.loading()))
                        .subscribe(
                                result -> responseLiveData.setValue(ApiResponse.success(result)),
                                throwable -> responseLiveData.setValue(ApiResponse.error(throwable))
                        )
        );

    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
