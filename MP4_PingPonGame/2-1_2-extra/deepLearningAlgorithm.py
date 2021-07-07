import random
import numpy as np
import math

filepath = "training_data_2500"
class deepLearningAlgorithm(object):
    epoch = 300
    batch_size = 125
    feature_node_size = 5
    hidden_node_size = 256
    outcome_size = 3
    learning_rate = 0.01
    weight = 0.1
    #test = False
    random_weight = False
    def __init__(self):
#         self.w1 = self.read_data('training_data_three_network_500/w1.txt')
#         self.w2 = self.read_data('training_data_three_network_500/w2.txt')
#         self.w3 = self.read_data('training_data_three_network_500/w3.txt')
#         self.w4 = self.read_data('training_data_three_network_500/w4.txt')
#         self.b1 = self.read_data('training_data_three_network_500/b1.txt')
#         self.b2 = self.read_data('training_data_three_network_500/b2.txt')
#         self.b3 = self.read_data('training_data_three_network_500/b3.txt')
#         self.b4 = self.read_data('training_data_three_network_500/b4.txt')
        self.w1 = self.read_data('training_data/w1.txt')
        self.w2 = self.read_data('training_data/w2.txt')
        self.w3 = self.read_data('training_data/w3.txt')
        self.w4 = self.read_data('training_data/w4.txt')
        self.b1 = self.read_data('training_data/b1.txt')
        self.b2 = self.read_data('training_data/b2.txt')
        self.b3 = self.read_data('training_data/b3.txt')
        self.b4 = self.read_data('training_data/b4.txt')
        expert_data = self.read_data('expert_policy.txt')
        self.mean = np.mean(expert_data[:,0:5], axis=0)
        self.std = np.std(expert_data[:,0:5], axis=0)
    def read_data(self, filename):
        data = []
        with open(filename,'r+') as f:
            for index in f:
                index = index.strip().split(' ')           
                temp = []
                for i in index: 
                    temp.append(float(i))              
                data.append(temp)
        return np.asarray(data)
    def affine_forward(self, A, W, b):
        Z = np.dot(A, W) + b;
       # Z = Z / np.ndarray.max(Z)
        cache = [A,W,b]
        return (Z, cache)
    def affline_backward(self, dZ, cache):
        dA = np.zeros_like(cache[0])
        dW = np.zeros_like(cache[1])
        db = np.zeros_like(cache[2])
        A = cache[0]
        W = cache[1]
        b = cache[2]
        for i in range(len(dA)):
            for k in range(np.shape(dA)[1]):
                dA[i][k] = np.inner(dZ[i,:], W[k,:])
        for j in range(np.shape(dW)[1]):
            for k in range(np.shape(dA)[1]):
                dW[k][j] = np.inner(A[:,k], dZ[:,j])
            db[0][j] = np.sum(dZ[:,j]) 
        return (dA, dW, db)
    def relu_forward(self, Z):
        cache = Z.copy()
        for x in np.nditer(Z, op_flags=['readwrite']):
            if x < 0:
                 x[...] = 0
        return (Z, cache)
    def relu_backward(self, dA, cache):
        for x,y in np.nditer([cache,dA], op_flags=['readwrite']):
            if x < 0:
                 y[...] = 0
        return (dA)
    def batch(self, i, data):
       # print data
        return (data[i*self.batch_size:(i+1)*self.batch_size,0:5], data[i*self.batch_size:(i+1)*self.batch_size,5])
    def three_network(self, X):
        X -= self.mean
        X /= self.std
        (z1, acache1) = self.affine_forward(X, self.w1, self.b1)
        (a1, rcache1) = self.relu_forward(z1)
        (z2, acache2) = self.affine_forward(a1, self.w2, self.b2)
        (a2, rcache2) = self.relu_forward(z2)
        (z3, acache3) = self.affine_forward(a2, self.w3, self.b3)  
        (a3, rcache3) = self.relu_forward(z3)
        (F, acache4) = self.affine_forward(a3, self.w4, self.b4)
       # T = np.ndarray.argmax(F,1)
       # num = 0
       # for t in range(len(y)):
       #     if int(y[t]) is int(T[t]):
       #         num = num + 1
       # print (float(num)/float(len(y)))
        #print len(y)  
        T = np.ndarray.argmax(F,1)
       # print T[0]-1 
        return T[0]-1 
    def cross_entropy(self, F, y):
        first = 0
        dF = np.zeros_like(F)
       # print F
        for i in range(len(F)):
            first += F[i][int(y[i])]
            exp_sum = 0
            for k in range(np.shape(F)[1]):
                exp_sum += math.exp(long(F[i][k]))
            #print exp_sum
            for j in range(np.shape(F)[1]):
                temp = 0
                if j == int(y[i]):
                    temp = 1
                second = temp - (math.exp(F[i][j])/(exp_sum))
                dF[i][j] = float(-1)/len(F)* second
            first -= math.log10(exp_sum)
        L = float(-1)/len(F)* first
        return (L, dF)
    def save_training_data(self, w1, w2, w3, b1, b2, b3):
        #ws1 = np.array2string(w1, formatter={'float_kind':lambda w1: "%f" % w1})
        with open('training_data/w1.txt','w+') as f1:
            for j in w1:
                f1.write(' '.join(str(e) for e in j)) 
                f1.write('\n')
        with open('training_data/w2.txt','w+') as f2:
            for j in w2:
                f2.write(' '.join(str(e) for e in j)) 
                f2.write('\n')
        with open('training_data/w3.txt','w+') as f3:
            for j in w3:
                f3.write(' '.join(str(e) for e in j)) 
                f3.write('\n')
        with open('training_data/b1.txt','w+') as f4:
            for j in b1:
                f4.write(' '.join(str(e) for e in j))
                f4.write('\n') 
        with open('training_data/b2.txt','w+') as f5:
            for j in b2:
                f5.write(' '.join(str(e) for e in j))
                f5.write('\n') 
        with open('training_data/b3.txt','w+') as f6:
            for j in b3:
                f6.write(' '.join(str(e) for e in j)) 
                f6.write('\n')
    def test(self):
        A = []
        with open('A.txt','r+') as f:
            for index in f:
                index = index.strip().split(' ')           
                temp = []
                for i in index: 
                    temp.append(float(i))              
                A.append(temp)
        A = np.asarray(A)
        b = []
        with open('b.txt','r+') as f:
            for index in f:
                index = index.strip().split(' ')           
                temp = []
                for i in index: 
                    temp.append(float(i))              
                b.append(temp)
        b = np.asarray(b)
        W = []
        with open('W.txt','r+') as f:
            for index in f:
                index = index.strip().split(' ')           
                temp = []
                for i in index: 
                    temp.append(float(i))              
                W.append(temp)
        W = np.asarray(W)
        dZ = []
        with open('dZ.txt','r+') as f:
            for index in f:
                index = index.strip().split(' ')           
                temp = []
                for i in index: 
                    temp.append(float(i))              
                dZ.append(temp)
        dZ = np.asarray(dZ)
        (Z, cache)= self.affine_forward(A, W, b)
        print Z
        (dA, dW, db) = self.affline_backward(dZ, cache)
        print dW
        print db
#test()
#A = np.array([[1,2,3],[4,-5,6],[7,8,9]])
#print  np.inner(A[:,0], A[:,0])
