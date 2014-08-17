import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

class OptimalHistogram {
	private int maxNumberOfClusters;
	private float dummy = -1;
	private ArrayList<TermFrequencyVector> tfvList;
	private ArrayList<TermFrequencyVector> centerList;
	private ArrayList<separatedClusters> clusterList;
	private float[][] memo;
	private int[][] trace;
	private ArrayList<PositionPair> positionList;
	private float[][] tcs;
	private TermFrequencyVector sumOfTFVector;
	private ArrayList<ArrayList<Segment>> segments;

	public OptimalHistogram() {

	}

	public void assign() {

	}

	public void computeQuality() {
		ArrayList<ArrayList<float[]>> a = new ArrayList<ArrayList<float[]>>();
		for (int i = 0; i < tcs.length; i++) {
			a.add(new ArrayList<float[]>());
			tcs[i][i] = TermFrequencyVector.findLength(this.tfvList.get(i)
					.getTF_IDF_Vector());
			a.get(a.size() - 1).add(this.tfvList.get(i).getTF_IDF_Vector());
		}
		for (int i = 0; i < tcs.length; i++) {
			for (int j = i + 1; j < tcs.length; j++) {
				float[] sum = separatedClusters.vectorAddition(
						a.get(i).get(a.get(i).size() - 1), this.tfvList.get(j)
								.getTF_IDF_Vector());
				tcs[i][j] = TermFrequencyVector.findLength(sum);
				a.get(i).add(sum);
			}
		}
	}

	public int getDimension() {
		return this.tfvList.get(0).getDimension();
	}

	public OptimalHistogram(int numberOfClusters,
			ArrayList<TermFrequencyVector> tfvList) {
		maxNumberOfClusters = numberOfClusters;
		this.tfvList = tfvList;
		this.centerList = new ArrayList<TermFrequencyVector>();
		this.clusterList = new ArrayList<separatedClusters>();
		this.positionList = new ArrayList<PositionPair>();
		memo = new float[tfvList.size()][maxNumberOfClusters + 1];
		trace = new int[tfvList.size()][maxNumberOfClusters + 1];
		this.tcs = new float[tfvList.size()][tfvList.size()];
		long time = System.nanoTime();
		this.computeQuality();

		System.out.println("M time:" + (System.nanoTime() - time));
		System.out.println("M size" + (this.tfvList.get(0).getDimension()));
		float[] sumvector = new float[tfvList.get(0).getDimension()];
		for (int i = 0; i < sumvector.length; i++)
			sumvector[i] = 0;
		for (int i = 0; i < tfvList.size(); i++) {
			// System.out.println(Arrays.toString(tfvList.get(i)
			// .getTF_DF_Vector()));
			sumvector = separatedClusters.vectorAddition(sumvector, tfvList
					.get(i).getTF_DF_Vector());
		}
		// System.out.println(Arrays.toString(sumvector));
		sumOfTFVector = new TermFrequencyVector(sumvector);
		// sumOfTFVector.print();
	}

	public void textTiling(PrintWriter pw) {
		int num = 3;
		separatedClusters[] sep = new separatedClusters[10];
		for (int i = 0; i < 10; i++) {
			sep[i] = this.getCluster(i * 3, (i + 1) * 3 - 1);
		}
		for (int i = 0; i < 10; i++) {
			pw.println(toS(this.getKeyWords(sep[i], 10)));
			pw.flush();
		}
	}

	public void summarization(PrintWriter pw) {
		int num = 3;
		separatedClusters sep = null;

		sep = this.getCluster(0, this.tfvList.size() - 1);

		pw.println(toS(this.getKeyWords(sep, 50)));
		pw.flush();
	}

	public void findOptimalHistogram() {
		methodUsingDynamicProgramming();
		// printMemoTable();//N * (K+1)
	}

	public ArrayList<TermFrequencyVector> getCenterList() {
		return centerList;
	}

	/*---Method 1: Using basic dynamic programming-------------------------------------------------------------*/
	private void methodUsingDynamicProgramming() {
		process2(tfvList.size() - 1, maxNumberOfClusters);
		// printTrace();
	}

