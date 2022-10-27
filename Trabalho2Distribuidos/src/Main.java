
import View.OptionMenu;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
    	 SwingUtilities.invokeLater(new Runnable() {
             @Override
             public void run() {
                 OptionMenu tela = new OptionMenu();
                 tela.setVisible(true);
             }
         });
    }
}
