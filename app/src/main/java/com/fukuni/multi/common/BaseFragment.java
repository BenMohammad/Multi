package com.fukuni.multi.common;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fukuni.multi.common.di.PresentationCompositionRoot;
import com.techyourchance.fragmenthelper.HierarchicalFragment;

public abstract class BaseFragment extends Fragment implements HierarchicalFragment {

    private PresentationCompositionRoot presentationCompositionRoot;

    protected final PresentationCompositionRoot getCompositionRoot() {
        if(presentationCompositionRoot == null) {
            presentationCompositionRoot = new PresentationCompositionRoot(requireActivity());
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


        super.onViewCreated(view, savedInstanceState);
    }

    protected abstract String getScreenTitle();
}