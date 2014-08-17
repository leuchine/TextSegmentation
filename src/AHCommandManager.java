import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AHCommandManager {
	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		File f = new File("wikiData");
		File[] fs = f.listFiles();
		double q = 0;
		// BigInteger t1 = new BigInteger("0");
		// BigInteger t2 = new BigInteger("0");
		for (int i = 0; i < fs.length; i++) {
			PrintWriter pw = new PrintWriter("queryWikiTextTiling/" + fs[i].getName());
			int numberOfParts = 30;
			int numberOfClusters = 10;
			long time = System.currentTimeMillis();
			VectorConvertor vc = new VectorConvertor(fs[i].getAbsolutePath(),
					numberOfParts);
			ArrayList<TermFrequencyVector> tfvList = new ArrayList<TermFrequencyVector>();

			try {
				tfvList = vc.constructTermFrequencyVector();
			} catch (IOException e) {
				System.out
						.println("Unable to construct term frequency vector.");
			}

			OptimalHistogram optimalHistogram = new OptimalHistogram(
					numberOfClusters, tfvList);
//			optimalHistogram.textTiling(pw);
			// long take1 = System.nanoTime();
			// long take3 = System.nanoTime();
			optimalHistogram.findOptimalHistogram();
			// long take4 = (System.nanoTime() - take3);
			optimalHistogram.constructCluster(10, pw);
			// optimalHistogram.assign(10);

			// double quality = optimalHistogram.printMemoTable();
			// optimalHistogram.printTFIPF();
			// long take2 = System.nanoTime() - take1;
			// System.out.println("overall time" + take2);
			// System.out.println("dp time:" + (take4));
			// pw.println("" + take2 + " " + take4 + " " + quality);
			// pw.flush();
			// q = q + quality;
			// t1 = t1.add(new BigInteger(optimalHistogram.getDimension() +
			// ""));
			// t2 = t2.add(new BigInteger(take4 + ""));
			// optimalHistogram.printMemoTable();
			// HierarchyTable ht = new HierarchyTable();
			// ht.findHierarchy(vc.getNonDuplicateWordList(), tfvList,
			// numberOfClusters);
			//
			// ArrayList<ArrayList<PositionPair>> parentList =
			// ht.getParentList();
			// ArrayList<ArrayList<String[]>> KeywordTermList = ht
			// .getKeywordTermList();
			// int numberOfWordsPerPart = vc.getNumberOfWordsPerPart();
			// System.out.println("*" + numberOfWordsPerPart);
			// ArrayList<Integer> record = vc.getRecord();
			// int preStartSen = 0;
			// writer.println(record.get(record.size() - 1));
			//
			// for (int i = 1; i <= parentList.size(); i++) {
			// System.out.println("Number of cluster: " + i);
			// for (int j = 0; j < i; j++) {
			//
			// if (i == parentList.size()) {
			// System.out.println("C" + j + ": " + "["
			// + parentList.get(i - 1).get(j).getEndPoint() + ", "
			// + parentList.get(i - 1).get(j).getStartPoint()
			// + "]");
			// int start = parentList.get(i - 1).get(j).getEndPoint();
			// int end = parentList.get(i - 1).get(j).getStartPoint();
			//
			// int startIndex = start * numberOfWordsPerPart + 1;
			// int endIndex = (end + 1) * numberOfWordsPerPart;
			// if (j == 0) {
			// endIndex = record.size() - 1;
			// }
			//
			// int startSen = record.get(startIndex);
			// int endSen = record.get(endIndex);
			// if (preStartSen != 0 && preStartSen == record.get(endIndex)) {
			// endSen--;
			// }
			// preStartSen = startSen;
			// writer.println(startSen + ", " + endSen);
			// }
			// }
			// }
			// writer.close();
		}

	}
}
