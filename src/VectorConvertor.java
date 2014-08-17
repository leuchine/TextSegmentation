import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

class VectorConvertor {
	private String filePath;
	private static int numberOfPart;
	private ArrayList<String> partList;
	private ArrayList<TermFrequencyVector> tfvList;
	private ArrayList<HashMap<String, ArrayList<Integer>>> occurrenceLocalList;
	private HashMap<String, ArrayList<Integer>> occurrenceGlobalList;
	private ArrayList<String> nonDuplicateWordList;
	private int numOfWordPerPart;
	private ArrayList<Integer> record;

	public VectorConvertor(String filePath, int numberOfPart) {
		this.filePath = filePath;
		this.numberOfPart = numberOfPart;

		partList = new ArrayList<String>();
		tfvList = new ArrayList<TermFrequencyVector>();
		occurrenceLocalList = new ArrayList<HashMap<String, ArrayList<Integer>>>();
		occurrenceGlobalList = new HashMap<String, ArrayList<Integer>>();
		nonDuplicateWordList = new ArrayList<String>();
		record = new ArrayList<Integer>();
	}

	public ArrayList<TermFrequencyVector> constructTermFrequencyVector()
			throws IOException {
		WordListConverter wordListConverter = new WordListConverter(filePath);
		String bookContent = wordListConverter.extractUsefulInformation();
//		record = wordListConverter.getRecordForSentenceNumber();
		String[] wordList = bookContent.split(" ");
		// System.out.println(Arrays.toString(wordList));
		// System.out.println(wordList.length);
		constructPartList(wordList);

//		for (int i = 0; i < partList.size(); i++) {
//			System.out.println(i + ": " + partList.get(i));
//		}

		nonDuplicateWordList = constructOccurrenceMap(wordList);
		// for(int i = 0; i < nonDuplicateWordList.size(); i++) {
		// System.out.println(nonDuplicateWordList.get(i));
		// }
//		System.out.println(nonDuplicateWordList);
		tfvList = createTermFrequencyVector(nonDuplicateWordList);

		storeNonDuplicateWordList(nonDuplicateWordList);
		return tfvList;
	}

	private void storeNonDuplicateWordList(ArrayList<String> list) {
		for (int startIndex = 0; startIndex < tfvList.size(); startIndex++) {
			tfvList.get(startIndex).setNonDuplicateWordList(list);
		}
	}

	private ArrayList<TermFrequencyVector> createTermFrequencyVector(
			ArrayList<String> list) {
		ArrayList<TermFrequencyVector> newTfvList = new ArrayList<TermFrequencyVector>();

		for (int startOuterIndex = 0; startOuterIndex < partList.size(); startOuterIndex++) {
			int countZeroCompression = 0;
			ArrayList<PairNumber> currentTfv = new ArrayList<PairNumber>();
			ArrayList<Float> TF_IDF_Vector = new ArrayList<Float>();
			ArrayList<Float> TF_DF_Vector = new ArrayList<Float>();
			float zero = 0;

			for (int startInnerIndex = 0; startInnerIndex < list.size(); startInnerIndex++) {
				String currentWord = list.get(startInnerIndex);
				ArrayList<Integer> localPositionList = occurrenceLocalList.get(
						startOuterIndex).get(currentWord);

				if (localPositionList != null) {
					int localFrequency = localPositionList.size();
					int numberOfPartsContainCurrentWord = 0;
					if (occurrenceGlobalList.get(currentWord) != null) {
						numberOfPartsContainCurrentWord = occurrenceGlobalList
								.get(currentWord).size();
					}
					float tf_idf = localFrequency
							* (float) Math.log((double) numberOfPart
									/ (double) numberOfPartsContainCurrentWord);
					float tf_df = localFrequency;
					float tf = localFrequency;
					TF_IDF_Vector.add(tf_idf);
					TF_DF_Vector.add(tf_df);
					currentTfv.add(new PairNumber(tf, countZeroCompression));
					countZeroCompression = 0;
				} else {
					TF_IDF_Vector.add(zero);
					TF_DF_Vector.add(zero);
					countZeroCompression++;
				}
			}

			// This step is to store the last followUp 0s.
			if (countZeroCompression != 0) {
				currentTfv.add(new PairNumber(0, countZeroCompression));
			}

			TermFrequencyVector newTfv = new TermFrequencyVector(currentTfv,
					list.size());
			newTfv.setTF_IDF_Vector(TF_IDF_Vector);
			newTfv.setTF_DF_Vector(TF_DF_Vector);
			newTfv.normalizeTFIDF();
			newTfvList.add(newTfv);
		}

		return newTfvList;
	}

