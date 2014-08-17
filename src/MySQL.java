import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class MySQL {

	public static void main(String[] args) throws FileNotFoundException {

		// 驱动程序名
		String driver = "com.mysql.jdbc.Driver";

		// URL指向要访问的数据库名scutcs
		String url = "jdbc:mysql://localhost:3306/test";

		// MySQL配置时的用户名
		String user = "root";

		// MySQL配置时的密码
		String password = "sea1992";
		PrintWriter pw = new PrintWriter("precision.txt");
		try {
			// 加载驱动程序
			Class.forName(driver);

			// 连续数据库
			Connection conn = (Connection) DriverManager.getConnection(url,
					user, password);

			if (!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");

			// statement用来执行SQL语句
			Statement statement = (Statement) conn.createStatement();
			File f = new File("ebookData/book");
			File[] fs = f.listFiles();
			for (int i = 0; i < fs.length; i++) {
				HashSet<Integer> a = new HashSet<Integer>();
				HashSet<Integer> b = new HashSet<Integer>();
				String bid = fs[i].getName().substring(0,
						fs[i].getName().length() - 4);
				String sql = "select aid from annotation where bid=" + bid;
				System.out.println("select aid from annotation where bid="
						+ bid);
				ResultSet rs = statement.executeQuery(sql);
				while (rs.next()) {

					Integer aid = rs.getInt("aid");
					a.add(aid);
				}
				FileReader fr = new FileReader("queryBook2/" + fs[i].getName());
				System.out.println(fs[i].getName());
				BufferedReader br = new BufferedReader(fr);
				String line = null;
				int c = 0;
				while ((line = br.readLine()) != null) {
					c++;
					if (c == 7)
						break;
					String[] words = line.split(" ");
					int j;
					for (j = 0; j + 3 < words.length; j = j + 1) {
						String sql2 = "select aid from annotation where msg like \"%"
								+ words[j]
								+ "%"
								+ words[j + 1]
								+ "%"
								+ words[j + 2] + "%" + words[j + 3] + "%\"";

						System.out.println(sql2);
						rs = statement.executeQuery(sql2);
						while (rs.next()) {
							int aid = rs.getInt("aid");
							if (!b.contains(aid))
								b.add(aid);
						}
					}
				}

				int count = 0;
				System.out.println("b size:" + b.size());
				System.out.println("a size:" + a.size());
				Iterator<Integer> t = b.iterator();
				while (t.hasNext()) {
					int n = t.next();
					if (a.contains(n))
						count++;
				}
				System.out.println("count:" + count);
				double precision;
				if (b.size() == 0)
					precision = 0;
				else
					precision = (double) count / b.size();
				double recall;
				if (a.size() == 0)
					recall = 0;
				else
					recall = (double) count / a.size();
				System.out.println("Precision:" + precision);
				System.out.println("Recall" + recall);
				pw.println(precision + "\t" + recall);
				pw.flush();
			}

		} catch (ClassNotFoundException e) {

			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();

		} catch (SQLException e) {

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		}
	}
}
