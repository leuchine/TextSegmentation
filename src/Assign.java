public class Assign {

	public static int jaccardSim(float[] a, float[] b) {
		int count = 0;
		for (int i = 0; i < a.length; i++) {
			if (a[i] == b[i])
				count++;
		}
		return count / a.length;
	}

	public static long assign(int m, int f) {
		float[][] levels = new float[6][m];
		float[][] tweets = new float[f][m];
		long a = System.currentTimeMillis();
		for (int i = 0; i < f; i++) {
			for (int j = 0; j < levels.length; j++) {
				jaccardSim(tweets[i], levels[j]);
			}
		}
		return (System.currentTimeMillis() - a);
	}

	public static void main(String[] args) {
		long sum=0;
		for (int i = 0; i < 1000; i++) {
			sum+=assign(140, 10000);
		}
		System.out.println(sum);
	}
}
