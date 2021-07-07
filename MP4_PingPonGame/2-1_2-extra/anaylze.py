import random
import numpy as np
import math
import matplotlib.pyplot as plt

filepath = "training_data_split"
def read_data(filename):
    data = []
    with open(filename,'r+') as f:
        for index in f:
            index = index.strip().split(' ')           
            temp = []
            for i in index: 
                temp.append(float(i))              
            data.append(temp)
    return data
def plot_loss():
    loss = read_data(filepath + '/loss_1_2000.txt')
    plt.plot(loss)
    plt.xlabel('epoch')
    plt.ylabel('loss')
    plt.show()
def plot_accuracy():
    accuracy = read_data(filepath + '/accuracy_training.txt')
    validation = read_data(filepath + '/accuracy_validation.txt')
    plt.plot(accuracy)
    plt.title('training accuracy with dropout')
   # plt.plot(validation)
    plt.xlabel('epoch')
    plt.ylabel('accuracy(%)')
    plt.show()
plot_accuracy()