package typeracer.game;

import java.util.*;

/**
 * A class to generate a text using a markov chain with probability transitions trained on a given corpus.
 */
public class TextGenerator {
  private final String corpus;
  private String[] preprocessedCorpus;
  private final Map<String, Map<String, Integer>> model = new HashMap<>();
  private final Map<String, Integer> startingProbabilities = new HashMap<>();
  private final Random random = new Random();

  public TextGenerator(String corpus) {
    this.corpus = corpus;
  }

  private String[] preprocessCorpus() {
    System.out.println("Preprocessing corpus...");
    preprocessedCorpus = corpus.toLowerCase().trim().replaceAll("\\p{Punct}|[^\\p{ASCII}]", "").split("\\s+");
    System.out.println("Corpus preprocessed.");
    return preprocessedCorpus;
  }

  public void trainModel() {
    System.out.println("Training model...");
    preprocessCorpus();
    for (int i = 0; i < preprocessedCorpus.length - 1; i++) {
      String currentWord = preprocessedCorpus[i];
      model.putIfAbsent(currentWord, new HashMap<>());
      startingProbabilities.putIfAbsent(currentWord, 1);
      Map<String, Integer> frequencyDistribution = model.get(currentWord);
      for (String word : preprocessedCorpus) {
        if (currentWord.equals(word)) {
          startingProbabilities.put(word, startingProbabilities.get(word) + 1);
          String followingWord = preprocessedCorpus[i+1];
          frequencyDistribution.putIfAbsent(followingWord, 0);
          frequencyDistribution.put(followingWord, frequencyDistribution.get(followingWord) + 1);
        }
      }
    }
    for (String word : preprocessedCorpus) {
      //normalizeDistribution(model.get(word));
    }
    //normalizeDistribution(startingProbabilities);
    System.out.println("Model trained.");
  }

  private void normalizeDistribution(Map<String, Double> distribution) {
    double sum = distribution.values().stream().reduce(0.0, Double::sum);
    distribution.replaceAll((w, frequency) -> frequency / sum);
  }

  public String generateText(int words) {
    System.out.println("Generating text...");
    StringBuilder output = new StringBuilder();

    String startingWord = sampleFromDistribution(startingProbabilities);

    output.append(startingWord).append(" ");

    String newWord = startingWord;
    for (int i = 0; i < words; i++) {
      newWord = sampleFromDistribution(model.get(newWord));
      output.append(newWord);
      if (i < words - 1) {
        output.append(" ");
      }
    }
    System.out.println("Text generated.");
    return output.toString();
  }

  private String sampleFromDistribution(Map<String, Integer> distribution) {
    int index = random.nextInt(distribution.values().stream().reduce(0, Integer::sum));
    int accumulator = 0;
    for (String word : distribution.keySet()) {
      accumulator += distribution.get(word);
      if (accumulator >= index) {
        return word;
      }
    }
    assert false;
    return "";
  }
}
