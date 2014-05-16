import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class AVList {
	private String attribute;
	private ArrayList<String> values;
	
	public AVList(String attribute){
		this.attribute = attribute;
		this.values = new ArrayList<String>();
	}
	
	public void add(String value){
		values.add(value);
	}

	@Override
	public String toString() {
		return "AVPair [attribute=" + attribute + ", values=" + values + "]";
	}
	
	public ArrayList<Item> getItems(int itemNum){
		ArrayList<Item> items = new ArrayList<Item>();
		Set<String> uniqueValues = new HashSet<String>(values);
		
		for(String value : uniqueValues){
			Item item = new Item(attribute, value, itemNum);
			int count = 0;
			for(String valueCheck : values){
				if (value.equals(valueCheck)) 
					count++;
			}
			item.setCount(count);
			items.add(item);
			itemNum++;
		}
		
		return items;
	}
}
