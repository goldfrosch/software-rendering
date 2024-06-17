package org.example;

class TrianglePos {
    public float x;
    public float y;

    public float deg;
}

public class Triangle3D {
    private final float mSpeed;

    private final TrianglePos[] mPos = new TrianglePos[3];

    public Triangle3D(int speed) {
        this.mSpeed = speed;
        for (int i = 0; i <= 2; i++) {
            InitPixel(i);
        }
    }

    private void InitPixel(int i) {
        mPos[i] = new TrianglePos();

        // star 때와 마찬가지로 -1 ~ 1까지의 난수를 만들어준다.
        // 대신 이전과는 다르게 spread 를 이용해 뿌려줄 이유는 없다.
        mPos[i].x = 2 * ((float)Math.random() - 0.5f);
        mPos[i].y = 2 * ((float)Math.random() - 0.5f);

        mPos[i].deg = (float)(Math.atan2(mPos[i].y, mPos[i].x) * 180 / 3.14);
    }

    // 그림 대상의 비트맵과 delta 타임을 가져옴
    public void UpdateRender(RenderContext target, float delta) {
        // 기존 필드 검은색으로 초기화
        target.Clear((byte) 0x00);

        // 중심점을 구함
        var halfWidth  = target.GetWidth() / 2.0f;
        var halfHeight = target.GetHeight() / 2.0f;

        target.FillTriangle(
                new Vertex((int) (mPos[0].x * halfWidth + halfWidth), (int) (mPos[0].y * halfHeight + halfHeight)),
                new Vertex((int) (mPos[1].x * halfWidth + halfWidth), (int) (mPos[1].y * halfHeight + halfHeight)),
                new Vertex((int) (mPos[2].x * halfWidth + halfWidth), (int) (mPos[2].y * halfHeight + halfHeight))
        );

        // TODO: 각도에 따라 움직이고 그 각도에 맞춰서 튕겨나가는 플로우 개발 필요
//        for (int i = 0; i < mPos.length; i++) {
//            target.DrawPixel((int)(mPos[i].x * halfWidth), (int)(mPos[i].y * halfHeight), (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF);
////            if(nextX < 0 || nextX >= target.GetWidth() ||
////                    (nextY < 0 || nextY >= target.GetHeight())) {
////                double newDeg = 0;
////                if (nextX < 0 || nextX >= target.GetWidth()) {
////                    newDeg = 90;
////                } else if (nextY < 0 || nextY >= target.GetHeight()) {
////                    newDeg = 0;
////                }
////
////                double relativeInbound = mPos[i].deg - newDeg;
////                mPos[i].deg = (float) (360 + newDeg - relativeInbound);
////            } else {
////                target.DrawPixel(nextX, nextY, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF);
////            }
//        }
    }
}
