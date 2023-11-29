package com.miraclepat.utils.helper;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

public class GeometryHelper {
    public Polygon createPolygon(Double leftLongitude, Double rightLongitude, Double bottomLatitude, Double topLatitude){
        Coordinate[] coordinates = new Coordinate[5];
        coordinates[0] = new Coordinate(leftLongitude, topLatitude);
        coordinates[1] = new Coordinate(rightLongitude, topLatitude);
        coordinates[2] = new Coordinate(rightLongitude, bottomLatitude);
        coordinates[3] = new Coordinate(leftLongitude, bottomLatitude);
        coordinates[4] = coordinates[0]; // 시작점과 종료점이 동일해야 함

        GeometryFactory geometryFactory = new GeometryFactory();
        Polygon polygon = geometryFactory.createPolygon(coordinates);
        polygon.setSRID(4326);
        return polygon;
    }
}
