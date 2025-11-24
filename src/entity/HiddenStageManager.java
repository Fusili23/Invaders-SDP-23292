package entity;

import engine.GameState;

/**l
 * Manages hidden stage transitions (Easter egg feature).
 */
public class HiddenStageManager {
    private GameState savedState;
    private boolean isInHiddenStage = false;

    /** Enters the hidden stage. Saves current state and sets to hidden. */
    public void enterHiddenStage(GameState currentState) {
        savedState = currentState.clone();
        isInHiddenStage = true;
        currentState.setLevel(GameState.HIDDEN_STAGE); // Use setLevel instead of setStage
    }

    /** Exits the hidden stage. Restores previous state. */
    public void exitHiddenStage(GameState currentState) {
        if (isInHiddenStage) {
            currentState.restore(savedState); // This method DOES exist
            isInHiddenStage = false;
        }
    }

    /** Returns true if currently in hidden stage. */
    public boolean isInHiddenStage() {
        return isInHiddenStage;
    }
}
