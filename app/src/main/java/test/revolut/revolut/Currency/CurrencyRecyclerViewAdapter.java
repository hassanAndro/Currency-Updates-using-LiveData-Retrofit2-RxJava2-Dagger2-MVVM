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

import com.mynameismidori.currencypicker.ExtendedCurrency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

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
    private AdapterCallback mAdapterCallback;
    private ViewHolder mViewHolder;
    private List<ExtendedCurrency> mCurrencyDetails = ExtendedCurrency.getAllCurrencies();

    // data is passed into the constructor
    CurrencyRecyclerViewAdapter(Context context, Rates mRates, AdapterCallback mAdapterCallback) {
        this.mInflater = LayoutInflater.from(context);
        this.mAdapterCallback = mAdapterCallback;
        this.mRates = mRates;
        this.mData = mRates.getData();
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return (null != mData ? mData.size() : 0);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_row, parent, false);
        mViewHolder = new ViewHolder(view);
        return mViewHolder;
    }

    // binds the data to the View in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String currency_main = mData.get(position).getName();
        float currency_value = mData.get(position).getValue();
        int FlagId = FlagId(currency_main);
        if (FlagId != 0) {
            holder.mCountryImage.setImageResource(FlagId);
        } else {
            holder.mCountryImage.setImageResource(R.drawable.ic_launcher_background);
        }
        holder.mCurrencyValue.setOnClickListener(null);
        holder.mCountryMain.setText(currency_main);
        holder.mCountryCurrency.setText(Currency.getInstance(mData.get(position).getName()).getDisplayName());
        if (currency_value != 0) {
            holder.mCurrencyValue.setText(Float.toString(Constant.round(currency_value, 2)));
        } else {
            holder.mCurrencyValue.setText("");
        }

        holder.mCurrencyValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (position != 0) {
                        if (mAdapterCallback != null) {
                            mAdapterCallback.scrollToTop();
                            mAdapterCallback.selectedCurrency(mData.get(position).getName());
                        }
                        Constant.mInputValue = mData.get(position).getValue();
                        swapItem(position, 0);
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
                if (position == 0) {
                    if (s != null && s.length() > 0) {
                        Constant.mInputValue = Float.valueOf(s.toString());
                    } else {
                        Constant.mInputValue = 0;
                    }
                }
            }
        });
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
//            for (int i = 0; i <= mData.size() - 1; i++) {
//                for (int j = 0; j <= mRates.getData().size() - 1; j++) {
//                    if (mData.get(i).getName().equals(mRates.getData().get(j).getName())) {
//                        Collections.swap(mRates.getData(), j, i);
//
//                    }
//                }
//            }
            mData.clear();
            mData.addAll(mRates.getData());
            notifyDataSetChanged();
        }
    }

    public void swapItem(int fromPosition, int toPosition) {
        Collections.swap(mData, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        notifyDataSetChanged();

    }

    public int FlagId(String currencyCode) {
        int id = 0;
        if (mCurrencyDetails != null) {
            for (int i = 0; i <= mCurrencyDetails.size() - 1; i++) {
                ExtendedCurrency c = mCurrencyDetails.get(i);
                if (c.getCode().toLowerCase().equals(currencyCode.toLowerCase())) {
                    id = c.getFlag();
                }
            }
        }
        return id;
    }
}