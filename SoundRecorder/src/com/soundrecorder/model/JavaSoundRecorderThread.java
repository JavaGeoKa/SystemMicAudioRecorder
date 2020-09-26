package com.soundrecorder.model;

import javax.sound.sampled.*;
import java.io.*;

public class JavaSoundRecorderThread
{
    AudioFileFormat.Type fileType;
    TargetDataLine line;
    AudioFormat format;

    public JavaSoundRecorderThread()
    {
        fileType = AudioFileFormat.Type.WAVE;
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        format = new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
    }

    public void start(File file)
    {
        Thread thread = new Thread(){
            @Override
            public void run()
            {
                try {
                    DataLine.Info info =
                        new DataLine.Info(TargetDataLine.class, format);
                    line = (TargetDataLine) AudioSystem.getLine(info);
                    line.open(format);
                    line.start();

                    AudioInputStream ais = new AudioInputStream(line);
                    AudioSystem.write(ais, fileType, file);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public void stop(File file, int seconds)
    {
        Thread thread = new Thread(){
            @Override
            public void run()
            {
                try {
                    Thread.sleep(1000 * seconds);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                line.stop();
                line.close();

                //TODO: upload to Dropbox
                //TODO: remove the file
                recordAudio(seconds);
            }
        };
        thread.start();
    }

    public void recordAudio(int seconds)
    {
        //TODO: file name in format: 20200925_141005.wav
        String filePath = "20200925_141005.wav";
        File file = new File(filePath);
        start(file);
        stop(file, seconds);
    }
}