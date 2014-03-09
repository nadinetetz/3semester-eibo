package objects;

import java.awt.Component;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * EIBO Projekt WS 2012/2013
 *
 * Klasse für ein eigenes TableModel zum Anzeigen einer Playlist in einer JTable
 *
 * @author Nadine Tetz, Julia Jochum, Marc Radziwill, Philipp Siegmund
 * @version 1.0
 */
public class PlaylistTable implements Serializable {

    private JTable table;
    private DefaultTableModel model;
    private Playlist playlist;
    private ImageIcon playIcon = new ImageIcon(getClass().getResource("/data/pictures/playing.png"));

    /**
     * Initialisierungskonstruktor
     *
     * @param table - Tabelle die befüllt werden soll
     * @param model - TableModel
     * @param playlist - Playlist die angezeigt werden soll
     */
    public PlaylistTable(JTable table, Playlist playlist) {
        this.table = table;
        this.model = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.playlist = playlist;
        this.table.setModel(this.model);
        createColumns();
    }

    /**
     * Fügt die Spalten mit Titeln hinzu
     */
    public void createColumns() {
        model.addColumn("");
        model.addColumn("Interpret");
        model.addColumn("Titel");
        model.addColumn("Dauer");
        model.addColumn("Album");
        model.addColumn("Genre");
    }

    /**
     * Setzt die Breiten der Spalten
     */
    public void setColumnSize() {
        table.getColumnModel().getColumn(0).setPreferredWidth(15);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(25);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
    }

    /**
     * Aktualisiert den Tabelleninhalt
     */
    public void updatePlaylistTable() {
        // Löscht die Zeilen
        if (model.getRowCount() > 0) {
            deleteRows();
        }
        // Wenn die Playlist nicht leer ist, wird die Tabelle mit den Trackinformationen befüllt
        if (playlist != null) {
            for (Track track : playlist.getTracks()) {
                model.addRow(new String[]{"", track.getArtist(), track.getTitle(), track.lengthToString(),
                            track.getAlbum(), track.getGenre()});
            }
        }
    }

    /**
     * Löscht alle Zeilen
     */
    public void deleteRows() {
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
    }

    /**
     * Setzt die Playlist
     *
     * @param playlist - Playlist die gesetzt werden soll
     */
    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    /**
     * Gibt den i-ten Spaltennamen zurück
     *
     * @param i - Index des Spalte
     * @return - Spaltenname
     */
    public String getColumnName(int i) {
        return model.getColumnName(i);
    }

    public void setPlayingRow(int a) {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(" ", i, 0);
        }
        table.getColumnModel().getColumn(0).setCellRenderer(new IconTableCellRenderer());
        table.setValueAt(playIcon, a, 0);
    }

    class IconTableCellRenderer extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            if (value instanceof ImageIcon) {
                label.setText(null);
                label.setIcon((ImageIcon) value);
            } else {
                label.setIcon(null);
            }
            return label;
        }
    }
}