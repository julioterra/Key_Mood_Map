package com.julioterra.keytreemap;

/*********************
 * Key Logging Map Sketch, by Julio Terra
 * 
 * Treemap library is required for this sketch. Download from Ben Fry's website at: 
 * - http://benfry.com/writing/ treemap/library.zip
 * Code extended from code developed by Ben Fry.
 * 
 ********************/


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import processing.core.PApplet;
import processing.core.PFont;
import quicktime.app.spaces.Collection;
import treemap.*;


public class KeyTreemap extends PApplet {

	Treemap map;

	public void setup() {
	  size(1024, 600);

	  WordMap mapData = new WordMap();  

	  // load text file into an array of strings
	  String[] lines = loadStrings("out_logFile.txt");
	  // create list of words to omit from analysis
	  String[] omitWords = {"this", "that", "to", "then", "the", ".jpg", "than", "these", "and", "for", 
			  				"from", "with", "here", "has", "but", "are", "when", "where", "who", "how", 
			  				"was", "can", "what", "his", "you"};
	 
	  // loop through each of entry in the lines array and split each line into individual words
	  for (int i = 0; i < lines.length; i++) {
		  String[] words = split(lines[i]," ");
		  // loop through each word in the words array 
		  for (int j = 0; j < words.length; j++) {
			  String cleanWords = trim(words[j]);			// clean the words by removing commas, and periods
			  cleanWords = cleanWords.toLowerCase();		// make the word lower case
			  boolean addWord = true;						// set addWord flag to true
			  for (int k = 0; k < omitWords.length; k++)	// compare word against each word in the omitWords array
				  if (omitWords[k].equals(cleanWords)) addWord = false;		// set addWords to false if word exists in the omitWords array
			  // if word is longer than two characters and addWord equals true then add word to map
			  if (cleanWords.length() > 2 && addWord == true) {		
				  mapData.addWord(cleanWords);			 
				  addWord = true;
			  }
		  }
	  }
	  // confirm that all data has been updated
	  mapData.finishAdd();

	  map = new Treemap(mapData, 0, 0, width, height);

	    // different choices for the layout method
		//	    MapLayout algorithm = new SliceLayout();
		//	    MapLayout algorithm = new StripTreemap();
		//	    MapLayout algorithm = new PivotBySplitSize();
		//	    MapLayout algorithm = new SquarifiedLayout();
	    // map.setLayout(algorithm);
	  
	  // only run draw() once
	  noLoop();
	  // print list of all words along with number of occurrences to the console
	  mapData.printScreen(); 

	}


	public void draw() {
	  background(255);
	  map.draw();
	}
	
	
	// Create the WordItem Class as an extension of the SingleMapItem class
	class WordItem extends SimpleMapItem {
		  String word;
		  protected double instances;
		  protected double minInstances;
		  protected int color; 

		  WordItem(String word) {
		    this.word = word;
		  }

		  public void incrementSize() {
			  	instances++;
			    if (instances > 3) { 
			    	size++; 
			  	} else {
			  		size = 0;
			  	}
			}
		  
		  public void draw() {

			  smooth();
			  strokeWeight(0.25f);
			  colorMode(HSB, 100);			
			  fill(0, (float)(70+size),(float)(100-size/2));
			  stroke(0, 0, 100);
			  rect(x, y, w, h);

			  PFont font = createFont("Serif", (int)(12+size/2));
			  textFont(font);
			  colorMode(RGB, 255, 255, 255);		    
			  fill(255);

			  if (w > textWidth(word) + 6) {
				  if (h > textAscent() + 6) {
					  textAlign(CENTER, CENTER);
					  text(word, x + w/2, y + h/2);
				  }
			  }
		  }
	}
	
	
	
	class WordMap extends SimpleMapModel {    
		  public HashMap words;
		    
		  WordMap() {
		    words = new HashMap();
		  }
		    
		  public void addWord(String word) {
		    WordItem item = (WordItem) words.get(word);
		    if (item == null) {
		      item = new WordItem(word);
		      words.put(word, item);
		    }
		    item.incrementSize();
		  }
		    
		  public void finishAdd() {
		    items = new WordItem[words.size()];
		    words.values().toArray(items);
		  }
		  
		  void printScreen() {
			   Iterator iterator = words.keySet().iterator();
			   while( iterator. hasNext() ){
			    	String indexString = (String) iterator.next();
			    	WordItem item = (WordItem) words.get(indexString);
			    	System.out.println("WORD: " + indexString + "  " + (item.getSize()));
		   		}
		  }
	}
}