	private float process2(int endIndex, int maxPartion) {
		for (int i = 0; i <= endIndex; i++) {
			memo[i][1] = this.tcs[0][i];
		}
		for (int j = 2; j <= maxPartion; j++) {
			for (int i = 0; i <= endIndex; i++) {
				memo[i][j] = Integer.MIN_VALUE;
				int position = 0;
				for (int k = 0; k <= i - 1; k++) {
					float value = memo[k][j - 1] + this.tcs[k + 1][i];
					if (value > memo[i][j]) {
						memo[i][j] = value;
						position = k + 1;
					}
				}
				this.trace[i][j] = position;
			}
		}
		return 0;
	}

	public separatedClusters getCluster(int start, int end) {
		ArrayList<TermFrequencyVector> arrays = new ArrayList<TermFrequencyVector>(
				end - start + 1);
		for (int i = start; i <= end; i++) {
			arrays.add(tfvList.get(i));
		}
		separatedClusters s = new separatedClusters(arrays);
		return s;
	}

	public void printWords() {
		System.out.println(tfvList.get(0).getNonDuplicateWordList());
	}

	public String toS(String[] args) {
		String r = "";
		for (int i = 0; i < args.length; i++) {
			if (i != args.length - 1)
				r = r + args[i] + " ";
			else
				r = r + args[i];
		}
		return r;
	}

	public void constructCluster(int keywordnum, PrintWriter pw) {
		this.segments = new ArrayList<ArrayList<Segment>>(
				this.maxNumberOfClusters + 2);
		System.out.println(Arrays.toString(this.getKeyWords(
				this.getCluster(0, this.trace.length - 1), keywordnum)));
		ArrayList<Segment> a = new ArrayList<Segment>();
		a.add(new Segment(0, this.trace.length - 1, 0));
		segments.add(new ArrayList<Segment>());
		segments.add(a);
		for (int i = 2; i <= this.maxNumberOfClusters; i++) {
			int endIndex = this.trace.length - 1;
			ArrayList<Segment> b = new ArrayList<Segment>();
			for (int j = i; j > 1; j--) {
				System.out.print("[" + trace[endIndex][j] + "," + endIndex
						+ "] ");
				int k;
				for (k = segments.get(i - 1).size() - 1; k >= 0; k--) {
					if (segments.get(i - 1).get(k).right >= endIndex) {
						break;
					}
				}
				b.add(new Segment(trace[endIndex][j], endIndex, k));
				if (i == this.maxNumberOfClusters) {
					pw.println(toS(this.getKeyWords(
							this.getCluster(trace[endIndex][j], endIndex),
							keywordnum)));
					pw.flush();
				}
				endIndex = trace[endIndex][j] - 1;
			}
			int k;
			for (k = segments.get(i - 1).size() - 1; k >= 0; k--) {
				if (segments.get(i - 1).get(k).right >= endIndex) {
					break;
				}
			}
			b.add(new Segment(0, endIndex, k));
			System.out.print("[0," + endIndex + "] ");
			if (i == this.maxNumberOfClusters) {
				pw.println(toS(this.getKeyWords(this.getCluster(0, endIndex),
						keywordnum)));
				pw.flush();
			}
			segments.add(b);
			System.out.println();
		}
		// System.out.println("level:");
		// System.out.println(segments);
	}

	public void constructCluster2() {
		int endIndex = this.trace.length - 1;
		for (int j = this.maxNumberOfClusters; j > 1; j--) {
			System.out.print("[" + trace[endIndex][j] + "," + endIndex + "] ");
			this.clusterList.add(this.getCluster(trace[endIndex][j], endIndex));
			endIndex = trace[endIndex][j] - 1;
		}
		System.out.print("[0," + endIndex + "] ");
		this.clusterList.add(this.getCluster(0, endIndex));
		System.out.println();
		for (int i = 0; i < clusterList.size(); i++) {
			System.out.println(Arrays.toString(this.getKeyWords(
					clusterList.get(i), 35)));
		}
	}

