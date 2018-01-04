/*
 * Copyright 2017 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.deadislands.support;

import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Vector2i;
import org.terasology.utilities.random.FastRandom;
import org.terasology.utilities.random.Random;

public class DeadIslandsMaze {
    public static final int mazeSize = 31;
    private boolean[][] grid = new boolean[mazeSize][mazeSize];

    public DeadIslandsMaze(long seed) {
        Random random = new FastRandom(seed);
        for (short i = 0; i < mazeSize; i++) {
            grid[0][i] = grid[mazeSize - 1][i] = grid[i][0] = grid[i][mazeSize - 1] = true; // set bounds to true
        }
        int remainingGenerators = ((mazeSize - 3) / 2) * ((mazeSize - 3) / 2); // amount of generators in line squared
        while (remainingGenerators > 0) {
            int randomGenerator = random.nextInt() % remainingGenerators;
            randomGenerator *= randomGenerator >= 0 ? 1 : -1;
            int randomDirection = random.nextInt() % 4; // 0: UP, 1: RIGHT, 2: DOWN, 3: LEFT
            randomDirection *= randomDirection >= 0 ? 1 : -1;
            Vector2i currentGenerator = null;
            for (int i = 1; i <= (mazeSize - 3) / 2; i++) { // here indexing from 1 and using <= to ease calculation
                for (int j = 1; j <= (mazeSize - 3) / 2; j++) { // same as above
                    if (grid[i * 2][j * 2]) { // if generator has already been used, skip it
                        continue;
                    }
                    if (randomGenerator == 0) { // this is the generator we want
                        currentGenerator = new Vector2i(2 * i, 2 * j);
                        i = mazeSize; // AKA break
                        break;
                    }
                    randomGenerator--; // generator is available, but not chosen by FastRandom
                }
            }
            if (currentGenerator == null) {
                return;
            }
            while (!isWall(currentGenerator)) {
                setWall(currentGenerator);
                switch (randomDirection){
                    case 0:
                        grid[currentGenerator.x][currentGenerator.y - 1] = true;
                        currentGenerator.addY(-2);
                        break;
                    case 1:
                        grid[currentGenerator.x + 1][currentGenerator.y] = true;
                        currentGenerator.addX(2);
                        break;
                    case 2:
                        grid[currentGenerator.x][currentGenerator.y + 1] =true;
                        currentGenerator.addY(2);
                        break;
                    case 3:
                        grid[currentGenerator.x - 1][currentGenerator.y] = true;
                        currentGenerator.addX(-2);
                        break;
                }
                remainingGenerators--;
            }
        }
        grid[0][1] = grid[mazeSize - 1][mazeSize - 2] = false;
    }

    public boolean isWall(BaseVector2i pos) {
        return grid[pos.x()][pos.y()];
    }

    private void setWall(BaseVector2i pos) {
        grid[pos.x()][pos.y()] = true;
    }
}
