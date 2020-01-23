package test.revolut.revolut.Currency;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import test.revolut.revolut.Currency.CurrencyModel.CurrencyMainModel;
import test.revolut.revolut.Currency.CurrencyModel.Data;
import test.revolut.revolut.Currency.CurrencyModel.Rates;
import test.revolut.revolut.MyApplication;
import test.revolut.revolut.R;
import test.revolut.revolut.utils.ApiResponse;
import test.revolut.revolut.utils.Constant;
import test.revolut.revolut.utils.ViewModelFactory;


public class MainActivity extends AppCompatActivity implements AdapterCallback {

    @Inject
    ViewModelFactory viewModelFactory;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private CurrencyRecyclerViewAdapter mAdapter;
    private CurrencyViewModel mViewModel;
    private ProgressDialog mProgressDialog;
    private String mBaseCurrency = "";
    private float mBaseValue = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressDialog = Constant.getProgressDialog(this, getString(R.string.loading_text));

        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ((MyApplication) getApplication()).getAppComponent().doInjection(this);

        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrencyViewModel.class);
        mViewModel.currencyResponse().observe(this, this::consumeResponse);
        mViewModel.hitCurrencyApi();

    }

    /*
     * method to handle response
     * */
    private void consumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {

            case LOADING:
                mProgressDialog.show();
                break;

            case SUCCESS:
                mProgressDialog.dismiss();
                renderSuccessResponse(apiResponse.data);
                break;

            case ERROR:
                mProgressDialog.dismiss();
                Toast.makeText(MainActivity.this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    /*
     * method to handle success response
     * */
    private void renderSuccessResponse(JsonElement response) {
        if (!response.isJsonNull()) {
            Gson gson = new Gson();
            CurrencyMainModel model = gson.fromJson(response, CurrencyMainModel.class);

            ArrayList<Data> data = new ArrayList<Data>();
            HashMap<String, Float> map = model.getResult();
            Data d;
            for (Map.Entry<String, Float> entry : map.entrySet()) {
                if (!TextUtils.isEmpty(mBaseCurrency)) {
                    if (mBaseCurrency.toLowerCase().equals(entry.getKey().toLowerCase())) {
                        d = addDataCurrencies(model.getBase(), 0);
                    } else {
                        d = addDataCurrencies(entry.getKey(), entry.getValue());
                    }
                } else {
                    d = addDataCurrencies(entry.getKey(), entry.getValue());
                }
                data.add(d);
            }

            Rates rates = new Rates();
            if (TextUtils.isEmpty(mBaseCurrency)) {
                mBaseCurrency = model.getBase();
            }
            rates.setBase(mBaseCurrency);


            if (data != null && data.size() > 0) {

                rates.setData(data);
            } else {
                rates.setData(null);
            }
            if (mAdapter == null) {
                mAdapter = new CurrencyRecyclerViewAdapter(this, rates, this);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.update(rates);
            }
        } else {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
        }
    }

    public Data addDataCurrencies(String name, float value) {
        Data data = new Data();
        data.setName(name);
        data.setValue(value);
        return data;
    }

    public ArrayList<Data> convertCurrency(ArrayList<Data> arrayList) {
        ArrayList<Data> dataArrayList = new ArrayList<>();
        for (int i = 0; i <= arrayList.size() - 1; i++) {
            Data d = new Data();
            d.setName(arrayList.get(i).getName());
            d.setValue(arrayList.get(i).getValue() * mBaseValue);
            dataArrayList.add(d);
        }
        return dataArrayList;
    }

    @Override
    public void scrollToTop() {
        if (mRecyclerView != null) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void selectedCurrency(String newBaseCurrency, float value) {
        mBaseCurrency = newBaseCurrency;
        mBaseValue = value;
    }
}
