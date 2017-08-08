package net.derohimat.bakingapp.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.derohimat.bakingapp.BaseApplication;
import net.derohimat.bakingapp.di.component.ActivityComponent;
import net.derohimat.bakingapp.di.component.DaggerActivityComponent;

public abstract class AppActivity extends AppCompatActivity {

    private ActivityComponent mComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponent = DaggerActivityComponent.builder().applicationComponent(getApp().getApplicationComponent()).build();
    }

    protected ActivityComponent getComponent() {
        return mComponent;
    }

    protected BaseApplication getApp() {
        return (BaseApplication) getApplicationContext();
    }

}
