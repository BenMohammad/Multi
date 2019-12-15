package com.fukuni.multi.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fukuni.multi.R;
import com.fukuni.multi.common.BaseFragment;
import com.fukuni.multi.common.ScreenNavigator;

import java.util.List;

public class HomeFragment extends BaseFragment implements HomeArrayAdapter.Listener {

    public static Fragment newInstance() {
        return new HomeFragment();
    }

    private ScreenNavigator screenNavigator;
    private ListView mListScreenReachableFromHome;
    private HomeArrayAdapter homeArrayAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        screenNavigator = getCompositionRoot().getScreenNavigator();

        homeArrayAdapter = new HomeArrayAdapter(requireContext(), this);
        mListScreenReachableFromHome = view.findViewById(R.id.list_screens);
        mListScreenReachableFromHome.setAdapter(homeArrayAdapter);

        return view;
    }

    @Override
    protected String getScreenTitle() {
        return "Home Screen";
    }



    @Override
    public void onScreenClicked(ScreenReachableFromHome screenReachableFromHome) {
        switch (screenReachableFromHome) {
            case TEMP:
                Toast.makeText(getContext(), "Temp Clicked", Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public Fragment getHierarchicalParentFragment() {
        return null;
    }
}
