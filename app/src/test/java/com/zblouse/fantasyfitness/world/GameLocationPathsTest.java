package com.zblouse.fantasyfitness.world;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Arrays;
import java.util.Set;

public class GameLocationPathsTest {

    @Test
    public void gameLocationPathsTest(){
        GameLocation testLocation1 = new GameLocation(GameLocationService.VALLEY_OF_MONSTERS, "valley of monsters description");
        GameLocation testLocation2 = new GameLocation(GameLocationService.THANADEL_VILLAGE, "thanadel village description");
        Path location2Path = new Path(10.0, Arrays.asList(testLocation1));
        GameLocation testLocation3 = new GameLocation(GameLocationService.MOUNTAIN_PASS, "mountain pass description");
        Path location3Path = new Path(15.0, Arrays.asList(testLocation1, testLocation2));
        GameLocationPaths testedPaths = new GameLocationPaths(testLocation1);
        testedPaths.addPath(testLocation2.getLocationName(), location2Path);
        testedPaths.addPath(testLocation3.getLocationName(), location3Path);
        Set<String> reachablePaths = testedPaths.getReachableLocationNames();
        assert(reachablePaths.contains(testLocation2.getLocationName()));
        assert(reachablePaths.contains(testLocation3.getLocationName()));
        assert(testedPaths.getStartLocation().equals(testLocation1));
        assertEquals(location2Path.getLocationPath(), testedPaths.getPath(testLocation2.getLocationName()).getLocationPath());
        assertEquals(location3Path.getDistanceKm(),testedPaths.getPath(testLocation3.getLocationName()).getDistanceKm(), 0.0);
    }
}
