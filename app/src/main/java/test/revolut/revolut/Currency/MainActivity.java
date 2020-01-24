package test.revolut.revolut.Currency;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Collections;
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
            for (Map.Entry<String, Float> entry : map.entrySet()) {
                Data data_value = new Data();
                data_value.setName(entry.getKey());
                data_value.setValue(entry.getValue());
                data.add(data_value);
            }

            Rates rates = new Rates();
            rates.setBase(Constant.BASE_CURRENCY_SELECTED);


            if (data != null && data.size() > 0) {
                ArrayList<Data> mData = Constant.convertCurrency(data);
                rates.setData(addBaseCurrencyOnTop(mData));
            } else {
                rates.setData(null);
            }

            RecyclerView.ItemAnimator animator = mRecyclerView.getItemAnimator();
            if (animator instanceof SimpleItemAnimator) {
                ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
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

    public ArrayList<Data> addBaseCurrencyOnTop(ArrayList<Data> mData) {
        if (mData != null && mData.size() > 0) {
            Data d = new Data();
            d.setName(Constant.BASE_CURRENCY_SELECTED);
            d.setValue(Constant.mInputValue);
            mData.add(0, d);
        }
        return mData;
    }

    @Override
    public void scrollToTop() {
        if (mRecyclerView != null) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void selectedCurrency(String newBaseCurrency) {
        Constant.BASE_CURRENCY_SELECTED = newBaseCurrency;
    }
}
