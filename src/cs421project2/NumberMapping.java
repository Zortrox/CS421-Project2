package cs421project2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class NumberMapping {

	private static void loadWords(WordsTrie vocab) {
		String[] filenames = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
				"S", "T", "U", "V", "W", "XYZ"};

		for (int i = 0; i < filenames.length; i++) {
			try (BufferedReader br = new BufferedReader(new FileReader("words/" + filenames[i] + ".txt"))) {
				String line;
				while ((line = br.readLine()) != null) {
					//don't add blank lines
					if (line.length() > 0) vocab.addWord(line);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void main(String args[]) {
		WordsTrie vocab = new WordsTrie();

		//add words to trie from all files, plan on maybe adding from less files depending
		loadWords(vocab);

		//vocab.addWord("whizzbangs");
		//vocab.addWord("zzbangs");
		//vocab.addWord("angs");

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
				ArrayList<ArrayList<String>> words = vocab.getWords(area + exchange + number);

				if (words.size() == 1 || (words.size() == 2 && words.get(0).get(0).length() > 4)) {
					for (int i = 0; i < words.size(); i++) {
						int len = words.get(i).get(0).length();
						if (len == 10) {
							for (int j = 0; j < words.get(i).size(); j++) {
								System.out.println("1-" + words.get(i).get(j).toUpperCase());
							}
						} else if (len == 7) {
							for (int j = 0; j < words.get(i).size(); j++) {
								System.out.println("1-" + area + "-" + words.get(i).get(j).toUpperCase());
							}
						} else if (len == 3) {
							for (int j = 0; j < words.get(i).size(); j++) {
								System.out.println("1-" + area + "-" + words.get(i).get(j).toUpperCase()
										+ "-" + number);
							}
						} else if (len == 4) {
							for (int j = 0; j < words.get(i).size(); j++) {
								System.out.println("1-" + area + "-" + exchange + "-"
										+ words.get(i).get(j).toUpperCase());
							}
						}
					}
					System.out.println();
				} else if (words.size() == 2) {
					for (int i = 0; i < words.get(0).size(); i++) {
						for (int j = 0; j < words.get(1).size(); j++) {
							System.out.println("1-" + area + "-" + words.get(0).get(i).toUpperCase() + "-"
									+ words.get(1).get(j).toUpperCase());
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
}