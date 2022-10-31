package ui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class Chooser extends Component {

    private JFileChooser fileChooser;

    public Chooser(){
        fileChooser = new JFileChooser();
    }

    public String getPath(){

        String path = "";

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        File currentDirectory = new File("./");

        if(currentDirectory.exists())
            fileChooser.setCurrentDirectory(currentDirectory);

        FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT", "txt");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);

        if (result != JFileChooser.CANCEL_OPTION) {

            File file = fileChooser.getSelectedFile();

            if(file.exists())
                path = file.getAbsolutePath();
        }

        return path;
    }

}
