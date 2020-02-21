# Atomination


# Intro
A two player game where the objective is to destroy all the atoms of your opponents so you are the remaining player. Each player puts an atom into a grid cell and thereby increasing the grid cell atom number. As each grid has a specific explosion value which means when too many atoms are placed in the same grid, an explosion occurs. One explosion can set off explosions to neighbouring grid cells, thereby possibly causing a chaining of explosions to occur.

Explosion value of each grid:
- Edge = 3 max
- Corner = 2 max
- Everywhere else = 4 max

Note: when a grid cell fills up and hits it's max value, it explodes outwards to neighbouring grid cell.

# Run it

git clone https://github.com/KhangNguyen220/Atomination.git

cd Atomination/

./buildgui.sh



Note: built in first semester of university.
