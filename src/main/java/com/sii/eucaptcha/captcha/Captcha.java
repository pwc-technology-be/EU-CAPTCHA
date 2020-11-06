package com.sii.eucaptcha.captcha;

import com.sii.eucaptcha.captcha.text.image.background.BackgroundProducer;
import com.sii.eucaptcha.captcha.text.image.background.impl.TransparentBackgroundProducer;
import com.sii.eucaptcha.captcha.text.image.gimpy.GimpyRenderer;
import com.sii.eucaptcha.captcha.text.image.gimpy.impl.ShearGimpyRenderer;
import com.sii.eucaptcha.captcha.text.image.noise.ImageNoiseProducer;
import com.sii.eucaptcha.captcha.text.image.noise.impl.CurvedLineImageNoiseProducer;
import com.sii.eucaptcha.captcha.text.textProducer.TextProducer;
import com.sii.eucaptcha.captcha.text.textProducer.impl.DefaultTextProducer;
import com.sii.eucaptcha.captcha.text.textRender.WordRenderer;
import com.sii.eucaptcha.captcha.text.textRender.impl.DefaultWordRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

public class Captcha implements Serializable {
    private static final long serialVersionUID = 617511236L;
    private final Builder builder;

    private Captcha(Builder builder) {
        this.builder = builder;
    }

    public static class Builder implements Serializable {
        private static final long serialVersionUID = 12L;
        /**
         * @serial
         */
        private String answer = "";
        /**
         * @serial
         */
        private BufferedImage img;
        /**
         * @serial
         */
        private BufferedImage bg;
        /**
         * @serial
         */
        private Date timeStamp;

        private boolean addBorder = false;

        public Builder(int width, int height) {
            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }

        /**
         * Add a background using the given {@link BackgroundProducer}.
         *
         * @param bgProd
         */
        public Builder addBackground(BackgroundProducer bgProd) {
            bg = bgProd.getBackground(img.getWidth(), img.getHeight());

            return this;
        }

        /**
         * Generate the answer to the CAPTCHA using the {@link DefaultTextProducer}.
         */
        public Builder addText() {
            return addText(new DefaultTextProducer());
        }

        /**
         * Generate the answer to the CAPTCHA using the given
         * {@link TextProducer}.
         *
         * @param txtProd
         */
        public Builder addText(TextProducer txtProd) {
            return addText(txtProd, new DefaultWordRenderer());
        }

        /**
         * Generate the answer to the CAPTCHA using the default
         * {@link TextProducer}, and render it to the image using the given
         * {@link WordRenderer}.
         *
         * @param wRenderer
         */
        public Builder addText(WordRenderer wRenderer) {
            return addText(new DefaultTextProducer(), wRenderer);
        }

        /**
         * Generate the answer to the CAPTCHA using the given
         * {@link TextProducer}, and render it to the image using the given
         * {@link WordRenderer}.
         *
         * @param txtProd
         * @param wRenderer
         */
        public Builder addText(TextProducer txtProd, WordRenderer wRenderer) {
            answer += txtProd.getText();
            wRenderer.render(answer, img);

            return this;
        }

        /**
         * Add noise using the default {@link CurvedLineImageNoiseProducer}.
         */
        public Builder addNoise() {
            return this.addNoise(new CurvedLineImageNoiseProducer());
        }

        /**
         * Add noise using the given NoiseProducer.
         *
         * @param nProd
         */
        public Builder addNoise(ImageNoiseProducer nProd) {
            nProd.makeNoise(img);
            return this;
        }

        /**
         * Gimp the image using the default {@link GimpyRenderer} (a {@link ShearGimpyRenderer}).
         */
        public Builder gimp() {
            return gimp(new ShearGimpyRenderer());
        }

        /**
         * Gimp the image using the given {@link GimpyRenderer}.
         *
         * @param gimpy
         */
        public Builder gimp(GimpyRenderer gimpy) {
            gimpy.gimp(img);
            return this;
        }

        /**
         * Draw a single-pixel wide black border around the image.
         */
        public Builder addBorder() {
            addBorder = true;

            return this;
        }

        /**
         * Build the CAPTCHA. This method should always be called, and should always
         * be called last.
         *
         * @return The constructed CAPTCHA.
         */
        public Captcha build() {
            if (bg == null) {
                bg = new TransparentBackgroundProducer().getBackground(img.getWidth(), img.getHeight());
            }

            // Paint the main image over the background
            Graphics2D g = bg.createGraphics();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g.drawImage(img, null, null);

            if (addBorder) {
                int width = img.getWidth();
                int height = img.getHeight();

                g.setColor(Color.BLACK);
                g.drawLine(0, 0, 0, width);
                g.drawLine(0, 0, width, 0);
                g.drawLine(0, height - 1, width, height - 1);
                g.drawLine(width - 1, height - 1, width - 1, 0);
            }

            img = bg;

            timeStamp = new Date();

            return new Captcha(this);
        }

        @Override
        public String toString() {

            return "[Answer: " +
                    answer +
                    "][Timestamp: " +
                    timeStamp +
                    "][Image: " +
                    img +
                    "]";
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeObject(answer);
            out.writeObject(timeStamp);
            ImageIO.write(img, "png", ImageIO.createImageOutputStream(out));
        }

        private void readObject(ObjectInputStream in) throws IOException,
                ClassNotFoundException {
            answer = (String) in.readObject();
            timeStamp = (Date) in.readObject();
            img = ImageIO.read(ImageIO.createImageInputStream(in));
        }
    }

    public boolean isCorrect(String answer) {
        return answer.equals(builder.answer);
    }

    public String getAnswer() {
        return builder.answer;
    }

    /**
     * Get the CAPTCHA image, a PNG.
     *
     * @return A PNG CAPTCHA image.
     */
    public BufferedImage getImage() {
        return builder.img;
    }

    public Date getTimeStamp() {
        return new Date(builder.timeStamp.getTime());
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
