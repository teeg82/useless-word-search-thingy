package main;

import java.awt.Point;
import java.util.List;

public class Word {

	private List<Point> characterPositions;
	private String word;
	
	public Word(String word, List<Point> characterPositions){
		this.word = word;
		this.characterPositions = characterPositions;
	}
	
	public boolean isPartOfWord(int row, int column){
		boolean isPart = false;
		for(Point characterPosition : characterPositions){
			if(characterPosition.x == column && characterPosition.y == row){
				isPart = true;
				break;
			}
		}			
		
		return isPart;
	}
}