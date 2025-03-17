import mysql.connector
import pandas as pd
import yaml

import traceback

from util import read_yaml,read_csv

class DatabaseProcessor:
    def __init__(self,config_path:str="./src/config.yaml"):
        
        config=read_yaml(config_path)
        
        db_config = config['database_config']
        try:
            conn = mysql.connector.connect(
                host=db_config['host'],
                user=db_config['user'],
                password=db_config['password'],
                database=db_config['database'],
                sql_mode=db_config["sql_mode"],
            )
            cursor = conn.cursor()
            print("Database connected")
            self.cursor=cursor
            self.conn=conn
            

        except Exception as e:
            print("Failed to connect to database", e)
            self.cursor=None
            self.conn=None
            
    def read_sql_config(self, sql_file_path: str):
        try:
            with open(sql_file_path, 'r', encoding='utf-8') as file:
                sql_statements = file.read()
            self.cursor.execute(sql_statements)
            self.conn.commit()
            print("Config set")
        except Exception as e:
            print("Error reading config:", e)
            
    def create_table(self, sql_file_path: str):
        try:
            with open(sql_file_path, 'r', encoding='utf-8') as file:
                sql_statements = file.read()
            self.cursor.execute(sql_statements)
            self.conn.commit()
            print("Table created successfully")
        except Exception as e:
            print("Error creating table:", e)
            
    def insert_data_from_csv(self, csv_file_path: str, table_name: str,primary_keys:str|list,encoding="utf-8"):
        """读取 CSV 数据，并将数据插入指定数据表"""
        try:
            df = read_csv(csv_file_path,encoding=encoding)
            columns = list(df.columns)
            placeholders = ', '.join(['%s'] * len(columns))
            col_names = ', '.join(columns)
            
            insert_sql_template = f"INSERT INTO {table_name} ({col_names}) VALUES ({placeholders})"
            
            filter_subset=primary_keys if isinstance(primary_keys,list) else [primary_keys]
            
            df = df.drop_duplicates(subset=filter_subset, keep="first")  
            
            

            if df.empty:
                print("All data already exists in the database. No insertion needed.")
                return
            
            for index, row in df.iterrows():
                values = tuple(None if pd.isna(x) else str(x) for x in row)
                self.cursor.execute(insert_sql_template,values)
                
            self.conn.commit()
            print(f"Data inserted successfully to table {table_name}")
        except Exception as e:
            print(f"Error inserting data to table {table_name}:", e)
            traceback.print_exc()

        
    def close(self):
        if self.conn and self.conn.is_connected():
            self.cursor.close()
            self.conn.close()
            print("Database connection closed")
            
                
if __name__=="__main__":
    processor=DatabaseProcessor()
    if processor.conn:
        
        
        processor.create_table("./src/create_table.sql")
        
        processor.insert_data_from_csv("./student.csv", "student_info", "registno")
        
        processor.insert_data_from_csv("./room.csv", "room_info", ["kdno","kcno","ccno"], encoding="gbk")
        
        # 完成操作后关闭连接
        processor.close()

