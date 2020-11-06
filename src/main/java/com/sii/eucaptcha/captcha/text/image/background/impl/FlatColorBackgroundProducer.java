package com.sii.eucaptcha.captcha.text.image.background.impl;

import com.sii.eucaptcha.captcha.text.image.background.BackgroundProducer;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class FlatColorBackgroundProducer implements BackgroundProducer {
    private Color color = Color.GRAY;

    public FlatColorBackgroundProducer() {
        this(Color.GRAY);
    }

    public FlatColorBackgroundProducer(Color color) {
        this.color = color;
    }

    @Override
    public BufferedImage addBackground(BufferedImage bi) {
        int width = bi.getWidth();
        int height = bi.getHeight();
        return this.getBackground(width, height);
    }

    @Override
    public BufferedImage getBackground(int width, int height) {
        BufferedImage img = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = img.createGraphics();
        graphics.setPaint(color);
        graphics.fill(new Rectangle2D.Double(0, 0, width, height));
        graphics.drawImage(img, 0, 0, null);
        graphics.dispose();

        return img;
    }
}
