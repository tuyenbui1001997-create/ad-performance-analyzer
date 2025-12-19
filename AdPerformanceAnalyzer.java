
import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import java.util.stream.*;

public class AdPerformanceAnalyzer {

	static class CampaignMetrics {
		String campaignId;
		long totalImpressions;
		long totalClicks;
		double totalSpend;
		long totalConversions;

		CampaignMetrics(String campaignId) {
			this.campaignId = campaignId;
		}

		void addRecord(long impressions, long clicks, double spend, long conversions) {
			this.totalImpressions += impressions;
			this.totalClicks += clicks;
			this.totalSpend += spend;
			this.totalConversions += conversions;
		}

		double getCTR() {
			return totalImpressions > 0 ? (double) totalClicks / totalImpressions : 0.0;
		}

		Double getCPA() {
			return totalConversions > 0 ? totalSpend / totalConversions : null;
		}

		String toCSV() {
			Double cpa = getCPA();
			String cpaStr = cpa != null ? String.format("%.2f", cpa) : "";
			return String.format("%s,%d,%d,%.2f,%d,%.6f,%s", campaignId, totalImpressions, totalClicks, totalSpend,
					totalConversions, getCTR(), cpaStr);
		}
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: java AdPerformanceAnalyzer <input_csv_file>");
			System.exit(1);
		}

		String inputFile = args[0];

		try {
			System.out.println("Processing file: " + inputFile);
			process(inputFile);
			System.out.println("Processing complete!");

		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static void process(String inputFile) throws Exception {
		String dbFile = "campaign_data.db";
		Files.deleteIfExists(Paths.get(dbFile));

		String url = "jdbc:sqlite:" + dbFile;

		try (Connection conn = DriverManager.getConnection(url)) {
			// Create table
			try (Statement stmt = conn.createStatement()) {
				stmt.execute("CREATE TABLE campaigns (" + "campaign_id TEXT, " + "impressions INTEGER, "
						+ "clicks INTEGER, " + "spend REAL, " + "conversions INTEGER)");
				stmt.execute("CREATE INDEX idx_campaign ON campaigns(campaign_id)");
			}

			// Load data in batches
			String insertSQL = "INSERT INTO campaigns VALUES (?, ?, ?, ?, ?)";
			try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFile));
					PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

				conn.setAutoCommit(false);
				String line = reader.readLine(); // Skip header
				long lineCount = 0;

				while ((line = reader.readLine()) != null) {
					if (line.trim().isEmpty())
						continue;

					try {
						String[] parts = line.split(",");
						if (parts.length < 6)
							continue;

						pstmt.setString(1, parts[0].trim());
						pstmt.setLong(2, Long.parseLong(parts[2].trim()));
						pstmt.setLong(3, Long.parseLong(parts[3].trim()));
						pstmt.setDouble(4, Double.parseDouble(parts[4].trim()));
						pstmt.setLong(5, Long.parseLong(parts[5].trim()));
						pstmt.addBatch();

						lineCount++;
						if (lineCount % 10000 == 0) {
							pstmt.executeBatch();
							conn.commit();
							System.out.println("Loaded " + lineCount + " lines...");
						}
					} catch (NumberFormatException e) {
						System.err.println("Skipping invalid line");
					}
				}

				pstmt.executeBatch();
				conn.commit();
				System.out.println("Total lines loaded: " + lineCount);
			}

			// Generate Top 10 CTR
			generateTopCTR(conn);

			// Generate Top 10 Lowest CPA
			generateLowestCPA(conn);
		}

		// Clean up
		Files.deleteIfExists(Paths.get(dbFile));
	}

	private static void generateTopCTR(Connection conn) throws Exception {
		String query = "SELECT campaign_id, " + "SUM(impressions) as total_impressions, "
				+ "SUM(clicks) as total_clicks, " + "SUM(spend) as total_spend, "
				+ "SUM(conversions) as total_conversions, " + "CAST(SUM(clicks) AS REAL) / SUM(impressions) as CTR, "
				+ "CASE WHEN SUM(conversions) > 0 THEN SUM(spend) / SUM(conversions) ELSE NULL END as CPA "
				+ "FROM campaigns " + "GROUP BY campaign_id " + "ORDER BY CTR DESC " + "LIMIT 10";

		writeQueryToCSV(conn, query, "top_10_highest_ctr.csv");
	}

	private static void generateLowestCPA(Connection conn) throws Exception {
		String query = "SELECT campaign_id, " + "SUM(impressions) as total_impressions, "
				+ "SUM(clicks) as total_clicks, " + "SUM(spend) as total_spend, "
				+ "SUM(conversions) as total_conversions, " + "CAST(SUM(clicks) AS REAL) / SUM(impressions) as CTR, "
				+ "SUM(spend) / SUM(conversions) as CPA " + "FROM campaigns " + "GROUP BY campaign_id "
				+ "HAVING SUM(conversions) > 0 " + "ORDER BY CPA ASC " + "LIMIT 10";

		writeQueryToCSV(conn, query, "top_10_lowest_cpa.csv");
	}

	private static void writeQueryToCSV(Connection conn, String query, String outputFile) throws Exception {
		try (Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile))) {

			writer.write("campaign_id,total_impressions,total_clicks,total_spend,total_conversions,CTR,CPA");
			writer.newLine();

			while (rs.next()) {
				String campaignId = rs.getString(1);
				long impressions = rs.getLong(2);
				long clicks = rs.getLong(3);
				double spend = rs.getDouble(4);
				long conversions = rs.getLong(5);
				double ctr = rs.getDouble(6);
				Double cpa = rs.getObject(7) != null ? rs.getDouble(7) : null;

				String cpaStr = cpa != null ? String.format("%.2f", cpa) : "";
				writer.write(String.format("%s,%d,%d,%.2f,%d,%.6f,%s", campaignId, impressions, clicks, spend,
						conversions, ctr, cpaStr));
				writer.newLine();
			}
		}
		System.out.println("Generated: " + outputFile);
	}
}