	public String[] getKeyWords(separatedClusters cluster, int number) {
		cluster.findTheCenterOfThisPartUsing_TFDF();
		TermFrequencyVector center = cluster.getCenter();
		float[] vector = center.getExpandedVector();
		ArrayList<String> wordLists = center.getNonDuplicateWordList();
		Pair[] array = new Pair[wordLists.size()];
		for (int i = 0; i < wordLists.size(); i++) {
//			array[i] = new Pair(0.5 * vector[i] + 0.5
//					* sumOfTFVector.getExpandedVector()[i], i);
			// array[i] = new Pair(vector[i], i);
			array[i] = new Pair(0.2 * vector[i] + 0.8*sumOfTFVector.getExpandedVector()[i], i);
		}
		Arrays.sort(array);
		String[] keys = new String[number];
		for (int i = 0; i < number && (wordLists.size() - i - 1) >= 0; i++) {
			keys[i] = wordLists.get(array[wordLists.size() - i - 1].position);
		}
		return keys;
	}

	private float process(int endIndex, int maxPartition) {
		if (maxPartition <= 1) {
			memo[endIndex][maxPartition] = TCS(0, endIndex);
			return TCS(0, endIndex);
		}

		if (endIndex + 1 <= maxPartition) { /* N <= K */
			memo[endIndex][maxPartition] = 0;
			return 0;
		}

		if (memo[endIndex][maxPartition] != -1) {
			return memo[endIndex][maxPartition];
		}

		int follower = 0;
		FloatBooleanPair fbp = new FloatBooleanPair(dummy, false);
		for (int startIndex = 0; startIndex <= endIndex; startIndex++) {
			fbp.setIndicator(false);
			fbp = findMax(
					fbp.getNumber(),
					process(endIndex - startIndex, maxPartition - 1)
							+ TCS(endIndex - startIndex + 1, endIndex));
			if (fbp.getIndicator()) {
				follower = endIndex - startIndex + 1;
			}
		}
		trace[endIndex][maxPartition] = follower;
		memo[endIndex][maxPartition] = fbp.getNumber();

		return fbp.getNumber();
	}

	/*
	 * Calculate the minimum distance and find the center among the part which
	 * consists of tfvs from indexOfStartPoint to indexOfEndPoint.
	 */
	private float TCS(int indexOfStartPoint, int indexOfEndPoint) {
		float intraDist = 0;

		if (indexOfStartPoint == indexOfEndPoint) {
			intraDist = 1;
		} else if (indexOfStartPoint > indexOfEndPoint) {
			intraDist = 0;
		} else {
			ArrayList<TermFrequencyVector> tempTfvList = new ArrayList<TermFrequencyVector>();
			for (int startIndex = indexOfStartPoint; startIndex <= indexOfEndPoint; startIndex++) {
				tempTfvList.add(tfvList.get(startIndex));
			}
			separatedClusters part = new separatedClusters(tempTfvList);
			intraDist = part.findTotalDistance();
			// if ((indexOfStartPoint == 0 && indexOfEndPoint == 6)
			// || (indexOfStartPoint == 0 && indexOfEndPoint == 1)
			// || (indexOfStartPoint == 2 && indexOfEndPoint == 6)
			// || (indexOfStartPoint == 0 && indexOfEndPoint == 1)||
			// (indexOfStartPoint==2&&indexOfEndPoint==4)||
			// (indexOfStartPoint==5&&indexOfEndPoint==6)) {
			// part.getCenter().printExpandedVector();
			// }
		}

		return intraDist;
	}

	private void initializeMemoTable() {
		for (int startIndex = 0; startIndex < memo.length; startIndex++) {
			Arrays.fill(memo[startIndex], -1);
		}
	}

	private FloatBooleanPair findMax(float firstNumber, float secondNumber) {
		if (firstNumber < secondNumber) {
			return new FloatBooleanPair(secondNumber, true);
		} else {
			return new FloatBooleanPair(firstNumber, false);
		}
	}

