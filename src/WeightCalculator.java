import java.util.ArrayList;


class WeightCalculator {
	private ArrayList<PositionPair> positionList;
	private ArrayList<separatedClusters> clusterList;
	
	public WeightCalculator() {
		
	}
	
	public WeightCalculator(ArrayList<PositionPair> positionList, ArrayList<separatedClusters> clusterList) {
		this.positionList = positionList;
		this.clusterList = clusterList;
	}
	
	public ArrayList<TermFrequencyVector> calculateWeight_TFIDF() {
		ArrayList<TermFrequencyVector> centerList = new ArrayList<TermFrequencyVector>();
		
		for(int startIndex = 0; startIndex < clusterList.size(); startIndex++) {
			separatedClusters sc = clusterList.get(startIndex);
			sc.findTheCenterOfThisPartUsing_TFIDF();
			centerList.add(sc.getCenter());
		}
		
		return centerList;
	}
	
	public ArrayList<TermFrequencyVector> calculateWeight_TFDF() {
		ArrayList<TermFrequencyVector> centerList = new ArrayList<TermFrequencyVector>();
		
		for(int startIndex = 0; startIndex < clusterList.size(); startIndex++) {
			separatedClusters sc = clusterList.get(startIndex);
			sc.findTheCenterOfThisPartUsing_TFDF();
			centerList.add(sc.getCenter());
		}
		
		return centerList;
	}
}
