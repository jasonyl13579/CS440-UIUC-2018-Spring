import csv
import numpy as np
import scipy as sci
import pandas as pd

dataset=pd.read_csv("training.csv")
X = dataset.iloc[:, 0:147].values
Y = dataset.iloc[:, 147].values

def train_logistic_regression(X,Y,lr):
    z=0
    weights=np.zeros(147)
    diffs=np.zeros(500)
    for x in range(1,301):
        for r in range(0,500):
            for c in range(0,147):
               z=z+weights[c]*X[r][c]
            z=1/(1+np.exp(-z))
            diffs[r]=z-Y[r]
            
        for c in range(0,147):
            grad=0
            for r in range(0,500):
                grad=grad+X[r][c]*diffs[r]
            grad=grad*lr/500
            weights[c]=weights[c]-grad
    return weights
weights=train_logistic_regression(X,Y,0.01)

output=open('weight.txt',"w")
for item in weights:
    output.write(str(item))
    output.write("\n")
output.close()
