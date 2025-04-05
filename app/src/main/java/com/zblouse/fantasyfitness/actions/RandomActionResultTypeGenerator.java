package com.zblouse.fantasyfitness.actions;

import com.zblouse.fantasyfitness.world.GameLocationService;

import java.util.Random;

public class RandomActionResultTypeGenerator {

    private Random random;

    public RandomActionResultTypeGenerator(){
        this.random = new Random();
    }

    public void setRandom(Random random){
        this.random = random;
    }

    public ActionResultType getRandomActionResult(String locationName){

        if(GameLocationService.isWildernessLocation(locationName)) {
            int result = random.nextInt(4);
            switch (result) {
                case 0:
                    return ActionResultType.NOTHING;
                case 1:
                    return ActionResultType.DIALOG;
                case 2:
                    return ActionResultType.SECRET;
                case 3:
                    return ActionResultType.COMBAT;
                default:
                    return ActionResultType.NOTHING;
            }
        } else {
            int result = random.nextInt(3);
            switch (result) {
                case 0:
                    return ActionResultType.NOTHING;
                case 1:
                    return ActionResultType.DIALOG;
                case 2:
                    return ActionResultType.SECRET;
                default:
                    return ActionResultType.NOTHING;
            }
        }
    }
}
