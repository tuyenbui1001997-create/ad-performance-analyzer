I combine both chatgpt and [clau](https://claude.ai/) with free to solve these problems, prompts used for in below:

- We have a large CSV dataset (~1GB) containing advertising performance records as an input with structure (campaign_id,date,impressions,clicks,spend,conversions). Requirements: 1. Aggregate data by campaign_id For each campaign_id, compute: total_impressions total_clicks total_spend total_conversions CTR = total_clicks / total_impressions CPA = total_spend / total_conversions If conversions = 0, ignore or return null for CPA 2. Generate two result lists A. Top 10 campaigns with the highest CTR output as CSV format with structure (campaign_id,total_impressions,total_clicks,total_spend,total_conversions,CTR,CPA) B. Top 10 campaigns with the lowest CPA output as CSV format with structure (campaign_id,total_impressions,total_clicks,total_spend,total_conversions,CTR,CPA) and exclude campaigns with zero conversions Give me console application by Java programming with

- Use Map<String, CampaignMetrics> as compaignMap result can be got OutOfMemoryError, give me the solutions to solve this problem

- I choose Solution 1: give me readme file to know how to run application