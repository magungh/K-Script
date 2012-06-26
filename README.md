K-Script
========

K-Script is an open sourced script parser with logic for java

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