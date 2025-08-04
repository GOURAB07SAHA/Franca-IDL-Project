import random
import msvcrt
import os
import sys
import time

# Game settings
WIDTH = 40
HEIGHT = 20
SNAKE_CHAR = 'O'
FOOD_CHAR = '*'
EMPTY_CHAR = ' '

# Directions
UP = 'w'
DOWN = 's'
LEFT = 'a'
RIGHT = 'd'

class SnakeGame:
    def __init__(self):
        self.snake = [(WIDTH // 2, HEIGHT // 2)]  # List of (x, y) tuples
        self.food = self._place_food()
        self.direction = RIGHT
        self.score = 0

    def _place_food(self):
        while True:
            position = (random.randint(0, WIDTH - 1), random.randint(0, HEIGHT - 1))
            if position not in self.snake:
                return position

    def _move_snake(self):
        head_x, head_y = self.snake[0]
        if self.direction == UP:
            head_y -= 1
        elif self.direction == DOWN:
            head_y += 1
        elif self.direction == LEFT:
            head_x -= 1
        elif self.direction == RIGHT:
            head_x += 1

        new_head = (head_x, head_y)

        if new_head in self.snake or head_x < 0 or head_x >= WIDTH or head_y < 0 or head_y >= HEIGHT:
            return False
        
        self.snake.insert(0, new_head)

        if new_head == self.food:
            self.score += 1
            self.food = self._place_food()
        else:
            self.snake.pop()

        return True

    def _change_direction(self, key):
        if key == UP and self.direction != DOWN:
            self.direction = UP
        elif key == DOWN and self.direction != UP:
            self.direction = DOWN
        elif key == LEFT and self.direction != RIGHT:
            self.direction = LEFT
        elif key == RIGHT and self.direction != LEFT:
            self.direction = RIGHT

    def _get_key(self):
        if msvcrt.kbhit():
            return msvcrt.getch().decode('utf-8').lower()
        return None

    def _draw(self):
        os.system('cls' if os.name == 'nt' else 'clear')
        for y in range(HEIGHT):
            line = ''
            for x in range(WIDTH):
                if (x, y) in self.snake:
                    line += SNAKE_CHAR
                elif (x, y) == self.food:
                    line += FOOD_CHAR
                else:
                    line += EMPTY_CHAR
            print(line)
        print(f'Score: {self.score}')

    def play(self):
        while True:
            key = self._get_key()
            if key:
                self._change_direction(key)

            if not self._move_snake():
                print('Game Over!')
                print(f'Final Score: {self.score}')
                break

            self._draw()
            time.sleep(0.1)

if __name__ == '__main__':
    game = SnakeGame()
    game.play()
