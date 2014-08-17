import java.util.ArrayList;
import java.util.Arrays;

class TermFrequencyVector {
	private int dimension;
	private ArrayList<PairNumber> compressedVector;
	private float[] expandedTFVector;
	private ArrayList<String> nonDuplicateWordList;
	private float[] TF_IDF_Vector;
	private float[] TF_DF_Vector;

	public TermFrequencyVector() {

	}

	public void setTF_IDF_Vector(ArrayList<Float> TF_IDF_List) {
		float[] vector = new float[TF_IDF_List.size()];

		for (int startIndex = 0; startIndex < TF_IDF_List.size(); startIndex++) {
			vector[startIndex] = TF_IDF_List.get(startIndex);
		}

		this.TF_IDF_Vector = vector;
	}

	public void setTF_DF_Vector(ArrayList<Float> TF_DF_List) {
		float[] vector = new float[TF_DF_List.size()];

		for (int startIndex = 0; startIndex < TF_DF_List.size(); startIndex++) {
			vector[startIndex] = TF_DF_List.get(startIndex);
		}

		this.TF_DF_Vector = vector;
	}

	public TermFrequencyVector(ArrayList<PairNumber> compressedVector,
			int dimension) {
		nonDuplicateWordList = new ArrayList<String>();
		this.compressedVector = compressedVector;
		this.dimension = dimension;
		this.TF_DF_Vector = new float[dimension];
		this.TF_IDF_Vector = new float[dimension];
		expandedTFVector = new float[dimension];
		expandCompressedVector();
		normalizeExpandedVector();
	}

	public TermFrequencyVector(float[] expandedVector) {
		nonDuplicateWordList = new ArrayList<String>();
		this.expandedTFVector = expandedVector;
		compressedVector = new ArrayList<PairNumber>();
		dimension = expandedVector.length;
		compressExpandedVector();
		normalizeExpandedVector();
	}

	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

	public int getDimension() {
		return dimension;
	}

	/*
	 * value should be positive, if value = 0, means this vector is for followUp
	 * 0s.
	 */
	public void setVector(float value, int compression) {
		compressedVector.add(new PairNumber(value, compression));
	}

	public void setNonDuplicateWordList(ArrayList<String> nonDuplicateWordList) {
		this.nonDuplicateWordList = nonDuplicateWordList;
	}

	public ArrayList<String> getNonDuplicateWordList() {
		return nonDuplicateWordList;
	}

	public ArrayList<PairNumber> getVector() {
		return compressedVector;
	}

	public float[] expandCompressedVector() {
		int p = 0;
		for (int startIndex = 0; startIndex < compressedVector.size(); startIndex++) {
			int compression = compressedVector.get(startIndex).getCompression();
			for (int j = 0; j < compression; j++) {
				expandedTFVector[p] = 0;
				p++;
			}
			if (compressedVector.get(startIndex).getRealValue() != 0) {
				expandedTFVector[p] = compressedVector.get(startIndex)
						.getRealValue();
				p++;
			}
		}

		return expandedTFVector;
	}

	private void compressExpandedVector() {
		int compression = 0;
		for (int startIndex = 0; startIndex < expandedTFVector.length; startIndex++) {
			if (expandedTFVector[startIndex] != 0) {
				compressedVector.add(new PairNumber(
						expandedTFVector[startIndex], compression));
				compression = 0;
			} else {
				compression++;
			}

			if (startIndex == expandedTFVector.length - 1 && compression != 0) {
				compressedVector.add(new PairNumber(0, compression));
			}
		}
	}

	public float calculateCosineSimilarityWith(TermFrequencyVector secondVector) {
		assert (this.dimension == secondVector.getDimension());

		float cosineSimilarity = 0;
		float dotProductOfTwoVectors = 0;

		float[] secondExpandedVector = secondVector.TF_IDF_Vector;
		for (int startIndex = 0; startIndex < this.dimension; startIndex++) {
			dotProductOfTwoVectors += (this.TF_IDF_Vector[startIndex] * secondExpandedVector[startIndex]);
		}
		cosineSimilarity = dotProductOfTwoVectors;

		return cosineSimilarity;
	}

