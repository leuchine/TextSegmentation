import java.util.ArrayList;

class separatedClusters {
	private ArrayList<TermFrequencyVector> tfvsInThisPart;
	private TermFrequencyVector center;
	private float intraDist;
	private float radius;

	public void print() {
		for(int i=0;i<this.tfvsInThisPart.size();i++){
			this.tfvsInThisPart.get(i).printTFDFVector();
		}
	}

	public separatedClusters() {
		this(new ArrayList<TermFrequencyVector>());
	}

	public separatedClusters(ArrayList<TermFrequencyVector> tfvListOfThisPart) {
		tfvsInThisPart = tfvListOfThisPart;
		center = new TermFrequencyVector();
	}

	public void findTheCenterOfThisPart() {
		if (!tfvsInThisPart.isEmpty()) {
			float[] sum = new float[tfvsInThisPart.get(0).getDimension()];
			for (int startIndex = 0; startIndex < tfvsInThisPart.size(); startIndex++) {
				sum = vectorAddition(sum, tfvsInThisPart.get(startIndex).getTF_IDF_Vector());
			}

			sum = normalizeVector(sum);
			ArrayList<Float> a=new ArrayList<Float>();
			for(int i=0;i<sum.length;i++){
				a.add(sum[i]);
			}
			center = new TermFrequencyVector(sum);
			center.setTF_IDF_Vector(a);
			center.setNonDuplicateWordList(tfvsInThisPart.get(0)
					.getNonDuplicateWordList());
		} else {
			System.out.println("TFVList empty.");
		}
	}

	public void findTheCenterOfThisPartUsing_TFIDF() {
		if (!tfvsInThisPart.isEmpty()) {
			float[] sum = new float[tfvsInThisPart.get(0).getDimension()];
			for (int startIndex = 0; startIndex < tfvsInThisPart.size(); startIndex++) {
				sum = vectorAddition(sum, tfvsInThisPart.get(startIndex)
						.getTF_IDF_Vector());
			}

			sum = normalizeVector(sum);
			center = new TermFrequencyVector(sum);
			center.setNonDuplicateWordList(tfvsInThisPart.get(0)
					.getNonDuplicateWordList());
		} else {
			System.out.println("TFVList empty.");
		}
	}

	public void findTheCenterOfThisPartUsing_TFDF() {
		if (!tfvsInThisPart.isEmpty()) {
			float[] sum = new float[tfvsInThisPart.get(0).getDimension()];
			for (int startIndex = 0; startIndex < tfvsInThisPart.size(); startIndex++) {
				sum = vectorAddition(sum, tfvsInThisPart.get(startIndex)
						.getTF_DF_Vector());
			}

			sum = normalizeVector(sum);
			center = new TermFrequencyVector(sum);
			center.setNonDuplicateWordList(tfvsInThisPart.get(0)
					.getNonDuplicateWordList());
		} else {
			System.out.println("TFVList empty.");
		}
	}

	public float findTotalDistance() {
		float dist = 0;
		findTheCenterOfThisPart();

		float minDist = 1;
		for (int startIndex = 0; startIndex < tfvsInThisPart.size(); startIndex++) {
			float currentDistance = center
					.calculateCosineSimilarityWith(tfvsInThisPart
							.get(startIndex));
			dist += currentDistance;

			if (currentDistance < minDist) {
				minDist = currentDistance;
			}
		}
		radius = minDist;
		intraDist = dist;
		return dist;
	}

	public float getRadius() {
		return radius;
	}

	public TermFrequencyVector getCenter() {
		return center;
	}

	public static float[] vectorAddition(float[] firstVector, float[] secondVector) {
		float[] result = new float[firstVector.length];

		if (firstVector.length != secondVector.length) {
			System.out.println("Size error.");
		} else {
			for (int startIndex = 0; startIndex < result.length; startIndex++) {
				result[startIndex] = firstVector[startIndex]
						+ secondVector[startIndex];
			}
		}
		return result;
	}

	private float findLength(float[] vector) {
		float length = 0;
		float squareResult = 0;

		for (int startIndex = 0; startIndex < vector.length; startIndex++) {
			squareResult += Math.pow(vector[startIndex], 2);
		}
		length = (float) Math.sqrt(squareResult);

		return length;
	}

	private float[] normalizeVector(float[] vector) {
		float lengthOfVector = findLength(vector);
		float[] result = new float[vector.length];

		for (int startIndex = 0; startIndex < result.length; startIndex++) {
			result[startIndex] = vector[startIndex] / lengthOfVector;
		}
		return result;
	}

	public float getMinDistance() {
		return intraDist;
	}

	/*----Function for manual testing-----------------------------------------------------*/
	public void printCenter() {
		TermFrequencyVector currentCenter = center;
		System.out.println("Center: ");
		currentCenter.printVector();
	}
}
