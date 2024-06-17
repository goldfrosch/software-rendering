package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class RenderContext extends Bitmap {
    // 각 픽셀의 y좌표 안에 들어갈 x좌표의 최소 좌표와 최대 좌표를 표시
    // y = 0일 때 [0], [1]에는 각 x의 최소와 최대 좌표를 삽입하는 처리를 진행한다.
    private final int mScanBuffer[];

    public RenderContext(int width, int height) {
        super(width, height);
        mScanBuffer = new int[height * 2];
    }

    public void InputScanBuffer(int yCoord, int xMin, int xMax) {
        // y좌표 안의 선을 그릴 최소와 최대 좌표를 넣는다.
        // 2를 곱하는 것은 하나의 y좌표 기반 배열을 2칸을 활용하기 때문
        mScanBuffer[yCoord * 2] = xMin;
        mScanBuffer[yCoord * 2 + 1] = xMax;
    }

    public void FillTriangle(Vertex v1, Vertex v2, Vertex v3) {
        List<Vertex> VertexList = Arrays.asList(v1, v2, v3);

        VertexList.sort(Comparator.comparingInt(Vertex::getY));

        var area = VertexList.get(0).getTriangleArea(VertexList.get(2), VertexList.get(1));
        // 영역의 크기에 따라 최솟값이 될 지 최대값이 될지 지정해준다.
        // 사실 boolean 이 더 좋다고 생각은 들지만 일단은... 혹시 몰라 int 로 설정한다.
        int handedness = area >= 0 ? 1 : 0;

        // 삼각형 스캔으로 각 영역의 위치를 채워주는 작업을 수행한다.
        ScanConvertTriangle(VertexList.get(0), VertexList.get(1), VertexList.get(2), handedness);
        // 이후 y 좌표 간의 영역을 그려주는 작업을 수행한다.
        FillShape(VertexList.get(0).getY(), VertexList.get(2).getY());
    }

    public void FillShape(int yMin, int yMax)
    {
        for(int j = yMin; j < yMax; j++)
        {
            int xMin = mScanBuffer[j * 2];
            int xMax = mScanBuffer[j * 2 + 1];

            // min 부터 max 까지 픽셀을 그리는 작업을 수행함으로써 최소한의 값으로 최대한의 효율을 사용함
            // 즉 불필요한 pixel 값을 없앨 수 있는 것이 이점이다.
            for(int i = xMin; i < xMax; i++)
            {
                // 하얀색을 칠할 것이기 때문에 255 값을 A,B,G,R 값에 다 넣어줌
                DrawPixel(i, j, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF);
            }
        }
    }

    public void ScanConvertTriangle(Vertex minYVertex, Vertex midYVertex, Vertex maxYVertex, int handedness) {
        // 3개의 꼭지점에 각각 하나씩 연동 처리를 진행함.
        ScanConvertLine(minYVertex, maxYVertex, handedness);
        ScanConvertLine(minYVertex, midYVertex, 1 - handedness);
        ScanConvertLine(midYVertex, maxYVertex, 1 - handedness);
    }

    private void ScanConvertLine(Vertex minYVertex, Vertex maxYVertex, int whichSide) {
        // 라인 그리는 작업을 수행할 때 필요한 X 좌표와 Y 좌표의 시작과 끝을 가져오는 작업을 수행
        int yStart = minYVertex.getY();
        int yEnd  = maxYVertex.getY();
        int xStart = minYVertex.getX();
        int xEnd = maxYVertex.getX();

        // 각각의 거리를 계산함 (x, y 좌표의 이동 거리)
        int yDist = yEnd - yStart;
        int xDist = xEnd - xStart;

        // y dist 가 0보다 작거나 같다면 굳이 작업을 수행할 이유가 없다.
        // 각각 더 작은 좌표와 큰 좌표를 받아왔기 때문.
        // x 좌표는 신경쓰지 않는 거리 계산 방법이기 때문이다.
        if (yDist <= 0) return;

        // x가 이동할 거리는 x / y로 결정한다.
        // y가 기본적으로 양수이고 min 과 max 의 결정 기준이 y 좌표 크기인 만큼
        // y를 기준으로 하여 y 거리에 따라 x 좌표를 찍어주는 처리를 진행한다.
        // ex. yDist 가 100이고 xDist 가 80이라면 1칸 갈 때 마다 0.8칸 씩 이동한다고 보면 된다.
        float xStep = (float)xDist / (float)yDist;
        // 현재 X 좌표 지점은 역시 첫 시작 지점으로 초기화 한다.
        float curX = (float)xStart;

        for(int j = yStart; j < yEnd; j++)
        {
            // mScanBuffer 에 각각의 최대, 최솟값을 더해주는 작업을 수행한다.
            // whichSide 는 0 아니면 1로 구성되는데 어디가 최소이고 어디가 최대인지를 구분해주는 역할을 한다.
            // 이후 각 x의 최소, 최대 값을 넣어줌으로써 그림 자체를 그릴 때 유용하게 활용할 수 있게 한다.
            mScanBuffer[j * 2 + whichSide] = (int)curX;
            // 이동한 만큼 값을 더해 다음 좌표를 설정한다.
            curX += xStep;
        }
    }
}
