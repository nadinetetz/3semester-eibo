package objects;

import javax.swing.ImageIcon;

/**
 *
 * EIBO Projekt WS 2012/2013
 *
 * Klasse eines Tracks
 *
 * @author Nadine Tetz, Julia Jochum, Marc Radziwill, Philipp Siegmund
 * @version 1.0
 */
public class Track {

    private int id;
    private String title;
    private int length;
    private String album;
    private String artist;
    private String soundFile;
    private ImageIcon coverFile;
    private String genre;

    /**
     * Initialisierungskonstruktor
     *
     * @param id - ID des Tracks
     * @param title - Titel des Tracks
     * @param length - Dauer des Tracks
     * @param album - Albumtitel des Tracks
     * @param band - Band/Interpret des Tracks
     * @param soundFile - Pfad der Musikdatei
     * @param coverfile - Pfad des Covers
     * @param genre - Genre des Tracks
     */
    public Track(int id, String title, int length, String album,
            String artist, String soundFile, ImageIcon coverFile, String genre) {

        this.id = id;
        this.title = title;
        this.length = length;
        this.album = album;
        this.artist = artist;
        this.soundFile = soundFile;
        this.coverFile = coverFile;
        this.genre = genre;
    }

    /**
     * Wandelt die Dauer des Tracks von Millisekunden in Minuten und Sekunden
     * als String um
     *
     * @return s - Dauer des Tracks
     */
    public String lengthToString() {
        String s;
        if ((length % 60) < 10) {
            s = String.valueOf(length / 60) + ":0" + String.valueOf(length % 60);
        } else {
            s = String.valueOf(length / 60) + ":" + String.valueOf(length % 60);
        }
        return s;
    }

    /**
     * Getter der Track-ID
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Getter des Tracktitels
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter der Tracklänge
     *
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * Getter des Albumtitels des Tracks
     *
     * @return the album
     */
    public String getAlbum() {
        return album;
    }

    /**
     * Getter des Artists des Tracks
     *
     * @return the artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Getter des Musikdateipfads des Tracks
     *
     * @return the soundFile
     */
    public String getSoundFile() {
        return soundFile;
    }

    /**
     * Getter des Covers zum Track
     *
     * @return the coverFile
     */
    public ImageIcon getCoverFile() {
        return coverFile;
    }

    /**
     * Getter des Genre des Tracks
     *
     * @return the genre
     */
    public String getGenre() {
        return genre;
    }
}