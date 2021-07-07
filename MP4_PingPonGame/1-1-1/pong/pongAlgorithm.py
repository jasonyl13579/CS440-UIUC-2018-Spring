import math
from bouncingBall import *
from movingPaddle import *
from QLearningAlgorithm import *

class pongAlgorithm(object):
    def __init__(self):
        self.RESTRICTION=True
        self.OPTIMIZAION=False
        self.ALPHA_VALUE=50
        self.GAMMA_VALUE=0.7
        self.TOLERANCE_THRESHOLD=0.05
        self.ROUND_TOTAL=100000
        self.previousState=None
        self.previousAction=None
        self.successCount=0
        self.loseCount=0
        self.collisionFlag=False
        self.overallScores=[]
        self.roundCount=0
        self.termination=False
        self.finish=False
        self.roundRecord=[0]
        self.totalScoresRecord=[0]
        self.currentScore=0    
        self.bouncingBall=bouncingBall()
        self.movingPaddle=movingPaddle('QLearning')
        self.QLearningAlgorithm=QLearningAlgorithm(self.ALPHA_VALUE,self.GAMMA_VALUE,self.TOLERANCE_THRESHOLD)    
        self.gameState=(self.bouncingBall.ball_x,self.bouncingBall.ball_y,self.bouncingBall.ball_velocity_x,self.bouncingBall.ball_velocity_y,self.movingPaddle.paddle_y)

    def ending(self):
        if len(self.overallScores)==1000:
            self.overallScores=self.overallScores[1:]
        totalScore=0
        self.overallScores.append(self.currentScore)
        self.currentScore=0
        self.loseCount+=1
        self.roundCount+=1
        self.termination=True
        if self.roundCount%1000==0:
            totalScore=float(sum(self.overallScores))/1000.0
            self.roundRecord.append(self.roundCount)
            self.totalScoresRecord.append(totalScore)
        if self.RESTRICTION:
            if self.roundCount==self.ROUND_TOTAL:
                self.finish=True
        if self.OPTIMIZAION:
            if totalScore>9.0:
                self.finish=True
    def boundaryCondition(self):
        if self.bouncingBall.ball_x>self.movingPaddle.paddle_x:
            if self.bouncingBall.ball_y>self.movingPaddle.paddle_y and self.bouncingBall.ball_y<self.movingPaddle.paddle_y+self.movingPaddle.paddle_height:
                self.successCount+=1
                self.currentScore+=1
                self.collisionFlag=True
                self.bouncingBall.successfulCollision()
            else:
                self.ending()

    def updateCondition(self):
        if self.termination:
            return (12,12,12,12,12)
        else:
            if self.bouncingBall.ball_velocity_x>0:
                velocity_x=1
            else:
                velocity_x=-1
            if self.bouncingBall.ball_velocity_y>=0.015:
                velocity_y=1
            elif self.bouncingBall.ball_velocity_y<=0.015:
                velocity_y=-1
            else:
                velocity_y=0
            ball_x_discrete=int(math.floor(12*self.bouncingBall.ball_x))
            if ball_x_discrete>11:
                ball_x_discrete=11
            
            ball_y_discrete=int(math.floor(12*self.bouncingBall.ball_y))
            if ball_y_discrete>11:
                ball_y_discrete=11
                
            moving_paddle_discrete=int(math.floor(12*self.movingPaddle.paddle_y/(1-self.movingPaddle.paddle_height)))
            if moving_paddle_discrete>11:
                moving_paddle_discrete=11
            return (ball_x_discrete,ball_y_discrete,velocity_x,velocity_y,moving_paddle_discrete)
        
    def updateFunction(self):
        self.boundaryCondition()
        gameState=self.updateCondition()
        currentReward=0.0
        
        if self.termination:
            currentReward=-1000.0
            if self.previousState is not None:
                self.QLearningAlgorithm.learning(self.previousState,self.gameState,currentReward,self.previousAction)
            self.previousState=None
            self.bouncingBall=bouncingBall()
            self.movingPaddle=movingPaddle('QLearning')
            self.termination=False
            return
        if self.collisionFlag:
            self.collisionFlag=False
            currentReward=1000.0
        if self.previousState is not None:
            self.QLearningAlgorithm.learning(self.previousState,self.gameState,currentReward,self.previousAction)
        gameState=self.updateCondition()
        action=self.QLearningAlgorithm.actionChoice(gameState)
        #action=self.QLearningAlgorithm.randomChoice()
        self.previousState=gameState
        self.previousAction=action
        self.movingPaddle.updateCondition(action)
        self.bouncingBall.updateCondition()
    
    def readWeight(self):
        with open('QValue.txt','r') as f:
            for index in f:
                index=index.strip().split(' ')
                self.QLearningAlgorithm.qValue[((int(index[0]),int(index[1]),int(index[2]),int(index[3]),int(index[4])),int(index[5]))]=float(index[6])

