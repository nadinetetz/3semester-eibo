package objects;

import ddf.minim.AudioMetaData;
import ddf.minim.Minim;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.datatype.Artwork;

/**
 * EIBO Projekt WS 2012/2013
 *
 * Klasse einer Repository
 *
 * @author Nadine Tetz, Julia Jochum, Marc Radziwill, Philipp Siegmund
 * @version 1.0
 */
public class MP3Repository extends Observable {

    //createTrack refactoring/kommentieren
    private int id = 1;
    private Mp3_Player player;
    private ArrayList<Playlist> playlists = new ArrayList<Playlist>();
    private Minim minim;

    /**
     * Initialisierungskonstruktor
     *
     * @param player - MP3 Player der Anwendung
     */
    public MP3Repository(Mp3_Player player) {
        this.player = player;
        minim = new Minim(this);
    }

    /**
     * Gibt die Trackanzahl der Playlist zurück
     *
     * @param name - Name der Playlist
     */
    public void createPlaylist(String name) {
        playlists.add(new Playlist(name));
        player.setActPlaylist(findByName(name));
        setChanged();
        // Informiere Observer über Änderung der Position
        notifyObservers("create");
    }

    /**
     * Pfad einer Datei wird eingelesen und die MetaDaten (ID3) ausgelesen. Der
     * erstellte Track wird der Playlist hinzugefügt
     *
     * @param path - Pfad der Datei
     */
    public void createTrack(String path) {
        Playlist p = player.getActPlaylist();
        String sort = p.getSorted();
        
        ImageIcon cover = null;
        AudioMetaData meta = minim.loadFile(path).getMetaData();

        try {
            cover = getCover(path);
        } catch (IOException ex) {
            Logger.getLogger(MP3Repository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CannotReadException ex) {
            Logger.getLogger(MP3Repository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TagException ex) {
            Logger.getLogger(MP3Repository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ReadOnlyFileException ex) {
            Logger.getLogger(MP3Repository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAudioFrameException ex) {
            Logger.getLogger(MP3Repository.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(meta.title());
        Track t = new Track(id, meta.title(), meta.length() / 1000, meta.album(), meta.author(), path, cover, meta.genre());
        p.addTrack(t);

        
        if (p.getNumberOfTracks() == 1) {
            player.setActTrack(p.getIndexOfTrack(t));
        }

        // Falls die Playlist vor dem Hinzufügen eines Tracks sortiert wurde wird das Modell
        // erneut in die richtige Reihenfolge gebracht Baby
        
        if (!sort.equals("")) {
            p.setSortAscending(!p.isSortAscending());
            p.sortModel(sort);
        }
        id++;
    }
    
    /**
     * Prüft, ob der Playlistname schon vorhanden ist
     *
     * @param path - Name der Playlist
     * @return boolean
     */
    public Boolean checkPlaylistName(String name) {
        for (int i = 0; i < playlists.size(); i++) {
            if (playlists.get(i).getTitle().equals(name)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Löscht die übergebene Playlist und ihre Tracks
     *
     * @param playlist - zulöschende Playlist
     */
    public void deletePlaylist(Playlist playlist) {
        player.stop();
        while (playlist.getNumberOfTracks() > 0) {
            playlist.removeTrack(playlist.getTrack(0));
        }
        for (Playlist item : playlists) {
            if (item.equals(playlist)) {
                playlists.remove(item);
                break;
            }
        }
        setChanged();
        // Informiere Observer über Änderung der Position
        notifyObservers("delete");
    }

    /**
     * Gibt das Cover des übergebenen Pfades zurück
     *
     * @param path - Pfad der Mp3-Datei
     * @return cover - Cover der Mp3-Datei
     * @throws IOException
     * @throws CannotReadException
     * @throws TagException
     * @throws ReadOnlyFileException
     * @throws InvalidAudioFrameException
     */
    public ImageIcon getCover(String path) throws IOException, CannotReadException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
        ImageIcon cover;
        File actFile = new File(path);
        AudioFile audioFile = AudioFileIO.read(actFile);
        Tag tags = audioFile.getTag();

        // Cover
        Artwork artwork = tags.getFirstArtwork();
        artwork.createArtworkFromFile(actFile);

        try {
            // Cover auf entsprechende Groesse zuschneiden
            Image image = toImage(artwork.getImage());
            Image imageNewScale = image.getScaledInstance(150, 150, 150);
            cover = new ImageIcon(imageNewScale);
        } catch (Exception e) {
            cover = new ImageIcon(getClass().getResource("/data/pictures/logo.png"));
        }
        return cover;
    }

    /**
     * Gibt ein BufferedImage als Image zurück
     *
     * @param image
     * @return
     */
    public Image toImage(BufferedImage image) {
        return Toolkit.getDefaultToolkit().createImage(image.getSource());
    }

    /**
     * Gibt alle Playlists zurück
     *
     * @return playlists - Liste aller Playlists
     */
    public ArrayList<Playlist> getAllPlaylists() {
        return playlists;
    }

    /**
     * Gibt die Playlist an dem übergebenen Index in der Liste zurück
     *
     * @param index - Index in der Liste der Playlists
     */
    public Playlist getPlaylistByIndex(int index) {
        return playlists.get(index);
    }

    /**
     * Fügt eine Playlist hinzu
     *
     * @param playlist - Playlist, die hinzugefügt werden soll
     */
    public void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
    }

    /**
     * Prüft, ob die Playlist leer ist
     *
     * @return boolean
     */
    public boolean isEmpty() {
        return playlists.isEmpty();
    }

    /**
     * Gibt die Playlist mit dem übergebenen Name zurück
     *
     * @param name - Name der Playlist
     */
    public Playlist findByName(String name) {
        for (Playlist p : playlists) {
            if (p.getTitle().equals(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Methode zum Nutzen des Minim-Players ohne Processing
     *
     * @param fileName
     * @return fileName
     *
     */
    public String sketchPath(String fileName) {
        return fileName;
    }

    /**
     * Methode zum Nutzen des Minim-Players ohne Processing
     *
     * @param fileName
     * @return in
     *
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
