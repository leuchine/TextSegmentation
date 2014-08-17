import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


class KeywordFilter {
	private ArrayList<TermFrequencyVector> centerList;
	private int numberOfKeyword;
	
	public KeywordFilter() {
		
	}
	
	public KeywordFilter(ArrayList<TermFrequencyVector> centerList, int numberOfKeyword) {
		this.centerList = centerList;
		this.numberOfKeyword = numberOfKeyword;
	}
	
	public ArrayList<String[]> constructKeywordList(HashMap<String, Boolean> referenceTable) {
		ArrayList<String[]> KeywordList = new ArrayList<String[]>();
		
		for(int startIndex = 0; startIndex < centerList.size(); startIndex++) {
			float[] frequencyList = centerList.get(startIndex).getExpandedVector();
			int[] indexOfHighestFrequency = findIndexListOfHighestFrequency(frequencyList);
			String[] currentStringList = findCorespondingWord(indexOfHighestFrequency, startIndex, referenceTable);
			KeywordList.add(currentStringList);
		}
		
		return KeywordList;
	}
	
	private String[] findCorespondingWord(int[] indexOfHighestFrequency, int indexOfCenter, HashMap<String, Boolean> referenceTable) {
		String[] stringList = new String[numberOfKeyword];
		int wordCounter = 0;
		
		for(int startIndex = 0; startIndex < indexOfHighestFrequency.length && wordCounter < numberOfKeyword; startIndex++) {
			String candidateWord = centerList.get(indexOfCenter).getNonDuplicateWordList().get(indexOfHighestFrequency[startIndex]);
			if(referenceTable.get(candidateWord) == false) {
				stringList[wordCounter] = candidateWord;
				referenceTable.put(candidateWord, true);
				wordCounter++;
			}
		}
		
		return stringList;
	}
	
	private int[] findIndexListOfHighestFrequency(float[] sortedFrequencyList) {
		int[] indexOfHighestFrequency = new int[sortedFrequencyList.length];
		
		for(int startOuterIndex = 0; startOuterIndex < sortedFrequencyList.length; startOuterIndex++) {
			float maxValue = -1;
			int indexOfMaxValue = -1;
			for(int startInnerIndex = 0; startInnerIndex < sortedFrequencyList.length; startInnerIndex++) {
				if(sortedFrequencyList[startInnerIndex] > maxValue) {
					maxValue = sortedFrequencyList[startInnerIndex];
					indexOfMaxValue = startInnerIndex;
				}
			}
			
			indexOfHighestFrequency[startOuterIndex] = indexOfMaxValue;
			if(indexOfMaxValue >= sortedFrequencyList.length) {
				System.out.println("Boudary error.");
			} else {
				sortedFrequencyList[indexOfMaxValue] = -1;
			}
		}
		
		return indexOfHighestFrequency;
	}
	
	private void printArray(float[] list) {
		System.out.print("(");
		for(int i = 0; i < list.length; i++) {
			System.out.print(list[i] + " ");
		}
		System.out.println(")");
	}
}
