package com.analyzeme.analyzers.result;

import com.analyzeme.analyzers.Point;
import com.analyzeme.analyzers.result.IResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by Ольга on 14.04.2016.
 */
public class PointResult implements IResult {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Point result;

    public PointResult(final Point result) {
        this.result = result;
    }

    public Point getValue() {
        return result;
    }

    public String toJson() throws JsonProcessingException {
        if(result == null){
            return null;
        }
        return mapper.writeValueAsString(result);
    }
}
