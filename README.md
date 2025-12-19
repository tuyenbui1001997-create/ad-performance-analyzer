Ad Performance Analyzer
A Java console application for processing large advertising performance datasets (1GB+) using SQLite database for efficient aggregation without memory constraints.

📋 Table of Contents

Requirements
Installation
Quick Start
Usage
Input Format
Output Files
Troubleshooting
Advanced Configuration


🔧 Requirements
System Requirements

Java: JDK 8 or higher
Memory: Minimum 256MB RAM (recommended 512MB)
Disk Space: At least 2x the size of your input CSV file
Operating System: Windows, Linux, or macOS

Software Dependencies

SQLite JDBC Driver (included in setup instructions)


📥 Installation
Step 1: Download the Application
Create a project directory and save the Java files:
bashmkdir ad-performance-analyzer
cd ad-performance-analyzer
Save these files in the directory:

AdPerformanceAnalyzer.java (main application)
CampaignDBInspector.java (optional - for database inspection)

Step 2: Download SQLite JDBC Driver
Option A: Using wget (Linux/Mac)
bashwget https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.51.1.0/sqlite-jdbc-3.51.1.0.jar
Option B: Using curl (Linux/Mac)
bashcurl -O https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.51.1.0/sqlite-jdbc-3.51.1.0.jar
Option C: Manual Download (Windows)

Open browser and go to: https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.51.1.0/
Download sqlite-jdbc-3.51.1.0.jar
Place it in the same directory as your Java files

Step 3: Verify Setup
Your directory should contain:
ad-performance-analyzer/
├── AdPerformanceAnalyzer.java
├── sqlite-jdbc-3.51.1.0.jar
└── your_data.csv (your input file)

🚀 Quick Start
1. Compile the Application
Linux/Mac:
bashjavac -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer.java
Windows:
cmdjavac -cp ".;sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer.java
2. Run the Application
Linux/Mac:
bashjava -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer your_data.csv
Windows:
cmdjava -cp ".;sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer your_data.csv
3. Check Output Files
The application generates two CSV files:

top_10_highest_ctr.csv
top_10_lowest_cpa.csv


📖 Usage
Basic Command
bashjava -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer <input_file>
With Increased Memory (for very large files)
bashjava -Xmx2g -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer input.csv
Memory Options
OptionHeap SizeRecommended ForDefault~256MBFiles < 2GB-Xmx1g1GBFiles 2-5GB-Xmx2g2GBFiles 5-10GB-Xmx4g4GBFiles > 10GB

📄 Input Format
CSV Structure
Your input CSV file must have this structure:
csvcampaign_id,date,impressions,clicks,spend,conversions
CAMP_001,2024-01-01,1000,50,100.00,5
CAMP_002,2024-01-01,2000,100,200.00,10
CAMP_001,2024-01-02,1500,75,150.00,8
Column Descriptions
ColumnTypeDescriptionRequiredcampaign_idStringUnique campaign identifierYesdateStringDate of record (not used in calculations)YesimpressionsIntegerNumber of ad impressionsYesclicksIntegerNumber of clicksYesspendDecimalAmount spentYesconversionsIntegerNumber of conversionsYes
Requirements

✅ First row must be header
✅ Comma-separated values
✅ No extra spaces in numbers
✅ UTF-8 encoding recommended


📊 Output Files
1. top_10_highest_ctr.csv
Contains the top 10 campaigns with the highest Click-Through Rate (CTR).
Structure:
csvcampaign_id,total_impressions,total_clicks,total_spend,total_conversions,CTR,CPA
CAMP_005,50000,5000,2500.00,250,0.100000,10.00
CAMP_003,30000,2700,1800.00,180,0.090000,10.00
Metrics:

CTR = total_clicks ÷ total_impressions
CPA = total_spend ÷ total_conversions (empty if no conversions)

2. top_10_lowest_cpa.csv
Contains the top 10 campaigns with the lowest Cost Per Acquisition (CPA).
Structure:
csvcampaign_id,total_impressions,total_clicks,total_spend,total_conversions,CTR,CPA
CAMP_007,40000,2000,1000.00,500,0.050000,2.00
CAMP_012,35000,1750,1050.00,350,0.050000,3.00
Note: Campaigns with zero conversions are excluded from this report.

🔍 Processing Details
What Happens During Execution

Initialization

   Processing file: your_data.csv
   Method: sqlite

Database Creation

Creates campaign_data.db (temporary file)
Creates table and index


Data Loading

   Loaded 10000 lines...
   Loaded 20000 lines...
   ...
   Total lines loaded: 5234891

Report Generation

   Generated: top_10_highest_ctr.csv
   Generated: top_10_lowest_cpa.csv

Cleanup

Database file is automatically deleted
Only CSV output files remain



