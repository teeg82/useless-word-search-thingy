package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

public class Words {

	/**
	 * Position: Y, X
	 */
	public static final int[][] directions = {
		{0, 1}, // Right
		{1, 1}, // Down Right
		{1, 0}, // Down
		{1, -1}, // Down Left
		{0, -1}, // Left
		{-1, -1}, // Up Left
		{-1, 0}, // Up
		{-1, 1} // Up Right
	};

	public static final String[] words = {
			"Paul",
			"Arianna",
			"Heather",
			"Christina",
			"Sarah",
			"Bishop",
			"Joanne",
			"Jim",
			"Gordon",
			"Mollison",
			"ParrySound",
			"ColdLake",
			"Ottawa",
			"Bagotville",
			"StJean",
			"Summerside",
			"Egypt",
			"Father",
			"Son",
			"Grandfather",
			"Brother",
			"Uncle",
			"Nephew",
			"Husband",
			"Lover",
			"Married",
			"Pops",
			"MartialArts",
			"Golf",
			"VideoGames",
			"Reading",
			"math",
			"Computers",
			"Chinese",
			"Military",
	};
	
	public static final String secretMessage = "MerryChristmas";
	
	public static final char[][] grid = {{'H','H','E','A','T','H','E','R','Z','L','R','A','L','D','C'},
										 {'D','A','O','R','U','N','C','L','E','E','N','O','I','H','B'},
										 {'L','W','R','I','N','G','S','G','H','O','V','G','R','A','V'},
										 {'O','T','T','A','W','A','Y','T','D','E','U','I','G','Z','I'},
										 {'R','Y','O','N','S','P','A','R','R','Y','S','O','U','N','D'},
										 {'E','J','R','N','T','F','O','R','N','T','T','P','C','M','E'},
										 {'A','I','I','A','D','G','E','O','I','V','P','O','H','D','O'},
										 {'D','M','M','N','T','H','S','N','I','H','G','H','I','B','G'},
										 {'I','M','A','R','T','I','A','L','A','R','T','S','N','M','A'},
										 {'N','R','C','O','L','D','L','A','K','E','R','I','E','A','M'},
										 {'G','S','R','L','A','E','Y','I','P','E','J','B','S','R','E'},
										 {'Q','B','O','H','P','H','C','O','M','P','U','T','E','R','S'},
										 {'T','M','S','T','T','A','P','M','Y','B','K','F','S','I','K'},
										 {'M','D','N','A','B','S','U','H','V','W','E','H','P','E','N'},
										 {'S','M','K','M','Q','S','F','L','O','G','Y','M','Y','D','Z'}};
	
	public static List<Point> findUnconnectedLetters(){
		List<Word> wordList = new ArrayList<Word>();
		List<Point> unconnectedLetters = new ArrayList<Point>();
		
		for(int row = 0; row < grid.length; row++){
			for(int column = 0; column < grid[row].length; column++){
				char letter = grid[row][column];
				boolean isWord = false;
				
				boolean letterAlreadyFound = false;
				for(Word existingWord : wordList){
					if(existingWord.isPartOfWord(row, column)){
						letterAlreadyFound = true;
						break;
					}
				}
				
				for(int wordIndex = 0; wordIndex < words.length && !isWord && !letterAlreadyFound; wordIndex++){
					String word = words[wordIndex];
					boolean foundInDirection = false;
					if(word.startsWith(String.valueOf(letter))){
						// check directions
						for(int directionIndex = 0; directionIndex < directions.length && !isWord; directionIndex++){
							int[] direction = directions[directionIndex];
							String currentWord = String.valueOf(letter);

							int yPos = row;
							int xPos = column;
							List<Point> characterPositions = new ArrayList<Point>(word.length());
							characterPositions.add(new Point(xPos, yPos));
							while(!foundInDirection && (yPos + direction[0]) < grid.length && (xPos + direction[1]) < grid[yPos].length && (yPos + direction[0]) >= 0 && (xPos + direction[1]) >= 0){
								yPos += direction[0];
								xPos += direction[1];
								
								char currentLetter = grid[yPos][xPos];
								currentWord += String.valueOf(currentLetter);
								
								if(!word.toLowerCase().startsWith(currentWord.toLowerCase())){
									break;
								}else{
									characterPositions.add(new Point(xPos, yPos));
									foundInDirection = (currentWord.equalsIgnoreCase(word)); // true if the current word is equal to one of the words in the word array
								}									
							}
							
							if(foundInDirection){
								System.out.println("Found word: " + currentWord);
								isWord = true;
								wordList.add(new Word(currentWord, characterPositions));
							}
						}
					}
				}
				
				if(!isWord && !letterAlreadyFound){
					System.out.println("Adding character " + letter + " at row " + row + " and column " + column);
					unconnectedLetters.add(new Point(column, row));
				}
			}
		}
		return unconnectedLetters;
	}
	
