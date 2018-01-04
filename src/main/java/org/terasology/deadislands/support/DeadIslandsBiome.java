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

import org.terasology.world.biomes.Biome;

public enum DeadIslandsBiome implements Biome{
    OCEAN("Ocean", 0.3f, 1f, 0.3f),
    BEACH("Beach", 0.3f, 0.99f, 0.6f),
    ISLAND("Island", 0.25f, 0.95f, 0.8f),
    SKY("Sky", 0.1f, 0.25f, 0.2f),
    UNDERGROUND("Underground", 0f, 0.8f, 0.1f);

    private final String id, name;
    private final float fog, humidity, temperature;

    DeadIslandsBiome(String name, float fog, float humidity, float temperature) {
        this.name = name;
        this.id = "DeadIslands:" + name.toLowerCase();
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
        return this.name;
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
