package com.u3d3.weatherapp.data;

/**
 * Created by U3D3 on 15/4/6.
 */
public class DataParseConditionCode {
    public static DataCondition getCondition(int code){
        DataCondition condition = DataCondition.SUNNY;

        switch(code){
            case 21: case 32: case 34: case 36:
                condition = DataCondition.SUNNY;
                break;
            case 0: case 19: case 23: case 24:
                condition = DataCondition.WINDY;
                break;
            case 5: case 6: case 7: case 8: case 13: case 14: case 15: case 16: case 43: case 46:
                condition = DataCondition.SNOWY;
                break;
            case 1:	case 2: case 3: case 4: case 37: case 38: case 39: case 45: case 47:
                condition = DataCondition.STORMY;
                break;
            case 25: case 26: case 27: case 28:
                condition = DataCondition.CLOUDY;
                break;
            case 29: case 30: case 44:
                condition = DataCondition.PARTIALLY_CLOUDY;
                break;
            case 9: case 10: case 11: case 12: case 17: case 18: case 35: case 40:
                condition = DataCondition.RAINY;
                break;
            case 31: case 33:
                condition = DataCondition.CLEAR;
                break;
            case 20: case 22:
                condition = DataCondition.FOGGY;
                break;
        }

        return condition;
    }
}
