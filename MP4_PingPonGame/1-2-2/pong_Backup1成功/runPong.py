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
        for valuePair in pongGame.SarsaAlgorithm.qValue.keys():
            f.write(str(valuePair[0][0])+' '+str(valuePair[0][1])+' '+str(valuePair[0][2])+' '+str(valuePair[0][3])+' '+str(valuePair[0][4])+' '+str(int(float(valuePair[1])))+' '+str(pongGame.SarsaAlgorithm.qValue[valuePair])+'\n')
       
    with open('round.txt','w+') as r:
        r.write(' '.join([str(index) for index in pongGame.roundRecord]))

    with open('totalScore.txt','w+') as t:
        t.write(' '.join([str(index) for index in pongGame.totalScoresRecord]))
"""
    x_axis=[0,pongGame.ROUND_TOTAL]
    y_axis=[9,9]
    figure,axises=plt.subplots()
    axises.plot(pongGame.roundRecord,pongGame.totalScoresRecord,'b',label='Bounce number average')
    axises.plot(x_axis,y_axis,'r',label='9')
    plt.xlabel('Current round of games')
    plt.ylabel('Bounce number average')
    plt.title('Alpha_value='+str(pongGame.ALPHA_VALUE)+', Gamma_value'+str(pongGame.GAMMA_VALUE)+', Epsilon ='+str(pongGame.TOLERANCE_THRESHOLD))
    plt.legend(loc='lower right')
    plt.ylim(0,14)
    plt.grid(True)
    plt.show()
"""     
def frame_display(signum, frame):
    plotGraph()
 
signal.signal(signal.SIGINT, frame_display)

FPS=200

WIDTH=700
HEIGHT=700

THICKNESS=10
PADDLE_NUMBER=100

WHITE_COLOR=(255,255,255)
BLACK_COLOR=(0,0,0)
BLUE_COLOR=(0,0,255)

def plotEnvironment():
	DISPLAYSURF.fill(WHITE_COLOR)

def plotBall(ballPosition, pongGame):
	ballPosition.x = resizeBall(pongGame.bouncingBall.ball_x)
	ballPosition.y = resizeBall(pongGame.bouncingBall.ball_y)
	pygame.draw.rect(DISPLAYSURF, BLUE_COLOR, ballPosition)

def plotPaddle(paddlePosition, pongGame):
	paddlePosition.y = resizeBall(pongGame.movingPaddle.paddle_y)
	pygame.draw.rect(DISPLAYSURF, BLACK_COLOR, paddlePosition)

def plotWall(walls):
	pygame.draw.rect(DISPLAYSURF, BLACK_COLOR, walls)

def resizeBall(measurement):
	return 700*measurement + THICKNESS/2

def resizePaddle(measurement):
	return 700*measurement + THICKNESS

def resizeWall(measurement):
	return 700*measurement-THICKNESS

if __name__=='__main__':
	if algorithmChoice == 'practicing':
		while True:
			pongGame.updateFunction()
			if pongGame.finish:
				plotGraph()

	elif algorithmChoice=='game':
		pygame.init()
		global DISPLAYSURF
		clockTime=pygame.time.Clock()
		DISPLAYSURF=pygame.display.set_mode((WIDTH,HEIGHT))
		pygame.display.set_caption('Pong game')
        
		ball_x=resizeBall(pongGame.gameState[0])
		ball_y=resizeBall(pongGame.gameState[1])
		ball=pygame.Rect(ball_x,ball_y,THICKNESS,THICKNESS)
		paddle_x=resizePaddle(0.0)
		paddle_y=resizePaddle(pongGame.gameState[4])
		paddle_height=resizeWall(pongGame.movingPaddle.paddle_height)
		paddle=pygame.Rect(paddle_x,paddle_y,THICKNESS,paddle_height)
		wall_x=resizeWall(1.0)
		wall_y=resizeWall(0.0)
		wall=pygame.Rect(wall_x,wall_y,THICKNESS,HEIGHT)
        
		plotEnvironment()
		plotBall(ball,pongGame)
		plotPaddle(paddle,pongGame)
		plotWall(wall)

		while True:
			for e in pygame.event.get():
				if e.type==QUIT:
					pygame.quit()
					sys.exit()
			pongGame.updateFunction()
			plotEnvironment()
			plotBall(ball,pongGame)
			plotWall(wall)
			plotPaddle(paddle,pongGame)

			pygame.display.update()
			clockTime.tick(FPS)







