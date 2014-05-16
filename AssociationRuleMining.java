import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;


public class AssociationRuleMining {
	static int rowCount = 0;
	static DataBase db = null;
	static ArrayList<Itemset> infrequentItemsets = null;
	
	public static void main(String[] args) throws IOException {
		String filename = null;
		double minsupRate = 0.0;
		double minconfRate = 0.0;
		int minsupNum = 0;
		Scanner scan = new Scanner(System.in);
		
		// get input from the user
		System.out.println("What is the name of the file containing your data?: ");
		filename = scan.nextLine();
		
		try {
			System.out.println("Please select the minimum support rate (0.00-1.00): ");
			minsupRate = Double.parseDouble(scan.nextLine());
			
			System.out.println("Please select the minimum confidence rate (0.00-1.00): ");
			minconfRate = Double.parseDouble(scan.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("Invalid input format (0.0)");
			e.printStackTrace();
		}

		ArrayList<AVList> avPairs = loadDB(filename); 
		minsupNum = (int) Math.ceil(rowCount * minsupRate);
		
		ArrayList<Itemset> freq1Itemset = buildFirstItemset(avPairs, minsupNum);
		
		KItemset kItemset = new KItemset(2, genFreqKItemset(avPairs, freq1Itemset, minsupRate, 2));
		ArrayList<KItemset> allKgt1Itemsets = new ArrayList<KItemset>();
		
		int k = 3;
		while (null != kItemset.getkItemset()){
			allKgt1Itemsets.add(kItemset);
			kItemset = new KItemset(k, genFreqKItemset(avPairs, kItemset.getkItemset(), minsupRate, k));
			k++;
		}
		
		generateRules(allKgt1Itemsets, minconfRate, minsupRate);
		
		scan.close();
		
		System.out.println("Rule Mining Complete.");
	}

	private static ArrayList<AVList> loadDB(String filename){
		FileInputStream	file;
		BufferedReader dataReader;
		ArrayList<AVList> avLists = null;
		
		try {
			file = new FileInputStream(filename);
			dataReader = new BufferedReader(new InputStreamReader(file));
			
			avLists = new ArrayList<AVList>();
			StringTokenizer lineTok = null;		
			
			String attributeLine = dataReader.readLine();
			lineTok = new StringTokenizer(attributeLine);
			
			// build the database from the input file
			while(lineTok.hasMoreTokens()){
				avLists.add(new AVList(lineTok.nextToken()));
			}
			db = new DataBase(attributeLine.split(" "));
			
			String valueLine = dataReader.readLine();
			while(valueLine != null){
				lineTok = new StringTokenizer(valueLine);
				rowCount++;
				
				for(int i = 0; lineTok.hasMoreTokens(); i++){
					avLists.get(i).add(lineTok.nextToken());
				}
				db.addRow(valueLine.split("\\s+"));
				
				valueLine = dataReader.readLine();
			}
			
			dataReader.close();
		} catch (IOException e) {
			System.out.println("File not found...");
			e.printStackTrace();
		}
		return avLists;
	}
	
	private static ArrayList<Itemset> buildFirstItemset(ArrayList<AVList> avList, int minsup){
		// build candidate 1-item set
		int itemNumber = 0;
		ArrayList<Item> itemList = new ArrayList<Item>();
		for(AVList avPair : avList){
			itemList.addAll(avPair.getItems(itemNumber));
			itemNumber = itemList.get(itemList.size()-1).getNumber()+1;
		}
		
		// create frequent 1-itemset
		ArrayList<Itemset> freqItemset = new ArrayList<Itemset>();
		for (Item item : itemList){
			if (item.getCount() >= minsup){
				Itemset itemset = new Itemset(1);
				itemset.addItem(item);
				freqItemset.add(itemset);
			}
		}
		
		return freqItemset;
	}

	private static ArrayList<Itemset> genFreqKItemset(ArrayList<AVList> avPairs,
			ArrayList<Itemset> freqkItemset, Double minsupRate, int k) {
		ArrayList<Itemset> kItemset = new ArrayList<Itemset>();
		
		if (k == 2){
			for (Itemset itemset : freqkItemset){
				int index = freqkItemset.lastIndexOf(itemset);
				for (Itemset testItemset : freqkItemset){
					Itemset c2Itemset = new Itemset(2);
					Item testItem = testItemset.getItemList().get(0);
					if (!itemset.getItemList().get(0).attrMatch(testItem) && itemset.getItemList().get(0).getNumber() < testItem.getNumber()){
						c2Itemset.addItem(itemset.getItemList().get(0));
						c2Itemset.addItem(testItem);
						kItemset.add(c2Itemset);
					}
				}
			}
		} else {
			for (Itemset itemset : freqkItemset){
				int index = freqkItemset.lastIndexOf(itemset);
				// for each freq k itemset, check all other itemsets to see if they can be joined
				for (Itemset testItemset : freqkItemset){
					// check if the itemsets have k-1 matching items
					if (itemset.firstItemsMatch(testItemset)){
						//check if the itemsets have different attributes in their non matching item
						if (itemset.hasDiffAttr(testItemset) && itemset.notMatchingLt(testItemset)){
							Itemset nextKItemset = new Itemset(itemset.getK()+1);
							
							Set<Item> setJoin = new HashSet<Item>();
							for(int j = 0; j < itemset.getItemList().size(); j++){
								setJoin.add(itemset.getItemList().get(j));
							}
							
							for(int j = 0; j < testItemset.getItemList().size(); j++){
								setJoin.add(testItemset.getItemList().get(j));
							}
							
							for (Item item : setJoin) {
								nextKItemset.addItem(item);
							}
							
							if (!(isInfrequentItemset(nextKItemset)) )
								kItemset.add(nextKItemset);
						}
					}
				}
			}
		}
		
		return pruneItemset(kItemset, minsupRate);
	}

	private static ArrayList<Itemset> pruneItemset(ArrayList<Itemset> kItemset, Double minsupRate) {
		ArrayList<Itemset> freqItemsets = new ArrayList<Itemset>();
		infrequentItemsets = new ArrayList<Itemset>();
		
		for (Itemset itemset : kItemset){
			//index++;
			itemset.setCount(db.countItemset(itemset));
			double supRate = ((double)itemset.getCount()) / rowCount;
			if (supRate >= minsupRate){
				freqItemsets.add(itemset);
			} else {
				infrequentItemsets.add(itemset);
			}
		}
		
		return freqItemsets.isEmpty() ? null : freqItemsets;
	}
	
	private static boolean isInfrequentItemset(Itemset potentialSet){
		boolean isInfrequent = false;
		
		for (Itemset infrequentSet : infrequentItemsets){
			if (potentialSet.containsSmaller(infrequentSet)){
				isInfrequent  = true;
				break;
			}
		}
		
		return isInfrequent;
	}
	
	private static void generateRules(ArrayList<KItemset> allKgt1Itemsets,
			double minconfRate, double minsupRate) {
		try {
			ArrayList<Rule> canRules = new ArrayList<Rule>();
			PrintWriter writer = new PrintWriter("rules", "UTF-8");
			int ruleCount = 0;
			
			for (KItemset kItemset : allKgt1Itemsets){
				for (Itemset itemset : kItemset.getkItemset()){
					ArrayList<Itemset> allSubsets = itemset.getSubsets();
					for (Itemset subset : allSubsets){
						Itemset otherSubset = subset.getOtherSubset(itemset);
						subset.setK(subset.getItemList().size());
						subset.setCount(db.countItemset(subset));
	 					double itemsetSup = (double)itemset.getCount();
	 					double subsetSup = (double)subset.getCount();
						double conf = itemsetSup / subsetSup;
						if (conf >= minconfRate){
							ruleCount++;
							Rule newRule = new Rule(subset, otherSubset, itemsetSup/rowCount, conf, ruleCount);
							canRules.add(newRule);
						}
					}
				}
			}
			
			DecimalFormat df = new DecimalFormat("#.##");
			writer.print("Summary:\n");
			writer.print("Total rows in the original set: " + rowCount + "\n");
			writer.print("Total rules discovered: " + ruleCount + "\n");
			writer.print("The selected measures: Support=" + minsupRate + " Confidence="+ minconfRate + "\n");
			writer.print("---------------------------------------------------------------------------------\n\n");
			writer.print("Rules: \n");
			for (Rule rule : canRules){
				writer.print("Rule#" + rule.getRuleNumber() + ": (Support=" + df.format(rule.getSupRate()) + ", Confidence=" + df.format(rule.getConfRate()) + ")\n");
				writer.print("{ ");
				for (Item item : rule.getLeftSet().getItemList()){
					writer.print(" " + item.getAttribute() + "=" + item.getValue());
				}
				writer.print(" }\n----> {");
				for (Item item : rule.getRightSet().getItemList()){
					writer.print(" " + item.getAttribute() + "=" + item.getValue());
				}
				writer.print(" }\n\n");
			}
			
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
}
