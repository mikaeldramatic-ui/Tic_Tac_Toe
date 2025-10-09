import java.util.*;

public class DialogManager {

    private final Random random = new Random();

    private final Map<DialogEvent, List<String>> phrases = new EnumMap<>(DialogEvent.class);
    private final Map<String, String> lastPhrasesForPlayer = new HashMap<>();
    private final Map<DialogEvent, Double> eventProbability = new EnumMap<>(DialogEvent.class);

    public DialogManager() {
        initDefaultProbabilities();
        initDefaultPhrases();
    }

    private void initDefaultProbabilities() {
        eventProbability.put(DialogEvent.PRE_MOVE, 0.35);
        eventProbability.put(DialogEvent.POST_MOVE, 0.25);
        eventProbability.put(DialogEvent.WIN, 1.0);
        eventProbability.put(DialogEvent.TIE, 1.0);
        eventProbability.put(DialogEvent.LOSE, 1.0);
        eventProbability.put(DialogEvent.BLOCK, 0.9);
        eventProbability.put(DialogEvent.ILLEGAL_MOVE, 1.0);
    }
    private void initDefaultPhrases() {
        phrases.put(DialogEvent.PRE_MOVE,Arrays.asList(
                "You're going down!!,",
                "Let's see here",
                "Let's Rock!",
                "Meet your maker!"
        ));

        phrases.put(DialogEvent.POST_MOVE,Arrays.asList(
                "Well that´s was a pour move",
                "My mother plays better than you",
                "DO you know this move....*Smack*"
        ));

        phrases.put(DialogEvent.WIN,Arrays.asList(
                "HA,I WIN!",
                "The battle is won",
                "Oh i'm sorry, should I call you loser now?",
                "Winner starts with W and yours is L for LOOOOSER"
                ));

        phrases.put(DialogEvent.LOSE,Arrays.asList(
                "Dammit!",
                "It's not over!",
                "CHEATER!!!!"

                ));

        phrases.put(DialogEvent.TIE, Arrays.asList(
                "HOW COULD THIS BE A TIE",
                "TIE TIE TIE TIE, {Player}",
                "WE NEED A WINNER!"
                ));

        phrases.put(DialogEvent.BLOCK ,Arrays.asList(
                "OH NOT TODAY!",
                "Close but no cigarr",
                "B is for...BLOCKING",
                "Smell this!"
                ));
        phrases.put(DialogEvent.ILLEGAL_MOVE,Arrays.asList(
                "Play fair!",
                "Learn the rules stupid!",
                "Come on Grandma, learn how to play, "
                ));



    }
public String getLine (Player actor, Player opponent , DialogEvent event, Move move) {
        double prob = eventProbability.getOrDefault(event, 1.0);
        if (random.nextDouble() >prob) return "";

        List<String> list = phrases.getOrDefault(event, Collections.emptyList());
        if (list.isEmpty()) return "";

        String last = lastPhrasesForPlayer.get(actor.getName());
        String chosen = pickNonRepeating(list, last);

        String formatted = formatPhrase(chosen, actor, opponent, move);

        lastPhrasesForPlayer.put(actor.getName(), chosen);

        return formatted;
}

public String getLine(Player actor, DialogEvent event) {
        return getLine(actor,null, event, null);
}

private String pickNonRepeating(List<String> list, String last) {
        if (list.size() == 1) return list.get(0);
        String pick;
        int attempts = 0;
        do {
            pick = list.get(random.nextInt(list.size()));
            attempts++;
        } while (pick.equals(last) && attempts <5);
        return pick;
}

private String formatPhrase(String template, Player actor , Player opponent, Move move) {
        String result = template;
        if (actor != null) result = result.replace("{player}", actor.getName());
        if(opponent != null)result = result.replace("{opponent}", opponent.getName());
        if (move !=null) {
            result = result.replace("{row}", String.valueOf(move.getRow()));
            result = result.replace("{col}", String.valueOf(move.getCol()));
        }
        return result;
}

public void setEventProbability (DialogEvent event, double probability) {
        eventProbability.put(event, Math.max(0.0,Math.min(1.0, probability)));
    }

public void addPhrase(DialogEvent event, String phrase) {
        phrases.computeIfAbsent(event, k-> new ArrayList<>()).add(phrase);
}

}
