package com.milestonemania.service.util;

import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class for generating unique game slugs in "adverb-verb-animal" format.
 */
@Component
public class SlugGenerator {
    
    private final SecureRandom random = new SecureRandom();
    
    private static final List<String> ADVERBS = Arrays.asList(
        "boldly", "quickly", "gracefully", "cleverly", "happily", "smoothly", "eagerly", "gently",
        "proudly", "swiftly", "cheerfully", "carefully", "bravely", "kindly", "wisely", "calmly",
        "actively", "brightly", "clearly", "easily", "fairly", "freely", "highly", "lively",
        "nicely", "openly", "quietly", "rapidly", "safely", "simply", "truly", "warmly",
        "amazingly", "brilliantly", "creatively", "delightfully", "efficiently", "fantastically",
        "gloriously", "harmoniously", "impressively", "joyfully", "magnificently", "naturally",
        "optimally", "perfectly", "remarkably", "splendidly", "tremendously", "uniquely",
        "vibrantly", "wonderfully", "excellently", "dynamically", "energetically", "enthusiastically",
        "graciously", "magnificently", "marvelously", "spectacularly", "triumphantly", "victoriously"
        // Add more adverbs to reach 500...
    );
    
    private static final List<String> VERBS = Arrays.asList(
        "surfing", "dancing", "jumping", "flying", "running", "swimming", "climbing", "racing",
        "exploring", "discovering", "creating", "building", "painting", "singing", "playing", "laughing",
        "celebrating", "adventuring", "soaring", "gliding", "bouncing", "spinning", "twirling", "leaping",
        "skating", "skiing", "sailing", "rowing", "cycling", "hiking", "camping", "fishing",
        "gardening", "cooking", "baking", "crafting", "writing", "reading", "studying", "learning",
        "teaching", "helping", "sharing", "caring", "loving", "smiling", "dreaming", "hoping",
        "wishing", "believing", "achieving", "succeeding", "winning", "competing", "performing", "entertaining",
        "inspiring", "motivating", "encouraging", "supporting", "guiding", "leading", "following", "cooperating"
        // Add more verbs to reach 500...
    );
    
    private static final List<String> ANIMALS = Arrays.asList(
        "penguin", "dolphin", "elephant", "tiger", "lion", "eagle", "hawk", "owl",
        "butterfly", "hummingbird", "peacock", "flamingo", "panda", "koala", "kangaroo", "giraffe",
        "zebra", "cheetah", "leopard", "jaguar", "wolf", "fox", "bear", "deer",
        "rabbit", "squirrel", "chipmunk", "otter", "seal", "whale", "shark", "turtle",
        "frog", "dragonfly", "bee", "ladybug", "spider", "ant", "cricket", "firefly",
        "horse", "unicorn", "dragon", "phoenix", "griffin", "pegasus", "mermaid", "fairy",
        "octopus", "jellyfish", "starfish", "seahorse", "parrot", "toucan", "cardinal", "robin",
        "sparrow", "swan", "duck", "goose", "crane", "stork", "pelican", "albatross"
        // Add more animals to reach 500...
    );
    
    /**
     * Generates a random slug in "adverb-verb-animal" format.
     *
     * @return a randomly generated slug
     */
    public String generateSlug() {
        String adverb = ADVERBS.get(random.nextInt(ADVERBS.size()));
        String verb = VERBS.get(random.nextInt(VERBS.size()));
        String animal = ANIMALS.get(random.nextInt(ANIMALS.size()));
        
        return adverb + "-" + verb + "-" + animal;
    }
}

