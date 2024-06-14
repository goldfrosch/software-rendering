package org.example;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class Display extends Canvas {
    private final JFrame mFrame;
    /** The bitmap representing the final image to display */
    @Getter
    private final Bitmap mFrameBitmapBuffer;

    @Getter
    private final RenderContext mFrameBuffer;
    private final BufferedImage mDisplayImage;
    private final byte[] mDisplayComponents;
    private final BufferStrategy mBufferStrategy;
    private final Graphics mGraphics;

    public Display(int width, int height, String title) {
        Dimension size = new Dimension(width, height);

        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        mFrameBitmapBuffer = new Bitmap(width, height);
        mFrameBuffer = new RenderContext(width, height);
        mDisplayImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        mDisplayComponents = ((DataBufferByte)mDisplayImage.getRaster().getDataBuffer()).getData();

        mFrameBuffer.Clear((byte)0x80);
        mFrameBuffer.DrawPixel(100, 100, (byte)0x00, (byte)0xFF, (byte)0x00, (byte)0xFF);

        mFrame = new JFrame();

        mFrame.add(this);
        mFrame.pack();
        mFrame.setResizable(false);
        mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mFrame.setLocationRelativeTo(null);
        mFrame.setSize(width, height);
        mFrame.setTitle(title);
        mFrame.setVisible(true);

        createBufferStrategy(1);
        mBufferStrategy = getBufferStrategy();
        mGraphics = mBufferStrategy.getDrawGraphics();
    }

    public void SwapBuffers()
    {
        mFrameBuffer.CopyToByteArray(mDisplayComponents);
        mGraphics.drawImage(mDisplayImage, 0, 0,
                mFrameBuffer.GetWidth(), mFrameBuffer.GetHeight(), null);
        mBufferStrategy.show();
    }
}
