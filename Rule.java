
public class Rule {
	private Itemset leftSet;
	private Itemset rightSet;
	private double supRate;
	private double confRate;
	private int ruleNumber;
	
	public Rule(Itemset leftSet, Itemset rightSet, double supRate, double confRate, int ruleNumber){
		this.leftSet = leftSet;
		this.rightSet = rightSet;
		this.supRate = supRate;
		this.confRate = confRate;
		this.ruleNumber = ruleNumber;
	}

	public Itemset getLeftSet() {
		return leftSet;
	}

	public Itemset getRightSet() {
		return rightSet;
	}

	public double getSupRate() {
		return supRate;
	}

	public double getConfRate() {
		return confRate;
	}
	
	public int getRuleNumber() {
		return ruleNumber;
	}
}
