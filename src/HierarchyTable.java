import java.util.ArrayList;
import java.util.HashMap;

class HierarchyTable {
	private HashMap<String, Boolean> referenceTable;
	private ArrayList<ArrayList<String[]>> KeywordTermList;
	private ArrayList<ArrayList<PositionPair>> parentList;
	
	public HierarchyTable() {
		referenceTable = new HashMap<String, Boolean>();
		KeywordTermList = new ArrayList<ArrayList<String[]>>();
		parentList = new ArrayList<ArrayList<PositionPair>>();
	}
	
	public void findHierarchy(ArrayList<String> nonDuplicateWordList, 
			ArrayList<TermFrequencyVector> tfvList, int numberOfClusters) {
	
		constructReferenceTable(nonDuplicateWordList);
		
		for(int startIndex = 1; startIndex <= numberOfClusters; startIndex++) {
			System.out.println("NumberOfCluster: " + startIndex);
			OptimalHistogram optimalHistogram = new OptimalHistogram(numberOfClusters, tfvList);
			optimalHistogram.findOptimalHistogram();
//			optimalHistogram.constructCluster(startIndex);
//			
			ArrayList<PositionPair> positionList = optimalHistogram.getPositionList();
			ArrayList<separatedClusters> clusterList = optimalHistogram.getClusterList();
			
			WeightCalculator wc = new WeightCalculator(positionList, clusterList);
			KeywordFilter kf = new KeywordFilter();
			if(startIndex == 1) {
				ArrayList<TermFrequencyVector> centerList = wc.calculateWeight_TFDF();
				kf = new KeywordFilter(centerList, 25);
			} else {
				ArrayList<TermFrequencyVector> centerList = wc.calculateWeight_TFIDF();
				kf = new KeywordFilter(centerList, 25);
			}
			ArrayList<String[]> KeywordList = kf.constructKeywordList(referenceTable);
			KeywordTermList.add(KeywordList);
			
			ArrayList<PositionPair> current = new ArrayList<PositionPair>();
			for(int i = 0; i < KeywordList.size(); i++) {
				System.out.print("C" + i + ": [" + positionList.get(i).getStartPoint() + ", " + positionList.get(i).getEndPoint() + "] ");
				current.add(new PositionPair(positionList.get(i).getStartPoint(), positionList.get(i).getEndPoint()));
				
				for(int j = 0; j < KeywordList.get(i).length; j++) {
					String[] temp = KeywordList.get(i); 
					System.out.print(temp[j] + " ");
				}
				System.out.println();
			}
			parentList.add(current);
		}
	}

	private void constructReferenceTable(ArrayList<String> nonDuplicateWordList) {
		for(int startIndex = 0; startIndex < nonDuplicateWordList.size(); startIndex++) {
			referenceTable.put(nonDuplicateWordList.get(startIndex), false);
		}
	}
	
	public ArrayList<ArrayList <String[]>> getKeywordTermList() {
		return KeywordTermList;
	}
	
	public ArrayList<ArrayList<PositionPair>> getParentList() {
		return parentList;
	}
}
