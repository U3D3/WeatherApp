package com.u3d3.weatherapp.data;

/**
 * Created by U3D3 on 15/4/6.
 */
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//As provided by QMUL
//Parses website for a specific city (using WOEID) and returns "element/information"

public class DataWeatherAPI {
    String weatherRss;
    String city;

    public void update(){
        weatherRss = getWeatherAsRSS();
    }


    private String getWeatherAsRSS()
    {
        try{
			/*
			Adapted from: http://stackoverflow.com/questions/1381617/simplest-way-to-correctly-load-html-from-web-page-into-a-string-in-java
			Answer provided by: erickson
			*/
            URL url = new URL("http://weather.yahooapis.com/forecastrss?w="+this.city+"&u=c");
            URLConnection con = url.openConnection();
            Pattern p = Pattern.compile("text/html;\\s+charset=([^\\s]+)\\s*");
            Matcher m = p.matcher(con.getContentType());
			/* If Content-Type doesn't match this pre-conception, choose default and
			 * hope for the best. */
            String charset = m.matches() ? m.group(1) : "ISO-8859-1";
            Reader r = new InputStreamReader(con.getInputStream(), charset);
            StringBuilder buf = new StringBuilder();
            while (true) {
                int ch = r.read();
                if (ch < 0)
                    break;
                buf.append((char) ch);
            }
            String str = buf.toString();
            return(str);
        }
        catch(Exception e) {System.err.println("Weather API Exception: "+e);}
        return null;
    }


    public String parseWeather(String element, String info)
    {
        int startIndex = 0;
        String start= "<yweather:"+ element ;

        startIndex = weatherRss.indexOf(start, startIndex);

        // found a weather element
        int endIndex = weatherRss.indexOf(">", startIndex);
        String weatherForecast = weatherRss.substring(startIndex, endIndex+1);

        // get weather detail
        String information = getValueForKey(weatherForecast, info);
        return information;
    }

    private String getValueForKey(String theString, String keyString)
    {
        int startIndex = theString.indexOf(keyString);
        startIndex = theString.indexOf("\"", startIndex);
        int endIndex = theString.indexOf("\"", startIndex+1);
        String resultString = theString.substring(startIndex+1, endIndex);
        return resultString;
    }


    //public getters setters
    public void setCity(String city){
        this.city = city;
        update();
    }
}

