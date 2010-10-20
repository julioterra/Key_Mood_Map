package keyloggingmap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import processing.core.PApplet;
import processing.core.PFont;
import quicktime.app.spaces.Collection;
import treemap.*;


public class KeyLoggingMap extends PApplet {

	Treemap map;

	public void setup() {
	  size(1024, 600);

	  smooth();
	  strokeWeight(0.25f);
	  PFont font = createFont("Serif", 13);
	  textFont(font);

	  WordMap mapData = new WordMap();  
	  String[] lines = loadStrings("out_logFile.txt");

	  for (int i = 0; i < lines.length; i++) {
		  String[] indWords = split(lines[i]," ");
		  for (int j = 0; j < indWords.length; j++) {
			  String cleanWords = trim(indWords[j]);
			  cleanWords = cleanWords.toLowerCase();
			  if (cleanWords.length() > 2 && !cleanWords.equals("this") && !cleanWords.equals("that") 
				  && !cleanWords.equals("to") && !cleanWords.equals("then") && !cleanWords.equals("the")
				  && !cleanWords.equals(".jpg") && !cleanWords.equals("than") && !cleanWords.equals("these")
				  && !cleanWords.equals("and") && !cleanWords.equals("for") && !cleanWords.equals("from")
				  && !cleanWords.equals("with") && !cleanWords.equals("here") && !cleanWords.equals("has")
				  && !cleanWords.equals("but") && !cleanWords.equals("are") && !cleanWords.equals("when")
				  && !cleanWords.equals("where") && !cleanWords.equals("who") && !cleanWords.equals("how"))
			  mapData.addWord(cleanWords);			  
		  }
	  }
	  mapData.finishAdd();

	    // different choices for the layout method
//	    MapLayout algorithm = new SliceLayout();
//	    MapLayout algorithm = new StripTreemap();
//	    MapLayout algorithm = new PivotBySplitSize();
	    MapLayout algorithm = new SquarifiedLayout();

	  map = new Treemap(mapData, 0, 0, width, height);
	  map.setLayout(algorithm);
	  
	  // only run draw() once
	  mapData.printScreen();
	  noLoop();
	}


	public void draw() {
	  background(255);
	  map.draw();
	}
	
	
	class WordItem extends SimpleMapItem {
		  String word;
		  protected double instances;
		  protected double minInstances;


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
		    fill(255);
		    rect(x, y, w, h);

		    fill(0);
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
//		    bounds = new Rect[words.size()];
		    words.values().toArray(items);
		  }
		  
		  void printScreen() {
			   Iterator iterator = words.keySet().iterator();
			   while( iterator. hasNext() ){
			    	String indexString = (String) iterator.next();
			    	WordItem item = (WordItem) words.get(indexString);
			    	System.out.println("WORD: " + indexString + "  " + (item.getSize()));
		   		}
//			   System.out.println(bounds.x);
		  }
	}
}
