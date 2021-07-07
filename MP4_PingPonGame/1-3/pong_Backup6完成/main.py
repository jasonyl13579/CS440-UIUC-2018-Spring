import pygame, sys
from pygame.locals import *
import atexit
import signal
import sys
import matplotlib.pyplot as plt
from pongAlgorithm import *

PLAYER = 2
MODE = 'play'
game = pongAlgorithm()


def plot():
	with open('weight.txt', 'w+') as weightFile:
		for key in game.qlearning.q.keys():
			weightFile.write(str(key[0][0]) + ' ' +str(key[0][1]) + ' ' + str(key[0][2]) + ' ' + str(key[0][3]) + ' ' + str(key[0][4]) + ' ')
			weightFile.write(str(key[1]) + ' ')
			weightFile.write(str(game.qlearning.q[key]) + '\n')
"""
	line_x = [0, ROUND]
	line_y = [9,9]
	f,ax = plt.subplots()
	ax.plot(game.x, game.y, 'b', label='Average Bounce')
	ax.plot(game.x, game.winrate, 'g', label='Winning Rate')
	ax.plot(line_x, line_y, 'r', label='9')
	plt.ylabel('average number of rebounce')
	plt.xlabel('the number of games')
	plt.title('Alpha = ' + str(ALPHA) + '/(' + str(ALPHA) + '+N(s,a)), Gamma = ' + str(GAMMA) + ', Epsilon = ' + str(EPSILON))
	plt.legend(loc='lower right')
	plt.ylim(0,12)
	# plt.xlim(0,100000)
	plt.grid(True)
	plt.show()
"""
def sigint_handler(signum, frame):
    plot()
 
signal.signal(signal.SIGINT, sigint_handler)

FPS = 20
WINDOWWIDTH = 700
WINDOWHEIGHT = 700
THICKNESS = 10
PADDLESIZE = 100
WHITE = (255,255,255)
BLACK = (0,0,0)
BLUE = (0,0,255)

def plotEnvironment():
	DISPLAYSURF.fill(WHITE)

def plotBall(ball, game):
	ball.x = scaleBall(game.bouncingBall.x)
	ball.y = scaleBall(game.bouncingBall.y)
	pygame.draw.rect(DISPLAYSURF, BLUE, ball)

def plotPaddle1(paddle, game):
	paddle.y = scaleBall(game.paddle1.y)
	pygame.draw.rect(DISPLAYSURF, BLACK, paddle)

def plotPaddle2(paddle, game):
	paddle.y = scaleWall(game.paddle2.y)
	pygame.draw.rect(DISPLAYSURF, BLACK, paddle)

def plotWall(wall):
	pygame.draw.rect(DISPLAYSURF, BLACK, wall)

def scaleBall(unit):
	return 700*unit - THICKNESS/2

def scalePaddle(unit):
	return 700*unit - THICKNESS

def scaleWall(unit):
	return 700*unit

if __name__=='__main__':
	if MODE == 'train':
		while True:
			game.update()
			if game.finish:
				plot()
				
	elif MODE == 'play':
		# initial pygame and surface
		pygame.init()
		global DISPLAYSURF
		
		# set up screen
		FPSCLOCK = pygame.time.Clock()
		DISPLAYSURF = pygame.display.set_mode((WINDOWWIDTH,WINDOWHEIGHT)) 
		pygame.display.set_caption('Pong game')
		
		# ball
		ball_x = scaleBall(game.state[0])
		ball_y = scaleBall(game.state[1])
		ball = pygame.Rect(ball_x, ball_y, THICKNESS, THICKNESS)
		# paddle1
		paddle_x = scalePaddle(1.0)
		paddle_y = scalePaddle(game.state[4])
		paddle_height = scaleWall(game.paddle1.height)
		paddle = pygame.Rect(paddle_x, paddle_y, THICKNESS, paddle_height)
		# paddle2
		paddle2_x = scaleWall(0.0)
		paddle2_y = scaleWall(game.paddle2.y)
		paddle2_height = scaleWall(game.paddle2.height)
		paddle2 = pygame.Rect(paddle2_x, paddle2_y, THICKNESS, paddle2_height)
		# wall
		wall_x = scaleWall(0.0)
		wall_y = scaleWall(0.0)
		wall = pygame.Rect(wall_x, wall_y, THICKNESS, WINDOWHEIGHT)

		# plot game
		plotEnvironment()
		plotBall(ball, game)
		plotPaddle1(paddle, game)
		plotPaddle2(paddle2, game)
		# plotWall(wall)

		while True: # main game loop
			for event in pygame.event.get():
				# exit game
				if event.type == QUIT:
					pygame.quit()
					sys.exit()

			# update game
			game.update()
			# if game.termination:
			# 	print game.success
			# 	print game.lose
			# 	game = pong()
			plotEnvironment()
			plotBall(ball, game)
			# plotWall(wall)
			plotPaddle1(paddle, game)
			plotPaddle2(paddle2, game)


			# update the screen
			pygame.display.update()
			FPSCLOCK.tick(FPS)



