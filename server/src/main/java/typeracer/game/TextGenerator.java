package typeracer.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * A class to generate a text using a markov chain with probability transitions trained on a given
 * corpus.
 */
public class TextGenerator {
  private final String corpus;
  private List<String> preprocessedCorpus = new ArrayList<>();
  private HashMap<String, HashMap<String, Integer>> model = new HashMap<>();
  private HashMap<String, Integer> startingProbabilities = new HashMap<>();
  private final Random random = new Random();

  /**
   * Constructs a TextGenerator with the specified corpus.
   *
   * @param corpus the corpus of text to be used for generating text.
   */
  public TextGenerator(String corpus) {
    this.corpus = corpus;
  }

  /**
   * Preprocesses the corpus by converting it to lowercase, removing punctuation and non-ASCII
   * characters, and splitting it into words.
   *
   * @return the preprocessed corpus as an array of words.
   */
  private List<String> preprocessCorpus() {
    System.out.println("Preprocessing corpus...");
    preprocessedCorpus =
        List.of(
            corpus
                .toLowerCase()
                .trim()
                .replaceAll("\\p{Punct}|[^\\p{ASCII}]", "")
                .split("[\\s \\t\\p{Cntrl}\\p{javaWhitespace}]+", 0));
    preprocessedCorpus =
        preprocessedCorpus.stream().filter(word -> !word.isEmpty() && !word.isBlank()).toList();
    System.out.println("Corpus preprocessed.");
    return preprocessedCorpus;
  }

  /**
   * Trains a model based on the provided corpus and saves it with a unique model name. If a model
   * with the same name already exists, it loads the existing model instead of training a new one.
   *
   * @param uniqueModelName the unique name for the model file.
   */
  public void trainModel(String uniqueModelName) {
    File modelFile = new File(uniqueModelName + ".model");
    File startingProbabilitiesFile = new File(uniqueModelName + ".sProbs");
    if (modelFile.exists() && startingProbabilitiesFile.exists()) {
      loadModelFromFiles(modelFile, startingProbabilitiesFile);
    } else {
      System.out.println("Training model...");
      preprocessCorpus();
      for (int i = 0; i < preprocessedCorpus.size() - 1; i++) {
        String currentWord = preprocessedCorpus.get(i);
        int currentPosition = i;
        model.computeIfAbsent(
            currentWord,
            k -> {
              startingProbabilities.put(currentWord, 0);
              HashMap<String, Integer> frequencyDistribution = new HashMap<>();
              for (int j = currentPosition; j < preprocessedCorpus.size() - 1; j++) {
                if (currentWord.equals(preprocessedCorpus.get(j))) {
                  startingProbabilities.put(
                      currentWord, startingProbabilities.get(currentWord) + 1);
                  String followingWord = preprocessedCorpus.get(j + 1);
                  frequencyDistribution.putIfAbsent(followingWord, 0);
                  frequencyDistribution.put(
                      followingWord, frequencyDistribution.get(followingWord) + 1);
                }
              }
              return frequencyDistribution;
            });
      }
      writeModelToFile(uniqueModelName);
      System.out.println("Model trained.");
    }
  }

  private void writeModelToFile(String uniqueModelName) {
    try {
      ObjectOutputStream modelOutputStream =
          new ObjectOutputStream(new FileOutputStream(uniqueModelName + ".model"));
      modelOutputStream.writeObject(model);
      modelOutputStream.flush();
      modelOutputStream.close();

      ObjectOutputStream startingProbabilitiesOutputStream =
          new ObjectOutputStream(new FileOutputStream(uniqueModelName + ".sProbs"));
      startingProbabilitiesOutputStream.writeObject(startingProbabilities);
      startingProbabilitiesOutputStream.flush();
      startingProbabilitiesOutputStream.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void loadModelFromFiles(File modelFile, File startingProbabilitiesFile) {
    try {
      ObjectInputStream modelInputStream = new ObjectInputStream(new FileInputStream(modelFile));
      model = (HashMap<String, HashMap<String, Integer>>) modelInputStream.readObject();
      modelInputStream.close();

      ObjectInputStream startingProbabilitiesInputStream =
          new ObjectInputStream(new FileInputStream(startingProbabilitiesFile));
      startingProbabilities =
          (HashMap<String, Integer>) startingProbabilitiesInputStream.readObject();
      startingProbabilitiesInputStream.close();
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Generates a sequence of text based on the trained model. This method generates a specified
   * number of words by sampling from the starting probabilities and the trained model.
   *
   * @param words the number of words to generate.
   * @return the generated text as a string.
   */
  public String generateText(int words) {
    System.out.println("Generating text...");
    StringBuilder output = new StringBuilder();

    String startingWord = sampleFromDistribution(startingProbabilities);

    output.append(startingWord).append(" ");

    String newWord = startingWord;
    for (int i = 0; i < words; i++) {
      if (model.get(newWord) == null || newWord.isBlank()) {
        newWord = sampleFromDistribution(startingProbabilities);
      } else {
        newWord = sampleFromDistribution(model.get(newWord));
      }
      output.append(newWord);
      if (i < words - 1) {
        output.append(" ");
      }
    }
    System.out.println("Text generated.");
    return output.toString();
  }

  /**
   * Samples a word from a given frequency distribution. This method randomly selects a word from
   * the distribution based on their frequencies.
   *
   * @param distribution the frequency distribution of words.
   * @return a randomly selected word from the distribution.
   */
  private String sampleFromDistribution(HashMap<String, Integer> distribution) {
    int index = random.nextInt(distribution.values().stream().reduce(0, Integer::sum));
    int accumulator = 0;
    for (String word : distribution.keySet()) {
      if (accumulator >= index) {
        return word;
      }
      accumulator += distribution.get(word);
      if (accumulator >= index) {
        return word;
      }
    }
    return "";
  }
}
