package com.fukuni.multi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fukuni.multi.common.ScreenNavigator;
import com.fukuni.multi.common.ToolbarManipulator;
import com.fukuni.multi.common.di.PresentationCompositionRoot;
import com.techyourchance.fragmenthelper.FragmentContainerWrapper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class MainActivity extends AppCompatActivity implements FragmentContainerWrapper, ToolbarManipulator {

    private PresentationCompositionRoot presentationCompositionRoot;
    private ScreenNavigator screenNavigator;
    private ImageButton mBackBtn;
    private TextView mTxtScreenTitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presentationCompositionRoot = new PresentationCompositionRoot(this);
        screenNavigator = presentationCompositionRoot.getScreenNavigator();

        mBackBtn = findViewById(R.id.backBtn);
        mTxtScreenTitle = findViewById(R.id.txt_screen_title);
        mBackBtn.setOnClickListener(v -> screenNavigator.naigateBack());

        if(savedInstanceState == null) {
            screenNavigator.toHomeScreen();
        }

        reduceChoreographerSkippedFramesWarningThreshold();
    }

    private void reduceChoreographerSkippedFramesWarningThreshold() {
        Field field = null;
        try {
            field = Choreographer.class.getDeclaredField("SKIPPED_FRAME_WARNING_LIMIT");
            field.setAccessible(true);
            field.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, 1);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            //failed
        }
    }



    @Override
    public void setScreenTitle(String screenTitle) {
        mTxtScreenTitle.setText(screenTitle);
    }

    @Override
    public void showUpButton() {
        mBackBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        screenNavigator.naigateBack();
    }

    @Override
    public void hideUpButton() {
        mBackBtn.setVisibility(View.INVISIBLE);
    }

    @NonNull
    @Override
    public ViewGroup getFragmentContainer() {
        return findViewById(R.id.frame_content);
    }
}
