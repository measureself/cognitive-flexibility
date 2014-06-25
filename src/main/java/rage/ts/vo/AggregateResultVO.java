package rage.ts.vo;

import java.text.DecimalFormat;

public class AggregateResultVO {
    private static final DecimalFormat DF = new DecimalFormat("#.##");

    private int participantId;
    private String testType;
    private String info;
    private double hitsPercentage;
    private double reactionTime;
    private int hitsOutsideTimespan;
    private double hitsRepeated;
    private double hitsChanged;
    private double repeatedReactionTime;
    private double changedReactionTime;

    public AggregateResultVO() {
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId.intValue();
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getHitsPercentage() {
        return DF.format(hitsPercentage);
    }

    public void setHitsPercentage(double hitsPercentage) {
        this.hitsPercentage = hitsPercentage;
    }

    public String getReactionTime() {
        return DF.format(reactionTime);
    }

    public void setReactionTime(double reactionTime) {
        this.reactionTime = reactionTime;
    }

    public int getHitsOutsideTimespan() {
        return hitsOutsideTimespan;
    }

    public void setHitsOutsideTimespan(int hitsOutsideTimespan) {
        this.hitsOutsideTimespan = hitsOutsideTimespan;
    }

    public String getHitsRepeated() {
        return DF.format(hitsRepeated);
    }

    public void setHitsRepeated(double hitsRepeated) {
        this.hitsRepeated = hitsRepeated;
    }

    public String getHitsChanged() {
        return DF.format(hitsChanged);
    }

    public void setHitsChanged(double hitsChanged) {
        this.hitsChanged = hitsChanged;
    }

    public String getRepeatedReactionTime() {
        return DF.format(repeatedReactionTime);
    }

    public void setRepeatedReactionTime(double repeatedReactionTime) {
        this.repeatedReactionTime = repeatedReactionTime;
    }

    public String getChangedReactionTime() {
        return DF.format(changedReactionTime);
    }

    public void setChangedReactionTime(double changedReactionTime) {
        this.changedReactionTime = changedReactionTime;
    }
}
