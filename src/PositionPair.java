
class PositionPair {
	private int startPoint;
	private int endPoint;
	
	public PositionPair(int startPoint,int endPoint) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}
	
	public void setStartPoinnt(int startPoint) {
		this.startPoint = startPoint;
	}
	
	public void setEndPoint(int endPoint) {
		this.endPoint = endPoint;
	}
	
	public int getStartPoint() {
		return this.startPoint;
	}
	
	public int getEndPoint() {
		return this.endPoint;
	}
}
