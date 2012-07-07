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
 * Functions:
 * print 	<object val>											- this prints a message to the console
 * memory 	<object val> 											- this clears the memory cache and resets it to the new value
 * store	<memoery index> <object val>							- this stores the value into that memory index
 * goto		<goto line>												- this goes to that certain line
 * if 		<object val> <object val> <goto line> <goto line>		- this checks if value eqauls value, if so it goes to gotoline 1, if not gotoline2
 * 
 * Operators:
 * memory	<address>												- this loads a value from the memory address
 * +																- this adds onto the existing string (need to make a check for int)
 * --																- this converts the strings to ints then does the math function
 * ++																- this converts the strings to ints then does the math function
 * 
 * To add onto K-Script you need to add your own outcall operators and your own functions, right now it's
 * 100% hardcoded, but in the future I'll have some kind of easy system to add outcall operators/functions
 * 
 * @author Konloch
 *
 */

public class ScriptManager {
	
	public static void main(String[] args) {
		try {
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
		} catch(KScriptException e) {
			System.out.println("[ERROR]: K-Script error");
			e.printStackTrace();
		} catch(FileNotFoundException e) {
			System.out.println("[ERROR]: File not found: ");
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
	 * This is simply used for ease of loading/running scripts
	 */
	public static void runScript(String scriptName) throws Exception {
		loadScript(scriptName);
		doScript();
	}
	
	/*
	 * This is the actual script, it contains all of the content for the script to be ran
	 */
	private static ArrayList<String> script;
	
	/*
	* This is used as the virtual memory for the script, it allows loading to/from memory
	*/
	private static ArrayList<String> memory;
	
	/*
	 * This method loads the script into the array
	 */
	private static void loadScript(String scriptName) throws Exception {
		String fileName = "scripts/" + scriptName + ".ks";
		    script = new ArrayList<String>();
		    
		    BufferedReader reader = new BufferedReader(new FileReader(fileName));
		    String contents;
		    while ((contents = reader.readLine()) != null) {
		        script.add(contents);
		    }
		 reader.close();
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
				if(argument[0].startsWith("ifeq")) {
					if(parseOperators(argument[1]).equals(parseOperators(argument[2]))) {
						line = Integer.parseInt(argument[3]) -2;
					} else {
						line = Integer.parseInt(argument[4]) -2;
					}
				}
				if(argument[0].startsWith("ifne")) {
					if(!parseOperators(argument[1]).equals(parseOperators(argument[2]))) {
						line = Integer.parseInt(argument[3]) -2;
					} else {
						line = Integer.parseInt(argument[4]) -2;
					}
				}
				if(argument[0].startsWith("ifgt")) {
					if(Integer.parseInt(parseOperators(argument[1])) > Integer.parseInt((parseOperators(argument[2])))) {
						line = Integer.parseInt(argument[3]) -2;
					} else {
						line = Integer.parseInt(argument[4]) -2;
					}
				}
				if(argument[0].startsWith("iflt")) {
					if(Integer.parseInt(parseOperators(argument[1])) < Integer.parseInt((parseOperators(argument[2])))) {
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
	     StringTokenizer st = new StringTokenizer(value,"_");
	     
	     String finalValue = ""; //all the shit gets added to this
	     
	     int flag = 0;
	     
	     while (st.hasMoreTokens()) {
	         String currentToken = st.nextToken();

	         /* Pre-Defined Value */
	         if(currentToken.startsWith("\"")) {
	        	 currentToken = currentToken.substring(1,currentToken.length()-1);
	         } else {
	        	 /* Outcall Operators */
	        	 /* Feel free to add more */
		         if(currentToken.startsWith("memory")) {
					currentToken = memory.get(Integer.parseInt(currentToken.substring("memory".length())));
			      } else if(currentToken.startsWith("times")) {
					currentToken = "" + (int) (System.currentTimeMillis() / 1000) % 60 ;
			      } else
			      
			      /* Operators */
			      /* I don't suggest messing with these */
			      if(currentToken.equals("+")) {
			      } else if(currentToken.equals("--")) {
			      } else if(currentToken.equals("++")) {
			      }
			      
			      else {
			    	  throw new KScriptException("Cannot find that operator");
			      }
	         }

	         /* Operator Flags */
	         if(flag == 1) {
		         finalValue += currentToken;
		         flag = 0;
	         } else if(flag == 2) {
	        	 finalValue = ""+(Integer.parseInt(finalValue) - Integer.parseInt(currentToken));
		         flag = 0;
	         } else if(flag == 3) {
	        	 finalValue = ""+ (Integer.parseInt(finalValue) + Integer.parseInt(currentToken));
		         flag = 0;
	         } else

		     /* Operators */
		     if(currentToken.equals("+")) {
		    	 flag = 1;
		     } else if(currentToken.equals("--")) {
	        	 flag = 2;
	         } else if(currentToken.equals("++")) {
		    	 flag = 3;
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
