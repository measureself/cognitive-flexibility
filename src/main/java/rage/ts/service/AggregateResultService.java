package rage.ts.service;

import java.util.List;
import rage.ts.domain.Reaction;
import rage.ts.domain.TestResult;
import rage.ts.vo.AggregateResultVO;
import org.springframework.stereotype.Service;

@Service
public class AggregateResultService {

    public AggregateResultVO calculateResult(TestResult result) {
        switch (result.getTestType()) {
            case "REACTION":
                return calculateReactionAggregateResult(result);
            case "NUMBERREACTION":
                return calculateNumberReactionAggregateResult(result);
            case "CHARACTERREACTION":
                return calculateCharacterReactionAggregateResult(result);
            case "TASKSWITCHING":
                return calculateTaskSwitchingAggregateResult(result);
        }

        return new AggregateResultVO();
    }

    private AggregateResultVO calculateReactionAggregateResult(TestResult result) {
        AggregateResultVO aggregateResult = new AggregateResultVO();
        if (result.getReactions() == null) {
            return aggregateResult;
        }

        double correct = 0;
        double reactionTime = 0;
        for (Reaction reaction : result.getReactions()) {
            if (reaction.getKeyPressTime() == null) {
                continue;
            }

            double stimulusVisible = reaction.getKeyPressTime() - reaction.getShowTime();
            if (stimulusVisible < 40) {
                continue;
            }

            reactionTime += stimulusVisible;
            correct++;
        }

        if (result.getParticipant() != null) {
            aggregateResult.setParticipantId(result.getParticipant().getId());
        }

        aggregateResult.setInfo(result.getInfo());
        aggregateResult.setTestType(result.getTestType());
        if (result.getReactions().isEmpty()) {
            aggregateResult.setHitsPercentage(0);
        } else {
            aggregateResult.setHitsPercentage(100.0 * correct / result.getReactions().size());
        }
        if (correct == 0) {
            aggregateResult.setReactionTime(0);
        } else {
            aggregateResult.setReactionTime(Math.round(reactionTime / correct));
        }
        aggregateResult.setHitsOutsideTimespan(result.getAdditionalKeyPresses().size());

        return aggregateResult;
    }

    private AggregateResultVO calculateNumberReactionAggregateResult(TestResult result) {

        AggregateResultVO aggregateResult = new AggregateResultVO();
        if (result.getReactions() == null) {
            return aggregateResult;
        }

        double correct = 0;
        double reactionTime = 0;
        for (Reaction reaction : result.getReactions()) {
            if (reaction.getCorrect() == null || !reaction.getCorrect()) {
                continue;
            }

            double stimulusVisible = reaction.getKeyPressTime() - reaction.getShowTime();
            if (stimulusVisible < 40) {
                continue;
            }

            reactionTime += stimulusVisible;
            correct++;
        }

        aggregateResult.setParticipantId(result.getParticipant().getId());
        aggregateResult.setInfo(result.getInfo());
        aggregateResult.setTestType(result.getTestType());
        if (result.getReactions().isEmpty()) {
            aggregateResult.setHitsPercentage(0);
        } else {
            aggregateResult.setHitsPercentage(100.0 * correct / result.getReactions().size());
        }
        if (correct == 0) {
            aggregateResult.setReactionTime(0);
        } else {
            aggregateResult.setReactionTime(Math.round(reactionTime / correct));
        }
        aggregateResult.setHitsOutsideTimespan(result.getAdditionalKeyPresses().size());

        return aggregateResult;
    }

    private AggregateResultVO calculateCharacterReactionAggregateResult(TestResult result) {
        // same functionality as for prev
        return calculateNumberReactionAggregateResult(result);
    }

    private AggregateResultVO calculateTaskSwitchingAggregateResult(TestResult result) {

        AggregateResultVO aggregateResult = new AggregateResultVO();
        if (result.getReactions() == null) {
            return aggregateResult;
        }

        double correct = 0;
        double reactionTime = 0;
        for (Reaction reaction : result.getReactions()) {
            if (reaction.getCorrect() == null || !reaction.getCorrect()) {
                continue;
            }

            double stimulusVisible = reaction.getKeyPressTime() - reaction.getShowTime();
            if (stimulusVisible < 40) {
                continue;
            }

            reactionTime += stimulusVisible;
            correct++;
        }

        aggregateResult.setParticipantId(result.getParticipant().getId());
        aggregateResult.setInfo(result.getInfo());
        aggregateResult.setTestType(result.getTestType());
        if (result.getReactions().isEmpty()) {
            aggregateResult.setHitsPercentage(0);
        } else {
            aggregateResult.setHitsPercentage(100.0 * correct / result.getReactions().size());
        }
        if (correct == 0) {
            aggregateResult.setReactionTime(0);
        } else {
            aggregateResult.setReactionTime(Math.round(reactionTime / correct));
        }
        aggregateResult.setHitsOutsideTimespan(result.getAdditionalKeyPresses().size());

        addRepeatedAggregateResults(result, aggregateResult);
        addChangedAggregateResults(result, aggregateResult);

        return aggregateResult;
    }

    private void addRepeatedAggregateResults(TestResult result, AggregateResultVO aggregateResult) {
        List<Reaction> reactions = result.getReactions();

        int count = 0;
        double correct = 0;
        double reactionTime = 0;
        for (int i = 1; i < reactions.size(); i++) {
            Reaction reaction = reactions.get(i);
            Reaction previous = reactions.get(i - 1);
            if (!reaction.getElementType().equals(previous.getElementType())) {
                continue;
            }

            count++;
            if (reaction.getCorrect() == null || !reaction.getCorrect()) {
                continue;
            }

            double stimulusVisible = reaction.getKeyPressTime() - reaction.getShowTime();
            reactionTime += stimulusVisible;
            correct++;
        }

        if (count == 0) {
            aggregateResult.setHitsRepeated(0);
        } else {
            aggregateResult.setHitsRepeated(100.0 * correct / count);
        }

        if (correct == 0) {
            aggregateResult.setRepeatedReactionTime(0);
        } else {
            aggregateResult.setRepeatedReactionTime(reactionTime / correct);
        }
    }

    private void addChangedAggregateResults(TestResult result, AggregateResultVO aggregateResult) {
        List<Reaction> reactions = result.getReactions();

        int count = 0;
        double correct = 0;
        double reactionTime = 0;
        for (int i = 1; i < reactions.size(); i++) {
            Reaction reaction = reactions.get(i);
            Reaction previous = reactions.get(i - 1);
            if (reaction.getElementType().equals(previous.getElementType())) {
                continue;
            }

            count++;
            if (reaction.getCorrect() == null || !reaction.getCorrect()) {
                continue;
            }

            double stimulusVisible = reaction.getKeyPressTime() - reaction.getShowTime();
            reactionTime += stimulusVisible;
            correct++;
        }


        if (count == 0) {
            aggregateResult.setHitsRepeated(0);
        } else {
            aggregateResult.setHitsRepeated(100.0 * correct / count);
        }

        if (correct == 0) {
            aggregateResult.setRepeatedReactionTime(0);
        } else {
            aggregateResult.setRepeatedReactionTime(reactionTime / correct);
        }
    }
}