	public static void main(String[] args){
		List<Point> unconnectedLetters = findUnconnectedLetters();

		for(Point position : unconnectedLetters){
			System.out.println("At row " + position.y + " column " + position.x + ", found letter " + grid[position.y][position.x]);
		}
		
		System.out.println("Found " + unconnectedLetters.size() + " letters");
		System.out.println("Secret message size " + secretMessage.length());
		if(unconnectedLetters.size() >= secretMessage.length()){
			
		}else{
			System.err.println("Not enough room for letters " + secretMessage);
		}
		
		for(int letterIndex = 0; letterIndex < secretMessage.length(); letterIndex++){
			Point unconnectedLetter = unconnectedLetters.get(letterIndex);
			grid[unconnectedLetter.y][unconnectedLetter.x] = secretMessage.charAt(letterIndex);
		}
		
		createImage();
	}
	
	private static final int IMAGE_WIDTH = 1700;
	private static final int IMAGE_HEIGHT = 1100;
	private static final int LETTER_SPACING_PIXELS = 6;
	private static final int HEADER_SPACE_PIXELS = 100;
	private static final int SPACE_BETWEEN_PUZZLE_AND_WORD_LIST = 150;
	private static final int FONT_SIZE = 36;
	private static final int GRID_LEFT_MARGIN = FONT_SIZE * grid.length;
	private static final int WORDS_PER_ROW = 5;
	private static final int WORD_LIST_LEFT_MARGIN = FONT_SIZE * WORDS_PER_ROW;
	private static final int WORD_WIDTH = (int) (WORDS_PER_ROW * (FONT_SIZE *1.6 ));
//	private static final int LEFT_MARGIN = 20;
	
	public static void createImage(){
		BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setFont(new Font("Courier", Font.PLAIN, FONT_SIZE));
		
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
		
		graphics.setColor(Color.black);
		
		for(int row = 0; row < grid.length; row++){
			for(int column = 0; column < grid[row].length; column++){
				char character = grid[row][column];
				graphics.drawString(String.valueOf(character).toUpperCase(), GRID_LEFT_MARGIN + (FONT_SIZE * column), HEADER_SPACE_PIXELS + (FONT_SIZE * row));
			}
		}
		
		List<String> wordList = Arrays.asList(words);
		Collections.shuffle(wordList);

		int rowOffset = 1;
		for(int wordIndex = 0; wordIndex < words.length; wordIndex++){
			String word = wordList.get(wordIndex);
			
			graphics.drawString(titleize(splitCamelCase(word)), WORD_LIST_LEFT_MARGIN + ((wordIndex + 1) % WORDS_PER_ROW) * WORD_WIDTH, HEADER_SPACE_PIXELS + (FONT_SIZE * grid[0].length) + SPACE_BETWEEN_PUZZLE_AND_WORD_LIST + (rowOffset * FONT_SIZE));
			
			if((wordIndex + 1) % WORDS_PER_ROW == 0){
				rowOffset++;
			}
		}
		
		try {
			ImageIO.write(image, "png", new File("word_search.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String splitCamelCase(String s) {
		   return s.replaceAll(
		      String.format("%s|%s|%s",
		         "(?<=[A-Z])(?=[A-Z][a-z])",
		         "(?<=[^A-Z])(?=[A-Z])",
		         "(?<=[A-Za-z])(?=[^A-Za-z])"
		      ),
		      " "
		   );
	}
	
	public static String titleize(String s){
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
}