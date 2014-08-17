import java.util.ArrayList;


class TupleWriter {
	private ArrayList<ArrayList<PositionPair>> parentList;
	private ArrayList<ArrayList<String[]>> KeywordTermList;
	
	public TupleWriter() {
		
	}
	
	public TupleWriter(ArrayList<ArrayList<PositionPair>> parentList, ArrayList<ArrayList<String[]>> KeywordTermList) {
		this.parentList = parentList;
		this.KeywordTermList = KeywordTermList;
	}
	
	public void writeTuple() {
		MySQLConnector sqlConnector = new MySQLConnector();
		sqlConnector.createTable();
		//int nodeIdCounter = 0;
		for(int i = 1; i <= KeywordTermList.size(); i++) {
			System.out.println("Number of cluster: " + i);
			
			for(int j = i - 1; j >= 0; j--) {
				System.out.print("C" + j + ": [" + parentList.get(i-1).get(j).getStartPoint() + ", " +
						parentList.get(i-1).get(j).getEndPoint() + "]  ");
				//nodeIdCounter++;
				int parentId = findParentId(i, j);
				String keywordStr = findKeywordString(i, j);
				String termStr = findTermString(i, j);
				
				System.out.print("parentId: " + parentId);
				System.out.print(" Keyword: " + keywordStr);
				System.out.println(" Term: " + termStr);
				sqlConnector.insertTuple(parentList.get(i-1).get(j).getEndPoint(), 
						parentList.get(i-1).get(j).getStartPoint(), parentId, 1, keywordStr, termStr);
			}
		}
	}
	
	private int findParentId(int i, int j) {
		int parentId = -1;
		if(i == 1) {//root
			parentId = -1;
		} else if(i == 2) {
			parentId = 1;
		} else {
			int curStart = parentList.get(i-1).get(j).getStartPoint();
			int curEnd = parentList.get(i-1).get(j).getEndPoint();
			
			for(int m = 0; m < i - 1; m++) {
				int preStart = parentList.get(i-2).get(m).getStartPoint();
				int preEnd = parentList.get(i-2).get(m).getEndPoint();
				if(preStart >= curStart && preEnd <= curEnd) {
					parentId = calculateParentId(m, i-1);
					break;
				}
			}
		}
		return parentId;
	}
	
	private int calculateParentId(int rank, int level) {
		int sum = 0;
		for(int i = 1; i <= level - 1; i++) {
			sum += i;
		}
		sum = sum + level - rank;
		
		return sum;
	}
	
	private String findKeywordString(int i, int j) {
		StringBuffer keywordStr = new StringBuffer();
		
		String[] strList = KeywordTermList.get(i-1).get(j);
		for(int k = 0; k < 5; k++) {
			keywordStr.append(strList[k] + " ");
		}
		
		return keywordStr.toString();
	}
	
	private String findTermString(int i, int j) {
		StringBuffer termStr = new StringBuffer();
		
		String[] strList = KeywordTermList.get(i-1).get(j);
		for(int k = 5; k < 25; k++) {
			termStr.append(strList[k] + " ");
		}
		
		return termStr.toString();
	}
}
