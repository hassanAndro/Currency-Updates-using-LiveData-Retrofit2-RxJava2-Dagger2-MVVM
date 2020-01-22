package test.revolut.revolut.Currency;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;

import de.hdodenhof.circleimageview.CircleImageView;
import test.revolut.revolut.Currency.CurrencyModel.Data;
import test.revolut.revolut.Currency.CurrencyModel.Rates;
import test.revolut.revolut.R;
import test.revolut.revolut.utils.Constant;

public class CurrencyRecyclerViewAdapter extends RecyclerView.Adapter<CurrencyRecyclerViewAdapter.ViewHolder> {

    private Rates mRates;
    private ArrayList<Data> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    float mInputValue = 1;
    AdapterCallback mAdapterCallback;

    // data is passed into the constructor
    CurrencyRecyclerViewAdapter(Context context, Rates mRates, AdapterCallback mAdapterCallback) {
        this.mInflater = LayoutInflater.from(context);
        this.mAdapterCallback = mAdapterCallback;
        this.mRates = mRates;
        this.mData = convertCurrency(mRates.getData());
        addBaseCurrencyOnTop(mRates);
        this.mContext = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the View in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String currency_main = mData.get(position).getName();
        float currency_value = mData.get(position).getValue();
//        holder.mCountryImage.setImageResource(R.drawable.ic_list_country_eu);
        holder.mCountryMain.setText(currency_main);
        holder.mCountryCurrency.setText(Currency.getInstance(mData.get(position).getName()).getDisplayName());
        holder.mCurrencyValue.setText(Float.toString(Constant.round(currency_value, 2)));

        holder.mCurrencyValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != 0) {
                    swapeItem(position, 0);
                    if (mAdapterCallback != null) {
                        mAdapterCallback.scrollToTop();
                        mAdapterCallback.selectedCurrency(mData.get(position).getName(), mData.get(position).getValue());
                    }
                }
            }
        });
        holder.mCurrencyValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0 && s.charAt(s.length() - 1) == ' ') {
                    mInputValue = Float.valueOf(s.toString());
                }
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        } else {
            return 0;
        }
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mCountryImage;
        TextView mCountryMain;
        TextView mCountryCurrency;
        AppCompatEditText mCurrencyValue;

        ViewHolder(View itemView) {
            super(itemView);
            mCountryMain = itemView.findViewById(R.id.country_main);
            mCountryCurrency = itemView.findViewById(R.id.country_currency);
            mCurrencyValue = itemView.findViewById(R.id.currency_value);
            mCountryImage = itemView.findViewById(R.id.country_flag);
        }
    }

    public void update(Rates mRates) {
        if (mRates != null) {
            mData.clear();
            mData.addAll(convertCurrency(mRates.getData()));
            addBaseCurrencyOnTop(mRates);
            notifyDataSetChanged();
        }
    }

    public ArrayList<Data> convertCurrency(ArrayList<Data> arrayList) {
        ArrayList<Data> dataArrayList = new ArrayList<>();
        for (int i = 0; i <= arrayList.size() - 1; i++) {
            Data d = new Data();
            d.setName(arrayList.get(i).getName());
            d.setValue(arrayList.get(i).getValue() * mInputValue);
            dataArrayList.add(d);
        }
        return dataArrayList;
    }

    public void addBaseCurrencyOnTop(Rates mRates) {
        if (mRates != null) {
            if (mRates.getData() != null) {
                if (mData != null && mData.size() > 0) {
                    Data d = new Data();
                    Log.e("TAGx","inside: "+mRates.getBase());
                    d.setName(mRates.getBase());
                    d.setValue(mInputValue);
                    mData.add(0, d);
                    notifyItemInserted(0);
                }

            }
        }
    }

    public void swapeItem(int fromPosition, int toPosition) {
        Collections.swap(mData, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        notifyDataSetChanged();
    }
}