	public float[] getExpandedVector() {
		return this.expandedTFVector;
	}

	public float[] getTF_IDF_Vector() {
		return this.TF_IDF_Vector;
	}

	public float[] getTF_DF_Vector() {
		return this.TF_DF_Vector;
	}

	public void printExpandedVector() {
		System.out.print("(");
		for (int startIndex = 0; startIndex < expandedTFVector.length; startIndex++) {
			System.out.print(expandedTFVector[startIndex] + ", ");
		}
		System.out.println(")");
	}

	public void printTFIDFVector() {
		System.out.print("(");
		for (int startIndex = 0; startIndex < TF_IDF_Vector.length; startIndex++) {
			System.out.print(TF_IDF_Vector[startIndex] + ", ");
		}
		System.out.println(")");
	}

	public void printTFDFVector() {
		System.out.print("(");
		for (int startIndex = 0; startIndex < TF_DF_Vector.length; startIndex++) {
			System.out.print(TF_DF_Vector[startIndex] + ", ");
		}
		System.out.println(")");
	}

	public void printVector() {
		System.out.print("(");
		for (int startIndex = 0; startIndex < compressedVector.size(); startIndex++) {
			int compression = compressedVector.get(startIndex).getCompression();
			if (compressedVector.get(startIndex).getRealValue() == 0) {
				int followUp = compressedVector.get(startIndex)
						.getCompression();
				while (followUp != 0) {
					if (followUp == 1) {
						System.out.print("0");
					} else {
						System.out.print("0, ");
					}
					followUp--;
				}
			} else {
				while (compression != 0) {
					System.out.print("0, ");
					compression--;
				}

				if (startIndex == compressedVector.size() - 1) {
					System.out.print(compressedVector.get(startIndex)
							.getRealValue());
				} else {
					System.out.print(compressedVector.get(startIndex)
							.getRealValue() + ", ");
				}
			}
		}
		System.out.println(")");
	}

	public void normalizeExpandedVector() {
		float lengthOfExpandedVector = findLength(expandedTFVector);

		for (int startIndex = 0; startIndex < expandedTFVector.length; startIndex++) {
			expandedTFVector[startIndex] = (expandedTFVector[startIndex] / lengthOfExpandedVector);
		}
	}
	
	public void normalizeTFIDF() {
		float lengthOfExpandedVector = findLength(this.TF_IDF_Vector);

		for (int startIndex = 0; startIndex < this.TF_IDF_Vector.length; startIndex++) {
			this.TF_IDF_Vector[startIndex] = (this.TF_IDF_Vector[startIndex] / lengthOfExpandedVector);
		}
	}
	
	public static float findLength(float[] vector) {
		float squareOfLength = 0;

		for (int startIndex = 0; startIndex < vector.length; startIndex++) {
			squareOfLength += Math.pow(vector[startIndex], 2);
		}

		float length = (float) Math.sqrt(squareOfLength);
		return length;
	}

	public static float findLength2(float[] vector) {
		float squareOfLength = 0;

		for (int startIndex = 0; startIndex < vector.length; startIndex++) {
			squareOfLength += Math.pow(vector[startIndex], 2);
		}

		float length =squareOfLength;
		return length;
	}
	public void print() {
		System.out.print("[");
		for (int i = 0; i < this.expandedTFVector.length; i++) {
			System.out.print(this.expandedTFVector[i] + ",");
		}
		System.out.println("]");
	}
}

class PairNumber {
	private float realValue;
	private int compression;

	public PairNumber() {

	}

	public PairNumber(float realValue, int compression) {
		this.realValue = realValue;
		this.compression = compression;
	}

	public float getRealValue() {
		return realValue;
	}

	public int getCompression() {
		return compression;
	}

}
