package com.sii.eucaptcha.captcha.text.textRender;

import java.awt.image.BufferedImage;

public interface WordRenderer {
    /**
     * Render a word to a BufferedImage.
     *
     * @param word The sequence of characters to be rendered.
     * @param image The image onto which the word will be rendered.
     */
    void render(String word, BufferedImage image);
}
