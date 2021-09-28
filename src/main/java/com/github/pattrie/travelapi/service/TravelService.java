package com.github.pattrie.travelapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pattrie.travelapi.enumaration.TravelTypeEnum;
import com.github.pattrie.travelapi.factory.TravelFactory;
import com.github.pattrie.travelapi.factory.TravelFactoryImpl;
import com.github.pattrie.travelapi.model.Travel;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TravelService {
    private TravelFactory factory;
    private List<Travel> travels;

    public void createTravelFactory() {
        if (factory == null) factory = new TravelFactoryImpl();
    }

    public boolean isJsonValid(String jsonInString) {
        try {
            return new ObjectMapper().readTree(jsonInString) != null;
        } catch (IOException e) {
            return false;
        }
    }

    private Integer parseId(JSONObject travel) {
        return Integer.parseInt(String.valueOf(travel.get("id")));
    }

    private BigDecimal parseAmount(JSONObject travel) {
        return new BigDecimal(String.valueOf(travel.get("amount")));
    }

    private LocalDateTime parseDate(String travel) {
        var date = travel;
        return ZonedDateTime.parse(date).toLocalDateTime();
    }

    public boolean isStartDateGreaterThanEndDate(Travel travel) {
        if (travel.getEndDate() == null) return false;
        return travel.getStartDate().isAfter(travel.getEndDate());
    }

    private void setTravelValues(JSONObject jsonTravel, Travel travel) {
        String orderNumber = jsonTravel.get("orderNumber").toString();
        String type = jsonTravel.get("type").toString();
        String startDate = jsonTravel.get("startDate").toString();
        String endDate = jsonTravel.get("endDate").toString();

        travel.setOrderNumber(orderNumber != null ? orderNumber : travel.getOrderNumber());
        travel.setAmount(jsonTravel.get("amount") != null ? parseAmount(jsonTravel) : travel.getAmount());
        travel.setStartDate(startDate != null ?
                parseDate(startDate) : travel.getStartDate());
        travel.setEndDate(endDate != null ? parseDate(endDate) : travel.getEndDate());
        travel.setType(type != null ? TravelTypeEnum.getEnum(type) : travel.getType());
    }

    public Travel create(JSONObject jsonTravel) {
        createFactory();

        Travel travel = factory.createTravel(String.valueOf(jsonTravel.get("type")));
        travel.setId(parseId(jsonTravel));
        setTravelValues(jsonTravel, travel);

        return travel;
    }

    public void createFactory() {
        if (factory == null) {
            factory = new TravelFactoryImpl();
        }
    }

    public Travel update(Travel travel, JSONObject jsonTravel) {
        setTravelValues(jsonTravel, travel);
        return travel;
    }

    public void add(Travel travel) {
        createTravelList();
        travels.add(travel);
    }

    public void createTravelList() {
        if (travels == null) {
            travels = new ArrayList<>();
        }
    }

    public List<Travel> find() {
        createTravelList();
        return travels;
    }

    public Travel findById(long id) {
        Travel t = travels.stream()
                .filter(travel -> id == travel.getId())
                .collect(Collectors.toList())
                .get(0);
        if (t == null) return null;
        else return t;
    }

    public void delete() {
        travels.clear();
    }

    public void clearObjects() {
        travels = null;
        factory = null;
    }
}