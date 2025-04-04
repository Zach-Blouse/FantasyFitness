package com.zblouse.fantasyfitness.actions;

import java.util.Map;

public class CombatActionResultGenerator implements ActionResultGenerator{
    @Override
    public ActionResult generate(Map<String, Object> metadata) {
        return new CombatActionResult();
    }
}
