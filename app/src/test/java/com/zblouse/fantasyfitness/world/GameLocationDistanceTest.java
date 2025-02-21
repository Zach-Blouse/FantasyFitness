package com.zblouse.fantasyfitness.world;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GameLocationDistanceTest {

    @Test
    public void createTest(){
        GameLocationDistance testedDistance = new GameLocationDistance(5,1,2,10);
        assertEquals(5L,(long)testedDistance.getId());
    }
}
