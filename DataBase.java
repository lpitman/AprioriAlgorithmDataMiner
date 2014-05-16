import java.util.ArrayList;


public class DataBase {
	private String[] attributes;
	private ArrayList<String[]> dbRows;
	
	public DataBase(String[] attributes){
		this.attributes = attributes;
		dbRows = new ArrayList<String[]>();
	}
	
	public void addRow(String[] row){
		dbRows.add(row);
	}
	
	public ArrayList<String[]> getData(){
		return dbRows;
	}
	
	public int countItemset(Itemset itemset){
		int count = 0;
		
		for (String[] row : dbRows){
			int matchCount = 0;
			int k = itemset.getK();
			for (String value : row){
				for (Item item : itemset.getItemList()){
					if (item.getValue().equals(value))
						matchCount++;
				}
			}
			if (matchCount == k)
				count++;
		}
		
		return count;
	}
}
