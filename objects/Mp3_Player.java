package objects;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;

/**
 * EIBO Projekt WS 2012/2013
 *
 * Klasse eines Mp3-Players
 *
 * @author Nadine Tetz, Julia Jochum, Marc Radziwill, Philipp Siegmund
 * @version 1.0
 */
public class Mp3_Player extends Observable {

    private Minim minim;
    private AudioPlayer player;
    private Playlist actPlaylist;
    private Track actTrack;
    private int trackNo;
    private Timer timer;
    private int timerPosition;
    private float volume = -12;
    private boolean isPaused = false;
    public boolean isShuffling = false;
    public boolean isRepeating = false;
    public boolean isRepeatingAll = false;

    /**
     * Initialisierungskonstruktor
     */
    public Mp3_Player() {
        minim = new Minim(this);
        player = null;
    }

    /**
     * Startet das Abspielen eines Tracks
     */
    public void play() {
        actTrack = null;
        // Prüft, ob eine Playlist vorhanden ist
        if (getActPlaylist() != null) {
            setActTrack(trackNo);
            // Lädt die mp3-Datei in den Player
            player = minim.loadFile(actTrack.getSoundFile());
            // Setzt die Lautstärke des Players
            player.setGain(volume);
            timerPosition = 0;
            timer = new Timer();
            MyTaskGetPosition task = new MyTaskGetPosition();
            // Start des Timers in einer Sekunde, dann Ablauf jede Sekunden
            timer.schedule(task, 1000, 1000);
            // Spielt den Track ab
            player.play();
            isPaused = false;
            setChanged();
            // Informiere Observer über Änderung der Position
            notifyObservers("play");
        }
    }

    /**
     * Stoppt das Abspielen eines Tracks und setzt den
     */
    public void stop() {
        if (player != null) {
            // Wenn der Player gerade am Abspielen ist, wird gestoppt, der Timer beendet 
            // und die aktuelle Tracknummer der Playlist auf 0 gesetzt
            if (player.isPlaying()) {
                player.close();
                player = null;
                minim.stop();
                timer.cancel();
                timerPosition = 0;
                setChanged();
                // Informiere Observer über Änderung der Position
                notifyObservers("stop");
            }
        }
    }

    /**
     * Pausiert das Abspielen eines Tracks
     */
    public void pause() {
        // Wenn der Player gerade am Abspielen ist, wird pausiert und der Timer beendet
        if (player.isPlaying()) {
            player.pause();
            timer.cancel();
            isPaused = true;
            setChanged();
            // Informiere Observer über Änderung der Position
            notifyObservers("pause");
        }
    }

    /**
     * Setzt das Abspielen eines Tracks nach Pause fort
     */
    public void resume() {
        player.play();
        timer = new Timer();
        isPaused = false;
        MyTaskGetPosition task = new MyTaskGetPosition();
        // Start des Timers in einer Sekunde, dann Ablauf jede Sekunden
        timer.schedule(task, 1000, 1000);
        setChanged();
        // Informiere Observer über Änderung der Position
        notifyObservers("play");
    }

    /**
     * Schaltet bei der aktuellen Playlist um einen Track weiter. Bei dem
     * letzten Track wird wieder auf den ersten Track der Playlist gesprungen
     */
    public synchronized void skip() {
        // Prüft ob eine Playlist vorhanden
        if (getActPlaylist() != null) {
            setNextTrack();
            // this.setActTrack(trackNo);            
            // Prüft, ob der Player gerade am Abspielen ist
            if (player != null) {
                if (player.isPlaying()) {

                    // Stoppt den Player
                    player.close();
                    minim.stop();
                    // Beendet den Timer und setzt die Timerposition zurück auf 0
                    timer.cancel();
                    timerPosition = 0;
                    // nach dem Weiterschalten wird der nächste Track abgespielt
                    play();
                }
            }
            isPaused = false;

            setChanged();
            // Informiere Observer über Änderung der Position
            notifyObservers("track");
        }
    }

    public void setNextTrack() {
        if (isShuffling) {
            trackNo = (int) (Math.random() * actPlaylist.getNumberOfTracks());
        } else {
            // wenn es nicht der letzte Track ist, gehe einen Track weiter
            if (trackNo < getActPlaylist().getNumberOfTracks() - 1) {
                trackNo++;
            } else {
                trackNo = 0;
            }
        }
    }

    /**
     * Schaltet bei der aktuellen Playlist um einen Track zurück. Bei dem ersten
     * Track wird auf den letzten Track der Playlist gesprungen
     */
    public synchronized void skipBack() {
        // Prüft ob eine Playlist vorhanden
        if (getActPlaylist() != null) {
            setPreviousTrack();
            //this.setActTrack(trackNo);
            // Prüft, ob der Player gerade am Abspielen ist
            if (player != null && player.isPlaying()) {
                // Stoppt den Player
                player.close();
                minim.stop();
                // Beendet den Timer und setzt die Timerposition zurück auf 0
                timer.cancel();
                timerPosition = 0;
                // nach dem Zurückschalten wird der vorherige Track abgespielt
                play();
            }
            isPaused = false;

            setChanged();
            // Informiere Observer über Änderung der Position
            notifyObservers("track");
        }
    }
    
    public void setPreviousTrack(){
        if (isShuffling) {
            trackNo = (int) (Math.random() * actPlaylist.getNumberOfTracks());
        } else {
            // wenn es nicht der erste Track ist, gehe einen Track zurück
            if (trackNo != 0) {
                trackNo--;
            } // wenn erster Track abgespielt wird, springe auf den letzten Track
            else {
                trackNo = getActPlaylist().getNumberOfTracks() - 1;
            }
        }
    }

