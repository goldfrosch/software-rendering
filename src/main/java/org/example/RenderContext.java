package org.example;

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
}
