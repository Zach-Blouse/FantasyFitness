package com.zblouse.fantasyfitness.workout;


import static org.junit.Assert.assertEquals;

import android.location.Location;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class DistanceTrackerTest {

    //Pre-calculated distances were done using: https://www.cqsrg.org/tools/GCDistance/
    @Test
    public void distanceCalculationTest() {
        DistanceTracker testedDistanceTracker = new DistanceTracker();
        Location location1 = new Location("provider");
        location1.setLatitude(10.10);
        location1.setLongitude(10.10);
        Location location2 = new Location("provider");
        location2.setLatitude(10.11);
        location2.setLongitude(10.11);
        Location location3 = new Location("provider");
        location3.setLatitude(10.12);
        location3.setLongitude(10.15);

        testedDistanceTracker.start();
        testedDistanceTracker.update(location1);
        double result1 = testedDistanceTracker.update(location2);
        assertEquals(1557.153,result1,.01);
        double result2 = testedDistanceTracker.update(location3);
        assertEquals(6078.556,result2,.01);

    }

    @Test
    public void distanceCalculationPauseTest() {
        DistanceTracker testedDistanceTracker = new DistanceTracker();
        Location location1 = new Location("provider");
        location1.setLatitude(10.10);
        location1.setLongitude(10.10);
        Location pauseLocation = new Location("provider");
        pauseLocation.setLatitude(80.80);
        pauseLocation.setLongitude(80.80);
        Location location2 = new Location("provider");
        location2.setLatitude(10.11);
        location2.setLongitude(10.11);
        Location location3 = new Location("provider");
        location3.setLatitude(10.12);
        location3.setLongitude(10.15);

        testedDistanceTracker.start();
        testedDistanceTracker.update(location1);
        testedDistanceTracker.pause();
        testedDistanceTracker.update(pauseLocation);
        testedDistanceTracker.unpause();
        testedDistanceTracker.update(location2);
        double result2 = testedDistanceTracker.update(location3);
        assertEquals(4521.403,result2,.01);

    }
}
