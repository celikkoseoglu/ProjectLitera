package litera.Multimedia;

import javax.sound.sampled.*;
import java.io.*;

/**
 * Created by orhun on 23.04.2015.
 */
public class Audio
{
    final AudioFormat format = getFormat();
    protected boolean running;
    private ByteArrayOutputStream out;
    private ByteArrayOutputStream recordBytes;
    private String fileName;

    public Audio(String fileName)
    {
        this.fileName = fileName;
    }

    /**
     *
     */
    public static void playSound(String strFilename)
    {

        final int BUFFER_SIZE = 128000;
        File soundFile = null;
        AudioInputStream audioStream = null;
        AudioFormat audioFormat;
        SourceDataLine sourceLine = null;
        try
        {
            soundFile = new File(strFilename);
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            System.exit(1);
        }

        try
        {
            audioStream = AudioSystem.getAudioInputStream(soundFile);
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            System.exit(1);
        }

        audioFormat = audioStream.getFormat();

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try
        {
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
        }
        catch ( LineUnavailableException e )
        {
            e.printStackTrace();
            System.exit(1);
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            System.exit(1);
        }

        sourceLine.start();

        int nBytesRead = 0;
        byte[] abData = new byte[BUFFER_SIZE];
        while ( nBytesRead != -1 )
        {
            try
            {
                nBytesRead = audioStream.read(abData, 0, abData.length);
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
            if ( nBytesRead >= 0 )
            {
                @SuppressWarnings( "unused" )
                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
            }
        }

        sourceLine.drain();
        sourceLine.close();
    }

    public void captureAudio()
    {
        try
        {

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            final TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            Runnable runner = new Runnable()
            {
                int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
                byte buffer[] = new byte[bufferSize];

                public void run()
                {
                    out = new ByteArrayOutputStream();
                    running = true;
                    try
                    {
                        while ( running )
                        {
                            int count =
                                    line.read(buffer, 0, buffer.length);
                            if ( count > 0 )
                            {
                                out.write(buffer, 0, count);
                            }
                        }
                        out.close();
                        line.drain();
                        line.close();

                    }
                    catch ( IOException e )
                    {
                        System.err.println("I/O problems: " + e);
                        System.exit(-1);
                    }
                }
            };
            Thread captureThread = new Thread(runner);
            captureThread.start();
        }
        catch ( LineUnavailableException e )
        {
            System.err.println("Line unavailable: " + e);
            System.exit(-2);
        }
    }

    /* ben playe basınca kaydedilsin gibi düşünüp buraya attım kayıt kodunu ama capture metoduna da koyulabilir gibi.
    *  Bir de muhtemelen metodu playe attım diye play çalışmıyor ama biraz uğraşırsam altından kalkabilirim gibime geliyor
    *
    *File path'i değiştir - orrun
    * */
    public void saveAudio()
    {
        try
        {
            byte audio[] = out.toByteArray();
            InputStream input = new ByteArrayInputStream(audio);
            final AudioFormat format = getFormat();
            final AudioInputStream ais = new AudioInputStream(input, format, audio.length / format.getFrameSize());
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            File wavFile = new File(fileName);//saved place
            line.open(format);
            line.start();
            //System.out.println("Start recording...");

            // start recording
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, wavFile);//starts writing into wav file
            Runnable runner = new Runnable()
            {
                int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
                byte buffer[] = new byte[bufferSize];

                public void run()
                {
                    try
                    {
                        int count;
                        while ( (count = ais.read(buffer, 0, buffer.length)) != -1 )
                        {
                            if ( count > 0 )
                            {
                                line.write(buffer, 0, count);
                            }
                        }
                        line.drain();
                        line.close();
                    }
                    catch ( IOException e )
                    {
                        System.err.println("I/O problems: " + e);
                        System.exit(-3);
                    }
                }
            };
            Thread playThread = new Thread(runner);
            playThread.start();
        }
        catch ( LineUnavailableException e )
        {
            System.err.println("Line unavailable: " + e);
            System.exit(-4);
        }
        catch ( IOException e )
        {
            System.err.println("I/O problems: " + e);
            System.exit(-3);
        }
    }

    public void stopCapture()
    {
        running = false;
    }

    private AudioFormat getFormat()
    {
        float sampleRate = 8000;//44100;
        int sampleSizeInBits = 8;//16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = true;//false;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String name)
    {
        fileName = name;
    }
    /*public double getDuration(){
        try
        {
            stream = AudioSystem.getAudioInputStream(file);

            AudioFormat format = stream.getFormat();

            return file.length() / format.getSampleRate() / (format.getSampleSizeInBits() / 8.0) / format.getChannels();
        }
        catch (Exception e)
        {
            // log an error
            return -1;
        }
        finally
        {
            try { stream.close(); } catch (Exception ex) { }
        }

    }*/


}





