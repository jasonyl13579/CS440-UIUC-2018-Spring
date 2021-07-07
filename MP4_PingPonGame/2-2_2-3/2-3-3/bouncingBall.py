import random

PLAYER = 2

class bouncingBall(object):
	""" ball class """
	def __init__(self):
		self.ball_x = 0.5
		self.ball_y = 0.5
		self.ball_velocity_x = 0.03
		self.ball_velocity_y = 0.01

	def bounce(self):
		if self.ball_y < 0:
			self.ball_y = -self.ball_y
			self.ball_velocity_y = -self.ball_velocity_y
		elif self.ball_y > 1:
			self.ball_y = 2 - self.ball_y
			self.ball_velocity_y = -self.ball_velocity_y
		if PLAYER == 1:
			if self.ball_x < 0:
				self.ball_x = -self.ball_x
				self.ball_velocity_x = -self.ball_velocity_x

	def hit(self):
		self.ball_x = 2 - self.ball_x
		U = random.uniform(-0.015, 0.015)
		V = random.uniform(-0.03, 0.03)
		self.ball_velocity_x = -self.ball_velocity_x + U
		self.ball_velocity_y += V
		if self.ball_velocity_x < 0:
			self.ball_velocity_x = max(-1.0, min(-0.03, self.ball_velocity_x))
		elif self.ball_velocity_x > 0:
			self.ball_velocity_y = min(1.0, max(0.03, self.ball_velocity_y))
		if self.ball_velocity_y < 0:
			self.ball_velocity_y = max(-1.0, self.ball_velocity_y)
		elif self.ball_velocity_y > 0:
			self.ball_velocity_y = min(1.0, self.ball_velocity_y)

	def hit2(self):
		self.ball_x = -self.ball_x
		U = random.uniform(-0.015, 0.015)
		V = random.uniform(-0.03, 0.03)
		self.ball_velocity_x = -self.ball_velocity_x + U
		self.ball_velocity_y += V
		if self.ball_velocity_x < 0:
			self.ball_velocity_x = max(-1.0, min(-0.03, self.ball_velocity_x))
		elif self.ball_velocity_x > 0:
			self.ball_velocity_y = min(1.0, max(0.03, self.ball_velocity_y))
		if self.ball_velocity_y < 0:
			self.ball_velocity_y = max(-1.0, self.ball_velocity_y)
		elif self.ball_velocity_y > 0:
			self.ball_velocity_y = min(1.0, self.ball_velocity_y)

	def update(self):
		self.ball_x += self.ball_velocity_x
		self.ball_y += self.ball_velocity_y
		self.bounce()

		