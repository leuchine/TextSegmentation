
class FloatBooleanPair {
	private float floatNumber;
	private boolean booleanIndicator;
	
	public FloatBooleanPair() {
		
	}

	public FloatBooleanPair(float number, boolean indicator) {
		floatNumber = number;
		booleanIndicator = indicator;
	}
	
	public void setNumber(float number) {
		floatNumber = number;
	}
	
	public void setIndicator(boolean indicator) {
		booleanIndicator = indicator;
	}
	
	public float getNumber() {
		return floatNumber;
	}
	
	public boolean getIndicator() {
		return booleanIndicator;
	}
}
