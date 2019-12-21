package com.fukuni.multi.common;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fukuni.multi.MyApplication;
import com.fukuni.multi.common.di.PresentationCompositionRoot;
import com.fukuni.multi.home.HomeFragment;
import com.techyourchance.fragmenthelper.HierarchicalFragment;

public abstract class BaseFragment extends Fragment implements HierarchicalFragment {

    private PresentationCompositionRoot presentationCompositionRoot;

    protected final PresentationCompositionRoot getCompositionRoot() {
        if(presentationCompositionRoot == null) {
            presentationCompositionRoot = new PresentationCompositionRoot(requireActivity(),
                    ((MyApplication)requireActivity().getApplication()).getApplicationCompositionRoot());
        }
        return presentationCompositionRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ToolbarManipulator toolbarManipulator = getCompositionRoot().getToolbarManipulator();
        toolbarManipulator.setScreenTitle(getScreenTitle());
        if(getHierarchicalParentFragment() != null) {
            toolbarManipulator.showUpButton();
        } else {
            toolbarManipulator.hideUpButton();
        }


    }

    @Nullable
    @Override
    public Fragment getHierarchicalParentFragment() {
        return HomeFragment.newInstance();
    }

    protected abstract String getScreenTitle();
}
