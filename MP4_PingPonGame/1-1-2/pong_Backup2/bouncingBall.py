import random

class bouncingBall(object):
    def __init__(self):
        self.ball_x=0.5
        self.ball_y=0.5
        self.ball_velocity_x=0.03
        self.ball_velocity_y=0.01
        self.paddle_location=1
    def updateCondition(self):
        self.ball_x+=self.ball_velocity_x
        self.ball_y+=self.ball_velocity_y
        if self.ball_x<0:
            self.ball_x=-self.ball_x
            self.ball_velocity_x=-self.ball_velocity_x
        if self.ball_y<0:
            self.ball_y=-self.ball_y
            self.ball_velocity_y=-self.ball_velocity_y
        if self.ball_y>1:
            self.ball_y=2-self.ball_y
            self.ball_velocity_y=-self.ball_velocity_y
    def successfulCollision(self):
        self.ball_x=2*self.paddle_location-self.ball_x
        self.ball_velocity_x=-self.ball_velocity_x+random.uniform(-0.015,0.015)
        self.ball_velocity_y+=random.uniform(-0.03,0.03)
        if self.ball_velocity_x<0:
            if self.ball_velocity_x>-0.03:
                self.ball_velocity_x=-0.03
            if self.ball_velocity_x<-1:
                self.ball_velocity_x=-1
        elif self.ball_velocity_x>0:
            if self.ball_velocity_x<0.03:
                self.ball_velocity_x=0.03
            if self.ball_velocity_x>1:
                self.ball_velocity_x=1
        if self.ball_velocity_y<0:
            if self.ball_velocity_y<-1:
                self.ball_velocity_y=-1
            if self.ball_velocity_y>1:
                self.ball_velocity_y=1
            