
public class Item {
	private String attribute;
	private String value;
	private int count;
	private int number;
	
	public Item(String attribute, String value, int number){
		this.attribute = attribute;
		this.value = value;
		this.number = number;
		count = 0;
	}

	public Item(){
		this.attribute = "";
		this.value = "";
		this.number = -1;
		count = 0;
	}
	
	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public int getNumber(){
		return number;
	}

	@Override
	public String toString() {
		return "Item [attribute=" + attribute + ", value=" + value + "]";
	}
	
	public boolean isMatch(Item otherItem){
		return attribute.equals(otherItem.getAttribute()) && value.equals(otherItem.getValue());
	}
	
	public boolean attrMatch(Item otherItem){
		return attribute.equals(otherItem.getAttribute());
	}

	public boolean lessThan(Item nonMatchingItem) {
		return number < nonMatchingItem.getNumber();
	}
}
