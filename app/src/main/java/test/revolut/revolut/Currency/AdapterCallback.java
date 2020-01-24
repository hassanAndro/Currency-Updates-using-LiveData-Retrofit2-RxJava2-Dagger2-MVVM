package test.revolut.revolut.Currency;


import java.util.ArrayList;

import test.revolut.revolut.Currency.CurrencyModel.ArrayData;

public interface AdapterCallback {
    void scrollToTop();

    void selectedCurrency(String newBaseCurrency, double inputValue);

    void swapCurrency(ArrayList<ArrayData> mData);
}
