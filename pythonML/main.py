import os
import sys
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.model_selection import train_test_split
from nltk.corpus import stopwords
from sklearn.metrics import accuracy_score, f1_score
from sklearn.linear_model import LogisticRegression
from sklearn.ensemble import RandomForestClassifier
from sklearn.svm import SVC
from sklearn.naive_bayes import GaussianNB
import tensorflow as tf
from tensorflow.keras.utils import plot_model
from sklearn import tree
from tensorflow.keras import models, layers
import warnings

from sklearn.metrics import confusion_matrix, classification_report, f1_score, precision_score, recall_score, accuracy_score
from sklearn.metrics import precision_recall_curve,precision_recall_fscore_support
import seaborn as sns
warnings.filterwarnings('ignore')
from sklearn.feature_extraction.text import TfidfVectorizer  
from transformers import TFBertModel, BertTokenizer
import sqlparse
import keras
from keras.models import load_model
import pickle



def parse_sql(sql):
    try:
        stmt = sqlparse.parse(sql)[0]
    except:
        stmt = None
    if stmt == None:
        return None
    else :
        return stmt.tokens
    
    
def clean_data(input_val):
    input_val=input_val.replace('\n', '')
    input_val=input_val.replace('%20', ' ')
    input_val=input_val.replace('=', ' = ')
    input_val=input_val.replace('((', ' (( ')
    input_val=input_val.replace('))', ' )) ')
    input_val=input_val.replace('(', ' ( ')
    input_val=input_val.replace(')', ' ) ')
    input_val=input_val.replace('1 ', 'numeric')
    input_val=input_val.replace(' 1', 'numeric')
    input_val=input_val.replace("'1 ", "'numeric ")
    input_val=input_val.replace(" 1'", " numeric'")
    input_val=input_val.replace('1,', 'numeric,')
    input_val=input_val.replace(" 2 ", " numeric ")
    input_val=input_val.replace(' 3 ', ' numeric ')
    input_val=input_val.replace(' 3--', ' numeric--')
    input_val=input_val.replace(" 4 ", ' numeric ')
    input_val=input_val.replace(" 5 ", ' numeric ')
    input_val=input_val.replace(' 6 ', ' numeric ')
    input_val=input_val.replace(" 7 ", ' numeric ')
    input_val=input_val.replace(" 8 ", ' numeric ')
    input_val=input_val.replace('1234', ' numeric ')
    input_val=input_val.replace("22", ' numeric ')
    input_val=input_val.replace(" 8 ", ' numeric ')
    input_val=input_val.replace(" 200 ", ' numeric ')
    input_val=input_val.replace("23 ", ' numeric ')
    input_val=input_val.replace('"1', '"numeric')
    input_val=input_val.replace('1"', '"numeric')
    input_val=input_val.replace("7659", 'numeric')
    input_val=input_val.replace(" 37 ", ' numeric ')
    input_val=input_val.replace(" 45 ", ' numeric ')
    return input_val


def predict_sqli_attack(sql):
    mymodel = tf.keras.models.load_model('D:/DataBase-CNN-5Xuanxue20230521/my_model_cnn_Modified.h5')
    myvectorizer = pickle.load(open("D:/DataBase-CNN-5Xuanxue20230521/vectorizer_cnn_sqlparse", 'rb'))
    input_val=sql

    input_val=clean_data(input_val)
    df = pd.DataFrame({
    'sql': [input_val]
    })
    Y = df['sql']
    Y=Y.apply(parse_sql)

    Y=myvectorizer.transform(Y.astype('U')).toarray()

    
    Y.shape=(-1,1,3966)
     
    result=mymodel.predict(Y)



    if result>0.5:
         print("ALERT :::: This can be SQL injection")


    elif result<=0.5:
        print("safe")
            
if __name__ == '__main__':
    predict_sqli_attack(sys.argv[1])