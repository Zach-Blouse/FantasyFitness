package com.zblouse.fantasyfitness.workout;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Handler;

import com.zblouse.fantasyfitness.MainActivity;
import com.zblouse.fantasyfitness.user.CreateAccountResponseEvent;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class WorkoutServiceTest {

    @Test
    public void startTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        TimeTracker mockTimeTracker = Mockito.mock(TimeTracker.class);
        Handler mockHandler = Mockito.mock(Handler.class);
        WorkoutService workoutService = new WorkoutService(mockActivity, mockHandler, mockTimeTracker);
        workoutService.startWorkout();
        verify(mockTimeTracker).start();
        verify(mockHandler).post(any());
    }

    @Test
    public void pauseTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        TimeTracker mockTimeTracker = Mockito.mock(TimeTracker.class);
        Handler mockHandler = Mockito.mock(Handler.class);
        WorkoutService workoutService = new WorkoutService(mockActivity, mockHandler, mockTimeTracker);
        when(mockTimeTracker.pause()).thenReturn(3456L);
        workoutService.pause();
        verify(mockTimeTracker).pause();
        ArgumentCaptor<TimeUpdateEvent> timeUpdateEventArgumentCaptor = ArgumentCaptor.forClass(TimeUpdateEvent.class);
        verify(mockActivity).publishEvent((TimeUpdateEvent)timeUpdateEventArgumentCaptor.capture());
        assert(timeUpdateEventArgumentCaptor.getValue().getTime() == 3456L);
    }

    @Test
    public void unpauseTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        TimeTracker mockTimeTracker = Mockito.mock(TimeTracker.class);
        Handler mockHandler = Mockito.mock(Handler.class);
        WorkoutService workoutService = new WorkoutService(mockActivity, mockHandler, mockTimeTracker);
        workoutService.unpause();
        verify(mockTimeTracker).unpause();
    }

    @Test
    public void stopTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        TimeTracker mockTimeTracker = Mockito.mock(TimeTracker.class);
        when(mockTimeTracker.stop()).thenReturn(3456L);
        Handler mockHandler = Mockito.mock(Handler.class);
        WorkoutService workoutService = new WorkoutService(mockActivity, mockHandler, mockTimeTracker);
        workoutService.stopWorkout();
        verify(mockTimeTracker).stop();
        verify(mockHandler).removeCallbacks(any());
        ArgumentCaptor<TimeUpdateEvent> timeUpdateEventArgumentCaptor = ArgumentCaptor.forClass(TimeUpdateEvent.class);
        verify(mockActivity).publishEvent((TimeUpdateEvent)timeUpdateEventArgumentCaptor.capture());
        assert(timeUpdateEventArgumentCaptor.getValue().getTime() == 3456L);
    }
}
