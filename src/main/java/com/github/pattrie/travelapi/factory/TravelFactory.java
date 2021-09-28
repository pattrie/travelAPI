package com.github.pattrie.travelapi.factory;

import com.github.pattrie.travelapi.model.Travel;

public interface TravelFactory {
    Travel createTravel(String type);
}