	private void constructPartList(String[] wordList) throws IOException {
		partList = splitContent(wordList);
	}

	private ArrayList<String> constructOccurrenceMap(String[] wordList) {
		return findOccurence(wordList);
	}

	private ArrayList<String> findOccurence(String[] wordList) {
		ArrayList<String> list = new ArrayList<String>();
		int numberOfWord = wordList.length;
		int numberOfWordsPerPart = numberOfWord / numberOfPart;
		numOfWordPerPart = numberOfWordsPerPart;

		for (int startOuterIndex = 0; startOuterIndex < numberOfPart; startOuterIndex++) {
			occurrenceLocalList.add(new HashMap<String, ArrayList<Integer>>());
			int wordPosition = 0;

			int startInnerIndex = 0;
			for (startInnerIndex = startOuterIndex * numberOfWordsPerPart; startInnerIndex < (startOuterIndex + 1)
					* numberOfWordsPerPart; startInnerIndex++) {
				String currentWord = wordList[startInnerIndex];

				if (!list.contains(currentWord)) {
					list.add(currentWord);
				}

				ArrayList<Integer> localPosition = new ArrayList<Integer>();
				if (occurrenceLocalList.get(startOuterIndex).containsKey(
						currentWord)) {
					localPosition = occurrenceLocalList.get(startOuterIndex)
							.get(currentWord);
				} else {
					ArrayList<Integer> globalPartList = new ArrayList<Integer>();
					if (occurrenceGlobalList.containsKey(currentWord)) {
						globalPartList = occurrenceGlobalList.get(currentWord);
					}
					globalPartList.add(startOuterIndex);
					occurrenceGlobalList.put(currentWord, globalPartList);
				}
				localPosition.add(wordPosition);
				occurrenceLocalList.get(startOuterIndex).put(currentWord,
						localPosition);
				wordPosition += currentWord.length() + 1;
			}

			if (startOuterIndex == numberOfPart - 1) {
				while (startInnerIndex < wordList.length) {
					String currentWord = wordList[startInnerIndex];

					if (!list.contains(currentWord)) {
						list.add(currentWord);
					}

					ArrayList<Integer> localPosition = new ArrayList<Integer>();
					if (occurrenceLocalList.get(startOuterIndex).containsKey(
							currentWord)) {
						localPosition = occurrenceLocalList
								.get(startOuterIndex).get(currentWord);
					} else {
						ArrayList<Integer> globalPartList = new ArrayList<Integer>();
						if (occurrenceGlobalList.containsKey(currentWord)) {
							globalPartList = occurrenceGlobalList
									.get(currentWord);
						}
						globalPartList.add(startOuterIndex);
						occurrenceGlobalList.put(currentWord, globalPartList);
					}
					localPosition.add(wordPosition);
					occurrenceLocalList.get(startOuterIndex).put(currentWord,
							localPosition);
					wordPosition += currentWord.length() + 1;
					startInnerIndex++;
				}
			}
		}
		Collections.sort(list);
		return list;
	}

	private ArrayList<String> splitContent(String[] wordList) {
		ArrayList<String> newPartList = new ArrayList<String>();
		int numberOfWord = wordList.length;
		int numberOfWordsPerPart = numberOfWord / numberOfPart;

		for (int startOuterIndex = 0; startOuterIndex < numberOfPart; startOuterIndex++) {
			StringBuffer strBuf = new StringBuffer();
			int startInnerIndex = 0;
			for (startInnerIndex = 0; startInnerIndex < numberOfWordsPerPart; startInnerIndex++) {
				strBuf.append(wordList[startOuterIndex * numberOfWordsPerPart
						+ startInnerIndex]
						+ " ");
			}

			if (startOuterIndex == numberOfPart - 1) {
				while (startOuterIndex * numberOfWordsPerPart + startInnerIndex != wordList.length) {
					strBuf.append(wordList[startOuterIndex
							* numberOfWordsPerPart + startInnerIndex]
							+ " ");
					startInnerIndex++;
				}
			}
			newPartList.add(strBuf.toString());
		}
		return newPartList;
	}

	public ArrayList<Integer> getRecord() {
		return this.record;
	}

	public int getNumberOfWordsPerPart() {
		return this.numOfWordPerPart;
	}

	public ArrayList<String> getNonDuplicateWordList() {
		return nonDuplicateWordList;
	}
}