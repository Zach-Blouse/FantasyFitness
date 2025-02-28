package com.zblouse.fantasyfitness.workout;

public class WorkoutRecord {

    private final String userId;
    private final Long mileRecord;
    private final Long fiveKRecord;
    private final Long tenKRecord;
    private final Long twentyFiveKRecord;
    private final Long marathonRecord;

    public WorkoutRecord(String userId, Long mileRecord, Long fiveKRecord, Long tenKRecord, Long twentyFiveKRecord, Long marathonRecord){
        this.userId = userId;
        this.mileRecord = mileRecord;
        this.fiveKRecord = fiveKRecord;
        this.tenKRecord = tenKRecord;
        this.twentyFiveKRecord= twentyFiveKRecord;
        this.marathonRecord = marathonRecord;
    }

    public String getUserId(){
        return this.userId;
    }

    public Long getMileRecord(){
        return this.mileRecord;
    }

    public Long getFiveKRecord(){
        return this.fiveKRecord;
    }

    public Long getTenKRecord(){
        return this.tenKRecord;
    }

    public Long getTwentyFiveKRecord(){
        return this.twentyFiveKRecord;
    }

    public Long getMarathonRecord(){
        return this.marathonRecord;
    }
}

