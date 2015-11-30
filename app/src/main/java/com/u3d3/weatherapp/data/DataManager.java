package com.u3d3.weatherapp.data;

/**
 * Created by U3D3 on 15/4/6.
 */
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

//Holds app initialization Data
//Is an Observable class (implements Observer Design Pattern, part of MVC Pattern)
//Will initialize to London and broadcasts data set to all Observers (GUI modules)
//Requires DataWeatherAPI (provided by QMUL) and static DataCityCodeParser to access several other cities

public class DataManager extends Observable {

    private String currentTemp;
    private String minTemp;
    private String maxTemp;
    private String wind;
    private String visibility;
    private String sunset;
    private String sunrise;
    private DataCondition currentCondition;

    //tracks if we are displaying origin or destination,
    //updates WOEIDs when new cities are entered
    private String currentlyDisplaying;
    private static String initOriginLocation = "London";
    private static String initDestinationLocation = "null";
    private String originLocation;
    private String originLocationWOEID;
    private String destinationLocation;
    private String destinationLocationWOEID;

    private DataWeatherAPI weatherData;
    private String screenLayoutOrientation;

    boolean resetFlag = false;
    boolean saveFileChanged = false;
    boolean closeSearchPanes = false;
    boolean floodWarningFlag = false;
    boolean glareWarningFlag = false;

    //initializes DataManager
    //prevents app from starting without information to display
    //App will always start with London set as origin
    public DataManager(){
        this.currentlyDisplaying = "ORIGIN";
        this.originLocation = "London";
        this.destinationLocation = "null";
        this.originLocationWOEID = DataCityCodeParser.getCityWOEID(originLocation);

        weatherData = new DataWeatherAPI();
        weatherData.setCity(originLocationWOEID);

        refreshData();
    }


    //Observable Pattern overrides
    //everytime concurrent thread forces an update or user does an action
    //this method gets called. Forces Observable to broadcast to all observers
    public void refreshData(){
        if (currentlyDisplaying == "ORIGIN") weatherData.setCity(originLocationWOEID);
        else if (currentlyDisplaying == "DESTINATION") weatherData.setCity(destinationLocationWOEID);

        this.setCurrentTemp();
        this.setMaxTemp();
        this.setMinTemp();
        this.setWind();
        this.setSunrise();
        this.setSunset();
        this.setVisibility();
        this.setCurrentCondition();

        measurementsChanged();
    }

    public void measurementsChanged(){
        setChanged();
        notifyObservers();
        this.resetFlag = false;
        this.saveFileChanged = false;
        this.closeSearchPanes = false;
    }


    //Action setters, getters
    //All these have "broadcast" side effects
    void setToLandscapeScene(){
        this.screenLayoutOrientation = "LANDSCAPE";
        refreshData();
    }

    void setToPortraitScene(){
        this.screenLayoutOrientation = "PORTRAIT";
        refreshData();
    }

    void setOriginLocation(String originLocation){
        this.originLocationWOEID = DataCityCodeParser.getCityWOEID(originLocation);
        this.originLocation = firstLetterUpperCase(originLocation);
        closeSearchPanes = true;
        refreshData();
    }

    public String getOriginLocation(){
        return originLocation;
    }

    void setDestinationLocation(String destinationLocation){
        if (destinationLocation.equals("null")) {
            this.destinationLocation = "null";
            this.setCurrentlyDisplaying("ORIGIN");
        }
        else {
            this.destinationLocationWOEID = DataCityCodeParser.getCityWOEID(destinationLocation);
            this.destinationLocation = firstLetterUpperCase(destinationLocation);
            closeSearchPanes = true;
            refreshData();
        }
    }

    private String firstLetterUpperCase(String location){
        return Character.toUpperCase(location.charAt(0)) + location.substring(1);
    }

    public String getDestinationLocation(){
        return destinationLocation;
    }

    void setCurrentlyDisplaying(String currentlyDisplaying){
        this.currentlyDisplaying = currentlyDisplaying;
        refreshData();
    }

    String getCurrentlyDisplaying(){
        return this.currentlyDisplaying;
    }

    void reset(){
        //resets app to initial values
        resetFlag = true;
        this.currentlyDisplaying = "ORIGIN";
        this.destinationLocation = "null";
        setOriginLocation(initOriginLocation);
    }

    void setSaveFileChanged(Boolean state){
        this.saveFileChanged = state;
        measurementsChanged();
    }


    //Setters, Getters
    //GUI objects will "get" these when their DataManager broadcasts an update()
    void setCurrentTemp(){
        currentTemp= weatherData.parseWeather("condition", "temp");
    }

    public int getCurrentTemp(){
        return Integer.parseInt(currentTemp);
    }

    void setMinTemp(){
        minTemp= weatherData.parseWeather("forecast", "low");
    }

    public int getMinTemp(){
        return Integer.parseInt(minTemp);
    }

    void setMaxTemp(){
        maxTemp=weatherData.parseWeather("forecast", "high");
    }

    public int getMaxTemp(){
        return Integer.parseInt(maxTemp);
    }

    void setWind(){
        wind= weatherData.parseWeather("wind", "speed");
    }

    double getWind(){
        return Double.parseDouble(wind);
    }

    void setVisibility(){
        visibility= weatherData.parseWeather("atmosphere", "visibility");
    }

    public double getVisibility(){
        if (visibility.isEmpty()) return 10000.0;
        return Double.parseDouble(visibility);
    }

    void setSunset(){
        sunset= weatherData.parseWeather("astronomy", "sunset");
    }

    public String getSunset(){
        return sunset;
    }

    void setSunrise(){
        sunrise= weatherData.parseWeather("astronomy", "sunrise");
    }

    public String getSunrise(){
        return sunrise;
    }

    void setCurrentCondition(){
        String conditionCode = weatherData.parseWeather("condition", "code");
        currentCondition = DataParseConditionCode.getCondition(Integer.parseInt(conditionCode));
    }

    public DataCondition getCurrentCondition(){
        return currentCondition;
    }

    void setFloodWarningFlag(Boolean flag){
        this.floodWarningFlag = flag;
        measurementsChanged();
    }

    public Boolean getFloodWarningFlag(){
        return floodWarningFlag;
    }

    void setGlareWarningFlag(Boolean flag){
        this.glareWarningFlag = flag;
        measurementsChanged();
    }

    public Boolean getGlareWarningFlag(){
        return glareWarningFlag;
    }

    //API does not provide handle for precipitation
    //void setPrecipitation()
    //void getPrecipitation()
}
