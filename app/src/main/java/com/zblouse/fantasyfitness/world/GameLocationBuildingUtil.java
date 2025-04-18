package com.zblouse.fantasyfitness.world;

import com.zblouse.fantasyfitness.R;

import java.util.Arrays;
import java.util.List;

public class GameLocationBuildingUtil {

    public static String getPrintableStringForBuilding(int buildingId){
        if(buildingId == R.id.marsh_button) {
            return "marsh";
        } else if(buildingId == R.id.cave_button) {
            return "cave";
        }else if(buildingId == R.id.dark_forest_button) {
            return "dark forest";
        } else if(buildingId == R.id.inn_button) {
            return "inn";
        }else if(buildingId == R.id.general_store_button){
            return "general store";
        }else if(buildingId == R.id.blacksmith_button){
            return "blacksmith";
        } else {
            return "unknown location";
        }

    }

    public static List<Integer> getBuildingsInLocation(String gameLocation){
        if(gameLocation.equals(GameLocationService.WOODLANDS)){
            return Arrays.asList(R.id.marsh_button, R.id.cave_button, R.id.dark_forest_button);
        } else if(gameLocation.equals(GameLocationService.THANADEL_VILLAGE)){
            return Arrays.asList(R.id.inn_button, R.id.general_store_button);
        } else if(gameLocation.equals(GameLocationService.FARMLANDS)){
            return Arrays.asList(R.id.inn_button);
        } else if(gameLocation.equals(GameLocationService.FAOLYN)){
            return Arrays.asList(R.id.inn_button, R.id.general_store_button, R.id.blacksmith_button);
        } else if(gameLocation.equals(GameLocationService.BRIDGETON)){
            return Arrays.asList(R.id.inn_button, R.id.general_store_button, R.id.blacksmith_button);
        }
        else {
            throw new UnsupportedOperationException("Game Location: " + gameLocation + " has not been implemented in GameLocationBuildingUtil");
        }
    }
}
