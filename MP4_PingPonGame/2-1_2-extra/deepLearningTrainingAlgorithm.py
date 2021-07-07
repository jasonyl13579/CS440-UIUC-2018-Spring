import random
import numpy as np
import math
import matplotlib.pyplot as plt
BALL_X = 0
BALL_Y = 1
VELOCITY_X = 2
VELOCITY_Y = 3
PADDLE_Y = 4
ACTION = 5
epoch = 500
batch_size = 125
feature_node_size = 5
hidden_node_size = 256
outcome_size = 3
learning_rate = 0.005
weight = 0.1
test = False
random_weight = False
s = (3,3)
confusion_matrix = np.zeros(s)
def read_data(filename):
    data = []
    with open(filename,'r+') as f:
        for index in f:
            index = index.strip().split(' ')           
            temp = []
            for i in index: 
                temp.append(float(i))              
            data.append(temp)
    return np.asarray(data)
def affine_forward(A, W, b):
    Z = np.dot(A, W) + b;
   # Z = Z / np.ndarray.max(Z)
    cache = [A,W,b]
    return (Z, cache)
def affline_backward(dZ, cache):
    dA = np.zeros_like(cache[0])
    dW = np.zeros_like(cache[1])
    db = np.zeros_like(cache[2])
    A = cache[0]
    W = cache[1]
    b = cache[2]
    dA = np.dot(dZ, np.transpose(W));
    dW = np.dot(np.transpose(A), dZ);
#     for i in range(len(dA)):
#         for k in range(np.shape(dA)[1]):
#             dA[i][k] = np.inner(dZ[i,:], W[k,:])
#     for j in range(np.shape(dW)[1]):
#         for k in range(np.shape(dA)[1]):
#             dW[k][j] = np.inner(A[:,k], dZ[:,j])
#         db[0][j] = np.sum(dZ[:,j]) 
    for j in range(np.shape(dW)[1]):
        db[0][j] = np.sum(dZ[:,j]) 
    return (dA, dW, db)
def relu_forward(Z):
#     for x in np.nditer(Z, op_flags=['readwrite']):
#         if x < 0:
#              x[...] = 0
    return (np.maximum(Z, 0), np.maximum(Z, 0))
def relu_backward(dA, cache):
#     for x,y in np.nditer([cache,dA], op_flags=['readwrite']):
#         if x < 0:
#              y[...] = 0
    dA[cache == 0] = 0
    return (dA)
def batch(i, data):
   # print data
    return (data[i*batch_size:(i+1)*batch_size,0:5], data[i*batch_size:(i+1)*batch_size,5])
def three_network(X, W, B, y, test):
    (w1, w2, w3, w4) = W
    (b1, b2, b3, b4) = B
    (z1, acache1) = affine_forward(X, w1, b1)
    (a1, rcache1) = relu_forward(z1)
    (z2, acache2) = affine_forward(a1, w2, b2)
    (a2, rcache2) = relu_forward(z2)
    (z3, acache3) = affine_forward(a2, w3, b3)    
    (a3, rcache3) = relu_forward(z3)
    (F, acache4) = affine_forward(a3, w4, b4) 
    T = np.ndarray.argmax(F,1)
    num = 0
    for t in range(len(y)):
        if int(y[t]) is int(T[t]):
            num = num + 1
    for t in range(len(y)):
        confusion_matrix[int(y[t])][int(T[t])] = confusion_matrix[int(y[t])][int(T[t])] + 1
    (loss, dF) = cross_entropy(F, y)
    (da3, dw4, db4) = affline_backward(dF, acache4)
    dz3 = relu_backward(da3, rcache3)
    (da2, dw3, db3) = affline_backward(dz3, acache3)
    dz2 = relu_backward(da2, rcache2)
    (da1, dw2, db2) = affline_backward(dz2, acache2)
    dz1 = relu_backward(da1, rcache1)
    (dx, dw1, db1) = affline_backward(dz1, acache1)
   # print 'before'
   # print w1
   # print dw1
    w1 = w1 - learning_rate * dw1
    w2 = w2 - learning_rate * dw2
    w3 = w3 - learning_rate * dw3
    w4 = w4 - learning_rate * dw4
    b1 = b1 - learning_rate * db1
    b2 = b2 - learning_rate * db2
    b3 = b3 - learning_rate * db3
    b4 = b4 - learning_rate * db4
    #np.subtract(w1, learning_rate * dw1 )
   # print 'after'
   # print w1
    Wr = (w1, w2, w3, w4)
    Br = (b1, b2, b3, b4)
    return (loss, Wr, Br, num)
