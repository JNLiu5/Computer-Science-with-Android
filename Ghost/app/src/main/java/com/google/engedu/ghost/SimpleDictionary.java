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

package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if(prefix.isEmpty()) {
            Random rand = new Random();
            int r = rand.nextInt(words.size());
            return words.get(r);
        }

        // binary search
        int start = 0;
        int end = words.size() - 1;
        while(start <= end) {
            int middle = (start + end)/2;
            String candidate = words.get(middle);
            if(candidate.startsWith(prefix)) {
                return candidate;
            }
            if(prefix.compareTo(candidate) < 0) {
                end = middle - 1;
            }
            else {
                start = middle + 1;
            }
        }

        return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        if(prefix.isEmpty()) {
            Random rand = new Random();
            int r = rand.nextInt(words.size());
            return words.get(r);
        }

        // binary search
        int start = 0;
        int end = words.size() - 1;
        int middle = 0;
        while(start <= end) {
            middle = (start + end)/2;
            String candidate = words.get(middle);
            if(candidate.startsWith(prefix)) {
                break;
            }
            if(prefix.compareTo(candidate) < 0) {
                end = middle - 1;
            }
            else {
                start = middle + 1;
            }
        }

        if(start > end) { // no valid longer word
            return null;
        }

        // sort into odd and even length words
        ArrayList<String> odd = new ArrayList<>();
        ArrayList<String> even = new ArrayList<>();

        // handles all elements before and including middle
        int temp = middle;
        while(true) {
            if(temp >= 0) {
                String s = words.get(temp);
                if(s.startsWith(prefix)) {
                    if(s.length() % 2 == 0) {
                        even.add(s);
                    }
                    else {
                        odd.add(s);
                    }
                }
                else {
                    break;
                }
            }
            else {
                break;
            }
            temp--;
        }
        // handles all elements after middle
        temp = middle + 1;
        while(true) {
            if(temp < words.size()) {
                String s = words.get(temp);
                if(s.startsWith(prefix)) {
                    if(s.length() % 2 == 0) {
                        even.add(s);
                    }
                    else {
                        odd.add(s);
                    }
                }
                else {
                    break;
                }
            }
            else {
                break;
            }
            temp++;
        }

        Random rand = new Random();

        // state variables
        boolean selector = (prefix.length() % 2 == 1);;   // true if selecting odds, false if selecting evens
        boolean emptySet = false;   // true if the first choice set was empty, false otherwise
        while(true) {
            if(selector) {
                if(odd.isEmpty()) {
                    if(emptySet) {
                        return null;    // both sets were empty
                    }
                    // choose an even-length word even though it's not ideal
                    selector = false;
                    emptySet = true;
                    continue;
                }
                else {
                    return odd.get(rand.nextInt(odd.size()));
                }
            }
            else {
                if(even.isEmpty()) {
                    if(emptySet) {
                        return null;
                    }
                    selector = true;
                    emptySet = true;
                    continue;
                }
                else {
                    return even.get(rand.nextInt(even.size()));
                }
            }
        }
    }
}
