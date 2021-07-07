#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Apr 25 12:20:18 2018

@author: hundredball
"""
import numpy as np
import random
import matplotlib.pyplot as plt
from copy import copy

class DeepQAlgorithm(object) : 
    def __init__(self, epoch, num_layer, batchSize) : 
        self.data = []
        self.weights = []       # num_layer weight matrices
        self.biases = []        # num_layer bias vectors
        self.A = []             # store output from relu
        self.Z = []             # store output from Affine
        self.dZ = []            # store backward output for each layer 
        self.num_node = 256     # except the last layer
        self.learningRateDecay = 0;
        self.learningRate = 0.002
#        self.alpha = 0.1
        self.weightScale = 0.2
        self.num_data = 0
        self.epoches = epoch
        self.batchSize = batchSize
        self.num_layer = num_layer
        self.losses = np.zeros((self.epoches, 1))
        self.accuracy = np.zeros((self.epoches, 1))      
        self.average = []        #Average of each column of training data
        self.std = []            #Std of each columm of training data
        
        # (weightScale, LR, LRD) -> loss
        # (0.1,0.01,0.00008) -> 0.7589
        
        #import data
        f = open("expertAdvantage.txt")
        for line in f:
            str_array = line.split()
            float_array=list(map(lambda x: float(x), str_array))
            self.data.append(np.array(float_array))
        self.data = np.array(self.data)
        self.num_data = len(self.data)
        self.normalize()  
        
        #initialize weights and biases
        W = np.zeros((5,self.num_node))
        b = np.zeros((batchSize, self.num_node))
        self.weights.append(W)
        self.biases.append(b)
        
        for i in range(2,num_layer):
            W = np.zeros((self.num_node,self.num_node))
            b = np.zeros((batchSize, self.num_node))
            self.weights.append(W)
            self.biases.append(b)
        W = np.zeros((self.num_node,3))
        b = np.zeros((batchSize,3))
        self.weights.append(W)
        self.biases.append(b)
        
        #set weights random values
        for matrix in self.weights:
            for row in matrix:
                for col in range(len(row)):
                    row[col] = (2*random.random()-1) * self.weightScale
#                    row[col] = random.random() * self.weightScale
                    
        #initialize output matrix and dZ
        for i in range(1, num_layer):
            Y1 = np.zeros((self.batchSize, self.num_node))
            Y2 = np.zeros((self.batchSize, self.num_node))
            Y3 = np.zeros((self.batchSize, self.num_node))
            self.A.append(Y1)
            self.Z.append(Y2)
            self.dZ.append(Y3)
        Y1 = np.zeros((batchSize, 3))
        Y2 = np.zeros((batchSize, 3))
        self.Z.append(Y1)
        self.dZ.append(Y2)
        
        
    def runAlgorithm(self, mode) :
        if mode == "training" :
            for epoch in range(0, self.epoches):
                print("---------------------Epoch : " + str(epoch))
                (features, labels) = self.splitBatch()
                
                for index_batch in range(0, int(self.num_data/self.batchSize)):
#                    print("Batch : " + str(index_batch))
                    # Forward
                    self.Z[0] = self.affineForward(features[index_batch], self.weights[0], self.biases[0])
                    self.A[0] = self.reluForward(self.Z[0])
                    for i in range(1, self.num_layer):
                        self.Z[i] = self.affineForward(self.A[i-1], self.weights[i], self.biases[i])
                        if i != self.num_layer-1:
                            self.A[i] = self.reluForward(self.Z[i])
#                    self.Z[1] = self.affineForward(self.A[0], self.weights[1], self.biases[1])
#                    self.A[1] = self.reluForward(self.Z[1])
#                    self.Z[2] = self.affineForward(self.A[1], self.weights[2], self.biases[2])
    
                    # calculate loss and dF
                    (loss, dF, misclass) = self.crossEntropy(self.Z[self.num_layer-1], labels[index_batch])
                    self.dZ[self.num_layer-1] = dF
                    self.losses[epoch] += loss
                    self.accuracy[epoch] += misclass
#                    print(str(index_batch) + " : " + str(loss))
                    
                    # Backward
                    for i in range(0, self.num_layer-1):
                        dA = self.affineBackward(self.dZ[self.num_layer-1-i], self.weights[self.num_layer-1-i], self.A[self.num_layer-1-i-1], self.biases[self.num_layer-1-i])
                        self.dZ[self.num_layer-1-i-1] = self.reluBackward(dA, self.Z[self.num_layer-1-i-1])
#                    dA = self.affineBackward(self.dZ[self.num_layer-1], self.weights[self.num_layer-1], self.A[self.num_layer-2], self.biases[self.num_layer-1])
#                    self.dZ[self.num_layer-2] = self.reluBackward(dA, self.Z[self.num_layer-2])
#                    dA = self.affineBackward(self.dZ[self.num_layer-1-1], self.weights[self.num_layer-1-1], self.A[self.num_layer-2-1], self.biases[self.num_layer-1-1])
#                    self.dZ[self.num_layer-2-1] = self.reluBackward(dA, self.Z[self.num_layer-2-1])
                    dA = self.affineBackward(self.dZ[0], self.weights[0], features[index_batch], self.biases[0])
#                    print(index_batch)
                    
#                print("Z[3][0] : " + str(self.Z[self.num_layer-1][0])) 
#                print("W[num_layer-1][0] : " + str(self.weights[self.num_layer-1][0]))
                self.accuracy[epoch] = 100 - self.accuracy[epoch]/self.num_data*100
                self.losses[epoch] /= (self.num_data/self.batchSize)
                self.learningRate *= (1./(1.+self.learningRateDecay*epoch))
                print("Loss : " + str(self.losses[epoch]))
                print("Learning Rate : " + str(self.learningRate) ) 
                print("Accuracy : " + str(self.accuracy[epoch]) + "%")
        
            #plot loss curve
            x = np.arange(0,self.epoches)
            plt.plot(x, self.losses)
            plt.title('Loss : ' + str(self.losses[self.epoches-1]) + ' | LR : ' + str(self.learningRate) + ' | LRD : ' + str(self.learningRateDecay))
            plt.xlabel('Epoch')
            plt.ylabel('Loss')
            plt.show()
        
    def crossEntropy(self, Z, labels) :
        loss = 0
        misclass = 0
        dF = np.empty([0,3])
        for i in range(0, len(Z)):
            avg = sum(Z[i]) / 3         
            F_list = list(map(lambda j: (Z[i][j] - avg - labels[i][j])**2, range(3)))
            loss += sum(F_list)
            dF = np.append(dF, np.reshape(Z[i] - avg - labels[i], (1,3)), axis = 0)
            
            if(np.argmax(Z[i]) != np.argmax(labels[i])):
#                print(np.argmax(Z[i]))
                misclass += 1       
            
        loss /= (2*len(Z))
        dF /= len(Z)
        return (loss, dF, misclass)
        
    def affineForward(self, A, W, b) : 
        return np.dot(A,W) + b
        
            
    def reluForward(self, Z) :
        return np.maximum(Z,0)
        
    def reluBackward(self, dA, Z):
        dA[Z == 0] = 0
        return dA
        
    def affineBackward(self, dZ, W,A,B) : 
        dB = np.zeros((self.batchSize, len(W[0])))
        dA = np.dot(dZ, np.transpose(W))
        dW = np.dot(np.transpose(A), dZ)
        for i in range(len(dZ[0])):
            dB[:,i] = sum(dZ[:,i])
        
        #update weights and biases
        W[:] -=  self.learningRate * dW
        B[:] -=  self.learningRate * dB
        
        return dA
        
    def splitBatch(self):
        features = []
        labels = []
        tempFeatures = []
        tempLabels = []
        data = self.data
        np.random.shuffle(data)
        
        for i in range(0, self.num_data):
            tempFeatures.append(data[i][0:5])
            tempLabels.append(data[i][5:8])
            if i%self.batchSize == self.batchSize-1:
                features.append(tempFeatures)
                labels.append(tempLabels)
                tempFeatures = []
                tempLabels = []
                
        features = np.array(features)
        labels = np.array(labels)
        return (features, labels)
                
    def normalize(self):
        for i in range(len(self.data[0])-3):
            mean = np.mean(self.data[:,i])
            std = np.std(self.data[:,i])
            self.data[:,i] = (self.data[:,i]-mean) / std
            self.average.append(mean)
            self.std.append(std)
            
    def actionChoice(self,gameState):
#        print(gameState)
        gameState = np.array(gameState)
        gameState = np.divide((gameState - self.average[0:5]), self.std[0:5])
        self.Z[0] = self.affineForward(gameState, self.weights[0], self.biases[0])
        self.A[0] = self.reluForward(self.Z[0])
        for i in range(1, self.num_layer):
            self.Z[i] = self.affineForward(self.A[i-1], self.weights[i], self.biases[i])
            if i != self.num_layer-1:
                self.A[i] = self.reluForward(self.Z[i])
                
        
        action = np.argmax(self.Z[self.num_layer-1])
#        print(action)
        return action-1
        
    def readWeights(self):
        for i in range(0, self.num_layer):
            W_string = 'W' + str(i) + '.txt'
            B_string = 'B' + str(i) + '.txt'
            W = []
            B = []
            with open(W_string, 'r+') as f:
                for line in f:
                    str_array = line.split()
                    float_array=list(map(lambda x: float(x), str_array))
                    W.append(np.array(float_array))
            W = np.array(W)
            self.weights[i][:] = W
            
            with open(B_string, 'r+') as f:
                for line in f:
                    str_array = line.split()
                    float_array=list(map(lambda x: float(x), str_array))
                    B.append(np.array(float_array))
            B = np.array(B)
            
            for j in range(0, len(self.biases[i])):
                self.biases[i][j][:] = B[0]
            
    def writeWeights(self):
        for i in range(0, self.num_layer):
            W_string = 'W' + str(i) + '.txt'
            B_string = 'B' + str(i) + '.txt'
            with open(W_string, 'w+') as f1:
                for row in self.weights[i]:
                    f1.write(' '.join(str(e) for e in row))
                    f1.write('\n')
            with open(B_string, 'w+') as f1:
                for row in self.biases[i]:
                    f1.write(' '.join(str(e) for e in row))
                    f1.write('\n')            
        
        
#dq = DeepQAlgorithm(500,4,125)
##dq.readWeights()
#dq.runAlgorithm("training")
#dq.writeWeights()
