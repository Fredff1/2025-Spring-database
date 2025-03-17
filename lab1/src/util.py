import yaml
import pandas as pd

def read_yaml(path:str,encoding="utf-8",mode="r") -> dict:
    with open(path, mode, encoding=encoding) as file:
        data:dict=yaml.safe_load(file)
    return data

def read_csv(path:str,encoding="utf-8")->pd.DataFrame:
    data:dict=pd.read_csv(path,encoding=encoding)
    return data

