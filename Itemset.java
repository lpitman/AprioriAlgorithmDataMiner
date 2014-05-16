import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;


public class Itemset {
	private int k;
	private ArrayList<Item> itemList;
	private int i;
	private int count;
	
	public Itemset(int k){
		this.k = k;
		itemList = new ArrayList<Item>();
		i = 0;
		count = 0;
	}
	
	public Itemset(){
		this.k = 0;
		itemList = new ArrayList<Item>();
		i = 0;
		count = 0;
	}
	
	public Itemset(Itemset otherset){
		this.k = otherset.k;
		this.itemList = otherset.itemList;
		this.i = otherset.i;
		this.count = otherset.count;
	}
	
	public void addItem(Item item){
		itemList.add(item);
	}
	
	public void setCount(int count){
		this.count = count;
	}
	
	public void setK(int k){
		this.k = k;
	}
	
	public int getCount(){
		return count;
	}

	@Override
	public String toString() {
		return "ItemList=" + itemList + " count = " + count + "\n";
	}
	
	public ArrayList<Item> getItemList(){
		return itemList;
	}
	
	public int getK(){
		return k;
	}
	
	public int getMatchingItems(Itemset otherset){
		int matchCount = 0;
		
		for (int i = 0; i < itemList.size(); i++){
			for (int j = 0; j < otherset.getItemList().size(); j++){
				if (itemList.get(i).isMatch(otherset.getItemList().get(j)))
					matchCount++;
			}
		}
		
		return matchCount;
	}
	
	public boolean firstItemsMatch(Itemset otherset){
		boolean match = true;
		
		for (int i = 0; i < itemList.size()-1; i++){
			if (!itemList.get(i).isMatch(otherset.getItemList().get(i))){
				match = false;
				break;
			}
		}
		
		return match;
	}
	
	public boolean notMatchingLt(Itemset otherset){
		return itemList.get(k-1).lessThan(otherset.getItemList().get(k-1));
	}
	
	public boolean hasDiffAttr(Itemset otherset){
		int matchCount = 0;
		
		for (int i = 0; i < itemList.size(); i++){
			for (int j = 0; j < otherset.getItemList().size(); j++){
				if (itemList.get(i).attrMatch(otherset.getItemList().get(j)))
					matchCount++;
			}
		} 
		
		return matchCount == k-1;
	}
	
	public boolean containsSmaller(Itemset checkItemset){
		
		return getMatchingItems(checkItemset) == checkItemset.getK();
	}
	
	public boolean contains(Itemset checkItemset){
		
		return getMatchingItems(checkItemset) == k;
	}

	public ArrayList<Itemset> getSubsets(){
		int psSize = itemList.size()*itemList.size();
		ArrayList<Itemset> ps = new ArrayList<Itemset>();
		
		for (int i = 0; i<psSize; i++){
			Itemset newItemset = new Itemset();
			for (int j = 0; j<itemList.size(); j++){
				if (BigInteger.valueOf((long)i).testBit(j)){
					newItemset.addItem(itemList.get(j));
				}
			}
			if (newItemset.getItemList().size() > 0 && newItemset.getItemList().size() < itemList.size())
				ps.add(newItemset);
		}
		
		return ps;
	}
	
	private void removeItem(Item removeItem){
		ArrayList<Item> newItemList = new ArrayList<Item>();
		for (Item item : itemList){
			if (!item.isMatch(removeItem)){
				newItemList.add(item);
			}
		}
		itemList = newItemList;
	}

	public Itemset getOtherSubset(Itemset itemset) {
		Itemset newSubset = new Itemset(itemset);
		
		for (Item item : itemList){
			for (Item otherItem : itemset.getItemList()){
				if (item.isMatch(otherItem))
					newSubset.removeItem(otherItem);
			}
		}
		
		return newSubset;
	}
}
