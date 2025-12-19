# Ad Performance Analyzer - SQLite Solution

A Java console application for processing large advertising performance datasets (1GB+) using SQLite database for efficient aggregation without memory constraints.

## 📋 Table of Contents

- [Requirements](#requirements)
- [Installation](#installation)
- [Quick Start](#quick-start)
- [Usage](#usage)
- [Input Format](#input-format)
- [Output Files](#output-files)
- [Troubleshooting](#troubleshooting)
- [Advanced Configuration](#advanced-configuration)

---

## 🔧 Requirements

### System Requirements

- **Java**: JDK 8 or higher
- **Memory**: Minimum 256MB RAM (recommended 512MB)
- **Disk Space**: At least 2x the size of your input CSV file
- **Operating System**: Windows, Linux, or macOS

### Software Dependencies

- SQLite JDBC Driver (included in setup instructions)

---

## 📥 Installation

### Step 1: Download the Application

Create a project directory and save the Java files:

```bash
mkdir ad-performance-analyzer
cd ad-performance-analyzer
```

Save these files in the directory:
- `AdPerformanceAnalyzer.java` (main application)
- `CampaignDBInspector.java` (optional - for database inspection)

### Step 2: Download SQLite JDBC Driver

**Option A: Using wget (Linux/Mac)**
```bash
wget https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.51.1.0/sqlite-jdbc-3.51.1.0.jar
```

**Option B: Using curl (Linux/Mac)**
```bash
curl -O https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.51.1.0/sqlite-jdbc-3.51.1.0.jar
```

**Option C: Manual Download (Windows)**
1. Open browser and go to: https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.51.1.0/
2. Download `sqlite-jdbc-3.51.1.0.jar`
3. Place it in the same directory as your Java files

### Step 3: Verify Setup

Your directory should contain:
```
ad-performance-analyzer/
├── AdPerformanceAnalyzer.java
├── CampaignDBInspector.java
├── sqlite-jdbc-3.51.1.0.jar
└── ad_data.csv (your input file)
```

---

## 🚀 Quick Start

### 1. Compile the Application

**Linux/Mac:**
```bash
javac -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer.java
```

**Windows:**
```cmd
javac -cp ".;sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer.java
```

### 2. Run the Application

**Linux/Mac:**
```bash
java -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer ad_data.csv.csv
```

**Windows:**
```cmd
java -cp ".;sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer ad_data.csv.csv
```

### 3. Check Output Files

The application generates two CSV files:
- `top_10_highest_ctr.csv`
- `top_10_lowest_cpa.csv`

---

## 📖 Usage

### Basic Command

```bash
java -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer <input_file>
```

### With Increased Memory (for very large files)

```bash
java -Xmx2g -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer input.csv
```

### Memory Options

| Option | Heap Size | Recommended For |
|--------|-----------|-----------------|
| Default | ~256MB | Files < 2GB |
| `-Xmx1g` | 1GB | Files 2-5GB |
| `-Xmx2g` | 2GB | Files 5-10GB |
| `-Xmx4g` | 4GB | Files > 10GB |

---

## 📄 Input Format

### CSV Structure

Your input CSV file must have this structure:

```csv
campaign_id,date,impressions,clicks,spend,conversions
CAMP_001,2024-01-01,1000,50,100.00,5
CAMP_002,2024-01-01,2000,100,200.00,10
CAMP_001,2024-01-02,1500,75,150.00,8
```

### Column Descriptions

| Column | Type | Description | Required |
|--------|------|-------------|----------|
| `campaign_id` | String | Unique campaign identifier | Yes |
| `date` | String | Date of record (not used in calculations) | Yes |
| `impressions` | Integer | Number of ad impressions | Yes |
| `clicks` | Integer | Number of clicks | Yes |
| `spend` | Decimal | Amount spent | Yes |
| `conversions` | Integer | Number of conversions | Yes |

### Requirements

- ✅ First row must be header
- ✅ Comma-separated values
- ✅ No extra spaces in numbers
- ✅ UTF-8 encoding recommended

---

## 📊 Output Files

### 1. top_10_highest_ctr.csv

Contains the top 10 campaigns with the highest Click-Through Rate (CTR).

**Structure:**
```csv
campaign_id,total_impressions,total_clicks,total_spend,total_conversions,CTR,CPA
CAMP_005,50000,5000,2500.00,250,0.100000,10.00
CAMP_003,30000,2700,1800.00,180,0.090000,10.00
```

**Metrics:**
- **CTR** = total_clicks ÷ total_impressions
- **CPA** = total_spend ÷ total_conversions (empty if no conversions)

### 2. top_10_lowest_cpa.csv

Contains the top 10 campaigns with the lowest Cost Per Acquisition (CPA).

**Structure:**
```csv
campaign_id,total_impressions,total_clicks,total_spend,total_conversions,CTR,CPA
CAMP_007,40000,2000,1000.00,500,0.050000,2.00
CAMP_012,35000,1750,1050.00,350,0.050000,3.00
```

**Note:** Campaigns with zero conversions are excluded from this report.

---

## 🔍 Processing Details

### What Happens During Execution

1. **Initialization**
   ```
   Processing file: your_data.csv
   Method: sqlite
   ```

2. **Database Creation**
   - Creates `campaign_data.db` (temporary file)
   - Creates table and index

3. **Data Loading**
   ```
   Loaded 10000 lines...
   Loaded 20000 lines...
   ...
   Total lines loaded: 5234891
   ```

4. **Report Generation**
   ```
   Generated: top_10_highest_ctr.csv
   Generated: top_10_lowest_cpa.csv
   ```

5. **Cleanup**
   - Database file is automatically deleted
   - Only CSV output files remain

### Performance Expectations

| Input Size | Processing Time | Memory Usage | Disk Usage |
|------------|----------------|--------------|------------|
| 100MB | ~30 seconds | 100MB | 80MB |
| 1GB | ~3-4 minutes | 100MB | 800MB |
| 5GB | ~15-20 minutes | 100MB | 4GB |
| 10GB | ~30-40 minutes | 100MB | 8GB |

---

## 🛠️ Troubleshooting

### Error: "Class not found: org.sqlite.JDBC"

**Problem:** SQLite JDBC driver not in classpath

**Solution:**
```bash
# Make sure you include the jar in classpath
java -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer ad_data.csv
```

### Error: "OutOfMemoryError"

**Problem:** Not enough heap memory

**Solution:**
```bash
# Increase heap size
java -Xmx2g -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer ad_data.csv
```

### Error: "No space left on device"

**Problem:** Not enough disk space for database file

**Solution:**
- Free up disk space (need ~80% of CSV file size)
- Or process on a different drive

### Error: "File not found"

**Problem:** Input file path is incorrect

**Solution:**
```bash
# Use absolute path
java -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer /full/path/to/ad_data.csv

# Or ensure you're in the correct directory
cd /path/to/data
java -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer ad_data.csv
```

### Slow Performance

**Solutions:**

1. **Increase batch size** (edit Java file):
```java
if (lineCount % 50000 == 0) {  // Changed from 10000
    pstmt.executeBatch();
```

2. **Use SSD instead of HDD** for faster I/O

3. **Close other applications** to free system resources

### Invalid Data Issues

**Problem:** "Skipping invalid line" messages

**Common Causes:**
- Missing columns in CSV
- Non-numeric values in numeric fields
- Extra commas in campaign_id or other fields

**Solution:**
- Check CSV format matches requirements
- Remove or fix problematic lines
- Ensure proper CSV escaping for special characters

---

## 🔬 Advanced Configuration

### Inspecting the Database

While processing, you can inspect the database:

**Compile inspector:**
```bash
javac -cp ".:sqlite-jdbc-3.51.1.0.jar" CampaignDBInspector.java
```

**Run inspector (in another terminal):**
```bash
java -cp ".:sqlite-jdbc-3.51.1.0.jar" CampaignDBInspector campaign_data.db
```

### Keeping the Database

To keep `campaign_data.db` for further analysis:

**Edit AdPerformanceAnalyzer.java:**
```java
// Comment out this line near the end of processSQLite():
// Files.deleteIfExists(Paths.get(dbFile));

System.out.println("Database saved: " + dbFile);
```

Then you can query it directly:
```bash
sqlite3 campaign_data.db
```

### Custom SQL Queries

Once you have the database, run custom queries:

```sql
-- Top 20 campaigns by spend
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
```

### Batch Processing Multiple Files

**Create a script (process_all.sh):**
```bash
#!/bin/bash

for file in data/*.csv; do
    echo "Processing $file"
    java -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer "$file"
    
    # Rename output files
    mv top_10_highest_ctr.csv "ctr_$(basename $file)"
    mv top_10_lowest_cpa.csv "cpa_$(basename $file)"
done
```

**Run:**
```bash
chmod +x process_all.sh
./process_all.sh
```

---

## 📞 Support

### Common Commands Reference

**Linux/Mac:**
```bash
# Compile
javac -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer.java

# Run
java -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer ad_data.csv

# Run with more memory
java -Xmx2g -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer ad_data.csv
```

**Windows:**
```cmd
# Compile
javac -cp ".;sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer.java

# Run
java -cp ".;sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer ad_data.csv

# Run with more memory
java -Xmx2g -cp ".;sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer ad_data.csv
```

### Example Session

```bash
$ ls
AdPerformanceAnalyzer.java  advertising_data.csv  sqlite-jdbc-3.51.1.0.jar

$ javac -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer.java

$ java -cp ".:sqlite-jdbc-3.51.1.0.jar" AdPerformanceAnalyzer advertising_data.csv
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
```

---

## 📝 License

This is a utility application for data processing. Use at your own discretion.

## ✨ Tips for Best Performance

1. **Use SSD** for faster disk I/O
2. **Process during off-peak hours** for large files
3. **Monitor disk space** - ensure 2x input file size available
4. **Close unnecessary applications** to free memory
5. **Use absolute paths** to avoid file not found errors
6. **Validate CSV format** before processing large files

---