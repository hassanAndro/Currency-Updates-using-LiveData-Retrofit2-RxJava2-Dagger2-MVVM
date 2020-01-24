package test.revolut.revolut.Currency;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

    private ArrayList<Data> mData;
    private LayoutInflater mInflater;
    private AdapterCallback mAdapterCallback;
    private ViewHolder mViewHolder;
    private List<ExtendedCurrency> mCurrencyDetails = ExtendedCurrency.getAllCurrencies();

    // data is passed into the constructor
    CurrencyRecyclerViewAdapter(Context context, Rates mRates, AdapterCallback mAdapterCallback) {
        this.mInflater = LayoutInflater.from(context);
        this.mAdapterCallback = mAdapterCallback;
        this.mData = mRates.getData();
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
        double currency_value = mData.get(position).getValue();
        int FlagId = FlagId(currency_main);
        if (FlagId != 0) {
            holder.mCountryImage.setImageResource(FlagId);
        } else {
            holder.mCountryImage.setImageResource(R.drawable.ic_launcher_background);
        }
        holder.mCountryMain.setText(currency_main);
        holder.mCountryCurrency.setText(Currency.getInstance(mData.get(position).getName()).getDisplayName());

        holder.mCurrencyValue.removeTextChangedListener(holder.textWatcher);
        holder.textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (position == 0) {
                    double value = 0.00;
                    if (s != null && s.length() > 0) {
                        value = Double.valueOf(Double.valueOf(Constant.ReplaceIfString(s.toString())));
                    }
                    if (mAdapterCallback != null) {
                        mAdapterCallback.selectedCurrency(mData.get(position).getName(),
                                value);
                    }
                }
                if (!TextUtils.isEmpty(s.toString())) {
                    mData.get(position).setValue(Double.valueOf(Constant.ReplaceIfString(s.toString())));
                } else {
                    mData.get(position).setValue(0.00);
                }
                holder.mCurrencyValue.setSelection(holder.mCurrencyValue.getSelectionStart());

            }
        };

        holder.mCurrencyValue.addTextChangedListener(holder.textWatcher);

        if (currency_value != 0) {
            holder.mCurrencyValue.setText(Constant.currencyFormat(String.valueOf(currency_value)));
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
                            mAdapterCallback.selectedCurrency(mData.get(position).getName(),
                                    mData.get(position).getValue());
                        }
                        swapItem(position, 0);
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
        TextWatcher textWatcher;

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
            if (mRates != null) {
                if (mRates.getData() != null && mRates.getData().size() > 0) {
                    for (int i = 0; i <= mData.size() - 1; i++) {
                        if (i != 0) {
                            mData.get(i).setName(mRates.getData().get(i).getName());
                            mData.get(i).setValue(mRates.getData().get(i).getValue());
                            notifyItemChanged(i);
                        }
                    }
                }
            }
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