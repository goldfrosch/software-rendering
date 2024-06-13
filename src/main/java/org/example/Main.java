package org.example;

public class Main {
    public static void main(String[] args) {
        var Display = new Display(800, 600, "Software Rendering");
        var target = Display.GetFrameBuffer();
        var stars = new StarField3D(1024, 32.0f, 8.0f);

        var previousTime = System.nanoTime();
        // 계속 유지하기 위해서 while true 로 설정함
        // JS의 캔버스와 동일한 원리로 동작 즉 프레임별로 그려주고 다시 초기화 하는 것이 기본이다.
        while(true)
        {
            var currentTime = System.nanoTime();
            // 실행 전 시간과 현재 시간의 격차를 계산한다.
            var delta = (float)((currentTime - previousTime)/1000000000.0);
            // 다음 반복문이 실행되기 전 현재 시간을 이전 시간 변수 정보에 저장해 이후 재사용한다.
            previousTime = currentTime;

            stars.UpdateRender(target, delta);
            Display.SwapBuffers();
        }
    }
}