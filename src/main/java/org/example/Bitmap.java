package org.example;
import java.util.Arrays;

public class Bitmap {
    private final int mWidth;
    private final int mHeight;
    private final byte mComponents[];

    public int GetWidth() {
        return mWidth;
    }

    public int GetHeight() {
        return mHeight;
    }

    public Bitmap(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        // 픽셀은 A, R, G, B 총 4가지 옵션이 존재하기 때문에 모든 배열에 4를 곱해줘 실제로 들어가는 배열의 크기의 4배로 설정한다.
        mComponents = new byte[mWidth * mHeight * 4];
    }

    public void Clear(byte shade)
    {
        Arrays.fill(mComponents, shade);
    }

    public void DrawPixel(int x, int y, byte a, byte b, byte g, byte r) {
        // 2중 배열을 1중 배열로 만들어둔 것이기 때문에 y 만큼의 width 를 더하고 거기에 x를 더해 1중 배열 안에서 좌표를 구한다.
        // 4를 곱하는 이유는 역시 argb 값 때문이다.
        var index = (x + y * mWidth) * 4;

        mComponents[index] = a;
        mComponents[index + 1] = b;
        mComponents[index + 2] = g;
        mComponents[index + 3] = r;
    }

    public void CopyToByteArray(byte[] dest) {
        // 1중 배열 안에 2중 배열로 취급하는 비트맵 자료를 다 넣기에 width 와 height를 곱해서 1중 배열로 처리한다.
        for (int i = 0; i < mWidth * mHeight; i++) {
            dest[i * 3] = mComponents[i * 4 + 1];
            dest[i * 3 + 1] = mComponents[i * 4 + 2];
            dest[i * 3 + 2] = mComponents[i * 4 + 3];
        }
    }
}
