package com.zblouse.fantasyfitness.world;

public class GameLocationDistance {

    private Integer id;
    private final Integer location1Id;
    private final Integer location2Id;
    private final double distanceMiles;

    public GameLocationDistance(Integer id, Integer location1Id, Integer location2Id, double distanceMiles){
        this.id = id;
        this.location1Id = location1Id;
        this.location2Id = location2Id;
        this.distanceMiles = distanceMiles;
    }

    public GameLocationDistance(Integer location1Id, Integer location2Id, double distanceMiles){
        this.location1Id = location1Id;
        this.location2Id = location2Id;
        this.distanceMiles = distanceMiles;
    }

    public Integer getId(){
        return this.id;
    }

    public Integer getLocation1Id(){
        return this.location1Id;
    }

    public Integer getLocation2Id(){
        return this.location2Id;
    }

    public double getDistanceMiles(){
        return this.distanceMiles;
    }
}
