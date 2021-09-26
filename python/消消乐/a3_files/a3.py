"""
CSSE1001 Assignment 3
Semester 2, 2017
"""

# There are a number of jesting comments in the support code
# They should not be taken seriously. Keep it fun folks :D
# Students are welcome to add their own source code humour, provided it remains civil

import tkinter as tk
from tkinter.messagebox import showinfo
from tkinter import filedialog
import os
import random
import pygame
import pickle

try:
    from PIL import ImageTk, Image

    HAS_PIL = True
except ImportError:
    HAS_PIL = False

from view import GridView, ObjectivesView
from game import DotGame, ObjectiveManager, CompanionGame
from dot import BasicDot, WildcardDot
from util import create_animation, ImageManager
from companion import AbstractCompanion

# Fill these in with your details
__author__ = "Xiaowei Zhang (s4425003)"
__email__ = "zxw971118@gmail.com"
__date__ = "27/10/2017"
__version__ = "1.1.2"


def load_image_pil(image_id, size, prefix, suffix='.png'):
    """Returns a tkinter photo image

    Parameters:
        image_id (str): The filename identifier of the image
        size (tuple<int, int>): The size of the image to load
        prefix (str): The prefix to prepend to the filepath (i.e. root directory
        suffix (str): The suffix to append to the filepath (i.e. file extension)
    """
    width, height = size
    file_path = os.path.join(prefix, f"{width}x{height}", image_id + suffix)
    return ImageTk.PhotoImage(Image.open(file_path))


def load_image_tk(image_id, size, prefix, suffix='.gif'):
    """Returns a tkinter photo image

    Parameters:
        image_id (str): The filename identifier of the image
        size (tuple<int, int>): The size of the image to load
        prefix (str): The prefix to prepend to the filepath (i.e. root directory
        suffix (str): The suffix to append to the filepath (i.e. file extension)
    """
    width, height = size
    file_path = os.path.join(prefix, f"{width}x{height}", image_id + suffix)
    return tk.PhotoImage(file=file_path)


# This allows you to simply load png images with PIL if you have it,
# otherwise will default to gifs through tkinter directly
load_image = load_image_pil if HAS_PIL else load_image_tk  # pylint: disable=invalid-name

DEFAULT_ANIMATION_DELAY = 0  # (ms)
ANIMATION_DELAYS = {
    # step_name => delay (ms)
    'ACTIVATE_ALL': 50,
    'ACTIVATE': 100,
    'ANIMATION_BEGIN': 300,
    'ANIMATION_DONE': 0,
    'ANIMATION_STEP': 200
}


# Define your classes here
class BuffaloCompanion(AbstractCompanion):
    """The function of the buffaloCompanion"""

    def activate(self, game):
        """Randomly places a few wildcard dots on the grid
            Parameters:
                    game(DotGame): the playing game"""

        cell = [(0, 0), (0, 1), (0, 2), (0, 3), (0, 4),
                (0, 5), (0, 6), (1, 0), (1, 1),
                (1, 2), (1, 3), (1, 4), (1, 5), (1, 6),
                (2, 0), (2, 1), (2, 5), (2, 6),
                (2, 7), (3, 0), (3, 1), (3, 5),
                (3, 6), (3, 7), (4, 0), (4, 1), (4, 5), (4, 6),
                (4, 7), (5, 0), (5, 1), (5, 2), (5, 3), (5, 4),
                (5, 5), (5, 6), (5, 7), (6, 0), (6, 1), (6, 2),
                (6, 3), (6, 4), (6, 5), (6, 6), (7, 0), (7, 1), (7, 2),
                (7, 3), (7, 4), (7, 5), (7, 6)]
        random_cell = random.sample(cell, 3)
        for i in random_cell:
            position = i
            game.grid[position].set_dot(WildcardDot())

    def get_charge(self):
        """Get charge"""
        return self._charge


class CompanionDot(BasicDot):
    """The companion dot in companion game"""
    DOT_NAME = "companion"

    def activate(self, position, game, activated, has_loop=False):
        """When activate companion dot then charge it
            parameter:
                game(CompanionGame) the playing game"""
        game.companion.charge()


class IntervalBar(tk.Canvas):
    """The Bar shows the progress of the game """

    def __init__(self, parent):
        """Set the width and height for the canvas"""
        super().__init__(parent, width=310, height=30)

        self.layout()

    def rectangle(self, step):
        """Create the blue rectangle in each step of charges"""
        for m in range(step):
            squars = self.create_rectangle(50 * m, 0, 50 + 50 * m, 25, fill='blue')
            self.move(squars, 2, 2)

    def layout(self):
        """The layout of the interval bar"""
        for m in range(6):
            layout = self.create_rectangle(50 * m, 0, 50 + 50 * m, 25)
            self.move(layout, 2, 2)


