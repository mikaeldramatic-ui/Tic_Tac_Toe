import java.util.*;

public class DialogueManager {

    private final Random random = new Random();

    private final Map<DialogueEvent, List<String>> phrases = new EnumMap<>(DialogueEvent.class);
    private final Map<String, String> lastPhrasesForPlayer = new HashMap<>();
    private final Map<DialogueEvent, Double> eventProbability = new EnumMap<>(DialogueEvent.class);

    public DialogueManager() {
        initDefaultProbabilities();
        initDefaultPhrases();
    }

    private void initDefaultProbabilities() {
        eventProbability.put(DialogueEvent.PRE_MOVE, 0.35);
        eventProbability.put(DialogueEvent.POST_MOVE, 0.25);
        eventProbability.put(DialogueEvent.WIN, 1.0);
        eventProbability.put(DialogueEvent.TIE, 1.0);
        eventProbability.put(DialogueEvent.LOSE, 1.0);
        eventProbability.put(DialogueEvent.BLOCK, 0.9);
        eventProbability.put(DialogueEvent.ILLEGAL_MOVE, 1.0);
    }
    private void initDefaultPhrases() {
        phrases.put(DialogueEvent.PRE_MOVE,Arrays.asList(
                "You're going down!!",{HumanPlayer,CpuPlayer},
                "Let's me see here.....",{HumanPlayer,CpuPlayer},
                "Let's Rock!", {CpuPlayer,CpuPlayer},
                "Meet your maker!", {CpuPlayer,Player},
        ));

        phrases.put(DialogueEvent.POST_MOVE,Arrays.asList(
                "Well that´s was a pour move",{CpuPlayer},
                "My mother plays better than you", {HumanPlayer},
                "DO you know this move....*Smack*", {HumanPlayer, CpuPlayer},
        ));

        phrases.put(DialogueEvent.WIN,Arrays.asList(
                "HA,I WIN ",{CpuPlayer , HumanPlayer},
                "I believe that i've won this battle", {HumanPlayer},
                "Oh i'm sorry, should I call you loser from here now ?", {HumanPlayer, CpuPlayer},
                ));

        phrases.put(DialogueEvent.LOSE,Arrays.asList(
                "Dance for me loser ",{CpuPlayer},
                "Better luck next time", {HumanPlayer},
                "Winner starts with W and yours is L for LOOOOSER", {HumanPlayer, CpuPlayer},
                ));

        phrases.put(DialogueEvent.TIE, Arrays.asList(
                "HOW COULD THIS BE A TIE",{CpuPlayer},
                "TIE TIE TIE TIE", {HumanPlayer},
                "WE NEED A WINNER!", {HumanPlayer, CpuPlayer},
                ));

        phrases.put(DialogueEvent.BLOCK ,Arrays.asList(
                "OH NOT TODAY!",{CpuPlayer},
                "Close but no cigarr", {HumanPlayer},
                "B is for...BLOCKING", {HumanPlayer, CpuPlayer},
                ));
        phrases.put(DialogueEvent.ILLEGAL_MOVE,Arrays.asList(
                "Play fair!",{CpuPlayer},
                "Learn the rules stupid!", {HumanPlayer},
                "Come on Grandma, learn how to play", {HumanPlayer, CpuPlayer},
                ));



    }
public String getLine (Player actor, Player opponent , DialogueEvent, Move move) {
        double prob = eventProbability.getOrDefault(event, 1.0);
        if (rnd.nextDouble() >prob) {
            return "";
        }

        List<String> list = phrases.getOrDefault(event, Collections.emptyList());
        if (list.isEmpty()) return "";

        String last = lastPhrasesForPlayer.get(actor.getName());
        String chosen = pickNonRepeating(list, last);

        String formatted = formatPhrase(chosen, actor, opponent, move);

        lastPhrasesForPlayer.put(actor.getName(), chosen);

        return formatted;
}

public String getLine(Player actor, dialougeEvent event) {
        return getLine(actor, null, event, null);
}

private String pickNonRepeating(List<String> list, String last) {
        if (list.size() == 1) return list.get(0);
        String pick;
        int attempts = 0;
        do {
            pick = list.get(rnd.nextInt(list.size()));
            attempts++;

        } while (pick.equals(last) && attempts <5);
        return pick;
}

private String formatPhrase(String template, Player actor , Player opponent, Move move) {
        String result = template;
        if (actor != null) result = result.replace("{Player}", actor.getName());
        if(opponent != null)result = result.replace("{Opponent}", opponent.getName());
        if (move !=null){
            result = result.replace("{row}"), String.valueOF(move.getRow()));
            result = result.replace("{col}"), String.valueOF(move.getCol()));
        }
        return result;
}

public void setEventProbability ( DialogueEvent event, double probability) {
        eventProbability.put(event, Math.max(0.0,Math.min(1.0, probability)));

}

public void addPhrase(dialougeEvent event, String phrase) {
        phrases.computeIfAbsent(event, k-> new ArrayList<>()).add(phrase);
}

}
