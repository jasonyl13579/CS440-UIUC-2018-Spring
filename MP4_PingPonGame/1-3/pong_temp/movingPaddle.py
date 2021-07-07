import random
from bouncingBall import *

class movingPaddle(object):
    def __init__(self,strategy):
        self.paddle_height=0.2
        self.paddle_x=1.0
        self.paddle_y=0.5-self.paddle_height/2
        self.paddle_velocity=0.04
        self.strategy=strategy
        if strategy=='User':
            self.paddle_x=0.0
    def boundaryCondition(self):
        if(self.paddle_y<0):
            self.paddle_y=0
        if(self.paddle_y>1-self.paddle_height):
            self.paddle_y=1-self.paddle_height
    def user(self,ball_y):
        if ball_y<self.paddle_y+self.paddle_height/2:
            self.paddle_y-=0.02
        elif ball_y>self.paddle_y+self.paddle_height/2:
            self.paddle_y+=0.02
        self.boundaryCondition()
    def randomChoice(self):
        self.paddle_y+=random.choice([-0.04,0.0,0.04])
        self.boundaryCondition()
    def qlearning(self,actions):
        self.paddle_y+=actions*0.04
        self.boundaryCondition()
    def updateCondition(self,ball_position):
        if self.strategy=='QLearning':
            self.qlearning(ball_position)
        elif self.strategy=='User':
            self.user(ball_position)
        else:
            self.randomChoice()
    
        