class InfoPanel(tk.Frame):
    """A IntervalBar shows remaining moves,scores, objects and companion image"""

    def __init__(self, master, parent):
        """Set the InfoPanel in the master window

                Parameters:
                    master(tk.Tk): the main window
                    parent: can command the method in master class
                """
        super().__init__(master)

        self._image_manager = ImageManager('images/dots/', loader=load_image)

        counts = [10, 15, 25, 25]
        random.shuffle(counts)
        objectives = zip([BasicDot(1), BasicDot(2), BasicDot(4), BasicDot(3)], counts)

        self._objectives = ObjectiveManager(list(objectives))
        photo = tk.PhotoImage(file='images/companions/giphy_s.gif')
        self._frame = tk.Frame(self)
        self._music_buttom = tk.Button(self, text="Music", command=parent.open_music).pack()

        self._image1 = tk.Label(self._frame, image=photo)
        self._scores = tk.Label(self._frame, text='0', font=(None, 30), fg='grey', width=3)
        self._image1.image = photo
        self._moves = tk.Label(self, text='20', font=(None, 30), width=4)
        self._moves.pack(expand=1, side=tk.LEFT, anchor=tk.NW, pady=10)
        self._frame.pack(side=tk.LEFT)
        self._scores.pack(side=tk.LEFT, anchor=tk.S, pady=10)
        self._image1.pack(expand=1, side=tk.LEFT)
        self._object_view = ObjectivesView(self, image_manager=self._image_manager)

        self._object_view.draw(self._objectives.get_status())
        self._object_view.pack(expand=1, side=tk.LEFT, anchor=tk.E)

    def set_score(self, score):
        """Change the store label in every step"""
        self._scores.config(text=score)

    def set_objectives(self, objectives):
        """Change the objects in status"""
        self._object_view.draw(objectives)

    def move_changed(self, move):
        """Change moves label in every step"""
        self._moves.config(text=move)


