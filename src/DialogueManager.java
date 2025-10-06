//import java.util.*;
//
//public class DialogueManager {
//
//    private final Random random = new Random();
//
//    private final Map<DialogueEvent, List<String>> phrases = new EnumMap<>(DialogueEvent.class);
//    private final Map<String, String> lastPhrasesForPlayer = new HashMap<>();
//    private final Map<DialogueEvent, Double> eventProbability = new EnumMap<>(DialogueEvent.class);
//
//    public DialogueManager() {
//        initDefaultProbabilities();
//        initDefaultPhrases();
//    }
//
//    private void initDefaultProbabilities() {
//        eventProbability.put(DialogueEvent.PRE_MOVE, 0.35);
//        eventProbability.put(DialogueEvent.POST_MOVE, 0.25);
//        eventProbability.put(DialogueEvent.WIN, 1.0);
//        eventProbability.put(DialogueEvent.TIE, 1.0);
//        eventProbability.put(DialogueEvent.LOSE, 1.0);
//        eventProbability.put(DialogueEvent.BLOCK, 0.9);
//        eventProbability.put(DialogueEvent.ILLEGAL_MOVE, 1.0);
//    }
//    private void initDefaultPhrases() {
//        phrases.put(DialogueEvent.PRE_MOVE,Arrays.asList(
//                "You're going down!!",{Player,CpuPlayer},
//                "Let's me see here.....",{Player,CpuPlayer},
//                "Let's Rock!", {CpuPlayer,CpuPlayer},
//                "Meet your maker!", {CpuPlayer,Player},
//        ));
//
//        phrases.put(DialogueEvent.POST_MOVE,Arrays.asList(
//                "Wh"
//        ));
//    }
//
//}
