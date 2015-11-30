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

//Based of QMUL's DataWeatherAPI. This allows any valid city name to be entered as a string
//returns WOEID of that string, required by the API
//Class is static so no object needs to be created.

public class DataCityCodeParser{

    public static String getCityWOEID(String cityName){
        String cityParse = getWOEIDAsRSS(replaceSpacesWithUnderscore(cityName));
        int startIndex = 0;
        String start= "<locality1" ;

        startIndex = cityParse.indexOf(start, startIndex);

        // found a element
        int endIndex = cityParse.indexOf(">", startIndex);
        String cityString = cityParse.substring(startIndex, endIndex+1);

        // get city name
        String information = getValueForKey(cityString, "woeid");
        return information;
    }

    public static String replaceSpacesWithUnderscore(String city){
        return city.replaceAll(" ", "_");
    }

    public static String getWOEIDAsRSS(String processedCity)
    {
        try{
            /*Adapted from: http://stackoverflow.com/questions/1381617/simplest-way-to-correctly-load-html-from-web-page-into-a-string-in-java
             *Answer provided by: erickson
             */
            URL url = new URL("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.places%20where%20text%3D%22"+processedCity+"%22&format=xml");
            URLConnection con = url.openConnection();

            Pattern p = Pattern.compile("text/html;\\s+charset=([^\\s]+)\\s*");
            Matcher m = p.matcher(con.getContentType());

            /* If Content-Type doesn't match this pre-conception, choose default and
             * hope for the best.
             */

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
        catch(Exception e) {
            System.err.println("CityCodeParser exception "+e);
        }
        return null;
    }

    public static String getValueForKey(String theString, String keyString)
    {
        int startIndex = theString.indexOf(keyString);
        startIndex = theString.indexOf("\"", startIndex);
        int endIndex = theString.indexOf("\"", startIndex+1);
        String resultString = theString.substring(startIndex+1, endIndex);
        return resultString;
    }
}