class DotsApp:
    """Top level GUI class for simple Dots & Co game"""

    def __init__(self, master):
        """Constructor

        Parameters:
            master (tk.Tk|tk.Frame): The parent widget
        """
        self._master = master
        master.protocol("WM_DELETE_WINDOW", self.exit_game)
        self._master.title("Dots & Co")
        self._master.geometry("550x900")

        menubar = tk.Menu(self._master)

        self._master.config(menu=menubar)
        filemenu = tk.Menu(menubar)
        menubar.add_cascade(label="File", menu=filemenu)
        filemenu.add_command(label="New Basic Game", command=self.reset)
        filemenu.add_command(label="New Companion Game", command=self.companion_game)
        filemenu.add_command(label="Save", command=self.save_game)
        filemenu.add_command(label="Load", command=self.load_game)
        filemenu.add_command(label="Exit", command=self.exit_game)
        self._infopanel = InfoPanel(master, self)
        self._infopanel.pack(fill=tk.X)

        self._interval = IntervalBar(master)
        self._interval.pack()

        self._playing = True
        self._image_manager = ImageManager('images/dots/', loader=load_image)
        self._step = -1

        # Game
        counts = [10, 15, 25, 25]
        random.shuffle(counts)
        # randomly pair counts with each kind of dot
        objectives = zip([BasicDot(1), BasicDot(2), BasicDot(4), BasicDot(3)], counts)

        self._objectives = ObjectiveManager(list(objectives))
        self._companion = BuffaloCompanion()

        # Game
        self._dead_cells = {(2, 2), (2, 3), (2, 4),
                            (3, 2), (3, 3), (3, 4),
                            (4, 2), (4, 3), (4, 4),
                            (0, 7), (1, 7), (6, 7), (7, 7)}
        self._game = DotGame({BasicDot: 1}, objectives=self._objectives, kinds=(1, 2, 3, 4), size=(8, 8),
                             dead_cells=self._dead_cells)

        self._music = True

        self._basic_game = True

        # Grid View
        self._grid_view = GridView(master, size=self._game.grid.size(), image_manager=self._image_manager)
        self._grid_view.pack()
        self._grid_view.draw(self._game.grid)

        self.draw_grid_borders()

        # Events
        self.bind_events()

        # Set initial score again to trigger view update automatically
        self._refresh_status()
        self._music_status = 0

        pygame.mixer.init()

    def companion_game(self):
        """Change the game mode to companion game"""
        self._basic_game = False
        self._game = CompanionGame({BasicDot: 0.8, CompanionDot: 0.2}, companion=self._companion,
                                   objectives=self._objectives, kinds=(1, 2, 3, 4), size=(8, 8),
                                   dead_cells=self._dead_cells)
        self.bind_events()
        self.reset_progress()

    def exit_game(self):
        """Exit the game"""
        ans = tk.messagebox.askokcancel('Confirm Exit', 'Are you sure you want to quit?')

        if ans:
            self._master.destroy()
            if self._music_status == 1:
                self.close_music()

    def draw_grid_borders(self):
        """Draws borders around the game grid"""

        borders = list(self._game.grid.get_borders())

        # this is a hack that won't work well for multiple separate clusters
        outside = max(borders, key=lambda border: len(set(border)))

        for border in borders:
            self._grid_view.draw_border(border, fill=border != outside)

    def bind_events(self):
        """Binds relevant events"""
        self._grid_view.on('start_connection', self._drag)
        self._grid_view.on('move_connection', self._drag)
        self._grid_view.on('end_connection', self._drop)

        self._game.on('reset', self._refresh_status)
        self._game.on('complete', self._drop_complete)

        self._game.on('connect', self._connect)
        self._game.on('undo', self._undo)

    def _animation_step(self, step_name):
        """Runs for each step of an animation
        
        Parameters:
            step_name (str): The name (type) of the step    
        """

        self._refresh_status()
        self.draw_grid()

    def animate(self, steps, callback=lambda: None):
        """Animates some steps (i.e. from selecting some dots, activating companion, etc.
        
        Parameters:
            steps (generator): Generator which yields step_name (str) for each step in the animation
        """

        if steps is None:
            steps = (None for _ in range(1))

        animation = create_animation(self._master, steps,
                                     delays=ANIMATION_DELAYS, delay=DEFAULT_ANIMATION_DELAY,
                                     step=self._animation_step, callback=callback)
        animation()

    def _drop(self, position):  # pylint: disable=unused-argument
        """Handles the dropping of the dragged connection

        Parameters:
            position (tuple<int, int>): The position where the connection was
                                        dropped
        """
        if not self._playing:
            return

        if self._game.is_resolving():
            return

        self._grid_view.clear_dragged_connections()
        self._grid_view.clear_connections()

        self.animate(self._game.drop())

    def _connect(self, start, end):
        """Draws a connection from the start point to the end point

        Parameters:
            start (tuple<int, int>): The position of the starting dot
            end (tuple<int, int>): The position of the ending dot
        """

        if self._game.is_resolving():
            return
        if not self._playing:
            return
        self._grid_view.draw_connection(start, end,
                                        self._game.grid[start].get_dot().get_kind())

    def _undo(self, positions):
        """Removes all the given dot connections from the grid view

        Parameters:
            positions (list<tuple<int, int>>): The dot connects to remove
        """
        for _ in positions:
            self._grid_view.undo_connection()

    def _drag(self, position):
        """Attempts to connect to the given position, otherwise draws a dragged
        line from the start

        Parameters:
            position (tuple<int, int>): The position to drag to
        """

        if self._game.is_resolving():
            return
        if not self._playing:
            return

        tile_position = self._grid_view.xy_to_rc(position)

        if tile_position is not None:
            cell = self._game.grid[tile_position]
            dot = cell.get_dot()

            if dot and self._game.connect(tile_position):
                self._grid_view.clear_dragged_connections()
                return

        kind = self._game.get_connection_kind()

        if not len(self._game.get_connection_path()):
            return

        start = self._game.get_connection_path()[-1]

        if start:
            self._grid_view.draw_dragged_connection(start, position, kind)

    @staticmethod
    def remove(*_):
        """Deprecated in 1.1.0"""
        raise DeprecationWarning("Deprecated in 1.1.0")

    def draw_grid(self):
        """Draws the grid"""
        self._grid_view.draw(self._game.grid)

    def reset(self):
        """Resets the basic game"""
        self._basic_game = True
        self._game = DotGame({BasicDot: 1}, objectives=self._objectives, kinds=(1, 2, 3, 4), size=(8, 8),
                             dead_cells=self._dead_cells)
        self.bind_events()
        self.reset_progress()

    def reset_progress(self):
        """The common reset code same in companion game and basic game"""
        self._step = -1
        self._interval.delete(tk.ALL)
        self._interval.layout()
        self._game.reset()
        self.draw_grid()

        self._objectives.reset()
        self._infopanel._object_view.draw(self._objectives.get_status())
        self._infopanel._moves.config(text='20')
        self._playing = True

    def check_game_over(self):
        """Checks whether the game is over and shows an appropriate message box if so"""
        state = self._game.get_game_state()

        if state == self._game.GameState.WON:
            showinfo("Game Over!", "You won!!!")
            self._playing = False
        elif state == self._game.GameState.LOST:
            showinfo("Game Over!",
                     f"You didn't reach the objective(s) in time. You connected {self._game.get_score()} points")
            self._playing = False

    def _drop_complete(self):
        """Handles the end of a drop animation"""
        active_sound = pygame.mixer.Sound('activate.wav')
        active_sound.play()

        self._step += 1
        if self._basic_game == False:

            charges = self._companion.get_charge()
            self._interval.rectangle(charges)

            if self._companion.is_fully_charged():
                self._companion.reset()
                steps = self._companion.activate(self._game)
                self._refresh_status()
                self._interval.delete(tk.ALL)
                self._interval.layout()
                return self.animate(steps)
        if self._basic_game == True:
            if self._step % 6 == 0:
                self._interval.delete(tk.ALL)
                self._interval.layout()
                self._interval.rectangle((self._step % 6) + 1)

            if self._step % 6 != 0:
                self._interval.rectangle((self._step % 6) + 1)


        self.check_game_over()

    def _refresh_status(self):
        """Handles change in game status"""
        score = self._game.get_score()
        objectives = self._objectives.get_status()
        move = self._game.get_moves()
        self._infopanel.set_score(score)
        self._infopanel.set_objectives(objectives)
        self._infopanel.move_changed(move)

    def close_music(self):
        """Stop the background music"""
        pygame.mixer.music.stop()

    def save_game(self):
        """Save the game progress in somewhere"""
        filename = filedialog.asksaveasfilename()
        f = open(filename, 'wb')
        pickle.dump(self.program_dict(), f)
        f.close()
        tk.messagebox.showinfo('Saved', 'Your game has been saved')

    def load_game(self):
        """Chose the save game state file from somewhere"""
        filename = filedialog.askopenfilename()
        f = open(filename, 'rb')  # rb is important
        data_1 = pickle.load(f)
        if data_1['game_mode'] == True:
            self.reset()
            self._basic_game = data_1['game_mode']
            self._infopanel._moves.config(text=data_1['moves'])
            self._game.set_moves(data_1['moves'])
            self._objectives.status = data_1['objectives']
            self._infopanel._object_view.draw(self._objectives.status)
            self._game._score = data_1['score']
            self._infopanel._scores.config(text=data_1['score'])
            for position, cell in data_1['grid']:
                self._game.grid[position] = cell
            self.draw_grid()
            self._interval.delete(tk.ALL)
            self._interval.layout()

            if data_1['step'] == -1:
                self._interval.rectangle(0)
            else:
                self._step = data_1['step']
                self._interval.rectangle((self._step % 6) + 1)
        if data_1['game_mode'] == False:
            self.companion_game()
            self._basic_game = data_1['game_mode']
            self._infopanel._moves.config(text=data_1['moves'])
            self._game.set_moves(data_1['moves'])
            self._objectives.status = data_1['objectives']
            self._infopanel._object_view.draw(self._objectives.status)
            self._game._score = data_1['score']
            self._infopanel._scores.config(text=data_1['score'])
            for position, cell in data_1['grid']:
                self._game.grid[position] = cell
            self.draw_grid()
            self._interval.delete(tk.ALL)
            self._interval.layout()
            self._companion._charge = data_1['charge']
            self._interval.rectangle(self._companion.get_charge())
        f.close()

    def program_dict(self):
        """Set the game progress in a dictionary"""
        self._data = {'moves': self._game.get_moves(),
                      'score': self._game.get_score(),
                      'objectives': self._objectives.get_status(),
                      'grid': list(self._game.grid.items()),
                      'step': self._step,
                      'charge': self._companion.get_charge(),
                      'game_mode': self._basic_game
                      }

        return self._data

    def open_music(self):
        """Open the background music"""
        self._music = not self._music

        if self._music is False:

            pygame.mixer.music.load('music.mp3')
            pygame.mixer.music.play(-1)
            self._music_status = 1
        else:
            self.close_music()


def main():
    """Sets-up the GUI for Dots & Co"""
    # Write your GUI instantiation code here
    root = tk.Tk()
    app = DotsApp(root)
    root.mainloop()


if __name__ == "__main__":
    main()
