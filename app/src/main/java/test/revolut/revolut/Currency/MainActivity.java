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


public class MainActivity extends AppCompatActivity {

    @Inject
    ViewModelFactory viewModelFactory;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private CurrencyRecyclerViewAdapter adapter;

    private CurrencyViewModel viewModel;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = Constant.getProgressDialog(this, getString(R.string.loading_text));

        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ((MyApplication) getApplication()).getAppComponent().doInjection(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrencyViewModel.class);

        viewModel.currencyResponse().observe(this, this::consumeResponse);
        viewModel.hitCurrencyApi();

    }

    /*
     * method to handle response
     * */
    private void consumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {

            case LOADING:
                progressDialog.show();
                break;

            case SUCCESS:
                progressDialog.dismiss();
                renderSuccessResponse(apiResponse.data);
                break;

            case ERROR:
                progressDialog.dismiss();
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
            HashMap<String, Double> map = model.getResult();

            for (Map.Entry<String, Double> entry : map.entrySet()) {
                Data data_value = new Data();
                data_value.setName(entry.getKey());
                data_value.setValue(entry.getValue());
                data.add(data_value);
            }

            Rates rates = new Rates();
            if (!TextUtils.isEmpty(model.getBase())) {
                rates.setBase(model.getBase());
            } else {
                rates.setBase("EUR");
            }
            if (data != null) {
                rates.setData(data);
            } else {
                rates.setData(null);
            }
            if (adapter==null){
                adapter = new CurrencyRecyclerViewAdapter(this, rates);
                mRecyclerView.setAdapter(adapter);
            }else {
                adapter.update(rates);
            }
        } else {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
        }
    }
}
