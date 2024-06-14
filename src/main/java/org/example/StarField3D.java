package org.example;

class Pos {
    public float x;
    public float y;
    public float z;
}

public class StarField3D {
    private final float mSpeed;
    private final float mSpread;

    private final Pos mStarPos[];

    public StarField3D(int numStars, float spread, float speed) {
        this.mSpread = spread;
        this.mSpeed = speed;
        this.mStarPos = new Pos[numStars];

        for (int i = 0; i < mStarPos.length; i++) {
            InitStar(i);
        }
    }

    public void InitStar(int i) {
        mStarPos[i] = new Pos();
        // 초반 위치 선정에 사용함.
        // x, y의 경우 화면의 너비와 상관없이 비율로 표현하기 위해서 -0.5 ~ 0.5 까지의 난수를 2를 곱해줘서 -1 ~ 1까지 화면 비율 위치를 선정해주는 작업을 진행한다.
        // 그 후 퍼짐도만큼 곱해서 비율을 조금 늘려주는 작업을 수행한다. 퍼짐도에 따라서 더 넓게 이동하냐 좁게 이동하냐가 정해지는데 좁게 이동할 수록 조금 더 빨라보이는 효과가 생긴다.
        // z의 경우는 크게 상관 없지만 출발지를 아주 약간 다르게 하기 위해 0.00001f만큼 제거
        mStarPos[i].x = 2 * ((float)Math.random() - 0.5f) * mSpread;
        mStarPos[i].y = 2 * ((float)Math.random() - 0.5f) * mSpread;
        mStarPos[i].z = ((float)Math.random() - 0.00001f) * mSpread;
    }

    public void UpdateRender(Bitmap target, float delta) {
        // 기존 필드 검은색으로 초기화
        target.Clear((byte) 0x00);

        // 중심점을 구함
        var halfWidth  = target.GetWidth() / 2.0f;
        var halfHeight = target.GetHeight() / 2.0f;
        final float tanHalfFOV = (float)Math.tan(Math.toRadians(90.0/2.0));

        for (int i = 0; i < mStarPos.length; i++) {
            // 서버 시간과 동일하게 애니메이션을 맞추는 작업을 실행.
            // 서버 자체의 TPS 에 따라 속도가 불규칙해질 수 있어서 처리하는 작업
            // 해당 작업은 언리얼 엔진에서도 액터에 대한 애니메이션 or 투사체 발사에 대한 속도 계산 시 주로 사용된다.
            mStarPos[i].z -= delta * mSpeed;

            // 계속해서 빼지다가 0 혹은 그보다 더 작은 수가 되어 화면 밖에서 벗어나는 경우
            if (mStarPos[i].z <= 0) {
                // i번째 값을 초기화해주는 작업을 진행한다.
                InitStar(i);
            }

            // 현재 x 좌표를 z 좌표로 나눈 값을 화면의 너비만큼 곱하고 화면의 너비만큼 더해준다.
            // 확장되는 느낌을 사용해야 하기에 다음 위치는 속도에 비례해 x나 y나 옆으로 이동하게 하기 위해서 x,y / z로 나눔으로써
            // 값을 조금 더 늘리는 작업을 수행한다.
            // 이후 -1 ~ 1까지의 비율로 된 좌표 값에 화면 절반의 길이를 곱함으로써 정확한 좌표를 추적해준다.
            // 그리고 화면의 경우 음수가 아닌 0부터 시작하기 때문에 절반의 너비와 높이를 더해줌으로써 0부터 너비, 높이의 좌표까지로 좌표 값을 설정해준다.
            // 물론 해당 값은 실제 저장값은 아니기에 그릴 때만 사용한다.
//            int x = (int)((mStarPos[i].x / mStarPos[i].z) * halfWidth + halfWidth);
//            int y = (int)((mStarPos[i].y / mStarPos[i].z) * halfHeight + halfHeight);

            int x = (int)((mStarPos[i].x / (mStarPos[i].z * tanHalfFOV)) * halfWidth + halfWidth);
            int y = (int)((mStarPos[i].y / (mStarPos[i].z * tanHalfFOV)) * halfHeight + halfHeight);

            // 이후 계산한 좌표가 실제 화면에서 벗어날 위치가 되는 경우에는 새로 할당하고 아니라면 새로운 점을 그려준다.
            if(x < 0 || x >= target.GetWidth() ||
                    (y < 0 || y >= target.GetHeight()))
            {
                InitStar(i);
            }
            else
            {
                // 초기화할 필요가 없는 데이터라면 하얀색 점을 그려준다.
                target.DrawPixel(x, y, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF);
            }
        }

    }
}
