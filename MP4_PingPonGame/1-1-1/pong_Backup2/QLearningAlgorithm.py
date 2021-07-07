import random
import numpy as np

class QLearningAlgorithm(object):
    def __init__(self,alphaValue,gammaValue,toleranceThreshold):
        self.qValue={}
        self.nValue={}
        self.actionChoices=[-1.0,0.0,1.0]
        self.alphaValue=alphaValue
        self.gammaValue=gammaValue
        self.toleranceThreshold=toleranceThreshold
    def QLearning(self,reward,value,state,action):
        if (state,action) not in self.nValue:
            self.nValue[(state,action)]=1
        else:
            self.nValue[(state,action)]+=1
        if self.qValue.get((state,action),None) is None:
            self.qValue[(state,action)]=reward
        else:
            self.qValue[(state,action)]=self.qValue.get((state,action),None)+float(self.alphaValue)/float(self.alphaValue+self.nValue[(state,action)])*(value-self.qValue.get((state,action),None))
    def randomChoice(self):
        return random.choice([-1.0,0.0,1.0])
    def actionChoice(self,state): #may have problems
        if random.random()>self.toleranceThreshold:
            Q=[self.qValue.get((state,action),0.0) for action in self.actionChoices]
            return self.actionChoices[Q.index(max(Q))]
        else:
            return random.choice(self.actionChoices)
    def learning(self,state_old,state_new,reward,action):
        QValueUpdate=max([self.qValue.get((state_new,action),0.0) for action in self.actionChoices])
        self.QLearning(reward,reward+self.gammaValue*QValueUpdate,state_old,action)
        