package com.zblouse.fantasyfitness.actions;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.zblouse.fantasyfitness.world.GameLocationService;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Random;

public class RandomActionResultTypeGeneratorTest {

    @Test
    public void nothingResultTest(){
        Random mockRandom = Mockito.mock(Random.class);
        when(mockRandom.nextInt(eq(4))).thenReturn(0);
        RandomActionResultTypeGenerator randomActionResultTypeGenerator = new RandomActionResultTypeGenerator();
        randomActionResultTypeGenerator.setRandom(mockRandom);

        ActionResultType result = randomActionResultTypeGenerator.getRandomActionResult(GameLocationService.WOODLANDS);
        assertEquals(ActionResultType.NOTHING, result);
    }

    @Test
    public void dialogResultTest(){
        Random mockRandom = Mockito.mock(Random.class);
        when(mockRandom.nextInt(eq(4))).thenReturn(1);
        RandomActionResultTypeGenerator randomActionResultTypeGenerator = new RandomActionResultTypeGenerator();
        randomActionResultTypeGenerator.setRandom(mockRandom);

        ActionResultType result = randomActionResultTypeGenerator.getRandomActionResult(GameLocationService.WOODLANDS);
        assertEquals(ActionResultType.DIALOG, result);
    }

    @Test
    public void secretResultTest(){
        Random mockRandom = Mockito.mock(Random.class);
        when(mockRandom.nextInt(eq(4))).thenReturn(2);
        RandomActionResultTypeGenerator randomActionResultTypeGenerator = new RandomActionResultTypeGenerator();
        randomActionResultTypeGenerator.setRandom(mockRandom);

        ActionResultType result = randomActionResultTypeGenerator.getRandomActionResult(GameLocationService.WOODLANDS);
        assertEquals(ActionResultType.SECRET, result);
    }

    @Test
    public void combatResultTest(){
        Random mockRandom = Mockito.mock(Random.class);
        when(mockRandom.nextInt(eq(4))).thenReturn(3);
        RandomActionResultTypeGenerator randomActionResultTypeGenerator = new RandomActionResultTypeGenerator();
        randomActionResultTypeGenerator.setRandom(mockRandom);

        ActionResultType result = randomActionResultTypeGenerator.getRandomActionResult(GameLocationService.WOODLANDS);
        assertEquals(ActionResultType.COMBAT, result);
    }

    @Test
    public void nothingResultNonWildernessTest(){
        Random mockRandom = Mockito.mock(Random.class);
        when(mockRandom.nextInt(eq(3))).thenReturn(0);
        RandomActionResultTypeGenerator randomActionResultTypeGenerator = new RandomActionResultTypeGenerator();
        randomActionResultTypeGenerator.setRandom(mockRandom);

        ActionResultType result = randomActionResultTypeGenerator.getRandomActionResult(GameLocationService.THANADEL_VILLAGE);
        assertEquals(ActionResultType.NOTHING, result);
    }

    @Test
    public void dialogResultNonWildernessTest(){
        Random mockRandom = Mockito.mock(Random.class);
        when(mockRandom.nextInt(eq(3))).thenReturn(1);
        RandomActionResultTypeGenerator randomActionResultTypeGenerator = new RandomActionResultTypeGenerator();
        randomActionResultTypeGenerator.setRandom(mockRandom);

        ActionResultType result = randomActionResultTypeGenerator.getRandomActionResult(GameLocationService.THANADEL_VILLAGE);
        assertEquals(ActionResultType.DIALOG, result);
    }

    @Test
    public void secretResultNonWildernessTest(){
        Random mockRandom = Mockito.mock(Random.class);
        when(mockRandom.nextInt(eq(3))).thenReturn(2);
        RandomActionResultTypeGenerator randomActionResultTypeGenerator = new RandomActionResultTypeGenerator();
        randomActionResultTypeGenerator.setRandom(mockRandom);

        ActionResultType result = randomActionResultTypeGenerator.getRandomActionResult(GameLocationService.THANADEL_VILLAGE);
        assertEquals(ActionResultType.SECRET, result);
    }
}
