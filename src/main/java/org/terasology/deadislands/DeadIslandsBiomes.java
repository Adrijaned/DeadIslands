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
package org.terasology.deadislands;

import org.terasology.world.biomes.Biome;

public enum DeadIslandsBiomes implements Biome {
    OCEAN("Ocean", .3f, 1f, .3f),
    BEACH("Beach", .3f, .99f, .4f),
    ISLAND("Island", .25f, .95f, .6f);

    private final String id, name;
    private final float fog, humidity, temperature;

    DeadIslandsBiomes(String name, float fog, float humidity, float temperature) {
        this.name = name;
        this.id = "DeadIslands:" + name().toLowerCase();
        this.fog = fog;
        this.humidity = humidity;
        this.temperature = temperature;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getFog() {
        return fog;
    }

    @Override
    public float getHumidity() {
        return humidity;
    }

    @Override
    public float getTemperature() {
        return temperature;
    }
}
