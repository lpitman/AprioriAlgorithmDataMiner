README - Association Rule Mining
--------------------------------

This program implements the apriori association rule
mining algorithm in Java.

The code consists of the following Java files:
	AssociationRuleMining.java	- main(), loadDB(), buildFirstItemset(),
																genFreqKItemset(), pruneItemset(),
																isInfrequentItemset(), generateRules()
	AVList.java									- AVList(), add(), toString(), getItems()
	DataBase.java								- DataBase(), addRow(), getData(),
																countItemset()
	Item.java										- Item(), getters(), setters(), isMatch()
																attrMatch(), lessThan(), toString()
	Itemset.java								- Itemset(), getters(), setters(), addItem(),
																getMatchingItems(), firstItemsMatch(),
																notMatchingLt(), hasDiffAttr(), 
																containsSmaller(), contains(), getSubsets(),
																getOtherSubset(), removeItem(), toString()
	KItemeset.java							- KItemset(), getters(), toString()
	Rule.java										- Rule(), getters()
	
	AssociationRuleMining.java 	- The main part of the program, does the bulk
																of the work.
	AVList.java									- Represents the attribute=value pairs found
																in the data in the form of an attribute and
																all the values associated.
	DataBase.java 							- Represents the data for the program in the 
																form of all of the rows in a list.
	Item.java										- Represents an item from the db.
	Itemset.java								- Represents a set of k items from the db.
	KItemset.java								- Represents a set of Itemsets of size k.
	Rule.java										- Represents a rule for the data.
	
Program Structure:
	
	main()---->loadDB
				
				---->buildFirstItemset
								---->getItems
				
				---->genFreqKItemset
								---->firstItemsMatch
								---->hasDiffAttr
								---->notMatchingLt
								---->isInfrequentItemset
								---->pruneItemset
												---->countItemset
								
				---->generateRules
								---->getKItemset
								---->getSubsets
								---->getOtherSubset
								---->countItemset

Run the Program:
	bluenose: make
	bluenose: java AssociationRuleMining

The result is stored in the file Rules, to view the result:                         
   bluenose: more Rules 
