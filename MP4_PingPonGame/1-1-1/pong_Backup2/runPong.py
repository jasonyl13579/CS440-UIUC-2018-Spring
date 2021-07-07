import matplotlib.pyplot as plt
import sys
import signal
import pygame
import atexit
from pygame.locals import *
from pongAlgorithm import *

algorithmChoice='game'
pongGame=pongAlgorithm()
pongGame.readWeight()

def plotGraph():
    with open('QValue.txt','w+') as f:
        for valuePair in pongGame.QLearningAlgorithm.qValue.keys():
            f.write(str(valuePair[0][0])+' '+str(valuePair[0][1])+' '+str(valuePair[0][2])+' '+str(valuePair[0][3])+' '+str(valuePair[0][4])+' '+str(valuePair[1])+' '++str(pongGame.QLearningAlgorithm.qValue[valuePair])+'\n')
        
        