import java.util.ArrayList;


public class KItemset {
	private int k;
	private ArrayList<Itemset> kItemset;
	
	public KItemset(int k, ArrayList<Itemset> kItemset){
		this.k = k;
		this.kItemset = kItemset;
	}

	public int getK() {
		return k;
	}

	public ArrayList<Itemset> getkItemset() {
		return kItemset;
	}

	@Override
	public String toString() {
		String output = "";
		for (Itemset item : kItemset){
			output.concat("\n" + item.toString() + "\n");
		}
		return "k = " + k + "; number = " + kItemset.size() + "\n" + kItemset + "\n";
	}
	
	
}
