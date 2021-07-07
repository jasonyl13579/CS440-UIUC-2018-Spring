import math
from bouncingBall import *
from movingPaddle import *
from DeepQAlgorithm import *
from QLearningAlgorithm import *

class pongAlgorithm(object):
    def __init__(self, choice):
        self.RESTRICTION=True
        self.ALPHA_VALUE=50
        self.GAMMA_VALUE=0.7
        self.TOLERANCE_THRESHOLD=0.05
        self.successCount_R = 0
        self.successCount_L = 0
        self.R_win = False
        self.L_win = False
        self.collisionFlag=False
        self.roundCount=0
        self.termination=False
        self.finish=False
        if(choice == 'Animation') :
            self.ROUND_TOTAL = 1
        elif(choice == 'Statistics') :
            self.ROUND_TOTAL = 1000
        
        self.win_number_R = [0] * self.ROUND_TOTAL
        self.win_number_L = [0] * self.ROUND_TOTAL
        self.scores_R = [0] * self.ROUND_TOTAL
        self.scores_L = [0] * self.ROUND_TOTAL
        self.bouncingBall=bouncingBall()
        self.movingPaddle_R=movingPaddle('AI_R')
        self.movingPaddle_L = movingPaddle('AI_L')
        
        #Left AI
        self.epoch = 1
        self.num_layer = 4
        self.batchSize = 1
        self.AI_L=DeepQAlgorithm(self.epoch,self.num_layer,self.batchSize)  
        self.AI_L.readWeights()
        
        #Right AI
        self.AI_R=QLearningAlgorithm(self.ALPHA_VALUE,self.GAMMA_VALUE,self.TOLERANCE_THRESHOLD)    
        self.readWeight()
        
#        self.gameState = (self.bouncingBall.ball_x,self.bouncingBall.ball_y,self.bouncingBall.ball_velocity_x,self.bouncingBall.ball_velocity_y,self.movingPaddle_R.paddle_y,self.movingPaddle_L.paddle_y)
        
    def ending(self):
        
        if self.R_win:
            self.win_number_R[self.roundCount] = 1
            self.R_win = False
        elif self.L_win:
            self.win_number_L[self.roundCount] = 1
            self.L_win = False
        
        self.scores_R[self.roundCount] = self.successCount_R
        self.scores_L[self.roundCount] = self.successCount_L
        self.successCount_R = 0
        self.successCount_L = 0
        self.roundCount+=1
        self.termination=True
        
        if self.RESTRICTION:
            if self.roundCount==self.ROUND_TOTAL:
                self.finish=True
    def boundaryCondition(self):
        if self.bouncingBall.ball_x>self.movingPaddle_R.paddle_x:
            if self.bouncingBall.ball_y>self.movingPaddle_R.paddle_y and self.bouncingBall.ball_y<self.movingPaddle_R.paddle_y+self.movingPaddle_R.paddle_height:
                self.successCount_R+=1
                self.collisionFlag=True
                self.bouncingBall.hit()
            else:
                self.L_win = True
                self.ending()
        elif self.bouncingBall.ball_x < self.movingPaddle_L.paddle_x:
            if self.bouncingBall.ball_y > self.movingPaddle_L.paddle_y and self.bouncingBall.ball_y < self.movingPaddle_L.paddle_y + self.movingPaddle_L.paddle_height:
                self.successCount_L+=1
                self.collisionFlag = True
                self.bouncingBall.hit2()
            else:
                self.R_win = True
                self.ending()
        
    def updateCondition(self):
        if self.termination:
            gameState = [[12,12,12,12,12],[-1,-1,-1,-1,-1]]
            return gameState
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
                
            moving_paddle_discrete=int(math.floor(12*self.movingPaddle_R.paddle_y/(1-self.movingPaddle_R.paddle_height)))
            if moving_paddle_discrete>11:
                moving_paddle_discrete=11
            gameState = []
            gameState.append([ball_x_discrete,ball_y_discrete,velocity_x,velocity_y,moving_paddle_discrete])
            gameState.append([(1-self.bouncingBall.ball_x),self.bouncingBall.ball_y,-1*self.bouncingBall.ball_velocity_x,self.bouncingBall.ball_velocity_y,self.movingPaddle_L.paddle_y])
#            self.gameState = (self.bouncingBall.ball_x,self.bouncingBall.ball_y,self.bouncingBall.ball_velocity_x,self.bouncingBall.ball_velocity_y,self.movingPaddle_R.paddle_y,self.movingPaddle_L.paddle_y)
            return gameState
        
    def updateFunction(self):
        self.boundaryCondition()
        gameState=self.updateCondition()
        
        if self.termination:
            self.bouncingBall = bouncingBall()
            self.movingPaddle_R = movingPaddle('AI_R')
            self.movingPaddle_L = movingPaddle('AI_L')
            self.termination=False
            return
        gameState=self.updateCondition()
        action_R = self.AI_R.actionChoice((gameState[0][0],gameState[0][1],gameState[0][2],gameState[0][3],gameState[0][4]))
        action_L = self.AI_L.actionChoice(gameState[1])
        self.movingPaddle_R.updateCondition(action_R)
        self.movingPaddle_L.updateCondition(action_L)
        self.bouncingBall.update()
        
    def readWeight(self):
        with open('QValue.txt','r') as f:
            for index in f:
                index=index.strip().split(' ')
                self.AI_R.qValue[((int(index[0]),int(index[1]),int(index[2]),int(index[3]),int(index[4])),int(index[5]))]=float(index[6])

