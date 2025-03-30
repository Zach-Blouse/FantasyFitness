package com.zblouse.fantasyfitness.actions;

import java.util.Random;

public class RandomActionResultTypeGenerator {

    public ActionResultType getRandomActionResult(){
        Random random = new Random();

        int result = random.nextInt(4);
        switch(result){
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
    }
}
