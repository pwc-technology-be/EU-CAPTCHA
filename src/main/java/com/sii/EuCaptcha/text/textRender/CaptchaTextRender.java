package com.sii.EuCaptcha.text.textRender;

import nl.captcha.text.renderer.WordRenderer;

import java.awt.*;

import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author mousab.aidoud
 * @version 1.0
 * Captcha Text drawing class
 */
public class CaptchaTextRender implements WordRenderer {

    /**
     * List of Color and font
     */
    private static final Random RAND = new SecureRandom();
    private static final List<Color> DEFAULT_COLORS = new ArrayList<Color>();
    private static final List<Font> DEFAULT_FONTS = new ArrayList<Font>();


    static {
        DEFAULT_COLORS.add(Color.BLACK);
        DEFAULT_FONTS.add(new Font("Arial", Font.BOLD, 80));
        DEFAULT_FONTS.add(new Font("Courier", Font.BOLD, 80));
    }

    private final List<Color> _colors = new ArrayList<Color>();
    private final List<Font> _fonts = new ArrayList<Font>();


    public CaptchaTextRender() {
        this(DEFAULT_COLORS, DEFAULT_FONTS);
    }

    /**
     * Build a <code>WordRenderer</code> using the given <code>Color</code>s and
     * <code>Font</code>s.
     *
     * @param colors
     * @param fonts
     */
    public CaptchaTextRender(List<Color> colors, List<Font> fonts) {
        _colors.addAll(colors);
        _fonts.addAll(fonts);
    }


    @Override
    public void render(String word, BufferedImage image) {
        Graphics2D g = image.createGraphics();

        RenderingHints hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        hints.add(new RenderingHints(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY));
        g.setRenderingHints(hints);

        FontRenderContext frc = g.getFontRenderContext();
        SecureRandom rand = new SecureRandom();
        double lowerX = 0.3;
        double upperX = 0.2;
        double x = (rand.nextFloat() * (upperX - lowerX)) + lowerX;

        double lowerY = 0.45;
        double upperY = 0.7;
        double y = (rand.nextFloat() * (upperY - lowerY)) + lowerY;

        int xBaseline = (int) Math.round(image.getWidth() * x);
        int yBaseline = image.getHeight() - (int) Math.round(image.getHeight() * y);

        char[] chars = new char[1];

        for (char c : word.toCharArray()) {
            chars[0] = c;
            g.setColor(_colors.get(RAND.nextInt(_colors.size())));
            int choiceFont = RAND.nextInt(_fonts.size());
            Font font = _fonts.get(choiceFont);
            g.setFont(font);

            GlyphVector gv = font.createGlyphVector(frc, chars);

            AffineTransform affineTransform = new AffineTransform();
            affineTransform.rotate(Math.toRadians(15), 0, 0);
            g.setTransform(affineTransform);

            g.drawChars(chars, 0, chars.length, xBaseline, yBaseline);

            int width = (int) gv.getVisualBounds().getWidth();
            xBaseline = xBaseline + width;
        }


    }
}