def cross_entropy(F, y):
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
def save_training_data(W, B):
    #ws1 = np.array2string(w1, formatter={'float_kind':lambda w1: "%f" % w1})
    with open('training_data_690/w1.txt','w+') as f1:
        for j in W[0]:
            f1.write(' '.join(str(e) for e in j)) 
            f1.write('\n')
    with open('training_data_690/w2.txt','w+') as f2:
        for j in W[1]:
            f2.write(' '.join(str(e) for e in j)) 
            f2.write('\n')
    with open('training_data_690/w3.txt','w+') as f3:
        for j in W[2]:
            f3.write(' '.join(str(e) for e in j)) 
            f3.write('\n')
    with open('training_data_690/w4.txt','w+') as f4:
        for j in W[3]:
            f4.write(' '.join(str(e) for e in j)) 
            f4.write('\n') 
    with open('training_data_690/b1.txt','w+') as f5:
        for j in B[0]:
            f5.write(' '.join(str(e) for e in j))
            f5.write('\n') 
    with open('training_data_690/b2.txt','w+') as f6:
        for j in B[1]:
            f6.write(' '.join(str(e) for e in j))
            f6.write('\n') 
    with open('training_data_690/b3.txt','w+') as f7:
        for j in B[2]:
            f7.write(' '.join(str(e) for e in j)) 
            f7.write('\n')
    with open('training_data_690/b4.txt','w+') as f8:
        for j in B[3]:
            f8.write(' '.join(str(e) for e in j)) 
            f8.write('\n')
def test():
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
    (Z, cache)= affine_forward(A, W, b)
    print Z
    (dA, dW, db) = affline_backward(dZ, cache)
    print dW
    print db
#test()
#A = np.array([[1,2,3],[4,-5,6],[7,8,9]])
#print  np.inner(A[:,0], A[:,0])
expert_data = read_data('expert_policy.txt')
print np.mean(expert_data[:,0:5], axis=0)
expert_data[:,0:5]-= np.mean(expert_data[:,0:5], axis=0)
expert_data[:,0:5]/= np.std(expert_data[:,0:5], axis=0)
if not random_weight:
    w1 = read_data('training_data_690/w1.txt')
    w2 = read_data('training_data_690/w2.txt')
    w3 = read_data('training_data_690/w3.txt')
    w4 = read_data('training_data_690/w4.txt')
    b1 = read_data('training_data_690/b1.txt')
    b2 = read_data('training_data_690/b2.txt')
    b3 = read_data('training_data_690/b3.txt')
    b4 = read_data('training_data_690/b4.txt')
else:
    w1 = np.random.rand(feature_node_size, hidden_node_size)
    w2 = np.random.rand(hidden_node_size, hidden_node_size)
    w3 = np.random.rand(hidden_node_size, hidden_node_size)
    w4 = np.random.rand(hidden_node_size, outcome_size)
    s = (1,hidden_node_size)
    b1 = np.zeros(s)
    b2 = np.zeros(s)
    b3 = np.zeros(s)
    s = (1,outcome_size)
    b4 = np.zeros(s)
    w1 = w1 * weight
    w2 = w2 * weight
    w3 = w3 * weight
    w4 = w4 * weight
W = (w1, w2, w3, w4)
B = (b1, b2, b3, b4)
accuracy_trainingloss_array = []
accuracy_array = []
for e in range(epoch):
    loss_sum = 0
    loss = 0
    correctNum = 0
    print ('epoch:{0}'.format(e))
    np.random.shuffle(expert_data)
    if int(e) is int(epoch-1):
        test = True       
    for i in range(len(expert_data)/batch_size): 
        (X, y) = batch(i, expert_data)
        (loss, W, B, num) = three_network(X, W, B, y, test)
            #print num
        correctNum = correctNum + num
        loss_sum += loss
    print (loss_sum / float(len(expert_data)/batch_size))
    print correctNum / float(len(expert_data))
    accuracy_array.append(correctNum / float(len(expert_data)))
    loss_array.append(loss_sum / float(len(expert_data)/batch_size))
    #if e % 10 is 0:
    #    save_training_data(W, B)   
print confusion_matrix
with open('training_data/loss.txt','w+') as l:
    for loss in accuracy_array:
        l.write(str(loss))
        l.write('\n')
with open('training_data/Accuracy.txt','w+') as l:
    for a in accuracy_array:
        l.write(str(a))
        l.write('\n')