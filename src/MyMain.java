import java.util.Arrays;

public class MyMain {

	public static void main(String[] args) {
		// TermFrequencyVector v1 = new TermFrequencyVector(new float[] { 1, 1,
		// 1,
		// 1 });
		// TermFrequencyVector v2 = new TermFrequencyVector(new float[] { 3, 2,
		// 3,
		// 4 });
		// TermFrequencyVector v3 = new TermFrequencyVector(new float[] { 3, 3,
		// 3,
		// 4 });
		// TermFrequencyVector v4 = new TermFrequencyVector(new float[] { 3, 3,
		// 3,
		// 5 });
		// ArrayList<TermFrequencyVector> list = new
		// ArrayList<TermFrequencyVector>();
		// ;
		// list.add(v1);
		// list.add(v2);
		// list.add(v3);
		// list.add(v4);
		// OptimalHistogram his = new OptimalHistogram(4, list);
		// his.findOptimalHistogram();
		// his.printMemoTable();
		// his.printTrace();
		// his.constructCluster2();
		Pair a1 = new Pair(1, 0);
		Pair a2 = new Pair(3, 1);
		Pair a3 = new Pair(2, 2);
		Pair[] p = new Pair[3];
		p[0] = a1;
		p[1] = a2;
		p[2] = a3;
		Arrays.sort(p);
		System.out.println(Arrays.toString(p));
	}

}
