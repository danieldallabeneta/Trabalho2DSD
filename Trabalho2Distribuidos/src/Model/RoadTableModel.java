package Model;

import Controller.ControllerMalha;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author danie
 */
public class RoadTableModel extends AbstractTableModel {

    private ControllerMalha meshController;
    private static final long serialVersionUID = 1L;

    public RoadTableModel() {
        this.meshController = ControllerMalha.getInstance();
    }

    @Override
    public int getRowCount() {
        return meshController.getLinha();
    }

    @Override
    public int getColumnCount() {
        return meshController.getColuna();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return new ImageIcon(getClass().getResource(meshController.getImagemFromPosicao(rowIndex, columnIndex)));
    }

}
