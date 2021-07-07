import random
from bouncingBall import *

class movingPaddle(object):
    def __init__(self,strategy):
        if strategy == 'AI_R':
            self.paddle_x=1.0
        elif strategy == 'AI_L':
            self.paddle_x=0
            
        self.paddle_height=0.2
        self.paddle_y=0.5-self.paddle_height/2
        self.paddle_velocity=0.04
        self.strategy=strategy
    def boundaryCondition(self):
        if(self.paddle_y<0):
            self.paddle_y=0
        if(self.paddle_y>1-self.paddle_height):
            self.paddle_y=1-self.paddle_height
#    def randomChoice(self):
#        self.paddle_y+=random.choice([-0.04,0.0,0.04])
#        self.boundaryCondition()
    def qlearning(self,actions):
        self.paddle_y+=actions*0.04
        self.boundaryCondition()
    def updateCondition(self,ball_position):
        self.qlearning(ball_position)
    
        