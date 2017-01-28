/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private ArrayList<String> wordList = new ArrayList<String>();
    private HashSet<String> wordSet = new HashSet<String>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<String, ArrayList<String>>();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<Integer, ArrayList<String>>();
    private int wordLength;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);

            String sorted = sortLetters(word);
            if(lettersToWord.containsKey(sorted)) {
                lettersToWord.get(sorted).add(word);
            }
            else {
                ArrayList<String> relatedWords = new ArrayList<String>();
                relatedWords.add(word);
                lettersToWord.put(sorted, relatedWords);
            }

            Integer wordLength = new Integer(word.length());
            if(sizeToWords.containsKey(wordLength)) {
                sizeToWords.get(wordLength).add(word);
            }
            else {
                ArrayList<String> nLengthWords= new ArrayList<String>();
                nLengthWords.add(word);
                sizeToWords.put(wordLength, nLengthWords);
            }
        }
        wordLength = DEFAULT_WORD_LENGTH;
    }

    public boolean isGoodWord(String word, String base) {
        if(!wordSet.contains(word)) {
            return false;
        }
        if(word.toLowerCase().contains(base.toLowerCase())) {
            return false;
        }
        return true;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String target = sortLetters(targetWord);
        for(String entry : wordList) {
            if(target.length() == entry.length() && target.equals(sortLetters(entry))) {
                result.add(entry);
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(char letter = 'a'; letter <= 'z'; letter++) {
            String searchKey = sortLetters(word + letter);
            if(lettersToWord.containsKey(searchKey)) {
                for(String answer : lettersToWord.get(searchKey)) {
                    if(isGoodWord(answer, word)) {
                        result.add(answer);
                    }
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        while(true) {
            int random = new Random().nextInt(wordList.size());
            String starter = wordList.get(random);
            if(starter.length() <= wordLength && getAnagramsWithOneMoreLetter(starter).size() >= MIN_NUM_ANAGRAMS) {
                if(wordLength != MAX_WORD_LENGTH) {
                    wordLength++;
                }
                //  Cheating
                System.out.println("Answers: " + getAnagramsWithOneMoreLetter(starter));
                return starter;
            }
        }
    }

    private String sortLetters(String word) {
        char[] charArray = word.toCharArray();
        Arrays.sort(charArray);
        String sortedString = new String(charArray);
        return sortedString;
    }
}
