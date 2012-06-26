package me.konloch.kscript;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * This is my simple script parsing engine called K-Script
 * 
 * The logic behind the scripting system is pretty simple
 * load all of the contents into an array, do a simple loop
 * to parse through all of the data and do the actions
 * based on simple logic
 * 
 * Commands:
 * print 	<object val>											- this prints a message to the console
 * memory 	<object val> 											- this clears the memory cache and resets it to the new value
 * store	<memoery index>\t<object val>							- this stores the value into that memory index
 * goto		<goto line>												- this goes to that certain line
 * if 		<object val>\t<object val>\t<goto line>\t<goto line>	- this checks if value eqauls value, if so it goes to gotoline 1, if not gotoline2
 * 
 * In-Commands:
 * memory	<memory index>											- this loads a value from the memory index
 * 
 * Operators:
 * +																- this adds onto the existing string (need to make a check for int)
 * -																- this converts the strings to ints then does the math function
 * 
 * 
 * memory 	initialize	<value>
 * memory 	add	<value>
 * memory	set	<value>
 * 
 * 
 * A "command" in KScript is the instruction you want the script to do, I.E. goto <value>
 * An "in-command" is something that you would call inside of another command, I.E. goto memory(1)
 * An operator is something you would use inside of an in-command, I.E. goto memory(1)+"5"
 * 
 * 
 * CHANGELOG:
 * 06/24/2012:
 * Added script loading/parsing (loads the script to memory, then processes it)
 * Added command print (Prints out to console)
 * Added command memory (Creates a new memory cache)
 * Added command store (Stores values into the memory cache)
 * Added command mprint (Prints from memory)
 * Added command goto (goes to that lines)
 * Added command if (goes to a certain line depending on the values eqauling>
 * 
 * 06/25/2012:
 * Added in-command parsing for memory (if memory(1)), etc
 * Added a new exception (KScriptException, only thrown if there is an error with the KScript Commands
 * Cleaned a lot of the old script syntax out and replaced with tabs
 * 
 * 06/26/2012:
 * Added + operator
 * Added - operator
 * 
 * 
 * @author Konloch
 *
 */

/*
 * TODO: nothing
 */

public class ScriptManager {
	
	public static void main(String[] args) {
		System.out.println("Welcome to K-Script");
		System.out.println("It's currently being developed by Konloch");
		System.out.println("To keep up with development, please visit www.konloch.me");
		
		Scanner scan = new Scanner(System.in);
		System.out.print("Which script would you like to load?: ");
		String script = scan.nextLine();
		scan.close();
		System.out.println("");
		
		System.out.println("Starting script: " + script);
		System.out.println("=========================================");
		long start = System.nanoTime();
		runScript(script);
		long finish = System.nanoTime() - start;
		System.out.println("=========================================");
		System.out.println("Finished in: " + finish + " NS, or in " + (double)finish / 1000000.0 + " MS");
	}
	
	/*
	 * This is simply used for ease of loading/running scripts
	 */
	public static void runScript(String scriptName) {
		loadScript(scriptName);
		try {
			doScript();
		} catch(KScriptException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * This is the actual script, it contains all of the content for the script to be ran
	 */
	private static ArrayList<String> script;
	
	/*
	* This is used as the virtual memory for the script, it allows loading from memory
	*/
	private static ArrayList<String> memory;
	
	/*
	 * This method loads the script into the array
	 */
	private static void loadScript(String scriptName) {
		String fileName = "scripts/" + scriptName + ".ks";
		try {
		    script = new ArrayList<String>();
		    
		    BufferedReader reader = new BufferedReader(new FileReader(fileName));
		    String contents;
		    while ((contents = reader.readLine()) != null) {
		        script.add(contents);
		    }
		    reader.close();
			
		} catch(FileNotFoundException e) {
			System.out.println("[ERROR]: File not found: " + fileName);
			e.printStackTrace();
		} catch(IOException e) {
			System.out.println("[ERROR]: An IO error has occured");
			e.printStackTrace();
		} catch(Exception e) {
			System.out.println("[ERROR]: Unknown exception");
			e.printStackTrace();
		}
	}

	/*
	 * All of the actual logic behind the scripting language is here
	 * 
	 * Functions are handled here
	 * 
	 */
	private static void doScript() throws KScriptException  {
		if(!script.isEmpty()) {
			for(int line = 0; line < script.size(); line++) {
				if(script.get(line).startsWith("#")) {
					continue;
				}
				
				String[] argument = script.get(line).split("\t");
				if(argument[0].equals("end")) {
					break;
				}
				if(argument[0].startsWith("memory")) {
					int cap = Integer.parseInt(argument[1]);
					memory = new ArrayList<String>(cap);
					for(int i = 0; i < cap; i++) {
						memory.add("");
					}
				}
				if(argument[0].startsWith("print")) {
					System.out.println(parseOperators(argument[1]));
				}
				if(argument[0].startsWith("store")) {
					memory.set(Integer.parseInt(argument[1]), parseOperators(argument[2]));
				}
				if(argument[0].startsWith("goto")) {
					line = Integer.parseInt(argument[1]) -2;
				}
				if(argument[0].startsWith("if")) {
					if(parseOperators(argument[1]).equals(parseOperators(argument[2]))) {
						line = Integer.parseInt(argument[3]) -2;
					} else {
						line = Integer.parseInt(argument[4]) -2;
					}
				}
			}
		} else {
			System.out.println("[ERROR]: Script is empty");
		}
	}

	/*
	 * This is used to parse operators
	 * 
	 * TODO: Clean it up a bit
	 */
	private static String parseOperators(String value) throws KScriptException {
	     StringTokenizer st = new StringTokenizer(value,"-");
	     
	     String finalValue = ""; //all the shit gets added to this
	     
	     int flag = 0;
	     
	     while (st.hasMoreTokens()) {
	         String currentToken = st.nextToken();

	         /* In-Commands */
	         if(currentToken.startsWith("\"")) { //is a pre-defined value
	        	 currentToken = currentToken.substring(1,currentToken.length()-1);
	         } else {
		         if(currentToken.startsWith("memory")) {
					currentToken = memory.get(Integer.parseInt(currentToken.substring("memory".length())));
			      } else if(currentToken.equals("+")) {
			      } else if(currentToken.equals(";")) {
			      } else {
			    	  throw new KScriptException("Cannot find that operator or in-command");
			      }
	         }

	         /* Operator Flags */
	         if(flag == 1) {
		         finalValue += currentToken;
		         flag = 0;
	         } else if(flag == 2) {
	        	 finalValue = ""+(Integer.parseInt(finalValue) - Integer.parseInt(currentToken));
		         flag = 0;
	         } else

		     /* Operators */
		     if(currentToken.equals("+")) {
		    	 flag = 1;
		     } else if(currentToken.equals(";")) {
	        	 flag = 2;
	         } else {
		         finalValue += currentToken;
	         }
	     }
	     
		return finalValue;
		
	}
	
}

/**
 * This class is used for exceptions thrown by the parser/syntax/etc
 * 
 * @author Konloch
 *
 */

class KScriptException extends Exception {
	private static final long serialVersionUID = 8170663931339970813L;
	
	public KScriptException() {
		super();
	}
	
	public KScriptException(String description) {
		super(description);
	}
}
