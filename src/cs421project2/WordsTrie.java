/**
 * Created by Zortrox on 10/8/2016.
 */

package cs421project2;

import java.util.*;

public class WordsTrie {
	private class SubGrouping implements Comparable<SubGrouping>{
		public char letter = '\0';
		public ArrayList<SubGrouping> subLetters = new ArrayList<>();
		public SubGrouping parent = null;
		public int depth = 0;

		SubGrouping(char l, SubGrouping p, int d) {
			letter = l;
			parent = p;
			depth = d;
		}

		public int compareTo(SubGrouping o) {
			if (o.letter < letter) return 1;
			else if (o.letter > letter) return -1;
			else return 0;
		}
	}

	ArrayList<SubGrouping> arrWords = new ArrayList<>();

	WordsTrie() {

	}

	//add words to the trie
	//returns true if word is correct length
	//returns false otherwise
	public boolean addWord(String word) {
		int len = word.length();
		word = word.toLowerCase().trim();
		if (len == 10 || len == 7 || len == 4 || len == 3) {
			word += '\0';
			ArrayList<SubGrouping> currentArray = arrWords;

			int index = -1;
			int depth = 0;
			SubGrouping parent = null;
			for (int i = 0; i < word.length(); i++) {
				if ((index = indexOfChar(currentArray, word.charAt(i))) != -1) {
					parent = currentArray.get(index);
					currentArray = parent.subLetters;
				} else {
					SubGrouping sub = new SubGrouping(word.charAt(i), parent, depth);
					currentArray.add(sub);
					Collections.sort(currentArray);
					parent = sub;
					currentArray = parent.subLetters;
				}

				depth++;
			}

			return true;
		} else {
			return false;
		}
	}

	//returns arrays of words that can be made
	//will return array of size 1 if length of words is 10 or 7
	//and size 2 for words length 3 and 4
	public ArrayList< ArrayList<String> > getWords(String number) {
		List< List<Character> > digits = new ArrayList<>(Arrays.asList(new ArrayList<>(), new ArrayList<>(),
				new ArrayList<>(Arrays.asList('a', 'b', 'c')), new ArrayList<>(Arrays.asList('d', 'e', 'f')),
				new ArrayList<>(Arrays.asList('g', 'h', 'i')), new ArrayList<>(Arrays.asList('j', 'k', 'l')),
				new ArrayList<>(Arrays.asList('m', 'n', 'o')), new ArrayList<>(Arrays.asList('p', 'q', 'r', 's')),
				new ArrayList<>(Arrays.asList('t', 'u', 'v')), new ArrayList<>(Arrays.asList('w', 'x', 'y', 'z'))));

		ArrayList< ArrayList<String> > wordGroup = new ArrayList<>();

		for (int loop = 0; loop < 4; loop++) {
			ArrayList<SubGrouping> finalLetters = new ArrayList<>();
			int wordLength;
			int startIndex;
			switch(loop) {
				case 0:
					wordLength = 10;
					startIndex = 0;
					break;
				case 1:
					wordLength = 7;
					startIndex = 3;
					break;
				case 2:
					wordLength = 3;
					startIndex = 3;
					break;
				case 3:
					wordLength = 4;
					startIndex = 6;
					break;
				default:
					wordLength = 0;
					startIndex = 0;
					break;
			}

			Stack<SubGrouping> stack = new Stack<>();

			for (int j = 0; j < digits.get(Integer.parseInt("" + number.charAt(startIndex))).size(); j++) {
				int phoneInt = Integer.parseInt("" + number.charAt(startIndex));
				char phoneChar = digits.get(phoneInt).get(j);
				int index = indexOfChar(arrWords, phoneChar);

				if (index != -1) stack.add(arrWords.get(index));
			}

			while (!stack.isEmpty()) {
				SubGrouping sub = stack.pop();

				ArrayList<SubGrouping> children = sub.subLetters;
				for (int i = 0; i < children.size(); i++) {
					SubGrouping child = children.get(i);

					if (child.depth < wordLength) {
						int phoneInt = Integer.parseInt("" + number.charAt(startIndex + child.depth));
						boolean isPhoneChar = digits.get(phoneInt).contains(child.letter);

						if (child.depth < wordLength && child.letter != '\0' && isPhoneChar) {
							stack.add(child);
							continue;
						}
					}

					//if word end and correct length
					if (child.letter == '\0' && child.depth == wordLength) {
						finalLetters.add(child);
					}
				}
			}

			//break if a word in the grouping was found
			//don't break if searching for 3 & 4 letter words
			if (finalLetters.size() != 0) {
				ArrayList<String> words = reverseWords(finalLetters);
				wordGroup.add(words);
			}

			if (wordGroup.size() != 0 && loop != 0 && loop != 2) {
				break;
			}
		}

		return wordGroup;
	}

	private int indexOfChar(ArrayList<SubGrouping> arr, char c) {
		for (int i = 0; i < arr.size(); i++) {
			if (arr.get(i).letter == c) {
				return i;
			}
		}

		return -1;
	}

	private ArrayList<String> reverseWords(ArrayList<SubGrouping> words) {
		ArrayList<String> reverseWords = new ArrayList<>();

		for (int i = 0; i < words.size(); i++) {
			String word = "";

			SubGrouping tempGroup = words.get(i).parent;
			for (int j = tempGroup.depth; j >= 0; j--) {
				word = tempGroup.letter + word;
				tempGroup = tempGroup.parent;
			}

			reverseWords.add(word);
		}

		return reverseWords;
	}
}
