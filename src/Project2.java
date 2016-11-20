/* Written by Jared Prince & Matthew Clark
 * */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Project2 {
	//conflict chaining resolution class (linked list)
	private static class HashChain {
		String word;	//word
		long value;		//phone number
		HashChain next;

		HashChain(long val, String w) {
			value = val;
			word = w;
			next = null;
		}
	}

	public static void main(String args[]) throws FileNotFoundException{
		int m = 113;
		int numWords = 0;
		int power = 0;
		HashChain[] words = null;
		double a = (Math.sqrt(5) - 1) / 2;
		String[] filenames = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",	"S", "T", "U", "V", "W", "XYZ"};
		boolean doubleTable = true;

		while(doubleTable){
			numWords = 0;
			doubleTable = false;
			m = getNextM(m);
			
			System.out.println("m = " + m);
			
			words = new HashChain[m];
			
			for (int i = 0; i < filenames.length && !doubleTable; i++) {
				Scanner in = new Scanner(new File("words\\" + filenames[i] + ".txt"));
				
				while (in.hasNext()){
					String word = in.next();
					
					if(word.length() != 3 && word.length() != 4 && word.length() != 7 && word.length() != 10){
						continue;
					}

					//increase number of words
					numWords++;
					long value = Long.parseLong(wordToNumber(word));
					double rem = (value * a) % 1;
					int hash = (int) (m * rem);
					
					if (words[hash] == null) {
						words[hash] = new HashChain(value, word);
					} else if (!words[hash].word.equals(word)) {
						//conflict occurred
						if (numWords > m) {
							//increase table size
							doubleTable = true;
							break;
						} else {
							//chaining
							HashChain entry = words[hash];
							while (entry.next != null) {
								entry = entry.next;
							}
							entry.next = new HashChain(value, word);
						}
					}
				}
			}
		}

		System.out.println();

		//get phone number & words
		Scanner in = new Scanner(System.in);

		boolean bExit = false;
		while (!bExit) {
			String phone = "";
			String area = "";
			String exchange = "";
			String number = "";

			boolean valid = false;
			while (!valid) {
				System.out.print("Enter a phone number (ex. 123-456-7890): ");

				phone = in.next();
				phone = phone.trim();

				if (phone.toLowerCase() == "exit") {
					bExit = true;
					break;
				} else if (!phone.contains("-")) {
					System.out.println("The number must have dashes(-).\n");
					continue;
				} else if (phone.length() < 12) {
					System.out.println("There are not enough digits in this number.\n");
					continue;
				} else if (phone.length() > 12) {
					System.out.println("There are too many digits in this number.\n");
					continue;
				}

				area = phone.substring(0, phone.indexOf("-"));
				phone = phone.substring(phone.indexOf("-") + 1);
				exchange = phone.substring(0, phone.indexOf("-"));
				phone = phone.substring(phone.indexOf("-") + 1);
				number = phone;

				if (area.length() != 3 || exchange.length() != 3 || number.length() != 4) {
					System.out.println("This number is not formatted correctly.");
					continue;
				}

				try {
					Integer.parseInt(area);
					Integer.parseInt(exchange);
					Integer.parseInt(number);
				} catch (NumberFormatException e) {
					System.out.println("This is not a number.");
					continue;
				}

				valid = true;
			}

			if (!bExit) {
				//get word from phone number
				ArrayList<ArrayList<String>> phoneWords = getWords(words, area + exchange + number);

				if (phoneWords.size() == 1 || (phoneWords.size() == 2 && phoneWords.get(0).get(0).length() > 4)) {
					for (int i = 0; i < phoneWords.size(); i++) {
						int len = phoneWords.get(i).get(0).length();
						if (len == 10) {
							for (int j = 0; j < phoneWords.get(i).size(); j++) {
								System.out.println("1-" + phoneWords.get(i).get(j).toUpperCase());
							}
						} else if (len == 7) {
							for (int j = 0; j < phoneWords.get(i).size(); j++) {
								System.out.println("1-" + area + "-" + phoneWords.get(i).get(j).toUpperCase());
							}
						} else if (len == 3) {
							for (int j = 0; j < phoneWords.get(i).size(); j++) {
								System.out.println("1-" + area + "-" + phoneWords.get(i).get(j).toUpperCase()
										+ "-" + number);
							}
						} else if (len == 4) {
							for (int j = 0; j < phoneWords.get(i).size(); j++) {
								System.out.println("1-" + area + "-" + exchange + "-"
										+ phoneWords.get(i).get(j).toUpperCase());
							}
						}
					}
					System.out.println();
				} else if (phoneWords.size() == 2) {
					for (int i = 0; i < phoneWords.get(0).size(); i++) {
						for (int j = 0; j < phoneWords.get(1).size(); j++) {
							System.out.println("1-" + area + "-" + phoneWords.get(0).get(i).toUpperCase() + "-"
									+ phoneWords.get(1).get(j).toUpperCase());
						}
					}
					System.out.println();
				} else {
					System.out.println("1-" + area + "-" + exchange + "-" + number);
					System.out.println();
				}
			}
		}
	}

	private static ArrayList<ArrayList<String>> getWords(HashChain[] words, String number) {
		ArrayList<ArrayList<String>> phoneWords = new ArrayList<>();

		ArrayList<String> tempArr;

		//check 10 length number
		tempArr = addWords(words, number);
		if (tempArr.size() > 0) {
			phoneWords.add(tempArr);
		}

		//check 7 length number
		tempArr = addWords(words, number.substring(3));
		if (tempArr.size() > 0) {
			phoneWords.add(tempArr);
		}

		//return if words were found
		if (phoneWords.size() > 0) {
			return phoneWords;
		}

		//check 3 length words
		tempArr = addWords(words, number.substring(3, 6));
		if (tempArr.size() > 0) {
			phoneWords.add(tempArr);
		}

		//check 4 length words
		tempArr = addWords(words, number.substring(6));
		if (tempArr.size() > 0) {
			phoneWords.add(tempArr);
		}

		return phoneWords;
	}

	private static ArrayList<String> addWords(HashChain[] words, String str) {
		ArrayList<String> arr = new ArrayList<>();

		//hash table properties
		double a = (Math.sqrt(5) - 1) / 2;
		int m = words.length;
		long value = Long.parseLong(str);
		double rem = (value * a) % 1;
		int hash = (int) (m * rem);
		HashChain entry = words[hash];
		while (entry != null) {
			if (entry.value == value) {
				arr.add(entry.word);
			}

			entry = entry.next;
		}

		return arr;
	}

	private static int getNextM(int m){
		m = m * 2;
		
		if(m % 2 == 0){
			m++;
		}
		
		boolean prime = false;
		
		while(!prime){
			prime = false;
			
			for(int i = 1; i < m / 2; i = i + 2){
				if(m % i == 0){
					prime = true;
					break;
				}
			}
			
			m = m + 2;
		}
		
		return m;
	}


	private static String wordToNumber(String word){
		String numWord = "";
		
		for(int i = 0; i < word.length(); i++){
			char c = word.charAt(i);
			
			if(c == 'a' || c == 'b' || c == 'c'){
				numWord = numWord + 2;
			}
			
			else if(c == 'd' || c == 'e' || c == 'f'){
				numWord = numWord + 3;
			}
			
			else if(c == 'g' || c == 'h' || c == 'i'){
				numWord = numWord + 4;
			}
			
			else if(c == 'j' || c == 'k' || c == 'l'){
				numWord = numWord + 5;
			}
			
			else if(c == 'm' || c == 'n' || c == 'o'){
				numWord = numWord + 6;
			}
			
			else if(c == 'p' || c == 'q' || c == 'r' || c == 's'){
				numWord = numWord + 7;
			}
			
			else if(c == 't' || c == 'u' || c == 'v'){
				numWord = numWord + 8;
			}
			
			else if(c == 'w' || c == 'x' || c == 'y' || c == 'z'){
				numWord = numWord + 9;
			}
		}
		
		return numWord;
	}
}
