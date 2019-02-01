package org.unindra.cobazxing;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ScannerQR extends JFrame implements Runnable {

    private static final long serialVersionUID = 6441489157408381878L;

    private Webcam webcam;

    private Result result;

    ScannerQR() {
        super();

        setLayout(new FlowLayout());
        setTitle("Scan QR");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        Dimension size = WebcamResolution.QVGA.getSize();

        webcam = Webcam.getWebcams().get(0);
        webcam.setViewSize(size);

        WebcamPanel webcamPanel = new WebcamPanel(webcam);
        webcamPanel.setPreferredSize(size);
        webcamPanel.setFPSDisplayed(true);

        add(webcamPanel);

        pack();

        setVisible(true);
    }

    @Override
    public void run() {
        try {
            while (result == null && isVisible()) {
                BufferedImage image;

                if (webcam.isOpen()) {

                    if ((image = webcam.getImage()) == null) {
                        continue;
                    }

                    LuminanceSource source = new BufferedImageLuminanceSource(image);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                    try {
                        result = new MultiFormatReader().decode(bitmap);
                    } catch (NotFoundException e) {
                        // fall thru, it means there is no QR code in image
                    }

                    if (result != null || ! isVisible()) {
                        dispose();
                        webcam.close();
                    }
                }
            }
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    Result getResult() {
        return result;
    }
}