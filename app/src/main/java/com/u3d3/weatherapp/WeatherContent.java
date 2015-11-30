package com.u3d3.weatherapp;

import android.widget.TextView;

import com.u3d3.weatherapp.data.DataCondition;
import com.u3d3.weatherapp.data.DataManager;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by U3D3 on 15/4/7.
 */
public class WeatherContent implements Observer{
    int currentTemp;
    int maxTemp;
    int minTemp;
    DataCondition currentCondition = null;

    DataManager dataManager;
    public WeatherContent(DataManager dataManager){
        this.dataManager = dataManager;
        //????????????
        dataManager.addObserver(this);
        //TextView currentTempView = (TextView) findViewById(R.id.currentTempView);
        //displayTemp(currentTempView, currentTemp);

    }

    public void displayTemp(TextView textView, int temp ){
        textView.setText(temp);
    }




    @Override
    public void update(Observable observable, Object o) {
        this.currentTemp = dataManager.getCurrentTemp();
        this.maxTemp = dataManager.getMaxTemp();
        this.minTemp = dataManager.getMinTemp();
        this.currentCondition = dataManager.getCurrentCondition();
    }
}
