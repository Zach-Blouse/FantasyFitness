package com.zblouse.fantasyfitness.actions;

import java.util.Map;

public interface ActionResultGenerator {

    public ActionResult generate(Map<String, Object> metadata);
}
