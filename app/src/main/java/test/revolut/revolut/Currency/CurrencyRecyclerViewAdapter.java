package test.revolut.revolut.Currency;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Currency;

import de.hdodenhof.circleimageview.CircleImageView;
import test.revolut.revolut.Currency.CurrencyModel.Data;
import test.revolut.revolut.Currency.CurrencyModel.Rates;
import test.revolut.revolut.R;

public class CurrencyRecyclerViewAdapter extends RecyclerView.Adapter<CurrencyRecyclerViewAdapter.ViewHolder> {

    private Rates mRates;
    private ArrayList<Data> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    // data is passed into the constructor
    CurrencyRecyclerViewAdapter(Context context, Rates mRates) {
        this.mInflater = LayoutInflater.from(context);
        this.mRates = mRates;
        this.mData = mRates.getData();
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
        Double currency_value = mData.get(position).getValue();
//        holder.mCountryImage.setImageResource(R.drawable.ic_list_country_eu);
        holder.mCountryMain.setText(currency_main);
        holder.mCountryCurrency.setText(Currency.getInstance(mData.get(position).getName()).getDisplayName());
        holder.mCurrencyValue.setText(currency_value.toString());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            mCurrencyValue.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Data getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}