	// public void constructCluster(int numberOfCluster) {
	// constructCluster(trace, numberOfCluster);
	// }

	private void constructCluster(int[][] traceTable, int numberOfCluster) {
		int endIndex = tfvList.size() - 1;
		int cluster = numberOfCluster;
		int clusterNumberForPrinting = 0;
		for (int i = endIndex; i >= 0; i--) {
			if (i == cluster - 1) {
				int k = i;
				while (k >= 0) {
					System.out.print("C" + clusterNumberForPrinting + "\t");
					ArrayList<TermFrequencyVector> current = new ArrayList<TermFrequencyVector>();
					current.add(tfvList.get(k));
					separatedClusters sc = new separatedClusters(current);
					sc.findTotalDistance();
					positionList.add(new PositionPair(k, k));
					centerList.add(sc.getCenter());
					clusterList.add(sc);
					k--;
					clusterNumberForPrinting++;
				}
				System.out.println();
				return;
			}

			if (i == trace[i][cluster]) {
				ArrayList<TermFrequencyVector> current = new ArrayList<TermFrequencyVector>();
				for (int j = trace[i][cluster]; j <= i; j++) {
					current.add(tfvList.get(j));
				}
				separatedClusters sc = new separatedClusters(current);
				sc.findTotalDistance();
				positionList.add(new PositionPair(i, trace[i][cluster]));
				centerList.add(sc.getCenter());
				clusterList.add(sc);
				cluster--;
			} else {
				ArrayList<TermFrequencyVector> current = new ArrayList<TermFrequencyVector>();
				for (int j = trace[i][cluster]; j <= i; j++) {
					current.add(tfvList.get(j));
				}
				separatedClusters sc = new separatedClusters(current);
				sc.findTotalDistance();
				positionList.add(new PositionPair(i, trace[i][cluster]));
				centerList.add(sc.getCenter());
				clusterList.add(sc);
				i = trace[i][cluster];
				cluster--;
			}
			clusterNumberForPrinting++;
		}
	}

	public ArrayList<separatedClusters> getClusterList() {
		return this.clusterList;
	}

	public ArrayList<PositionPair> getPositionList() {
		return this.positionList;
	}

	/*---Method 1: Using basic dynamic programming-------------------------------------------------------------*/

	/*----Function for manual testing-----------------------------------------------------*/
	public double printMemoTable() {
		System.out.println("memo:");
		// DecimalFormat df = new DecimalFormat("0.000000");
		// for (int i = 0; i < memo.length; i++) {
		// for (int j = 1; j < memo[0].length; j++) {
		// System.out.print(df.format(memo[i][j]) + "  ");
		// }
		// System.out.println();
		System.out.println(memo[memo.length - 1][memo[0].length - 1]);
		return memo[memo.length - 1][memo[0].length - 1];
		// }
	}

	public void printTrace() {
		System.out.println("trace:");
		for (int i = 0; i < trace.length; i++) {
			for (int j = 1; j < trace[i].length; j++) {
				System.out.print(trace[i][j] + " ");
			}
			System.out.println();
		}
	}

	public void printTFIPF() {
		for (int i = 0; i < this.tfvList.size(); i++) {
			this.tfvList.get(i).printTFIDFVector();
		}
	}
}

class Pair implements Comparable {
	double value;
	int position;

	public Pair(double d, int p) {
		this.value = d;
		this.position = p;
	}

	public int compareTo(Object o) {
		Pair a = (Pair) o;
		if (this.value < a.value)
			return -1;
		else if (this.value > a.value) {
			return 1;
		} else {
			return 0;
		}
	}

	public String toString() {
		return "(" + value + " " + position + ")";
	}
}

class Segment {
	public int left;
	public int right;
	public int parent;

	public Segment(int l, int r, int p) {
		this.left = l;
		this.right = r;
		this.parent = p;
	}

	public String toString() {
		return "[" + left + "," + right + "," + parent + "]";
	}
}