Performance Expectations
Input SizeProcessing TimeMemory UsageDisk Usage100MB~30 seconds100MB80MB1GB~3-4 minutes100MB800MB5GB~15-20 minutes100MB4GB10GB~30-40 minutes100MB8GB

🛠️ Troubleshooting
Error: "Class not found: org.sqlite.JDBC"
Problem: SQLite JDBC driver not in classpath
Solution:
bash# Make sure you include the jar in classpath
java -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer input.csv sqlite
Error: "OutOfMemoryError"
Problem: Not enough heap memory
Solution:
bash# Increase heap size
java -Xmx2g -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer input.csv sqlite
Error: "No space left on device"
Problem: Not enough disk space for database file
Solution:

Free up disk space (need ~80% of CSV file size)
Or process on a different drive

Error: "File not found"
Problem: Input file path is incorrect
Solution:
bash# Use absolute path
java -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer /full/path/to/input.csv sqlite

# Or ensure you're in the correct directory
cd /path/to/data
java -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer input.csv sqlite
Slow Performance
Solutions:

Increase batch size (edit Java file):

javaif (lineCount % 50000 == 0) {  // Changed from 10000
    pstmt.executeBatch();

Use SSD instead of HDD for faster I/O
Close other applications to free system resources

Invalid Data Issues
Problem: "Skipping invalid line" messages
Common Causes:

Missing columns in CSV
Non-numeric values in numeric fields
Extra commas in campaign_id or other fields

Solution:

Check CSV format matches requirements
Remove or fix problematic lines
Ensure proper CSV escaping for special characters


🔬 Advanced Configuration
Inspecting the Database
While processing, you can inspect the database:
Compile inspector:
bashjavac -cp ".:sqlite-jdbc-3.51.1.0.jar" CampaignDBInspector.java
Run inspector (in another terminal):
bashjava -cp ".:sqlite-jdbc-3.51.1.0.jar" CampaignDBInspector campaign_data.db
Keeping the Database
To keep campaign_data.db for further analysis:
Edit AdPerformanceAnalyzer.java:
java// Comment out this line near the end of processSQLite():
// Files.deleteIfExists(Paths.get(dbFile));

System.out.println("Database saved: " + dbFile);
Then you can query it directly:
bashsqlite3 campaign_data.db
Custom SQL Queries
Once you have the database, run custom queries:
sql-- Top 20 campaigns by spend
SELECT campaign_id, SUM(spend) as total_spend
FROM campaigns
GROUP BY campaign_id
ORDER BY total_spend DESC
LIMIT 20;

-- Campaigns with CTR > 5%
SELECT campaign_id, 
       CAST(SUM(clicks) AS REAL) / SUM(impressions) as CTR
FROM campaigns
GROUP BY campaign_id
HAVING CTR > 0.05;
Batch Processing Multiple Files
Create a script (process_all.sh):
bash#!/bin/bash

for file in data/*.csv; do
    echo "Processing $file"
    java -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer "$file" sqlite
    
    # Rename output files
    mv top_10_highest_ctr.csv "ctr_$(basename $file)"
    mv top_10_lowest_cpa.csv "cpa_$(basename $file)"
done
Run:
bashchmod +x process_all.sh
./process_all.sh

📞 Support
Common Commands Reference
Linux/Mac:
bash# Compile
javac -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer.java

# Run
java -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer input.csv sqlite

# Run with more memory
java -Xmx2g -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer input.csv sqlite
Windows:
cmd# Compile
javac -cp ".;sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer.java

# Run
java -cp ".;sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer input.csv sqlite

# Run with more memory
java -Xmx2g -cp ".;sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer input.csv sqlite
Example Session
bash$ ls
AdPerformanceAnalyzer.java  advertising_data.csv  sqlite-jdbc-3.51.1.0.jar

$ javac -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer.java

$ java -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer advertising_data.csv sqlite
Processing file: advertising_data.csv
Method: sqlite
Loaded 10000 lines...
Loaded 20000 lines...
...
Total lines loaded: 1234567
Generated: top_10_highest_ctr.csv
Generated: top_10_lowest_cpa.csv
Processing complete!

$ ls
AdPerformanceAnalyzer.class  advertising_data.csv        top_10_highest_ctr.csv
AdPerformanceAnalyzer.java   sqlite-jdbc-3.51.1.0.jar   top_10_lowest_cpa.csv

$ head -5 top_10_highest_ctr.csv
campaign_id,total_impressions,total_clicks,total_spend,total_conversions,CTR,CPA
CAMP_0042,125000,12500,5000.00,625,0.100000,8.00
CAMP_0156,98000,9310,4200.00,520,0.095000,8.08
...

📝 License
This is a utility application for data processing. Use at your own discretion.
✨ Tips for Best Performance

Use SSD for faster disk I/O
Process during off-peak hours for large files
Monitor disk space - ensure 2x input file size available
Close unnecessary applications to free memory
Use absolute paths to avoid file not found errors
Validate CSV format before processing large files