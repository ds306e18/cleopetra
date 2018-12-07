package dk.aau.cs.ds306e18.tournament.model.format;

public interface StageStatusChangeListener {

    void onStageStatusChanged(Format format, StageStatus oldStatus, StageStatus newStatus);
}
