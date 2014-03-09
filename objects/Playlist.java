package objects;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Observable;

/**
 * EIBO Projekt WS 2012/2013
 *
 * Klasse einer Playlist
 *
 * @author Nadine Tetz, Julia Jochum, Marc Radziwill, Philipp Siegmund
 * @version 1.0
 */
public class Playlist extends Observable {

    private String title;
    private LinkedList<Track> tracks = new LinkedList<Track>();
    private boolean sortAscending = true;
    private String sorted = "";
    private String sorterBevor = "";

    /**
     * Initialisierungskonstruktor
     *
     * @param title - Title der Playlist
     */
    public Playlist(String title) {
        this.title = title;
    }

    /**
     * Gibt die Trackanzahl der Playlist zurück
     *
     * @return the size - Trackanzahl der Playlist
     */
    public int getNumberOfTracks() {
        if (getTracks() != null) {
            return getTracks().size();
        } else {
            return 0;
        }
    }

    /**
     * Gibt den Track mit der Nummer des Parameters zurück
     *
     * @param no - Nummer des Tracks der zurückgegeben werden soll
     * @return Track - Track mit der Nummer des Parameters
     */
    public Track getTrack(int no) {
        if (no < getNumberOfTracks()) {
            return getTracks().get(no);
        } else {
            return null;
        }
    }

    /**
     * Fügt der Playlist einen Track hinzu
     *
     * @param track - Track der hinzugefügt werden soll
     */
    public void addTrack(Track track) {
        getTracks().add(track);
        setChanged();
        // Informiere Observer über Änderung der Position
        notifyObservers("track");
    }

    /**
     * Löscht aus der Playlist einen Track
     *
     * @param track - Track der gelöscht werden soll
     */
    public void removeTrack(Track track) {
        for (int i = 0; i < tracks.size(); i++) {
            if (tracks.get(i) == track) {
                tracks.remove(i);
                break;
            }
        }
        setChanged();
        // Informiere Observer über Änderung der Position
        notifyObservers("track");
    }

    /**
     * Getter des Playlisttitels
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter des Playlisttitels
     *
     * @param title - Playlisttitel der gesetzt werden soll
     */
    public void setTitle(String title) {
        this.title = title;
        setChanged();
        // Informiere Observer über Änderung der Position
        notifyObservers("edit");
    }

    /**
     * Getter der Trackliste der Playlist
     *
     * @return the tracks
     */
    public LinkedList<Track> getTracks() {
        return tracks;
    }

    /**
     * Gibt den Index, des übergebenen Tracks, in der Playlist zurück
     *
     * @param t - Track
     * @return
     */
    public int getIndexOfTrack(Track t) {
        return this.getTracks().indexOf(t);
    }

        /**
     * @return the sorted
     */
    public String getSorterBevor() {
        return sorterBevor;
    }

    /**
     * @param sorted the sorted to set
     */
    public void setSorterBevor(String sorterBevor) {
        this.sorterBevor = sorterBevor;
    }
    
    /**
     * @return the sorted
     */
    public String getSorted() {
        return sorted;
    }

    /**
     * @param sorted the sorted to set
     */
    public void setSorted(String sorted) {
        this.sorted = sorted;
    }
    
    /**
     * Sortiert die aktuelle Playlist nach Titeln
     */
    public void sortByTitle() {
//        tracks = helpTracks;
        if (sortAscending) {
            // ascending order
            Collections.sort(this.tracks, new Comparator<Track>() {
                @Override
                public int compare(Track o1, Track o2) {
                    return o1.getTitle().compareTo(o2.getTitle());
                }
            });
            sortAscending= false;
        } else {
            // descending order
            Collections.sort(this.tracks, new Comparator<Track>() {
                @Override
                public int compare(Track o1, Track o2) {
                    return o2.getTitle().compareTo(o1.getTitle());
                }
            });
            sortAscending = true;
        }
    }

    /**
     * Sortiert die aktuelle Playlist nach Artist
     */
    public void sortByArtist() {
//        tracks = helpTracks;
        if (sortAscending) {
            // ascending order
            Collections.sort(this.tracks, new Comparator<Track>() {
                @Override
                public int compare(Track o1, Track o2) {
                    return o1.getArtist().compareTo(o2.getArtist());
                }
            });
            sortAscending = false;
        } else {
            // descending order
            Collections.sort(this.tracks, new Comparator<Track>() {
                @Override
                public int compare(Track o1, Track o2) {
                    return o2.getArtist().compareTo(o1.getArtist());
                }
            });
            sortAscending = true;
        }
    }

    /**
     * Sortiert die aktuelle Playlist nach der Länge
     */
    public void sortByLength() {
//        tracks = helpTracks;
        if (sortAscending) {
            // ascending order
            Collections.sort(this.tracks, new Comparator<Track>() {
                @Override
                public int compare(Track o1, Track o2) {
                    return o1.getLength() > o2.getLength() ? +1 : o1.getLength() < o2.getLength() ? -1 : 0;
                }
            });
            sortAscending = false;
        } else {
            // descending order
            Collections.sort(this.tracks, new Comparator<Track>() {
                @Override
                public int compare(Track o1, Track o2) {
                    return o2.getLength() > o1.getLength() ? +1 : o2.getLength() < o1.getLength() ? -1 : 0;
                }
            });
            sortAscending = true;
        }
    }

    /**
     * Sortiert die aktuelle Playlist nach Alben
     */
    public void sortByAlbum() {
//        tracks = helpTracks;
        if (sortAscending) {
            // ascending order
            Collections.sort(this.tracks, new Comparator<Track>() {
                @Override
                public int compare(Track o1, Track o2) {
                    return o1.getAlbum().compareTo(o2.getAlbum());
                }
            });
            setSortAscending(false);
        } else {
            // descending order
            Collections.sort(this.tracks, new Comparator<Track>() {
                @Override
                public int compare(Track o1, Track o2) {
                    return o2.getAlbum().compareTo(o1.getAlbum());
                }
            });
            setSortAscending(true);
        }
    }

    /**
     * Sortiert die aktuelle Playlist nach Genre
     */
    public void sortByGenre() {
//        tracks = helpTracks;
        if (sortAscending) {
            // ascending order
            Collections.sort(this.tracks, new Comparator<Track>() {
                @Override
                public int compare(Track o1, Track o2) {
                    return o1.getGenre().compareTo(o2.getGenre());
                }
            });
            setSortAscending(false);
        } else {
            // descending order
            Collections.sort(this.tracks, new Comparator<Track>() {
                @Override
                public int compare(Track o1, Track o2) {
                    return o2.getGenre().compareTo(o1.getGenre());
                }
            });
            setSortAscending(true);
        }
    }
    
    /**
     * Entscheidet anhand des übergebenen Strings welche Sortier-Methode aufgerufen wird
     * 
     * @param c 
     */
    public void sortModel(String c) {
        if (c.equals("Titel")) {
            this.sortByTitle();
        } else if (c.equals("Interpret")) {
            this.sortByArtist();
        } else if (c.equals("Dauer")) {
            this.sortByLength();
        } else if (c.equals("Genre")) {
            this.sortByGenre();
        } else if (c.equals("Album")) {
            this.sortByAlbum();
        }
        setChanged();
        // Informiere Observer über Änderung der Position
        notifyObservers("sort");
    }

    /**
     * @return the sortAscending
     */
    public boolean isSortAscending() {
        return sortAscending;
    }

    /**
     * @param sortAscending the sortAscending to set
     */
    public void setSortAscending(boolean sortAscending) {
        this.sortAscending = sortAscending;
    }
}