    /**
     * Setzt die Lautstärke des Players
     *
     * @param value - Lautstärkewert
     */
    public void setVolume(float value) {
        player.setGain(value);
        volume = player.getGain();
    }

    /**
     * Prüft, ob das Abspielen fortgesetzt oder neu begonnen werden soll
     */
    public void playOrResume() {
        if (isPaused) {
            resume();
        } else {
            play();
        }
    }

    /**
     * Gibt den aktuellen Track zurück
     *
     * @return actTrack - aktueller Track
     */
    public Track getActTrack() {
        return actTrack;
    }

    /**
     * Setzt die aktuelle Tracknummer in der Playlist und den aktuellen Track
     *
     * @param no - Tracknummer die gesetzt werden soll
     */
    public void setActTrack(int no) {
        trackNo = no;
        actTrack = getActPlaylist().getTrack(trackNo);
        setChanged();
        // Informiere Observer über Änderung der Position
        notifyObservers("track");
    }

    /**
     * Gibt die aktuelle Tracknummer in der Playlist zurück
     *
     * @return the trackNo - aktuelle Tracknummer in der Playlist
     */
    public int getTrackNo() {
        return trackNo;
    }

    /**
     * Gibt die aktuelle Playlist zurück
     *
     * @return the actPlaylist - aktuelle Playlist
     */
    public Playlist getActPlaylist() {
        return actPlaylist;
    }

    /**
     * Setzt die aktuelle Playlist
     *
     * @param actPlaylist - Playlist die gesetzt werden soll
     */
    public void setActPlaylist(Playlist actPlaylist) {
        this.actPlaylist = actPlaylist;
        if (actPlaylist != null) {
            setChanged();
            // Informiere Observer über Änderung der Position
            notifyObservers("playlist");
        }
    }

    /**
     * Timer für die Position im Track
     */
    private class MyTaskGetPosition extends TimerTask {

        @Override
        public void run() {
            // Wenn die TimerPosition kleiner als die Tracklänge ist wird weitergezählt, 
            // ansonsten wird ein Track weitergesprungen
            if (timerPosition < actTrack.getLength()) {
                timerPosition += 1;
                // Eine Änderung ist aufgetreten 
                setChanged();
                // Informiere Observer über Änderung der Position
                notifyObservers(timerPosition);
            } else {
                this.cancel();
                nextTrack();
            }
        }
    }

    private void nextTrack() {
        if (isShuffling) {
            trackNo = (int) (Math.random() * actPlaylist.getNumberOfTracks());
            play();
            
        }else if(isRepeating){
            play();
        } 
        else {
            // wenn es nicht der letzte Track ist, gehe einen Track weiter
            if (trackNo < getActPlaylist().getNumberOfTracks() - 1) {
                trackNo++;
                play();
            } // wenn letzter Track erreicht ist, springe wieder auf den ersten Track
            else {
                if (isRepeatingAll) {
                    trackNo = 0;
                    play();
                }else{
                    setActTrack(0);
                    setChanged();
                    // Informiere Observer über Änderung der Position
                    notifyObservers("stop"); 
                }
            }
        }

    }

    /**
     * Prüft, ob die aktuelle Playlist leer ist
     *
     * @return boolean
     */
    public boolean playlistIsEmpty() {
        if (actPlaylist.getNumberOfTracks() > 0) {
            return false;
        }
        return true;
    }

    /**
     * Prüft, ob die aktuelle Playlist null ist
     *
     * @return boolean
     */
    public boolean playlistIsNull() {
        if (actPlaylist != null) {
            return false;
        }
        return true;
    }

    /**
     * Setzt den aktuellen Track Null
     */
    public void setTrackNull() {
        actTrack = null;
    }

    /**
     * Gibt den Titel des aktuellen Tracks zurück
     *
     * @return
     */
    public String getActTitle() {
        return actTrack.getTitle();
    }

    /**
     * Gibt das Cover des aktuellen Tracks zurück
     *
     * @return
     */
    public ImageIcon getActCoverFile() {
        return actTrack.getCoverFile();
    }

    /**
     * Gibt die ID des aktuellen Tracks zurück
     *
     * @return
     */
    public int getActId() {
        return actTrack.getId();
    }

    /**
     * Gibt den Interpreten des aktuellen Tracks zurück
     *
     * @return
     */
    public String getActArtist() {
        return actTrack.getArtist();
    }

    /**
     * Gibt die Länge des aktuellen Tracks zurück
     *
     * @return
     */
    public int getActTrackLength() {
        return actTrack.getLength();
    }

    /**
     * Gibt die Länge des aktuellen Tracks als String zurück
     *
     * @return
     */
    public String getActTrackLengthAsString() {
        return actTrack.lengthToString();
    }

    /**
     * Methode zum Nutzen des Minim-Players ohne Processing
     *
     * @param fileName
     * @return
     */
    public String sketchPath(String fileName) {
        return fileName;
    }

    /**
     * Methode zum Nutzen des Minim-Players ohne Processing
     *
     * @param fileName
     * @return
     */
    public InputStream createInput(String fileName) {
        InputStream in;
        try {
            in = new FileInputStream(fileName);
            return in;
        } catch (Exception ex) {
            in = null;
        }
        return in;
    }
}
