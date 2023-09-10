#coding=gbk 
import socket
import sys
import threading
import json
import numpy as np
import os
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

def main():
    # �����������׽���
    serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # ��ȡ������������
    host = socket.gethostname()
    # ����һ���˿�
    port = 12345
    # ���׽����뱾�������Ͷ˿ڰ�
    serversocket.bind((host,port))
    # ���ü������������
    serversocket.listen(5)
    # ��ȡ���ط�������������Ϣ
    myaddr = serversocket.getsockname()
    print("��������ַ:%s"%str(myaddr))
    # ѭ���ȴ����ܿͻ�����Ϣ
    while True:
        # ��ȡһ���ͻ�������
        clientsocket,addr = serversocket.accept()
        print("���ӵ�ַ:%s" % str(addr))
        try:
            t = ServerThreading(clientsocket)#Ϊÿһ��������һ�������߳�
            t.start()
            pass
        except Exception as identifier:
            print(identifier)
            pass
        pass
    serversocket.close()
    pass
    
class ServerThreading(threading.Thread):
    # words = text2vec.load_lexicon()
    def __init__(self,clientsocket,recvsize=1024*1024,encoding="utf-8"):
        threading.Thread.__init__(self)
        self._socket = clientsocket
        self._recvsize = recvsize
        self._encoding = encoding
        pass

    def run(self):
        print("�����߳�.....")
        try:
            #��������
            msg = ''
            while True:
                # ��ȡrecvsize���ֽ�
                rec = self._socket.recv(self._recvsize)
                # ����
                msg += rec.decode(self._encoding)
                # �ı������Ƿ���ϣ���Ϊpython socket�����Լ��жϽ��������Ƿ���ϣ�
                # ������Ҫ�Զ���Э���־���ݽ������
                if msg.strip().endswith('over'):
                    msg=msg[:-4]
                    break
            # ����json��ʽ������
            re = json.loads(msg)
            # ����������ģ�ʹ�������
            res = predict_sqli_attack(re['content'])
            # res = nnservice.hand(re['content'])
            sendmsg = json.dumps(res)
            # ��������
            self._socket.send(("%s"%sendmsg).encode(self._encoding))
            pass
        except Exception as identifier:
            self._socket.send("500".encode(self._encoding))
            print(identifier)
            pass
        finally:
            self._socket.close() 
        print("�������.....")
        
        pass

    def __del__(self):
        
        pass

if __name__ == "__main__":
    mymodel = tf.keras.models.load_model('D:/DataBase-CNN-5Xuanxue20230521/my_model_cnn_Modified.h5')
    myvectorizer = pickle.load(open("D:/DataBase-CNN-5Xuanxue20230521/vectorizer_cnn_sqlparse", 'rb'))
    def parse_sql(sql):
        try:
            stmt = sqlparse.parse(sql)[0]
        except:
            stmt = None
        if stmt == None:
            return None
        else :
            return stmt.tokens
    def predict_sqli_attack(sql):
        input_val=sql

        df = pd.DataFrame({
        'sql': [input_val]
        })
        Y = df['sql']
        Y=Y.apply(parse_sql)

        Y=myvectorizer.transform(Y.astype('U')).toarray()


        Y.shape=(-1,1,3966)
        
        result=mymodel.predict(Y)

        if result>0.5:
            return 'nononono'


        elif result<=0.5:
            return 'yesyesyesyes'
    main()