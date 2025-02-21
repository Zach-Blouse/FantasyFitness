package com.zblouse.fantasyfitness.workout;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TimeTrackerTest {

    @Test
    public void updateTest() throws InterruptedException {
        TimeTracker testedTracker = new TimeTracker();
        testedTracker.start();
        Thread.sleep(500);
        assertEquals(500L,testedTracker.update(),50);
    }

    @Test
    public void stopTest() throws InterruptedException {
        TimeTracker testedTracker = new TimeTracker();
        testedTracker.start();
        Thread.sleep(500);
        assertEquals(500L,testedTracker.stop(),50);
    }

    @Test
    public void pauseTest() throws InterruptedException {
        TimeTracker testedTracker = new TimeTracker();
        testedTracker.start();
        Thread.sleep(500);
        assertEquals(500L,testedTracker.pause(),50);
        Thread.sleep(250);
        testedTracker.unpause();
        Thread.sleep(500);
        assertEquals(1000L,testedTracker.stop(),50);
    }
}
