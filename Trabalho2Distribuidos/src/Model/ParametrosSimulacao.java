package Model;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ParametrosSimulacao {

    private static final String MESH_FILES_PATH = "./src/malhas/";

    private int qtdCarros;
    private int intervaloCarros;
    private int maxCarros;
    private File malha;

    public ParametrosSimulacao(int qtdCarros, int intervaloCarros, int maxCarros, String malha) {
        this.qtdCarros       = qtdCarros;
        this.intervaloCarros = intervaloCarros;
        this.maxCarros       = maxCarros;
        this.malha           = this.readMeshFile(malha);
    }

    private File readMeshFile(String meshFileName) {
        Path path = Paths.get(MESH_FILES_PATH + meshFileName);
        return path.toFile();
    }

    public int getQtdCarros() {
        return qtdCarros;
    }

    public int getIntervaloCarros() {
        return intervaloCarros;
    }

    public File getMalha() {
        return malha;
    }

    public int getMaxCarros() {
        return maxCarros;
    }
    
    public void setQtdCarros(int qtd){
        this.qtdCarros = qtd;
    }

    public void setMaxCarros(int qtd) {
        this.maxCarros = qtd;
    }
    
    
}
