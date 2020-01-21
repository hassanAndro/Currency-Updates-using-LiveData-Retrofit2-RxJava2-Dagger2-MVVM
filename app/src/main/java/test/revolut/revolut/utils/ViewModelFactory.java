package test.revolut.revolut.utils;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import test.revolut.revolut.Currency.CurrencyViewModel;
import test.revolut.revolut.Currency.Repository;


public class ViewModelFactory implements ViewModelProvider.Factory {

    private Repository repository;

    @Inject
    public ViewModelFactory(Repository repository) {
        this.repository = repository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CurrencyViewModel.class)) {
            return (T) new CurrencyViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}
