package org.example;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Vertex {
    private final int x;
    private final int y;

    public float getTriangleArea(Vertex a, Vertex b) {
        var xStart = a.getX() - x;
        var yStart = a.getY() - y;

        var xEnd = b.getX() - x;
        var yEnd = b.getY() - y;

        return (xStart * yEnd - xEnd * yStart);
    }
}
