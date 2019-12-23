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

        homeArrayAdapter.addAll(ScreenReachableFromHome.values());
        homeArrayAdapter.notifyDataSetChanged();
        return view;
    }

    @Override
    protected String getScreenTitle() {
        return "Home Screen";
    }



    @Override
    public void onScreenClicked(ScreenReachableFromHome screenReachableFromHome) {
        switch (screenReachableFromHome) {
            case UIHANDLER_DEMO:
                screenNavigator.toUiHandlerDemo();
                break;
            case UITHREAD_DEMO:
                screenNavigator.toUiThreadDemo();
                break;
            case EXERCISE_1:
                screenNavigator.toExercise1();
                break;
            case EXERCISE_2:
                screenNavigator.toExercise2();
                break;
            case CUSTOM_HANDLER:
                screenNavigator.toCustomHandlerDemo();
                break;
            case ATOMICITY:
                screenNavigator.toAtomicityDemo();
                break;
            case EXERCISE_3:
                screenNavigator.toExercise3();
                break;
            case EXERCISE_4:
                screenNavigator.toExercise4();
                break;

            case THREAD_WAIT:
                screenNavigator.toThreadWaitDemo();
                break;
            case EXERCISE_5:
                screenNavigator.toExercise5();
                break;
            case DESIGN_WITH_THREADS:
                screenNavigator.toDesignWithThreads();
                break;
            case EXERCISE_6:
                screenNavigator.toExercise6();
                break;
            case DESIGN_THREAD_POOL:
                screenNavigator.toDesignWithThreadPool();
                break;
            case EXERCISE_7:
                screenNavigator.toExercise7();
                break;
            case DESIGN_WITH_ASYNC:
                screenNavigator.toDesignWithAsync();
                break;
            case DESIGN_WITH_THREADPOSTER:
                screenNavigator.toDesignWithThreadPoster();
                break;
            case EXERCISE_8:
                screenNavigator.toExercise8();
                break;
            case DESIGN_RXJAVA:
                screenNavigator.toDesignWithRxJava();
                break;
        }
    }

    @Nullable
    @Override
    public Fragment getHierarchicalParentFragment() {
        return null;
    }
}
